package de.rwth.i2.attestor.its;

public class LitIntExpr implements IntExpr {
    private final int literal;

    public int getLiteral() {
        return literal;
    }

    public LitIntExpr(int literal) {
        this.literal = literal;
    }

    @Override
    public String toString() {
        return Integer.toString(literal);
    }
}
