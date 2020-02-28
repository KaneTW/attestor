package de.rwth.i2.attestor.its;

import de.rwth.i2.attestor.its.certificate.ConstExpr;
import de.rwth.i2.attestor.its.certificate.Expression;
import de.rwth.i2.attestor.its.certificate.ProductExpr;
import de.rwth.i2.attestor.its.certificate.SumExpr;
import de.rwth.i2.attestor.semantics.TerminalStatement;
import de.rwth.i2.attestor.semantics.jimpleSemantics.jimple.statements.Statement;
import de.rwth.i2.attestor.stateSpaceGeneration.*;

import java.io.IOException;
import java.util.*;

public class ITS {

    private static final int INITIAL_STATE = 0;

    private StateSpace stateSpace;
    private Program program;
    private T2Invoker invoker;
    private T2Result result;



    private Set<ITSTransition> transitions = new LinkedHashSet<>();


    public ITS(StateSpace stateSpace, Program program, T2Invoker invoker) {
        this.stateSpace = stateSpace;
        this.program = program;
        this.invoker = invoker;

        Set<ProgramState> initial = stateSpace.getInitialStates();

        for(ProgramState ps : initial) {
            ITSTransition t = new ITSTransition(INITIAL_STATE, getStateId(ps), Collections.emptySet());

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

    public Set<ITSTransition> getTransitions() {
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

    private void fillITS(ITSTransition t, ProgramState ps) {
        SemanticsCommand cmd = program.getStatement(ps.getProgramCounter());
        if (cmd instanceof Statement) {
            Statement stmt = (Statement) cmd;
            HashSet<ProgramState> succs = new HashSet<>(stateSpace.getControlFlowSuccessorsOf(ps));

//            succs.addAll(stateSpace.getMaterializationSuccessorsOf(ps));

            for (ProgramState succ : succs) {
                Collection<Action> actions = stmt.computeITSActions(ps, succ, invoker);

                ITSTransition candidate = new ITSTransition(t.getTo(), getStateId(succ), actions);

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

        for (ITSTransition t : transitions) {
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


    private boolean isConstant(Expression expr) {
        if (expr instanceof ConstExpr) {
            return true;
        }

        boolean isConst = true;
        if (expr instanceof SumExpr) {
            SumExpr sum = (SumExpr) expr;
            for (Expression op :  sum.getOperands()) {
                if (!(op instanceof ConstExpr)) {
                    isConst = false;
                }
            }
        }

        if (expr instanceof ProductExpr) {
            ProductExpr product = (ProductExpr) expr;
            for (Expression op : product.getOperands()) {
                if (!(op instanceof ConstExpr)) {
                    isConst = false;
                }
            }
        }

        return isConst;
    }

}
