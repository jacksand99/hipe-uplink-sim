package vega.uplink.commanding.gui;

import herschel.ia.gui.kernel.VariableSelection;
import herschel.ia.gui.kernel.parts.AbstractFileEditorComponent;
import herschel.ia.gui.kernel.parts.EditorComponent;
import herschel.ia.gui.kernel.parts.ProxyEditorComponent;
import herschel.ia.gui.kernel.util.IconLoader;
import herschel.share.interpreter.InterpreterUtil;

import java.io.File;
import java.io.IOException;

import javax.swing.Icon;

import vega.uplink.commanding.Mib;
import vega.uplink.commanding.PorUtils;
import vega.uplink.commanding.SuperPor;

public class MibFileComponent extends AbstractFileEditorComponent implements ProxyEditorComponent<VariableSelection>{

    //private static final String IMAGE = "image";
    private static final long serialVersionUID = 1L;
    private static final Icon ICON = IconLoader.getKernelIcon("data/Fits.gif");

    /** Default constructor */
    public MibFileComponent() {
        super();
    }

    @Override
    public Icon getComponentIcon() {
        return ICON;
    }

    @Override
    public VariableSelection getTargetSelection() throws IOException {
        File file = getFile();
        Mib mib = Mib.getMibFromTarFile(file.getAbsolutePath());
        String variableName = file.getName().toLowerCase();
        variableName = variableName.replaceAll("[.]ROS$", "");
        variableName = variableName.replaceAll("[.]tar$", "");
        variableName = InterpreterUtil.makeVariableName(variableName);
        herschel.ia.gui.kernel.util.VariablesUtil.addVariable(variableName, mib);
        return new VariableSelection(variableName, mib);
    }

    @Override
    public String getTargetViewer() {
        return null;    // use default viewer
    }

    @Override
    public void setTargetEditor(EditorComponent editor) {}


}
