package de.rwth.i2.attestor.its;

import java.util.Objects;

public class ITSConjunction implements ITSFormula {
    private final ITSFormula lhs;
    private final ITSFormula rhs;
    public ITSConjunction(ITSFormula lhs, ITSFormula rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    @Override
    public String toString() {
        return String.format("%s && %s", lhs, rhs);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ITSConjunction that = (ITSConjunction) o;
        return lhs.equals(that.lhs) &&
                rhs.equals(that.rhs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lhs, rhs);
    }
}
