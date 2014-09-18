package vega.uplink.pointing.gui;

//import herschel.ia.gui.kernel.Selection;
import herschel.ia.gui.kernel.VariableSelection;
import herschel.ia.gui.kernel.parts.EditorPart;
import herschel.ia.numeric.String1d;

//import javax.swing.JPanel;

public class MetadataCommentsPanel extends herschel.ia.dataset.gui.views.ArrayDataComponent{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String1d data;
	public MetadataCommentsPanel(EditorPart part,String1d comments){
		super();
		data=comments;
		VariableSelection selection=new VariableSelection("comments",comments);
		init(selection,part);
		//setSelection(selection);
	}
	//public 
}
