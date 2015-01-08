/**
 * 
 */
package mirnator.parser.statistics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.logging.Logger;

/**
 * This is the parser for the array statistics files containing one line entries for each mirna and each array.<br>
 * e.g.:<br>
 * <code>
 * ID,ARRAYID,MIRNAID,Mann-Whitney-stat,Ttest-stat,RR-stat,preset
 * 1,GSM19221.soft,1411,0.12688695951113194,3.550285034888062E-5,-1,0
 * 2,GSM19225.soft,1411,0.07612375776533108,1.5893845308747435E-4,-1,0
 * ...
 * </code>
 * 
 * @author mjaeger
 *
 */
public class ArrayStatisticParser {

	private static final Logger logger = Logger.getGlobal();

	private final static int ID = 0;
	private final static int ARRAY_ID = 1;
	private final static int MIRNA_ID = 2;
	private final static int STAT_MW = 3;
	private final static int STAT_TT = 4;
	private final static int STAT_RR = 5;
	private final static int DESIGN = 6;

	private HashSet<String> arrays;
	private HashSet<String> mirnas;

	private String filename;

	// Container cane;
	// JFrame frame;
	// JProgressBar progressBar;

	public ArrayStatisticParser(String filename) {
		this.filename = filename;
	}

	/**
	 * Returns the {@link HashSet} with the arraynames. If the file isn't parsed yet it will be done now.
	 * 
	 * @return the arrays
	 */
	public HashSet<String> getArrays() {
		if (this.arrays == null) {
			this.getIdentifierIDs(this.filename);
		}
		return arrays;
	}

	/**
	 * Returns the {@link HashSet} with the mirna IDs. If the file isn't parsed yet it will be done now.
	 * 
	 * @return the mirna IDs
	 */
	public HashSet<String> getMirnasIDs() {
		if (this.arrays == null) {
			this.getIdentifierIDs(this.filename);
		}
		return mirnas;
	}

	public void parseFile(String array) {

	}

	private void getIdentifierIDs(String filename) {

		this.logger.info(String.format("parsing the file (%s) to get the array and mirna IDs.", filename));
		// ArrayList<String> arrays = new ArrayList<>();
		this.arrays = new HashSet<String>();
		this.mirnas = new HashSet<String>();
		String[] fields;
		long readBytes = 0;
		long fileSize;
		int curPerc = 0;
		int n;

		// frame = new JFrame("Progress");
		// frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		// cane = frame.getContentPane();
		// progressBar = new JProgressBar();
		// progressBar.setValue(0);
		// progressBar.setStringPainted(true);
		// Border border = BorderFactory.createTitledBorder("Reading...");
		// progressBar.setBorder(border);
		// cane.add(progressBar, BorderLayout.NORTH);
		//
		// frame.setSize(300, 70);
		// frame.setVisible(true);
		// frame.setEnabled(true);

		BufferedReader in = null;
		File file;
		try {
			file = new File(filename);
			fileSize = file.length();
			in = new BufferedReader(new FileReader(file));
			String str;
			System.out.println("0%      50%     100%");
			while ((str = in.readLine()) != null) {
				readBytes += str.getBytes().length;
				fields = str.split(",");
				arrays.add(fields[ARRAY_ID]);
				mirnas.add(fields[MIRNA_ID]);
				n = (int) (100.0 * readBytes / fileSize);
				if (n > curPerc) {
					curPerc = n;
					// progressBar.setValue(curPerc);
					if (n % 5 == 0)
						System.out.print("*");
					// System.out.println(String.format("%d %% processed", curPerc));
				}
			}
			System.out.println();

			// frame.setVisible(false);
			// frame.setEnabled(false);
			// System.out.println("Bytes: "+readBytes);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null)
					in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

}
