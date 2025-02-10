package vega.uplink.pointing.attitudes;

import vega.uplink.pointing.PointingAttitude;
import vega.uplink.pointing.PointingElement;
import vega.uplink.pointing.PtrParameters.Boresight;
import vega.uplink.pointing.PtrParameters.TargetDir;

public class TrackDir extends PointingAttitude{
    public TrackDir(PointingElement org){
        super(org);
    }
    public TrackDir() {
        super(PointingAttitude.POINTING_ATTITUDE_TYPE_TRACKDIR);
    }
    public TrackDir(Boresight boresight) {
        this();
        addChild(boresight);
    }
    public TrackDir(Boresight boresight,TargetDir trackDir) {
     this(boresight);
     addChild(trackDir);
    }
    public TrackDir copy(){
        TrackDir result = new TrackDir(super.copy());
        return result;
    }

}
