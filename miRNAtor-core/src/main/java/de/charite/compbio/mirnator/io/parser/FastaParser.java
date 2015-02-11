/**
 * 
 */
package de.charite.compbio.mirnator.io.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import de.charite.compbio.mirnator.reference.SequenceModel;
import de.charite.compbio.mirnator.util.IOUtil;

/**
 * 
 *
 * @author Marten Jäger <marten.jaeger@charite.de>
 *
 */
public abstract class FastaParser {

	protected File file;
	protected String accession;
	protected StringBuilder sequence;

	protected ArrayList<SequenceModel> sequenceModels;

	/**
	 * Parse the mRNA sequences and thereby add these to the {@link TranscriptInfo}s.
	 *
	 * @return list of sequence annotated {@link TranscriptInfo}s
	 */
	public ArrayList<SequenceModel> parse() {
		BufferedReader in = null;
		String str;

		try {
			in = IOUtil.getBufferedReaderFromFileName(file);
			while ((str = in.readLine()) != null) {
				if (str.startsWith(">")) {
					if (sequence != null)
						addSequenceModel();
					accession = processHeader(str);
					sequence = new StringBuilder();
				} else {
					sequence.append(str);
				}
			}

			if (sequence != null)
				addSequenceModel();
		} catch (IOException e) {
			// LOGGER.warn("failed to read the FASTA file: {}", e);
		} finally {
			IOUtil.close(in);
		}
		return sequenceModels;
	}

	/**
	 * Adds the Sequence to
	 */
	protected abstract void addSequenceModel();

	/**
	 * Selects the unique identifier from the header line to match the sequence to the {@link TranscriptInfo}
	 * definition.
	 *
	 * @param header
	 *            The FastA header line
	 * @return A unique identifier (e.g. NR_024540.1)
	 */
	protected abstract String processHeader(String header);

	/**
	 * 
	 * @return
	 */
	public abstract ArrayList<SequenceModel> getModels();
}
