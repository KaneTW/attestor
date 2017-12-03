package de.rwth.i2.attestor.programState.indexedState;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import de.rwth.i2.attestor.MockupSceneObject;
import de.rwth.i2.attestor.main.environment.SceneObject;
import org.junit.*;

import de.rwth.i2.attestor.grammar.Grammar;
import de.rwth.i2.attestor.grammar.IndexMatcher;
import de.rwth.i2.attestor.grammar.materialization.*;
import de.rwth.i2.attestor.grammar.materialization.indexedGrammar.*;
import de.rwth.i2.attestor.graph.heap.HeapConfiguration;
import de.rwth.i2.attestor.stateSpaceGeneration.*;
import de.rwth.i2.attestor.programState.indexedState.index.DefaultIndexMaterialization;


public class TestGeneralMaterializationStrategy_OldIndexedTests {
	
	private MaterializationStrategy materializer;
	ViolationPoints inputVioPoints;

	private SceneObject sceneObject;
	private ExampleIndexedGraphFactory graphFactory;

	@Before
	public void setup(){

		sceneObject = new MockupSceneObject();
		graphFactory = new ExampleIndexedGraphFactory(sceneObject);

		BalancedTreeGrammar balancedTreeGrammar = new BalancedTreeGrammar(sceneObject);

		Grammar grammar = balancedTreeGrammar.getGrammar();
		ViolationPointResolver violationPointResolver = new ViolationPointResolver(grammar);

		IndexMatcher indexMatcher = new IndexMatcher( new DefaultIndexMaterialization() );
		MaterializationRuleManager grammarManager = 
				new IndexedMaterializationRuleManager(violationPointResolver, indexMatcher);
		
		GrammarResponseApplier ruleApplier = 
				new IndexedGrammarResponseApplier( new IndexMaterializationStrategy(), new GraphMaterializer() );
		
		this.materializer = new GeneralMaterializationStrategy( grammarManager, ruleApplier );
	
		inputVioPoints = new ViolationPoints();
		inputVioPoints.add("x", "left");
	}

	@Test
	public void testMaterialize_small() {
		HeapConfiguration inputGraph 
				= graphFactory.getInput_MaterializeSmall_Z();
		ProgramState inputState = new IndexedState(inputGraph).prepareHeap();
		
		ProgramState expected = 
				new IndexedState( graphFactory.getExpected_MaterializeSmall_Z() )
				.prepareHeap();
			
		List<ProgramState> materializedStates = materializer.materialize( inputState, inputVioPoints );
		assertEquals( 1, materializedStates.size() );
		assertTrue( materializedStates.contains(expected) );
	}

	
	@Test
	public void testMaterialize_small2() {
		HeapConfiguration inputGraph 
				= graphFactory.getInput_MaterializeSmall_sZ();
		ProgramState inputState = new IndexedState(inputGraph).prepareHeap();
		List<ProgramState> materializedStates = materializer.materialize( inputState, inputVioPoints );
		assertEquals( 3, materializedStates.size() );
		
		
		ProgramState res1 = 
				new IndexedState( graphFactory.getExpected_MaterializeSmall2_Res1() )
				.prepareHeap();
		ProgramState res2 = 
				new IndexedState( graphFactory.getExpected_MaterializeSmall2_Res2() )
				.prepareHeap();
		ProgramState res3 = 
				new IndexedState( graphFactory.getExpected_MaterializeSmall2_Res3() )
				.prepareHeap();
				
		assertTrue("should contain res1", materializedStates.contains(res1) );
		assertTrue("should contain res2", materializedStates.contains(res2) );
		assertTrue("should contain res3", materializedStates.contains(res3) );
	}
	

	@Ignore
	public void testMaterialize_big() {
		HeapConfiguration inputGraph 
				= graphFactory.getInput_MaterializeBig();
		ProgramState inputState = new IndexedState(inputGraph).prepareHeap();
		List<ProgramState> materializedStates = materializer.materialize( inputState, inputVioPoints );
		assertEquals( 5, materializedStates.size() );
		
		
		ProgramState res1 = 
				new IndexedState( graphFactory.getExpected_MaterializeBig_Res1() )
				.prepareHeap();
		ProgramState res2 = 
				new IndexedState( graphFactory.getExpected_MaterializeBig_Res2() )
				.prepareHeap();
		ProgramState res3 = 
				new IndexedState( graphFactory.getExpected_MaterializeBig_Res3() )
				.prepareHeap();
		ProgramState res4 = new IndexedState( graphFactory.getExpected_MaterializeBig_Res4() ).prepareHeap();
		ProgramState res5 = new IndexedState( graphFactory.getExpected_MaterializeBig_Res5() ).prepareHeap();
		
		
		assertTrue("should contain res1", materializedStates.contains(res1) );
		assertTrue("should contain res2", materializedStates.contains(res2) );
		assertTrue("should contain res3", materializedStates.contains(res3) );
		assertTrue("should contain res4", materializedStates.contains(res4) );
		assertTrue("should contain res5", materializedStates.contains(res5) );
	}
}
