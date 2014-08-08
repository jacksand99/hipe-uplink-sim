package vega.uplink.pointing.attitudes;

import vega.uplink.pointing.PointingAttitude;
import vega.uplink.pointing.PointingMetadata;
import vega.uplink.pointing.PtrParameters.Boresight;
import vega.uplink.pointing.PtrParameters.OffsetRefAxis;
import vega.uplink.pointing.PtrParameters.PhaseAngle;
import vega.uplink.pointing.PtrParameters.Surface;
import vega.uplink.pointing.PtrParameters.Offset.OffsetAngles;

public class Specular extends PointingAttitude {
	public Specular(PointingMetadata org){
		super(org);
	}
	public Specular(Boresight boresight,PhaseAngle phaseAngle,Surface surface,OffsetAngles oAngles,OffsetRefAxis oRefAxis){
		this(boresight,phaseAngle,surface,oAngles);
		addChild(oRefAxis);
	}
	public Specular(Boresight boresight,PhaseAngle phaseAngle,Surface surface,OffsetAngles oAngles){
		this(boresight,phaseAngle,surface);
		addChild(oAngles);
	}

	public Specular(Boresight boresight,PhaseAngle phaseAngle,Surface surface){
		super("specular");
		setBoresight(boresight);
		setPhaseAngle(phaseAngle);
		setSurface(surface);
	}
	
	public Specular(Surface surface){
		super("specular");
		setSurface(surface);
	}
	
	public Specular(){
		super("specular");
		//this(new Surface("CG"));
	}
	
	public void setSurface(Surface surface){
		this.addChild(surface);
	}
	
	public Surface getSurface(){
		return (Surface) this.getChild("surface");
	}
}
