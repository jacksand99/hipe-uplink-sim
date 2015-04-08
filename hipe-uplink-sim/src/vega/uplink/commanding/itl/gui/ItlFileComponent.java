package vega.uplink.commanding.itl.gui;

import herschel.ia.gui.kernel.parts.AbstractFileCommandComponent;
import herschel.ia.gui.kernel.util.IconLoader;

import javax.swing.Icon;

import vega.uplink.commanding.itl.ItlParser;

public class ItlFileComponent extends AbstractFileCommandComponent{

	    private static final long serialVersionUID = 1L;
	    private static final Icon ICON = IconLoader.getKernelIcon("data/Fits.gif");

	    /** Default constructor */
	    public ItlFileComponent() {
	        super();
	    }

	    @Override
	    public Icon getComponentIcon() {
	        return ICON;
	    }


	    
		@Override
		public String getCommandStatement() {

			String path=getFile().getAbsolutePath();
			path=path.replace("\\", "\\\\");
	        return ItlParser.class.getSimpleName()+".itlToPor(\""+path+"\")";

		}

	}