/**
 * 
 */
package de.charite.compbio.mirnator.io.factor;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import de.charite.compbio.mirnator.constants.MREtype;
import de.charite.compbio.mirnator.exceptions.MisformedMirbaseHeaderException;
import de.charite.compbio.mirnator.reference.Mirna;
import de.charite.compbio.mirnator.reference.Mre;
import de.charite.compbio.mirnator.reference.SimpleTranscriptModel;

/**
 * 
 *
 * @author Marten Jäger <marten.jaeger@charite.de>
 *
 */
public class SingleBartelMreFactoryTest {
	Mirna mirna;
	SimpleTranscriptModel model;
	SingleBartelMreFactory mreFactory;

	@Before
	public void setup() {
		try {
			mirna = new MirnaFactory().createMirna(">cel-miR-38-3p MIMAT0000009 Caenorhabditis elegans miR-38-3p",
					"UCACCGGGAGAAAAACUGGAGU");
		} catch (MisformedMirbaseHeaderException e) {
			assert false;
		}

	}

	/**
	 * Test that there is a running valid index check.
	 */
	@Test
	public void testToSmallIndex() {
		model = new SimpleTranscriptModel("ID1", "NCCGGUGA");
		mreFactory = new SingleBartelMreFactory(mirna, model, -1);
		assertEquals(null, mreFactory.build());
		mreFactory = new SingleBartelMreFactory(mirna, model, 0);
		assertEquals(null, mreFactory.build());
		mreFactory = new SingleBartelMreFactory(mirna, model, 1);
		assertEquals(null, mreFactory.build());
		mreFactory = new SingleBartelMreFactory(mirna, model, 2);
		assertEquals(null, mreFactory.build());
		mreFactory = new SingleBartelMreFactory(mirna, model, 3);
		assertEquals(null, mreFactory.build());
		mreFactory = new SingleBartelMreFactory(mirna, model, 4);
		assertEquals(null, mreFactory.build());
		mreFactory = new SingleBartelMreFactory(mirna, model, 5);
		assertEquals(null, mreFactory.build());
		mreFactory = new SingleBartelMreFactory(mirna, model, 6);
		assertEquals(null, mreFactory.build());
	}

	/**
	 * Test for Bartel 7mer-A1 site<br>
	 * ....01234567<br>
	 * 5'..NCCGGUGA..3' mRNA<br>
	 * .....||||||:<br>
	 * 3'..GGGCCACU 5' miRNA<br>
	 * ....76543210<br>
	 */
	@Test
	public void test7merA1site() {
		model = new SimpleTranscriptModel("ID1", "NCCGGUGA");
		mreFactory = new SingleBartelMreFactory(mirna, model, 7);
		Mre mre = mreFactory.build();
		assertEquals(mirna, mre.mirna);
		assertEquals(model, mre.sequenceModel);
		assertEquals(1, mre.sequence_start);
		assertEquals(7, mre.sequence_end);
		assertEquals(MREtype.SEVEN_A1, mre.type);
		assertEquals(true, mre.hasA1site);
		assertEquals(false, mre.hasPos9UA);
		assertEquals(false, mre.hasCompensatorySite);
		assertEquals(null, mre.conservation);
		assertEquals(null, mre.free_energy);
	}

	/**
	 * Test for Bartel 7mer-A1 site + offset <br>
	 * ....01234567<br>
	 * 5'..NCCGGUGA..3' mRNA<br>
	 * .....||||||:<br>
	 * 3'..GGGCCACU 5' miRNA<br>
	 * ....76543210<br>
	 */
	@Test
	public void test7merA1sitePlusOffset() {
		model = new SimpleTranscriptModel("ID1", "NNNNNNNNNNNCCGGUGA");
		mreFactory = new SingleBartelMreFactory(mirna, model, 17);
		Mre mre = mreFactory.build();
		assertEquals(mirna, mre.mirna);
		assertEquals(model, mre.sequenceModel);
		assertEquals(11, mre.sequence_start);
		assertEquals(17, mre.sequence_end);
		assertEquals(MREtype.SEVEN_A1, mre.type);
		assertEquals(true, mre.hasA1site);
		assertEquals(false, mre.hasPos9UA);
		assertEquals(false, mre.hasCompensatorySite);
		assertEquals(null, mre.conservation);
		assertEquals(null, mre.free_energy);
	}

	/**
	 * Test for Bartel 8mer-A1 site<br>
	 * ....01234567<br>
	 * 5'..CCCGGUGA..3' mRNA<br>
	 * ....|||||||:<br>
	 * 3'..GGGCCACU 5' miRNA<br>
	 * ....76543210<br>
	 */
	@Test
	public void test8merA1site() {
		model = new SimpleTranscriptModel("ID1", "CCCGGUGA");
		mreFactory = new SingleBartelMreFactory(mirna, model, 7);
		Mre mre = mreFactory.build();
		assertEquals(mirna, mre.mirna);
		assertEquals(model, mre.sequenceModel);
		assertEquals(0, mre.sequence_start);
		assertEquals(7, mre.sequence_end);
		assertEquals(MREtype.EIGHT_A1, mre.type);
		assertEquals(true, mre.hasA1site);
		assertEquals(false, mre.hasPos9UA);
		assertEquals(false, mre.hasCompensatorySite);
		assertEquals(null, mre.conservation);
		assertEquals(null, mre.free_energy);
	}

	/**
	 * Test for Bartel 8mer-A1 site + offset<br>
	 * ....01234567<br>
	 * 5'..CCCGGUGA..3' mRNA<br>
	 * ....|||||||:<br>
	 * 3'..GGGCCACU 5' miRNA<br>
	 * ....76543210<br>
	 */
	@Test
	public void test8merA1sitePlusOffset() {
		model = new SimpleTranscriptModel("ID1", "NNNNNNNNNNCCCGGUGA");
		mreFactory = new SingleBartelMreFactory(mirna, model, 17);
		Mre mre = mreFactory.build();
		assertEquals(mirna, mre.mirna);
		assertEquals(model, mre.sequenceModel);
		assertEquals(10, mre.sequence_start);
		assertEquals(17, mre.sequence_end);
		assertEquals(MREtype.EIGHT_A1, mre.type);
		assertEquals(true, mre.hasA1site);
		assertEquals(false, mre.hasPos9UA);
		assertEquals(false, mre.hasCompensatorySite);
		assertEquals(null, mre.conservation);
		assertEquals(null, mre.free_energy);
	}

	/**
	 * Test for Bartel 7mer-m8 site<br>
	 * ....01234567<br>
	 * 5'..CCCGGUGN..3' mRNA<br>
	 * ....|||||||.<br>
	 * 3'..GGGCCACU 5' miRNA<br>
	 * ....76543210<br>
	 */
	@Test
	public void test7merM8site() {
		model = new SimpleTranscriptModel("ID1", "CCCGGUGN");
		mreFactory = new SingleBartelMreFactory(mirna, model, 7);
		Mre mre = mreFactory.build();
		assertEquals(mirna, mre.mirna);
		assertEquals(model, mre.sequenceModel);
		assertEquals(0, mre.sequence_start);
		assertEquals(7, mre.sequence_end);
		assertEquals(MREtype.SEVEN_M8, mre.type);
		assertEquals(false, mre.hasA1site);
		assertEquals(false, mre.hasPos9UA);
		assertEquals(false, mre.hasCompensatorySite);
		assertEquals(null, mre.conservation);
		assertEquals(null, mre.free_energy);
	}

	/**
	 * Test for Bartel 7mer-m8 site + U9<br>
	 * .......01234567<br>
	 * 5'..U/ACCCGGUGN..3' mRNA<br>
	 * .......|||||||.<br>
	 * 3'.....GGGCCACU 5' miRNA<br>
	 * .......76543210<br>
	 */
	@Test
	public void test7merM8siteU9() {
		model = new SimpleTranscriptModel("ID1", "UCCCGGUGN");
		mreFactory = new SingleBartelMreFactory(mirna, model, 8);
		Mre mre = mreFactory.build();
		assertEquals(mirna, mre.mirna);
		assertEquals(model, mre.sequenceModel);
		assertEquals(1, mre.sequence_start);
		assertEquals(8, mre.sequence_end);
		assertEquals(MREtype.SEVEN_M8, mre.type);
		assertEquals(false, mre.hasA1site);
		assertEquals(true, mre.hasPos9UA);
		assertEquals(false, mre.hasCompensatorySite);
		assertEquals(null, mre.conservation);
		assertEquals(null, mre.free_energy);
	}

	/**
	 * Test for Bartel 7mer-m8 site + A9<br>
	 * .......01234567<br>
	 * 5'..U/ACCCGGUGN..3' mRNA<br>
	 * .......|||||||.<br>
	 * 3'.....GGGCCACU 5' miRNA<br>
	 * .......76543210<br>
	 */
	@Test
	public void test7merM8siteA9() {
		model = new SimpleTranscriptModel("ID1", "ACCCGGUGN");
		mreFactory = new SingleBartelMreFactory(mirna, model, 8);
		Mre mre = mreFactory.build();
		assertEquals(mirna, mre.mirna);
		assertEquals(model, mre.sequenceModel);
		assertEquals(1, mre.sequence_start);
		assertEquals(8, mre.sequence_end);
		assertEquals(MREtype.SEVEN_M8, mre.type);
		assertEquals(false, mre.hasA1site);
		assertEquals(true, mre.hasPos9UA);
		assertEquals(false, mre.hasCompensatorySite);
		assertEquals(null, mre.conservation);
		assertEquals(null, mre.free_energy);
	}

	/**
	 * Test for Bartel 7mer-m8 site + no comp 9 site<br>
	 * .......01234567<br>
	 * 5'..U/ACCCGGUGN..3' mRNA<br>
	 * .......|||||||.<br>
	 * 3'.....GGGCCACU 5' miRNA<br>
	 * .......76543210<br>
	 */
	@Test
	public void test7merM8siteNoComp9() {
		model = new SimpleTranscriptModel("ID1", "GCCCGGUGN");
		mreFactory = new SingleBartelMreFactory(mirna, model, 8);
		Mre mre = mreFactory.build();
		assertEquals(mirna, mre.mirna);
		assertEquals(model, mre.sequenceModel);
		assertEquals(1, mre.sequence_start);
		assertEquals(8, mre.sequence_end);
		assertEquals(MREtype.SEVEN_M8, mre.type);
		assertEquals(false, mre.hasA1site);
		assertEquals(false, mre.hasPos9UA);
		assertEquals(false, mre.hasCompensatorySite);
		assertEquals(null, mre.conservation);
		assertEquals(null, mre.free_energy);
	}

	/**
	 * Test for Bartel 7mer-m8 site + offset + Comp9UA<br>
	 * .......01234567<br>
	 * 5'..U/ACCCGGUGN..3' mRNA<br>
	 * .......|||||||.<br>
	 * 3'.....GGGCCACU 5' miRNA<br>
	 * .......76543210<br>
	 */
	@Test
	public void test7merM8sitePlusOffsetUA9() {
		model = new SimpleTranscriptModel("ID1", "NNNNNNNNNNCCCGGUGN");
		mreFactory = new SingleBartelMreFactory(mirna, model, 17);
		Mre mre = mreFactory.build();
		assertEquals(mirna, mre.mirna);
		assertEquals(model, mre.sequenceModel);
		assertEquals(10, mre.sequence_start);
		assertEquals(17, mre.sequence_end);
		assertEquals(MREtype.SEVEN_M8, mre.type);
		assertEquals(false, mre.hasA1site);
		assertEquals(false, mre.hasPos9UA);
		assertEquals(false, mre.hasCompensatorySite);
		assertEquals(null, mre.conservation);
		assertEquals(null, mre.free_energy);
	}

	/**
	 * Test for Bartel 6mer site<br>
	 * ....01234567<br>
	 * 5'..NCCGGUGN..3' mRNA<br>
	 * .....||||||.<br>
	 * 3'..GGGCCACU 5' miRNA<br>
	 * ....76543210<br>
	 */
	@Test
	public void test6merSite() {
		model = new SimpleTranscriptModel("ID1", "NCCGGUGN");
		mreFactory = new SingleBartelMreFactory(mirna, model, 7);
		Mre mre = mreFactory.build();
		assertEquals(mirna, mre.mirna);
		assertEquals(model, mre.sequenceModel);
		assertEquals(1, mre.sequence_start);
		assertEquals(7, mre.sequence_end);
		assertEquals(MREtype.SIX, mre.type);
		assertEquals(false, mre.hasA1site);
		assertEquals(false, mre.hasPos9UA);
		assertEquals(false, mre.hasCompensatorySite);
		assertEquals(null, mre.conservation);
		assertEquals(null, mre.free_energy);
	}

	/**
	 * Test for Bartel 6mer site + offset<br>
	 * ....01234567<br>
	 * 5'..NCCGGUGN..3' mRNA<br>
	 * .....||||||.<br>
	 * 3'..GGGCCACU 5' miRNA<br>
	 * ....76543210<br>
	 */
	@Test
	public void test6merSitePlusOffset() {
		model = new SimpleTranscriptModel("ID1", "NNNNNNNNNNNCCGGUGN");
		mreFactory = new SingleBartelMreFactory(mirna, model, 17);
		Mre mre = mreFactory.build();
		assertEquals(mirna, mre.mirna);
		assertEquals(model, mre.sequenceModel);
		assertEquals(11, mre.sequence_start);
		assertEquals(17, mre.sequence_end);
		assertEquals(MREtype.SIX, mre.type);
		assertEquals(false, mre.hasA1site);
		assertEquals(false, mre.hasPos9UA);
		assertEquals(false, mre.hasCompensatorySite);
		assertEquals(null, mre.conservation);
		assertEquals(null, mre.free_energy);
	}

	/**
	 * Test for Bartel 6mer Offset site<br>
	 * ....01234567<br>
	 * 5'..CCCGGUCN..3' mRNA<br>
	 * ....||||||..<br>
	 * 3'..GGGCCACU 5' miRNA<br>
	 * ....76543210<br>
	 */
	@Test
	public void test6merOffsetSite() {
		model = new SimpleTranscriptModel("ID1", "CCCGGUCN");
		mreFactory = new SingleBartelMreFactory(mirna, model, 7);
		Mre mre = mreFactory.build();
		assertEquals(mirna, mre.mirna);
		assertEquals(model, mre.sequenceModel);
		assertEquals(0, mre.sequence_start);
		assertEquals(6, mre.sequence_end);
		assertEquals(MREtype.OFFSET_SIX, mre.type);
		assertEquals(false, mre.hasA1site);
		assertEquals(false, mre.hasPos9UA);
		assertEquals(false, mre.hasCompensatorySite);
		assertEquals(null, mre.conservation);
		assertEquals(null, mre.free_energy);
	}

	/**
	 * Test for Bartel 6mer Offset site + offset<br>
	 * ....01234567<br>
	 * 5'..CCCGGUCN..3' mRNA<br>
	 * ....||||||..<br>
	 * 3'..GGGCCACU 5' miRNA<br>
	 * ....76543210<br>
	 */
	@Test
	public void test6merSiteOffsetPlusOffset() {
		model = new SimpleTranscriptModel("ID1", "NNNNNNNNNNCCCGGUCN");
		mreFactory = new SingleBartelMreFactory(mirna, model, 17);
		Mre mre = mreFactory.build();
		assertEquals(mirna, mre.mirna);
		assertEquals(model, mre.sequenceModel);
		assertEquals(10, mre.sequence_start);
		assertEquals(16, mre.sequence_end);
		assertEquals(MREtype.OFFSET_SIX, mre.type);
		assertEquals(false, mre.hasA1site);
		assertEquals(false, mre.hasPos9UA);
		assertEquals(false, mre.hasCompensatorySite);
		assertEquals(null, mre.conservation);
		assertEquals(null, mre.free_energy);
	}

	/**
	 * Test for Bartel 8mer site + compensatory<br>
	 * ...01234567..890123456<br>
	 * 5'.CCAGUUUU--ACCCGGUGA..3' mRNA<br>
	 * ...:::||||:..:|||||||:<br>
	 * 3' GGUCAAAAAGAGGGCCACU 5' miRNA<br>
	 * ...9876543210987654321<br>
	 */
	@Test
	public void test8merSitePlusCompensarory1() {
		model = new SimpleTranscriptModel("ID1", "CCAGUUUUACCCGGUGA");
		mreFactory = new SingleBartelMreFactory(mirna, model, 16);
		Mre mre = mreFactory.build();
		assertEquals(mirna, mre.mirna);
		assertEquals(model, mre.sequenceModel);
		assertEquals(9, mre.sequence_start);
		assertEquals(16, mre.sequence_end);
		assertEquals(MREtype.EIGHT_A1, mre.type);
		assertEquals(true, mre.hasA1site);
		assertEquals(true, mre.hasPos9UA);
		assertEquals(true, mre.hasCompensatorySite);
		assertEquals(null, mre.conservation);
		assertEquals(null, mre.free_energy);
	}

	/**
	 * Test for Bartel 8mer site + compensatory<br>
	 * ...01234567.8901234567<br>
	 * 5'.CCAGUUUU-GACCCGGUGA..3' mRNA<br>
	 * ...:::||||:..:|||||||:<br>
	 * 3' GGUCAAAAAGAGGGCCACU 5' miRNA<br>
	 * ...9876543210987654321<br>
	 */
	@Test
	public void test8merSitePlusCompensarory2() {
		model = new SimpleTranscriptModel("ID1", "CCAGUUUUGACCCGGUGA");
		mreFactory = new SingleBartelMreFactory(mirna, model, 17);
		Mre mre = mreFactory.build();
		assertEquals(mirna, mre.mirna);
		assertEquals(model, mre.sequenceModel);
		assertEquals(10, mre.sequence_start);
		assertEquals(17, mre.sequence_end);
		assertEquals(MREtype.EIGHT_A1, mre.type);
		assertEquals(true, mre.hasA1site);
		assertEquals(true, mre.hasPos9UA);
		assertEquals(true, mre.hasCompensatorySite);
		assertEquals(null, mre.conservation);
		assertEquals(null, mre.free_energy);
	}

	/**
	 * Test for Bartel 8mer site + compensatory<br>
	 * ...0123456789012345678<br>
	 * 5'.CCAGUUUUGGACCCGGUGA..3' mRNA<br>
	 * ...:::||||:..:|||||||:<br>
	 * 3' GGUCAAAAAGAGGGCCACU 5' miRNA<br>
	 * ...9876543210987654321<br>
	 */
	@Test
	public void test8merSitePlusCompensarory3() {
		model = new SimpleTranscriptModel("ID1", "CCAGUUUUGGACCCGGUGA");
		mreFactory = new SingleBartelMreFactory(mirna, model, 18);
		Mre mre = mreFactory.build();
		assertEquals(mirna, mre.mirna);
		assertEquals(model, mre.sequenceModel);
		assertEquals(11, mre.sequence_start);
		assertEquals(18, mre.sequence_end);
		assertEquals(MREtype.EIGHT_A1, mre.type);
		assertEquals(true, mre.hasA1site);
		assertEquals(true, mre.hasPos9UA);
		assertEquals(true, mre.hasCompensatorySite);
		assertEquals(null, mre.conservation);
		assertEquals(null, mre.free_energy);
	}
}
