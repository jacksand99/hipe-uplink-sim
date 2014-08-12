package vega.uplink.pointing.PtrParameters;

import vega.uplink.pointing.PointingMetadata;

/**
 * The boresight is pointed towards the point that lies the
 * specified height along the local normal of the selected point on the limb.
 * @author jarenas
 *
 */
public class Height extends PointingMetadata {
	public Height(PointingMetadata org){
		super(org);
	}

		/**
		 * Creates a height with units and value
		 * @param units
		 * @param value
		 */
		public Height(String units,String value){
			super("height",value);
			this.addAttribute(new PointingMetadata("units",units));
			
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
			this("km",0);
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
		
		/**
		 * Get the unit where the height is expressed
		 * @return
		 */
		public String getUnit(){
			return getAttribute("units").getValue();
		}
		/**
		 * Set the unit where the height is expressed
		 * @return
		 */		
		public void setUnit(String units){
			addAttribute(new PointingMetadata("units",units));
		}

}
