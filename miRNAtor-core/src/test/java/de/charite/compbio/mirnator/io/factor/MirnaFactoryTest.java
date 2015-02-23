/**
 * 
 */
package de.charite.compbio.mirnator.io.factor;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.charite.compbio.mirnator.exceptions.MisformedMirbaseHeaderException;
import de.charite.compbio.mirnator.reference.Mirna;

/**
 * 
 *
 * @author Marten Jäger <marten.jaeger@charite.de>
 *
 */
public class MirnaFactoryTest {
	Mirna testMirna;

	/**
	 * Test method for
	 * {@link de.charite.compbio.mirnator.io.factor.MirnaFactory#createMirna(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testCreateMirnaSuccess() {

		try {
			testMirna = new MirnaFactory().createMirna(">cel-miR-38-3p MIMAT0000009 Caenorhabditis elegans miR-38-3p",
					"UCACCGGGAGAAAAACUGGAGU");
			assertEquals(testMirna.getAccession(), "MIMAT0000009");
			assertEquals(testMirna.getFamily(), null);
			assertEquals(testMirna.getSpecies(), "cel");
			assertEquals(testMirna.getName(), "miR-38-3p");
			assertEquals(testMirna.getSequence(), "UCACCGGGAGAAAAACUGGAGU");
		} catch (MisformedMirbaseHeaderException e) {
			e.printStackTrace();
		}
	}

	@Test(expected = MisformedMirbaseHeaderException.class)
	public void testCreateMirnaFail() throws MisformedMirbaseHeaderException {
		testMirna = new MirnaFactory().createMirna(">cel-miR-38-3p MIMAT0000009 Caenorhabditis elegans miR-38-3p",
				"UCACCGGGAGAAAAACUGGAGU");
	}

	@Test(expected = MisformedMirbaseHeaderException.class)
	public void testCreateMirnaFail2() throws MisformedMirbaseHeaderException {
		testMirna = new MirnaFactory().createMirna(">", "UCACCGGGAGAAAAACUGGAGU");
	}

	@Test(expected = MisformedMirbaseHeaderException.class)
	public void testCreateMirnaFail3() throws MisformedMirbaseHeaderException {
		testMirna = new MirnaFactory().createMirna(null, "UCACCGGGAGAAAAACUGGAGU");
	}

}
