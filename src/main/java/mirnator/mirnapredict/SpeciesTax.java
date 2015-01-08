package mirnator.mirnapredict;

/**
 * 
 * @author mjaeger
 *
 */
public class SpeciesTax {
	private String symbol;
	private String commonName;
	private String species;
	private int speciesID;

	public SpeciesTax(String symbol, String commonName, String species, String speciesID) {
		this.symbol = symbol;
		this.commonName = commonName;
		this.species = species;
		this.speciesID = Integer.parseInt(speciesID);
	}

	public int getSpeciesID() {
		return this.speciesID;
	}

	public String getCommonName() {
		return commonName;
	}

	public String getSpecies() {
		return species;
	}

	public String getSymbol() {
		return symbol;
	}
}
