package mirnator.parser.microarray;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;

import mirnator.exception.InvalidArrayException;
import mirnator.structs.microarraydata.MicroArrayData;

public abstract class MicroArrayParser {

	protected String header;
	protected String filename;
	protected String referenzarray;
	protected int geneKeyID = -1; // used identifier / key
	protected int type;

	protected HashMap<String, MicroArrayData> datasets; //
	protected Vector<String> dsnames; // dataset names
	protected Vector<String> dscategories; // dataset categories
	protected HashSet<String> targetedGenes;

	protected String line;

	// protected boolean printing;

	/**
	 * Default Constructor
	 * 
	 * @param filename
	 *            - name to microarray data file
	 */
	public MicroArrayParser(String filename) {
		this(filename, -1);
	}

	/**
	 * possible geneKeyIDs: <br>
	 * -1 : GeneSymbol (default)<br>
	 * 0 : GenBank/RefSeq ID<br>
	 * 1 : EnsemblID<br>
	 * 2 : EntrezGene <br>
	 * 3 : GenBank/RefSeq ID<br>
	 * 
	 * @param filename
	 *            - name to microarray data file
	 * @param geneKeyID
	 *            - identifier to be used
	 */
	public MicroArrayParser(String filename, int geneKeyID) {
		this.filename = filename;
		this.geneKeyID = geneKeyID;
		this.dsnames = new Vector<String>();
		this.dscategories = new Vector<String>();
		this.datasets = new HashMap<String, MicroArrayData>();
		// BufferedReader in;
		// try {
		// in = new BufferedReader(new FileReader(filename));
		// initHeader(in);
		// initBody(in);
		// } catch (IOException e) {
		// e.printStackTrace();
		// }

	}

	/**
	 * possible geneKeyIDs: <br>
	 * -1 : GeneSymbol (default)<br>
	 * 0 : GenBank/RefSeq ID<br>
	 * 1 : EnsemblID<br>
	 * 2 : EntrezGene <br>
	 * 3 : GenBank/RefSeq ID<br>
	 * 
	 * @param filename
	 *            - name to microarray data file
	 * @param geneKeyID
	 *            - identifier to be used
	 */
	public MicroArrayParser(String filename, int geneKeyID, HashSet<String> targets) {
		this.filename = filename;
		this.geneKeyID = geneKeyID;
		this.dsnames = new Vector<String>();
		this.dscategories = new Vector<String>();
		this.datasets = new HashMap<String, MicroArrayData>();
		this.targetedGenes = targets;
		// BufferedReader in;
		// try {
		// in = new BufferedReader(new FileReader(filename));
		// initHeader(in);
		// initBody(in, true);
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

	}

	/**
	 * initiates the header and body and optionally prints the condensed arrays into a file.
	 * 
	 * @param filter
	 *            - indicated if the probes should be reduced to a subset
	 */
	public void init(boolean filter) {
		BufferedReader in;
		try {
			in = new BufferedReader(new FileReader(filename));
			initHeader(in);
			initBody(in, filter);
			// if ( this.printing ) {
			// System.out.println("file: " + this.filename.split("[.]").length);
			// System.out.println("print file " + this.filename.split("[.]")[0] + "-mirnator.txt");
			// printFile(this.filename.split("[.]")[0] + "-mirnator.txt");
			// }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * inits the header respectively the header String with filename and array names, the dataset names and the datasets
	 * itself
	 * 
	 * @param in
	 */
	abstract protected void initHeader(BufferedReader in) throws IOException;

	/**
	 * inits the body by parsing and combining the expression values for the given geneKeyID
	 * 
	 * @param in
	 *            - Reader pointing to the file
	 * @throws IOException
	 * @throws InvalidArrayException
	 */
	abstract protected void initBody(BufferedReader in) throws IOException, InvalidArrayException;

	/**
	 * inits the body by parsing and combining the expression values for the given geneKeyID
	 * 
	 * @param in
	 *            - Reader pointing to the file
	 * @param filter
	 *            - should the used probes be filtered to
	 * @throws IOException
	 * @throws InvalidArrayException
	 */
	abstract protected void initBody(BufferedReader in, boolean filter) throws IOException, InvalidArrayException;

	/**
	 * checks microarray entries for empty strings and returns true if non- empty entry, otherwise false
	 * 
	 * @param w
	 *            - microarray entry
	 * @return - false if empty
	 */
	protected boolean checkWord(String w) {
		if (w.equals(""))
			return false;
		else if (w.equals("NA"))
			return false;
		else if (w.equals("---"))
			return false;
		else if (w.equals("null"))
			return false;
		else
			return true;
	}

	/**
	 * This method returns the dataset to a given key
	 * 
	 * @param key
	 *            - of single dataset
	 * @return dataset of the corresponding key
	 */
	public MicroArrayData getDataSet(String key) {
		return this.datasets.get(key);
	}

	/**
	 * Returns the filename of the array data
	 * 
	 * @return - filename
	 */
	public String getReferenz() {
		return this.referenzarray;
	}

	/**
	 * @return all keys for datasets stored in the MAP
	 */
	public Vector<String> getDSkeys() {
		return this.dsnames;
	}

	public Vector<String> getDScat() {
		return this.dscategories;
	}

	/**
	 * stores all values of a given microarrayexperiment in a tab-sep file. headers are the dataset names
	 * (Arrayname+index). for each probeset the mean of all occurences is stored. if one array leaks the probe (absent
	 * etc. ) a null is inserted
	 * 
	 * @param file
	 *            - filename(+path)
	 * @throws IOException
	 */
	public void printFile(String file) throws IOException {
		printFile(new BufferedWriter(new FileWriter(file)), true);
	}

	/**
	 * stores all values of a given microarrayexperiment in a tab-sep file. headers are the dataset names
	 * (Arrayname+index). for each probeset the mean of all occurences is stored. if one array leaks the probe (absent
	 * etc. ) a null is inserted
	 * 
	 * @param out
	 *            - BufferedStream the data are writen to
	 * @param close
	 *            - should the BufferedStream be closed when finished
	 * @throws IOException
	 */
	public void printFile(BufferedWriter out, boolean close) throws IOException {
		// erstmal alle Identifier (GeneSymbols) klarmachen
		HashMap<String, Integer> identifier2idx = new HashMap<String, Integer>();

		String[] probeNames;
		int idx = 0;
		int narrays = this.dsnames.size();
		for (String id : this.dsnames) {
			probeNames = this.datasets.get(id).getProbeNames();
			for (String name : probeNames) {
				if (!identifier2idx.containsKey(name)) {
					identifier2idx.put(name, idx++);
				}
			}
		}
		// System.out.println(identifier2idx.size());

		// okay und nun durch alles arrays nochmal durch undnach den Expressionswerten schauen

		double[][] exprs = new double[narrays][identifier2idx.size()];
		probeNames = identifier2idx.keySet().toArray(new String[identifier2idx.size()]);

		out.write("Probename");
		MicroArrayData mad;
		int arridx = 0;
		double curval;
		for (String id : this.dsnames) {
			out.write("\t" + id);
			idx = 0;
			mad = this.datasets.get(id);
			for (String name : probeNames) {
				if (mad.getEntries().containsKey(name)) {
					exprs[arridx][idx++] = mad.getEntity(name).getMeanValue();
					// System.out.println(arridx+" "+idx);
				} else {
					exprs[arridx][idx++] = Double.MIN_VALUE;
					// System.out.println(arridx+" "+idx);
					// idx++;
				}
			}
			arridx++;
		}
		out.write("\n");

		for (idx = 0; idx < probeNames.length; idx++) {
			// System.out.println(probeNames[idx]);
			out.write(probeNames[idx]);
			for (arridx = 0; arridx < this.dsnames.size(); arridx++) {
				// System.out.println(idx+" "+arridx);
				if (exprs[arridx][idx] == Double.MIN_VALUE)
					out.write("\tnull");
				else
					out.write("\t" + exprs[arridx][idx]);
			}
			out.write("\n");
		}

		if (close)
			out.close();
	}

	@Override
	public String toString() {

		return getClass().getName() + "[filename=" + this.filename + ", geneKeyID=" + this.geneKeyID + ", type="
				+ this.type + ", no. datasets=" + this.datasets.size() + ", no. targetedGenes="
				+ (this.targetedGenes == null ? "null" : this.targetedGenes.size()) + "]";
	}

}
