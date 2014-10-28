package vega.uplink.planning;

import herschel.share.interpreter.InterpreterUtil;

import java.text.ParseException;
import java.util.Date;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import vega.uplink.pointing.PointingAttitude;
import vega.uplink.pointing.PointingBlock;
import vega.uplink.pointing.PointingElement;
import vega.uplink.pointing.PointingMetadata;
import vega.uplink.pointing.PtrParameters.OffsetRefAxis;
import vega.uplink.pointing.PtrParameters.Offset.*;



//public class ObservationPointingBlock implements PointingBlockInterface{
public class ObservationPointingBlock extends PointingBlock{

	private ObservationEvent startTimeEvent;
	private ObservationEvent endTimeEvent;
	private  long startTimeDelta;
	private  long endTimeDelta;
	private Observation parent; 
	
	public ObservationPointingBlock(String type,Observation parent,ObservationEvent startEvent,long startDelta,ObservationEvent endEvent,long endDelta){
		super(type,new Date(parent.getDateForEvent(startEvent).getTime()+startDelta),new Date(parent.getDateForEvent(endEvent).getTime()+endDelta));
		this.parent=parent;
		startTimeEvent=startEvent;
		endTimeEvent=endEvent;
		startTimeDelta=startDelta;
		endTimeDelta=endDelta;
	}
	public ObservationPointingBlock copy(){
		ObservationPointingBlock result = new ObservationPointingBlock(this.getType(),parent,startTimeEvent,startTimeDelta,endTimeEvent,endTimeDelta);
		result.copyFrom(this);
		result.setMetadata(this.getMetadataElement().copy());
		//System.out.println(this.toObsXml(0));
		return result;
	}
	
	public void setParent(Observation parent){
		this.parent=parent;
	}
	public ObservationPointingBlock(Observation parent,ObservationEvent startEvent,long startDelta,ObservationEvent endEvent,long endDelta,PointingBlock block){
		super(block);
		try {
		PointingAttitude blockAttitude=new PointingAttitude(block.getChild(PointingAttitude.ATTITUDE_TAG));
		OffsetAngles of = blockAttitude.getOffsetAngles();
		
		//OffsetAngles of = block.getAttitude().getOffsetAngles();
		if (of!=null && !of.isFixed() && !of.getClass().getName().contains("Observation")){
			Date ost=null;
			//try {
				if (of.isCustom()) ost = ((OffsetCustom) of).getStartDate();
				if (of.isRaster()) ost = ((OffsetRaster) of).getStartDate();
				if (of.isScan()) ost = ((OffsetScan) of).getStartDate();
			
			long offSetDelta = ost.getTime()-block.getStartTime().getTime();
			of.addChild(new PointingElement("startTime","START_OBS"+ObservationUtil.getOffset(offSetDelta)));
			OffsetAngles ofA = ObservationUtil.readOffsetPointing(parent, of);
			//OffsetRefAxis axis = this.getAttitude().getOffsetRefAxis();
			//System.out.println("axis:"+axis.toXml(0));
			//System.out.println("ofA:"+ofA.toXml(0));
			blockAttitude.addChild(ofA);
			//blockAttitude.SetOffset(axis, ofA);
			this.setAttitude(blockAttitude);
		}
		this.parent=parent;
		startTimeEvent=startEvent;
		endTimeEvent=endEvent;
		startTimeDelta=startDelta;
		endTimeDelta=endDelta;
		} catch (Exception e) {
			IllegalArgumentException iae = new IllegalArgumentException("Problem reading obaservation "+block.toXml(0)+e.getMessage());
			iae.initCause(e);
			throw(iae);
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		
	}

	public ObservationEvent getStartEvent(){
		return startTimeEvent;
	}
	
	public ObservationEvent getEndEvent(){
		return endTimeEvent;
	}
	
	public long getStartDelta(){
		return startTimeDelta;
	}
	
	public long getEndDelta(){
		return endTimeDelta;
	}

	public Date getStartTime() {
		return new Date(parent.getDateForEvent(startTimeEvent).getTime()+startTimeDelta);
		// TODO Auto-generated method stub
		//return null;
	}

	public Date getEndTime() {
		return new Date(parent.getDateForEvent(endTimeEvent).getTime()+endTimeDelta);

		// TODO Auto-generated method stub
		//return null;
	}
	public void setStartTime(ObservationEvent event,long delta){
		startTimeEvent=event;
		startTimeDelta=delta;

	}

	public void setStartTime(Date time) {
		long oldStartDate=this.getStartTime().getTime();
		long newTime=time.getTime();
		long delta = newTime-oldStartDate;
		parent.shiftEvent(this.getStartEvent(), delta);
		//throw new IllegalArgumentException("Can not set the start time to an ObservationPointingBlock");
		// TODO Auto-generated method stub
		
	}
	public void setEndTime(ObservationEvent event,long delta){
		endTimeEvent=event;
		endTimeDelta=delta;
		
	}

	public void setEndTime(Date time) {
		long oldEndDate=this.getEndTime().getTime();
		long newTime=time.getTime();
		long delta = newTime-oldEndDate;
		parent.shiftEvent(this.getEndEvent(), delta);

		//throw new IllegalArgumentException("Can not set the end time to an ObservationPointingBlock");

		// TODO Auto-generated method stub
		
	}
	
	public String toXml(int indent){
		super.setStartTime(getStartTime());
		super.setEndTime(getEndTime());
		return super.toXml(indent);
	}
	
	public String toObsXml(int indent){
		//System.out.println(this.toXml(0));
		PointingElement tempElement=new PointingElement(this);
		this.getChild("startTime").setValue(getStartEvent().getName()+ObservationUtil.getOffset(getStartDelta()));
		this.getChild("endTime").setValue(getEndEvent().getName()+ObservationUtil.getOffset(getEndDelta()));
		PointingElement att = this.getChild(PointingAttitude.ATTITUDE_TAG).copy();
		PointingElement os = att.getChild(OffsetAngles.OFFSETANGLES_TAG);
		
		if (os!=null){

			
			att.addChild(new PointingElement(os));

			tempElement.addChild(att);
		}
		//System.out.println(tempElement.toXml(0));
		return tempElement.toXml(indent);
		
	}
	












}
