package de.rwth.i2.attestor.grammar.canonicalization;

import de.rwth.i2.attestor.graph.Nonterminal;
import de.rwth.i2.attestor.graph.heap.HeapConfiguration;
import de.rwth.i2.attestor.stateSpaceGeneration.ProgramState;
import de.rwth.i2.attestor.stateSpaceGeneration.Semantics;

public interface CanonicalizationHelper {

	/**
	 * If an embedding of rhs in toAbstract can be found it computes it
	 * and replaces it with a nonterminal edge labeled with lhs. 
	 * @param toAbstract the target graph
	 * @param rhs the pattern graph
	 * @param lhs the label of the replacing nonterminaledge
	 * @param semantics the current semantics statement (necessary to configure the embedding mechanism)
	 * @return the abstracted graph if an embedding of rhs can be found, null otherwise.
	 */
	ProgramState tryReplaceMatching( ProgramState toAbstract, 
										  HeapConfiguration rhs, Nonterminal lhs,
										  Semantics semantics );
	
	/**
	 * If the grammar type requires a modification of the graph before it can be abstracted,
	 * it is done with this method.
	 * @param toAbstract the graph which shall be abstracted
	 * @return the modified graph (or the graph itself if no modification is necessary)
	 */
	ProgramState prepareHeapForCanonicalization( ProgramState toAbstract );

}