package mirnator.parser.microarray;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashSet;

import mirnator.exception.InvalidArrayException;

/**
 * The MiRNAtorParser parses condensed arrays as summarized by the miRNAtor. e.g.
 * 
 * <GeneSymbol> <expression value>
 * 
 * @author mjaeger
 *
 */
public class MiRNAtorParser extends MicroArrayParser {

	public MiRNAtorParser(String filename) {
		super(filename);
		// TODO Auto-generated constructor stub
	}

	public MiRNAtorParser(String filename, int geneKeyID) {
		super(filename, geneKeyID);
		// TODO Auto-generated constructor stub
	}

	public MiRNAtorParser(String filename, int geneKeyID, HashSet<String> targets) {
		super(filename, geneKeyID, targets);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void initBody(BufferedReader in) throws IOException, InvalidArrayException {
		initBody(in, false);
	}

	@Override
	protected void initBody(BufferedReader in, boolean filter) throws IOException, InvalidArrayException {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initHeader(BufferedReader in) throws IOException {
		if ((line = in.readLine()) == null) {
			System.err.println("Could not read first line of miRNAtor file " + filename);
			System.exit(1);
		}
		if (!line.startsWith("Probename")) {
			System.err.println("miRNAtor file has wrong " + filename);
			System.exit(1);
		}

		line = line.trim();
		String[] a = line.split("\\t"); // contains categories

	}

}
