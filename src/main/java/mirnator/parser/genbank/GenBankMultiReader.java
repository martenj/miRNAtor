package mirnator.parser.genbank;


import java.io.*;
import java.util.*;
import java.util.regex.*;

import mirnator.structs.genbank.Exon;
import mirnator.structs.genbank.GenBankGene;

/**
 * TODO until now the GenBankMultiReader builds a hashmap with gene names as key (e.g. ELN) 
 * 		solving multiple occurences of the same gene name by using the transcript with the 
 * 		longest CDS  
 * @author mjaeger
 *
 */
public class GenBankMultiReader {

    private HashMap<String,GenBankGene> genes;
    /** A temporary variable that holds the DNA sequence of the current gene */
    private String mRNA;

    
    public GenBankMultiReader(String path) {
		genes = new HashMap<String,GenBankGene>();
		inputFile(path);
    }
    
    /**
     * This method returns a HashMap with GenBankGenes where the RefSeq/GenBank IDs are used as keys
     * @return HashMap of Genes
     */
    public HashMap<String, GenBankGene> getGenes(){
    	return this.genes;
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
		mRNA += sb.toString();
    }

    private boolean readNextGene(BufferedReader br) throws IOException{
		 String str="";
		 String def="";
		 String acc="";
		 String ver="";
		 String name=null;
		 int mRNAstart=0;
		 int mRNAend=0;
		 int CDSstart=0;
		 int CDSend=0;
		 this.mRNA = ""; /* reset */
		 ArrayList<Exon> exons = new ArrayList<Exon>();
		 while ((str = br.readLine()) != null) {
		     if (str.startsWith("LOCUS")) break;
		 }
		 if (str == null) return false;
		 /* If we get here, we are starting to parse the next gene */
		 String definitionRegEx = "DEFINITION\\s+(.*)";			// TODO does not fit, there are mRNAs with two or more () --> solved by using gene tag 
		 Pattern definitionPattern = Pattern.compile(definitionRegEx);
		 String accessionRegEx = "ACCESSION\\s+([A-Z]{2}\\_\\d+)";			// TODO does not fit, there are mRNAs with two or more () --> solved by using gene tag 
		 Pattern accessionPattern = Pattern.compile(accessionRegEx);
		 String sourceRegEx = "source\\s+(\\d+)\\.\\.(\\d+)";	
		 Pattern sourcePattern = Pattern.compile(sourceRegEx);
//		 String CDSregex = "CDS\\s+(\\d+)\\.\\.(\\d+)";			// TODO dat paßt so nicht. optional gibt es auch noch: join(zahl..zahl,zahl..zahl,...) und <zahl.. ..zahl>
//		 String CDSregex = "CDS\\s+[join]?\\p{Punct}?(\\d+)\\.[\\p{Punct}0-9]?\\.(\\d+)\\p{Punct}?";			
		 String CDSregex = "CDS\\s+[join]?[\\(<]?(\\d+)\\.[\\p{Punct}0-9]?\\.(\\d+)[\\)>]?";		//	
		 Pattern CDSpattern = Pattern.compile(CDSregex);
		 String Exon_regex = "exon\\s+(\\d+)\\.\\.(\\d+)";
		 Pattern ExonPattern = Pattern.compile(Exon_regex);
//		 String geneRegex="\\/gene=([\"])(?:(?=(\\?))\2.)*?\1";
		 String geneRegex="\\/gene=\"(.*)\"";
		 Pattern genePattern = Pattern.compile(geneRegex);
		 String versionRegex = "VERSION\\s+[A-Z]{2}\\_\\d+\\.[0-9]{1,2}\\s+(GI:\\d+)";
		 Pattern versionPattern = Pattern.compile(versionRegex);
		 
		 while ((str = br.readLine()) !=null) {					// searching for Definition
		     Matcher matcher = definitionPattern.matcher(str);
		     if (matcher.find() ) {
		    	 def = matcher.group(1);
		     }	
		     matcher = accessionPattern.matcher(str);
		     if(matcher.find()){
		    	 acc = matcher.group(1);
		     }
		     matcher = versionPattern.matcher(str);
		     if(matcher.find()){
		    	 ver = matcher.group(1);
		     }
		     if (str.startsWith("FEATURES")) break;
		 }
		 if ((str = br.readLine()) !=null) {					// start and end mRNA
		     Matcher matcher = sourcePattern.matcher(str );
		     if (matcher.find()) {
			 mRNAstart = Integer.parseInt(matcher.group(1));
			 mRNAend   = Integer.parseInt(matcher.group(2));
//			 System.out.println("Gene: " + mRNAstart + " - " + mRNAend);
		     } else {
			 System.err.println("Could not parse source line: " + str);
			 System.exit(1);
		     }
		 }
		 while ((str = br.readLine()) !=null) {
		     if (str.startsWith("ORIGIN")) break;
		     Matcher CDSmatcher = CDSpattern.matcher(str);		// start & end of CDS
		     Matcher ExonMatcher = ExonPattern.matcher(str);	//
		     Matcher geneMatcher = genePattern.matcher(str);
		     if (geneMatcher.find()) {
			 name = geneMatcher.group(1);
		     } else  if (CDSmatcher.find()) {
			 CDSstart = Integer.parseInt(CDSmatcher.group(1));
			 CDSend   = Integer.parseInt(CDSmatcher.group(2));
//			 System.out.println("CDS: " + CDSstart + " - " + CDSend);
		     } else if (ExonMatcher.find()) {
			 int ex_start = Integer.parseInt(ExonMatcher.group(1));
			 int ex_end   = Integer.parseInt(ExonMatcher.group(2));
			 Exon e = new Exon(ex_start,ex_end);
			 exons.add(e);
		     }
		 }
//		 System.out.println("Reading sequence...");
		 while ((str = br.readLine()) != null) {
		     if (str.startsWith("//")) break; // End of sequence.
		     readSequenceLine(str.trim());
		 }

		 if(name.length() < 1){
			 System.err.println("There was no valid gene name found for " + def);
			 System.exit(2);
		 }		 
		 /** When we get here, we have all the info we need to
		     create a Gene object. */
		 GenBankGene gbg = new GenBankGene(this.mRNA, CDSstart,
						   CDSend,mRNAstart,mRNAend, def,name,exons,acc,ver);
//		 gbg.setName(name);
//		 if(!(name.equals(gbg.getName()))){
//			 System.out.println("Mismatch: " + name + "\t" + gbg.getName());
//			 System.exit(1);
//		 }
		 /* is there already a entry with the same key? get CDS length and ggf. replace */
		 if(genes.containsKey(name)){
//		     	 if(genes.get(name).get3UTRlength() <= gbg.get3UTRlength())
			 if(genes.get(name).getCDSlength() < gbg.getCDSlength())
				 genes.put(gbg.getName(),gbg);
//			 if(genes.get(acc).getCDSlength() < gbg.getCDSlength())
//				 genes.put(acc, gbg);
		 }
		 else
			 genes.put(gbg.getName(),gbg);
//		 System.out.println("Done with " + gbg.getName());
//		 System.out.println(gbg.getName());
//		 System.out.println("Seq: "+mRNA);
		 return true;
    }

    protected void inputFile(String path) {
		System.err.println("Inputting genbank file at " + path);
		try{
	    	FileInputStream fstream = new FileInputStream(path);
		    DataInputStream in = new DataInputStream(fstream);
		    BufferedReader br = new BufferedReader(new InputStreamReader(in));
		   
		    while ( readNextGene(br)) { };
	       
		    in.close();
		} catch (Exception e){
		    System.err.println("Error: " + e.getMessage());
		    System.exit(1);
		}

    }

    public void printFASTA(String out){
    	try{
    		BufferedWriter bw = new BufferedWriter(new FileWriter(out));
    		
    		Iterator<String> iter = this.genes.keySet().iterator();
    		while(iter.hasNext()){
//    			System.out.println("Iter: "+iter.next());
    			this.genes.get(iter.next()).printSingleFasta(bw);
    		}
    		bw.close();
    	}
    	catch(IOException e){
    		e.printStackTrace();
    	}
    }
    


}