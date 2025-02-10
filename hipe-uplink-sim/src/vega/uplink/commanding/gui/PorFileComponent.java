package vega.uplink.commanding.gui;

import herschel.ia.gui.kernel.SiteAction;
import herschel.ia.gui.kernel.VariableSelection;
import herschel.ia.gui.kernel.menus.ActionBars;
import herschel.ia.gui.kernel.menus.Retarget;
import herschel.ia.gui.kernel.parts.AbstractEditorAction;
import herschel.ia.gui.kernel.parts.AbstractFileCommandComponent;
import herschel.ia.gui.kernel.parts.AbstractFileEditorComponent;
import herschel.ia.gui.kernel.parts.EditorComponent;
import herschel.ia.gui.kernel.parts.ProxyEditorComponent;
import herschel.ia.gui.kernel.util.IconLoader;
import herschel.ia.gui.kernel.util.component.FileChooser;
import herschel.share.interpreter.InterpreterUtil;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JFileChooser;

import vega.uplink.commanding.Por;
import vega.uplink.commanding.PorUtils;
import vega.uplink.pointing.PtrUtils;
import static herschel.ia.gui.kernel.util.Constants.CTRL;
//import vega.uplink.pointing.Ptr;
//import vega.uplink.pointing.PtrUtils;

/**
 * This is a class that provides means for opening an editor (view) for a
 * product stored in a POR file.
 */
public class PorFileComponent extends AbstractFileCommandComponent{
//extends AbstractFileEditorComponent implements ProxyEditorComponent<VariableSelection>{

    //private static final String IMAGE = "image";
    private static final long serialVersionUID = 1L;
    private static final Icon ICON = IconLoader.getKernelIcon("data/Fits.gif");
    private String path="";
    private Por porEdited;

    /** Default constructor */
    public PorFileComponent() {
        super();
    }

    @Override
    public Icon getComponentIcon() {
        return ICON;
    }

    /*@Override
    public VariableSelection getTargetSelection() throws IOException {
        File file = getFile();
        path=file.getParent();
        Por por = PorUtils.readPORfromFile(file.getAbsolutePath());
        porEdited=por;
        String variableName = file.getName().toLowerCase();
        variableName = variableName.replaceAll("[.]ROS$", "");
        variableName = variableName.replaceAll("[.]xml$", "");
        variableName = InterpreterUtil.makeVariableName(variableName);
        herschel.ia.gui.kernel.util.VariablesUtil.addVariable(variableName, por);
        return new VariableSelection(variableName, por);
    }

    @Override
    public String getTargetViewer() {
        return null;    // use default viewer
    }

    @Override
    public void setTargetEditor(EditorComponent editor) {}
    
    /** Creates common actions for undoable text editors. */
    @Override
    @SuppressWarnings("serial")
    protected List<SiteAction> makeActions() {
        SiteAction saveAs = new AbstractEditorAction("SaveAs") {
 
			@Override
			public void actionPerformed(ActionEvent e) {
				saveFileAs(true);
				
			}
        };
        SiteAction save = new AbstractEditorAction("Save", CTRL + "S") {
            @Override public void actionPerformed(ActionEvent e) {
                saveFile();
            }
        };
        final String id = getId();
        ActionBars menus = getArea().getActionBars(id);
        menus.retarget(Retarget.SAVE, save);
        menus.retarget(Retarget.SAVE_AS, saveAs);
        return Arrays.asList( save, saveAs);
    }
    
    public void saveFile(){
    	saveFile(new File(path+"/"+porEdited.getName()));
    	//PorUtils.writePORtofile(path+"/"+porEdited.getName(), porEdited);
    }
    
    public void saveFile(File file){
    	PorUtils.writePORtofile(file.getAbsolutePath(),porEdited);
    }
    
    public void saveFileAs(boolean value){
    	saveDialog(true);
    }
    
    /**
     * Opens a dialog to save the editor's contents.
     * @param sameDirectory if {@code true}, the dialog will be shown in the file's directory;
     * if {@code false}, it will be opened in its current directory
     * @return {@code true} if the file has been saved; {@code false} otherwise
     */
    public boolean saveDialog(boolean sameDirectory) {
    	File currentFile=new File(path+"/"+porEdited.getName());
        boolean res = false;

        FileChooser fileChooser = FileChooser.getChooser(PorFileComponent.class);
        File directory = fileChooser.getCurrentDirectory();
        //File file = (currentFile == null)? new File(getTitle() + "." + extension) : currentFile;
        File file=currentFile;
        fileChooser.setSelectedFile(file);
        if (!sameDirectory) {
            fileChooser.setCurrentDirectory(directory);
        }

        if (fileDialog(JFileChooser.SAVE_DIALOG)) {
            saveFile(fileChooser.getSelectedFile());
            res = true;
        } else {
            fileChooser.setCurrentDirectory(directory); // cancelled: reset original directory
        }
        return res;
    }
    
    private boolean fileDialog(int type) {
        FileChooser fileChooser = FileChooser.getChooser(PorFileComponent.class);
        fileChooser.setDialogType(type);
        fileChooser.resetChoosableFileFilters();
        fileChooser.setFileFilters("ROS");
        return fileChooser.showDialog(this, null) == JFileChooser.APPROVE_OPTION;
    }
    
	public String getCommandStatement() {
		String path=getFile().getAbsolutePath();
		path=path.replace("\\", "\\\\");
        return PorUtils.class.getSimpleName()+".readPORfromFile(\""+path+"\")";
	}




}

