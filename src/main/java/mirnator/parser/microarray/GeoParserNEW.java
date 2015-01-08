package mirnator.parser.microarray;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.logging.Logger;

import mirnator.constants.FileTypeConstants;
import mirnator.exception.InvalidArrayException;
import mirnator.structs.microarraydata.AffyAnnot;
import mirnator.structs.microarraydata.MicroArrayData;

public class GeoParserNEW extends MicroArrayParserNEW {

	private static final String body_start = "ID_REF";
	private static final String geo_accession_string = "!Sample_geo_accession";
	private static final String organism_string = "!Sample_organism_ch1";
	private static final String taxid_string = "!Sample_taxid_ch1";
	private static final String platform_id_string = "!Sample_platform_id";

	private static final int affy_id = 0;

	private static final String affy430_2anot;
	private static String hostName = null;
	static {
		try {
			hostName = InetAddress.getLocalHost().getHostName().toUpperCase();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		System.out.println(hostName);
		if (hostName.contains("GEN-MARVIN"))
			affy430_2anot = "/media/data/data/affymetrix/Mouse430A_2.na32.annot.csv";
		else if (hostName.contains("FOO"))
			affy430_2anot = "/home/mjaeger/data/Affymetrix/Mouse430A_2.na32.annot.csv";
		else if (hostName.contains("KNATTERTON"))
			affy430_2anot = "/home/mjaeger/data/Affymetrix/Mouse430A_2.na32.annot.csv";
		else {
			affy430_2anot = "..";
			System.err.println("annotations file not found");
			System.exit(0);
		}
	}

	private static final Logger logger = Logger.getLogger(GeoParserNEW.class.getSimpleName());

	/**
	 * Build up an MicroarrayParser from GEO file. The path to a GEO microarray file is needed as the used key - also
	 * used for summarization of replicated probes possible geneKeyID: <br>
	 * -1 : GeneSymbol<br>
	 * 0 : GenBank/RefSeq ID<br>
	 * 1 : EnsemblID<br>
	 * 2 : EntrezGene <br>
	 * 3 : GenBank/RefSeq ID<br>
	 * An additional list of identifier (see targets) can be provided. Only probes with matching identifiers are read.
	 * 
	 * @param filename
	 *            - (path+)name of the Affymetrix file
	 * @param geneKeyID
	 *            - the Key the data should be merged by
	 * @param targets
	 *            - list of Genes (same format as geneKeyID) the data should be filtert to
	 * @param filterAbsent
	 *            - only use present marked Entries (if available)
	 * @return a {@link Map} with {@link MicroArrayData} accessible by a header or number
	 */
	public static HashMap<String, MicroArrayData> parse(String filename, int geneKeyID, HashSet<String> targets,
			boolean filterAbsent) {
		logger.info("No Affymetrix annotation provided - parsing own.");
		AffyAnnot affy_annot = new AffyAnnot(affy430_2anot);
		return (parse(filename, geneKeyID, targets, affy_annot, filterAbsent));
	}

	/**
	 * Build up an MicroarrayParser from GEO file. The path to a GEO microarray file is needed as the used key - also
	 * used for summarization of replicated probes possible geneKeyID: <br>
	 * -1 : GeneSymbol<br>
	 * 0 : GenBank/RefSeq ID<br>
	 * 1 : EnsemblID<br>
	 * 2 : EntrezGene <br>
	 * 3 : GenBank/RefSeq ID<br>
	 * An additional list of identifier (see targets) can be provided. Only probes with matching identifiers are read. <br>
	 * Normaly GEO datafile have no array names but sometimes multiple array in one file. The datasets are accessibility
	 * by "<geo-accession>-Data<No.>" (e.g. GSM447605-Data1)
	 * 
	 * @param filename
	 *            - (path+)name of the Affymetrix file
	 * @param geneKeyID
	 *            - the Key the data should be merged by
	 * @param targets
	 *            - list of Genes (same format as geneKeyID) the data should be filtert to
	 * @param filterAbsent
	 *            - only use present marked Entries (if available)
	 * @return a {@link Map} with {@link MicroArrayData} accessible by a header or number
	 */
	public static HashMap<String, MicroArrayData> parse(String filename, int geneKeyID, HashSet<String> targets,
			AffyAnnot affy_annot, boolean filterAbsent) {
		HashMap<String, MicroArrayData> datasets = new HashMap<String, MicroArrayData>();

		ArrayList<Integer> dataset_values = new ArrayList<Integer>();
		ArrayList<String> dataset_names = new ArrayList<String>();
		ArrayList<Integer> present_call = new ArrayList<Integer>();

		BufferedReader in = null;

		String geo_accession = "";
		// String organism;
		// String taxid;
		// String platform_id;
		String PN;
		String EID;
		String GS;
		String EG;
		String RF;
		String key;

		int data = 0;

		boolean processed_header = false;
		boolean present_call_available = false;

		// check geneKey
		switch (geneKeyID) {
		case -1:
		case 0:
		case 1:
		case 2:
		case 3:
			break;
		default:
			geneKeyID = -1;
			logger.warning("invalid target source number: " + geneKeyID + " - set to default (GeneSymbol)");
		}

		try {
			in = new BufferedReader(new FileReader(filename));
			String str;
			String[] fields;
			while ((str = in.readLine()) != null) {
				// header of the microarrayfile
				if (!processed_header) {
					if (str.startsWith(platform_id_string)) {
						fields = str.split(" = ");
						if (!fields[1].equals("GPL1261"))
							throw new InvalidArrayException();
					}// body column line
					if (str.startsWith(geo_accession_string)) {
						fields = str.split(" = ");
						geo_accession = fields[1];
					}
					if (str.startsWith(body_start)) {
						processed_header = true;
						fields = str.split("\t");
						for (int i = 0; i < fields.length; i++) {
							if (fields[i].equals("VALUE")) {
								data++;
								dataset_values.add(i);
								datasets.put(geo_accession + "-Data" + data, new MicroArrayData(filename,
										FileTypeConstants.EXPaffymetrix));
								dataset_names.add(geo_accession + "-Data" + data);
							}
							if (fields[i].equals("ABS_CALL")) {
								present_call_available = true;
								present_call.add(i);
							}
							if (fields[i].equals("FLAG")) {
								present_call_available = true;
								present_call.add(i);
							}
						}
						logger.info("Found " + data + " datasets.");
					}
				}// body
				else {
					/* skip controlls */
					if (str.startsWith("AFFY"))
						continue;
					fields = str.split("\t");
					if (!affy_annot.getMap().containsKey(fields[affy_id]))
						continue;

					PN = affy_annot.getMap().get(fields[affy_id]).repPublicID;
					PN = checkWord(PN) ? PN : "NA";
					EID = affy_annot.getMap().get(fields[affy_id]).ensID;
					EID = checkWord(EID) ? EID : "NA";
					GS = affy_annot.getMap().get(fields[affy_id]).geneSymbol;
					GS = checkWord(GS) ? GS : "NA";
					EG = affy_annot.getMap().get(fields[affy_id]).entrezGID;
					EG = checkWord(EG) ? EG : "NA";
					RF = affy_annot.getMap().get(fields[affy_id]).refSeqID;
					RF = checkWord(RF) ? RF : "NA";
					GS = GS.toLowerCase();

					switch (geneKeyID) {
					case -1:
						key = GS;
						break;
					case 0:
					case 3:
						key = RF;
						break;
					case 1:
						key = EID;
						break;
					case 2:
						key = EG;
						break;
					default:
						key = GS;
						logger.warning("invalid target source number: " + geneKeyID + " - set to default (GeneSymbol)");
					}

					for (int i = 0; i < data; i++) {
						if (fields[dataset_values.get(i)].equals("null") || fields[dataset_values.get(i)].equals("NA"))
							continue;
						if (filterAbsent && present_call_available) {
							if (fields[present_call.get(i)].equals("A") || fields[present_call.get(i)].equals("Absent")) {
								// System.out.println(fields[present_call.get(i)]);
								// System.out.println(!fields[present_call.get(i)].equals("P"));
								// System.out.println(!fields[present_call.get(i)].equals("Present"));
								continue;
							}
						}

						if (datasets.get(dataset_names.get(i)).getEntries().containsKey(key)) { // add new Entry
							datasets.get(dataset_names.get(i)).getEntries().get(key)
									.addValue(Double.parseDouble(fields[dataset_values.get(i)]));

						} else {
							// or append expression value to existing entry
							datasets.get(dataset_names.get(i)).addEntity(key, RF, GS, EID, EID, PN, EG,
									Double.parseDouble(fields[dataset_values.get(i)]));
						}
					}
				}
			}
		} catch (InvalidArrayException e) {
			logger.warning("The plattform of file: " + filename + " is not Affymetrix Mouse 430v2.");

		} catch (IOException e) {
			e.printStackTrace();
			// handle
		} finally {
			try {
				if (in != null)
					in.close();
			} catch (IOException e) {
				e.printStackTrace();
				// handle
			}
		}

		return datasets;
	}

	/**
	 * checks microarray entries for empty strings and returns true if non- empty entry, otherwise false
	 * 
	 * @param w
	 *            - microarray entry
	 * @return - false if empty
	 */
	protected static boolean checkWord(String w) {
		if (w.equals(""))
			return false;
		else if (w.equals("NA"))
			return false;
		else if (w.equals("---"))
			return false;
		else if (w.equals("null"))
			return false;
		else
			return true;
	}

}
