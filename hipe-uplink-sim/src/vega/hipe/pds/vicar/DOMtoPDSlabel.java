/*
*
* @(#)DOMtoPDSlabel.java	1.0 00/12/15
 *
 * Steve Levoe
 * Jet Propulsion Laboratory
 * Multimission Image Processing Laboratory
 * 12-2000 ImageIO EA2 versionsv.length
 * 8-2002 JDK1.4 version
* sept 2005 - Steve Levoe
* prevent lines over 80 chars
***************************************/
package vega.hipe.pds.vicar;
// used to be in jpl.mipl.io.codec

import org.w3c.dom.Document;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.*;

import javax.xml.parsers.*;

import java.io.IOException;
import java.util.*;

import javax.imageio.metadata.*;

// import VicarIO stuff
// VicarInputFile  SystemLabel
//import jpl.mipl.io.streams.*;
//import jpl.mipl.io.vicar.*;
//import jpl.mipl.io.plugins.*;
//import jpl.mipl.io.util.*;

/**
 * This class creates a text label for a PDS image file<BR>
 * 
 * This will be called from inside a writer.
 * The Document used to drive the output may have come from a reader,
 * transcoder, or be created programatically.
 * <BR>
 * This class may become more general purpose. Either as a class to subclassed 
 * for specific formats. OR if the parameters to define output can be 
 * generalized, then an xml config file will be used to set it up for each
 * specific case.
 * @version 1.0
 */
public class DOMtoPDSlabel {
     
    private boolean debug = false;
	// private boolean debug = true;
    
    private static final int indentMax = 35; // 32
    private static final int wrapStart = 38; // 35
    private static final int lineMax   = 80; // 80
    private static final int valueMax  = 40; // 43
    /* make these values configurable? with the above as defaults? */
    private static final int longValueMax = 76;
    
    /************************************************
    lineMax is the maximum number of characters allowed on a line
    lineMax INCLUDES the CR LF end of line. Therefore 78 prinatble characters
    are allowed on a line.
    if the line would be over lineMax the text is chopped at the
    nearest word boundary. This wrapped line will begin at wrapStart
    indentMax is the number of characters keywords will have including spaces
    before the = sign
    valueMax is the maximum number of charatcters in the value portion of a
    line.
    longValueMax is the length of an unusually long word portion which will be broken
    into parts, a hyphen will end an part to indicate continuation
    
    example:
                                   
    PRODUCER_INSTITUTION_NAME      = "MULTIMISSION IMAGE PROCESSING LABORATORY,
                                      JET PROPULSION LAB"
                                  ^
                                  indentMax
    <-------+indentMax+---------+->                              
                                                                               ^
                                                                         lineMax
                                      ^
                                      wrapStart
                                      
                                      ^                                        ^
                                      <-------+ valueMax+---------+---------+-->
                                      
    *******************************************/
    
    // something to indicate if there are or are not spaces around the =
    boolean equalHasSpaces = true;
    // hasPad indicates if there is space padding before the equal sign
    // if hasPad is false then the equal sign is immediately after the keyword
    // then the value will be printed
    boolean hasPad = true;
    
    boolean imageObjectFound = false;
    
    // String LineEnd = "\n";  // PDS
    String LineEnd = "\r\n";  // PDS
    // String LineEnd = "\n\r";  // PDS
    // String LineEnd = "  ";  // vicar (spaces)
    
    // --- end of formatting values ---
    boolean inObject = false;
    private Document _document = null;
    
    int labelRecordCt = 0;
    int imageStartRecord = 0;
    int imageLabelByteCt = 0;
    int record_bytes = 0;
    int labelByteCt = 0;
    int imageLines = 0;
    int labelSize = 0;
    int imageSize = 0;
    int bands = 1;
    int fileRecords = 0; // this is total number of records in the file, includes all labels and image
    int imageRecords = 0; // this is the number of records in the image
    
    String pds_ptr = null; // used to set and/or override the values of POINTER objects in the label
    // usefull when the vicar label doesn't provide enough info
    
    boolean calculatingSize = false;
    
    String outputFilename = null;
    String band_storage_type = "BAND_SEQUENTIAL";
    
    // string to hold the output
    boolean outputToString = false;
    String outputString = null;
    StringBuffer sb = null;
    
    
    String vicarLabelString = "";
    int vicarLabelBytes = 0;
    int vicarLabelRecordCt = 0;
    int vicarLabelStartRecord = 0;
    int vicarLabelStartByte = 0;
    
    // PDS detached label support items
    String inputFilename = null;
	boolean detachedLabelOnly = false;
	boolean detachedLabel = false;
	// values of the input vicar image
	int inputVicarLabelRecordCt = 0;
	int inputVicarRecordLength = 0;
	int inputVicarFileRecords = 0; 
    
    static final int ELEMENT_TYPE =   1;
    static final int ATTR_TYPE =      2;
    static final int TEXT_TYPE =      3;
    static final int CDATA_TYPE =     4;
    static final int ENTITYREF_TYPE = 5;
    static final int ENTITY_TYPE =    6;
    static final int PROCINSTR_TYPE = 7;
    static final int COMMENT_TYPE =   8;
    static final int DOCUMENT_TYPE =  9;
    static final int DOCTYPE_TYPE =  10;
    static final int DOCFRAG_TYPE =  11;
    static final int NOTATION_TYPE = 12;
    
    // Constructor
    public DOMtoPDSlabel(Document d) {
        _document = d;
    }
    
    public DOMtoPDSlabel(Document d, String fname) {
        _document = d;
        outputFilename = fname;
    }
    
    public void setOutputFilename(String fname) {
        outputFilename = fname;
    }
    
    public int getLabelSize() {
        return labelSize;
    }
    
    public int getImageSize() {
        // imageSize = record_bytes * imageLines ;
        return imageSize ;       
    }
    
    public void setVicarLabelString(String  s)  {
    	vicarLabelString = s;
    }
    
    public String getVicarLabelString( )  {
    	return vicarLabelString ;
    }
    
    public void setDebug(boolean d) {
    	
    	debug = d;
    }
    
    public void setPds_ptr(String s) {
    	pds_ptr = s;
    }
    
    /**
     * places these setters are used *
    dom2PDS.setOutputFilename(outputFilename);
	dom2PDS.setInputFilename(inputFilename);
	dom2PDS.setDetachedLabelOnly(true);
	
	dom2PDS.setVicarLabelRecordCt(vicarLabelRecordCt);
	dom2PDS.setRecordLength(recordLength);
	*****/
    
    public void setInputFilename( String ifn) {
    	inputFilename = ifn;
    }
    public void setDetachedLabelOnly(boolean f) {
    	detachedLabelOnly = f;
    }
    public void setDetachedLabel(boolean f) {
    	detachedLabel = f;
    }
    public void setInputVicarLabelRecordCt (int i) {
    	inputVicarLabelRecordCt = i;
    }
    public void setInputVicarRecordLength( int i) {
    	inputVicarRecordLength = i;
    }
    public void setInputVicarFileRecords( int i) {
    	inputVicarFileRecords = i;
    }
    
    public void display() {
        
        Node root = _document.getDocumentElement();
        // get a few values we will need to use in calculations
        DOMutils domUtils = new DOMutils();
        // domUtils.setDebug(true);
        String xPath = "//item[@key='BANDS']" ;
        
        // more specific xpath expressions
        // String xPath = "//IMAGE/item[@key='BANDS']" ;
        // String xPath = "//IMAGE_OBJECT/item[@key='BANDS']" ;
        String nodeValue = domUtils.getItemValue(root, xPath);
        if (debug) System.out.println("PDSInputFile "+xPath+"  >"+nodeValue+"<");
        if (nodeValue == "") {
        	xPath = "//item[@name='BANDS']" ;
        	nodeValue = domUtils.getItemValue(root, xPath);
        	// if (debug) System.out.println("PDSInputFile "+xPath+"  >"+nodeValue+"<");        	
        }
        
        
        bands = Integer.parseInt(nodeValue);
        if (debug) System.out.println("PDSInputFile.display() BANDS="+bands);
        
        
        xPath = "//item[@key='BAND_STORAGE_TYPE']" ;
        nodeValue = domUtils.getItemValue(root, xPath);
        // if (debug) System.out.println("PDSInputFile "+xPath+"  "+nodeValue);
        
        if (nodeValue == "") {
        	xPath = "//item[@name='BAND_STORAGE_TYPE']" ;
        	nodeValue = domUtils.getItemValue(root, xPath);       	
        	// if (debug) System.out.println("PDSInputFile "+xPath+"  >"+nodeValue+"<");
        }
        // for a single banded imge this keyword may not be in the label
        if (nodeValue != "") {  
        	band_storage_type = nodeValue; 
        }   
        
        if (debug) System.out.println("PDSInputFile.display() BAND_STORAGE_TYPE="+band_storage_type);
        
        // now go thru the tree and print out values
        displayMetadata(root);
        String ihos = imageHeaderObject() ;
        if (ihos != null) {
        	sb.append(ihos);
        }
        
        String ides = imageObject() ;
        if (ides != null) {
        	sb.append(ides);
        }
        
    }
    
    void displayMetadata(Node root) {
        if (root == null) {
            System.out.print("null metadata");
        }
        else {
            displayMetadata(root, 0);
        }
    }

    public String toString() {
        outputToString = true;
        outputString = null;
        calculatingSize = true;
        sb = new StringBuffer();
        
        // boolean sDebug = debug;
        // debug = true;
        
        /*
         * go thru the process twice, the first time calculatingSize is true. The values for 
         * the label size items is calculated. The second time thru these values are 
         * used in the label creation         */
         if (debug) {
			System.out.println("*** DOMtoPdsLabel.toString() ****");    
         	System.out.println("****************************************************");       	
         	System.out.println("    start calculating label sizes ******************");
         	System.out.println("    record_bytes "+record_bytes );
         	System.out.println("****************************************************");
         }
        
        // debug = sDebug;       
        display();
        // debug = true;
		
        outputToString = false;
        // return outputString;
        sb.append("END"+LineEnd);
        String s = sb.toString();
        // calculate the number of RECORDS in the label
        // this will NOT include the Vicar Label
        labelByteCt = s.length();
        if (debug) {
			System.out.println("*** DOMtoPdsLabel.toString() ****");       	
         	System.out.println("    labelByteCt "+labelByteCt );
         	System.out.println("    record_bytes "+record_bytes );
        }
        
        labelRecordCt = labelByteCt / record_bytes ;
        
        // labelSize = (labelRecordCt + 1) * record_bytes ;
		labelSize = labelRecordCt * record_bytes ;
        imageSize = record_bytes * imageLines ;
        
        
        
        int z = labelSize - labelByteCt;
        if (debug) {
        	System.out.println("****************************************************");
        	System.out.println("record_bytes="+record_bytes +"  labelSize="+labelSize+"  z="+z);
        	System.out.println("labelByteCt="+labelByteCt+"  labelRecordCt="+labelRecordCt );
        }
        
        
        
		while (z < 0) {
					// add records until it is the right size
        	
					labelRecordCt++;
					// padCt = record_bytes + padCt; 
					labelSize = labelRecordCt  * record_bytes ;
					z = labelSize - labelByteCt;
				}
		/***
		 * // add at least 300 bytes to the label size
		 * so there is space for a few values to be added next time thru
		 */
		int recordsToAdd = 300/record_bytes;
		// recordsToAdd = 0;
		if (recordsToAdd < 1) {
			recordsToAdd = 1;
		}
		// if (z < 100) {
		int bytesToAdd = recordsToAdd * record_bytes ;
		labelRecordCt += recordsToAdd ;
		/**
		if (recordsToAdd)
			labelRecordCt += recordsToAdd * 2;
			}
		else {
			labelRecordCt += recordsToAdd ;
			}
		**/
		/*
        if (z < 100) {
            labelRecordCt += 2;
        }
        else {
            labelRecordCt++;
        }
        ***/
        
        /// checking for bug
        imageStartRecord = labelRecordCt + 1;
        // imageStartRecord = labelRecordCt ;
        if (debug) {
        	System.out.println("imageStartRecord="+imageStartRecord+"  labelRecordCt="+labelRecordCt);
			System.out.println("recordsToAdd="+recordsToAdd+"   bytesToAdd "+bytesToAdd);
        }
        	
        
        labelSize = labelRecordCt * record_bytes ;
        vicarLabelStartByte = labelSize ; // start the vicar label after the PDS label
        // vicarLabelStartByte++;
        
        // record_bytes is set during the read thru the label
        
        // later we must add the size of an embedded Vicar Label
        
        if (debug) {
        	System.out.println("imageStartRecord="+imageStartRecord+"  labelRecordCt="+labelRecordCt);
        	System.out.println("vicarLabelStartByte "+vicarLabelStartByte);
        	System.out.println("    start REAL label *******************************");
        	System.out.println("****************************************************");
        }
        
        sb = null;
        // start over -----------------------------------
        // build the real label to be written toi the file
        
        calculatingSize = false; // now the correct values will be filled in
        sb = new StringBuffer();
        
		// debug = sDebug;
        display();
        // debug = true;
		
        outputToString = false;
        // return outputString;
        
        sb.append("END"+LineEnd);
        // get the size of this string, add padding so that it is filled out to
        // labelRecordCt * record_bytes
        s = sb.toString();
        labelByteCt = s.length();
        
        labelSize = labelRecordCt * record_bytes ;
        
        // create a pad string, add it to the label
        int padCt = labelSize - labelByteCt;
        
        
        
        
        
        if (debug) {
        	System.out.println("labelRecordCt="+labelRecordCt+ " labelSize="+labelSize+"  imageLines="+imageLines);
        	System.out.println( labelSize + " - " + labelByteCt+ " = "+padCt);
        	System.out.println("padCt="+padCt+"   labelSize="+labelSize+" labelByteCt="+labelByteCt);
        	// these were calculated the first time thru display() above
        	System.out.println("vicarLabelBytes "+vicarLabelBytes);
        	System.out.println("vicarLabelRecordCt "+vicarLabelRecordCt);
        	System.out.println("vicarLabelStartRecord "+vicarLabelStartRecord);
        	System.out.println("vicarLabelStartByte "+vicarLabelStartByte);
        	System.out.println("imageStartRecord="+imageStartRecord);
			System.out.println("record_bytes="+record_bytes);
        	
        }
        
        
        while (padCt < 0) {
        	// add a full record more ??
        	
        	labelRecordCt++;
        	// padCt = record_bytes + padCt; 
			labelSize = labelRecordCt * record_bytes ;
			padCt = labelSize - labelByteCt;
        	
        	padCt = labelSize - labelByteCt;
        }
        
		if (debug) {
			System.out.println("************************************************");
			System.out.println("labelRecordCt="+labelRecordCt+ " labelSize="+labelSize+"  imageLines="+imageLines);
			System.out.println( labelSize + " - " + labelByteCt+ " = "+padCt);
			System.out.println("padCt="+padCt+"   labelSize="+labelSize+" labelByteCt="+labelByteCt);
			System.out.println("************************************************");
		}
        // end padding goes after the PDS label and Before the Vicar Label
        String p = endPadding(padCt);
        
        if (vicarLabelRecordCt != 0 && vicarLabelString != null) {
        	s = s + p + vicarLabelString;
        	// labelSize += (vicarLabelRecordCt * record_bytes);
        	labelSize += vicarLabelBytes;
        }
        else {
        	s = s + p;
        }
        sb = null;
        
        // debug = sDebug;
        
        return s;
    }
    
    void indent(int level) {
        for (int i = 0; i < level; i++) {
            if (debug) System.out.print(" ");
            if (sb != null) { sb.append(" ") ; }
        }
    }

    String getWrapStart() {
        String r = "";
        for (int i=1 ; i < wrapStart ; i++ ) {
            r = r+" ";
        }
       return r; 
    }
    
    String getPadding(String s, int indent) {
        int x = indentMax - s.length();
        x -= indent;
        // x -= indent; // indent is 2 spaces
        // String r = x+"";
        // make it the correct size to avoid resizing time
        StringBuffer sbb = new StringBuffer(x);
        if (sbb == null) {
        	return " ";
        }
        else {
        	for (int i = 1; i <x; i++) {
            	// System.out.print(" ");
            	if (sbb != null) { sbb.append(" ") ; }
        	}
        	String r = sbb.toString();
        	return r;
        }
        
        /**
        String r = "";
        for (int i=1 ; i < x ; i++ ) {
            r = r+" ";
        }
       return r; 
       **/
    }
    
    /**
     * Constructs a String of spaces to pad a print line
     * @param x int, number of spaces to put in the pad String
     * @return a String with spaces
     * 
     */
    String getValuePadding(int x) {
        
    	if (x <= 0) {
    		return "";
    	}
        // make it the correct size to avoid resizing time
        StringBuffer sbb = new StringBuffer(x);
        if (sbb == null) {
        	return "";
        }
        else {
        	for (int i = 0; i <x; i++) {
            	// System.out.print(" ");
            	if (sbb != null) { sbb.append(" ") ; }
        	}
        	String r = sbb.toString();
        	return r;
        }
        
    }
    
    /**
     * 
     * @param ct
     * @return
     */
    String endPadding(int ct) {
    	// make it the correct size to avoid resizing time
        StringBuffer sbb = new StringBuffer(ct);
        for (int i = 0; i < ct; i++) {
            // System.out.print(" ");
            if (sbb != null) { sbb.append(" ") ; }
        }
        String s = sbb.toString();
        return s;
    }
    
    /**
     * Breaks a String into a vector where each element is a word.
     * Uses spaces and commas to break up the String.
     * If a word is longer than valueMax characters then the word is broken into pieces
     * that are valueMax long. A hyphen is added to the end of the broken up words.
     * The hypen is printed in the label and should be ignored by PDS label readers as
     * a line continuation character.
     * @param value The String to be chopped up
     * @return A Vector of all the words
     */
    Vector chopValueString(String value) {
    String token, subToken;
    String s;
    Vector v = new Vector();;
    int len = 0;
    int tokLen = 0;
    int subTokLen = 0;
    int count = 0;
    StringBuffer sb = new StringBuffer();
    // this breaks the String on spaces, spaces are NOT included
    // StringTokenizer st = new StringTokenizer(value);
    if (debug) System.out.println("### chopValueString "+value);
    StringTokenizer st ;
    // returns the delimiters as tokens
    if (value.indexOf(',') != -1) {
    	st = new StringTokenizer(value,",",true); // tokenize on commas
    }
    else {
    	st = new StringTokenizer(value," ",true); // tokenize on spaces
    }
    while (st.hasMoreTokens() ) {
	    token = st.nextToken();
	    tokLen = token.length();
	    
	    if (sb.length() == 0) {
	    	if (tokLen >= longValueMax) {
	    		
	    		int ti = 0;
	    		int endi = ti + longValueMax ;
	    		if (debug) {
	    		  System.out.println("#### token="+token+" tokLen="+tokLen);
	    		  System.out.println(" ### ti="+ti+"  endi="+endi);
	    		}
	    		
	    		do { 	    			
	    			subToken = token.substring(ti,endi);
	    			sb = new StringBuffer(subToken);
		    				    		
	    			
	    			ti += longValueMax ;
	    			if (ti > tokLen) {
	    				ti = tokLen;
	    			}
	    			endi += longValueMax;
	    			if (endi > tokLen) {
	    				endi = tokLen;
	    			}
	    				
	    			if (ti == endi) {
	    				// v.addElement(sb.toString());
	    				// this one gets added later
	    				if (debug) {
	    				 System.out.println("  ##  subToken="+subToken+" tokLen="+tokLen);
				    	 System.out.println("  ##  ti="+ti+"  endi="+endi+"  tokLen-endi="+(tokLen-endi));
	    				}
	    			} else {
	    				v.addElement(sb.toString()+"-");
	    				if (debug) {
	    				  System.out.println("  ##- subToken="+subToken+" tokLen="+tokLen);
	    				  System.out.println("  ##- ti="+ti+"  endi="+endi+"  tokLen-endi="+(tokLen-endi));
	    				}
	    			}
	    			
	    				    				    		
	    		} while (ti < tokLen) ;
	    		
	    	} else {  	
	    		sb = new StringBuffer(token);
	    		
	    	}
	    	
	    } else if ((sb.length() + tokLen ) < valueMax) {
	        // sb.append(" "+token);
	        sb.append(token);  // spaces and commas are now tokens
	    } else { 
	    	
	    	if (tokLen >= longValueMax) {
	    		
	    		int ti = 0;
	    		int endi = ti + longValueMax ;
	    		if (debug) {
	    		  System.out.println("###$ token="+token+" tokLen="+tokLen);
	    		  System.out.println(" ##$ ti="+ti+"  endi="+endi);
	    		}
	    		
	    		do { 	    			
	    			subToken = token.substring(ti,endi);
	    			sb = new StringBuffer(subToken);
		    				    		
	    			
	    			ti += longValueMax ;
	    			if (ti > tokLen) {
	    				ti = tokLen;
	    			}
	    			endi += longValueMax;
	    			if (endi > tokLen) {
	    				endi = tokLen;
	    			}
	    				
	    			if (ti == endi) {
	    				// v.addElement(sb.toString());
	    				// this one gets added later
	    				if (debug) {
	    				  System.out.println("  ##$  subToken="+subToken+" tokLen="+tokLen);
				    	  System.out.println("  #$  ti="+ti+"  endi="+endi+"  tokLen-endi="+(tokLen-endi));
	    				}
	    			} else {
	    				v.addElement(sb.toString()+"-");
	    				if (debug) {
	    				  System.out.println("  #$- subToken="+subToken+" tokLen="+tokLen);
	    				  System.out.println("  #$- ti="+ti+"  endi="+endi+"  tokLen-endi="+(tokLen-endi));
	    				}
	    			}
	    			
	    				    				    		
	    		} while (ti < tokLen) ;
	    		
	    	} else {  	
	    		v.addElement(sb.toString());
	    		sb = new StringBuffer(token);
	    	}
	    }
    }
    v.addElement(sb.toString());
    return v;
    }


 /*
  * Used to get the value from an "item" or "subitem" node
  * Will look for attributes to create a String with the value
  * assume this is an item with NO subitems 
  * OR it is a subitem
  * */
   private String getNodeValueString(Node node) {
   	
   		String nodeName = node.getNodeName();
   		String value = "";
   		if (nodeName.equalsIgnoreCase("item") || nodeName.equalsIgnoreCase("subitem") ) {
   			// continue processing
   		} else {
   			// Visit the children recursively
            Node child = node.getFirstChild();
            String childName = "";
            String nodeValue = null;
            if (child != null) {
                nodeValue = "";
                // System.out.println(">");
                while (child != null) {
                    int type = child.getNodeType();
                    // displayMetadata(child, level + 1);
                    if (type == ELEMENT_TYPE) {
                    	// check for "subitem" get value from the text node
                    	childName = child.getNodeName();
                    	if (childName.equalsIgnoreCase("subitem") ) {
                    		return null;
                    	}
                    	else {
                    		return null; 
                    	}
                    }
                    else if (type == TEXT_TYPE) {
                       nodeValue = nodeValue+child.getNodeValue(); 
                    } else if (type == CDATA_TYPE) {
                        nodeValue = nodeValue+child.getNodeValue(); 
                    }
                    child = child.getNextSibling();
                }
                
            } else {
                nodeValue = "?";
            }
   			return nodeValue;
   		}
   		
   		NamedNodeMap map = node.getAttributes();
   		
   		String nodeValue = "";
   		String attrKey = "";
   		String   attrQuotedValue = null;
   		String attrUnitValue = null;
         if (map != null) {
           	int length = map.getLength();
            for (int i = 0; i < length; i++) {
                Node attr = map.item(i);
                String attrNodeName = attr.getNodeName();
                if (attrNodeName.equalsIgnoreCase("key")) {
                    attrKey = attr.getNodeValue();
                	}
               	else  if (attrNodeName.equalsIgnoreCase("name")) {
                    attrKey = attr.getNodeValue();
                	}
                	
                if (attrNodeName.equalsIgnoreCase("quoted")) {
                    attrQuotedValue = attr.getNodeValue();
               	}
               	
               	if (attrNodeName.equalsIgnoreCase("unit") || attrNodeName.equalsIgnoreCase("units")) {
                    attrUnitValue = attr.getNodeValue();
               	}
             } 
          // get value from the text or CDATA node
         }
         else {
         // do nothing, no attributes
         }
       
        
         // Visit the children recursively
            Node child = node.getFirstChild();
            String childName = "";
            if (child != null) {
                nodeValue = "";
                // System.out.println(">");
                while (child != null) {
                    int type = child.getNodeType();
                    // displayMetadata(child, level + 1);
                    if (type == ELEMENT_TYPE) {
                    	// check for "subitem" get value from the text node
                    	childName = child.getNodeName();
                    	if (childName.equalsIgnoreCase("subitem") ) {
                    		return null;
                    	}
                    	else {
                    		return null; 
                    	}
                    }
                    else if (type == TEXT_TYPE) {
                       nodeValue = nodeValue+child.getNodeValue(); 
                    } else if (type == CDATA_TYPE) {
                        nodeValue = nodeValue+child.getNodeValue(); 
                    }
                    child = child.getNextSibling();
                }
                
            } else {
                nodeValue = "";
            }
        
        // nodeValue is just the text part
        
        // now add any units and quotes
        if (attrQuotedValue != null && attrQuotedValue.equalsIgnoreCase("true") ) {
        	value = "\""+nodeValue+"\"" ;
        }
        else {
        	value = nodeValue;
        }
        
         if (attrUnitValue != null ) {
         	// something with Units will NEVER have quotes ???
         	// add a space between the value and the <unit>
        	value = value+" <" +attrUnitValue+">";// append the units value (capitalize ???)
        }
        
        
   return value;	
   }
   
   /* 
    * creates a HEADER OBJECT for the embeded Vicar Label
    *     */
    private String imageHeaderObject() {
    	String s = "";
    	if (vicarLabelString == null || vicarLabelString.length() == 0 ) {
    		return s;
    	}
    	if (debug) System.out.println("imageHeaderObject ###############################");
    	if (vicarLabelString != null && vicarLabelBytes > 0) {
    		if (debug) System.out.println("imageHeaderObject vicarLabelBytes="+vicarLabelBytes);
    		StringBuffer _sb = new StringBuffer();
    		// should really use getPadding to be consistent
    		// should also really add this stuff to the DOM ???
    		_sb.append(LineEnd); 
            _sb.append("/* IMAGE HEADER DATA ELEMENTS */"+LineEnd); 
            _sb.append(LineEnd); 
            _sb.append("OBJECT                         = IMAGE_HEADER"+LineEnd); 
            _sb.append(" HEADER_TYPE                   = VICAR2"+LineEnd);
            _sb.append(" INTERCHANGE_FORMAT            = ASCII"+LineEnd);
            _sb.append(" BYTES                         = "+vicarLabelBytes+LineEnd);
            _sb.append(" ^DESCRIPTION                  = \"VICAR2.TXT\""+LineEnd);
            _sb.append("END_OBJECT                     = IMAGE_HEADER"+LineEnd); 
            // _sb.append(LineEnd);   
            // indent(level);
            // nodeName = "LABEL_RECORDS";
            // pad = getPadding(nodeName, level);
            // if (sb != null) { sb.append(nodeName+pad+"= "+labelRecordCt+LineEnd);  }
            
            s = _sb.toString(); 		
    	}
    	return s;
    }
    
    /*
     * creates a String with an OBJECT = IMAGE <br>
     * If the input image data has not been modified in any way there will
     * be an IMAGE OBJECT in the Document passed to this class. It will usually
     * include some calculated image statistics. <br>
     * In the case where no IMAGE OBJECT is in the input Document one will be
     * created here to be added to the PDS label.     */
    private String imageObject() {
    	String s = "";
    	if (imageObjectFound) {
    		// this is only valid if the images data has not been modified in any way
    	// all the values were calculated in the input image
    		return s;
    	}
    	else {
    		// build up an IMAGE OBJECT for this image
    		StringBuffer _sb = new StringBuffer();
    		_sb.append(LineEnd); 
            _sb.append("/* IMAGE DATA ELEMENTS */"+LineEnd); 
            _sb.append(LineEnd);
            _sb.append("OBJECT                         = IMAGE"+LineEnd); 
            _sb.append(" PLACEHOLDER                   = BLAH BLAH BLAH"+LineEnd);
            _sb.append("END_OBJECT                     = IMAGE"+LineEnd);  
            // s = _sb.toString(); 	
    	}
    	
    	return s;
    }
    
    /**
     * Creates a hash table of key, value pairs froma pds_ptr string
     * The String is expected to be 1 or more "key=value" pairs
     * If there are more than one they will be separated by a comma
     * @param s
     * @return
     */
    private Hashtable getPds_ptrHash(String s) {
    	
    	Hashtable h = new Hashtable();
    	// remove any parens 
    	s = s.replaceFirst("\\(","");
    	s = s.replaceFirst("\\)","");
    	// remove all whitespace 
    	s = s.replaceAll(" ","");
    	
    	// System.out.println("getPds_ptrHash( "+s) ;
    	if (s != null) {
    		// split on commas,
    		StringTokenizer st1 = new StringTokenizer(s, ",", false); // return delimiter
    		while (st1.hasMoreTokens()) {
    			String s1 = st1.nextToken();
    			// System.out.println("  token "+s1);
    			// split on =
    			StringTokenizer st = new StringTokenizer(s1, "=", false); 
    			// make a hash from each pair
    			int ct = st.countTokens();
    			if (ct == 2) {
    				String key = st.nextToken();
    				String value = st.nextToken();
    				// System.out.println("key "+key+" = "+value);
    				h.put(key,value);
    			}

    		}
    		
    		
    	}
    	
    	return h;
    	
    }
    
    /**
     * c80 debug method for 80 charater line fix
     * @param msg
     * @param nodeName
     * @param nodeValue
     * @param wrapStart
     * @param pad
     */
    private void c80(String msg, String nodeName, String nodeValue, int wrapStart, String pad) {
    	
    	if (debug == false) return ;
    	
    	int vlen = nodeValue.length();
    	int plen = pad.length();
    	int nlen = nodeName.length();
    	int x = 0;
    	int wx = 0;
    	
    	
    	 
    	wx = wrapStart+ vlen;
    	
    	x = nlen+plen+2+vlen;
    	
    	// TELEMETRY_SOURCE_NAME
    	// if (x > 80 || wx > 80) {
    	if (nodeName.startsWith("TELEMETRY_") ||
    			nodeName.equalsIgnoreCase("SPICE_FILE_NAME") ||
				nodeName.equalsIgnoreCase("PRODUCT_ID") ||
				nodeName.startsWith("INST_CMPRS_SEG") ||
				nodeName.equalsIgnoreCase("DATA_SET_ID") ) 
    	    {
    		
    		
    		System.out.println("c80 "+msg+" "+nodeName+" = "+nodeValue);
    		System.out.println("  x="+x+" wx="+wx+"  wrapStart="+wrapStart+"  plen="+plen+"  vlen="+vlen);
    		// System.out.println(" ");
    	}
    	
    }
    
    
    private void displayMetadata(Node node, int level) {
        // Print node name and attribute names and values
        // indent(level);
        // System.out.println("<" +level+"> "+ node.getNodeName());
        String nodeName = node.getNodeName();
        String nodeValue = node.getNodeValue();
        String pad, vpad ;
        
        String attrNodeName = null;
        String attrKey = null;
        String attrNodeValue = null;
        String attrQuotedValue = null;
        String attrUnit = null;
        
        String objectName = null;
        
        
        // String attr
        
        // System.out.println("displayMetadata debug="+debug);
        
        if (nodeName.equalsIgnoreCase("PDS_VERSION_ID") ) {
            
            level = 0;
            Node child = node.getFirstChild();
            if (child != null) {
                nodeValue = "";
                while (child != null) {
                    int type = child.getNodeType();
                    // displayMetadata(child, level + 1);
                    if (type == TEXT_TYPE) {
                       nodeValue = nodeValue+child.getNodeValue(); 
                    } else if (type == CDATA_TYPE) {
                        nodeValue = nodeValue+child.getNodeValue(); 
                    }
                    child = child.getNextSibling();
                }
                
            } else {
                nodeValue = "?";
            }
            
            pad = getPadding(nodeName, 0);
            if (debug) System.out.println(nodeName+pad+"= "+nodeValue);
            if (sb != null) { sb.append(nodeName+pad+"= "+nodeValue+LineEnd); }
            
            if (sb != null) { 
            	sb.append(LineEnd); 
            	sb.append("/* FILE DATA ELEMENTS */"+LineEnd); 
            	sb.append(LineEnd); 
            	}
        }
        else if (nodeName.equalsIgnoreCase("OUTPUT_FILENAME") ) {
            
            level = 0;
            Node child = node.getFirstChild();
            if (child != null) {
                nodeValue = "";
                while (child != null) {
                    int type = child.getNodeType();
                    // displayMetadata(child, level + 1);
                    if (type == TEXT_TYPE) {
                       nodeValue = nodeValue+child.getNodeValue(); 
                    } else if (type == CDATA_TYPE) {
                        nodeValue = nodeValue+child.getNodeValue(); 
                    }
                    child = child.getNextSibling();
                }
                
            } else {
                nodeValue = "?";
            }
            
            /**
            * it will probably be better if the outputFilename is 
            * set directly. That way it will be set at the time of writing the file
            * The filename should be the one used when the file is written
            ***/
            pad = getPadding(nodeName, 0);
            if (debug) {
            	System.out.println("OUTPUT_FILENAME node");
            	System.out.println(nodeName+pad+"= "+nodeValue);
            }
            if (outputFilename == null) {
                outputFilename = nodeValue;
            }
        }
        else if (nodeName.equalsIgnoreCase("IMAGE_START_RECORD") ) {
            /*
            * This is really a placeholder to tell us when to put this info
            * into the output label
            * "^IMAGE" WILL BE OUTPUT the ^ is an illegal character in an Element name
            ***/
        	int image_ptr = -1;
        	int image_header_ptr = -1;
        	Hashtable pds_ptrs_hash=null;
        	if (debug) {
        	System.out.println("########################################");
        	System.out.println("##");
        	System.out.println("########################################");
        	System.out.println("IMAGE_START_RECORD");
        	System.out.println("pds_ptr "+pds_ptr);
        	System.out.println("########################################");
        	}
        	
        	/* 
             * add in a Vicar Label pointer if there is a String set for the Vicar Label             */
             if (sb != null) { 
             	sb.append(LineEnd) ;
             	sb.append("/* POINTERS TO DATA OBJECTS */"+LineEnd);
             	sb.append(LineEnd);
             }
             
        	if (pds_ptr != null) {
        		// split the string into keys and values
        		pds_ptrs_hash = getPds_ptrHash(pds_ptr);
        	}
        	Enumeration keys = null;
        	if (pds_ptrs_hash != null) {
        		keys = pds_ptrs_hash.keys();
        		while (keys.hasMoreElements()) {
        			String key = (String) keys.nextElement();
        			
        			String value = (String) pds_ptrs_hash.get(key);
        			if (debug) System.out.println("# "+key+" = "+value);
        			if (key.equalsIgnoreCase("IMAGE")){
        			   image_ptr = Integer.parseInt(value);
        			   // now remove it from the has
        			   // pds_ptrs_hash.remove(key);
        			   if (debug) System.out.println("image_ptr = "+image_ptr);
        			}
        			else if (key.equalsIgnoreCase("IMAGE_HEADER")){
         			   image_header_ptr = Integer.parseInt(value);
         			   // now remove it from the has
         			   // pds_ptrs_hash.remove(key);
         			  if (debug) System.out.println("image_header_ptr = "+image_header_ptr);
         			 if (vicarLabelString == null && detachedLabelOnly) {   
         			 	nodeName = "^"+key ;
     	            	pad = getPadding(nodeName, 0); // this should be at level 0
                		sb.append(nodeName+pad+"= (\""+inputFilename+"\","+value+")"+LineEnd);              	      			 	
         			 }
         			  
         			} else {
         				// add an item for this pointer
         				
         				nodeName = "^"+key ;
     	            	pad = getPadding(nodeName, 0); // this should be at level 0
         				if (detachedLabelOnly) {                   		
                    		// this is the record the image starts at (1 is the first record, not 0)
                    		sb.append(nodeName+pad+"= (\""+inputFilename+"\","+value+")"+LineEnd); 
                    	}
                        else if (outputFilename != null) {
                            sb.append(nodeName+pad+"= (\""+outputFilename+"\","+value+")"+LineEnd); 
                            }
                        else {
                            sb.append(nodeName+pad+"= "+value+LineEnd); 
                             }
         			}
        		}
        	}
            
             // add all the pointers passed in
             
             
             if (vicarLabelString != null && vicarLabelString.length() != 0)  {
             	vicarLabelBytes = vicarLabelString.length();
             	// we could check LBLSIZE too to be sure, it would also confirm this is
             	String lbl = vicarLabelString.substring(0,15);
             	if (debug) System.out.println("lbl="+lbl);
             	// parse out the size from here as a check
             	// a valid vicar label
             	vicarLabelRecordCt = vicarLabelBytes / record_bytes;
             	vicarLabelStartRecord = imageStartRecord;
             	imageStartRecord += vicarLabelRecordCt ;
             	if (debug) {
             		System.out.println("IMAGE_START_RECORD ");
             		System.out.println("vicarLabelStartRecord="+vicarLabelStartRecord);
             		System.out.println("vicarLabelRecordCt="+vicarLabelRecordCt);
             		System.out.println("vicarLabelBytes="+vicarLabelBytes+"record_bytes="+record_bytes);
             	}
             	nodeName = "^IMAGE_HEADER" ;
            	pad = getPadding(nodeName, 0); // this should be at level 0
            	if (detachedLabelOnly) {
            		vicarLabelStartRecord = 1; // vicar label is the start of inputFilename
            		// records start at 1 (not 0)
            	}
            
            	if (sb != null) {         
            		// have a command line option to choose bytes or records ??? 
            		if (detachedLabelOnly) {
            			vicarLabelStartRecord = 1;
            			if (image_header_ptr != -1) {
            				vicarLabelStartRecord = image_header_ptr;
            			}
            			         		
            			sb.append(nodeName+pad+"= (\""+inputFilename+"\","+vicarLabelStartRecord+")"+LineEnd); 
            		}
            		else if (outputFilename != null) {
	                    sb.append(nodeName+pad+"= (\""+outputFilename+"\","+vicarLabelStartRecord+")"+LineEnd); 
	                    }
            		else {
            			sb.append(nodeName+pad+"= "+vicarLabelStartRecord+LineEnd); 
                    }
                    // sb.append(nodeName+pad+"= "+vicarLabelStartByte+" <BYTES>"+LineEnd);                  
                }
             	
             }
            
             if (debug) {
         		System.out.println("^IMAGE ");
         		System.out.println("inputFilename "+inputFilename);
         		System.out.println("outputFilename "+outputFilename);
         		System.out.println("inputVicarLabelRecordCt "+ inputVicarLabelRecordCt);
         		System.out.println("imageStartRecord "+ imageStartRecord);
             }
            nodeName = "^IMAGE" ;
            pad = getPadding(nodeName, 0); // this should be at level 0
            
            if (sb != null) { 
            	
            	if (detachedLabelOnly) {
            		if (image_ptr != -1) {
        				imageStartRecord = image_ptr;
        			} else {
            		    imageStartRecord = inputVicarLabelRecordCt + 1 ;
        			}
            		// this is the record the image starts at (1 is the first record, not 0)
            		sb.append(nodeName+pad+"= (\""+inputFilename+"\","+imageStartRecord+")"+LineEnd); 
            	}
                else if (outputFilename != null) {
                    sb.append(nodeName+pad+"= (\""+outputFilename+"\","+imageStartRecord+")"+LineEnd); 
                    }
                else {
                    sb.append(nodeName+pad+"= "+imageStartRecord+LineEnd); 
                     }
                
             	// sb.append(LineEnd) ; // add an extra blank line
                }
            
            
        }
        else if (nodeName.equalsIgnoreCase("comment") ) {
            
            Node child = node.getFirstChild();
            if (child != null) {
                nodeValue = "";
                while (child != null) {
                    int type = child.getNodeType();
                    // displayMetadata(child, level + 1);
                    if (type == TEXT_TYPE) {
                       nodeValue = nodeValue+child.getNodeValue(); 
                    } else if (type == CDATA_TYPE) {
                        nodeValue = nodeValue+child.getNodeValue(); 
                    }
                    child = child.getNextSibling();
                }
                
            } else {
                nodeValue = "";
            }
            
            if (debug) System.out.println("\n"+nodeValue+"\n");
            if (sb != null) { sb.append(LineEnd+nodeValue+LineEnd+LineEnd) ; }
        }
        else if (nodeName.equalsIgnoreCase("VICAR_SYSTEM") ) {
        	// DON'T PUT THIS IN TO THE LABEL IT MAY BE USED FOR THE EMBEDDED VICAR LABEL
        	// go thru all its children so they don't get printed
        	// Visit the children recursively
            Node child = node.getFirstChild();
            if (child != null) {
                // System.out.println(">");
                while (child != null) {
                    // displayMetadata(child, level + 1);
                    child = child.getNextSibling();
                }         
            } 
        }
        else if (nodeName.equalsIgnoreCase("class") ) {
            /*
             * CLASS is a special type of group/object
             * there should be a comment in the group
             * print this , then the items in the class 
             * there should NOT be a CLASS END_CLASS used
             * */
            NamedNodeMap map = node.getAttributes();
            if (map != null) {
                int length = map.getLength();
                for (int i = 0; i < length; i++) {
                    Node attr = map.item(i);
                    attrNodeName = attr.getNodeName();
                    if (attrNodeName.equalsIgnoreCase("name")) {
                        attrKey = attr.getNodeValue();
                        objectName = attrKey;
                     }
                 }
            }
            
            
            // System.out.println(         "CLASS                        = "+objectName);
            // if (sb != null) { sb.append("CLASS                         = "+objectName+LineEnd); }
            /**
             * indent(level);
            nodeName = "CLASS";
            pad = getPadding(nodeName, level);
            if (sb != null) { sb.append(nodeName+pad+"= "+objectName+LineEnd);  }             */
            inObject = true; // used by displayMetadata to know we are inside something
            // level++; // may remove this so a CLASS's items are NOT indented
            
            // Visit the children recursively
            Node child = node.getFirstChild();
            // look for the comment ? print it and then the other children 
            // set a flag so extra empty lines are prtined around the comment ????
            if (child != null) {
                // System.out.println(">");
                while (child != null) {
                    displayMetadata(child, level + 1);
                    child = child.getNextSibling();
                }          
            } 
                               
             inObject = false;
             // level--;
             // System.out.println(         "END_CLASS                     = "+objectName);
             // if (sb != null) { sb.append("END_CLASS                     = "+objectName+LineEnd); }
              // if (sb != null) { sb.append(" "+LineEnd); } // add a blank line after a class
              
              /**
               * indent(level);
            nodeName = "END_CLASS";
            pad = getPadding(nodeName, level);
            if (sb != null) { sb.append(nodeName+pad+"= "+objectName+LineEnd);  }               */
            
        }
        else if (nodeName.equalsIgnoreCase("group") ) {
            
            NamedNodeMap map = node.getAttributes();
            if (map != null) {
                int length = map.getLength();
                for (int i = 0; i < length; i++) {
                    Node attr = map.item(i);
                    attrNodeName = attr.getNodeName();
                    if (attrNodeName.equalsIgnoreCase("name")) {
                        attrKey = attr.getNodeValue();
                        objectName = attrKey;
                     }
                 }
            }
            
            indent(level);
            nodeName = "GROUP";
            
            pad = getPadding(nodeName, level);
            if (debug) System.out.println(nodeName+pad+"= "+objectName);
            if (sb != null) { sb.append(nodeName+pad+"= "+objectName+LineEnd);  }
            
            inObject = true;
            level++;
            
            // Visit the children recursively
            Node child = node.getFirstChild();
            if (child != null) {
                // System.out.println(">");
                while (child != null) {
                    displayMetadata(child, level + 1);
                    child = child.getNextSibling();
                }
                
            
               
            } 
                
                
             inObject = false;
             level--;
             
             indent(level);
             nodeName = "END_GROUP";
             pad = getPadding(nodeName, level);
             if (debug) System.out.println( nodeName+pad+"= "+objectName);    
             if (sb != null) { sb.append(nodeName+pad+"= "+objectName+LineEnd);  }
            
        }
        else if (nodeName.equalsIgnoreCase("object") ) {
            
            NamedNodeMap map = node.getAttributes();
            if (map != null) {
                int length = map.getLength();
                for (int i = 0; i < length; i++) {
                    Node attr = map.item(i);
                    attrNodeName = attr.getNodeName();
                    if (attrNodeName.equalsIgnoreCase("name")) {
                        attrKey = attr.getNodeValue();
                        objectName = attrKey;
                     }
                 }
            }
            
            if (objectName.equalsIgnoreCase("IMAGE")) {
            	if (debug) System.out.println( "IMAGE OBJECT found --------------");
            	imageObjectFound = true;
            }
            
            
            // if (sb != null) { sb.append("OBJECT                         = "+objectName+LineEnd); }
            indent(level);
            nodeName = "OBJECT";
            pad = getPadding(nodeName, level);
            if (debug) System.out.println(nodeName+pad+"= "+objectName);
            if (sb != null) { sb.append(nodeName+pad+"= "+objectName+LineEnd);  }
             
            inObject = true;
            level++;
            
            // Visit the children recursively
            Node child = node.getFirstChild();
            if (child != null) {
                // System.out.println(">");
                while (child != null) {
                    displayMetadata(child, level + 1);
                    child = child.getNextSibling();
                }
                
            
               
            } 
                
                
             inObject = false;
             level--;
             // if (debug) System.out.println(         "END_OBJECT                     = "+objectName);
             // if (sb != null) { sb.append("END_OBJECT                     = "+objectName+LineEnd); }
             
             indent(level);
             nodeName = "END_OBJECT";
             pad = getPadding(nodeName, level);
             if (debug) System.out.println( nodeName+pad+"= "+objectName);
             if (sb != null) { sb.append(nodeName+pad+"= "+objectName+LineEnd);  }
            
        }
        else if (nodeName.equalsIgnoreCase("item") ) {
          /**
          * an item contains attributes key, quoted
          * the item node has no value
          * the value is stored in a text node child
          * or subitems
          * item or subitems may have units
          * if there are units append to value enclosed in <>
          **/
          boolean hasSubitems = false;
        
          attrUnit = null;
          NamedNodeMap map = node.getAttributes();
          if (map != null) {
            int length = map.getLength();
            for (int i = 0; i < length; i++) {
                Node attr = map.item(i);
                attrNodeName = attr.getNodeName();
                if (attrNodeName.equalsIgnoreCase("key")) {
                    attrKey = attr.getNodeValue();
                }
               else  if (attrNodeName.equalsIgnoreCase("name")) {
                    attrKey = attr.getNodeValue();
                }
                if (attrNodeName.equalsIgnoreCase("unit") ) {
                    attrUnit = attr.getNodeValue();
                }
                if (attrNodeName.equalsIgnoreCase("quoted")) {
                    attrQuotedValue = attr.getNodeValue();
                }
             } 
            }
           
			/*
			 * look for subitems (they are children)
			 * construct the value string from the concatenation of the subitems
			 * add quotes and units too
			 */
            nodeName = attrKey;
                     
            NodeList childList = node.getChildNodes();
            // even if there are no children an empty (non NULL) list wiull be returned
            int len = childList.getLength();
            int lastIndex = 0; // use to know when to close a multivalued String
            if (len > 0) { 
            	lastIndex = len -1; 
            	}
            	
           nodeValue = "";  
           String sval = "";
           Node child = null;
           Node subChild;
           String childNodeName = null;
           String subChildNodeName = null;
           int type = 0;
           int subType = 0;
           for (int i = 0 ; i< len ; i++) {
           		child = childList.item(i);
           		type = child.getNodeType();
           		childNodeName = child.getNodeName();
                if (childNodeName.equalsIgnoreCase("subitem") ) {
                	hasSubitems = true;
                	sval = getNodeValueString(child);
                	// now determine how to assemble the string value
                	if (i == 0 && len == 1) {
                		// System.out.print(i+") len="+len+"  sval="+sval+"< ");
						sval = sval.replaceFirst("^\\s+",""); // remove any leading spaces
						// sval = sval.replaceFirst("\\s+$",""); // remove any trailing spaces
                		nodeValue = sval;
						// System.out.println(" nodeValue="+nodeValue+"<");
                	}
                	else if (i == 0 && len > 0) {
						// System.out.print(i+") len="+len+"  sval="+sval+"< ");
                		sval = sval.replaceFirst("^\\s+",""); // remove any leading spaces
                		nodeValue = "("+sval ;
						// System.out.println(" nodeValue="+nodeValue+"<");
                	}
                	else if (len > 0 && i == len-1) {
                		nodeValue = nodeValue +","+sval+")" ;
                	}
                	else {
                		nodeValue = nodeValue +","+sval ;
                	}
                }   else {
                	nodeValue += getNodeValueString(node); // assume the value is a TEXT node of the "item"
                }
                // now determine how to assemble the string value
           
           		if (hasSubitems == true) {
           			// subitems has already taken units and quotes into account
           			attrQuotedValue = null;
           			attrUnit = null;
             	}    
             	else if (attrUnit != null) {
             		//  append units to the nodeValue
             		// units are already taken care of in getNodeValueString(node)
             		// nodeValue = nodeValue +"<"+attrUnit+">" ;
            	} 
              }
        
          
          
            // System.out.println("item: "+nodeName+"  "+nodeValue);
            // we need this to calculate the label size in records
            if (nodeName.equalsIgnoreCase("RECORD_BYTES") ) {
            	if (debug) {
            		System.out.println("record_bytes="+record_bytes);
            		System.out.println("detachedLabelOnly="+detachedLabelOnly);
            		System.out.println("record_bytes="+record_bytes);
            		System.out.println("inputVicarRecordLength="+inputVicarRecordLength);
            	}
            	if (detachedLabelOnly == true) {
            		record_bytes = inputVicarRecordLength;            		
            	}
            	else {
            	  nodeValue = getNodeValueString(node);
                  record_bytes = Integer.parseInt(nodeValue);
            	}
                
            }
            
            if (nodeName.equalsIgnoreCase("LINES") ) {
            	String na = "\"N/A\"" ;
            	String na2 = "N/A";
            	// System.out.println("LINES "+nodeValue+" $$ "+na +" ***************");
            	
            	nodeValue = getNodeValueString(node);
            	// this may be in a GROUP ?? get the parent ??
            	if (nodeValue.equalsIgnoreCase(na) || nodeValue.equalsIgnoreCase(na2)) {
            		// do nothing 
                	// imageLines = 0;
            		// System.out.println("ERROR \"LINES\" "+nodeValue );
            	}
            	else {
            		try {
            		imageLines = Integer.parseInt(nodeValue);  
            		}
            		catch (NumberFormatException e) {
            			System.out.println("NumberFormatException "+e );
            			System.out.println("LINES "+nodeValue );
            		}         	
            	}
                // System.out.println("record_bytes="+record_bytes);
            }
           
               indent(level);
               pad = getPadding(nodeName, level);
            
               // if (detachedLabelOnly == true) 
            if (nodeName.equalsIgnoreCase("FILE_RECORDS") ) {
            	if (debug) System.out.println("FILE_RECORDS band_storage_type = "+band_storage_type);
            	nodeValue = getNodeValueString(node);
                // if the image is written as BAND_SEQUENTIAL
                if (band_storage_type.equals("SAMPLE_INTERLEAVED")) { 
                    fileRecords = (imageLines )+ labelRecordCt + vicarLabelRecordCt;
                    imageRecords = imageLines;
                }
                else { // BAND_SEQUENTIAL   LINE_INTERLEAVED
                    fileRecords = (imageLines * bands)+ labelRecordCt + vicarLabelRecordCt;
                    imageRecords = imageLines * bands;
                }
                // for SAMPLE_INTERLEAVED
                // Int fileRecords = imageLines + labelRecordCt;
                // now RECORD_BYTES must = samples * bytes_per_sample * bands
                // if there is an embedded Vicar Label that must be added in too
                if (debug) {
                	System.out.println("  *******************************************");
                	System.out.println("fileRecords="+fileRecords+ " imageLines="+imageLines);
                	System.out.println(" bands="+bands+" labelRecordCt="+labelRecordCt);
                	System.out.println(" vicarLabelRecordCt "+vicarLabelRecordCt);
               		System.out.println("\n"+nodeValue+"\n");
                }
                if (sb != null) { sb.append(nodeName+pad+"= "+fileRecords+LineEnd);  }
                
                // add LABEL_RECORDS
                indent(level);
                nodeName = "LABEL_RECORDS";
                pad = getPadding(nodeName, level);
                if (sb != null) { sb.append(nodeName+pad+"= "+labelRecordCt+LineEnd);  }
            }
            
            else if (attrQuotedValue != null && attrQuotedValue.equalsIgnoreCase("true")) {
            	// all quotes are handled in getNodeValue()
                if (nodeValue.length() < 80 - wrapStart) {
                    // if (debug) System.out.println(nodeName+pad+"= \""+nodeValue+"\"");
                    if (debug) System.out.println(nodeName+pad+"= "+nodeValue);
                    // if (sb != null) { sb.append(nodeName+pad+"= \""+nodeValue+"\""+LineEnd); }
                    
                    if (sb != null) { 
                    	c80("q 1) ",nodeName, nodeValue, wrapStart, pad);
                    	// sb.append(nodeName+pad+"= \""+nodeValue+"\""+LineEnd); 
                    	sb.append(nodeName+pad+"= "+nodeValue+LineEnd); 
                    	}
                } else {
                    Vector vec = chopValueString(nodeValue);
                    String s = (String) vec.elementAt(0);
                    // if (debug) System.out.println(nodeName+pad+"= \""+s);
                    if (debug) System.out.println(nodeName+pad+"= "+s);
                    // if (sb != null) { sb.append( nodeName+pad+"= \""+s+LineEnd); }
                    int vlen = s.length();
                    if (vlen <= valueMax) {
                      if (sb != null) { 
                    	c80("q 2) ",nodeName, s, wrapStart, pad);
                    	// sb.append(nodeName+pad+"= \""+s+"\""+LineEnd); 
                    	sb.append(nodeName+pad+"= "+s+LineEnd); 
                    	}
                    } else if (vlen < lineMax) {
                    	c80("q 3) ",nodeName, s, wrapStart, pad);
                    	// put the value on the next line
                    	sb.append(nodeName+pad+"= "+LineEnd );
                    	// add a pad at the start ?? add 2 for quotes
                    	int vx = lineMax - vlen - 2;
                    	vpad = getValuePadding(vx );
                    	if (debug) System.out.println("q3 vx="+vx+" lineMax="+lineMax+" vlen="+vlen+" vpad="+vpad+"<");
                    	// sb.append(pad+"\""+s+"\""+LineEnd); 
                    	sb.append(vpad+s+LineEnd); 
                    } else {
                    	c80("q 4) ",nodeName, s, wrapStart, pad);
                    	if (debug) System.out.println("value is TOO LONG ");
                    	sb.append(nodeName+pad+"= "+LineEnd );
                    	// value is too long, break it into hyphenated parts
                    	sb.append("/* this value is TOO long and must be hyphenated */"+LineEnd);
                    	
                    	sb.append(s+LineEnd);
                    }
                   
                    
                    for (int i = 1; i< vec.size() ; i++) {
                      s = (String) vec.elementAt(i);
                      if (i == vec.size() - 1) {
                        // if (debug) System.out.println(getWrapStart()+s+"\""); 
                        if (debug) System.out.println(getWrapStart()+s);   
                        // if (sb != null) { sb.append( getWrapStart()+s+"\""+LineEnd);  }
                        
                        if (sb != null) { 
                        	c80("q 5) ",nodeName, s, wrapStart, pad);
                        	vlen = s.length();
                        	if (vlen + wrapStart > longValueMax ) {
                        		int vx = lineMax - vlen - 4;
                        		
                            	vpad = getValuePadding(vx );
                            	if (debug) System.out.println("q5 vx="+vx+" lineMax="+lineMax+" vlen="+vlen+" vpad="+vpad+"<");
                            	// sb.append(pad+"\""+s+"\""+LineEnd); 
                            	sb.append(vpad+s+LineEnd); 
                        	                      		
                        	} else {
                        		sb.append( getWrapStart()+s+LineEnd); 
                        	}
                        }
                        	
                      } else {
                        
                        if (sb != null) { 
                        	c80("q 6) ",nodeName, s, wrapStart, pad);
                        	sb.append(getWrapStart()+s+LineEnd);
                        	}
                      }
                    }
                   }
                } // no quotes
                else {
                 if (nodeValue.length() < 80 - wrapStart) {
                    if (debug) System.out.println(nodeName+pad+"= "+nodeValue);
                    if (sb != null) { 
                    	c80("nq 1) ",nodeName, nodeValue, wrapStart, pad);
                    	sb.append(nodeName+pad+"= "+nodeValue+LineEnd); 
                    	}
                    
                } else {
                    Vector vec = chopValueString(nodeValue);
                    String s = (String) vec.elementAt(0);
                    // if (debug) System.out.println(nodeName+pad+"= \""+s);
                    if (debug) System.out.println(nodeName+pad+"= "+s);
                    
                    int vlen = s.length();
                    if (vlen <= valueMax) {
                        if (sb != null) { 
                      	c80("nq 2) ",nodeName, s, wrapStart, pad);
                      	sb.append(nodeName+pad+"= "+s+LineEnd); 
                      	}
                      } else if (vlen < lineMax) {
                      	// put the value on the next line
                      	c80("nq 3) ",nodeName, s, wrapStart, pad);
                      	// add a pad at the start ??
                      	sb.append(nodeName+pad+"= "+LineEnd );
                      	// CR LF end a line and are part of the 80 character count
                      	int vx = lineMax - vlen - 2;
                      	vpad = getValuePadding(vx);
                      	if (debug) System.out.println("nq3 vx="+vx+" lineMax="+lineMax+" vlen="+vlen+" vpad="+vpad+"<");
                      	sb.append(vpad+s+LineEnd); 
                      } else {
                      	if (debug) System.out.println("value is TOO LONG ");
                      	c80("nq 4) ",nodeName, s, wrapStart, pad);
                      	// value is too long, break it into hyphenated parts
                      	sb.append(nodeName+pad+"= "+LineEnd );
                      	sb.append("/* this value is TOO long and must be hyphenated */"+LineEnd);
                      	sb.append(s+LineEnd);
                      }
                    
                    for (int i = 1; i< vec.size() ; i++) {
                      s = (String) vec.elementAt(i);
                      // check each of these to see if the value is over 80 chars ?
                      if (sb != null) { 
                    	c80("nq 5) ",nodeName, s, wrapStart, pad);
                    	vlen = s.length();
                    	if (vlen + wrapStart > longValueMax ) {
                    		int vx = lineMax - vlen - 4;
                        	vpad = getValuePadding(vx );
                        	if (debug) System.out.println("nq5 vx="+vx+" lineMax="+lineMax+" vlen="+vlen+" vpad="+vpad+"<");
                        	// sb.append(pad+"\""+s+"\""+LineEnd); 
                        	sb.append(vpad+s+LineEnd); 
                    	                      		
                    	} else {
                    		sb.append(getWrapStart()+s+LineEnd);
                    	}
                      }
                    }
                   }
            }
    } else {
    // Visit the children recursively
        Node child = node.getFirstChild();
        if (child != null) {
            while (child != null) {
                displayMetadata(child, level);
                child = child.getNextSibling();
            }
            
        } 
    }
    }
}
