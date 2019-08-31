package de.rwth.i2.attestor.its;

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
}
