package de.rwth.i2.attestor.semantics.jimpleSemantics.jimple.values.boolExpr;

import de.rwth.i2.attestor.its.ITSFormula;
import de.rwth.i2.attestor.semantics.jimpleSemantics.jimple.values.Value;

public interface BoolValue extends Value {
    ITSFormula asITSFormula();
}
