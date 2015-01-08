package mirnator.parser.mirna.sequences;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import mirnator.constants.ExceptionConstants;
import mirnator.sql2java.MirnaBean;
//import mirnator.sql2java.MirnaFamilyBean;
//import mirnator.sql2java.MirnaFamilyManager;
import mirnator.sql2java.MirnaManager;

public class MirbaseFastaParser extends MirnaDBParser implements ExceptionConstants{
	
//	private String headerRegex = ">([a-z0-9]{3,5})-([miR|let|lin|bantam]-*[0-9a-zA-Z.\\-]+\\**) (MIMAT[0-9]{7}) [\\w\\p{Punct} ]+";
	private String headerRegex = ">([a-z0-9]{3,5})-([miR|let|lin|bantam]-*[0-9a-zA-Z.\\-\\/\\**]+) ([MIMATF]{3,5}[0-9]{6,7})[\\w\\p{Punct} ]*";
	
//	private ArrayList<MirnaBean> mirnas;
//	private HashMap<String, String> mirnaAC2mirAC;
	
	private MirnaManager mirman;

	public MirbaseFastaParser(String file) {
		super(file);
//		this.mirnas		= new ArrayList<MirnaBean>();
//		this.mirnaAC2mirAC	= new HashMap<String, String>();
		this.mirman		= MirnaManager.getInstance();
	}
	
//	public MirbaseFastaParser(String file,HashMap<String, String> mirna2fam){
//		super(file,mirna2fam);
//		this.mirnas			= new ArrayList<MirnaBean>();
//		this.mirnaAC2mirAC	= new HashMap<String, String>();
//	}

    @Override
    public void parse() {

	Pattern pat = Pattern.compile(this.headerRegex);
	Matcher mat;
	String line;
	MirnaBean mir_templ;

	System.out.println(this.checkfile());
	if (this.checkfile()) {
	    try {
		BufferedReader buf = new BufferedReader(new FileReader(
			this.filename));
		while ((line = buf.readLine()) != null) {
		    mat = pat.matcher(line);
		    //
		    if (mat.find()) {

//			 System.out.println("0: "+mat.group(0)+"\t1: "+mat.group(1)+"\t2: "+mat.group(2)+"\t3: "+mat.group(3));
			this.species = mat.group(1);
			this.name = mat.group(2);
			this.accession = mat.group(3);
		    } else {
			if (line.startsWith(">")) {
			    System.err.println(line);
			    log.log(Level.SEVERE, "missed miRNA FastA header: "
				    + line);
			} else {
			    this.sequence = line;
			    mir_templ = this.mirman.createMirnaBean();
			    mir_templ.setMirbaseAccession(this.accession);
			    mir_templ.setMirnaName(this.name);
			    mir_templ.setMirnaSpecies(this.species);
			    mir_templ.setMirnaSequence(this.sequence);
			    this.mirnas.add(mir_templ);
			    // System.out.println(mir_templ.toString());
			    // TODO fix the folowing lines
			    // Mirna mir = new Mirna(this.name, this.accession,
			    // this.sequence, this.species, this.mirna2fam ==
			    // null ||
			    // this.mirna2fam.get(this.species+"-"+this.name) ==
			    // null ? this.name :
			    // this.mirna2fam.get(this.species+"-"+this.name));
			    // System.out.println(mir.toString());
			    // this.mirnas.add(mir);
			}
		    }
		}
		buf.close();
	    } catch (IOException e) {
		log.log(Level.WARNING,
			"File wasn't found or could not be read.\n This should never happen since the file was checked.");
		e.printStackTrace();
		System.exit(MREfilemissing);
	    }
	}
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
				if(line.matches(this.headerRegex))
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
			return false;
		}
		
		return true;
	}


	
	
//	public ArrayList<MirnaBean> updateMirnaAtDB(){
//		MirbaseFamilyParser mbfp = new MirbaseFamilyParser("/home/mjaeger/workspace/miRNAtor/data/miRBase/miFam.dat");
//		mbfp.parse();
//		HashMap<String, String> mirAC2mirfamAC = mbfp.getMirnaAC2familyAC();
////		System.out.println(mirAC2mirfamAC.toString());
//		return this.updateMirnaAtDB(this.mirnas,mirAC2mirfamAC,false);
//	}
	

//	public ArrayList<MirnaBean> updateMirnaAtDB(HashMap<String, String> mirAC2mirfamAC){
//		return this.updateMirnaAtDB(this.mirnas,mirAC2mirfamAC);
//	}

	

//	/**
//	 * 
//	 */
//	public ArrayList<MirnaBean> updateMirnaAtDB(ArrayList<MirnaBean> mirnas,HashMap<String, String> mirAC2mirfamAC, boolean artFamily){
//		if(mirnas != null){
//			this.log.log(Level.INFO, "start updating "+mirnas.size()+" mirnas in DB");
//			MirnaDatParser mdp = new MirnaDatParser("/home/mjaeger/workspace/miRNAtor/data/miRBase/miRNA.dat");
//			mdp.parse();
//			HashMap<String, String> matureAC2mirAC = mdp.getMatureAC2mirnaAC();
////			System.out.println(matureAC2mirAC.toString());
////			System.exit(0);
//			String famAC;
//			String mirAC;
//			String matureAC;
////			MirnaFamilyManager mirfamman = MirnaFamilyManager.getInstance();
////			MirnaFamilyBean familyBean;
//			int counter = 1;
//			
//			for (MirnaBean mirnaBean : mirnas) {
//				try {
//					switch (this.mirman.countUsingTemplate(mirnaBean)) {
//					case 0:
////						if((famAC = mirAC2mirfamAC.get(matureAC2mirAC.get(mirnaBean.getMirbaseAccession()))) == null){
////						    if(artFamily){
////							familyBean	= mirfamman.createMirnaFamilyBean();
////							familyBean.setFamilyAccession(String.format("MIPFX%06d", counter++));
////							familyBean.setFamilyName(String.format("%s-%s", mirnaBean.getMirnaSpecies(),mirnaBean.getMirnaName()));
////							familyBean	= mirfamman.insert(familyBean);
////							mirnaBean.setMirnaFamilyRef(familyBean.getFamilyId());
////						    }
////						    
////						}
////						else{
////							familyBean	= mirfamman.createMirnaFamilyBean();
////							familyBean.setFamilyAccession(famAC);
////							familyBean	= mirfamman.loadUniqueUsingTemplate(familyBean);
//////							System.out.println("dit war wohl nichts "+matureAC);
//////							mirnaBean.setMirFamilyRef(newVal)
////							mirnaBean.setMirnaFamilyRef(familyBean.getFamilyId());
////						}
////						mirnaBean.setMirFamilyRef(familyBean.getFamilyId());
////						System.out.println(mirnaBean.toString());
//						mirnaBean	= this.mirman.insert(mirnaBean);
//						break;
//					case 1:
//						mirnaBean	= this.mirman.loadUniqueUsingTemplate(mirnaBean);
//						break;
//					default:
//						this.log.log(Level.SEVERE, "This should never happen that there are more than one DB entries matching our mirnabean!");
//						break;
//					}
//				} catch (DAOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//			this.log.log(Level.INFO, "finished updating mirnas in DB");
//		}
//		return this.mirnas;
//	}
	
	
	public static void main(String[] args){
	    MirbaseFastaParser mbfp = new MirbaseFastaParser("/home/mjaeger/data/miRBase/mature_hsa-collapsed.fa");
	    mbfp.parse();
	    System.out.printf("Found %d miRNAs %n",mbfp.getMirnas().size());
	    
	}

}
