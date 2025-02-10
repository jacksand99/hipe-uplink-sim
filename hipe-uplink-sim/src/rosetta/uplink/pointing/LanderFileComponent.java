package rosetta.uplink.pointing;

import herschel.ia.gui.kernel.parts.AbstractFileCommandComponent;
import herschel.ia.gui.kernel.util.IconLoader;

import javax.swing.Icon;


public class LanderFileComponent extends AbstractFileCommandComponent{
	    private static final long serialVersionUID = 1L;
	    private static final Icon ICON = IconLoader.getKernelIcon("data/Fits.gif");

	    /** Default constructor */
	    public LanderFileComponent() {
	        super();
	    }

	    @Override
	    public Icon getComponentIcon() {
	        return ICON;
	    }

		public String getCommandStatement() {
			String path=getFile().getAbsolutePath();
			path=path.replace("\\", "\\\\");
			
	        return LanderVisibility.class.getSimpleName()+".readFromFile(\""+path+"\")";
		}
	}
