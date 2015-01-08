/**
 * 
 */
package mirnator.mirnapredict;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import mirnator.sql2java.MirnaBean;
import mirnator.structs.gene.SimpleTranscript;
import mirnator.structs.mirna.Mre;
//import de.charite.mirnator.sql2java.KgsequenceBean;
//import de.charite.mirnator.sql2java.KnowngeneBean;
//import de.charite.mirnator.sql2java.MreBean;
//import de.charite.mirnator.sql2java.MreManager;

/**
 * @author mjaeger
 *
 */
public abstract class SimpleMREpredictor implements Runnable {

	protected String sequence;
	protected String species;
	protected String sequenceID;

	protected MirnaBean mirna;
	protected List<SimpleTranscript> sequences;

	protected ArrayList<Mre> mres;
	protected ArrayList<Mre> mres_local;

	protected static int max_mres = 10000;
	protected int current_mres = 0;

	// used to generate MRE
	protected int mre_id;
	protected int mirna_ref;
	protected int exon_start;
	protected int exon_end;
	protected int mre_type_ref;
	protected double mre_score;
	protected int source;
	protected int mirna_start;
	protected int mirna_end;
	protected String chromosom;
	protected boolean strand;
	protected int chr_start;
	protected int chr_end;

	// TODO remove me
	// protected int[] noOFmres;
	// protected int localNoMres = 0;
	// protected BlockingQueue<MreBean> mreQueue;

	private int i;
	private BlockingQueue<Mre> mreBeans;

	public SimpleMREpredictor(MirnaBean mir, ArrayList<SimpleTranscript> sequences, BlockingQueue<Mre> mreBeans) {
		// this.useExons = true;
		this.mirna = mir;
		// this.mres = mres;
		// this.knowngenes = knowngenes;
		this.sequences = sequences;
		// this.mreBeans = mreBeans;
		this.mres_local = new ArrayList<Mre>(SimpleMREpredictor.max_mres);
		// this.mreman = MreManager.getInstance();
	}

	/**
	 * Adds the predicted MREs from local list to global the global list received in the constructor. TODO: check if I
	 * should better use max
	 */
	protected synchronized void addMREsToGlobalList() {

		// add to global list

		if (this.current_mres == MREpredictor.max_mres)
			this.mres.addAll(this.mres_local);
		else
			this.mres.addAll(this.mres_local.subList(0, this.current_mres));

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
	protected void addMREtoLocalList(Mre mre) {
		// this.addMREtoLocalList(mre,false);
		this.mreBeans.add(mre);
	}

	/**
	 * Adds a new found MRE to the local list of MREs. If the number of local MREs is equal to the maximum number of
	 * locally stored MREs they are moved to the global MRE set. <br>
	 * If the forceGlobal is true the mre is added to the local List and the local list is copied to the global
	 * independent of the current_mre count.<br>
	 * TODO until now the local list of MRE will be stored with the old values since new MREs replace the old ones and
	 * the index is stored in <code>this.current_mres</code>
	 * 
	 * @param mre
	 *            - the new MreBean to be inserted
	 * @param forceGlobal
	 *            - should the localMREs be NOW added to the global List?
	 */
	protected void addMREtoLocalList(Mre mre, boolean forceGlobal) {
		this.mres_local.add(this.current_mres++, mre);
		if (this.current_mres == SimpleMREpredictor.max_mres | forceGlobal) {
			addMREsToGlobalList();
			this.current_mres = 0;
		}
	}

}
