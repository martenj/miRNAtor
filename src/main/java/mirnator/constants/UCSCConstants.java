/**
 * 
 */
package mirnator.constants;

import java.util.HashMap;

/**
 * @author mjaeger
 *
 */
public interface UCSCConstants {

	final String goldenpathFTP = "ftp://hgdownload.cse.ucsc.edu/goldenPath/";

	final HashMap<String, String> speciesPathMap = new HashMap<String, String>() {
		/**
	 * 
	 */
		private static final long serialVersionUID = -1964340214475624890L;

		{
			put("mm9", "mm9/");
			put("mmu", "mm9/");
			put("mouse", "mm9/");
			put("hg19", "hg19/");
			put("hsa", "hg19/");
			put("human", "hg19/");
		}
	};

	final HashMap<String, String> speciesMap = new HashMap<String, String>() {
		/**
	 * 
	 */
		private static final long serialVersionUID = 7312454269517178562L;

		{
			put("mm9", "mmu");
			put("mmu", "mmu");
			put("mouse", "mmu");
			put("hg19", "hsa");
			put("hsa", "hsa");
			put("human", "hsa");
		}
	};

	final HashMap<String, String> conservationMap = new HashMap<String, String>() {
		/**
	 * 
	 */
		private static final long serialVersionUID = -5702020811590157328L;

		{
			put("mmu", "phastCons30way/vertebrate/");
			put("hsa", "phastCons46way/vertebrate/");
		}
	};

	final HashMap<String, String> conservationRegex = new HashMap<String, String>() {
		/**
	 * 
	 */
		private static final long serialVersionUID = 7732628562532277869L;

		{
			put("mmu", "chr[0-9XYMUn]{1,2}[_a-z0-9]*.data");
			put("hsa", "chr[0-9XYMUn]{1,2}[_a-z0-9]*.phastCons46way.wigFix");
		}
	};

	final String knownGene = "knownGene.txt";
	final String kgAlias = "kgAlias.txt";
	final String kgXref = "kgXref.txt";
	final String knownGeneMrna = "knownGeneMrna.txt";
	final String knownIsoforms = "knownIsoforms.txt";
	final String knownToRefSeq = "knownToRefSeq.txt";
	final String knownToEnsembl = "knownToEnsembl.txt";

	// final String mm9 = "mm9/";
	// final String hg19 = "hg19/";
	//
	// final String mmu = "mm9/";
	// final String hsa = "hg19/";
	//
	// final String mouse = "mm9/";
	// final String human = "hg19/";

	final String dbPath = "database/";
	final String chrPath = "chromosomes/";

	final String arrayLocalPath = "/media/data/data/GEO/sorted_tissues/";

}
