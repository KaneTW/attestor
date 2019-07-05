package de.rwth.i2.attestor.its;

public class CompExpr {
    public IntExpr lhs;
    public IntExpr rhs;
    public CompOp operator;

    @Override
    public String toString() {
        return String.format("%s %s %s", lhs, operator, rhs)
    }
}
