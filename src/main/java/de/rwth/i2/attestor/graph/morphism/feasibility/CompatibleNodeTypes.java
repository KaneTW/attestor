package de.rwth.i2.attestor.graph.morphism.feasibility;

import de.rwth.i2.attestor.graph.digraph.NodeLabel;
import de.rwth.i2.attestor.graph.morphism.CandidatePair;
import de.rwth.i2.attestor.graph.morphism.FeasibilityFunction;
import de.rwth.i2.attestor.graph.morphism.Graph;
import de.rwth.i2.attestor.graph.morphism.VF2State;
import de.rwth.i2.attestor.types.GeneralType;
import de.rwth.i2.attestor.types.Type;

/**
 * Checks whether the labels of the nodes in the candidate pair coincide.
 *
 * @author Christoph
 */
public class CompatibleNodeTypes implements FeasibilityFunction {

	private static final Type nullType = GeneralType.getType("NULL");

	@Override
	public boolean eval(VF2State state, CandidatePair candidate) {

		Graph patternGraph = state.getPattern().getGraph();
		Graph targetGraph = state.getTarget().getGraph();

		NodeLabel patternLabel = patternGraph.getNodeLabel(candidate.p);
		NodeLabel targetLabel = targetGraph.getNodeLabel(candidate.t);

		if(patternLabel.getClass() == GeneralType.class && targetLabel.getClass() == GeneralType.class){
			GeneralType patternType = (GeneralType) patternLabel;
			GeneralType targetType = (GeneralType) targetLabel;
			return patternType.typeEquals(targetType)
					|| targetType.typeEquals( GeneralType.getType("NULL") );
		}else {
			return patternLabel.matches(targetLabel);
		}
	}

}
