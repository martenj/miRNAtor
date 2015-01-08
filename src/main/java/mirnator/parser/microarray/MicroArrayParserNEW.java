/**
 * 
 */
package mirnator.parser.microarray;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Logger;

import mirnator.exception.MicroArrayFileNotFoundException;
import mirnator.structs.microarraydata.MicroArrayData;

/**
 * @author mjaeger
 *
 */
public class MicroArrayParserNEW {
	// protected static String filename;
	// protected static int geneKeyID;

	private static final Logger logger = Logger.getLogger(MicroArrayParserNEW.class.getName());

	/**
	 * This method parses the microarray file specified with the filename and returns a {@link HashMap} with the
	 * filename of the original file + index as the key and the {@link MicroArrayData} as the value. Genes are always
	 * summarized by the GeneSymbol.
	 * 
	 * @param filename
	 *            - path to microarray data file
	 * @return {@link HashMap} with the {@link MicroArrayData}
	 */
	public static HashMap<String, MicroArrayData> parse(String filename) {
		return MicroArrayParserNEW.parse(filename, -1, null);
	}

	/**
	 * This method parses the microarray file specified with the filename and returns a {@link HashMap} with the
	 * filename of the original file + index as the key and the {@link MicroArrayData} as the value. possible
	 * geneKeyIDs: <br>
	 * -1 : GeneSymbol (default)<br>
	 * 0 : GenBank/RefSeq ID<br>
	 * 1 : EnsemblID<br>
	 * 2 : EntrezGene <br>
	 * 3 : GenBank/RefSeq ID<br>
	 * 
	 * @param filename
	 *            - path to microarray data file
	 * @param geneKeyID
	 *            - identifier to be used
	 * @return {@link HashMap} with the {@link MicroArrayData}
	 */
	public static HashMap<String, MicroArrayData> parse(String filename, int geneKeyID) {
		// this.filename = filename;
		// this.geneKeyID = geneKeyID;
		return MicroArrayParserNEW.parse(filename, geneKeyID, null);
	}

	/**
	 * This method parses the microarray file specified with the filename and returns a {@link HashMap} with the
	 * filename of the original file + index as the key and the {@link MicroArrayData} as the value. If the
	 * <cpde>targets</code> are <code>not null</code> the array data are filtered by a list of targets of interest.
	 * possible geneKeyIDs: <br>
	 * -1 : GeneSymbol (default)<br>
	 * 0 : GenBank/RefSeq ID<br>
	 * 1 : EnsemblID<br>
	 * 2 : EntrezGene <br>
	 * 3 : GenBank/RefSeq ID<br>
	 * 
	 * @param filename
	 *            - path to microarray data file
	 * @param geneKeyID
	 *            - identifier to be used
	 * @param targets
	 *            - List of targets of interest
	 * @return {@link HashMap} with the {@link MicroArrayData}
	 */
	public static HashMap<String, MicroArrayData> parse(String filename, int geneKeyID, HashSet<String> targets) {
		// define the type of inputdata
		BufferedReader in = null;
		String line;
		try {
			in = new BufferedReader(new FileReader(filename));
			int l = 0;
			while ((line = in.readLine()) != null && l < 50) {
				l++;
				// if(line.startsWith("#"))
				// continue;

				if (line.startsWith("ProbeName	EnsemblID	GeneSymbol	EntrezGene	GenbankAccession")) {
					logger.info("The given microarray file is most likely a summarized file.");
					// this.exprSet = 0;
					return SummarizedParserNEW.parse(filename, geneKeyID, targets);
					// GeneSymbol
					// this.microArrayData = new
					// SummarizedParser(this.geneExpFilename, 2,
					// this.mirnaTargets.getRNAtargetList()); // by EntrezID
				} else if (line.startsWith("Scan REF")) {
					logger.info("The given microarray file is most likely an ArrayExpress file.");
					// this.exprSet = 3;

					return ArrayExpressParserNEW.parse(filename, geneKeyID, targets);
					// GeneSymbol
					// this.microArrayData = new
					// ArrayExpressParser(this.geneExpFilename, 2,
					// this.mirnaTargets.getRNAtargetList()); // by EntrezID

				} else if (line.startsWith("ID_REF")) {
					logger.info("The given mircoarray file is most likely a GEO file");
					return GeoParserNEW.parse(filename, geneKeyID, targets);

				} else if (line.startsWith("^SAMPLE")) {
					logger.info("The given mircoarray file is most likely a GEO(UCSC) file");
					return GeoParserUCSC.parse(filename, geneKeyID, targets);

				} else {
					logger.severe("Doesn't recognize file format. Aborting");
					System.exit(0);
				}
			}
		} catch (MicroArrayFileNotFoundException e) {
			logger.severe("No microarray file found at: " + filename);
			System.exit(0);
		} catch (FileNotFoundException e) {
			logger.severe("No microarray file found at: " + filename);
			e.printStackTrace();
		} catch (IOException e) {
			logger.severe("An error occured while parsing: " + filename);
			e.printStackTrace();
		} finally {
			try {
				if (in != null)
					in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return null;
	}

}
