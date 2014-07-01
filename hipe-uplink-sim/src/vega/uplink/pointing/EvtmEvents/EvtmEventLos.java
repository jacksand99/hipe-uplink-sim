package vega.uplink.pointing.EvtmEvents;

import vega.uplink.pointing.EvtmEvent;

public class EvtmEventLos extends EvtmEventSignal {
	public EvtmEventLos(String eventId,java.util.Date eventTime,long eventDuration,String eventEms_station,String eventCriteria,int eventElevation,long eventEms_rtlt){
		super(EvtmEvent.EVENT_TYPE_LOS,eventId,eventTime,eventDuration,eventEms_station,eventCriteria,eventElevation,eventEms_rtlt);
	}

}
