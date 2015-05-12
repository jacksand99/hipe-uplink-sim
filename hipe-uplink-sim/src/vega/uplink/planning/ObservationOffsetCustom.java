package vega.uplink.planning;
import java.util.Date;

import vega.uplink.DateUtil;
import vega.uplink.pointing.PointingBlock;
import vega.uplink.pointing.PointingElement;
import vega.uplink.pointing.PtrParameters.TargetDir;
import vega.uplink.pointing.PtrParameters.Offset.OffsetCustom;
public class ObservationOffsetCustom extends OffsetCustom implements ObservationOffset{
	private ObservationEvent startTimeEvent;
	private  long startTimeDelta;
	private Observation parent; 
	
	/**
	 * Offsetcustom that is driven by an Observation event instead of times for start time of the offset
	 * @param parent
	 * @param startEvent
	 * @param startDelta
	 * @param org
	 */
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
		return DateUtil.dateToZulu(getStartDate());
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
		
	}

	public void addChild(PointingElement element){
		super.addChild(element);
	}
	public String toXml(int indent){
		super.setStartTime(getStartTime());
		return super.toXml(indent);
	}
	
	public String toObsXml(int indent){
		PointingElement tempElement=new PointingElement(this);
		this.getChild("startTime").setValue(getStartEvent().getName()+ObservationUtil.getOffset(getStartDelta()));
		return tempElement.toXml(indent);
		
	}
	
	public ObservationOffsetCustom(PointingElement pm){
		super(pm);
	}
	
	public ObservationOffsetCustom copy(){
		ObservationOffsetCustom result = new ObservationOffsetCustom(parent,startTimeEvent,startTimeDelta,super.copy());
		return result;
	}
	@Override
	public void setParent(Observation obs) {
		parent=obs;
		
	}



}
