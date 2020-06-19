package de.rwth.i2.attestor.its;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ITSLiteral that = (ITSLiteral) o;
        return literal == that.literal;
    }

    @Override
    public int hashCode() {
        return Objects.hash(literal);
    }

    @Override
    public Set<ITSVariable> occurringVariables() {
        return Collections.emptySet();
    }
}
