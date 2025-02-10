package vega.uplink.pointing.net;

public class AttitudeGeneratorException extends Exception {
	String message;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public AttitudeGeneratorException(String message){
		super();
		this.message=message;
	}
	public AttitudeGeneratorException(String message,Exception cause){
		this(message);
		this.initCause(cause);
	}

	public String getMessage(){
		return message;
	}

}
