/**
 * 
 */
package de.charite.compbio.mirnator.cmd.find;

import de.charite.compbio.mirnator.MirnatorOptions;
import de.charite.compbio.mirnator.cmd.MirnatorCommand;
import de.charite.compbio.mirnator.exceptions.MirnatorException;

/**
 * Will find all mirna binding sites for the given sequences and mirnas.
 *
 * @author Marten Jäger <marten.jaeger@charite.de>
 *
 */
public class FindCommand extends MirnatorCommand {

	public FindCommand(String[] options) {
		super(options);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.charite.compbio.cmd.MirnatorCommand#parseCommandLine(java.lang.String[])
	 */
	@Override
	protected MirnatorOptions parseCommandLine(String[] options) {
		FindCommandlineParser parser = new FindCommandlineParser();
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.charite.compbio.cmd.MirnatorCommand#run()
	 */
	@Override
	public void run() throws MirnatorException {
		// TODO Auto-generated method stub

	}

}
