package de.rwth.i2.attestor.its;

public class AssumeAction implements Action {
    private final CompExpr expr;

    public AssumeAction(CompExpr expr) {
        this.expr = expr;
    }

    public CompExpr getExpr() {
        return expr;
    }

    @Override
    public String toString() {
        return String.format("assume(%s);", expr);
    }
}
