package vega.uplink.planning;
import java.text.ParseException;
import java.util.Date;

import vega.uplink.pointing.PointingBlock;
import vega.uplink.pointing.PointingElement;
import vega.uplink.pointing.PtrParameters.Offset.OffsetCustom;
public class ObservationOffsetCustom extends OffsetCustom{
	private ObservationEvent startTimeEvent;
	private  long startTimeDelta;
	private Observation parent; 
	
	public ObservationOffsetCustom (Observation parent,ObservationEvent startEvent,long startDelta,OffsetCustom org){
		//org.gets
		super(org);
		this.getChild("startTime").setValue(startEvent.getName()+ObservationUtil.getOffset(startDelta));
		startTimeEvent=startEvent;
		startTimeDelta=startDelta;
		this.parent=parent;
	}
	public ObservationEvent getStartEvent(){
		return startTimeEvent;
	}
	

	
	public long getStartDelta(){
		return startTimeDelta;
	}
	

	public Date getStartDate() {
		return new Date(parent.getDateForEvent(startTimeEvent).getTime()+startTimeDelta);
	}
	public String getStartTime() {
		return PointingBlock.dateToZulu(getStartDate());
		// TODO Auto-generated method stub
		//return null;
	}


	public void setStartTime(ObservationEvent event,long delta){
		startTimeEvent=event;
		startTimeDelta=delta;

	}

	public void setStartTime(Date time) {
		long oldStartDate;
		
		oldStartDate = this.getStartDate().getTime();
		
		long newTime=time.getTime();
		long delta = newTime-oldStartDate;
		parent.shiftEvent(this.getStartEvent(), delta);
		//throw new IllegalArgumentException("Can not set the start time to an ObservationPointingBlock");
		// TODO Auto-generated method stub
		
	}

	public void addChild(PointingElement element){
		/*if (element.getName().equals("startTime")){
			Thread.dumpStack();
		}*/
		super.addChild(element);
	}
	public String toXml(int indent){
		super.setStartTime(getStartTime());
		//super.setEndTime(getEndTime());
		return super.toXml(indent);
	}
	
	public String toObsXml(int indent){
		PointingElement tempElement=new PointingElement(this);
		this.getChild("startTime").setValue(getStartEvent().getName()+ObservationUtil.getOffset(getStartDelta()));
		//this.getChild("endTime").setValue(getEndEvent().getName()+ObservationUtil.getOffset(getEndDelta()));

		return tempElement.toXml(indent);
		
	}



}
