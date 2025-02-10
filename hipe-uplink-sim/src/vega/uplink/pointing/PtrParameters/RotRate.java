package vega.uplink.pointing.PtrParameters;

import vega.uplink.pointing.PointingElement;
import vega.uplink.pointing.Units;

/**
 * @author jarenas
 *
 */
public class RotRate extends PointingElement {
    /**
     * height
     */
    public static String ROTRATE_TAG="rotRate";
    /**
     * units
     */
    public static String UNITS_TAG="units";
    public RotRate(PointingElement org){
        super(org);
    }
    public Height copy(){
        Height result = new Height(super.copy());
        return result;
    }

        /**
         * Creates a rotrate with units and value
         * @param units
         * @param value
         */
        public RotRate(String units,String value){
            super(ROTRATE_TAG,value);
            this.addAttribute(new PointingElement(UNITS_TAG,units));
            
        }
        /**
         * Creates a rotrate with units and value
         * @param units
         * @param value
         */
        public RotRate(String units,float value){
            this(units,value+"");
        }
        /**
         * Creates a rotrate expressed in km and value 0
         * @param units
         * @param value
         */
        public RotRate(){
            this(Units.DEGREES_PER_DAY,0);
        }

        
        /**
         * Set the value of the rotrate
         * @param h
         */
        public void setRotRate(float h){
            setValue(""+h);
        }
        
        /**
         * Get the value of the rotrate
         * @return
         */
        public float getRotRate(){
            return Float.parseFloat(getValue());
        }
        public float getHeight(String unit){
            return Units.convertUnit(getRotRate(), getUnit(), unit);
        }
        
        /**
         * Get the unit where the rotrate is expressed
         * @return
         */
        public String getUnit(){
            return getAttribute(UNITS_TAG).getValue();
        }
        /**
         * Set the unit where the rotrate is expressed
         * @return
         */     
        public void setUnit(String units){
            addAttribute(new PointingElement(UNITS_TAG,units));
        }

}
