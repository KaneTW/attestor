package de.rwth.i2.attestor.its;

import de.rwth.i2.attestor.semantics.TerminalStatement;
import de.rwth.i2.attestor.semantics.jimpleSemantics.jimple.statements.Statement;
import de.rwth.i2.attestor.stateSpaceGeneration.*;
import de.rwth.i2.attestor.util.Pair;

import java.io.IOException;
import java.util.*;

public class ITS {

    private static final int INITIAL_STATE = 0;

    private StateSpace stateSpace;
    private Program program;
    private T2Invoker invoker;
    private T2Result result;



    private Set<Transition> transitions = new LinkedHashSet<>();


    public ITS(StateSpace stateSpace, Program program, T2Invoker invoker) {
        this.stateSpace = stateSpace;
        this.program = program;
        this.invoker = invoker;

        Set<ProgramState> initial = stateSpace.getInitialStates();

        for(ProgramState ps : initial) {
            Transition t = new Transition(INITIAL_STATE, getStateId(ps), Collections.emptySet());

            // add an initial state
            transitions.add(t);

            // fill the rest
            fillITS(t, ps);

            try {
                result = invoker.checkTermination(this);
            } catch (IOException ex) {
                result = null;
                throw new RuntimeException(ex);
            }
        }
    }

    public Set<Transition> getTransitions() {
        return transitions;
    }

    // make sure state 0 is free
    private int getStateId(ProgramState ps) {
        int id = ps.getStateSpaceId();
        if (id < 0) {
            throw new IllegalArgumentException("Attempted to ITS convert an invalid program state: " + ps.getStateSpaceId());
        }

        return id + 1;
    }

    private void fillITS(Transition t, ProgramState ps) {
        SemanticsCommand cmd = program.getStatement(ps.getProgramCounter());
        if (cmd instanceof Statement) {
            Statement stmt = (Statement) cmd;
            HashSet<ProgramState> succs = new HashSet<>(stateSpace.getControlFlowSuccessorsOf(ps));

//            succs.addAll(stateSpace.getMaterializationSuccessorsOf(ps));

            for (ProgramState succ : succs) {
                Collection<Action> actions = stmt.computeITSActions(ps, succ, invoker);

                Transition candidate = new Transition(t.getTo(), getStateId(succ), actions);

                if (transitions.contains(candidate)) {
                    // avoid cyclic loops
                    return;
                }

                transitions.add(candidate);

                fillITS(candidate, succ);

                }
        } else if (cmd instanceof TerminalStatement) {
            // blank
        } else {
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

    public T2Result getResult() {
        return result;
    }

    public void setResult(T2Result result) {
        this.result = result;
    }

}
