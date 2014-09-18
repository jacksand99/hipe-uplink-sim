package vega.uplink.commanding;

import herschel.ia.dataset.Column;
import herschel.ia.dataset.StringParameter;
import herschel.ia.dataset.TableDataset;
import herschel.ia.numeric.Float1d;
import herschel.ia.numeric.String1d;
import herschel.share.util.ObjectUtil;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.TreeSet;


public class Orcd extends TableDataset{
	
	public static int INDEX_MODE=0;
	public static int INDEX_POWER=1;
	public static int INDEX_SEQUENCE=2;
	public static int INDEX_OFFSET=3;
	public static int INDEX_ALLOWED=4;
	public static int INDEX_NOT_ALLOWED=5;
	
	
	public Orcd(){
		super();
		Column mode=new Column(new String1d());
		
		Column power=new Column(new String1d());
		Column sequence=new Column(new String1d());
		Column offSet=new Column(new String1d());
		Column allowed=new Column(new String1d());
		Column notAllowed=new Column(new String1d());

		this.addColumn(mode);
		this.addColumn(power);
		this.addColumn(sequence);
		this.addColumn(offSet);
		this.addColumn(allowed);
		this.addColumn(notAllowed);		
		this.setColumnName(INDEX_MODE, "Destination Mode");
		this.setColumnName(INDEX_POWER, "Power");
		this.setColumnName(INDEX_SEQUENCE, "Transition via sequence");
		this.setColumnName(INDEX_OFFSET, "Transition Offset");
		this.setColumnName(INDEX_ALLOWED, "Transition allowed from mode");
		this.setColumnName(INDEX_NOT_ALLOWED, "Transition forbidden from mode");
		this.setVersion("1.0");
	}
	public void setVersion(String version){
		this.getMeta().set("version", new StringParameter(version));
	}
	
	public String getVersion(){
		return (String) this.getMeta().get("version").getValue();
	}
	
	public herschel.ia.dataset.TableDataset asTable(){
		return this;
	}
	
	public java.util.HashMap<Long,String> getModes(String sequence){
		java.util.HashMap<Long,String> result = new java.util.HashMap<Long,String>();
		TableDataset tb=findInTable(this,INDEX_SEQUENCE,sequence);
		for (int i=0;i<tb.getRowCount();i++){
			java.util.List<Object> row = tb.getRow(i);
			long os=getOffsetSeconds((String)row.get(INDEX_OFFSET));
			if (result.containsKey(os)) os=os+i;
			result.put(os , (String) row.get(INDEX_MODE));
			
		}
		return result;
	}
	
	public java.util.HashMap<Long,String> getModesAsHistory(String sequence,long initialTime){
		java.util.HashMap<Long,String> result = new java.util.HashMap<Long,String>();
		
		TableDataset tb=findInTable(this,INDEX_SEQUENCE,sequence);
		for (int i=0;i<tb.getRowCount();i++){
			java.util.List<Object> row = tb.getRow(i);
			long tm=(getOffsetSeconds((String)row.get(INDEX_OFFSET))*1000)+initialTime;
			if (result.containsKey(tm)) tm=tm+i;
			result.put(tm , (String) row.get(INDEX_MODE));
			
		}

		return result;
	}
	
	public boolean checkTransion(String mode1, String mode2){
		boolean result=false;
		TableDataset tb=findInTable(this,INDEX_MODE,mode2);
		if (tb.getRowCount()>0){
			java.util.List<Object> row = tb.getRow(0);
			result=isSequenceInList(mode1,(String) row.get(INDEX_ALLOWED));
			
			if (!isSequenceInList("null",(String) row.get(INDEX_NOT_ALLOWED))){
				result=!isSequenceInList(mode1,(String) row.get(INDEX_NOT_ALLOWED));
				
			}
			
			
		}
		
		
		return result;
	}
	
	
	public float getPowerForMode(String mode){
		float result=0;
		TableDataset tb=findInTable(this,INDEX_MODE,mode);
		if (tb.getRowCount()>0){
			java.util.List<Object> row = tb.getRow(0);
			result=new Float((String) row.get(INDEX_POWER)).floatValue();
		}
		
		return result;
	}
	
	public float getTotalPowerForModes(String[] modes){
		float result=0;
		for (int i=0;i<modes.length;i++){
			result=result+getPowerForMode(modes[i]);
		}
		return result;
	}
	
	private boolean isSequenceInList(String seq,String list){
		//System.out.println("Find "+seq+" in "+list);
		String[] arr=list.split(" ");
		boolean result=false;
		for (int i=0;i<arr.length;i++){
			if (arr[i].equals(seq)) result=true;
		}
		return result;
	}
	
	public TableDataset findInTable(TableDataset table, int columnIndex, String search){
		TableDataset result=table.select(table.getColumn(columnIndex).getData().where(new herschel.binstruct.util.String1dRegex(search)));
		return result;
	}
	
	public void addRow(String mode,String power,String sequence,String offSet,String allowed,String notAllowed){
		String[] array=new String[6];
		array[0]=mode;
		array[1]=power;
		array[2]=sequence;
		array[3]=offSet;
		array[4]=allowed;
		array[5]=notAllowed;
		this.addRow(array);
	}
	public void writeToFile(String file){
		try{
			PrintWriter writer = new PrintWriter(file, "UTF-8");
			writer.print("#Mode,Cons,transition via,allowed,not allowed\n");
			int size=this.getRowCount();
			for (int i=0;i<size;i++){
				List<Object> row = this.getRow(i);
				String mode = (String) row.get(INDEX_MODE);
				String power = (String) row.get(INDEX_POWER);
				String sequence = (String) row.get(INDEX_SEQUENCE);
				String offset = (String) row.get(INDEX_OFFSET);
				String allowed = (String) row.get(INDEX_ALLOWED);
				String notAllowed = (String) row.get(INDEX_NOT_ALLOWED);
				writer.print(mode+","+power+","+sequence+"+"+offset+","+allowed+","+notAllowed+"\n");

			}
			//writer.print(PORtoITL(POR));
			writer.close();
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	private static Orcd readORCDBuffer(BufferedReader br){
		Orcd result= new Orcd();
		String line = "";
		String cvsSplitBy = ",";
		try {
			 
			while ((line = br.readLine()) != null) {
				if (!line.startsWith("#")){
			        // use comma as separator
					String[] fields = line.split(cvsSplitBy);
					//int size=fields.length;
					String[] cArray=fields[2].split(" ");
					for (int i=0;i<cArray.length;i++){
						String seq="";
						String off="00:00:00";
						try{
							String[] temp=cArray[i].split("\\+");
							seq=temp[0];
							if (temp.length==2){
								off=temp[1];
							}
						}catch (Exception e){
								e.printStackTrace();
								seq=cArray[i];
								off="00:00:00";
						}
						//System.out.println(fields[0]+","+fields[1]+","+seq+","+off+","+fields[3]+","+fields[4]);
		
						result.addRow(fields[0], fields[1], seq, off, fields[3], fields[4]);
					}
					//System.out.println("Country [code= " + country[4] + " , name=" + country[5] + "]");
				}
			}
	 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	 
		System.out.println(result);
		return result;


	}
	public static Orcd readORCDfromJar(){
		
		InputStream is = ObjectUtil.getClass("vega.uplink.commanding.Por").getResourceAsStream("/mib/orcd.csv");
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		return readORCDBuffer(br);
	}
	
	public static Orcd readORCDfile(String csvFile){
		try{
			return readORCDBuffer(new BufferedReader(new FileReader(csvFile)));
		}catch(Exception e){
			e.printStackTrace();
			return new Orcd();
		}
	  
	}
	
	public long getOffsetSeconds(String sOffset){
		long offset=0;
		String[] times=sOffset.split(":");
		int hours=Integer.parseInt(times[0]);
		int minutes=Integer.parseInt(times[1]);
		int seconds=Integer.parseInt(times[2]);
		offset=(hours*3600)+(minutes*60)+seconds;
		return offset;
	}
	
	public String toXml(){
		TreeSet<String> modes=new TreeSet<String>();
		int rowCount=this.getRowCount();
		for (int i=0;i<rowCount;i++){
			String[] array=new String[6];
			array = this.getRow(i).toArray(array);
			String mode=array[0];
			modes.add(mode);
		}
		
		String result="<orcd version="+getVersion()+">\n";
		result=result+"\t<powerModel>\n";
		Iterator<String> it = modes.iterator();
		while (it.hasNext()){
			String mode=it.next();
			result=result+"\t\t<mode name="+mode+">\n";
			//result=result+"\t\t<mode>"+mode+"</mode>\n";
			result=result+"\t\t\t<power>"+this.getPowerForMode(mode)+"</power>\n";
			TableDataset table = findInTable(this, INDEX_MODE, mode);
			int rowCount2=table.getRowCount();
			String allowed="null";
			String notAllowed="null";
			for (int i=0;i<rowCount2;i++){
				String[] array=new String[6];
				array = table.getRow(i).toArray(array);
				//String mode=array[0];
				//String power=array[1];
				String sequence=array[2];
				String offSet=array[3];
				allowed=array[4];
				notAllowed=array[5];
				//result=result+"\t<entry>\n";
				//result=result+"\t\t<mode>"+mode+"</mode>\n";
				//result=result+"\t\t<power>"+power+"</power>\n";
				if (!offSet.equals("00:00:00")) result=result+"\t\t\t<sequence offset=\""+offSet+"\""+">"+sequence+"</sequence>\n";
				else result=result+"\t\t\t<sequence>"+sequence+"</sequence>\n";
				//result=result+"\t\t<offset>"+offSet+"</offset>\n";
				/*if (!allowed.equals("null")){
					StringTokenizer allTok = new StringTokenizer(allowed);
					while(allTok.hasMoreElements()){
						result=result+"\t\t<allowed>"+allTok.nextToken()+"</allowed>\n";
					}
					//result=result+"\t\t<allowed>"+allowed+"</allowed>\n";
				}
				if (!notAllowed.equals("null")){
					StringTokenizer notallTok = new StringTokenizer(notAllowed);
					while(notallTok.hasMoreElements()){
						result=result+"\t\t<notAllowed>"+notallTok.nextToken()+"</notAllowed>\n";
					}
				}*/
				//result=result+"\t</entry>\n";
				
				
			}
			if (!allowed.equals("null")){
				StringTokenizer allTok = new StringTokenizer(allowed);
				while(allTok.hasMoreElements()){
					result=result+"\t\t\t<transitionAllowed>"+allTok.nextToken()+"</transitionAllowed>\n";
				}
			//result=result+"\t\t<allowed>"+allowed+"</allowed>\n";
			}
			if (!notAllowed.equals("null")){
				StringTokenizer notallTok = new StringTokenizer(notAllowed);
				while(notallTok.hasMoreElements()){
					result=result+"\t\t\t<transitionNotAllowed>"+notallTok.nextToken()+"</transitionNotAllowed>\n";
				}
			}

			result=result+"\t\t</mode>\n";
		}
		result = result+"\t</powerModel>\n";

		result = result+"</orcd>";
		return result;
	}
	
}
