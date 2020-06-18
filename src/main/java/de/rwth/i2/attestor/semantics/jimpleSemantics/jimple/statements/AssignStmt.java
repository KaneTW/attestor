package de.rwth.i2.attestor.semantics.jimpleSemantics.jimple.statements;


import de.rwth.i2.attestor.grammar.materialization.util.ViolationPoints;
import de.rwth.i2.attestor.graph.SelectorLabel;
import de.rwth.i2.attestor.graph.heap.HeapConfiguration;
import de.rwth.i2.attestor.its.*;
import de.rwth.i2.attestor.main.scene.SceneObject;
import de.rwth.i2.attestor.semantics.jimpleSemantics.jimple.values.*;
import de.rwth.i2.attestor.semantics.util.DeadVariableEliminator;
import de.rwth.i2.attestor.stateSpaceGeneration.ProgramState;
import de.rwth.i2.attestor.types.Types;
import de.rwth.i2.attestor.util.SingleElementUtil;
import fj.test.Gen;
import gnu.trove.list.array.TIntArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * AssignStmts model assignments of locals or fields to values e.g. x.y = z
 *
 * @author hannah
 */
public class AssignStmt extends Statement {

    private static final Logger logger = LogManager.getLogger("AssignStmt");
    /**
     * the element to which something will be assigned (e.g. variable or field)
     */
    private final SettableValue lhs;
    /**
     * The expression that will be assigned
     */
    private final Value rhs;
    /**
     * the program counter of the successor state
     */
    private final int nextPC;

    private final ViolationPoints potentialViolationPoints;

    private final Set<String> liveVariableNames;

    public AssignStmt(SceneObject sceneObject, SettableValue lhs, Value rhs, int nextPC, Set<String> liveVariableNames) {

        super(sceneObject);
        this.rhs = rhs;
        this.lhs = lhs;
        this.nextPC = nextPC;
        this.liveVariableNames = liveVariableNames;

        potentialViolationPoints = new ViolationPoints();
        potentialViolationPoints.addAll(lhs.getPotentialViolationPoints());
        potentialViolationPoints.addAll(rhs.getPotentialViolationPoints());


    }

    /**
     * evaluates the rhs and assigns it to the left hand side. In case the rhs
     * evaluates to undefined, the variable will be removed from the heap (It
     * will not point to its old value). <br>
     * If the types of the lhs and the rhs do not match, there will be a
     * warning, but the assignment will still be realized.<br>
     * <p>
     * If the variable in rhs is not live in this statement, it will be removed from the heap
     * to enable abstraction at this point.
     */
    @Override
    public Collection<ProgramState> computeSuccessors(ProgramState programState) {

        programState = programState.clone();
        ConcreteValue concreteRHS;

        try {
            concreteRHS = rhs.evaluateOn(programState);
        } catch (NullPointerDereferenceException e) {
            logger.error(e.getErrorMessage(this));
            concreteRHS = programState.getUndefined();
        }

        try {
            lhs.evaluateOn(programState); // enforce materialization if necessary
            lhs.setValue(programState, concreteRHS);
        } catch (NullPointerDereferenceException e) {
            logger.error(e.getErrorMessage(this));
        }

        if (scene().options().isRemoveDeadVariables()) {
            DeadVariableEliminator.removeDeadVariables(this, rhs.toString(),
                    programState, liveVariableNames);

            DeadVariableEliminator.removeDeadVariables(this, lhs.toString(),
                    programState, liveVariableNames);
        }

        ProgramState result = programState.clone();
        result.setProgramCounter(nextPC);

        return SingleElementUtil.createSet(result);
    }

    public String toString() {

        return lhs.toString() + " = " + rhs.toString() + ";";
    }

    @Override
    public ViolationPoints getPotentialViolationPoints() {

        return potentialViolationPoints;
    }

    @Override
    public Set<Integer> getSuccessorPCs() {

        return SingleElementUtil.createSet(nextPC);
    }

    @Override
    public boolean needsCanonicalization() {
        return false;
    }

    private int getNodeFromConcreteValue(ConcreteValue value) {
        if (value instanceof GeneralConcreteValue) {
            GeneralConcreteValue gv = (GeneralConcreteValue) value;
            return gv.getNode();
        }
        return HeapConfiguration.INVALID_ELEMENT;
    }


    @Override
    public Collection<Action> computeITSActions(ProgramState previous, ProgramState next, T2Invoker invoker) {
        ITSTerm rhsTerm = rhs.asITSTerm();
        List<Action> actions = new LinkedList<>();


        if (rhs instanceof NewExpr) {
            actions.add(new AssumeAction(new ITSCompareFormula(lhs.asITSTerm(), new ITSLiteral(0), CompOp.Greater)));
        } else if (rhs instanceof LengthExpr) {
            // array lengths are >= 0
            actions.add(new AssumeAction(new ITSCompareFormula(lhs.asITSTerm(), new ITSLiteral(0), CompOp.GreaterEqual)));
        }


        // object case
        if (!lhs.getType().isPrimitiveType()) {
            // x.f = y
            if (lhs instanceof Field) {
                Field field = (Field)lhs;
                Local local = (Local)field.getOriginValue();

                ITSVariable localVar = new ITSVariable(local);
                ITSVariable fieldVar = new ITSVariable(field);

                actions.add(new AssignAction(fieldVar, new ITSNondetTerm(field.getType())));

                actions.add(new AssumeAction(new ITSCompareFormula(fieldVar, new ITSLiteral(0), CompOp.GreaterEqual)));
                actions.add(new AssumeAction(new ITSCompareFormula(fieldVar, localVar, CompOp.Less)));

                ITSTerm y = extractConcreteValue(next, rhs, true);

                actions.add(new AssignAction(localVar, new ITSBinaryTerm(
                        new ITSBinaryTerm(localVar, y, IntOp.PLUS),
                        fieldVar, IntOp.MINUS)));

            } else if (rhs instanceof Field) { // x = y.f
                Field field = (Field)rhs;
                Local local = (Local)field.getOriginValue();
                actions.add(new AssignAction(lhs, new ITSNondetTerm(lhs.getType())));

                actions.add(new AssumeAction(new ITSCompareFormula(lhs.asITSTerm(), new ITSLiteral(0), CompOp.GreaterEqual)));
                actions.add(new AssumeAction(new ITSCompareFormula(lhs.asITSTerm(), local.asITSTerm(), CompOp.Less)));
            } else { // x = y
                actions.add(new AssignAction(lhs, rhs.asITSTerm()));
            }
        } else {
            actions.add(new AssignAction(lhs, extractConcreteValue(next, rhs, false)));
        }

        return actions;
    }

    private ITSTerm extractConcreteValue(ProgramState next, Value value, boolean isSize) {
        if (!(value instanceof Local || value instanceof Field)) {
            return value.asITSTerm();
        }

        ITSTerm rhsTerm = new ITSNondetTerm(value.getType());

        try {
            GeneralConcreteValue concreteValue = (GeneralConcreteValue) value.evaluateOn(next);
            // NULL and int 0 all map to 0
            if (concreteValue.type().equals(Types.NULL) || concreteValue.type().equals(Types.INT_0)) {
                rhsTerm = new ITSLiteral(0);
            } else if (concreteValue.type().equals(Types.INT_PLUS_1)) {
                rhsTerm = new ITSLiteral(1);
            } else if (concreteValue.type().equals(Types.INT_MINUS_1)) {
                if (isSize) {
                    // use absolute value when signed
                    rhsTerm = new ITSLiteral(1);
                } else {
                    rhsTerm = new ITSLiteral(-1);
                }
            } else if (value instanceof Local) {
                // if we're not on the heap and a primitive type, use the variable
                return value.asITSTerm();
            } else {
                System.out.println("Couldn't extract concrete value from " + value);
            }

        } catch (NullPointerDereferenceException ex) {
            // blank
        }
        return rhsTerm;
    }
}
