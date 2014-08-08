package vega.uplink.pointing.PtrParameters;

import vega.uplink.pointing.PointingMetadata;

/**
 * Fixed vector defined relative to inertial frame, solar system object or landmarks defined in the PDFM
 * @author jarenas
 *
 */
public class StateVector extends PointingMetadata {
	public StateVector(PointingMetadata org){
		super(org);
	}

	/**
	 * Creates a new Target refereed to a solar system object or landmarks defined in the PDFM
	 * @param ref solar system object or landmarks defined in the PDFM
	 */
	public StateVector(String type,String ref){
		super(type,"");
		setRef(ref);

	}
	
	/**
	 * Set the referenced solar system object or landmark
	 * @param ref solar system object or landmarks defined in the PDFM
	 */
	public void setRef(String ref){
		this.addAttribute(new PointingMetadata("ref",ref));		
	}
	
	/**
	 * Get the referenced solar system object or landmark
	 * @return solar system object or landmark defined in the PDFM
	 */
	public String getRef(){
		return this.getAttribute("ref").getValue();
	}
	public StateVector(String type,String landmark,String origin,String frame,String x,String y,String z){
		this(type,origin,frame,x,y,z);
		/*super(type,"");
		setLandMark(landmark);
		PointingMetadata fraChild = new PointingMetadata("position","");
		fraChild.addAttribute(new PointingMetadata("frame",frame));
		this.addChild(fraChild);
		this.setOrigin(origin);
		setX("km",Float.parseFloat(x));
		setY("km",Float.parseFloat(y));
		setZ("km",Float.parseFloat(z));*/
		this.setLandMark(landmark);

	}
	public StateVector(String type,String origin,String frame,String x,String y,String z){
		super(type,"");
		//setLandMark(landmark);
		PointingMetadata fraChild = new PointingMetadata("position","");
		fraChild.addAttribute(new PointingMetadata("frame",frame));
		this.addChild(fraChild);
		this.setOrigin(origin);
		setX("km",Float.parseFloat(x));
		setY("km",Float.parseFloat(y));
		setZ("km",Float.parseFloat(z));

	}

	public StateVector(String type,String landmark,String origin,String frame,float x,float y,float z){
		this(type,origin,frame,x,y,z);
		/*super(type,"");
		setLandMark(landmark);
		PointingMetadata fraChild = new PointingMetadata("position","");
		fraChild.addAttribute(new PointingMetadata("frame",frame));
		this.addChild(fraChild);
		this.setOrigin(origin);
		setX("km",x);
		setY("km",y);
		setZ("km",z);*/
		setLandMark(landmark);

	}
	public StateVector(String type,String origin,String frame,String xUnit,float x,String yUnit,float y,String zUnit,float z){
		super(type,"");
		//setLandMark(landmark);
		PointingMetadata fraChild = new PointingMetadata("position","");
		fraChild.addAttribute(new PointingMetadata("frame",frame));
		this.addChild(fraChild);
		this.setOrigin(origin);
		setX(xUnit,x);
		setY(yUnit,y);
		setZ(zUnit,z);
		
	}

	public StateVector(String type,String origin,String frame,float x,float y,float z){
		super(type,"");
		//setLandMark(landmark);
		PointingMetadata fraChild = new PointingMetadata("position","");
		fraChild.addAttribute(new PointingMetadata("frame",frame));
		this.addChild(fraChild);
		this.setOrigin(origin);
		setX("km",x);
		setY("km",y);
		setZ("km",z);

	}

	public void setOrigin(String origin){
		PointingMetadata org = new PointingMetadata("origin","");
		org.addAttribute(new PointingMetadata("ref",origin));
		this.addChild(org);
	}
	
	public String getOrigin(){
		return this.getChild("origin").getAttribute("ref").getValue();
	}
	private PointingMetadata getPosition(){
		return this.getChild("position");
	}
	
	
	public void setX(String unit,float x){
		getPosition().addChild(new PointingMetadata("x",""+x));
		setUnit("x",unit);
	}

	public void setY(String unit,float y){
		getPosition().addChild(new PointingMetadata("y",""+y));
		setUnit("y",unit);
	}

	public void setZ(String unit,float z){
		getPosition().addChild(new PointingMetadata("z",""+z));
		setUnit("z",unit);
	}
	
	public float getX(){
		return Float.parseFloat(getPosition().getChild("x").getValue());
	}

	public float getY(){
		return Float.parseFloat(getPosition().getChild("y").getValue());
	}

	public float getZ(){
		return Float.parseFloat(getPosition().getChild("z").getValue());
	}
	public String getUnitX(){
		return getUnit("x");
	}
	public String getUnitY(){
		return getUnit("y");
	}
	public String getUnitZ(){
		return getUnit("z");
	}

	public String getLandMark(){
		return getAttribute("landmark").getValue();
	}
	
	public void setLandMark(String name){
		addAttribute(new PointingMetadata("landmark",name));
	}
	
	public String getUnit(String field){
		PointingMetadata deltaTimesCh = getPosition().getChild(field);
		if (deltaTimesCh==null) return null;
		if (deltaTimesCh.getAttribute("units")==null) return null;
		return deltaTimesCh.getAttribute("units").getValue();
		
	}
	
	public void setUnit(String field,String unit){
		PointingMetadata deltaTimesCh = getPosition().getChild(field);
		//System.out.println(deltaTimesCh);
		if (deltaTimesCh==null) deltaTimesCh=new PointingMetadata(field,"");
		deltaTimesCh.addAttribute(new PointingMetadata("units",unit));
		getPosition().addChild(deltaTimesCh);
		
	}
	
	public String toXml(int indent){
		String result="";
		String iString="";
		for (int i=0;i<indent;i++){
			iString=iString+"\t";
		}
		PointingMetadata[] childs=this.getChildren();
		PointingMetadata[] attr=this.getAttributes();
		if (childs.length==0){
			if (attr.length>0 || getValue()!=""){
					result=result+iString+"<"+getName();
					for (int z=0;z<attr.length;z++){
						if (!attr[z].getName().equals("landmark")){

							result=result+" "+attr[z].getName()+"='"+attr[z].getValue()+"'";
						}else{
							result=result+" "+"name"+"='"+attr[z].getValue()+"'";
						}
					}
					if(!getValue().equals("")) result=result+">"+getValue()+"</"+getName()+">\n";
					else{
						result=result+"/>\n";
					}
			}
		}else{
			result=result+iString+"<"+getName();
			for (int z=0;z<attr.length;z++){
				if (!attr[z].getName().equals("landmark")){
					result=result+" "+attr[z].getName()+"='"+attr[z].getValue()+"'";
				}else{
					result=result+" "+"name"+"='"+attr[z].getValue()+"'";
				}
			}
			result=result+">\n";


			for (int j=0;j<childs.length;j++){
				result=result+childs[j].toXml(indent+1);
			}
			result=result+iString+"</"+getName()+">\n";
			
		}
		return result;
	}
	




}
