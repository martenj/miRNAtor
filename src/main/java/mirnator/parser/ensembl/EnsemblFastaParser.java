/**
 * 
 */
package mirnator.parser.ensembl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import mirnator.structs.gene.SimpleTranscript;

/**
 * @author mjaeger
 *
 */
public class EnsemblFastaParser extends EnsemblParser {

	private final static int GENE_ID = 0;
	private final static int TRANSCRIPT_ID = 1;
	private final static int CHROMOSOM = 2;
	private final static int STRAND = 3;
	private final static int EXON_START = 4;
	private final static int EXON_END = 5;
	private final static int EXON_POS = 6;
	private final static int TX_START = 7;
	private final static int TX_END = 8;
	private final static int UTR5_START = 9;
	private final static int UTR5_END = 10;
	private final static int UTR3_START = 11;
	private final static int UTR3_END = 12;
	private final static int BIOTYPE = 13;
	private final static int MIN_HEADER = 14;

	private int counter = 0;

	private String gene_id;
	private String transcript_id;
	private String biotype;
	// private String chromosom;
	// private boolean strand;
	// private int[] exon_start;
	// private int[] exon_end;
	// private int[] exon_pos;
	// private int tx_start;
	// private int tx_end;
	// private int utr5_start;
	// private int utr5_end;
	// private int utr3_start;
	// private int utr3_end;
	private StringBuffer sequence;

	// private ArrayList<KnowngeneBean> knowngenes;
	// private ArrayList<KgsequenceBean> ḱgSequences;
	// private KnowngeneManager kgMan;
	// private KgsequenceManager kgsMan;
	//
	// private KnowngeneBean kgTemplate;
	// private KgsequenceBean kgsTemplate;

	// private SimpleTranscript trans;
	// private ArrayList<SimpleTranscript> transcripts;

	public EnsemblFastaParser(File infile) {
		super(infile);
		// this.kgMan = KnowngeneManager.getInstance();
		// this.kgsMan = KgsequenceManager.getInstance();
	}

	public EnsemblFastaParser(String filename) {
		super(filename);
		// this.kgMan = KnowngeneManager.getInstance();
		// this.kgsMan = KgsequenceManager.getInstance();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mirnator.parser.ensembl.EnsemblParser#parse()
	 */
	@Override
	public ArrayList<SimpleTranscript> parse() {
		// knowngenes = new ArrayList<KnowngeneBean>();

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
					if (fields.length < EnsemblFastaParser.MIN_HEADER) {
						this.logger.warning("Found missformed header: " + line);
						System.exit(0);
					}
					this.transcript_id = fields[EnsemblFastaParser.TRANSCRIPT_ID];
					this.gene_id = fields[EnsemblFastaParser.GENE_ID];
					this.biotype = fields[EnsemblFastaParser.BIOTYPE];

					System.out.println(c + "\t" + biotype);
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

		return null;
	}

	private void addEntry() {
		// if(this.knowngenes == null)
		// this.knowngenes = new ArrayList<KnowngeneBean>();
		// if(this.ḱgSequences == null)
		// this.ḱgSequences = new ArrayList<KgsequenceBean>();
		//
		// this.kgTemplate = this.kgMan.createKnowngeneBean();
		// this.kgsTemplate = this.kgsMan.createKgsequenceBean();
		//
		// this.kgsTemplate.setSequenceId(this.counter);
		// this.kgsTemplate.setKnowngeneRef(this.counter);
		// this.kgsTemplate.setMrnaSequence(this.sequence);
		//
		// this.kgTemplate.setGeneId(this.counter);
		// this.kgTemplate.setUcscId(this.transcript_id);
		// this.kgTemplate.setStrand(this.strand);
		// this.kgTemplate.setTxStart(this.tx_start);
		// this.kgTemplate.setTxEnd(this.tx_end);
		// if(this.utr5_end >= 0)
		// this.kgTemplate.setCdsStart(this.utr5_end);
		// if(this.utr3_start >= 0)
		// this.kgTemplate.setCdsEnd(this.utr3_start-1);
		// this.kgTemplate.setExonCount(this.exon_pos.length);
		// this.kgTemplate.setExonStarts(intArray2String(this.exon_start));
		// this.kgTemplate.setExonEnds(intArray2String(this.exon_end));
		// this.kgTemplate.setChromosom(this.chromosom);
		sequence = new StringBuffer();
		this.counter++;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		EnsemblFastaParser fcp = new EnsemblFastaParser("/media/data/data/Ensembl/mmu/NCBIM37_cDNA_full.fa");
		ArrayList<SimpleTranscript> trans = fcp.parse();
	}

}
