package mirnator.structs.mirna;

public class MatureMiRNA {

	private String name;
	private String rna;
	private int len;

	public MatureMiRNA(String nameline, String rna) {
		int start, end;
		start = 0;
		if (nameline.charAt(0) == '>')
			start = 1; // remove FASTA symbol if necessary
		end = nameline.indexOf(' ', start); // find first space
		String s = nameline.substring(start, end);
		this.name = s;
		this.rna = rna.trim().toUpperCase();
	}

	/**
	 * Returns the miRNA name (e.g. mmu-miR-29a etc.).
	 * 
	 * @return miRNA name
	 */
	public String getName() {
		return this.name;
	}

	public String getRNA() {
		return this.rna;
	}

	/**
	 * Returns the length of the miRNA.
	 * 
	 * @return the length
	 */
	public int getLen() {
		return this.rna.length();
	}

	public String getSeed7AsRNA() {
		return this.rna.substring(1, 8);
	}

	public String getSeed8AsRNA() {
		return this.rna.substring(0, 8);
	}

	/**
	 * Return the 7mer seed sequence as the reverse complement DNA sequence that would be present in the mRNA target of
	 * this microRNA.
	 */
	public String getSeed7AsDNArc() {
		String R = getSeed7AsRNA();
		return getDNAreverseComp(R);
	}

	/**
	 * Return the 8mer seed sequence as the reverse complement DNA sequence that would be present in the mRNA target of
	 * this microRNA.
	 */
	public String getSeed8AsDNArc() {
		String R = getSeed8AsRNA();
		return getDNAreverseComp(R);
	}

	private String getDNAreverseComp(String RNA) {
		StringBuilder DNA = new StringBuilder(RNA).reverse();

		for (int i = 0; i < DNA.length(); ++i) {
			char c = DNA.charAt(i);
			switch (c) {
			case 'A':
				DNA.setCharAt(i, 'T');
				break;
			case 'C':
				DNA.setCharAt(i, 'G');
				break;
			case 'G':
				DNA.setCharAt(i, 'C');
				break;
			case 'U':
				DNA.setCharAt(i, 'A');
				break;
			default:
				System.err.println("Found invalid character in miRNA seed:" + c);
				System.err.println("while trying to convert " + this.rna);
				System.exit(1);
			}
		}
		return DNA.toString();
	}

}