package de.charite.compbio.mirnator.reference;

/**
 * This is the simplest possible sequence representation. Only consisting of an identifier and the corresponding
 * sequence.
 *
 * @author Marten Jäger <marten.jaeger@charite.de>
 *
 */
public class SequenceModel {

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
	 * Returns the sequence.
	 * 
	 * @return the sequence
	 */
	public String getSequence() {
		return sequence;
	}

}
