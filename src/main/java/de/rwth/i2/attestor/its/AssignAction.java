package de.rwth.i2.attestor.its;

import de.rwth.i2.attestor.semantics.jimpleSemantics.jimple.values.SettableValue;

public class AssignAction implements Action {
    private final ITSVariable lhs;
    private final ITSTerm rhs;

    public ITSVariable getLhs() {
        return lhs;
    }

    public ITSTerm getRhs() {
        return rhs;
    }

    public AssignAction(SettableValue lhs, ITSTerm rhs) {
        this.lhs = new ITSVariable(lhs);
        this.rhs = rhs;
    }

    public AssignAction(ITSVariable lhs, ITSTerm rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    @Override
    public String toString() {
        return String.format("%s := %s;", lhs, rhs);
    }
}
