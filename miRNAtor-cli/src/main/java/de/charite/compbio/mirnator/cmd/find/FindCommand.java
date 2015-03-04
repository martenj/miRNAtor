/**
 * 
 */
package de.charite.compbio.mirnator.cmd.find;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.cli.ParseException;

import de.charite.compbio.jannovar.cmd.CommandLineParsingException;
import de.charite.compbio.jannovar.cmd.HelpRequestedException;
import de.charite.compbio.mirnator.MirnatorOptions;
import de.charite.compbio.mirnator.cmd.MirnatorCommand;
import de.charite.compbio.mirnator.exceptions.MirnatorException;
import de.charite.compbio.mirnator.io.parser.ensembl.EnsemblFastaParser;
import de.charite.compbio.mirnator.io.parser.mirna.MirbaseFastaParser;
import de.charite.compbio.mirnator.io.writer.MREfileWriter;
import de.charite.compbio.mirnator.predictor.BartelMREpredictor;
import de.charite.compbio.mirnator.reference.Mirna;
import de.charite.compbio.mirnator.reference.Mre;
import de.charite.compbio.mirnator.reference.SequenceModel;

/**
 * Will find all mirna binding sites for the given sequences and mirnas.
 *
 * @author Marten Jäger <marten.jaeger@charite.de>
 *
 */
public class FindCommand extends MirnatorCommand {

	private static final Logger logger = Logger.getLogger(FindCommand.class.getSimpleName());

	public FindCommand(String[] options) throws HelpRequestedException, CommandLineParsingException {
		super(options);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.charite.compbio.cmd.MirnatorCommand#parseCommandLine(java.lang.String[])
	 */
	@Override
	protected MirnatorOptions parseCommandLine(String[] options) throws HelpRequestedException,
			CommandLineParsingException {
		try {
			return new FindCommandlineParser().parse(options);
		} catch (ParseException e) {
			throw new CommandLineParsingException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.charite.compbio.cmd.MirnatorCommand#run()
	 */
	@Override
	public void run() throws MirnatorException {
		System.err.println("Options\n" + options);

		// init mirnas
		MirbaseFastaParser mbfp = new MirbaseFastaParser(options.mirna_path);
		mbfp.parse();
		ArrayList<Mirna> mirnas = mbfp.getMirnas(options.taxon);
		logger.info("Found " + mirnas.size() + " mirnas.");

		// init MREs
		BlockingQueue<Mre> mres = new LinkedBlockingQueue<Mre>();
		// predict MREs
		ExecutorService es;

		if (options.online_cpus == 1) {
			es = Executors.newFixedThreadPool(options.online_cpus);
			logger.log(Level.INFO, "will use " + options.online_cpus + " cpus");
		} else {
			es = Executors.newFixedThreadPool(options.online_cpus > 2 ? options.online_cpus - 1 : options.online_cpus);
			logger.log(Level.INFO, "will use "
					+ (options.online_cpus > 2 ? options.online_cpus - 1 : options.online_cpus) + " cpus");
		}

		logger.log(Level.INFO, "Start predicting MRE sites");
		int c = 0;

		es.execute(new MREfileWriter(mres, logger.getName(), options.output_file, true));
		int i = 0;
		// init SequenceModel iterator
		EnsemblFastaParser efp;
		try {
			efp = new EnsemblFastaParser(options.sequence_path);
			for (SequenceModel seq : efp) {

				for (Mirna mir : mirnas) {
					es.execute(new BartelMREpredictor(mir, seq, mres));
				}
				if (++i > 10)
					break;
			}
		} catch (IOException e1) {
			throw new MirnatorException("Failed to open the Sequence Fasta file");
		}

		efp.close();
		es.shutdown();
		try {
			es.awaitTermination(1, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.exit(1);
		}
		logger.log(Level.INFO, "finished predicting MREs");
	}
}
