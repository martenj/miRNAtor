/**
 * 
 */
package mirnator.constants;

import mirnator.parser.mirna.targets.MREdatabaseParser;

/**
 * @author mjaeger
 *
 */
public enum TestDesignConstants {
	ThreeUTRonly, ThreeUTRplusCDS, CDSonly, ThreeUTRplus2MREs, ThreeUTRplus2MREin40bp, ThreeUTRplusCONSERVATION, ThreeUTRplusFREEENERGY, ThreeUTRplusCONSERVATIONplusFREEENERGY;

	/**
	 * Returns a short description for the MRE represented by the given MRE type constant.
	 * 
	 * @param m
	 *            - the MREtypeConstant
	 * @return short description for the MRE
	 */
	public String getDescription(TestDesignConstants c) {
		String description;
		switch (c) {
		case ThreeUTRonly:
			description = "only 3'UTR hits";
			break;
		case ThreeUTRplusCDS:
			description = "3'UTR hits and CDS hits";
			break;
		case CDSonly:
			description = "only CDS hits";
			break;
		case ThreeUTRplus2MREs:
			description = "only 3'UTR and at least two MRE hits";
			break;
		case ThreeUTRplus2MREin40bp:
			description = "only 3'UTR and at least two MRE hits in a distance of 40bp";
			break;
		case ThreeUTRplusCONSERVATION:
			description = "3'UTR hits and a high conservation score";
			break;
		case ThreeUTRplusFREEENERGY:
			description = "3'UTR hits and a high free energy score";
			break;
		case ThreeUTRplusCONSERVATIONplusFREEENERGY:
			description = "3'UTR hits and a high conservation and free energy score";
			break;
		default:
			description = "unknown test design";
			break;
		}

		return description;
	}

	/**
	 * 
	 * @param c
	 *            - Testdesignconstant
	 * @param path
	 *            - jdbc DB URL
	 * @param mreGroup
	 *            -
	 * @return
	 */
	public static MREdatabaseParser getMREdatabaseParser(TestDesignConstants c, String path, int mreGroup) {
		MREdatabaseParser mdp;
		switch (c) {
		case ThreeUTRonly:
			mdp = new MREdatabaseParser(path, false, false, true, Integer.MIN_VALUE, Integer.MAX_VALUE, -1, mreGroup);
			break;
		case ThreeUTRplusCDS:
			mdp = new MREdatabaseParser(path, false, true, true, Integer.MIN_VALUE, Integer.MAX_VALUE, -1, mreGroup);
			break;
		case CDSonly:
			mdp = new MREdatabaseParser(path, false, true, false, Integer.MIN_VALUE, Integer.MAX_VALUE, -1, mreGroup);
			break;
		case ThreeUTRplus2MREs:
			mdp = new MREdatabaseParser(path, false, false, true, Integer.MIN_VALUE, Integer.MAX_VALUE, 2, mreGroup);
			break;
		case ThreeUTRplus2MREin40bp:
			mdp = new MREdatabaseParser(path, false, false, true, Integer.MIN_VALUE, Integer.MAX_VALUE, 40, mreGroup);
			break;
		case ThreeUTRplusCONSERVATION:
			mdp = new MREdatabaseParser(path, false, false, true, Integer.MIN_VALUE, 2, -1, mreGroup);
			break;
		case ThreeUTRplusFREEENERGY:
			mdp = new MREdatabaseParser(path, false, false, true, -8, Integer.MAX_VALUE, -1, mreGroup);
			break;
		case ThreeUTRplusCONSERVATIONplusFREEENERGY:
			mdp = new MREdatabaseParser(path, false, false, true, -8, 2, -1, mreGroup);
			break;
		default:
			mdp = null;
			break;
		}
		return mdp;
	}
}
