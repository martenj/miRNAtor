/**
 * 
 */
package de.charite.compbio.mirnator.io.writer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;

import de.charite.compbio.mirnator.reference.Mre;
import de.charite.compbio.mirnator.util.IOUtil;

/**
 * @author mjaeger
 *
 */
public class MREfileWriter implements Runnable {

	private BlockingQueue<Mre> mreBeans;
	private Logger log = null;
	private BufferedWriter buf;
	private String filename;
	private boolean simpleFormat = false;

	public MREfileWriter(BlockingQueue<Mre> mreBeans) {
		this(mreBeans, "MREfileWriterLog", "resultMREprediction.txt");
	}

	public MREfileWriter(BlockingQueue<Mre> mreBeans, String logger, String filename) {
		this.mreBeans = mreBeans;
		this.log = Logger.getLogger(logger);
		this.filename = filename;
	}

	public MREfileWriter(BlockingQueue<Mre> mreBeans, String logger, String filename, boolean simpleFormat) {
		this.mreBeans = mreBeans;
		this.log = Logger.getLogger(logger);
		this.filename = filename;
		this.simpleFormat = simpleFormat;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {

		try {
			this.buf = IOUtil.getBufferedFileWriter(new File(filename));
			int id = 1;
			if (this.simpleFormat)
				buf.write("mirnID,sequenceID,sequenceStart,sequenceEnd,mreType,mirnaStart,mirnaEnd\n");

			while (true) {
				// check if there are no longer MREs added to the BlockingQueue
				if (mreBeans.isEmpty()) {
					Thread.sleep(1000);
					if (mreBeans.isEmpty()) {
						System.err.println("!!!Beende den MREwriter!!!");
						log.info("The BlockedQueue was empty for more than 500 ms - closing the Writer.");
						break;
					}
				}

				if (this.simpleFormat)
					buf.write(this.mreBeans.take().toStringFlatFileSimple() + "\n");
				else
					buf.write(this.mreBeans.take().toStringFlatFile(id++) + "\n");

			}
		} catch (InterruptedException e) {
			log.severe("Problem occured. Interrupted : " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			log.severe("Problem occured. IOexception : " + e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				// need to check for null
				if (buf != null) {
					buf.close();
				}
			} catch (IOException ex) {
				log.severe("Problem occured. Cannot close reader : " + ex.getMessage());
			}
		}
	}

}
