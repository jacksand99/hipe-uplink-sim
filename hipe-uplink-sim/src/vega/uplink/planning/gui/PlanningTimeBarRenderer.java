package vega.uplink.planning.gui;

import herschel.share.interpreter.InterpreterUtil;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JList;

import vega.uplink.Properties;
import de.jaret.util.date.Interval;
import de.jaret.util.ui.timebars.TimeBarViewerDelegate;
import de.jaret.util.ui.timebars.swing.TimeBarViewer;
import de.jaret.util.ui.timebars.swing.renderer.DefaultTimeBarRenderer;
import de.jaret.util.ui.timebars.swing.renderer.TimeBarRenderer;
import vega.uplink.planning.Observation;

public class PlanningTimeBarRenderer implements TimeBarRenderer {
	JList<Observation> list;
	public PlanningTimeBarRenderer(JList<Observation> list){
		this.list=list;
	}
    /** component used for rendering. */
	protected JButton _component;
	//= new JButton();

      public JComponent getTimeBarRendererComponent(TimeBarViewer tbv, Interval value, boolean isSelected, boolean overlapping) {
         return defaultGetTimeBarRendererComponent(tbv, value, isSelected, overlapping);
      }
  
      /**       * {@inheritDoc}
       */
      public JComponent defaultGetTimeBarRendererComponent(TimeBarViewer tbv, Interval value, boolean isSelected,
              boolean overlapping) {
    	  _component=new JButton();
    	  if (InterpreterUtil.isInstance(ObservationInterval.class, value)){
    		  if (isSelected) {
    			  list.setSelectedValue(((ObservationInterval)value).getObservation(), true);
        			  _component.setBackground(Color.BLUE);
        		  
                } else{
           		  try{
        			  String ins = ((ObservationInterval) value).getObservation().getInstrument();
        			  java.awt.Color colIns=Properties.getColor(Properties.SUBINSTRUMENT_COLOR_PROPERTY_PREFIX+ins);
        			  _component.setBackground(colIns);
        		  }catch (Exception e){
        			  _component.setBackground(Color.LIGHT_GRAY);
        		  }
                }
    	  }
    	  if (InterpreterUtil.isInstance(CommandingInterval.class, value)){
    		  String ins = ((CommandingInterval) value).getInstrument();
    		  java.awt.Color colIns=Properties.getColor(Properties.SUBINSTRUMENT_COLOR_PROPERTY_PREFIX+ins);
    		  _component.setBackground(colIns);
    	  }
    	  if (InterpreterUtil.isInstance(BlockInterval.class, value)){
    		  String ins = ((BlockInterval) value).getInstrument();
    		  try{
    			  java.awt.Color colIns=Properties.getColor(Properties.SUBINSTRUMENT_COLOR_PROPERTY_PREFIX+ins);
    			  _component.setBackground(colIns);
    		  }catch (Exception e){
    			  
    		  }
    		  
    	  }
          _component.setText(value.toString());
          _component.setToolTipText(value.toString());
          return _component;
      }
  
      /**
       * {@inheritDoc} Simple default implementation.
       */
  	public Rectangle getPreferredDrawingBounds(Rectangle intervalDrawingArea,
  			TimeBarViewerDelegate delegate, Interval interval,
  			boolean selected, boolean overlap) {
  		return intervalDrawingArea;
  	}

}
