/**
 * 
 */
package de.charite.compbio.mirnator.exceptions;

/**
 * Error during program run.
 *
 * @author Marten Jäger <marten.jaeger@charite.de>
 *
 */
public class MirnatorException extends Exception {

	public MirnatorException() {
		super();
	}

	public MirnatorException(String e) {
		super(e);
	}
}
