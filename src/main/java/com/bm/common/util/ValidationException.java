package com.bm.common.util;

/**
 * Wird geworfen wenn beim validieren etwas schief geht.
 * 
 * @author Daniel
 * 
 */
public class ValidationException extends Exception {

	private static final long serialVersionUID = 1L;

	private final String reason;

	/**
	 * Constructor.
	 * 
	 * @param reason
	 *            der grund warum was schiefgegengen ist.
	 */
	public ValidationException(String reason) {
		super(reason);
		this.reason = reason;

	}

	/**
	 * Gets the reason.
	 * 
	 * @return the reason
	 */
	public String getReason() {
		return reason;
	}

}
