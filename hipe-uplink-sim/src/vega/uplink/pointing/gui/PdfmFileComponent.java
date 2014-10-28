package vega.uplink.pointing.gui;

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

import vega.uplink.pointing.Pdfm;
import vega.uplink.pointing.PtrUtils;

/**
 * This is a class that provides means for opening an editor (view) for a
 * product stored in a PTR file.
 */
public class PdfmFileComponent extends AbstractFileCommandComponent{
//extends AbstractFileEditorComponent implements ProxyEditorComponent<VariableSelection>{

    //private static final String IMAGE = "image";
    private static final long serialVersionUID = 1L;
    private static final Icon ICON = IconLoader.getKernelIcon("data/Fits.gif");

    /** Default constructor */
    public PdfmFileComponent() {
        super();
    }

    @Override
    public Icon getComponentIcon() {
        return ICON;
    }

    /*@Override
    public VariableSelection getTargetSelection() throws IOException {
        File file = getFile();
        Pdfm pdfm;
		try {
			pdfm = PtrUtils.readPdfmfromFile(file.getAbsolutePath());
		} catch (Exception e) {
			IOException ioe = new IOException(e.getMessage());
			ioe.initCause(e);
			throw(ioe);
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
        String variableName = file.getName().toLowerCase();
        variableName = variableName.replaceAll("[.]ROS$", "");
        variableName = variableName.replaceAll("[.]xml$", "");
        variableName = InterpreterUtil.makeVariableName(variableName);
        herschel.ia.gui.kernel.util.VariablesUtil.addVariable(variableName, pdfm);
        return new VariableSelection(variableName, pdfm);
    }

    @Override
    public String getTargetViewer() {
        return null;    // use default viewer
    }

    @Override
    public void setTargetEditor(EditorComponent editor) {}*/
	public String getCommandStatement() {
        return PtrUtils.class.getSimpleName()+".readPdfmfromFile(\""+getFile().getAbsolutePath()+"\")";
	}
}


