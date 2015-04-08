package vega.uplink.commanding.gui;

import herschel.ia.gui.kernel.parts.AbstractFileCommandComponent;
import herschel.ia.gui.kernel.util.IconLoader;




import javax.swing.Icon;






import vega.uplink.commanding.Pdor;
import vega.uplink.commanding.PorUtils;


/**
 * This is a class that provides means for opening an editor (view) for a
 * product stored in a PORG file.
 */
public class PdorFileComponent extends AbstractFileCommandComponent{

    private static final long serialVersionUID = 1L;
    private static final Icon ICON = IconLoader.getKernelIcon("data/Fits.gif");

    /** Default constructor */
    public PdorFileComponent() {
        super();
    }

    @Override
    public Icon getComponentIcon() {
        return ICON;
    }

 
	public String getCommandStatement() {
		String path=getFile().getAbsolutePath();
		path=path.replace("\\", "\\\\");
        return Pdor.class.getSimpleName()+".readPdor(\""+path+"\")";
	}

}
