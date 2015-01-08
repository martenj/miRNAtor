package mirnator.structs.genbank;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Instances of this class represent individual genes (single genbank items).
 */
public class GenBankGene {

	private int mRNAstart;
	private int mRNAend;
	private int CDSstart;
	private int CDSend;
	private ArrayList<Exon> exons;

	/** mRNA sequence (shown as DNA) of this gene) */
	private String mRNAseq;
	private String definition = "";
	private String name;
	private String accession = "";
	private String versionGI = "";

	public GenBankGene(String dna, int CDSstart, int CDSend, int mRNAstart, int mRNAend, String definition,
			String name, ArrayList<Exon> exons) {

		this.mRNAseq = dna;
		this.mRNAstart = mRNAstart;
		this.mRNAend = mRNAend;
		this.CDSstart = CDSstart;
		this.CDSend = CDSend;
		this.definition = definition;
		this.exons = exons;
		// extractName();
		this.name = name;
	}

	public GenBankGene(String dna, int CDSstart, int CDSend, int mRNAstart, int mRNAend, String definition,
			String name, ArrayList<Exon> exons, String accession) {

		this.mRNAseq = dna;
		this.mRNAstart = mRNAstart;
		this.mRNAend = mRNAend;
		this.CDSstart = CDSstart;
		this.CDSend = CDSend;
		this.definition = definition;
		this.exons = exons;
		this.name = name;
		this.accession = accession;
	}

	public GenBankGene(String dna, int CDSstart, int CDSend, int mRNAstart, int mRNAend, String definition,
			String name, ArrayList<Exon> exons, String accession, String versionGI) {

		this.mRNAseq = dna;
		this.mRNAstart = mRNAstart;
		this.mRNAend = mRNAend;
		this.CDSstart = CDSstart;
		this.CDSend = CDSend;
		this.definition = definition;
		this.exons = exons;
		this.name = name;
		this.accession = accession;
		this.versionGI = versionGI;
	}

	public String get_mRNA() {
		return mRNAseq;
	}

	// public int get_length() { return (mRNAend-mRNAstart+1); }
	public int get_length() {
		return (mRNAseq.length());
	}

	public int getCDSlength() {
		return (CDSend - CDSstart + 1);
	}

	public int get3UTRlength() {
		return (get_length() - CDSend + 1);
	}

	public int get_number_of_exons() {
		return exons.size();
	}

	public String get_definition() {
		return definition;
	}

	public String getName() {
		return this.name;
	}

	public String getAccession() {
		return this.accession;
	}

	public void setName(String n) {
		if (n != null)
			name = n;
	}

	public int getCDSstart() {
		return this.CDSstart;
	}

	public int getCDSend() {
		return this.CDSend;
	}

	public boolean isCDS(int start, int end) {
		return start >= CDSstart && end <= CDSend; // TODO is hier ein >= bzw <= richtiger?
	}

	public boolean is_3UTR(int start, int end) {
		return start > CDSend;
	}

	public boolean is_5UTR(int start, int end) {
		return end < CDSstart;
	}

	public boolean is_5UTRCDS(int start, int end) {
		return start < CDSstart && end >= CDSstart;
	}

	public boolean is_CDS3UTR(int start, int end) {
		return start <= CDSend && end > CDSend;
	}

	public String getLocation(int start, int end) {
		if (end < CDSstart)
			return "5' UTR";
		if (start > CDSend)
			return "3' UTR";
		if (start < CDSstart)
			return "5'UTR/CDS";
		if (end > CDSend)
			return "CDS/3'UTR";
		int i = getExonNumber(start, end);
		if (i > 0) {
			String s = new String("CDS-Exon " + i);
			return s;
		} else {
			i *= -1;
			String s = new String("CDS-Exon " + i + "/" + (i + 1));
			return s;
		}
		// return "CDS";
	}

	private int getExonNumber(int start, int end) {
		int n = 0;
		System.out.println("start = " + start + "  end = " + end);
		Iterator<Exon> it = this.exons.iterator();
		while (it.hasNext()) {
			Exon e = it.next();
			n++;
			if (start >= e.get_start() && start <= e.get_end()) {
				if (end <= e.get_end())
					return n;
				else
					return -1 * n; /* flag for overlap */
			}

		}
		return 0;

	}

	private void extractName() {
		int i, j, len;

		len = this.definition.length();
		i = this.definition.indexOf("(");
		j = this.definition.indexOf(")");
		if (i < j && j < len)
			this.name = definition.substring(i + 1, j);
		else
			this.name = "?";
	}

	public void printSingleFasta(BufferedWriter buf) throws IOException {
		// System.out.println(this.accession+"\t"+this.versionGI);
		buf.write(">gi|" + this.versionGI.split(":")[1] + "\n");
		int i = 0;
		int j;
		j = (this.mRNAseq.length() < 80) ? this.mRNAseq.length() : 80;

		while (j < this.mRNAseq.length()) {
			buf.write(this.mRNAseq.substring(i, j).toLowerCase() + "\n");
			i += 80;
			j += 80;
		}
		buf.write(this.mRNAseq.substring(i, this.mRNAseq.length()).toLowerCase() + "\n");
	}

}
