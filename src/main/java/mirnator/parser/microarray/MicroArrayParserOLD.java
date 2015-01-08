package mirnator.parser.microarray;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

import mirnator.constants.FileTypeConstants;
import mirnator.structs.microarraydata.MicroArrayData;



public class MicroArrayParserOLD {
	
	protected short type = -1;
	private int targets = -1;
	protected String filename;
	private int nData = 0;						// number of MicroaArrayData
	

	
	protected HashMap<String,MicroArrayData> datasets;
	protected Vector<String> dsnames;
	/**
	 * parses microarray file given by filename. sets filetype to '-1' (unknown) not jet handeled.
	 * targets identifier set to '-1' - GeneSymbol
	 * @param filename - path to file
	 */
	public MicroArrayParserOLD(String filename){
		this(filename,(short)-1);
	}
	/**
	 * parses microarray file given by filename.
	 * targets identifier set to '-1' - GeneSymbol
	 * possible filetypes:
	 * \t -1: unknown (not jet implemented)
	 * \t  0: summarized
	 * \t  1: Agilent (not jet implemented)
	 * \t  2: Affymetrix (not jet implemented)
	 * \ŧ  3: ArrayExpress  
	 * @param filename - path to file
	 * @param type - type of input data
	 */
	public MicroArrayParserOLD(String filename,short type){
		this.filename = filename;
		this.type = type;
		this.datasets = new HashMap<String,MicroArrayData>();
		this.dsnames = new Vector<String>();
		parse();
	}
	
	/**
	 * parses microarray file given by filename.
	 * possible filetypes:
	 * \t -1: unknown (not jet implemented)
	 * \t  0: summarized
	 * \t  1: Agilent (not jet implemented)
	 * \t  2: Affymetrix (not jet implemented)
	 * \ŧ  3: ArrayExpress  
	 * @param filename - path to file
	 * @param type - type of input data
	 * @param targets - used identifier for target summarization
	 */
	public MicroArrayParserOLD(String filename, int type, int targets) {
		this(filename,(short)type,(short)targets);
	}
	
	/**
	 * parses microarray file given by filename.
	 * possible filetypes:
	 * \t -1: unknown (not jet implemented)
	 * \t  0: summarized
	 * \t  1: Agilent (not jet implemented)
	 * \t  2: Affymetrix (not jet implemented)
	 * \ŧ  3: ArrayExpress  
	 * @param filename - path to file
	 * @param type - type of input data
	 */
	public MicroArrayParserOLD(String filename, short type, short targets) {
		this.filename = filename;
		this.type = type;
		this.datasets = new HashMap<String,MicroArrayData>();
		this.dsnames = new Vector<String>();
		this.targets = targets;
		parse();
	}
	/**
	 * parses the file given by filename according to microarray type
	 */
	public void parse(){
		try{
			switch(type){
			case FileTypeConstants.EXPsummarized: parseSummarized(); break;
	//		case 1: parseAgilent(); break;
	//		case 2: parseAffymetrix(); break;
	//		case 3: parseArrayExpress(); break;
			default: System.out.println("not yet implemented"); break;
			}
		}
		catch(IOException e){
			System.err.println("Error while getting inputfile: " + e.getMessage());
			System.exit(1);
		}
	}
	
	@SuppressWarnings("unused")
	private void parseArrayExpress() throws IOException {
		// TODO Auto-generated method stub
		
	}

	@SuppressWarnings("unused")
	private void parseAffymetrix() {
		// TODO Auto-generated method stub
		
	}

	@SuppressWarnings("unused")
	private void parseAgilent() {
		// TODO Auto-generated method stub
		
	}

	private void parseSummarized() throws IOException {

			BufferedReader bf = new BufferedReader(new FileReader(this.filename));
			//check header
			String[] h = {"ProbeName","EnsemblID","GeneSymbol","EntrezGene","GenbankAccession"};
			String line;
			String[] linesp;
			String PN;
			String EID;
			String GS;
			String EG;
			String GB;
			if((line = bf.readLine()) != null){
				line = line.trim();
				linesp = line.split("\t");
				if(linesp.length < 6)
					printHeaderError(line);
				for(int i=0;i<5;i++){
					if(!linesp[i].equals(h[i]))
						printHeaderError(line);
//					System.out.println(linesp[i]);
				}
				for(int i=5;i<linesp.length;i++){
					System.out.println("Create new found MicroArrayData nr: "+(++this.nData)+" - "+linesp[i]);
					this.datasets.put(linesp[i],new MicroArrayData(linesp[i], this.type));
					this.dsnames.add(linesp[i]);
				}
			}
			//read body
//			System.out.println("Read Body");
			while((line = bf.readLine()) != null){
				line = line.trim();
				linesp = line.split("\t");
				if(checkWord(linesp[0]))
					PN = linesp[0]; 
				else 
					continue;
				EID = checkWord(linesp[1]) ? linesp[1] : "NA";
				GS = checkWord(linesp[2]) ? linesp[2] : "NA";
				EG = checkWord(linesp[3]) ? linesp[3] : "NA";
				GB = checkWord(linesp[4]) ? linesp[4] : "NA";
				
				GS = GS.toLowerCase();
//				GS = GS.toUpperCase();
//				System.out.println("add entry");
//				System.out.println(this.datasets.size());
				//add Entities
				String key;
				switch(this.targets){
				case -1: key = GS; break;
				case 0:
				case 3: key = GB; break;
				case 1: key = EID; break;
				case 2: key = EG; break;		
				default: key = null; System.out.println("invalid target source number: "+this.targets); System.exit(1);
				}
				key = GS;
				if(key.equals("NA"))	//TODO filter for NA keys
					continue;
				for(int i=5;i<linesp.length;i++){	
					if(this.datasets.get(this.dsnames.elementAt(i-5)).getEntries().containsKey(key))						// add new Entry
						this.datasets.get(this.dsnames.elementAt(i-5)).getEntries().get(key).addValue(Double.parseDouble(linesp[i]));
					else																							// or append expression value to existing entry
						this.datasets.get(this.dsnames.elementAt(i-5)).addEntity(key,GB, GS, EID, EID, PN, EG,Double.parseDouble(linesp[i]));
				}
			
				
			}
			
//			for (String key : this.dsnames) {
//				MicroArrayData mad = this.datasets.get(key);
//				for (String ent : mad.entries.keySet()) {
//					System.err.println(ent);
//					mad.entries.get(ent).printEntity();
//					
//				}
//			}
			bf.close();
			System.out.println("finished");	
			
			
		
		
	}
	
	private boolean checkWord(String w){
		if(w.equals("")) 
			return false;
		else if(w.equals("NA")) 
			return false;
		else
			return true;		
	}
		
	private void printHeaderError(String s){
		System.err.println("No valid header (ProbeName	EnsemblID	GeneSymbol	EntrezGene	GenbankAccession ...\n"+s);
		System.exit(1);
	}

	
//	public String[] getProbeNames(){
//		return this.datasets.
//	}
	
	public int getNumberOfData(){
		return this.nData;
	}
	
	public MicroArrayData getDataSet(String key){
		return this.datasets.get(key);
	}
	
	/**
	 * @return all keys for datasets stored in the MAP
	 */
	public Vector<String> getDSkeys(){
		return this.dsnames;
	}
}
