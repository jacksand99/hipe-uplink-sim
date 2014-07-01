package vega.uplink.commanding;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.util.List;

import vega.uplink.Properties;
import herschel.binstruct.tools.*;
import herschel.ia.dataset.TableDataset;
import herschel.ia.io.ascii.AsciiTableTool;
import herschel.ia.io.ascii.CsvParser;
import herschel.ia.io.ascii.TableTemplate;
import herschel.share.interpreter.InterpreterUtil;
import herschel.share.util.Configuration;
import herschel.share.util.ObjectUtil;

public class Mib {
	static Mib MIB;
	//public static String ROSETTA_MIB_LOCATION="rosetta.mib.location";
	TableDataset sdf_table;
	TableDataset csp_table;
	static String[] sdf_columns={"SDF_SQNAME","SDF_ENTRY","SDF_ELEMID","SDF_POS","SDF_PNAME","SDF_FTYPE","SDF_VTYPE","SDF_VALUE","SDF_VALSET","SDF_REPPOS"};
	static String[] sdf_types={"String","Integer","String","Integer","String","String","String","String","String","Integer"}; 
	static String[] csp_columns={"CSP_SQNAME","CSP_FPNAME","CSP_FPNUM","CSP_DESCR","CSP_PTC","CSP_PFC","CSP_DISPFMT","CSP_RADIX","CSP_TYPE","CSP_VTYPE","CSP_DEFVAL","CSP_CATEG","CSP_PRFREF","CSP_CCAREF","CSP_PAFREF","CSP_UNIT"};
	static String[] csp_types={"String","String","Integer","String","Integer","Integer","String","String","String","String","String","String","String","String","String","String"};


	private Mib(){
		sdf_table=new TableDataset();
		csp_table=new TableDataset();
	}
	public static Mib getMibFromFiles() throws IOException{
		FileInputStream sdf_data=new FileInputStream(Properties.MIB_LOCATION+"sdf.dat");
		FileInputStream csp_data=new FileInputStream(Properties.MIB_LOCATION+"sdf.dat");
		return readMib(sdf_data,csp_data);
/*		MibAsciiTableImporter importer=new MibAsciiTableImporter();
		importer.setMibAsciiTableLocation(rosettaMibLocation);
		try{
			importer.makeConfiguration("sdf",sdf_columns,sdf_types);
			sdf_table=importer.load("sdf");
			importer.makeConfiguration("csp",csp_columns,csp_types);
			csp_table=importer.load("csp");
			
		}catch (Exception e){
			e.printStackTrace();

		}*/
		
	}
	
	private static Mib readMib(InputStream sdfIS,InputStream cspIS) throws IOException{
		Mib result=new Mib();
		//try{
	        CsvParser parser = new CsvParser();
	        parser.setDelimiter('\t');
			AsciiTableTool att=new AsciiTableTool();
			TableTemplate sdf_template=new TableTemplate(sdf_columns.length);
			sdf_template.setNames(sdf_columns);
			sdf_template.setTypes(sdf_types);
			TableTemplate csp_template=new TableTemplate(csp_columns.length);
			csp_template.setNames(csp_columns);
			csp_template.setTypes(csp_types);
			att.setTemplate(sdf_template);
			att.setParser(parser);
			result.sdf_table=att.load(sdfIS);
			att.setTemplate(csp_template);
			result.csp_table=att.load(cspIS);
		//}catch(Exception e){
		//	e.printStackTrace();
		//}
		return result;

	}
	
	public static Mib getMibFromJar() throws IOException{
		InputStream sdf_data = ObjectUtil.getClass("vega.uplink.commanding.Por").getResourceAsStream("/mib/sdf.dat");
		InputStream csp_data = ObjectUtil.getClass("vega.uplink.commanding.Por").getResourceAsStream("/mib/csp.dat");
		return readMib(sdf_data,csp_data);


	}
	
	
	public boolean checkSequence(String sequence){
		TableDataset paramsForCommand=findInTable(csp_table,"CSP_SQNAME",sequence);
		if (paramsForCommand.getRowCount()>0) return true;
		else return false;
	}
	
	public boolean checkParameterSequence(String sequence,String parameter){
		
		TableDataset paramsForCommand=findInTable(csp_table,"CSP_SQNAME",sequence);
		TableDataset resultTable=findInTable(paramsForCommand,"CSP_FPNAME",parameter);
		if (resultTable.getRowCount()>0) return true;
		else return false;
		
	}
	
	//public TableDataset 
	
	public TableDataset findInTable(TableDataset table, String columnName, String search){
		return table.select(table.getColumn(columnName).getData().where(new herschel.binstruct.util.String1dRegex(search)));
	}
	
	public Parameter getDefaultParameter(String parameterName){
		Parameter result=new Parameter(parameterName,"Default","Default");
		TableDataset resultTable=findInTable(csp_table,"CSP_FPNAME",parameterName);
		java.util.List<Object> row = resultTable.getRow(0);
		String value_type=(String) row.get(9);
		String value=(String) row.get(10);
		String radix=(String) row.get(7);
		String final_radix="";
		if (radix.equals("D")) final_radix=Parameter.RADIX_DECIMAL;
		if (radix.equals("H")) final_radix=Parameter.RADIX_HEX;
		if (radix.equals("O")) final_radix=Parameter.RADIX_OCTAL;
				
		if (value_type.equals("R")){
			
			if (radix.equals("D"))result=new ParameterFloat(parameterName,Parameter.REPRESENTATION_RAW,final_radix,new Float(value).floatValue());
			if (radix.equals("H"))result=new ParameterFloat(parameterName,Parameter.REPRESENTATION_RAW,final_radix,Long.parseLong(value, 16));
			
		}

		if (value_type.equals("E")){
			result=new ParameterString(parameterName,Parameter.REPRESENTATION_ENGINEERING,value);
		}

		return result;
	}
	
	static public Mib getMib() throws IOException{
		if (MIB==null){
			try{
				MIB=Mib.getMibFromFiles();
			}
			catch(IOException ioe){
					MIB=Mib.getMibFromJar();
			}
			//MIB=new Mib();
		}
		if (MIB==null) return new Mib();
		return MIB;
	}
}
