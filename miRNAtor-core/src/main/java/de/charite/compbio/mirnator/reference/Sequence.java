package de.charite.compbio.mirnator.reference;

/**
 * This is the simplest possible sequence representation. Only consisting of an identifier and the corresponding
 * sequence.
 *
 * @author Marten Jäger <marten.jaeger@charite.de>
 *
 */
public class Sequence {

	/** Unique sequence identifier */
	String id;
	/** The sequence */
	String sequence;

	/**
	 * plain contructor.
	 */
	public Sequence() {
	}

	/**
	 * Creates a new simple {@link Sequence}.
	 * 
	 * @param id
	 *            unique ID
	 * @param sequence
	 *            sequence representation
	 */
	public Sequence(String id, String sequence) {
		this.id = id;
		this.sequence = sequence;
	}

	/**
	 * Returns the (unique) sequence ID.
	 * 
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the sequence ID. This ID has to be unique in the used dataset.
	 * 
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Returns the sequence.
	 * 
	 * @return the sequence
	 */
	public String getSequence() {
		return sequence;
	}

	/**
	 * Sets the sequence.
	 * 
	 * @param sequence
	 *            the sequence to set
	 */
	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

}
