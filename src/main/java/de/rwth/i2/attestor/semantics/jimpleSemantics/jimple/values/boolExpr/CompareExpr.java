package de.rwth.i2.attestor.semantics.jimpleSemantics.jimple.values.boolExpr;

import de.rwth.i2.attestor.grammar.materialization.util.ViolationPoints;
import de.rwth.i2.attestor.its.*;
import de.rwth.i2.attestor.semantics.jimpleSemantics.jimple.values.ConcreteValue;
import de.rwth.i2.attestor.semantics.jimpleSemantics.jimple.values.Value;
import de.rwth.i2.attestor.stateSpaceGeneration.ProgramState;
import de.rwth.i2.attestor.types.Type;
import de.rwth.i2.attestor.types.Types;

public class CompareExpr implements BoolValue {
    private final Value leftExpr;
    private final Value rightExpr;
    private final CompOp op;
    private final ViolationPoints potentialViolationPoints;

    private final ITSCompareFormula formula;

    public CompareExpr(Value leftExpr, Value rightExpr, CompOp op) {
        this.leftExpr = leftExpr;
        this.rightExpr = rightExpr;
        this.op = op;

        this.potentialViolationPoints = new ViolationPoints();
        this.potentialViolationPoints.addAll(leftExpr.getPotentialViolationPoints());
        this.potentialViolationPoints.addAll(rightExpr.getPotentialViolationPoints());

        this.formula = new ITSCompareFormula(leftExpr.asITSTerm(), rightExpr.asITSTerm(), op);
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
        return Types.BOOL;
    }

    @Override
    public ViolationPoints getPotentialViolationPoints() {
        return potentialViolationPoints;
    }

    @Override
    public ITSTerm asITSTerm() {
        return new ITSNondetTerm(getType());
    }

    @Override
    public ITSFormula asITSFormula() {
        return formula;
    }
}
