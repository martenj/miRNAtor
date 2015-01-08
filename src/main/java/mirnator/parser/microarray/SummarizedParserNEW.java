/**
 * 
 */
package mirnator.parser.microarray;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Logger;

import mirnator.constants.FileTypeConstants;

import mirnator.structs.microarraydata.Affy2UCSCannot;
import mirnator.structs.microarraydata.MicroArrayData;

/**
 * @author mjaeger
 * 
 */
public class SummarizedParserNEW {

    private static final Logger logger = Logger.getLogger(SummarizedParserNEW.class.getName());

    private static final int affy_id	= 0;
    
    /**
     * Build up an MicroarrayParser from GEO file. The path to a
     * GEO microarray file is needed as the used key - also used for
     * summarization of replicated probes possible geneKeyID: <br>
     * -1 : GeneSymbol<br>
     * 0 : GenBank/RefSeq ID<br>
     * 1 : EnsemblID<br>
     * 2 : EntrezGene <br>
     * 3 : GenBank/RefSeq ID<br>
     * An additional list of identifier (see targets) can be provided. Only
     * probes with matching identifiers are read. <br>
     * Normaly GEO datafile have no array names but sometimes multiple array in one file.
     * The datasets are accessibility by "<geo-accession>-Data<No.>" (e.g. GSM447605-Data1)
     * @param filename - (path+)name of the Affymetrix file
     * @param geneKeyID - the Key the data should be merged by
     * @param targets - list of Genes (same format as geneKeyID) the data should be filtert to
     * @param filterAbsent - only use present marked Entries (if available)
     * @return a {@link Map} with {@link MicroArrayData} accessible by a header or number
     */
    public static HashMap<String, MicroArrayData> parse(String filename, int geneKeyID, HashSet<String> targets, Affy2UCSCannot affy_annot){
	HashMap<String, MicroArrayData> datasets = new HashMap<String, MicroArrayData>();
	Vector<String> dsnames = new Vector<String>();
	boolean filter = targets == null ? false : true;
	boolean init_header = false;
	
	BufferedReader in = null;
	String line;
	String GS;
	String key;
	String[] linepattern;
	String[] h = { "ProbeName", "EnsemblID", "GeneSymbol", "EntrezGene", "GenbankAccession" };
	try {

	    in = new BufferedReader(new FileReader(filename));
	    while ((line = in.readLine()) != null) {
		if (line.startsWith("#"))
		    continue;
		line = line.trim();
		linepattern = line.split("\t");
		// init header
		if (!init_header) {
		    if (linepattern.length < 6)
			headerError(line);
		    for (int i = 0; i < 5; i++) {
			if (!linepattern[i].equals(h[i]))
			    headerError(line);
		    }
		    for (int i = 5; i < linepattern.length; i++) {
			// if (i == 5)
			// this.referenzarray = linepattern[i];
			// System.out.println("Create new found MicroArrayData nr: "+(++this.nData)+" - "+linesp[i]);
			datasets.put(linepattern[i], new MicroArrayData(linepattern[i], FileTypeConstants.EXPsummarized));
			dsnames.add(linepattern[i]);
		    }
		    init_header = true;
		    logger.info("Found " + datasets.size() + " datasets.");
		    continue;
		}

		// init body
		if (!affy_annot.getMap().containsKey(linepattern[affy_id]))
		    continue;

		GS = affy_annot.getUCSCid(linepattern[affy_id]);
		GS = checkWord(GS) ? GS : "NA";

		switch (geneKeyID) {
		case -1:
		    key = GS;
		    break;
		default:
		    key = GS;
		    logger.warning("invalid target source number: " + geneKeyID + " - set to default (GeneSymbol)");
		}

//		String PN;
//		if (checkWord(linepattern[0]))
//		    PN = linepattern[0];
//		else
//		    continue;
//		String EID = checkWord(linepattern[1]) ? linepattern[1] : "NA";
//		String GS = checkWord(linepattern[2]) ? linepattern[2] : "NA";
//		String EG = checkWord(linepattern[3]) ? linepattern[3] : "NA";
//		String GB = checkWord(linepattern[4]) ? linepattern[4] : "NA";
//
//		GS = GS.toLowerCase();
//		// GS = GS.toUpperCase();
//		// System.out.println("add entry");
//		// System.out.println(this.datasets.size());
//		// add Entities
//		String key;
//		switch (geneKeyID) {
//		case -1:
//		    key = GS;
//		    break;
//		case 0:
//		case 3:
//		    key = GB;
//		    break;
//		case 1:
//		    key = EID;
//		    break;
//		case 2:
//		    key = EG;
//		    break;
//		default:
//		    key = null;
//		    System.out.println("invalid target source number: " + geneKeyID);
//		    System.exit(1);
//		}
		if (key.equals("NA"))
		    continue;
		if (filter && !targets.contains(key.toLowerCase()))
		    continue;
		for (int i = 5; i < linepattern.length; i++) {
		    if (linepattern[i].equals("NA"))
			continue;
		    else if (linepattern[i].equals("-Inf"))
			continue;
		    else if (linepattern[i].equals("Inf"))
			continue;
		    else if (datasets.get(dsnames.elementAt(i - 5)).getEntries().containsKey(key)) // add new Entry
			datasets.get(dsnames.elementAt(i - 5)).getEntries().get(key).addValue(Double.parseDouble(linepattern[i]));
		    else // or append expression value to existing entry
			datasets.get(dsnames.elementAt(i - 5)).addEntity(key, null, GS, null, null, null, null, Double.parseDouble(linepattern[i]));
		}

	    }
	} catch (IOException e) {
	    e.printStackTrace();
	    logger.severe("Ann severe error occured while parsing the summarized file: " + filename + "\nExit.");
	    System.exit(1);
	} finally {
	    try {
		if (in != null)
		    in.close();
	    } catch (IOException e) {
		e.printStackTrace();
		logger.severe("Ann severe error occured while parsing the summarized file: " + filename + "\nExit.");
		System.exit(1);
	    }
	}

	// TODO Auto-generated method stub
	return datasets;
    }

    /**
     * 
     * @param filename
     * @param geneKeyID
     * @param targets
     * @return
     */
    public static HashMap<String, MicroArrayData> parse(String filename, int geneKeyID, HashSet<String> targets) {

	HashMap<String, MicroArrayData> datasets = new HashMap<String, MicroArrayData>();
	Vector<String> dsnames = new Vector<String>();
	boolean filter = targets == null ? false : true;
	boolean init_header = false;

	BufferedReader in = null;
	String line;
	String[] linepattern;
	String[] h = { "ProbeName", "EnsemblID", "GeneSymbol", "EntrezGene", "GenbankAccession" };
	try {

	    in = new BufferedReader(new FileReader(filename));
	    while ((line = in.readLine()) != null) {
		if (line.startsWith("#"))
		    continue;
		line = line.trim();
		linepattern = line.split("\t");
		// init header
		if (!init_header) {
		    if (linepattern.length < 6)
			headerError(line);
		    for (int i = 0; i < 5; i++) {
			if (!linepattern[i].equals(h[i]))
			    headerError(line);
		    }
		    for (int i = 5; i < linepattern.length; i++) {
			// if (i == 5)
			// this.referenzarray = linepattern[i];
			// System.out.println("Create new found MicroArrayData nr: "+(++this.nData)+" - "+linesp[i]);
			datasets.put(linepattern[i], new MicroArrayData(linepattern[i], FileTypeConstants.EXPsummarized));
			dsnames.add(linepattern[i]);
		    }
		    init_header = true;
		    logger.info("Found " + datasets.size() + " datasets.");
		    continue;
		}

		// init body

		String PN;
		if (checkWord(linepattern[0]))
		    PN = linepattern[0];
		else
		    continue;
		String EID = checkWord(linepattern[1]) ? linepattern[1] : "NA";
		String GS = checkWord(linepattern[2]) ? linepattern[2] : "NA";
		String EG = checkWord(linepattern[3]) ? linepattern[3] : "NA";
		String GB = checkWord(linepattern[4]) ? linepattern[4] : "NA";

		GS = GS.toLowerCase();
		// GS = GS.toUpperCase();
		// System.out.println("add entry");
		// System.out.println(this.datasets.size());
		// add Entities
		String key;
		switch (geneKeyID) {
		case -1:
		    key = GS;
		    break;
		case 0:
		case 3:
		    key = GB;
		    break;
		case 1:
		    key = EID;
		    break;
		case 2:
		    key = EG;
		    break;
		default:
		    key = null;
		    System.out.println("invalid target source number: " + geneKeyID);
		    System.exit(1);
		}
		if (key.equals("NA"))
		    continue;
		if (filter && !targets.contains(key.toLowerCase()))
		    continue;
		for (int i = 5; i < linepattern.length; i++) {
		    if (linepattern[i].equals("NA"))
			continue;
		    else if (datasets.get(dsnames.elementAt(i - 5)).getEntries().containsKey(key)) // add new Entry
			datasets.get(dsnames.elementAt(i - 5)).getEntries().get(key).addValue(Double.parseDouble(linepattern[i]));
		    else // or append expression value to existing entry
			datasets.get(dsnames.elementAt(i - 5)).addEntity(key, GB, GS, EID, EID, PN, EG, Double.parseDouble(linepattern[i]));
		}

	    }
	} catch (IOException e) {
	    e.printStackTrace();
	    logger.severe("An severe error occured while parsing the summarized file: " + filename + "\nExit.");
	    System.exit(1);
	} finally {
	    try {
		if (in != null)
		    in.close();
	    } catch (IOException e) {
		e.printStackTrace();
		logger.severe("An severe error occured while parsing the summarized file: " + filename + "\nExit.");
		System.exit(1);
	    }
	}

	// TODO Auto-generated method stub
	return datasets;
    }

    /**
     * checks microarray entries for empty strings and returns <code>true</code>
     * if non- empty entry, otherwise <code>false</code>
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

    /**
     * loggs the wrong header line and exits the program.
     * 
     * @param line
     *            - incorrect header line
     */
    private static void headerError(String line) {
	logger.severe("No valid header (ProbeName	EnsemblID	GeneSymbol	EntrezGene	GenbankAccession ...\n" + line);
	System.exit(1);
    }

    // @Override
    // private void initHeader(BufferedReader in) throws IOException {
    // // TODO Auto-generated method stub
    // String[] h = { "ProbeName", "EnsemblID", "GeneSymbol", "EntrezGene",
    // "GenbankAccession" };
    // String line;
    // String[] linesp;
    // String PN;
    // String EID;
    // String GS;
    // String EG;
    // String GB;
    // if ((line = in.readLine()) != null) {
    // line = line.trim();
    // linesp = line.split("\t");
    // if (linesp.length < 6)
    // printHeaderError(line);
    // for (int i = 0; i < 5; i++) {
    // if (!linesp[i].equals(h[i]))
    // printHeaderError(line);
    // // System.out.println(linesp[i]);
    // }
    // for (int i = 5; i < linesp.length; i++) {
    // if (i == 5)
    // this.referenzarray = linesp[i];
    // //
    // System.out.println("Create new found MicroArrayData nr: "+(++this.nData)+" - "+linesp[i]);
    // this.datasets.put(linesp[i], new MicroArrayData(linesp[i],
    // this.type));
    // this.dsnames.add(linesp[i]);
    // }
    // }
    // }

    // @Override
    // public void initBody(BufferedReader in, boolean filter) throws
    // IOException {
    // String[] linesp;
    // while ((line = in.readLine()) != null) {
    // line = line.trim();
    // linesp = line.split("\t");
    // String PN;
    // if (checkWord(linesp[0]))
    // PN = linesp[0];
    // else
    // continue;
    // String EID = checkWord(linesp[1]) ? linesp[1] : "NA";
    // String GS = checkWord(linesp[2]) ? linesp[2] : "NA";
    // String EG = checkWord(linesp[3]) ? linesp[3] : "NA";
    // String GB = checkWord(linesp[4]) ? linesp[4] : "NA";
    //
    // GS = GS.toLowerCase();
    // // GS = GS.toUpperCase();
    // // System.out.println("add entry");
    // // System.out.println(this.datasets.size());
    // // add Entities
    // String key;
    // switch (this.geneKeyID) {
    // case -1:
    // key = GS;
    // break;
    // case 0:
    // case 3:
    // key = GB;
    // break;
    // case 1:
    // key = EID;
    // break;
    // case 2:
    // key = EG;
    // break;
    // default:
    // key = null;
    // System.out.println("invalid target source number: " + this.geneKeyID);
    // System.exit(1);
    // }
    // // key = GS;
    // if (key.equals("NA")) // TODO filter for NA keys
    // continue;
    // if (filter && !this.targetedGenes.contains(key.toLowerCase()))
    // continue;
    // for (int i = 5; i < linesp.length; i++) {
    // if (linesp[i].equals("NA"))
    // continue;
    // else if (this.datasets.get(this.dsnames.elementAt(i -
    // 5)).getEntries().containsKey(key)) // add new Entry
    // this.datasets.get(this.dsnames.elementAt(i -
    // 5)).getEntries().get(key).addValue(Double.parseDouble(linesp[i]));
    // else
    // // or append expression value to existing entry
    // this.datasets.get(this.dsnames.elementAt(i - 5)).addEntity(
    // key, GB, GS, EID, EID, PN, EG,
    // Double.parseDouble(linesp[i]));
    // }
    //
    // }
    //
    // }

}
