package rosetta.uplink.pointing;

import vega.uplink.pointing.exclusion.AbstractExclusion;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.Iterator;
import java.util.regex.Pattern;







import vega.uplink.DateUtil;

public class ExclusionPeriod extends AbstractExclusion{
	public static void main(String args[]){
		try {
			ExclusionPeriod periods = readFromFile("/Users/jarenas 1/Rosetta/hack 11/EXCL_DL_004_01____H__00076.evf");
			System.out.println(periods);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}	
	public static ExclusionPeriod readFromFile(String evtFile) throws ParseException{
		String[] evtlines;
		java.util.HashMap<String,Date> events=new java.util.HashMap<String,Date>();
		ExclusionPeriod result=new ExclusionPeriod();
		
		try {
			File f=new File(evtFile);
			result.setName(f.getName());
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
				events.put(parts[1]+" "+parts[2], parseExDate(parts[0]));
			}
		}
		Iterator<String> it = events.keySet().iterator();
		while(it.hasNext()){
			String event = it.next();
			if (event.contains("START")){
				if (event.contains("NAV")){
					result.addNavPeriod(events.get(event), events.get(event.replace("START", "END")));
				}
				if (event.contains("CMD")){
					result.addCmdPeriod(events.get(event), events.get(event.replace("START", "END")));
				}

			}

		}
		return result;

		
	}
	
	protected static String[] readFile(String file) throws IOException, ParseException{
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
		try{
			return clean(result);
		}catch (java.text.ParseException pex){
			ParseException nEx = new ParseException("In file "+file+"."+pex.getMessage(),0);
			nEx.initCause(pex);
			throw nEx;
		}
	}
	
	private static String[] clean(String[] sourceLines) throws ParseException{
		String[] result;
		result=removeComments(sourceLines);
		result=removeBreaks(result);
		result=removeSpaces(result);
		//result=resolveIncludes(result,path);
		result=removeDuplicates(result,"Version");
		result=removeDuplicates(result,"Start_time");
		result=removeDuplicates(result,"End_time");
		
		return result;
	}
	
	protected static String[] removeComments(String[] sourceLines){
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

	
	protected static String[] removeBreaks(String[] sourceLines){
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
	
	protected static String[] removeSpaces(String[] sourceLines){
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
	
	private static java.util.Date parseExDate(String exDate) throws ParseException{
		java.util.Date result=new java.util.Date();
			try {
				result=DateUtil.literalToDate(exDate);
			} catch (ParseException e1) {
				try {
					result=DateUtil.DOYToDate(exDate);
				} catch (ParseException e2) {
						System.out.println("Could not parse "+exDate);
				}
			}
		
		return result;
	}


	
}


