/**
 * 
 */
package mirnator.parser.ucsc;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

import mirnator.structs.ucsc.UCSCknownGene;

/**
 * @author mjaeger
 *
 */
public class UCSCknownGeneParser extends UCSCparser {
    
    private static final Logger logger = Logger.getLogger(UCSCknownGeneParser.class.getName());
    
    private enum Fieldnames {	kgName,
				chrom,
				strand,
				txStart,
				txEnd,
				cdsStart,
				cdsEnd,
				exonCount,
				exonStarts,
				exonEnds,
				proteinID}
    
    private HashMap<String, ArrayList<UCSCknownGene>> knownGenes;
    private UCSCknownGene singleKnownGene;
    
    int i;
    
    public UCSCknownGeneParser(File infile) {
	super(infile);
	this.init();
    }
    
    public UCSCknownGeneParser(String infile) {
	super(infile);
	this.init();
    }
    
    private void init(){
	this.knownGenes	= new HashMap<String, ArrayList<UCSCknownGene>>();
    }

    /* (non-Javadoc)
     * @see mirnator.ucsc.parser.UCSCparser#processFields(java.lang.String[])
     */
    @Override
    protected boolean processFields(String[] fields) {
	if(fields.length < Fieldnames.values().length)
	    return false;
	try{
        	this.singleKnownGene	= new UCSCknownGene();
        	this.singleKnownGene.setKgName(fields[Fieldnames.kgName.ordinal()]);
        	this.singleKnownGene.setChrom(fields[Fieldnames.chrom.ordinal()]);
        	this.singleKnownGene.setStrand(parseStrand(fields[Fieldnames.strand.ordinal()]));
        	this.singleKnownGene.setTxStart(Integer.parseInt(fields[Fieldnames.txStart.ordinal()]));
        	this.singleKnownGene.setTxEnd(Integer.parseInt(fields[Fieldnames.txEnd.ordinal()]));
        	this.singleKnownGene.setCdsStart(Integer.parseInt(fields[Fieldnames.cdsStart.ordinal()]));
        	this.singleKnownGene.setCdsEnd(Integer.parseInt(fields[Fieldnames.cdsEnd.ordinal()]));
        	this.singleKnownGene.setExonCount(Integer.parseInt(fields[Fieldnames.exonCount.ordinal()]));
        	this.singleKnownGene.setExonStarts(parseExonPositions(fields[Fieldnames.exonStarts.ordinal()]));
        	this.singleKnownGene.setExonEnds(parseExonPositions(fields[Fieldnames.exonEnds.ordinal()]));
        	this.singleKnownGene.setProteinID(fields[Fieldnames.proteinID.ordinal()]);
        	
        	if(!this.knownGenes.containsKey(this.singleKnownGene.getChrom())){
        	    this.knownGenes.put(this.singleKnownGene.getChrom(),new ArrayList<UCSCknownGene>());
        	}
        	
        	this.knownGenes.get(this.singleKnownGene.getChrom()).add(this.singleKnownGene);
	}
	catch(NumberFormatException e){
	    e.printStackTrace();
	    return false;
	}
	return true;
    }

    /**
     * Parse the strand field (e.g. + or -) and returns <code>true</code> if the strand 
     * is "+" or <code>false</code> if "-" (otherwise).
     * 
     * @param strand - "+" or "-"
     * @return <code>true</code> if "+" or <code>false</code> otherwise
     */
    private boolean parseStrand(String strand) {
	if(strand.equals("+"))
	    return true;
	return false;
    }

    /**
     * Splits and parses the Exon start/end positions from databasefile.<br>
     * e.g. 1234,1267,1468
     * @param string
     * @return positions as integer
     */
    private int[] parseExonPositions(String positionstring) throws NumberFormatException{
	String[] positions	= positionstring.split(",");
	int[] pos		= new int[positions.length];
	for(i=0;i<positions.length;i++){
	    pos[i]	= Integer.parseInt(positions[i]);
	}
	return pos;
    }

    /**
     * Returns a HashMap with all knownGenes stored in the database file.
     * The known Genes are ordered by chromosom and the keys of the HashMap 
     * are the chromosomes (An ArrayList of KnownGenes located at this chromosom).
     *  
     * @return the knownGenes 
     */
    public HashMap<String, ArrayList<UCSCknownGene>> getKnownGenes() {
	parse();
        return knownGenes;
    }


}
