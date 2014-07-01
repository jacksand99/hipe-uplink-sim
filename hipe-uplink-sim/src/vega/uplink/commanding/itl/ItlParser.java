package vega.uplink.commanding.itl;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.regex.Pattern;

import vega.uplink.commanding.Orcd;
import vega.uplink.commanding.Parameter;
import vega.uplink.commanding.ParameterFloat;
import vega.uplink.commanding.ParameterString;
import vega.uplink.commanding.Por;
import vega.uplink.commanding.Sequence;
import vega.uplink.commanding.SequenceProfile;

public class ItlParser {
	public static String path="";
	public static String dEvtPath="";
	public static Por parseItl(String itlFile,String evtFile,String defaultEvtPath,int uIDSed) throws ParseException{
		dEvtPath=defaultEvtPath;
		Por result = new Por();
		java.io.File file=new java.io.File(itlFile);
		java.util.HashMap<String,String> events=new java.util.HashMap<String,String>();
		java.util.Vector<String> commandLines=new java.util.Vector<String>();
		path=file.getParent();
		String[] itllines;
		try {
			itllines = readFile(itlFile);
		} catch (IOException e1) {
			ParseException e2=new ParseException(e1.getMessage(),0);
			throw e2;
		}
		String[] evtlines;
		try {
			evtlines = readFile(evtFile);
		} catch (IOException e1) {
			ParseException e2=new ParseException(e1.getMessage(),0);
			e2.initCause(e1);
			throw e2;
		}
		for (int i=0;i<evtlines.length;i++){
			String[] parts=evtlines[i].split(" ");
			if (parts.length==3){
				parts[0]=parts[0].replaceAll(" ", "");
				parts[1]=parts[1].replaceAll(" ", "");
				parts[2]=parts[2].replaceAll(" ", "");
				int count=Integer.parseInt(parts[2].split("=")[1].replace(")", ""));
				parts[2]="(COUNT="+new Integer(count).toString()+")";
				events.put(parts[1]+" "+parts[2], parts[0]);
			}
		}
		for (int i=0;i<itllines.length;i++){
			if(itllines[i].contains("COUNT") || itllines[i].contains("count")){
				String[] parts=itllines[i].split(" ");
				String evtString=parts[0]+" "+parts[1];
				String[] oParts=parts[1].split("=");
				if (oParts.length<2) System.out.println(parts[1]);
				int count=Integer.parseInt(oParts[1].replace(")", ""));
				String key=parts[0]+" "+"(COUNT="+new Integer(count).toString()+")";
				String val=events.get(key);
				if (val!=null){
					itllines[i]=itllines[i].replace(evtString, val);
				}else{
					//System.out.println(key+" not found");
				}
			}
			java.util.Date[] vDates=new java.util.Date[2];
			vDates[0]=new java.util.Date();
			vDates[1]=new java.util.Date();
			
			if (itllines[i].startsWith("Version") || itllines[i].startsWith("Start") || itllines[i].startsWith("End") || itllines[i].startsWith("Init") || itllines[i].startsWith("Source_file") ||itllines[i].contains("ANTENNA")){
				if (itllines[i].startsWith("Start")){
					String sSD=itllines[i].replace("Start_time:", "");
					java.text.SimpleDateFormat dateFormat2 = new java.text.SimpleDateFormat("dd-MMMMMMMMM-yyyy'_'HH:mm:ss");
					dateFormat2.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
						vDates[0]=dateFormat2.parse(sSD);
				}
				if (itllines[i].startsWith("End")){
					String sSD=itllines[i].replace("End_time:", "");
					java.text.SimpleDateFormat dateFormat2 = new java.text.SimpleDateFormat("dd-MMMMMMMMM-yyyy'_'HH:mm:ss");
					dateFormat2.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
						vDates[1]=dateFormat2.parse(sSD);
				}
				
			}else{
				commandLines.add(itllines[i]);
			}
		}
		try{
			for (int i=0;i<commandLines.size();i++){
				String cl=commandLines.get(i);
				String[] parts=cl.split(Pattern.quote("("));
				String sComm=parts[0];
				String[] pComm=sComm.split(" ");
				String commandName=pComm[pComm.length-1];
				/*if (commandName==null | commandName.equals("")){
					System.out.println(cl);
				}*/
				/*if (i==0){
					System.out.println(cl);
				}*/
				String commandTime=pComm[0];
				java.util.Date exTime=parseExDate(commandTime);
				String commandDelta="";
				if (pComm.length>4){
					commandDelta=pComm[1];
				}
				String sParam="";
				if (parts.length>1){
					sParam=parts[1].replace(")", "");
				}
				String[] sParamArr=separateParameters(sParam);
				DecimalFormat df=new DecimalFormat("000000000");
				//String uid="P"+new Integer((uIDSed*10000)+(i*100)).toString();
				String uid="P"+df.format((uIDSed*10000)+(i*100));
				/*if (uid.equals("P000010000")){
					System.out.println(sComm);
					System.out.println("Command name:"+commandName);

				}*/

				Sequence seq=new Sequence(commandName,uid,Sequence.dateToZulu(addDelta(exTime,commandDelta)));
				int repeat=1;
				long separation=0;

				for (int j=0;j<sParamArr.length;j++){
					if (!sParamArr[j].startsWith("DATA_RATE") && !sParamArr[j].startsWith("POWER") && !sParamArr[j].startsWith("REPEAT") && !sParamArr[j].startsWith("SEPARATION")){
						seq.addParameter(parseParameter(sParamArr[j]));
					}
					if (sParamArr[j].startsWith("DATA_RATE") || sParamArr[j].startsWith("POWER")){
						SequenceProfile[] profs=parseProfile(sParamArr[j]);
						for (int k=0;k<profs.length;k++){
							seq.addProfile(profs[k]);
						}
					}
					if (sParamArr[j].startsWith("REPEAT")){
						String[] temp=sParamArr[j].split("=");
						repeat=Integer.parseInt(temp[1].replace(" ", ""));
						

					}
					if (sParamArr[j].startsWith("SEPARATION")){
						String[] temp=sParamArr[j].split("=");
						temp[1]=temp[1].replace(" ", "");
						String temp2[]=temp[1].split(":");
						separation=((Integer.parseInt(temp2[0])*3600)+(Integer.parseInt(temp2[1])*60)+Integer.parseInt(temp2[2]))*1000;
					}
				}
				for (int k=0;k<repeat;k++){
					//Sequence newSeq=new Sequence(seq);//=seq.copy();
					Sequence newSeq=new Sequence(new String(seq.getName()),new String(seq.getUniqueID()),new String(seq.getFlag()),new Character(seq.getSource()),new Character(seq.getDestination()),new String(seq.getExecutionTime()),seq.getParameters(),seq.getProfiles());
					newSeq.setExecutionDate(new java.util.Date(seq.getExecutionDate().getTime()+(separation*k)));
					//newSeq.setUniqueID("P"+new Integer((uIDSed*10000)+(i*100)+k).toString());
					newSeq.setUniqueID("P"+df.format((uIDSed*10000)+(i*100)+k));
					result.addSequence(newSeq);

				}

				
					
					
					
	
			}
		}catch (Exception e){
			ParseException e2=new ParseException(e.getMessage(),0);
			e.printStackTrace();
			throw e2;
		}
		

		return result;
	}
	
	private static SequenceProfile[] parseProfile(String profile){
		SequenceProfile[] result;
		java.util.Vector<SequenceProfile> proVector=new java.util.Vector<SequenceProfile>();
		profile=profile.replace(" [Watts]", "");
		profile=profile.replace(" [W]", "");
		profile=profile.replace("[W]", "");

		profile=profile.replace(" [bits/sec]", "");
		profile=profile.replace("[Watts]", "");
		profile=profile.replace("[bits/sec]", "");
		String type="";
		if (profile.startsWith("DATA_RATE_PROFILE")) type=SequenceProfile.PROFILE_TYPE_DR;
		if (profile.startsWith("POWER_PROFILE")) type=SequenceProfile.PROFILE_TYPE_PW;
		profile=profile.replace("DATA_RATE_PROFILE=", "");
		profile=profile.replace("POWER_PROFILE=", "");
		String[] temp=profile.split(" ");
		for (int i=0;i<temp.length;i=i+2){
			proVector.add(new SequenceProfile(type,temp[i],Double.parseDouble(temp[i+1])));
		}
		result=new SequenceProfile[proVector.size()];
		proVector.toArray(result);
		return result;
	}
	private static Parameter parseParameter(String param) throws ParseException{
		if (param.contains("\"")){
			int loc1=param.indexOf("\"");
			int loc2=param.indexOf("\"",loc1+1);
			String sub=param.substring(loc1,loc2);
			String sub2=sub.replace(" ", "-");
			
			param=param.replace(sub, sub2);
			
		}
		Parameter result=new Parameter("","","");
		String type="[RAW]";
		String[] temp=param.split("=");
		String[] temp2=temp[1].split(" ");
		if (temp2.length>1){
			type=temp2[1];
		}
		
		if (temp2[0].contains("\"")){
			int loc1=param.indexOf("\"");
			int loc2=param.indexOf("\"",loc1+1);
			String sub=param.substring(loc1,loc2);
			String sub2=sub.replace("-", " ");

			
			temp2[0]=temp2[0].replace(sub, sub2);
			
		}
		
		if (type.equals("[ENG]")){
			result=new ParameterString(temp[0],Parameter.REPRESENTATION_ENGINEERING,temp2[0]);
		}else{
			String rad=Parameter.RADIX_DECIMAL;
			float value;
			if (temp2[0].startsWith("0x") || temp2[0].startsWith("0X")){
				rad=Parameter.RADIX_HEX;
				value=new Integer(Integer.parseInt(temp2[0].substring(2), 16)).floatValue();
			}else{
				try{
					value=Float.parseFloat(temp2[0]);
				}catch (java.lang.NumberFormatException e){
					ParseException e2=new ParseException(e.getMessage(),0);
					throw e2;
				}

			}
			
			result=new ParameterFloat(temp[0],Parameter.REPRESENTATION_RAW,rad,value);
		}
		
		return result;
	}
	
	private static java.util.Date addDelta(java.util.Date initial,String delta){
		if (delta.equals("")) return initial;
		long ms=initial.getTime();
		boolean symbol=true;
		int days=0;
		int hours=0;
		int minutes=0;
		int seconds=0;
		if (delta.startsWith("-")) symbol=false;
		delta=delta.replace("+", "");
		delta=delta.replace("-", "");
		if (delta.contains("_")){
			String sDays=delta.substring(0,3);
			days=Integer.parseInt(sDays);
			delta=delta.replace(sDays+"_","");
		}
		String[] sTimes=delta.split(":");
		hours=Integer.parseInt(sTimes[0]);
		minutes=Integer.parseInt(sTimes[1]);
		seconds=Integer.parseInt(sTimes[2]);
		long deltams=((days*86400)+(hours*3600)+(minutes*60)+seconds)*1000;
		if (symbol){
			ms=ms+deltams;
		}
		else{
			ms=ms-deltams;
		}
		java.util.Date result=new java.util.Date(ms);

		return result;
		
	}
	
	private static java.util.Date parseExDate(String exDate) throws ParseException{
		java.util.Date result=new java.util.Date();
		java.text.SimpleDateFormat dateFormat2 = new java.text.SimpleDateFormat("dd-MMMMMMMMM-yyyy'_'HH:mm:ss");
		dateFormat2.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
		java.text.SimpleDateFormat dateFormat3=new java.text.SimpleDateFormat("yyyy-D'T'HH:mm:ss'Z'");
		dateFormat3.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
			try {
				result=dateFormat2.parse(exDate);
			} catch (ParseException e1) {
				try {
					result=dateFormat3.parse(exDate);
					if (result.getYear()<2000) result=dateFormat3.parse("20"+exDate);
				} catch (ParseException e2) {
					try {
						result=dateFormat3.parse("20"+exDate);
					} catch (ParseException e3) {
						ParseException pae=new ParseException("Could not parse "+exDate+". "+e3.getMessage(),0);
						pae.setStackTrace(e3.getStackTrace());
						System.out.println("Could not parse "+exDate);
					}
				}
			}
		//}
		
		return result;
	}
	
	private static String[] separateParameters(String params){
		java.util.Vector<String> parNames=new java.util.Vector<String>();
		java.util.Vector<String> parVal=new java.util.Vector<String>();
		String[] result;
		String[] temp=params.split("=");
		for (int i=0;i<temp.length-1;i++){
			String[] temp2=temp[i].split(" ");
			parNames.add(temp2[temp2.length-1]);
		}
		result=new String[parNames.size()];
		for (int i=0;i<parNames.size();i++){
			int loc1=params.indexOf(parNames.get(i))+parNames.get(i).length()+1;
			int loc2=params.length();
			if (i!=parNames.size()-1){
				loc2=params.indexOf(parNames.get(i+1));
			}

			parVal.add(params.substring(loc1, loc2));
			result[i]=parNames.get(i)+"="+params.substring(loc1, loc2);
		}
		return result;
	}
	private static String[] clean(String[] sourceLines) throws ParseException{
		String[] result;
		result=removeComments(sourceLines);
		result=removeBreaks(result);
		result=removeSpaces(result);
		result=resolveIncludes(result,path);
		result=removeDuplicates(result,"Version");
		result=removeDuplicates(result,"Start_time");
		result=removeDuplicates(result,"End_time");
		
		return result;
	}
	
	private static String[] resolveIncludes(String[] sourceLines,String filePath) throws ParseException{
		java.util.Vector<String> lines=new java.util.Vector<String>();
		String[] result;
		for (int i=0;i<sourceLines.length;i++){
			String line=sourceLines[i];
			if (line.startsWith("Include:") || line.startsWith("Include_file")){
				String relativePath=line.split(":")[1];
				relativePath=relativePath.replaceAll("\"", "");
				//relativePath=relativePath.replaceAll("/", File.separator);

				String[] includeLines;
				try{
					//includeLines=readFile(filePath+"\\"+relativePath);
					includeLines=readFile(filePath+"/"+relativePath.trim());

				}
				catch (java.io.IOException e){
					try{
						includeLines=readFile(dEvtPath+"/"+relativePath.trim());

						//includeLines=readFile(dEvtPath+"\\"+relativePath);
					}catch (java.io.IOException e2){
						e2.printStackTrace();
						System.out.println(filePath+"/"+relativePath);
						System.out.println(dEvtPath+"/"+relativePath);
						
						ParseException pae=new ParseException("At line:"+line+". File "+relativePath+" can not be resolved from "+filePath+" or "+dEvtPath,0);
						pae.initCause(e);
						throw pae;
					}
				}
				for (int j=0;j<includeLines.length;j++){
					lines.add(includeLines[j]);
				}
			}else{
				lines.add(line);
			}
		}
		
		result = new String[lines.size()];
		lines.toArray(result);
		return result;
	}
	
	private static String[] readFile(String file) throws IOException, ParseException{
		BufferedReader br = null;
		String line = "";
		String[] result;
		java.util.Vector<String> lines=new java.util.Vector<String>();
		
				br = new BufferedReader(new FileReader(file));
			while ((line = br.readLine()) != null){
				lines.add(line);
			}
		
		result = new String[lines.size()];
		lines.toArray(result);
		br.close();
		return clean(result);
	}
	
	private static String[] removeComments(String[] sourceLines){
		java.util.Vector<String> lines=new java.util.Vector<String>();
		String[] result;
		
		for (int i=0;i<sourceLines.length;i++){
			String line=sourceLines[i];
			int index=line.indexOf("#");
			if (index==-1){
				lines.add(line);
			}
			if (index>0){
				lines.add(line.substring(0, index));
			}
		}
		
		result = new String[lines.size()];
		lines.toArray(result);
		return result;
	}
	
	private static String[] removeBreaks(String[] sourceLines){
		java.util.Vector<String> lines=new java.util.Vector<String>();
		String[] result;
		for (int i=0;i<sourceLines.length;i++){
			String line=sourceLines[i];
			int index=line.indexOf("\\");
			if (index==-1){
				lines.add(line);
			}else{
				String newLine=line.substring(0, index)+sourceLines[i+1];
				sourceLines[i+1]=newLine;
			}
			
		}
		
		result = new String[lines.size()];
		lines.toArray(result);
		return result;
	}
	
	private static String[] removeSpaces(String[] sourceLines){
		java.util.Vector<String> lines=new java.util.Vector<String>();
		String[] result;
		for (int i=0;i<sourceLines.length;i++){
			String newLine=removeMultipleSpaces(sourceLines[i]);
			newLine=removeUselesSpaces(newLine);
			if(newLine.length()>1){
				lines.add(newLine);
			}
		}
		result = new String[lines.size()];
		lines.toArray(result);
		return result;
	}
	
	private static String removeMultipleSpaces(String line){
		line=line.replaceAll("\t", " ");
		int index=line.indexOf("  ");
		if (index==-1){
			return line;
		}else{
			return removeMultipleSpaces(line.replaceAll("  ", " "));
		}
	}
	
	private static String removeUselesSpaces(String line){
		if (line.startsWith(" ")) line=line.substring(1);
		line=line.replaceAll(Pattern.quote("(")+" ", "(");
		line=line.replaceAll(" "+Pattern.quote(")"), ")");
		line=line.replaceAll(" =", "=");
		line=line.replaceAll("= ", "=");
		line=line.replaceAll(": ", ":");
		return line;
		

	}
	
	private static String[] removeDuplicates(String[] sourceLines,String pattern){
		java.util.Vector<String> lines=new java.util.Vector<String>();
		String[] result;
		boolean found=false;
		for (int i=0;i<sourceLines.length;i++){
			String line=sourceLines[i];
			if (line.startsWith(pattern)){
				if (!found){
					lines.add(line);
					found=true;
				}
			}else{
				lines.add(line);
			}
		}
		
		result = new String[lines.size()];
		lines.toArray(result);
		return result;
	}
	
	public static String[] substitute(String[] sourceLines,String pattern,String newString){
		java.util.Vector<String> lines=new java.util.Vector<String>();
		String[] result;
		boolean found=false;
		for (int i=0;i<sourceLines.length;i++){
			String line=sourceLines[i];
			line=line.replaceAll(pattern, newString);
			lines.add(line);
			if (line.indexOf(newString)>-1) found=true;
		}
		//if (!found) System.out.println("Pattern: "+pattern+" not found");
		result = new String[lines.size()];
		lines.toArray(result);
		return result;
	}
	

	

}
