/**
 * 
 */
package de.charite.compbio.mirnator.predictor;

import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;

import de.charite.compbio.mirnator.constants.MREtype;
import de.charite.compbio.mirnator.reference.Mirna;
import de.charite.compbio.mirnator.reference.Mre;
import de.charite.compbio.mirnator.reference.SequenceModel;

/**
 * Factory for MREs will check short sequences and a miRNA sequences if there is a possible Seed match and additionally
 * a compensatory site. If the short sequence is to short or the compensatory site check is disabled, the check will be
 * skipped and the compensatory site flag in the {@link Mre} will be <code>null</code>
 *
 * @author Marten Jäger <marten.jaeger@charite.de>
 *
 */
public class MreCollectionfactory extends MreFactory {

	private Logger log;

	private final static int MINSEEDLENGTH = 8;
	private final static int MINCOMPENSATORYCONTINUOUSLENGTH = 4;
	private final static int MIRNACOMPENSATORYSITESTART = 11; // normally 12 but we are operating with '0'-based
																// indexes

	private boolean checkA9;
	private boolean checkCompensatory;

	/** flag indicating that an 'A' was found in the target for miRNA position 1 */
	boolean hasA1;
	/** flag indicating that an 'A' or 'U' was found in the target for miRNA position 9 */
	boolean hasAU9;
	/** flag indicating a compensatory site was found */
	boolean hasCompensatory;
	int mismatch;
	int match;
	int start;
	int end;
	int bulge;

	/**
	 * Resets all values to the default.
	 */
	private void reset() {
		hasA1 = false;
		hasAU9 = false;
		mismatch = 0;
		match = 0;
		start = 0;
		end = 0;
		bulge = 0;

	}

	/**
	 * Creates a collection
	 * 
	 * @param mir
	 * @param sequenceModel
	 * @param mreBeans
	 */
	public MreCollectionfactory(Mirna mir, SequenceModel sequences, BlockingQueue<Mre> mres) {
		super(mir, sequences, mres);
	}

	@Override
	public void run() {
		for (i_subseq = MINSEEDLENGTH; i_subseq < this.sequenceModel.sequence.length(); i_subseq++) {
			reset();
			if (checkForSeedMatch(mirna.sequence, sequenceModel.sequence, i_subseq) != null) {
				// check for target target A/U at position 9
				if (checkForAU9())
					mre_dummy.hasPos9UA = true;
				// check for compensatory
				int i = 1;
				// System.out.println(".");
				while (!hasCompensatory && i <= 5) {
					if (checkForCompensatorySite(i_subseq - MINSEEDLENGTH - i)) {
						hasCompensatory = true;
						mre_dummy.hasCompensatorySite = true;
					}
					i++;
				}
				// finally add MRE
				mres.add(mre_dummy);
			}
		}
	}

	/**
	 * This method will check for a compensatory site, which is quite similar to seed site check but starting from a
	 * different position.
	 * 
	 * @return
	 */
	private boolean checkForCompensatorySite(int c_idx) {
		int c_match = 0;
		int c_mismatch = 0;
		// int c_end = 0;
		// int c_start = 0;
		// false if not enough target sequence to check
		if (c_idx < MINCOMPENSATORYCONTINUOUSLENGTH + 1)
			return false;
		// look up matches
		for (int i = 0; i < MINCOMPENSATORYCONTINUOUSLENGTH + 1; i++) {
			if (c_mismatch > 1)
				break;
			if (MreFactory.checkComplementaryMatch(sequenceModel.sequence.charAt(c_idx - i),
					mirna.sequence.charAt(i + MIRNACOMPENSATORYSITESTART))) {
				end++;
				match++;
			} else {
				// c_start++;
				mismatch++;
			}
		}

		if (c_match >= MINCOMPENSATORYCONTINUOUSLENGTH) {
			hasCompensatory = true;
			return true;
		}
		// false if no MINCOMPENSATORYCONTINUOUSLENGTH continuous matches
		// if (c_match < MINCOMPENSATORYCONTINUOUSLENGTH)
		// return false;
		return false;
	}

	/**
	 * Checks if the 9th binding position between miRNA<>mRNA is an 'A' or 'U' in the target sequence
	 * 
	 * @return true if 'A'/'U' at 9. target position
	 */
	private boolean checkForAU9() {
		if (i_subseq < this.sequenceModel.sequence.length() - 9
				&& (MreFactory.checkComplementaryMatch(sequenceModel.sequence.charAt(i_subseq), 'U') || MreFactory
						.checkComplementaryMatch(sequenceModel.sequence.charAt(i_subseq), 'A'))) {
			hasAU9 = true;
			return true;
		} else
			return false;
	}

	/**
	 * Checks the subsequence for a seed match with the given miRNA sequence. The subsequence is checked from the 3' end
	 * and so has to be at least 8bp long.
	 * 
	 * @param mirna
	 *            the nucleic acid sequence of the miRNA from 5'-3'
	 * @param subsequence
	 *            the nucleic acid sequence of the target sequence from 5'-3'
	 * @return the miRNA seed match {@link MREtype} or <code>null</code> if no seed match was found.
	 */
	private Mre checkForSeedMatch(String mirna, String subsequence) {
		return checkForSeedMatch(mirna, subsequence, 0);
	}

	/**
	 * Checks the subsequence for a seed match with the given miRNA sequence. The subsequence is checked from the 3' end
	 * and so has to be at least 8bp long. Starting from the idx position.
	 * 
	 * @param mirna_sequence
	 *            the nucleic acid sequence of the miRNA from 5'-3'
	 * @param target_sequence
	 *            the nucleic acid sequence of the target sequence from 5'-3'
	 * @param idx
	 *            the starting position in the sequence (inclusive)
	 * @return the miRNA seed match {@link MREtype} or <code>null</code> if no seed match was found.
	 */
	private Mre checkForSeedMatch(String mirna_sequence, String target_sequence, int idx) {
		// reset all values
		reset();

		// check if the sequence is at least 8 bp long
		if (idx < MreCollectionfactory.MINSEEDLENGTH)
			return null;

		// check each position
		for (int i = 0; i < MINSEEDLENGTH; i++) {
			if (mismatch > 1)
				break;
			// check for 'A' in the first target position
			if (start == 0) {
				if (MreFactory.checkComplementaryMatch(target_sequence.charAt(idx - i), 'U'))
					hasA1 = true;
				start++;
				continue;
			}

			if (MreFactory.checkComplementaryMatch(target_sequence.charAt(idx - i), mirna_sequence.charAt(i))) {
				end++;
				match++;
			} else {
				mismatch++;
				if (match == 0)
					start++;
			}

		}

		// now check: are there at least 6matches and are these contiguous?
		if (match > 5) {
			int mirna_start = MINSEEDLENGTH - end;
			int mirna_end = MINSEEDLENGTH - start;
			int sequence_start = idx - end;
			int sequence_end = idx - start;

			if (match == 6) {
				if (start == 1) { // match 2-7/ / and A1?
					mre_dummy = new Mre(mirna, sequenceModel, sequence_start, sequence_end, mirna_start, mirna_end,
							hasA1 ? MREtype.SEVEN_A1 : MREtype.SIX);
				} else {
					mre_dummy = new Mre(mirna, sequenceModel, sequence_start, sequence_end, mirna_start, mirna_end,
							MREtype.OFFSET_SIX);
				}
			} else { // this could only be 7 matches // and A1?
				mre_dummy = new Mre(mirna, sequenceModel, sequence_start, sequence_end, mirna_start, mirna_end,
						hasA1 ? MREtype.EIGHT_A1 : MREtype.SEVEN_M8);
			}
			return mre_dummy;
		}

		return null;
	}

}
