/**
 * 
 */
package mirnator.parser.ensembl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;

import mirnator.structs.gene.SimpleTranscript;

/**
 * @author mjaeger
 *
 */
public class FastaCdsParser extends EnsemblParser {

	private static final int GENE_ID = 0;
	private static final int TRANSCRIPT_ID = 1;
	private static final int BIOTYPE = 2;
	private static final int MIN_HEADER = 3;

	private String transcriptID = null;
	private String geneID = null;
	private String biotype = null;
	private String sequence = "";
	private SimpleTranscript trans;
	private ArrayList<SimpleTranscript> transcripts;

	public FastaCdsParser(File infile) {
		super(infile);
	}

	public FastaCdsParser(String filename) {
		super(filename);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mirnator.parser.ensembl.EnsemblParser#parse()
	 */
	@Override
	public ArrayList<SimpleTranscript> parse() {
		transcripts = new ArrayList<SimpleTranscript>();
		int c = 0;
		boolean readFirstEntry = false;
		try {
			if (this.infile.getName().endsWith(".gz"))
				in = new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(infile))));
			else
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
					if (fields.length < FastaCdsParser.MIN_HEADER) {
						this.logger.warning("Found missformed header: " + line);
						System.exit(0);
						// TODO: modify to skip the fasta entry
					}
					// System.out.println(c+"\t"+biotype);
					transcriptID = fields[FastaCdsParser.TRANSCRIPT_ID];
					geneID = fields[FastaCdsParser.GENE_ID].substring(1);
					biotype = fields[FastaCdsParser.BIOTYPE];
					continue;
				}
				sequence += line;

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

		return transcripts;
	}

	private void addEntry() {
		if (biotype.equals("protein_coding")) {
			trans = new SimpleTranscript();
			trans.setId(transcriptID);
			trans.setGeneID(geneID);
			trans.setSequence(sequence);
			trans.setBiotype(biotype);
			trans.setCdsStart(0);
			trans.setCdsEnd(sequence.length());
			// System.out.println(">"+geneID+"|"+transcriptID);
			// System.out.println(sequence);
			transcripts.add(trans);
		}
		sequence = "";
	}

	// public static void main(String[] args){
	// FastaCdsParser fcp = new FastaCdsParser("/media/data/data/Ensembl/mmu/NCBIM37_cDNA_full.fa");
	// ArrayList<SimpleTranscript> trans = fcp.parse();
	// }
}
