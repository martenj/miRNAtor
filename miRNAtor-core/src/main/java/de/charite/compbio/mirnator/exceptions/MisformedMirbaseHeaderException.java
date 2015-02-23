/**
 * 
 */
package de.charite.compbio.mirnator.exceptions;

/**
 * Exception for misformed miRBase fastA headers. Called when the header differs from this schema:<br>
 * <code>>cel-let-7-5p MIMAT0000001 Caenorhabditis elegans let-7-5p</code>
 *
 * @author Marten Jäger <marten.jaeger@charite.de>
 *
 */
public class MisformedMirbaseHeaderException extends Exception {

	public MisformedMirbaseHeaderException() {
		super();
	}

	public MisformedMirbaseHeaderException(String e) {
		super(e);
	}

}
