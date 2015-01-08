/**
 * 
 */
package mirnator.parser.ucsc;

import java.io.File;
import java.util.HashMap;

import mirnator.structs.ucsc.UCSCtxInfo;

/**
 * @author mjaeger
 *
 */
public class UCSCtxInfoParser extends UCSCparser {

    private enum Fieldnames {	kgName,
				category, 	
				sourceAcc,
				isRefSeq,
				sourceSize,
				aliCoverage,
				aliIdRatio,
				genoMapCount,
				exonCount,
				orfSize,
				cdsScore,
				startComplete,
				endComplete,
				nonsenseMediatedDecay,
				retainedIntron,
				bleedIntoIntron,
				strangeSplice,
				atacIntrons,
				cdsSingleInIntron,
				cdsSingleInUtr3,
				selenocysteine,
				genomicFrameShift,
				genomicStop}
    private HashMap<String, UCSCtxInfo> knownTxInfo;
    private UCSCtxInfo singleTxInfo;
    
    public UCSCtxInfoParser(File infile) {
	super(infile);
	this.init();
    }
    
    public UCSCtxInfoParser(String filename){
	super(filename);
	this.init();
    }
    
    private void init(){
	this.knownTxInfo	= new HashMap<String, UCSCtxInfo>();
    }
    /* (non-Javadoc)
     * @see mirnator.parser.ucsc.UCSCparser#processFields(java.lang.String[])
     */
    @Override
    protected boolean processFields(String[] fields) {
	if(fields.length != Fieldnames.values().length)
	    return false;
	try{
        	this.singleTxInfo	= new UCSCtxInfo();
        	this.singleTxInfo.setKgName(fields[Fieldnames.kgName.ordinal()]);
        	this.singleTxInfo.setCategory(fields[Fieldnames.category.ordinal()]);
        	this.singleTxInfo.setSourceAcc(fields[Fieldnames.sourceAcc.ordinal()]);
        	this.singleTxInfo.setRefSeq(fields[Fieldnames.isRefSeq.ordinal()].equals("1") ? true : false);
        	this.singleTxInfo.setSourceSize(Integer.parseInt(fields[Fieldnames.sourceSize.ordinal()]));
        	this.singleTxInfo.setAliCoverage(Double.parseDouble(fields[Fieldnames.aliCoverage.ordinal()]));
        	this.singleTxInfo.setAliIdRatio(Double.parseDouble(fields[Fieldnames.aliIdRatio.ordinal()]));
        	this.singleTxInfo.setGenoMapCount(Integer.parseInt(fields[Fieldnames.genoMapCount.ordinal()]));
        	this.singleTxInfo.setExonCount(Integer.parseInt(fields[Fieldnames.exonCount.ordinal()]));
        	this.singleTxInfo.setOrfSize(Integer.parseInt(fields[Fieldnames.orfSize.ordinal()]));
        	this.singleTxInfo.setCdsScore(Double.parseDouble(fields[Fieldnames.cdsScore.ordinal()]));
        	this.singleTxInfo.setStartComplete(fields[Fieldnames.startComplete.ordinal()].equals("1") ? true : false);
        	this.singleTxInfo.setEndComplete(fields[Fieldnames.endComplete.ordinal()].equals("1") ? true : false);
        	this.singleTxInfo.setNonsenseMediatedDecay(fields[Fieldnames.nonsenseMediatedDecay.ordinal()].equals("1") ? true : false);
        	this.singleTxInfo.setRetainedIntron(fields[Fieldnames.retainedIntron.ordinal()].equals("1") ? true : false);
        	this.singleTxInfo.setBleedIntoIntron(Integer.parseInt(fields[Fieldnames.bleedIntoIntron.ordinal()]));
        	this.singleTxInfo.setStrangeSplice(Integer.parseInt(fields[Fieldnames.strangeSplice.ordinal()]));
        	this.singleTxInfo.setAtacIntrons(Integer.parseInt(fields[Fieldnames.atacIntrons.ordinal()]));
        	this.singleTxInfo.setCdsSingleInIntron(fields[Fieldnames.cdsSingleInIntron.ordinal()].equals("1") ? true : false);
        	this.singleTxInfo.setCdsSingleInUtr3(fields[Fieldnames.cdsSingleInUtr3.ordinal()].equals("1") ? true : false);
        	this.singleTxInfo.setSelenocysteine(fields[Fieldnames.selenocysteine.ordinal()].equals("1") ? true : false);
        	this.singleTxInfo.setGenomicFrameShift(fields[Fieldnames.genomicFrameShift.ordinal()].equals("1") ? true : false);
        	this.singleTxInfo.setGenomicStop(fields[Fieldnames.genomicStop.ordinal()].equals("1") ? true : false);
        	
        	if(!this.knownTxInfo.containsKey(this.singleTxInfo.getKgName())){
        	    this.knownTxInfo.put(this.singleTxInfo.getKgName(),this.singleTxInfo);
        	}
        	else{
        	    System.err.println("Duplicate entry: "+this.singleTxInfo.getKgName());
        	}
	}
	catch(NumberFormatException e){
	    e.printStackTrace();
	    return false;
	}
	return true;
    }

    /**
     * Returns a {@link HashMap} with the knowngene transcription informations with the UCSC_id as key and 
     * the {@link UCSCtxInfo} as value.
     * @return the knownXref
     */
    public HashMap<String, UCSCtxInfo> getKnownTxInfo() {
	parse();
        return this.knownTxInfo;
    }
}
