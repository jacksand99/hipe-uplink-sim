package vega.uplink.planning.gui;

/*
 *  File: JaretDateFormatter.java 
 *  Copyright (c) 2004-2007  Peter Kliem (Peter.Kliem@jaret.de)
 *  A commercial license is available, see http://www.jaret.de.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */


import java.text.DateFormat;
import java.text.DateFormatSymbols;
//import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
//import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Logger;

import vega.uplink.planning.Schedule;
import vega.uplink.pointing.PointingBlock;
import de.jaret.util.date.JaretDate;
import de.jaret.util.date.JaretDateFormatter;
import de.jaret.util.date.iterator.DateIterator.Format;
import de.jaret.util.date.iterator.IIteratorFormatter;
//import de.jaret.util.misc.FormatHelper;
import de.jaret.util.misc.FormatHelper;

/**
 * Delegate for the JaretDate class supplying convenient methods for generating formatted, i8ned output.
 * 
 * @author Peter Kliem
 * @version $Id: JaretDateFormatter.java 243 2007-02-11 22:08:49Z olk $
 */
public class DateFormatter extends JaretDateFormatter implements IIteratorFormatter{
	//private final Logger LOG = Logger.getLogger(DateFormatter.class.getName());
    Locale _locale;
    private DateFormat _df;
    private DateFormat _df2;
    private DateFormatSymbols _dateFormatSymbols;
    private SimpleDateFormat _dfTimeSeconds;
    private SimpleDateFormat _dfTimeNoSeconds;

    /**
     * Construcor specifying a locale.
     * 
     * @param locale locale to be used
     */
    public DateFormatter(Locale locale) {
        setLocale(locale);
    }

    /**
     * Default constructor unsing default locale.
     */
    public DateFormatter() {
        this(Locale.getDefault());
    }

    /**
     * Retrieve the used locale.
     * 
     * @return the locale set for the JaretDateFormatter
     */
    public Locale getLocale() {
        return _locale;
    }

    /**
     * Set the locale for this JaretDateFormatter.
     * 
     * @param locale Locale to be used
     */
    public void setLocale(Locale locale) {
        _locale = locale;
        _df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, _locale);
        _df.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
        _df2 = DateFormat.getDateInstance(DateFormat.SHORT, _locale);
        _df2.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
        _df.setLenient(false);
        _df2.setLenient(false);
    }

    public Date parseTextualDate(String text) {
        Date date = null;
        try {
            date = _df2.parse(text);
        } catch (ParseException e) {
            // ignore
        }
        return date;
    }

    /**
     * {@inheritDoc}
     */
    public boolean equals(Object obj) {
        JaretDateFormatter jdf = (JaretDateFormatter) obj;
        // to JaretDateFormatters are equal if they operate on the same locale
        return jdf.getLocale().equals(_locale);
    }

    /**
     * {@inheritDoc}
     */
    public int hashCode() {
        // hash code is the hash code of the loale since this is the base for the iomplementation of the
        // equals method
        return _locale.hashCode();
    }

    /**
     * Generate a textual representation of the given JaretDate.
     * 
     * @param date the JaretDate to be formatted
     * @return JaretDate to format
     */
    public String generateDisplayString(JaretDate date) {
    	//LOG.info(PointingBlock.dateToZulu(date.getDate())+" "+_df.format(date.getDate()));
        return _df.format(date.getDate());
    }

    /**
     * Generate a textual representation of the given JaretDate.
     * 
     * @param date the JaretDate to be formatted
     * @return textual represantation of the date (day)
     */
    public String generateDisplayStringDate(JaretDate date) {
    	//LOG.info(PointingBlock.dateToZulu(date.getDate())+" "+_df2.format(date.getDate()));
    	
        return _df2.format(date.getDate());
    }

    /**
     * Generate an output of the format hh:mm:ss for a given number of seconds.
     * 
     * @param sec Seconds
     * @return textual representation
     */
    public static String secondsToDisplayString(int sec) {
        int hours = sec / 3600;
        int minutes = (sec % 3600) / 60;
        int seconds = (sec % 60);
        String str = FormatHelper.NFInt2Digits().format(hours) + ":" + FormatHelper.NFInt2Digits().format(minutes)
                + ":" + FormatHelper.NFInt2Digits().format(seconds);
        return str;
    }

    /**
     * Retrieve a localized name for the day of the week.
     * 
     * @param dayOfWeek
     * @return the localized name of the weekday
     */
    public String getDayOfWeekString(int dayOfWeek) {
        if (_dateFormatSymbols == null) {
            _dateFormatSymbols = new DateFormatSymbols(_locale);
        }
        return _dateFormatSymbols.getWeekdays()[dayOfWeek];
    }

    public String getMonthString(int month) {
        if (_dateFormatSymbols == null) {
            _dateFormatSymbols = new DateFormatSymbols(_locale);
        }
        return _dateFormatSymbols.getMonths()[month];
    }

    public String getShortDayOfWeekString(int dayOfWeek) {
        if (_dateFormatSymbols == null) {
            _dateFormatSymbols = new DateFormatSymbols(_locale);
        }
        return _dateFormatSymbols.getShortWeekdays()[dayOfWeek];
    }

    public String getShortMonthString(int month) {
        if (_dateFormatSymbols == null) {
            _dateFormatSymbols = new DateFormatSymbols(_locale);
        }
        return _dateFormatSymbols.getShortMonths()[month];
    }

    /**
     * Generates a textual representation of the time only.
     * 
     * @param date JaretDate to be represenetd
     * @param seconds if true seconds will be included (hh:mm:ss), hh:mm otherwise
     * @return a Textual represenation of the time
     */
    public String toDisplayStringTime(JaretDate date, boolean seconds) {
    	
        if (_dfTimeSeconds == null) {
            _dfTimeSeconds = new SimpleDateFormat("HH:mm:ss", _locale);
            _dfTimeSeconds.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
            _dfTimeNoSeconds = new SimpleDateFormat("HH:mm", _locale);
            _dfTimeNoSeconds.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
        }
        if (seconds) {
        	//LOG.info(PointingBlock.dateToZulu(date.getDate())+" "+_dfTimeSeconds.format(date.getDate()));

            return _dfTimeSeconds.format(date.getDate());
        } else {
            return _dfTimeNoSeconds.format(date.getDate());
        }
    }

	@Override
	public String getLabel(JaretDate arg0, Format arg1) {
		// TODO Auto-generated method stub
		return PointingBlock.dateToZulu(arg0.getDate());
	}








}
