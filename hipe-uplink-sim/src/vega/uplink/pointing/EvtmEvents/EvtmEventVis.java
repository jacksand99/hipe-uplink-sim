package vega.uplink.pointing.EvtmEvents;

import vega.uplink.pointing.EvtmEvent;

import java.util.Date;

//import rosetta.uplink.EvtmEvent;

public class EvtmEventVis extends EvtmEvent {
	public EvtmEventVis(String eventId,Date eventTime,long eventDuration){
		super(EvtmEvent.EVENT_TYPE_VIS,eventId,eventTime,eventDuration);
	}
}
