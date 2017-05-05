package fr.unilim.automaton.utils;

import static org.junit.Assert.*;

import org.junit.Test;

public class ConditionValueToHexTest {

	@Test
	public void testPositive() {
		String value = "value > 50";
		String expected = "value > 0x32";
		assertEquals(expected, ConditionValueToHex.transform(value));
	}
	
	@Test
	public void testNegative() {
		String value = "value > -128";
		String expected = "value > 0x80";
		assertEquals(expected, ConditionValueToHex.transform(value));
	}
	
	@Test
	public void testNumberInValueName() {
		String value = "value12 > 10";
		String expected = "value12 > 0x0A";
		assertEquals(expected, ConditionValueToHex.transform(value));
	}
	
	@Test
	public void testComplex() {
		String value = "value12 > 1 && value == 20";
		String expected = "value12 > 0x01 && value == 0x14";
		assertEquals(expected, ConditionValueToHex.transform(value));
	}
	
	@Test
	public void testComplex2() {
		String value = "(value12 > 1) && (value == 20)";
		String expected = "(value12 > 0x01) && (value == 0x14)";
		assertEquals(expected, ConditionValueToHex.transform(value));
	}
	
	@Test
	public void testNotByte() {
		String value = "value12 > 259";
		String expected = "value12 > 259";
		assertEquals(expected, ConditionValueToHex.transform(value));
	}

}
