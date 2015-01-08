/**
 * 
 */
package mirnator.exception;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;

/**
 * Signals that an attempt to open the file denoted by a specified pathname has failed.
 *
 * <p>
 * This exception will be thrown by the {@link FileInputStream}, {@link FileOutputStream}, and {@link RandomAccessFile}
 * constructors when a file with the specified pathname does not exist. It will also be thrown by these constructors if
 * the file does exist but for some reason is inaccessible.
 * 
 * @author mjaeger
 *
 */
public class MreFileNotFoundException extends FileNotFoundException {

	/**
     * 
     */
	private static final long serialVersionUID = -4211443781832813675L;

	/**
	 * Constructs a <code>MreFileNotFoundException</code> with <code>null</code> as its error detail message.
	 */
	public MreFileNotFoundException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Constructs a <code>MreFileNotFoundException</code> with the specified detail message. The string <code>s</code>
	 * can be retrieved later by the <code>{@link java.lang.Throwable#getMessage}</code> method of class
	 * <code>java.lang.Throwable</code>.
	 *
	 * @param s
	 *            the detail message.
	 */
	public MreFileNotFoundException(String s) {
		super(s);
		// TODO Auto-generated constructor stub
	}

}
