package vega.hipe.pds;

public class InvalidValueException extends Exception {

	/**
	 * Exception to be thrown by the PDS hipe package whenever an invalid value is passed to a function
	 */
	private static final long serialVersionUID = 1L;
	
	public InvalidValueException(String message){
		super(message);
	}

}
