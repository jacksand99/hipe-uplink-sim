package vega.uplink.pointing;
import herschel.ia.dataset.Dataset;
import herschel.ia.dataset.Product;
import herschel.ia.dataset.StringParameter;
import herschel.share.fltdyn.time.FineTime;

import java.util.Iterator;
import java.util.Date;


import vega.uplink.Properties;
/**
 * A class that manages spacecraft event timing and metadata for mission operations.
 * This class extends Product to handle spacecraft events with their associated temporal data
 * and metadata. It provides functionality for storing, retrieving, and managing events
 * with precise timing requirements.
 * 
 * The class supports:
 * - Event storage and retrieval with timestamp management
 * - Validity period tracking for events
 * - Spacecraft and ICD version metadata
 * - Event filtering by time ranges and types
 * - XML serialization for data exchange
 * - Automatic collision handling for events at the same timestamp
 * 
 * Events are stored with metadata and can be retrieved using various criteria such as
 * time ranges, specific timestamps, or event types. All temporal operations use the
 * FineTime class for precise time handling.
 *
 * @author jarenas
 */
public class Evtm extends Product{
    /**
     * XML tag constants used for EVTM file parsing and generation.
     * These constants define the standard tag names used in the EVTM XML structure.
     */

    /** XML tag for the root event file element */
    public static String EVENTFILE_TAG = "eventfile";

    /** XML tag for the events container element */
    public static String EVENTS_TAG = "events";

    /** XML tag for the header section */
    public static String HEADER_TAG = "header";

    /** XML tag for spacecraft identification */
    public static String SPACECRAFT_TAG = "spacecraft";

    /** XML tag for ICD (Interface Control Document) version */
    public static String ICDVERION_TAG = "icd_version";

    /** XML tag for event generation timestamp */
    public static String GENTIME_TAG = "gen_time";

    /** XML tag for validity period start time */
    public static String VALIDITY_START_TAG = "validity_start";

    /** XML tag for validity period end time */
    public static String VALIDITY_END_TAG = "validity_end";
	/**
	 * Constructs a new EVTM (Event Time Management) instance with full parameter specification.
	 * This constructor initializes an EVTM by:
	 * - Setting a unique name based on current timestamp
	 * - Setting the path to user's home directory
	 * - Setting the type as "EVTM"
	 * - Initializing timing information using FineTime
	 * - Setting spacecraft and ICD version metadata
	 *
	 * @param evtmGenerationTime The timestamp when this EVTM was generated
	 * @param evtmValidityStart The timestamp when this EVTM becomes valid
	 * @param evtmValidityEnd The timestamp when this EVTM's validity expires
	 * @param evtmSpacecraft The spacecraft identifier (e.g., "SOLO", "ROS")
	 * @param evtmIcdVersion The Interface Control Document version being used
	 * @see FineTime
	 * @see StringParameter
	 */

	public Evtm(Date evtmGenerationTime,Date evtmValidityStart,Date evtmValidityEnd,String evtmSpacecraft,String evtmIcdVersion){
		super();
		setName(""+new java.util.Date().getTime());
		setPath(Properties.getProperty("user.home"));
		this.setType("EVTM");

		this.setCreationDate(new FineTime(evtmGenerationTime));
		this.setStartDate(new FineTime(evtmValidityStart));
		this.setEndDate(new FineTime(evtmValidityEnd));
		getMeta().set(SPACECRAFT_TAG, new StringParameter(evtmSpacecraft));
		getMeta().set("icdVersion", new StringParameter(evtmIcdVersion));
	}
	/**
	 * Sets the name of this EVTM instance in the metadata.
	 * The name is stored as a StringParameter in the metadata collection
	 * using "name" as the key.
	 *
	 * @param name The name to be assigned to this EVTM instance
	 * @see StringParameter
	 */
	public void setName(String name){
	    getMeta().set("name", new StringParameter(name));
	}

    /**
     * Sets the path for this EVTM instance in the metadata.
     * The path is stored as a StringParameter in the metadata collection
     * using "path" as the key.
     *
     * @param path The file system path to be assigned to this EVTM instance
     * @see StringParameter
     */
	public void setPath(String path){
		getMeta().set("path", new StringParameter(path));
	}
	/**
	 * Retrieves the name of this EVTM instance from the metadata.
	 * The name is stored as a StringParameter in the metadata collection
	 * with "name" as the key.
	 *
	 * @return The name of this EVTM instance as a String
	 * @see StringParameter
	 */
	public String getName(){
	    return (String) getMeta().get("name").getValue();
	}
    /**
     * Retrieves the path of this EVTM instance from the metadata.
     * The path is stored as a StringParameter in the metadata collection
     * with "path" as the key.
     *
     * @return The file system path of this EVTM instance as a String
     * @see StringParameter
     */

	public String getPath(){
		return (String) getMeta().get("path").getValue();
	}
	/**
	 * Constructs a new EVTM instance with specified timing parameters and ICD version.
	 * Uses "SOLO" as the default spacecraft identifier.
	 *
	 * @param evtmGenerationTime The timestamp when this EVTM was generated
	 * @param evtmValidityStart The timestamp when this EVTM becomes valid
	 * @param evtmValidityEnd The timestamp when this EVTM's validity expires
	 * @param evtmIcdVersion The Interface Control Document version being used
	 */
	public Evtm(Date evtmGenerationTime, Date evtmValidityStart, Date evtmValidityEnd, String evtmIcdVersion){
	    this(evtmGenerationTime, evtmValidityStart, evtmValidityEnd, "SOLO", evtmIcdVersion);
	}

	/**
	 * Constructs a new EVTM instance using current time as generation time.
	 * Uses "SOLO" as the default spacecraft identifier.
	 *
	 * @param evtmValidityStart The timestamp when this EVTM becomes valid
	 * @param evtmValidityEnd The timestamp when this EVTM's validity expires
	 * @param evtmIcdVersion The Interface Control Document version being used
	 */
	public Evtm(Date evtmValidityStart, Date evtmValidityEnd, String evtmIcdVersion){
	    this(new java.util.Date(), evtmValidityStart, evtmValidityEnd, "SOLO", evtmIcdVersion);
	}

	/**
	 * Constructs a new EVTM instance using current time as generation time
	 * and default ICD version "SGS-FD ICD 2.7".
	 * Uses "SOLO" as the default spacecraft identifier.
	 *
	 * @param evtmValidityStart The timestamp when this EVTM becomes valid
	 * @param evtmValidityEnd The timestamp when this EVTM's validity expires
	 */
	public Evtm(Date evtmValidityStart, Date evtmValidityEnd){
	    this(new java.util.Date(), evtmValidityStart, evtmValidityEnd, "SOLO", "SGS-FD ICD 2.7");
	}

	/**
	 * Default constructor that creates a new EVTM instance with:
	 * - Current time for generation time, validity start, and validity end
	 * - "ROS" as the spacecraft identifier
	 * - "SGS-FD ICD 2.7" as the default ICD version
	 */
	public Evtm(){
	    this(new java.util.Date(), new java.util.Date(), new java.util.Date(), "ROS", "SGS-FD ICD 2.7");
	}
	/**
	 * Retrieves the generation time of this EVTM instance.
	 *
	 * @return A Date object representing when this EVTM was generated
	 */
	public Date getGenerationTime() {
	    return this.getCreationDate().toDate();
	}

	/**
	 * Sets the generation time for this EVTM instance.
	 *
	 * @param evtmGenerationTime The Date object representing when this EVTM was generated
	 */
	public void setGenerationTime(Date evtmGenerationTime) {
	    this.setCreationDate(new FineTime(evtmGenerationTime));
	}

	/**
	 * Retrieves the start time of the validity period.
	 *
	 * @return A Date object representing when this EVTM becomes valid
	 */
	public Date getValidityStart() {
	    return this.getStartDate().toDate();
	}

	/**
	 * Sets the start time of the validity period.
	 *
	 * @param evtmValidityStart The Date object representing when this EVTM becomes valid
	 */
	public void setValidityStart(Date evtmValidityStart) {
	    this.setStartDate(new FineTime(evtmValidityStart));
	}

	/**
	 * Sets the end time of the validity period.
	 *
	 * @param evtmValidityEnd The Date object representing when this EVTM's validity expires
	 */
	public void setValidityEnd(Date evtmValidityEnd) {
	    this.setEndDate(new FineTime(evtmValidityEnd));
	}

	/**
	 * Sets the spacecraft identifier in the metadata.
	 *
	 * @param evtmSpacecraft The spacecraft identifier to be set
	 */
	public void setSpacecraft(String evtmSpacecraft) {
	    getMeta().set(SPACECRAFT_TAG, new StringParameter(evtmSpacecraft));
	}

	/**
	 * Sets the ICD version in the metadata.
	 *
	 * @param evtmIcdVersion The ICD version to be set
	 */
	public void setIcdVersion(String evtmIcdVersion) {
	    getMeta().set("icdVersion", new StringParameter(evtmIcdVersion));
	}

	/**
	 * Retrieves the end time of the validity period.
	 *
	 * @return A Date object representing when this EVTM's validity expires
	 */
	public Date getValidityEnd() {
	    return this.getEndDate().toDate();
	}

	/**
	 * Retrieves the spacecraft identifier from the metadata.
	 *
	 * @return The spacecraft identifier as a String
	 */
	public String getSpacecraft() {
	    return (String) getMeta().get(SPACECRAFT_TAG).getValue();
	}

	/**
	 * Retrieves the ICD version from the metadata.
	 *
	 * @return The ICD version as a String
	 */
	public String getIcdVersion() {
	    return (String) getMeta().get("icdVersion").getValue();
	}	
	
	/**
	 * Adds a new event to the EVTM timeline.
	 * Delegates to addEventInternal for handling potential time collisions.
	 *
	 * @param event The EvtmEvent to be added to the timeline
	 */
	public void addEvent(EvtmEvent event) {
	    this.addEventInternal(event.getTime(), event);
	}

	/**
	 * Internal method to handle event addition with collision resolution.
	 * If an event already exists at the specified time, recursively attempts
	 * to add the event at time + 1 millisecond until a free slot is found.
	 *
	 * @param time The timestamp for the event
	 * @param event The EvtmEvent to be added
	 */
	private void addEventInternal(Date time, EvtmEvent event) {
	    if (this.getEvent(time) != null) {
	        this.addEventInternal(new Date(time.getTime() + 1), event);
	        return;
	    }
	    this.set(EvtmEvent.dateToZulu(time), event);
	}

	/**
	 * Retrieves an event at the specified time.
	 *
	 * @param time The timestamp to look up
	 * @return The EvtmEvent at the specified time, or null if none exists
	 */
	public EvtmEvent getEvent(Date time) {
	    return (EvtmEvent)get(EvtmEvent.dateToZulu(time));
	}

	/**
	 * Retrieves all events that occur at the specified time.
	 * Events are sorted in their natural order before being returned.
	 *
	 * @param time The timestamp to look up
	 * @return An array of EvtmEvents occurring at the specified time
	 */
	public EvtmEvent[] getEvents(Date time) {
	    java.util.Vector<EvtmEvent> vEvents = new java.util.Vector<EvtmEvent>();
	    Iterator<Dataset> it = this.getSets().values().iterator();
	    while (it.hasNext()) {
	        EvtmEvent ev = (EvtmEvent) it.next();
	        if (ev.getTime().equals(time)) vEvents.add(ev);
	    }
	    EvtmEvent[] result = new EvtmEvent[vEvents.size()];
	    vEvents.toArray(result);
	    java.util.Arrays.sort(result);
	    return result;
	}
	/**
	 * Retrieves all events in the EVTM timeline.
	 * Returns events sorted in their natural order.
	 *
	 * @return An array of all EvtmEvents in chronological order
	 */
	public EvtmEvent[] getAllEvents() {
	    EvtmEvent[] result = new EvtmEvent[this.getSets().size()];
	    this.getSets().values().toArray(result);
	    java.util.Arrays.sort(result);
	    return result;
	}

	/**
	 * Retrieves all events within a specified time range.
	 * Returns events that occur after the start date and before the end date,
	 * sorted in their natural order.
	 *
	 * @param start The beginning of the time range (exclusive)
	 * @param end The end of the time range (exclusive)
	 * @return An array of EvtmEvents within the specified time range
	 */
	public EvtmEvent[] getAllEventsBetween(Date start, Date end) {
	    java.util.Vector<EvtmEvent> vEvents = new java.util.Vector<EvtmEvent>();
	    Iterator<Dataset> it = this.getSets().values().iterator();
	    while (it.hasNext()) {
	        EvtmEvent ev = (EvtmEvent) it.next();
	        if (ev.getTime().after(start) && ev.getTime().before(end)) {
	            vEvents.add(ev);
	        }
	    }
	    EvtmEvent[] result = new EvtmEvent[vEvents.size()];
	    vEvents.toArray(result);
	    java.util.Arrays.sort(result);
	    return result;
	}

	/**
	 * Creates a new EVTM instance containing only events within the specified time range.
	 *
	 * @param startDate The beginning of the time range
	 * @param endDate The end of the time range
	 * @return A new Evtm instance containing only the events between startDate and endDate
	 */
	public Evtm getSubEventsFile(Date startDate, Date endDate) {
	    Evtm result = new Evtm(startDate, endDate);
	    result.addEvents(this.getAllEventsBetween(startDate, endDate));    
	    return result;
	}

	/**
	 * Adds multiple events to the EVTM timeline.
	 *
	 * @param events An array of EvtmEvents to be added
	 */
	public void addEvents(EvtmEvent[] events) {
	    for (int i = 0; i < events.length; i++) {
	        this.addEvent(events[i]);
	    }
	}

	/**
	 * Checks if a specific event exists in the EVTM timeline.
	 *
	 * @param e The EvtmEvent to search for
	 * @return true if the event exists in the timeline, false otherwise
	 */
	public boolean contains(EvtmEvent e) {
	    boolean result = false;
	    EvtmEvent[] ae = this.getEvents(e.getTime());
	    for (int i = 0; i < ae.length; i++) {
	        if (ae[i].equals(e)) {
	            result = true;
	        }
	    }
	    return result;
	}

	/**
	 * Retrieves all events of a specific type from the EVTM timeline.
	 *
	 * @param type The type of events to retrieve
	 * @return An array of EvtmEvents matching the specified type
	 */
	public EvtmEvent[] getEventsByType(String type) {
	    java.util.Vector<EvtmEvent> resultVector = new java.util.Vector<EvtmEvent>();
	    EvtmEvent[] allEvents = getAllEvents();
	    for (int i = 0; i < allEvents.length; i++) {
	        if (allEvents[i].getType().equals(type)) {
	            resultVector.add(allEvents[i]);
	        }
	    }
	    EvtmEvent[] result = new EvtmEvent[resultVector.size()];
	    resultVector.toArray(result);
	    return result;
	}
/**
 * Converts the EVTM instance to its XML string representation.
 * Generates an XML document containing all events and their metadata in the following format:
 * - XML declaration with UTF-8 encoding
 * - Root element with XML schema and namespace declarations
 * - Header section with format version, spacecraft, ICD version, and timing information
 * - Events section containing all events in chronological order
 * 
 * The method handles event ID counters to manage multiple occurrences of the same event ID.
 *
 * @return A String containing the XML representation of the EVTM instance
 */
	public String toString(){
		java.util.HashMap<String,Integer> counters=new java.util.HashMap<String,Integer>();
		String result="";
		result=result+"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
		result=result+"<"+EVENTFILE_TAG+" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://esa.esoc.events rosetta_event_definitions.xsd\" xmlns=\"http://esa.esoc.events\" xmlns:ems=\"http://esa.esoc.ems\">\n";
		result=result+"\t<"+HEADER_TAG+" format_version=\"1\" "+SPACECRAFT_TAG+"=\""+this.getSpacecraft()+"\" "+ICDVERION_TAG+"=\""+this.getIcdVersion()+"\" "+GENTIME_TAG+"=\""+EvtmEvent.dateToZulu(this.getGenerationTime())+"\" "+VALIDITY_START_TAG+"=\""+EvtmEvent.dateToZulu(this.getValidityStart())+"\" "+VALIDITY_END_TAG+"=\""+EvtmEvent.dateToZulu(this.getValidityEnd())+"\"/>\n";
		result=result+"\t<"+EVENTS_TAG+">\n";
		EvtmEvent[] allEvents=this.getAllEvents();
		for (int i=0;i<allEvents.length;i++){
			String key=allEvents[i].getId();
			Integer temp=counters.get(key);
			if (temp==null) temp=1;
			
			result=result+"\t\t"+allEvents[i].toString(temp);
			counters.put(key, temp+1);
		}
		result=result+"\t</"+EVENTS_TAG+">\n";
		result=result+"</"+EVENTFILE_TAG+">";
		return result;
		
	}
}
