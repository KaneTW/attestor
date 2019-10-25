package de.rwth.i2.attestor.semantics.jimpleSemantics.jimple.statements;


import de.rwth.i2.attestor.its.Action;
import de.rwth.i2.attestor.its.T2Invoker;
import de.rwth.i2.attestor.main.scene.SceneObject;
import de.rwth.i2.attestor.stateSpaceGeneration.ProgramState;
import de.rwth.i2.attestor.stateSpaceGeneration.SemanticsCommand;

import java.util.*;

/**
 * Statements are {@link SemanticsCommand Sementics}
 * with {@link de.rwth.i2.attestor.stateSpaceGeneration.ProgramState ProgramState}
 * as heaps.
 *
 * @author Hannah Arndt
 */
public abstract class Statement extends SceneObject implements SemanticsCommand {

    protected Statement(SceneObject otherObject) {

        super(otherObject);
    }



    /**
     * Given a current program state and a target, computes the effects of a single step of the abstract program semantics onto an ITS transition.
     * @return A transition that can occur from executing the semantics on this state
     */
    // HACKHACK: can't re-run computeSuccessors, so gotta do this weird stuff
    public Collection<Action> computeITSActions(ProgramState current, ProgramState next, T2Invoker invoker) {
        return Collections.emptySet();
    }

}
