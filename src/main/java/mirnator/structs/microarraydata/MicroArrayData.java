package mirnator.structs.microarraydata;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Vector;

public class MicroArrayData {

	private String header = "";
	protected String source;
	protected int type = -1; // -1 - unknown
	// 0 - summarizedData (see data/summarizer...pl
	// 1 - Agilent
	// 2 - Affymetrix
	// 3 - ArrayExpress
	// 4 - ...

	protected HashMap<String, Entity> entries;
	protected String[] probeNames;
	private String[] targetNames; // depending on the miRNA input
	private boolean setProbeNames = false;

	public MicroArrayData(String source) {
		this(source, (short) 0);
		this.entries = new HashMap<String, Entity>();
	}

	public MicroArrayData(String source, int type) {
		this.source = source;
		this.type = type;
		this.entries = new HashMap<String, Entity>();
	}

	public MicroArrayData(HashMap<String, Entity> ent) {
		this(ent, (short) -1);
	}

	public MicroArrayData(HashMap<String, Entity> ent, int type) {
		this.entries = ent;
		this.type = type;
	}

	public void addEntity(Entity e) {
		if (this.entries.containsKey(e.key)) {
			this.entries.get(e.key).addValue(e.exprVals);
		} else {
			this.entries.put(e.key, e);
		}
	}

	public Entity getEntity(String key) {
		return this.entries.get(key);
	}

	public void removeEntity(String key) {
		if (this.entries.containsKey(key)) {
			this.entries.remove(key);
			this.setProbeNames = false;
		}
	}

	/**
	 * returns header with additional information about the array
	 * 
	 * @return
	 */
	public String getHeader() {
		return header;
	}

	/**
	 * Sets header to given string
	 * 
	 * @param header
	 */
	public void setHeader(String header) {
		this.header = header;
	}

	public void addEntity(String key, String GenBankID, String GeneSymbol, String EnsemblTID, String EnsemblGID,
			String ProbeName, String EntrezGen, double exprVal) {
		// System.out.println(GenBankID+" "+GeneSymbol+" "+EnsemblTID+" "+EnsemblGID+" "+ProbeName+" "+exprVal);

		if (this.entries.containsKey(key))
			this.entries.get(key).addValue(exprVal);
		else
			this.entries.put(key, new Entity(key, GenBankID, GeneSymbol, EnsemblTID, EnsemblGID, ProbeName, EntrezGen,
					exprVal));
	}

	public String[] getProbeNames() {
		int i = 0;
		String[] pn = new String[this.entries.size()];
		for (Iterator<String> iterator = this.entries.keySet().iterator(); iterator.hasNext();) {
			pn[i++] = (String) iterator.next();
		}
		return pn;
	}

	public String getArrayName() {
		return this.source;
	}

	public int getSize() {
		return this.entries.size();
	}

	public HashMap<String, Entity> getEntries() {
		return this.entries;
	}

	/**
	 * iterates over all Genes in the Array and returns a List of ints indicating if the gene is in the targetID list.
	 * 
	 * @param targetIDs
	 *            - list with target genesymbols
	 * @return array with '0' if the array gene is not in the targetList and '1' if it is
	 */
	public int[] getTargetStatusGeneName(HashSet<String> targetIDs) {
		int i = 0;
		int b[] = new int[this.entries.size()];
		for (String e : this.entries.keySet()) {
			// System.out.println("\""+this.entries.get(e).GeneSymbol+"\"");
			if (targetIDs.contains(this.entries.get(e).GeneSymbol)) {
				b[i++] = 1;
			} else {
				b[i++] = 0;
			}
		}
		return b;
	}

	/**
	 * iterates over all Genes in the Array and returns a List of ints indicating if the gene is in the targetID lists.
	 * 
	 * @param targetIDs
	 *            - first list with target genesymbols
	 * @param targetIDs2
	 *            - second list with target genesymbols
	 * @return array with '0' if the array gene is not in the targetList and '1' if it is
	 */
	public int[] getTargetStatusGeneName(HashSet<String> targetIDs, HashSet<String> targetIDs2) {
		int i = 0;
		int b[] = new int[this.entries.size()];
		for (String e : this.entries.keySet()) {
			// System.out.println("\""+this.entries.get(e).GeneSymbol+"\"");
			if (targetIDs.contains(this.entries.get(e).GeneSymbol)) {
				b[i++] = 1;
			} else if (targetIDs2.contains(this.entries.get(e).GeneSymbol)) {
				b[i++] = 2;
			} else {
				b[i++] = 0;
			}
		}
		return b;
	}

	/**
	 * Summarizes the probes depending on identical identifier (according to the chosen target source) e.g. TargetScan -
	 * EntrezID miRBase - EnsemblID(Transcript) MiRSeeker - GenBankID
	 * 
	 * @param target
	 *            coding the miRNA->mRNA target source
	 * @return differential expression
	 */
	public double[] getDifferentialExpression(int target) {
		if (!this.setProbeNames) {
			this.probeNames = (String[]) this.entries.keySet().toArray(new String[this.entries.keySet().size()]);
			this.setProbeNames = true;
		}

		HashMap<String, Vector<Double>> targetIDs = new HashMap<String, Vector<Double>>();

		for (String probe : this.probeNames) {
			String newID;
			switch (target) {
			case 0:
			case 3:
				newID = this.entries.get(probe).GenBankID;
				break;
			case 1:
				newID = this.entries.get(probe).EnsemblTID;
				break;
			case 2:
				newID = this.entries.get(probe).EntrezGen;
				break; // TODO I have to add EntrezGene ID to Entity
			default:
				newID = null;
				System.out.println("invalid target source number: " + target);
				System.exit(1);
			}
			if (!targetIDs.containsKey(newID)) {
				targetIDs.put(newID, new Vector<Double>());
			}
			targetIDs.get(newID).add(this.entries.get(probe).getMeanValue());
		}

		this.targetNames = new String[targetIDs.size()];
		double[] expr = new double[targetIDs.size()];
		int i = 0;
		for (String key : targetIDs.keySet()) {
			expr[i] = calcMean(targetIDs.get(key));
			this.targetNames[i] = key;
			i++;
			// System.out.println(key+"\t"+expr[i-1]);
		}
		return expr;
	}

	/**
	 * If not already done this method generates a microarraydata global list for the probenames and the corresponding
	 * expression values.
	 * 
	 * @return a Vector of expression values
	 */
	public double[] getDifferentialExpression() {
		if (!this.setProbeNames) {
			this.probeNames = (String[]) this.entries.keySet().toArray(new String[this.entries.keySet().size()]);
			this.setProbeNames = true;
		}
		// this.targetNames = new String[this.probeNames.length];
		this.targetNames = this.probeNames;
		double[] expr = new double[this.probeNames.length];
		int i = 0;
		for (String key : this.probeNames) {
			expr[i] = this.entries.get(key).getMeanValue();
			// this.targetNames[i] = key;
			i++;
			// System.out.println(key+"\t"+expr[i-1]);
		}
		return expr;
	}

	/**
	 * If not already done this method generates a microarraydata global list for the probenames and the corresponding
	 * expression values
	 * 
	 * @return a Vector of log transformed expression values
	 */
	public double[] getDifferentialExpressionLog() {
		if (!this.setProbeNames) {
			this.probeNames = (String[]) this.entries.keySet().toArray(new String[this.entries.keySet().size()]);
			this.setProbeNames = true;
		}
		// this.targetNames = new String[this.probeNames.length];
		this.targetNames = this.probeNames;
		double[] expr = new double[this.probeNames.length];
		int i = 0;
		for (String key : this.probeNames) {
			expr[i] = Math.log(this.entries.get(key).getMeanValue());
			// this.targetNames[i] = key;
			i++;
			// System.out.println(key+"\t"+expr[i-1]);
		}
		return expr;
	}

	private double calcMean(Vector<Double> vector) {
		// TODO Auto-generated method stub
		double res = 0;
		for (Double double1 : vector) {
			res += double1;
		}
		return res / (double) vector.size();
	}

	// public int[] getTargetStatusProbeName(HashSet<String> targets) {
	// if(!this.setProbeNames){
	// this.probeNames = (String[])this.entries.keySet().toArray(new String[this.entries.keySet().size()]);
	// this.setProbeNames =true;
	// }
	//
	// int b[] = new int[this.probeNames.length];
	// for (int i = 0; i < this.probeNames.length; ++i) {
	// if (targets.contains(this.probeNames[i])) {
	// b[i] = 1;
	// } else {
	// b[i] = 0;
	// }
	// }
	// return b;
	// }
	//
	// public int[] getTargetStatusEnsemblTID(HashSet<String> targets) {
	// if(!this.setProbeNames){
	// this.probeNames = (String[])this.entries.keySet().toArray(new String[this.entries.keySet().size()]);
	// this.setProbeNames =true;
	// }
	// // this.targetNames = new String[]; //TODO brauche EnsembleTID liste
	//
	// int b[] = new int[this.targetNames.length];
	// for (int i = 0; i < this.targetNames.length; ++i) {
	// if (targets.contains(this.targetNames[i])) {
	// b[i] = 1;
	// } else {
	// b[i] = 0;
	// }
	// }
	// return b;
	// }
	public int getType() {
		return this.type;
	}

	/**
	 * for a given Set of genes a integer vector of 0/1 is returned
	 * 
	 * @param targets
	 *            - set of genes
	 * @return binary vector indicating if a genes is represented on the microarray
	 */
	public int[] getTargetStatus(HashSet<String> targets) {
		// for (String string : targets) {
		// System.out.println("\'"+string+"\'");
		// }
		int b[] = new int[this.targetNames.length];
		for (int i = 0; i < this.targetNames.length; ++i) {
			// System.out.println("\'"+this.targetNames[i]+"\'");
			if (targets.contains(this.targetNames[i])) {
				b[i] = 1;
				// System.out.println("1");
			} else {
				b[i] = 0;
				// System.out.println("0");
			}
		}
		return b;
	}

	/**
	 * Randomize the target IDs for this miRNA by randomly shuffling the targets.
	 */
	public int[] getTargetStatusRandomized(int[] b) {
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

	/***************************************************************************************************************/
	/**
	 * inner class for storing MicroArrayEntities
	 * 
	 * @author mjaeger
	 *
	 */
	public class Entity {
		String key;
		String GenBankID;
		String GeneSymbol;
		String EntrezGen;
		String EnsemblTID;
		String EnsemblGID;
		String ProbeName; // Identifier
		Vector<Double> exprVals;
		int evLength = 0;

		public Entity(String key, String GenBankID, String GeneSymbol, String EnsemblTID, String EnsemblGID,
				String ProbeName, String EntrezGen) {
			this.key = key;
			this.GenBankID = GenBankID;
			this.GeneSymbol = GeneSymbol;
			this.EntrezGen = EntrezGen;
			this.EnsemblTID = EnsemblTID;
			this.EnsemblGID = EnsemblGID;
			this.ProbeName = ProbeName;
			this.exprVals = new Vector<Double>();
		}

		public Entity(String key, String GenBankID, String GeneSymbol, String EnsemblTID, String EnsemblGID,
				String ProbeName, String EntrezGen, double exprVal) {
			this(key, GenBankID, GeneSymbol, EnsemblTID, EnsemblGID, ProbeName, EntrezGen);
			this.exprVals.add(exprVal);
			this.evLength++;
		}

		public void addValue(double v) {
			this.exprVals.add(v);
		}

		public void addValue(Vector<Double> v) {
			this.exprVals.addAll(v);
		}

		public double getMinValue() {
			double min = Double.MAX_VALUE;
			Double[] dd = (Double[]) this.exprVals.toArray(new Double[this.exprVals.size()]);
			for (int i = 0; i < dd.length; i++) {
				if (dd[i] < min)
					min = dd[i];
			}
			return min;
		}

		public double getMaxValue() {
			double max = Double.MIN_VALUE;
			Double[] dd = (Double[]) this.exprVals.toArray(new Double[this.exprVals.size()]);
			for (int i = 0; i < dd.length; i++) {
				if (dd[i] > max)
					max = dd[i];
			}
			return max;
		}

		public double getMeanValue() {
			double mean = 0;
			Double[] dd = (Double[]) this.exprVals.toArray(new Double[this.exprVals.size()]);
			for (int i = 0; i < dd.length; i++) {
				mean += dd[i];
			}
			return mean / (double) dd.length;
		}

		public void printEntity() {
			System.out.print("ProbeName:  " + this.ProbeName + "\nGenBankID:  " + GenBankID + "\nGeneSymbol: "
					+ GeneSymbol + "\nEntrezGen: " + EntrezGen + "\nEnsemblGID: " + EnsemblGID + "\nEnsemblTID: "
					+ EnsemblTID + "\nExprVals: ");
			for (Iterator<Double> iter = this.exprVals.iterator(); iter.hasNext();) {
				Double type = iter.next();
				System.out.print(type + ", ");

			}
			System.out.println("\n");
		}

		public String getProbeName() {
			return this.ProbeName;
		}

		public String getKey() {
			return this.key;
		}

	}

}
