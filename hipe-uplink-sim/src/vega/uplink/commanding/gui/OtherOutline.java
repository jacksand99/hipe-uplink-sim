package vega.uplink.commanding.gui;

import java.util.Iterator;
import java.util.List;

import javax.swing.JComponent;

import vega.uplink.commanding.Por;
import vega.uplink.commanding.PorUtils;
import herschel.ia.dataset.gui.views.ProductOutline;
import herschel.ia.gui.apps.views.outline.OutlineComponent;
import herschel.ia.gui.apps.views.outline.OutlineView;
import herschel.ia.gui.kernel.Selection;
import herschel.ia.gui.kernel.SelectionVisitor;
import herschel.ia.gui.kernel.VariableSelection;
import herschel.ia.pal.gui.components.ContextOutline;

//public class OtherOutline extends ContextOutline implements OutlineComponent{
public class OtherOutline extends PorOutline {
	/*private static final long serialVersionUID = 1L;
	VariableSelection selection;
	ContextOutline realOutline;
	public OtherOutline(){
		super();
		System.out.println("Construcot");
		
		//realOutline=new ProductOutline();
	}

	public boolean init(Selection arg0, OutlineView arg1) {
		System.out.println("init");
		Iterator<String> it=((VariableSelection) arg0).getKeys().iterator();
		VariableSelection VS=new VariableSelection();
		while (it.hasNext()){
			String key=it.next();
			Object obj=((VariableSelection) arg0).getValue(key);
			VS=new VariableSelection(key,PorUtils.porToContext((Por)obj));
		}

		//selection=(VariableSelection) arg0;
		return super.init(VS, arg1);
		
		// TODO Auto-generated method stub
		//return false;
	}*/
	


}
