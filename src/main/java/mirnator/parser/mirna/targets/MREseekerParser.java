package mirnator.parser.mirna.targets;

import java.awt.BorderLayout;
import java.io.*;
import java.util.HashSet;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.JTextField;

import mirnator.constants.ExceptionConstants;

public class MREseekerParser extends MiRNAParser {

    // private class MRE{
    // private int[] positions;
    // private int[] types;
    //
    // public int[] getPositions() {
    // return positions;
    // }
    // public int[] getTypes() {
    // return types;
    // }
    //
    // public void setPositions(int[] positions) {
    // this.positions = positions;
    // }
    //
    // public void setTypes(int[] types) {
    // this.types = types;
    // }
    // }

//    public class Gene {
//	private int cdsstart;
//	private int utr3start;
//
//	public int getCdsstart() {
//	    return cdsstart;
//	}
//
//	public int getUtr3start() {
//	    return utr3start;
//	}
//
//	public void setCdsstart(int cdsstart) {
//	    this.cdsstart = cdsstart;
//	}
//
//	public void setUtr3start(int utr3start) {
//	    this.utr3start = utr3start;
//	}
//    }

    static final int GB_access = 0;
    static final int GENE_SYMBOL = 1;
    static final int miR_ID = 2;
    static final int UTR5_hits = 3;
    static final int CDS_hits = 4;
    static final int UTR3_hits = 5;
    static final int UTR5_length = 8;
    static final int CDS_length = 9;
    static final int UTR3_length = 10;
    static final int MRE_positions = 11;
    static final int MRE_types = 12;

    static final int MiRnator_NFIELDS = 13;

    // static final int MRE_M8 = 20;
    // static final int MRE_A1 = 21;
    // static final int MRE_8m = 22;

    protected int 	location;
    private String 	taxonID = "hsa";
    private short[][] 	cdsMREs;
    private short[][] 	utr5MREs;

    private Vector<Gene> mRNAs;

    /**
     * default constructor considering all MREs (CDS and 3'UTR)
     * 
     * @param path
     *            path to the MiRnator outputfile
     */
    public MREseekerParser(String path) {
	this(path, -1);
    }

    public MREseekerParser(String path, String taxon) {
	this(path, -1);
	this.taxonID = taxon;
    }

    /**
     * 
     * @param path
     *            path to the MiRnator outputfile
     * @param location
     *            which MREs should be considered 3 - 5'utr 4 - cds 5 - 3'utr
     *            other - all
     */
    public MREseekerParser(String path, int location) {
	super(path);
	this.location = location;
	this.mRNAs = new Vector<Gene>();
	// TODO add selection to rest of code !!!
    }

    public void parse() {
	int lineCounter	= register_miRNAs_and_mRNAs();
	int mreCounter	= 0;
	
	JFrame frame = new JFrame();
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	JTextField text	= new JTextField("Percentage of miRNAs read:");
	JProgressBar bar = new JProgressBar(0, lineCounter);
	lineCounter = 0;
	bar.setStringPainted(true);
	frame.add(bar, BorderLayout.PAGE_END);
	frame.add(text, BorderLayout.PAGE_START);
	frame.pack();
	frame.setVisible(true);
	
	this.miRTargetMatrix 	= new boolean[n_mRNA][n_miRNA];
	this.cdsMREs 		= new short[n_mRNA][n_miRNA];
	this.utr3MREs 		= new short[n_mRNA][n_miRNA];
	this.utr5MREs 		= new short[n_mRNA][n_miRNA];
	// this.mrePositions = new MRE[n_mRNA][n_miRNA];
	// this.mreTypes = new MRE[n_mRNA][n_miRNA];
	this.mres = new MRElist[n_mRNA][n_miRNA];
	for (int i = 0; i < n_mRNA; ++i)
	    for (int j = 0; j < n_miRNA; ++j)
		miRTargetMatrix[i][j] = false;
//	int currentMessengerRNAIndex = 0;
//	int currentMicroRNAIndex = 0;
	String line;
	try {
	    BufferedReader in = new BufferedReader(new FileReader(this.path));
	    /** Skip header again */
	    // while ((line = in.readLine()) != null) {
	    // if (line.startsWith("GBaccession"))
	    // break;
	    // }

	    while ((line = in.readLine()) != null) {
		if (line.length() < 1)
		    continue;
		if (line.charAt(0) == '#')
		    continue; /* skips comments */
		if (line.startsWith("GBaccession"))
		    continue;
		String[] A = line.split("\t");
		if (A.length != MiRnator_NFIELDS)
		    continue; /* skip malformed or empty lines */
		if (this.location >= 3 && this.location <= 5) {
		    if (Integer.parseInt(A[this.location]) == 0) {
			continue;
		    }
		}
		String mirna = A[miR_ID];
		// System.out.println("Added "+mirna);
		if (!mirna.startsWith(this.taxonID))
		    continue;
		String target_gene = A[GB_access];
		/*
		 * If we get here, we have a miRNA/mRNA pair. Enter it into the
		 * matrix. First get the appropriate indices.
		 */
		int n = mRNA2index.get(target_gene);
		int m = mirna2index.get(mirna);
		this.miRTargetMatrix[n][m] = true;

		/*
		 * now check for the localizations of the MREs and save in the
		 * arrays
		 */
		this.utr5MREs[n][m] = Short.parseShort(A[MREseekerParser.UTR5_hits]); // (Integer.parseInt(A[this.UTR5_hits])
									   // >
									   // 0)
									   // ?
									   // true
									   // :
									   // false;
		this.cdsMREs[n][m] = Short.parseShort(A[MREseekerParser.CDS_hits]); // (Integer.parseInt(A[this.CDS_hits])
									 // > 0)
									 // ?
									 // true
									 // :
									 // false;
		this.utr3MREs[n][m] = Short.parseShort(A[MREseekerParser.UTR3_hits]); // (Integer.parseInt(A[this.UTR3_hits])
									   // >
									   // 0)
									   // ?
									   // true
									   // :
									   // false;

		// save position and type of MRE

		String[] pos = A[MREseekerParser.MRE_positions].split(" ");
		int l = pos.length;
		String[] type = A[MREseekerParser.MRE_types].split(" ");
		int l2 = type.length;
		if (l != l2) {
		    System.err.println("number of MRE positions and types dosn't match - will exit");
		    System.exit(0);
		}

		// position
		this.mres[n][m] = new MRElist();
		// int[] posInt = new int[l];
		// int[] typeInt = new int[l];
		for (int i = 0; i < l; i++) {
		    mreCounter++;
		    MRE mre = new MRE();
		    // if(type[i].equals("M8"))
		    // mre.setType(this.MRE_M8);
		    // if(type[i].equals("A1"))
		    // mre.setType(this.MRE_A1);
		    // if(type[i].equals("8m"))
		    // mre.setType(this.MRE_8m);
		    mre.setType(checkType(type[i]));
		    mre.setPosition(Integer.parseInt(pos[i]));
		    this.mres[n][m].addMRE(mre);
		}
		bar.setValue(lineCounter++);

	    } /* end while */
	    frame.dispose();
	    in.close();
	} catch (IOException e) {
	    System.out.println("Found no file at specified position or no filename was submitted.");
	    // e.printStackTrace();
	    System.exit(ExceptionConstants.MREfilemissing);
	}
	// System.out.println("Input a total of " + mirna2index.size() +
	// " miRNAs");
	System.out.println("Found and initialized "+mreCounter+" MREs.");
    }

    /**
     * This method counts the number of miRNAs and mRNAs and initializes the
     * HashMaps that will contain the indices of the miRNA/mRNAs and their names
     * as Strings.
     */
    private int register_miRNAs_and_mRNAs() {
	String line;
	int currentTranscriptIndex = 0;
	int currentMicroRNAIndex = 0;
	int linecounter = 0;
	try {
	    BufferedReader in = new BufferedReader(new FileReader(this.path));
	    // check header line
	    if ((line = in.readLine()) != null) {
		checkHeader(line);
	    }

	    while ((line = in.readLine()) != null) {
		linecounter++;
//		 System.out.println("Hallo");
		String[] A = line.split("\t");
		if (A.length != MiRnator_NFIELDS)
		    continue; /* skip malformed or empty lines */
//		System.out.println("hallo");
		String mirna = A[miR_ID];
		if (!mirna.startsWith(this.taxonID))
		    continue; // JUST use mmu predictions
//		System.out.println("hallo");
		if (this.location >= 3 && this.location <= 5) {
		    if (Integer.parseInt(A[this.location]) == 0) {
			continue;
		    }
		}
//		System.out.println("hallo");
		String target_transcript = A[GB_access];
		String gene_symbol = A[GENE_SYMBOL];
		/* Have we seen this miRNA before? */
		if (null == mirna2index.get(mirna)) {
		    this.mirna2index.put(mirna, currentMicroRNAIndex);
		    this.index2mirna.put(currentMicroRNAIndex, mirna);
		    currentMicroRNAIndex++;
		}
		/* Have we seen this mRNA before? */
		if (null == mRNA2index.get(target_transcript)) {
		    this.mRNA2index.put(target_transcript, currentTranscriptIndex);
		    this.index2mRNA.put(currentTranscriptIndex, target_transcript);
		    this.index2GeneSymbol.put(currentTranscriptIndex, gene_symbol);
		    Gene gen = new Gene();
		    gen.setCdsstart(Integer.parseInt(A[MREseekerParser.UTR5_length]));
		    gen.setCdsend(Integer.parseInt(A[MREseekerParser.UTR5_length]) + Integer.parseInt(A[MREseekerParser.CDS_length]));
		    this.mRNAs.add(currentTranscriptIndex, gen);
		    currentTranscriptIndex++;
		}
	    }
	    in.close();
	} catch (IOException e) {
	    System.out.println("Found no file at the specified position.");
	    // e.printStackTrace();
	    System.exit(1);
	} catch (NullPointerException e) {
	    System.out.println("No filename was submitted.");
	    System.exit(1);
	}
	this.n_miRNA = currentMicroRNAIndex;
	this.n_mRNA = currentTranscriptIndex;
	System.out.println("Input a total of " + this.n_miRNA + " miRNAs" + " and " + this.n_mRNA + " mRNAs");
	return linecounter;
    }

    private void checkHeader(String line) {
	String[] A = line.split("\t");
	String[] head = { "GBaccession", "gene", "miRNA", "5utr", "cds", "3utr", "5utrcds", "cds3utr", "5utrlength", "cdslength", "3utrlength", "positions",
		"type" };

	if (A.length != MiRnator_NFIELDS)
	    usage("Misformed header line" + line + "\nLength was " + A.length + " instead of " + MiRnator_NFIELDS);
	for (int i = 0; i < MiRnator_NFIELDS; i++) {
	    if (!A[i].equals(head[i]))
		usage("Misformed header line" + line + "\bBad element " + i + ":\"" + A[i] + "\"");
	}
    }

    private void usage(String msg) {
	System.err.println(msg);
	System.exit(1);
    }

    public HashSet<String> getAllTarget_3utr_GeneSymbols(String miRNA) {
	HashSet<String> hs = new HashSet<String>();
	if (mirna2index == null)
	    System.out.println("mirna2index is null");
	int n, m;
	if (!mirna2index.containsKey(miRNA))
	    return hs;
	m = mirna2index.get(miRNA); /* The correct column for this miR */
	for (n = 0; n < n_mRNA; ++n) {
	    if (this.utr3MREs[n][m] > 0) {
		String target = index2GeneSymbol.get(n);
		hs.add(target.toLowerCase());
	    }
	}
	return hs;
    }

    public HashSet<String> getAllTarget_cds_GeneSymbols(String miRNA) {
	HashSet<String> hs = new HashSet<String>();
	if (mirna2index == null)
	    System.out.println("mirna2index is null");
	int n, m;
	if (!mirna2index.containsKey(miRNA))
	    return hs;
	m = mirna2index.get(miRNA); /* The correct column for this miR */
	for (n = 0; n < n_mRNA; ++n) {
	    if (this.cdsMREs[n][m] > 0) {
		String target = index2GeneSymbol.get(n);
		hs.add(target.toLowerCase());
	    }
	}
	return hs;
    }

    /**
     * This method returns the target genes for a given miRNA, satisfying the
     * additional condition: depending on c It is known that multiple sites have
     * additive or cooperative effects, especially if located within about 40
     * nucleotides (nt), but no closer than 8 nt to one another. IF (0 < c < 9)
     * - number of MREs in cds is considered IF (8 < c < 41) - distance between
     * MREs is considered ELSE all targets
     * 
     * @param miRNA
     *            - name of microRNA (mmu-miR-29a)
     * @param c
     *            - number of MREs/minimal distance
     * @return List of target genes, full satisfying the given conditions
     */
    public HashSet<String> getAllTarget_cds_GeneSymbols(String miRNA, int c) {
	HashSet<String> hs = new HashSet<String>();
	if (mirna2index == null)
	    System.out.println("mirna2index is null");
	int n, m;
	if (!mirna2index.containsKey(miRNA))
	    return hs;
	m = mirna2index.get(miRNA); /* The correct column for this miR */
	for (n = 0; n < n_mRNA; ++n) {
	    if (this.cdsMREs[n][m] > 0) {
		String target = index2GeneSymbol.get(n);
		Gene gene = mRNAs.get(n);
		// TODO check for C
		if (c > 0 & c < 9) {
		    if (this.cdsMREs[n][m] >= c)
			hs.add(target.toLowerCase());
		} else if (c > 0 & c < 41) {
		    // check for distance between MREs
		    MRElist mre = this.mres[n][m];
		    if ((this.cdsMREs[n][m] > 1) && checkCdsMreDistance(c, gene, mre)) {
			hs.add(target.toLowerCase());
		    }
		} else
		    hs.add(target.toLowerCase());
	    }
	}
	return hs;
    }

    /**
     * Checks if there is another MRE within the max. distance on the same gene.
     * 
     * @param c
     *            - max. distance
     * @param gene
     *            - the gene
     * @param mre
     *            - list with MREs located on the gene
     * @return <code>true</code> if there is another MRE within the max.
     *         distance, otherwise <code>false</code>
     */
    private boolean checkCdsMreDistance(int c, Gene gene, MRElist mre) {
	int start = gene.getCdsstart();
	int end = gene.getCdsend();
	int[] positions = mre.getPositions();
	for (int i = 0; i < positions.length; i++) {
	    if (positions[i] > start && positions[i] < end) {
		for (int j = i + 1; j < positions.length; j++) {
		    if (positions[j] > end)
			break;
		    int dist = positions[j] - positions[i];
		    if ((dist > 8) && (dist <= c))
			return true;
		}
	    }
	}
	return false;
    }

    /**
     * This method returns the target genes for a given miRNA. ELSE all targets
     * 
     * @param miRNA
     *            - name of microRNA (mmu-miR-29a)
     * @return List of target genes, full satisfying the given conditions
     */
    public HashSet<String> getAllTarget_5utr_GeneSymbols(String miRNA) {
	HashSet<String> hs = new HashSet<String>();
	if (mirna2index == null)
	    System.out.println("mirna2index is null");
	int n, m;
	if (!mirna2index.containsKey(miRNA))
	    return hs;
	m = mirna2index.get(miRNA); /* The correct column for this miR */
	for (n = 0; n < n_mRNA; ++n) {
	    if (this.utr5MREs[n][m] > 0) {
		String target = index2GeneSymbol.get(n);
		hs.add(target.toLowerCase());
	    }
	}
	return hs;
    }

    /**
     * This method returns the target genes for a given miRNA, satisfying the
     * additional condition: depending on c It is known that multiple sites have
     * additive or cooperative effects, especially if located within about 40
     * nucleotides (nt), but no closer than 8 nt to one another. IF (0 < c < 9)
     * - number of MREs in cds is considered IF (8 < c < 41) - distance between
     * MREs is considered ELSE all targets
     * 
     * @param miRNA
     *            - name of microRNA (mmu-miR-29a)
     * @param c
     *            - number of MREs/minimal distance
     * @return List of target genes, full satisfying the given conditions
     */
    public HashSet<String> getAllTarget_5utr_GeneSymbols(String miRNA, int c) {
	HashSet<String> hs = new HashSet<String>();
	if (mirna2index == null)
	    System.out.println("mirna2index is null");
	int n, m;
	if (!mirna2index.containsKey(miRNA))
	    return hs;
	m = mirna2index.get(miRNA); /* The correct column for this miR */
	for (n = 0; n < n_mRNA; ++n) {
	    if (this.utr5MREs[n][m] > 0) {
		String target = index2GeneSymbol.get(n);
		Gene gene = mRNAs.get(n);
		// TODO check for C
		if (c > 0 & c < 9) {
		    if (this.utr5MREs[n][m] >= c)
			hs.add(target.toLowerCase());
		} else if (c > 0 & c < 41) {
		    // check for distance between MREs
		    MRElist mre = this.mres[n][m];
		    if ((this.utr5MREs[n][m] > 1) && checkutr5MreDistance(c, gene, mre)) {
			hs.add(target.toLowerCase());
		    }
		} else
		    hs.add(target.toLowerCase());
	    }
	}
	return hs;
    }

    private boolean checkutr5MreDistance(int c, Gene gene, MRElist mre) {
	int start = 0;
	int end = gene.getCdsstart();
	int[] positions = mre.getPositions();
	for (int i = 0; i < positions.length; i++) {
	    if (positions[i] > start && positions[i] < end) {
		for (int j = i + 1; j < positions.length; j++) {
		    if (positions[j] > end)
			break;
		    int dist = positions[j] - positions[i];
		    if ((dist > 8) && (dist <= c))
			return true;
		}
	    }
	}
	return false;
    }

}
