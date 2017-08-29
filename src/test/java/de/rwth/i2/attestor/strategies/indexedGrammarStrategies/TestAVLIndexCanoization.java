package de.rwth.i2.attestor.strategies.indexedGrammarStrategies;

import static org.junit.Assert.*;

import org.junit.*;

import de.rwth.i2.attestor.UnitTestGlobalSettings;
import de.rwth.i2.attestor.graph.heap.HeapConfiguration;
import de.rwth.i2.attestor.strategies.indexedGrammarStrategies.index.*;


public class TestAVLIndexCanoization {

	private AVLIndexCanonizationStrategy canonizer;

	@BeforeClass
	public static void init() {

		UnitTestGlobalSettings.reset();
	}

	@Before
	public void setup(){
		canonizer = new AVLIndexCanonizationStrategy();
	}
	
	@Test
	public void testCanonizeIndex() {
		HeapConfiguration graph = ExampleIndexedGraphFactory.getBalancedTreeLeft3();

		IndexedNonterminal leftNonterminal = getLabelOfVar(graph, "left");
		assertEquals("left before", 4, leftNonterminal.getIndex().size());
		assertTrue("left before", leftNonterminal.getIndex().hasConcreteIndex());
		IndexedNonterminal rightNonterminal = getLabelOfVar(graph, "right");
		assertEquals("right before", 3, rightNonterminal.getIndex().size());
		assertTrue("right before", rightNonterminal.getIndex().hasConcreteIndex());
		
		canonizer.canonizeIndex(graph);

		//ensure original Nonterminals did not alter
		assertEquals("left before", 4, leftNonterminal.getIndex().size());
		assertTrue("left before", leftNonterminal.getIndex().hasConcreteIndex());
		assertEquals("right before", 3, rightNonterminal.getIndex().size());
		assertTrue("right before", rightNonterminal.getIndex().hasConcreteIndex());
		
		//ensure nonterminals now present are correctly altered
		IndexedNonterminal leftNonterminalRes = getLabelOfVar(graph, "left");

		assertEquals("left after abs", 2, leftNonterminalRes.getIndex().size());
		assertFalse("left after abs", leftNonterminalRes.getIndex().hasConcreteIndex());
		assertEquals( leftNonterminalRes.getIndex().get( 0 ), ConcreteIndexSymbol.getIndexSymbol( "s", false ));
		assertEquals( leftNonterminalRes.getIndex().get( 1 ), AbstractIndexSymbol.get( "X" ) );
		IndexedNonterminal rightNonterminalRes = getLabelOfVar(graph, "right");
		assertEquals("right after abs", 1, rightNonterminalRes.getIndex().size());
		assertEquals( rightNonterminalRes.getIndex().get( 0 ), AbstractIndexSymbol.get( "X" ) );
	}
	
	@Test
	public void testCanonizeIndex2() {
		HeapConfiguration graph = ExampleIndexedGraphFactory.getCannotAbstractIndex();
		
		IndexedNonterminal leftNonterminal = getLabelOfVar(graph, "left");
		
		assertEquals("left before",4, leftNonterminal.getIndex().size());
		assertFalse("left before",leftNonterminal.getIndex().hasConcreteIndex());
		IndexedNonterminal rightNonterminal = getLabelOfVar(graph, "right");
		assertEquals("right before",3, rightNonterminal.getIndex().size());
		assertTrue("right before",rightNonterminal.getIndex().hasConcreteIndex());
		
		canonizer.canonizeIndex(graph);
		
		//IndexedNonterminal leftNonterminal = graph.getVariable("left").getTarget().getAttachedNonterminalEdges().get(0).computeAtomicPropositions();
		assertEquals("left after abs",4, leftNonterminal.getIndex().size());
		assertFalse("left after abs",leftNonterminal.getIndex().hasConcreteIndex());
		//IndexedNonterminal rightNonterminal = graph.getVariable("right").getTarget().getAttachedNonterminalEdges().get(0).computeAtomicPropositions();
		assertEquals("right after abs",3, rightNonterminal.getIndex().size());
		assertTrue("right after abs",rightNonterminal.getIndex().hasConcreteIndex());
	}
	
	private IndexedNonterminal getLabelOfVar(HeapConfiguration graph, String name) {
		
		int var = graph.variableWith(name);
		int tar = graph.targetOf(var);
		int ntEdge = graph.attachedNonterminalEdgesOf(tar).get(0);
		return (IndexedNonterminal) graph.labelOf(ntEdge);
	}
	
	@Test
	public void testCanonizeIndex_Blocked(){
		HeapConfiguration input = ExampleIndexedGraphFactory.getInput_indexCanonization_Blocked();
		canonizer.canonizeIndex(input);
		assertEquals(ExampleIndexedGraphFactory.getInput_indexCanonization_Blocked(), input);		
	}

}