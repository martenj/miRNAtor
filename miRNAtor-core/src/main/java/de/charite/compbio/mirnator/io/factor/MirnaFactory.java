/**
 * 
 */
package de.charite.compbio.mirnator.io.factor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.charite.compbio.mirnator.exceptions.MisformedMirbaseHeaderException;
import de.charite.compbio.mirnator.reference.Mirna;

/**
 * Factory for {@link Mirna}s.
 *
 * @author Marten Jäger <marten.jaeger@charite.de>
 *
 */
public class MirnaFactory {

	private final static String mirbaseHeaderRegex = ">([a-z0-9]{3,5})-([miR|let|lin|bantam]-*[0-9a-zA-Z.\\-\\/\\**]+) (MIMAT[0-9]{7})[\\w\\p{Punct} ]*";
	private final static Pattern mirbasePattern = Pattern.compile(MirnaFactory.mirbaseHeaderRegex);

	private String name;
	private String species;
	private String accession;

	// private static MirnaFactory instance;
	//
	// private MirnaFactory() {
	// }

	// /**
	// * Returns the {@link MirnaFactory} instance. If not already
	// * @return
	// */
	// public MirnaFactory getInstance() {
	// if (instance == null)
	// instance = new MirnaFactory();
	// return instance;
	// }

	/**
	 * Creates a new {@link Mirna} object from a given fasta header and sequence.<br>
	 * Will return <code>null</code> if the header or sequence is <code>null</code> or an empty {@link String}.<br>
	 * header like: <code>>cel-let-7-5p MIMAT0000001 Caenorhabditis elegans let-7-5p</code><br>
	 * sequence like: <code>UGAGGUAGUAGGUUGUAUAGUU</code>
	 * 
	 * @param fastaHeader
	 *            the fasta header line
	 * @param fastaSequence
	 *            the fasta sequence
	 * @return
	 * @throws MisformedMirbaseHeaderException
	 */
	public Mirna createMirna(String fastaHeader, String fastaSequence) throws MisformedMirbaseHeaderException {
		// check non empty values.
		if (fastaSequence == null || fastaSequence.equals(""))
			return null;
		processMirbaseHeader(fastaHeader);
		return new Mirna(accession, fastaSequence, name, species, null);

	}

	/**
	 * Extracts the header information from a miRBase fasta entry.<br>
	 * e.g. >cel-let-7-5p MIMAT0000001 Caenorhabditis elegans let-7-5p<br>
	 * species: cel<br>
	 * name: let-7-5p<br>
	 * accession: MIMAT0000001
	 * 
	 * The line is checked using a regular expression. If the line does not match a
	 * {@link MisformedMirbaseHeaderException} is thrown.
	 * 
	 * @param header
	 * @throws MisformedMirbaseHeaderException
	 */
	private void processMirbaseHeader(String header) throws MisformedMirbaseHeaderException {

		if (header == null || header.equals(">"))
			throw new MisformedMirbaseHeaderException("empty miRNA FastA header: " + header);
		Pattern pat = Pattern.compile(MirnaFactory.mirbaseHeaderRegex);
		Matcher mat = pat.matcher(header);

		if (mat.find()) {

			this.species = mat.group(1);
			this.name = mat.group(2);
			this.accession = mat.group(3);
		} else {
			throw new MisformedMirbaseHeaderException("misformed miRNA FastA header: " + header);
		}
	}
}
