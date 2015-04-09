package vega.uplink;

import herschel.share.fltdyn.time.FineTime;
import herschel.share.fltdyn.time.SimpleTimeFormat;
import herschel.share.fltdyn.time.TimeScale;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Class that provides a number of utility methods to transform strings from and into dates and finetimes
 * from a number of formats used in operational files.
 * @author jarenas
 *
 */
public class DateUtil {
	
	public static String DEFAULT_DATE_FORMAT_PROPERTY="vega.uplink.defaultDateFormat";
	public static String ZULU="yyyy-MM-ddTHH:mm:ss";
	public static String DOY="yyyy-DTHH:mm:ss.SSS";
	public static String LITERAL="dd-MMMMMMMMM-yyyy'_'HH:mm:ss";
	/**
	 * Try to parse a date expressed as String trying consecutive formats.
	 * @param date the date expressed as string
	 * @return the date
	 * @throws ParseException if could not find any method to parse the string
	 */
	public static Date parse(String date) throws ParseException {
		try {
			return zuluToDate(date);
		} catch (ParseException e) {
			try {
				return DOYToDate(date);
			} catch (ParseException e1) {
				
					try {
						return literalToDate(date);
					} catch (ParseException e2) {
						try {
							return literalToDateNoTime(date);
						} catch (ParseException e3) {
							try {
								return zuluToDateSlash(date);
							} catch (ParseException e4) {
								try {
									return zuluToDateSlashSpace(date);
								} catch (ParseException e5) {
									try {
										return TDBToDate(date);
									} catch (ParseException e6) {
										ParseException pe = new ParseException("Could not parse "+date,0);
										throw(pe);
									}
								}
							}
						}
					}
				
			}
		}
	}
	/**
	 * Transform a date into a string of the format yyyy-MM-ddTHH:mm:ss
	 * @param date date to be transformed
	 * @return a string representation of the date
	 */
	public static String dateToZulu(java.util.Date date){
		java.text.SimpleDateFormat dateFormat=new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		dateFormat.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
		return dateFormat.format(date);
	}
	/**
	 * Transform a FineTime into a string of the format yyyy-MM-ddTHH:mm:ss
	 * @param date date to be transformed
	 * @return a string representation of the date
	 */
	public static String fineTimeToZulu(FineTime date){
		return dateToZulu(date.toDate());
	}
	
	/**
	 * Transform a string of the format yyyy-MM-ddTHH:mm:ss into a date
	 * @param zuluTime the string to be read
	 * @return the date represented by the string
	 * @throws ParseException if string is malformed
	 */
	public static java.util.Date zuluToDate(String zuluTime) throws ParseException{
		if (zuluTime==null) throw new IllegalArgumentException("Can not parse a null value");
		java.text.SimpleDateFormat dateFormat=new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		dateFormat.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
		return dateFormat.parse(zuluTime.trim().replaceAll("\n", ""));
	}
	/**
	 * Transform a string of the format yyyy-MM-ddTHH:mm:ss into a FineTime
	 * @param zuluTime the string to be read
	 * @return the date represented by the string
	 * @throws ParseException if string is malformed
	 */
	public static FineTime zuluToFineTime(String zuluTime) throws ParseException{
		return new FineTime(zuluToDate(zuluTime));
	}

	/**
	 * Transform a date into a string of the format yyyy/MM/ddTHH:mm:ss.SSS
	 * @param date date to be transformed
	 * @return a string representation of the date
	 */
	public static String dateToZuluSlash(java.util.Date date){
		java.text.SimpleDateFormat dateFormat=new java.text.SimpleDateFormat("yyyy/MM/dd'T'HH:mm:ss.SSS");
		dateFormat.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
		return dateFormat.format(date);
	}
	/**
	 * Transform a FineTime into a string of the format yyyy/MM/ddTHH:mm:ss.SSS
	 * @param date date to be transformed
	 * @return a string representation of the date
	 */
	public static String fineTimeToZuluSlash(FineTime date){
		return dateToZuluSlash(date.toDate());
	}
	
	/**
	 * Transform a string of the format yyyy/MM/ddTHH:mm:ss.SSS into a date
	 * @param zuluTime the string to be read
	 * @return the date represented by the string
	 * @throws ParseException if string is malformed
	 */
	public static java.util.Date zuluToDateSlash(String zuluTime) throws ParseException{
		java.text.SimpleDateFormat dateFormat=new java.text.SimpleDateFormat("yyyy/MM/dd'T'HH:mm:ss.SSS");
		dateFormat.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
		return dateFormat.parse(zuluTime.trim().replaceAll("\n", ""));
	}
	/**
	 * Transform a string of the format yyyy/MM/ddTHH:mm:ss.SSS into a FineTime
	 * @param zuluTime the string to be read
	 * @return the date represented by the string
	 * @throws ParseException if string is malformed
	 */
	public static FineTime zuluToFineTimeSlash(String zuluTime) throws ParseException{
		return new FineTime(zuluToDateSlash(zuluTime));
	}

	/**
	 * Transform a date into a string of the format yyyy/MM/dd HH:mm:ss.SSS
	 * @param date date to be transformed
	 * @return a string representation of the date
	 */
	public static String dateToZuluSlashSpace(java.util.Date date){
		java.text.SimpleDateFormat dateFormat=new java.text.SimpleDateFormat("yyyy/MM/dd' 'HH:mm:ss.SSS");
		dateFormat.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
		return dateFormat.format(date);
	}
	/**
	 * Transform a FineTime into a string of the format yyyy/MM/dd HH:mm:ss.SSS
	 * @param date date to be transformed
	 * @return a string representation of the date
	 */
	public static String fineTimeToZuluSlashSpace(FineTime date){
		return dateToZuluSlashSpace(date.toDate());
	}
	
	/**
	 * Transform a string of the format yyyy/MM/dd HH:mm:ss.SSS into a date
	 * @param zuluTime the string to be read
	 * @return the date represented by the string
	 * @throws ParseException if string is malformed
	 */
	public static java.util.Date zuluToDateSlashSpace(String zuluTime) throws ParseException{
		java.text.SimpleDateFormat dateFormat=new java.text.SimpleDateFormat("yyyy/MM/dd' 'HH:mm:ss.SSS");
		dateFormat.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
		return dateFormat.parse(zuluTime.trim().replaceAll("\n", ""));
	}	
	/**
	 * Transform a string of the format yyyy/MM/dd HH:mm:ss.SSS into a FineTime
	 * @param zuluTime the string to be read
	 * @return the date represented by the string
	 * @throws ParseException if string is malformed
	 */
	public static FineTime zuluToFineTimeSlashSpace(String zuluTime) throws ParseException{
		return new FineTime(zuluToDateSlashSpace(zuluTime));
	}	
	
	
	/**
	 * Transform a string of the format yyyy-DTHH:mm:ss into a date
	 * @param zuluTime the string to be read
	 * @return the date represented by the string
	 * @throws ParseException if string is malformed
	 */
	public static java.util.Date DOYToDate(String zuluTime) throws ParseException {
		java.text.SimpleDateFormat dateFormat=new java.text.SimpleDateFormat("yyyy-D'T'HH:mm:ss.SSS'Z'");
		java.text.SimpleDateFormat dateFormat3=new java.text.SimpleDateFormat("yyyy-D'T'HH:mm:ss");
		dateFormat.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
		dateFormat3.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
		TimeZone tz = java.util.TimeZone.getTimeZone("UTC");
		dateFormat.setTimeZone(tz);
		try {
			Date result;
			try{
				result = dateFormat.parse(zuluTime);
			}catch(Exception e2){
				result = dateFormat3.parse(zuluTime);
			}
			GregorianCalendar c=new GregorianCalendar(tz);
			c.setTime(result);
			int year = c.get(Calendar.YEAR);
			//System.out.println(year);
			if (year<100){
				//System.out.println("Could not read "+zuluTime+" adding 20. Year is "+result.getYear());
				result=DateUtil.DOYToDate("20"+zuluTime);
			}
			return result;
		} catch (ParseException e) {
			java.text.SimpleDateFormat dateFormat2=new java.text.SimpleDateFormat("yyyy-D'T'HH:mm:ss'Z'");
			dateFormat2.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
			
			// TODO Auto-generated catch block
			//e.printStackTrace();
			Date result = dateFormat2.parse(zuluTime);
			
			GregorianCalendar c=new GregorianCalendar(tz);
			c.setTime(result);
			int year = c.get(Calendar.YEAR);
			//System.out.println(year);
			if (year<100){
				//System.out.println("Could not read "+zuluTime+" adding 20. Year is "+result.getYear());
				result=DateUtil.DOYToDate("20"+zuluTime);
			}
			return result;

		}
	}
	/**
	 * Transform a string of the format yyyy-DTHH:mm:ss into a FineTime
	 * @param zuluTime the string to be read
	 * @return the date represented by the string
	 * @throws ParseException if string is malformed
	 */
	public static FineTime DOYToFineTime(String zuluTime) throws ParseException{

		return new FineTime(DOYToDate(zuluTime));
	}
	
	
	/**
	 * Transform a date into a string of the format yyyy-DTHH:mm:ss
	 * @param date date to be transformed
	 * @return a string representation of the date
	 */
	public static String dateToDOY(java.util.Date date){
		java.text.SimpleDateFormat dateFormat=new java.text.SimpleDateFormat("yyyy-D'T'HH:mm:ss.SSS'Z'");
		dateFormat.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
		return dateFormat.format(date);
	}
	/**
	 * Transform a FineTime into a string of the format yyyy-DTHH:mm:ss
	 * @param date date to be transformed
	 * @return a string representation of the date
	 */
	public static String fineTimeToDOY(FineTime date){
		return dateToDOY(date.toDate());
	}

	/**
	 * Transform a string of the format dd-MMMMMMMMM-yyyy_HH:mm:ss into a date
	 * @param literal the string to be read
	 * @return the date represented by the string
	 * @throws ParseException if string is malformed
	 */	
	public static Date literalToDate(String literal) throws ParseException{
		java.text.SimpleDateFormat dateFormat2 = new java.text.SimpleDateFormat("dd-MMMMMMMMM-yyyy'_'HH:mm:ss");
		dateFormat2.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
		return dateFormat2.parse(literal);

	}
	/**
	 * Transform a string of the format dd-MMMMMMMMM-yyyy_HH:mm:ss into a FineTime
	 * @param literal the string to be read
	 * @return the date represented by the string
	 * @throws ParseException if string is malformed
	 */	
	public static FineTime literalToFineTime(String literal) throws ParseException{
		return new FineTime(literalToDate(literal));
	}

	/**
	 * Transform a date into a string of the format dd-MMMMMMMMM-yyyy_HH:mm:ss
	 * @param date date to be transformed
	 * @return a string representation of the date
	 */	
	public static String dateToLiteral(Date date){
		java.text.SimpleDateFormat dateFormat2 = new java.text.SimpleDateFormat("dd-MMMMMMMMM-yyyy'_'HH:mm:ss");
		dateFormat2.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
		return dateFormat2.format(date);
		
	}
	/**
	 * Transform a FineTime into a string of the format dd-MMMMMMMMM-yyyy_HH:mm:ss
	 * @param date date to be transformed
	 * @return a string representation of the date
	 */	
	public static String fineTimeTiLiteral(FineTime date){
		return dateToLiteral(date.toDate());
		
	}
	/**
	 * Transform a string of the format dd-MMMMMMMMM-yyyy into a date
	 * @param literal the string to be read
	 * @return the date represented by the string
	 * @throws ParseException if string is malformed
	 */	
	public static Date literalToDateNoTime(String literal) throws ParseException{
		java.text.SimpleDateFormat dateFormat2 = new java.text.SimpleDateFormat("dd-MMMMMMMMM-yyyy");
		dateFormat2.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
		return dateFormat2.parse(literal);

	}
	/**
	 * Transform a string of the format dd-MMMMMMMMM-yyyy into a FineTime
	 * @param literal the string to be read
	 * @return the date represented by the string
	 * @throws ParseException if string is malformed
	 */	
	public static FineTime literalToFineTimeNoTime(String literal) throws ParseException{
		return new FineTime(literalToDateNoTime(literal));
	}

	/**
	 * Transform a date into a string of the format dd-MMMMMMMMM-yyyy
	 * @param date date to be transformed
	 * @return a string representation of the date
	 */	
	public static String dateToLiteralNoTime(Date date){
		java.text.SimpleDateFormat dateFormat2 = new java.text.SimpleDateFormat("dd-MMMMMMMMM-yyyy");
		dateFormat2.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
		return dateFormat2.format(date);
		
	}
	/**
	 * Transform a FineTime into a string of the format dd-MMMMMMMMM-yyyy
	 * @param date date to be transformed
	 * @return a string representation of the date
	 */	
	public static String fineTimeTiLiteralNoTime(FineTime date){
		return dateToLiteralNoTime(date.toDate());
		
	}

	/**
	 * Transform a string on the TDB scale into a date
	 * @param tdbString the string to be read
	 * @return the date represented by the string
	 * @throws ParseException 
	 */	
		public static Date TDBToDate(String tdbString) throws ParseException{
			return TDBToFineTime(tdbString).toDate();
		}
		/**
		 * Transform a string on the TDB scale into a FineTime
		 * @param tdbString the string to be read
		 * @return the date represented by the string
		 * @throws ParseException 
		 */	
			public static FineTime TDBToFineTime(String tdbString) throws ParseException{
			SimpleTimeFormat tdb = new SimpleTimeFormat(TimeScale.TDB);
			tdb.setDecimals(6);
			try{
				FineTime ftime = tdb.parse(tdbString+" TDB");
				return ftime;
			}catch (Exception e){
				ParseException pe = new ParseException(e.getMessage(), 0);
				pe.initCause(e);
				throw(pe);
				
			}
			
			
		}

	/**
	 * Transform a date into a string on the TDB scale
	 * @param date date to be transformed
	 * @return a string representation of the date
	 */		
	public static String dateToTDB(Date date){
		return fineTimeToTDB(new FineTime(date));
	}
	/**
	 * Transform a FineTime into a string on the TDB scale
	 * @param date date to be transformed
	 * @return a string representation of the date
	 */		
	public static String fineTimeToTDB(FineTime date){
		SimpleTimeFormat tdb = new SimpleTimeFormat(TimeScale.TDB);
		tdb.setDecimals(6);
		return tdb.format(date);
	}
	
	public static String defaultDateToString(Date date){
		try{
			String configured = Properties.getProperty(DateUtil.DEFAULT_DATE_FORMAT_PROPERTY);
			if (configured.equals(DateUtil.DOY)) return dateToDOY(date);
			if (configured.equals(DateUtil.LITERAL)) return dateToLiteral(date);
			return dateToZulu(date);
		}catch (Exception e){
			return dateToZulu(date);
		}
	}
	

}
