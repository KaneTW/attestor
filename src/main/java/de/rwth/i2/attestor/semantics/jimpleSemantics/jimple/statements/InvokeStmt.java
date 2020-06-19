package de.rwth.i2.attestor.semantics.jimpleSemantics.jimple.statements;

import de.rwth.i2.attestor.grammar.materialization.util.ViolationPoints;
import de.rwth.i2.attestor.its.*;
import de.rwth.i2.attestor.main.scene.SceneObject;
import de.rwth.i2.attestor.procedures.Method;
import de.rwth.i2.attestor.semantics.jimpleSemantics.jimple.statements.invoke.InvokeCleanup;
import de.rwth.i2.attestor.semantics.jimpleSemantics.jimple.statements.invoke.InvokeHelper;
import de.rwth.i2.attestor.semantics.jimpleSemantics.jimple.values.SettableValue;
import de.rwth.i2.attestor.semantics.jimpleSemantics.jimple.values.Value;
import de.rwth.i2.attestor.stateSpaceGeneration.ProgramState;
import de.rwth.i2.attestor.util.Pair;
import de.rwth.i2.attestor.util.SingleElementUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * InvokeStmt models statements like foo(); or bar(1,2);
 *
 * @author Hannah Arndt
 */
public class InvokeStmt extends Statement implements InvokeCleanup {

    /**
     * the abstract representation of the called method
     */
    private final Method method;
    /**
     * handles arguments, and if applicable the this-reference.
     */
    private final InvokeHelper invokePrepare;
    /**
     * the program location of the successor state
     */
    private final int nextPC;

    public InvokeStmt(SceneObject sceneObject, Method method, InvokeHelper invokePrepare, int nextPC) {

        super(sceneObject);
        this.method = method;
        this.invokePrepare = invokePrepare;
        this.nextPC = nextPC;
    }

    /**
     * gets the fixpoint from the method
     * for the input heap and returns it for the successor
     * location.<br>
     * <p>
     * If any variable appearing in the arguments is not live at this point,
     * it will be removed from the heap to enable abstraction.
     */
    @Override
    public Collection<ProgramState> computeSuccessors(ProgramState programState) {

        ProgramState preparedState = programState.clone();
        invokePrepare.prepareHeap(preparedState);

        Collection<ProgramState> methodResult = method
                .getMethodExecutor()
                .getResultStates(programState, preparedState);

        methodResult.forEach(invokePrepare::cleanHeap);
        methodResult.forEach(ProgramState::clone);
        methodResult.forEach(x -> x.setProgramCounter(nextPC));

        return methodResult;
    }

    public ProgramState getCleanedResultState(ProgramState state) {

        invokePrepare.cleanHeap(state);
        return state;
    }

    public boolean needsMaterialization(ProgramState programState) {

        return invokePrepare.needsMaterialization(programState);
    }


    public String toString() {

        return invokePrepare.baseValueString() + method.toString() + "(" + invokePrepare.argumentString() + ");";
    }

    @Override
    public ViolationPoints getPotentialViolationPoints() {

        return invokePrepare.getPotentialViolationPoints();
    }

    @Override
    public Set<Integer> getSuccessorPCs() {

        return SingleElementUtil.createSet(nextPC);
    }

    @Override
    public boolean needsCanonicalization() {
        return true;
    }


    @Override
    public Collection<Action> computeITSActions(ProgramState current, ProgramState next, T2Invoker invoker) {
        List<Value> affectedValues = new ArrayList<>();

        affectedValues.addAll(invokePrepare.getArgumentValues().stream().filter(value -> !value.getType().isPrimitiveType()).collect(Collectors.toList()));

        List<Action> actions = new ArrayList<>();

        for (Value value : affectedValues) {
            if (!(value instanceof SettableValue)) continue; // shouldn't happen but w/e
            actions.add(new AssignAction((SettableValue) value, new ITSNondetTerm(value.getType())));
            if (!value.getType().isPrimitiveType()) { // only potentially true for lhs but w/e
                actions.add(new AssumeAction(new ITSCompareFormula(value.asITSTerm(), new ITSLiteral(0), CompOp.GreaterEqual)));
            }
        }
        System.out.println("InvokeStmt: all affected variables are indeterminate");
        return  actions;
    }

}
