package mirnator.structs.genbank;

public class SimpleGene {
	private String identifier;
	private String sequence;

	public SimpleGene(String identifier, String sequence) {
		this.identifier = identifier;
		this.sequence = sequence;
	}

	/**
	 * @return the identifier
	 */
	public String getIdentifier() {
		return identifier;
	}

	/**
	 * @return the sequence
	 */
	public String getSequence() {
		return sequence;
	}

	/**
	 * Returns the File in a Fasta Formated String representation
	 * 
	 * @return
	 */
	public String toFasta() {
		StringBuilder sb = new StringBuilder();
		sb.append(">" + this.identifier + "\n");
		int start = 0;
		int end = 60;
		while (end < this.sequence.length()) {
			sb.append(this.sequence.substring(start, end) + "\n");
			start += 60;
			end += 60;
		}
		sb.append(this.sequence.substring(start, this.sequence.length()) + "\n");
		return sb.toString();
	}
}