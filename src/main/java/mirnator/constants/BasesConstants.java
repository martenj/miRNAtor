package mirnator.constants;

public enum BasesConstants {

	A, C, G, U, X;

	/**
	 * Get the letter for the corresponding base for a given {@link BasesConstants} c.
	 * 
	 * @param c
	 *            - the questioned {@link BasesConstants}
	 * @return the bases Lettercode
	 */
	public char getBaseLetter(BasesConstants c) {
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
