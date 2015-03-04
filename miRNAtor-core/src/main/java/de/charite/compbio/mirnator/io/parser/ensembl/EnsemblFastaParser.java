/**
 * 
 */
package de.charite.compbio.mirnator.io.parser.ensembl;

import java.io.File;
import java.io.IOException;

import de.charite.compbio.mirnator.io.parser.FastaParser;
import de.charite.compbio.mirnator.reference.SequenceModel;

/**
 * 
 *
 * @author Marten Jäger <marten.jaeger@charite.de>
 *
 */
public class EnsemblFastaParser extends FastaParser {

	/**
	 * @param file
	 * @throws IOException
	 */
	public EnsemblFastaParser(File file) throws IOException {
		super(file);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.charite.compbio.mirnator.io.parser.FastaParser#buildSequenceModel()
	 */
	@Override
	protected void buildSequenceModel() {
		// System.out.println("acc: " + accession);
		// System.out.println("seq: " + sequence);
		// System.out.println();
		model = new SequenceModel(accession, sequence.toString());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.charite.compbio.mirnator.io.parser.FastaParser#processHeader(java.lang.String)
	 */
	@Override
	protected String processHeader(String header) {
		String[] fields;
		if (header.startsWith(">"))
			fields = header.substring(1).split(" ");
		else
			fields = header.split(" ");
		if (fields.length > 0)
			return fields[0];
		else
			return null;
	}

}
