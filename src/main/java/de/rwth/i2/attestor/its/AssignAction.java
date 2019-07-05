package de.rwth.i2.attestor.its;

import de.rwth.i2.attestor.semantics.jimpleSemantics.jimple.values.SettableValue;
import de.rwth.i2.attestor.semantics.jimpleSemantics.jimple.values.Value;

public class AssignAction implements Action {
    private final ITSVar lhs;
    private final IntExpr rhs;

    public ITSVar getLhs() {
        return lhs;
    }

    public IntExpr getRhs() {
        return rhs;
    }

    public AssignAction(SettableValue lhs, IntExpr rhs) {
        this.lhs = new ITSVar(lhs);
        this.rhs = rhs;
    }

    public AssignAction(ITSVar lhs, IntExpr rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    @Override
    public String toString() {
        return String.format("%s := %s;", lhs, rhs);
    }
}
