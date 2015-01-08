/**
 * 
 */
package mirnator.structs.uniprot;

import java.util.HashSet;

/**
 * @author mjaeger
 *
 */
public class UniProtEntry {

	private String uniprotID;
	private int speciesTax;
	private HashSet<String> features;
	private HashSet<String> crossRefs;

	/**
     * 
     */
	public UniProtEntry() {
		this.features = new HashSet<String>();
		this.crossRefs = new HashSet<String>();
	}

	/**
	 * Returns the UniProt ID<br>
	 * e.g.: ACCD_LACCB
	 * 
	 * @return the uniprotID
	 */
	public String getUniprotID() {
		return uniprotID;
	}

	/**
	 * Ste the UniProt ID
	 * 
	 * @param uniprotID
	 *            - the uniprotID to set
	 */
	public void setUniprotID(String uniprotID) {
		this.uniprotID = uniprotID;
	}

	/**
	 * @return the features
	 */
	public HashSet<String> getFeatures() {
		return features;
	}

	/**
	 * Adds the given features to the featureSet
	 * 
	 * @param features
	 *            the features to set
	 */
	public void setFeatures(String... features) {
		for (String feature : features) {
			this.features.add(feature);
		}

	}

	/**
	 * @return the crossRef
	 */
	public HashSet<String> getCrossRef() {
		return crossRefs;
	}

	/**
	 * Adds the cross-references to the list of references.
	 * 
	 * @param crossRefs
	 *            - the crossRefs to be added
	 */
	public void setCrossRef(String... crossRefs) {
		for (String crossRef : crossRefs) {
			this.crossRefs.add(crossRef);
		}

	}

	/**
	 * Returns the species Tax (NCBI)<br>
	 * e.g.: for human 9606, mouse 10090, ...
	 * 
	 * @return the speciesTax
	 */
	public int getSpeciesTax() {
		return speciesTax;
	}

	/**
	 * Set the species Tax (NCBI)<br>
	 * e.g.: for human 9606, mouse 10090, ...
	 * 
	 * @param speciesTax
	 *            the speciesTax to set
	 */
	public void setSpeciesTax(int speciesTax) {
		this.speciesTax = speciesTax;
	}

	public String toLine() {
		return getUniprotID() + "\t" + getSpeciesTax() + "\t" + hash2String(getFeatures()) + "\t"
				+ hash2String(getCrossRef());
	}

	private String hash2String(HashSet<String> hash) {
		String res = "";
		if (hash.size() > 0) {
			res += hash.toString();
		}
		return res;
	}

}
