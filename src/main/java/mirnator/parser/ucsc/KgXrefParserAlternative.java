/**
 * 
 */
package mirnator.parser.ucsc;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

import mirnator.sql2java.XrefBean;
import mirnator.sql2java.XrefManager;

/**
 * @author mjaeger
 *
 */
public class KgXrefParserAlternative {
    
    private static final Logger logger 	= Logger.getLogger(KgXrefParserAlternative.class.getName());
    private static XrefManager	xMan	= XrefManager.getInstance();
    
    private static final int UCSC_ID		= 0;
    private static final int MRNA		= 1;
    private static final int SP_ID		= 2;
    private static final int SP_DISPLAY_ID	= 3;
    private static final int GENE_SYMBOL	= 4;
    private static final int REFSEQ		= 5;
    private static final int PROT_ACC		= 6;
    private static final int DESCRIPTION	= 7;
    private static final int COLUMN_NUMBERS	= 8;
    
    
    
    public static ArrayList<XrefBean> parse(String filename){
	ArrayList<XrefBean> xrefs = new ArrayList<XrefBean>();
	
	BufferedReader in = null;
	int errorLines = 0;
	
	XrefBean xrefTemplate;
	
	try {
	    in = new BufferedReader(new FileReader(filename));
	    String str;
	    String[] fields;
	    while ((str = in.readLine()) != null) {
		fields	= str.split("\t");
		if(fields.length != COLUMN_NUMBERS){
		    logger.info("found incorrect number of columns for line: "+str);
		    continue;
		}
		
		xrefTemplate	= xMan.createXrefBean();
		
//		xrefTemplate.setkno
	    }
	} catch (IOException e) {
	    e.printStackTrace();
	    logger.severe("An serious Error occured while reading the Xref file: "+filename);
	} finally {
	    try{
		if(in != null)
		    in.close();
	    }catch (IOException e){
		e.printStackTrace();
		logger.severe("An serious Error occured while closing the Xref file: "+filename);
	    }
	}
	
	return xrefs;
    }

}
