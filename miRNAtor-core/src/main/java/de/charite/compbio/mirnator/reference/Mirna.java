package de.charite.compbio.mirnator.reference;

/**
 * Simple bean representation of a {@link Mirna}
 * 
 *
 * @author Marten Jäger <marten.jaeger@charite.de>
 *
 */
public class Mirna extends SequenceModel {
	private String name;
	// private String accession;
	// private String sequence;
	private String species;
	private MirnaFamily family;

	public Mirna(String accession, String sequence) {
		this(accession, sequence, null, null, null);
	}

	public Mirna(String accession, String sequence, String name, String species, MirnaFamily family) {
		super(accession, sequence);
		this.name = name;
		this.species = species;
		this.family = family;
	}

	/**
	 * Get the miRNA name (e.g. miR-123).
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the miRNA name (e.g. miR-123).
	 * 
	 * @param name
	 *            the name to set
	 */
	public void setName(String mirna) {
		this.name = mirna;
	}

	/**
	 * Set the miRNA accession number (e.g. MIMAT0012112).
	 * 
	 * @return the accession
	 */
	public String getAccession() {
		return accession;
	}

	// /**
	// * Set the miRNA accession number (e.g. MIMAT0012112).
	// *
	// * @param accession
	// * the accession to set
	// */
	// public void setAccession(String accession) {
	// this.accession = accession;
	// }

	/**
	 * Set the miRNA sequence (e.g. CUGUACAGCCUCCUAGCUUUCC).
	 * 
	 * @return the sequence
	 */
	public String getSequence() {
		return sequence;
	}

	// /**
	// * Set the miRNA sequence (e.g. CUGUACAGCCUCCUAGCUUUCC).
	// *
	// * @param sequence
	// * the sequence to set
	// */
	// public void setSequence(String sequence) {
	// this.sequence = sequence;
	// }

	/**
	 * Get the miRNA species in 3 letter code (e.g.hsa - homo sapien).
	 * 
	 * @return the species
	 */
	public String getSpecies() {
		return species;
	}

	/**
	 * Set the miRNA species in 3 letter code (e.g. hsa - homo sapien).
	 * 
	 * @param species
	 *            the species to set
	 */
	public void setSpecies(String species) {
		this.species = species;
	}

	/**
	 * Get the miRNA-family name (e.g. miR-15).
	 * 
	 * @return the family
	 */
	public MirnaFamily getFamily() {
		return family;
	}

	/**
	 * Set the miRNA-family name (e.g. miR-15).
	 * 
	 * @param family
	 *            the family to set
	 */
	public void setFamily(MirnaFamily family) {
		this.family = family;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Mirna [accession=" + accession + ", family=" + family + ", name=" + name + ", sequence=" + sequence
				+ ", species=" + species + "]";
	}

	/**
	 * Returns the reverse of the seed sequence (eg. 5'AACGUG 3' --> 5'CACGUU 3') or <code>null</code> if the sequence
	 * is to short or itself <code>null</code>.
	 * 
	 * @return the resverse seed sequence or <code>null</code> if the sequence is to short or itself <code>null</code>
	 */
	public StringBuilder getReverseSeed() {
		if (this.sequence == null || this.sequence.length() < 8)
			return null;
		else {
			StringBuilder revseed = new StringBuilder(sequence.subSequence(0, 8)).reverse();
			for (int i = 0; i < 8; i++) {
				if (revseed.charAt(i) == 'A') {
					revseed.setCharAt(i, 'U');
				} else if (revseed.charAt(i) == 'T') {
					revseed.setCharAt(i, 'A');
				} else if (revseed.charAt(i) == 'U') {
					revseed.setCharAt(i, 'A');
				} else if (revseed.charAt(i) == 'G') {
					revseed.setCharAt(i, 'C');
				} else if (revseed.charAt(i) == 'C') {
					revseed.setCharAt(i, 'G');
				}
			}
			return revseed;
		}

	}

}
