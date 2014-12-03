package vega.uplink.planning.gui;

import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.IOException;
import java.util.Date;

import vega.uplink.planning.Observation;
import herschel.ia.gui.kernel.VariableSelection;
import herschel.ia.gui.kernel.dnd.DnDSelection;
import herschel.ia.gui.kernel.dnd.DragSelection;
import herschel.ia.gui.kernel.dnd.DropSelection;
import herschel.ia.gui.kernel.dnd.SelectionTransferHandler;
import herschel.share.interpreter.InterpreterUtil;
import de.jaret.util.date.JaretDate;
import de.jaret.util.ui.timebars.model.TimeBarRow;
import de.jaret.util.ui.timebars.swing.TimeBarViewer;

public class ScheduleTimeBarViewer extends TimeBarViewer implements DropSelection<VariableSelection>, DragSelection<VariableSelection>,DnDSelection<VariableSelection>{

	/**
	 * 
	 */
	private VariableSelection selection;
	private static final long serialVersionUID = 1L;
	private ScheduleModel model;
	private Point location;
	
	public ScheduleTimeBarViewer(ScheduleModel model){
		super(model);
		this.model=model;
		SelectionTransferHandler.register(this);

        // Receive data in Text
        /*final TextTransfer textTransfer = TextTransfer.getInstance();
        types = new Transfer[] {textTransfer};
        target.setTransfer(types);*/

        DropTargetListener dropTarget=new DropTargetListener() {

			@Override
			public void dragEnter(DropTargetDragEvent dtde) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void dragOver(DropTargetDragEvent dtde) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void dropActionChanged(DropTargetDragEvent dtde) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void dragExit(DropTargetEvent dte) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void drop(DropTargetDropEvent dtde) {
				location = dtde.getLocation();
				DataFlavor[] flovors = dtde.getTransferable().getTransferDataFlavors();
				DataFlavor flavor=flovors[0];
				//System.out.println(dtde.getTransferable().get);
				/*for (int i=0;i<flovors.length;i++){
					System.out.println(flovors[i]);
				}*/
				try {
					VariableSelection selection=(VariableSelection) dtde.getTransferable().getTransferData(flavor);
					setSelection(selection,location);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				System.out.println(dtde.getLocation());
                /*int destY = Display.getCurrent().map(null, tbv, event.x, event.y).y;
                int destX = Display.getCurrent().map(null, tbv, event.x, event.y).x;
                int offY = _yOffsets.get(i);*/

                // drop operation. the drop operation places the dragged intervals in the destination rows.
                // if a row can not be determined (no row under the dragged interval) the interval will be discarded
                /*if (textTransfer.isSupportedType(dtde.currentDataType)) {
                    String text = (String) dtde.data;
                    System.out.println("DROP: " + text);

                    if (_draggedIntervals != null) {
                        for (int i = 0; i < _draggedIntervals.size(); i++) {
                            int destY = Display.getCurrent().map(null, tbv, event.x, event.y).y;
                            int destX = Display.getCurrent().map(null, tbv, event.x, event.y).x;
                            int offY = _yOffsets.get(i);
                            TimeBarRow overRow = null;
                            if (_tbv.getOrientation().equals(TimeBarViewerInterface.Orientation.HORIZONTAL)) {
                                overRow = tbv.rowForY(destY + offY + _startOffsetY);
                            } else {
                                overRow = tbv.rowForY(destX + offY + _startOffsetX);
                            }
                            DefaultTimeBarRowModel row = (DefaultTimeBarRowModel) overRow;

                            // NOTE: copies are placed in the dest rows
                            if (overRow != null) {
                                row.addInterval(_draggedIntervals.get(i));
                                Appointment app = (Appointment) _draggedIntervals.get(i);
                                Day destDay = (Day) overRow;
                                JaretDate realBegin = app.getRealBegin().copy();
                                realBegin.setDate(destDay.getDayDate().getDay(), destDay.getDayDate().getMonth(),
                                        destDay.getDayDate().getYear());
                                app.setRealBegin(realBegin);
                                JaretDate realEnd = app.getRealEnd().copy();
                                realEnd.setDate(destDay.getDayDate().getDay(), destDay.getDayDate().getMonth(), destDay
                                        .getDayDate().getYear());
                                app.setRealEnd(realEnd);

                            }
                        }
                    }*/
				// TODO Auto-generated method stub
				
			}

        };
        
        final DropTarget target = new DropTarget(this,dropTarget);

	}

	public Class<VariableSelection> getSelectionType() {
		// TODO Auto-generated method stub
		return VariableSelection.class;
	}

	@Override
	public void setSelection(VariableSelection arg0) {
		Object value = arg0.getValue();
		System.out.println(value.getClass());
		if (InterpreterUtil.isInstance(Observation.class, value)){
			System.out.println("is observation");
			model.addObservation(((Observation) value).copy());
			System.out.println("Loccation is "+location);
			//((ObservationListModel) list.getModel()).addObservation(((Observation) value).copy());
			//model.addObservation(((Observation) value).copy());
		}
		
		selection=arg0;
		// TODO Auto-generated method stub
		
	}
	public void setSelection(VariableSelection arg0,Point location) {
		Object value = arg0.getValue();
		System.out.println(value.getClass());
		if (InterpreterUtil.isInstance(Observation.class, value)){
			System.out.println("is observation");
			Observation obs=((Observation) value).copy();
			System.out.println("Loccation is "+location);
			JaretDate date = this.getDelegate().dateForXY(new Double(location.getX()).intValue(), new Double(location.getY()).intValue());
			long duration = obs.getDurationMilliSecs();
			obs.setObsStartDate(date.getDate());
			obs.setObsEndDate(new Date(date.getDate().getTime()+duration));
			model.addObservation(obs);
			
			//TimeBarRow overRow = this.getRowForXY(new Double(location.getX()).intValue(), new Double(location.getY()).intValue());
			//overRow.
//this.get
			//overRow.
			//((ObservationListModel) list.getModel()).addObservation(((Observation) value).copy());
			//model.addObservation(((Observation) value).copy());
		}
		
		selection=arg0;
		// TODO Auto-generated method stub
		
	}

	@Override
	public VariableSelection getSelection() {
		// TODO Auto-generated method stub
		return selection;
	}

}
