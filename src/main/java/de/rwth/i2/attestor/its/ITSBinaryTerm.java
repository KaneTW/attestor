package de.rwth.i2.attestor.its;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ITSBinaryTerm that = (ITSBinaryTerm) o;
        return lhs.equals(that.lhs) &&
                rhs.equals(that.rhs) &&
                operator == that.operator;
    }

    @Override
    public int hashCode() {
        return Objects.hash(lhs, rhs, operator);
    }
}
