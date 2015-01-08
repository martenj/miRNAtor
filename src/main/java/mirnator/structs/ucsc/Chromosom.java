/**
 * 
 */
package mirnator.structs.ucsc;

import java.util.ArrayList;
import java.util.logging.Logger;

import mirnator.sql2java.KnowngeneBean;

/**
 * @author mjaeger
 *
 */
public class Chromosom {

	private static final Logger logger = Logger.getLogger(Chromosom.class.getSimpleName());

	private ArrayList<StringBuffer> dna;

	private int subStringLength = 10000;
	private int curridx = 0;

	// private int sequenceLength = 0;
	// private int sequenceLengthTMP = 0;

	public Chromosom() {
		this.dna = new ArrayList<StringBuffer>();
		this.dna.add(this.curridx, new StringBuffer(this.subStringLength));
	}

	/**
	 * Appends the sequence to the end of the chromosomal dna.<br>
	 * If the current subStringBuffer is to short the remaining characters are added to a new substringbuffer and the
	 * curridx is incremented.
	 * 
	 * @param sequence
	 *            - Sequence to be appended
	 */
	public void appendSequence(String sequence) {
		int seqlength = sequence.length(); // length of the to be appended sequence
		int curbufpos = this.dna.get(curridx).length(); // length of the buffered substring,
		// System.out.println("append Sequence : "+seqlength);
		// System.out.println("currbuffposition: "+curbufpos);
		// System.out.println("SUBSTR-seqlength: "+(subStringLength - seqlength));
		// System.out.println("current index   : "+curridx);
		// this.sequenceLength += seqlength;

		if ((subStringLength - seqlength) > curbufpos) {
			this.dna.get(curridx).append(sequence);
		} else {
			// this.sequenceLengthTMP += this.subStringLength;
			int balance = seqlength - subStringLength + curbufpos;
			this.dna.get(curridx).append(sequence.substring(0, seqlength - balance));
			this.dna.add(++this.curridx, new StringBuffer(this.subStringLength));
			this.dna.get(curridx).append(sequence.substring(seqlength - balance));
			// System.out.println("balance : " + balance);
			// System.out.println("append old: 0-" + (seqlength - balance) + "\t"+ sequence.substring(0, seqlength -
			// balance));
			// System.out.println("append new: "+(seqlength-balance)+"-"+seqlength+"\t"+sequence.substring(seqlength-balance));
			// System.out.println("sequence  : "+sequence);
		}
		// System.out.println();

	}

	/**
	 * Returns the chromosomal sequence from start to end. First <code>start &lt; end</code> is checked, then range of
	 * the requested substring (e.g. <code>end &lt;</code> chromosom length)
	 * 
	 * @param start
	 *            - the beginning index, inclusive
	 * @param end
	 *            - the ending index, exclusive
	 * @return the chromosomal sequence from start to end or null if StringIndexOutOfBounds
	 */
	public String getSequence(int start, int end) {

		// 1. check if the start position is smaller than the end position
		if (start > end) {
			return null;
		}
		// 2. check if the start and end positions are in valid area
		// System.out.println("curr idx: "+this.curridx);
		// System.out.println("voll: "+(this.curridx)*this.subStringLength);
		// System.out.println("rest: "+this.dna.get(curridx).length());
		// System.out.println("gesmatLänge: "+((this.curridx)*this.subStringLength+this.dna.get(curridx).length()));
		if (end > ((this.curridx) * this.subStringLength + this.dna.get(curridx).length())) {
			return null;
		}

		int startidx = start / this.subStringLength; // the index in the
		int endidx = end / this.subStringLength;

		int startsub = start % this.subStringLength;
		int endsub = end % this.subStringLength;

		// System.out.println("curridx: "+curridx);
		// System.out.println("startidx: "+startidx);
		// System.out.println("endidx: "+endidx);
		// System.out.println("startsub: "+startsub);
		// System.out.println("endsub: "+endsub);

		String subSequence = "";

		if (startidx == endidx)
			subSequence += this.dna.get(startidx).substring(startsub, endsub);
		else {
			subSequence += this.dna.get(startidx).substring(startsub);
			for (int i = startidx + 1; i < endidx; i++) {
				subSequence += this.dna.get(i);
			}
			subSequence += this.dna.get(endidx).substring(0, endsub);
		}

		return subSequence;

	}

	/**
	 * Returns the mRNA sequence from <code>start</code> to <code>end</code>. First <code>start &lt; end</code> is
	 * checked, then range of the requested substring (e.g. <code>end &lt;</code> chromosom length) <br>
	 * The chromosomal coordinates are <code>zero</code>-based!!!
	 * 
	 * @param start
	 *            - the beginning index, inclusive
	 * @param end
	 *            - the ending index, exclusive
	 * @return the mRNA sequence (lower letters) from start to end or <code>null</code> if
	 *         {@link StringIndexOutOfBoundsException}
	 */
	public String getMrnaSequence(int start, int end) {
		return this.getMrnaSequence(start, end, true);
	}

	/**
	 * Returns the mRNA sequence from <code>start</code> to <code>end</code>. First <code>start &lt; end</code> is
	 * checked, then range of the requested substring (e.g. <code>end &lt;</code> chromosom length)<br>
	 * The chromosomal coordinates are <code>zero</code>-based!!!
	 * 
	 * @param start
	 *            - the beginning index, inclusive
	 * @param end
	 *            - the ending index, exclusive
	 * @param strand
	 *            - is the sequence on the complementary strand needed?
	 * @return the mRNA sequence (lower letters) from start to end or null if StringIndexOutOfBounds
	 */
	public String getMrnaSequence(int start, int end, boolean strand) {
		String mrna;
		if ((mrna = this.getSequence(start, end)) != null)
			mrna = mrna.toLowerCase();
		else {
			logger.severe("The start and end coordinates are invalid.\nstart: " + start + "\nend: " + end
					+ "\nstrand: " + strand + "\n");
			return mrna;
		}
		if (strand) { // System.out.println("+ strand");
			return mrna;
		} else {
			// System.out.println("- strand");
			StringBuilder revmRNA = new StringBuilder(mrna).reverse();

			for (int i = 0; i < revmRNA.length(); i++) {
				switch (revmRNA.charAt(i)) {
				case 'a':
					revmRNA.setCharAt(i, 't');
					break;
				case 'A':
					revmRNA.setCharAt(i, 't');
					break;
				case 'c':
					revmRNA.setCharAt(i, 'g');
					break;
				case 'C':
					revmRNA.setCharAt(i, 'g');
					break;
				case 'g':
					revmRNA.setCharAt(i, 'c');
					break;
				case 'G':
					revmRNA.setCharAt(i, 'c');
					break;
				case 't':
					revmRNA.setCharAt(i, 'a');
					break;
				case 'T':
					revmRNA.setCharAt(i, 'a');
					break;
				}
			}
			return revmRNA.toString();
		}

	}

	public String getMrnaSequence(UCSCknownGene knowngene) {
		return this.getMrnaSequence(knowngene.getExonStarts(), knowngene.getExonEnds(), knowngene.getStrand());
	}

	public String getMrnaSequence(KnowngeneBean knowngene) {
		return this.getMrnaSequence(parseExonPositions(knowngene.getExonStarts()),
				parseExonPositions(knowngene.getExonEnds()), knowngene.getStrand());
	}

	/**
	 * Splits and parses the Exon start/end positions from databasefile.<br>
	 * e.g. 1234,1267,1468
	 * 
	 * @param string
	 * @return positions as integer
	 */
	private int[] parseExonPositions(String positionstring) throws NumberFormatException {
		String[] positions = positionstring.split(",");
		int[] pos = new int[positions.length];
		for (int i = 0; i < positions.length; i++) {
			pos[i] = Integer.parseInt(positions[i]);
		}
		return pos;
	}

	/**
	 * Assembles the mRNA sequence for the given <code>start</code> and <code>end</code> coordinates. First
	 * <code>start &lt; end</code> is checked, then range of the requested substring (e.g. <code>end &lt;</code>
	 * chromosom length) <br>
	 * Returns the mRNA sequence (lower letters) from start to end or null if {@link StringIndexOutOfBoundsException} or
	 * the number of start and end positions is unequal.<br>
	 * The chromosomal coordinates are <code>zero</code>-based!!!
	 * 
	 * @param start
	 *            - the starting indizes, inclusive
	 * @param end
	 *            - the ending indezes, exclusive
	 * @return the mRNA sequence (lower letters) from start to end
	 */
	public String getMrnaSequence(int[] start, int[] end) {
		return this.getMrnaSequence(start, end, true);
	}

	/**
	 * Assembles the mRNA sequence for the given <code>start</code> and <code>end</code> coordinates. First
	 * <code>start &lt; end</code> is checked, then range of the requested substring (e.g. <code>end &lt;</code>
	 * chromosom length) <br>
	 * Returns the mRNA sequence (lower letters) from start to end or null if {@link StringIndexOutOfBoundsException} or
	 * the number of start and end positions is unequal.<br>
	 * The chromosomal coordinates are <code>zero</code>-based!!!
	 * 
	 * @param start
	 *            - the starting indizes, inclusive
	 * @param end
	 *            - the ending indezes, exclusive
	 * @param reverse
	 *            - is the sequence on the complementary strand needed?
	 * @return the mRNA sequence (lower letters) from start to end
	 */
	public String getMrnaSequence(int[] start, int[] end, boolean strand) {
		// first check if #start == #end
		if (start.length != end.length)
			return null;

		String mRNA = "";
		String seqTemp;
		if (strand) {
			// System.out.print("+");
			for (int i = 0; i < start.length; i++) {
				seqTemp = getMrnaSequence(start[i], end[i]);
				if (seqTemp == null)
					return null;
				else
					mRNA += seqTemp;
			}
		} else {
			// System.out.print("-");
			for (int i = start.length - 1; i > -1; i--) {
				seqTemp = getMrnaSequence(start[i], end[i], strand);
				if (seqTemp == null)
					return null;
				else
					mRNA += seqTemp;
			}
		}

		return mRNA;
	}

	// /**
	// * @return the sequenceLength
	// */
	// public int getSequenceLength() {
	// return sequenceLength;
	// }
	//
	//
	// public int getSequenceLengthTMP() {
	// this.sequenceLengthTMP += this.dna.get(curridx).length();
	// return sequenceLengthTMP;
	// }

}
