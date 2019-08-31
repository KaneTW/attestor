package de.rwth.i2.attestor.semantics.jimpleSemantics.jimple.statements;


import de.rwth.i2.attestor.its.Action;
import de.rwth.i2.attestor.main.scene.SceneObject;
import de.rwth.i2.attestor.stateSpaceGeneration.ProgramState;
import de.rwth.i2.attestor.stateSpaceGeneration.SemanticsCommand;
import de.rwth.i2.attestor.util.Pair;

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
     * Given a program state, computes the effects of a single step of the abstract program semantics onto an ITS transition.
     * @return All transitions that can occur from executing the semantics on this state
     */
    public Collection<Pair<Collection<Action>, ProgramState>> computeITSActions(ProgramState programState) {
        Collection<ProgramState> succs = computeSuccessors(programState);

        List<Pair<Collection<Action>, ProgramState>> out = new LinkedList<>();

        for (ProgramState succ : succs) {
            out.add(new Pair<>(Collections.emptySet(), succ));
        }

        return out;
    }

}
