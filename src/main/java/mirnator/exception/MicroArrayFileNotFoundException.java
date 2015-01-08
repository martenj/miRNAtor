package mirnator.exception;

import java.io.FileNotFoundException;

public class MicroArrayFileNotFoundException extends FileNotFoundException {

	/**
     * 
     */
	private static final long serialVersionUID = 8039899400966970693L;

	public MicroArrayFileNotFoundException() {
		// TODO Auto-generated constructor stub
	}

	public MicroArrayFileNotFoundException(String s) {
		super(s);
		System.err.println("Sorry tro hear you provided a illegal or no microarray file");
	}

}
