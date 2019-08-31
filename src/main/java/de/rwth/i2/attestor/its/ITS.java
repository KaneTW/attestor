package de.rwth.i2.attestor.its;

import de.rwth.i2.attestor.semantics.jimpleSemantics.jimple.statements.Statement;
import de.rwth.i2.attestor.stateSpaceGeneration.*;
import de.rwth.i2.attestor.util.Pair;

import java.util.*;

public class ITS {

    private static final int INITIAL_STATE = 0;

    private StateSpace stateSpace;
    private Program program;

    private Set<Transition> transitions = new LinkedHashSet<>();


    public ITS(StateSpace stateSpace, Program program) {
        this.stateSpace = stateSpace;
        this.program = program;

        Set<ProgramState> initial = stateSpace.getInitialStates();

        for(ProgramState ps : initial) {
            Transition t = new Transition(INITIAL_STATE, getStateId(ps), Collections.emptySet());

            // add an initial state
            transitions.add(t);

            // fill the rest
            fillITS(t, ps);
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

    private void fillITS(Transition t, ProgramState ps) {
        SemanticsCommand cmd = program.getStatement(ps.getProgramCounter());

        if (cmd instanceof Statement) {
            Statement stmt = (Statement) cmd;
            for (Pair<Collection<Action>, ProgramState> pair : stmt.computeITSActions(ps)) {
                ProgramState succ = pair.second();
                Transition candidate = new Transition(t.getTo(), getStateId(succ), pair.first());

                if (transitions.contains(candidate)) {
                    // avoid cyclic loops
                    return;
                }

                transitions.add(candidate);

                fillITS(candidate, succ);

            }
        }
        else {
            throw new UnsupportedOperationException("Can't handle non-Statements");
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
