/**
 * 
 */
package de.charite.compbio.mirnator.predictor;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

import de.charite.compbio.mirnator.reference.Mirna;
import de.charite.compbio.mirnator.reference.Mre;
import de.charite.compbio.mirnator.reference.SequenceModel;

/**
 * @author mjaeger
 *
 */
public abstract class MREpredictor implements Runnable {

	protected Mirna mirna;
	protected SequenceModel sequenceModel;
	protected BlockingQueue<Mre> mre_collection;

	public MREpredictor(Mirna mir, SequenceModel sequenceModel, BlockingQueue<Mre> mreBeans) {
		this.mirna = mir;
		this.sequenceModel = sequenceModel;
		this.mre_collection = mreBeans;
	}

	/**
	 * Adds a new found MRE to the local list of MREs. If the number of local MREs is equal to the maximum number of
	 * locally stored MREs they are moved to the global MRE set. <br>
	 * TODO until now the local list of MRE will be stored with the old values since new MREs replace the old ones and
	 * the index is stored in <code>this.current_mres</code>
	 * 
	 * @param mre
	 *            - the new MreBean to be inserted
	 */
	protected void addMreToCollection(ArrayList<Mre> mres) {
		this.mre_collection.addAll(mres);
	}

}
