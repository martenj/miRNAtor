package mirnator.structs.mirna;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mirnator.parser.mirna.sequences.MirbaseFastaParser;
import mirnator.sql2java.MirnaBean;

/**
 * miRNAsummarize is designed to summarize a given set of miRNAs according to identical seed sequences (bp 1-8) or
 * respectively (bp n..n+i)<br>
 * 
 * @author mjaeger TODO this class needs to be checked for functionality
 */
public class MiRNAsummarize {
	private int start;
	private int stop;
	private String species;
	private int acc; // used only for generation of new family names like
						// MIF100000

	HashMap<String, MirnaBean> oldMirnas;
	HashMap<String, MirnaBean> summmarizedMirnas;

	/**
	 * initiates summarization with default seed (bp 1..8)
	 * 
	 * @param miRNAs
	 *            miRNAs styled: (mmu-miR-29a,UAGCACCAUCUGAAAUCGGUUA)
	 */
	public MiRNAsummarize(HashMap<String, MirnaBean> miRNAs) {
		this(miRNAs, 1, 8);
	}

	public MiRNAsummarize(HashMap<String, MirnaBean> miRNAs, String species) {
		this(miRNAs, 1, 8, species);
	}

	public MiRNAsummarize(HashMap<String, MirnaBean> miRNAs, int start, int stop) {
		this(miRNAs, start, stop, "");
	}

	public MiRNAsummarize(HashMap<String, MirnaBean> miRNAs, int start, int stop, String species) {
		this.oldMirnas = miRNAs;
		this.start = start;
		this.stop = stop;
		this.species = species;
		this.summmarizedMirnas = new HashMap<String, MirnaBean>();
		this.summarize();
	}

	/**
	 * go through all mirnas ...
	 * 
	 */
	public void summarize() {
		System.err.printf("Filtering %d miRNAs for species.%n", this.oldMirnas.size());
		if (!this.species.equals("")) {
			Iterator<MirnaBean> mirnaIter = this.oldMirnas.values().iterator();
			MirnaBean mirBean;
			while (mirnaIter.hasNext()) {
				mirBean = mirnaIter.next();
				// System.out.println(mirBean.getMirnaName());
				if (!mirBean.getMirnaSpecies().equals(this.species)) {
					mirnaIter.remove();
					// this.oldMirnas.remove(mirBean.getMirbaseAccession());
				}
			}
		}
		System.out.printf("found %d miRNAs for species: %s%n", this.oldMirnas.size(), this.species);

		// System.out.println("original # miRNAs: "+this.oldMirnas.size());
		while (!this.oldMirnas.isEmpty()) {

			ArrayList<String> keys = new ArrayList<String>();
			Iterator<String> iter = this.oldMirnas.keySet().iterator();
			String actualmiRNAkey = iter.next();
			// System.out.println("act mirna key: "+actualmiRNAkey);
			keys.add(actualmiRNAkey);
			MirnaBean actMirna = this.oldMirnas.get(actualmiRNAkey);
			String actSeed = actMirna.getMirnaSequence().substring(start, stop);
			while (iter.hasNext()) {
				String checkMirnaKey = iter.next();
				MirnaBean chkMirna = this.oldMirnas.get(checkMirnaKey);
				String chkseed = chkMirna.getMirnaSequence().substring(start, stop);
				if (actSeed.equals(chkseed)) {
					keys.add(checkMirnaKey);
					// System.out.println("check key: "+checkMirnaKey);
				}
			}

			// System.out.println("mirnakeys size: "+keys.size());
			String newName = generateKey(keys);
			String acc = generateACC();
			// System.out.println("newkey: "+newKey);

			actMirna.setMirbaseAccession(acc);
			actMirna.setMirnaName(newName);
			actMirna.setMirnaSequence(actMirna.getMirnaSequence().substring(0, stop));
			this.summmarizedMirnas.put(newName, actMirna);

			for (String key : keys) {
				this.oldMirnas.remove(key);
			}
		}

		System.out.println("now: " + this.summmarizedMirnas.size());
	}

	/**
	 * Takes a List of miRNA identifiers (e.g. miR-29a, miR-29b,...) and generates combined key like
	 * "species"-miR-29a/29b/29c etc.
	 * 
	 * @param keys
	 *            - List of miRNA identifiers (e.g. miR-29a, miR-29b,...)
	 * @return combined key
	 */
	private String generateKey(ArrayList<String> keys) {
		String miR = "miR-";
		if (this.species.length() > 0)
			// miR = this.species+"-miR-";
			miR = "miR-";
		Pattern p = Pattern.compile("([a-z0-9]{3,5})-([miR|let|lin|bantam]*)-*([0-9a-zA-Z.\\-]+\\**)");
		// Pattern p =
		// Pattern.compile("([a-z]{3}\\-[letmiR]{3}\\-?)([0-9a-z*]+(\\-[0-9a-z*]+)?)");
		for (int i = 0; i < keys.size(); i++) {
			String miRname = keys.get(i);
			Matcher m = p.matcher(miRname);
			// System.out.println(miRname);
			if (m.find()) {
				// System.out.println("g1: "+m.group(1)+"g2: "+m.group(2));

				miR += (i < 1) ? m.group(3) : "/" + m.group(3);
				// miR+= (i < 1)? m.group(1) : "/"+m.group(1);
			} else {
				System.err.println("Error while matching miRNA " + miRname);
			}
		}
		// System.out.println(miR);
		return miR;
	}

	private String generateACC() {
		int i = 100000;

		return "MIF" + (i + this.acc++);
	}

	/**
	 * 
	 * @return
	 */
	public HashMap<String, MirnaBean> getSumedMirnas() {
		return this.summmarizedMirnas;
	}

	public static void main(String[] args) {
		MirbaseFastaParser mbfp = new MirbaseFastaParser("/home/mjaeger/data/miRBase/mature.fa");
		// MirbaseFastaParser mbfp = new
		// MirbaseFastaParser("/home/mjaeger/data/miRBase/mature_mmu-miR-15fam.fa");
		mbfp.parse();
		HashMap<String, MirnaBean> mirnas = new HashMap<String, MirnaBean>();
		for (MirnaBean mirBean : mbfp.getMirnas()) {
			// System.out.println(mirBean);
			mirnas.put(mirBean.getMirnaSpecies() + "-" + mirBean.getMirnaName(), mirBean);
		}
		MiRNAsummarize ms = new MiRNAsummarize(mirnas, 1, 8, "gga");

		BufferedWriter out = null;
		try {
			out = new BufferedWriter(
					new FileWriter("/home/mjaeger/data/miRBase/mature_" + ms.species + "-collapsed.fa"));
			for (MirnaBean mirBean : ms.getSumedMirnas().values()) {
				out.write(mirBean.toStringFasta() + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null)
					out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

}
