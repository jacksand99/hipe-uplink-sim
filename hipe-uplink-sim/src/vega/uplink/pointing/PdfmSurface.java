package vega.uplink.pointing;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import vega.uplink.pointing.PtrParameters.DirectionVector;

public class PdfmSurface extends PointingMetadata {
	public PdfmSurface(PointingMetadata org){
		
		super(org);
	}
	public PdfmSurface(){
		super("surface","");
	}
	public PdfmSurface(String surfaceName){
		super("surface","");
		setSurfaceName(surfaceName);
	}
	public PdfmSurface(String name,String frame,String origin,String unitsA,float a,String unitsB,float b,String unitsC,float c,String axisAFrame,float axisAX,float axisAY,float axisAZ,String axisBFrame,float axisBX,float axisBY,float axisBZ,String axisCFrame,float axisCX,float axisCY,float axisCZ){
		this();
		setSurfaceName(name);
		this.setFrame(frame);
		this.setOrigin(origin);
		this.setA(unitsA, a);
		this.setB(unitsB, b);
		this.setC(unitsC, c);
		this.setAAxis(axisAFrame, axisAX, axisAY, axisAZ);
		this.setBAxis(axisBFrame, axisBX, axisBY, axisBZ);
		this.setCAxis(axisCFrame, axisCX, axisCY, axisCZ);
		
	}
	public PdfmSurface(String name,String frame,String origin,String units,float a,float b,float c,float axisAX,float axisAY,float axisAZ,float axisBX,float axisBY,float axisBZ,float axisCX,float axisCY,float axisCZ){
		this(name,frame,origin,units,a,units,b,units,c,frame,axisAX,axisAY,axisAZ,frame,axisBX,axisBY,axisBZ,frame,axisCX,axisCY,axisCZ);
	}
	
	public void setSurfaceName(String name){
		//System.out.println("Set surface name:"+name);
		if (name!=null) this.addChild(new PointingMetadata("surfacename",name));
	}
	public void setFrame(String frame){
		addAttribute(new PointingMetadata("frame",frame));
	}
	public void setOrigin(String origin){
		PointingMetadata originChild = new PointingMetadata("origin","");
		originChild.addAttribute(new PointingMetadata("ref",origin));
		this.addChild(originChild);
	}
	public String getOrigin(){
		return this.getChild("origin").getAttribute("ref").getValue();
	}
	public void setA(String units,float value){
		//this.addChild(new PointingMetadata("lat",""+latitude));
		this.setFloatField("a", value);
		this.setUnit("a", units);
	}
	public void setB(String units,float value){
		//this.addChild(new PointingMetadata("lat",""+latitude));
		this.setFloatField("b", value);
		this.setUnit("b", units);
	}
	public void setC(String units,float value){
		//this.addChild(new PointingMetadata("lat",""+latitude));
		this.setFloatField("c", value);
		this.setUnit("c", units);
	}
	public void setAxis(String name,String frame,float x,float y, float z){
		this.addChild(new DirectionVector(name,frame,x,y,z));
	}
	public void setAAxis(String frame,float x,float y, float z){
		setAxis("axisA",frame,x,y,z);
	}
	public void setBAxis(String frame,float x,float y, float z){
		setAxis("axisB",frame,x,y,z);
	}
	public void setCAxis(String frame,float x,float y, float z){
		setAxis("axisC",frame,x,y,z);
	}

	public void setFloatField(String field,float value){
		String sDT=""+value;
		PointingMetadata deltaTimesCh;
		deltaTimesCh=getChild(field);
		if (deltaTimesCh==null){
			deltaTimesCh = new PointingMetadata(field,sDT);
		}else{
			deltaTimesCh.setValue(sDT);
		}
		//PointingMetadata deltaTimesCh = new PointingMetadata("xRates",sDT);
		addChild(deltaTimesCh);
		
	}
	public String getUnit(String field){
		PointingMetadata deltaTimesCh = getChild(field);
		if (deltaTimesCh==null) return null;
		if (deltaTimesCh.getAttribute("units")==null) return null;
		return deltaTimesCh.getAttribute("units").getValue();
		
	}
	
	public void setUnit(String field,String unit){
		PointingMetadata deltaTimesCh = getChild(field);
		//System.out.println(deltaTimesCh);
		if (deltaTimesCh==null) deltaTimesCh=new PointingMetadata(field,"");
		deltaTimesCh.addAttribute(new PointingMetadata("units",unit));
		addChild(deltaTimesCh);
		
	}

	/**
	 * Return the frame to which the Surface is relative
	 * @return Frame to which the vector is relative
	 */
	public String getFrame(){
		return getAttribute("frame").getValue();
	}

	public String getSurfaceName(){
		//System.out.println(this.getChild(childName))
		if (this.getChild("surfacename")==null) return null;
		return this.getChild("surfacename").getValue();
	}
	public boolean hasSurfaceName(){
		if (this.getChild("surfacename")==null){
			System.out.println("false");
			return false;
		}
		else return true;
	}

	public String toXml(int indent){
		String result="";
		String iString="";
		for (int i=0;i<indent;i++){
			iString=iString+"\t";
		}
		PointingMetadata[] childs=this.getChildren();
		PointingMetadata[] attr=this.getAttributes();
		String surfaceName=getSurfaceName();
		if (childs.length==0){
			if (attr.length>0 || getValue()!=""){
				if (hasSurfaceName()){
					//System.out.println("I do not care if "+hasSurfaceName());
					result=result+iString+"<surface name=\""+surfaceName+"\"";
				}
				else result=result+iString+"<surface ";
				for (int z=0;z<attr.length;z++){
					if (!attr[z].getName().equals("name")) result=result+" "+attr[z].getName()+"='"+attr[z].getValue()+"'";
				}
				if(!getValue().equals("")) result=result+">"+getValue()+"</"+getName()+">\n";
				else{
					result=result+"/>\n";
				}
			}
		}else{
			if (hasSurfaceName()){
				//System.out.println("I do not care if "+hasSurfaceName());
				result=result+iString+"<surface name=\""+surfaceName+"\"";
			}
			else result=result+iString+"<surface ";
			for (int z=0;z<attr.length;z++){
				if (!attr[z].getName().equals("name")) result=result+" "+attr[z].getName()+"='"+attr[z].getValue()+"'";
			}
			result=result+">\n";


			for (int j=0;j<childs.length;j++){
				if (!childs[j].getName().equals("surfacename")) result=result+childs[j].toXml(indent+1);
			}
			result=result+iString+"</surface>\n";
			
		}
		return result;
	}
	
	public static PdfmSurface readFrom(Node node){
		String name=node.getAttributes().getNamedItem("name").getTextContent();
		//String name=node.getNodeName();
		if (name.equals("#text")) return null;
		if (name.equals("#comment")) return null;

		//System.out.println(name);
		//String value="";
		String value=node.getTextContent();
		if (value==null) value="";
		PdfmSurface result = new PdfmSurface(name);

		if (node.hasAttributes()){
			NamedNodeMap att = node.getAttributes();
			int size=att.getLength();
			for (int i=0;i<size;i++){
				String attname=att.item(i).getNodeName();
				String attvalue=att.item(i).getNodeValue();
				if (!attname.equals("name") && !attname.equals("") && !attvalue.equals("")) result.addAttribute(new PointingMetadata(attname,attvalue));
			}
		}
		if (node.hasChildNodes()){
			NodeList children = node.getChildNodes();
			int size=children.getLength();
			for (int i=0;i<size;i++){
				PointingMetadata child = PointingMetadata.readFrom(children.item(i));
				if (child!=null) result.addChild(child);
			}			
		}
		//else{
			//value=node.getNodeValue();
			//if (value!="" && value!=null)result.setValue(value);
		//}
		return result;
	}

}
