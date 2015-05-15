package rosetta.uplink.pointing;

import herschel.ia.gui.kernel.parts.AbstractFileCommandComponent;
import herschel.ia.gui.kernel.util.IconLoader;

import javax.swing.Icon;

//import vega.uplink.commanding.PorUtils;

public class ExclFileComponent extends AbstractFileCommandComponent{
	//extends AbstractFileEditorComponent implements ProxyEditorComponent<VariableSelection>{

	    //private static final String IMAGE = "image";
	    private static final long serialVersionUID = 1L;
	    private static final Icon ICON = IconLoader.getKernelIcon("data/Fits.gif");

	    /** Default constructor */
	    public ExclFileComponent() {
	        super();
	    }

	    @Override
	    public Icon getComponentIcon() {
	        return ICON;
	    }

	    /*@Override
	    public VariableSelection getTargetSelection() throws IOException {
	        File file = getFile();
	        Fecs fecs;
			try {
				fecs = PorUtils.readFecsFromFile(file.getAbsolutePath());
			} catch (Exception e) {
				IOException io=new IOException("Error while reading FECS:"+e.getMessage());
				io.initCause(e);
				
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw(io);
			}
	        String variableName = file.getName().toLowerCase();
	        variableName = variableName.replaceAll("[.]ROS$", "");
	        variableName = variableName.replaceAll("[.]xml$", "");
	        variableName = InterpreterUtil.makeVariableName(variableName);
	        herschel.ia.gui.kernel.util.VariablesUtil.addVariable(variableName, fecs);
	        return new VariableSelection(variableName, fecs);
	    }

	    @Override
	    public String getTargetViewer() {
	        return null;    // use default viewer
	    }

	    @Override
	    public void setTargetEditor(EditorComponent editor) {}*/
		public String getCommandStatement() {
			String path=getFile().getAbsolutePath();
			path=path.replace("\\", "\\\\");
			
	        return ExclusionPeriod.class.getSimpleName()+".readFromFile(\""+path+"\")";
		}
	}
