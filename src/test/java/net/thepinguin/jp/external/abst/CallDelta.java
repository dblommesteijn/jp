package net.thepinguin.jp.external.abst;

/**
 * CallDelta returns an object with information about last call
 * - this object is provisioned exclusively by AbstractCommandTest.callMain()
 */
public class CallDelta {
	
	private String _stdOutStr = "";
	private String _stdErrStr = "";
	private int _status = 0;

	public CallDelta() {
	}

	/**
	 * Get stdout as string from latest call
	 * @return string of stdout
	 */
	public String getStdOutStr() {
		return _stdOutStr;
	}

	/**
	 * Get stderr as string from latest call
	 * @return string of stderr
	 */
	public String getStdErrStr() {
		return _stdErrStr;
	}

	/**
	 * Set stdout value
	 * @param stdOutStr value of stdout
	 */
	public void setStdOut(String stdOutStr) {
		_stdOutStr = stdOutStr;
	}

	/**
	 * Set stderr value
	 * @param stdErrStr value of stderr
	 */
	public void setStdErr(String stdErrStr) {
		_stdErrStr = stdErrStr;
	}

	/**
	 * Set exit status
	 * @param status exit status
	 */
	public void setStatus(int status) {
		_status = status;
	}
	
	/**
	 * Get exit status
	 * @return exit status
	 */
	public int getStatus() {
		return _status;
	}
}
