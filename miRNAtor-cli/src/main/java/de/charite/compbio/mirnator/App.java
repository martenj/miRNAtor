package de.charite.compbio.mirnator;

import de.charite.compbio.mirnator.cmd.MirnatorCommand;

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
		System.err.println("         java -jar mirnator.jar find -s sequence.fa -m mirnas.fa");
		System.err.println("         java -jar mirnator.jar find -s ucsc_hg18.ser -m mirnas.fa");
		System.err.println("");
	}

}
