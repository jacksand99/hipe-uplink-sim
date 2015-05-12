package vega.uplink.planning.gui;

import herschel.ia.gui.kernel.Selection;
import herschel.ia.gui.kernel.SelectionVisitor;
import herschel.ia.gui.kernel.VariableSelection;
import herschel.ia.gui.kernel.dnd.DnDSelection;
import herschel.ia.gui.kernel.dnd.DragSelection;
import herschel.ia.gui.kernel.dnd.DropSelection;
import herschel.ia.gui.kernel.dnd.SelectionTransferHandler;
import herschel.share.interpreter.InterpreterUtil;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JTextArea;

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
	protected JComponent _component;
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
    		  _component=new DraggableComponent(((ObservationInterval)value).getObservation());
    		  //_component.
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
    		  ((DraggableComponent) _component).setText(value.toString());
    	  }
    	  if (InterpreterUtil.isInstance(CommandingInterval.class, value)){
    		  String ins = ((CommandingInterval) value).getInstrument();
    		  java.awt.Color colIns=Properties.getColor(Properties.SUBINSTRUMENT_COLOR_PROPERTY_PREFIX+ins);
    		  _component.setBackground(colIns);
    		  ((JButton) _component).setText(value.toString());
    	  }
    	  if (InterpreterUtil.isInstance(BlockInterval.class, value)){
    		  String ins = ((BlockInterval) value).getInstrument();
    		  try{
    			  java.awt.Color colIns=Properties.getColor(Properties.SUBINSTRUMENT_COLOR_PROPERTY_PREFIX+ins);
    			  _component.setBackground(colIns);
    		  }catch (Exception e){
    			  
    		  }
    		 ((JButton) _component).setText(value.toString());
    		  
    	  }
          //_component.setText(value.toString());
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
  	
  	class DraggableComponent extends JButton implements DragGestureListener, DragSourceListener, Selection {
  	  /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
	DragSource dragSource;
	Observation obs;

  	  public DraggableComponent(Observation obs) {
  		  super();
  		  this.obs=obs;
  		SelectionTransferHandler.register(this);
  		
  	    dragSource = new DragSource();
  	    dragSource.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_COPY_OR_MOVE, this);
  	    
  	  }

  	  public void dragGestureRecognized(DragGestureEvent evt) {
  	    Transferable t = new StringSelection("aString");
  	    dragSource.startDrag(evt, DragSource.DefaultCopyDrop, t, this);
  	  }

  	  public void dragEnter(DragSourceDragEvent evt) {
  	    System.out.println("enters");
  	  }

  	  public void dragOver(DragSourceDragEvent evt) {

  	    System.out.println("over");
  	  }

  	  public void dragExit(DragSourceEvent evt) {
  	    System.out.println("leaves");
  	  }

  	  public void dropActionChanged(DragSourceDragEvent evt) {
  	    System.out.println("changes the drag action between copy or move");
  	  }

  	  public void dragDropEnd(DragSourceDropEvent evt) {
  	    System.out.println("finishes or cancels the drag operation");
  	  }

	@Override
	public void accept(SelectionVisitor arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Class<?> getType() {
		// TODO Auto-generated method stub
		return Observation.class;
	}

	@Override
	public Object getValue() {
		// TODO Auto-generated method stub
		return obs;
	}

	@Override
	public List<?> getValues() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isMultiple() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isRoot() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isSingle() {
		// TODO Auto-generated method stub
		return true;
	}
  	}




}
