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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Iterator;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import vega.hipe.logging.VegaLog;
import vega.hipe.pds.vicar.PDSInputFile;
import vega.uplink.DateUtil;
import herschel.ia.dataset.DoubleParameter;
import herschel.ia.dataset.Parameter;
import herschel.ia.dataset.StringParameter;
import herschel.ia.dataset.image.Image;
import herschel.ia.dataset.image.SimpleImage;
import herschel.ia.numeric.AbstractOrdered2dData;
import herschel.ia.numeric.Float2d;
import herschel.ia.numeric.Int2d;
import herschel.ia.numeric.Selection;
import herschel.ia.numeric.Short2d;
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
	/**
	 * OFFSET_ANGLE
	 */
	public static String OFFSET_ANGLE="OFFSET_ANGLE";
	/**
	 * PERIAPSIS_TIME The PERIAPSIS_TIME element is the time, in UTC format
	 * 'YYYY-MM-DDThh:mm:ss[.fff]Z', when the spacecraft passes
	 * through periapsis on a particular orbit. Periapsis is the
	 * closest approach point of the spacecraft to the target
	 * body surface in its orbit around the target body.
	 */
	public static String PERIAPSIS_TIME="PERIAPSIS_TIME";
	/**
	 * LINE_SAMPLES The line_samples element indicates the total number of data
	 * instances along the horizontal axis of an image. 
	 */
	public static String LINE_SAMPLES="LINE_SAMPLES";
	/**
	 * WESTERNMOST_LONGITUDE The following definitions describe westernmost longitude for the
	 * body-fixed, rotating coordinate systems:
	 * For Planetocentric coordinates and for Planetographic coordinates in
	 * which longitude increases toward the east, the westernmost (leftmost)
	 * longitude of a spatial area (e.g.,a map, mosaic, bin,feature or region)
	 * is the minimum numerical value of longitude unless it crosses the
	 * Prime Meridian.
	 * 
	 * For Planetographic coordinates in which longitude increases toward the
	 * west (prograde rotator), the westernmost (leftmost) longitude of a
	 * spatial area (e.g., a map,mosaic, bin, feature or region) is the maximum
	 * numerical value of longitude unless it crosses the Prime Meridian.
	 * 
	 * For the Earth, Moon and Sun, PDS also supports the traditional use of
	 * the range (-180,180) in which case the westernmost (leftmost) longitude
	 * is the minimum numerical value of longitude unless it crosses -180.
	 */
	public static String WESTERNMOST_LONGITUDE="WESTERNMOST_LONGITUDE";
	/**
	 * ^DATA_SET_MAP_PROJECTION_CATALOG
	 */
	public static String DATA_SET_MAP_PROJECTION_CATALOG="^DATA_SET_MAP_PROJECTION_CATALOG";
	/**
	 * DATA_QUALITY_DESC The data_quality_desc element describes the data
	 * quality which is associated with a particular data_quality_id value.
	 * The various values of data_quality_id and
	 * data_quality_desc are instrument dependent.
	 */
	public static String DATA_QUALITY_DESC="DATA_QUALITY_DESC";
	/**
	 * MEAN The mean element provides the average of the DN values in the image array. 
	 */
	public static String MEAN="MEAN";
	/**
	 * The RELEASE_ID element identifies the unique identifier 
	 * associated with a specific release of a data set. All initial 
	 * releases should use a RELEASE_ID value of '0001'. Subsequent 
	 * releases should use a value that represents the next increment
	 * over the previous RELEASE_ID (e.g., the second release should use
	 * a RELEASE_ID of '0002').
	 * 
	 * Releases are done when an existing data set or portion of a data
	 * set becomes available for distribution.
	 * 
	 * Note:  The DATA_SET_ID and RELEASE_ID are used as a combined
	 * key to ensure all releases are unique.
	 */
	public static String RELEASE_ID="RELEASE_ID";
	/**
	 * LINE_FIRST_PIXEL The line_first_pixel element provides the line index for the first pixel that was physically recorded at the beginning of the image array. 
	 */
	public static String LINE_FIRST_PIXEL="LINE_FIRST_PIXEL";
	/**
	 * FILE_RECORDS The file_records element indicates the number of physical
	 * file records, including both label records and data records.
	 */
	public static String FILE_RECORDS="FILE_RECORDS";
	/**
	 * SAMPLE_PROJECTION_OFFSET The sample_projection_offset element provides the sample
	 * offset value of the map projection origin position from
	 * line and sample 1,1 (line and sample 1,1 is considered the
	 * upper left corner of the digital array).
	 * Note: that the positive direction is to the right and down. 
	 */
	public static String SAMPLE_PROJECTION_OFFSET="SAMPLE_PROJECTION_OFFSET";
	/**
	 * INST_CMPRS_RATIO The inst_cmprs_ratio element provides the ratio of the size, in bytes,
	 * of the original uncompressed data file to its compressed form.
	 */
	public static String INST_CMPRS_RATIO="INST_CMPRS_RATIO";
	/**
	 * REVISION_ID
	 */
	public static String REVISION_ID="REVISION_ID";
	/**
	 * ORBITAL_INCLINATION The orbital_inclination element provides the value of the angle
	 * between the orbital plane of a target body and the ecliptic.
	 * The body's orbit direction is prograde if 0 < i < 90 degrees,
	 * where i is the value of orbital inclination.  The orbit
	 * direction is retrograde if 90 < i < 180 degrees. 
	 */
	public static String ORBITAL_INCLINATION="ORBITAL_INCLINATION";
	/**
	 * SAMPLE_LAST_PIXEL The sample_last_pixel element provides the sample index for
	 * the last pixel that was physically recorded at the end of
	 * the image array.
	 */
	public static String SAMPLE_LAST_PIXEL="SAMPLE_LAST_PIXEL";
	/**
	 * REFLECTANCE_SCALING_FACTOR The reflectance_scaling_factor element identifies the
	 * conversion factor from DN to reflectance.
	 */
	public static String REFLECTANCE_SCALING_FACTOR="REFLECTANCE_SCALING_FACTOR";
	/**
	 * EASTERNMOST_LONGITUDE The following definitions describe easternmost longitude for the
	 * body-fixed, rotating coordinate systems: 
	 * 
	 * For Planetocentric coordinates and for Planetographic coordinates in
	 * which longitude increases toward the east, the easternmost (rightmost)
	 * longitude of a spatial area (e.g.,a map, mosaic, bin,feature or region)
	 * is the maximum numercial value of longitude unless it crosses the
	 * Prime Meridian.
	 * 
	 * For Planetographic coordinates in which longitude increases toward the
	 * west, the easternmost (rightmost) longitude of a spatial area (e.g., a
	 * map,mosaic, bin, feature or region) is the minimum numerical value
	 * of longitude unless it crosses the Prime Meridian.
	 * 
	 * For the Earth, Moon and Sun, PDS also supports the traditional use of
	 * the range (-180,180) in which case the easternmost (rightmost) longitude
	 * is the maximum numerical value of longitude unless it crosses 180.
	 */
	public static String EASTERNMOST_LONGITUDE="EASTERNMOST_LONGITUDE";
	/**
	 * MINIMUM_LATITUDE The minimum_latitude element specifies the southernmost
	 * latitude of a spatial area, such as a map, mosaic, bin,
	 * feature, or region.
	 */
	public static String MINIMUM_LATITUDE="MINIMUM_LATITUDE";
	/**
	 * MINIMUM The minimum element indicates the smallest value occurring
	 * in a given instance of the data object.
	 */
	public static String MINIMUM="MINIMUM";
	/**
	 * BANDS The BANDS element indicates the number of bands in an image or other object.
	 */
	public static String BANDS="BANDS";
	/**
	 * REFERENCE_LONGITUDE The reference_longitude element defines the zero longitude
	 * in a rotated spherical coordinate system that was used in a
	 * given map_projection_type.
	 */
	public static String REFERENCE_LONGITUDE="REFERENCE_LONGITUDE";
	/**
	 * SPACECRAFT_POINTING_MODE The spacecraft_pointing_mode element provides the pointing mode of the
	 * spacecraft.  The definition of the modes and the standard values are
	 * given via the SPACECRAFT_POINTING_MODE_DESC element, which shall always
	 * accompany this keyword
	 */
	public static String SPACECRAFT_POINTING_MODE="SPACECRAFT_POINTING_MODE";
	/**
	 * COORDINATE_SYSTEM_NAME The coordinate_system_name element provides the full name
	 * of the coordinate system to which the state vectors are referenced.
	 * PDS has currently defined body-fixed rotating coordinate systems.
	 * 
	 * The Planetocentric system has an origin at the center of mass of the body.
	 * The planetocentric latitude is the angle between the equatorial plane
	 * and a vector connecting the point of interest and the origin of the
	 * coordinate system.  Latitudes are defined to be positive in the northern
	 * hemisphere of the body, where north is in the direction of Earth's
	 * angular momentum vector, i.e., pointing toward the hemisphere north of
	 * the solar system invariant plane.  Longitudes increase toward the east,
	 * making the Planetocentric system right-handed.
	 * 
	 * The Planetographic system has an origin at the center of mass of the body.
	 * The planetographic latitude is the angle between the equatorial plane
	 * and a vector through the point of interest, where the vector is normal
	 * to a biaxial ellipsoid reference surface.  Planetographic longitude is
	 * defined to increase with time to an observer fixed in space above the
	 * object of interest.  Thus, for prograde rotators (rotating counter
	 * clockwise as seen from a fixed observer located in the hemisphere to the
	 * north of the solar system invariant plane), planetographic longitude
	 * increases toward the west.  For a retrograde rotator, planetographic
	 * longitude increases toward the east.
	 * Note:  If this data element is not present in the PDS Image Map
	 * Projection Object (for pre-V3.1 PDS Standards), the default coordinate
	 * system is assumed to body-fixed rotating Planetographic.
	 */
	public static String COORDINATE_SYSTEM_NAME="COORDINATE_SYSTEM_NAME";
	/**
	 * DETECTOR_ID The detector_id element identifies a particular instrument detector.
	 * The associated detector_desc element describes the detector. 
	 */
	public static String DETECTOR_ID="DETECTOR_ID";
	/**
	 * The MISSING_FRAMES element is the total number of frames that are missing from a file.
	 * 
	 * Note: For MARS EXPRESS, a frame, which is also called a 'row', is eight
	 * lines of data. Each line, in turn, is composed of a sync marker followed by
	 * a group of blocks (GOB). This refers to the Data Compression Electronics (DCE) frames.
	 */
	public static String MISSING_FRAMES="MISSING_FRAMES";
	/**
	 * COORDINATE_SYSTEM_TYPE There are three basic types of coordinate systems: body-fixed rotating,
	 * body-fixed non-rotating and inertial. A body-fixed coordinate system is one
	 * associated with a body (e.g., planetary body or satellite).  In contrast to
	 * inertial coordinate systems, a body-fixed coordinate system is centered
	 * on the body and rotates with the body (unless it is a non-rotating type).
	 * For the inertial coordinate system type, the coordinate system is fixed
	 * at some point in space.
	 * 
	 * Note:  If this data element is not present in the PDS Image Map
	 * Projection Object (for pre-V3.1 PDS Standards), the default coordinate
	 * system is assumed to be body-fixed rotating Planetographic.
	 */
	public static String COORDINATE_SYSTEM_TYPE="COORDINATE_SYSTEM_TYPE";
	/**
	 * INTERCHANGE_FORMAT The interchange_format element represents the manner in
	 * which data items are stored.  Example values: BINARY, ASCII.
	 */
	public static String INTERCHANGE_FORMAT="INTERCHANGE_FORMAT";
	/**
	 * PROCESSING_LEVEL_ID The processing_level_id element identifies the
	 * processing level of a set of data according to the eight_level CODMAC standard.  
	 */
	public static String PROCESSING_LEVEL_ID="PROCESSING_LEVEL_ID";
	/**
	 * SPACECRAFT_CLOCK_STOP_COUNT The spacecraft_clock_stop_count element provides the value
	 * of the spacecraft clock  at the end of a time period of interest.  
	 */
	public static String SPACECRAFT_CLOCK_STOP_COUNT="SPACECRAFT_CLOCK_STOP_COUNT";
	/**
	 * BANDWIDTH The bandwidth element provides a measure of the spectral
	 * width of a filter or channel.  For a root-mean-square
	 * detector this is the effective bandwidth of the filter
	 * i.e., the full width of an ideal square filter having a flat
	 * response over the bandwidth and zero response elsewhere.
	 */
	public static String BANDWIDTH="BANDWIDTH";
	/**
	 * A_AXIS_RADIUS The a_axis_radius element provides the value of the semimajor axis of
	 * the ellipsoid that defines the approximate shape of a target body.  'A'
	 * is usually in the equitorial plane.   
	 */
	public static String A_AXIS_RADIUS="A_AXIS_RADIUS";
	/**
	 * SPACECRAFT_CLOCK_START_COUNT The spacecraft_clock_start_count element provides the
	 * value of the spacecraft clock at the beginning of a time period of interest.
	 */
	public static String SPACECRAFT_CLOCK_START_COUNT="SPACECRAFT_CLOCK_START_COUNT";
	/**
	 * PRODUCT_ID The product_id data element represents a permanent, unique
	 * identifier assigned to a data product by its producer.
	 * See also:  source_product_id.
	 * Note:  In the PDS, the value assigned to product_id must
	 * be unique within its data set.
	 * Additional note: The product_id can describe the
	 * lowest-level data object that has a PDS label.  
	 */
	public static String PRODUCT_ID="PRODUCT_ID";
	/**
	 * MAXIMUM_LATITUDE The maximum_latitude element specifies the northernmost
	 * latitude of a spatial area, such as a map, mosaic, bin, feature, or region. 
	 */
	public static String MAXIMUM_LATITUDE="MAXIMUM_LATITUDE";
	/**
	 * The PERIAPSIS_ALTITUDE element provides the distance between the spacecraft
	 * and the target body surface at periapsis on a particular orbit.
	 * 
	 * Note: For MARS EXPRESS, the altitude is measured from the surface of the
	 * target body, which is defined by an ellipsoid in the NAIF planetary
	 * constants kernel. (Contact the NAIF NODE for more information.)
	 */
	public static String PERIAPSIS_ALTITUDE="PERIAPSIS_ALTITUDE";
	/**
	 * The PIXEL_SUBSAMPLING_FLAG element indicates whether the product is the
	 * result of subsampling of the data. Subsampling is the process of measuring
	 * the brightness or intensity of a continuous image of discrete points, at
	 * an arbitrary interval, producing a new array of values.
	 */
	public static String PIXEL_SUBSAMPLING_FLAG="PIXEL_SUBSAMPLING_FLAG";
	/**
	 * ^DESCRIPTION
	 */
	public static String DESCRIPTION="^DESCRIPTION";
	/**
	 * SPACECRAFT_ORIENTATION The spacecraft orientation element provides the orientation of a
	 * spacecraft in orbit or cruise in respect to a given frame, (e.g.
	 * a non-spinning spacecraft might be flown in +Y or -Y direction in
	 * respect to the spacecraft mechanical build frame). This element
	 * shall be used in combination with the keyword spacecraft_orientation_desc
	 * that describes the convention used to describe the spacecraft orientation.
	 * The spacecraft orientation shall be given as a 3-tuple, one value for the
	 * x, y and z axes
	 */
	public static String SPACECRAFT_ORIENTATION="SPACECRAFT_ORIENTATION";
	/**
	 * B_AXIS_RADIUS The b_axis_radius element provides the value of the intermediate axis
	 * of the ellipsoid that defines the approximate shape of a target body.
	 * 'B' is usually in the equatorial plane. 
	 */
	public static String B_AXIS_RADIUS="B_AXIS_RADIUS";
	/**
	 * MISSION_NAME The mission_name element identifies a major planetary mission or project.
	 */
	public static String MISSION_NAME="MISSION_NAME";
	/**
	 * SAMPLE_FIRST_PIXEL The sample_first_pixel element provides the sample index
	 * for the first pixel that was physically recorded at the
	 * beginning of the image array.
	 */
	public static String SAMPLE_FIRST_PIXEL="SAMPLE_FIRST_PIXEL";
	/**
	 * The DETECTOR_TEMPERATURE is the temperature that the instrument
	 * (detector) operated at while a measurement was made.
	 */
	public static String DETECTOR_TEMPERATURE="DETECTOR_TEMPERATURE";
	/**
	 * PDS_VERSION_ID The PDS_version_id data element represents the version
	 * number of the PDS standards documents that is valid
	 * when a data product label is created.  Values for the
	 * PDS_version_id are formed by appending the integer for
	 * the latest version number to the letters 'PDS'.  Examples:
	 * PDS3, PDS4.        
	 */
	public static String PDS_VERSION_ID="PDS_VERSION_ID";
	/**
	 * EVENT_TYPE The event_type element identifies the classification of an event.
	 * Example values:  MAGNETOPAUSE CROSSING, VOLCANIC ERUPTION, CLOSEST APPROACH. 
	 */
	public static String EVENT_TYPE="EVENT_TYPE";
	/**
	 * DATA_SET_ID The data_set_id element is a unique
	 * alphanumeric identifier for a data set or a data product.
	 * The data_set_id value for a given data set or
	 * product is constructed according to flight project
	 * naming conventions.  In most cases the data_set_id is an
	 * abbreviation of the data_set_name.
	 */
	public static String DATA_SET_ID="DATA_SET_ID";
	/**
	 * FIRST_STANDARD_PARALLEL The first_standard_parallel element  is used in Conic 
	 * projections.  If a Conic projection has a single standard
	 * parallel, then the first_standard_parallel is the point of
	 * tangency between the sphere of the planet and the cone of
	 * the projection.  If there are two standard parallels
	 * (first_standard_parallel, second_standard_parallel), these
	 * parallel are the intersection lines  between the sphere of
	 * the planet and the cone of the projection.  The map_scale
	 * is defined at the standard parallels.
	 */
	public static String FIRST_STANDARD_PARALLEL="FIRST_STANDARD_PARALLEL";
	/**
	 * SPACECRAFT_SOLAR_DISTANCE The spacecraft_solar_distance element provides the distance
	 * from the spacecraft to the center of the sun.  See also: solar_distance.
	 */
	public static String SPACECRAFT_SOLAR_DISTANCE="SPACECRAFT_SOLAR_DISTANCE";
	/**
	 * MAP_RESOLUTION The map_resolution element identifies the scale of a given
	 * map.  Please refer to the definition for map_scale for a 
	 * more complete definition. 
	 * 
	 * Note: map_resolution and map_scale both define the scale
	 * of a map except that they are expressed in different
	 * units:  map_resolution is in PIXEL/DEGREE and
	 * map_scale is in KM/PIXEL.
	 */
	public static String MAP_RESOLUTION="MAP_RESOLUTION";
	/**
	 * ORBIT_NUMBER The orbit_number element identifies the number of the
	 * orbital revolution of the spacecraft around a target body.
	 * Note:  In PDS Magellan altimetry and radiometry labels,
	 * the orbit_number data element refers to the Magellan
	 * orbit number corresponding to the following files: 
	 * ephemeris, altimetry, and radiometry.
	 */
	public static String ORBIT_NUMBER="ORBIT_NUMBER";
	/**
	 * BYTES The bytes element indicates the number of bytes allocated for a particular
	 * data representation. When BYTES describes an object with variable length
	 * (e.g., FIELD), BYTES gives the maximum number of bytes allowed. 
	 */
	public static String BYTES="BYTES";
	/**
	 * The MACROPIXEL_SIZE element provides the sampling array size (e.g., 2x2,
	 * 4x4, 8x8), in pixels, that is used to reduce the amount of data an image
	 * contains by summing the values of the pixels, along the lines of the image.
	 * This process may be performed for images with increased exposure times in
	 * flight direction. Also known as summation mode.
	 */
	public static String MACROPIXEL_SIZE="MACROPIXEL_SIZE";
	/**
	 * INST_CMPRS_NAME The inst_cmprs_name element identifies the type of on-board compression
	 * used for data storage and transmission.
	 * Note:  The inst_cmprs_mode element provides an abbreviated identifier
	 * for the inst_cmprs_name.
	 */
	public static String INST_CMPRS_NAME="INST_CMPRS_NAME";
	/**
	 * RECORD_TYPE The record_type element indicates the record format of a file.
	 * 
	 * Note:  In the PDS, when record_type is used in a
	 * detached label file it always describes its corresponding
	 * detached data file, not the label file itself.
	 */
	public static String RECORD_TYPE="RECORD_TYPE";
	/**
	 * ORBITAL_ECCENTRICITY The orbital_eccentricity provides a measure of the non-circularity of an
	 * orbit.  Circular orbits have eccentricities of 0, elliptical orbits have
	 * eccentricities between 0 and 1, parabolic trajectories have eccentricities
	 * of 1, and hyperbolic trajectories have eccentricities greater than 1.
	 */
	public static String ORBITAL_ECCENTRICITY="ORBITAL_ECCENTRICITY";
	/**
	 * END_OBJECT
	 */
	public static String END_OBJECT="END_OBJECT";
	/**
	 *  The DECLINATION element provides the value of an angle on the
	 *  celestial sphere, measured north from the celestial equator to the 
	 *  point in question. (For points south of the celestial equator, 
	 *  negative values are used.) DECLINATION is used in conjunction with 
	 *  the RIGHT_ASCENSION keyword to specify a point on the sky. 
	 *  
	 *  To accurately specify a point on the sky, the following additional 
	 *  keywords are needed: 
	 *  
	 *  COORDINATE_SYSTEM_ID - Specifies the reference system as B1950 or J2000. 
	 *  
	 *  EQUINOX_EPOCH - Specifies the epoch of equinox in decimal years. 
	 *  
	 *  To relate the specified values of right ascension and declination to 
	 *  an image, the following keyword is needed: 
	 *  
	 *  RA_DEC_REF_PIXEL - A two-valued keyword to specify the reference 
	 *  pixel to which the RIGHT_ASCENSION and DECLINATION apply. 
	 *  
	 *  An additional useful keyword for specifying the relation of 
	 *  declination and right_ascension to an image is: 
	 *  
	 *  PIXEL_ANGULAR_SCALE - the angular scale of the image in arcseconds 
	 *  per pixel. 
	 */
	public static String DECLINATION="DECLINATION";
	/**
	 * LINES The lines element indicates the total number of data 
	 * instances along the vertical axis of an image.
	 * 
	 * Note: In PDS label convention, the number of lines is
	 * stored in a 32-bit integer field. The minimum value of 0  
	 * indicates no data received.  
	 */
	public static String LINES="LINES";
	/**
	 * MISSION_PHASE_NAME The mission_phase_name element provides the commonly-used
	 * identifier of a mission phase.
	 */
	public static String MISSION_PHASE_NAME="MISSION_PHASE_NAME";
	/**
	 * LENS_TEMPERATURE The lens_temperature element provides the temperature of
	 * the lens in degrees kelvin at the time the observation was
	 * made.
	 */
	public static String LENS_TEMPERATURE="LENS_TEMPERATURE";
	/**
	 * HEADER_TYPE The HEADER_TYPE element identifies a specific type of
	 * header data structure.  For example: FITS, VICAR. 
	 */
	public static String HEADER_TYPE="HEADER_TYPE";
	/**
	 * SECOND_STANDARD_PARALLEL Please refer to the definition for first_standard_parallel
	 * element to see how second_standard_parallel is defined.
	 */
	public static String SECOND_STANDARD_PARALLEL="SECOND_STANDARD_PARALLEL";
	/**
	 * CENTER_LONGITUDE The center_longitude element provides a reference longitude 
	 * for certain map projections. For example, in an 
	 * Orthographic projection, the center_longitude along with 
	 * the center_latitude defines the point or tangency between 
	 * the sphere of the planet and the plane of the projection. 
	 * The map_scale (or map_resolution) is typically defined at 
	 * the center_latitude and center_longitude. In unprojected 
	 * images, center_longitude represents the longitude at the 
	 * center of the image frame. 
	 */
	public static String CENTER_LONGITUDE="CENTER_LONGITUDE";
	/**
	 * MAP_PROJECTION_TYPE The map_projection_type element identifies the type of projection
	 * characteristic of a given map.  Example value: ORTHOGRAPHIC. 
	 */
	public static String MAP_PROJECTION_TYPE="MAP_PROJECTION_TYPE";
	/**
	 * SAMPLE_BITS The sample_bits element indicates the stored number of
	 * bits, or units of binary information, contained in a
	 * line_sample value.
	 */
	public static String SAMPLE_BITS="SAMPLE_BITS";
	/**
	 * ^IMAGE_HEADER
	 */
	public static String IMAGE_HEADER="^IMAGE_HEADER";
	/**
	 * OBJECT
	 */
	public static String OBJECT="OBJECT";
	/**
	 * C_AXIS_RADIUS The c_axis_radius element provides the value of the semiminor axis of 
	 * the ellipsoid that defines the approximate shape of a target body. 'C' 
	 * is normal to the plane defined by 'A' and 'B'. 
	 */
	public static String C_AXIS_RADIUS="C_AXIS_RADIUS";
	/**
	 * ^IMAGE
	 */
	public static String IMAGE="^IMAGE";
	/**
	 * LINE_PROJECTION_OFFSET The line_projection_offset element provides the line 
	 * offset value of the map projection origin position from 
	 * the line and sample 1,1 (line and sample 1,1 is considered 
	 * the upper left corner of the digital array). 
	 * Note: that the positive direction is to the right and down.
	 */
	public static String LINE_PROJECTION_OFFSET="LINE_PROJECTION_OFFSET";
	/**
	 * The RIGHT_ASCENSION element provides the value of right ascension, 
	 * which is defined as the arc of the celestial equator between the 
	 * vernal equinox and the point where the hour circle through the point 
	 * in question intersects the celestial equator (reckoned eastward). 
	 * Right ascension is used in conjunction with the DECLINATION keyword 
	 * to specify a point on the sky. 
	 * 
	 * To accurately specify a point on the sky, the following additional 
	 * keywords are needed: 
	 * 
	 * COORDINATE_SYSTEM_ID -- Specifies the reference system as B1950 or J2000. 
	 * EQUINOX_EPOCH - Specifies the epoch of equinox in decimal years. 
	 * 
	 * To relate the specified values of right ascension and declination to 
	 * an image, the following keyword is needed: 
	 * 
	 * RA_DEC_REF_PIXEL - A two-valued keyword to specify the reference
	 * pixel to which the RA and dec apply. 
	 * 
	 * An additional useful keyword for specifying the relation of 
	 * declination and right_ascension to an image is: 
	 * 
	 * PIXEL_ANGULAR_SCALE - the angular scale of the image in arcseconds per pixel. 
	 */
	public static String RIGHT_ASCENSION="RIGHT_ASCENSION";
	/**
	 * REFERENCE_LATITUDE The reference_latitude element provides the new zero 
	 * latitude in a rotated spherical coordinate system that was                   
	 * used in a given map_projection_type.
	 */
	public static String REFERENCE_LATITUDE="REFERENCE_LATITUDE";
	/**
	 * INST_CMPRS_QUANTZ_TBL_ID The inst_cmprs_quantz_tbl_id element identifies the reference table 
	 * used for quantization in the frequency domain for on-board transform 
	 * compression. This name or code should be specific enough to allow the 
	 * user of the data to have sufficient information to reference the 
	 * quantization table used to compress the data. 
	 */
	public static String INST_CMPRS_QUANTZ_TBL_ID="INST_CMPRS_QUANTZ_TBL_ID";
	/**
	 * STANDARD_DEVIATION The standard_deviation element provides the standard
	 * deviation of the DN values in the image array. 
	 */
	public static String STANDARD_DEVIATION="STANDARD_DEVIATION";
	/**
	 * INSTRUMENT_NAME The instrument_name element provides the full name of an 
	 * instrument. 
	 * Note: that the associated instrument_id element 
	 * provides an abbreviated name or acronym for the instrument. 
	 * Example values: FLUXGATE MAGNETOMETER, NEAR_INFRARED 
	 * MAPPING SPECTROMETER
	 */
	public static String INSTRUMENT_NAME="INSTRUMENT_NAME";
	/**
	 * INST_CMPRS_QUALITY The inst_cmprs_quality element is a JPEG specific variable which 
	 * identifies the resultant or targeted image quality index for on-board 
	 * data compression. 
	 * Note: For MPF, if an odd IMP inst_cmprs_mode was used for 
	 * on-board compression, the inst_cmprs_quality indicated the 
	 * desired image quality index. If an odd inst_cmprs_mode was 
	 * used, this indicates the resultant image quality used to 
	 * reach the desired on-board compression factor.
	 */
	public static String INST_CMPRS_QUALITY="INST_CMPRS_QUALITY";
	/**
	 * ORBITAL_SEMIMAJOR_AXIS The orbital_semimajor_axis element provides the value of 
	 * the semimajor axis of the orbit of a target body. The 
	 * semimajor axis is one_half of the maximum dimension of an 
	 * orbit. 
	 */
	public static String ORBITAL_SEMIMAJOR_AXIS="ORBITAL_SEMIMAJOR_AXIS";
	/**
	 * RADIANCE_SCALING_FACTOR The radiance_scaling_factor element provides the constant value by which 
	 * a stored radiance is multiplied. 
	 * Note: Expressed as an equation: true_radiance_value = 
	 *  radiance_offset + radiance_scaling_factor * stored_radiance_value. 
	 *  Use of this element is discouraged in favor of the more general scaling_factor. 
	 */
	public static String RADIANCE_SCALING_FACTOR="RADIANCE_SCALING_FACTOR";
	/**
	 * The FOOTPRINT_POINT_LATITUDE element provides an array of values that 
	 * represent the latitudes of points along the edge of an image footprint 
	 * on the planet's surface. Latitude values are planetocentric. 
	 */
	public static String FOOTPRINT_POINT_LATITUDE="FOOTPRINT_POINT_LATITUDE";
	/**
	 * TARGET_NAME The  target_name element identifies a target.  The target
	 * may be a planet, satellite,ring,region, feature,
	 * asteroid or comet. 
	 */
	public static String TARGET_NAME="TARGET_NAME";
	/**
	 * BAND_STORAGE_TYPE The band_storage_type element indicates the storage 
	 * sequence of lines, samples and bands in an image. The 
	 * values describe, for example, how different samples are 
	 * interleaved in image lines, or how samples from different 
	 * bands are arranged sequentially. Example values: 
	 * BAND SEQUENTIAL, SAMPLE INTERLEAVED, LINE INTERLEAVED.
	 */
	public static String BAND_STORAGE_TYPE="BAND_STORAGE_TYPE";
	/**
	 * ASCENDING_NODE_LONGITUDE The ascending_node_longitude element provides the value of 
	 * the angle measured eastward along the ecliptic from the 
	 * vernal equinox to the ascending node of the orbit. 
	 * The ascending node is defined as the point where the body 
	 * in its orbit rises north of the ecliptic. 
	 */
	public static String ASCENDING_NODE_LONGITUDE="ASCENDING_NODE_LONGITUDE";
	/**
	 * MAP_SCALE The map_scale element identifies the scale of a given map. 
	 * The scale is defined as the ratio of the actual distance 
	 * between two points on the surface of the target body to the 
	 * distance between the corresponding points on the map. 
	 * The map_scale references the scale of a map at a certain 
	 * reference point or line. Certain map projections 
	 * vary in scale throughout the map. For example, in a 
	 * Mercator projection, the map_scale refers to the scale of 
	 * the map at the equator. For Conic projections, the 
	 * map_scale refers to the scale at the standard parallels. 
	 * For an Orthographic point, the map_scale refers to the scale 
	 * at the center latitude and longitude. The relationship 
	 * between map_scale and the map_resolution element is that 
	 * they both define the scale of a given map, except they are 
	 * expressed in different units: map_scale is in KM/PIXEL 
	 * and map_resolution is in PIXEL/DEGREE. Also note that one 
	 * is inversely proportional to the other and that kilometers and degrees 
	 * can be related given the radius of the planet: 1 degree = 
	 * (2 * RADIUS * PI) / 360 kilometers. 
	 */
	public static String MAP_SCALE="MAP_SCALE";
	/**
	 * POSITIVE_LONGITUDE_DIRECTION The positive_longitude_direction element identifies the 
	 * direction of longitude (e.g. EAST, WEST) for a planet. The 
	 * IAU definition for direction of positive longitude is 
	 * adopted. Typically, for planets with prograde rotations, 
	 * positive longitude direction is to the WEST. For planets 
	 * with retrograde rotations, positive longitude direction is 
	 * to the EAST. 
	 * Note: The positive_longitude_direction keyword should be used for 
	 * planetographic systems, but not for planetocentric. 
	 */
	public static String POSITIVE_LONGITUDE_DIRECTION="POSITIVE_LONGITUDE_DIRECTION";
	/**
	 * The FOOTPRINT_POINT_LONGITUDE element provides an array of values that 
	 * represent the longitudes of points along the edge of an image footprint 
	 * on the planet's surface. Longitude values are planetocentric. 
	 */
	public static String FOOTPRINT_POINT_LONGITUDE="FOOTPRINT_POINT_LONGITUDE";
	/**
	 * LINE_LAST_PIXEL The line_last_pixel element provides the line index for the 
	 * last pixel that was physically recorded at the end of the 
	 * image array.
	 */
	public static String LINE_LAST_PIXEL="LINE_LAST_PIXEL";
	/**
	 * FOCAL_PLANE_TEMPERATURE The focal_plane_temperature element provides the 
	 * temperature of the focal plane array in degrees kelvin at 
	 * the time the observation was made.
	 */
	public static String FOCAL_PLANE_TEMPERATURE="FOCAL_PLANE_TEMPERATURE";
	/**
	 * LABEL_RECORDS The label_records element indicates the number of physical 
	 * file records that contain only label information. The 
	 * number of data records in a file is determined by 
	 * subtracting the value of label_records from the value of 
	 * file_records.
	 */
	public static String LABEL_RECORDS="LABEL_RECORDS";
	/**
	 * SAMPLE_TYPE The sample_type element indicates the data storage 
	 * representation of sample value. 
	 * 
	 * Standard Values:
	 * CHARACTER 
	 * IEEE_REAL 
	 * LSB_INTEGER 
	 * LSB_UNSIGNED_INTEGER 
	 * MSB_INTEGER 
	 * MSB_UNSIGNED_INTEGER 
	 * PC_REAL 
	 * UNSIGNED_INTEGER 
	 * VAX_REAL 
	 */
	public static String SAMPLE_TYPE="SAMPLE_TYPE";
	/**
	 * The MAXIMUM_RESOLUTION element provides the 
	 * value of the highest resolution obtained for 
	 * a given image or data product. 
	 */
	public static String MAXIMUM_RESOLUTION="MAXIMUM_RESOLUTION";
	/**
	 * PERIAPSIS_ARGUMENT_ANGLE The periapsis_argument_angle element provides the value of 
	 * the periapsis argument angle, which is defined as the angle 
	 * measured from the ascending node of the orbit of a target 
	 * body (relative to the reference plane) to the point in the 
	 * orbit at which the target body obtains its closest approach 
	 * to the primary body. 
	 * See also: ascending_node_longitude.
	 */
	public static String PERIAPSIS_ARGUMENT_ANGLE="PERIAPSIS_ARGUMENT_ANGLE";
	/**
	 * The RADIANCE_OFFSET element provides the constant value by which a stored 
	 * radiance value is shifted or displaced. 
	 * Note: Expressed as an equation: true_radiance_value = radiance_offset + 
	 * radiance_scaling_factor * stored_radiance_value. Use of this element is 
	 * discouraged in favor of the more general offset element. 
	 */
	public static String RADIANCE_OFFSET="RADIANCE_OFFSET";
	/**
	 * INSTRUMENT_HOST_ID The instrument_host_id element provides a unique identifier 
	 * for the host where an instrument is located. This host can be either a 
	 * spacecraft or an earth base (e.g., and observatory or laboratory on the 
	 * earth). Thus, the instrument_host_id element can contain values which 
	 * are either spacecraft_id values or earth_base_id values. 
	 */
	public static String INSTRUMENT_HOST_ID="INSTRUMENT_HOST_ID";
	/**
	 * FILE_NAME The file_name element provides the location independent name of a file. 
	 * It excludes node or volume location, directory path names, and version 
	 * specification. To promote portability across multiple platforms, PDS 
	 * requires the file_name to be limited to a 36-character basename, a full 
	 * stop (. period), and a 3-character extension. Valid characters include 
	 * capital letters A - Z, numerals 0 - 9, and the underscore character (_). 
	 * See chapter 10 of the PDS Standards Reference for more details on naming 
	 * files included in PDS archives. 
	 */
	public static String FILE_NAME="FILE_NAME";
	/**
	 * INSTRUMENT_HOST_NAME The instrument_host_name element provides the full name of 
	 * the host on which an instrument is based. This host can be 
	 * either a spacecraft or an earth base. Thus, the 
	 * instrument_host_name element can contain values which are 
	 * either spacecraft_name values or earth_base_name values.
	 */
	public static String INSTRUMENT_HOST_NAME="INSTRUMENT_HOST_NAME";
	/**
	 * DATA_QUALITY_ID The data_quality_id element provides a numeric 
	 * key which identifies the quality of data available for a 
	 * particular time period. 
	 * The data_quality_id scheme is unique to a given 
	 * instrument and is described by the associated 
	 * data_quality_desc element. 
	 */
	public static String DATA_QUALITY_ID="DATA_QUALITY_ID";
	/**
	 * MAXIMUM The maximum element indicates the largest value occurring 
	 * in a given instance of the data object. 
	 * Note: For PDS and Mars Observer applications -- because of 
	 * the unconventional data type of this data element, the 
	 * element should appear in labels only within an explicit 
	 * object, i.e. anywhere between an 'OBJECT =' and an 
	 * 'END_OBJECT'. 
	 */
	public static String MAXIMUM="MAXIMUM";
	/**
	 * CENTER_LATITUDE The center_latitude element provides a reference latitude 
	 * for certain map projections. For example, in an 
	 * Orthographic projection, the center_latitude along with the 
	 * center_longitude defines the point or tangency between the 
	 * sphere of the planet and the plane of the projection. The 
	 * map_scale (or map_resolution) is typically defined at the 
	 * center_latitude and center_longitude. In unprojected images, 
	 * center_latitude represents the latitude at the center of the 
	 * image frame.
	 */
	public static String CENTER_LATITUDE="CENTER_LATITUDE";
	/**
	 * The INSTRUMENT_TEMPERATURE element provides the temperature, in degrees 
	 * Celsius, of an instrument or some part of an instrument. 
	 * This keyword may be used in conjunction with 
	 * INSTRUMENT_TEMPERATURE_POINT to more fully describe either single or 
	 * multiple temperatures at various locations within a single instrument. If 
	 * there is more than one measurement taken for a given instrument, a multi- 
	 * value ordered set of values (i.e., sequence) may be constructed to 
	 * associate each temperature measurement in the INSTRUMENT_TEMPERATURE list 
	 * with a corresponding item in the INSTRUMENT_TEMPERATURE_POINT sequence of 
	 * values. 
	 */
	public static String INSTRUMENT_TEMPERATURE="INSTRUMENT_TEMPERATURE";
	/**
	 * MAP_PROJECTION_ROTATION The map_projection_rotation element provides the clockwise 
	 * rotation, in degrees, of the line and sample coordinates 
	 * with respect to the map projection origin 
	 * (line_projection_offset, line_projection_offset) This 
	 * parameter is used to indicate where 'up' is in the 
	 * projection. For example, in a polar stereographic 
	 * projection does the zero meridian go center to bottom, 
	 * center to top, center to left, or center to right? The 
	 * polar projection is defined such that the zero meridian 
	 * goes center to bottom. However, by rotating the map 
	 * projection, the zero meridian can go in any direction. 
	 * Note: 180 degrees is at the top of the North Pole and 0 
	 * degrees is at the top of the South Pole. For example, if 0 
	 * degrees is at the top of the North Pole than the 
	 * map_projection_rotation would be 180 degrees. 
	 */
	public static String MAP_PROJECTION_ROTATION="MAP_PROJECTION_ROTATION";
	/**
	 * RECORD_BYTES The record_bytes element indicates the number of bytes in a physical 
	 * file record, including record terminators and separators. When 
	 * RECORD_BYTES describes a file with RECORD_TYPE = STREAM 
	 * (e.g. a SPREADSHEET), its value is set to the length of the longest 
	 * record in the file. 
	 */
	public static String RECORD_BYTES="RECORD_BYTES";
	/**
	 * SIGNAL_CHAIN_ID The SIGNAL_CHAIN_ID element identifies the signal chain (electronic signal 
	 * path) number selected for charge-coupled device (CCD) output. 
	 * Note: For MARS EXPRESS the High-Resolution Stereo Colour Imager (HRSC) is 
	 * composed of 10 channels, each consisting of a charge-coupled device (CCD). 
	 * The data from these sensors are sent to the Data Processing Unit (DPU) via 
	 * 4 signal chains. One chain can be used for the Super Resolution Channel 
	 * (SRC), leaving 3 chains available for the other 9 HRSC sensors. 
	 */
	public static String SIGNAL_CHAIN_ID="SIGNAL_CHAIN_ID";
	/**
	 * CENTER_FILTER_WAVELENGTH The center_filter_wavelength element provides the mid_point 
	 * wavelength value between the minimum and maximum instrument 
	 * filter wavelength values. 
	 */
	public static String CENTER_FILTER_WAVELENGTH="CENTER_FILTER_WAVELENGTH";
	/**
	 * LSB_INTEGER signed integers stored in Least Significant Byte first (LSB) order.
	 */
	public static String LSB_INTEGER="LSB_INTEGER";
	/**
	 * PC_INTEGER Alias for LSB_INTEGER
	 */
	public static String PC_INTEGER="PC_INTEGER";
	/**
	 * VAX_INTEGER Alias for LSB_INTEGER
	 */
	public static String VAX_INTEGER="VAX_INTEGER";
	
	/**
	 * LSB_UNSIGNED_INTEGER unsigned integers stored in Least Significant Byte first (LSB) format.
	 */
	public static String LSB_UNSIGNED_INTEGER="LSB_UNSIGNED_INTEGER";
	/**
	 * PC_UNSIGNED_INTEGER Alias for LSB_UNSIGNED_INTEGER
	 */
	public static String PC_UNSIGNED_INTEGER="PC_UNSIGNED_INTEGER";
	/**
	 * VAX_UNSIGNED_INTEGER Alias for LSB_UNSIGNED_INTEGER
	 */
	public static String VAX_UNSIGNED_INTEGER="VAX_UNSIGNED_INTEGER";
	/**
	 * MSB_UNSIGNED_INTEGER unsigned integers stored in Most Significant Byte first (MSB) format.
	 */
	public static String MSB_UNSIGNED_INTEGER="MSB_UNSIGNED_INTEGER";
	/**
	 * SUN_UNSIGNED_INTEGER Alias for MSB_UNSIGNED_INTEGER
	 */
	public static String SUN_UNSIGNED_INTEGER="SUN_UNSIGNED_INTEGER";	
	/**
	 * MAC_UNSIGNED_INTEGER  Alias for MSB_UNSIGNED_INTEGER
	 */
	public static String MAC_UNSIGNED_INTEGER="MAC_UNSIGNED_INTEGER";	
	/**
	 * UNSIGNED_INTEGER Alias for MSB_UNSIGNED_INTEGER
	 */
	public static String UNSIGNED_INTEGER="UNSIGNED_INTEGER";
	/**
	 * MSB_INTEGER signed integers stored in Most Significant Byte first (MSB) order.
	 */
	public static String MSB_INTEGER="MSB_INTEGER";
	/**
	 * INTEGER Alias for MSB_INTEGER
	 */
	public static String INTEGER="INTEGER";
	/**
	 * MAC_INTEGER Alias for MSB_INTEGER
	 */
	public static String MAC_INTEGER="MAC_INTEGER";
	/**
	 * SUN_INTEGER Alias for MSB_INTEGER
	 */
	public static String SUN_INTEGER="SUN_INTEGER";
	/**
	 * PC_REAL  PC_REAL data type
	 */
	public static String PC_REAL="PC_REAL";
	/**
	 * IEEE_REAL IEEE format floating point numbers.
	 */
	public static String IEEE_REAL="IEEE_REAL";
	/**
	 * FLOAT Alias for IEEE_REAL
	 */
	public static String FLOAT="FLOAT";
	/**
	 * REAL Alias for IEEE_REAL
	 */
	public static String REAL="REAL";
	/**
	 * MAC_REAL Alias for IEEE_REAL
	 */
	public static String MAC_REAL="MAC_REAL";
	/**
	 * SUN_REAL Alias for IEEE_REAL
	 */
	public static String SUN_REAL="SUN_REAL";
	/**
	 * IEEE_COMPLEX IEEE complex numbers consist of two IEEE_REAL format numbers of the same precision, contiguous in memory. The first number represents the real part and the second the imaginary part of the complex value.
	 */
	public static String IEEE_COMPLEX="IEEE_COMPLEX";
	/**
	 * COMPLEX Alias for IEEE_COMPLEX
	 */
	public static String COMPLEX="COMPLEX";
	/**
	 * MAC_COMPLEX Alias for IEEE_COMPLEX
	 */
	public static String MAC_COMPLEX="MAC_COMPLEX";
	/**
	 * SUN_COMPLEX Alias for IEEE_COMPLEX
	 */
	public static String SUN_COMPLEX="SUN_COMPLEX";
	/**
	 * PC_COMPLEX PC complex numbers consist of two PC_REAL format numbers of the same precision, contiguous in memory. The first number represents the real part and the second the imaginary part of the complex value.
	 */
	public static String PC_COMPLEX="PC_COMPLEX";
	
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
		private static PDSImage readBinaryData(HashMap<String,String> properties,DataInputStream in,int offset) throws IOException{
			int height=0;
			int width=0;
			try{
				height = Integer.parseInt(properties.get("IMAGE."+"LINES"));
			}catch (Exception e){
				IOException ioe = new IOException("Could not read number of lines in PDS file:"+e.getMessage());
				ioe.initCause(e);
				throw ioe;
			}
			try{
				width = Integer.parseInt(properties.get("IMAGE."+"LINE_SAMPLES"));
			}catch (Exception e){
				IOException ioe = new IOException("Could not read number of samples per line in PDS file:"+e.getMessage());
				ioe.initCause(e);
				//f.close();
				throw ioe;
			}

			byte data[]=new byte[in.available()];
			in.read(data);
			int sample_bits=8;
			String sample_type=LSB_UNSIGNED_INTEGER;
			try{
				sample_bits = Integer.parseInt(properties.get("IMAGE."+"SAMPLE_BITS"));
			}catch (Exception e){
				sample_bits=8;
			}
			try{
				sample_type = properties.get("IMAGE."+"SAMPLE_TYPE");
			}catch (Exception e){
				sample_type=LSB_UNSIGNED_INTEGER;
			}

			PDSImage si = new PDSImage();
			
			if (sample_bits==8 && (sample_type.equals(LSB_UNSIGNED_INTEGER) || sample_type.equals(MSB_UNSIGNED_INTEGER) || sample_type.equals(UNSIGNED_INTEGER))){
				VegaLog.info("PDS image is LSB_UNSIGNED_INTEGER 8 bits");
				Int2d image2D=null;
				int os =0;
				if (offset>0){
					os=offset;
				}else{
					os = data.length-(height*width);
				}
				image2D=new Int2d(height,width);
				int locator=0;
		        for (int row = 0; row < height; row++) {
		            for (int column = 0; column < width; column++) {
		            	image2D.set(row,column,data[os+locator] & 0xFF);
		            	locator++;
		            }
		        }
		        si.setImage(image2D);
			}
			
			if (sample_bits==16 && (sample_type.equals(MSB_UNSIGNED_INTEGER) )){
				VegaLog.info("PDS image is MSB_UNSIGNED_INTEGER 8 bits");
				/*Short2d image2D=null;
				int os =0;
				if (offset>0){
					os=offset;
				}else{
					os = data.length-(height*width*2);
				}
				image2D=new Short2d(height,width);
				int locator=0;
		        for (int row = 0; row < height; row++) {
		            for (int column = 0; column < width*2; column=column+2) {
		            	short val=(short)( ((data[os+locator]&0xFF)<<8) | (data[os+locator+1]&0xFF) );
		            	image2D.set(row,column/2,val );
		            	locator=locator+2;
		            }
		        }
		        si.setImage(image2D);*/
				Int2d image2D=null;
				int os =0;
				if (offset>0){
					os=offset;
				}else{
					os = data.length-(height*width*2);
				}
				image2D=new Int2d(height,width);
				int locator=0;
		        for (int row = 0; row < height; row++) {
		            for (int column = 0; column < width*2; column=column+2) {
		            	int val = ((data[os+locator] & 0xff) << 8) | (data[os+locator+1] & 0xff);
		            	image2D.set(row,column/2,val );
		            	locator=locator+2;
		            }
		        }
		        si.setImage(image2D);
			}		
			

			if (sample_bits==16 && sample_type.equals(LSB_UNSIGNED_INTEGER)){
				VegaLog.info("PDS image is LSB_UNSIGNED_INTEGER 16 bits");
				Short2d image2D=null;
				int os =0;
				if (offset>0){
					os=offset;
				}else{
					os = data.length-(height*width*2);
				}
				image2D=new Short2d(height,width);
				int locator=0;
		        for (int row = 0; row < height; row++) {
		            for (int column = 0; column < width*2; column=column+2) {
		            	short val=(short)( ((data[os+locator+1]&0xFF)<<8) | (data[os+locator]&0xFF) );
		            	image2D.set(row,column/2,val );
		            	locator=locator+2;
		            }
		        }
		        si.setImage(image2D);
			}
			if (sample_bits==8 && (sample_type.equals(MSB_INTEGER) || sample_type.equals(INTEGER) || sample_type.equals(MAC_INTEGER) || sample_type.equals(SUN_INTEGER))){
				VegaLog.info("PDS image is MSB_INTEGER 8 bits");
				Int2d image2D=null;
				int os =0;
				if (offset>0){
					os=offset;
				}else{
					os = data.length-(height*width);
				}

				image2D=new Int2d(height,width);
				int locator=0;
		        for (int row = 0; row < height; row++) {
		            for (int column = 0; column < width; column++) {
		            	
		            	int val=data[os+locator] & 0xff;
		            	image2D.set(row,column,val );
		            	locator++;
		            }
		        }
		        si.setImage(image2D);
			}

			if (sample_bits==16 && (sample_type.equals(MSB_INTEGER) || sample_type.equals(INTEGER) || sample_type.equals(MAC_INTEGER) || sample_type.equals(SUN_INTEGER))){
				VegaLog.info("PDS image is MSB_INTEGER 16 bits");
				Int2d image2D=null;
				int os =0;
				if (offset>0){
					os=offset;
				}else{
					os = data.length-(height*width*2);
				}
				image2D=new Int2d(height,width);
				int locator=0;
		        for (int row = 0; row < height; row++) {
		            for (int column = 0; column < width*2; column=column+2) {
		            	int val = ((data[os+locator] & 0xff) << 8) | (data[os+locator+1] & 0xff);
		            	image2D.set(row,column/2,val );
		            	locator=locator+2;
		            }
		        }
		        si.setImage(image2D);
			}

			if (sample_bits==32 && sample_type.equals(LSB_UNSIGNED_INTEGER)){
				VegaLog.info("PDS image is LSB_UNSIGNED_INTEGER 32 bits");
				Int2d image2D=null;
				int os =0;
				if (offset>0){
					os=offset;
				}else{
					os = data.length-(height*width*4);
				}
				image2D=new Int2d(height,width);
				int locator=0;
		        for (int row = 0; row < height; row++) {
		            for (int column = 0; column < width*4; column=column+4) {
		            	int val=(int)( ((data[os+locator+3]&0xFF)<<24) | ((data[os+locator+2]&0xFF)<<16) | ((data[os+locator+1]&0xFF)<<8) | (data[os+locator]&0xFF) );
		            	image2D.set(row,column/4,val );
		            	locator=locator+4;
		            }
		            	
		        }
		        si.setImage(image2D);
			}
			
			if (sample_bits==32 && sample_type.equals(PC_REAL)){
				VegaLog.info("PDS image is PC_REAL 32 bits");
				Float2d image2D=null;
				int os =0;
				if (offset>0){
					os=offset;
				}else{
					os = data.length-(height*width*4);
				}
				image2D=new Float2d(height,width);
				int locator=0;
		        for (int row = 0; row < height; row++) {
		            for (int column = 0; column < width*4; column=column+4) {
		            	byte[] pcReal=new byte[4];
		            	pcReal[3]=data[os+locator];
		            	pcReal[2]=data[os+locator+1];
		            	pcReal[1]=data[os+locator+2];
		            	pcReal[0]=data[os+locator+3];
		            	ByteBuffer buffer = ByteBuffer.wrap(pcReal);
		            	float f = buffer.getFloat();
		            	image2D.set(row,column/4,f );
		            	locator=locator+4;
		            }
		            	
		        }
		        si.setImage(image2D);
			}
			

	        return si;
			
		}
		public static PDSImage readLBLFile(String file) throws IOException{
			BufferedReader f;
			
			f = new BufferedReader(new FileReader(file));
			HashMap<String, String> properties=null;
			HashMap<String, String> units=null;
			try{
				PDSInputFile pdsFile = new PDSInputFile(file);
				Document doc = pdsFile.getPDSDocument();
				
				properties=new HashMap<String, String>();
				units=new HashMap<String, String>();
				listValues(doc,"","",properties,units);
			}catch (Exception e){
				e.printStackTrace();
			}
			f.close();
			File tf=new File(file);
			String path = tf.getParent();
			String imageProperty=properties.get(IMAGE);
			if (imageProperty==null) imageProperty=properties.get("FILE_NAME");
			System.out.println(properties.get(imageProperty));
			String imgFile = imageProperty.replace("(", "").replace(")", "").split(",")[0].trim();

			FileInputStream fin = new FileInputStream(path+"/"+imgFile);
			DataInputStream in = new DataInputStream(fin);
			PDSImage result = readBinaryData(properties,in,-1);
			String creationTime=properties.get("PRODUCT_CREATION_TIME");
			try{
				if (creationTime!=null){
					result.setCreationDate(new FineTime(DateUtil.parse(creationTime)));
					properties.remove("PRODUCT_CREATION_TIME");
				}
				
			}catch (Exception e){
				e.printStackTrace();
			}
			String startTime=properties.get("START_TIME");
			try{
				if (startTime!=null){
					
					result.setStartDate(new FineTime(DateUtil.parse(startTime)));
					properties.remove("START_TIME");
				}
				
			}catch (Exception e){
				e.printStackTrace();
			}
			String stopTime=properties.get("STOP_TIME");
			try{
				if (stopTime!=null){
					result.setEndDate(new FineTime(DateUtil.parse(stopTime)));
					properties.remove("STOP_TIME");
				}
				
			}catch (Exception e){
				e.printStackTrace();
			}
			
			String instrument=properties.get("INSTRUMENT_ID");
			if (instrument!=null){
				result.setInstrument(instrument);
				properties.remove("INSTRUMENT_ID");
			}
			result.setType("PDSImage");
			result.setCreator("Vega PDS Hipe plugin");
			Iterator<String> it2 = properties.keySet().iterator();
			while (it2.hasNext()){
				String name=it2.next();
				String val=properties.get(name);
				String unit="";
				unit=units.get(name);
				//String nValue="";
				try{
					if (unit!=null){

						DoubleParameter param = new DoubleParameter(Double.parseDouble(val));
	
						try{
							param.setUnit(herschel.share.unit.Unit.parse(unit));
						
							result.getMeta().set(name, param);
						}catch (Exception e){
							e.printStackTrace();
							result.getMeta().set(name, new StringParameter(val));
						}
					}
					else{
						result.getMeta().set(name, new StringParameter(val));
					}
				}catch (Exception e){
					if (unit!=null){
						val=val+"["+unit+"]";
						
					}
					result.getMeta().set(name, new StringParameter(val));
				}
			}
			fin.close();
			return result;

	
		}
		private static HashMap<String,String> readAsciiData(BufferedReader f) throws IOException{
			String line="";
			String keyword="";
			String value="";

			HashMap<String,String> properties=new HashMap<String,String>();
			String currentObject="";
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
				if (keyword.length()>0 && value.length()>0){
					if (keyword.equals("OBJECT")){
						currentObject=value+".";
					}else{
						properties.put(currentObject+keyword, value);
					}
				}
			}
			
			return properties;
		}
		private static void listValues(Node node,String tabs,String name,HashMap<String,String> properties,HashMap<String,String> units){
			if (node.getNodeName().equals("OBJECT")){
				name=name+node.getAttributes().getNamedItem("name").getNodeValue()+".";
			}
			String key =null; 
			String value=null;
			String unit=null;
			NamedNodeMap att = node.getAttributes();
			if (att!=null){
				for (int i=0;i<att.getLength();i++){
					if (att.item(i).getNodeName().equals("key")) key=att.item(i).getNodeValue();
					if (att.item(i).getNodeName().equals("units")) unit=att.item(i).getNodeValue();
				}	
			}
			NodeList nl = node.getChildNodes();
			for (int i=0;i<nl.getLength();i++){
				if (nl.item(i).getNodeName().equals("#text")) value=nl.item(i).getNodeValue();
				if (nl.item(i).getNodeName().equals("units")) unit=nl.item(i).getNodeValue();
				listValues(nl.item(i),tabs+"\t",name,properties,units);
			}
			if (key!=null && value!=null){
				String prev = properties.get(name+key);
				if (prev!=null){
					properties.put(name+key, prev+","+value);
				}else{
					properties.put(name+key, value);
					System.out.println(name+key+"="+value);
				}
			}
			if (key!=null && unit!=null){
				units.put(name+key, unit);
			}
		}
		
		public static PDSImage readImgFile(String file) throws IOException{
			try{
				return readPdsFile(file);
			}catch (Exception e){
				String filename = file.replace(".IMG", ".LBL");
				try{
					return readLBLFile(filename);
				}catch (Exception e2){
					throw e;
				}
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
			FileInputStream fin ;
			HashMap<String, String> properties=null;
			HashMap<String, String> units=null;
			int os=-1;
			try{
				PDSInputFile pdsFile = new PDSInputFile(file);
				Document doc = pdsFile.getPDSDocument();
				
				properties=new HashMap<String, String>();
				units=new HashMap<String, String>();
				listValues(doc,"","",properties,units);
				os = pdsFile.getImageStart(doc);
			}catch (Exception e){
				os=-1;
			}

			fin = new FileInputStream(file);
			DataInputStream in = new DataInputStream(fin);
			PDSImage result = readBinaryData(properties,in,os);
			String creationTime=properties.get("PRODUCT_CREATION_TIME");
			try{
				if (creationTime!=null){
					result.setCreationDate(new FineTime(DateUtil.zuluToDate(creationTime)));
					properties.remove("PRODUCT_CREATION_TIME");
				}
				
			}catch (Exception e){
				e.printStackTrace();
			}
			String startTime=properties.get("START_TIME");
			try{
				if (startTime!=null){
					result.setStartDate(new FineTime(DateUtil.zuluToDate(startTime)));
					properties.remove("START_TIME");
				}
				
			}catch (Exception e){
				e.printStackTrace();
			}
			String stopTime=properties.get("STOP_TIME");
			try{
				if (stopTime!=null){
					result.setEndDate(new FineTime(DateUtil.zuluToDate(stopTime)));
					properties.remove("STOP_TIME");
				}
				
			}catch (Exception e){
				e.printStackTrace();
			}
			
			String instrument=properties.get("INSTRUMENT_ID");
			if (instrument!=null){
				result.setInstrument(instrument);
				properties.remove("INSTRUMENT_ID");
			}
			result.setType("PDSImage");
			result.setCreator("Vega PDS Hipe plugin");
			Iterator<String> it2 = properties.keySet().iterator();
			while (it2.hasNext()){
				String name=it2.next();
				String val=properties.get(name);
				String unit="";
				unit=units.get(name);
				try{
					if (unit!=null){

						DoubleParameter param = new DoubleParameter(Double.parseDouble(val));
	
						try{
							param.setUnit(herschel.share.unit.Unit.parse(unit));
						
							result.getMeta().set(name, param);
						}catch (Exception e){
							e.printStackTrace();
							result.getMeta().set(name, new StringParameter(val));
						}
					}
					else{
						result.getMeta().set(name, new StringParameter(val));
					}
				}catch (Exception e){
					if (unit!=null){
						val=val+"["+unit+"]";
						
					}
					result.getMeta().set(name, new StringParameter(val));
				}
			}
			fin.close();
			return result;


			

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
