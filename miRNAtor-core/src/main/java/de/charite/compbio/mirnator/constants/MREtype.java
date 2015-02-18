/**
 * 
 */
package de.charite.compbio.mirnator.constants;

/**
 * @author mjaeger
 *
 */
public enum MREtype {
	// SEVEN_A1, SEVEN_M8, EIGHT, SIX, OFFSET_SIX, COMPENSATORY17, COMPENSATORY28, COMPENSATORY18,
	// MIRBASESPECIFIC, SEVEN_M1, FIVE_BULG_FOUR, SIX_BULG_FIVE, SEVEN_BULG_SIX, BULG_A, BULG_C, BULG_G, BULG_U;
	SEVEN_A1, SEVEN_M8, SIX, OFFSET_SIX, EIGHT_A1;

	/**
	 * Returns the short name (e.g. 7a1, 8m, etc.) for the given MRE type constant.
	 * 
	 * @param m
	 *            MREtypeConstant
	 * @return MRE short name
	 */
	public String getShortName(MREtype m) {
		String shortName;
		switch (m) {
		case SEVEN_A1:
			shortName = "7a1"; // 0
			break;
		case SEVEN_M8:
			shortName = "7m8"; // 1
			break;
		// case EIGHT:
		// shortName = "8m"; // 2
		// break;
		case SIX:
			shortName = "6m"; // 3
			break;
		case OFFSET_SIX:
			shortName = "6o"; // 4
			break;
		// case COMPENSATORY17:
		// shortName = "c17"; // 5
		// break;
		// case COMPENSATORY28:
		// shortName = "c28"; // 6
		// break;
		// case COMPENSATORY18:
		// shortName = "c18"; // 7
		// break;
		case EIGHT_A1:
			shortName = "8a1"; // 8
			break;
		// case MIRBASESPECIFIC:
		// shortName = "mrb"; // 9
		// break;
		// case SEVEN_M1:
		// shortName = "7m1"; // 10
		// break;
		// case FIVE_BULG_FOUR:
		// shortName = "5b4"; // 11
		// break;
		// case SIX_BULG_FIVE:
		// shortName = "6b5"; // 12
		// break;
		// case SEVEN_BULG_SIX:
		// shortName = "7b6"; // 13
		// break;
		// case BULG_A:
		// shortName = "6ba"; // 14
		// break;
		// case BULG_C:
		// shortName = "6bc"; // 15
		// break;
		// case BULG_G:
		// shortName = "6bg"; // 16
		// break;
		// case BULG_U:
		// shortName = "6bu"; // 17
		// break;
		default:
			shortName = "unknown MRE";
			break;
		}
		return shortName;
	}

	/**
	 * Returns the group the MRE is assigned to:<br>
	 * group 6+ = 0 - all MREs with at least 6 matching positions<br>
	 * group 7+ = 1 - all MREs with at least 7 matching positions + A1<br>
	 * group 8+ = 2 - all MREs with at least 8 matching positions + A1<br>
	 * other = -1 - includes unknown and MirBase specific
	 * 
	 * @param m
	 *            MREtypeConstant
	 * @return MRE group
	 */
	public static int getMreGroup(MREtype m) {
		switch (m) {
		case SEVEN_A1:
			return (1); // 0
		case SEVEN_M8:
			return (1); // 1
			// case EIGHT:
			// return (2); // 2
		case SIX:
			return (0); // 3
		case OFFSET_SIX:
			return (0); // 4
			// case COMPENSATORY17:
			// return (-1); // 5
			// case COMPENSATORY28:
			// return (-1); // 6
			// case COMPENSATORY18:
			// return (-1); // 7
		case EIGHT_A1:
			return (2); // 8
			// case MIRBASESPECIFIC:
			// return (-1); // 9
			// case SEVEN_M1:
			// return (1); // 10
			// case FIVE_BULG_FOUR:
			// return (-11); // 11
			// case SIX_BULG_FIVE:
			// return (-11); // 12
			// case SEVEN_BULG_SIX:
			// return (-11); // 13
			// case BULG_A:
			// return (-11); // 14
			// case BULG_C:
			// return (-11); // 15
			// case BULG_G:
			// return (-11); // 16
			// case BULG_U:
			// return (-11); // 17

		default:
			return (-1);
		}
	}

	/**
	 * Returns a short description for the MRE represented by the given MRE type constant.
	 * 
	 * @param m
	 *            - the MREtypeConstant
	 * @return short description for the MRE
	 */
	public String getDescription(MREtype m) {
		String description;
		switch (m) {
		case SEVEN_A1:
			description = "Bartel - Seed match + A at position 1 of transcript";
			break;
		case SEVEN_M8:
			description = "Bartel - Seed match + match at position 8";
			break;
		// case EIGHT:
		// description = "Bartel* - Seed match + match at position 1 and 8";
		// break;
		case SIX:
			description = "Bartel - Seed match";
			break;
		case OFFSET_SIX:
			description = "Bartel - matching at position 3-8 ";
			break;
		// case COMPENSATORY17:
		// description = "Bartel* - Seed match + ";
		// break;
		// case COMPENSATORY28:
		// description = "Bartel* - Seed match + ";
		// break;
		// case COMPENSATORY18:
		// description = "Bartel* - Seed match + ";
		// break;
		case EIGHT_A1:
			description = "Bartel* - Seed match + match at position 8 + A at position 1 of transcript";
			break;
		// case MIRBASESPECIFIC:
		// description = "miRBase specific MRE";
		// break;
		// case SEVEN_M1:
		// description = "Bartel* - Seed match + match at position 1";
		// break;
		// case FIVE_BULG_FOUR:
		// description = "Chi* - Seed match + mRNA bulg between bp 4-5 (miRNA)";
		// break;
		// case SIX_BULG_FIVE:
		// description = "Chi - Seed match + mRNA bulg between bp 5-6 (miRNA)";
		// break;
		// case SEVEN_BULG_SIX:
		// description = "Chi* - Seed match + mRNA bulg between bp 6-7 (miRNA)";
		// break;
		// case BULG_A:
		// description = "Chi - Seed match + mRNA A bulg";
		// break;
		// case BULG_C:
		// description = "Chi - Seed match + mRNA C bulg";
		// break;
		// case BULG_G:
		// description = "Chi - Seed match + mRNA G bulg";
		// break;
		// case BULG_U:
		// description = "Chi - Seed match + mRNA U bulg";
		// break;

		default:
			description = "unknown MRE";
			break;
		}
		return description;
	}
}
