package vega.uplink.commanding.itl.gui;

import herschel.ia.gui.kernel.parts.AbstractFileCommandComponent;
import herschel.ia.gui.kernel.util.IconLoader;

import javax.swing.Icon;

import rosetta.uplink.pointing.ExclusionPeriod;
import vega.uplink.commanding.itl.EventList;

public class EvfFileComponent extends AbstractFileCommandComponent{

	    private static final long serialVersionUID = 1L;
	    private static final Icon ICON = IconLoader.getKernelIcon("data/Fits.gif");

	    /** Default constructor */
	    public EvfFileComponent() {
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
			if (getFile().getName().startsWith("EXCL")){
				return ExclusionPeriod.class.getSimpleName()+".readFromFile(\""+path+"\")";
			}else{
				return EventList.class.getSimpleName()+".parseEvf(\""+path+"\")";
			}

		}

	}