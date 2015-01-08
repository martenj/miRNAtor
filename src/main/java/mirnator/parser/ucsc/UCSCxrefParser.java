/**
 * 
 */
package mirnator.parser.ucsc;

import java.io.File;
import java.util.HashMap;


import mirnator.structs.ucsc.UCSCxref;

/**
 * @author mjaeger
 *
 */
public class UCSCxrefParser extends UCSCparser {
    
    private enum Fieldnames {	kgName,
        			mRNA,
        			spID,
        			spDisplayID,
        			geneSymbol,
        			refseq,
        			protAcc,
        			description}
    private HashMap<String, UCSCxref> knownXref;
    private UCSCxref singleKnownXref;
    /**
     * @param infile
     */
    public UCSCxrefParser(File infile) {
	super(infile);
	this.init();
    }

    /**
     * @param filename
     */
    public UCSCxrefParser(String filename) {
	super(filename);
	this.init();
    }
    
    private void init(){
	
	this.knownXref	= new HashMap<String, UCSCxref>();
    }


    /* (non-Javadoc)
     * @see mirnator.ucsc.parser.UCSCparser#processFields(java.lang.String[])
     */
    @Override
    protected boolean processFields(String[] fields) {
	if(fields.length != Fieldnames.values().length)
	    return false;
	try{
        	this.singleKnownXref	= new UCSCxref();
        	this.singleKnownXref.setKgName(fields[Fieldnames.kgName.ordinal()]);
        	this.singleKnownXref.setmRNA(fields[Fieldnames.mRNA.ordinal()]);
        	this.singleKnownXref.setSpID(fields[Fieldnames.spID.ordinal()]);
        	this.singleKnownXref.setSpDisplayID(fields[Fieldnames.spDisplayID.ordinal()]);
        	this.singleKnownXref.setGeneSymbol(fields[Fieldnames.geneSymbol.ordinal()]);
        	this.singleKnownXref.setRefseq(fields[Fieldnames.refseq.ordinal()]);
        	this.singleKnownXref.setProtAcc(fields[Fieldnames.protAcc.ordinal()]);
        	this.singleKnownXref.setDescription(fields[Fieldnames.description.ordinal()]);
        	
        	if(!this.knownXref.containsKey(this.singleKnownXref.getKgName())){
        	    this.knownXref.put(this.singleKnownXref.getKgName(),this.singleKnownXref);
        	}
        	else{
        	    System.err.println("Duplicate entry: "+this.singleKnownXref.getKgName());
        	}
	}
	catch(NumberFormatException e){
	    e.printStackTrace();
	    return false;
	}
	return true;
    }

    /**
     * Returns a {@link HashMap} with the knowngene cross references with the UCSC_id as key and 
     * the {@link UCSCxref} as value.
     * @return the knownXref
     */
    public HashMap<String, UCSCxref> getKnownXref() {
	parse();
        return knownXref;
    }

}
