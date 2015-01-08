/**
 * 
 */
package mirnator.parser.ucsc;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;

import mirnator.structs.ucsc.UCSCtxInfo;

/**
 * This parser is based on the assumption, that the knownIsoforms.txt is organised 
 * in accending order fo
 * @author mjaeger
 *
 */
public class UCSCisoformsParser extends UCSCparser {
	
	private enum Fieldnames {	cluster,
		kgName
	}
	
	private HashMap<String, Integer> kgCluster;
	private ArrayList<HashSet<String>> cluster2kg;

	/**
	 * @param infile
	 */
	public UCSCisoformsParser(File infile) {
		super(infile);
		this.init();
	}

	/**
	 * @param filename
	 */
	public UCSCisoformsParser(String filename) {
		super(filename);
		this.init();
	}
	
	private void init(){
		this.kgCluster	= new HashMap<String, Integer>();
		this.cluster2kg	= new ArrayList<HashSet<String>>();
	}

	/* (non-Javadoc)
	 * @see mirnator.parser.ucsc.UCSCparser#processFields(java.lang.String[])
	 */
	@Override
	protected boolean processFields(String[] fields) {
		String kgName;
		Integer cluster;
		
		if(fields.length != Fieldnames.values().length)
		    return false;
		try{
			kgName	= fields[Fieldnames.kgName.ordinal()];
			cluster	= Integer.parseInt(fields[Fieldnames.cluster.ordinal()]);
			if(!this.kgCluster.containsKey(kgName)){
        	    this.kgCluster.put(kgName,cluster);
        	}
        	else{
        	    System.err.println("Duplicate entry: "+kgName);
        	}
			
			if(this.cluster2kg.size() < cluster){
				this.cluster2kg.add(new HashSet<String>());
			}
			this.cluster2kg.get(cluster-1).add(kgName);
				
		}catch(NumberFormatException e){
		    e.printStackTrace();
		    return false;
		}
		return true;
	}
	
	/**
	 * @return the cluster2kg
	 */
	public ArrayList<HashSet<String>> getCluster2kg() {
		this.parse();
		return cluster2kg;
	}

//	public static void main(String[] args){
//		UCSCisoformsParser uip	= new UCSCisoformsParser("/home/mjaeger/data/UCSC/mm9/database/knownIsoforms.txt");
//		uip.parse();
//		
//		for(int i=0;i<10;i++){
//			System.out.println(uip.cluster2kg.get(i));
//		}
//	}

}
