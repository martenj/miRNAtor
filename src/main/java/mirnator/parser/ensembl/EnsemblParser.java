/**
 * 
 */
package mirnator.parser.ensembl;

import java.io.BufferedReader;
import java.io.File;
import java.util.ArrayList;
import java.util.logging.Logger;

import mirnator.structs.gene.SimpleTranscript;

/**
 * This is the abstrcat Parser class for all Ensembl Parsers in FastA format
 * 
 * @author mjaeger
 *
 */
public abstract class EnsemblParser {

	protected String filename; // the path+filename where the data are stored
	protected File infile; // or alternative the File Object pointing to this data file

	protected BufferedReader in;
	protected String line;
	protected String[] fields;
	protected ArrayList<SimpleTranscript> simpleTranscripts;

	protected static final Logger logger = Logger.getLogger(EnsemblParser.class.getSimpleName());

	public EnsemblParser(String filename) {
		this(new File(filename));
	}

	public EnsemblParser(File infile) {
		this.filename = infile.getName();
		this.infile = infile;
	}

	/**
	 * 
	 * @return
	 */
	public abstract ArrayList<SimpleTranscript> parse();

}
