package mirnator.mirnapredict;

public class Sequence {

	String id;
	String sequence;

	public Sequence() {
	}

	public Sequence(String id, String sequence) {
		this.id = id;
		this.sequence = sequence;
	}

	/**
	 * Returns the (unique) sequence ID (an integer).
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
