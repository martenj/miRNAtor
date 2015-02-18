/**
 * 
 */
package de.charite.compbio.mirnator.predictor;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;

import de.charite.compbio.mirnator.io.factor.SingleBartelMreFactory;
import de.charite.compbio.mirnator.reference.Mirna;
import de.charite.compbio.mirnator.reference.Mre;
import de.charite.compbio.mirnator.reference.SequenceModel;
import de.charite.compbio.mirnator.util.MreTools;

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

	int seqlength = -1;

	private int i_subseq;

	public BartelMREpredictor(Mirna mir, SequenceModel sequenceModel, BlockingQueue<Mre> mreBeans) {
		super(mir, sequenceModel, mreBeans);
	}

	@Override
	public void run() {
		ArrayList<Mre> mres = new ArrayList<Mre>();
		Mre mre;
		for (i_subseq = MreTools.MINSEEDLENGTH; i_subseq < this.sequenceModel.sequence.length(); i_subseq++) {
			mre = new SingleBartelMreFactory(mirna, sequenceModel, i_subseq, true, true).build();
			if (mre != null)
				mres.add(mre);
		}
		mre_collection.addAll(mres);
	}
}
