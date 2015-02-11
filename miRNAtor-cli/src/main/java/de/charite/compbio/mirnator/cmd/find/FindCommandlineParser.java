/**
 * 
 */
package de.charite.compbio.mirnator.cmd.find;

import java.io.File;
import java.io.PrintWriter;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;

import de.charite.compbio.jannovar.cmd.HelpRequestedException;
import de.charite.compbio.mirnator.MirnatorOptions;

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
		options.addOption("t", "taxon", true, "species taxon (e.g.  mmu, hsa, dre, ...)");
		options.addOption("s", "sequence", true, "path to the sequence file");
		options.addOption("j", "jannovar", true, "path to a jannovar serialized sequence file");
		options.addOption("o", "outfile", true, "path to a output MRE file");
		options.addOption("t", "taxon", true, "species taxon (e.g.  mmu, hsa, dre, ...)");
		options.addOption("c", "threads", true, "threads to be used in parallel (default to number of online CPUs -1)");
		options.addOption("h", "help", false, "show help");

		parser = new GnuParser();
	}

	public MirnatorOptions parse(String[] argv) throws ParseException, HelpRequestedException {
		// Parse the command line.
		CommandLine cmd = parser.parse(options, argv);

		// fill the MiRNAtoroptions
		MirnatorOptions results = new MirnatorOptions();
		results.command = MirnatorOptions.Command.FIND;

		if (cmd.hasOption("help")) {
			printHelp();
			throw new HelpRequestedException();
		}

		if (cmd.hasOption("sequence")) {
			results.sequence_path = new File(cmd.getOptionValue("sequence"));
			if (!results.sequence_path.exists())
				printHelp("Invalid path to noexisting sequence file: " + results.sequence_path.getAbsolutePath());
		}

		if (cmd.hasOption("jannovar")) {
			results.jannovar_path = new File(cmd.getOptionValue("jannovar"));
			if (!results.jannovar_path.exists())
				printHelp("Invalid path to noexisting (jannovar) sequence file: "
						+ results.jannovar_path.getAbsolutePath());
		}

		if (cmd.hasOption("outfile")) {
			results.output_file = cmd.getOptionValue("outfile");
		} else {
			printHelp("Missing output file.\n");
		}

		if (cmd.hasOption("taxon")) {
			results.taxon = cmd.getOptionValue("taxon");
		}
		if (cmd.hasOption("threads")) {
			try {
				results.online_cpus = Integer.parseInt(cmd.getOptionValue("threads"));
			} catch (NumberFormatException e) {
				System.err.println("invalid number of CPUs to be used: " + cmd.getOptionValue("threads"));
				System.err.println("Fallback to online CPUs available.");
				results.online_cpus = Runtime.getRuntime().availableProcessors();
			}
		} else {
			System.err.println("Fallback to online CPUs available.");
			results.online_cpus = Runtime.getRuntime().availableProcessors();
		}

		if (results.jannovar_path == null && results.sequence_path == null) {
			printHelp("Missing input sequence data.");
		}

		return results;
	}

	/**
	 * Prints the help for the MiRNAtor command call.
	 */
	private void printHelp() {
		printHelp(null);
	}

	/**
	 * Prints the help for the MiRNAtor command call.
	 * 
	 * @param e
	 *            Error message thrown.
	 */
	private void printHelp(String e) {
		final String HEADER = new StringBuilder()
				.append("MiRNAtor Command: find\n\n")
				.append("Use this command to find potential MREs in a set of given Sequences for a given list of miRNAs.\n")
				.append("Usage: java -jar mirnator.jar find [options] <datasource>\n")
				.append("  implemented datasource: miRBase - 'mirbase'\n\n").toString();

		final String FOOTER = new StringBuilder().append(
				"\n\nExample: java -jar mirnator.jar find -o results/mres.txt -s sequence.fa -t mmu mirbase \n\n")
				.toString();

		if (e != null)
			System.err.println(e);
		System.err.print(HEADER);

		HelpFormatter hf = new HelpFormatter();
		PrintWriter pw = new PrintWriter(System.err, true);
		hf.printOptions(pw, 78, options, 2, 2);

		System.err.print(FOOTER);
		System.exit(1);
	}
}
