package vega.uplink.commanding.itl.gui;



import java.util.logging.Logger;






import vega.uplink.commanding.Por;
import vega.uplink.commanding.gui.PorTree;
import vega.uplink.commanding.itl.EventList;
import herschel.ia.dataset.Product;
import herschel.ia.gui.kernel.Selection;
import herschel.ia.pal.gui.components.ContextTree;

public class EvfTree extends ContextTree{

	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//private static final Logger LOG = Logger.getLogger(PorTree.class.getName());

	 public EvfTree(){
		 super();

	 }
	 

	    

	 public boolean init(Selection newSelection, String newViewId) {

		 return super.init(newSelection, newViewId+"+OTHER");
	 }


    
    protected Class<? extends Product> getVariableType() {
        return EventList.class;
    }
}