/**
 * 
 */
package de.charite.compbio.mirnator.cmd.download;

import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;

import de.charite.compbio.jannovar.cmd.HelpRequestedException;
import de.charite.compbio.mirnator.MirnatorOptions;

/**
 * This calls can be used to download the needed FIles for the miRNA binding site detection
 *
 * @author Marten Jäger <marten.jaeger@charite.de>
 *
 */
public class DownloadCommandlineParser {

	/** options representation for the Apache commons command line parser */
	protected Options options;
	/** the Apache commons command line parser */
	protected Parser parser;

	public DownloadCommandlineParser() {
		initializeParser();
	}

	private void initializeParser() {
		options = new Options();
		options.addOption("h", "help", false, "show this help");
		options.addOption("s", "data-source-list", true, "INI file with data source list");
		options.addOption("d", "data-dir", true,
				"target folder for downloaded and serialized files, defaults to \"data\"");

		parser = new GnuParser();
	}

	public MirnatorOptions parse(String[] argv) throws ParseException, HelpRequestedException {
		// Parse the command line.
		CommandLine cmd = parser.parse(options, argv);

		// fill the MiRNAtoroptions
		MirnatorOptions result = new MirnatorOptions();
		result.command = MirnatorOptions.Command.DOWNLOAD;

		if (cmd.hasOption("help")) {
			printHelp();
			throw new HelpRequestedException();
		}

		if (cmd.hasOption("data-dir"))
			result.data_path = cmd.getOptionValue("data-dir");

		// get proxy settings from system environment if possible
		Map<String, String> env = System.getenv();
		if (getProxyURL(env.get("HTTP_PROXY")) != null)
			result.httpProxy = getProxyURL(env.get("HTTP_PROXY"));
		if (getProxyURL(env.get("http_proxy")) != null)
			result.httpProxy = getProxyURL(env.get("http_proxy"));
		if (getProxyURL(env.get("HTTPS_PROXY")) != null)
			result.httpsProxy = getProxyURL(env.get("HTTPS_PROXY"));
		if (getProxyURL(env.get("https_proxy")) != null)
			result.httpsProxy = getProxyURL(env.get("https_proxy"));
		if (getProxyURL(env.get("FTP_PROXY")) != null)
			result.ftpProxy = getProxyURL(env.get("FTP_PROXY"));
		if (getProxyURL(env.get("ftp_proxy")) != null)
			result.ftpProxy = getProxyURL(env.get("ftp_proxy"));

		// get proxy settings from the command line (--proxy), can be overwritten below
		if (cmd.hasOption("proxy")) {
			result.httpProxy = getProxyURL(cmd.getOptionValue("proxy"));
			result.httpsProxy = getProxyURL(cmd.getOptionValue("proxy"));
			result.ftpProxy = getProxyURL(cmd.getOptionValue("proxy"));
		}

		// get proxy settings from the command line, overriding the environment settings
		if (cmd.hasOption("http-proxy"))
			result.httpProxy = getProxyURL(cmd.getOptionValue("http-proxy"));
		if (cmd.hasOption("https-proxy"))
			result.httpsProxy = getProxyURL(cmd.getOptionValue("https-proxy"));
		if (cmd.hasOption("ftp-proxy"))
			result.ftpProxy = getProxyURL(cmd.getOptionValue("ftp-proxy"));

		return result;
	}

	/**
	 * Build {@link URL} from an environment proxy configuration
	 *
	 * @param envValue
	 *            environment value with proxy host and port as URL
	 * @return {@link URL} with configuration from <code>envValue</code> or <code>null</code> if not set or not
	 *         successful
	 */
	private URL getProxyURL(String envValue) {
		if (envValue == null)
			return null;

		try {
			return new URL(envValue);
		} catch (MalformedURLException e) {
			System.err.println("WARNING: Could not parse proxy value " + envValue + " as URL.");
			return null;
		}
	}

	private void printHelp() {
		final String HEADER = new StringBuilder().append("MiRNAtor Command: download\n\n")
				.append("Use this command to download a transcript database. This file then can\n")
				.append("later be used by the find command.\n\n")
				.append("Usage: java -jar mirnator.jar download [options] <datasource>+\n\n").toString();

		final String FOOTER = new StringBuilder().append("\n\nExample: java -jar mirnator.jar download hg19/ucsc\n\n")
				.append("Note that Jannovar also interprets the environment variables\n")
				.append("HTTP_PROXY, HTTPS_PROXY and FTP_PROXY for downloading files.\n").toString();

		System.err.print(HEADER);

		HelpFormatter hf = new HelpFormatter();
		PrintWriter pw = new PrintWriter(System.err, true);
		hf.printOptions(pw, 78, options, 2, 2);

		System.err.print(FOOTER);
	}

}
