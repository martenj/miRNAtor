/**
 * 
 */
package mirnator.parser.kegg;

import java.util.HashMap;
import java.util.HashSet;

/**
 * This is the base class all KEGG  gene<->pathway association files
 * @author mjaeger
 *
 */
public abstract class KEGGparser {
	String filepath;
	protected HashMap<String, Integer> entrez2index;
	protected HashMap<Integer, String> index2entrez;
	protected HashMap<String, Integer> pathway2index;
	protected HashMap<Integer, String> index2pathway;
	
	protected boolean[][] pathwayMatrix;				// [genes][pathways]
	
	protected int nGenes;
	protected int nPathways;

	public KEGGparser(String file) {
		this.filepath = file;
		this.entrez2index = new HashMap<String, Integer>();
		this.pathway2index = new HashMap<String, Integer>();
		this.index2entrez = new HashMap<Integer, String>();
		this.index2pathway = new HashMap<Integer, String>();
	}
	
	public HashSet<String> getAllGenesEntrez(String pathway) {
		HashSet<String> genes = new HashSet<String>();
		int n = this.pathway2index.get(pathway);
		for(int m=0;m<this.nGenes;m++){
			if(this.pathwayMatrix[m][n]){
				String gene = this.index2entrez.get(m);
				genes.add(gene);
			}
		}
		return genes;		
	}
	
	public int getNumberOfRNAs() {
		return nGenes;
	}
	
	public int getNumberOfPathways() {
		return nPathways;
	}
	
	/**
	 * 
	 * @return HashSet with all Pathways
	 */
	public Iterable<String> getPathways(){
		HashSet<String> pathways = new HashSet<String>();
		pathways.addAll(this.pathway2index.keySet());
		return pathways;
	}
	
	
	public HashSet<String> getRNAs(){
		HashSet<String> rnas = new HashSet<String>();
		rnas.addAll(this.entrez2index.keySet());
		return rnas;
	}
	/**
	 * This is the method called by the subclasses to parse the input files.
	 */
	abstract public void parse();
	
	
}
