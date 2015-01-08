package mirnator.structs.microarraydata;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * Parse the affymetrix file, which is comma-separated, whereby individual fields are put in quotation marks. Inside a
 * field, there can be a comma! For the MiRnator, we want to generate data with fields such as EnsemblID - data (real
 * number) - GeneSymbol - EntrezGene - GenbankAccession
 */
public class AffyAnnot {

	/** A struct to hold one line of annotation data. */
	public class ProbeSetInfo {
		public String psetID;
		public String repPublicID;
		public String geneSymbol;
		public String ensID;
		public String entrezGID;
		public String refSeqID;

		public ProbeSetInfo(String publicID, String genSym, String ensemblID, String entrezID, String RefSeqID) {

			this.repPublicID = publicID;
			this.geneSymbol = genSym;
			this.ensID = ensemblID;
			this.entrezGID = entrezID;
			this.refSeqID = RefSeqID;
		}

		public String getGeneSymbol() {
			return geneSymbol;
		}

		public String getEnsemblID() {
			return ensID;
		}

		public String getEntrezGID() {
			return entrezGID;
		}

		public String getRefSeqID() {
			return refSeqID;
		}
	}

	HashMap<String, ProbeSetInfo> map;

	protected String affyFilePath;

	/** The field separator */
	public static final char SEP = ',';

	/* The current line of the File. */
	private String line;

	/**
	 * The following variables are to note the indices of the variables we are interested in
	 */
	private int probeset_id = 0;
	private int sequence_type = 4;
	private int representative_public_id = 8;
	private int gene_symbol = 14;
	private int ensembl = 17;
	private int entrez_gene = 18;
	private int refseq_transcript_id = 22;
	private int transriptAssignment = 0;
	private int nfields = 0;

	/** The list of objects for the CURRENT line */
	protected ArrayList<String> list = null;

	public AffyAnnot(String path) {
		affyFilePath = path;
		list = new ArrayList<String>();
		map = new HashMap<String, ProbeSetInfo>();
		parseAffyAnnotations();
	}

	private void parseAffyAnnotations() {

		try {
			BufferedReader in = new BufferedReader(new FileReader(this.affyFilePath));
			/** Skip header again */
			inputHeader(in);

			while ((line = in.readLine()) != null) {
				if (line.startsWith("#"))
					continue;
				parseAnnotationLine();
				if (list.size() < nfields) {
					System.out.println("Error number of fields only " + list.size() + " instead of " + nfields);
					System.exit(1);
				}
				extractFields();
			}
		} catch (IOException ioe) {
			System.err.println("Error opening file " + this.affyFilePath);
			ioe.printStackTrace();
		}

	}

	/*
	 * Get the indices of the columns from the header line. "Probe Set ID","GeneChip Array","Species Scientific Name",
	 * "Annotation Date" ,"Sequence Type","Sequence Source","Transcript ID(Array Design)", "Target Description"
	 * ,"Representative Public ID","Archival UniGene Cluster", "UniGene ID","Genome Version"
	 * ,"Alignments","Gene Title","Gene Symbol","Chromosomal Location", "Unigene Cluster Type"
	 * ,"Ensembl","Entrez Gene","SwissProt","EC","OMIM","RefSeq Protein ID", "RefSeq Transcript ID"
	 * ,"FlyBase","AGI","WormBase","MGI Name","RGD Name","SGD accession number",
	 * "Gene Ontology Biological Process","Gene Ontology Cellular Component", "Gene Ontology Molecular Function"
	 * ,"Pathway","InterPro","Trans Membrane","QTL", "Annotation Description","Annotation Transcript Cluster"
	 * ,"Transcript Assignments", "Annotation Notes"
	 */

	protected void inputHeader(BufferedReader in) throws IOException {
		String L;
		while ((line = in.readLine()) != null) {
			if (!line.startsWith("#"))
				break;
		}
		// System.out.println(line);
		/* Now parse the line with the Column headers */
		parseAnnotationLine();
		this.nfields = list.size();

		for (int i = 0; i < list.size(); ++i) {
			String s = list.get(i);
			// System.out.println(i + ": " + s);
			if (s.equals("Probe Set ID"))
				this.probeset_id = i;
			else if (s.equals("Representative Public ID"))
				this.representative_public_id = i;
			else if (s.equals("Gene Symbol"))
				this.gene_symbol = i;
			else if (s.equals("Ensembl"))
				this.ensembl = i;
			else if (s.equals("Entrez Gene"))
				this.entrez_gene = i;
			else if (s.equals("RefSeq Transcript ID"))
				this.refseq_transcript_id = i;
			else if (s.equals("Transcript Assignments"))
				this.transriptAssignment = i;
		}

	}

	protected void extractFields() {
		// Try to extract a transcript ID from ensembl.

		String REGEX = "ENSMUST\\d+";
		Pattern p = Pattern.compile(REGEX);

		String psetID = list.get(probeset_id);
		String repPublicID = list.get(representative_public_id);
		String genSym = list.get(gene_symbol);
		String ensID = list.get(ensembl);
		String entrezGID = list.get(entrez_gene);
		String refSeqID = list.get(refseq_transcript_id);
		String transAssign = list.get(transriptAssignment);

		if (genSym.contains("///")) {
			// System.out.println(genSym);
			// System.exit(0);
			genSym = genSym.split("///")[0].trim();
		}

		ProbeSetInfo psi = new ProbeSetInfo(repPublicID, genSym, ensID, entrezGID, refSeqID);
		// System.out.println("affyID: "+repPublicID
		// +"\nGeneSym: "+genSym+"\nensemblID: "+ensID+"\nentrezID: "+entrezGID+"\nRefSeqID: "+refSeqID);
		this.map.put(psetID, psi);

		/**
		 * Impossible to get the ''correct'' ENSTRANSCRIPT ID, es gibt bis 5 pro pset-id
		 */
		/*
		 * Matcher m = p.matcher(transAssign); while (m.find()) { System.out.println("\t\t" + m.group()); }
		 */

	}

	int bad = 0;

	/** CHANGE THIS AS NEEDED FOR MiRNator */
	public void outputLine(BufferedWriter out, String affy_id, Double d) throws IOException {
		ProbeSetInfo psi = this.map.get(affy_id);
		if (psi == null) {
			++bad;
			System.err.println("Could not find a match for " + affy_id + " [n=" + bad + "]");
			return;
		}
		out.write(psi.ensID + "\t" + d + "\t" + psi.geneSymbol + "\t" + psi.entrezGID + "\t" + psi.repPublicID + "\n");

	}

	/** Parse the current line and put fields into the List */
	private void parseAnnotationLine() {
		StringBuffer sb = new StringBuffer();
		list.clear(); /* Reset */
		int i = 0;

		if (line.length() == 0) {
			list.add(line);
			return; /* There is an empty line, list with one empty object */
		}

		do {
			sb.setLength(0); /* reset */
			if (i < line.length() && line.charAt(i) == '"')
				i = advQuoted(line, sb, ++i);
			else
				i = advPlain(line, sb, i);
			list.add(sb.toString());
			i++;
		} while (i < line.length());

	}

	/** advQuoted: quoted field; return index of next separator */
	protected int advQuoted(String s, StringBuffer sb, int i) {

		int j;
		int len = s.length();
		for (j = i; j < len; j++) {
			if (s.charAt(j) == '"' && j + 1 < len) {
				if (s.charAt(j + 1) == '"') {
					j++; // skip escape char
				} else if (s.charAt(j + 1) == SEP) { // next delimeter
					j++; // skip end quotes
					break;
				}
			} else if (s.charAt(j) == '"' && j + 1 == len) { // end quotes at
																// end of line
				break; // done
			}
			sb.append(s.charAt(j)); // regular character.
		}
		return j;
	}

	/** advPlain: unquoted field; return index of next separator */
	protected int advPlain(String s, StringBuffer sb, int i) {
		int j;

		j = s.indexOf(SEP, i); // look for separator
		if (j == -1) { // none found
			sb.append(s.substring(i));
			return s.length();
		} else {
			sb.append(s.substring(i, j));
			return j;
		}
	}

	/**
	 * Returns a map from Affymetrix_IDs to ProbesetInfo (GeneSymbol, EnsemblID, etc.)
	 * 
	 * @return the map
	 */
	public HashMap<String, ProbeSetInfo> getMap() {
		return map;
	}

}
