package vega.uplink.planning.gui;

//package vega.uplink.planning.gui;

import herschel.ia.gui.kernel.parts.AbstractFileCommandComponent;
import herschel.ia.gui.kernel.util.IconLoader;

import javax.swing.Icon;





import vega.uplink.planning.period.Plan;

public class PeriodsFileComponent extends AbstractFileCommandComponent{
    private static final long serialVersionUID = 1L;
    private static final Icon ICON = IconLoader.getKernelIcon("data/Fits.gif");

    /** Default constructor */
    public PeriodsFileComponent() {
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
        return Plan.class.getSimpleName()+".readFromFile(\""+path+"\")";
	}

}

