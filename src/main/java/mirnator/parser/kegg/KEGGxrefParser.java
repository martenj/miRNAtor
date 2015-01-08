package mirnator.parser.kegg;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import mirnator.constants.ExceptionConstants;

public class KEGGxrefParser extends KEGGparser implements ExceptionConstants{
	
	public KEGGxrefParser(String file) {
		super(file);
	}

	@Override
	public void parse() {
		try{

			registerGenesandPathways();
			this.pathwayMatrix = new boolean[this.nGenes][this.nPathways];
			BufferedReader in = new BufferedReader(new FileReader(this.filepath));
			String line;
			while((line = in.readLine())!= null){
				String[] fields = line.split("\\t");
				if(!fields[fields.length-1].substring(0,5).equals("path:"))
					continue;
				
				String geneID = fields[0].split(":")[1];
				int geneIndex = this.entrez2index.get(geneID);
				
				String[] pathways = fields[fields.length-1].substring(5).split(" ");
				for(int i=0;i<pathways.length;i++){
					int pathwayIndex = this.pathway2index.get(pathways[i]);
					this.pathwayMatrix[geneIndex][pathwayIndex] = true;
				}
			}
			in.close();
		}
		catch(IOException e){
			e.printStackTrace();
			System.exit(ExceptionConstants.KEGGxrefEX);
		}
		
		
	}

	private void registerGenesandPathways() throws IOException{
		String line;
		BufferedReader in = new BufferedReader(new FileReader(this.filepath));
		int geneNr=0;
		int pathwayNr = 0;
		while((line = in.readLine())!= null){
			String[] fields = line.split("\\t");
			if(!fields[fields.length-1].substring(0,5).equals("path:"))
				continue;
			
			String geneID = fields[0].split(":")[1];
			this.entrez2index.put(geneID, geneNr);
			this.index2entrez.put(geneNr, geneID);
			geneNr++;
			
			String[] pathways = fields[fields.length-1].substring(5).split(" ");
			for(int i=0;i<pathways.length;i++){
				if(!this.pathway2index.containsKey(pathways[i])){
					this.pathway2index.put(pathways[i], pathwayNr);
					this.index2pathway.put(pathwayNr, pathways[i]);
					pathwayNr++;
				}
			}
		}
		this.nGenes = geneNr;
		this.nPathways = pathwayNr;
		in.close();
	}
}

