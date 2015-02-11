/**
 * 
 */
package de.charite.compbio.mirnator.cmd;

import de.charite.compbio.jannovar.cmd.CommandLineParsingException;
import de.charite.compbio.jannovar.cmd.HelpRequestedException;
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
	 * @throws CommandLineParsingException
	 * @throws HelpRequestedException
	 */
	public MirnatorCommand(String[] options) throws HelpRequestedException, CommandLineParsingException {
		this.options = parseCommandLine(options);
	}

	/**
	 * Parse the command line.
	 * 
	 * @param args
	 * @return {@link MirnatorOptions}
	 * @throws HelpRequestedException
	 * @throws CommandLineParsingException
	 */
	protected abstract MirnatorOptions parseCommandLine(String[] options) throws HelpRequestedException,
			CommandLineParsingException;

	/**
	 * Function for the execution of the command.
	 *
	 * @throws MirnatorException
	 *             on problems executing the command.
	 */
	public abstract void run() throws MirnatorException;

}
