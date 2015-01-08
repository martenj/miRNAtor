package mirnator.parser.microarray;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mirnator.exception.InvalidArrayException;
import mirnator.structs.microarraydata.AffyAnnot;
import mirnator.structs.microarraydata.MicroArrayData;

/**
 * This class expects to get a file from arrayexpress - tab separated. Parsing the arrayexpressfile unsing annotations
 * from AffyAnnot Object
 * 
 * @author mjaeger
 * 
 */
public class ArrayExpressParser extends MicroArrayParser {

	private final static String affy430_2anot;
	// private final static String REGEX =
	// "Affymetrix:CompositeSequence:Mouse430_2:([\\w-/]+)";
	private final static String REGEX = "Affymetrix:CompositeSequence:Mouse430[A]*_2:([\\w-/]+)";

	protected Vector<String> descriptions; // descriptions retrieved from
	// sdrf-files

	private Pattern patt;
	private static String hostName = null;
	private boolean useCallFlag = false;

	private AffyAnnot aann = null;

	// private String[] arrayNames;
	private Integer[] arrayColumns;
	private Integer[] callsColumns;
	private String[] arrayNames;

	static {
		try {
			hostName = InetAddress.getLocalHost().getHostName().toUpperCase();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		System.out.println(hostName);
		if (hostName.contains("GEN-MARVIN"))
			affy430_2anot = "/nfs/mjaeger/data/affy/Mouse430_2.na30.annot.csv";
		else if (hostName.contains("GARNET"))
			affy430_2anot = "/home/marcel87/workspace/data/Mouse430_2.na30.annot.csv";
		else if (hostName.contains("C17WW-MG-FO-11"))
			affy430_2anot = "T:/workspace_marcel/data/Mouse430_2.na30.annot.csv";
		else if (hostName.contains("SAMSUNG"))
			affy430_2anot = "/home/marcel87/workspace/temp-data/Mouse430_2.na30.annot.csv";
		else if (hostName.contains("MEDHUNTER"))
			affy430_2anot = "/home/mjaeger/data/affymetrix/Mouse430_2.na33.annot.csv";
		else if (hostName.contains("CRICK"))
			affy430_2anot = "/nfs/bioc/data/affy/Mouse430_2.na30.annot.csv";
		else {
			affy430_2anot = "..";
			System.err.println("annotations file not found");
			System.exit(0);
		}
	}

	/**
	 * Build up an MicroarrayParser from Affymetrix file. The path to a Affymetrix microarray file is needed as the used
	 * key - also used for summarization of replicated probes possible geneKeyID: <br>
	 * -1 : GeneSymbol (default)<br>
	 * 0 : GenBank/RefSeq ID<br>
	 * 1 : EnsemblID<br>
	 * 2 : EntrezGene <br>
	 * 3 : GenBank/RefSeq ID<br>
	 * 
	 * @param filename
	 *            - Path to microarray file
	 * @param geneKeyID
	 *            - used key
	 */

	public ArrayExpressParser(String filename, int geneKeyID) {
		super(filename, geneKeyID);
		this.type = 3;
		init(false);
	}

	/**
	 * Build up an MicroarrayParser from Affymetrix file. The path to a Affymetrix microarray file is needed as the used
	 * key - also used for summarization of replicated probes possible geneKeyID: <br>
	 * -1 : GeneSymbol (default)<br>
	 * 0 : GenBank/RefSeq ID<br>
	 * 1 : EnsemblID<br>
	 * 2 : EntrezGene <br>
	 * 3 : GenBank/RefSeq ID<br>
	 * 
	 * @param filename
	 *            - Path to microarray file
	 * @param geneKeyID
	 *            - used key
	 * @param annot
	 *            - AffyAnnot Object (annotation for Affymetrix chip)
	 */

	public ArrayExpressParser(String filename, int geneKeyID, AffyAnnot annot) {
		super(filename, geneKeyID);
		this.type = 3;
		this.aann = annot;
		init(false);
	}

	/**
	 * Build up an MicroarrayParser from Affymetrix file. The path to a Affymetrix microarray file is needed as the used
	 * key - also used for summarization of replicated probes possible geneKeyID: <br>
	 * -1 : GeneSymbol<br>
	 * 0 : GenBank/RefSeq ID<br>
	 * 1 : EnsemblID<br>
	 * 2 : EntrezGene <br>
	 * 3 : GenBank/RefSeq ID<br>
	 * An additional list of identifier (see targetID) can be provided. Only probes with matching identifiers are read.
	 * 
	 * @param filename
	 *            - Path to microarray file
	 * @param geneKeyID
	 *            - used key
	 * @param geneSet
	 *            - list of identifier
	 */
	public ArrayExpressParser(String filename, int geneKeyID, HashSet<String> geneSet) {
		super(filename, geneKeyID, geneSet);
		this.type = 3;
		init(true);
	}

	/**
	 * Build up an MicroarrayParser from Affymetrix file. The path to a Affymetrix microarray file is needed as the used
	 * key - also used for summarization of replicated probes possible geneKeyID: <br>
	 * -1 : GeneSymbol<br>
	 * 0 : GenBank/RefSeq ID<br>
	 * 1 : EnsemblID<br>
	 * 2 : EntrezGene <br>
	 * 3 : GenBank/RefSeq ID<br>
	 * An additional list of identifier (see targetID) can be provided. Only probes with matching identifiers are read.
	 * 
	 * @param filename
	 *            - Path to microarray file
	 * @param geneKeyID
	 *            - used key
	 * @param geneSet
	 *            - list of identifierfymetrix chip)
	 */
	public ArrayExpressParser(String filename, int geneKeyID, HashSet<String> geneSet, AffyAnnot annot) {
		super(filename, geneKeyID, geneSet);
		this.type = 3;
		this.aann = annot;
		init(true);
	}

	/**
	 * Build up an MicroarrayParser from Affymetrix file. The path to a Affymetrix microarray file is needed as the used
	 * key - also used for summarization of replicated probes possible geneKeyID: <br>
	 * -1 : GeneSymbol<br>
	 * 0 : GenBank/RefSeq ID<br>
	 * 1 : EnsemblID<br>
	 * 2 : EntrezGene <br>
	 * 3 : GenBank/RefSeq ID<br>
	 * An additional list of identifier (see targetID) can be provided. Only probes with matching identifiers are read.
	 * 
	 * @param filename
	 *            - Path to microarray file
	 * @param geneKeyID
	 *            - used key
	 * @param geneSet
	 *            - list of identifierfymetrix chip)
	 * @param printfile
	 *            - whether the file should be written to the filesystem
	 */
	public ArrayExpressParser(String filename, int geneKeyID, HashSet<String> geneSet, AffyAnnot annot,
			boolean printfile) {
		super(filename, geneKeyID, geneSet);
		this.type = 3;
		this.aann = annot;
		// this.printing = printfile;
		init(true);
	}

	@Override
	public void initBody(BufferedReader in) throws IOException, InvalidArrayException {
		initBody(in, false);
	}

	@Override
	public void initBody(BufferedReader in, boolean filter) throws IOException, InvalidArrayException {
		if (this.aann == null) {
			// System.err.println("annotation-file is null! will exit.");
			System.out.println("read new annotation-file");
			// System.exit(0);
			this.aann = new AffyAnnot(this.affy430_2anot);
		}
		this.patt = Pattern.compile(REGEX);
		while ((line = in.readLine()) != null) {
			if (line.startsWith("#"))
				continue;
			String a[] = line.split("\\t");
			String pset = a[0];
			Matcher m = patt.matcher(pset);
			String aff = "";
			if (m.find()) {
				aff = m.group(1);
				// System.out.println(line+"\n"+aff);
			} else {
				System.out.println("Bad format for line " + line + " The first field was " + pset);
				// System.exit(1);
				// continue;
				throw new InvalidArrayException("Illegal array input " + pset + " does not match Regex: " + this.REGEX);
			}
			/* skip controlls */
			if (aff.startsWith("AFFY"))
				continue;
			if (!aann.getMap().containsKey(aff))
				continue;
			String PN = aann.getMap().get(aff).repPublicID;
			PN = checkWord(PN) ? PN : "NA";
			String EID = aann.getMap().get(aff).ensID;
			EID = checkWord(EID) ? EID : "NA";
			String GS = aann.getMap().get(aff).geneSymbol;
			GS = checkWord(GS) ? GS : "NA";
			String EG = aann.getMap().get(aff).entrezGID;
			EG = checkWord(EG) ? EG : "NA";
			String RF = aann.getMap().get(aff).refSeqID;
			RF = checkWord(RF) ? RF : "NA";
			GS = GS.toLowerCase();

			String key;
			switch (this.geneKeyID) {
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
				key = null;
				System.out.println("invalid target source number: " + this.geneKeyID);
				System.exit(1);
			}
			// key = GS;
			// System.out.println(line);
			// System.out.println(GS);
			if (key.equals("NA") || key.equals("na"))
				continue;
			if (key.isEmpty())
				continue;
			if (filter && !this.targetedGenes.contains(key.toLowerCase()))
				continue;

			// System.out.println(key);
			for (int i = 0; i < this.dsnames.size(); i++) {
				if (a[this.arrayColumns[i]].equals("null") || a[this.arrayColumns[i]].equals("NA"))
					continue;
				if ((this.useCallFlag && !a[this.callsColumns[i]].equals("P"))
						|| (this.useCallFlag && !a[this.callsColumns[i]].equals("Present")))
					continue;
				// System.out.println("\n"+this.arrayNames[i]);
				// System.out.println(key+" "+RF+" "+GS+" "+EID+" "+EID+" "+PN+" "+EG+" "+Double.parseDouble(a[this.arrayColumns[i]]));
				if (this.datasets.get(this.dsnames.elementAt(i)).getEntries().containsKey(key)) { // add
					// new
					// Entry
					this.datasets.get(this.dsnames.elementAt(i)).getEntries().get(key)
							.addValue(Double.parseDouble(a[this.arrayColumns[i]]));

				} else
					// or append expression value to existing entry
					this.datasets.get(this.dsnames.elementAt(i)).addEntity(key, RF, GS, EID, EID, PN, EG,
							Double.parseDouble(a[this.arrayColumns[i]]));
			}

		}
	}

	/**
	 * iterates over lists of known possible headers (expression values, present flag)
	 * 
	 * @param header
	 * @return -1 if header is not recognized 0 if header is expression values 1 if header is present flag
	 */
	private int checkHeader(String header) {
		String[] possheader = { "GEO:AFFYMETRIX_VALUE", "Affymetrix:CHPSignal", "Affymetrix:CHPDetection",
				"Affymetrix:Quantification", "Unknown:VALUE", "GEO:ABS_CALL", "Unknown:ABS_CALL", "VALUE",
				"GEO:AFFYMETRIX_Detection", "Software Unknown:RMA Normalised Expression", "Affymetrix:Signal",
				"Software Unknown:RMA", "Software Unknown:GCRMA", "Software Unknown:Signal",
				"Software Unknown:Detection", "AppliedBiosystems:RMA_Signal", "AppliedBiosystems:Signal",
				"Software Unknown:RMA-Signal", "dChip:Unknown", "Unknown:LOG2" };

		String[] possPresent = { "PRESENCE_CALL", "GEO:AFFYMETRIX_ABS_CALL", "GEO:AFFYMETRIX_ABS-CALL",
				"DETECTION_CALL", "GEO:AFFYMETRIX_FLAG", "GEO:AFFYMETRIX_CALL", "Affymetrix:Detection" };

		// System.out.println("check header " + header.toUpperCase());

		for (String ph : possheader) {
			if (header.toUpperCase().startsWith(ph.toUpperCase())) {
				// System.out.println(header + " starts with " + ph);
				return 0;
			}
		}

		for (String pp : possPresent) {
			if (header.toUpperCase().startsWith(pp.toUpperCase())) {
				// System.out.println(header + " starts with " + pp);
				return 1;
			}
		}

		return -1;
	}

	@Override
	public void initHeader(BufferedReader in) throws IllegalArgumentException, IOException {
		if ((line = in.readLine()) == null) {
			System.err.println("Could not read first line of ArrayExpress file " + filename);
			System.exit(1);
		}
		line = line.trim();
		String[] a = line.split("\\t"); // contains categories

		if ((line = in.readLine()) == null) {
			System.err.println("Could not read second line of ArrayExpress file " + filename);
			System.exit(1);
		}

		ArrayList<String> header = new ArrayList<String>();
		ArrayList<Integer> columns = new ArrayList<Integer>();
		ArrayList<Integer> calls = new ArrayList<Integer>();
		String[] b = line.split("\\t");

		for (int i = 1; i < b.length; i++) {
			switch (checkHeader(b[i])) {
			case 0:
				columns.add(i);
				header.add(a[i]);
				break;
			case 1:
				calls.add(i);
				this.useCallFlag = true;
				break;
			}

			// if(checkHeader(b[i]) == 0){
			// columns.add(i);
			// header.add(a[i]);
			// System.err.println("column: "+i+"\t"+b[i]);
			// } else if(checkHeader(b[i]) == 1) {
			// columns containing ABS-Calls
			// calls.add(i);
			// this.useCallFlag = true;
			// }
		}

		if (columns.size() == 0) {
			throw new IllegalArgumentException("Did not find any relevant columns!");
			// System.err.println("Did not find any relevant columns!");
		}

		if ((calls.size() == 0) || (columns.size() != calls.size())) {
			// System.err.println("found " + columns.size() +
			// " data-columns and " + calls.size() + " calls-columns");
			// System.err.println("Ignoring ABS-Calls");
			this.useCallFlag = false;
		}

		String[] filepath = filename.split("[-/.]");
		int pathlen = filepath.length;
		// System.out.println(arrayname[arrayname.length-1]);
		// for (int i=0; i<filepath.length; i++)
		// System.out.println(filepath[i]);
		String arrayname = filepath[pathlen - 7] + "-" + filepath[pathlen - 6] + "-" + filepath[pathlen - 5] + "-"
				+ filepath[pathlen - 2];
		// System.out.println(arrayname);

		// this.referenzarray = "array_1";
		/* init dsnames and datasets */
		for (int i = 0; i < columns.size(); i++) {
			this.dsnames.add(arrayname + "-" + (i + 1)); // add arrayname
			this.dscategories.add(a[columns.get(i)]); // add category of tissue
			// System.out.println(a[2*i]);
			this.datasets.put(arrayname + "-" + (i + 1), new MicroArrayData(arrayname + "-" + (i + 1), this.type));

		}
		this.referenzarray = this.dsnames.firstElement();

		/* init header */
		this.header += "# source file: " + this.filename + "\n arrays:";
		for (String string : header)
			this.header += "\n# " + string;

		this.arrayNames = (String[]) header.toArray(new String[header.size()]);
		this.arrayColumns = (Integer[]) columns.toArray(new Integer[columns.size()]);
		this.callsColumns = (Integer[]) calls.toArray(new Integer[columns.size()]);
	}

}
