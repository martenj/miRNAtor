package mirnator.parser.ucsc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import mirnator.sql2java.MreBean;
import mirnator.sql2java.MreManager;

public class MREfileReader {

	private static final Integer NoOfColumns = 22;

	private String filename;
	private File file;
	private BufferedReader buf;

	private ArrayList<MreBean> mres;

	public MREfileReader(String filename) {
		this.filename = filename;
		this.file = new File(filename);
	}

	/**
	 * Reads and stores all MREs for the given chromosom (e.g. chr5). <br>
	 * The ArrayList with the mres is here initialized, since it was never used before.
	 * 
	 * @param chromosom
	 */
	public void readAllMresForChromosom(String chromosom) {
		this.mres = new ArrayList<MreBean>();

		String line;
		String[] fields;
		MreBean mre;

		long start;
		long end;

		start = System.currentTimeMillis();
		try {
			this.buf = new BufferedReader(new FileReader(this.file));
			while ((line = buf.readLine()) != null) {
				if (line.contains(chromosom + ",")) { // need to add a "," for chromosomes like (chr1, chr10,...)
					fields = line.split(",");
					// System.out.println(fields.length);
					if (fields.length != NoOfColumns) {
						System.err.println("invalid line: " + line);
						continue;
					}
					// else
					// System.out.println("valid line: "+line);
					mre = MreManager.getInstance().createMreBean();
					mre.setMreId(Integer.parseInt(fields[0]));
					mre.setMirnaRef(Integer.parseInt(fields[1]));
					mre.setKnowngeneRef(Integer.parseInt(fields[2]));
					mre.setKnowngeneStart(Integer.parseInt(fields[3]));
					mre.setKnowngeneEnd(Integer.parseInt(fields[4]));
					mre.setMreTypeRef(Integer.parseInt(fields[5]));
					mre.setMreSource(Integer.parseInt(fields[6]));
					mre.setMirnaStart(Integer.parseInt(fields[7]));
					mre.setMirnaEnd(Integer.parseInt(fields[8]));
					mre.setChromosom(fields[9]);
					// mre.setStrand(Boolean.parseBoolean(fields[10]));
					mre.setStrand(parseBoolean(fields[10]));
					// System.out.println(mre.getStrand());
					mre.setChrStart(Integer.parseInt(fields[11]));
					mre.setChrEnd(Integer.parseInt(fields[12]));
					if (fields[13].equals("<null>"))
						mre.setMreConservationScore(null);
					else
						mre.setMreConservationScore(Float.parseFloat(fields[13]));
					if (fields[14].equals("<null>"))
						mre.setMreFreeEnergyScore(null);
					else
						mre.setMreFreeEnergyScore(Float.parseFloat(fields[14]));
					if (fields[15].equals("<null>"))
						mre.setMreConservationLocalScore(null);
					else
						mre.setMreConservationLocalScore(Float.parseFloat(fields[15]));
					if (fields[16].equals("<null>"))
						mre.setCodon1Frequency(null);
					else
						mre.setCodon1Frequency(Float.parseFloat(fields[16]));
					if (fields[17].equals("<null>"))
						mre.setCodon2Frequency(null);
					else
						mre.setCodon2Frequency(Float.parseFloat(fields[17]));
					if (fields[18].equals("<null>"))
						mre.setCodon3Frequency(null);
					else
						mre.setCodon3Frequency(Float.parseFloat(fields[18]));
					if (fields[19].equals("<null>"))
						mre.setCodon1FrequencyAa(null);
					else
						mre.setCodon1FrequencyAa(Float.parseFloat(fields[19]));
					if (fields[20].equals("<null>"))
						mre.setCodon2FrequencyAa(null);
					else
						mre.setCodon2FrequencyAa(Float.parseFloat(fields[20]));
					if (fields[21].equals("<null>"))
						mre.setCodon3FrequencyAa(null);
					else
						mre.setCodon3FrequencyAa(Float.parseFloat(fields[21]));

					this.mres.add(mre);
				}
			}
			this.buf.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		end = System.currentTimeMillis();

		System.out.println("Daten einlesen: " + ((end - start) / 1000) + " sec");

	}

	private boolean parseBoolean(String string) {
		if (string.equals("TRUE"))
			return true;
		if (string.equals("true"))
			return true;
		if (string.equals("t"))
			return true;
		if (string.equals("1"))
			return true;

		return false;
	}

	/**
	 * Sorts the list of MREs according to their chromosomal starting position.
	 */
	public void sortMREs() {
		long start;
		long end;

		start = System.currentTimeMillis();
		Comparator<MreBean> mreByChromStartComparator = new Comparator<MreBean>() {

			public int compare(MreBean o1, MreBean o2) {
				if (o1.getChrStart() < o2.getChrStart())
					return -1;
				if (o1.getChrStart() > o2.getChrStart())
					return 1;
				return 0;
			}
		};

		Collections.sort(this.mres, mreByChromStartComparator);
		end = System.currentTimeMillis();

		System.out.println("Daten sortieren: " + ((end - start) / 1000) + " sec");
	}

	// public static void main (String[] args){
	// MREfileReader test = new
	// MREfileReader("/home/mjaeger/workspace/miRNAtor/results/resultMREprediction_top10000.hsa");
	// test.readAllMresForChromosom("chr5");
	// System.out.println("# of mres: "+test.mres.size());
	// // for(int i=0;i< 10;i++){
	// // System.out.print(test.mres.get(i).toStringFlatFile());
	// // }
	// test.sortMREs();
	// // for(int i=0;i< 10;i++){
	// // System.out.print(test.mres.get(i).toStringFlatFile());
	// // }
	// }

	/**
	 * @return the mres
	 */
	public ArrayList<MreBean> getMres() {
		return mres;
	}

}
