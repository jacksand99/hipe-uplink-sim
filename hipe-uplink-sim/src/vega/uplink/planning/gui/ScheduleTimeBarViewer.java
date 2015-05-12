package vega.uplink.planning.gui;

import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;

import vega.uplink.planning.Observation;
import vega.uplink.pointing.PointingBlock;
import herschel.ia.gui.kernel.VariableSelection;
import herschel.ia.gui.kernel.dnd.DnDSelection;
import herschel.ia.gui.kernel.dnd.DragSelection;
import herschel.ia.gui.kernel.dnd.DropSelection;
import herschel.ia.gui.kernel.dnd.SelectionTransferHandler;
import herschel.share.interpreter.InterpreterUtil;
import de.jaret.util.date.JaretDate;
import de.jaret.util.ui.timebars.swing.TimeBarViewer;

public class ScheduleTimeBarViewer extends TimeBarViewer implements DropSelection<VariableSelection>, DragSelection<VariableSelection>,DnDSelection<VariableSelection>{

	private VariableSelection selection;
	private static final long serialVersionUID = 1L;
	private ScheduleModel model;
	private Point location;
	
	public ScheduleTimeBarViewer(ScheduleModel model){
		super(model);
		this.model=model;
		SelectionTransferHandler.register(this);

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
				try {
					VariableSelection selection=(VariableSelection) dtde.getTransferable().getTransferData(flavor);
					setSelection(selection,location);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				System.out.println(dtde.getLocation());
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
		}
		
		selection=arg0;
		
	}
	public void setSelection(VariableSelection arg0,Point location) {
		Object value = arg0.getValue();
		System.out.println(value.getClass());
		if (InterpreterUtil.isInstance(Observation.class, value)){
			System.out.println("is observation");
			Observation obs=((Observation) value).copy();
			System.out.println("Loccation is "+location);
			JaretDate date = this.getDelegate().dateForXY(new Double(location.getX()).intValue(), new Double(location.getY()).intValue());
			PointingBlock ptslBlock = model.schedule.getPtslSegment().getBlockAt(date.getDate());
			obs.setObsStartDate(ptslBlock.getStartTime());
			obs.setObsEndDate(ptslBlock.getEndTime());
			model.addObservation(obs);
		}
		
		selection=arg0;
		
	}

	@Override
	public VariableSelection getSelection() {
		return selection;
	}

}
