package vega.uplink.pointing.gui;
import java.util.Iterator;
import java.util.List;

import javax.swing.JComponent;


//import rosetta.uplink.commanding.Por;
//import rosetta.uplink.commanding.PorUtils;
import herschel.ia.dataset.gui.views.ProductOutline;
import herschel.ia.gui.apps.views.outline.OutlineComponent;
import herschel.ia.gui.apps.views.outline.OutlineView;
import herschel.ia.gui.kernel.Selection;
import herschel.ia.gui.kernel.SelectionVisitor;
import herschel.ia.gui.kernel.VariableSelection;
import herschel.ia.pal.gui.components.ContextOutline;
import vega.uplink.pointing.*;
public class PtrOutline extends ContextOutline implements OutlineComponent{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	VariableSelection selection;
	ContextOutline realOutline;
	public PtrOutline(){
		super();
		System.out.println("Construcot");
	}

	public boolean init(Selection arg0, OutlineView arg1) {
		System.out.println("init");
		Iterator<String> it=((VariableSelection) arg0).getKeys().iterator();
		VariableSelection VS=new VariableSelection();
		while (it.hasNext()){
			String key=it.next();
			Object obj=((VariableSelection) arg0).getValue(key);
			VS=new VariableSelection(key,((Ptr)obj).asContext());
		}

		return super.init(VS, arg1);
		
	}
	


}

