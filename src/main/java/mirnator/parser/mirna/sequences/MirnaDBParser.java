package mirnator.parser.mirna.sequences;

import java.util.ArrayList;
import java.util.HashMap;
//import mirnator.mirna.Mirna;
import mirnator.sql2java.MirnaBean;

/**
 * Abstract class for all miRNA DBs like TargetScan, miRBase, etc.
 * @author mjaeger
 *
 */
public abstract class MirnaDBParser extends MirnaParser{
	protected ArrayList<MirnaBean> mirnas;
	protected HashMap<String, String> mirna2fam;
	
	protected String sequence;
	protected String species; 
	
	
	/**
	 * Inits the mirna data structure and set the filename.
	 * @param file - path to miRNA file
	 */
	public MirnaDBParser(String filename) {
		super(filename);
		this.mirnas = new ArrayList<MirnaBean>();
	}
	
	public MirnaDBParser(String filename, HashMap<String, String> mirna2fam) {
		super(filename);
		this.mirnas 	= new ArrayList<MirnaBean>();
		this.mirna2fam	= mirna2fam;
	}
	/**
	 * Get the list of miRNAs
	 * @return the mirnas
	 */
	public ArrayList<MirnaBean> getMirnas() {
		return mirnas;
	}
	
}
