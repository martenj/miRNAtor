package de.charite.compbio.mirnator.reference;

import java.io.Serializable;

/**
 * This is the simplest possible sequence representation. Only consisting of an identifier and the corresponding
 * sequence.
 *
 * @author Marten Jäger <marten.jaeger@charite.de>
 *
 */
public class SequenceModel implements Serializable, Comparable<SequenceModel> {

	/** Class version (for serialization). */
	private static final long serialVersionUID = 1L;
	/** The sequence identifier. This ID has to be unique in the used dataset. */
	public final String accession;
	/** The sequence */
	public final String sequence;

	/**
	 * Creates a new simple {@link SequenceModel}.
	 * 
	 * @param id
	 *            unique ID
	 * @param sequence
	 *            sequence representation
	 */
	public SequenceModel(String accession, String sequence) {
		this.accession = accession;
		this.sequence = sequence;
	}

	/**
	 * Returns the (unique) sequence ID.
	 * 
	 * @return the id
	 */
	public String getId() {
		return accession;
	}

	/**
	 * Returns the sequence. (e.g. cDNA)
	 * 
	 * @return the sequence
	 */
	public String getSequence() {
		return sequence;
	}

	public String toFastaFormat() {
		return toFastaFormat(60);
	}

	public String toFastaFormat(int b) {
		StringBuilder res = new StringBuilder().append(toFastaHeader()).append("\n");
		int c = 0;
		while (c + b < sequence.length()) {
			res.append(sequence.substring(c, c + b)).append("\n");
			c += b;
		}
		res.append(sequence.substring(c));
		return res.toString();
	}

	protected String toFastaHeader() {
		return new StringBuilder().append(">").append(accession).toString();
	}

	@Override
	public int compareTo(SequenceModel o) {
		return this.accession.compareTo(o.accession);
	}
}
