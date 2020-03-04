package de.rwth.i2.attestor.its;

import de.rwth.i2.attestor.semantics.jimpleSemantics.jimple.values.SettableValue;

import java.util.Objects;

public class ITSVariable implements ITSTerm {
    private final SettableValue var;
    private final String formatted;

    public ITSVariable(SettableValue var) {
        this.var = var;

        this.formatted = formatVar(var);
    }

    public SettableValue getVar() {
        return var;
    }


    private static String formatVar(SettableValue var) {
        return var.toString().replace("$", "__");
    }


    @Override
    public String toString() {
        return formatted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ITSVariable that = (ITSVariable) o;
        return formatted.equals(that.formatted);
    }

    @Override
    public int hashCode() {
        return Objects.hash(formatted);
    }
}
