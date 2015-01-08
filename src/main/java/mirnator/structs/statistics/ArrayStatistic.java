/**
 * 
 */
package mirnator.structs.statistics;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This is the container for the statistics for all miRNAs and one array file
 * 
 * @author mjaeger
 *
 */
public class ArrayStatistic {

	String arrayid;

	ArrayList<Double> mw_stat;
	ArrayList<Double> tt_stat;
	ArrayList<Double> rr_stat;

	HashMap<String, Integer> mirnaID2idx;

	public ArrayStatistic() {
		this.mirnaID2idx = new HashMap<String, Integer>();
	}

	public ArrayStatistic(String arrayid) {
		this.arrayid = arrayid;
		this.mirnaID2idx = new HashMap<String, Integer>();
	}
}
