package de.rwth.i2.attestor.its;

import de.rwth.i2.attestor.semantics.jimpleSemantics.jimple.values.SettableValue;

import java.util.Objects;

public class ITSOldVariable implements ITSTerm {
    private final ITSVariable var;
    private final String formatted;

    public ITSOldVariable(ITSVariable var) {
        this.var = var;

        this.formatted = formatVar(var);
    }

    public ITSVariable getVar() {
        return var;
    }


    private static String formatVar(ITSVariable var) {
        return var.toString() + "_old";
    }


    @Override
    public String toString() {
        return formatted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ITSOldVariable that = (ITSOldVariable) o;
        return formatted.equals(that.formatted);
    }

    @Override
    public int hashCode() {
        return Objects.hash(formatted);
    }
}
