package mirnator.parser.genbank;


import java.io.*;
import java.util.*;
import java.util.regex.*;

import mirnator.structs.genbank.Exon;

public class GenBankReader {

   
    
    private int mRNAstart;
    private int mRNAend;
    private int CDSstart;
    private int CDSend;
    private ArrayList<Exon> exons;
    private String fname;
    private String cDNA;
    private String definition="";

    public GenBankReader(String filename) {
		this.fname = filename;
		exons = new ArrayList<Exon>();
		cDNA = "";
		inputFile();
    }





    public String get_cDNA() { return cDNA; }
    public int get_length() { return (mRNAend-mRNAstart+1); }
    public int get_number_of_exons() { return exons.size(); }
    public String get_definition() { return definition; }


    protected void inputFile() {
	try{
    	    FileInputStream fstream = new FileInputStream(this.fname);
	    DataInputStream in = new DataInputStream(fstream);
	    BufferedReader br = new BufferedReader(new InputStreamReader(in));
	    String str;
	    if ((str = br.readLine()) != null) {
		if (! str.startsWith("LOCUS"))
		    { System.err.println("Malformed GenBank file; first line: "
					 + str );
			System.exit(1);
		    }
		
	    }
	    while ((str = br.readLine()) !=null) {
		String regEx = "DEFINITION\\s+(.*)";
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(str);
		if (matcher.find() ) {
		    definition = matcher.group(1);
		}
		if (str.startsWith("FEATURES")) break;
	    }
	    if ((str = br.readLine()) !=null) {
		String regEx = "source\\s+(\\d+)\\.\\.(\\d+)";
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(str );
		if (matcher.find()) {
		    mRNAstart = Integer.parseInt(matcher.group(1));
		    mRNAend   = Integer.parseInt(matcher.group(2));
		    System.out.println("Gene: " + mRNAstart + " - " + mRNAend);
		} else {
		    System.err.println("Could not parse source line: " + str);
		    System.exit(1);
		}
		 String CDSregex = "CDS\\s+(\\d+)\\.\\.(\\d+)";
		 String Exon_regex = "exon\\s+(\\d+)\\.\\.(\\d+)";
		 Pattern CDSpattern = Pattern.compile(CDSregex);
		 Pattern ExonPattern = Pattern.compile(Exon_regex);
		while ((str = br.readLine()) !=null) {
		    if (str.startsWith("ORIGIN")) break;
		    Matcher CDSmatcher = CDSpattern.matcher(str);
		    Matcher ExonMatcher = ExonPattern.matcher(str);
		    if (CDSmatcher.find()) {
			CDSstart = Integer.parseInt(CDSmatcher.group(1));
			CDSend   = Integer.parseInt(CDSmatcher.group(2));
			 System.out.println("CDS: " + CDSstart + " - " + CDSend);
		    } else if (ExonMatcher.find()) {
			int ex_start = Integer.parseInt(ExonMatcher.group(1));
			int ex_end   = Integer.parseInt(ExonMatcher.group(2));
			Exon e = new Exon(ex_start,ex_end);
			exons.add(e);
		    }
		}
		while ((str = br.readLine()) != null) {
		    if (str.startsWith("//")) break; // End of sequence.
		    readSequenceLine(str.trim());
		}
		//System.out.println("Got cDNA " + cDNA + "\n\n of len " + cDNA.length());
	    }    
	    in.close();
	} catch (Exception e){
	    System.err.println("Error: " + e.getMessage());
	    System.exit(1);
	}

    }

    protected void readSequenceLine(String line) {

	line = line.toUpperCase();
	int len = line.length();
	StringBuffer sb = new StringBuffer();
	for (int i = 0; i < len; i ++)
	    {
		int c = (int) line.charAt(i);
		if (c < 65 || c > 90) continue;
		sb.append((char)c);
	    }	
	cDNA += sb.toString();
	
    }


    public boolean isCDS(int start, int end) {
	return start > CDSstart && end < CDSend;
    }

    public boolean is_3UTR(int start,int end) {
	return start > CDSend;
    }
    

    public String getLocation(int start,int end){
	if (end< CDSstart) return "5' UTR";
	if (start >CDSend) return "3' UTR";
	if (start < CDSstart) return "5' UTR/CDS";
	if (end > CDSend) return "CDS/3' UTR";
	int i = getExonNumber(start,end);
	if (i>0) {
	    String s  = new String("CDS-Exon "+i);
	    return s;
	} else {
	    i *= -1;
	    String s = new String("CDS-Exon "+i+"/" + (i+1));
	    return s;
	}
	//   return "CDS";
    }

    private int getExonNumber( int start,int end) {
	int n = 0;
	System.out.println("start = " + start + "  end = " + end);
	Iterator<Exon> it = this.exons.iterator();
	while (it.hasNext()) {
	    Exon e = it.next();
	    n++;
	    if (start >= e.get_start() && start <= e.get_end()) {
		if (end <= e.get_end()) return n;
		else return -1*n; /* flag for overlap */
	    }

	}
	return 0;

    }	


}
