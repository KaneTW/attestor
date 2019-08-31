package de.rwth.i2.attestor.semantics.jimpleSemantics.jimple.values;

import de.rwth.i2.attestor.grammar.materialization.util.ViolationPoints;
import de.rwth.i2.attestor.its.ITSBinaryTerm;
import de.rwth.i2.attestor.its.ITSNondetTerm;
import de.rwth.i2.attestor.its.ITSTerm;
import de.rwth.i2.attestor.its.IntOp;
import de.rwth.i2.attestor.stateSpaceGeneration.ProgramState;
import de.rwth.i2.attestor.types.Type;
import de.rwth.i2.attestor.types.Types;

public class ArithExpr implements Value {
    private final Value leftExpr;
    private final Value rightExpr;
    private final IntOp op;
    private final Type type;

    private final ViolationPoints potentialViolationPoints;

    private final ITSTerm term;

    public ArithExpr(Value leftExpr, Value rightExpr, IntOp op, Type type) {
        this.leftExpr = leftExpr;
        this.rightExpr = rightExpr;
        this.op = op;


        this.potentialViolationPoints = new ViolationPoints();
        this.potentialViolationPoints.addAll(leftExpr.getPotentialViolationPoints());
        this.potentialViolationPoints.addAll(rightExpr.getPotentialViolationPoints());

        this.type = type;

        this.term = new ITSBinaryTerm(leftExpr.asITSTerm(), rightExpr.asITSTerm(), op);
    }

    @Override
    public ConcreteValue evaluateOn(ProgramState programState) {
        return programState.getUndefined();
    }

    @Override
    public boolean needsMaterialization(ProgramState programState) {
        return rightExpr.needsMaterialization(programState) || leftExpr.needsMaterialization(programState);
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public ViolationPoints getPotentialViolationPoints() {
        return potentialViolationPoints;
    }

    @Override
    public ITSTerm asITSTerm() {
        return term;
    }

}
