/**
 * 
 */
package de.charite.compbio.mirnator;

import java.net.URL;

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

	/** Path to the directory used for the downloaded transcript/sequence information files */
	public String downloadPath = "data";

	/** proxy for HTTP */
	public URL httpProxy = null;

	/** proxy for HTTPS */
	public URL httpsProxy = null;

	/** proxy for FTP */
	public URL ftpProxy = null;

}
