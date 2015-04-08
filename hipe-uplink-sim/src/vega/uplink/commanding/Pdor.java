package vega.uplink.commanding;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.logging.Logger;

import vega.uplink.DateUtil;
import vega.uplink.commanding.itl.ItlParser;
import vega.uplink.planning.ObservationPor;

public class Pdor {
	private static final Logger LOG = Logger.getLogger(Pdor.class.getName());
	protected static String[] readFile(BufferedReader br) throws IOException, ParseException{
		String line = "";
		String[] result;
		java.util.Vector<String> lines=new java.util.Vector<String>();
		
			while ((line = br.readLine()) != null){
				

				lines.add(line);
			}
		
		result = new String[lines.size()];
		lines.toArray(result);
		br.close();
		//try{
			return result;
		/*}catch (java.text.ParseException pex){
			ParseException nEx = new ParseException(pex.getMessage(),0);
			nEx.initCause(pex);
			throw nEx;
		}*/
	}
	protected static String[] readFile(File f) throws IOException, ParseException{
		BufferedReader br = null;
		
				br = new BufferedReader(new FileReader(f));
				try{
					return readFile(br);
				}catch (java.text.ParseException pex){
					ParseException nEx = new ParseException("In file "+f+"."+pex.getMessage(),0);
					nEx.initCause(pex);
					throw nEx;
				}
	}
	
	protected static String[] readFile(String file) throws IOException, ParseException{
		return readFile(new File(file));
	}
	
	protected static String removeMultipleSpaces(String line){
		line=line.replaceAll("\t", " ");
		int index=line.indexOf("  ");
		if (index==-1){
			return line;
		}else{
			return removeMultipleSpaces(line.replaceAll("  ", " "));
		}
	}
	
	protected static String[] clean(String[] lin){
		//String line = "";
		String[] result;
		java.util.Vector<String> lines=new java.util.Vector<String>();
		for (int i=0;i<lin.length;i++){
			String line=lin[i];
			line=removeMultipleSpaces(line);
			if (!line.startsWith("C") && !line.equals("") && !line.equals(" ")){
				lines.add(line);
			}
		}
		
		
		result = new String[lines.size()];
		result=lines.toArray(result);		
		return result;
		
	}
	
	public static void main(String[] args){
    	herschel.share.util.Configuration.setProperty("var.hcss.dir", "/Users/jarenas 1/Downloads/hcss-12.0.2524");
    	herschel.share.util.Configuration.setProperty("var.hcsstest.dir", "/Users/jarenas 1/Downloads/hcss-12.0.2524");
    	herschel.share.util.Configuration.setProperty("vega.mib.location", "/Users/jarenas 1/Downloads/MAPPS/MIB");
    	herschel.share.util.Configuration.setProperty("vega.instrument.names","{ALICE,CONSERT,OSIRIS}");
    	herschel.share.util.Configuration.setProperty("vega.instrument.acronyms.ALICE","AL");
    	herschel.share.util.Configuration.setProperty("vega.instrument.acronyms.OSIRIS","SR");
    	herschel.share.util.Configuration.setProperty("vega.instrument.acronyms.CONSERT","CN");
		try{
			/*String[] lines = readFile("/Users/jarenas 1/Downloads/PDOR_D_AL_DC_AL00018_00255.ROS");
			lines=clean(lines);
			for (int i=0;i<lines.length;i++){
				System.out.println(lines[i]);
				//if (lines[i].startsWith("P")) System.out.println(readParameter(lines[i]).toXML(0));
				//if (lines[i].startsWith("H4")) System.out.println(DateUtil.defaultDateToString(readExDate(lines[i])));
				//if (lines[i].startsWith("H1")) System.out.println(readSequence(lines[i]).toXml());
			}
			AbstractSequence[] seqs = readSequences(lines);
			for (int i=0;i<seqs.length;i++){
				System.out.println(seqs[i].toXml());
			}*/
			Por por=readPdor("/Users/jarenas 1/Downloads/PDOR_D_AL_DC_AL00018_00255.ROS");
			System.out.println(por.toXml());
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public static Por readPdor(String file) throws IOException, ParseException{
		Por result=new Por();
		File f=new File(file);
		String[] lines = readFile(file);
		lines=clean(lines);
		AbstractSequence[] seqs = readSequences(lines);
		result.setSequences(seqs);
		result.setName(f.getName());
		result.setPath(f.getParent());
		return result;
	}
	protected static AbstractSequence[] readSequences(String[] lines) throws ParseException{
		Mib mib;
		try{
			mib=Mib.getMib();
		}catch (Exception e){
			IllegalArgumentException iae = new IllegalArgumentException("Could dnot get MIB "+e.getMessage());
			iae.initCause(e);
			throw(iae);
		}
		Date refDate=null;
		Date[] validity=new Date[2];
		validity[0]=null;
		validity[1]=null;
		AbstractSequence seq=null;
		java.util.Vector<AbstractSequence> sequences=new java.util.Vector<AbstractSequence>();
		for (int i=0;i<lines.length;i++){
			System.out.println(lines[i]);
			if (lines[i].startsWith("DOR_") || lines[i].startsWith("POR_")){
				refDate=readRefDate(lines[i]);
				validity=readValidity(lines[i+1]);
			}
			if (lines[i].startsWith("P")){
				if (seq!=null) seq.addParameter(readParameter(lines[i]));
			}
			if (lines[i].startsWith("H3")){
				try{
					long delta=readDelta(lines[i]);
					if (seq!=null) seq.setExecutionDate(new Date(seq.getExecutionDate().getTime()+delta));
				}catch (Exception e){
					LOG.info("Could not parse delta");
				}
				//System.out.println(DateUtil.defaultDateToString(readExDate(lines[i])));
			}
			if (lines[i].startsWith("H4")){
				try{
					if (seq!=null) seq.setExecutionDate(readExDate(lines[i]));
				}catch (Exception e){
					LOG.info("Could not parse execution date");
				}
				//System.out.println(DateUtil.defaultDateToString(readExDate(lines[i])));
			}
			if (lines[i].startsWith("H1")){
				Date rd=refDate;
				if (validity[0]!=null){
					rd=validity[0];
				}
				if (seq!=null){
					sequences.add(new Sequence(seq));
					long duration = mib.getTotalSequenceDuration(seq.getName());
					rd=new Date(seq.getExecutionDate().getTime()+(duration*1000));
				}
				seq=readSequence(lines[i],rd);
				/*if (validity[0]!=null){
					seq=readSequence(lines[i],validity[0]);
				}else{
					seq=readSequence(lines[i],refDate);
				}*/
			}
		}
		if (seq!=null) sequences.add(new Sequence(seq));
		AbstractSequence[] result=new AbstractSequence[sequences.size()];
		result =sequences.toArray(result);
		return result;

		
	}
	protected static Date[] readValidity(String line) throws ParseException{
		String[] parts = line.split(" ");
		try{
			Date start=DateUtil.parse(parts[0]);
			Date end=DateUtil.parse(parts[1]);
			Date[] result=new Date[2];
			result[0]=start;
			result[1]=end;
			return result;
			
		}catch (Exception e){
			ParseException pa = new ParseException("Could not parse secondary header:"+line+" "+e.getMessage(),0);
			pa.initCause(e);
			throw pa;
		}
			
		
	}
	protected static AbstractSequence readSequence(String line,Date refDate) throws ParseException{
		if (refDate==null) refDate=new Date();
		line=line.replace("H1", "");
		String[] parts = line.split(" ");
		if (!parts[1].equals("S")) return null;
		else {
			String name=parts[0];
			return new Sequence(name,ObservationPor.getUniqueID(),DateUtil.dateToDOY(refDate));
		}
	}
	protected static Date readExDate(String line) throws ParseException{
		
			return DateUtil.parse(line.replace("H4", ""));
		
			
		
	}
	protected static Date readRefDate(String line) throws ParseException{
		String[] parts = line.split(" ");
		return DateUtil.parse(parts[2]);
	}
	protected static long readDelta(String line) throws ParseException{
		String[] parts = line.split(" ");
		return ItlParser.deltaToMilli(parts[1]);
		//return DateUtil.parse(parts[1]);
	}
	protected static Parameter readParameter(String line){
		Mib mib;
		try{
			mib=Mib.getMib();
		}catch (Exception e){
			IllegalArgumentException iae = new IllegalArgumentException("Could dnot get MIB "+e.getMessage());
			iae.initCause(e);
			throw(iae);
		}
		line=line.substring(1);
		String[] parts = line.split(" ");
		if (parts.length==1){
			String name=parts[0];
			return mib.getDefaultParameter(name);
		}
		try{
			if (parts[1].equals("E")) return new ParameterString(parts[0],Parameter.REPRESENTATION_ENGINEERING,parts[2]);
			else{
				String radix = Parameter.RADIX_DECIMAL;
				if (parts[2].equals("H")) radix = Parameter.RADIX_HEX;
				String value = parts[3];
				String name=parts[0];
				String representation=Parameter.REPRESENTATION_RAW;
				if (radix.equals(Parameter.RADIX_HEX)){
					try{
						String hexValue = value.replace("0x", "");
						return new ParameterFloat(name,representation,radix,new Integer(Integer.parseInt(hexValue, 16)).floatValue());
					}
					catch (java.lang.NumberFormatException e){
						try{
							return new ParameterFloat(name,representation,radix,new Integer(Integer.parseInt("0x"+value, 16)).floatValue());
						}
						catch (java.lang.NumberFormatException e1){
							e.printStackTrace();
							e1.printStackTrace();
							return new ParameterFloat(name,representation,radix,0);
						}
					}
					
				}
				if (radix.equals(Parameter.RADIX_DECIMAL)) {
					return new ParameterFloat(name,representation,radix,new Float(value).floatValue());
				}
	
	
			}
		}catch (Exception e){
			String message = "Could not parse parameter "+line;
			LOG.severe(message);
			IllegalArgumentException iae = new IllegalArgumentException(message+" "+e.getMessage());
			iae.initCause(e);
			throw iae;
			
		}
		return null;
	}
	
	
}
