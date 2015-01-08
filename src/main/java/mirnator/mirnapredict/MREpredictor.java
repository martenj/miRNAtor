/**
 * 
 */
package mirnator.mirnapredict;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import mirnator.sql2java.KgsequenceBean;
import mirnator.sql2java.KnowngeneBean;
import mirnator.sql2java.MirnaBean;
import mirnator.sql2java.MreBean;
import mirnator.sql2java.MreManager;

/**
 * @author mjaeger
 *
 */
public abstract class MREpredictor implements Runnable {

	protected String sequence;
	protected String species;
	protected String sequenceID;
	protected boolean useExons = false;

	protected MirnaBean mirna;
	protected List<Sequence> sequences;
	protected List<KnowngeneBean> knowngenes;
	protected List<KgsequenceBean> kgSequences;

	protected ArrayList<MreBean> mres;
	protected ArrayList<MreBean> mres_local;

	// protected MreBean[] mres_local;
	protected MreManager mreman;

	protected static int max_mres = 10000;
	protected int current_mres = 0;

	// used to generate MRE
	protected int mre_id;
	protected int mirna_ref;
	protected int exon1_ref;
	protected int exon2_ref;
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
	private BlockingQueue<MreBean> mreBeans;

	// public MREpredictor(MirnaBean mirna, List<Sequence> sequences, ArrayList<MreBean> mres, String species) {
	// this.mirna = mirna;
	// this.sequences = sequences;
	// this.species = species;
	// this.mres = mres;
	// // this.mres_local = new MreBean[this.max_mres];
	// this.mres_local = new ArrayList<MreBean>(this.max_mres);
	// // System.out.println(this.mres_local.length);
	//
	// // System.exit(0);
	// this.mreman = MreManager.getInstance();
	// }
	//
	//
	// public MREpredictor(MirnaBean mirna, List<ExonBean> sequences, ArrayList<MreBean> mres) {
	// this.useExons = true;
	// this.mirna = mirna;
	// this.mres = mres;
	// this.exons = sequences;
	// this.mres_local = new ArrayList<MreBean>(this.max_mres);
	// this.mreman = MreManager.getInstance();
	// }

	/**
	 * TODO remove me !!!
	 * 
	 * @param kgSequences
	 * @param mirna
	 * @param sequences
	 * @param noOFmres
	 */
	// public MREpredictor(MirnaBean mirna, List<ExonBean> sequences, int[] noOFmres) {
	// this.useExons = true;
	// this.mirna = mirna;
	// this.noOFmres = noOFmres;
	// this.exons = sequences;
	// this.mres_local = new ArrayList<MreBean>(this.max_mres);
	// this.mreman = MreManager.getInstance();
	// }
	// public MREpredictor(MirnaBean mirna, List<ExonBean> sequences, int[] noOFmres, BlockingQueue<MreBean> mreQueue) {
	// this.useExons = true;
	// this.mirna = mirna;
	// this.noOFmres = noOFmres;
	// this.exons = sequences;
	// this.mres_local = new ArrayList<MreBean>(this.max_mres);
	// this.mreman = MreManager.getInstance();
	// this.mreQueue = mreQueue;
	// }

	public MREpredictor(MirnaBean mir, ArrayList<Sequence> sequences, BlockingQueue<MreBean> mreBeans) {
		this.useExons = false;
		this.mirna = mir;
		this.sequences = sequences;
		this.mreBeans = mreBeans;
		this.mres_local = new ArrayList<MreBean>(MREpredictor.max_mres);
		this.mreman = MreManager.getInstance();
	}

	public MREpredictor(MirnaBean mir, ArrayList<KnowngeneBean> knowngenes, ArrayList<KgsequenceBean> ḱgSequences,
			BlockingQueue<MreBean> mreBeans) {
		this.useExons = true;
		this.mirna = mir;
		// this.mres = mres;
		this.knowngenes = knowngenes;
		this.kgSequences = ḱgSequences;
		this.mreBeans = mreBeans;
		this.mres_local = new ArrayList<MreBean>(MREpredictor.max_mres);
		this.mreman = MreManager.getInstance();
	}

	/**
	 * Adds the predicted MREs from local list to global the global list received in the constructor. TODO: check if I
	 * should better use max
	 */
	protected synchronized void addMREsToGlobalList() {

		// send directly to database
		// try {
		// if(this.current_mres == this.max_mres)
		// mreman.insert(this.mres_local);
		// else
		// mreman.insert(this.mres_local.subList(0, this.current_mres));
		// } catch (DAOException e) {
		// System.err.println("failed to upload MRE beans");
		// e.printStackTrace();
		// }

		// add to global list

		if (this.current_mres == MREpredictor.max_mres)
			this.mres.addAll(this.mres_local);
		else
			this.mres.addAll(this.mres_local.subList(0, this.current_mres));

		// this.noOFmres[0] += this.current_mres;
		// this.localNoMres += this.current_mres;
		// if(this.current_mres == this.max_mres)
		// this.mres.addAll(this.mres_local);
		// else
		// this.mres.addAll(this.mres_local.subList(0, this.current_mres));

		// this.mreQueue.addAll(this.mres.subList(0, this.current_mres));
		// this.mreQueue.addAll(null);

		// for(i=0;i<this.current_mres;i++) {
		// this.mres.add(this.mres_local[i]);
		// }
		// System.out.println(this.mres.size());
		// this.mres.addAll(this.mres_local);
	}

	// /**
	// *
	// */
	// protected void addMREtoLocalList(){
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
	protected void addMREtoLocalList(MreBean mre) {
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
	protected void addMREtoLocalList(MreBean mre, boolean forceGlobal) {
		this.mres_local.add(this.current_mres++, mre);
		if (this.current_mres == MREpredictor.max_mres | forceGlobal) {
			addMREsToGlobalList();
			this.current_mres = 0;
		}
	}

	// /**
	// * Predicts possible MREs. Returns <code>TRUE</code> if at least one MRE is found <code>FALSE</code> otherwise.
	// * It will also return <code>FALSE</code> if there is no transcript or mirna.
	// * @return <code>TRUE</code>
	// */
	// abstract boolean predictMRE();

}
