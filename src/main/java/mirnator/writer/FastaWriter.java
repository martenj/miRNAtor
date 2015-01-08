/**
 * 
 */
package mirnator.writer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * This writer prints a FastA formated file with the given header and Sequence
 * 
 * @author mjaeger
 *
 */
public class FastaWriter {

	private String header;
	private String sequence;

	public FastaWriter(String header, String sequence) {
		this.header = header;
		this.sequence = sequence;
	}

	/**
	 * Write fastA file to filename with default linewidth = 60.
	 * 
	 * @param filename
	 *            - the name of the output file
	 */
	public void writeToFile(String filename) {
		this.writeToFile(filename, 60);
	}

	/**
	 * Write fastA file to filename with the given linewidth for the sequence.
	 * 
	 * @param filename
	 *            - the name of the output file
	 * @param c
	 *            - the length of the lines before linebreak
	 */
	public void writeToFile(String filename, int c) {

		BufferedWriter out = null;
		int start = 0;
		int end = start + c;
		try {
			out = new BufferedWriter(new FileWriter(filename));
			out.write(">" + this.header + "\n");
			while (end < this.sequence.length()) {
				out.write(this.sequence.substring(start, end) + "\n");
				start = end;
				end = start + c;
			}
			out.write(this.sequence.substring(start) + "\n");
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
