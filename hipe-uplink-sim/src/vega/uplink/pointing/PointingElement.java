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
import herschel.share.unit.Unit;
import herschel.share.unit.impl.UnitFactory;

public class PointingElement extends CompositeDataset  {
	private String description;
	
	/**
	 * This class stores pointing metadata of a pointing block plus any kind of children of a pointing block.
	 * It has convenience method to store XML attributes, values and children.
	 */
	public PointingElement(){
		super();

		
		description="";
	}
	/**
	 * Read a pointing metadata from another pointing metadata.
	 * Basically, it copies all it attributes, children and value.
	 * It is convenient for subclasses as originally the whole ptr is read as pointed metadata and then translated into the specific classes.
	 * @param org
	 */
	public PointingElement(PointingElement org) {
		this(org.getName(),"");
		
		this.setValue(org.getValue());
		PointingElement[] ch = org.getChildren();
		for (int i=0;i<ch.length;i++){
			addChild(ch[i]);
		}
		PointingElement[] att = org.getAttributes();
		for (int i=0;i<att.length;i++){
			addAttribute(att[i]);
		}
		//return result;
	}
	/**
	 * copy all attributes, children and value from another pointing metadata into this one.
	 * @param org
	 */
	public void copyFrom(PointingElement org){
		this.setValue(org.getValue());
		PointingElement[] ch = org.getChildren();
		for (int i=0;i<ch.length;i++){
			addChild(ch[i]);
		}
		PointingElement[] att = org.getAttributes();
		for (int i=0;i<att.length;i++){
			addAttribute(att[i]);
		}
		
	}

	@Override
	public PointingElement copy() {
		PointingElement result = new PointingElement();
		result.setValue(getValue());
		PointingElement[] ch = getChildren();
		for (int i=0;i<ch.length;i++){
			result.addChild(ch[i]);
		}
		PointingElement[] att = getAttributes();
		for (int i=0;i<att.length;i++){
			result.addAttribute(att[i]);
		}
		return result;
	}

	
	/**
	 * Creates a pointing matadata with the given name and value.
	 * This is traslated into xml as <name>value</name>
	 * @param metaName
	 * @param metaValue
	 */
	public PointingElement(String metaName,String metaValue){
		this();
		this.set("name", new ArrayDataset(new String1d().append(metaName)));
		this.set("value", new ArrayDataset(new String1d().append(metaValue)));
		

	}
	
	public static StringParameter toStringParameter(PointingElement mt){
		return new StringParameter(mt.getValue());
	}
	/**
	 * Add an attribute to this pointing metadata.
	 * This is translated into XML as <name attribute.name=attribute.value>value</name>
	 * @param attribute
	 */
	public void addAttribute(PointingElement attribute){
		this.getMeta().set(attribute.getName(), toStringParameter(attribute));
		
	}
	
	/**
	 * Remove an attribute from this pointing metadata.
	 * @param attribute
	 */
	public void removeAttribute(PointingElement attribute){
		this.getMeta().remove(attribute.getName());
	}
	public void removeAttribute(String key){
		this.getMeta().remove(key);
	}
	
	/**
	 * Get all attributes of this pointing metadata
	 * @return
	 */
	public PointingElement[] getAttributes(){
		Vector<PointingElement> vAtt=new Vector<PointingElement>();
		Set<String> keySet = this.getMeta().keySet();
		Iterator<String> it = keySet.iterator();
		while (it.hasNext()){
			String metaName=it.next();
			String sp = (String) this.getMeta().get(metaName).getValue();
			vAtt.add(new PointingElement(metaName,sp));
		}
		PointingElement[] result=new PointingElement[vAtt.size()];
		vAtt.toArray(result);
		return result;
	}

	/**
	 * Add a child to this element
	 * This is translated into xml as 
	 * <name>
	 * 		<child.name> child.value</child.name>
	 * </name>
	 * @param child
	 */
	public void addChild(PointingElement child){
		set(child.getName(),child);
	}
	
	/**
	 * Get all the children of this element
	 * @return
	 */
	public PointingElement[] getChildren(){
		Map<String, Dataset> sets = getSets();
		Iterator<Entry<String, Dataset>> it = sets.entrySet().iterator();
		Vector<PointingElement> vResult=new Vector<PointingElement>();
		while(it.hasNext()){
			Entry<String, Dataset> item = it.next();
			Dataset ds=item.getValue();
			//Parameter val = meta.get(key);
			if (InterpreterUtil.isInstance(PointingElement.class, ds)) vResult.add((PointingElement)ds);
		}
		PointingElement[] result=new PointingElement[vResult.size()];
		vResult.toArray(result);
		return result;

	}
	
	/**
	 * Get the name of this element
	 * @return
	 */
	public String getName(){
		ArrayDataset ads = (ArrayDataset) this.get("name");
		String1d s1d=(String1d) ads.getData();
		return s1d.get(0);
	}
	
	/**
	 * 
	 * @return True if this element has children. False otherwise
	 */
	public boolean hasChildren(){
		PointingElement[] ch=getChildren();
		if (ch.length>0) return true;
		else return false;
	}
	
	/**
	 * @return True if this element has Attributes. False otherwise
	 */
	public boolean hasAttributtes(){
		PointingElement[] ch=getAttributes();
		if (ch.length>0) return true;
		else return false;

	}
	
	/**
	 * Get the value of this element
	 * @return
	 */
	public String getValue(){
		ArrayDataset ads = (ArrayDataset) this.get("value");
		String1d s1d=(String1d) ads.getData();
		return s1d.get(0);

	}
	
	/**
	 * Set the value of this element
	 * @param newValue
	 */
	public void setValue(String newValue){
		this.set("value", new ArrayDataset(new String1d().append(newValue)));

	}
	
	/**
	 * Write a XML representation of this element
	 * @param indent The initial indent to be applied to the XML
	 * @return
	 */
	public String toXml(int indent){
		String result="";
		String iString="";
		for (int i=0;i<indent;i++){
			iString=iString+"\t";
		}
		PointingElement[] childs=this.getChildren();
		PointingElement[] attr=this.getAttributes();
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
	
	/**
	 * Get the attribute of the given name or null if it does not exist
	 * @param attributeName
	 * @return
	 */
	public PointingElement getAttribute(String attributeName){
		try{
			String sp = (String) this.getMeta().get(attributeName).getValue();
			return new PointingElement(attributeName,sp);
		}catch(java.lang.NullPointerException npe){
			return null;
		}
	}
	
	/**
	 * Get the child of the given name or null if it does not exist
	 * @param childName
	 * @return
	 */
	public PointingElement getChild(String childName){
		try{
			return (PointingElement) get(childName);
		}catch (java.lang.NullPointerException ne){
			return null;
		}
	}
	public String toString(){
		String result=this.getName()+"="+this.getValue();
		PointingElement[] att = this.getAttributes();
		result=result+" Attributes[";
		for (int i=0;i<att.length;i++){
			result=result+att[i].toString();
		}
		result=result+"]";
		PointingElement[] ch = this.getChildren();
		result =result+" Children[";
		for (int i=0;i<ch.length;i++){
			result=result+ch[i].toString();
		}
		result=result+"]";
		return result;
	}
	public boolean equals(PointingElement other){
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
		PointingElement[] childs=getChildren();
		for (int i=0;i<childs.length;i++){
			if (!childs[i].equals(other.getChild(childs[i].getName()))){
				System.out.println("children different");

				return false;
			}
		}
		PointingElement[] attr=getAttributes();
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
	

	public CompositeDataset asDataset(){
		return this;
	}
	/**
	 * Read this element from an XML node
	 * @param node
	 * @return
	 */
	public static PointingElement readFrom(Node node){
		String name=node.getNodeName().trim();
		if (name.equals("#text")) return null;
		if (name.equals("#comment")) return null;
		//String nodeValue = node.getNodeValue();
		String value=null;
		//if (!node.hasChildNodes()) value=node.getTextContent();
		value=getFirstLevelTextContent(node);
		//if (nodeValue!=null) value=nodeValue.trim();
		if (value==null) value="";
		else value=value.trim();
		PointingElement result = new PointingElement(name,value);

		if (node.hasAttributes()){
			NamedNodeMap att = node.getAttributes();
			int size=att.getLength();
			for (int i=0;i<size;i++){
				String attname=att.item(i).getNodeName().trim();
				String attvalue=att.item(i).getNodeValue().trim();
				if (attname!="" && attvalue!="") result.addAttribute(new PointingElement(attname,attvalue));
			}
		}
		if (node.hasChildNodes()){
			NodeList children = node.getChildNodes();
			int size=children.getLength();
			for (int i=0;i<size;i++){
				PointingElement child = PointingElement.readFrom(children.item(i));
				if (child!=null) result.addChild(child);
			}			
		}
		return result;
	}
	
	public static String getFirstLevelTextContent(Node node) {
	    NodeList list = node.getChildNodes();
	    StringBuilder textContent = new StringBuilder();
	    for (int i = 0; i < list.getLength(); ++i) {
	        Node child = list.item(i);
	        if (child.getNodeType() == Node.TEXT_NODE)
	            textContent.append(child.getTextContent());
	    }
	    return textContent.toString();
	}
	

}
