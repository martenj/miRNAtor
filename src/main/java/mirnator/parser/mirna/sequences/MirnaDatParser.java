package mirnator.parser.mirna.sequences;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MirnaDatParser extends MirnaParser {

	private String regexACmi 	= "AC\\s{3}(MI[0-9]{7})";
	private String regexACmimat	= "FT\\s+/accession=\"(MIMAT[0-9]{7})\"";
	
	private HashMap<String, String> matureAC2mirnaAC;

	public MirnaDatParser(String filename) {
		super(filename);
		this.matureAC2mirnaAC	= new HashMap<String, String>();
	}

	@Override
	public boolean checkfile() {
		if(this.filename == null) 
			return false;
		try {
			String line;
			int i=0;
			BufferedReader buf = new BufferedReader(new FileReader(this.filename));
			while((line = buf.readLine()) !=  null){
				if(line.startsWith("#")) continue;
				if(line.matches(this.regexACmi))
					break;
				if(i++>10){
					buf.close();
					return false;
				}
			}
			buf.close();
				
		} catch (FileNotFoundException e) {
			log.log(Level.WARNING, "File \""+this.filename+"\" not found.\n");
			log.log(Level.WARNING, e.getMessage());
			return false;
		} catch (IOException e) {
			log.log(Level.WARNING, "Error reading line from \""+this.filename+"\".\n");
			log.log(Level.WARNING, e.getMessage());
		}
		
		return true;
	}

	@Override
	public void parse() {
		Pattern patternACmi = Pattern.compile(this.regexACmi);
		Pattern patternACmimat = Pattern.compile(this.regexACmimat);
		Matcher matcherACmi;
		Matcher matcherACmimat;
		
		String line;
		String miAC = "";		// to store the accession (e.g. MI0016735)
		
		
		try {
			BufferedReader buf = new BufferedReader(new FileReader(this.filename));
			while((line = buf.readLine()) != null){
				if(line.startsWith("AC") | line.startsWith("FT")){
					
	//				System.out.println(line);
					
					// look for the stem loop AC (e.g. MI0016735)
					matcherACmi	= patternACmi.matcher(line);
					if(matcherACmi.find()){
	//					System.out.print("\n"+matcherACmi.group(1)+": ");
						miAC 	= matcherACmi.group(1);
						continue;
					}
					
					// look for mature/minor ACs (e.g. MIMAT0015108)
					matcherACmimat	= patternACmimat.matcher(line);
					if(matcherACmimat.find()){
	//					System.out.print(matcherACmimat.group(1)+" ");
						this.matureAC2mirnaAC.put(matcherACmimat.group(1),miAC);
						continue;
					}
					
	//				System.out.println(line);
				}
				
			}
			buf.close();
		} catch (FileNotFoundException e) {
			log.log(Level.WARNING, "File \""+this.filename+"\" not found.\n");
			log.log(Level.WARNING, e.getMessage());
		} catch (IOException e) {
			log.log(Level.WARNING, "Error reading line from \""+this.filename+"\".\n");
			log.log(Level.WARNING, e.getMessage());
		}


	}

	/**
	 * Returns a mapping table from mature/minor miRNa accession (e.g. MIMAT0000001, MIMAT0015091) to the miRNA accession (e.g. MI0000001)
	 * @return the matureAC2mirnaAC
	 */
	public HashMap<String, String> getMatureAC2mirnaAC() {
		return matureAC2mirnaAC;
	}

}
