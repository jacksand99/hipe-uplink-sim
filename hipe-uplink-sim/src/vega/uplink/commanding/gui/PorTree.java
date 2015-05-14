package vega.uplink.commanding.gui;



import java.util.logging.Logger;




import vega.uplink.commanding.Por;
import herschel.ia.dataset.Product;


import herschel.ia.gui.kernel.Selection;

import herschel.ia.pal.gui.components.ContextTree;

public class PorTree extends ContextTree{

	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//private static final Logger LOG = Logger.getLogger(PorTree.class.getName());

	 public PorTree(){
		 super();

	 }
	 

	    

	 public boolean init(Selection newSelection, String newViewId) {

		 return super.init(newSelection, newViewId+"+OTHER");
	 }


    
    protected Class<? extends Product> getVariableType() {
        return Por.class;
    }
}
