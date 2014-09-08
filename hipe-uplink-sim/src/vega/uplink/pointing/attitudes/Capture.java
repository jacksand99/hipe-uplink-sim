package vega.uplink.pointing.attitudes;

import java.text.ParseException;
import java.util.Date;

import vega.uplink.pointing.PointingAttitude;
import vega.uplink.pointing.PointingBlock;
import vega.uplink.pointing.PointingElement;

/**
 * A capture pointing is used for calibration purposes. It is required if an attitude that is not
 * exactly known at MTP needs to be flown again at a later time within the same MTP. The
 * capture pointing allows to implement a fixed attitude that was implemented in the same
 * PTR at an earlier time.
 * @author jarenas
 *
 */
public class Capture extends PointingAttitude {
	public Capture(PointingElement pm){
		super(pm);
	}
	/**
	 * Generates an attitude a fixed attitude that was implemented in the same PTR at an earlier time.
	 * @param captureDate Time at which the attitude is evaluated
	 */
	public Capture(Date captureDate){
		super(PointingAttitude.POINTING_ATTITUDE_TYPE_CAPTURE);
		setCaptureDate(captureDate);
		//this.addChild(new PointingMetadata("captureTime",PointingBlock.dateToZulu(captureDate)));
	}
	
	/**
	 * Get the time at which the attitude is evaluated
	 * @return
	 */
	public Date getCaptureDate(){
		try {
			return PointingBlock.zuluToDate(this.getChild("captureTime").getValue());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new Date();
		}
	}
	/**
	 * Set the time at which the attitude is evaluated
	 * @return
	 */	
	public void setCaptureDate(Date captureDate){
		this.addChild(new PointingElement("captureTime",PointingBlock.dateToZulu(captureDate)));
	}
	
	
	
}
