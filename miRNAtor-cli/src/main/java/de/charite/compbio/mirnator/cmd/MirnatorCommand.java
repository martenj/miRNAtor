/**
 * 
 */
package de.charite.compbio.mirnator.cmd;

import de.charite.compbio.mirnator.MirnatorOptions;
import de.charite.compbio.mirnator.exceptions.MirnatorException;

/**
 * 
 *
 * @author Marten Jäger <marten.jaeger@charite.de>
 *
 */
public abstract class MirnatorCommand {

	/** Configuration to use for the command execution. */
	protected MirnatorOptions options;

	/**
	 * Initializes the {@link MirnatorCommand}.
	 * 
	 * @param options
	 *            cli passed options
	 */
	public MirnatorCommand(String[] options) {
		this.options = parseCommandLine(options);
	}

	/**
	 * Parse the command line.
	 * 
	 * @param args
	 * @return {@link MirnatorOptions}
	 */
	protected abstract MirnatorOptions parseCommandLine(String[] options);

	/**
	 * Function for the execution of the command.
	 *
	 * @throws MirnatorException
	 *             on problems executing the command.
	 */
	public abstract void run() throws MirnatorException;

}
