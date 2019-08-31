package de.rwth.i2.attestor.its;

import de.rwth.i2.attestor.semantics.jimpleSemantics.jimple.values.SettableValue;

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
        return var.toString().replaceAll("$", "___");
    }


    @Override
    public String toString() {
        return formatted;
    }
}
