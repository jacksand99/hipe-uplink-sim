package vega.uplink.pointing;

//import rosetta.uplink.commanding.HistoryModes;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;

import vega.uplink.Properties;
import vega.uplink.pointing.PtrParameters.Boresight;
import herschel.ia.dataset.Dataset;
import herschel.ia.dataset.Product;
import herschel.ia.dataset.StringParameter;
import herschel.share.fltdyn.time.FineTime;
import herschel.share.interpreter.InterpreterUtil;

public class Pdfm extends Product {
	public Pdfm(){
		super();
		setName(""+new java.util.Date().getTime());
		setPath(Properties.getProperty("user.home"));
		this.setType("PDFM");
		this.setCreationDate(new FineTime(new java.util.Date()));

		
	}
	public void setName(String name){
		getMeta().set("name", new StringParameter(name));
	}
	public void setPath(String path){
		getMeta().set("path", new StringParameter(path));
	}
	
	public String getName(){
		return (String) getMeta().get("name").getValue();
	}
	public String getPath(){
		return (String) getMeta().get("path").getValue();
	}

	public void addDefinition(PdfmDirVector dirVector){
		set(dirVector.getDirVectorName(),dirVector);
	}
	public void addDefinition(PdfmSurface surface){
		set(surface.getSurfaceName(),surface);
	}

	/*public void addChild(PointingMetadata child){
		set(child.getName(),child);
	}*/
	
	public PointingElement getDefinition(String name){
		Dataset result = get(name);
		if (InterpreterUtil.isInstance(PointingElement.class, result)) return (PointingElement) result;
		else return null;
		
	}
	
	public PointingElement[] getAllChildren(){
		Vector<PointingElement> rVector=new Vector<PointingElement>();
		Map<String, Dataset> sets = this.getSets();
		Iterator<String> it = sets.keySet().iterator();
		while (it.hasNext()){
			String key = it.next();
			PointingElement bore=getDefinition(key);
			if (bore!=null) rVector.add(bore);
		}
		PointingElement[] result=new PointingElement[rVector.size()];
		rVector.toArray(result);
		return result;
		
	}
	
	public String toXml(){
		String result="";
		result=result+"<?xml version=\"1.0\"?>\n";
		result=result+"<definition>\n";
		//Boresight[] bores = getAllBoresight();
		Map<String, Dataset> sets = this.getSets();
		Iterator<String> it = sets.keySet().iterator();
		while (it.hasNext()){
			String key = it.next();
			PointingElement bore=getDefinition(key);
			if (bore!=null){
				result=result+bore.toXml(1);
				//rVector.add(bore);
			}
		}

		/*for (int i=0;i<bores.length;i++){
			Boresight dirVector = bores[i].copy();
			//dirVector.set
			result=result+bores[i].toDirVectorXml("dummy");
		}*/
		result=result+"</definition>\n";
		return result;
	}
}
