package vega.uplink.pointing.attitudes;

import vega.uplink.pointing.PointingAttitude;
import vega.uplink.pointing.PointingElement;
import vega.uplink.pointing.PtrParameters.Boresight;
import vega.uplink.pointing.PtrParameters.InitTargetPoint;
import vega.uplink.pointing.PtrParameters.RotRate;
import vega.uplink.pointing.PtrParameters.TargetDir;

public class TrackPointRot extends PointingAttitude{
    public TrackPointRot(PointingElement org){
        super(org);
    }
    public TrackPointRot() {
        super(PointingAttitude.POINTING_ATTITUDE_TYPE_TRACKPOINTROT);
    }
    public TrackPointRot(Boresight boresight) {
        this();
        addChild(boresight);
    }
    public TrackPointRot(Boresight boresight,InitTargetPoint initTargetPoint,RotRate rotRate) {
     this(boresight);
     addChild(initTargetPoint);
     addChild(rotRate);
    }
    public TrackPointRot copy(){
        TrackPointRot result = new TrackPointRot(super.copy());
        return result;
    }
    public InitTargetPoint getInitTargetPoint(){
        PointingElement result = this.getChild(InitTargetPoint.INITTARGETPOINT_TAG);
        if (result!=null){
            return (InitTargetPoint) result;
        }else{
            return null;
        }
    }
    
    public RotRate getRotRate(){
        PointingElement result = this.getChild(RotRate.ROTRATE_TAG);
        if (result!=null){
            return (RotRate) result;
        }else{
            return null;
        }
    }

}