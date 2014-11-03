package vega.uplink.pointing.PtrParameters;

import vega.uplink.pointing.PointingElement;
import vega.uplink.pointing.Units;

/**
 * The boresight is pointed towards the point that lies the
 * specified height along the local normal of the selected point on the limb.
 * @author jarenas
 *
 */
public class Height extends PointingElement {
	/**
	 * height
	 */
	public static String HEIGHT_TAG="height";
	/**
	 * units
	 */
	public static String UNITS_TAG="units";
	public Height(PointingElement org){
		super(org);
	}
	public Height copy(){
		Height result = new Height(super.copy());
		return result;
	}

		/**
		 * Creates a height with units and value
		 * @param units
		 * @param value
		 */
		public Height(String units,String value){
			super(HEIGHT_TAG,value);
			this.addAttribute(new PointingElement(UNITS_TAG,units));
			
		}
		/**
		 * Creates a height with units and value
		 * @param units
		 * @param value
		 */
		public Height(String units,float value){
			this(units,value+"");
		}
		/**
		 * Creates a height expressed in km and value 0
		 * @param units
		 * @param value
		 */
		public Height(){
			this(Units.KILOMETER,0);
		}

		
		/**
		 * Set the value of the height
		 * @param h
		 */
		public void setHeight(float h){
			setValue(""+h);
		}
		
		/**
		 * Get the value of the height
		 * @return
		 */
		public float getHeight(){
			return Float.parseFloat(getValue());
		}
		public float getHeight(String unit){
			return Units.convertUnit(getHeight(), getUnit(), unit);
		}
		
		/**
		 * Get the unit where the height is expressed
		 * @return
		 */
		public String getUnit(){
			return getAttribute(UNITS_TAG).getValue();
		}
		/**
		 * Set the unit where the height is expressed
		 * @return
		 */		
		public void setUnit(String units){
			addAttribute(new PointingElement(UNITS_TAG,units));
		}

}
