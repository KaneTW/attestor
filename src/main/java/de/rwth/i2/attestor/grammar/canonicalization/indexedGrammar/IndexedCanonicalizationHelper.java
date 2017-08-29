package de.rwth.i2.attestor.grammar.canonicalization.indexedGrammar;

import de.rwth.i2.attestor.grammar.canonicalization.CanonicalizationHelper;
import de.rwth.i2.attestor.grammar.canonicalization.EmbeddingCheckerProvider;
import de.rwth.i2.attestor.graph.Nonterminal;
import de.rwth.i2.attestor.graph.heap.HeapConfiguration;
import de.rwth.i2.attestor.graph.heap.Matching;
import de.rwth.i2.attestor.graph.heap.matching.AbstractMatchingChecker;
import de.rwth.i2.attestor.stateSpaceGeneration.ProgramState;
import de.rwth.i2.attestor.stateSpaceGeneration.Semantics;
import de.rwth.i2.attestor.strategies.indexedGrammarStrategies.index.IndexCanonizationStrategy;

/**
 * This class provides the methods to canonicalisation which are specific for
 * indexed grammars.
 * 
 * @author Hannah
 *
 */
public class IndexedCanonicalizationHelper implements CanonicalizationHelper {

	public IndexCanonizationStrategy indexCanonizationStrategy;
	public EmbeddingCheckerProvider checkerProvider;
	public EmbeddingIndexChecker indexChecker;

	/**
	 * @param indexCanonicalizer canonicalises the indices before the graph is canonicalised.
	 * Also responsible to canonicalise them only when admissible
	 * @param checkerProvider generates a EmbeddingChecker for given graph and pattern. Responsible
	 * to generate the correct one for given settings and semantics.
	 * @param indexChecker responsible to match the indices of embeddings provided my the embeddingChecker
	 */
	public IndexedCanonicalizationHelper( IndexCanonizationStrategy indexCanonicalizer,
								   EmbeddingCheckerProvider checkerProvider, 
								   EmbeddingIndexChecker indexChecker ) {
		super();
		this.indexCanonizationStrategy = indexCanonicalizer;
		this.checkerProvider = checkerProvider;
		this.indexChecker = indexChecker;
	}

	/* (non-Javadoc)
	 * @see de.rwth.i2.attestor.grammar.canonicalization.MatchingHandler#tryReplaceMatching(de.rwth.i2.attestor.stateSpaceGeneration.ProgramState, de.rwth.i2.attestor.graph.heap.HeapConfiguration, de.rwth.i2.attestor.graph.Nonterminal, de.rwth.i2.attestor.stateSpaceGeneration.Semantics, boolean)
	 */
	@Override
	public ProgramState tryReplaceMatching( ProgramState state, 
												 HeapConfiguration rhs, Nonterminal lhs,
												Semantics semantics ) {

		ProgramState result = null;

		AbstractMatchingChecker checker = 
				checkerProvider.getEmbeddingChecker( state.getHeap(), rhs, semantics);

		while( checker.hasNext() && result == null  ) {
			ProgramState toAbstract  = state;

			Matching embedding = checker.getNext();
			try {
				IndexEmbeddingResult res = 
						indexChecker.getIndexEmbeddingResult(toAbstract.getHeap(), embedding, lhs);
	
				HeapConfiguration abstracted = replaceEmbeddingBy( res.getMaterializedToAbstract(), 
															  embedding, res.getInstantiatedLhs() );
				result = toAbstract.shallowCopyWithUpdateHeap( abstracted );
				
			}catch( CannotMatchException e ) {
				//this may happen. loop will continue.
			}
	
		}
		return result;
	}

	/**
	 * replaces the embedding in  abstracted by the given nonterminal
	 * 
	 * @param toAbstract the outer graph.
	 * @param embedding the embedding of the inner graph in the outer graph
	 * @param nonterminal the nonterminal to replace the embedding
	 */
	private HeapConfiguration replaceEmbeddingBy( HeapConfiguration toAbstract, Matching embedding, Nonterminal nonterminal) {
		toAbstract = toAbstract.clone();
		HeapConfiguration abstracted = toAbstract.builder()
											     .replaceMatching(embedding, nonterminal)
											     .build();
		return abstracted;
	}

	/**
	 * For indexed HeapConfigurations this canonicalises the indices.
	 */
	@Override
	public ProgramState prepareHeapForCanonicalization(ProgramState toAbstract) {
		HeapConfiguration heap = toAbstract.getHeap().clone();
		indexCanonizationStrategy.canonizeIndex( heap );
		return toAbstract.shallowCopyWithUpdateHeap( heap );
	}


}
