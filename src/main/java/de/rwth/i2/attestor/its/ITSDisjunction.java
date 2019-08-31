package de.rwth.i2.attestor.its;

public class ITSDisjunction implements ITSFormula {
    private final ITSFormula lhs;
    private final ITSFormula rhs;
    public ITSDisjunction(ITSFormula lhs, ITSFormula rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    @Override
    public String toString() {
        return String.format("%s || %s", lhs, rhs);
    }
}
