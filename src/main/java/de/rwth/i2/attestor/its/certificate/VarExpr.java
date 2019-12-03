package de.rwth.i2.attestor.its.certificate;

public class VarExpr implements Expression {
    private final String variable;

    public VarExpr(String variable) {
        this.variable = variable;
    }

    @Override
    public String toString() {
        return variable;
    }
}
