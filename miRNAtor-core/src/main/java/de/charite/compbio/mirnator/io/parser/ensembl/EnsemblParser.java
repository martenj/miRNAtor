/**
 * 
 */
package de.charite.compbio.mirnator.io.parser.ensembl;

import java.io.BufferedReader;
import java.io.File;
import java.util.ArrayList;
import java.util.logging.Logger;

import de.charite.compbio.mirnator.reference.SimpleTranscriptModel;

/**
 * This is the abstrcat Parser class for all Ensembl Parsers in FastA format
 * 
 * @author mjaeger
 *
 */
public abstract class EnsemblParser {

	protected File infile; // or alternative the File Object pointing to this data file

	protected BufferedReader in;
	protected String line;
	protected String[] fields;
	protected ArrayList<SimpleTranscriptModel> simpleTranscripts;

	protected static final Logger logger = Logger.getLogger(EnsemblParser.class.getSimpleName());

	public EnsemblParser(String filename) {
		this(new File(filename));
	}

	public EnsemblParser(File infile) {
		this.infile = infile;
	}

	/**
	 * 
	 * @return
	 */
	public abstract ArrayList<SimpleTranscriptModel> parse();

}
