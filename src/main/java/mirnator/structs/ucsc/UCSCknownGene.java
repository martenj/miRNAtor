/**
 * 
 */
package mirnator.structs.ucsc;

/**
 * A Simple Class to store the known Genes of the UCSCknownGeneParser
 * 
 * @author mjaeger
 *
 */
// public class UCSCknownGene implements Comparable<UCSCknownGene>{
public class UCSCknownGene implements UCSCDatabaseEntry {

	private String kgName;
	private String chrom;
	private boolean strand;
	private int txStart;
	private int txEnd;
	private int cdsStart;
	private int cdsEnd;
	private int exonCount;
	private int[] exonStarts;
	private int[] exonEnds;
	private String proteinID;

	/**
	 * Returns the length of the 3'UTR of this knowngene.
	 * 
	 * @return3'UTR length
	 */
	public int get3utrLength() {
		int length = 0;
		for (int i = 0; i < exonCount; i++) {
			if (exonEnds[i] > cdsEnd) {
				if (exonStarts[i] < cdsEnd)
					length += exonEnds[i] - cdsEnd;
				else
					length += exonEnds[i] - exonStarts[i] + 1;
			}
		}
		return length;
	}

	/**
	 * Returns the length of the 5'UTR otf this knowngene.
	 * 
	 * @return 5'UTR length
	 */
	public int get5utrLength() {
		int length = 0;
		for (int i = 0; i < exonCount; i++) {
			if (exonStarts[i] < cdsStart) {
				if (exonEnds[i] < cdsStart)
					length += exonEnds[i] - exonStarts[i] + 1;
				else {
					length += cdsStart - exonStarts[i];
					break;
				}
			}
		}
		return length;

	}

	/**
	 * Returns the length of the CDS of this knowngene.
	 * 
	 * @return CDS length TODO check if there are index errors!!!
	 */
	public int getCdsLength() {
		int length = 0;
		for (int i = 0; i < exonCount; i++) {
			if (exonStarts[i] > cdsEnd) {
				break;
			} // E
			if (exonEnds[i] < cdsStart) {
				continue;
			} // F

			if (exonStarts[i] <= cdsStart && exonEnds[i] <= cdsEnd) {
				length += exonEnds[i] - cdsStart + 1;
			} // D
			if (cdsStart <= exonStarts[i] && exonEnds[i] <= cdsEnd) {
				length += exonEnds[i] - exonStarts[i] + 1;
			} // B
			if (exonStarts[i] <= cdsStart && cdsEnd <= exonEnds[i]) {
				length += cdsEnd - cdsStart + 1;
			} // C
			if (cdsStart <= exonStarts[i] && cdsEnd <= exonEnds[i]) {
				length += cdsEnd - exonStarts[i] + 1;
			} // A

		}

		return length;

	}

	/**
	 * @return the kgName
	 */
	public String getKgName() {
		return kgName;
	}

	/**
	 * @param kgName
	 *            the kgName to set
	 */
	public void setKgName(String kgName) {
		this.kgName = kgName;
	}

	/**
	 * @return the chrom
	 */
	public String getChrom() {
		return chrom;
	}

	/**
	 * @param chrom
	 *            the chrom to set
	 */
	public void setChrom(String chrom) {
		this.chrom = chrom;
	}

	/**
	 * @return the txStart
	 */
	public int getTxStart() {
		return txStart;
	}

	/**
	 * @param txStart
	 *            the txStart to set
	 */
	public void setTxStart(int txStart) {
		this.txStart = txStart;
	}

	/**
	 * @return the txEnd
	 */
	public int getTxEnd() {
		return txEnd;
	}

	/**
	 * @param txEnd
	 *            the txEnd to set
	 */
	public void setTxEnd(int txEnd) {
		this.txEnd = txEnd;
	}

	/**
	 * @return the cdsStart
	 */
	public int getCdsStart() {
		return cdsStart;
	}

	/**
	 * @param cdsStart
	 *            the cdsStart to set
	 */
	public void setCdsStart(int cdsStart) {
		this.cdsStart = cdsStart;
	}

	/**
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
	 * @return the exonCount
	 */
	public int getExonCount() {
		return exonCount;
	}

	/**
	 * @param exonCount
	 *            the exonCount to set
	 */
	public void setExonCount(int exonCount) {
		this.exonCount = exonCount;
	}

	/**
	 * @return the exonStarts
	 */
	public int[] getExonStarts() {
		return exonStarts;
	}

	/**
	 * @return the exonEnds
	 */
	public String getExonStartssLine() {
		return intArraytoString(exonStarts);
	}

	/**
	 * @param exonStarts
	 *            the exonStarts to set
	 */
	public void setExonStarts(int[] exonStarts) {
		this.exonStarts = exonStarts;
	}

	/**
	 * @return the exonEnds
	 */
	public int[] getExonEnds() {
		return exonEnds;
	}

	/**
	 * @return the exonEnds
	 */
	public String getExonEndsLine() {
		return intArraytoString(exonEnds);
	}

	/**
	 * @param exonEnds
	 *            the exonEnds to set
	 */
	public void setExonEnds(int[] exonEnds) {
		this.exonEnds = exonEnds;
	}

	/**
	 * returns the Strand: <code>true</code> for the forward (+) strand and <code>false</code> for the (-) backward
	 * strand.
	 * 
	 * @return the strand
	 */
	public boolean getStrand() {
		return strand;
	}

	/**
	 * @param strand
	 *            the strand to set
	 */
	public void setStrand(boolean strand) {
		this.strand = strand;
	}

	// @Override
	// public int compareTo(UCSCknownGene o) {
	// if(this.txStart < o.txStart)
	// return -1;
	// else if(this.txStart == o.txStart)
	// return 0;
	// else
	// return 1;
	// }

	/**
	 * @param proteinID
	 *            the proteinID to set
	 */
	public void setProteinID(String proteinID) {
		this.proteinID = proteinID;
	}

	/**
	 * @return the proteinID
	 */
	public String getProteinID() {
		return proteinID;
	}

	private String intArraytoString(int[] exonStarts2) {
		String result = "";
		for (int i : exonStarts2) {
			result += i + ",";
		}
		return result;
	}

	@Override
	public String toString() {
		return getClass().getName() + "[name=" + getKgName() + ",chrom=" + getChrom() + ",strand="
				+ (getStrand() ? "+" : "-") + ",txStart=" + getTxStart() + ",txEnd=" + getTxEnd() + ",cdsStart="
				+ getCdsStart() + ",cdsEnd=" + getCdsEnd() + ",exonCount=" + getExonCount() + ",exonStarts="
				+ intArraytoString(getExonStarts()) + ",exonEnds=" + intArraytoString(getExonEnds()) + ",proteinID="
				+ getProteinID() + ",alignID=" + getKgName() + "]"; // TODO Auto-generated method stub

	}

	public String toLine() {
		return getKgName() + "\t" + getChrom() + "\t" + (getStrand() ? "+" : "-") + "\t" + getTxStart() + "\t"
				+ getTxEnd() + "\t" + getCdsStart() + "\t" + getCdsEnd() + "\t" + getExonCount() + "\t"
				+ intArraytoString(getExonStarts()) + "\t" + intArraytoString(getExonEnds()) + "\t" + getProteinID()
				+ "\t" + getKgName();

	}

	/**
	 * Returns the CDS start on the mRNA sequence not Chromosomal.
	 * 
	 * @return mRNA CDS start
	 */
	public int getLocalCdsStart() {
		int start = 0;
		for (int i = 0; i < exonCount; i++) {
			// System.out.println("cdsStart: "+this.cdsStart);
			// System.out.println("cdsEnd: "+this.cdsEnd);
			// System.out.println("exonStart: "+exonStarts[i]);
			// System.out.println("exonEnd: "+exonEnds[i]);

			if (this.cdsStart < exonStarts[i])
				break;
			if (exonEnds[i] <= this.cdsStart)
				start += exonEnds[i] - exonStarts[i];
			if (this.cdsStart <= exonEnds[i])
				start += this.cdsStart - exonStarts[i];
			// System.out.println(start);
		}
		return start;
	}

	/**
	 * Returns the CDS end on the mRNA sequence not Chromosomal.
	 * 
	 * @return mRNA CDS end
	 */
	public int getLocalCdsEnd() {
		int end = 0;
		for (int i = 0; i < exonCount; i++) {
			// System.out.println("cdsStart: "+this.cdsStart);
			// System.out.println("cdsEnd: "+this.cdsEnd);
			// System.out.println("exonStart: "+exonStarts[i]);
			// System.out.println("exonEnd: "+exonEnds[i]);
			if (this.cdsEnd < exonStarts[i]) {
				// System.out.println("break");
				break;
			}
			if (exonEnds[i] <= this.cdsEnd) {
				// System.out.println("----|||||||||--..end");
				end += exonEnds[i] - exonStarts[i];
			}
			if (this.cdsEnd <= exonEnds[i]) {
				// System.out.println("end...||||----");
				end += this.cdsEnd - exonStarts[i];
			}
			// System.out.println(end);
		}
		return end;
	}
}
