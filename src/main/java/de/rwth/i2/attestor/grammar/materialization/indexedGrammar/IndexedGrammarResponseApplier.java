package de.rwth.i2.attestor.grammar.materialization.indexedGrammar;

import de.rwth.i2.attestor.grammar.materialization.communication.*;
import de.rwth.i2.attestor.graph.heap.HeapConfiguration;
import de.rwth.i2.attestor.strategies.indexedGrammarStrategies.index.IndexSymbol;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * Capable of handling {@link MaterializationAndRuleResponse}
 * in addition to all {@link GrammarResponse}s handled by {@link DefaultGrammarResponseApplier}
 * 
 * @author Hannah
 *
 */
public class IndexedGrammarResponseApplier extends DefaultGrammarResponseApplier {

	private static final Logger logger = LogManager.getLogger( "IndexedGrammarResponseApplier" );

	IndexMaterializationStrategy indexMaterializationStrategy;

	public IndexedGrammarResponseApplier(IndexMaterializationStrategy indexMaterializationStrategy,
			GraphMaterializer graphMaterializer) {
		super( graphMaterializer );
		this.indexMaterializationStrategy = indexMaterializationStrategy;
	}
	
	public Collection<HeapConfiguration> applyGrammarResponseTo( HeapConfiguration inputGraph, 
			int edgeId, 
			GrammarResponse grammarResponse) 
			throws WrongResponseTypeException {
		
		if( grammarResponse instanceof MaterializationAndRuleResponse ){
			 MaterializationAndRuleResponse indexedRespose = 
					 (MaterializationAndRuleResponse) grammarResponse;
			
			 Collection<HeapConfiguration> materializedGraphs = new ArrayList<>();
			 
			 for( List<IndexSymbol> materialization : indexedRespose.getPossibleMaterializations() ){
				 	 
				 HeapConfiguration materializedStacks;
				try {
					materializedStacks = indexMaterializationStrategy
							.getMaterializedCloneWith(inputGraph, 
													  indexedRespose.getStackSymbolToMaterialize(), 
													  materialization );
					 materializedGraphs.addAll( super.applyGrammarResponseTo(materializedStacks, 
							 edgeId, 
							 new DefaultGrammarResponse( indexedRespose
									 .getRulesForMaterialization(materialization))) ); 
				} catch (CannotMaterializeException e) {
					logger.error("Could not materialize a graph. graph: " + inputGraph );
				}
				
			 }
			 
			 return materializedGraphs;
		}else{
			return super.applyGrammarResponseTo( inputGraph, edgeId, grammarResponse );
		}
	}

}

