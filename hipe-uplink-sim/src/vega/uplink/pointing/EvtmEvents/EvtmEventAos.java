package vega.uplink.pointing.EvtmEvents;

import vega.uplink.pointing.EvtmEvent;

public class EvtmEventAos extends EvtmEventSignal {
	public EvtmEventAos(String eventId,java.util.Date eventTime,long eventDuration,String eventEms_station,String eventCriteria,int eventElevation,long eventEms_rtlt){
		super(EvtmEvent.EVENT_TYPE_AOS,eventId,eventTime,eventDuration,eventEms_station,eventCriteria,eventElevation,eventEms_rtlt);
	}

}
