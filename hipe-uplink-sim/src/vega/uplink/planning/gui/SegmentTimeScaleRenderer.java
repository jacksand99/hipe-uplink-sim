package vega.uplink.planning.gui;

import java.util.Date;
import java.util.Vector;

import de.jaret.util.date.JaretDate;
import de.jaret.util.ui.timebars.TimeBarViewerDelegate;
import de.jaret.util.ui.timebars.swing.renderer.DefaultTimeScaleRenderer;

//import java.util.HashSet;
public class SegmentTimeScaleRenderer extends DefaultTimeScaleRenderer{
	public java.util.List<de.jaret.util.date.JaretDate> getMajorTicks(TimeBarViewerDelegate delegate){
		Vector<de.jaret.util.date.JaretDate> result=new Vector<de.jaret.util.date.JaretDate>();
		long minDate = delegate.getMinDate().getDate().getTime();
		long maxDate = delegate.getMaxDate().getDate().getTime();
		for (long i=minDate;i<maxDate;i=i+3600000){
			result.add(new JaretDate(new Date(i)));
		}
		return result;
	}
}
