/**
 * 
 */
package de.charite.compbio.mirnator.util;

import de.charite.compbio.mirnator.reference.Mre;

/**
 * Simple 'tools' used for the {@link Mre} construction.
 *
 * @author Marten Jäger <marten.jaeger@charite.de>
 *
 */
public class MreTools {
	public final static int MINSEEDLENGTH = 8;
	public final static int MINCOMPENSATORYCONTINUOUSLENGTH = 4;
	public final static int MIRNACOMPENSATORYSITESTART = 12; // normally 12 but we are operating with '0'-based
																// indexes

	/**
	 * Checks the two given characters for reverse complementary match - valid Watson-Crick-Pairing. e.g.<br>
	 * a,A <> t,T,u,U<br>
	 * c,C <> g,G<br>
	 * 
	 * It will check DNA and RNA nucleic acids. So also Uracil will be handled.
	 * 
	 * @param c1
	 *            the first nucleic acid (preferably from the target sequence)
	 * @param c2
	 *            the second nucleic acid (most likely from the miRNA sequence)
	 * @return
	 */
	public static boolean checkComplementaryMatch(char c1, char c2) {
		switch (c1) {
		case 'a':
		case 'A':
			return c2 == 'u' || c2 == 'U' || c2 == 't' || c2 == 'T' ? true : false;
		case 'c':
		case 'C':
			return c2 == 'g' || c2 == 'G' ? true : false;
		case 'g':
		case 'G':
			return c2 == 'c' || c2 == 'C' ? true : false;
		case 't':
		case 'T':
		case 'u':
		case 'U':
			return c2 == 'a' || c2 == 'A' ? true : false;
		default:
			return false;
		}
	}

}
