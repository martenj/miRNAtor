/**
 * 
 */
package de.charite.compbio.mirnator.cmd.download;

import org.apache.commons.cli.ParseException;

import de.charite.compbio.jannovar.cmd.CommandLineParsingException;
import de.charite.compbio.jannovar.cmd.HelpRequestedException;
import de.charite.compbio.mirnator.MirnatorOptions;
import de.charite.compbio.mirnator.cmd.MirnatorCommand;
import de.charite.compbio.mirnator.exceptions.MirnatorException;

/**
 * 
 *
 * @author Marten Jäger <marten.jaeger@charite.de>
 *
 */
public class DownloadCommand extends MirnatorCommand {

	public DownloadCommand(String[] options) throws HelpRequestedException, CommandLineParsingException {
		super(options);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.charite.compbio.mirnator.cmd.MirnatorCommand#parseCommandLine(java.lang.String[])
	 */
	@Override
	protected MirnatorOptions parseCommandLine(String[] options) throws HelpRequestedException,
			CommandLineParsingException {
		try {
			return new DownloadCommandlineParser().parse(options);
		} catch (ParseException e) {
			throw new CommandLineParsingException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.charite.compbio.mirnator.cmd.MirnatorCommand#run()
	 */
	@Override
	public void run() throws MirnatorException {
		System.err.println("Options\n" + options);
		// FileDownloader fd = new FileDownloader(new FileDownloader.Options());
	}
}
