package de.rwth.i2.attestor.semantics.jimpleSemantics.jimple.values.boolExpr;

import de.rwth.i2.attestor.grammar.materialization.util.ViolationPoints;
import de.rwth.i2.attestor.its.*;
import de.rwth.i2.attestor.semantics.jimpleSemantics.jimple.values.ConcreteValue;
import de.rwth.i2.attestor.semantics.jimpleSemantics.jimple.values.Value;
import de.rwth.i2.attestor.stateSpaceGeneration.ProgramState;
import de.rwth.i2.attestor.types.Type;
import de.rwth.i2.attestor.types.Types;

public class NotExpr implements BoolValue {
    private final BoolValue expr;

    private final ViolationPoints potentialViolationPoints;

    private final ITSNegation formula;

    public NotExpr(BoolValue expr) {
        this.expr = expr;

        this.potentialViolationPoints = expr.getPotentialViolationPoints();


        this.formula = new ITSNegation(expr.asITSFormula());
    }

    @Override
    public ConcreteValue evaluateOn(ProgramState programState) {
        return programState.getUndefined();
    }

    @Override
    public boolean needsMaterialization(ProgramState programState) {
        return expr.needsMaterialization(programState);
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
