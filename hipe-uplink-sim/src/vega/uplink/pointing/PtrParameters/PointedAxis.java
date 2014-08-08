package vega.uplink.pointing.PtrParameters;
import vega.uplink.pointing.PointingMetadata;
public class PointedAxis extends DirectionVector{
        public PointedAxis(PointingMetadata pm){
                super("pointedAxis",pm);
        }
        /*public PointedAxis(String frame,float x,float y, float z){
                super("pointedAxis",frame,x,y,z);
        }*/
    	public PointedAxis(String frame,float longitude,float latitude){
    		super("pointedAxis",frame,longitude,latitude);
    	}
    	public PointedAxis(String reference){
    		super("pointedAxis",reference);
    	}
    	public PointedAxis(String frame,float x,float y, float z){
    		super("pointedAxis",frame,x,y,z);
    	}
    	public PointedAxis(String frame,String unitLongitude,float longitude,String unitLatitude,float latitude){
    		super("pointedAxis",frame,unitLongitude,longitude,unitLatitude,latitude);
    	}
    	public PointedAxis(String origin,String target){
    		super("pointedAxis",origin,target);
    	}
    	public PointedAxis(String axis,String rotationAxis,float rotationAngle){
    		super("pointedAxis",axis,rotationAxis,rotationAngle);
    	}
    	public PointedAxis(String axis,String rotationAxis,String rotationAngleUnit,float rotationAngle){
    		super("pointedAxis",axis,rotationAxis,rotationAngleUnit,rotationAngle);
    		
    	}


        
}
