package de.rwth.i2.attestor.its;

import de.rwth.i2.attestor.semantics.jimpleSemantics.jimple.values.Value;

public class BinaryIntExpr implements IntExpr {
    private final IntExpr lhs;
    private final IntExpr rhs;
    private final IntOp operator;

    public IntExpr getLhs() {
        return lhs;
    }

    public IntExpr getRhs() {
        return rhs;
    }

    public IntOp getOperator() {
        return operator;
    }

    public BinaryIntExpr(IntExpr lhs, IntOp operator, IntExpr rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
        this.operator = operator;
    }

    @Override
    public String toString() {
        return String.format("(%s) %s (%s)", lhs, operator, rhs);
    }
}
