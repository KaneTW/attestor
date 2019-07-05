package de.rwth.i2.attestor.its;


import de.rwth.i2.attestor.stateSpaceGeneration.ProgramState;

import java.util.Objects;

public class Transition {

    private final int from;
    private final int to;
    private final Action ps;

    public Transition(int from, int to, Action ps) {
        this.from = from;
        this.to = to;
        this.ps = ps;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Transition) {
            Transition t = (Transition) o;
            return t.from == from && t.to == to && ps.equals(t.ps);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, ps);
    }

    @Override
    public String toString() {
        return String.format("FROM: %d;\n%s\nTO: %d;\n", from, getConditions(), to);
    }

    private String getConditions() {
        return "";
    }
}
