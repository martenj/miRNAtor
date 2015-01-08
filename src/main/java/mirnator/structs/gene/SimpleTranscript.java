/**
 * 
 */
package mirnator.structs.gene;

import mirnator.mirnapredict.Sequence;

/**
 * This is a simple class to store transcript informations (transcipt id, gene id, sequence, cds) TODO add exon
 * informations
 * 
 * @author mjaeger
 *
 */
public class SimpleTranscript extends Sequence {
	// private String id;
	private String geneID;
	private String sequence;
	private String biotype;
	private int fivePrimeUtrStart = -1;
	private int cdsStart = -1;
	private int threePrimeUtrStart = -1;
	private int fivePrimeUtrEnd = -1;
	private int cdsEnd = -1;

	public SimpleTranscript(String id, String sequence) {
		super(id, sequence);
	}

	public SimpleTranscript() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the biotype
	 */
	public String getBiotype() {
		return biotype;
	}

	/**
	 * @param biotype
	 *            the biotype to set
	 */
	public void setBiotype(String biotype) {
		this.biotype = biotype;
	}

	private int threePrimeUtrEnd = -1;

	// /**
	// * The transcript ID (e.g. ENSMUST00000000145)
	// * @return the transcriptID
	// */
	// public String getTranscriptID() {
	// return super.id;
	// }
	// /**
	// * Set the transcript ID (e.g. ENSMUST00000000145)
	// * @param transcriptID the transcriptID to set
	// */
	// public void setTranscriptID(String transcriptID) {
	// this.id = transcriptID;
	// }
	/**
	 * The gene ID the transcript origins from (e.g. ENSMUSG00000020333)
	 * 
	 * @return the geneID
	 */
	public String getGeneID() {
		return geneID;
	}

	/**
	 * Set the gene ID the transcript origins from (e.g. ENSMUSG00000020333)
	 * 
	 * @param geneID
	 *            the geneID to set
	 */
	public void setGeneID(String geneID) {
		this.geneID = geneID;
	}

	/**
	 * The transcript sequence.
	 * 
	 * @return the sequence
	 */
	public String getSequence() {
		return sequence;
	}

	/**
	 * The transcript CDS sequence.
	 * 
	 * @return the sequence
	 */
	public String getCdsSequence() {
		return sequence.substring(getCdsStart(), getCdsEnd());
	}

	/**
	 * Set the transcript sequence.
	 * 
	 * @param sequence
	 *            the sequence to set
	 */
	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	/**
	 * The start position of the CDS (including).<br>
	 * If no values was assigned '-1' will be returned.<br>
	 * Remember: the index is '0'-based.
	 * 
	 * @return the cdsStart
	 */
	public int getCdsStart() {
		return cdsStart;
	}

	/**
	 * Set the CDS start position (including).<br>
	 * Remember: the index is '0'-based.
	 * 
	 * @param cdsStart
	 *            the cdsStart to set
	 */
	public void setCdsStart(int cdsStart) {
		this.cdsStart = cdsStart;
	}

	/**
	 * If no values was assigned '-1' will be returned.<br>
	 * 
	 * @return the cdsEnd
	 */
	public int getCdsEnd() {
		return cdsEnd;
	}

	/**
	 * @param cdsEnd
	 *            the cdsEnd to set
	 */
	public void setCdsEnd(int cdsEnd) {
		this.cdsEnd = cdsEnd;
	}

	/**
	 * The start position of the 3'UTR (including).<br>
	 * If no values was assigned '-1' will be returned.<br>
	 * Remember: the index is '0'-based.
	 * 
	 * @return the threePrimeUtrStart
	 */
	public int getThreePrimeUtrStart() {
		return threePrimeUtrStart;
	}

	/**
	 * Set the 3'UTR start position (including).<br>
	 * Remember: the index is '0'-based.
	 * 
	 * @param threePrimeUtrStart
	 *            the threePrimeUtrStart to set
	 */
	public void setThreePrimeUtrStart(int threePrimeUtrStart) {
		this.threePrimeUtrStart = threePrimeUtrStart;
	}

	/**
	 * If no values was assigned '-1' will be returned.<br>
	 * 
	 * @return the threePrimeUtrEnd
	 */
	public int getThreePrimeUtrEnd() {
		return threePrimeUtrEnd;
	}

	/**
	 * Set the 3'UTR end position (excluding).<br>
	 * 
	 * @param threePrimeUtrEnd
	 *            the threePrimeUtrEnd to set
	 */
	public void setThreePrimeUtrEnd(int threePrimeUtrEnd) {
		this.threePrimeUtrEnd = threePrimeUtrEnd;
	}

	/**
	 * Get the 5'UTR start position (including).<br>
	 * If no values was assigned '-1' will be returned.<br>
	 * 
	 * @return the fivePrimeUtrStart
	 */
	public int getFivePrimeUtrStart() {
		return fivePrimeUtrStart;
	}

	/**
	 * Set the 5'UTR start position (including).<br>
	 * 
	 * @param fivePrimeUtrStart
	 *            the fivePrimeUtrStart to set
	 */
	public void setFivePrimeUtrStart(int fivePrimeUtrStart) {
		this.fivePrimeUtrStart = fivePrimeUtrStart;
	}

	/**
	 * Get the 5'UTR end position (excluding).<br>
	 * If no values was assigned '-1' will be returned.<br>
	 * 
	 * @return the fivePrimeUtrEnd
	 */
	public int getFivePrimeUtrEnd() {
		return fivePrimeUtrEnd;
	}

	/**
	 * Set the 5'UTR end position (excluding).<br>
	 * 
	 * @param fivePrimeUtrEnd
	 *            the fivePrimeUtrEnd to set
	 */
	public void setFivePrimeUtrEnd(int fivePrimeUtrEnd) {
		this.fivePrimeUtrEnd = fivePrimeUtrEnd;
	}

	/**
	 * Returns the File in a Fasta Formated String representation. The sequence will have newlines every 60 letters.
	 * 
	 * @return - transcript in FastA format
	 */
	public String toFasta() {
		StringBuilder sb = new StringBuilder();
		sb.append(">" + this.geneID + "|" + super.getId() + "|" + this.biotype + "\n");
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
