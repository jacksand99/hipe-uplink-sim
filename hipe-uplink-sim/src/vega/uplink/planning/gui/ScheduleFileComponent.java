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



//import vega.uplink.planning.Observation;
import vega.uplink.planning.ObservationUtil;
import vega.uplink.planning.Schedule;

public class ScheduleFileComponent extends AbstractFileCommandComponent{
//AbstractFileEditorComponent implements ProxyEditorComponent<VariableSelection>{

    //private static final String IMAGE = "image";
    private static final long serialVersionUID = 1L;
    private static final Icon ICON = IconLoader.getKernelIcon("data/Fits.gif");

    /** Default constructor */
    public ScheduleFileComponent() {
        super();
    }

    @Override
    public Icon getComponentIcon() {
        return ICON;
    }

    /*@Override
    public VariableSelection getTargetSelection() throws IOException {
        File file = getFile();
        Schedule sch;
		try {
			sch = ObservationUtil.readScheduleFromFile(file.getAbsolutePath());
	        String variableName = file.getName().toLowerCase();
	        variableName = variableName.replaceAll("[.]ROS$", "");
	        variableName = variableName.replaceAll("[.]xml$", "");
	        variableName = InterpreterUtil.makeVariableName(variableName);
	        herschel.ia.gui.kernel.util.VariablesUtil.addVariable(variableName, sch);
	        return new VariableSelection(variableName, sch);

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
        /*String variableName = getFile().getName().toLowerCase();
        variableName = variableName.replaceAll("[.]ROS$", "");
        variableName = variableName.replaceAll("[.]xml$", "");
        variableName = InterpreterUtil.makeVariableName(variableName);*/
        return ObservationUtil.class.getSimpleName()+".readScheduleFromFile(\""+getFile().getAbsolutePath()+"\")";
		// TODO Auto-generated method stub
		//return null;
	}

}
