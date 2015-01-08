//package de.charite.mirnator.mirnapredict;
//
//import java.io.*;
//
//import de.charite.mirnator.parser.genbank.GenBankReader;
//import de.charite.mirnator.sql2java.KgsequenceBean;
//import de.charite.mirnator.sql2java.KnowngeneBean;
//import de.charite.mirnator.sql2java.MirnaBean;
//import de.charite.mirnator.structs.genbank.GenBankGene;
//
//public class MREseekerUCSC {
//    
//    
//    private KnowngeneBean 	knownGene;
//    private String		mRNA_sequence;
//    private MirnaBean		miRNA;
//
////    private String seqName;
////    private String seq;
////    private String miRNA;
//    private String miRNA_name;
//    private String rev_miRNA;
//    private String seed;
//    private String revSeed;
//    private String comp;
//    private String revComp;
////    private GenBankReader gbr;
////    private GenBankGene gbg;
//
//    private int match_type;
//    private boolean withComp = false;
//
//    static final int SEVEN_A1 = 0;
//    static final int SEVEN_M8 = 1;
//    static final int EIGHT = 2;
//    static final int SIX = 3;
//    static final int OFFSET_SIX = 4;
//    static final int COMPENSATORY17 = 5;
//    static final int COMPENSATORY28 = 6;
//    static final int COMPENSATORY18 = 7;
//
//    static final int NO_MATCH = 99;
//
//    static final int MAXWOBBLE = 0; // Maximum number of wobble matches
//
//    private int n_seven_a1 = 0;
//    private int n_seven_m8 = 0;
//    private int n_eight = 0;
//    private int n_six = 0;
//    private int n_offset_six = 0;
//    private int n_compens = 0;
//
//    private char[] match;
//    private char[] compmatch;
//
//
////
////    public MREseekerUCSC(GenBankGene gbgene, String mirna, String miRNA_name) {
////	this.miRNA 	= mirna;
////	this.miRNA_name = miRNA_name;
////	this.seqName 	= gbgene.getName();
////	this.seq 	= gbgene.get_mRNA();
////	this.gbg 	= gbgene;
////	this.match 	= new char[8];
////	parseSeed();
////	// parseComp();
////    }
//    
//    public MREseekerUCSC(KnowngeneBean kgBean,String kgSequence, MirnaBean mirnaBean){
//	
//	this.knownGene		= kgBean;
//	this.mRNA_sequence	= kgSequence;
//	this.miRNA		= mirnaBean;
//	
////	this.seqName	= kgBean.getKnowngeneRef()+"";
////	this.seq	= kgBean.getMrnaSequence();
////	this.miRNA	= mirnaBean.getMirnaSequence();
////	this.miRNA_name	= mirnaBean.getMirnaName();
//	parseSeed();
//    }
//
////    public void parseGenbank(String filename) {
////	this.gbr = new GenBankReader(filename);
////	seq = gbr.get_cDNA();
////
////    }
//
////    /**
////     * Parse single Faste file for MRE search
////     * 
////     * @param filename
////     */
////    public void parseFASTA(String filename) {
////	try {
////	    FileInputStream fstream = new FileInputStream(filename);
////	    DataInputStream in = new DataInputStream(fstream);
////	    BufferedReader br = new BufferedReader(new InputStreamReader(in));
////	    String str;
////	    if ((str = br.readLine()) != null) {
////		if (str.charAt(0) == '>')
////		    str = str.substring(1);
////		else {
////		    System.err.println("Malformed FASTA line: " + str);
////		    System.exit(1);
////		}
////		this.seqName = str;
////	    }
////	    /* The rest should be nucleotide sequence */
////	    String dna = "";
////	    while ((str = br.readLine()) != null) {
////		dna += str.trim();
////	    }
////	    seq = dna;
////	    in.close();
////	} catch (Exception e) {
////	    System.err.println("Error: " + e.getMessage());
////	    System.exit(1);
////	}
////    }
//
//    /**
//     * Take the first 8 nucleotides (seed)
//     * 
//     */
//    public void parseSeed() {
//	if (miRNA.getMirnaSequence().length() >= 8) {
//	    this.seed = miRNA.getMirnaSequence().substring(0, 8).toUpperCase();
//	} else {
//	    System.out.println("Enter at least 8 nt miRNA");
//	    System.exit(1);
//	}
//	StringBuffer sb = new StringBuffer(seed);
//	this.revSeed = sb.reverse().toString().toUpperCase();
//	sb = new StringBuffer(miRNA.getMirnaSequence());
//	this.rev_miRNA = sb.reverse().toString().toUpperCase();
//    }
//
//    /**
//     * Take the compensatory side (nt 13-19)
//     */
//    public void parseComp() {
//	if (miRNA.getMirnaSequence().length() >= 19) {
//	    this.comp = miRNA.getMirnaSequence().substring(12, 19).toUpperCase();
//	    this.withComp = true;
//	} else {
//	    System.out.println("No compensatory sides - to short miRNA sequence");
//	}
//	StringBuffer sb = new StringBuffer(comp);
//	this.revComp = sb.reverse().toString().toUpperCase();
//    }
//
//    /**
//     * calculates the match-string containing of '|' - perfect match ':' lose
//     * match ' ' mismatch
//     * 
//     * @param target
//     *            - 8bp long substring originate from (target) mRNA
//     * @return number of matching bp between seed and target substring
//     */
//    protected int calculate_match(String target) {
//	/* Reset */
//	for (int i = 0; i < 8; ++i)
//	    match[i] = ' ';
//	int c = 0;
//	for (int j = 0; j < 8; ++j) {
//	    char Q = revSeed.charAt(j);
//
//	    char T = target.charAt(j);
//	    if (Q == 'A' && T == 'T') {
//		c++;
//		match[j] = '|';
//	    } else if ((Q == 'U' || Q == 'T') && (T == 'A')) {
//		c++;
//		match[j] = '|';
//	    }
//	    // else if ((Q=='U'||Q=='T') && (T=='G')) { c++;match[j] = ':'; }
//	    else if (Q == 'C' && T == 'G') {
//		c++;
//		match[j] = '|';
//	    } else if (Q == 'G' && T == 'C') {
//		c++;
//		match[j] = '|';
//	    }
//	    // else if (Q=='G' && T=='T') { c++;match[j] = ':'; }
//	    else
//		match[j] = ' ';
//	}
//	return c;
//    }
//
//    /**
//     * calculates compensatory sites TODO double check
//     * 
//     * @param target
//     * @return
//     */
//    protected int calculate_compensatory(String target) {
//	int c = this.comp.length() - 1;
//	boolean mismatch = false;
//
//	// char[] mymatch = new char[this.comp.length()];
//	// while(c>=0 ) {
//	// char Q = revComp.charAt(c);
//	// char T = target.charAt(c);
//	//
//	// if (Q=='A' && T=='T') {mymatch[c] = '|'; }
//	// else if ((Q=='U'||Q=='T') && (T=='A')) { mymatch[c] = '|'; }
//	// else if (Q=='C' && T=='G') {mymatch[c] = '|'; }
//	// else if (Q=='G' && T=='C') { mymatch[c] = '|';}
//	// else {mymatch[c] = ' ';}
//	// c--;
//	// }
//	// System.err.println("Compensatory:\n"+target +"\n"+new
//	// String(mymatch)+"\n"+revComp);
//	// c = this.comp.length()-1;
//
//	while (c >= 0 && !mismatch) {
//	    char Q = revComp.charAt(c);
//	    char T = target.charAt(c);
//
//	    if (Q == 'A' && T == 'T') {
//		c--;
//	    } else if ((Q == 'U' || Q == 'T') && (T == 'A')) {
//		c--;
//	    } else if (Q == 'C' && T == 'G') {
//		c--;
//	    } else if (Q == 'G' && T == 'C') {
//		c--;
//	    } else
//		mismatch = true;
//	    // System.err.println(c);
//
//	}
//	// int h = this.comp.length() - c-1;
//	// System.err.println("length:"+ h);
//	return this.comp.length() - c - 1;
//    }
//
////    public void printSummary() {
////	int len = seq.length();
////	int CDS = 0;
////	int UTR = 0;
////	for (int i = 0; i < len - 8; ++i) {
////	    String target = seq.substring(i, i + 8);
////	    int c = calculate_match(target);
////
////	    if (c < 6)
////		continue; /* Cannot be valid match */
////	    if (isValidMatch(match, target.charAt(7))) {
////		int end = 0;
////		int start = 0;
////		if (match_type == EIGHT)
////		    end = start + 7;
////		else if (match_type == SEVEN_M8)
////		    end = start + 6;
////		else if (match_type == SEVEN_A1) {
////		    end = start + 7;
////		    start++;
////		} else if (match_type == SIX) {
////		    end = start + 6;
////		    start++;
////		} else if (match_type == OFFSET_SIX) {
////		    end = start + 5;
////		}
////		if (gbr.is_3UTR(start, end))
////		    UTR++;
////		else if (gbr.isCDS(start, end))
////		    CDS++;
////	    }
////	}
////	System.out.println("\n##" + gbr.get_definition() + "--" + this.miRNA_name);
////	System.out.println("\tCDS hits: " + CDS + "\t3'UTR hits: " + UTR);
////    }
//
//    public void printresult(int seedvariant) {
//	this.printresult(seedvariant, null);
//    }
//
//    public void printresult(int seedvariant, OutputStream out) {
//	int len = seq.length();
//	int CDS = 0;
//	int UTR3 = 0;
//	int UTR5 = 0;
//	int UTR5CDS = 0;
//	int CDSUTR3 = 0;
//	String pos = "";
//	String type = "";
//	for (int i = 0; i < len - 8; ++i) {
//	    String target = seq.substring(i, i + 8);
//	    int c = calculate_match(target);
//
//	    if (c < 6)
//		continue; /* Cannot be valid match */
//	    if (isValidMatch(match, target.charAt(7))) {
//		// System.out.println("length "+c+"\tmatch: "+new
//		// String(match));
//		int end = 0;
//		int start = i + 1; // TODO ist das +1 für start richtig?
//		if (c == 8 && seedvariant == 2) { /* only perfect matches */
//		    if (match_type == EIGHT)
//			end = start + 7;
//		}
//		if (seedvariant == 3) { /* only perfect matches */
//		    if (match_type == EIGHT)
//			end = start + 7;
//		    if (match_type == SEVEN_M8)
//			end = start + 6;
//		}
//		if (seedvariant == 4) { /* only perfect matches */
//		    if (match_type == EIGHT)
//			end = start + 7;
//		    if (match_type == SEVEN_A1) {
//			end = start + 7;
//			start++;
//		    }
//		}
//		if (seedvariant == 1) {
//		    if (match_type == EIGHT)
//			end = start + 7;
//		    else if (match_type == SEVEN_M8)
//			end = start + 6;
//		    else if (match_type == SEVEN_A1) {
//			end = start + 7;
//			start++;
//		    }
//		}
//		if (seedvariant == 0) {
//		    if (match_type == EIGHT)
//			end = start + 7;
//		    else if (match_type == SEVEN_M8)
//			end = start + 6;
//		    else if (match_type == SEVEN_A1) {
//			end = start + 7;
//			start++;
//		    } else if (match_type == SIX) {
//			end = start + 6;
//			start++;
//		    } else if (match_type == OFFSET_SIX) {
//			end = start + 5;
//		    }
//		}
//		if (seedvariant == 5) {
//		    if (match_type == EIGHT)
//			end = start + 7;
//		    else if (match_type == SEVEN_M8)
//			end = start + 6;
//		    else if (match_type == SEVEN_A1) {
//			end = start + 7;
//			start++;
//		    } else if (match_type == SIX) {
//			end = start + 6;
//			start++;
//		    } else if (match_type == OFFSET_SIX) {
//			end = start + 5;
//		    } else if (match_type == COMPENSATORY17) {
//			end = start + 7;
//			start++;
//		    } else if (match_type == COMPENSATORY28) {
//			end = start + 6;
//		    } else if (match_type == COMPENSATORY18) {
//			end = start + 7;
//		    }
//
//		    if (i > 11 & match_type >= COMPENSATORY17 && match_type <= COMPENSATORY18) {
//			// TODO check compensatory side
//			// System.err.println(seq.substring(i-14,i+8)+"\n"+this.rev_miRNA);
//			boolean check_comp = false;
//			for (int j = 0; j < 5; j++) {
//			    int cc = calculate_compensatory(seq.substring(i - 9 - j, i - 2 - j));
//			    // System.err.println("Comp-match-size: "+cc);
//			    if (cc > 3)
//				check_comp = true;
//			}
//			if (check_comp) {
//			    end = start;
//			    System.err.println("Found valid compensatory match.");
//			}
//		    }
//
//		}
//
//		if (start < end) {
//		    pos += start + " ";
//		    type += getMatchType() + " ";
//		    // System.out.println("position: "+start+" - "+end);
//		    // System.out.println("CDSstart: "+gbg.getCDSstart()+"\tstart: "+start+"\nCDSend: "+gbg.getCDSend()+"\tend: "+end);
//
//		    if (gbg.is_3UTR(start, end))
//			UTR3++;
//		    else if (gbg.isCDS(start, end))
//			CDS++;
//		    else if (gbg.is_5UTR(start, end))
//			UTR5++;
//		    else if (gbg.is_5UTRCDS(start, end))
//			UTR5CDS++;
//		    else if (gbg.is_CDS3UTR(start, end))
//			CDSUTR3++;
//		    // System.err.println(gbg.getName() +"\t"+ this.miRNA_name
//		    // +"\t"+ start +"\t"+ end+
//		    // "\n\t3'-"+target +"-5'\n\t   " + new String(match)
//		    // +"\n\t5'-" + revSeed + "-3'");
//		}
//	    }
//	}
//	if (UTR3 + UTR5 + CDS + UTR5CDS + CDSUTR3 > 0) {
//	    // System.out.println("\n##" + gbg.get_definition() + "--" +
//	    // this.miRNA_name);
//	    // System.out.println("\tCDS hits: " + CDS + "\t3'UTR hits: " + UTR3
//	    // + "\t5'UTR hits: " + UTR5);
//
//	    // System.out.println(gbg.getAccession()+"\t"+gbg.getName() +"\t"+
//	    // this.miRNA_name +"\t"+ UTR5 +"\t"+ CDS +"\t"+ UTR3 +"\t"+
//	    // UTR5CDS +"\t"+ CDSUTR3 +"\t"+
//	    // (gbg.getCDSstart()-1) +"\t"+ gbg.getCDSlength() +"\t"+
//	    // (gbg.get_length()-gbg.getCDSend())+"\t"+pos+"\t"+type);
//	    String line = gbg.getAccession() + "\t" + gbg.getName() + "\t" + this.miRNA_name + "\t" + UTR5 + "\t" + CDS + "\t" + UTR3 + "\t" + UTR5CDS + "\t"
//		    + CDSUTR3 + "\t" + (gbg.getCDSstart() - 1) + "\t" + gbg.getCDSlength() + "\t" + (gbg.get_length() - gbg.getCDSend()) + "\t" + pos + "\t"
//		    + type + "\n";
//	    try {
//		out.write(line.getBytes());
//	    } catch (IOException e) {
//		e.printStackTrace();
//		System.exit(2);
//	    }
//	}
//
//    }
//
//    public void searchTarget() {
//	int MM = 0; /* Mismatch */
//	int M = 1; /* Match */
//	int W = 2; /* G-U Wobble match */
//
//	int len = seq.length();
//	for (int i = 0; i < len - 8; ++i) {
//	    String target = seq.substring(i, i + 8);
//	    int c = calculate_match(target);
//
//	    if (c < 6)
//		continue; /* Cannot be valid match */
//	    if (isValidMatch(match, target.charAt(7)))
//		printHit(i, target, revSeed);
//
//	}
//
//    }
//
//    /**
//     * This function checks the various kinds of matches according to the 2009
//     * Bartel Cell paper
//     */
//    public boolean isValidMatch(char[] match, char pos1) {
//	int maxmatch = 0;
//	int c = 0;
//	int w = 0;
//	for (int i = 0; i < 8; ++i) {
//	    if (match[i] != ' ')
//		c++;
//	    if (match[i] == ':')
//		w++;
//	}
//	// System.out.println("c=" +c);
//	if (c == 8 && w <= MAXWOBBLE) {
//	    match_type = EIGHT;
//	    n_eight++;
//	    return true;
//	} else if (c == 7 && w <= MAXWOBBLE && match[7] == ' ') {
//	    match_type = SEVEN_M8;
//	    n_seven_m8++;
//	    return true;
//	} else if (c == 6 && w <= MAXWOBBLE && match[0] == ' ' && match[7] == ' ') {
//	    match_type = SIX;
//	    n_six++;
//	    return true;
//	} else if (c == 6 && w <= MAXWOBBLE && match[6] == ' ' && match[7] == ' ') {
//	    match_type = OFFSET_SIX;
//	    n_offset_six++;
//	    return true;
//	} else if (match[0] == ' ' & pos1 == 'A' && w <= MAXWOBBLE) {
//	    for (int j = 1; j < 7; ++j) {
//		if (match[j] == ' ') {
//		    match_type = NO_MATCH;
//		    return false;
//		}
//	    }
//	    match_type = SEVEN_A1;
//	    n_seven_a1++;
//	    return true;
//	} else if (c == 6 && w <= MAXWOBBLE && match[7] == ' ') {
//	    // System.err.println("comp: 2-8 ");
//	    match_type = COMPENSATORY28;
//	    n_compens++;
//	    return true;
//
//	} else if (c == 6 && w <= MAXWOBBLE && match[0] == ' ') {
//	    // System.err.println("comp: 1-7 ");
//	    match_type = COMPENSATORY17;
//	    n_compens++;
//	    return true;
//	} else if (c == 7 && w <= MAXWOBBLE) {
//	    // System.err.println("comp: 1-8 ");
//	    match_type = COMPENSATORY18;
//	    n_compens++;
//	    return true;
//	} else {
//	    match_type = NO_MATCH;
//	    return false;
//	}
//    }
//
//    /**
//     * get enough nucleotides to fill out mRNA for all the miRNA bases. If there
//     * are not enough, add spaces,
//     */
//    protected String expandTarget(int i, String target) {
//	int len = this.rev_miRNA.length();
//	int tlen = target.length();
//	int x = len - tlen;
//	if (i > x) {
//	    String s = this.seq.substring(i - x, i - x + len);
//	    return s;
//	} else {
//	    char[] C = new char[x];
//	    String exp = new String(C) + target;
//	    return exp;
//	}
//    }
//
//    /** Pad the match symbols with spaces */
//    protected String expandMatch() {
//	int len = this.rev_miRNA.length();
//	int x = len - match.length;
//	char[] C = new char[x];
//	for (int i = 0; i < x; ++i)
//	    C[i] = ' ';
//	String s = new String(C) + new String(match);
//	return s;
//    }
//
//    protected String getTargetLoc(int i) {
//	int end = i + 7;
//	int start = end - this.miRNA.length() + 1;
//	String s = new String(start + "-" + end);
//	return s;
//
//    }
//
//    private String getMatchType() {
//	switch (match_type) {
//	case SEVEN_A1:
//	    return "A1";
//	case SEVEN_M8:
//	    return "M8";
//	case EIGHT:
//	    return "8m";
//	case SIX:
//	    return "6m";
//	case OFFSET_SIX:
//	    return "6o";
//	default:
//	    return "";
//	}
//    }
//
//    public void printHit(int i, String target, String revSeed) {
//	String M = new String(match);
//	String type = "";
//	if (match_type == SEVEN_A1)
//	    type = "7mer-A1";
//	else if (match_type == SEVEN_M8)
//	    type = "7mer-m8";
//	else if (match_type == EIGHT)
//	    type = "8mer";
//	else if (match_type == SIX)
//	    type = "6mer";
//	else if (match_type == OFFSET_SIX)
//	    type = "Offset 6mer";
//	else {
//	    System.err.println("Did not recognize type");
//	    System.exit(1);
//	}
//	String location = getLocation(i);
//	String targetLoc = getTargetLoc(i);
//	String expMatch = expandMatch();
//	String expTarget = expandTarget(i, target);
//	System.out.println("\n##" + gbr.get_definition() + "--" + this.miRNA_name);
//	System.out.println("## Match at pos. " + targetLoc + " (" + type + "; " + location + ")");
//	System.out.println("5'..." + expTarget + "... (target)");
//	System.out.println("     " + expMatch);
//	System.out.println("3'   " + rev_miRNA + "    (miRNA)");
//
//    }
//
//    protected String getLocation(int start) {
//	int end = 0;
//	if (match_type == EIGHT)
//	    end = start + 7;
//	else if (match_type == SEVEN_M8)
//	    end = start + 6;
//	else if (match_type == SEVEN_A1) {
//	    end = start + 7;
//	    start++;
//	} else if (match_type == SIX) {
//	    end = start + 6;
//	    start++;
//	} else if (match_type == OFFSET_SIX) {
//	    end = start + 5;
//	}
//	return gbr.getLocation(start, end);
//    }
//
//}
