/**
 * 
 */
package mirnator.parser.uniprot;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import mirnator.structs.uniprot.UniProtEntry;

/**
 * This is the abstract {@link UniProtParser} class for all UniProt file formats (xml,flat,html).<br>
 * The parser should be able to handle SWISSprot and Trembl files.
 * @author mjaeger
 *
 */
public abstract class UniProtParser {
    
    protected ArrayList<UniProtEntry> entries;
    protected int speciesID;
    

    /**
     * Parses the uniprot file given an {@link File} object containing the infos to the UniProt file
     * @param file - {@link File} with UNiProt data
     * @throws IOException
     */
    abstract public void parse(File file) throws IOException;

    /**
     * Parses the uniprot file given the (path +) filename to the UniProt file.
     * @param file - path +) filename to the UniProt file
     * @throws IOException
     */   
    abstract public void parse(String filename) throws IOException;

    /**
     * Returns the list with all parsed uniprot entries.
     * @return the entries
     */
    public ArrayList<UniProtEntry> getEntries() {
        return entries;
    }

}
