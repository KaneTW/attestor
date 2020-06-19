package de.rwth.i2.attestor.its;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

// not sure if this actually is syntactically an ITSTerm but w/e
public class ITSCompareFormula implements ITSFormula {
    private final ITSTerm lhs;
    private final ITSTerm rhs;
    private final CompOp op;

    public ITSCompareFormula(ITSTerm lhs, ITSTerm rhs, CompOp op) {
        this.lhs = lhs;
        this.rhs = rhs;
        this.op = op;
    }

    @Override
    public String toString() {
        return String.format("%s %s %s", lhs, op, rhs);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ITSCompareFormula that = (ITSCompareFormula) o;
        return lhs.equals(that.lhs) &&
                rhs.equals(that.rhs) &&
                op == that.op;
    }

    @Override
    public int hashCode() {
        return Objects.hash(lhs, rhs, op);
    }

    @Override
    public Set<ITSVariable> occurringVariables() {
        HashSet<ITSVariable> out = new HashSet<>();
        out.addAll(lhs.occurringVariables());
        out.addAll(rhs.occurringVariables());
        return out;
    }
}
