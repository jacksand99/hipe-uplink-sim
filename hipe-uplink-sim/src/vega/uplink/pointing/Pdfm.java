package vega.uplink.pointing;

//import rosetta.uplink.commanding.HistoryModes;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;

import vega.uplink.pointing.PtrParameters.Boresight;
import herschel.ia.dataset.Dataset;
import herschel.ia.dataset.Product;
import herschel.share.interpreter.InterpreterUtil;

public class Pdfm extends Product {
	public Pdfm(){
		super();
	}
	
	public void addChild(PointingMetadata child){
		set(child.getName(),child);
	}
	
	public PointingMetadata getChild(String name){
		Dataset result = get(name);
		if (InterpreterUtil.isInstance(PointingMetadata.class, result)) return (PointingMetadata) result;
		else return null;
		
	}
	
	public PointingMetadata[] getAllChildren(){
		Vector<PointingMetadata> rVector=new Vector<PointingMetadata>();
		Map<String, Dataset> sets = this.getSets();
		Iterator<String> it = sets.keySet().iterator();
		while (it.hasNext()){
			String key = it.next();
			PointingMetadata bore=getChild(key);
			if (bore!=null) rVector.add(bore);
		}
		PointingMetadata[] result=new PointingMetadata[rVector.size()];
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
			PointingMetadata bore=getChild(key);
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
