/**
 * 
 */
package mirnator.parser.microarray;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashSet;

import mirnator.exception.InvalidArrayException;

/**
 * @author mjaeger
 *
 */
public class GeoParser extends MicroArrayParser {

	/**
	 * @param filename
	 */
	public GeoParser(String filename) {
		super(filename);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param filename
	 * @param geneKeyID
	 */
	public GeoParser(String filename, int geneKeyID) {
		super(filename, geneKeyID);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param filename
	 * @param geneKeyID
	 * @param targets
	 */
	public GeoParser(String filename, int geneKeyID, HashSet<String> targets) {
		super(filename, geneKeyID, targets);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mirnator.parser.microarray.MicroArrayParser#initHeader(java.io.BufferedReader)
	 */
	@Override
	protected void initHeader(BufferedReader in) throws IOException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mirnator.parser.microarray.MicroArrayParser#initBody(java.io.BufferedReader)
	 */
	@Override
	protected void initBody(BufferedReader in) throws IOException, InvalidArrayException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mirnator.parser.microarray.MicroArrayParser#initBody(java.io.BufferedReader, boolean)
	 */
	@Override
	protected void initBody(BufferedReader in, boolean filter) throws IOException, InvalidArrayException {
		// TODO Auto-generated method stub

	}

}
