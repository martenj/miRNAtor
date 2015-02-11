/**
 * 
 */
package de.charite.compbio.mirnator.predictor;

import java.util.concurrent.BlockingQueue;

import de.charite.compbio.mirnator.reference.Mirna;
import de.charite.compbio.mirnator.reference.Mre;
import de.charite.compbio.mirnator.reference.SequenceModel;

/**
 * This will look for the Bartel MREs including a lookup for compensatory sites. We therefore have to start at the end
 * of the target sequence
 *
 * @author Marten Jäger <marten.jaeger@charite.de>
 *
 */
public class BartelPlusMREpredictor extends MREpredictor {

	private final static int MINTARGETLENGTH = 8;
	private final static int MINCOMPENSATORYLENGTH = 20;

	/** current 3' position in the target sequence */
	private int i_subseq;

	/** should the algorithm also look for compensatory sites? Only if remaining target length >= MINCOMPENSATORYLENGTH */
	private boolean lookupCompensatory = true;

	public BartelPlusMREpredictor(Mirna mir, SequenceModel sequenceModel, BlockingQueue<Mre> mreBeans) {
		super(mir, sequenceModel, mreBeans);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		for (i_subseq = sequenceModel.sequence.length(); i_subseq >= MINTARGETLENGTH; i_subseq--) {
			if (i_subseq < MINCOMPENSATORYLENGTH)
				lookupCompensatory = false;
		}

	}

	/**
	 * Checks if there is at least a valid seed match at the current target position
	 * 
	 * @param i
	 *            the current 3' position in the {@link SequenceModel} sequence
	 */
	private void checkSeed() {
		boolean a1 = false;
		int mismatch = 0;
		int match = 0;
		int start = 0;
		int end = 0;
		char T;
		char M;

		for (int i_checkSeed = i_subseq - 1; i_checkSeed >= i_subseq - 8; i_checkSeed--) {
			T = sequenceModel.sequence.charAt(i_checkSeed);
			// check for 'A' in the first target position
			if (start == 0) {
				if (T == 'A' || T == 'a')
					a1 = true;
				else

					start++;
				continue;
			}

		}
	}
}
