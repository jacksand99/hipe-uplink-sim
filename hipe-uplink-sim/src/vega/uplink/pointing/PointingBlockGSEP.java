package vega.uplink.pointing;

import java.util.Date;

import vega.uplink.pointing.PtrParameters.PointedAxis;

/**
 * The on-board implemented autonomous guidance attitude can be modelled in the PTR by using a GSEP block.
 * @author jarenas
 *
 */
public class PointingBlockGSEP extends PointingBlock {
	/**
	 * Creates a pointing block with an autonomous guidance attitude
	 * @param startTime Start time for the time interval covered by this block
	 * @param endTime End time for the time interval covered by this block
	 * @param coordinates SC axis that is either pointed to the Earth or the Sun
	 * @param sunPointing Flag stating whether the pointedAxis is pointed to the Sun (=true) or to Earth (=false).
	 * @param ecliptic Flag stating whether the SC-y-axis must be (nearly) perpendicular to the ecliptic plane (=true) or along the cross product of Sun and Earth direction (=false)
	 * @param north Defines the direction of the SC-y-axis. If ecliptic=true, the y-axis points towards ecliptic north and south for
	 * north=true and false respectively. If ecliptic=false, the y-axis points towards plus and minus the cross product of
	 * Earth and Sun direction for north=true and false, respectively.
	 */
	public PointingBlockGSEP(Date startTime,Date endTime,PointedAxis coordinates,boolean sunPointing,boolean ecliptic,boolean north){
		super("GSEP",startTime,endTime);
		this.addChild(coordinates);
		this.setBooleanField("sunPointing",sunPointing);
		this.setBooleanField("ecliptic",ecliptic);
		this.setBooleanField("north",north);
	}
	public void setBooleanField(String field,boolean value){
		if (value) this.addChild(new PointingMetadata(field,"true"));
		else this.addChild(new PointingMetadata(field,"false"));
	}
	
	/**
	 * @return True if pointedAxis is pointed to sun or False if it is pointed to Earth.
	 */
	public boolean isSunPointing(){
		return Boolean.parseBoolean(this.getChild("sunPointing").getValue());
	}
	/**
	 * @return True if SC-y-axis must be (nearly) perpendicular to the ecliptic plane or False if SC-y-axis must be
	 * along the cross product of Sun and Earth direction
	 */
	public boolean isEcliptic(){
		return Boolean.parseBoolean(this.getChild("ecliptic").getValue());
	}
	
	/**
	 * 
	 * @return True if ecliptic=true and the y-axis points towards ecliptic north. False if ecliptic=true and the y-axis points towards ecliptic south. 
	 * True if ecliptic=false, the y-axis points towards plus and minus the cross product of Earth direction.
	 * False if ecliptic=false, the y-axis points towards plus and minus the cross product of Sun direction.
	 */
	public boolean isNorth(){
		return Boolean.parseBoolean(this.getChild("north").getValue());
	}

}
