/**
 * 
 */
package de.charite.compbio.mirnator.reference;

/**
 * This is a simple class to store transcript informations (transcript id, gene id, sequence, cds) TODO add exon
 * informations
 * 
 * @author mjaeger
 *
 */
public class SimpleTranscriptModel extends SequenceModel {

	/** The gene identifier (e.g entrez gene ID) */
	public final String geneID;
	/** a biotype maybe will be something like CODING,NONCODING etc. */
	public final String biotype;
	/** The '0'-based start of the 5'UTR. This will be '-1' for noncoding transcripts. */
	public final int fivePrimeUtrStart;
	/** The '0'-based start of the coding sequence/CDS. This will be '-1' for noncoding transcripts. */
	public final int cdsStart;
	/** The '0'-based start of the 3'UTR. This will be '0' for noncoding transcripts. */
	public final int threePrimeUtrStart;

	public SimpleTranscriptModel(String id, String sequence) {
		this(id, sequence, null, null);
	}

	public SimpleTranscriptModel(String id, String sequence, String geneID, String biotype) {
		this(id, sequence, null, null, -1, 0);
	}

	public SimpleTranscriptModel(String id, String sequence, String geneID, String biotype, int cdsStart,
			int threePrimeUtrStart) {
		super(id, sequence);
		this.geneID = geneID;
		this.biotype = biotype;
		this.fivePrimeUtrStart = cdsStart <= 0 ? -1 : 0;
		this.threePrimeUtrStart = threePrimeUtrStart;
		this.cdsStart = cdsStart;
	}

	// /**
	// * @return the biotype
	// */
	// public String getBiotype() {
	// return biotype;
	// }
	//
	// /**
	// * @param biotype
	// * the biotype to set
	// */
	// public void setBiotype(String biotype) {
	// this.biotype = biotype;
	// }
	//
	// /**
	// * The gene ID the transcript origins from (e.g. ENSMUSG00000020333)
	// *
	// * @return the geneID
	// */
	// public String getGeneID() {
	// return geneID;
	// }
	//
	// /**
	// * Set the gene ID the transcript origins from (e.g. ENSMUSG00000020333)
	// *
	// * @param geneID
	// * the geneID to set
	// */
	// public void setGeneID(String geneID) {
	// this.geneID = geneID;
	// }
	//
	// /**
	// * The transcript CDS sequence.
	// *
	// * @return the sequence
	// */
	// public String getCdsSequence() {
	// return sequence.substring(getCdsStart(), getCdsEnd());
	// }
	//
	// /**
	// * The start position of the CDS (including).<br>
	// * If no values was assigned '-1' will be returned.<br>
	// * Remember: the index is '0'-based.
	// *
	// * @return the cdsStart
	// */
	// public int getCdsStart() {
	// return cdsStart;
	// }
	//
	// /**
	// * Set the CDS start position (including).<br>
	// * Remember: the index is '0'-based.
	// *
	// * @param cdsStart
	// * the cdsStart to set
	// */
	// public void setCdsStart(int cdsStart) {
	// this.cdsStart = cdsStart;
	// }
	//
	// /**
	// * If no values was assigned '-1' will be returned.<br>
	// *
	// * @return the cdsEnd
	// */
	// public int getCdsEnd() {
	// return cdsEnd;
	// }
	//
	// /**
	// * @param cdsEnd
	// * the cdsEnd to set
	// */
	// public void setCdsEnd(int cdsEnd) {
	// this.cdsEnd = cdsEnd;
	// }
	//
	// /**
	// * The start position of the 3'UTR (including).<br>
	// * If no values was assigned '-1' will be returned.<br>
	// * Remember: the index is '0'-based.
	// *
	// * @return the threePrimeUtrStart
	// */
	// public int getThreePrimeUtrStart() {
	// return threePrimeUtrStart;
	// }
	//
	// /**
	// * Set the 3'UTR start position (including).<br>
	// * Remember: the index is '0'-based.
	// *
	// * @param threePrimeUtrStart
	// * the threePrimeUtrStart to set
	// */
	// public void setThreePrimeUtrStart(int threePrimeUtrStart) {
	// this.threePrimeUtrStart = threePrimeUtrStart;
	// }
	//
	// /**
	// * If no values was assigned '-1' will be returned.<br>
	// *
	// * @return the threePrimeUtrEnd
	// */
	// public int getThreePrimeUtrEnd() {
	// return threePrimeUtrEnd;
	// }
	//
	// /**
	// * Set the 3'UTR end position (excluding).<br>
	// *
	// * @param threePrimeUtrEnd
	// * the threePrimeUtrEnd to set
	// */
	// public void setThreePrimeUtrEnd(int threePrimeUtrEnd) {
	// this.threePrimeUtrEnd = threePrimeUtrEnd;
	// }
	//
	// /**
	// * Get the 5'UTR start position (including).<br>
	// * If no values was assigned '-1' will be returned.<br>
	// *
	// * @return the fivePrimeUtrStart
	// */
	// public int getFivePrimeUtrStart() {
	// return fivePrimeUtrStart;
	// }
	//
	// /**
	// * Set the 5'UTR start position (including).<br>
	// *
	// * @param fivePrimeUtrStart
	// * the fivePrimeUtrStart to set
	// */
	// public void setFivePrimeUtrStart(int fivePrimeUtrStart) {
	// this.fivePrimeUtrStart = fivePrimeUtrStart;
	// }
	//
	// /**
	// * Get the 5'UTR end position (excluding).<br>
	// * If no values was assigned '-1' will be returned.<br>
	// *
	// * @return the fivePrimeUtrEnd
	// */
	// public int getFivePrimeUtrEnd() {
	// return fivePrimeUtrEnd;
	// }
	//
	// /**
	// * Set the 5'UTR end position (excluding).<br>
	// *
	// * @param fivePrimeUtrEnd
	// * the fivePrimeUtrEnd to set
	// */
	// public void setFivePrimeUtrEnd(int fivePrimeUtrEnd) {
	// this.fivePrimeUtrEnd = fivePrimeUtrEnd;
	// }

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
