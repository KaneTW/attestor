package de.rwth.i2.attestor.its;

import de.rwth.i2.attestor.stateSpaceGeneration.ProgramState;
import de.rwth.i2.attestor.stateSpaceGeneration.State;
import de.rwth.i2.attestor.stateSpaceGeneration.StateSpace;

import java.util.*;

public class ITS {

    private static final int INITIAL_STATE = 0;

    private StateSpace stateSpace;

    private Set<Transition> transitions = new LinkedHashSet<>();


    public ITS(StateSpace stateSpace) {
        this.stateSpace = stateSpace;

        Set<ProgramState> initial = stateSpace.getInitialStates();

        for(ProgramState ps : initial) {
            Transition t = new Transition(INITIAL_STATE, getStateId(ps), ps);

            // add an initial state
            transitions.add(t);

            // fill the rest
            fillITS(t);
        }

    }

    public Set<Transition> getTransitions() {
        return transitions;
    }

    // make sure state 0 is free
    private int getStateId(ProgramState ps) {
        int id = ps.getStateSpaceId();
        if (id < 0) {
            throw new IllegalArgumentException("Attempted to ITS convert an invalid program state");
        }

        return id + 1;
    }

    private void fillITS(Transition t) {
        Set<ProgramState> succs = stateSpace.getControlFlowSuccessorsOf(t.ps);

        for (ProgramState succ : succs) {
            Transition candidate = new Transition(t.to, getStateId(succ), succ);

            if (transitions.contains(candidate)) {
                // avoid cyclic loops
                return;
            }

            transitions.add(candidate);

            fillITS(candidate);

        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("START: %d;\n\n", INITIAL_STATE));

        for (Transition t : transitions) {
            sb.append(t).append("\n\n");
        }

        return sb.toString();
    }
}
