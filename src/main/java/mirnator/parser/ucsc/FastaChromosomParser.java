/**
 * 
 */
package mirnator.parser.ucsc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import mirnator.structs.ucsc.Chromosom;

/**
 * @author mjaeger
 *
 */
public class FastaChromosomParser {
//    Chromosom chromosom;
//    BufferedReader buf;
//    
//    String 	header;
//    String 	line;
//    String 	filename;
//    File 	file;
//    
//    public FastaChromosomParser(String filename) {
//	this.chromosom 	= new Chromosom();
//	this.filename 	= filename;
//	this.file 	= new File(filename);
//    }
//    
//    public FastaChromosomParser(File file){
//	this.chromosom 	= new Chromosom();
//	this.filename	= file.getAbsolutePath();
//	this.file	= file;
//    }
//    
//    public void parse() throws IOException{
//	buf = new BufferedReader(new FileReader(this.file));
//	while((line = buf.readLine()) != null){
//	    if(line.startsWith(">")){
//		this.header = line.substring(1);
//		continue;
//	    }
////	    System.out.print(line);
//	    this.chromosom.appendSequence(line);
//	}
//	buf.close();
//	
//    }
    
    /**
     * 
     * @param filename
     * @return
     * @throws IOException
     */
    public static Chromosom parse(String filename) throws IOException{
	
	return parse(new File(filename));
	
    }
    
    
    /**
     * 
     * @param file
     * @return
     * @throws IOException
     */
    public static Chromosom parse(File file) throws IOException {
	Chromosom chromosom = new Chromosom();
	BufferedReader buf;

//	String header;
	String line;
	
		buf = new BufferedReader(new FileReader(file));
	while((line = buf.readLine()) != null){
	    if(line.startsWith(">")){
//		header = line.substring(1);
		continue;
	    }
//	    System.out.print(line);
	    chromosom.appendSequence(line);
	}
	buf.close();


	return chromosom;

    }
//    /**
//     * @return the chromosom
//     * @throws IOException 
//     */
//    public Chromosom getChromosom() throws IOException {
//	this.parse();
//        return chromosom;
//    }

}
