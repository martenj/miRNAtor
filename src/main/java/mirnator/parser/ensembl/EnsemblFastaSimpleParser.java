/**
 * 
 */
package mirnator.parser.ensembl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import mirnator.mirnapredict.Sequence;
import mirnator.structs.gene.SimpleTranscript;

/**
 * This is a parser for the simplest form of Biomart fasta results. e.g e.g.<br>
 * >ENSMUST00000000145|ENSMUSG00000020333... <br>
 * AGAACGTTGCGGGGCGGGCGGCCCAGCCCCTCCCCCAGTCGGGCTCGGCAGTTCGGATGC<br>
 * CGCTAGATTGCTCTCTCACTTCTGGAGAAGATGCAGACCCAGGAGATCCTGAGGATCCTG<br>
 * ...<br>
 * (only the first identifier will be used for the {@link SimpleTranscript} identifier and should therefore be unique)<br>
 * 
 * @author mjaeger
 *
 */
public class EnsemblFastaSimpleParser extends EnsemblParser {

	private final static int TRANSCRIPT_ID = 0;
	private final static int MIN_HEADER = 1;

	private int counter = 0;

	private String transcript_id;
	private StringBuffer sequence;
	private SimpleTranscript transcriptTempl;

	/**
	 * @param filename
	 */
	public EnsemblFastaSimpleParser(String filename) {
		super(filename);
	}

	/**
	 * @param infile
	 */
	public EnsemblFastaSimpleParser(File infile) {
		super(infile);
	}

	public ArrayList<Sequence> parseSequence() {
		this.parse();
		ArrayList<Sequence> sequences = new ArrayList<Sequence>();
		for (SimpleTranscript transcript : this.simpleTranscripts) {
			// System.out.println(transcript.getId());
			sequences.add(transcript);
		}
		return sequences;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mirnator.parser.ensembl.EnsemblParser#parse()
	 */
	@Override
	public ArrayList<SimpleTranscript> parse() {
		this.simpleTranscripts = new ArrayList<SimpleTranscript>();
		sequence = new StringBuffer();
		int c = 0;
		boolean readFirstEntry = false;
		try {
			in = new BufferedReader(new FileReader(this.infile));
			while ((line = in.readLine()) != null) {
				// process header
				if (line.startsWith(">")) {
					c++;
					if (readFirstEntry)
						addEntry();
					else
						readFirstEntry = true;

					fields = line.split("\\|");
					if (fields.length < EnsemblFastaSimpleParser.MIN_HEADER) {
						this.logger.warning("Found missformed header: " + line);
						System.exit(0);
					}
					this.transcript_id = fields[EnsemblFastaSimpleParser.TRANSCRIPT_ID];

					continue;
				}
				sequence.append(line);

			}
			addEntry();
			in.close();

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

		return this.simpleTranscripts;
	}

	/**
	 * adds the currened parsed transcript to List.
	 */
	private void addEntry() {
		transcriptTempl = new SimpleTranscript();
		// System.out.println(this.transcript_id);
		transcriptTempl.setId(this.transcript_id.substring(1));
		// System.out.println(transcriptTempl.getTranscriptID());
		transcriptTempl.setSequence(this.sequence.toString());
		this.simpleTranscripts.add(transcriptTempl);
		this.sequence = new StringBuffer();
		this.transcript_id = null;
		this.counter++;
	}

}
