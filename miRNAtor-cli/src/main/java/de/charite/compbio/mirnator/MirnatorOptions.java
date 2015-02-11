/**
 * 
 */
package de.charite.compbio.mirnator;

import java.io.File;
import java.net.URL;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Configuration for the miRNAtor program.
 *
 * This class contains the configuration for all miRNAtor commands, even though most are not used by some commands. For
 * example, the proxy setting is only used when downloading data.
 *
 * @author Marten Jäger <marten.jaeger@charite.de>
 *
 */
public final class MirnatorOptions {

	/** the selected command */
	public Command command = null;

	/** Path to the directory used for the downloaded transcript/sequence information files */
	public String data_path = "../data";

	/** Full path to the output file */
	public String output_file = null;

	/** proxy for HTTP */
	public URL httpProxy = null;

	/** proxy for HTTPS */
	public URL httpsProxy = null;

	/** proxy for FTP */
	public URL ftpProxy = null;

	/** path to the miRNA MiRBase formated fasta file */
	// public File mirna_path = null;
	public File mirna_path = new File(data_path + "/mature.fa.gz");

	/** Path to the sequence file in fasta format */
	public File sequence_path = null;

	/** Path to a Jannovar serialized transcript file */
	public File jannovar_path = null;

	public String taxon = null;

	public int online_cpus = 1;

	/**
	 * The command that is to be executed.
	 */
	public enum Command {
		DOWNLOAD, FIND
	}

	@Override
	public String toString() {
		return toString(ToStringStyle.MULTI_LINE_STYLE);
	}

	/**
	 * you can use the following styles: <li>ToStringStyle.DEFAULT_STYLE</li> <li>ToStringStyle.MULTI_LINE_STYLE</li>
	 * <li>ToStringStyle.NO_FIELD_NAMES_STYLE</li> <li>ToStringStyle.SHORT_PREFIX_STYLE</li> <li>
	 * ToStringStyle.SIMPLE_STYLE</li>
	 */
	public String toString(ToStringStyle style) {
		if (this.command == Command.DOWNLOAD)
			return new ToStringBuilder(this, style).append("command", this.command).append("data_path", data_path)
					.append("http proxy", httpProxy).append("https proxy", httpsProxy).append("ftp proxy", ftpProxy)
					.toString();
		else if (this.command == Command.FIND)
			return new ToStringBuilder(this, style).append("command", this.command).append("species", taxon)
					.append("mirna_path", mirna_path).append("sequence_path", sequence_path)
					.append("jannovar_path", jannovar_path).append("threads_to_be_used", online_cpus).toString();
		else
			return "unknown command";
	}
}
