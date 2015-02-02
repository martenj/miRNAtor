/**
 * 
 */
package de.charite.compbio.mirnator.cmd.find;

import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.Parser;

/**
 * 
 *
 * @author Marten Jäger <marten.jaeger@charite.de>
 *
 */
public class FindCommandlineParser {
	/** options representation for the Apache commons command line parser */
	protected Options options;
	/** the Apache commons command line parser */
	protected Parser parser;

	public FindCommandlineParser() {
		initializeParser();
	}

	private void initializeParser() {
		options = new Options();
		// options.addOption(opt, longOpt, hasArg, description)

		parser = new GnuParser();
	}
}
