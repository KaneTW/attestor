package de.rwth.i2.attestor.its;

import de.rwth.i2.attestor.types.Type;

public class ITSNondetTerm implements ITSTerm {
    private final Type type;

    public ITSNondetTerm(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        return "nondet()";
    }
}
