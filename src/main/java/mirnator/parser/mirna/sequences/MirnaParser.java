package mirnator.parser.mirna.sequences;

import java.util.logging.Logger;

public abstract class MirnaParser {
	protected String filename;
	protected String name;
	protected String accession;

	static Logger log = Logger.getLogger("mirnalogger");
	
	public MirnaParser(String filename) {
		this.filename	= filename;
	}
	
	
	/**
	 * Checks if the filename is set, the file exists and the file is in the correct format. 
	 * @return true if the file is ok/correct format, false if not
	 */
	abstract public boolean checkfile();
	
	/**
	 * parse the data from <filename> 
	 * @throws DAOException 
	 */
	abstract public void parse();


}
