package de.rwth.i2.attestor.its;

import java.util.Objects;
import java.util.Set;

public class ITSNegation implements ITSFormula {
    private final ITSFormula rhs;

    public ITSNegation(ITSFormula rhs) {
        this.rhs = rhs;
    }

    @Override
    public String toString() {
        return String.format("!(%s)", rhs);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ITSNegation that = (ITSNegation) o;
        return rhs.equals(that.rhs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rhs);
    }

    @Override
    public Set<ITSVariable> occurringVariables() {
        return rhs.occurringVariables();
    }
}
