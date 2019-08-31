package de.rwth.i2.attestor.its;

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
}
