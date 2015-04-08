/*

   Licensed under GNU Lesser General Public License (http://www.gnu.org/licenses/lgpl.html)

 */
package vega.hipe.gui.xmlutils;

import herschel.ia.gui.kernel.parts.AbstractFileCommandComponent;
import herschel.ia.gui.kernel.util.IconLoader;

import javax.swing.Icon;



/**
 * This is a class that provides means for opening an editor (view) for a
 * product stored in a XML file.
 */
public class XmlFileComponent extends AbstractFileCommandComponent{

    private static final long serialVersionUID = 1L;
    private static final Icon ICON = IconLoader.getKernelIcon("data/Fits.gif");

    /** Default constructor */
    public XmlFileComponent() {
        super();
    }

    @Override
    public Icon getComponentIcon() {
        return ICON;
    }

	public String getCommandStatement() {
		String path=getFile().getAbsolutePath();
		path=path.replace("\\", "\\\\");
        return XmlData.class.getSimpleName()+".readFromFile(\""+path+"\")";
	}

}
