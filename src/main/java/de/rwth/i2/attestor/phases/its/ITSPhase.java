package de.rwth.i2.attestor.phases.its;

import de.rwth.i2.attestor.its.ITS;
import de.rwth.i2.attestor.its.T2Invoker;
import de.rwth.i2.attestor.its.T2Result;
import de.rwth.i2.attestor.its.T2Status;
import de.rwth.i2.attestor.main.AbstractPhase;
import de.rwth.i2.attestor.main.scene.Scene;
import de.rwth.i2.attestor.phases.communication.InputSettings;
import de.rwth.i2.attestor.phases.transformers.InputSettingsTransformer;
import de.rwth.i2.attestor.phases.transformers.ProgramTransformer;
import de.rwth.i2.attestor.phases.transformers.StateSpaceTransformer;
import de.rwth.i2.attestor.stateSpaceGeneration.Program;
import de.rwth.i2.attestor.stateSpaceGeneration.StateSpace;

import java.io.*;

public class ITSPhase extends AbstractPhase {

    private StateSpace stateSpace;
    private Program program;

    private ITS its;

    private T2Result result;

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
        InputSettings inputSettings = getPhase(InputSettingsTransformer.class).getInputSettings();

        String path = inputSettings.getT2Path();

        if (path != null) {
            T2Invoker invoker = new T2Invoker(inputSettings.getT2Path());
            this.its = new ITS(stateSpace, program, invoker);
            result = this.its.getResult();
        } else {
            result = null;
        }

    }

    @Override
    public void logSummary() {
        if (this.result != null) {
            logger.info("ITS checking result: " + result.getStatus() + " at " + result.getOutputDirectory());
            if (result.getStatus() == T2Status.TERMINATING) {
                result.getProofs();
            }
        } else {
            logger.info("Didn't run ITS");
        }
    }

    @Override
    public boolean isVerificationPhase() {
        return false;
    }
}
