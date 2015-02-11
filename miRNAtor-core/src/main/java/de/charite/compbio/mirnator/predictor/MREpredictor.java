/**
 * 
 */
package de.charite.compbio.mirnator.predictor;

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

	// protected ArrayList<Mre> mres;
	// protected ArrayList<Mre> mres_local;

	// protected static int max_mres = 10000;
	// protected int current_mres = 0;

	private BlockingQueue<Mre> mre_collection;

	public MREpredictor(Mirna mir, SequenceModel sequenceModel, BlockingQueue<Mre> mreBeans) {
		this.mirna = mir;
		this.sequenceModel = sequenceModel;
		this.mre_collection = mreBeans;
		// this.mres_local = new ArrayList<Mre>(MREpredictor.max_mres);
	}

	// /**
	// * Adds the predicted MREs from local list to global the global list received in the constructor. TODO: check if I
	// * should better use max
	// */
	// protected synchronized void addMREsToGlobalList() {
	//
	// // add to global list
	//
	// if (this.current_mres == MREpredictor.max_mres)
	// this.mres.addAll(this.mres_local);
	// else
	// this.mres.addAll(this.mres_local.subList(0, this.current_mres));
	//
	// }

	/**
	 * Adds a new found MRE to the local list of MREs. If the number of local MREs is equal to the maximum number of
	 * locally stored MREs they are moved to the global MRE set. <br>
	 * TODO until now the local list of MRE will be stored with the old values since new MREs replace the old ones and
	 * the index is stored in <code>this.current_mres</code>
	 * 
	 * @param mre
	 *            - the new MreBean to be inserted
	 */
	protected void addMreToCollection(Mre mre) {
		// this.addMREtoLocalList(mre, false);
		this.mre_collection.add(mre);
	}

	// /**
	// * Adds a new found MRE to the local list of MREs. If the number of local MREs is equal to the maximum number of
	// * locally stored MREs they are moved to the global MRE set. <br>
	// * If the forceGlobal is true the mre is added to the local List and the local list is copied to the global
	// * independent of the current_mre count.<br>
	// * TODO until now the local list of MRE will be stored with the old values since new MREs replace the old ones and
	// * the index is stored in <code>this.current_mres</code><br>
	// * TODO check if the else switch is working properly!!!
	// *
	// * @param mre
	// * - the new MreBean to be inserted
	// * @param forceGlobal
	// * - should the localMREs be NOW added to the global List?
	// */
	// protected void addMREtoLocalList(Mre mre, boolean forceGlobal) {
	// this.mres_local.add(this.current_mres++, mre);
	// if (this.current_mres == MREpredictor.max_mres | forceGlobal) {
	// addMREsToGlobalList();
	// this.current_mres = 0;
	// } else {
	// this.mre_collection.add(mre);
	// }
	// }
}
