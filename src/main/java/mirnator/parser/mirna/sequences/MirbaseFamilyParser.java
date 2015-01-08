package mirnator.parser.mirna.sequences;
///**
// * 
// */
//package mirnator.parsers.mirna;
//
//import java.io.BufferedReader;
//import java.io.FileNotFoundException;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.logging.Level;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//import mirnator.constants.ExceptionConstants;
//import mirnator.sql2javaold.MirnaFamilyBean;
//import mirnator.sql2javaold.MirnaFamilyManager;
//import mirnator.sql2java.exception.DAOException;
//
//
///**
// * @author mjaeger
// *
// */
//public class MirbaseFamilyParser extends MirnaParser implements ExceptionConstants{
//	
//	
//	private String regexAC = "AC[ ]{3}(MIPF[0-9]{7})";
//	private String regexID = "ID[ ]{3}([a-zA-Z\\_\\-0-9]+)";
//	private String regexMI = "MI[ ]{3}([MI0-9]{9})[ ]{2}([\\w\\-]+)";
//	
//	private ArrayList<MirnaFamilyBean> families;
//	private HashMap<String, String> mirnaAC2familyAC;
//	
//	private MirnaFamilyManager mirfamman;
//	
//	
//	public MirbaseFamilyParser(String filename) {
//		super(filename);
//		this.families 			= new ArrayList<MirnaFamilyBean>();
//		this.mirfamman 			= MirnaFamilyManager.getInstance();
//		this.mirnaAC2familyAC 	= new HashMap<String, String>();
//	}
//	
//
//	@Override
//	public boolean checkfile() {
//		if(this.filename == null) 
//			return false;
//		try {
//			String line;
//			int i=0;
//			BufferedReader buf = new BufferedReader(new FileReader(this.filename));
//			while((line = buf.readLine()) !=  null){
//				if(line.startsWith("#")) continue;
//				if(line.matches(this.regexAC))
//					break;
//				if(i++>10)
//					return false;
//			}
//			buf.close();
//				
//		} catch (FileNotFoundException e) {
//			log.log(Level.WARNING, "File \""+this.filename+"\" not found.\n");
//			log.log(Level.WARNING, e.getMessage());
//			return false;
//		} catch (IOException e) {
//			log.log(Level.WARNING, "Error reading line from \""+this.filename+"\".\n");
//			log.log(Level.WARNING, e.getMessage());
//		}
//		
//		return true;
//	}
//
//	@Override
//	public void parse(){
//		Pattern patternAC = Pattern.compile(this.regexAC);
//		Pattern patternID = Pattern.compile(this.regexID);
//		Pattern patternMI = Pattern.compile(this.regexMI);
//		Matcher matcherAC;
//		Matcher matcherID;
//		Matcher matcherMI;
//		String line;
//		
//		MirnaFamilyBean fambean_template;
//		if(this.checkfile()){		
//			try {
//				BufferedReader buf = new BufferedReader(new FileReader(this.filename));
//				while((line = buf.readLine()) != null){
//					// parse complete family -> add to families
//					if(line.startsWith("//")){
//						
//						//complete family data and submit
//						fambean_template = this.mirfamman.createMirnaFamilyBean();
//						fambean_template.setFamilyAccession(this.accession);
//						fambean_template.setFamilyName(this.name);
//						this.families.add(fambean_template);
//
//						// set all data to "zero"
//						this.accession 	= "";
//						this.name		= "";
//						continue;
//					}
//					
//					matcherAC	= patternAC.matcher(line);
//					// if a accession number is found - save accession
//					if(matcherAC.find()){
//						this.accession	= matcherAC.group(1);
//						continue;
//					}
//					
//					matcherID	= patternID.matcher(line);
//					// if a ID/name is found - save name
//					if(matcherID.find()){
//						this.name	= matcherID.group(1);
//						continue;
//					}
//					
//					matcherMI	= patternMI.matcher(line);
//					// if a mirna is found - add to mirna list for this family
//					if(matcherMI.find()){
//						this.mirnaAC2familyAC.put(matcherMI.group(1), this.accession);
//						continue;
//					}
//					log.log(Level.WARNING, "unrecognized line in mirna family file: "+line);
//				}
//				buf.close();
//			} catch (IOException e) {
//				log.log(Level.WARNING, "File wasn't found or could not be read.\n This should never happen since the file was checked.");
//				e.printStackTrace();
//				System.exit(MirbaseFileMissing);
//			}
//		}
//	}
//
//
//	/**
//	 * @return the families
//	 */
//	public ArrayList<MirnaFamilyBean> getFamilies() {
//		return families;
//	}
//	
//	
//	public ArrayList<MirnaFamilyBean> updateTranscriptAtDB() throws DAOException{
//		return this.updateTranscriptAtDB(this.families);
//	}
//	/**
//	 * Updates the miRNAfamilies in the database. If there still is a family matching the bean to be inserted, 
//	 * nothing is done, otherwise the bean is inserted and the familybean is updated. 
//	 * @param families - famnilies to be updated/inserted in the DB
//	 * @return The list of mirna families including their DB ids. 
//	 * @throws DAOException
//	 */
//	public ArrayList<MirnaFamilyBean> updateTranscriptAtDB(ArrayList<MirnaFamilyBean> families) throws DAOException{
//		if(families != null){
//			this.log.log(Level.INFO, "start updating "+families.size()+" families in DB");
//			for (MirnaFamilyBean mirnaFamilyBean : families) {
//				switch (this.mirfamman.countUsingTemplate(mirnaFamilyBean)) {
//				case 0:
//					mirnaFamilyBean	= this.mirfamman.insert(mirnaFamilyBean);
//					
//					break;
//				case 1:
//					mirnaFamilyBean = this.mirfamman.loadUniqueUsingTemplate(mirnaFamilyBean);
//					//TODO here nothing should be done I think - the family is already in the DB
//					break;
//				default:
//					this.log.log(Level.SEVERE, "This should never happen: There are more than one DB entries matching our bean!");
//					break;
//				}
//			}
//			this.log.log(Level.INFO, "finished updating families DB");
//		}
//		return families;
//	}
//
//
//	/**
//	 * @return the mirnaAC2familyAC
//	 */
//	public HashMap<String, String> getMirnaAC2familyAC() {
//		return mirnaAC2familyAC;
//	}
//	
////	/**
////	 * Return the Family (e.g. MIPF0000001) for a given miRNA ID (e.g. MI0000071).
////	 * @param mirna - miRNA ID for which the family is search
////	 * @return the miRNA family ID or <code>null</code> if the family is not known
////	 */
////	public String getMapFromMirnaID2Family(String mirna){
////		return this.mirnaAC2familyAC.get(mirna);
////	}
//	
////	/**
////	 * Return the Family (e.g. mir-17) for a given miRNA name (e.g. hsa-mir-17).
////	 * @param mirna - miRNA name for which the family is search
////	 * @return the miRNA family ID or <code>null</code> if the family is not known
////	 */
////	public String getMapFromMirnaName2Family(String mirna){
////		return this.miRNA2familyAC.get(mirna);
////	}
////	
////	/**
////	 * Returns the miRNAs corresponding to a 
////	 * @param family
////	 * @return
////	 */
////	public ArrayList<String> getMapFromFamily2Mirna(String family){
////		//TODO implement getMapFromFamily2MiRNA
////		return null;
////	}
////
////	/**
////	 * @return the miRNA2Family
////	 */
////	public HashMap<String, String> getMirna2Family() {
////		return this.miRNA2familyAC;
////	}
//	
//	
//
//}
