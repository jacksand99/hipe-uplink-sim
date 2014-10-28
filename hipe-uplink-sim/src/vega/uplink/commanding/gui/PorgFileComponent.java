package vega.uplink.commanding.gui;

//import herschel.ia.gui.kernel.VariableSelection;
import herschel.ia.gui.kernel.parts.AbstractFileCommandComponent;
//import herschel.ia.gui.kernel.parts.AbstractFileEditorComponent;
//import herschel.ia.gui.kernel.parts.EditorComponent;
//import herschel.ia.gui.kernel.parts.ProxyEditorComponent;
import herschel.ia.gui.kernel.util.IconLoader;
//import herschel.share.interpreter.InterpreterUtil;

//import java.io.File;
//import java.io.IOException;

import javax.swing.Icon;



//import vega.uplink.commanding.Por;
import vega.uplink.commanding.PorUtils;
//import vega.uplink.commanding.SuperPor;

/**
 * This is a class that provides means for opening an editor (view) for a
 * product stored in a PORG file.
 */
public class PorgFileComponent extends AbstractFileCommandComponent{
//extends AbstractFileEditorComponent implements ProxyEditorComponent<VariableSelection>{

    //private static final String IMAGE = "image";
    private static final long serialVersionUID = 1L;
    private static final Icon ICON = IconLoader.getKernelIcon("data/Fits.gif");

    /** Default constructor */
    public PorgFileComponent() {
        super();
    }

    @Override
    public Icon getComponentIcon() {
        return ICON;
    }

   /* @Override
    public VariableSelection getTargetSelection() throws IOException {
        File file = getFile();
        SuperPor spor = PorUtils.readPORGfromFile(file.getAbsolutePath());
        String variableName = file.getName().toLowerCase();
        variableName = variableName.replaceAll("[.]ROS$", "");
        variableName = variableName.replaceAll("[.]zip$", "");
        variableName = InterpreterUtil.makeVariableName(variableName);
        herschel.ia.gui.kernel.util.VariablesUtil.addVariable(variableName, spor);
        return new VariableSelection(variableName, spor);
    }

    @Override
    public String getTargetViewer() {
        return null;    // use default viewer
    }

    @Override
    public void setTargetEditor(EditorComponent editor) {}*/
	public String getCommandStatement() {
        return PorUtils.class.getSimpleName()+".readPORGfromFile(\""+getFile().getAbsolutePath()+"\")";
	}

}
