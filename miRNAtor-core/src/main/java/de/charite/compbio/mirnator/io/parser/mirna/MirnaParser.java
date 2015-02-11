package de.charite.compbio.mirnator.io.parser.mirna;

import java.io.File;
import java.util.logging.Logger;

/**
 * 
 * 
 *
 * @author Marten Jäger <marten.jaeger@charite.de>
 *
 */
public abstract class MirnaParser {
	protected File file;
	protected String name;
	protected String accession;

	static Logger log = Logger.getLogger("mirnalogger");

	/**
	 * Simple Constructor.
	 * 
	 * @param filename
	 *            Path to the miRNA* file.
	 */
	public MirnaParser(String filename) {
		this.file = new File(filename);
	}

	/**
	 * Simple Constructor.
	 * 
	 * @param file
	 *            The miRNA* file.
	 */
	public MirnaParser(File file) {
		this.file = file;
	}

	/**
	 * Checks if the filename is set, the file exists and the file is in the correct format.
	 * 
	 * @return true if the file is ok/correct format, false if not
	 */
	abstract public boolean validateFile();

	/**
	 * parse the data from <code>filename</code>
	 * 
	 * @throws DAOException
	 */
	abstract public void parse();

}
