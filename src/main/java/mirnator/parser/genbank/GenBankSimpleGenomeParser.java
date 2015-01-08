package mirnator.parser.genbank;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mirnator.structs.genbank.SimpleGene;

/**
 * The GenBankSimpleGenomeParser 
 * @author mjaeger
 *
 */
public class GenBankSimpleGenomeParser {
    
    private final static Pattern geneComplementPattern	= Pattern.compile(".+gene.{12}complement\\(([0-9]+)..([0-9]+)\\)");
    private final static Pattern genePattern		= Pattern.compile(".+gene.{12}([0-9]+)..([0-9]+)");
    private final static Pattern locusPattern		= Pattern.compile(".+\\/locus_tag=\"([A-Za-z0-9 ]+)\"");
    
  
    
    public static ArrayList<SimpleGene> parse(String filename){
	ArrayList<SimpleGene> 	genes	= new ArrayList<SimpleGene>();
	
	Vector<Integer> 	starts	= new Vector<Integer>();
	Vector<Integer> 	ends	= new Vector<Integer>();
	Vector<String> 		ids	= new Vector<String>();
	
	String id	= null;
	int start	= -1;
	int end		= -1;
	boolean	origin	= false;
	
	/** A temporary variable that holds the DNA sequence*/
//	String dna	= "";
	StringBuilder dna = new StringBuilder();
	
	BufferedReader in = null;
	try {
	    in = new BufferedReader(new FileReader(filename));
	    String str;
	    while ((str = in.readLine()) != null) {
		if(str.startsWith("ORIGIN")){
		    origin = true;
		    continue;
		}
		if(origin){
		    int len = str.length();
//		    sb = new StringBuffer();
		    for (int i = 0; i < len; i ++)
		    {
			int c = (int) str.charAt(i);
			if (c < 97 || c > 122) continue;
			dna.append((char)c);
		    }		
//		    dna += sb.toString();
		}
		Matcher geneComplementMatcher = geneComplementPattern.matcher(str);
		if(geneComplementMatcher.find()){
		    end 	= Integer.parseInt(geneComplementMatcher.group(1))-1;
		    start	= Integer.parseInt(geneComplementMatcher.group(2));
		    continue;
		}
		
		Matcher geneMatcher = genePattern.matcher(str);
		if(geneMatcher.find()){
		    start 	= Integer.parseInt(geneMatcher.group(1))-1;
		    end		= Integer.parseInt(geneMatcher.group(2));
		    continue;
		}
		
		Matcher locusMatcher = locusPattern.matcher(str);
		if(locusMatcher.find()){
		    id 		= locusMatcher.group(1);
		    if(start + end > 0 && id != null){
			starts.add(start);
			ends.add(end);
			ids.add(id);
			id 	= null;
			start 	= -1;
			end	= -1;
		    }
		    continue;
		}
		
		
		
	    }
	} catch (IOException e) {
	    e.printStackTrace();
	} finally {
	    try{
		if(in != null)
		    in.close();
	    }catch (IOException e){
		e.printStackTrace();
	    }
	}
	
	System.out.println("No. genes found: "+ids.size());
	
	for(int i=0;i<ids.size();i++){
//	    System.out.println(starts.elementAt(i)+" - "+ends.elementAt(i));
	    if(starts.elementAt(i)<ends.elementAt(i)){
		genes.add(new SimpleGene(ids.elementAt(i), dna.substring(starts.elementAt(i), ends.elementAt(i))));
	    }
	    else{
//		String seq = new StringBuffer(dna.substring(ends.elementAt(i),starts.elementAt(i))).reverse().toString();
//		System.out.println(seq);
		genes.add(new SimpleGene(ids.elementAt(i),new StringBuffer(dna.substring(ends.elementAt(i),starts.elementAt(i))).reverse().toString()));
	    }
	    
	}
//	System.out.println(dna.length());
	
	return genes;
    }
    


    

}
