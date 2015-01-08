package mirnator.parser.mirna.targets;

import java.io.*;
import java.util.*;

public class MirBaseParser extends MiRNAParser {

	/** Indices of corresponding objects in the MiRBase input file */
	static final int miRNA = 1;
	static final int MREstart = 5;
	static final int ENSEMBL_TRANSCRIPT_ID = 11;
	static final int GENE_SYMBOL_ID = 12;
	static final int SCORE = 9;
	static final int PVALUE = 10;
	static final int MIRBASE_NFIELDS = 13;
	
	private String taxon = "hsa";

	/************* CTOR *****************************/
	
	public MirBaseParser(String path, String taxon){
		super(path);
		this.taxon = taxon;
	}
	
	public MirBaseParser(String path) {
		this(path, -1.0); /* Call CTOR with flag for no p-value threshold. */
	}

	public MirBaseParser(String path, double T) {
		super(path);
		this.p_threshold = T;
	}

	/**
	 * Main parsing method. Read in entire MiRBase file. Make a map of
	 * mRNA=>miRNAs and miRNAs => mRNA. Note that the mRNAs are registered as
	 * ENS-transcript IDs.
	 */
	public void parse() {
		register_miRNAs_and_mRNAs();
		System.out.println("MiRBaseParser, file = " + path);
		this.miRTargetMatrix = new boolean[n_mRNA][n_miRNA];
		this.mres = new MRElist[n_mRNA][n_miRNA];
		this.utr3MREs = new short[n_mRNA][n_miRNA];
		for (int i = 0; i < n_mRNA; ++i)
			for (int j = 0; j < n_miRNA; ++j)
				miRTargetMatrix[i][j] = false;
//		int currentMessengerRNAIndex = 0;
//		int currentMicroRNAIndex = 0;
		String line;
		try {
			BufferedReader in = new BufferedReader(new FileReader(this.path));
			/** Skip header again */
//			while ((line = in.readLine()) != null) {
//				if (line.startsWith("##GROUP"))
//					break;
//			}

			while ((line = in.readLine()) != null) {
				if (line.length() < 1)
					continue;
				if (line.charAt(0) == '#')
					continue; /* skips comments */
				String[] A = line.split("\t");
				if (A.length < MIRBASE_NFIELDS)
					continue; /* skip malformed or empty lines */
				String mirna = A[miRNA];// System.out.println("Added "+mirna);
				if (!mirna.startsWith(this.taxon))
					continue; // JUST use mmu predictions
//				if(mirna.contains("425"))
//					System.out.println("MIrna mit stern" + mirna+"\t"+mirna2index.get(mirna));
				
				String target_gene = A[ENSEMBL_TRANSCRIPT_ID];
				if (this.p_threshold > 0) { /*
											 * Check if we are filtering on
											 * pvals
											 */
					String pval = A[PVALUE];
					Double dd = Double.parseDouble(pval);
					if (dd > this.p_threshold)
						continue;
				}
				/*
				 * If we get here, we have a miRNA/mRNA pair. Enter it into the
				 * matrix. First get the appropriate indices.
				 */
				int n = mRNA2index.get(target_gene);
				int m = mirna2index.get(mirna);
				this.miRTargetMatrix[n][m] = true;
				
				/*
				 * fill Matrix with more detailed info for MREs
				 */
				if(this.mres[n][m] == null)
					this.mres[n][m] = new MRElist();
				MRE mre = new MRE();
				mre.setPosition(Integer.parseInt(A[MREstart]));
				mre.setType(checkType(""));
				this.mres[n][m].addMRE(mre);
				this.utr3MREs[n][m]++;

			} /* end while */
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		System.out.println("Input a total of " + mirna2index.size() + " miRNAs");
	}

	/**
	 * This method counts the number of miRNAs and mRNAs and initializes the
	 * HashMaps that will contain the indices of the miRNA/mRNAs and their names
	 * as Strings.
	 */
	private void register_miRNAs_and_mRNAs() {
		String line;
		int currentTranscriptIndex = 0;
		int currentMicroRNAIndex = 0;
		try {
			BufferedReader in = new BufferedReader(new FileReader(this.path));
			/* Skip the header */
			while ((line = in.readLine()) != null) {
				if (line.startsWith("##GROUP") | line.startsWith("GROUP"))
					break;
			}
			/* From here on we should just have data lines */
			while ((line = in.readLine()) != null) {
				String[] A = line.split("\t");
				if (A.length < MIRBASE_NFIELDS)
					continue; /* skip malformed or empty lines */
				String mirna = A[miRNA];
				if (!mirna.startsWith(this.taxon))
					continue; // JUST use mmu predictions
				String target_transcript = A[ENSEMBL_TRANSCRIPT_ID];
				String gene_symbol = A[GENE_SYMBOL_ID];
				if (this.p_threshold > 0) { /*
											 * Check if we are filtering on
											 * pvals
											 */
					String pval = A[PVALUE];
					Double dd = Double.parseDouble(pval);
					if (dd > this.p_threshold)
						continue;
				}
				/* Have we seen this miRNA before? */
				if (null == mirna2index.get(mirna)) {
					this.mirna2index.put(mirna, currentMicroRNAIndex);
					this.index2mirna.put(currentMicroRNAIndex, mirna);
					currentMicroRNAIndex++;
				}
				/* Have we seen this mRNA before? */
				if (null == mRNA2index.get(target_transcript)) {
					this.mRNA2index.put(target_transcript,
							currentTranscriptIndex);
					this.index2mRNA.put(currentTranscriptIndex,
							target_transcript);
					this.index2GeneSymbol.put(currentTranscriptIndex,
							gene_symbol);
					currentTranscriptIndex++;
				}
			} /* end while */
			in.close();
		}
		catch(IOException e){
			System.out.println("Found no file at the specified position.");
//			e.printStackTrace();
			System.exit(1);
		}
//		catch(NullPointerException e){
//			System.out.println("No filename was submitted.");
//			System.exit(1);
//		}
		this.n_miRNA = currentMicroRNAIndex;
		this.n_mRNA = currentTranscriptIndex;
		System.out.println("Input a total of " + this.n_miRNA + " miRNAs" + " and " + this.n_mRNA + " mRNAs");
	}

	/**
	 * Check the first line of the miRBase/biomaRt data file. This is a 'sanity
	 * check' to make sure that the format of the file is as we expect. If not,
	 * the function will terminate the program.
	 */
//	private void checkFirstLineOfDataFile(String line) {
////		System.out.println("hallo");
//		String[] A = line.split("\t");
//		if (A.length != 8)
//			usage("Misformed header line" + line + "\nLength was " + A.length
//					+ " instead of 8");
//		if (!A[0].equals("miRNA"))
//			usage("Misformed header line" + line + "\bBad element 0:\"" + A[0]
//					+ "\"");
//		if (!A[1].equals("ensembl_transcript_id"))
//			usage("Misformed header line" + line + "\bBad element 1:\"" + A[1]
//					+ "\"");
//		if (!A[2].equals("ensembl_gene_id"))
//			usage("Misformed header line" + line + "\bBad element 2:\"" + A[2]
//					+ "\"");
//		if (!A[3].equals("gene"))
//			usage("Misformed header line" + line + "\bBad element 3:\"" + A[3]
//					+ "\"");
//		if (!A[4].equals("database"))
//			usage("Misformed header line" + line + "\bBad element 4:\"" + A[4]
//					+ "\"");
//		if (!A[5].equals("kind"))
//			usage("Misformed header line" + line + "\bBad element 5:\"" + A[5]
//					+ "\"");
//		if (!A[6].equals("score"))
//			usage("Misformed header line" + line + "\bBad element 6:\"" + A[6]
//					+ "\"");
//		if (!A[7].equals("p-value"))
//			usage("Misformed header line" + line + "\bBad element 7:\"" + A[7]
//					+ "\"");
//	}

	/*
	 * 
	 * public void printTranscript2miRNAtoSTDOUT() { Iterator<String> iter =
	 * this.targetGenes.keySet().iterator(); while (iter.hasNext()) { String s =
	 * iter.next(); TargetGene2miRNA t2m = targetGenes.get(s);
	 * t2m.printOutTargetGene(this.index2mirna); } }
	 */
	/*
	 * public void printTranscript2miRNAtoFile(String fname) { try {
	 * BufferedWriter out = new BufferedWriter(new FileWriter(fname));
	 * Iterator<String> iter = this.targetGenes.keySet().iterator(); while
	 * (iter.hasNext()) { String s = iter.next(); TargetGene2miRNA t2m =
	 * targetGenes.get(s); t2m.printTargetGeneToFile(this.index2mirna, out); }
	 * out.close(); } catch (IOException ioe) {
	 * 
	 * ioe.printStackTrace(); System.exit(1); } }
	 */
	/*
	 * public void printMiRNA2TranscriptToFile(String fname) {
	 * 
	 * mirna2index = null; // System.gc(mirna2index); try { BufferedWriter out =
	 * new BufferedWriter(new FileWriter(fname)); Iterator<String> iter =
	 * this.mirnas.keySet().iterator(); while (iter.hasNext()) { String s =
	 * iter.next(); MiRNA2TargetGene m2t = mirnas.get(s);
	 * m2t.printMirnaToFile(this.index2mRNA, out); } out.close(); } catch
	 * (IOException ioe) {
	 * 
	 * ioe.printStackTrace(); System.exit(1); }
	 * 
	 * }
	 */

	public void debugPrintMirna2Index() {
		int i;
		Iterator<String> iter = this.mirna2index.keySet().iterator();
		System.out.println("########## Checking mirna2index ##########");
		System.out.println("Size of HashMap is " + mirna2index.size());
		while (iter.hasNext()) {
			String s = iter.next();
			Integer ii = mirna2index.get(s);
			System.out.println(s + ": " + ii);
		}

	}

	private void usage(String msg) {
		System.err.println(msg);
		System.exit(1);
	}

}