package mirnator.parser.microarray;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashSet;

import mirnator.structs.microarraydata.MicroArrayData;


public class SummarizedParser extends MicroArrayParser {

    public SummarizedParser(String filename) {
	super(filename);
	this.type = 0;
	init(false);
    }

    /**
     * Build up an MicroarrayParser from summarized file. The path to a
     * summarized microarray file is needed as the used key - also used for
     * summarization of replicated probes possible targetIDs: <br>
     * -1 : GeneSymbol (default)<br>
     * 0 : GenBank/RefSeq ID<br>
     * 1 : EnsemblID<br>
     * 2 : EntrezGene <br>
     * 3 : GenBank/RefSeq ID<br>
     * 
     * @param filename
     *            - Path to microarray file
     * @param targetID
     *            - used key
     */
    public SummarizedParser(String filename, int targetID) {
	super(filename, targetID);
	this.type = 0;
	init(false);
    }

    /**
     * Build up an MicroarrayParser from summarized file. The path to a
     * summarized microarray file is needed as the used key - also used for
     * summarization of replicated probes possible targetIDs: <br>
     * -1 : GeneSymbol<br>
     * 0 : GenBank/RefSeq ID<br>
     * 1 : EnsemblID<br>
     * 2 : EntrezGene <br>
     * 3 : GenBank/RefSeq ID<br>
     * An additional list of identifier (see targetID) can be provided. Only
     * probes with matching identifiers are read.
     * 
     * @param filename
     *            - Path to microarray file
     * @param targetID
     *            - used key
     * @param targets
     *            - list of identifier
     */
    public SummarizedParser(String filename, int targetID,
	    HashSet<String> targets, boolean printFile) {
	super(filename, targetID, targets);
	this.type = 0;
//	this.printing = printFile;
	init(true);
    }

    /**
     * Build up an MicroarrayParser from summarized file. The path to a
     * summarized microarray file is needed as the used key - also used for
     * summarization of replicated probes possible targetIDs: <br>
     * -1 : GeneSymbol<br>
     * 0 : GenBank/RefSeq ID<br>
     * 1 : EnsemblID<br>
     * 2 : EntrezGene <br>
     * 3 : GenBank/RefSeq ID<br>
     * An additional list of identifier (see targetID) can be provided. Only
     * probes with matching identifiers are read.
     * 
     * @param filename
     *            - Path to microarray file
     * @param targetID
     *            - used key
     * @param targets
     *            - list of identifier
     */
    public SummarizedParser(String filename, int targetID,
	    HashSet<String> targets) {
	super(filename, targetID, targets);
	this.type = 0;
	init(true);
    }

    @Override
    public void initBody(BufferedReader in) throws IOException {
	initBody(in, false);
    }

    @Override
    public void initBody(BufferedReader in, boolean filter) throws IOException {
	String[] linesp;
	while ((line = in.readLine()) != null) {
	    line = line.trim();
	    linesp = line.split("\t");
	    String PN;
	    if (checkWord(linesp[0]))
		PN = linesp[0];
	    else
		continue;
	    String EID = checkWord(linesp[1]) ? linesp[1] : "NA";
	    String GS = checkWord(linesp[2]) ? linesp[2] : "NA";
	    String EG = checkWord(linesp[3]) ? linesp[3] : "NA";
	    String GB = checkWord(linesp[4]) ? linesp[4] : "NA";

	    GS = GS.toLowerCase();
	    // GS = GS.toUpperCase();
	    // System.out.println("add entry");
	    // System.out.println(this.datasets.size());
	    // add Entities
	    String key;
	    switch (this.geneKeyID) {
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
		System.out.println("invalid target source number: " + this.geneKeyID);
		System.exit(1);
	    }
	    // key = GS;
	    if (key.equals("NA")) // TODO filter for NA keys
		continue;
	    if (filter && !this.targetedGenes.contains(key.toLowerCase()))
		continue;
	    for (int i = 5; i < linesp.length; i++) {
		if (linesp[i].equals("NA"))
		    continue;
		else if (this.datasets.get(this.dsnames.elementAt(i - 5)).getEntries().containsKey(key)) // add new Entry
		    this.datasets.get(this.dsnames.elementAt(i - 5)).getEntries().get(key).addValue(Double.parseDouble(linesp[i]));
		else
		    // or append expression value to existing entry
		    this.datasets.get(this.dsnames.elementAt(i - 5)).addEntity(
			    key, GB, GS, EID, EID, PN, EG,
			    Double.parseDouble(linesp[i]));
	    }

	}

    }

    @Override
    public void initHeader(BufferedReader in) throws IOException {
	// TODO Auto-generated method stub
	String[] h = { "ProbeName", "EnsemblID", "GeneSymbol", "EntrezGene",
		"GenbankAccession" };
	String line;
	String[] linesp;
	String PN;
	String EID;
	String GS;
	String EG;
	String GB;
	if ((line = in.readLine()) != null) {
	    line = line.trim();
	    linesp = line.split("\t");
	    if (linesp.length < 6)
		printHeaderError(line);
	    for (int i = 0; i < 5; i++) {
		if (!linesp[i].equals(h[i]))
		    printHeaderError(line);
		// System.out.println(linesp[i]);
	    }
	    for (int i = 5; i < linesp.length; i++) {
		if (i == 5)
		    this.referenzarray = linesp[i];
		// System.out.println("Create new found MicroArrayData nr: "+(++this.nData)+" - "+linesp[i]);
		this.datasets.put(linesp[i], new MicroArrayData(linesp[i],
			this.type));
		this.dsnames.add(linesp[i]);
	    }
	}
    }

    private void printHeaderError(String s) {
	System.err
		.println("No valid header (ProbeName	EnsemblID	GeneSymbol	EntrezGene	GenbankAccession ...\n"
			+ s);
	System.exit(1);
    }

}
