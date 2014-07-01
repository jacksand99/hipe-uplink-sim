package vega.uplink.pointing.PtrParameters;

import vega.uplink.pointing.PointingMetadata;

public class Height extends PointingMetadata {
	public Height(PointingMetadata org){
		super(org);
	}

		public Height(String units,String value){
			super("height",value);
			this.addAttribute(new PointingMetadata("units",units));
			
		}
		
		public void setHeight(float h){
			setValue(""+h);
		}
		
		public float getHeight(){
			return Float.parseFloat(getValue());
		}
		
		public String getUnit(){
			return getAttribute("units").getValue();
		}
		
		public void setUnit(String units){
			addAttribute(new PointingMetadata("units",units));
		}

}
