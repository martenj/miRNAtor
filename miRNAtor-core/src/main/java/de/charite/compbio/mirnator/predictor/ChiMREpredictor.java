/**
 * 
 */
package de.charite.compbio.mirnator.predictor;

import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;

import de.charite.compbio.mirnator.constants.Base;
import de.charite.compbio.mirnator.reference.Mirna;
import de.charite.compbio.mirnator.reference.Mre;
import de.charite.compbio.mirnator.reference.SequenceModel;
import de.charite.compbio.mirnator.util.MreTools;

/**
 * @author mjaeger
 * 
 *         TODO: add constructor for simple Sequence --> have a look how to do in the BartelMREpredictor
 * 
 */
public class ChiMREpredictor extends MREpredictor {

	private Logger log;

	private static final int BULG_POS = 4; // corresponds to the 6th base of the miRNA
	private static final int MIRNASEEDLENGTH = 6;
	private static final int MIRNASEEDEND = 7;

	private int i_subseq; // index for the subsequences
	private int i_check; // index for the chars in the subsequences
	// private int seqlength;
	// private int mismatch;
	private int match;
	private int start;
	private int end;
	private int bulg;
	private Base bulgType;

	private char T; // target CHar
	private char M; // miRNA char

	/**
	 * @param mir
	 *            - miRNA family collapsed by the seed sequences
	 * @param sequences
	 *            - list with {@link SequenceModel}s
	 * @param mreBeans
	 */
	public ChiMREpredictor(Mirna mir, SequenceModel sequenceModel, BlockingQueue<Mre> mreBeans) {
		super(mir, sequenceModel, mreBeans);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {

		for (i_subseq = 0; i_subseq < this.sequenceModel.sequence.length() - 8; i_subseq++) {
			if (checkSubsequence(this.sequenceModel.sequence.subSequence(i_subseq, i_subseq + 9), i_subseq))
				i_subseq++;
		}
	}

	/**
	 * Search the the offered subsequence for one of the Chi MRE sites matches. TODO: change to match bulb MRE finding
	 * 
	 * @param subSequence
	 *            - subsequence of the transcript
	 * @param i
	 *            - position in the transcript
	 */
	private boolean checkSubsequence(CharSequence subSequence, int j) {

		// seqlength = subSequence.length();

		match = 0;
		bulg = 0;
		start = 0;
		end = 0;

		for (i_check = 0; i_check < ChiMREpredictor.MIRNASEEDEND + 1; i_check++) {

			if (i_check == ChiMREpredictor.BULG_POS) {
				bulg = 1;
				switch (subSequence.charAt(ChiMREpredictor.MIRNASEEDEND + 1 - i_check)) {
				case 'A':
				case 'a':
					this.bulgType = Base.A;
					break;
				case 'C':
				case 'c':
					this.bulgType = Base.C;
					break;
				case 'G':
				case 'g':
					this.bulgType = Base.G;
					break;
				case 'T':
				case 't':
				case 'U':
				case 'u':
					this.bulgType = Base.U;
					break;
				default:
					this.bulgType = Base.X;
					break;
				}
			}

			T = subSequence.charAt(ChiMREpredictor.MIRNASEEDEND + 1 - i_check - bulg); // char of transcript
			M = this.mirna.getSequence().charAt(i_check); // char of miRNA

			if (MreTools.checkComplementaryMatch(T, M)) {
				match++;
				end++;
			} else {
				if (i_check == 0) {
					start++;
					// mm1 = true;
					end++;
				} else
					break;
			}

			if (end >= ChiMREpredictor.MIRNASEEDEND && match >= ChiMREpredictor.MIRNASEEDLENGTH) {

				int mirna_start = start;
				int mirna_end = end;
				int sequence_start = j + subSequence.length() - end - bulg;
				int sequence_end = j + subSequence.length() - start - bulg + 1;

				// if (this.bulgType == Base.A)
				// addMreToCollection(new Mre(this.mirna, this.sequenceModel, sequence_start, sequence_end,
				// mirna_start, mirna_end, MREtype.BULG_A));
				// if (this.bulgType == Base.C)
				// addMreToCollection(new Mre(this.mirna, this.sequenceModel, sequence_start, sequence_end,
				// mirna_start, mirna_end, MREtype.BULG_C));
				// if (this.bulgType == Base.G)
				// addMreToCollection(new Mre(this.mirna, this.sequenceModel, sequence_start, sequence_end,
				// mirna_start, mirna_end, MREtype.BULG_G));
				// if (this.bulgType == Base.U)
				// addMreToCollection(new Mre(this.mirna, this.sequenceModel, sequence_start, sequence_end,
				// mirna_start, mirna_end, MREtype.BULG_U));
				return true;
			}
			// else{
			// if(match == 6)
			// System.out.println("start: "+start+"\tend: "+end+"\tmatch: "+match);
			// }

		}
		return false;
	}

}
