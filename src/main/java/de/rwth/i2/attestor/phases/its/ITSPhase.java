package de.rwth.i2.attestor.phases.its;

import com.google.common.graph.EndpointPair;
import com.google.common.graph.MutableNetwork;
import com.google.common.graph.Network;
import de.rwth.i2.attestor.its.*;
import de.rwth.i2.attestor.its.certificate.*;
import de.rwth.i2.attestor.main.AbstractPhase;
import de.rwth.i2.attestor.main.scene.Scene;
import de.rwth.i2.attestor.phases.communication.InputSettings;
import de.rwth.i2.attestor.phases.transformers.InputSettingsTransformer;
import de.rwth.i2.attestor.phases.transformers.ProgramTransformer;
import de.rwth.i2.attestor.phases.transformers.StateSpaceTransformer;
import de.rwth.i2.attestor.stateSpaceGeneration.Program;
import de.rwth.i2.attestor.stateSpaceGeneration.StateSpace;
import de.rwth.i2.attestor.util.Pair;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

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
                List<List<Invariant>> invariants = getInvariants(result);
                HashSet<String> terminationRelevantVars = new HashSet<>();
                HashSet<String> strictTerminationRelevantVars = new HashSet<>();

                for (List<Invariant> invariantSet : invariants) {
                    logger.info("Invariants: " + invariantSet.stream().map(invariant -> invariant.toString()).collect(Collectors.joining(", ")));
                    invariantSet.stream().forEach(invariant -> terminationRelevantVars.addAll(invariant.getOccurringVariables()));
                    invariantSet.stream().filter(Invariant::isStrict).forEach(invariant -> strictTerminationRelevantVars.addAll(invariant.getOccurringVariables()));
                }

                logger.info("Termination relevant variables: " + String.join(", ", terminationRelevantVars));
                logger.info("Termination relevant variables in strict invariants: " + String.join(", ", strictTerminationRelevantVars));
            }
        } else {
            logger.info("Didn't run ITS");
        }
    }

    private List<List<Invariant>> getInvariants(T2Result result) {
        MutableNetwork<String, Integer> graph = result.getLts().asMutableNetwork();

        CooperationProof proof = result.getCooperationTermination().getProof();

        return getInvariants(graph, proof);
    }

    private List<List<Invariant>> getInvariants(Network<String, Integer> graph, CooperationProof proof) {
        if (proof == null) {
            return Collections.emptyList();
        }

        if (proof instanceof SccDecompositionProof) {
            SccDecompositionProof sccProof = (SccDecompositionProof) proof;

            return sccProof.getContainedSccs().stream().flatMap(
                    scc -> getInvariants(scc.getCurrentGraph(graph), scc.getNextProof()).stream()
            ).collect(Collectors.toList());
        }

        if (proof instanceof CutTransitionSplitProof) {
            CutTransitionSplitProof cutProof = (CutTransitionSplitProof) proof;

            if (cutProof.getContainedCuts() == null) {
                return Collections.emptyList(); // terminal proof object
            }

            return cutProof.getContainedCuts().stream().flatMap(
                    cut -> getInvariants(graph, cut.getNextProof()).stream()
            ).collect(Collectors.toList());
        }

        if (proof instanceof LocationAdditionProof) {
            LocationAdditionProof locProof = (LocationAdditionProof) proof;
            return getInvariants(locProof.getCurrentGraph(graph), locProof.getNextProof());
        }

        if (proof instanceof NewInvariantsProof) {
            NewInvariantsProof newInvarProof = (NewInvariantsProof) proof;
            return getInvariants(graph, newInvarProof.getNextProof());
        }

        if (proof instanceof FreshVariableAdditionProof) {
            FreshVariableAdditionProof freshVarProof = (FreshVariableAdditionProof) proof;
            return getInvariants(graph, freshVarProof.getNextProof());
        }

        if (proof instanceof TransitionRemovalProof) {
            TransitionRemovalProof trProof = (TransitionRemovalProof) proof;

            ArrayList<Invariant> invariants = new ArrayList<>();

            for (Integer transition : graph.edges()) {
                EndpointPair<String> nodes = graph.incidentNodes(transition);

                RankingFunction rfSource = trProof.getRankingFunctions().stream()
                        .filter(rankingFunction -> rankingFunction.getLocation().isDuplicate()
                                && rankingFunction.getLocation().getLocation()
                                .equals(nodes.source())).findAny().get();

                RankingFunction rfTarget = trProof.getRankingFunctions().stream()
                        .filter(rankingFunction -> rankingFunction.getLocation().isDuplicate()
                                && rankingFunction.getLocation().getLocation()
                                .equals(nodes.target())).findAny().get();

                boolean strict = trProof.getRemove().stream().anyMatch(tr -> tr.getTransition() == transition);

                invariants.add(new Invariant(rfSource, rfTarget, strict));
            }

            List<List<Invariant>> nextInvariants = new ArrayList<>(getInvariants(trProof.getCurrentGraph(graph), trProof.getNextProof()));
            nextInvariants.add(0, invariants);
            return nextInvariants;
        }

        throw new RuntimeException("Unknown proof class: " + proof.getClass());
    }

    @Override
    public boolean isVerificationPhase() {
        return false;
    }
}
