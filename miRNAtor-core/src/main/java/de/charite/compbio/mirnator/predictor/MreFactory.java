/**
 * 
 */
package de.charite.compbio.mirnator.predictor;

import java.util.concurrent.BlockingQueue;

import de.charite.compbio.mirnator.reference.Mirna;
import de.charite.compbio.mirnator.reference.Mre;
import de.charite.compbio.mirnator.reference.SequenceModel;

/**
 * 
 *
 * @author Marten Jäger <marten.jaeger@charite.de>
 *
 */
public abstract class MreFactory implements Runnable {

	/** The {@link Mirna} object. */
	protected Mirna mirna;
	/** The {@link SequenceModel} object. */
	protected SequenceModel sequenceModel;
	/** The queue to push the found {@link Mre}s to. */
	protected BlockingQueue<Mre> mres;
	/** current index for the subsequence */
	protected int i_subseq;
	/** dummy {@link Mre} for the temporary storage during build */
	protected Mre mre_dummy;

	public MreFactory(Mirna mirna, SequenceModel sequence, BlockingQueue<Mre> mres) {
		this.mirna = mirna;
		this.sequenceModel = sequence;
		this.mres = mres;
	}

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
