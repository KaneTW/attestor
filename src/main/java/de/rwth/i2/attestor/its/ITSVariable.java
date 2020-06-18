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
        String out = var.toString();
        if (out.startsWith("obj_")) {
            out = out.replace("obj_", "_obj_");
        }

        return out.replace("$", "__");
    }

    // returns null if it's not a ITSVariable
    public static String fromFormatted(String formatted) {
        if (formatted.startsWith("obj_")) {
            return null;
        }

        if (formatted.matches("^__snapshot_[0-9]+_.*")) {
            formatted = formatted.replaceFirst("^__snapshot_[0-9]+_", "");
        }


        return formatted.replace("__", "$").replace("_obj_", "obj_").replace("_old_", "_old");
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
