package de.rwth.i2.attestor.its;

public class ITSBinaryTerm implements ITSTerm {
    private final ITSTerm lhs;
    private final ITSTerm rhs;
    private final IntOp operator;

    public ITSTerm getLhs() {
        return lhs;
    }

    public ITSTerm getRhs() {
        return rhs;
    }

    public IntOp getOperator() {
        return operator;
    }

    public ITSBinaryTerm(ITSTerm lhs, ITSTerm rhs, IntOp operator) {
        this.lhs = lhs;
        this.rhs = rhs;
        this.operator = operator;
    }

    @Override
    public String toString() {
        return String.format("(%s) %s (%s)", lhs, operator, rhs);
    }
}
