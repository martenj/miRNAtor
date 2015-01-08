/**
 * 
 */
package mirnator.parser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import mirnator.structs.TaxonomyMap;
import mirnator.structs.Taxon;

/**
 * @author mjaeger
 * 
 */
public class TaxonomyParser {
    // private String filename;
    // private File file;
    private final static int SYMBOL = 0;
    private final static int COMMON_NAME = 1;
    private final static int SPECIES = 2;
    private final static int TAX_ID = 3;

    private static final Logger logger = Logger.getLogger(TaxonomyParser.class.getName());

    /**
     * Parse the file (at filename) and generate a {@link TaxonomyMap}. If there is no file at the given location
     * a {@link FileNotFoundException} is thrown.
     * @param filename
     * @return a TaxonomyMap containing all data from the file
     * @throws FileNotFoundException - if there is no such file
     * @throws IOException - if an IOerror occurs while parsing
     */
    public static TaxonomyMap parse(String filename) {

	BufferedReader in;
	try {
	    in = new BufferedReader(new FileReader(filename));
	    String line;
	    String[] fields;

	    ArrayList<Taxon> taxons = new ArrayList<Taxon>();
	    HashMap<String, Integer> symbol2index = new HashMap<String, Integer>();
	    HashMap<Integer, Integer> taxID2index = new HashMap<Integer, Integer>();
	    String symbol;
	    String commonName;
	    String species;
	    int taxID;
	    int counter = 0;

	    // comments
	    // header
	    while ((line = in.readLine()) != null) {
		if (line.startsWith("#"))
		    continue;
		if (line.startsWith("Symbol")) {
		    break;
		}
	    }
	    // body
	    while ((line = in.readLine()) != null) {
		fields = line.split("\t");
		if (fields.length != 4) {
		    // System.out.println("malformed tax line: " + line);
		    logger.log(Level.WARNING, "malformed tax line: " + line);
		    continue;
		}
		try {
		    symbol = fields[SYMBOL];
		    commonName = fields[COMMON_NAME];
		    species = fields[SPECIES];
		    taxID = Integer.parseInt(fields[TAX_ID]);

		    taxons.add(new Taxon(symbol, commonName, species, taxID));
		    symbol2index.put(symbol, counter);
		    taxID2index.put(taxID, counter++);
		} catch (NumberFormatException e) {
		    logger.info("missformed line: " + line);
		    continue;
		}

		// this.speciesTax.put(fields[0], new SpeciesTax(fields[0],
		// fields[1], fields[2], fields[3]));
	    }

	    return new TaxonomyMap(taxons.toArray(new Taxon[0]), symbol2index, taxID2index);
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	    return null;
	} catch (IOException e) {
	    e.printStackTrace();
	    return null;
	}

    }

}
