package de.charite.compbio.mirnator.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class MreToolsTest {

	@Test
	public void testA() {
		assertTrue(MreTools.checkComplementaryMatch('A', 'T'));
		assertTrue(MreTools.checkComplementaryMatch('A', 't'));
		assertTrue(MreTools.checkComplementaryMatch('A', 'U'));
		assertTrue(MreTools.checkComplementaryMatch('A', 'u'));
		assertTrue(MreTools.checkComplementaryMatch('a', 'T'));
		assertTrue(MreTools.checkComplementaryMatch('a', 't'));
		assertTrue(MreTools.checkComplementaryMatch('a', 'U'));
		assertTrue(MreTools.checkComplementaryMatch('a', 'u'));

		assertFalse(MreTools.checkComplementaryMatch('a', 'N'));
		assertFalse(MreTools.checkComplementaryMatch('a', 'n'));
		assertFalse(MreTools.checkComplementaryMatch('a', 'X'));
		assertFalse(MreTools.checkComplementaryMatch('a', 'x'));
		assertFalse(MreTools.checkComplementaryMatch('a', 'a'));
		assertFalse(MreTools.checkComplementaryMatch('a', 'c'));
		assertFalse(MreTools.checkComplementaryMatch('a', 'g'));
		assertFalse(MreTools.checkComplementaryMatch('a', 'A'));
		assertFalse(MreTools.checkComplementaryMatch('a', 'C'));
		assertFalse(MreTools.checkComplementaryMatch('a', 'G'));

		assertFalse(MreTools.checkComplementaryMatch('A', '-'));
	}

	@Test
	public void testC() {
		assertTrue(MreTools.checkComplementaryMatch('C', 'G'));
		assertTrue(MreTools.checkComplementaryMatch('C', 'g'));
		assertTrue(MreTools.checkComplementaryMatch('c', 'G'));
		assertTrue(MreTools.checkComplementaryMatch('c', 'g'));

		assertFalse(MreTools.checkComplementaryMatch('C', 'A'));
		assertFalse(MreTools.checkComplementaryMatch('C', 'C'));
		assertFalse(MreTools.checkComplementaryMatch('C', 'T'));
		assertFalse(MreTools.checkComplementaryMatch('C', 'U'));
	}

	@Test
	public void testG() {
		assertTrue(MreTools.checkComplementaryMatch('G', 'C'));
		assertTrue(MreTools.checkComplementaryMatch('G', 'c'));
		assertTrue(MreTools.checkComplementaryMatch('g', 'C'));
		assertTrue(MreTools.checkComplementaryMatch('g', 'c'));

		assertFalse(MreTools.checkComplementaryMatch('G', 'A'));
		assertFalse(MreTools.checkComplementaryMatch('G', 'T'));
		assertFalse(MreTools.checkComplementaryMatch('G', 'G'));
		assertFalse(MreTools.checkComplementaryMatch('G', 'U'));
	}

	@Test
	public void testT() {
		assertTrue(MreTools.checkComplementaryMatch('T', 'A'));
		assertTrue(MreTools.checkComplementaryMatch('T', 'a'));
		assertTrue(MreTools.checkComplementaryMatch('t', 'A'));
		assertTrue(MreTools.checkComplementaryMatch('t', 'a'));
		assertTrue(MreTools.checkComplementaryMatch('U', 'A'));
		assertTrue(MreTools.checkComplementaryMatch('U', 'a'));
		assertTrue(MreTools.checkComplementaryMatch('u', 'A'));
		assertTrue(MreTools.checkComplementaryMatch('u', 'a'));

		assertFalse(MreTools.checkComplementaryMatch('T', 'T'));
		assertFalse(MreTools.checkComplementaryMatch('T', 'C'));
		assertFalse(MreTools.checkComplementaryMatch('T', 'G'));
		assertFalse(MreTools.checkComplementaryMatch('T', 'U'));
	}

	@Test
	public void testConstants() {
		assertEquals(MreTools.MINCOMPENSATORYCONTINUOUSLENGTH, 4);
		assertEquals(MreTools.MINSEEDLENGTH, 8);
		assertEquals(MreTools.MIRNACOMPENSATORYSITESTART, 11);
	}
}
