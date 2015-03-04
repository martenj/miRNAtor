/**
 * 
 */
package de.charite.compbio.mirnator.io.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import de.charite.compbio.mirnator.reference.SequenceModel;
import de.charite.compbio.mirnator.util.IOUtil;

/**
 * 
 *
 * @author Marten Jäger <marten.jaeger@charite.de>
 *
 */
public abstract class FastaParser implements Iterable<SequenceModel> {

	protected File file;
	protected String accession;
	protected StringBuilder sequence;

	private BufferedReader in = null;
	private String str;
	protected SequenceModel model;

	/**
	 * hide this constructor
	 */
	private FastaParser() {
	}

	/**
	 * Simple constructor from filename/path
	 * 
	 * @param filename
	 *            path to the fastA file
	 * @throws IOException
	 */
	private FastaParser(String filename) throws IOException {
		this(new File(filename));
	}

	/**
	 * Simple constructor from {@link File} object.
	 * 
	 * @param file
	 *            {@link File} object with fastA data
	 * @throws IOException
	 */
	public FastaParser(File file) throws IOException {
		this.file = file;
		in = IOUtil.getBufferedReaderFromFileName(file);
		sequence = new StringBuilder();
	}

	public Iterator<SequenceModel> iterator() {
		return new Iterator<SequenceModel>() {

			@Override
			public boolean hasNext() {
				// reset input
				sequence.setLength(0);
				try {

					while ((str = in.readLine()) != null) {
						if (str.startsWith(">")) {
							if (sequence.length() > 0) {
								buildSequenceModel();
								accession = processHeader(str);
								return true;
							}
							accession = processHeader(str);
							// return true;
						} else {
							sequence.append(str);
						}
					}

					if (sequence.length() > 0) {
						buildSequenceModel();
						return true;
					}
				} catch (IOException e) {
					System.err.println("failed to read the FASTA file: " + e);
				}
				return false;
			}

			@Override
			public SequenceModel next() {
				return model;
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	public void close() {
		IOUtil.close(in);
	}

	/**
	 * Builds a new {@link SequenceModel} with the information stored in the dummy variables
	 * 
	 * @return
	 */
	protected abstract void buildSequenceModel();

	/**
	 * Selects the unique identifier from the header line to match the sequence to the {@link TranscriptInfo}
	 * definition.
	 *
	 * @param header
	 *            The FastA header line
	 * @return A unique identifier (e.g. NR_024540.1)
	 */
	protected abstract String processHeader(String header);

	// /**
	// *
	// * @return
	// */
	// public abstract ArrayList<SequenceModel> getModels();
}
