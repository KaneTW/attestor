package de.rwth.i2.attestor.its;

import de.rwth.i2.attestor.types.Type;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ITSNondetTerm that = (ITSNondetTerm) o;
        return Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }
}
