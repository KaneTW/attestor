package de.rwth.i2.attestor.phases.its;

import de.rwth.i2.attestor.its.*;
import de.rwth.i2.attestor.its.certificate.LTS;
import de.rwth.i2.attestor.its.certificate.RankingFunction;
import de.rwth.i2.attestor.its.certificate.Transition;
import de.rwth.i2.attestor.its.certificate.TransitionRemovalProof;
import de.rwth.i2.attestor.main.AbstractPhase;
import de.rwth.i2.attestor.main.scene.Scene;
import de.rwth.i2.attestor.phases.communication.InputSettings;
import de.rwth.i2.attestor.phases.transformers.InputSettingsTransformer;
import de.rwth.i2.attestor.phases.transformers.ProgramTransformer;
import de.rwth.i2.attestor.phases.transformers.StateSpaceTransformer;
import de.rwth.i2.attestor.stateSpaceGeneration.Program;
import de.rwth.i2.attestor.stateSpaceGeneration.StateSpace;

import java.io.*;
import java.util.List;

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
                CertificateMapping mapping = new CertificateMapping(its, result.getLts(), result.getLocationAdditionProofs());
                List<TransitionRemovalProof> removalProofs = result.getTransitionRemovalProofs();

                for (TransitionRemovalProof proof : removalProofs) {
                    for (Transition tr : proof.getRemove()) {
                        ITSTransition itsTr = mapping.getTransitionMap().get(tr.getTransition());
                        if (itsTr == null) {
                            int originState = mapping.getTransitionSnapshotMap().get(tr.getTransition());

                            RankingFunction rf =  proof.getRankingFunctions().stream().filter(rankingFunction -> rankingFunction.getLocation().isDuplicate() && rankingFunction.getLocation().getLocation().equals(Integer.toString(originState))).findAny().get();
                            if (!rf.getExpression().isConstant()) {
                                logger.info("Program is terminating because at start state " + originState + " of a SCC, the rank function " + rf.getExpression() + " is strictly decreasing");
                            }
                        } else {
                            int source = itsTr.getFrom();
                            int target = itsTr.getTo();
                            RankingFunction rfSource =  proof.getRankingFunctions().stream().filter(rankingFunction -> rankingFunction.getLocation().isDuplicate() && rankingFunction.getLocation().getLocation().equals(Integer.toString(source))).findAny().get();
                            RankingFunction rfTarget =  proof.getRankingFunctions().stream().filter(rankingFunction -> rankingFunction.getLocation().isDuplicate() && rankingFunction.getLocation().getLocation().equals(Integer.toString(target))).findAny().get();

                            logger.info("Program is terminating because, in the transition from " + source + " to " + target + ", " +  rfSource + " is strictly smaller than " + rfTarget);
                        }
                    }
                }
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
