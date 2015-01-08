/**
 * 
 */
package mirnator.parser.ucsc;

import java.io.File;
import java.util.HashMap;

/**
 * @author mjaeger
 *
 */
public class UCSCknownGeneMrnaParser extends UCSCparser {
    
    private enum Fieldnames {	kgName,
				sequence}

    private HashMap<String, String> mRNAs;

    /**
     * @param infile
     */
    public UCSCknownGeneMrnaParser(File infile) {
	super(infile);
	this.init();
    }

    private void init() {
	this.mRNAs	= new HashMap<String, String>();
	
    }

    /**
     * @param filename
     */
    public UCSCknownGeneMrnaParser(String filename) {
	super(filename);
	this.init();
    }

    /* (non-Javadoc)
     * @see mirnator.ucsc.parser.UCSCparser#processFields(java.lang.String[])
     */
    @Override
    protected boolean processFields(String[] fields) {
	if(fields.length < Fieldnames.values().length)
	    return false;
	this.mRNAs.put(fields[Fieldnames.kgName.ordinal()], fields[Fieldnames.sequence.ordinal()]);
	return true;
    }

    /**
     * Returns a Hashmap of the mRNAs where the known gene ID is the key and the value the mRNA sequence
     * @return the mRNAs
     */
    public HashMap<String, String> getmRNAs() {
	parse();
        return mRNAs;
    }

}
