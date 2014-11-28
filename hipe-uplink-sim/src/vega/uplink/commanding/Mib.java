package vega.uplink.commanding;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.io.IOUtils;

import vega.uplink.Properties;
import herschel.binstruct.tools.*;
import herschel.ia.dataset.CompositeDataset;
import herschel.ia.dataset.TableDataset;
//import herschel.ia.io.ArchiveException;
import herschel.ia.io.ascii.AsciiTableTool;
import herschel.ia.io.ascii.CsvParser;
import herschel.ia.io.ascii.TableTemplate;
import herschel.ia.numeric.String1d;
import herschel.share.interpreter.InterpreterUtil;
//import herschel.share.io.FileUtil;
import vega.hipe.FileUtil;
import herschel.share.util.Configuration;
import herschel.share.util.ObjectUtil;

public class Mib extends CompositeDataset{
	static Mib MIB;
	//public static String ROSETTA_MIB_LOCATION="rosetta.mib.location";
	//TableDataset sdf_table;
	//TableDataset csp_table;
	private static final Logger LOG = Logger.getLogger(Mib.class.getName());
	static String[] sdf_columns={"SDF_SQNAME","SDF_ENTRY","SDF_ELEMID","SDF_POS","SDF_PNAME","SDF_FTYPE","SDF_VTYPE","SDF_VALUE","SDF_VALSET","SDF_REPPOS"};
	static String[] sdf_types={"String","Integer","String","Integer","String","String","String","String","String","Integer"}; 
	static String[] csp_columns={"CSP_SQNAME","CSP_FPNAME","CSP_FPNUM","CSP_DESCR","CSP_PTC","CSP_PFC","CSP_DISPFMT","CSP_RADIX","CSP_TYPE","CSP_VTYPE","CSP_DEFVAL","CSP_CATEG","CSP_PRFREF","CSP_CCAREF","CSP_PAFREF","CSP_UNIT"};
	static String[] csp_types={"String","String","Integer","String","Integer","Integer","String","String","String","String","String","String","String","String","String","String"};
	static String[] cdf_columns={"CDF_CNAME","CDF_ELTYPE","CDF_DESCR","CDF_ELLEN","CDF_BIT","CDF_GRPSIZE","CDF_PNAME","CDF_INTER","CDF_VALUE","CDF_TMID"};
	static String[] cdf_types={"String","String","String","Integer","Integer","Integer","String","String","String","String"};
	static String[] cpc_columns={"CPC_CNAME","CPC_DESCR","CPC_PTC","CPC_PFC","CPC_DISPFMT","CPC_RADIX","CPC_UNIT","CPC_CATEG","CPC_PRFREF","CPC_CCAREF","CPC_PAFREF","CPC_INTER","CPC_DEFVAL"};
	static String[] cpc_types={"String","String","Integer","Integer","String","String","String","String","Integer","Integer","Integer","String","String"};

	static String[] css_columns={"CSS_SQNAME","CSS_COMM","CSS_ENTRY","CSS_TYPE","CSS_ELEMID","CSS_NPARS","CSS_MANDISP","CPC_CATEG","CSS_RELTYPE","CSS_RELTIME","CSS_EXTIME","CSS_PREVREL","CSS_GROUPL","CSS_BLOCK","CSS_ILSCOPE","CSS_ILSTAGE","CSS_DYNPTV","CSS_STAPTV","CSS_CEV"};
	static String[] css_types={"String","String","Integer","String","String","Integer","String","String","String","String","String","String","String","String","String","String","String","String","String"};

	private Mib(){
		this.set("sdf_table", new TableDataset());
		this.set("csp_table", new TableDataset());
		this.set("cdf_table", new TableDataset());
		this.set("cpc_table", new TableDataset());
		this.set("css_table", new TableDataset());
		

		//sdf_table=new TableDataset();
		//csp_table=csp_table
	}
	public static Mib getMibFromTarFile(String tarFile) throws IOException {
		File tdir=null;
		
		try{
			tdir = FileUtil.createTempDir(tarFile+"_temp_tar", "untar");
			unTar(tarFile,tdir);
			FileInputStream sdf_data=new FileInputStream(tdir.getAbsolutePath()+"/"+"sdf.dat");
			FileInputStream csp_data=new FileInputStream(tdir.getAbsolutePath()+"/"+"csp.dat");
			FileInputStream cdf_data=new FileInputStream(tdir.getAbsolutePath()+"/"+"cdf.dat");
			FileInputStream cpc_data=new FileInputStream(tdir.getAbsolutePath()+"/"+"cdf.dat");
			FileInputStream css_data=new FileInputStream(tdir.getAbsolutePath()+"/"+"css.dat");

			return readMib(sdf_data,csp_data,cdf_data,cpc_data,css_data);
		}catch (ArchiveException aex){
			IOException ioe = new IOException(aex.getMessage());
			ioe.initCause(aex);
			throw ioe;
			
		}
		finally{
			if (tdir!=null) tdir.delete();
		}
	}
	public static Mib getMibFromFiles() throws IOException{
		FileInputStream sdf_data=new FileInputStream(Properties.getProperty(Properties.MIB_LOCATION)+"/"+"sdf.dat");
		FileInputStream csp_data=new FileInputStream(Properties.getProperty(Properties.MIB_LOCATION)+"/"+"csp.dat");
		FileInputStream cdf_data=new FileInputStream(Properties.getProperty(Properties.MIB_LOCATION)+"/"+"cdf.dat");
		FileInputStream cpc_data=new FileInputStream(Properties.getProperty(Properties.MIB_LOCATION)+"/"+"cdf.dat");
		FileInputStream css_data=new FileInputStream(Properties.getProperty(Properties.MIB_LOCATION)+"/"+"css.dat");

		return readMib(sdf_data,csp_data,cdf_data,cpc_data,css_data);
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
	
	private static Mib readMib(InputStream sdfIS,InputStream cspIS,InputStream cdfIS,InputStream cpcIS,InputStream cssIS) throws IOException{
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
			TableTemplate cdf_template=new TableTemplate(cdf_columns.length);
			cdf_template.setNames(cdf_columns);
			cdf_template.setTypes(cdf_types);	
			TableTemplate cpc_template=new TableTemplate(cpc_columns.length);
			cpc_template.setNames(cpc_columns);
			cpc_template.setTypes(cpc_types);						
			TableTemplate css_template=new TableTemplate(css_columns.length);
			css_template.setNames(css_columns);
			css_template.setTypes(css_types);						

			att.setTemplate(sdf_template);
			att.setParser(parser);
			result.set("sdf_table", att.load(sdfIS));
			//result.sdf_table=att.load(sdfIS);
			att.setTemplate(csp_template);
			result.set("csp_table", att.load(cspIS));
			att.setTemplate(cdf_template);
			result.set("cdf_table", att.load(cdfIS));
			att.setTemplate(cpc_template);
			result.set("cpc_table", att.load(cdfIS));
			att.setTemplate(css_template);
			result.set("css_table", att.load(cssIS));

			//result.csp_table=att.load(cspIS);
		//}catch(Exception e){
		//	e.printStackTrace();
		//}
		return result;

	}
	
	public static Mib getMibFromJar() throws IOException{
		InputStream sdf_data = ObjectUtil.getClass("vega.uplink.commanding.Por").getResourceAsStream("/mib/sdf.dat");
		InputStream csp_data = ObjectUtil.getClass("vega.uplink.commanding.Por").getResourceAsStream("/mib/csp.dat");
		InputStream cdf_data = ObjectUtil.getClass("vega.uplink.commanding.Por").getResourceAsStream("/mib/cdf.dat");
		InputStream cpc_data = ObjectUtil.getClass("vega.uplink.commanding.Por").getResourceAsStream("/mib/cpc.dat");
		InputStream css_data = ObjectUtil.getClass("vega.uplink.commanding.Por").getResourceAsStream("/mib/css.dat");

		return readMib(sdf_data,csp_data,cdf_data,cpc_data,css_data);


	}
	
	
	public boolean checkSequence(String sequence){
		TableDataset paramsForCommand=findInTable((TableDataset)this.get("csp_table"),"CSP_SQNAME",sequence);
		//TableDataset paramsForCommand=findInTable(csp_table,"CSP_SQNAME",sequence);
		if (paramsForCommand.getRowCount()>0) return true;
		else return false;
	}
	
	public Parameter[] getDefaultParameters(String sequence){
		TableDataset paramsForCommand=findInTable((TableDataset)this.get("csp_table"),"CSP_SQNAME",sequence);
		String1d paramNames=(String1d) paramsForCommand.getColumn("CSP_FPNAME").getData();
		Parameter[] result=new Parameter[paramNames.length()];
		for (int i=0;i<paramNames.length();i++){
			//Parameter param = getDefaultParameter(paramNames.get(i));
			
			result[i]=getDefaultParameter(paramNames.get(i));
		}
		return result;
	}
	public boolean checkParameterSequence(String sequence,String parameter){
		TableDataset paramsForCommand=findInTable((TableDataset)this.get("csp_table"),"CSP_SQNAME",sequence);
		
		//TableDataset paramsForCommand=findInTable(csp_table,"CSP_SQNAME",sequence);
		TableDataset resultTable=findInTable(paramsForCommand,"CSP_FPNAME",parameter);
		if (resultTable.getRowCount()>0){

			return true;
		}
		else return false;
		
	}
	public boolean checkParameter(Parameter param){
		//Parameter deParam = getDefaultParameter(param.getName());
		Parameter result=new Parameter(param.getName(),"Default","Default");
		TableDataset resultTable=findInTable((TableDataset)this.get("csp_table"),"CSP_FPNAME",param.getName());
		//TableDataset resultTable=findInTable(csp_table,"CSP_FPNAME",parameterName);
		java.util.List<Object> row = resultTable.getRow(0);
		String value_type=(String) row.get(9);
		String value=(String) row.get(10);
		String radix=(String) row.get(7);
		String final_radix="";
		String final_representation="";
		if (radix.equals("D")) final_radix=Parameter.RADIX_DECIMAL;
		if (radix.equals("H")) final_radix=Parameter.RADIX_HEX;
		if (radix.equals("O")) final_radix=Parameter.RADIX_OCTAL;
		if (value_type.equals("E")) final_representation=Parameter.REPRESENTATION_ENGINEERING;
		if (value_type.equals("R")) final_representation=Parameter.REPRESENTATION_RAW;
		
		if (!final_representation.equals(param.getRepresentation())){
			IllegalArgumentException iae = new IllegalArgumentException("Representation for parameter "+param.getName()+" in the MIB is "+final_representation+" but in the sequence is "+param.getRepresentation());
			throw(iae);

			//return false;
		}
		if (!final_representation.equals(Parameter.REPRESENTATION_RAW) && !final_radix.equals(param.getRadix())){
			IllegalArgumentException iae = new IllegalArgumentException("Radix for parameter "+param.getName()+" in the MIB is "+final_radix+" but in the sequence is "+param.getRadix());
			throw(iae);
			
			//return false;
		}
		return true;
	}
	public boolean checkSequence(SequenceInterface seq){
		TableDataset paramsForCommand=findInTable((TableDataset)this.get("csp_table"),"CSP_SQNAME",seq.getName());
		if (paramsForCommand.getRowCount()<=0){
			TableDataset sequencesTable=findInTable((TableDataset)this.get("sdf_table"),"SDF_SQNAME",seq.getName());
			if (sequencesTable.getRowCount()<=0){
				TableDataset sequences2Table=findInTable((TableDataset)this.get("css_table"),"CSS_SQNAME",seq.getName());
				if (sequences2Table.getRowCount()<=0){
					
					IllegalArgumentException iae = new IllegalArgumentException("Sequence "+seq.getName()+" definition not present in the MIB");
					throw(iae);
				}
			}
			return true;
			//return false;
		}
		Parameter[] params = seq.getParameters();
		for (int i=0;i<params.length;i++){
			if(!checkParameterSequence(seq.getName(),params[i].getName())){
				IllegalArgumentException iae = new IllegalArgumentException("Parameter "+params[i].getName()+" not valid for sequence "+seq.getName()+" definition not present in the MIB");
				throw(iae);
				
				//return false;
			}
			if(!checkParameter(params[i])) return false;
		}
		return true;
	}
	
	//public TableDataset 
	
	public TableDataset findInTable(TableDataset table, String columnName, String search){
		return table.select(table.getColumn(columnName).getData().where(new herschel.binstruct.util.String1dRegex(search)));
	}
	
	public Parameter getDefaultParameter(String parameterName){
		Parameter result=new Parameter(parameterName,"Default","Default");
		TableDataset resultTable=findInTable((TableDataset)this.get("csp_table"),"CSP_FPNAME",parameterName);
		//TableDataset resultTable=findInTable(csp_table,"CSP_FPNAME",parameterName);
		java.util.List<Object> row = resultTable.getRow(0);
		String value_type=(String) row.get(9);
		String value=(String) row.get(10);
		String radix=(String) row.get(7);
		String final_radix="";
		if (radix.equals("D")) final_radix=Parameter.RADIX_DECIMAL;
		if (radix.equals("H")) final_radix=Parameter.RADIX_HEX;
		if (radix.equals("O")) final_radix=Parameter.RADIX_OCTAL;
				
		if (value_type.equals("R")){
			
			if (radix.equals("D") && value!=null) result=new ParameterFloat(parameterName,Parameter.REPRESENTATION_RAW,final_radix,new Float(value).floatValue());
			if (radix.equals("H") && value!=null) result=new ParameterFloat(parameterName,Parameter.REPRESENTATION_RAW,final_radix,Long.parseLong(value, 16));
			/*if (radix.equals("D") && value==null) result=new ParameterFloat(parameterName,Parameter.REPRESENTATION_RAW,final_radix,-1);
			if (radix.equals("H") && value==null) result=new ParameterFloat(parameterName,Parameter.REPRESENTATION_RAW,final_radix,Long.parseLong("-1", 16));*/

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
				LOG.info("MIB read from files");
			}
			catch(IOException ioe){
					LOG.info("MIB failed read from files:"+ioe.getMessage());

					try{
						MIB=Mib.getMibFromJar();
						LOG.info("MIB read from jar");
					}catch (Exception e){
						LOG.info("MIB failed read from jar. Creatting an empty MIB."+e.getMessage());

						MIB=new Mib();
					}
			}
			//MIB=new Mib();
		}
		//if (MIB==null) return new Mib();
		return MIB;
	}
	private static List<File> unTar(final String inputFileName, final File outputDir) throws FileNotFoundException, IOException, ArchiveException {
		File inputFile=new File(inputFileName);
	    LOG.info(String.format("Untaring %s to dir %s.", inputFile.getAbsolutePath(), outputDir.getAbsolutePath()));

	    final List<File> untaredFiles = new LinkedList<File>();
	    final InputStream is = new FileInputStream(inputFile); 
	    final TarArchiveInputStream debInputStream = (TarArchiveInputStream) new ArchiveStreamFactory().createArchiveInputStream("tar", is);
	    TarArchiveEntry entry = null; 
	    while ((entry = (TarArchiveEntry)debInputStream.getNextEntry()) != null) {
	        final File outputFile = new File(outputDir, entry.getName());
	        if (entry.isDirectory()) {
	            LOG.info(String.format("Attempting to write output directory %s.", outputFile.getAbsolutePath()));
	            if (!outputFile.exists()) {
	                LOG.info(String.format("Attempting to create output directory %s.", outputFile.getAbsolutePath()));
	                if (!outputFile.mkdirs()) {
	                    throw new IllegalStateException(String.format("Couldn't create directory %s.", outputFile.getAbsolutePath()));
	                }
	            }
	        } else {
	            LOG.info(String.format("Creating output file %s.", outputFile.getAbsolutePath()));
	            final OutputStream outputFileStream = new FileOutputStream(outputFile); 
	            IOUtils.copy(debInputStream, outputFileStream);
	            outputFileStream.close();
	        }
	        untaredFiles.add(outputFile);
	    }
	    debInputStream.close(); 

	    return untaredFiles;
	}
}
