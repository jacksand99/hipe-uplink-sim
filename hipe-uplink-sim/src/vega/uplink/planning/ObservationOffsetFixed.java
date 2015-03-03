package vega.uplink.planning;
import java.util.Date;

import vega.uplink.DateUtil;
import vega.uplink.pointing.PointingBlock;
import vega.uplink.pointing.PointingElement;
import vega.uplink.pointing.PtrParameters.Offset.OffsetFixed;
public class ObservationOffsetFixed extends OffsetFixed implements ObservationOffset{
	private ObservationEvent startTimeEvent;
	private  long startTimeDelta;
	private Observation parent; 
	
	public ObservationOffsetFixed(PointingElement pm){
		super(pm);
	}
	
	public ObservationOffsetFixed copy(){
		ObservationOffsetFixed result = new ObservationOffsetFixed(parent,startTimeEvent,startTimeDelta,super.copy());
		return result;

		/*ObservationOffsetFixed result = new ObservationOffsetFixed(super.copy());
		return result;*/
	}

	
	/**
	 * OffsetFixed that is driven by an Observation event instead of times for start time of the offset
	 * @param parent
	 * @param startEvent
	 * @param startDelta
	 * @param org
	 */
	public ObservationOffsetFixed (Observation parent,ObservationEvent startEvent,long startDelta,OffsetFixed org){
		super(org);
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
		return DateUtil.dateToZulu(getStartDate());
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
	public void setParent(Observation obs) {
		// TODO Auto-generated method stub
		parent=obs;
		
	}


}
