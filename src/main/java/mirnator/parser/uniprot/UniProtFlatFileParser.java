/**
 * 
 */
package mirnator.parser.uniprot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mirnator.structs.uniprot.UniProtEntry;

/**
 * This is the parser for the UniProt flat file format.<br>
 * It's possible to parse for only one species specified. 
 * @author mjaeger
 *
 * TODO: missing lines from original UniProt file: DE,
 */
public class UniProtFlatFileParser extends UniProtParser {
    
    
    // Regex definition
    private static String regID = "ID\\s{3}([0-9\\_A-Z]+)\\s+([A-z]+;)+\\s+([0-9]+)\\sAA.";
//    private String regAC = "AC\\s{3}([A-Z0-9]{6};\\s{0,1})+";
//    private String regDT = "DT\\s{3}([0-9]{2}-[A-Z]{3}-[0-9]{4}),\\s{1}([0-9A-z\\s\\p{Punct}]+)";
//    private static String regDRensembl 	= "DR\\s{3}Ensembl; (ENS[A-Z]{0,3}T[0-9]{11}); (ENS[A-Z]{0,3}P[0-9]{11}); (ENS[A-Z]{0,3}G[0-9]{11}).";
    private static String regDRensembl 	= "ENS[A-Z]{0,3}[TPG][0-9]{11}";
//    private static String regDRrefseq	= "DR\\s{3}RefSeq; (([A-Z]{2}_[0-9]+.[0-9]{1});* *\\.*)+"; 
    private static String regDRrefseq	= "[A-Z]{2}_[0-9]+.[0-9]{1}"; 
    private static String regDRucsc	= "DR   UCSC; (.+);(.+)";
    private static final String regOX	= "(.+)=([0-9]+);";
    
    Pattern patID;
    Pattern patDRensembl;
    Pattern patDRrefseq;
    Pattern patDRucsc;
    Pattern patOX;
    
    Matcher matID;
    Matcher matDRensembl;
    Matcher matDRrefseq;
    Matcher matDRucsc;
    Matcher matOX;
    
    private UniProtEntry entryBean;
	

    /**
     * Default constructor.
     */
    public UniProtFlatFileParser() {
	initParser();
	
    }
    
    /**
     * Constructor for species specific parsing.
     * @param species
     */
    public UniProtFlatFileParser(int speciesID) {
	this.speciesID	= speciesID;
	initParser();
    }
    
    private void initParser(){
	this.entries 	= new ArrayList<UniProtEntry>();
	this.entryBean	= new UniProtEntry();
	this.patID	= Pattern.compile(this.regID);
	this.patDRensembl	= Pattern.compile(this.regDRensembl);
	this.patDRrefseq	= Pattern.compile(this.regDRrefseq);
	this.patDRucsc		= Pattern.compile(this.regDRucsc);
	this.patOX		= Pattern.compile(this.regOX);
    }
    

    @Override
    public void parse(File file) throws IOException {
	BufferedReader in = null;
	try {
	    in = new BufferedReader(new FileReader(file));
	    String str = "";
	    while ((str = in.readLine()) != null) {
//		System.out.println(str);
		if(str.startsWith("//")){addEntry();continue;}
		if(str.startsWith("ID")){parseIDline(str);continue;}
		if(str.startsWith("AC")){parseACline(str);continue;}
//		if(str.startsWith("DT")){parseDTline(str);continue;}
//		if(str.startsWith("DE")){parseDEline(str);continue;}
		if(str.startsWith("GN")){parseGNline(str);continue;}
		if(str.startsWith("OS")){parseOSline(str);continue;}
		if(str.startsWith("OG")){parseOGline(str);continue;}
//		if(str.startsWith("OC")){parseOCline(str);continue;}
		if(str.startsWith("OX")){parseOXline(str);continue;}
//		if(str.startsWith("OH")){parseOHline(str);continue;}
//		if(str.startsWith("CC")){parseCCline(str);continue;}
		if(str.startsWith("DR")){parseDRline(str);continue;}
//		if(str.startsWith("PE")){parsePEline(str);continue;}
//		if(str.startsWith("KW")){parseKWline(str);continue;}
		if(str.startsWith("FT")){parseFTline(str);continue;}
//		if(str.startsWith("SQ")){parseSQline(str);continue;}
//		if(str.startsWith("  ")){parseSequenceLine(str);continue;}
		
	    }
	} catch (IOException e) {
	    e.printStackTrace();
	} finally {
	    try{
		if(in != null)
		    in.close();
	    }catch (IOException e){
		e.printStackTrace();
	    }
	}
	
	
    }

//    private void parseSequenceLine(String str) {
//	// TODO Auto-generated method stub
//	
//    }

//    private void parseSQline(String str) {
//	// TODO Auto-generated method stub
//	
//    }

    /**
     * Parse the FT line, containing the feature table<br>
     * The table has a fixed format:
     * <table>
     *  <thead>
     *   <tr><th>Columns</th><th>Data item</th></tr>
     *  <thead>
     *  <tbody>
     *   <tr><td>1-2</td><td>FT</td></tr>
     *   <tr><td>6-13</td><td>Key name</td></tr>
     *   <tr><td>15-20</td><td>'From' endpoint</td></tr>
     *   <tr><td>22-27</td><td>'To' endpoint</td></tr>
     *   <tr><td>35-75</td><td>Description</td></tr>
     *  </tbody>
     * </table>
     * e.g.:<br>
     * FT   NON_TER       1      1<br>
     * FT   SIGNAL       <1     10       By similarity.<br>
     * FT   CHAIN        19     87       A-agglutinin.
     * TODO until now only the type of feature is further parsed
     * @param str - string containing the OX line
     */
    private void parseFTline(String str) {
	str = str.substring(5, 13).trim();
//	System.out.println("|"+str+"|");
	if(str.length() > 0)
	    this.entryBean.setFeatures(str);
    }

//    private void parseKWline(String str) {
//	// TODO Auto-generated method stub
//	
//    }

//    private void parsePEline(String str) {
//	// TODO Auto-generated method stub
//	
//    }

    /**
     * Parse the DR line, containing the database cross-references<br>
     * 
     * e.g.:<br>
     * DR   RefSeq; NP_006135.1; NM_006144.3.<br>
     * DR   Ensembl; ENST00000274306; ENSP00000274306; ENSG00000145649; Homo sapiens.<br>
     * DR   UCSC; uc003jpm.1; human.
     * @param str - string containing the OX line
     */
    private void parseDRline(String str) {
//	System.out.println(str);
	// Ensembl
	this.matDRensembl	= this.patDRensembl.matcher(str);
//	if(this.matDRensembl.matches()){
//	    this.entryBean.setCrossRef(this.matDRensembl.group(1),this.matDRensembl.group(2),this.matDRensembl.group(3));
//	}
	if(str.startsWith("DR   Ensembl;")){
	    while(this.matDRensembl.find()){
//		System.out.println(this.matDRensembl.group());
		this.entryBean.setCrossRef(this.matDRensembl.group());
	    }
	    return;
	}

	// RefSeq
	this.matDRrefseq	= this.patDRrefseq.matcher(str);
	if(str.startsWith("DR   RefSeq;")){
	    while(this.matDRrefseq.find()){
//		System.out.println(this.matDRrefseq.group());
		this.entryBean.setCrossRef(this.matDRrefseq.group());
	    }
	    return;
	}
	
	// UCSC
	this.matDRucsc	= this.patDRucsc.matcher(str);
	if(this.matDRucsc.matches()){
	    this.entryBean.setCrossRef(this.matDRucsc.group(1));
	    return;
	}
	
    }

//    private void parseCCline(String str) {
//	// TODO Auto-generated method stub
//	
//    }

//    private void parseOHline(String str) {
//	// TODO Auto-generated method stub
//	
//    }

    /**
     * Parse the OX line, containing the species TaxID<br>
     * The taxonomic ID used is that maintained at the NCBI.
     *  a nucleomorph or a plasmid.<br>
     * e.g.:<br>
     * OX   NCBI_TaxID=9606;<br>
     * OX   NCBI_TaxID=562;
     * @param str - string containing the OX line
     */    
    private void parseOXline(String str) {
	this.matOX	= this.patOX.matcher(str);
	if(this.matOX.find()){
//	    System.out.println(this.matOX.group(2));
	    this.entryBean.setSpeciesTax(Integer.parseInt(this.matOX.group(2)));
	}
    }

//    private void parseOCline(String str) {
//	// TODO Auto-generated method stub
//	
//    }

    /**
     * Parse the OG line, containing the Organism/Species<br>
     * The OG (OrGanelle) line indicates if the gene coding 
     * for a protein originates from mitochondria, a plastid,
     *  a nucleomorph or a plasmid.<br>
     * e.g.:<br>
     * OG   Mitochondrion.<br>
     * OG   Plastid; Organellar chromatophore.
     * @param str - string containing the OG line
     */    
    private void parseOGline(String str) {
	// TODO Auto-generated method stub
	
    }

    /**
     * Parse the OS line, containing the Organism/Species<br>
     * e.g.:<br>
     * OS   Solanum melongena (Eggplant) (Aubergine).
     * @param str - string containing the OS line
     */
    private void parseOSline(String str) {
	// TODO Auto-generated method stub
	//if more than one then append the string
	
    }
    
    /**
     * Parse the GN line, containign the GeneSymbols<br>
     * e.g.:<br>
     * GN   Name=GZMA; Synonyms=CTLA3, HFSP;Matcher cannot be resolved to a type
     * @param str - string containing the GN line
     */
    private void parseGNline(String str) {
	// TODO Auto-generated method stub
	
    }

//    private void parseDEline(String str) {
//	// TODO Auto-generated method stub
//	
//    }

//    private void parseDTline(String str) {
//	// TODO Auto-generated method stub
//	
//    }
    
    /**
     * Parse the AC line<br>
     * e.g.:<br>
     * AC   P12544; A4PHN1; Q6IB36;
     * @param str - string containing the AC line
     */
    private void parseACline(String str) {
	// TODO Auto-generated method stub
	
    }

    /**
     * Parse the ID line<br>
     * e.g.:<br>
     * ID   GRAA_HUMAN              Reviewed;         262 AA.
     * @param str - string containing the ID line
     */
    private void parseIDline(String str) {
	this.matID	= this.patID.matcher(str);
	if(this.matID.matches()){
	    this.entryBean.setUniprotID(this.matID.group(1));
	}	
    }

    /**
     * Adds an already processed entry to the array of entries and resets the 
     */
    private void addEntry() {
	if(this.speciesID != 0){
	    if(this.speciesID != this.entryBean.getSpeciesTax()){
		this.entryBean	= new UniProtEntry();		
		return;
	    }
	}
	
//	System.out.println("!!!!! next Entry - "+this.entryBean);
	this.entries.add(this.entryBean);
	this.entryBean	= new UniProtEntry();
    }
    
    

    @Override
    public void parse(String filename) throws IOException {
	this.parse(new File(filename));
	
    }
    
    
//    private parseIDline(String line){
//	
//    }

    
    public static void main(String[] args){

	String filename = "/home/mjaeger/data/UniProt/P01911.txt";
//	String filename = "/home/mjaeger/data/UniProt/uniprot_sprot.test";
	UniProtFlatFileParser upffp	= new UniProtFlatFileParser(9606);
	try {
	    upffp.parse(filename);
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	
	for (UniProtEntry entry : upffp.entries) {
	    System.out.println(entry.toLine());
//	    System.err.println(entry.getFeatures().size());
	}
    }


}
