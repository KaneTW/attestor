package de.rwth.i2.attestor.grammar.materialization.communication;

import java.util.*;

import de.rwth.i2.attestor.graph.heap.HeapConfiguration;


public class MaterializationAndRuleResponse implements GrammarResponse {

	private AbstractIndexSymbol symbolToMaterialize;
	private Map<List<IndexSymbol>, Collection<HeapConfiguration>> materializationsAndRules;

	public MaterializationAndRuleResponse(Map<List<IndexSymbol>, Collection<HeapConfiguration>> rules,
			AbstractIndexSymbol stackSymbolToMaterialize ) {
		super();
		this.materializationsAndRules = rules;
		this.symbolToMaterialize = stackSymbolToMaterialize;
	}
	

	public boolean hasStackSymbolToMaterialize(){
		return symbolToMaterialize != null;
	}
	
	public AbstractIndexSymbol getStackSymbolToMaterialize(){
		return symbolToMaterialize;
	}
	
	public Set<List<IndexSymbol>> getPossibleMaterializations(){
		return materializationsAndRules.keySet();
	}
	
	public Collection<HeapConfiguration> getRulesForMaterialization( List<IndexSymbol> materialization ){
		return materializationsAndRules.get( materialization );
	}
	
}
