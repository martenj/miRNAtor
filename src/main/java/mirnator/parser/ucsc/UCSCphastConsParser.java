/**
 * 
 */
package mirnator.parser.ucsc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mirnator.sql2java.KnowngeneBean;
import mirnator.sql2java.MreBean;

/**
 * The UCSCphastConsParser reads the phast conservation files from UCSC and stores the data in a ...<br>
 * The files have to be in the following format:<br> 
 * fixedStep chrom=chr1 start=10918 step=1<br>
 * 0.254<br>
 * 0.253<br>
 * ...<br>
 * <br>
 * Remember that there could be more than one line like "fixedStep ..." and that the start is in '1' 
 * annotation not '0'.
 * 
 * @author mjaeger
 *
 */
public class UCSCphastConsParser {

    private String filename;
    private File file;

    private String 	chromosom;
    private int 	tmp_start;
    private int		tmp_end;
    private int 	tmp_stepsize;
    private float 	tmp_score;
    private ArrayList<Double> tmp_scores;
    private String 	line;
    private boolean	isfirstSegment	= true;
    private boolean 	next 		= true;
    
    private FixedStepSeqment prev_segment;
    private FixedStepSeqment curr_segment;
    private FixedStepSeqment next_segment;

    private static final String regex 	= "fixedStep chrom=(chr[0-9XYMUn]{1,2}[_a-z0-9]*) start=([0-9]+) step=([0-9]+)";
    private static final Pattern patty 	= Pattern.compile(regex);
    private static final int 	CHROM 	= 1;
    private static final int 	START 	= 2;
    private static final int 	STEP 	= 3;

    private static Logger logger 	= Logger.getLogger("UCSCconservationUpdater");
    
    private BufferedReader buf;		


    /**
     * The default constructor for the a UCSC phastCons parser. Sets also the
     * filename.
     * 
     * @param file
     *            - the phastCons file
     */
    public UCSCphastConsParser(File file) {
	this.file = file;
	this.filename = file.getAbsolutePath();
	init();
    }

    /**
     * The alternative constructor for a UCSC phastCons parser. The file is also
     * initialised.
     * 
     * @param filename
     *            - the path to the phastCons file
     */
    public UCSCphastConsParser(String filename) {
	this(new File(filename));
    }
    
    private void init(){
	try {
	    this.buf	= new BufferedReader(new FileReader(this.file));
	} catch (FileNotFoundException e) {
	    this.logger.severe("Failed - no file found: "+this.filename);
	    e.printStackTrace();
	}
    }
    
    public void terminate(){
	try {
	    this.buf.close();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }
    
    /**
     * Parse the next conserved segment and switch prev<- curr, curr<- next, next<- new (if available) 
     * @return false if there is no next conserved segment 
     */
    public boolean parseNext(){
	boolean hasnext	= false;
	Matcher	match;
	
	this.prev_segment	= this.curr_segment;
	this.curr_segment	= this.next_segment;
	this.next_segment	= new FixedStepSeqment();
	
	this.tmp_scores		= new ArrayList<Double>();
	
	
	try {
	    // we need the fixedStep line for the first segment otherwise use the line in the buffer
	    if(isfirstSegment){
		while((this.line = buf.readLine()) != null || this.line.startsWith("fixed")){
		    match	= this.patty.matcher(line);
		    if(match.matches()){
			this.chromosom		= match.group(CHROM);
			this.tmp_start		= Integer.parseInt(match.group(START));
			this.tmp_end		= this.tmp_start;
			this.tmp_stepsize	= Integer.parseInt(match.group(STEP));
			this.isfirstSegment	= false;
			break;
		    }
		}
//		System.out.println();
	    } else {
		match = this.patty.matcher(line);
//		System.out.print("+");
//		System.out.println();
		if (match.matches()) {
		    this.chromosom 	= match.group(CHROM);
		    this.tmp_start 	= Integer.parseInt(match.group(START));
		    this.tmp_end	= this.tmp_start;
		    this.tmp_stepsize 	= Integer.parseInt(match.group(STEP));
//		    this.isfirstSegment = false;
		}
	    }
	    while((this.line = buf.readLine()) != null){
		if(this.line.startsWith("fixed")){
		    hasnext	= true;
		    break;
		}
		
		this.tmp_scores.add(Double.parseDouble(line));
		this.tmp_end++;
	    }
	} catch (NullPointerException e){
	    System.err.println(line);
	    e.printStackTrace();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} 
	
	this.next_segment.setStart(tmp_start-1);
	this.next_segment.setEnd(tmp_end-1);		// excl.
	this.next_segment.setScores(this.tmp_scores);
	this.next_segment.setStepSize(tmp_stepsize);
	
//	if(!hasnext)
//	    System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
	return hasnext;
    }
    
    
    
    public class FixedStepSeqment {
	private int stepSize;
	private int start;
	private int end;
	private ArrayList<Double> scores;

	/**
	 * @return the stepSize
	 */
	public int getStepSize() {
	    return stepSize;
	}

	/**
	 * @param stepSize
	 *            the stepSize to set
	 */
	public void setStepSize(int stepSize) {
	    this.stepSize = stepSize;
	}

	/**
	 * Returns the Start of the segment (incl.)
	 * @return the start
	 */
	public int getStart() {
	    return start;
	}

	/**
	 * @param start
	 *            the start to set
	 */
	public void setStart(int start) {
	    this.start = start;
	}

	/**
	 * Returns the end of the segment (excl.)
	 * @return the end
	 */
	public int getEnd() {
	    return end;
	}

	/**
	 * @param end
	 *            the end to set
	 */
	public void setEnd(int end) {
	    this.end = end;
	}

	/**
	 * @return the scores
	 */
	public ArrayList<Double> getScores() {
	    return scores;
	}

	/**
	 * @param scores
	 *            the scores to set
	 */
	public void setScores(ArrayList<Double> scores) {
	    this.scores = scores;
	}
    }
    
    /**
     * Splits and parses the Exon start/end positions from databasefile.<br>
     * e.g. 1234,1267,1468
     * @param string
     * @return positions as integer
     */
    private int[] parseExonPositions(String positionstring) throws NumberFormatException{
	String[] positions	= positionstring.split(",");
	int[] pos		= new int[positions.length];
	int i;
	for(i=0;i<positions.length;i++){
	    pos[i]	= Integer.parseInt(positions[i]);
	}
	return pos;
    }
    
    /**
     * Calculates the conservation score for the given MRE bean
     * @param bean
     * @return
     */
    public float getConservation(MreBean mreBean, KnowngeneBean kgBean){
	return this.getConservation(mreBean, kgBean, 10);
    }    
    
    /**
     * Calculates the conservation score for the given MRE bean
     * @param bean
     * @param neighbors - number of neighboring bases to compare with
     * @return
     */
    public float getConservation(MreBean mreBean, KnowngeneBean kgBean, int neighbors){
	float preScore	= -1;
	float mreScore	= 0;
	float postScore = -1;
	int curr_start,curr_end = 0;
	int exon_counter	= 0;
	
	// parse exon positions
	int[] starts	= parseExonPositions(kgBean.getExonStarts());
	int[] ends	= parseExonPositions(kgBean.getExonEnds());
	if(starts.length != ends.length){
	    logger.warning("number of starts end ends are unequal.");
	    return -1;
	}
	
	// read the first elements so they are no longer empty
	while(next && (curr_segment == null)){
	    next = parseNext();
//	    System.out.println("skip empty segments");
	}
	
	// parse next until the MRE is in the current segment
	while(next && (curr_segment.start < mreBean.getChrStart())){
	    next = parseNext();
	}
	
	if(neighbors > 0)
	    preScore	= calcConsScoreForSegment(mreBean.getChrStart()-neighbors,mreBean.getChrStart());
	// is the MRE located on only one exon?
	if((mreBean.getChrEnd() - mreBean.getChrStart()) == (mreBean.getKnowngeneEnd() - mreBean.getKnowngeneStart())){
//	    preScore	= calcConsScoreForSegment(mreBean.getChrStart()-neighbors,mreBean.getChrStart());
	    mreScore	= calcConsScoreForSegment(mreBean.getChrStart(),mreBean.getChrEnd());
//	    postScore	= calcConsScoreForSegment(mreBean.getChrEnd(),mreBean.getChrEnd()+neighbors);
	}	
	else{ // splice junction spanning exons
//	    preScore	= calcConsScoreForSegment(mreBean.getChrStart()-neighbors,mreBean.getChrStart());
	    
	    // iterate over the exons of the knowngene util the start and end of the MRE is passed
	    for(int i=0;i<starts.length;i++){

		curr_end	= ends[i] - starts[i] +1;
		if(curr_end >= mreBean.getKnowngeneStart()){
			
		    // get the correct conservation segment - the end of the seqmnnet has to be greather that the exon ends[i]
		    while(next && curr_segment.end < ends[i]){
			next = parseNext();
		    }
		    exon_counter++;
		    //----| s     |----
		    if(starts[i] < mreBean.getKnowngeneStart()){
			if(ends[i] > mreBean.getKnowngeneEnd()){//----| s    e |----    should never happen!!
			    mreScore	+= calcConsScoreForSegment(mreBean.getChrStart(),mreBean.getChrEnd());
			    break;
			}
			else //----| s     |----..e
			    mreScore	+= calcConsScoreForSegment(mreBean.getChrStart(),ends[i]);
		    }// s...--|    |---- before exonstart
		    else{
			if(ends[i] > mreBean.getKnowngeneEnd()){// s...--|   e  |----
			    mreScore	+= calcConsScoreForSegment(starts[i],mreBean.getChrEnd());
			    break;
			}
			else// s...--|    |----...e
			    mreScore	+= calcConsScoreForSegment(starts[i],ends[i]);
		    }
		}
	    }
	    mreScore	= mreScore/exon_counter;
	}
	
	if(mreScore == -1)
	    return -1;
	
	if(neighbors > 0){
	    postScore	= calcConsScoreForSegment(mreBean.getChrEnd(),mreBean.getChrEnd()+neighbors);
	
	    if(preScore == -1 && postScore == -1)
		return -1;
	}
	
	float nenner	= 0;
	int segments_used = 0;
	if(preScore >= 0){
	    nenner	+= postScore;
	    segments_used++;
	}
	if(postScore >= 0){
	    nenner	+= preScore;
	    segments_used++;
	}
	
	if(nenner == 0)
	    return mreScore;
	else
	    return mreScore*segments_used/nenner;
    }
    
    /**
     * Calculates the conservation score for the given MRE bean
     * @param bean
     * @return
     * @deprecated There is an error in this implementation. The introns are not taken into account.
     */
    public float getConservation(MreBean bean){
	return this.getConservation(bean, 10);
    }
    /**
     * Calculates the conservation score for the given MRE bean
     * @param bean
     * @param neighbors - number of neighboring bases to compare with
     * @return
     * @deprecated There is an error in this implementation. The introns are not taken into account.
     */
    public float getConservation(MreBean bean, int neighbors){
	// if there is no seqment read yet
//	boolean next = true;
	int segments_used	= 0;
	
	while(next && (curr_segment == null)){
	    next = parseNext();
//	    System.out.println("skip empty segments");
	}
	// parse next until the MRE is in the current segment
	while(next && (curr_segment.start < bean.getChrStart()) && next_segment.end < bean.getChrEnd()){
	    next = parseNext();
	}
	
	float preScore	= calcConsScoreForSegment(bean.getChrStart()-neighbors,bean.getChrStart());
	float mreScore	= calcConsScoreForSegment(bean.getChrStart(),bean.getChrEnd());
	float postScore	= calcConsScoreForSegment(bean.getChrEnd(),bean.getChrEnd()+neighbors);
	
	System.out.println("preScore: "+preScore+"\nmreScore: "+mreScore+"\npostScore: "+postScore);
	
	if(preScore == -1 && postScore == -1)
	    return -1;
	
	if(mreScore == -1)
	    return -1;
	
	float nenner	= 0;
	if(preScore >= 0){
	    nenner	+= postScore;
	    segments_used++;
	}
	if(postScore >= 0){
	    nenner	+= preScore;
	    segments_used++;
	}
	
	if(nenner == 0)
	    return mreScore;
	else
	    return mreScore*segments_used/nenner;
    }
    
    /**
     * Returns the normalized (mean) score for the seqment starting at <code>start</code> (incl.) and ending 
     * in <code>end</code> (excl.). If there are missing conservation scores then they are skipped or if 
     * there is no conservation score at all, then -1 is returned. 
     * 
     * @param start - start position incl.
     * @param end - end position excl.
     * @return the mean score from <code>start</code> (incl.) - <code>end</code>(excl.) or -1 if there is no conservation score
     */
    private float calcConsScoreForSegment(int start, int end){
	float 	score 		= -1;
	float 	sumScore	= 0;
	int 	counter		= 0;
	
	// if a part of the substring is in the previous segment
	if(prev_segment != null && start < prev_segment.end){
	    int s = (start - prev_segment.start) < 0 ? 0 : start-prev_segment.start;	// 
	    int e = prev_segment.end <= end ? prev_segment.end-prev_segment.start : end-prev_segment.start;
	    for(int i=s;i<e;i++){
		sumScore+= prev_segment.scores.get(i);
		counter++;
	    }
	}
	
	// if part of the substring is in the current segment
	if((curr_segment != null) && start < curr_segment.end && end > curr_segment.start ){
	    int s = (start - curr_segment.start) < 0 ? 0 : start-curr_segment.start;	// 
	    int e = curr_segment.end <= end ? curr_segment.end-curr_segment.start : end-curr_segment.start;
	    for(int i=s;i<e;i++){
		sumScore+= curr_segment.scores.get(i);
		counter++;
	    }
	}
	
	
	// if part of the substring is in the next seqment
	if((next_segment != null) && next_segment.start < end){
	    int s = (start - next_segment.start) < 0 ? 0 : start-next_segment.start;	// 
	    int e = next_segment.end <= end ? next_segment.end-next_segment.start : end-next_segment.start;
	    for(int i=s;i<e;i++){
		sumScore+= next_segment.scores.get(i);
		counter++;
	    }
	    
	}
	if(counter == 0)
	    return -1;
	
	score = sumScore/counter;
	
	return score;
    }
    

}
