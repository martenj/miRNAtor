package mirnator.parser.ucsc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 */

/**
 * This is the abstract super class for all UCSC Parser.
 * @author mjaeger
 *
 */
public abstract class UCSCparser {
	
    protected String 	filename;			// the path+filename where the data are stored 
	protected File 		infile;				// or alternative the File Object pointing to this data file
	
	
	protected BufferedReader  	buf;
	protected String 		line;
	protected String[] 		fields;	
	
	
	
	static Logger log = Logger.getLogger("ucsclogger");
	
	/**
	 * The default constructor for the UCSC parsers
	 * @param infile - file object with the data  
	 */
	public UCSCparser(File infile) {
		this.infile	= infile;
	}
	
	/**
	 * The alternative default constructor for the UCSC parsers
	 * @param filename - path to data file  
	 */
	public UCSCparser(String filename){
		this.filename	= filename;
		this.infile	= new File(filename);
	}
	
	
	protected void parse() {
	    log.log(Level.INFO, "Start parsing "+this.filename);
	    int valid 	= 0;
	    int invalid	= 0;
	    
        	try {
        	    buf = new BufferedReader(new FileReader(this.infile));
        	int c = 0;
        	
        
        	
        	    while ((line = buf.readLine()) != null) {
        		fields = line.split("\t");
        
        		if(processFields(fields))
        		    valid++;
        		else
        		    invalid++;
//        		if (c++ > 10)
//        		    break;
        
        	    }
        	} catch (IOException e) {
        	    log.log(Level.SEVERE,
        		    "Serious IO Exception occured while parsing UCSC file: "
        			    + this.infile.getPath() + this.infile.getName());
        	    e.printStackTrace();
        	}
        	finally{
        	    try { buf.close(); } catch ( IOException e ) { e.printStackTrace(); }
        	}
    	    log.log(Level.INFO, "Finished parsing data. Found "+valid+" ("+(valid+invalid)+") valid entries.");
        }

	protected abstract boolean processFields(String[] fields);

}
