/**
 * 
 */
package mirnator.structs.ucsc;

/**
 * @author mjaeger
 *
 */
public class UCSCmrnaSequence implements UCSCDatabaseEntry {

	private String kgName;
	private String sequence;

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
	 * @return the sequence
	 */
	public String getSequence() {
		return sequence;
	}

	/**
	 * @param sequence
	 *            the sequence to set
	 */
	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	@Override
	public String toString() {
		return getClass().getName() + "[name=" + getKgName() + ",sequence=" + getSequence() + "]"; // TODO
																									// Auto-generated
																									// method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mirnator.structs.ucsc.UCSCDatabaseEntry#toLine()
	 */
	public String toLine() {
		// TODO Auto-generated method stub
		return getKgName() + "\t" + getSequence();
	}

}
