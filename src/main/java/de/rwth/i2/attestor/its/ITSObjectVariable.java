package de.rwth.i2.attestor.its;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

public class ITSObjectVariable implements ITSTerm {
    private final int node;
    private final String formatted;

    public ITSObjectVariable(int node) {
        this.node = node;

        this.formatted = formatVar(node);
    }

    public int getNode() {
        return node;
    }


    private static String formatVar(int node) {
        return "obj_" + node;
    }


    @Override
    public String toString() {
        return formatted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ITSObjectVariable that = (ITSObjectVariable) o;
        return formatted.equals(that.formatted);
    }

    @Override
    public int hashCode() {
        return Objects.hash(formatted);
    }

    @Override
    public Set<ITSVariable> occurringVariables() {
        return Collections.emptySet();
    }
}
