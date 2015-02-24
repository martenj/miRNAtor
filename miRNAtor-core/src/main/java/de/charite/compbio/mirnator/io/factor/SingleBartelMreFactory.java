/**
 * 
 */
package de.charite.compbio.mirnator.io.factor;

import de.charite.compbio.mirnator.constants.MREtype;
import de.charite.compbio.mirnator.reference.Mirna;
import de.charite.compbio.mirnator.reference.Mre;
import de.charite.compbio.mirnator.reference.SequenceModel;
import de.charite.compbio.mirnator.util.MreTools;

/**
 * Creates a single {@link Mre} given a {@link Mirna}, a {@link SequenceModel} and the position in the
 * {@link SequenceModel}
 *
 * @author Marten Jäger <marten.jaeger@charite.de>
 *
 */
public class SingleBartelMreFactory {

	/** check for AU9 site */
	private boolean checkA9;
	/** check for compensatory site */
	private boolean checkCompensatory;

	/** The {@link Mirna} object. */
	protected Mirna mirna;
	/** The {@link SequenceModel} object. */
	protected SequenceModel sequenceModel;
	/** current index for the subsequence */
	protected int i_subseq;
	/** dummy {@link Mre} for the temporary storage during build */
	protected Mre mre_dummy;

	// collection of infos

	public SingleBartelMreFactory(Mirna mir, SequenceModel sequence, int idx) {
		this(mir, sequence, idx, true, true);
	}

	public SingleBartelMreFactory(Mirna mir, SequenceModel sequence, int idx, boolean checkA9, boolean checkComp) {
		this.mirna = mir;
		this.sequenceModel = sequence;
		this.i_subseq = idx;
		this.checkA9 = checkA9;
		this.checkCompensatory = checkComp;
	}

	public Mre build() {
		if (checkSeedMatch(mirna.sequence, sequenceModel.sequence) != null) {
			// check for target target A/U at position 9
			if (checkAU9site())
				mre_dummy.hasPos9UA = true;
			// check for compensatory
			int i = 0;
			// System.out.println(".");
			while (!mre_dummy.hasCompensatorySite && i < 5) {
				checkCompensatorySite(i_subseq - MreTools.MINSEEDLENGTH - i - 2);
				// System.out.println(i_subseq - MreTools.MINSEEDLENGTH - i - 2);
				i++;
			}
		}
		return mre_dummy;
	}

	/**
	 * This method will check for a compensatory site, which is quite similar to seed site check but starting from a
	 * different position.
	 * 
	 * @return
	 * 
	 * @return
	 */
	private void checkCompensatorySite(int c_idx) {
		int c_match = 0;
		int c_mismatch = 0;
		// int c_end = 0;
		// int c_start = 0;
		// false if not enough target sequence to check
		if (c_idx < MreTools.MINCOMPENSATORYCONTINUOUSLENGTH)
			return;
		// look up matches
		for (int i = 0; i < MreTools.MINCOMPENSATORYCONTINUOUSLENGTH; i++) {
			if (c_mismatch > 0)
				break;
			// System.out.println(sequenceModel.sequence.charAt(c_idx - i) + " - "
			// + mirna.sequence.charAt(i + MreTools.MIRNACOMPENSATORYSITESTART));
			if (MreTools.checkComplementaryMatch(sequenceModel.sequence.charAt(c_idx - i),
					mirna.sequence.charAt(i + MreTools.MIRNACOMPENSATORYSITESTART))) {
				// end++;
				c_match++;
			} else {
				// c_start++;
				c_mismatch++;
			}
		}

		if (c_match >= MreTools.MINCOMPENSATORYCONTINUOUSLENGTH) {
			mre_dummy.hasCompensatorySite = true;
		}
	}

	/**
	 * Checks if the 9th binding position between miRNA<>mRNA is an 'A' or 'U' in the target sequence Check:<br>
	 * - current position bigger than the minimum seed length (8) -> we are checking position 9
	 * 
	 * @return true if 'A'/'U' at 9. target position
	 */
	private boolean checkAU9site() {
		if (i_subseq >= MreTools.MINSEEDLENGTH
				&& (MreTools.checkComplementaryMatch(sequenceModel.sequence.charAt(i_subseq - MreTools.MINSEEDLENGTH),
						'U') || MreTools.checkComplementaryMatch(
						sequenceModel.sequence.charAt(i_subseq - MreTools.MINSEEDLENGTH), 'A'))) {
			mre_dummy.hasPos9UA = true;
			return true;
		} else
			return false;
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
	private Mre checkSeedMatch(String mirna_sequence, String target_sequence) {
		int mismatch = 0;
		int match = 0;
		int start = 0;
		int end = 0;
		// int bulge;
		boolean hasA1 = false;

		// check if the sequence is at least 8 bp long
		if (i_subseq < MreTools.MINSEEDLENGTH - 1)
			return null;

		// System.out.println("i\tstart\tend\tmatch\tmmatch\thasA1");
		// check each position
		for (int i = 0; i < MreTools.MINSEEDLENGTH; i++) {
			// System.out.print(i + "\t" + start + "\t" + end + "\t" + match + "\t" + mismatch + "\t" + hasA1);
			// System.out.println("\tcheck: " + target_sequence.charAt(i_subseq - i) + " - " +
			// mirna_sequence.charAt(i));
			if (mismatch > 1)
				break;
			// check for 'A' in the first target position
			if (start == 0) {
				if (MreTools.checkComplementaryMatch(target_sequence.charAt(i_subseq - i), 'U'))
					hasA1 = true;
				start++;
				continue;
			}

			if (MreTools.checkComplementaryMatch(target_sequence.charAt(i_subseq - i), mirna_sequence.charAt(i))) {
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
			int sequence_start = i_subseq - match - (start > 1 ? 1 : 0); // correct for offset
			int sequence_end = i_subseq - start + 1; // add mismatch for first position exclusive

			if (match == 6) {
				if (start == 1) { // match 2-7/ / and A1?
					mre_dummy = new Mre(mirna, sequenceModel, sequence_start, sequence_end, hasA1 ? MREtype.SEVEN_A1
							: MREtype.SIX);
				} else {
					mre_dummy = new Mre(mirna, sequenceModel, sequence_start, sequence_end, MREtype.OFFSET_SIX);
				}
			} else { // this could only be 7 matches // and A1?
				mre_dummy = new Mre(mirna, sequenceModel, sequence_start, sequence_end, hasA1 ? MREtype.EIGHT_A1
						: MREtype.SEVEN_M8);
			}
			return mre_dummy;
		}

		return null;
	}
}
