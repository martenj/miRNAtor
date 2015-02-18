package de.charite.compbio.mirnator.constants;

public enum Base {

	A, C, G, U, X;

	/**
	 * Get the letter for the corresponding base for a given {@link Base} c.
	 * 
	 * @param c
	 *            - the questioned {@link Base}
	 * @return the bases Lettercode
	 */
	public char getBaseLetter(Base c) {
		switch (c) {
		case A:
			return 'A';
		case C:
			return 'C';
		case G:
			return 'G';
		case U:
			return 'U';
			// case N: return 'N';
		default:
			return 'X';
		}
	}
}
