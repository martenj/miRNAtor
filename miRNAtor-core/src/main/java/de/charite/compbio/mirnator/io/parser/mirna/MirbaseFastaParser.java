package de.charite.compbio.mirnator.io.parser.mirna;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.charite.compbio.mirnator.reference.Mirna;
import de.charite.compbio.mirnator.util.IOUtil;

public class MirbaseFastaParser extends MirnaDBParser {

	// private String headerRegex =
	// ">([a-z0-9]{3,5})-([miR|let|lin|bantam]-*[0-9a-zA-Z.\\-]+\\**) (MIMAT[0-9]{7}) [\\w\\p{Punct} ]+";
	private String headerRegex = ">([a-z0-9]{3,5})-([miR|let|lin|bantam]-*[0-9a-zA-Z.\\-\\/\\**]+) ([MIMATF]{3,5}[0-9]{6,7})[\\w\\p{Punct} ]*";

	public MirbaseFastaParser(String filename) {
		this(new File(filename));
	}

	public MirbaseFastaParser(File file) {
		super(file);
	}

	@Override
	public void parse() {

		Pattern pat = Pattern.compile(this.headerRegex);
		Matcher mat;
		String line;

		// System.out.println(this.checkfile());
		if (this.validateFile()) {
			BufferedReader buf;
			try {
				buf = IOUtil.getBufferedReaderFromFileName(file);
				while ((line = buf.readLine()) != null) {
					if (line.startsWith("#"))
						continue;
					mat = pat.matcher(line);
					//
					if (mat.find()) {

						this.species = mat.group(1);
						this.name = mat.group(2);
						this.accession = mat.group(3);
					} else {
						if (line.startsWith(">")) {
							System.err.println(line);
							log.log(Level.WARNING, "misformed miRNA FastA header: " + line);
						} else {
							this.sequence = line;
							this.mirnas.add(new Mirna(this.accession, this.sequence, this.name, this.species, null));
						}
					}
				}
				buf.close();
			} catch (IOException e) {
				log.log(Level.WARNING,
						"File wasn't found or could not be read.\n This should never happen since the file was checked.");
				e.printStackTrace();

			}
		}
	}

	@Override
	public boolean validateFile() {
		if (this.file == null)
			return false;
		BufferedReader buf;
		try {
			String line;
			int i = 0;
			buf = IOUtil.getBufferedReaderFromFileName(this.file);
			while ((line = buf.readLine()) != null) {
				if (line.startsWith("#"))
					continue;
				if (line.matches(this.headerRegex))
					break;
				if (i++ > 10) {
					IOUtil.close(buf);
					return false;
				}
			}
			IOUtil.close(buf);

		} catch (FileNotFoundException e) {
			log.log(Level.WARNING, "File \"" + this.file.getAbsolutePath() + "\" not found.\n");
			log.log(Level.WARNING, e.getMessage());
			return false;
		} catch (IOException e) {
			log.log(Level.WARNING, "Error reading line from \"" + this.file.getAbsolutePath() + "\".\n");
			log.log(Level.WARNING, e.getMessage());
			return false;
		}

		return true;
	}
}
