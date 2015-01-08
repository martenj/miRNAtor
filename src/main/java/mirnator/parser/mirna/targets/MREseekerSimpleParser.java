/**
 * 
 */
package mirnator.parser.mirna.targets;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import mirnator.sql2java.MreBean;
import mirnator.sql2java.MreManager;

/**
 * This is the parser used for the output of the current MREseeker.
 * 
 * It takes two files in fastA format<br>
 * 1. the (transcript) sequences<br>
 * e.g.<br>
 * >ENSMUST00000000145|ENSMUSG00000020333... <br>
 * AGAACGTTGCGGGGCGGGCGGCCCAGCCCCTCCCCCAGTCGGGCTCGGCAGTTCGGATGC<br>
 * CGCTAGATTGCTCTCTCACTTCTGGAGAAGATGCAGACCCAGGAGATCCTGAGGATCCTG<br>
 * ...<br>
 * (only the first identifier will be used for the output file)<br>
 * <br>
 * 2. the miRNA sequences<br>
 * e.g. <br>
 * >mmu-miR-29b-1-5p MIMAT0004523 Mus musculus miR-29b-1-5p<br>
 * GCUGGUUUCAUAUGGUGGUUUA<br>
 * 
 * and a output file from the MREseeker<br>
 * e.g.<br>
 * mmu-miR-195a-5p,ENSMUST00000000145,968,974,3,1,7
 * 
 * the columns:<br>
 * 1. mirnaID<br>
 * 2. sequenceID<br>
 * 3. sequenceStart<br>
 * 4. sequenceEnd<br>
 * 5. mreType<br>
 * 6. mirnaStart<br>
 * 7. mirnaEnd<br>
 * 
 * @author Marten Jäger
 * @version 0.1 (2014-10-15)
 */
public class MREseekerSimpleParser implements Iterator {

	private final static int MIRNAID = 0;
	private final static int SEQUENCEID = 1;
	private final static int SEQUENCESTART = 2;
	private final static int SEQUENCEEND = 3;
	private final static int MRETYPE = 4;
	private final static int MIRNASTART = 5;
	private final static int MIRNAEND = 6;
	private final static int COLUMNS = 7;

	private File mrefile;
	private BufferedReader reader;
	/** The current line. */
	private String cachedLine;

	String mirnaID;
	String sequenceID;
	int sequenceStart;
	int sequenceEnd;
	int mirnaStart;
	int mirnaEnd;
	int mreType;

	ArrayList<MreBean> mreBeans = new ArrayList<MreBean>();
	MreBean templBean;
	MreManager mreMan = MreManager.getInstance();

	/**
	 * Inits the {@link MREseekerSimpleParser}.
	 * 
	 * @param filename
	 *            Path to the input file
	 * @throws FileNotFoundException
	 */
	public MREseekerSimpleParser(String filename) {
		this(new File(filename));
	}

	/**
	 * Inits the {@link MREseekerSimpleParser}.
	 * 
	 * @param filename
	 *            The input file
	 * @throws FileNotFoundException
	 */
	public MREseekerSimpleParser(File file) {
		this.mrefile = file;
		try {
			this.reader = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(2);
		}
	}

	public boolean hasNext() {
		try {
			cachedLine = reader.readLine();
			if (cachedLine != null) {
				return true;
			} else {
				// reader.close();
				close();
				return false;
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Returns the MRE Beans for the next line in the MRE file. If the line is not valid null will be returned.
	 */
	public ArrayList<MreBean> next() {
		String[] fields = cachedLine.split(",");
		if (fields.length < COLUMNS) {
			System.err.println(String
					.format("missing columns in line (%d|%d)): %s", fields.length, COLUMNS, cachedLine));
			return null;
		}
		mirnaID = fields[MIRNAID];
		sequenceID = fields[SEQUENCEID];
		try {
			sequenceStart = Integer.parseInt(fields[SEQUENCESTART]);
			sequenceEnd = Integer.parseInt(fields[SEQUENCEEND]);
			mirnaStart = Integer.parseInt(fields[MIRNASTART]);
			mirnaEnd = Integer.parseInt(fields[MIRNAEND]);
			mreType = Integer.parseInt(fields[MRETYPE]);
		} catch (NumberFormatException e) {
			System.err.println("Failed to parse Integer: ");
			e.printStackTrace();
			return null;
		}

		String[] ids = processMirnaID();
		mreBeans = new ArrayList<MreBean>();
		for (String id : ids) {
			templBean = mreMan.createMreBean();
			templBean.setMirnaIdString(id);
			templBean.setSequenceIdString(sequenceID);
			templBean.setKnowngeneStart(sequenceStart);
			templBean.setKnowngeneEnd(sequenceEnd);
			templBean.setMirnaStart(mirnaStart);
			templBean.setMirnaEnd(mirnaEnd);
			templBean.setMreTypeRef(mreType);
			mreBeans.add(templBean);
		}
		return mreBeans;
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Closes the underlying <code>Reader</code> quietly. This method is useful if you only want to process the first
	 * few lines of a larger file. If you do not close the iterator then the <code>Reader</code> remains open. This
	 * method can safely be called multiple times.
	 * 
	 * @throws IOException
	 */
	public void close() {
		if (reader != null) {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		cachedLine = null;
	}

	/**
	 * Processes the mirnaID. If the ID is a cumulated miRNA name (e.g.
	 * mmu-miR-195a-5p/15b-5p/1907/195b/322-5p/6419/6342/6353/16-5p/497-5p/15a-5p) it will be split and the original
	 * names will be restored.<br>
	 * e.g.<br>
	 * mmu-miR-195a-5p<br>
	 * mmu-miR-15b-5p<br>
	 * mmu-miR-1907<br>
	 * ...
	 * 
	 * @return
	 */
	private String[] processMirnaID() {
		String[] ids = this.mirnaID.split("/");
		if (ids.length > 1) {
			int pos = mirnaID.indexOf("miR") + 4;
			String prefix = pos < 0 ? "" : mirnaID.substring(0, pos);
			for (int i = 1; i < ids.length; i++) {
				ids[i] = prefix + ids[i];
			}
		}

		return ids;
	}

}
