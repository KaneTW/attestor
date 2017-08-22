package de.rwth.i2.attestor.grammar.canoncalization.moduleTest;

import static org.junit.Assert.assertEquals;

import java.util.*;

import org.junit.Before;
import org.junit.Test;

import de.rwth.i2.attestor.grammar.Grammar;
import de.rwth.i2.attestor.grammar.IndexMatcher;
import de.rwth.i2.attestor.grammar.canonicalization.EmbeddingCheckerProvider;
import de.rwth.i2.attestor.grammar.canonicalization.GeneralCanonicalizationStrategy;
import de.rwth.i2.attestor.grammar.canonicalization.indexedGrammar.EmbeddingIndexChecker;
import de.rwth.i2.attestor.grammar.canonicalization.indexedGrammar.IndexedMatchingHandler;
import de.rwth.i2.attestor.grammar.materialization.indexedGrammar.IndexMaterializationStrategy;
import de.rwth.i2.attestor.graph.*;
import de.rwth.i2.attestor.graph.heap.HeapConfiguration;
import de.rwth.i2.attestor.graph.heap.HeapConfigurationBuilder;
import de.rwth.i2.attestor.graph.heap.internal.InternalHeapConfiguration;
import de.rwth.i2.attestor.main.settings.Settings;
import de.rwth.i2.attestor.semantics.jimpleSemantics.jimple.statements.Skip;
import de.rwth.i2.attestor.semantics.jimpleSemantics.jimple.statements.Statement;
import de.rwth.i2.attestor.stateSpaceGeneration.ProgramState;
import de.rwth.i2.attestor.strategies.defaultGrammarStrategies.DefaultProgramState;
import de.rwth.i2.attestor.strategies.indexedGrammarStrategies.IndexedNonterminalImpl;
import de.rwth.i2.attestor.strategies.indexedGrammarStrategies.IndexedState;
import de.rwth.i2.attestor.strategies.indexedGrammarStrategies.index.*;
import de.rwth.i2.attestor.types.Type;
import gnu.trove.list.array.TIntArrayList;

public class GeneralCanonicalizationStrategy_Indexed_WithIndexCanonicalization {

	private static final String NT_LABEL = "GeneralCanonicalizationStrategyIC";
	private static final int RANK = 2;
	private static final boolean[] isReductionTentacle = new boolean[RANK];
	private static final Type TYPE = Settings.getInstance().factory().getType("type");
	private static final SelectorLabel SEL = GeneralSelectorLabel.getSelectorLabel("sel");
	
	private static final int sizeOfChain = 10;

	private IndexedMatchingHandler matchingHandler;
	
	@Before
	public void init() {
		IndexCanonizationStrategy indexStrategy = new AVLIndexCanonizationStrategy();
		
		final int minDereferenceDepth = 1;
		final int aggressiveAbstractionThreshold = 10;
		final boolean aggressiveReturnAbstraction = false;
		EmbeddingCheckerProvider checkerProvider = new EmbeddingCheckerProvider(minDereferenceDepth ,
																				aggressiveAbstractionThreshold, 
																				aggressiveReturnAbstraction);
		
		IndexMaterializationStrategy materializer = new IndexMaterializationStrategy();
		DefaultIndexMaterialization stackGrammar = new DefaultIndexMaterialization();
		IndexMatcher stackMatcher = new IndexMatcher( stackGrammar);
		EmbeddingIndexChecker stackChecker = 
				new EmbeddingIndexChecker( stackMatcher, 
											materializer );
		
		matchingHandler = new IndexedMatchingHandler( indexStrategy, checkerProvider, stackChecker);
		
	}

	@Test
	public void test() {
//		List<StackSymbol> lhsStack0 = makeConcrete( getEmptyStack() );
//		Nonterminal lhs0 = getNonterminal( lhsStack0 );
//		HeapConfiguration rhs0 = getPattern0();
		
		List<IndexSymbol> lhsStack1 = makeInstantiable(getStackPrefix());
		Nonterminal lhs1 = getNonterminal( lhsStack1  );
		HeapConfiguration rhs1 = getPattern1();
		HeapConfiguration rhs2 = getPattern2();
		Grammar grammar = Grammar.builder().addRule( lhs1, rhs1 )
										   .addRule(lhs1, rhs2)
										   .build();
		
		GeneralCanonicalizationStrategy canonizer 
				= new GeneralCanonicalizationStrategy( grammar, matchingHandler );
		
		ProgramState inputState = new DefaultProgramState( getInputGraph() );
		Statement stmt = new Skip( 0 );
		
		Set<ProgramState> res = canonizer.canonicalize(stmt, inputState);
		
		assertEquals( 1, res.size() );
		assertEquals( expectedSimpleAbstraction().getHeap(), res.iterator().next().getHeap() );
	}



	private List<IndexSymbol> getEmptyStack() {
		List<IndexSymbol> stack = new ArrayList<>();
		return stack;
	}
	
	private List<IndexSymbol> getStackPrefix() {
		List<IndexSymbol> stack = getEmptyStack();
		stack.add( DefaultIndexMaterialization.SYMBOL_s );
		return stack;
	}

	private List<IndexSymbol> makeConcrete( List<IndexSymbol> stack ){
		List<IndexSymbol> stackCopy = new ArrayList<>( stack );
		stackCopy.add( DefaultIndexMaterialization.SYMBOL_Z );
		return stackCopy;
	}
	
	private List<IndexSymbol> makeInstantiable( List<IndexSymbol> stack ){
		List<IndexSymbol> stackCopy = new ArrayList<>( stack );
		stackCopy.add( IndexVariable.getGlobalInstance() );
		return stackCopy;
	}


	private Nonterminal getNonterminal( List<IndexSymbol> stack ) {
		return new IndexedNonterminalImpl(NT_LABEL, RANK, isReductionTentacle, stack);
	}

	private HeapConfiguration getPattern0() {
		HeapConfiguration hc = new InternalHeapConfiguration();
		
		TIntArrayList nodes = new TIntArrayList();
		return hc.builder().addNodes(TYPE, 2, nodes)
				.addSelector(nodes.get(0), SEL , nodes.get(1) )
				.setExternal(nodes.get(0))
				.setExternal(nodes.get(1))
				.build();
	}


	private HeapConfiguration getPattern1() {
		HeapConfiguration hc = new InternalHeapConfiguration();
		
		TIntArrayList nodes = new TIntArrayList();
		return hc.builder().addNodes(TYPE, 3, nodes)
				.addNonterminalEdge( getNonterminal( makeInstantiable(getEmptyStack()) ))
					.addTentacle(nodes.get(0))
					.addTentacle(nodes.get(1))
					.build()
				.addSelector(nodes.get(1), SEL , nodes.get(2) )
				.setExternal(nodes.get(0))
				.setExternal(nodes.get(2))
				.build();
	}
	


	private HeapConfiguration getPattern2() {
		HeapConfiguration hc = new InternalHeapConfiguration();
		
		TIntArrayList nodes = new TIntArrayList();
		return hc.builder().addNodes(TYPE, 3, nodes)
				.addSelector(nodes.get(2), SEL , nodes.get(1) )
				.addNonterminalEdge( getNonterminal( makeInstantiable(getEmptyStack()) ))
					.addTentacle(nodes.get(1))
					.addTentacle(nodes.get(2))
					.build()
				.setExternal(nodes.get(0))
				.setExternal(nodes.get(2))
				.build();
	}
	


	private HeapConfiguration getInputGraph() {
		HeapConfiguration hc = new InternalHeapConfiguration();
		
		TIntArrayList nodes = new TIntArrayList();
		HeapConfigurationBuilder builder =  hc.builder().addNodes(TYPE, sizeOfChain + 1, nodes);
		for( int i = 1; i < sizeOfChain ; i++ ) {
				builder.addSelector(nodes.get(i), SEL , nodes.get(i+1) );
		}
		builder.addNonterminalEdge( getNonterminal( makeConcrete(getEmptyStack())))
					.addTentacle(nodes.get(0))
					.addTentacle(nodes.get(1))
					.build();
		
		return	builder.build();
	}
	
	private ProgramState expectedSimpleAbstraction() {
		HeapConfiguration hc = new InternalHeapConfiguration();
		
		List<IndexSymbol> expectedStack = getExpectedStack();
		
		TIntArrayList nodes = new TIntArrayList();
		hc =  hc.builder().addNodes(TYPE, 2, nodes)
				.addNonterminalEdge( getNonterminal(expectedStack ))
					.addTentacle(nodes.get(0))
					.addTentacle(nodes.get(1))
					.build()
				.build();
		
		return new IndexedState( hc );
	}

	private List<IndexSymbol> getExpectedStack() {
		List<IndexSymbol> stack = getEmptyStack();
		stack.add( DefaultIndexMaterialization.SYMBOL_X );
		return stack;
	}

}
