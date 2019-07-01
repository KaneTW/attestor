package de.rwth.i2.attestor.phases.its;

import de.rwth.i2.attestor.its.ITS;
import de.rwth.i2.attestor.main.AbstractPhase;
import de.rwth.i2.attestor.main.scene.Scene;
import de.rwth.i2.attestor.phases.transformers.StateSpaceTransformer;
import de.rwth.i2.attestor.stateSpaceGeneration.StateSpace;

import java.io.*;

public class ITSPhase extends AbstractPhase {

    private StateSpace stateSpace;

    private ITS its;

    public ITSPhase(Scene scene) {
        super(scene);
        
        this.stateSpace = getPhase(StateSpaceTransformer.class).getStateSpace();
    }

    @Override
    public String getName() {
        return "Integer transition system generation";
    }

    @Override
    public void executePhase() throws IOException {
        its = new ITS(stateSpace);


        // hack just to test if it works
        Writer w = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream("its.t2")));
        w.write(its.toString());
        w.close();
    }

    @Override
    public void logSummary() {

    }

    @Override
    public boolean isVerificationPhase() {
        return false;
    }
}
