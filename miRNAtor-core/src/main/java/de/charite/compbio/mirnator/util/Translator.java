/**
 * 
 */
package de.charite.compbio.mirnator.util;

/**
 * Singleton to perform some translation magic on input Strings.
 *
 * @author Marten Jäger <marten.jaeger@charite.de>
 *
 */
public class Translator {

	private static Translator instance = null;

	private Translator() {
	}

	public static Translator getInstance() {
		if (instance == null)
			instance = new Translator();
		return instance;

	}

	public String getcomplementSequence(String seq) {
		StringBuilder seqBuild = new StringBuilder(seq);
		seqBuild = seqBuild.reverse();
		for (int i = 0; i < seqBuild.length(); i++) {
			switch (seqBuild.charAt(i)) {
			case 'a':
			case 'A':
				seqBuild.setCharAt(i, 'T');
				break;
			case 'c':
			case 'C':
				seqBuild.setCharAt(i, 'G');
				break;
			case 'g':
			case 'G':
				seqBuild.setCharAt(i, 'C');
				break;
			case 't':
			case 'T':
			case 'u':
			case 'U':
				seqBuild.setCharAt(i, 'A');
				break;
			default:
				seqBuild.setCharAt(i, 'N');
				break;
			}
		}
		return seqBuild.toString();

	}

}
