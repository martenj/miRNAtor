/**
 * 
 */
package mirnator.writer.ucsc;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;

/**
 * @author mjaeger
 *
 */
public class StatisticsFileWriter implements Runnable {
	private BlockingQueue<String> statistics_queue;
	private Logger logger = null;
	private BufferedWriter buf;
	private String filename;

	public StatisticsFileWriter(BlockingQueue<String> statistics_queue) {
		this(statistics_queue, "StatisticsFileWriter", "results/resultMirnaStatistics.txt");
	}

	public StatisticsFileWriter(BlockingQueue<String> statistics_queue, String logger, String filename) {
		this.statistics_queue = statistics_queue;
		this.logger = Logger.getLogger(logger);
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
				if (statistics_queue.isEmpty()) {
					Thread.sleep(50000);
					if (statistics_queue.isEmpty()) {
						System.err.println("!!!Beende den MREwriter!!!");
						logger.info("The BlockedQueue was empty for more than 500 ms - closing the Writer.");
						break;
					}
				}

				buf.write((id++) + "," + statistics_queue.take() + "\n");

			}
		} catch (InterruptedException e) {
			logger.severe("Problem occured. Interrupted : " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			logger.severe("Problem occured. IOexception : " + e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				// need to check for null
				if (buf != null) {
					buf.close();
					logger.info("Writer closed.");
				}
			} catch (IOException ex) {
				logger.severe("Problem occured. Cannot close reader : " + ex.getMessage());
			}
		}
	}

}
