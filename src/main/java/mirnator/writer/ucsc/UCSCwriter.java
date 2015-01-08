/**
 * 
 */
package mirnator.writer.ucsc;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

import mirnator.structs.ucsc.UCSCDatabaseEntry;

/**
 * @author mjaeger
 *
 */
public class UCSCwriter {

	private static final Logger logger = Logger.getLogger(UCSCwriter.class.getSimpleName());

	// private String filename;
	// private List<UCSCDatabaseEntry> entries;

	/**
	 * Writes all UCSC database entries into the given file in the database flat file format provided by UCSC.
	 * 
	 * @param filename
	 *            - path to output file
	 * @param entries
	 *            - entrie to be written
	 */
	public static void write(String filename, ArrayList<? extends UCSCDatabaseEntry> entries) {
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new FileWriter(filename));
			for (UCSCDatabaseEntry ucscDatabaseEntry : entries) {
				out.write(ucscDatabaseEntry.toLine() + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
			logger.severe("An error occured while writing entries into " + filename);
		} finally {
			try {
				if (out != null)
					out.close();
			} catch (IOException e) {
				e.printStackTrace();
				logger.severe("An error occured while closing " + filename);
			}
		}
	}

	// public static void write(String filename, HashMap<String, UCSCDatabaseEntry> entries){
	// BufferedWriter out = null;
	// try {
	// out = new BufferedWriter(new FileWriter(filename));
	// for (UCSCDatabaseEntry ucscDatabaseEntry : entries.values()) {
	// out.write(ucscDatabaseEntry.toLine()+"\n");
	// }
	// } catch (IOException e) {
	// e.printStackTrace();
	// logger.severe("An error occured while writing entries into "+filename);
	// } finally {
	// try{
	// if(out != null)
	// out.close();
	// }catch (IOException e){
	// e.printStackTrace();
	// logger.severe("An error occured while closing "+filename);
	// }
	// }
	// }

}
