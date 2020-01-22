package de.rwth.i2.attestor.semantics.jimpleSemantics.jimple.values;

import de.rwth.i2.attestor.grammar.materialization.util.ViolationPoints;
import de.rwth.i2.attestor.its.ITSTerm;
import de.rwth.i2.attestor.its.ITSVariable;
import de.rwth.i2.attestor.stateSpaceGeneration.ProgramState;
import de.rwth.i2.attestor.types.Type;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Array length expressions
 *
 * @author Hannah Arndt
 */
public class LengthExpr implements SettableValue {

    private static final Logger logger = LogManager.getLogger("Local");

    /**
     * the expected type
     */
    private final Type type;
    /**
     * the name of the array
     */
    private final Value expr;

    public LengthExpr(Type type, Value expr) {

        this.type = type;
        this.expr = expr;

    }

    public Value getExpr() {

        return this.expr;
    }

    public Type getType() {

        return this.type;
    }

    @Override
    public ConcreteValue evaluateOn(ProgramState programState) {

        // not represented
        return programState.getUndefined();
    }

    @Override
    public boolean needsMaterialization(ProgramState programState) {

        return false;
    }

    @Override
    public void setValue(ProgramState programState, ConcreteValue concreteRHS) {
        // do nothing
    }

    public String toString() {

        return this.expr + "__length";
    }

    @Override
    public ViolationPoints getPotentialViolationPoints() {

        return ViolationPoints.getEmptyViolationPoints();
    }

    @Override
    public ITSTerm asITSTerm() {
        return new ITSVariable(this);
    }
}
