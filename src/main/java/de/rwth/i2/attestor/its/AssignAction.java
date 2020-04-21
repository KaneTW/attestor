package de.rwth.i2.attestor.its;

import de.rwth.i2.attestor.semantics.jimpleSemantics.jimple.values.SettableValue;

import java.util.Objects;

public class AssignAction implements Action {
    private final ITSTerm lhs;
    private final ITSTerm rhs;

    public ITSTerm getLhs() {
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

    public AssignAction(ITSObjectVariable lhs, ITSTerm rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    @Override
    public String toString() {
        return String.format("%s := %s;", lhs, rhs);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AssignAction that = (AssignAction) o;
        return lhs.equals(that.lhs) &&
                rhs.equals(that.rhs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lhs, rhs);
    }
}
