package vega.hipe.pds.vicar;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Test {

	public static void main(String[] args) {
		try {
			PDSInputFile pdsFile = new PDSInputFile("/Users/jarenas 1/Rosetta/NAC_WAC/N20070522T222601460ID30F22.IMG");
			Document doc = pdsFile.getPDSDocument();
			Element ele = doc.getDocumentElement();
			/*NamedNodeMap att = ele.getAttributes();
			for (int i=0;i<att.getLength();i++){
				System.out.println(att.item(i).getNodeName());
			}
			NodeList nl = ele.getChildNodes();
			for (int i=0;i<nl.getLength();i++){
				System.out.println(nl.item(i).getNodeName());
				System.out.println(nl.item(i).getNodeValue());
			}*/
			HashMap<String,String> properties=new HashMap<String,String>();
			HashMap<String,String> units=new HashMap<String,String>();
			listValues(ele,"","",properties,units);
			Iterator<String> it = properties.keySet().iterator();
			while (it.hasNext()){
				String prop = it.next();
				String val=properties.get(prop);
				String unit = units.get(prop);
				if (unit!=null) val=val+" "+unit;
				System.out.println(prop+"="+val);
			}
			int os = pdsFile.getImageStart(doc);
			System.out.println(os);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private static void listValues(Node node,String tabs,String name,HashMap<String,String> properties,HashMap<String,String> units){
		if (node.getNodeName().equals("OBJECT")){
			name=name+node.getAttributes().getNamedItem("name").getNodeValue()+".";
		}
		String key =null; 
		String value=null;
		String unit=null;
		NamedNodeMap att = node.getAttributes();
		if (att!=null){
			for (int i=0;i<att.getLength();i++){
				if (att.item(i).getNodeName().equals("key")) key=att.item(i).getNodeValue();
				if (att.item(i).getNodeName().equals("units")) unit=att.item(i).getNodeValue();
			}	
		}
		NodeList nl = node.getChildNodes();
		for (int i=0;i<nl.getLength();i++){
			if (nl.item(i).getNodeName().equals("#text")) value=nl.item(i).getNodeValue();
			if (nl.item(i).getNodeName().equals("units")) unit=nl.item(i).getNodeValue();
			listValues(nl.item(i),tabs+"\t",name,properties,units);
		}
		if (key!=null && value!=null){
			String prev = properties.get(name+key);
			if (prev!=null){
				properties.put(name+key, prev+","+value);
			}else{
				properties.put(name+key, value);
			}
		}
		if (key!=null && unit!=null){
			units.put(name+key, unit);
		}
	}

}
