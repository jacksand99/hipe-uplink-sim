package vega.hipe.gui.xmlutils;

/*

Licensed under GNU Lesser General Public License (http://www.gnu.org/licenses/lgpl.html)

*/


import herschel.ia.gui.kernel.parts.AbstractFileCommandComponent;
import herschel.ia.gui.kernel.util.IconLoader;

import javax.swing.Icon;



/**
* This is a class that provides means for opening an editor (view) for a
* product stored in a HTML file.
*/
public class HtmlFileComponent extends AbstractFileCommandComponent{

 private static final long serialVersionUID = 1L;
 private static final Icon ICON = IconLoader.getKernelIcon("data/Fits.gif");

 /** Default constructor */
 public HtmlFileComponent() {
     super();
 }

 @Override
 public Icon getComponentIcon() {
     return ICON;
 }

	public String getCommandStatement() {
		String path=getFile().getAbsolutePath();
		path=path.replace("\\", "\\\\");
     return HtmlDocument.class.getSimpleName()+".readFromFile(\""+path+"\")";
	}

}
