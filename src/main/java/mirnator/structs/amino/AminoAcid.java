/**
 * 
 */
package mirnator.structs.amino;

/**
 * The AminoAcid class stores more or less codon information for a single amino acid.<br>
 * e.g.<br>
 * <code>
 * Amino Acid	Code3	Code1	Codons 	Compressed<br>
 * Alanine	Ala	A 	GCU,GCC,GCA,GCG 	GCN<br>
 * </code>
 * 
 * @author mjaeger
 *
 */
public class AminoAcid {
	private String name;
	private String code3;
	private String code1;
	private String[] codons;
	private String[] compressedCodons;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the code3
	 */
	public String getCode3() {
		return code3;
	}

	/**
	 * @param code3
	 *            the code3 to set
	 */
	public void setCode3(String code3) {
		this.code3 = code3;
	}

	/**
	 * @return the code1
	 */
	public String getCode1() {
		return code1;
	}

	/**
	 * @param code1
	 *            the code1 to set
	 */
	public void setCode1(String code1) {
		this.code1 = code1;
	}

	/**
	 * @return the codons
	 */
	public String[] getCodons() {
		return codons;
	}

	/**
	 * @param codons
	 *            the codons to set
	 */
	public void setCodons(String[] codons) {
		this.codons = codons;
	}

	/**
	 * @return the compressedCodons
	 */
	public String[] getCompressedCodons() {
		return compressedCodons;
	}

	/**
	 * @param compressedCodons
	 *            the compressedCodons to set
	 */
	public void setCompressedCodons(String[] compressedCodons) {
		this.compressedCodons = compressedCodons;
	}

	/**
	 * Returns the amino acid in a {@link String} representation.
	 * 
	 * @return AA in string representation
	 */
	public String toLine() {
		return (getName() + "\t" + getCode3() + "\t" + getCode1() + "\t" + array2String(getCodons()) + "\t" + array2String(getCompressedCodons()));
	}

	private String array2String(String[] string) {
		String result = "";
		if (string != null) {
			boolean first = true;
			for (String string2 : string) {
				if (first) {
					first = false;
				} else
					result += ",";
				result += string2;
			}
		}
		return result;
	}

}
