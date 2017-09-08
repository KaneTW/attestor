package de.rwth.i2.attestor.ipa;

import static org.junit.Assert.fail;

import java.util.*;

import org.junit.Before;
import org.junit.Test;

import de.rwth.i2.attestor.main.settings.Settings;
import de.rwth.i2.attestor.semantics.jimpleSemantics.jimple.statements.*;
import de.rwth.i2.attestor.semantics.jimpleSemantics.jimple.values.*;
import de.rwth.i2.attestor.stateSpaceGeneration.Semantics;
import de.rwth.i2.attestor.types.Type;

public class AbstractMethodIPATest_2 {

	AbstractMethodIPA ipa = new AbstractMethodIPA( "testMethod", null );

	@Before
	public void setupProgram(){
		List<Semantics> program = new ArrayList<>();
		
		Type type = Settings.getInstance().factory().getType("node");
		Value baseValue = new Local(type, "@this");
		SettableValue lhs = new Field( type, baseValue, "next" );
		
		Value rhs = new Local(type, "@parameter0:");
		
		Statement assignStmt = new AssignStmt(lhs, rhs, 1, new HashSet<>(), false );
		Statement returnStmt = new ReturnVoidStmt();
		
		program.add(assignStmt);
		program.add(returnStmt);
		ipa.setControlFlow(program);
	}
	
	@Test
	public void testGetResult() {

		fail("Not yet implemented");
	}

}
