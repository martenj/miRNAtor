package de.charite.compbio.mirnator;

import de.charite.compbio.jannovar.cmd.CommandLineParsingException;
import de.charite.compbio.jannovar.cmd.HelpRequestedException;
import de.charite.compbio.mirnator.cmd.MirnatorCommand;
import de.charite.compbio.mirnator.cmd.download.DownloadCommand;
import de.charite.compbio.mirnator.cmd.find.FindCommand;
import de.charite.compbio.mirnator.exceptions.MirnatorException;

/**
 * This is the driver class for the miRNAtor command line tool.
 *
 */
public class App {
	MirnatorOptions options = null;

	public static void main(String[] args) {
		if (args.length == 0) {
			// No arguments, print top level help and exit.
			printTopLevelHelp();
			System.exit(1);
		}

		MirnatorCommand cmd = null;
		try {
			if (args[0].equals("find"))
				cmd = new FindCommand(args);
			else if (args[0].equals("download"))
				cmd = new DownloadCommand(args);
			else if (args[0].equals("-h") | args[0].equals("help")) {
				printTopLevelHelp();
				System.exit(1);
			} else {
				throw new CommandLineParsingException("unknown subcommand: " + args[0]);
			}
		} catch (HelpRequestedException e) {
			// printTopLevelHelp();
			// System.exit(1);
		} catch (CommandLineParsingException e) {
			System.err.println("Misformated command call: " + e.getMessage());
			printTopLevelHelp();
			System.exit(1);
		}

		// Stop if no command could be created.
		if (cmd == null)
			System.exit(1);

		// Execute the command.
		try {
			cmd.run();
		} catch (MirnatorException e) {
			System.err.println("ERROR: " + e.getMessage());
			System.exit(1);
		}
	}

	/**
	 * Print top level help (without any command).
	 */
	private static void printTopLevelHelp() {
		System.err.println("Program: mirnator (detection of microRNA binding sites)");
		System.err.println("Version: 0.0.1");
		System.err.println("Contact: Marten Jaeger <marten.jaeger@charite.de>");
		System.err.println("");
		System.err.println("Usage: java -jar mirnator.jar <command> [options]");
		System.err.println("");
		System.err.println("Command: download      download transcript database");
		System.err.println("         find          find miRNA binding sites");
		System.err.println("");
		System.err.println("Example: java -jar mirnator.jar download mirnas");
		System.err.println("         java -jar mirnator.jar download hg18/ucsc");
		System.err.println("         java -jar mirnator.jar find -s sequence.fa -t mmu mirbase");
		System.err.println("         java -jar mirnator.jar find -s ucsc_hg18.ser -t hsa mirbase");
		System.err.println("");
	}

}
