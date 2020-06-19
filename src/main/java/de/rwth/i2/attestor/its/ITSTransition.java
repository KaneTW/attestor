package de.rwth.i2.attestor.its;


import com.google.common.collect.HashMultiset;
import de.rwth.i2.attestor.stateSpaceGeneration.Program;
import de.rwth.i2.attestor.stateSpaceGeneration.ProgramState;

import java.util.Collection;
import java.util.Objects;

public class ITSTransition {

    private final ProgramState from;
    private final ProgramState to;
    private final Collection<Action> actions;

    public int getFrom() {
        return asPC(from);
    }

    public int getTo() {
        return asPC(to);
    }

    public ProgramState getFromState() { return from; }
    public ProgramState getToState() { return to; }

    public Collection<Action> getActions() {
        return actions;
    }

    private int asPC(ProgramState ps) {
        if (ps == null) return 0;
        int id = ps.getProgramCounter();
        if (id + 2 < 0) {
            throw new IllegalArgumentException("Attempted to ITS convert an invalid program state: " + ps.getProgramCounter());
        }

        return id + 2;
    }


    public ITSTransition(ProgramState from, ProgramState to, Collection<Action> actions) {
        this.from = from;
        this.to = to;
        this.actions = actions;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ITSTransition) {
            ITSTransition t = (ITSTransition) o;
            return (Objects.equals(t.from, from)) && t.to.equals(to) && HashMultiset.create(actions).equals(HashMultiset.create(t.actions));
        }

        return false;
    }

    public boolean equalsOutput(Object o) {
        if (o instanceof ITSTransition) {
            ITSTransition t = (ITSTransition) o;
            return getFrom() == t.getFrom() && getTo() == t.getTo() && HashMultiset.create(actions).equals(HashMultiset.create(t.actions));
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, actions);
    }

    @Override
    public String toString() {
        return String.format("FROM: %d;\n%s\nTO: %d;\n", asPC(from), getConditions(), asPC(to));
    }

    private String getConditions() {
        StringBuilder builder = new StringBuilder();
        actions.forEach(action -> builder.append(action.toString() + "\n"));
        return builder.toString();
    }
}
