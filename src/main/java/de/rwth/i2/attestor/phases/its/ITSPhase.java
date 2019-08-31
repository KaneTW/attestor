package de.rwth.i2.attestor.phases.its;

import de.rwth.i2.attestor.its.ITS;
import de.rwth.i2.attestor.main.AbstractPhase;
import de.rwth.i2.attestor.main.scene.Scene;
import de.rwth.i2.attestor.phases.transformers.ProgramTransformer;
import de.rwth.i2.attestor.phases.transformers.StateSpaceTransformer;
import de.rwth.i2.attestor.stateSpaceGeneration.Program;
import de.rwth.i2.attestor.stateSpaceGeneration.StateSpace;

import java.io.*;

public class ITSPhase extends AbstractPhase {

    private StateSpace stateSpace;
    private Program program;

    private ITS its;

    public ITSPhase(Scene scene) {
        super(scene);
    }

    @Override
    public String getName() {
        return "Integer transition system generation";
    }

    @Override
    public void executePhase() throws IOException {
        this.stateSpace = getPhase(StateSpaceTransformer.class).getStateSpace();
        this.program = getPhase(ProgramTransformer.class).getProgram();

        this.its = new ITS(stateSpace, program);


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
