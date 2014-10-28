package vega.uplink.commanding;

import java.text.ParseException;



public interface SequenceInterface {
	public String getName();
	
	public String getUniqueID();
	
	public String getFlag();
	
	public char getSource();
	
	public char getDestination();
	
	public java.util.Date getExecutionDate();
	
	
	public String getExecutionTime();
	
	public Parameter getParameter(String parameterName);
	
	public void setEngParameter(String paramName,String value);
	public Parameter[] getParameters();
	
	public SequenceProfile[] getProfiles();
	
	public void setName(String sequenceName);
	
	public void setUniqueID(String sequenceID);
	
	public void setFlag (String sequenceFlag);
	
	public void setSource(char sequenceSource);
	
	public void setDestination(char sequenceDestination);
	
	public void setExecutionDate(java.util.Date date);
	
	public void setExecutionTime(String time) throws ParseException;
	

	public void setParameters(Parameter[] sequenceParameters);
	
	public void setProfiles(SequenceProfile[] sequenceProfiles);
	
	public void addParameter(Parameter sequenceParameter);
	
	public void addProfile(SequenceProfile sequenceProfile);
	
	public String getInstrumentName();
	

	
	public String toXml(int indent);
	
	public String toXml();
	


	

}
