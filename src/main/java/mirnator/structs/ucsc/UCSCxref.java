/**
 * 
 */
package mirnator.structs.ucsc;

/**
 * @author mjaeger
 *
 */
public class UCSCxref implements UCSCDatabaseEntry {
	private String kgName; // Known Gene ID
	private String mRNA; // mRNA ID
	private String spID; // SWISS-PROT protein Accession number
	private String spDisplayID; // SWISS-PROT display ID
	private String geneSymbol; // Gene Symbol
	private String refseq; // RefSeq ID
	private String protAcc; // NCBI protein Accession number
	private String description; // Description

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
	 * @return the mRNA
	 */
	public String getmRNA() {
		return mRNA;
	}

	/**
	 * @param mRNA
	 *            the mRNA to set
	 */
	public void setmRNA(String mRNA) {
		this.mRNA = mRNA;
	}

	/**
	 * @return the spID
	 */
	public String getSpID() {
		return spID;
	}

	/**
	 * @param spID
	 *            the spID to set
	 */
	public void setSpID(String spID) {
		this.spID = spID;
	}

	/**
	 * @return the spDisplayID
	 */
	public String getSpDisplayID() {
		return spDisplayID;
	}

	/**
	 * @param spDisplayID
	 *            the spDisplayID to set
	 */
	public void setSpDisplayID(String spDisplayID) {
		this.spDisplayID = spDisplayID;
	}

	/**
	 * @return the geneSymbol
	 */
	public String getGeneSymbol() {
		return geneSymbol;
	}

	/**
	 * @param geneSymbol
	 *            the geneSymbol to set
	 */
	public void setGeneSymbol(String geneSymbol) {
		this.geneSymbol = geneSymbol;
	}

	/**
	 * @return the refseq
	 */
	public String getRefseq() {
		return refseq;
	}

	/**
	 * @param refseq
	 *            the refseq to set
	 */
	public void setRefseq(String refseq) {
		this.refseq = refseq;
	}

	/**
	 * @return the protAcc
	 */
	public String getProtAcc() {
		return protAcc;
	}

	/**
	 * @param protAcc
	 *            the protAcc to set
	 */
	public void setProtAcc(String protAcc) {
		this.protAcc = protAcc;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[kgID=" + getKgName() + ",mRNA=" + getmRNA() + ",spID=" + getSpID()
				+ ",spDisplayID=" + getSpDisplayID() + ",geneSymbol=" + getGeneSymbol() + ",refseq=" + getRefseq()
				+ ",protAcc=" + getProtAcc() + ",descrption=" + getDescription() + "]";
	}

	public String toLine() {
		return getKgName() + "\t" + getmRNA() + "\t" + getSpID() + "\t" + getSpDisplayID() + "\t" + getGeneSymbol()
				+ "\t" + getRefseq() + "\t" + getProtAcc() + "\t" + getDescription();
	}

}
