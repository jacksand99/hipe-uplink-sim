package vega.uplink.pointing;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;











import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import vega.uplink.pointing.PtrParameters.Boresight;
//import rosetta.uplink.commanding.HistoryModes;
import herschel.ia.dataset.*;
import herschel.ia.numeric.String1d;
import herschel.share.interpreter.InterpreterUtil;

public class PointingMetadata extends CompositeDataset  {
	//private String name;
	//private String value;
	//java.util.HashMap<String, PointingMetadata> children;
	private String description;
	//PointingMetadata[] children;
	//TableDataset attributes;
	//java.util.HashMap<String, PointingMetadata> attributes;
	
	public PointingMetadata(){
		super();
		//name=null;
		//value=null;
		//children=new java.util.HashMap<String, PointingMetadata>();
		//attributes=new java.util.HashMap<String, PointingMetadata>();
		/*attributes=new TableDataset();
		Column col1=new Column(new String1d());
		Column col2=new Column(new String1d());
		attributes.addColumn(col1);
		attributes.addColumn(col2);
		attributes.setColumnName(0, "Name");
		attributes.setColumnName(0, "Value");
		set("attributes",attributes);*/

		
		description="";
	}
	public PointingMetadata(PointingMetadata org) {
		this(org.getName(),"");
		
		this.setValue(org.getValue());
		PointingMetadata[] ch = org.getChildren();
		for (int i=0;i<ch.length;i++){
			addChild(ch[i]);
		}
		PointingMetadata[] att = org.getAttributes();
		for (int i=0;i<att.length;i++){
			addAttribute(att[i]);
		}
		//return result;
	}
	public void copyFrom(PointingMetadata org){
		this.setValue(org.getValue());
		PointingMetadata[] ch = org.getChildren();
		for (int i=0;i<ch.length;i++){
			addChild(ch[i]);
		}
		PointingMetadata[] att = org.getAttributes();
		for (int i=0;i<att.length;i++){
			addAttribute(att[i]);
		}
		
	}

	@Override
	public PointingMetadata copy() {
		PointingMetadata result = new PointingMetadata();
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

	
	public PointingMetadata(String metaName,String metaValue){
		this();
		this.set("name", new ArrayDataset(new String1d().append(metaName)));
		this.set("value", new ArrayDataset(new String1d().append(metaValue)));
		
		/*getMeta().set("name", new StringParameter(metaName));
		getMeta().set("value", new StringParameter(metaValue));*/

		//name=metaName;
		//value=metaValue;
		//children=new java.util.HashMap<String, PointingMetadata>();
		//attributes=new java.util.HashMap<String, PointingMetadata>();

	}
	
	public static StringParameter toStringParameter(PointingMetadata mt){
		return new StringParameter(mt.getValue());
	}
	public void addAttribute(PointingMetadata attribute){
		this.getMeta().set(attribute.getName(), toStringParameter(attribute));
		
		//System.out.println(attribute);
		/*String[] row=new String[2];
		row[0]=attribute.getName();
		row[1]=attribute.getValue();
		attributes.addRow(row);*/
		//getMeta().set(attribute.getName(),PointingMetadata.toStringParameter(attribute));
		//attributes.put(attribute.getName(), attribute);
	}
	
	public PointingMetadata[] getAttributes(){
		Vector<PointingMetadata> vAtt=new Vector<PointingMetadata>();
		Set<String> keySet = this.getMeta().keySet();
		Iterator<String> it = keySet.iterator();
		while (it.hasNext()){
			String metaName=it.next();
			//if (!it.equals("name") && !it.equals("value")){
			String sp = (String) this.getMeta().get(metaName).getValue();
			vAtt.add(new PointingMetadata(metaName,sp));
			//}
		}
		PointingMetadata[] result=new PointingMetadata[vAtt.size()];
		vAtt.toArray(result);
		return result;
		//int size=attributes.getRowCount();
		//PointingMetadata[] result=new PointingMetadata[size];
		/*for (int i=0;i<size;i++){
			String na=(String) attributes.getRow(i).get(0);
			String va=(String) attributes.getRow(i).get(1);
			result[i]=new PointingMetadata(na,va);
		}
		
		return result;*/
		/*MetaData meta = getMeta();
		Set<String> keys = meta.keySet();
		Vector<PointingMetadata> vResult=new Vector<PointingMetadata>();
		Iterator<String> it = keys.iterator();
		while(it.hasNext()){
			String key=it.next();
			Parameter val = meta.get(key);
			if (InterpreterUtil.isInstance(StringParameter.class, val)) vResult.add(new PointingMetadata(key,(String) val.getValue()));
		}
		PointingMetadata[] result=new PointingMetadata[vResult.size()];
		//PointingMetadata[] result=new PointingMetadata[attributes.size()];
		vResult.toArray(result);
		//attributes.values().toArray(result);
		return result;*/
	}

	public void addChild(PointingMetadata child){
		set(child.getName(),child);
		//children.put(child.getName(), child);
	}
	
	public PointingMetadata[] getChildren(){
		Map<String, Dataset> sets = getSets();
		Iterator<Entry<String, Dataset>> it = sets.entrySet().iterator();
		Vector<PointingMetadata> vResult=new Vector<PointingMetadata>();
		while(it.hasNext()){
			Entry<String, Dataset> item = it.next();
			Dataset ds=item.getValue();
			//Parameter val = meta.get(key);
			if (InterpreterUtil.isInstance(PointingMetadata.class, ds)) vResult.add((PointingMetadata)ds);
		}
		PointingMetadata[] result=new PointingMetadata[vResult.size()];
		//PointingMetadata[] result=new PointingMetadata[attributes.size()];
		vResult.toArray(result);
		//attributes.values().toArray(result);
		return result;

		//PointingMetadata[] result=new PointingMetadata[children.size()];
		//children.values().toArray(result);
		//return result;
	}
	
	public String getName(){
		ArrayDataset ads = (ArrayDataset) this.get("name");
		String1d s1d=(String1d) ads.getData();
		return s1d.get(0);
		//String name=(String) getMeta().get("name").getValue();
		//if (name==null) System.out.println("Is null");
		//System.out.println(getMeta().get("name").getValue());
		//return (String) getMeta().get("name").getValue();
		//return name;
	}
	
	public boolean hasChildren(){
		PointingMetadata[] ch=getChildren();
		if (ch.length>0) return true;
		else return false;
		//Map<String, Dataset> sets = getSets();
		//if (sets.s)
		//if (children.size()>0) return true;
		//else return false;
	}
	
	public boolean hasAttributtes(){
		/*if (attributes.getRowCount()>0) return true;
		else return false;*/
		PointingMetadata[] ch=getAttributes();
		if (ch.length>0) return true;
		else return false;

		/*if (attributes.size()>0) return true;
		else return false;*/
	}
	
	public String getValue(){
		ArrayDataset ads = (ArrayDataset) this.get("value");
		String1d s1d=(String1d) ads.getData();
		return s1d.get(0);

		//return (String) getMeta().get("value").getValue();

		//return value;
	}
	
	public void setValue(String newValue){
		this.set("value", new ArrayDataset(new String1d().append(newValue)));

		//if (value)
		//getMeta().set("value", new StringParameter(newValue));

		//value=newValue;
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
				for (int z=0;z<attr.length;z++) result=result+" "+attr[z].getName()+"='"+attr[z].getValue()+"'";
				if(!getValue().equals("")) result=result+">"+getValue()+"</"+getName()+">\n";
				else{
					result=result+"/>\n";
				}
			}
		}else{
			result=result+iString+"<"+getName();
			for (int z=0;z<attr.length;z++) result=result+" "+attr[z].getName()+"='"+attr[z].getValue()+"'";
			result=result+">\n";


			for (int j=0;j<childs.length;j++){
				result=result+childs[j].toXml(indent+1);
			}
			result=result+iString+"</"+getName()+">\n";
			
		}
		return result;
	}
	
	public PointingMetadata getAttribute(String attributeName){
		try{
			String sp = (String) this.getMeta().get(attributeName).getValue();
			return new PointingMetadata(attributeName,sp);
		}catch(java.lang.NullPointerException npe){
			//npe.printStackTrace();
			return null;
		}
		//TableDataset result=table.select(table.getColumn(columnIndex).getData().where(new herschel.binstruct.util.String1dRegex(search)));
		/*TableDataset table = attributes.select(attributes.getColumn(0).getData().where(new herschel.binstruct.util.String1dRegex(attributeName)));
		if (table.getRowCount()==0) return null;
		List<Object> row = table.getRow(0);
		if (row.size()==0) return null;
		return new PointingMetadata((String)row.get(0),(String)row.get(1));*/
		//return new PointingMetadata(attributeName,(String) getMeta().get(attributeName).getValue());
		//return attributes.get(attributeName);
	}
	
	public PointingMetadata getChild(String childName){
		try{
			return (PointingMetadata) get(childName);
		}catch (java.lang.NullPointerException ne){
			return null;
		}
		//return children.get(childName);
	}
	public String toString(){
		String result=this.getName()+"="+this.getValue();
		PointingMetadata[] att = this.getAttributes();
		result=result+" Attributes[";
		for (int i=0;i<att.length;i++){
			result=result+att[i].toString();
		}
		result=result+"]";
		PointingMetadata[] ch = this.getChildren();
		result =result+" Children[";
		for (int i=0;i<ch.length;i++){
			result=result+ch[i].toString();
		}
		result=result+"]";
		return result;
	}
	public boolean equals(PointingMetadata other){
		boolean result=true;
		if (!this.getName().equals(other.getName())){
			System.out.println("Name of two metadata different");

			return false;
		}
		if (!this.getValue().equals(other.getValue())){
			System.out.println("Value of two metadata different:"+this.getValue()+" "+other.getValue());

			return false;
		}
		if (this.getAttributes().length!=other.getAttributes().length){
			System.out.println("Number of attributes different");

			return false;
		}
		if (this.getChildren().length!=other.getChildren().length){
			System.out.println("Name of children different");

			return false;
		}
		PointingMetadata[] childs=getChildren();
		for (int i=0;i<childs.length;i++){
			if (!childs[i].equals(other.getChild(childs[i].getName()))){
				System.out.println("children different");

				return false;
			}
		}
		PointingMetadata[] attr=getAttributes();
		for (int i=0;i<attr.length;i++){
			if (!attr[i].equals(other.getAttribute(attr[i].getName()))){
				System.out.println("Attributes different");

				return false;
			}
		}
		return result;
	}

	public String getDescription() {
		// TODO Auto-generated method stub
		return description;
	}

	public void setDescription(String arg0) {
		description=arg0;
		
	}
	

	/*public StringParameter asParameter(){
		StringParameter result=new StringParameter(value);
		return result;
	}*/
	public CompositeDataset asDataset(){
		return this;
		/*CompositeDataset result=new CompositeDataset();

		if (value!=null){
			MetaData meta = new MetaData();
			meta.set("value", new StringParameter(value));
			result.setMeta(meta);
		}
		
		//CompositeDataset children=new CompositeDataset();
		PointingMetadata[] ch = this.getChildren();
		for (int i=0;i<ch.length;i++){
			result.set(ch[i].getName(), ch[i].asDataset());
		}
		
		TableDataset att=new TableDataset();
		String1d attNames=new String1d();
		String1d attValues=new String1d();
		PointingMetadata[] attri = this.getAttributes();
		for (int i=0;i<attri.length;i++){
			attNames.append(attri[i].getName());
			attValues.append(attri[i].getValue());
		}
		att.addColumn(new Column(attNames));
		att.addColumn(new Column(attValues));
		result.set("attributes", att);
		return result;*/
	}
/*
	@Override
	public void accept(ParameterVisitor arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addParameterListener(ParameterListener arg0) {
		// TODO Auto-generated method stub
		
	}
*/

	/*@Override
	public Class<?> getType() {
		return String.class;
		// TODO Auto-generated method stub
		//return null;
	}*/
/*
	@Override
	public boolean hasValue() {
		if (getValue()!=null) return true;
		else return false;
		// TODO Auto-generated method stub
		//return false;
	}

	@Override
	public void removeParameterListener(ParameterListener arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setValue(Object arg0) {
		this.setValue((String) arg0);
		// TODO Auto-generated method stub
		
	}
*/
	public static PointingMetadata readFrom(Node node){
		String name=node.getNodeName();
		if (name.equals("#text")) return null;
		if (name.equals("#comment")) return null;
		//System.out.println(name);
		//String value="";
		String value=node.getTextContent();
		if (value==null) value="";
		PointingMetadata result = new PointingMetadata(name,value);

		if (node.hasAttributes()){
			NamedNodeMap att = node.getAttributes();
			int size=att.getLength();
			for (int i=0;i<size;i++){
				String attname=att.item(i).getNodeName();
				String attvalue=att.item(i).getNodeValue();
				if (attname!="" && attvalue!="") result.addAttribute(new PointingMetadata(attname,attvalue));
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
