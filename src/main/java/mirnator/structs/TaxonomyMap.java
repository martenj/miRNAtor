package mirnator.structs;

import java.util.HashMap;

public class TaxonomyMap {

	private static Taxon[] taxons;

	private static HashMap<String, Integer> symbol2index;
	private static HashMap<Integer, Integer> taxID2index;

	public TaxonomyMap(Taxon[] taxons, HashMap<String, Integer> symbol2index, HashMap<Integer, Integer> taxID2index) {
		this.taxons = taxons;
		this.symbol2index = symbol2index;
		this.taxID2index = taxID2index;
	}

	/**
	 * Returns the corresponding {@link Taxon} entry if the given species symbol (e.g. hsa, mmu, etc.) is known or
	 * <code>null</code> otherwise.
	 * 
	 * @param symbol
	 *            - species symbol (e.g. hsa,mmu,etc.)
	 * @return the Taxon entry or <code>null</code>
	 */
	public Taxon getTaxon(String symbol) {
		if (this.symbol2index.containsKey(symbol))
			return this.taxons[this.symbol2index.get(symbol)];
		else
			return null;
	}

	/**
	 * Returns the corresponding {@link Taxon} entry if the given TaxonID (e.g. 9606, 10090, etc.) is known or
	 * <code>null</code> otherwise.
	 * 
	 * @param taxID
	 *            - TaxonID (e.g. 9960, 10090, etc.)
	 * @return the Taxon entry or <code>null</code>
	 */
	public Taxon getTaxon(int taxID) {
		if (this.taxID2index.containsKey(taxID))
			return this.taxons[this.taxID2index.get(taxID)];
		else
			return null;
	}

	@Override
	public String toString() {
		return getClass().getName() + "[taxons: size=" + this.taxons.length + ", known symbols="
				+ this.symbol2index.size() + ", known taxIDs=" + this.taxID2index.size() + "]";
	}

}
