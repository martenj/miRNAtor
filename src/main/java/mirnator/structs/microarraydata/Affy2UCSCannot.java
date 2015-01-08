/**
 * 
 */
package mirnator.structs.microarraydata;

import java.util.HashMap;
import java.util.Map;

/**
 * This class reads a UCSC database file with moe430 identifiers and
 * 
 * @author mjaeger
 *
 */
public class Affy2UCSCannot {

	HashMap<String, String> map_affy2ucsc;

	public Affy2UCSCannot() {
		this.map_affy2ucsc = new HashMap<String, String>();
	}

	/**
	 * Adds a new entry to the affy2ucsc map.
	 * 
	 * @param affy_id
	 *            - affymetrix id (e.g. 1450285_at)
	 * @param ucsc_id
	 *            - ucsc id (e.g uc009vht.1)
	 */
	public void addEntry(String affy_id, String ucsc_id) {
		this.map_affy2ucsc.put(affy_id, ucsc_id);
	}

	/**
	 * Returns the corresponding UCSC_id to the given affymetrix id.
	 * 
	 * @param affy_id
	 *            - affymetrix id
	 * @return the corresponding UCSC id or <code>null</code> if the is noc mapping
	 */
	public String getUCSCid(String affy_id) {
		return this.map_affy2ucsc.get(affy_id);
	}

	public int getNumberOfMappings() {
		return map_affy2ucsc.size();
	}

	public Map<String, String> getMap() {
		return map_affy2ucsc;
	}
}
