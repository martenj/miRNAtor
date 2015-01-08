/**
 * 
 */
package mirnator.structs.ucsc;

/**
 * @author mjaeger
 *
 */
public class UCSCtxInfo implements UCSCDatabaseEntry {

	private String kgName; // Name of transcript
	private String category; // coding/nearCoding/noncoding for now
	private String sourceAcc; // Accession of genbank transcript patterned on (may be refSeq)
	private boolean isRefSeq; // Is a refSeq
	private int sourceSize; // Number of bases in source, excluding poly-A tail.
	private double aliCoverage; // Fraction of bases in source aligning.
	private double aliIdRatio; // matching/total bases in alignment
	private int genoMapCount; // Number of times source aligns in genome.
	private int exonCount; // Number of exons (excludes gaps from frame shift/stops)
	private int orfSize; // Size of ORF
	private double cdsScore; // Score of best CDS according to txCdsPredict
	private boolean startComplete; // Starts with ATG
	private boolean endComplete; // Ends with stop codon
	private boolean nonsenseMediatedDecay; // If true, is a nonsense mediated decay candidate.
	private boolean retainedIntron; // True if has a retained intron compared to overlapping transcripts
	private int bleedIntoIntron; // If nonzero number of bases start or end of tx bleeds into intron
	private int strangeSplice; // Count of splice sites not gt/ag, gc/ag, or at/ac
	private int atacIntrons; // Count of number of at/ac introns
	private boolean cdsSingleInIntron; // True if CDS is single exon and in intron of other transcript.
	private boolean cdsSingleInUtr3; // True if CDS is single exon and in 3' UTR of other transcript.
	private boolean selenocysteine; // If true TGA codes for selenocysteine
	private boolean genomicFrameShift; // True if genomic version has frame shift we cut out
	private boolean genomicStop; // True if genomic version has stop codon we cut out

	public String toString() {
		return getClass().getSimpleName() + "[kgName=" + getKgName() + ",category=" + getCategory() + ",sourceAcc="
				+ getSourceAcc() + ",isRefSeq=" + isRefSeq + ",sourceSize=" + getSourceSize() + ",aliCoverage="
				+ getAliCoverage() + ",aliIdRatio=" + getAliIdRatio() + ",genoMapCount=" + getGenoMapCount()
				+ ",exonCount=" + getExonCount() + ",orfSize=" + getOrfSize() + ",cdsScore=" + getCdsScore()
				+ ",startComplete=" + isStartComplete() + ",endComplete=" + isEndComplete() + ",nonsenseMediatedDecay="
				+ isNonsenseMediatedDecay() + ",retainedIntron=" + isRetainedIntron() + ",bleedIntoIntron="
				+ getBleedIntoIntron() + ",strangeSplice=" + getStrangeSplice() + ",atacIntrons=" + getAtacIntrons()
				+ ",cdsSingleInIntron=" + isCdsSingleInIntron() + ",cdsSingleInUtr3=" + isCdsSingleInUtr3()
				+ ",selenocysteine=" + isSelenocysteine() + ",genomicFrameShift=" + isGenomicFrameShift()
				+ ",genomicStop=" + isGenomicStop() + "]";
	}

	public String toLine() {
		return getKgName() + "\t" + getCategory() + "\t" + getSourceAcc() + "\t" + isRefSeq + "\t" + getSourceSize()
				+ "\t" + getAliCoverage() + "\t" + getAliIdRatio() + "\t" + getGenoMapCount() + "\t" + getExonCount()
				+ "\t" + getOrfSize() + "\t" + getCdsScore() + "\t" + (isStartComplete() ? 1 : 0) + "\t"
				+ (isEndComplete() ? 1 : 0) + "\t" + (isNonsenseMediatedDecay() ? 1 : 0) + "\t"
				+ (isRetainedIntron() ? 1 : 0) + "\t" + getBleedIntoIntron() + "\t" + getStrangeSplice() + "\t"
				+ getAtacIntrons() + "\t" + (isCdsSingleInIntron() ? 1 : 0) + "\t" + (isCdsSingleInUtr3() ? 1 : 0)
				+ "\t" + (isSelenocysteine() ? 1 : 0) + "\t" + (isGenomicFrameShift() ? 1 : 0) + "\t"
				+ (isGenomicStop() ? 1 : 0);
	}

	/**
	 * @return the name
	 */
	public String getKgName() {
		return kgName;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setKgName(String kgName) {
		this.kgName = kgName;
	}

	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * @param category
	 *            the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * @return the sourceAcc
	 */
	public String getSourceAcc() {
		return sourceAcc;
	}

	/**
	 * @param sourceAcc
	 *            the sourceAcc to set
	 */
	public void setSourceAcc(String sourceAcc) {
		this.sourceAcc = sourceAcc;
	}

	/**
	 * @return the isRefSeq
	 */
	public boolean isRefSeq() {
		return isRefSeq;
	}

	/**
	 * @param isRefSeq
	 *            the isRefSeq to set
	 */
	public void setRefSeq(boolean isRefSeq) {
		this.isRefSeq = isRefSeq;
	}

	/**
	 * @return the sourceSize
	 */
	public int getSourceSize() {
		return sourceSize;
	}

	/**
	 * @param sourceSize
	 *            the sourceSize to set
	 */
	public void setSourceSize(int sourceSize) {
		this.sourceSize = sourceSize;
	}

	/**
	 * @return the aliCoverage
	 */
	public double getAliCoverage() {
		return aliCoverage;
	}

	/**
	 * @param aliCoverage
	 *            the aliCoverage to set
	 */
	public void setAliCoverage(double aliCoverage) {
		this.aliCoverage = aliCoverage;
	}

	/**
	 * @return the aliIdRatio
	 */
	public double getAliIdRatio() {
		return aliIdRatio;
	}

	/**
	 * @param aliIdRatio
	 *            the aliIdRatio to set
	 */
	public void setAliIdRatio(double aliIdRatio) {
		this.aliIdRatio = aliIdRatio;
	}

	/**
	 * @return the genoMapCount
	 */
	public int getGenoMapCount() {
		return genoMapCount;
	}

	/**
	 * @param genoMapCount
	 *            the genoMapCount to set
	 */
	public void setGenoMapCount(int genoMapCount) {
		this.genoMapCount = genoMapCount;
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
	 * @return the orfSize
	 */
	public int getOrfSize() {
		return orfSize;
	}

	/**
	 * @param orfSize
	 *            the orfSize to set
	 */
	public void setOrfSize(int orfSize) {
		this.orfSize = orfSize;
	}

	/**
	 * @return the cdsScore
	 */
	public double getCdsScore() {
		return cdsScore;
	}

	/**
	 * @param cdsScore
	 *            the cdsScore to set
	 */
	public void setCdsScore(double cdsScore) {
		this.cdsScore = cdsScore;
	}

	/**
	 * @return the startComplete
	 */
	public boolean isStartComplete() {
		return startComplete;
	}

	/**
	 * @param startComplete
	 *            the startComplete to set
	 */
	public void setStartComplete(boolean startComplete) {
		this.startComplete = startComplete;
	}

	/**
	 * @return the endComplete
	 */
	public boolean isEndComplete() {
		return endComplete;
	}

	/**
	 * @param endComplete
	 *            the endComplete to set
	 */
	public void setEndComplete(boolean endComplete) {
		this.endComplete = endComplete;
	}

	/**
	 * @return the nonsenseMediatedDecay
	 */
	public boolean isNonsenseMediatedDecay() {
		return nonsenseMediatedDecay;
	}

	/**
	 * @param nonsenseMediatedDecay
	 *            the nonsenseMediatedDecay to set
	 */
	public void setNonsenseMediatedDecay(boolean nonsenseMediatedDecay) {
		this.nonsenseMediatedDecay = nonsenseMediatedDecay;
	}

	/**
	 * @return the retainedIntron
	 */
	public boolean isRetainedIntron() {
		return retainedIntron;
	}

	/**
	 * @param retainedIntron
	 *            the retainedIntron to set
	 */
	public void setRetainedIntron(boolean retainedIntron) {
		this.retainedIntron = retainedIntron;
	}

	/**
	 * @return the bleedIntoIntron
	 */
	public int getBleedIntoIntron() {
		return bleedIntoIntron;
	}

	/**
	 * @param bleedIntoIntron
	 *            the bleedIntoIntron to set
	 */
	public void setBleedIntoIntron(int bleedIntoIntron) {
		this.bleedIntoIntron = bleedIntoIntron;
	}

	/**
	 * @return the strangeSplice
	 */
	public int getStrangeSplice() {
		return strangeSplice;
	}

	/**
	 * @param strangeSplice
	 *            the strangeSplice to set
	 */
	public void setStrangeSplice(int strangeSplice) {
		this.strangeSplice = strangeSplice;
	}

	/**
	 * @return the atacIntrons
	 */
	public int getAtacIntrons() {
		return atacIntrons;
	}

	/**
	 * @param atacIntrons
	 *            the atacIntrons to set
	 */
	public void setAtacIntrons(int atacIntrons) {
		this.atacIntrons = atacIntrons;
	}

	/**
	 * @return the cdsSingleInIntron
	 */
	public boolean isCdsSingleInIntron() {
		return cdsSingleInIntron;
	}

	/**
	 * @param cdsSingleInIntron
	 *            the cdsSingleInIntron to set
	 */
	public void setCdsSingleInIntron(boolean cdsSingleInIntron) {
		this.cdsSingleInIntron = cdsSingleInIntron;
	}

	/**
	 * @return the cdsSingleInUtr3
	 */
	public boolean isCdsSingleInUtr3() {
		return cdsSingleInUtr3;
	}

	/**
	 * @param cdsSingleInUtr3
	 *            the cdsSingleInUtr3 to set
	 */
	public void setCdsSingleInUtr3(boolean cdsSingleInUtr3) {
		this.cdsSingleInUtr3 = cdsSingleInUtr3;
	}

	/**
	 * @return the selenocysteine
	 */
	public boolean isSelenocysteine() {
		return selenocysteine;
	}

	/**
	 * @param selenocysteine
	 *            the selenocysteine to set
	 */
	public void setSelenocysteine(boolean selenocysteine) {
		this.selenocysteine = selenocysteine;
	}

	/**
	 * @return the genomicFrameShift
	 */
	public boolean isGenomicFrameShift() {
		return genomicFrameShift;
	}

	/**
	 * @param genomicFrameShift
	 *            the genomicFrameShift to set
	 */
	public void setGenomicFrameShift(boolean genomicFrameShift) {
		this.genomicFrameShift = genomicFrameShift;
	}

	/**
	 * @return the genomicStop
	 */
	public boolean isGenomicStop() {
		return genomicStop;
	}

	/**
	 * @param genomicStop
	 *            the genomicStop to set
	 */
	public void setGenomicStop(boolean genomicStop) {
		this.genomicStop = genomicStop;
	}

}
