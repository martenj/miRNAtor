package mirnator.structs.microarraydata;

/**
 * Instances of this class represent individual Genes indentified as DE or so. Just a dumb helper class, probably
 * refactor later.
 */
public class DEgene {
	// GeneSymbl pval.BH FoldChange Direction EnsemblID EntrezGeneID
	public String GeneSymbol;
	public double pval;
	public double foldchange;
	public boolean upregulated;
	public String ensembl;
	public String entrez;

	public DEgene(String gsym, String p, String FC, String direction, String EnsemblID, String EntrezID) {
		this.GeneSymbol = gsym;
		try {
			this.pval = Double.parseDouble(p);
		} catch (Exception e) {
			System.err.println("Couldnt parse double: " + p);
			System.exit(1);
		}
		try {
			this.foldchange = Double.parseDouble(p);
		} catch (Exception e) {
			System.err.println("Couldnt parse foldchange: " + FC);
			System.exit(1);
		}
		if (direction.equals("up"))
			upregulated = true;
		else if (direction.equals("down"))
			upregulated = false;
		else {
			System.err.println("Could not parse direction: " + direction);
			System.exit(1);
		}
		this.ensembl = EnsemblID;
		this.entrez = EntrezID;
	}

}