package vega.uplink.commanding.gui;


import java.util.logging.Logger;



import vega.hipe.logging.VegaLog;
import herschel.ia.pal.gui.components.ContextOutline;
import herschel.ia.dataset.gui.views.ProductTree;


public class PorOutline extends ContextOutline { 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//private static final Logger LOG = Logger.getLogger(PorOutline.class.getName());
	public PorOutline(){
		super();
		VegaLog.info("New PorOutline");
		
	}
    public ProductTree getTree() {
    	
    	return new PorTree();
    }


}
