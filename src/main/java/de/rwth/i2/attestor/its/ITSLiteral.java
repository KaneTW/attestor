package de.rwth.i2.attestor.its;

public class ITSLiteral implements ITSTerm {
    private final int literal;

    public int getLiteral() {
        return literal;
    }

    public ITSLiteral(int literal) {
        this.literal = literal;
    }

    @Override
    public String toString() {
        return Integer.toString(literal);
    }
}
