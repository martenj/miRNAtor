package mirnator.structs.microarraydata;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

/**
 * This class expects to get a file with format genename\tensembl\tC\tT where C control (or time point 1) and T test (or
 * time point 2) is. There are rownames (gene name ) and ensembl
 * 
 * @author peter
 * 
 */
public class Agilent {
	/**
	 * The base-2 logarithm of the expresion difference. To get the fold change exponentiate to the base 2.
	 */
	private double diff[] = null;
	private String geneNames[];
	private String ensemblTranscriptIds[];
	private String entrezGeneIDs[];
	private String GenBankIDs[];
	private int ngenes;
	private String filename;
	private HashSet<String> miR_targets = null;
	private boolean filter_for_targets = false;

	public Agilent(String fname) {
		this(fname, -1);
	}

	public class GeneName {
		private String name;
		private String entrez;
		private String ensembl_transcript;
		private ArrayList<Integer> indices;

		public GeneName(String s, String entrez, String ensembl) {
			this.name = s;
			this.entrez = entrez;
			this.ensembl_transcript = ensembl;
			indices = new ArrayList<Integer>();
		}

		public GeneName(String s, String entrez, String ensembl, int i) {
			this.name = s;
			this.entrez = entrez;
			this.ensembl_transcript = ensembl;
			indices = new ArrayList<Integer>();
			indices.add(i);
		}

		public void addIndex(int i) {
			indices.add(i);
		}

		public String getName() {
			return name;
		}

		public String getEntrez() {
			return entrez;
		}

		public String getEnsemblTranscript() {
			return ensembl_transcript;
		}

		public int getCount() {
			return indices.size();
		}

		public Iterator<Integer> iterator() {
			return indices.iterator();
		}

		public Integer top() {
			return indices.get(0);
		}
	}

	/**
	 * Assume that more than one value (probe) per gene can be present in the data. Assume that the geneNames are for
	 * the unique gene entities and that more than one ensemblTranscriptID can match to the same gene. This function
	 * will compress the data so that there is just one entry per gene. This may be most appropriate for the TargetScan
	 * dataset.
	 */
	public void compressData(String compress_option) {
		System.out.println("Compressing data with option: " + compress_option);

		System.out.println("From " + geneNames.length + " ... ");
		HashMap<String, GeneName> geneMultiplicity = new HashMap<String, GeneName>();
		int i;
		for (i = 0; i < geneNames.length; ++i) {
			if (geneMultiplicity.containsKey(geneNames[i])) {
				GeneName gn = geneMultiplicity.get(geneNames[i]);
				gn.addIndex(i);
			} else {
				GeneName gn = new GeneName(geneNames[i], entrezGeneIDs[i], ensemblTranscriptIds[i], i);
				geneMultiplicity.put(geneNames[i], gn);
			}
		}
		int s = geneMultiplicity.size();
		double diff_c[] = new double[s];
		String geneNames_c[] = new String[s];
		String ensemblTranscriptIds_c[] = new String[s];
		String entrezGeneIDs_c[] = new String[s];
		Iterator<String> it = geneMultiplicity.keySet().iterator();
		int j = 0;
		while (it.hasNext()) {
			String str = it.next();
			GeneName gn = geneMultiplicity.get(str);
			geneNames_c[j] = gn.getName();
			ensemblTranscriptIds_c[j] = gn.getEnsemblTranscript();
			entrezGeneIDs_c[j] = gn.getEntrez();
			double val;
			if (gn.getCount() == 1) {
				Integer ii = gn.top();
				val = diff[ii];
			} else {
				val = compressFunction(gn.iterator(), compress_option);
			}
			diff_c[j] = val;
			j++;
		}
		this.diff = diff_c;
		this.geneNames = geneNames_c;
		this.ensemblTranscriptIds = ensemblTranscriptIds_c;
		this.entrezGeneIDs = entrezGeneIDs_c;
		System.out.println("To " + geneNames.length + " ... ");
		this.ngenes = geneNames.length;
	}

	private double compressFunction(Iterator<Integer> iter, String compress_option) {
		double max = Double.MIN_VALUE;
		double min = Double.MAX_VALUE;
		double mean = 0;
		if (compress_option.equals("max")) {
			while (iter.hasNext()) {
				Integer ii = iter.next();
				double val = diff[ii];
				if (val > max)
					max = val;
			}
			return max;
		} else if (compress_option.equals("min")) {
			while (iter.hasNext()) {
				Integer ii = iter.next();
				double val = diff[ii];
				if (val < min)
					min = val;
			}
			return min;
		} else if (compress_option.equals("mean")) {
			int c = 0;
			while (iter.hasNext()) {
				Integer ii = iter.next();
				double val = diff[ii];
				mean += val;
				c++;
			}
			return (mean / (double) c);
		} else {
			System.err.println("Error: Did not recognize compression operation type: " + compress_option);
			System.exit(1);
		}
		return 0d; /* Should never get here. */
	}

	/**
	 * This constructor is used if the user has a flag on the command line to analyze either the neo or the 6-week data
	 * separately.
	 */
	public Agilent(String fname, int dataset) {
		init_vars(fname);
		System.out.println(" nrow =" + ngenes);
		input_data(dataset);
	}

	public Agilent(String fname, HashSet<String> targets) {
		this(fname, -1, targets);
	}

	public Agilent(String fname, int dataset, HashSet<String> targets) {
		this.miR_targets = targets;
		filter_for_targets = true;
		init_vars(fname);
		System.out.println(" nrow =" + ngenes + "Filtering = on");
		input_data(dataset);

	}

	/** Set up function called by both constructors. */
	private void init_vars(String fname) {
		this.filename = fname;
		determineRowCount();
		diff = new double[this.ngenes];
		geneNames = new String[this.ngenes];
		ensemblTranscriptIds = new String[this.ngenes];
		entrezGeneIDs = new String[this.ngenes];
		GenBankIDs = new String[this.ngenes];
	}

	public double[] getDifferentialExpression() {
		return diff;
	}

	public String[] getEnsemblTranscriptIds() {
		return ensemblTranscriptIds;
	}

	/**
	 * @param
	 * @return boolean array of length ngenes with [1] if entry in targetList [0] otherwise
	 */
	public boolean[] getEntrezGeneIDTargetStatus(HashSet<String> targetIDs) {
		boolean b[] = new boolean[ngenes];
		for (int i = 0; i < ngenes; ++i) {
			// System.out.println("ENtrez ID = " +entrezGeneIDs[i]);
			if (targetIDs.contains(entrezGeneIDs[i])) {
				b[i] = true;
			} else {
				b[i] = false;
			}
		}
		return b;
	}

	/**
	 * Randomize the target IDs for this miRNA by randomly shuffling the targets.
	 */
	public int[] getEntrezGeneIDRandomized(HashSet<String> targetIDs) {
		int b[] = new int[ngenes];
		for (int i = 0; i < ngenes; ++i) {
			if (targetIDs.contains(entrezGeneIDs[i])) {
				b[i] = 1;
			} else {
				b[i] = 0;
			}
		}

		Random rgen = new Random(); // Random number generator

		// --- Shuffle by exchanging each element randomly
		for (int i = 0; i < b.length; i++) {
			int randomPosition = rgen.nextInt(b.length);
			int temp = b[i];
			b[i] = b[randomPosition];
			b[randomPosition] = temp;
		}
		return b;
	}

	/**
	 * Randomize the target IDs for this miRNA by randomly shuffling the targets.
	 */
	public int[] getGenBankIDRandomized(HashSet<String> targetIDs) {
		int b[] = new int[ngenes];
		for (int i = 0; i < ngenes; ++i) {
			if (targetIDs.contains(GenBankIDs[i])) {
				b[i] = 1;
			} else {
				b[i] = 0;
			}
		}

		Random rgen = new Random(); // Random number generator

		// --- Shuffle by exchanging each element randomly
		for (int i = 0; i < b.length; i++) {
			int randomPosition = rgen.nextInt(b.length);
			int temp = b[i];
			b[i] = b[randomPosition];
			b[randomPosition] = temp;
		}
		return b;
	}

	public boolean[] getTargetStatus(HashSet<String> targetIDs) {
		boolean b[] = new boolean[ngenes];
		for (int i = 0; i < ngenes; ++i) {
			if (targetIDs.contains(ensemblTranscriptIds[i])) {
				b[i] = true;
			} else {
				b[i] = false;
			}
		}
		return b;
	}

	public int[] getTargetStatusIntEntrez(HashSet<String> targetIDs) {
		int b[] = new int[ngenes];
		for (int i = 0; i < ngenes; ++i) {
			if (targetIDs.contains(entrezGeneIDs[i])) {
				b[i] = 1;
			} else {
				b[i] = 0;
			}
		}
		return b;
	}

	public int[] getTargetStatusInt(HashSet<String> targetIDs) {
		int b[] = new int[ngenes];
		for (int i = 0; i < ngenes; ++i) {
			if (targetIDs.contains(ensemblTranscriptIds[i])) {
				b[i] = 1;
			} else {
				b[i] = 0;
			}
		}
		return b;
	}

	public int[] getTargetStatusIntGenBank(HashSet<String> targetIDs) {
		int b[] = new int[ngenes];
		for (int i = 0; i < ngenes; ++i) {
			// System.out.println(GenBankIDs[i]);
			if (targetIDs.contains(GenBankIDs[i])) {
				b[i] = 1;
			} else {
				b[i] = 0;
			}
		}
		return b;
	}

	public int[] getTargetStatusIntRandomized(HashSet<String> targetIDs) {
		int b[] = new int[ngenes];
		for (int i = 0; i < ngenes; ++i) {
			if (targetIDs.contains(ensemblTranscriptIds[i])) {
				b[i] = 1;
			} else {
				b[i] = 0;
			}
		}
		Random rgen = new Random(); // Random number generator

		// --- Shuffle by exchanging each element randomly
		for (int i = 0; i < b.length; i++) {
			int randomPosition = rgen.nextInt(b.length);
			int temp = b[i];
			b[i] = b[randomPosition];
			b[randomPosition] = temp;
		}
		return b;

	}

	/**
	 * Input a file representing the Agilent data from the Aorta project consisting of a mean expression value
	 * (normalized) for each of the probes on the chip. This file must have the tab-separated format: 0) Row number 1)
	 * EnsemblID 2) mean_neo 3) mean_6w 4) GeneSymbol 5) EntrezGene 6) GenbankAccession For this program, we only need
	 * the first 4 fields.
	 */

	private void input_data(int flag) {
		System.out.println("Inputting data: flag = " + flag);

		if (filter_for_targets)
			System.out.println("\tFiltering for miR targets: on");
		else
			System.out.println("\tFiltering for miR targets: off");
		int kept = 0;
		int discarded = 0;
		try {
			BufferedReader in = new BufferedReader(new FileReader(this.filename));

			String line;
			int row = 0;
			double neo = 0, sixw = 0, de;
			if ((line = in.readLine()) != null) {
				if (!line.startsWith("ProbeName")) {
					System.err.println("#Bad Agilent File (" + filename + "); first line: " + line);
					System.exit(1);
				}
			} // This skips the header line
			System.out.println("Inputting data,out to start  ");
			while ((line = in.readLine()) != null) {
				String A[] = line.split("\t");
				if (A.length < 6) {
					System.err.println("Bad agilent file input line:\n" + line + "\n\t length of fields = " + A.length);
					System.exit(1);
				}

				if (filter_for_targets) {
					if (miR_targets.contains(A[2]) || miR_targets.contains(A[1])
							|| (A.length > 5 && miR_targets.contains(A[3]))
							|| (A.length > 6 && miR_targets.contains(A[4]))) {
						kept++;
					} else {
						discarded++;
						continue;
					} // SKIP THIS LINE
				}

				geneNames[row] = A[2];
				ensemblTranscriptIds[row] = A[1];
				entrezGeneIDs[row] = A[3];
				// System.out.println("A length: "+A.length);
				// System.out.println("Added entrez id = " + A[5]);
				// System.out.println("Added GBid = " + A[6]);
				if (A.length > 6)
					GenBankIDs[row] = A[4];
				try {
					neo = Double.parseDouble(A[5]);
					sixw = Double.parseDouble(A[6]);
					de = sixw - neo;
					if (flag == -1) {
						diff[row] = de;
					} else if (flag == 1) {
						diff[row] = neo;
					} else if (flag == 2) {
						diff[row] = sixw;
					}
					row++;
				} catch (NumberFormatException nfe) {
					System.err.println("Problem parsing double in line\n" + line);
					nfe.printStackTrace();
					System.exit(1);
				} catch (Exception e) {
					e.printStackTrace();
					System.exit(1);
				}

			}
			if (row != ngenes) {
				System.err.println("Bad number of rows");
				System.exit(1);
			}
			in.close();
			System.out.println("Filter results: Retained " + kept + " RNAs, skipped " + discarded);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	/** Count number of genes (lines) in input file */
	protected void determineRowCount() {
		int nrow = 0;
		String line = null;
		System.out.println("In determineRowCount");
		try {
			BufferedReader in = new BufferedReader(new FileReader(this.filename));

			if ((line = in.readLine()) != null) {
				if (!line.startsWith("ProbeName")) {
					System.err.println("Bad first line: " + line);
					System.exit(1);
				}
			} // This skips the header line
			System.out.println("determineRowCOunt");
			while ((line = in.readLine()) != null) {
				// System.out.println(line);
				String A[] = line.split("\t");
				if (A.length < 6) {
					System.err.println("Bad agilent file input line:\n" + line + "\n\t length of fields = " + A.length);
					System.exit(1);
				}
				// geneNames[row] = A[4];
				// ensemblTranscriptIds[row] = A[1];
				// entrezGeneIDs[row] = A[5]; //
				// System.out.println("Added entrez id = " + A[5]);
				if (filter_for_targets) {
					if (miR_targets.contains(A[4]) || miR_targets.contains(A[1])
							|| (A.length > 5 && miR_targets.contains(A[5])))
						nrow++;
					// else {System.out.println("LEAVING OFF " + A[1]);}
				} else {
					nrow++; // no filtering
				}
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (Exception ex) {
			System.err.println("Exception in reading file: " + this.filename);
			System.err.println("Problem with line: " + line);
			ex.printStackTrace();
			System.exit(1);
		}
		this.ngenes = nrow;
	}

}
