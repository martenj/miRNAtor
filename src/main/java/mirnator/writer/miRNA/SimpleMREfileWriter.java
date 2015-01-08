/**
 * 
 */
package mirnator.writer.miRNA;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;

import mirnator.structs.mirna.Mre;

/**
 * @author mjaeger
 *
 */
public class SimpleMREfileWriter implements Runnable {

	private BlockingQueue<Mre> mreBeans;
	private Logger log = null;
	private BufferedWriter buf;
	private String filename;

	public SimpleMREfileWriter(BlockingQueue<Mre> mreBeans) {
		this(mreBeans, "SimpleMREfileWriterLog", "resultMREprediction.txt");
	}

	public SimpleMREfileWriter(BlockingQueue<Mre> mreBeans, String logger, String filename) {
		this.mreBeans = mreBeans;
		this.log = Logger.getLogger(logger);
		this.filename = filename;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {

		try {
			this.buf = new BufferedWriter(new FileWriter(filename));
			int id = 1;

			while (true) {
				// check if there are no longer MREs added to the BlockingQueue
				if (mreBeans.isEmpty()) {
					Thread.sleep(500);
					if (mreBeans.isEmpty()) {
						System.err.println("!!!Beende den MREwriter!!!");
						log.info("The BlockedQueue was empty for more than 500 ms - closing the Writer.");
						break;
					}
				}

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
