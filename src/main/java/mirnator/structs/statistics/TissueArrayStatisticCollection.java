/**
 * 
 */
package mirnator.structs.statistics;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This struct stores all arrays with their statistics. At the oment it's only the Mann_Whitney p-value.
 * 
 * @author mjaeger
 *
 */
public class TissueArrayStatisticCollection {
	private ArrayList<Double[]> arrays;
	private HashMap<String, Integer> array2index;
	private int array_index;
	private final int max_mirnaID;

	public TissueArrayStatisticCollection(int max_mirnaID) {
		this.arrays = new ArrayList<Double[]>();
		this.array2index = new HashMap<String, Integer>();
		this.array_index = 0;
		this.max_mirnaID = max_mirnaID;

	}

	public void putValue(String arrayID, int mirnaID, double value) {
		// check if this array is already known
		if (!this.array2index.containsKey(arrayID)) {
			array2index.put(arrayID, this.array_index++);
			arrays.add(new Double[this.max_mirnaID + 1]); // indx 0 is always empty -> no enties in the database
		}
		// add value
		this.arrays.get(this.array2index.get(arrayID))[mirnaID] = value;

	}

	public void printToFile(String file_name) {
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new FileWriter(file_name));
			for (String arrayname : this.array2index.keySet()) {
				out.write("," + arrayname);
			}
			out.write("\n");
			for (int i = 1; i < this.max_mirnaID + 1; i++) {
				out.write(i + "");
				for (String arrayname : this.array2index.keySet()) {
					out.write("," + this.arrays.get(this.array2index.get(arrayname))[i]);
				}
				out.write("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null)
					out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}
