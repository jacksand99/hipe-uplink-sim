package vega.hipe.pds.pds4;

public class TestPds4 {
	public static String[] names={"ASCII_AnyURI",	 
 	 	 	"ASCII_Boolean",	 
 	 	 	"ASCII_DOI",	 
 	 	 	"ASCII_Date_DOY",	 
 	 	 	"ASCII_Date_Time_DOY",	 
 	 	 	"ASCII_Date_Time_DOY_UTC",	 
 	 	 	"ASCII_Date_Time_YMD",	 
 	 	 	"ASCII_Date_Time_YMD_UTC",	 
 	 	 	"ASCII_Date_YMD",	 
 	 	 	"ASCII_Directory_Path_Name",	 
 	 	 	"ASCII_File_Name",	 
 	 	 	"ASCII_File_Specification_Name",	 
 	 	 	"ASCII_Integer",	 
 	 	 	"ASCII_LID",	 
 	 	 	"ASCII_LIDVID",	 
 	 	 	"ASCII_LIDVID_LID",	 
 	 	 	"ASCII_MD5_Checksum",	 
 	 	 	"ASCII_NonNegative_Integer",	 
 	 	 	"ASCII_Numeric_Base16",	 
 	 	 	"ASCII_Numeric_Base2",	 
 	 	 	"ASCII_Numeric_Base8",	 
 	 	 	"ASCII_Real",	 
 	 	 	"ASCII_String",	 
 	 	 	"ASCII_Time",	 
 	 	 	"ASCII_VID",	 
 	 	 	"UTF8_String"};
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/*ModificationDetail modification=new ModificationDetail();
		modification.setModicationDate("2012-01-10");
		modification.setVersionId("1.0");
		modification.setDescription("extracted metadata from PDS3 catalog and modified to comply with PDS4 Information Model");
		IdentificationArea ia=new IdentificationArea();
		ia.setLogicalIdentifier("urn:nasa:pds:example.dph.sampleproducts:exampleproducts:tablechar.phx-m-tt-5-wind-vel-dir-v1.0");
		ia.setVersionId("1.0");
		ia.setInformationModelVersion("1.4.0.0");
		ia.setProductClass("Product_Observational");
		ia.addModificationDetail(modification);
		ia.setTitle("PHOENIX Mars Wind Experiment");
		System.out.println(ia.toXml());
		
		ObservationArea observationArea=new ObservationArea();
		TimeCoordinates tc=new TimeCoordinates();
		tc.setStartDateTime("2008-08-26T20:36:36.856Z");
		tc.setStopDateTime("2008-10-27T15:32:50.952Z");
		tc.setLocalMeanSolarTime("not applicable");
		tc.setLocalTrueSolarTime("not applicable");
		observationArea.setTimeCoordinates(tc);
		
		PrimaryResultSummary prs = new PrimaryResultSummary();
		prs.setType("Image");
		prs.setPurpose("science");
		prs.setDataRegime("Visible");
		prs.setProcessingLevel("Raw");
		prs.setDescription("This is where a generic description of the product goes");
		observationArea.setPrimaryResultSummary(prs);

		
		InvestigationArea investigationArea = new InvestigationArea();
		investigationArea.setName("PHOENIX");
		investigationArea.setType("Mission");
		investigationArea.getInternalReference().setLidvidReference("urn:nasa:pds:context:investigation:mission.phoenix::1.0");
		investigationArea.getInternalReference().setReferenceType("data_to_investigation");
		observationArea.setInvestigationArea(investigationArea);

		
		ObservingSystem observingSystem = new ObservingSystem();
		observingSystem.setName("Observing System for PHX+TT");
		ObservingSystemComponent oC1 = new ObservingSystemComponent();
		oC1.setName("TT");
		oC1.setType("Spacecraft");
		oC1.setDescription("instrument_host PHX");
		oC1.getInternalReference().setLidvidReference("urn:nasa:pds:context:instrument_host:instrument_host.phx");
		oC1.getInternalReference().setReferenceType("is_instrument_host");
		ObservingSystemComponent oC2 = new ObservingSystemComponent();
		oC2.setName("PHX");
		oC2.setType("Instrument");
		oC2.setDescription("instrument TT");
		oC2.getInternalReference().setLidvidReference("urn:nasa:pds:context:instrument:instrument.tt__phx");
		oC2.getInternalReference().setReferenceType("is_instrument");
		observingSystem.addObservingSystemComponent(oC1);
		observingSystem.addObservingSystemComponent(oC2);
		observationArea.setObservingSystem(observingSystem);

		
		TargetIdentification target = new TargetIdentification();
		target.setName("MARS");
		target.setType("Planet");
		target.setDescription("description of MARS goes here");
		target.getInternalReference().setLidvidReference("urn:nasa:pds:context:target:target.mars::1.0");
		target.getInternalReference().setReferenceType("data_to_target");
		observationArea.setTargetIdentification(target);

		System.out.println(observationArea.toXml());
		
		ReferenceList referenceList=new ReferenceList();
		referenceList.addReference(new InternalReference("urn:nasa:pds:context:investigation:mission.phoenix::1.0","data_to_document"));
		referenceList.addReference(new InternalReference("urn:nasa:pds:context:instrument_host:instrument_host.phx","data_to_document"));
		referenceList.addReference(new InternalReference("urn:nasa:pds:context:instrument:instrument.tt__phx","data_to_document"));
		referenceList.addReference(new InternalReference("urn:nasa:pds:context:target:target.mars","data_to_document"));
		referenceList.addReference(new InternalReference("urn:nasa:pds:context:node:node.atmos","data_to_document"));
		
		System.out.println(referenceList.toXml());
		
		File file=new File();
		file.setFileName("PDS4_ATM_TABLE_CHAR.TAB");
		file.setLocalIdentifier("ATM_PRODUCT_TABLE_CHAR_ID");
		file.setCreationDateTime("2009-04-30T12:12:12Z");
		file.setFileSize("2024");
		file.setFileSizeUnit("byte");
		file.setRecords("23");
		file.setMd5Checksum("5c1f6d2e359314f5d6a674739c5a9f43");
		
		System.out.println(file.toXml());
		
		
		File f=new File();
		f.setFileName("PDS4_ATM_TABLE_CHAR.TAB");
		f.setLocalIdentifier("ATM_PRODUCT_TABLE_CHAR_ID");
		f.setCreationDateTime("2009-04-30T12:12:12Z");
		f.setFileSize("2024");
		f.setFileSizeUnit("byte");
		f.setRecords("23");
		f.setMd5Checksum("5c1f6d2e359314f5d6a674739c5a9f43");
		
		FieldCharacter fc1 = new FieldCharacter();
		fc1.setName("SOL");
		fc1.setFieldNumber("1");
		fc1.setFieldLocation("1");
		fc1.setFieldLocationUnit("byte");
		fc1.setDataType("ASCII_Integer");
		fc1.setFieldLength("3");
		fc1.setFieldLengthUnit("byte");
		fc1.setFieldFormat("I3");
		fc1.setDescription("PHOENIX Sol number");
		
		RecordCharacter rc=new RecordCharacter();
		rc.setFields("10");
		rc.setGroups("0");
		rc.setRecordLenght("88");
		rc.setRecordLenghtUnit("byte");
		rc.addFieldCharacter(fc1);
		
		TableCharacter tableCharacter=new TableCharacter();
		tableCharacter.setLocalIdentifier("PHX-M-TT-5-WIND-VEL-DIR_TABLE_CHAR");
		tableCharacter.setOffset("0");
		tableCharacter.setOffsetUnit("byte");
		tableCharacter.setRecords("23");
		tableCharacter.setRecordDelimiter("Carriage-Return Line-Feed");
		tableCharacter.setRecordCharacter(rc);
		
		//System.out.println(tableCharacter.toXml());
		FileAreaObservational fao=new FileAreaObservational();
		fao.setFile(f);
		fao.setTable(tableCharacter);
		//System.out.println(fao.toXml());
		ProductObservational pObservational=new ProductObservational();
		
		pObservational.setIdentificationArea(ia);
		pObservational.setObservationArea(observationArea);
		pObservational.setReferenceList(referenceList);
		pObservational.setFileAreaObservational(fao);
		System.out.println(pObservational.toXml());*/
		ProductObservational[] products = Pds4Reader.readProductObservationalfromFile("/Users/jarenas 1/Downloads/dph_example_products/product_table_character/Product_Table_Character.xml");
		for (int i=0;i<products.length;i++){
			System.out.println(products[i].toXml());
		}
		for (int i=0;i<names.length;i++){
			System.out.println("public static String DATA_TYPE_"+names[i].toUpperCase()+"=\""+names[i]+"\";");
		}
		
		//System.out.println(herschel.share.unit.Unit.parse("bytes"));
		//System.out.println(herschel.share.unit.Unit.parse("kbytes"));
		//System.out.println(vega.uplink.pointing.Units.convertUnit(1, "bytes", "kbytes"));
	}

}
