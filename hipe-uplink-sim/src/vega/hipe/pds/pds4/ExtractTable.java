package vega.hipe.pds.pds4;



import gov.nasa.arc.pds.xml.generated.DisciplineArea;
import gov.nasa.arc.pds.xml.generated.FileAreaObservational;
import gov.nasa.arc.pds.xml.generated.IdentificationArea;
import gov.nasa.arc.pds.xml.generated.InvestigationArea;
import gov.nasa.arc.pds.xml.generated.MissionArea;
import gov.nasa.arc.pds.xml.generated.ObservingSystem;
import gov.nasa.arc.pds.xml.generated.ProductObservational;
import gov.nasa.arc.pds.xml.generated.TableBinary;
import gov.nasa.arc.pds.xml.generated.TableCharacter;
import gov.nasa.arc.pds.xml.generated.TableDelimited;
import gov.nasa.arc.pds.xml.generated.TargetIdentification;
import gov.nasa.pds.label.object.FieldDescription;
import gov.nasa.pds.label.object.FieldType;
import gov.nasa.pds.label.object.TableRecord;
import gov.nasa.pds.objectAccess.ExporterFactory;
import gov.nasa.pds.objectAccess.ObjectAccess;
import gov.nasa.pds.objectAccess.ObjectProvider;
import gov.nasa.pds.objectAccess.TableReader;
import herschel.ia.dataset.Column;
import herschel.ia.dataset.Product;
import herschel.ia.dataset.StringParameter;
import herschel.ia.dataset.TableDataset;
import herschel.ia.numeric.Array1dData;
import herschel.ia.numeric.Double1d;
import herschel.ia.numeric.Int1d;
import herschel.ia.numeric.Long1d;
import herschel.ia.numeric.String1d;
import herschel.share.fltdyn.time.FineTime;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import vega.hipe.logging.VegaLog;
import vega.uplink.DateUtil;

/*import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;*/

/**
 * Implements a table extraction application. Uses the Apache
 * Jakarta Commons CLI library to parse the command line.
 */
public class ExtractTable {

	private static final String HELP_OPTION = "help";

	private static final String LIST_TABLES_OPTION = "list-tables";

	private static final String FIELDS_OPTION = "fields";

	private static final String INDEX_OPTION = "index";

	private static final String OUTPUT_FILE_OPTION = "output-file";

	private static final String CSV_OPTION = "csv";

	private static final String FIXED_WIDTH_OPTION = "fixed-width";

	private static final String FIELD_SEPARATOR_OPTION = "field-separator";

	private static final String QUOTE_CHARACTER_OPTION = "quote-character";

	private static final String PLATFORM_OPTION = "platform";

	private static final String UNIX_OPTION = "unix";

	private static final String WINDOWS_OPTION = "windows";

	/** A system property name for setting the program name in the
	 * usage message.
	 */
	private static final String PROGRAM_NAME = "pds4.tools.progname";

	//private Options options;

	private boolean listTables;
	private File labelFile;
	private File outputFile;
	private PrintWriter out;
	private OutputFormat format;
	private String fieldSeparator="\t";
	private String lineSeparator="\n";
	private String quoteCharacter;
	private Pattern quoteCharacterPattern;
	private int tableIndex;
	private String[] requestedFields;
	
	public static Product ps4toProduct(String path){
		Product p = (new ExtractTable()).getProduct(path);
		return p;
	}

	/**
	 * Runs the application with given command-line arguments.
	 *
	 * @param args the command-line arguments
	 */
	public static void main(String[] args) {
    	herschel.share.util.Configuration.setProperty("var.hcss.dir", "/Users/jarenas 1/Downloads/hcss-12.0.2524");
    	herschel.share.util.Configuration.setProperty("var.hcsstest.dir", "/Users/jarenas 1/Downloads/hcss-12.0.2524");
    	herschel.share.util.Configuration.setProperty("vega.mib.location", "/Users/jarenas 1/Downloads/MAPPS/MIB");
    	herschel.share.util.Configuration.setProperty("vega.instrument.names","{ALICE,CONSERT,COSIMA,GIADA,MIDAS,MIRO,ROSINA,RPC,RSI,OSIRIS,VIRTIS,SREM,LANDER,PTR,ANTENNA}");
    	herschel.share.util.Configuration.setProperty("vega.instrument.acronyms.ALICE","AL");
    	herschel.share.util.Configuration.setProperty("vega.instrument.acronyms.OSIRIS","SR");
		
		//Product p = (new ExtractTable()).getProduct("/Users/jarenas 1/Rosetta/NAC_WAC/nmd_raw_hk_hk2_20150625T112000-20150625T113600.xml");
		//System.out.println(ExtractTable.ps4toProduct("/Users/jarenas 1/Rosetta/NAC_WAC/nmd_raw_sc_uvis_20150625T160158-20150625T161758-M01-000005.xml"));
		//System.out.println(ExtractTable.ps4toProduct("/Users/jarenas 1/Rosetta/NAC_WAC/frd_raw_sc_d_20150625T133700-20150625T135700.xml"));
    	//System.out.println(ExtractTable.ps4toProduct("/Users/jarenas 1/Downloads/dph_example_products/product_table_character/Product_Table_Character.xml"));
    	System.out.println(ExtractTable.ps4toProduct("/Users/jarenas 1/Downloads/dph_example_products/product_table_binary_packed/Product_Table_Binary_packed.xml"));

	}
	
	public Product getProduct (String file){
		Product result=new Product();
		labelFile=new File(file);


		if (!labelFile.isFile() || !labelFile.canRead()) {
			IllegalArgumentException iae=new IllegalArgumentException("Cannot read label file " + labelFile.getPath());
			throw(iae);
		}

		ObjectProvider objectAccess = new ObjectAccess();
		ProductObservational product = null;
		try {
			product = objectAccess.getProduct(labelFile, ProductObservational.class);
		} catch (gov.nasa.pds.objectAccess.ParseException e) {
			IllegalArgumentException iae=new IllegalArgumentException("Cannot read label file " + labelFile.getPath());
			throw(iae);

		}
		result.getMeta().set("Comment", new StringParameter(product.getObservationArea().getComment()));
		
		try {
			result.setStartDate(new FineTime(DateUtil.parse(product.getObservationArea().getTimeCoordinates().getStartDateTime().getValue())));
		} catch (ParseException e) {

			IllegalArgumentException iae = new IllegalArgumentException(e.getMessage());
			iae.initCause(e);
			throw iae;
		}
		try {
			result.setEndDate(new FineTime(DateUtil.parse(product.getObservationArea().getTimeCoordinates().getStopDateTime().getValue())));
		} catch (ParseException e) {

			IllegalArgumentException iae = new IllegalArgumentException(e.getMessage());
			iae.initCause(e);
			throw iae;
		}
		List<InvestigationArea> l=product.getObservationArea().getInvestigationAreas();
		Iterator<InvestigationArea> it = l.iterator();
		while (it.hasNext()){
			InvestigationArea n = it.next();
			try{
				result.getMeta().set(n.getType(), new StringParameter(n.getName()));
			}catch (Exception e){
				VegaLog.warning("Could not set "+n.getType());
			}	
				
		}
		try{
			List<ObservingSystem> os = product.getObservationArea().getObservingSystems();
			Iterator<ObservingSystem> it2 = os.iterator();
			while (it2.hasNext()){
				ObservingSystem n = it2.next();
				if (n.getName()!=null){
					
					result.getMeta().set(n.getName(), new StringParameter(n.getDescription()));
					
				}else{
					result.getMeta().set("Observation System", new StringParameter(n.getDescription()));
				}
				//System.out.println(n.getDescription());
				//System.out.println("Observing System "+n.getName());
				//result.getMeta().set(n.getType(), new StringParameter(n.getName()));
			}
		}catch (Exception e){
			VegaLog.warning("Could not set Observation System");
		}
		try{
			result.getMeta().set("ProcessingLevel", new StringParameter(product.getObservationArea().getPrimaryResultSummary().getProcessingLevel()));
		}catch (Exception e){
			VegaLog.warning("Could not set ProcessingLevel");
		}
		try{
			result.getMeta().set("Purpose", new StringParameter(product.getObservationArea().getPrimaryResultSummary().getPurpose()));
		}catch (Exception e){
			VegaLog.warning("Could not set Purpose");
		}
		//System.out.println(product.getObservationArea().getPrimaryResultSummary().getDescription());
		//System.out.println(product.getObservationArea().getPrimaryResultSummary().getProcessingLevel());
		//System.out.println(product.getObservationArea().getPrimaryResultSummary().getProcessingLevelId());
		//System.out.println(product.getObservationArea().getPrimaryResultSummary().getPurpose());
		//System.out.println("-----");
		List<TargetIdentification> l2 = product.getObservationArea().getTargetIdentifications();
		Iterator<TargetIdentification> it3 = l2.iterator();
		String targets="";
		int co=0;
		while (it3.hasNext()){
			TargetIdentification n = it3.next();
			if (co==0){
				targets=n.getName();
			}else{
				targets=targets+","+n.getName();
			}

			
		}
		result.getMeta().set("Targets", new StringParameter(targets));
		//System.out.println("-----");
		result.setCreator("Hipe PDS");
		try{
			result.setModelName(product.getIdentificationArea().getInformationModelVersion());
		}catch (Exception e){
			VegaLog.warning("Could not set Model");
		}
		try{
			result.getMeta().set("LogicalIdentifier", new StringParameter(product.getIdentificationArea().getLogicalIdentifier()));
		}catch (Exception e){
			VegaLog.warning("Could not set LogicalIdentifier");
		}
		try{
			result.getMeta().set("Title", new StringParameter(product.getIdentificationArea().getTitle()));
		}catch (Exception e){
			VegaLog.warning("Could not set Title");
		}
		try{
			result.getMeta().set("VersionId", new StringParameter(product.getIdentificationArea().getVersionId()));
		}catch (Exception e){
			VegaLog.warning("Could not set VersionID");
		}
			//System.out.println(product.getIdentificationArea().getInformationModelVersion());
		//System.out.println(product.getIdentificationArea().getLogicalIdentifier());
		//System.out.println(product.getIdentificationArea().getTitle());
		//System.out.println(product.getIdentificationArea().getVersionId());
		//System.out.println(product.getIdentificationArea().getCitationInformation());
		
		
		//System.out.println(product.);
		
		//System.out.println(da.toString());

		
		
		FileAreaObservational fileArea = product.getFileAreaObservationals().get(0);
		String fileName = fileArea.getFile().getFileName();
		File dataFile = new File(labelFile.getParent(), fileName);
		String name="";
		String description="";		
		//System.out.println(dataFile.getName());

		int currentIndex = 1;
		for (Object obj : objectAccess.getTableObjects(fileArea)) {
			System.out.println(obj);
			TableType tableType = TableType.FIXED_BINARY;
			if (obj instanceof TableBinary) {
				tableType = TableType.FIXED_BINARY;
				
				TableBinary tb=(TableBinary) obj;
				name=tb.getName();
				description=tb.getDescription();
				VegaLog.info("Loading table "+name+" with "+tb.getRecords()+"records ("+description+")");
				//System.out.println("Number of records:"+tb.getRecords());
			}
			//TableType.
			
			if (obj instanceof TableCharacter) {
				tableType = TableType.FIXED_TEXT;
				name=((TableCharacter) obj).getName();
				description=((TableCharacter) obj).getDescription();
			} else if (obj instanceof TableDelimited) {
				name=((TableDelimited) obj).getName();
				description=((TableDelimited) obj).getDescription();
				tableType = TableType.DELIMITED;
			}

			TableReader reader = null;
			try {
				//gov.nasa.pds.objectAccess.table.
				//ExporterFactory.getTableExporter(fileArea, provider)
				reader = ExporterFactory.getTableReader(obj, dataFile);
			} catch (Exception ex) {
				IllegalArgumentException iae=new IllegalArgumentException("Cannot create a table reader for the table: " + ex.getMessage());
				iae.initCause(ex);
				throw(iae);

			}
			if (reader==null) System.out.println("reader is null ");
			//if (listTables) {
				//out.println("table " + currentIndex + ": " + tableType.getReadableType());
				//listFields(reader.getFields());
				
				TableDataset table = extractTableDataset(reader);
				try{
					table.getMeta().set("Description", new StringParameter(description));
				}catch (Exception e){
					VegaLog.warning("Could not set the tabnle description");
				}
				if (name!="" && name!=null){
					//System.out.println("name:"+name+"*");
					result.set(name, table);
				}else{
					result.set("table "+currentIndex, table);
				}

				//return table;

			/*} else if (currentIndex == tableIndex) {
				TableDataset table = extractTableDataset(reader);
				table.getMeta().set("Description", new StringParameter(description));
				result.set(name, table);
				System.out.println("Set Tabkle 2");
				//extractTable(reader);
				break;
			}*/

			++currentIndex;
		}

		//out.close();
		
		return result;
		
	}



	/**
	 * Runs the extractor.
	 *
	 * @param args the command-line arguments
	 */
	private void run() {
		//parseArguments(args);
		labelFile=new File("/Users/jarenas 1/Rosetta/NAC_WAC/nmd_raw_sc_uvis_20150625T160158-20150625T161758-M01-000005.xml");
		if (outputFile != null) {
			try {
				out = new PrintWriter(new FileWriter(outputFile));
			} catch (IOException e) {
				System.err.println("Cannot open output file: " + e.getMessage());
				System.exit(1);
			}
		} else {
			out = new PrintWriter(new OutputStreamWriter(System.out));
		}

		if (!labelFile.isFile() || !labelFile.canRead()) {
			System.err.println("Cannot read label file " + labelFile.getPath());
			System.exit(1);
		}

		ObjectProvider objectAccess = new ObjectAccess();
		ProductObservational product = null;
		try {
			product = objectAccess.getProduct(labelFile, ProductObservational.class);
		} catch (gov.nasa.pds.objectAccess.ParseException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}

		FileAreaObservational fileArea = product.getFileAreaObservationals().get(0);
		String fileName = fileArea.getFile().getFileName();
		File dataFile = new File(labelFile.getParent(), fileName);
		System.out.println(dataFile.getName());

		int currentIndex = 1;
		for (Object obj : objectAccess.getTableObjects(fileArea)) {
			TableType tableType = TableType.FIXED_BINARY;
			if (obj instanceof TableCharacter) {
				tableType = TableType.FIXED_TEXT;
			} else if (obj instanceof TableDelimited) {
				tableType = TableType.DELIMITED;
			}

			TableReader reader = null;
			try {
				reader = ExporterFactory.getTableReader(obj, dataFile);
			} catch (Exception ex) {
				System.err.println("Cannot create a table reader for the table: " + ex.getMessage());
				ex.printStackTrace();
				out.close();
				return;
			}

			//if (listTables) {
				out.println("table " + currentIndex + ": " + tableType.getReadableType());
				listFields(reader.getFields());
				
				TableDataset table = extractTableDataset(reader);
				extractTable(reader);
				System.out.println(table);
			/*} else if (currentIndex == tableIndex) {
				extractTable(reader);
				break;
			}*/

			++currentIndex;
		}

		out.close();
	}

	/**
	 * Lists the fields in a table.
	 *
	 * @param fields an array of field descriptions
	 */
	private void listFields(FieldDescription[] fields) {
		int i = 0;

		for (FieldDescription field : fields) {
			++i;
			out.println("    field " + i + ": " + field.getName() + " (" + field.getType().getXMLType() + ")");
		}
	}
	/**
	 * Extracts a table to the output file.
	 *
	 * @param reader the table reader to use for reading data
	 */
	public TableDataset extractTableDataset(TableReader reader) {
		FieldDescription[] fields = reader.getFields();
		int[] displayFields = getSelectedFields(fields);

		int[] fieldLengths = getFieldLengths(fields, displayFields);
		return this.getTableDataset(reader, fields, displayFields, fieldLengths);
	}
	/**
	 * Extracts a table to the output file.
	 *
	 * @param reader the table reader to use for reading data
	 */
	private void extractTable(TableReader reader) {
		FieldDescription[] fields = reader.getFields();
		int[] displayFields = getSelectedFields(fields);

		int[] fieldLengths = getFieldLengths(fields, displayFields);

		displayHeaders(fields, displayFields, fieldLengths);
		displayRows(reader, fields, displayFields, fieldLengths);
	}

	/**
	 * Gets an array of field indices to display. Uses the
	 * field indices specified on the command line, if any,
	 * otherwise all fields will be displayed.
	 *
	 * @param totalFields the total number of fields in the table
	 * @return an array of fields to display
	 */
	private int[] getSelectedFields(FieldDescription[] fields) {
		int[] displayFields;

		if (requestedFields == null) {
			displayFields = new int[fields.length];
			for (int i=0; i < fields.length; ++i) {
				displayFields[i] = i;
			}
		} else {
			displayFields = new int[requestedFields.length];
			for (int i=0; i < requestedFields.length; ++i) {
				displayFields[i] = findField(requestedFields[i], fields);
			}
		}

		return displayFields;
	}

	/**
	 * Try to convert a field name or index into a field index.
	 * Prints an error message and exits if the field is not
	 * present in the table.
	 *
	 * @param nameOrIndex the string form of the name or index requested
	 * @param fields the field descriptions for the table fields
	 * @return the index of the requested field
	 */
	private int findField(String nameOrIndex, FieldDescription[] fields) {
		// First try to convert as an integer.
		try {
			return Integer.parseInt(nameOrIndex) - 1;
		} catch (NumberFormatException ex) {
			// ignore
		}

		// Now try to find a matching field name, ignoring case.
		for (int i=0; i < fields.length; ++i) {
			if (nameOrIndex.equalsIgnoreCase(fields[i].getName())) {
				return i;
			}
		}

		// If we get here, then we couldn't find a matching field.
		System.err.println("Requested field not present in table: " + nameOrIndex);
		System.exit(1);
		return -1; // Still have to return, because Java doesn't know that exit() doesn't return.
	}

	/**
	 * Gets an array of field lengths to use for output.
	 *
	 * @param fields an array of field descriptions
	 * @param displayFields an array of field indices to display
	 * @return
	 */
	private int[] getFieldLengths(FieldDescription[] fields, int[] displayFields) {
		int[] fieldLengths = new int[displayFields.length];

		for (int i=0; i < displayFields.length; ++i) {
			int fieldIndex = displayFields[i];

			if (format == OutputFormat.CSV) {
				fieldLengths[i] = 0;
			} else {
				fieldLengths[i] = Math.max(fields[fieldIndex].getName().length(), fields[fieldIndex].getLength());
			}
		}

		return fieldLengths;
	}

	/**
	 * Displays the headers of the table.
	 *
	 * @param fields an array of field descriptions
	 * @param displayFields an array of field indices to display
	 * @param fieldLengths an array of field lengths to use for output
	 */
	private void displayHeaders(FieldDescription[] fields, int[] displayFields, int[] fieldLengths) {
		for (int i=0; i < displayFields.length; ++i) {
			if (i > 0) {
				out.append(fieldSeparator);
			}

			FieldDescription field = fields[displayFields[i]];
			displayJustified(field.getName(), fieldLengths[i], field.getType().isRightJustified());
		}
		out.append(lineSeparator);
	}
	
	public TableDataset getTableDataset(TableReader reader, FieldDescription[] fields, int[] displayFields, int[] fieldLengths) {
		TableDataset tableResult=new TableDataset();
		TableRecord record;
		Column[] columns=new Column[displayFields.length];
		Array1dData[] data=new Array1dData[displayFields.length];
		String1d rawData=new String1d();
		try {
			while ((record = reader.readNext()) != null) {
				for (int i=0; i < displayFields.length; ++i) {
					/*if (i > 0) {
						out.append(fieldSeparator);
					}*/

					int index = displayFields[i];
					rawData.append(record.getString(index+1).trim());
					//FieldDescription field = fields[index];
					
					//displayJustified(record.getString(index+1).trim(), fieldLengths[i], field.getType().isRightJustified());
				}

				//out.append(lineSeparator);
			}
		} catch (IOException e) {
			IllegalArgumentException iae = new IllegalArgumentException("Cannot read the next table record: " + e.getMessage());
			//System.err.println("Cannot read the next table record: " + e.getMessage());
			//System.exit(1);
			throw iae;
		}
		int nRecords = rawData.length();
		int nColumns=displayFields.length;
		
		for (int i=0; i < displayFields.length; ++i) {
			FieldDescription field = fields[i];
			boolean processed=false;
			//System.out.println(field.getType());
			//nRecords
			if (field.getType().equals(FieldType.ASCII_INTEGER) || field.getType().equals(FieldType.ASCII_NONNEGATIVE_INTEGER) ){
				Int1d intData = new Int1d();
				for (int d=i;d<nRecords;d=d+nColumns){
					try{
						intData.append(Integer.parseInt(rawData.get(d)));
					}catch (java.lang.NumberFormatException ex){
						VegaLog.warning("Can not parse int "+rawData.get(d)+" setting 0 "+field.getType());
						intData.append(0);
					}
				}
				columns[i]= new Column(intData);
				columns[i].setDescription(field.getName());
				//field.get
				tableResult.addColumn(field.getName(),columns[i]);
				processed=true;
				
			}
			if (field.getType().equals(FieldType.ASCII_REAL) || field.getType().equals(FieldType.IEEE754LSBDOUBLE) || field.getType().equals(FieldType.IEEE754MSBDOUBLE) ){
				Double1d doubleData = new Double1d();
				for (int d=i;d<nRecords;d=d+nColumns){
					try{
						doubleData.append(Double.parseDouble(rawData.get(d)));
					}catch (java.lang.NumberFormatException ex){
						VegaLog.warning("Can not parse double "+rawData.get(d)+" setting 0"+field.getType()+ex.getMessage());
						doubleData.append(0);
					}
				}
				columns[i]= new Column(doubleData);
				columns[i].setDescription(field.getName());
				tableResult.addColumn(field.getName(),columns[i]);
				processed=true;

			}
			if (field.getType().equals(FieldType.ASCII_NUMERIC_BASE16)){
				Long1d doubleData = new Long1d();
				for (int d=i;d<nRecords;d=d+nColumns){
					try{
						
						doubleData.append(Long.parseLong(rawData.get(d),16));
					}catch (java.lang.NumberFormatException ex){
						VegaLog.warning("Can not parse double "+rawData.get(d)+" setting 0"+field.getType()+ex.getMessage());
						doubleData.append(0);
						ex.printStackTrace();
						BigInteger bi=new BigInteger(rawData.get(d),16);
						//System.out.println(bi);
						try {
							System.out.println("try");
							System.out.println(reader.getRecord(d).getBigInteger(i+1));
						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							//e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							//e.printStackTrace();
						}
					}
				}
				columns[i]= new Column(doubleData);
				columns[i].setDescription(field.getName());
				tableResult.addColumn(field.getName(),columns[i]);
				processed=true;

			}
			if (field.getType().equals(FieldType.ASCII_NUMERIC_BASE8)){
				Long1d doubleData = new Long1d();
				for (int d=i;d<nRecords;d=d+nColumns){
					try{
						
						doubleData.append(Long.parseLong(rawData.get(d),8));
					}catch (java.lang.NumberFormatException ex){
						VegaLog.warning("Can not parse double "+rawData.get(d)+" setting 0"+field.getType()+ex.getMessage());
						doubleData.append(0);
					}
				}
				columns[i]= new Column(doubleData);
				columns[i].setDescription(field.getName());
				tableResult.addColumn(field.getName(),columns[i]);
				processed=true;

			}
			if (field.getType().equals(FieldType.ASCII_NUMERIC_BASE2)){
				Long1d doubleData = new Long1d();
				for (int d=i;d<nRecords;d=d+nColumns){
					try{
						
						doubleData.append(Long.parseLong(rawData.get(d),2));
					}catch (java.lang.NumberFormatException ex){
						VegaLog.warning("Can not parse double "+rawData.get(d)+" setting 0"+field.getType()+ex.getMessage());
						doubleData.append(0);
					}
				}
				columns[i]= new Column(doubleData);
				columns[i].setDescription(field.getName());
				tableResult.addColumn(field.getName(),columns[i]);
				processed=true;

			}

			if (field.getType().equals(FieldType.ASCII_STRING) ){
				String1d stringData = new String1d();
				for (int d=i;d<nRecords;d=d+nColumns){
					stringData.append(rawData.get(d));
				}
				columns[i]= new Column(stringData);
				columns[i].setDescription(field.getName());
				tableResult.addColumn(field.getName(),columns[i]);
				processed=true;

			}

			
			
		}

		
		return tableResult;
	}

	/**
	 * Displays the rows from the table.
	 *
	 * @param reader the table reader for reading rows
	 * @param fields an array of field descriptions
	 * @param displayFields an array of field indices to display
	 * @param fieldLengths an array of field lengths to use for output
	 * @throws IOException
	 */
	private void displayRows(TableReader reader, FieldDescription[] fields, int[] displayFields, int[] fieldLengths) {
		TableRecord record;
		try {
			while ((record = reader.readNext()) != null) {
				for (int i=0; i < displayFields.length; ++i) {
					if (i > 0) {
						out.append(fieldSeparator);
					}

					int index = displayFields[i];
					FieldDescription field = fields[index];
					displayJustified(record.getString(index+1).trim(), fieldLengths[i], field.getType().isRightJustified());
				}

				out.append(lineSeparator);
			}
		} catch (IOException e) {
			System.err.println("Cannot read the next table record: " + e.getMessage());
			System.exit(1);
		}
	}

	/**
	 * Displays a string, justified in a field.
	 *
	 * @param s the string to display
	 * @param length the field length
	 * @param isRightJustified true, if the value should be right-justified, else left-justified
	 */
	private void displayJustified(String s, int length, boolean isRightJustified) {
		if (format == OutputFormat.CSV) {
			// Double any quote characters.
			if (s.contains(quoteCharacter)) {
				Matcher matcher = quoteCharacterPattern.matcher(s);
				s = matcher.replaceAll(quoteCharacter + quoteCharacter);
			}

			// If the value is all whitespace or contains the field separator, quote the value.
			if (s.trim().isEmpty() || s.contains(fieldSeparator)) {
				s = quoteCharacter + s + quoteCharacter;
			}
		}

		int padding = length - s.length();

		if (isRightJustified) {
			displayPadding(padding);
		}
		out.append(s);
		if (!isRightJustified) {
			displayPadding(padding);
		}
	}

	/**
	 * Displays a number of padding spaces.
	 *
	 * @param n the number of spaces
	 */
	private void displayPadding(int n) {
		for (int i=0; i < n; ++i) {
			out.append(' ');
		}
	}

	/**
	 * Parses the command-line arguments.
	 *
	 * @param args the command-line arguments
	 */
	private void parseArguments(String[] args) {
		/*CommandLineParser parser = new GnuParser();
		CommandLine cmdLine = null;
		try {
			cmdLine = parser.parse(options, args);
		} catch (ParseException e) {
			showHelp("Error parsing command-line options: " + e.getMessage(), 1);
		}

		if (cmdLine.hasOption(HELP_OPTION)) {
			showHelp(null, 0);
		}

		listTables = (cmdLine.hasOption(LIST_TABLES_OPTION));

		if (cmdLine.hasOption(INDEX_OPTION)) {
			tableIndex = Integer.parseInt(cmdLine.getOptionValue(INDEX_OPTION));
		} else {
			tableIndex = 1;
		}

		String[] files = cmdLine.getArgs();
		if (files.length == 0) {
			showHelp("A label file is required", 1);
		}
		labelFile = new File(files[0]);

		if (cmdLine.hasOption(CSV_OPTION)) {
			format = OutputFormat.CSV;
		} else {
			format = OutputFormat.FIXED_WIDTH;
		}

		if (cmdLine.hasOption(FIELD_SEPARATOR_OPTION)) {
			fieldSeparator = cmdLine.getOptionValue(FIELD_SEPARATOR_OPTION);
		} else if (format == OutputFormat.FIXED_WIDTH) {
			fieldSeparator = " ";
		} else {
			fieldSeparator = ",";
		}

		if (cmdLine.hasOption(QUOTE_CHARACTER_OPTION)) {
			quoteCharacter = cmdLine.getOptionValue(QUOTE_CHARACTER_OPTION);
		} else {
			quoteCharacter = "\"";
		}
		quoteCharacterPattern = Pattern.compile("\\Q" + quoteCharacter + "\\E");

		if (cmdLine.hasOption(WINDOWS_OPTION)) {
			lineSeparator = "\r\n";
		} else if (cmdLine.hasOption(UNIX_OPTION)) {
			lineSeparator = "\n";
		} else {
			lineSeparator = System.getProperty("line.separator");
		}

		if (!cmdLine.hasOption(FIELDS_OPTION)) {
			requestedFields = null;
		} else {
			requestedFields = cmdLine.getOptionValue(FIELDS_OPTION).split(" *, *");
		}

		if (cmdLine.hasOption(OUTPUT_FILE_OPTION)) {
			outputFile = new File(cmdLine.getOptionValue(OUTPUT_FILE_OPTION));
		} else {
			outputFile = null;
		}*/
	}

	/**
	 * Shows the help message and, optionally, an error message, and exits.
	 *
	 * @param errorMessage the error message, or null if there is no error message
	 * @param exitCode the exit code to use
	 */
	private void showHelp(String errorMessage, int exitCode) {
		if (errorMessage != null) {
			System.err.println(errorMessage);
			System.err.println();
		}

		String programName = getClass().getName();
		if (System.getProperty(PROGRAM_NAME) != null) {
			programName = System.getProperty(PROGRAM_NAME);
		}

		/*HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp(programName + " [-f field,...] [-o outputfile] [options] labelfile", options);*/
		System.exit(exitCode);
	}

	/**
	 * Defines an enumeration for the different table types
	 * that can be extracted. Holds a readable description of
	 * the table type.
	 */
	private static enum TableType {

		/** A fixed-width binary table. */
		FIXED_BINARY("fixed-width binary table"),

		/** A fixed-width text table. */
		FIXED_TEXT("fixed-width character table"),

		/** A delimited table. */
		DELIMITED("delimited table");

		private String readableType;

		private TableType(String readableType) {
			this.readableType = readableType;
		}

		/**
		 * Gets the readable name for the table type.
		 *
		 * @return the name of the table type
		 */
		public String getReadableType() {
			return readableType;
		}

	}

	/**
	 * Defines an enumeration for the different output formats.
	 */
	private static enum OutputFormat {
		CSV, FIXED_WIDTH;
	}

}

