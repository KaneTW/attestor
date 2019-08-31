package de.rwth.i2.attestor.its;


import java.util.Collection;
import java.util.Objects;

public class Transition {

    private final int from;
    private final int to;
    private final Collection<Action> actions;

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }

    public Collection<Action> getActions() {
        return actions;
    }

    public Transition(int from, int to, Collection<Action> actions) {
        this.from = from;
        this.to = to;
        this.actions = actions;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Transition) {
            Transition t = (Transition) o;
            return t.from == from && t.to == to && actions.equals(t.actions);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, actions);
    }

    @Override
    public String toString() {
        return String.format("FROM: %d;\n%s\nTO: %d;\n", from, getConditions(), to);
    }

    private String getConditions() {
        StringBuilder builder = new StringBuilder();
        actions.forEach(action -> builder.append(action.toString() + "\n"));
        return builder.toString();
    }
}
