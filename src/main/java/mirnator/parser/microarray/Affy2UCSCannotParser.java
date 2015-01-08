package mirnator.parser.microarray;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Logger;

import mirnator.structs.microarraydata.Affy2UCSCannot;

public class Affy2UCSCannotParser {
    
    private static final int 	UCSC_ID		= 0;
    private static final int	AFFY_ID		= 1;
    private static final int 	NUM_FIELDS	= 2;
    
    private static final Logger logger = Logger.getLogger(Affy2UCSCannotParser.class.getSimpleName());
    
    
    public static Affy2UCSCannot parse(String filename){
	Affy2UCSCannot annot	= new Affy2UCSCannot();
	BufferedReader in = null;
	try {
	    in = new BufferedReader(new FileReader(filename));
	    String str;
	    String[] fields;
	    while ((str = in.readLine()) != null) {
		fields	= str.split("\t");
		if(fields.length != NUM_FIELDS){
		    logger.info("malformed line: "+str);
		    continue;
		}
		annot.addEntry(fields[AFFY_ID].substring(2), fields[UCSC_ID]);
	    }
	    logger.info("Found "+annot.getNumberOfMappings()+" valied mappings affy<->ucsc");
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
	
	
	return annot;
	
    }

}
