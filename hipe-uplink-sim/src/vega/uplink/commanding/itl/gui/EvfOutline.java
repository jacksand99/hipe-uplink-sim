package vega.uplink.commanding.itl.gui;




import java.util.logging.Logger;

import vega.hipe.logging.VegaLog;
import herschel.ia.pal.gui.components.ContextOutline;
import herschel.ia.dataset.gui.views.ProductTree;


public class EvfOutline extends ContextOutline { 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//private static final Logger LOG = Logger.getLogger(EvfOutline.class.getName());
	public EvfOutline(){
		super();
		VegaLog.info("New EvfOutline");
		
	}
    public ProductTree getTree() {
    	
    	return new EvfTree();
    }


}
