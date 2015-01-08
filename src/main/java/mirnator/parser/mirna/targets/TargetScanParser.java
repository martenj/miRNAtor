package mirnator.parser.mirna.targets;

import java.io.*;


/**
 * Extension of the abstract miRNAParser base class
 * 
 * TODO: STUMP UNFINISHED !!!
 * 
 * @author mjaeger
 *
 */

public class TargetScanParser extends MiRNAParser {

	/**
	 * Indices of corresponding objects in the perl-parsed target scan input
	 * file
	 */
	static final int miR_INDEX = 0;
	static final int ENTREZ_GENE_ID = 1;
	static final int GENE_SYMBOL = 2;
	static final int TAXON = 3;
	static final int MREstart = 4;	//TODO check if they mixed up UTRstart and MSAstart
	static final int MREtype = 8;
	static final int PCT = 9;
	static final int TARGETSCAN_NFIELDS = 10;

	protected String taxonID;
	protected boolean useGeneSymbol	= false;
	
	/**
	 * Generates a TargetScanParser class for TargetScan files of type: <br>
	 * Predicted_Targets_Info.txt<br>
	 * Conserved_Family_Info.txt<br>
	 * Nonconserved_Family_Info.txt<br>
	 * The default target Identifier is EntrezGene.
	 * @param path - path to the TargetScan file 
	 * @param taxon - species taxon id (NCBI) e.g. hsa for homo sapiens
	 */
	public TargetScanParser(String path, String taxon) {
		this(path, taxon, -1.0,false); /* Call CTOR with flag for no p-value threshold. */
	}

	/**
	 * Generates a TargetScanParser class for TargetScan files of type: <br>
	 * Predicted_Targets_Info.txt<br>
	 * Conserved_Family_Info.txt<br>
	 * Nonconserved_Family_Info.txt<br>
	 * The default target Identifier is EntrezGene.
	 * @param path - path to the TargetScan file 
	 * @param taxon - species taxon id (NCBI) e.g. hsa for homo sapiens
	 * @param T - percent of conservet targeting (unimplemented)
	 */
	public TargetScanParser(String path, String taxon, double T) {
		this(path,taxon+"",T,false);
	}

	/**
	 * Generates a TargetScanParser class for TargetScan files of type: <br>
	 * Predicted_Targets_Info.txt<br>
	 * Conserved_Family_Info.txt<br>
	 * Nonconserved_Family_Info.txt<br>
	 * The default target Identifier is EntrezGene.
	 * @param path - path to the TargetScan file 
	 * @param taxon - species taxon id (NCBI) e.g. hsa for homo sapiens
	 */
	public TargetScanParser(String path, int taxon) {
		this(path,taxon+"",-1.0,false);
	}
	
	/**
	 * Generates a TargetScanParser class for TargetScan files of type: <br>
	 * Predicted_Targets_Info.txt<br>
	 * Conserved_Family_Info.txt<br>
	 * Nonconserved_Family_Info.txt<br>
	 * The default target Identifier is EntrezGene, but can be switched to GeneSymbol.
	 * @param path - path to the TargetScan file 
	 * @param taxon - species taxon id (NCBI) e.g. hsa for homo sapiens
	 * @param useGeneSymbol - should the GeneSymbol used instead of EntrezGeneID
	 */
	public TargetScanParser(String path, String taxon, boolean useGeneSymbol) {
		this(path, taxon, -1.0,useGeneSymbol); /* Call CTOR with flag for no p-value threshold. */
	}
	
	/**
	 * Generates a TargetScanParser class for TargetScan files of type: <br>
	 * Predicted_Targets_Info.txt<br>
	 * Conserved_Family_Info.txt<br>
	 * Nonconserved_Family_Info.txt<br>
	 * The default target Identifier is EntrezGene, but can be switched to GeneSymbol.
	 * @param path - path to the TargetScan file 
	 * @param taxon - species taxon id (NCBI) e.g. hsa for homo sapiens
	 * @param T - percent of conservet targeting (unimplemented)
	 * @param useGeneSymbol - should the GeneSymbol used instead of EntrezGeneID
	 */
	public TargetScanParser(String path, String taxon, double T, boolean useGeneSymbol) {
		super(path);
		this.taxonID 		= taxon;
		this.p_threshold 	= T;
		this.useGeneSymbol	= useGeneSymbol;
	}
	
	/**
	 * Generates a TargetScanParser class for TargetScan files of type: <br>
	 * Predicted_Targets_Info.txt<br>
	 * Conserved_Family_Info.txt<br>
	 * Nonconserved_Family_Info.txt<br>
	 * The default target Identifier is EntrezGene, but can be switched to GeneSymbol.
	 * @param path - path to the TargetScan file 
	 * @param taxon - species taxon id (NCBI) e.g. 9096 for homo sapiens
	 * @param useGeneSymbol - should the GeneSymbol used instead of EntrezGeneID
	 */
	public TargetScanParser(String path, int taxon, boolean useGeneSymbol) {
		this(path,taxon+"",-1.0,useGeneSymbol);
	}


	/**
	 * Main parsing method. Read in entire TargetScan file. Make a map of
	 * mRNA=>miRNAs and miRNAs => mRNA. Note that the mRNAs are registered as
	 * Entrez Gene IDs, rather than as ensembl transcript IDs.
	 */

	public void parse() {
		register_miRNAs_and_mRNAs();
		System.out.println("TargetScanParser, file = " + path);
		this.miRTargetMatrix = new boolean[n_mRNA][n_miRNA];
		this.mres = new MRElist[n_mRNA][n_miRNA];
		this.utr3MREs = new short[n_mRNA][n_miRNA];
		for (int i = 0; i < n_mRNA; ++i)
			for (int j = 0; j < n_miRNA; ++j)
				miRTargetMatrix[i][j] = false;
		int currentMessengerRNAIndex = 0;
		int currentMicroRNAIndex = 0;
		String mirna; /* A miR or miR family */
		String geneID;
		
		MRE mre;
		
		String line;
		try {
			BufferedReader in = new BufferedReader(new FileReader(this.path));
			/* skip first line */
			if ((line = in.readLine()) != null) {
				String A[] = line.split("\t");
				if ((A.length != TARGETSCAN_NFIELDS)
						|| (!A[0].equals("miR Family"))) {
					System.err.println("Target scan file malformed ");
					System.exit(1);
				}
			}
			while ((line = in.readLine()) != null) {
				String[] A = line.split("\t");
				if (A.length < TARGETSCAN_NFIELDS) {

					continue; /* skip malformed or empty lines */
				}
				if (!this.taxonID.equals(A[TAXON]))
					continue; /* Skip wrong species */

				mirna = A[miR_INDEX]; /* A miR or miR family */
				if(this.useGeneSymbol)
				    geneID = A[GENE_SYMBOL].toLowerCase();
				else
				    geneID = A[ENTREZ_GENE_ID];
				

				/*
				 * If we get here, we have a miRNA/mRNA pair. Enter it into the
				 * matrix. First get the appropriate indices.
				 */
				currentMessengerRNAIndex = mRNA2index.get(geneID);
				currentMicroRNAIndex = mirna2index.get(mirna);
				this.miRTargetMatrix[currentMessengerRNAIndex][currentMicroRNAIndex] = true;
				
				/*
				 * fill Matrix with more detailed info for MREs
				 */
				if(this.mres[currentMessengerRNAIndex][currentMicroRNAIndex] == null)
					this.mres[currentMessengerRNAIndex][currentMicroRNAIndex] = new MRElist();
				mre = new MRE();
				mre.setPosition(Integer.parseInt(A[MREstart]));
				mre.setType(checkType(A[MREtype]));
				this.mres[currentMessengerRNAIndex][currentMicroRNAIndex].addMRE(mre);
				this.utr3MREs[currentMessengerRNAIndex][currentMicroRNAIndex]++;

			} /* end while */
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		System.out.println("Input a total of " + mirna2index.size() + " miRNAs");
	}

	/**
	 * Get a complete list of miRs from the target scan file. Note that
	 * TargetScan combines miR families. This program will separate them into
	 * separate miRs.
	 */
	private void register_miRNAs_and_mRNAs() {
		String line;
		int currentTranscriptIndex = 0;
		int currentMicroRNAIndex = 0;
		int badLines = 0;
		
		String mirna; /* A miR or miR family */
		String geneID;
		String genesymbol;
		MRE mre;
		
		try {
			BufferedReader in = new BufferedReader(new FileReader(this.path));
			/* First check that first line is OK */
			if ((line = in.readLine()) != null) {
				String A[] = line.split("\t");
				if (A.length != TARGETSCAN_NFIELDS) {
					System.err.println("Target scan file malformed (" + A.length + " fields):\n" + line);
					System.exit(1);
				}
				if (!A[0].equals("miR Family")) {
					System.err.println("Target scan file malformed (first header field: " + A[0] + ")");
					System.exit(1);
				}
			}
			/* From here on we should just have data lines */
			while ((line = in.readLine()) != null) {
				String[] A = line.split("\t");
				if (A.length < TARGETSCAN_NFIELDS) {
					badLines++;
					continue; /* skip malformed or empty lines */
				}
				if (!this.taxonID.equals(A[TAXON]))
					continue; /* Skip wrong species */

				mirna = A[miR_INDEX]; /* A miR or miR family */
				if(this.useGeneSymbol)
				    geneID 	= A[GENE_SYMBOL].toLowerCase();
				else
				    geneID 	= A[ENTREZ_GENE_ID];
				genesymbol	= A[GENE_SYMBOL].toLowerCase();
				
				/* Have we seen this miRNA before? */
				if (!mirna2index.containsKey(mirna)) {
//				if (null == mirna2index.get(mirna)) {
					this.mirna2index.put(mirna, currentMicroRNAIndex);
					this.index2mirna.put(currentMicroRNAIndex, mirna);
					currentMicroRNAIndex++;
				}
				/* Have we seen this mRNA before? */
				if(!mRNA2index.containsKey(geneID)){
//				if (null == mRNA2index.get(entrezID)) {
					this.mRNA2index.put(geneID, currentTranscriptIndex);
					this.index2mRNA.put(currentTranscriptIndex, geneID);
					this.index2GeneSymbol.put(currentTranscriptIndex,genesymbol);
					currentTranscriptIndex++;
				}
			} /* end while */
			
			System.out.println("#mRNAs: "+currentTranscriptIndex +"\tbadline: "+badLines);
			// System.out.println(miRFam + ": " + entrezID + " :" + GeneSymbol);
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
		if (badLines > 0) {
			System.out.println("Bad lines: " + badLines);
			System.exit(1);
		}
		this.n_miRNA = currentMicroRNAIndex;
		this.n_mRNA = currentTranscriptIndex;
		System.out.println("Input a total of " + this.n_miRNA + " miRNAs"
				+ " and " + this.n_mRNA + " mRNAs");
	}

}