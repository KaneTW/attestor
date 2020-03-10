package de.rwth.i2.attestor.its.certificate;

import java.util.HashSet;
import java.util.Set;

public class Invariant {
    private final RankingFunction sourceFunction;
    private final RankingFunction targetFunction;
    private final boolean strict;

    /**
     * An invariant asserting that source > target if strict, source >= target otherwise
     */
    public Invariant(RankingFunction source, RankingFunction target, boolean strict) {
        this.sourceFunction = source;
        this.targetFunction = target;
        this.strict = strict;
    }

    public RankingFunction getSourceFunction() {
        return sourceFunction;
    }

    public RankingFunction getTargetFunction() {
        return targetFunction;
    }

    public boolean isStrict() {
        return strict;
    }

    @Override
    public String toString() {
        String op = strict ? ">" : ">=";
        return String.format("(%s -> %s) %s %s %s", sourceFunction.getLocation().getLocation(), targetFunction.getLocation().getLocation(), sourceFunction.getExpression(), op, targetFunction.getExpression());
    }

    /**
     * Return all variables occurring in the invariant
     */
    public Set<String> getOccurringVariables() {
        HashSet<String> out = new HashSet<>();
        out.addAll(sourceFunction.getExpression().getOccurringVariables());
        out.addAll(targetFunction.getExpression().getOccurringVariables());
        return out;
    }

}
