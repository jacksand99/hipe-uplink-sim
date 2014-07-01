package vega.uplink.pointing.PtrParameters;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import vega.uplink.pointing.PointingMetadata;

public class Boresight extends PointingMetadata {
	public Boresight(PointingMetadata org){
		super(org);
	}

	public Boresight(String frame,String x,String y,String z){
		super("boresight","");
		addAttribute(new PointingMetadata("frame",frame));
		setX(Float.parseFloat(x));
		setY(Float.parseFloat(y));
		setZ(Float.parseFloat(z));
		
		
	}

	public Boresight(String frame,float x,float y,float z){
		super("boresight","");
		addAttribute(new PointingMetadata("frame",frame));
		setX(x);
		setY(y);
		setZ(z);
	}

	
	public float getX(){
		return Float.parseFloat(getChild("x").getValue());
	}
	
	public void setX(float x){
		addChild(new PointingMetadata("x",""+x));
	}

	public float getY(){
		return Float.parseFloat(getChild("y").getValue());
	}

	public void setY(float y){
		addChild(new PointingMetadata("y",""+y));
	}

	public float getZ(){
		return Float.parseFloat(getChild("z").getValue());
	}

	public void setZ(float z){
		addChild(new PointingMetadata("z",""+z));
	}


	public Boresight(String ref){
		super("boresight","");
		addAttribute(new PointingMetadata("ref",ref));
		
	}
	public Boresight(){
		//this("SC","0,","0.","1.");
		this("Nadir_Nav_Boresight");
	}
	
	public String getFrame(){
		return getAttribute("frame").getValue();
	}
	
	
	public Boresight copy() {
		Boresight result = new Boresight();
		result.setValue(getValue());
		PointingMetadata[] ch = getChildren();
		for (int i=0;i<ch.length;i++){
			result.addChild(ch[i]);
		}
		PointingMetadata[] att = getAttributes();
		for (int i=0;i<att.length;i++){
			result.addAttribute(att[i]);
		}
		return result;
	}
	
	public String toDirVectorXml(String dirVectorName){
		int indent=1;
		String result="";
		String iString="";
		for (int i=0;i<indent;i++){
			iString=iString+"\t";
		}
		PointingMetadata[] childs=this.getChildren();
		PointingMetadata[] attr=this.getAttributes();
		if (childs.length==0){
			if (attr.length>0 || getValue()!=""){
				result=result+iString+"<"+"dirVector name=\""+dirVectorName+"\"";
				for (int z=0;z<attr.length;z++) result=result+" "+attr[z].getName()+"='"+attr[z].getValue()+"'";
				if(!getValue().equals("")) result=result+">"+getValue()+"</"+getName()+">\n";
				else{
					result=result+"/>\n";
				}
			}
		}else{
			result=result+iString+"<"+"dirVector name=\""+dirVectorName+"\"";
			for (int z=0;z<attr.length;z++) result=result+" "+attr[z].getName()+"='"+attr[z].getValue()+"'";
			result=result+">\n";


			for (int j=0;j<childs.length;j++){
				result=result+childs[j].toXml(indent+1);
			}
			result=result+iString+"</"+"dirVector"+">\n";
			
		}
		return result;

	}


}
