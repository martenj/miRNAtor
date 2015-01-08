/**
 * 
 */
package mirnator.cmd.find;

import mirnator.MirnatorOptions;
import mirnator.cmd.MirnatorCommand;
import mirnator.exception.MirnatorException;

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
