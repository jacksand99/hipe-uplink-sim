package vega.uplink.planning.gui;

import herschel.ia.gui.kernel.VariableSelection;
import herschel.ia.gui.kernel.parts.AbstractFileCommandComponent;
import herschel.ia.gui.kernel.parts.AbstractFileEditorComponent;
import herschel.ia.gui.kernel.parts.EditorComponent;
import herschel.ia.gui.kernel.parts.ProxyEditorComponent;
import herschel.ia.gui.kernel.util.IconLoader;
import herschel.share.interpreter.InterpreterUtil;

import java.io.File;
import java.io.IOException;

import javax.swing.Icon;

import vega.uplink.planning.Observation;
import vega.uplink.planning.ObservationUtil;


public class ObservationFileComponent extends AbstractFileCommandComponent{
//AbstractFileEditorComponent implements ProxyEditorComponent<VariableSelection>{

    //private static final String IMAGE = "image";
    private static final long serialVersionUID = 1L;
    private static final Icon ICON = IconLoader.getKernelIcon("data/Fits.gif");

    /** Default constructor */
    public ObservationFileComponent() {
        super();
    }

    @Override
    public Icon getComponentIcon() {
        return ICON;
    }

    /*@Override
    public VariableSelection getTargetSelection() throws IOException {
        File file = getFile();
        Observation obs;
		try {
			obs = ObservationUtil.readObservationFromFile(file.getAbsolutePath());
	        String variableName = file.getName().toLowerCase();
	        variableName = variableName.replaceAll("[.]ROS$", "");
	        variableName = variableName.replaceAll("[.]xml$", "");
	        variableName = InterpreterUtil.makeVariableName(variableName);
	        herschel.ia.gui.kernel.util.VariablesUtil.addVariable(variableName, obs);
	        return new VariableSelection(variableName, obs);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			IOException io = new IOException(e.getMessage());
			io.initCause(e);
			throw io;
		}
    }*/

    /*@Override
    public String getTargetViewer() {
        return null;    // use default viewer
    }

    @Override
    public void setTargetEditor(EditorComponent editor) {}*/

	@Override
	public String getCommandStatement() {
		String path=getFile().getAbsolutePath();
		path=path.replace("\\", "\\\\");
        return ObservationUtil.class.getSimpleName()+".readObservationFromFile(\""+path+"\")";
	}


}
