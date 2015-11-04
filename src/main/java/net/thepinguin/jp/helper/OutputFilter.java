package net.thepinguin.jp.helper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

/**
 * OutputFilter will capture stdout/ stderr outputstreams to a string. 
 * 
 * @author Dennis Blommesteijn
 * @since 11-2015
 */
public class OutputFilter {
	
	private PrintStream _stdout;
	private ByteArrayOutputStream _newByteOut;
	private PrintStream _stderr;
	private ByteArrayOutputStream _newByteErr;

	/**
	 * Start capturing stdout/ stderr streams into an outputstream
	 *   - Nothing will be printed to console anymore;
	 *   - getStdOutStr and getStdErrStr will contain console output.
	 * @throws IOException
	 */
	public void capture() {
		try {
		if(_stdout == null) {
			System.out.flush();
			_stdout = System.out;
		} else {
			_newByteOut.flush();
		}
		if(_stderr == null) {
			System.err.flush();
			_stderr = System.err;
		} else {
			_newByteErr.flush();
		}
		this.clear();
		} catch(IOException e) {
			// something is wrong
		}
	}

	/**
	 * Reset stdout/ stderr to system defaults
	 */
	public void release() {
		// re-enable stdout, stderr
		System.setOut(_stdout);
		System.setErr(_stderr);
	}
	
	/**
	 * Clear stdout/ stderr output streams
	 * @throws IOException
	 */
	public void clear() throws IOException {
		this.clearStdOut();
		this.clearStdErr();
	}
	
	/**
	 * Reassign new outputstream to stdout
	 */
	private void clearStdOut() {
		_newByteOut = new ByteArrayOutputStream();
		System.setOut(new PrintStream(_newByteOut));
	}
	
	/**
	 * Reassign new outputstream to stderr
	 */
	private void clearStdErr(){
		_newByteErr = new ByteArrayOutputStream();
		System.setErr(new PrintStream(_newByteErr));
	}

	/**
	 * Get stdout as a string, and clear stream
	 * @return all that is written to stdout (between capture/ release)
	 */
	public String getStdOutStr() {
		String ret = _newByteOut.toString();
		// create new stdout buffer
		this.clearStdOut();
		return ret;
	}
	
	/**
	 * Get stderr as a string, and clear stream
	 * @return all that is written to stderr (between capture/ release)
	 */
	public String getStdErrStr() {
		String ret = _newByteErr.toString();
		// create new stderr buffer
		this.clearStdErr();
		return ret;
	}
	

}
