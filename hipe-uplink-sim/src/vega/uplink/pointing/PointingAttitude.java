package vega.uplink.pointing;


import vega.uplink.pointing.PtrParameters.Boresight;
import vega.uplink.pointing.PtrParameters.PhaseAngle;
import vega.uplink.pointing.PtrParameters.Offset.OffsetAngles;
import vega.uplink.pointing.PtrParameters.Offset.OffsetRefAxis;
import herschel.ia.dataset.CompositeDataset;
import herschel.ia.dataset.MetaData;
import herschel.ia.dataset.StringParameter;
import herschel.share.interpreter.InterpreterUtil;

public class PointingAttitude extends PointingMetadata{
	
	public PointingAttitude(PointingMetadata org){
		super(org);
	}

	public PointingAttitude(String type,Boresight boresight,PhaseAngle phaseAngle){
		super("attitude","");
		this.addAttribute(new PointingMetadata("ref",type));
		setBoresight(boresight);
		setPhaseAngle(phaseAngle);
	}
	public PointingAttitude(String type){
		super("attitude","");
		this.addAttribute(new PointingMetadata("ref",type));
		setBoresight(new Boresight());
		setPhaseAngle(new PhaseAngle());
	}
	
	public String getAttitudeType(){
		return this.getAttribute("ref").getValue();
	}
	
	public void setBoresight(Boresight boresight){
		this.addChild(boresight);
	}
	
	public Boresight getBoresight(){
		return (Boresight) this.getChild("boresight");
	}
	
	public PhaseAngle getPhaseAngle(){
		if (getAttitudeType().endsWith("_pwropt")){
			PhaseAngle result = new PhaseAngle();
			return result;
			//result.addAttribute(new PointingMetadata("ref","powerOptimised"));
			
		}
		return (PhaseAngle) this.getChild("phaseAngle");
	}
	
	public void setPhaseAngle(PhaseAngle phaseAngle){
		if (getAttitudeType().endsWith("_pwropt")) return; //If it is power optimised, then do not add attitude
		
		this.addChild(phaseAngle);
	}
	
	public void SetOffset(OffsetRefAxis refAxis,OffsetAngles angles ){
		this.addChild(refAxis);
		this.addChild(angles);
	}
	
	public OffsetRefAxis getOffsetRefAxis(){
		PointingMetadata child = this.getChild("offsetRefAxis");
		if (child==null) return null;
		if (InterpreterUtil.isInstance(OffsetRefAxis.class, child)){
			return (OffsetRefAxis) child;
		}else return null;
	}
	
	public OffsetAngles getOffsetAngles(){
		PointingMetadata child = this.getChild("offsetAngles");
		if (child==null) return null;
		if (InterpreterUtil.isInstance(OffsetAngles.class, child)){
			return (OffsetAngles) child;
		}else return null;
	}

	
	public PointingAttitude copy() {
		PointingAttitude result = new PointingAttitude(getAttitudeType());
		PointingMetadata[] ch = getChildren();
		for (int i=0;i<ch.length;i++){
			result.addChild(ch[i]);
		}
		
		return result;
	}


}
