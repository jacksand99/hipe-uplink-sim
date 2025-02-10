package vega.uplink.commanding;

import java.text.ParseException;



/**
 * Interface for all classes containing TC sequences
 * @author jarenas
 *
 */
public interface SequenceInterface {
	/**
	 * Get the name of the sequence
	 * @return
	 */
	public String getName();
	
	/**
	 * Get the unique ID of the sequence
	 * @return
	 */
	public String getUniqueID();
	
	/**
	 * Get the flag of the sequence
	 * @return
	 */
	public String getFlag();
	
	/**
	 * Get the source of the sequence
	 * @return
	 */
	public String getSource();
	
	/**
	 * Get the destination of the sequence
	 * @return
	 */
	public char getDestination();
	
	/**
	 * Get the execution date of the sequence
	 * @return
	 */
	public java.util.Date getExecutionDate();
	
	
	/**
	 * Get the execution date of the sequence as string
	 * @return
	 */
	public String getExecutionTime();
	
	/**
	 * Get the parameter with the given name
	 * @param parameterName
	 * @return
	 */
	public Parameter getParameter(String parameterName);
	
	/**
	 * set an enginnering parameter with the given value
	 * @param paramName
	 * @param value
	 */
	public void setEngParameter(String paramName,String value);
	/**
	 * Get all parameters in this sequence
	 * @return
	 */
	public Parameter[] getParameters();
	
	/**
	 * Get the profiles (power and datarate) in thsi sequence
	 * @return
	 */
	public SequenceProfile[] getProfiles();
	
	/**
	 * set the name of this sequence
	 * @param sequenceName
	 */
	public void setName(String sequenceName);
	
	/**
	 * set the unique id of this sequence
	 * @param sequenceID
	 */
	public void setUniqueID(String sequenceID);
	
	/**
	 * set the sequence flag
	 * @param sequenceFlag
	 */
	public void setFlag (String sequenceFlag);
	
	/**
	 * set the sequence source
	 * @param sequenceSource
	 */
	public void setSource(char sequenceSource);
	
	/**
	 * set the sequence destination
	 * @param sequenceDestination
	 */
	public void setDestination(char sequenceDestination);
	
	/**
	 * set the execution date of the sequence
	 * @param date
	 */
	public void setExecutionDate(java.util.Date date);
	
	/**
	 * set the execution date of the sequence as string
	 * @param time
	 * @throws ParseException
	 */
	public void setExecutionTime(String time) throws ParseException;
	

	/**
	 * set the parameters of the sequence
	 * @param sequenceParameters
	 */
	public void setParameters(Parameter[] sequenceParameters);
	
	/**
	 * set the sequence profiles
	 * @param sequenceProfiles
	 */
	public void setProfiles(SequenceProfile[] sequenceProfiles);
	
	/**
	 * Add a parameter to the sequence
	 * @param sequenceParameter
	 */
	public void addParameter(Parameter sequenceParameter);
	
	/**
	 * Add a profile to the sequence
	 * @param sequenceProfile
	 */
	public void addProfile(SequenceProfile sequenceProfile);
	
	/**
	 * Get the instrument name of this sequence
	 * @return
	 */
	public String getInstrumentName();
	

	
	/**
	 * Get a XML representation of this sequence with a specific indentation
	 * @param indent
	 * @return
	 */
	public String toXml(int indent);
	
	/**
	 * Get a XML representation of this sequence
	 * @return
	 */
	public String toXml();
	


	

}
