package de.rwth.i2.attestor.its;

import java.util.Objects;
import java.util.Set;

public class AssumeAction implements Action {
    private final ITSFormula expr;

    public AssumeAction(ITSFormula expr) {
        this.expr = expr;
    }

    public ITSFormula getExpr() {
        return expr;
    }

    @Override
    public String toString() {
        return String.format("assume(%s);", expr);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AssumeAction that = (AssumeAction) o;
        return expr.equals(that.expr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(expr);
    }

    @Override
    public Set<ITSVariable> occurringVariables() {
        return expr.occurringVariables();
    }
}
