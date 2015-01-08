package mirnator.parser.mirna.targets;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

/**
 * This is the base class for the various miR parsers.
 * 
 * TODO: split this into two classes MiRNAParser and MiRNATargets
 */
public abstract class MiRNAParser {

	static final int MRE_M8 = 20;
	static final int MRE_A1 = 21;
	static final int MRE_8m = 22;
	static final int MRE_unknown = 29;

	/** path to the MRE input file. */
	protected String path;
	/**
	 * Threshold significance value for a miR hit. Default is not to use a threshold.
	 */
	protected double p_threshold;
	/** Threshold score for a miR hit. Default is not to use. */
	protected double score_threshold;
	/** Key: hsa-miR123 Value: Integer */
	protected HashMap<String, Integer> mirna2index;
	/** Key: Integerhsa-miR123 Value: hsa-miR123 */
	protected HashMap<Integer, String> index2mirna;
	/** Key: Integer Value: ENS000123 */
	protected HashMap<Integer, String> index2mRNA;
	/** Keys: The same Integer, Value: THe GeneSymbol */
	protected HashMap<Integer, String> index2GeneSymbol;

	protected HashMap<String, Integer> mRNA2index;
	/** Key: ENS0000123 Value TargetGene2miRNA object */
	// protected HashMap<String, TargetGene2miRNA> targetGenes;
	/** Key: Integer Value MiRNA2TargetGene object */
	// protected HashMap<String, MiRNA2TargetGene> mirnas;
	/** Number of microRNAs parsed. */
	protected int n_miRNA;
	/** Number of mRNAs or transcripts parsed. */
	protected int n_mRNA;

	protected boolean[][] miRTargetMatrix = null;
	protected MRElist[][] mres = null;
	protected short[][] utr3MREs;

	/**
	 * This is the method called by the subclasses to parse the input files.
	 */
	abstract public void parse();

	public void printMatrixToFile(String file) {

		System.out.println("#mRNA: " + n_mRNA + "   #n_miRNA: " + n_miRNA);
		try {
			BufferedWriter buf = new BufferedWriter(new FileWriter(file));

			for (int i = 0; i < n_miRNA; i++) {
				buf.write("\t" + index2mirna.get(i));
			}
			buf.write("\n");

			for (int i = 0; i < n_mRNA; i++) {
				buf.write(index2GeneSymbol.get(i));
				for (int j = 0; j < n_miRNA; j++) {
					buf.write("\t" + utr3MREs[i][j]);
				}
				buf.write("\n");
			}

			buf.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected class MRE implements Comparable<MRE> {
		private int position;
		private int type;

		public int compareTo(MRE o) {
			if (this.position < o.getPosition())
				return -1;
			else if (this.position > o.getPosition())
				return 1;
			else
				return 0;
		}

		public int getPosition() {
			return position;
		}

		public int getType() {
			return type;
		}

		public void setPosition(int position) {
			this.position = position;
		}

		public void setType(int type) {
			this.type = type;
		}

		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return position + " " + type;
		}

	}

	protected class MRElist {
		Vector<MRE> mres = new Vector<MRE>();

		public int[] getPositions() {
			int[] positions = new int[mres.size()];
			for (int i = 0; i < mres.size(); i++)
				positions[i] = mres.get(i).getPosition();
			return positions;
		}

		public int[] getTypes() {
			int[] types = new int[mres.size()];
			for (int i = 0; i < mres.size(); i++)
				types[i] = mres.get(i).getType();
			return types;
		}

		public MRE getMRE(int i) {
			return mres.get(i);
		}

		public void addMRE(MRE mres) {
			this.mres.add(mres);
		}

		public void sort() {
			Collections.sort(mres);
		}
	}

	public MiRNAParser(String path) {
		this.path = path;
		this.mirna2index = new HashMap<String, Integer>();
		this.index2mirna = new HashMap<Integer, String>();
		this.index2mRNA = new HashMap<Integer, String>();
		this.mRNA2index = new HashMap<String, Integer>();
		this.index2GeneSymbol = new HashMap<Integer, String>();
	}

	/** Returns all the miRs that target the given mRNA or transcript. */
	public HashSet<String> getAllmiRs_for_mRNA(String mRNA) {
		HashSet<String> hs = new HashSet<String>();
		int n, m;
		n = mRNA2index.get(mRNA); /* The correct row for this mRNA */
		for (m = 0; m < n_miRNA; ++m) {
			if (miRTargetMatrix[n][m]) {
				String miR = index2mirna.get(m);
				hs.add(miR);
			}
		}
		return hs;
	}

	/**
	 * Return a HashSet containing the names of all of the mRNA genes or transcripts that are targeted by miRNAs. This
	 * can be used to filter the list of mRNAs on the microarray so as to analyze only genes that are miRNA targets for
	 * at least one miR.
	 */
	public HashSet<String> getRNAtargetList() {
		if (miRTargetMatrix == null) {
			System.err.println("Error: Attempt to get list of RNA targets " + "before initializing miR/mRNA matrix");
			System.exit(1);
		}
		HashSet<String> hs = new HashSet<String>();
		Iterator<String> it = mRNA2index.keySet().iterator();
		while (it.hasNext()) {
			String s = it.next();
			hs.add(s);
		}
		return hs;
	}

	/**
	 * Return a HashSet containing the names of all of the mRNA (by GeneSymbol) that are targeted by miRNAs. This can be
	 * used to filter the list of mRNAs on the microarray so as to analyze only genes that are miRNA targets for at
	 * least one miR.
	 */
	public HashSet<String> getGeneSymboltargetList() {
		if (miRTargetMatrix == null) {
			System.err.println("Error: Attempt to get list of RNA targets " + "before initializing miR/mRNA matrix");
			System.exit(1);
		}
		HashSet<String> hs = new HashSet<String>();
		for (int n = 0; n < n_mRNA; ++n) {
			String target = index2GeneSymbol.get(n);
			hs.add(target.toLowerCase());
			// System.out.println(target.toLowerCase());
		}
		return hs;
	}

	/**
	 * Returns all the mRNAs (by GeneSymbol) that are targeted by a miRNA.
	 * 
	 * @param miRNA
	 * @return
	 */

	public HashSet<String> getAllTarget_GeneSymbols(String miRNA) {

		HashSet<String> hs = new HashSet<String>();
		if (mirna2index == null)
			System.out.println("mirna2index is null");
		int n, m;
		if (!mirna2index.containsKey(miRNA))
			return hs;
		m = mirna2index.get(miRNA); /* The correct column for this miR */
		for (n = 0; n < n_mRNA; ++n) {
			if (miRTargetMatrix[n][m]) {
				String target = index2GeneSymbol.get(n);
				hs.add(target.toLowerCase());
			}
		}
		return hs;
	}

	/** Returns all the mRNAs (by GeneSymbol) that are targeted by a miRNA. */
	/**
	 * This method returns the target genes for a given miRNA, satisfying the additional condition: depending on c It is
	 * known that multiple sites have additive or cooperative effects, especially if located within about 40 nucleotides
	 * (nt), but no closer than 8 nt to one another.<br>
	 * IF (c < 0) the exact number of |c| MREs<br>
	 * IF (0 < c < 9) - number of MREs in 3'UTR is considered<br>
	 * IF (8 < c < n) - distance between MREs is considered<br>
	 * ELSE all targets<br>
	 * 
	 * @param miRNA
	 *            - name of microRNA (mmu-miR-29a)
	 * @param c
	 *            - number of MREs/minimal distance
	 * @return List of target genes, satisfying the given conditions (empty when miRNA is unknown)
	 */
	public HashSet<String> getAllTarget_3utr_GeneSymbols(String miRNA, int c) {
		HashSet<String> hs = new HashSet<String>();
		if (mirna2index == null)
			System.out.println("mirna2index is null");
		int n, m;
		if (!mirna2index.containsKey(miRNA))
			return hs;
		m = mirna2index.get(miRNA); /* The crrect column for this miR */

		MRElist mre;
		String target;

		for (n = 0; n < n_mRNA; ++n) {
			if (miRTargetMatrix[n][m]) {
				target = index2GeneSymbol.get(n);
				if (c > 0 && c < 9) {
					if (this.utr3MREs[n][m] >= c)
						hs.add(target.toLowerCase());
				} else if (c > 8) {
					// check for distance between MREs
					mre = this.mres[n][m];
					if ((this.utr3MREs[n][m] > 1) && checkMREDistance(c, mre)) {
						hs.add(target.toLowerCase());
					}
				} else {
					if (c == 0)
						hs.add(target.toLowerCase());
					else {
						if (this.utr3MREs[n][m] == -c)
							hs.add(target.toLowerCase());
					}
				}
			}
		}
		return hs;
	}

	/**
	 * Returns all the mRNAs that are targeted by a miRNA.
	 * 
	 * @param miRNA
	 *            - name of microRNA (mmu-miR-29a)
	 * @return List of target genes
	 */
	public HashSet<String> getAllTarget_mRNAs(String miRNA) {
		HashSet<String> hs = new HashSet<String>();
		if (mirna2index == null)
			System.out.println("mirna2index is null");
		int n, m;
		if (!mirna2index.containsKey(miRNA))
			return hs;
		m = mirna2index.get(miRNA); /* The crrect column for this miR */
		for (n = 0; n < n_mRNA; ++n) {
			if (miRTargetMatrix[n][m]) {
				String target = index2mRNA.get(n);
				hs.add(target);
			}
		}
		return hs;
	}

	/** Returns all the mRNAs that are targeted by a miRNA. */
	/**
	 * This method returns the target genes for a given miRNA, satisfying the additional condition: depending on c It is
	 * known that multiple sites have additive or cooperative effects, especially if located within about 40 nucleotides
	 * (nt), but no closer than 8 nt to one another. IF (c < 9) - number of MREs in 3'UTR is considered IF (8 < c < n) -
	 * distance between MREs is considered ELSE all targets
	 * 
	 * @param miRNA
	 *            - name of microRNA (mmu-miR-29a)
	 * @param c
	 *            - number of MREs/minimal distance
	 * @return List of target genes, satisfying the given conditions
	 */
	public HashSet<String> getAllTarget_3tur_mRNAs(String miRNA, int c) {
		HashSet<String> hs = new HashSet<String>();
		if (mirna2index == null)
			System.out.println("mirna2index is null");
		int n, m;
		if (!mirna2index.containsKey(miRNA))
			return hs;
		m = mirna2index.get(miRNA); /* The crrect column for this miR */
		for (n = 0; n < n_mRNA; ++n) {
			if (miRTargetMatrix[n][m]) {
				String target = index2mRNA.get(n);
				// hs.add(target);
				// String target = index2GeneSymbol.get(n);
				// Gene gene = mRNAs.get(n);
				// TODO check for C
				if (c > 0 && c < 9) {
					if (this.utr3MREs[n][m] >= c)
						hs.add(target.toLowerCase());
				} else if (c > 8 && c < 41) {
					// check for distance between MREs
					MRElist mre = this.mres[n][m];
					if ((this.utr3MREs[n][m] > 1) && checkMREDistance(c, mre)) {
						hs.add(target.toLowerCase());
					}
				} else
					hs.add(target.toLowerCase());
			}
		}
		return hs;
	}

	/**
	 * Checks if in the list of MREs at least one MRE pair is in between the max distance of <code>c</code>. <br>
	 * Return <code>true</code> if at least <code>distrance MRE a & b <= c </code> or <code>false</code> if not.
	 * 
	 * @param c
	 *            - max distance
	 * @param ml
	 *            - MRE list
	 * @return <code>true</code> if ||a-b|| <= c else <code>false</code>
	 */
	protected boolean checkMREDistance(int c, MRElist ml) {
		Vector<MRE> mres = ml.mres;
		Collections.sort(mres);
		for (int i = 0; i < mres.size() - 1; i++)
			if (Math.abs(mres.get(i).getPosition() - mres.get(i + 1).getPosition()) <= c)
				return true;
		return false;
	}

	public HashMap<String, Integer> getListOfmiRNAs() {
		return this.mirna2index;
	}

	/** Returns the number of mRNAs. */
	public int getNgenes() {
		return n_mRNA;
	}

	/**
	 * 
	 * @param s
	 * @return TODO: change to use the constants !!!!
	 */
	protected int checkType(String s) {
		if (s.equals("M8"))
			return (MiRNAParser.MRE_M8);
		else if (s.equals("m8"))
			return (MiRNAParser.MRE_M8);
		else if (s.equals("A1"))
			return (MiRNAParser.MRE_A1);
		else if (s.equals("1a"))
			return (MiRNAParser.MRE_A1);
		else if (s.equals("8m"))
			return (MiRNAParser.MRE_8m);
		else if (s.equals("8mer"))
			return (MiRNAParser.MRE_8m);
		else {
			// System.out.println("unknown MRE type: "+s);
			return (MiRNAParser.MRE_unknown);
		}
	}

	public void checkMRElist() {
		for (int i = 0; i < n_mRNA; i++) {
			// System.out.println(i);
			for (int j = 0; j < n_miRNA; j++) {
				// System.out.print(j);
				if (this.mres[i][j] != null) {
					MRElist mre = this.mres[i][j];
				}
			}
		}
	}

	public class Gene {
		private int cdsstart;
		private int cdsend;

		public int getCdsstart() {
			return cdsstart;
		}

		public int getCdsend() {
			return cdsend;
		}

		public void setCdsstart(int cdsstart) {
			this.cdsstart = cdsstart;
		}

		public void setCdsend(int cdsend) {
			this.cdsend = cdsend;
		}
	}

}