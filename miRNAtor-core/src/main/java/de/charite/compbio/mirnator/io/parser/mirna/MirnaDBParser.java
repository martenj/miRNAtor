package de.charite.compbio.mirnator.io.parser.mirna;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import de.charite.compbio.mirnator.reference.Mirna;

//import de.charite.mirnator.mirna.Mirna;

/**
 * Abstract class for all miRNA DBs like TargetScan, miRBase, etc.
 * 
 * @author mjaeger
 *
 */
public abstract class MirnaDBParser extends MirnaParser {
	protected ArrayList<Mirna> mirnas;
	protected HashMap<String, String> mirna2fam;

	protected String sequence;
	protected String species;

	/**
	 * Inits the mirna data structure and set the file.
	 * 
	 * @param filename
	 *            - path to miRNA file
	 */
	public MirnaDBParser(String filename) {
		this(new File(filename));
	}

	/**
	 * Inits the mirna data structure and set the filename.
	 * 
	 * @param file
	 *            - The miRNA definition file
	 */
	public MirnaDBParser(File file) {
		super(file);
		this.mirnas = new ArrayList<Mirna>();
	}

	public MirnaDBParser(String filename, HashMap<String, String> mirna2fam) {
		super(filename);
		this.mirnas = new ArrayList<Mirna>();
		this.mirna2fam = mirna2fam;
	}

	/**
	 * Get the list of miRNAs
	 * 
	 * @return the mirnas
	 */
	public ArrayList<Mirna> getMirnas() {
		return mirnas;
	}

	/**
	 * Get the list of miRNAs. The wildcard symbol '*' can be used to get all mirnas.
	 * 
	 * @return the mirnas
	 */
	public ArrayList<Mirna> getMirnas(String species) {
		if (species == null || species.equals("*"))
			return this.getMirnas();
		ArrayList<Mirna> subset = new ArrayList<Mirna>();
		for (Mirna mirna : mirnas) {
			if (mirna.getSpecies().equals(species))
				subset.add(mirna);
		}
		return subset;
	}

}
