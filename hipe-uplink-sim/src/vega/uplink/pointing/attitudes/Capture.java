package vega.uplink.pointing.attitudes;

import java.text.ParseException;
import java.util.Date;

import vega.uplink.pointing.PointingAttitude;
import vega.uplink.pointing.PointingBlock;
import vega.uplink.pointing.PointingMetadata;

public class Capture extends PointingAttitude {
	public Capture(PointingMetadata pm){
		super(pm);
	}
	public Capture(Date captureDate){
		super("capture");
		setCaptureDate(captureDate);
		//this.addChild(new PointingMetadata("captureTime",PointingBlock.dateToZulu(captureDate)));
	}
	
	public Date getCaptureDate(){
		try {
			return PointingBlock.zuluToDate(this.getChild("captureTime").getValue());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new Date();
		}
	}
	
	public void setCaptureDate(Date captureDate){
		this.addChild(new PointingMetadata("captureTime",PointingBlock.dateToZulu(captureDate)));
	}
	
	
	
}
