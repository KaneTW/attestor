package de.rwth.i2.attestor.its;

public class ITSNegation implements ITSFormula {
    private final ITSFormula rhs;

    public ITSNegation(ITSFormula rhs) {
        this.rhs = rhs;
    }

    @Override
    public String toString() {
        return String.format("!(%s)", rhs);
    }
}
