/**
 * 
 */
package mirnator.structs.ucsc;

/**
 * @author mjaeger
 *
 */
public interface UCSCDatabaseEntry {

	/**
	 * Returns a line representation of the UCSC database entry as it is stored in the database flat files provided by
	 * UCSC.
	 * 
	 * @return line representation of this entry
	 */
	public String toLine();

}
