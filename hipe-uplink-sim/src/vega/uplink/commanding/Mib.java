package vega.uplink.commanding;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.io.IOUtils;

import vega.uplink.Properties;
import herschel.ia.dataset.CompositeDataset;
import herschel.ia.dataset.TableDataset;
import herschel.ia.io.ascii.AsciiTableTool;
import herschel.ia.io.ascii.CsvParser;
import herschel.ia.io.ascii.TableTemplate;
import herschel.ia.numeric.String1d;
import vega.hipe.FileUtil;
import herschel.share.util.ObjectUtil;

/**
 * The MIB to be used. It only contains the tables from the MIB relevant for mission planning
 * The tables read are SDF, CSP, CDF, CPC and CSS
 * @author jarenas
 *
 */
public class Mib extends CompositeDataset{
	static Mib MIB;
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
	private HashMap<String,Long> cacheDuration;
	private HashMap<String,String> cacheSeqDescription;
	private HashMap<String,String> cacheParamDescription;

	private Mib(){
		this.set("sdf_table", new TableDataset());
		this.set("csp_table", new TableDataset());
		this.set("cdf_table", new TableDataset());
		this.set("cpc_table", new TableDataset());
		this.set("css_table", new TableDataset());
		cacheDuration=new HashMap<String,Long>();
		cacheSeqDescription=new HashMap<String,String>();
		cacheParamDescription=new HashMap<String,String>();
	}
	/*public static Mib getMibFromTarFile(String tarFile) throws IOException {
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
	}*/
	
	/**
	 * Read the MIB from files. The location will be read from the properties
	 * @return the MIB
	 * @throws IOException In the case the files are not found or are not readable 
	 */
	public static Mib getMibFromFiles() throws IOException{
		FileInputStream sdf_data=new FileInputStream(Properties.getProperty(Properties.MIB_LOCATION)+"/"+"sdf.dat");
		FileInputStream csp_data=new FileInputStream(Properties.getProperty(Properties.MIB_LOCATION)+"/"+"csp.dat");
		FileInputStream cdf_data=new FileInputStream(Properties.getProperty(Properties.MIB_LOCATION)+"/"+"cdf.dat");
		FileInputStream cpc_data=new FileInputStream(Properties.getProperty(Properties.MIB_LOCATION)+"/"+"cdf.dat");
		FileInputStream css_data=new FileInputStream(Properties.getProperty(Properties.MIB_LOCATION)+"/"+"css.dat");

		return readMib(sdf_data,csp_data,cdf_data,cpc_data,css_data);
		
	}
	
	private static Mib readMib(InputStream sdfIS,InputStream cspIS,InputStream cdfIS,InputStream cpcIS,InputStream cssIS) throws IOException{
		Mib result=new Mib();
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
		att.setTemplate(csp_template);
		result.set("csp_table", att.load(cspIS));
		att.setTemplate(cdf_template);
		result.set("cdf_table", att.load(cdfIS));
		att.setTemplate(cpc_template);
		result.set("cpc_table", att.load(cdfIS));
		att.setTemplate(css_template);
		result.set("css_table", att.load(cssIS));

		return result;

	}
	
	/**
	 * Read the MIB from the jars in the class path. The need to be in a directory called mib in the first level of the class path
	 * @return The MIB
	 * @throws IOException IN case that the directory mib is not found in the class path
	 */
	public static Mib getMibFromJar() throws IOException{
		InputStream sdf_data = ObjectUtil.getClass("vega.uplink.commanding.Por").getResourceAsStream("/mib/sdf.dat");
		InputStream csp_data = ObjectUtil.getClass("vega.uplink.commanding.Por").getResourceAsStream("/mib/csp.dat");
		InputStream cdf_data = ObjectUtil.getClass("vega.uplink.commanding.Por").getResourceAsStream("/mib/cdf.dat");
		InputStream cpc_data = ObjectUtil.getClass("vega.uplink.commanding.Por").getResourceAsStream("/mib/cpc.dat");
		InputStream css_data = ObjectUtil.getClass("vega.uplink.commanding.Por").getResourceAsStream("/mib/css.dat");
		return readMib(sdf_data,csp_data,cdf_data,cpc_data,css_data);
	}
	
	
	/**
	 * Check if a sequence exist in the MIB
	 * @param sequence The sequence name to be checked
	 * @return True if the sequence exist and false otherwise
	 */
	public boolean checkSequence(String sequence){
		TableDataset paramsForCommand=findInTable((TableDataset)this.get("csp_table"),"CSP_SQNAME",sequence);
		if (paramsForCommand.getRowCount()>0) return true;
		else return false;
	}
	
	/**
	 * Get the defaulr parameters for a sequence name
	 * @param sequence The sequence name
	 * @return Array with the default parameters defined in the mib for the given sequence
	 */
	public Parameter[] getDefaultParameters(String sequence){
		TableDataset paramsForCommand=findInTable((TableDataset)this.get("csp_table"),"CSP_SQNAME",sequence);
		String1d paramNames=(String1d) paramsForCommand.getColumn("CSP_FPNAME").getData();
		Parameter[] result=new Parameter[paramNames.length()];
		for (int i=0;i<paramNames.length();i++){
			result[i]=getDefaultParameter(paramNames.get(i));
		}
		return result;
	}
	/**
	 * Check if a particular sequence has a given parameter defined in the MIB or not
	 * @param sequence  Sequence name
	 * @param parameter Parameter name
	 * @return True if the parameter is defined in the MIB for the given sequence. False otherwise
	 */
	public boolean checkParameterSequence(String sequence,String parameter){
		TableDataset paramsForCommand=findInTable((TableDataset)this.get("csp_table"),"CSP_SQNAME",sequence);
		
		TableDataset resultTable=findInTable(paramsForCommand,"CSP_FPNAME",parameter);
		if (resultTable.getRowCount()>0){

			return true;
		}
		else return false;
		
	}
	/**
	 * Check if the representation and radix of a parameter is the same as defined in the MIB
	 * @param param Parameter to be checked
	 * @return True if the parameter representation and radix are the same as defined in the MIB
	 * @throws IllegalArgumentException If the representation or radix are different. The message in the exception will actually say what is the problem
	 * 
	 */
	public boolean checkParameter(Parameter param){
		TableDataset resultTable=findInTable((TableDataset)this.get("csp_table"),"CSP_FPNAME",param.getName());
		java.util.List<Object> row = resultTable.getRow(0);
		String value_type=(String) row.get(9);
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
		}
		if (final_representation.equals(Parameter.REPRESENTATION_RAW) && !final_radix.equals(param.getRadix())){
			IllegalArgumentException iae = new IllegalArgumentException("Radix for parameter "+param.getName()+" in the MIB is "+final_radix+" but in the sequence is "+param.getRadix());
			throw(iae);
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
		}
		Parameter[] params = seq.getParameters();
		for (int i=0;i<params.length;i++){
			if(!checkParameterSequence(seq.getName(),params[i].getName())){
				IllegalArgumentException iae = new IllegalArgumentException("Parameter "+params[i].getName()+" not valid for sequence "+seq.getName()+" definition not present in the MIB");
				throw(iae);
			}
			if(!checkParameter(params[i])) return false;
		}
		return true;
	}
	
	private TableDataset findInTable(TableDataset table, String columnName, String search){
		return table.select(table.getColumn(columnName).getData().where(new herschel.binstruct.util.String1dRegex(search)));
	}
	
	/**
	 * Get the default Parameter definition in the MIB for a parameter name
	 * @param parameterName The parameter name
	 * @return The parameter as default definition in the MIB
	 */
	public Parameter getDefaultParameter(String parameterName){
		Parameter result=new Parameter(parameterName,"Default","Default");
		TableDataset resultTable=findInTable((TableDataset)this.get("csp_table"),"CSP_FPNAME",parameterName);
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

		}

		if (value_type.equals("E")){
			result=new ParameterString(parameterName,Parameter.REPRESENTATION_ENGINEERING,value);
		}

		return result;
	}
	
	/**
	 * Get the MIB. It will try first to read the MIB form files, from the location defined in the properties.
	 * Then it will try to read it form the jar files in the class path. If it fails as well, the it will crate an empty MIB.
	 * @return the MIB
	 */
	static public Mib getMib() {
		if (MIB==null){
			try{
				MIB=Mib.getMibFromFiles();
				LOG.info("MIB read from files");
			}
			catch(Exception ioe){
					LOG.info("MIB failed read from files:"+ioe.getMessage());

					try{
						MIB=Mib.getMibFromJar();
						LOG.info("MIB read from jar");
					}catch (Exception e){
						LOG.info("MIB failed read from jar. Creatting an empty MIB."+e.getMessage());

						MIB=new Mib();
					}
			}
		}
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
	
	/**
	 * Get the description of a parameter in the MIB
	 * @param parameterName
	 * @return The description if the parameter
	 */
	public String getParameterDescription(String parameterName){
		String cDescription = cacheParamDescription.get(parameterName);
		if (cDescription!=null) return cDescription;

		try{
			TableDataset resultTable=findInTable((TableDataset)this.get("csp_table"),"CSP_FPNAME",parameterName);
			java.util.List<Object> row = resultTable.getRow(0);
			String description=(String) row.get(3);
			cacheParamDescription.put(parameterName, description);
			return description;

		}catch (Exception e){
			return "UNKNOWN";
		}
	}
	/**
	 * Return an array with all valid sequences names
	 * @return
	 */
	public String[] getAllSequences(){
		return removeDuplicates(((String1d)((TableDataset)this.get("css_table")).getColumn("CSS_SQNAME").getData()).toArray());
	}
	private static String[] removeDuplicates(String[] arr) {
		  return new HashSet<String>(Arrays.asList(arr)).toArray(new String[0]);
		}
	
	/**
	 * Get the description in the MIB of a given sequence
	 * @param sequenceName Sequence name
	 * @return The description of the sequence
	 */
	public String getSequenceDescription(String sequenceName){
		String cDescription = cacheSeqDescription.get(sequenceName);
		if (cDescription!=null) return cDescription;
		try{
			TableDataset resultTable=findInTable((TableDataset)this.get("css_table"),"CSS_SQNAME",sequenceName);
			java.util.List<Object> row = resultTable.getRow(0);
			String description=(String) row.get(1);
			cacheSeqDescription.put(sequenceName, description);
			return description;
		}catch (Exception e){
			return "UNKNOWN";
		}
	}
	
	/**
	 * Get the total duration in seconds of a sequence as defined in the MIB
	 * @param sequenceName The sequence name
	 * @return Duration in seconds
	 */
	public long getTotalSequenceDuration(String sequenceName){
		Long cDuration = cacheDuration.get(sequenceName);
		if (cDuration!=null) return cDuration;
		long result=0;
		try{
			TableDataset resultTable=findInTable((TableDataset)this.get("css_table"),"CSS_SQNAME",sequenceName);
			int rows = resultTable.getRowCount();
			for (int i=0;i<rows;i++){
				java.util.List<Object> row = resultTable.getRow(i);
				String tcDuration=(String) row.get(8);
				result=result+Mib.mibTimeToSecs(tcDuration);
			}
			cacheDuration.put(sequenceName, result);
			return result;
		}catch (Exception e){
			return 0;
		}
		
	}
	
	private static long mibTimeToSecs(String mibTime){
		StringTokenizer items=new StringTokenizer(mibTime,".");
		int h=Integer.parseInt(items.nextToken());
		int m=Integer.parseInt(items.nextToken());
		int s=Integer.parseInt(items.nextToken());
		long result=s+(m*60)+(h*60*60);
		return result;
	}

}
