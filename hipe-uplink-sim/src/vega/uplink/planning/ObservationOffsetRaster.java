package vega.uplink.planning;
import java.util.Date;

import vega.uplink.DateUtil;
import vega.uplink.pointing.PointingBlock;
import vega.uplink.pointing.PointingElement;
import vega.uplink.pointing.PtrParameters.Offset.OffsetRaster;
public class ObservationOffsetRaster extends OffsetRaster implements ObservationOffset{
	private ObservationEvent startTimeEvent;
	private  long startTimeDelta;
	private Observation parent; 
	
	public ObservationOffsetRaster(PointingElement pm){
		super(pm);
	}
	
	public ObservationOffsetRaster copy(){
		ObservationOffsetRaster result = new ObservationOffsetRaster(parent,startTimeEvent,startTimeDelta,super.copy());
		return result;

		/*ObservationOffsetRaster result = new ObservationOffsetRaster(super.copy());
		return result;*/
	}

	/**
	 * OffsetRaster that is driven by an Observation event instead of times for start time of the offset
	 * @param parent
	 * @param startEvent
	 * @param startDelta
	 * @param org
	 */
	public ObservationOffsetRaster (Observation parent,ObservationEvent startEvent,long startDelta,OffsetRaster org){
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