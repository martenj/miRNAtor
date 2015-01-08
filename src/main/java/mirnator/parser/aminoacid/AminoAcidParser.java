/**
 * 
 */
package mirnator.parser.aminoacid;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;

import mirnator.structs.amino.AminoAcid;

/**
 * This class parses files in the following form
 * 
 * @author mjaeger
 * 
 */
public class AminoAcidParser {

	private static final Logger logger = Logger.getLogger(AminoAcidParser.class.getSimpleName());

	private static final int AA_NAME = 0;
	private static final int AA_CODE3 = 1;
	private static final int AA_CODE1 = 2;
	private static final int AA_CODONS = 3;
	private static final int AA_COMP = 4;

	public static HashMap<String, AminoAcid> parse() {

		return AminoAcidParser.parse("data/AminoAcids/aminoacids.tsv");
	}

	/**
	 * Parse the file stored at <code>filename</code>
	 * 
	 * @param filename
	 *            - path to the file with the AA data
	 * @return array with {@link AminoAcid}s
	 */
	public static HashMap<String, AminoAcid> parse(String filename) {
		HashMap<String, AminoAcid> aminoacids = new HashMap<String, AminoAcid>();
		boolean correctHeader = false;

		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(filename));
			String str;
			String[] fields;
			AminoAcid aa;
			while ((str = in.readLine()) != null) {
				if (str.startsWith("#"))
					continue;
				fields = str.split("\t");
				if (!correctHeader && fields[0].equals("Amino Acid")) {
					correctHeader = true;
					continue;
				}
				aa = new AminoAcid();
				aa.setName(fields[AA_NAME]);
				aa.setCode3(fields[AA_CODE3]);
				aa.setCode1(fields[AA_CODE1]);
				aa.setCodons(fields[AA_CODONS].split(","));
				if (fields.length > 4)
					aa.setCompressedCodons(fields[AA_COMP].split(","));
				else
					aa.setCompressedCodons(null);
				aminoacids.put(aa.getCode3(), aa);
			}
		} catch (IOException e) {
			e.printStackTrace();
			logger.severe("Failed to load the file: " + filename);
		} finally {
			try {
				if (in != null)
					in.close();
			} catch (IOException e) {
				e.printStackTrace();
				logger.severe("Failed to close the file: " + filename);
			}
		}

		return aminoacids;
	}

	public static HashMap<String, String> parseCodon() {

		return AminoAcidParser.parseCodon("data/AminoAcids/aminoacids.tsv");
	}

	/**
	 * Parses the specified file and returns a {@link HashMap} containing the codons (e.g AUG) as keys and the
	 * corresponding Aminoacid 3 lettercode as value (e.g. Met)
	 * 
	 * @param filename
	 * @return {@link HashMap} with <code>key</code>=codon and <code>value</code>=amino acid
	 */
	public static HashMap<String, String> parseCodon(String filename) {
		HashMap<String, String> aminoacids = new HashMap<String, String>();
		boolean correctHeader = false;

		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(filename));
			String str;
			String aa;
			String[] fields;
			String[] codons;
			while ((str = in.readLine()) != null) {
				if (str.startsWith("#"))
					continue;
				fields = str.split("\t");
				if (!correctHeader && fields[0].equals("Amino Acid")) {
					correctHeader = true;
					continue;
				}

				aa = fields[AA_CODE3];
				codons = fields[AA_CODONS].split(",");
				for (String codon : codons) {
					// System.out.println(codon+" "+aa);
					aminoacids.put(codon, aa);
				}
				// System.out.println(codons.length+"\t"+aminoacids.size());

			}
		} catch (IOException e) {
			e.printStackTrace();
			logger.severe("Failed to load the file: " + filename);
		} finally {
			try {
				if (in != null)
					in.close();
			} catch (IOException e) {
				e.printStackTrace();
				logger.severe("Failed to close the file: " + filename);
			}
		}

		return aminoacids;
	}

	// public static void main(String[] args) {
	// ArrayList<AminoAcid> aa = AminoAcidParser.parse();
	// for (AminoAcid aminoAcid : aa) {
	// System.out.println(aminoAcid.toLine());
	// }
	// }
}
