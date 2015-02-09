/**
 * 
 */
package de.charite.compbio.mirnator.predictor;

import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.charite.compbio.mirnator.constants.MREtype;
import de.charite.compbio.mirnator.reference.Mirna;
import de.charite.compbio.mirnator.reference.Mre;
import de.charite.compbio.mirnator.reference.Sequence;

//import org.apache.commons.lang.text.StrBuilder;

/**
 * The BartelMREpredictor searches for miRNA binding elements (MRE). All end-positions (miRNA_end, exon_end, chr_end) in
 * the MREs are exclusive and all indizes are '0'-based.
 * 
 * @author mjaeger
 * 
 */
public class BartelMREpredictor extends MREpredictor {

	private Logger log;

	private char T;
	private char M;
	int seqlength = -1;

	private boolean a1 = false;
	private boolean mm1 = false;
	private int mismatch;
	private int match;
	private int start;
	private int end;
	private int i_subseq;
	private int i_check;

	public BartelMREpredictor(Mirna mir, Sequence sequence, BlockingQueue<Mre> mreBeans) {
		super(mir, sequence, mreBeans);
	}

	@Override
	public void run() {
		for (i_subseq = 0; i_subseq < this.sequence.getSequence().length() - 7; i_subseq++) {
			checkSubsequence(this.sequence.getSequence().subSequence(i_subseq, i_subseq + 8), i_subseq);
		}
	}

	/**
	 * Search the offered subsequence for one of the Bartel MRE sites matche.
	 * 
	 * @param subSequence
	 *            - subsequence of the transcript
	 * @param i
	 *            - position in the transcript
	 */
	private void checkSubsequence(CharSequence subSequence, int j) {
		seqlength = subSequence.length();
		a1 = false;
		mm1 = false;
		mismatch = 0;
		match = 0;
		start = 0;
		end = 0;
		// StringBuilder hits = new StringBuilder("        ");
		for (i_check = 0; i_check < seqlength; i_check++) {
			if (mismatch > 1)
				break;
			T = subSequence.charAt(i_check); // char of transcript
			M = this.mirna.getSequence().charAt(seqlength - 1 - i_check); // char of miRNA
			if ((T == 'A' || T == 'a') && (M == 'T' || M == 'U')) {
				match++;
				end++;
			} else if (M == 'A' && (T == 'T' || T == 'U' || T == 't' || T == 'u')) {
				match++;
				end++;
			} else if ((T == 'C' || T == 'c') && M == 'G') {
				match++;
				end++;
			} else if ((T == 'G' || T == 'g') && M == 'C') {
				match++;
				end++;
			} else {

				if (i_check == 7 && (T == 'A' || T == 'a')) {
					a1 = true;
					// hits.setCharAt(i, ':');
					match++;
				} else {
					mismatch++;
					// hits.setCharAt(i, ' ');
				}
				if (i_check == 0) {
					start++;
					mm1 = true;
					end++;
				}
				// if(i == 0){start++;}
				else {
					end = i_check;
					break;
				}
			}
		}

		// now check: are there at least 6matches and are these contiguous?
		if (match > 5) {
			int mirna_start;
			int mirna_end;
			int sequence_start;
			int sequence_end;

			if ((end - start) == match) {
				mirna_start = 8 - end;
				mirna_end = 8 - start;
				sequence_start = j + start;
				sequence_end = j + end;

				if (match == 6) {
					if (start == 1) {
						// log.log(Level.INFO,
						// this.transcriptID+":"+this.mirna.getMirName()+" - 6mer site ("+(j+start)+"|"+(j+end)+");");
						addMreToCollection(new Mre(this.mirna, this.sequence, sequence_start, sequence_end,
								mirna_start, mirna_end, MREtype.SIX));
					} else {
						// log.log(Level.INFO,
						// this.transcriptID+":"+this.mirna.getMirName()+" - 6mer offset site ("+(j+start)+"|"+(j+end)+");");
						addMreToCollection(new Mre(this.mirna, this.sequence, sequence_start, sequence_end,
								mirna_start, mirna_end, MREtype.OFFSET_SIX));
					}
				} else if (match == 7) {
					if (mm1) {
						// log.log(Level.INFO,
						// this.transcriptID+":"+this.mirna.getMirName()+" - 7mer m1 site ("+(j+start)+"|"+(j+end)+");");
						addMreToCollection(new Mre(this.mirna, this.sequence, sequence_start, sequence_end,
								mirna_start, mirna_end, MREtype.SEVEN_M1));
					} else {
						// log.log(Level.INFO,
						// this.transcriptID+":"+this.mirna.getMirName()+" - 7mer m8 site ("+(j+start)+"|"+(j+end)+");");
						addMreToCollection(new Mre(this.mirna, this.sequence, sequence_start, sequence_end,
								mirna_start, mirna_end, MREtype.SEVEN_M8));
					}
				} else {
					// log.log(Level.INFO,
					// this.transcriptID+":"+this.mirna.getMirName()+" - 8mer site ("+(j+start)+"|"+(j+end)+");");
					addMreToCollection(new Mre(this.mirna, this.sequence, sequence_start, sequence_end, mirna_start,
							mirna_end, MREtype.EIGHT));
				}
			} else if (a1) {
				mirna_start = 8 - end - 1;
				mirna_end = 8 - start;
				sequence_start = j + start;
				sequence_end = j + end + 1;

				if (start == 0) {
					// log.log(Level.INFO,
					// this.transcriptID+":"+this.mirna.getMirName()+" - 8mer A1 site ("+(j+start)+"|"+(j+end)+");");
					addMreToCollection(new Mre(this.mirna, this.sequence, sequence_start, sequence_end, mirna_start,
							mirna_end, MREtype.EIGHT_A1));
				} else {
					// log.log(Level.INFO,
					// this.transcriptID+":"+this.mirna.getMirName()+" - 7mer A1 site ("+(j+start)+"|"+(j+end)+");");
					addMreToCollection(new Mre(this.mirna, this.sequence, sequence_start, sequence_end, mirna_start,
							mirna_end, MREtype.SEVEN_A1));
				}

			} else {
				log.log(Level.INFO, "so what? I should never end here, it's reserved for inseed mismatches!\nstart: "
						+ start + " end: " + end + " match: " + match + "\n" + subSequence + "\n"
						+ (new StringBuilder(this.mirna.getSequence().substring(0, 8)).reverse()));
			}

		}
	}

}
