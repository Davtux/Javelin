package fr.unilim.automaton.utils;

import static org.junit.Assert.*;

import org.junit.Test;

public class ConditionInverterTest {

	
	@Test
	public void testAND() {
		String test = "a && b";
		String expected = "!a || !b";
		assertEquals(expected, ConditionInverter.invertCondition(test));		
	}
	@Test
	public void testOR() {
		String test = "a || b";
		String expected = "!a && !b";
		assertEquals(expected, ConditionInverter.invertCondition(test));		
	}
	
	@Test
	public void testLandG(){
		String testL = "a < b";
		String expectedL = "a >= b";
		String testG = "a > 1.23";
		String expectedG = "a <= 1.23";
		assertEquals(expectedL, ConditionInverter.invertCondition(testL));
		assertEquals(expectedG, ConditionInverter.invertCondition(testG));
	}
	
	@Test
	public void testLEandGE(){
		String testL = "a >= 7";
		String expectedL = "a < 7";
		String testG = "a >= 1.23";
		String expectedG = "a < 1.23";
		assertEquals(expectedL, ConditionInverter.invertCondition(testL));
		assertEquals(expectedG, ConditionInverter.invertCondition(testG));
	}
	
	@Test
	public void testEqual(){
		String testE = "a == 7";
		String expectedE = "a != 7";
		String testNE = "a != 1.23";
		String expectedNE = "a == 1.23";
		assertEquals(expectedE, ConditionInverter.invertCondition(testE));
		assertEquals(expectedNE, ConditionInverter.invertCondition(testNE));
	}
	
	@Test
	public void testCombine(){
		String testE = "(a == 7) && a";
		String expectedE = "(a != 7) || !a";
		String testNE = "(a != 7) || !c";
		String expectedNE = "(a == 7) && c";
		assertEquals(expectedE, ConditionInverter.invertCondition(testE));
		assertEquals(expectedNE, ConditionInverter.invertCondition(testNE));
	}
	
	@Test
	public void testEquals(){
		String testEquals = "foo.equals('a')";
		String expectedEquals = "!foo.equals('a')";
		assertEquals(expectedEquals, ConditionInverter.invertCondition(testEquals));
	}

}
