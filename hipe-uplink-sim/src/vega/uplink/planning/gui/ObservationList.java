package vega.uplink.planning.gui;

import herschel.ia.gui.kernel.Selection;
import herschel.ia.gui.kernel.VariableSelection;
import herschel.ia.gui.kernel.dnd.DnDSelection;
import herschel.ia.gui.kernel.dnd.DragSelection;
import herschel.ia.gui.kernel.dnd.DropSelection;
import herschel.ia.gui.kernel.dnd.SelectionTransferHandler;
import herschel.share.interpreter.InterpreterUtil;

import javax.swing.JList;

import vega.uplink.planning.Observation;

public class ObservationList extends JList<Observation> implements DropSelection<VariableSelection>, DragSelection<VariableSelection>,DnDSelection<VariableSelection>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private VariableSelection selection;
	ObservationListModel model;
	public ObservationList(ObservationListModel observationListModel) {
		super(observationListModel);
		model=observationListModel;
		//this.setTransferHandler(SelectionTransferHandler());
		SelectionTransferHandler.register(this);
		// TODO Auto-generated constructor stub
	}

	///@Override
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
		}
		
		selection=arg0;
		// TODO Auto-generated method stub
		
	}

	@Override
	public VariableSelection getSelection() {
		// TODO Auto-generated method stub
		return selection;
	}

	//@Override
	/*public Selection getSelection() {
		// TODO Auto-generated method stub
		return selection;
	}*/

}
