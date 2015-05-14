package vega.hipe.pds;


import java.awt.Point;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;

import java.util.HashMap;
import java.util.Iterator;

import vega.uplink.DateUtil;
import herschel.ia.dataset.DoubleParameter;
import herschel.ia.dataset.Parameter;
import herschel.ia.dataset.StringParameter;
import herschel.ia.dataset.image.Image;
import herschel.ia.dataset.image.SimpleImage;
import herschel.ia.numeric.AbstractOrdered2dData;
import herschel.ia.numeric.Int2d;
import herschel.ia.numeric.Selection;
import herschel.ia.numeric.toolbox.basic.IsFinite;
import herschel.ia.numeric.toolbox.basic.Max;
import herschel.ia.numeric.toolbox.basic.Min;
import herschel.share.fltdyn.time.FineTime;
import herschel.share.interpreter.InterpreterUtil;
import herschel.share.unit.Unit;

/**
 * Class that stores an image read in Planetary Data Science format. As it inherit from SimpleImage it can be handle by Hipe and save as FITS 
 * @author jarenas
 *
 */
public class PDSImage extends SimpleImage {
	public static String OFFSET_ANGLE="OFFSET_ANGLE";
	public static String PERIAPSIS_TIME="PERIAPSIS_TIME";
	public static String LINE_SAMPLES="LINE_SAMPLES";
	public static String WESTERNMOST_LONGITUDE="WESTERNMOST_LONGITUDE";
	public static String DATA_SET_MAP_PROJECTION_CATALOG="^DATA_SET_MAP_PROJECTION_CATALOG";
	public static String DATA_QUALITY_DESC="DATA_QUALITY_DESC";
	public static String MEAN="MEAN";
	public static String RELEASE_ID="RELEASE_ID";
	public static String LINE_FIRST_PIXEL="LINE_FIRST_PIXEL";
	public static String FILE_RECORDS="FILE_RECORDS";
	public static String SAMPLE_PROJECTION_OFFSET="SAMPLE_PROJECTION_OFFSET";
	public static String INST_CMPRS_RATIO="INST_CMPRS_RATIO";
	public static String REVISION_ID="REVISION_ID";
	public static String ORBITAL_INCLINATION="ORBITAL_INCLINATION";
	public static String SAMPLE_LAST_PIXEL="SAMPLE_LAST_PIXEL";
	public static String REFLECTANCE_SCALING_FACTOR="REFLECTANCE_SCALING_FACTOR";
	public static String EASTERNMOST_LONGITUDE="EASTERNMOST_LONGITUDE";
	public static String MINIMUM_LATITUDE="MINIMUM_LATITUDE";
	public static String MINIMUM="MINIMUM";
	public static String BANDS="BANDS";
	public static String REFERENCE_LONGITUDE="REFERENCE_LONGITUDE";
	public static String SPACECRAFT_POINTING_MODE="SPACECRAFT_POINTING_MODE";
	public static String COORDINATE_SYSTEM_NAME="COORDINATE_SYSTEM_NAME";
	public static String DETECTOR_ID="DETECTOR_ID";
	public static String MISSING_FRAMES="MISSING_FRAMES";
	public static String COORDINATE_SYSTEM_TYPE="COORDINATE_SYSTEM_TYPE";
	public static String INTERCHANGE_FORMAT="INTERCHANGE_FORMAT";
	public static String PROCESSING_LEVEL_ID="PROCESSING_LEVEL_ID";
	public static String SPACECRAFT_CLOCK_STOP_COUNT="SPACECRAFT_CLOCK_STOP_COUNT";
	public static String BANDWIDTH="BANDWIDTH";
	public static String A_AXIS_RADIUS="A_AXIS_RADIUS";
	public static String SPACECRAFT_CLOCK_START_COUNT="SPACECRAFT_CLOCK_START_COUNT";
	public static String PRODUCT_ID="PRODUCT_ID";
	public static String MAXIMUM_LATITUDE="MAXIMUM_LATITUDE";
	public static String PERIAPSIS_ALTITUDE="PERIAPSIS_ALTITUDE";
	public static String PIXEL_SUBSAMPLING_FLAG="PIXEL_SUBSAMPLING_FLAG";
	public static String DESCRIPTION="^DESCRIPTION";
	public static String SPACECRAFT_ORIENTATION="SPACECRAFT_ORIENTATION";
	public static String B_AXIS_RADIUS="B_AXIS_RADIUS";
	public static String MISSION_NAME="MISSION_NAME";
	public static String SAMPLE_FIRST_PIXEL="SAMPLE_FIRST_PIXEL";
	public static String DETECTOR_TEMPERATURE="DETECTOR_TEMPERATURE";
	public static String PDS_VERSION_ID="PDS_VERSION_ID";
	public static String EVENT_TYPE="EVENT_TYPE";
	public static String DATA_SET_ID="DATA_SET_ID";
	public static String FIRST_STANDARD_PARALLEL="FIRST_STANDARD_PARALLEL";
	public static String SPACECRAFT_SOLAR_DISTANCE="SPACECRAFT_SOLAR_DISTANCE";
	public static String MAP_RESOLUTION="MAP_RESOLUTION";
	public static String ORBIT_NUMBER="ORBIT_NUMBER";
	public static String BYTES="BYTES";
	public static String MACROPIXEL_SIZE="MACROPIXEL_SIZE";
	public static String INST_CMPRS_NAME="INST_CMPRS_NAME";
	public static String RECORD_TYPE="RECORD_TYPE";
	public static String ORBITAL_ECCENTRICITY="ORBITAL_ECCENTRICITY";
	public static String END_OBJECT="END_OBJECT";
	public static String DECLINATION="DECLINATION";
	public static String LINES="LINES";
	public static String MISSION_PHASE_NAME="MISSION_PHASE_NAME";
	public static String LENS_TEMPERATURE="LENS_TEMPERATURE";
	public static String HEADER_TYPE="HEADER_TYPE";
	public static String SECOND_STANDARD_PARALLEL="SECOND_STANDARD_PARALLEL";
	public static String CENTER_LONGITUDE="CENTER_LONGITUDE";
	public static String MAP_PROJECTION_TYPE="MAP_PROJECTION_TYPE";
	public static String SAMPLE_BITS="SAMPLE_BITS";
	public static String IMAGE_HEADER="^IMAGE_HEADER";
	public static String OBJECT="OBJECT";
	public static String C_AXIS_RADIUS="C_AXIS_RADIUS";
	public static String IMAGE="^IMAGE";
	public static String LINE_PROJECTION_OFFSET="LINE_PROJECTION_OFFSET";
	public static String RIGHT_ASCENSION="RIGHT_ASCENSION";
	public static String REFERENCE_LATITUDE="REFERENCE_LATITUDE";
	public static String INST_CMPRS_QUANTZ_TBL_ID="INST_CMPRS_QUANTZ_TBL_ID";
	public static String STANDARD_DEVIATION="STANDARD_DEVIATION";
	public static String INSTRUMENT_NAME="INSTRUMENT_NAME";
	public static String INST_CMPRS_QUALITY="INST_CMPRS_QUALITY";
	public static String ORBITAL_SEMIMAJOR_AXIS="ORBITAL_SEMIMAJOR_AXIS";
	public static String RADIANCE_SCALING_FACTOR="RADIANCE_SCALING_FACTOR";
	public static String FOOTPRINT_POINT_LATITUDE="FOOTPRINT_POINT_LATITUDE";
	public static String TARGET_NAME="TARGET_NAME";
	public static String BAND_STORAGE_TYPE="BAND_STORAGE_TYPE";
	public static String ASCENDING_NODE_LONGITUDE="ASCENDING_NODE_LONGITUDE";
	public static String MAP_SCALE="MAP_SCALE";
	public static String POSITIVE_LONGITUDE_DIRECTION="POSITIVE_LONGITUDE_DIRECTION";
	public static String FOOTPRINT_POINT_LONGITUDE="FOOTPRINT_POINT_LONGITUDE";
	public static String LINE_LAST_PIXEL="LINE_LAST_PIXEL";
	public static String FOCAL_PLANE_TEMPERATURE="FOCAL_PLANE_TEMPERATURE";
	public static String LABEL_RECORDS="LABEL_RECORDS";
	public static String SAMPLE_TYPE="SAMPLE_TYPE";
	public static String MAXIMUM_RESOLUTION="MAXIMUM_RESOLUTION";
	public static String PERIAPSIS_ARGUMENT_ANGLE="PERIAPSIS_ARGUMENT_ANGLE";
	public static String RADIANCE_OFFSET="RADIANCE_OFFSET";
	public static String INSTRUMENT_HOST_ID="INSTRUMENT_HOST_ID";
	public static String FILE_NAME="FILE_NAME";
	public static String INSTRUMENT_HOST_NAME="INSTRUMENT_HOST_NAME";
	public static String DATA_QUALITY_ID="DATA_QUALITY_ID";
	public static String MAXIMUM="MAXIMUM";
	public static String CENTER_LATITUDE="CENTER_LATITUDE";
	public static String INSTRUMENT_TEMPERATURE="INSTRUMENT_TEMPERATURE";
	public static String MAP_PROJECTION_ROTATION="MAP_PROJECTION_ROTATION";
	public static String RECORD_BYTES="RECORD_BYTES";
	public static String SIGNAL_CHAIN_ID="SIGNAL_CHAIN_ID";
	public static String CENTER_FILTER_WAVELENGTH="CENTER_FILTER_WAVELENGTH";


	public String toString(){
			String result="";
			java.util.Vector<String> vector=new java.util.Vector<String>();
			Iterator<String> it = getMeta().keySet().iterator();
			while (it.hasNext()){
				vector.add(it.next());
			}
			result=result+"PDS_VERSION_ID\t="+getStringMetadata(PDS_VERSION_ID)+"\n";
			vector.remove(PDS_VERSION_ID);
			result=result+"RECORD_TYPE\t="+getStringMetadata(RECORD_TYPE)+"\n";
			vector.remove(RECORD_TYPE);
			result=result+"RECORD_BYTES\t="+getStringMetadata(RECORD_BYTES)+"\n";
			vector.remove(RECORD_BYTES);
			result=result+"FILE_RECORDS\t="+getStringMetadata(FILE_RECORDS)+"\n";
			vector.remove(FILE_RECORDS);
			result=result+"LABEL_RECORDS\t="+getStringMetadata(LABEL_RECORDS)+"\n";
			vector.remove(LABEL_RECORDS);
			result=result+"^IMAGE_HEADER\t="+getStringMetadata(IMAGE_HEADER)+"\n";
			vector.remove(IMAGE_HEADER);
			result=result+"^IMAGE\t="+getStringMetadata(IMAGE)+"\n";
			vector.remove(IMAGE);
			result=result+"PRODUCT_CREATION_TIME\t="+DateUtil.dateToZulu(this.getCreationDate().toDate())+"\n";
			result=result+"START_TIME\t="+DateUtil.dateToZulu(this.getStartDate().toDate())+"\n";
			result=result+"STOP_TIME\t="+DateUtil.dateToZulu(this.getEndDate().toDate())+"\n";
			result=result+"INSTRUMENT_ID\t="+this.getInstrument()+"\n";
			Iterator<String> it2 = vector.iterator();
			while (it2.hasNext()){
				String name=it2.next();
				String value=getStringMetadata(name);
				if (value!=null) result=result+name+"\t="+value+"\n";
			}
			
			
			
			return result;
					
		}
		
		/**
		 * Get the value of a metadata as String
		 * @param name the name of the matadata
		 * @return the string representation of the metadata or null if the metadata does not exist or it can not converted to String
		 */
		public String getStringMetadata(String name){
			String value="";
			try{
				Parameter parameter = getMeta().get(name);
				if (InterpreterUtil.isInstance(DoubleParameter.class, parameter)){
					Unit<?> unit = ((DoubleParameter) parameter).getUnit();
					String unitString="";
					if (unit!=null){
						unitString=" <"+unit.toString()+">";
					}
					value=""+(Double) parameter.getValue()+unitString;
				}else{
					value=(String) getMeta().get(name).getValue();
				}
				
				return value;
			}catch (Exception e){
				return null;
			}
		}
		/**
		 * Get the value of a metadata as Double. The metadata requested must be a double parameter or a string that can be parsed to a double
		 * @param name the name of the matadata
		 * @return the double representation of the metadata or null if the metadata does not exist or it can not converted to double
		 */

		public Double getDoubleMetadata(String name){
			try{
				Parameter parameter = getMeta().get(name);
				if (InterpreterUtil.isInstance(DoubleParameter.class, parameter)){
					return (Double) parameter.getValue();
				}
				return Double.parseDouble((String) getMeta().get(name).getValue());
			}catch (Exception e){
				return null;
			}
		}
		/**
		 * Get the value of a metadata as Long. The metadata requested must be a double parameter or a string that can be parsed to a long
		 * @param name the name of the metadata
		 * @return the double representation of the metadata or null if the metadata does not exist or it can not converted to long
		 */
		public Long getLongMetadata(String name){
			
			try{
				Parameter parameter = getMeta().get(name);
				if (InterpreterUtil.isInstance(DoubleParameter.class, parameter)){
					return ((Double) parameter.getValue()).longValue();
				}

				return Long.parseLong((String) getMeta().get(name).getValue());
			}catch (Exception e){
				return null;
			}
		}
		/**
		 * Get the value of a metadata as integer. The metadata requested must be a double parameter or a string that can be parsed to a integer
		 * @param name the name of the metadata
		 * @return the double representation of the metadata or null if the metadata does not exist or it can not converted to integer
		 */
		public Integer getIntegerMetadata(String name){
			try{
				Parameter parameter = getMeta().get(name);
				if (InterpreterUtil.isInstance(DoubleParameter.class, parameter)){
					return ((Double) parameter.getValue()).intValue();
				}

				return Integer.parseInt((String) getMeta().get(name).getValue());
			}catch (Exception e){
				return null;
			}
		}
		

		
		/**
		 * Read a PDS image from a file. It has been only tested with MEX images, so it is improbable that can read universal PDS files.
		 * @param file the full path to the file
		 * @return
		 * @throws IOException If it has problems opening the file
		 */
		public static PDSImage readPdsFile(String file) throws IOException{
			BufferedReader f;
			
			f = new BufferedReader(new FileReader(file));
			
			String line="";
			String keyword="";
			String value="";
			int height=0;
			int width=0;
			HashMap<String,String> properties=new HashMap<String,String>();
			while (!line.trim().equals("END")) {//read label until we get to the END statement
				    
				line = f.readLine();
				if (line == null) break;
				line = line.replace('"',' ');
				if (line.trim().equals("")) continue;
				if (line.indexOf("=") > 0) {
					keyword = line.substring(0,line.indexOf("=")).trim();
					value = line.substring(line.indexOf("=")+1,line.length()).trim().toUpperCase();
					}
				else continue;
				if (value.length() == 0) continue;
				if (keyword.length()>0 && value.length()>0) properties.put(keyword, value);
			}
			height = Integer.parseInt(properties.get("LINES"));
			width = Integer.parseInt(properties.get("LINE_SAMPLES"));
			
			f.close();
			FileInputStream fin = new FileInputStream(file);
			DataInputStream in = new DataInputStream(fin);
			byte data[]=new byte[in.available()];
			in.read(data);
			int os = data.length-(height*width);
			Int2d image2D=new Int2d(height,width);
			int locator=0;
	        for (int row = 0; row < height; row++) {
	            for (int column = 0; column < width; column++) {
	            	image2D.set(row,column,data[os+locator] & 0xFF);
	            	locator++;
	            }
	        }
	        PDSImage si = new PDSImage();
	        si.setImage(image2D);
	        fin.close();
			String creationTime=properties.get("PRODUCT_CREATION_TIME");
			try{
				if (creationTime!=null){
					si.setCreationDate(new FineTime(DateUtil.zuluToDate(creationTime)));
					properties.remove("PRODUCT_CREATION_TIME");
				}
				
			}catch (Exception e){
				e.printStackTrace();
			}
			String startTime=properties.get("START_TIME");
			try{
				if (startTime!=null){
					si.setStartDate(new FineTime(DateUtil.zuluToDate(startTime)));
					properties.remove("START_TIME");
				}
				
			}catch (Exception e){
				e.printStackTrace();
			}
			String stopTime=properties.get("STOP_TIME");
			try{
				if (stopTime!=null){
					si.setEndDate(new FineTime(DateUtil.zuluToDate(stopTime)));
					properties.remove("STOP_TIME");
				}
				
			}catch (Exception e){
				e.printStackTrace();
			}
			
			String instrument=properties.get("INSTRUMENT_ID");
			if (instrument!=null){
				si.setInstrument(instrument);
				properties.remove("INSTRUMENT_ID");
			}
			si.setType("PDSImage");
			si.setCreator("JMARS Hipe plugin");
			Iterator<String> it2 = properties.keySet().iterator();
			while (it2.hasNext()){
				String name=it2.next();
				String val=properties.get(name);
				int unitS = val.indexOf("<");
				int unitE=val.indexOf(">");
				String unit="";
				String nValue="";
				if (unitS>-1){
					unit=val.substring(unitS+1, unitE);
					nValue=val.substring(0,unitS-1);
					DoubleParameter param = new DoubleParameter(Double.parseDouble(nValue));

					try{
						param.setUnit(herschel.share.unit.Unit.parse(unit));
					
						si.getMeta().set(name, param);
					}catch (Exception e){
						e.printStackTrace();
						si.getMeta().set(name, new StringParameter(val));
					}
				}
				else{
					si.getMeta().set(name, new StringParameter(val));
				}
			}

	        return si;

			

		}
		
		
		/**
		 * Get the maximum latitude of this image. It reads the value form MAXIMUM_LATITUDE metadata. If the metadata is not present, it will fail.
		 * @return the maximum latitude of this image
		 */
		public double getMaximumLatitude(){
			return this.getDoubleMetadata(MAXIMUM_LATITUDE);
		}
		/**
		 * Get the minimum latitude of this image. It reads the value form MINIMUM_LATITUDE metadata. If the metadata is not present, it will fail.
		 * @return the maximum latitude of this image
		 */
		public double getMinimumLatitude(){
			return this.getDoubleMetadata(MINIMUM_LATITUDE);
		}
		/**
		 * Get the minimum longitude of this image. It reads the value form WESTERNMOST_LONGITUDE metadata. If the metadata is not present, it will fail.
		 * @return the maximum latitude of this image
		 */
		public double getMinimumLongitude(){
			return this.getDoubleMetadata(WESTERNMOST_LONGITUDE);
		}
		/**
		 * Get the maximum longitude of this image. It reads the value form EASTERNMOST_LONGITUDE metadata. If the metadata is not present, it will fail.
		 * @return the maximum latitude of this image
		 */
		public double getMaximumLongitude(){
			return this.getDoubleMetadata(EASTERNMOST_LONGITUDE);
		}
		
		/**
		 * Get the x and y postion of the pixel that represents these coordinates in latitude and longitude
		 * @param latitude
		 * @param longitude
		 * @return An array of int with size 2. Index 0 represents the x value and index 1 the y value.
		 * @throws InvalidValueException If the latitude and longitude coordinates are outside this image.
		 */
		public int[] getPixelAt(double latitude,double longitude) throws InvalidValueException{
			if (latitude<getMinimumLatitude() || latitude>getMaximumLatitude()){
				throw new InvalidValueException("Latitude requested "+latitude+" is outside the data in the image "+getMinimumLatitude()+"-"+getMaximumLatitude());
			}
			if (longitude<getMinimumLongitude() || longitude>getMaximumLongitude()){
				throw new InvalidValueException("Longitude requested "+longitude+" is outside the data in the image "+getMinimumLongitude()+"-"+getMaximumLongitude());
			}

			int[] result = new int[2];
			double maxLon = getMaximumLongitude();
			double minLon = getMinimumLongitude();
			double maxLat = getMaximumLatitude();
			double minLat = getMinimumLatitude();
			
			double lonPerPixel = this.getHeight()/(maxLon-minLon);
			double latPerPixel = this.getWidth()/(maxLat-minLat);
			double factorLongitude = longitude-minLon;
			double factorLatitude = latitude-minLat;
			result[1]=new Double(lonPerPixel*factorLongitude).intValue();
			result[0]=new Double(latPerPixel*factorLatitude).intValue();
			return result;
		}
		
		/**
		 * Get a subset of this image described by a rectangle. All corners of the rectangle need to be within this picture
		 * @param minimumLatitude Latitude of the left upper corner of the rectangle
		 * @param minimumLongitude Longitude of the left upper corner of the rectangle
		 * @param maximumLatitude Latitude of the right down corner of the rectangle
		 * @param maximumLongitude Longitude of the right down corner of the rectangle
		 * @return
		 * @throws InvalidValueException If any of the corners are outside the current picture
		 */
		public PDSImage getSubImage(double minimumLatitude,double minimumLongitude,double maximumLatitude,double maximumLongitude) throws InvalidValueException{
			PDSImage result=new PDSImage();
			Iterator<String> it = getMeta().keySet().iterator();
			while (it.hasNext()){
				String name=it.next();
				result.getMeta().set(name, getMeta().get(name));
			}
			Int2d data = (Int2d) this.getDataset().getData();
			int[] startPixel = getPixelAt(minimumLatitude,minimumLongitude);
			int[] endPixel = getPixelAt(maximumLatitude,maximumLongitude);

			int width=endPixel[0]-startPixel[0];
			int height=endPixel[1]-startPixel[1];
			
			Int2d subdata=new Int2d(height,width);
			for (int x=startPixel[0];x<endPixel[0];x++){
				for (int y=startPixel[1];y<endPixel[1];y++){
					int newX = x-startPixel[0];
					int newY = y-startPixel[1];
					subdata.set(newY,newX,  data.get(y, x));
				}
			}
			result.setImage(subdata);
			result.getMeta().set(MAXIMUM_LATITUDE, new StringParameter(""+maximumLatitude));
			result.getMeta().set(MINIMUM_LATITUDE, new StringParameter(""+minimumLatitude));
			result.getMeta().set(WESTERNMOST_LONGITUDE, new StringParameter(""+minimumLongitude));
			result.getMeta().set(EASTERNMOST_LONGITUDE, new StringParameter(""+maximumLongitude));
			result.getMeta().set(LINE_SAMPLES, new StringParameter(""+width));
			result.getMeta().set(LINES, new StringParameter(""+height));
			
			
			return result;
		}
		
	    /**
	     * Get the writable raster of this image
	     * @return The writable raster
	     */
	    public  WritableRaster getWritableRaster() {
	        Int2d image2D = (Int2d) this.getImage();
	        int width = image2D.getDimensions()[1];
	        int height = image2D.getDimensions()[0];
	        int bands = 1;

	        Selection sel = image2D.where(IsFinite.PREDICATE);
	        int max = (Integer) image2D.get(sel).apply(Max.FOLDR);
	        int min = (Integer) image2D.get(sel).apply(Min.FOLDR);

	        WritableRaster raster = Raster.createBandedRaster(DataBuffer.TYPE_BYTE, width, height, bands,
	                new Point(0, 0));

	        double value = 0.0;

	        for (int row = 0; row < height; row++) {
	            for (int column = 0; column < width; column++) {
	            	value=image2D.get(row, column);
	                raster.setSample(column, row, 0, (value - min) * 255 / (max - min));
	            }
	        }
	        return raster;
	    }
	    
	    /**
	     * Get this image as BufferedImage so it can be handle by standard java image library
	     * @return
	     */
	    public  BufferedImage asBufferedImage() {
	        ColorSpace colorSpace = ColorSpace.getInstance(ColorSpace.CS_GRAY);
	        boolean hasAlpha = false;
	        boolean isAlphaPremultiplied = false;
	        int transparency = Transparency.OPAQUE;
	        int transferType = DataBuffer.TYPE_BYTE;
	        ColorModel colorModel = new ComponentColorModel(colorSpace, hasAlpha, isAlphaPremultiplied,
	                transparency, transferType);
	        WritableRaster raster = getWritableRaster();

	        BufferedImage buffered = new BufferedImage(colorModel, raster, isAlphaPremultiplied, null);
	        return buffered;
	    }
	    
	    /**
	     * Reduce the scale of this picture by a factor. 1.0 is the same scale of this picture. 0.0 is a no picture
	     * @param factor a float value between 0 and 1
	     * @return The escalated image
	     * @throws InvalidValueException if the factor is greater than 1 or less than 0
	     */
	    public PDSImage descalate(float factor) throws InvalidValueException{
	    	if (factor>1 || factor<0) throw new InvalidValueException("Can not scalate to a bigger resolution:"+factor);
	    	Image newImage = this.getPreview(factor);
	    	AbstractOrdered2dData data = newImage.getImage();
			PDSImage result=new PDSImage();
			Iterator<String> it = getMeta().keySet().iterator();
			while (it.hasNext()){
				String name=it.next();
				result.getMeta().set(name, getMeta().get(name));
			}
			result.setImage(data);
			DoubleParameter mapScale = new DoubleParameter(getDoubleMetadata(MAP_SCALE)*factor);
			DoubleParameter mapResolution = new DoubleParameter(getDoubleMetadata(MAP_RESOLUTION)*factor);
			mapScale.setUnit(((DoubleParameter)getMeta().get(MAP_SCALE)).getUnit());
			mapResolution.setUnit(((DoubleParameter)getMeta().get(MAP_RESOLUTION)).getUnit());

			result.getMeta().set(MAP_SCALE, mapScale);
			result.getMeta().set(MAP_RESOLUTION, mapResolution);
			
			return result;

	    }
	    
	    /**
	     * Get the picture at a specific KM/PIXEL scale
	     * @param scale
	     * @return The escalated image
	     * @throws InvalidValueException In the case that the scale result in a bigger resolution than the current image
	     */
	    public PDSImage getAtScale(float scale) throws InvalidValueException{
	    	float currentScale = new Double(getDoubleMetadata(MAP_SCALE)).floatValue();
	    	if (scale==currentScale) return this;
	    	return descalate(1/(scale/currentScale));
	    }
	    
	    /**
	     * Get the image a specific PIXEL/DEGREE resolution
	     * @param resolution 
	     * @return
	     * @throws InvalidValueException In the case that the PIXEL/DEGREE resolution result in a bigger resolution than the current image
	     */
	    public PDSImage getAtResolution(float resolution) throws InvalidValueException{
	    	return descalate(resolution/new Double(getDoubleMetadata(MAP_RESOLUTION)).floatValue());
	    }





}
