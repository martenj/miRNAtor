/**
 * 
 */
package mirnator.parser.ensembl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.logging.Logger;

import mirnator.sql2java.KgsequenceBean;
import mirnator.sql2java.KgsequenceManager;
import mirnator.sql2java.KnowngeneBean;
import mirnator.sql2java.KnowngeneManager;

import org.apache.commons.lang.builder.ToStringStyle;

//import mirnator.structs.gene.SimpleTranscript;

/**
 * This Parser will parse the Ensemblfiles with a full header information. E.g. the following attributes needs to be
 * included: Attributes: Ensembl Gene ID Ensembl Transcript ID cDNA sequences Chromosome Name Strand Exon Chr Start (bp)
 * Exon Chr End (bp) Exon Rank in Transcript Transcript Start (bp) Transcript End (bp) 5‘ UTR Start 5‘ UTR End 3‘ UTR
 * Start 3‘ UTR End Gene Biotype
 * 
 * @author mjaeger
 *
 */
public class EnsemblFastaFullParser {

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

	private static final Logger logger = Logger.getLogger(EnsemblFastaFullParser.class.getSimpleName());

	private String filename;
	private int counter = 0;
	private boolean filterBiotype;
	private HashSet<String> biotypeFilter; // filter the resulting List to these biotypes;

	private String gene_id;
	private String transcript_id;
	private String biotype;
	private String chromosom;
	private boolean strand;
	private int[] exon_start;
	private int[] exon_end;
	private int[] exon_pos;
	private int tx_start;
	private int tx_end;
	private int utr5_start;
	private int utr5_end;
	private int utr3_start;
	private int utr3_end;
	private StringBuffer sequence;

	ArrayList<KnowngeneBean> knowngenes;
	ArrayList<KgsequenceBean> ḱgSequences;
	KnowngeneManager kgMan;
	KgsequenceManager kgsMan;

	KnowngeneBean kgTemplate;
	KgsequenceBean kgsTemplate;

	public EnsemblFastaFullParser(String filename) {
		this(filename, null);
	}

	public EnsemblFastaFullParser(String filename, HashSet<String> bioTypes) {
		this.filename = filename;
		if (bioTypes != null) {
			this.filterBiotype = true;
			this.biotypeFilter = bioTypes;
		}
		this.kgMan = KnowngeneManager.getInstance();
		this.kgsMan = KgsequenceManager.getInstance();
		this.sequence = new StringBuffer();
		// System.out.println("filter: "+this.filterBiotype);
		// for (String string : this.biotypeFilter) {
		// System.out.println(string);
		// }
		// System.exit(0);
	}

	/**
	 * Parses the file specified in the filename.
	 * 
	 * @return
	 */
	public boolean parse() {
		if (this.filename == null)
			return false;
		boolean readFirstEntry = false;
		// boolean skip = false;
		BufferedReader in = null;
		String line = null;
		String[] fields;
		int hc = 0;
		int hvc = 0;
		try {
			in = new BufferedReader(new FileReader(this.filename));
			while ((line = in.readLine()) != null) {
				if (line.startsWith(">")) {
					hc++;
					// System.out.print(++hc);
					// if(skip){
					// this.sequence = "";
					// skip = false;
					// }
					if (readFirstEntry)
						addEntry();
					else
						readFirstEntry = true;

					fields = line.split("\\|");
					if (fields.length < EnsemblFastaFullParser.MIN_HEADER) {
						this.logger.warning("Found missformed header: " + line);
						this.logger.warning("length: " + fields.length + "/" + EnsemblFastaFullParser.MIN_HEADER);
						System.exit(0);
					}

					this.biotype = fields[EnsemblFastaFullParser.BIOTYPE];
					if (this.filterBiotype && !this.biotypeFilter.contains(this.biotype)) {
						// System.out.println(hc+"\t"+this.biotype);
						// System.out.println("Skip line: "+line);
						// if(this.knowngenes != null)
						// System.out.println(hc+" ("+(++hvc)+")\t"+this.knowngenes.size());
						continue;
					}
					// System.out.println(hc+" "+(++hvc));
					this.transcript_id = fields[EnsemblFastaFullParser.TRANSCRIPT_ID];
					this.gene_id = fields[EnsemblFastaFullParser.GENE_ID].substring(1);
					this.chromosom = fields[EnsemblFastaFullParser.CHROMOSOM];
					this.strand = Integer.parseInt(fields[EnsemblFastaFullParser.STRAND]) == 1 ? true : false;
					this.exon_pos = parsePositions(fields[EnsemblFastaFullParser.EXON_POS].split(";"));
					this.exon_start = parsePositions(fields[EnsemblFastaFullParser.EXON_START].split(";"),
							this.exon_pos, true);
					this.exon_end = parsePositions(fields[EnsemblFastaFullParser.EXON_END].split(";"), this.exon_pos,
							false);
					this.tx_start = Integer.parseInt(fields[EnsemblFastaFullParser.TX_START]);
					this.tx_end = Integer.parseInt(fields[EnsemblFastaFullParser.TX_END]);
					this.utr5_start = parsePositions(fields[EnsemblFastaFullParser.UTR5_START].split(";")) == null ? -1
							: min(parsePositions(fields[EnsemblFastaFullParser.UTR5_START].split(";")));
					this.utr5_end = parsePositions(fields[EnsemblFastaFullParser.UTR5_END].split(";")) == null ? -1
							: max(parsePositions(fields[EnsemblFastaFullParser.UTR5_END].split(";")));
					this.utr3_start = parsePositions(fields[EnsemblFastaFullParser.UTR3_START].split(";")) == null ? -1
							: min(parsePositions(fields[EnsemblFastaFullParser.UTR3_START].split(";")));
					this.utr3_end = parsePositions(fields[EnsemblFastaFullParser.UTR3_END].split(";")) == null ? -1
							: max(parsePositions(fields[EnsemblFastaFullParser.UTR3_END].split(";")));
					// this.utr3_start = min(parsePositions(fields[EnsemblFastaFullParser.UTR3_START].split(";")));
					// this.utr3_end = max(parsePositions(fields[EnsemblFastaFullParser.UTR3_END].split(";")));

					continue;
				}
				sequence.append(line);

			}
			addEntry();
			in.close();

		} catch (NumberFormatException e) {
			e.printStackTrace();
			logger.severe(line);
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (in != null)
					in.close();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}

		// System.out.println(this.counter);
		return true;

	}

	/**
	 * Returns the maximum integer in the pos list
	 * 
	 * @param pos
	 *            - List with integers
	 * @return maximum of the list
	 */
	private int max(int[] pos) {
		int res = Integer.MIN_VALUE;
		for (int i : pos) {
			if (i > res)
				res = i;
		}
		return res;
	}

	/**
	 * Returns the minumum integer in the pos list
	 * 
	 * @param pos
	 *            - List with integers
	 * @return minimum of the list
	 */
	private int min(int[] pos) {
		int res = Integer.MAX_VALUE;
		for (int i : pos) {
			if (i < res)
				res = i;
		}
		return res;
	}

	/**
	 * Parse the String2int array
	 * 
	 * @param fields
	 *            String arrays with value
	 * @return integer array with the values
	 */
	private int[] parsePositions(String[] fields) {
		// String[] field = str.split(";");
		if (fields.length == 1 && fields[0].equals(""))
			return null;
		int[] pos = new int[fields.length];
		for (int i = 0; i < fields.length; i++)
			pos[i] = Integer.parseInt(fields[i]);
		return pos;
	}

	/**
	 * Parse the String2int array and reordes
	 * 
	 * @param fields
	 *            - String arrays with value
	 * @param idx
	 *            - reorder index
	 * @param adapt
	 *            - correct 1-based index
	 * @return integer array with the values
	 */
	private int[] parsePositions(String[] fields, int[] idx, boolean adapt) {
		int[] pos = new int[fields.length];
		for (int i = 0; i < fields.length; i++) {
			if (adapt)
				pos[idx[i] - 1] = Integer.parseInt(fields[i]) - 1;
			else
				pos[idx[i] - 1] = Integer.parseInt(fields[i]);
		}
		return pos;
	}

	private String intArray2String(int[] pos) {
		String res = "";
		if (pos.length < 1) {
			logger.warning("Get empty position array");
		} else {
			res += pos[0];
			for (int i = 1; i < pos.length; i++)
				res += "," + pos[i];
		}
		return res;

	}

	/**
	 * adds the parsed transcript to the knowngene and knowngenemRNA arrays TODO: fix the filter dingsbums !!!!
	 */
	private void addEntry() {
		if (this.filterBiotype && this.biotypeFilter.contains(this.biotype)) {
			if (this.knowngenes == null)
				this.knowngenes = new ArrayList<KnowngeneBean>();
			if (this.ḱgSequences == null)
				this.ḱgSequences = new ArrayList<KgsequenceBean>();

			this.kgTemplate = this.kgMan.createKnowngeneBean();
			this.kgsTemplate = this.kgsMan.createKgsequenceBean();

			this.kgsTemplate.setSequenceId(this.counter);
			this.kgsTemplate.setKnowngeneRef(this.counter);
			this.kgsTemplate.setMrnaSequence(this.sequence.toString());
			this.ḱgSequences.add(this.kgsTemplate);

			this.kgTemplate.setGeneId(this.counter);
			this.kgTemplate.setUcscId(this.transcript_id);
			this.kgTemplate.setStrand(this.strand);
			this.kgTemplate.setTxStart(this.tx_start);
			this.kgTemplate.setTxEnd(this.tx_end);
			if (this.utr5_end >= 0)
				this.kgTemplate.setCdsStart(this.utr5_end);
			if (this.utr3_start >= 0)
				this.kgTemplate.setCdsEnd(this.utr3_start - 1);
			this.kgTemplate.setExonCount(this.exon_pos.length);
			this.kgTemplate.setExonStarts(intArray2String(this.exon_start));
			this.kgTemplate.setExonEnds(intArray2String(this.exon_end));
			this.kgTemplate.setChromosom(this.chromosom);
			this.knowngenes.add(this.kgTemplate);

			this.counter++;
		}
		this.sequence = new StringBuffer();

		// if(biotype.equals("protein_coding")){
		// trans = new SimpleTranscript();
		// trans.setTranscriptID(transcriptID);
		// trans.setGeneID(geneID);
		// trans.setSequence(sequence);
		// trans.setBiotype(biotype);
		// trans.setCdsStart(0);
		// trans.setCdsEnd(sequence.length());
		// // System.out.println(">"+geneID+"|"+transcriptID);
		// // System.out.println(sequence);
		// transcripts.add(trans);
		// }
		// sequence = "";
	}

	public static void main(String[] args) {
		HashSet<String> bt = new HashSet<String>();
		bt.add("lincRNA");
		bt.add("non_coding");
		EnsemblFastaFullParser effp = new EnsemblFastaFullParser("/media/data/data/Ensembl/mmu/NCBIM37_cDNA_full.fa",
				bt);
		// EnsemblFastaFullParser effp = new
		// EnsemblFastaFullParser("/media/data/data/Ensembl/mmu/NCBIM37_cDNA_full.fa");
		effp.parse();

		for (KgsequenceBean kgBean : effp.getḰgSequences()) {
			System.out.println(kgBean.getSequenceId() + "\t" + kgBean.getMrnaSequence().length());
		}

		System.exit(0);

		System.out.println("KG: " + effp.getKnowngenes().size());
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new FileWriter("/media/data/data/Ensembl/mmu/NCBIM37_cDNA_full.idx"));
			for (KnowngeneBean kgBean : effp.getKnowngenes()) {
				out.write(kgBean.toString(ToStringStyle.SIMPLE_STYLE) + "\n");
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

	/**
	 * @return the knowngenes
	 */
	public ArrayList<KnowngeneBean> getKnowngenes() {
		return knowngenes;
	}

	/**
	 * @return the ḱgSequences
	 */
	public ArrayList<KgsequenceBean> getḰgSequences() {
		return ḱgSequences;
	}

}
