package de.charite.compbio.mirnator.reference;

/**
 * A typical taxon object contains at least the following four informations:<br>
 * symbol - the letter code (e.g. hsa, mmu) common Name - the common name of the species (e.g. Human, Mouse) species -
 * the latin name of the species (e.g. homo sapiens, mus musculus) taxID - the taxonomy ID from NCBI taxonomy database
 * (e.g. 9606, 10090)
 * 
 * @author mjaeger
 *
 */
public class Taxon {
	private String symbol;
	private String commonName;
	private String species;
	private Integer taxID;

	public Taxon(String symbol, String commonName, String species, int taxID) {
		this.setSymbol(symbol);
		this.setCommonName(commonName);
		this.setSpecies(species);
		this.setTaxID(taxID);
	}

	@Override
	public String toString() {
		return getClass().getName() + "[symbol=" + this.symbol + ", commonName=" + this.commonName + ", species="
				+ this.species + ", taxID=" + this.taxID + "]";
	}

	/**
	 * @param symbol
	 *            the symbol to set
	 */
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	/**
	 * @return the symbol
	 */
	public String getSymbol() {
		return symbol;
	}

	/**
	 * @param commonName
	 *            the commonName to set
	 */
	public void setCommonName(String commonName) {
		this.commonName = commonName;
	}

	/**
	 * @return the commonName
	 */
	public String getCommonName() {
		return commonName;
	}

	/**
	 * @param species
	 *            the species to set
	 */
	public void setSpecies(String species) {
		this.species = species;
	}

	/**
	 * @return the species
	 */
	public String getSpecies() {
		return species;
	}

	/**
	 * @param taxID
	 *            the taxID to set
	 */
	public void setTaxID(Integer taxID) {
		this.taxID = taxID;
	}

	/**
	 * @return the taxID
	 */
	public Integer getTaxID() {
		return taxID;
	}
}