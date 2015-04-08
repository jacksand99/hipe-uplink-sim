package vega.uplink.commanding.itl.gui;

/*

Licensed under GNU Lesser General Public License (http://www.gnu.org/licenses/lgpl.html)

*/


import herschel.ia.gui.apps.components.util.BottomPanel;
import herschel.ia.gui.apps.components.util.TextComponentFindAction;
import herschel.ia.gui.kernel.SiteAction;
import herschel.ia.gui.kernel.menus.ActionBars;
import herschel.ia.gui.kernel.menus.Insert;
import herschel.ia.gui.kernel.menus.Retarget;
import herschel.ia.gui.kernel.parts.AbstractEditorAction;
import herschel.ia.gui.kernel.parts.AbstractVariableEditorComponent;
import herschel.ia.jconsole.util.TextLineNumber;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.text.JTextComponent;

import vega.IconResources;
import vega.hipe.gui.xmlutils.XMLTextEditor;
import vega.uplink.commanding.itl.EventList;
import herschel.ia.jconsole.gui.JSearchDialog;
import static herschel.ia.gui.kernel.menus.Insert.MAIN;
import static herschel.ia.gui.kernel.menus.Insert.Extension.EDIT_ADDON;
import static herschel.ia.gui.kernel.util.Constants.CTRL;
import herschel.ia.gui.kernel.util.IconLoader;
import herschel.ia.gui.kernel.util.PopupDialog;
import static herschel.ia.gui.kernel.SiteAction.Style.AS_PUSH;
import static herschel.ia.gui.kernel.menus.Insert.Scheme.MENU;

/**
* @author Javier Arenas (javier.arenas@gmail.com)
*
*/
public class EvfFileEditor extends AbstractVariableEditorComponent<EventList> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String NEWLINE = "\n";
	
	EventList data;
	JEditorPane editor;
	JScrollPane scroll;
	JSearchDialog searchDialog;
	JLabel classLabel;
	public EvfFileEditor(){
		super(new BorderLayout());
	}
	public void init(){
		
		JPanel topPanel=new JPanel();
		topPanel.setLayout(new BorderLayout());
		JPanel buttonsPanel=new JPanel();
		buttonsPanel.setLayout(new BorderLayout());
		classLabel=new JLabel(data.getName());
		classLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		topPanel.add(classLabel,BorderLayout.EAST);
		JPanel global = new JPanel (new BorderLayout());
		global.add(topPanel,BorderLayout.NORTH);
		editor = new JEditorPane();
		editor.setEditorKitForContentType(ITLEditorKit.ITL_MIME_TYPE, new ITLEditorKit());
		editor.setContentType(ITLEditorKit.ITL_MIME_TYPE);
		JScrollPane scroll=new JScrollPane(editor);
		editor.setText(data.toEvf());
		scroll=new JScrollPane(editor);
		scroll.setRowHeaderView(new TextLineNumber(editor));
		searchDialog=new JSearchDialog(editor,data.getName());
		global.add(scroll,BorderLayout.CENTER);
		this.add(new BottomPanel(this, global));
		ActionBars menus = getArea().getActionBars(getId());
		SiteAction print = new PrintAction();
		SiteAction saveAs = new AbstractEditorAction("SaveAs") {
         /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override public void actionPerformed(ActionEvent e) {
             saveAs();
         }
     };
		SiteAction save = new AbstractEditorAction("Save") {
         /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override public void actionPerformed(ActionEvent e) {
             save();
         }
     };
		SiteAction cut = new AbstractEditorAction("Cut") {
         /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override public void actionPerformed(ActionEvent e) {
             cut();
         }
     };
		SiteAction copy = new AbstractEditorAction("Copy") {
         /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override public void actionPerformed(ActionEvent e) {
             copy();
         }
     };
		SiteAction paste = new AbstractEditorAction("Paste") {
         /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override public void actionPerformed(ActionEvent e) {
             paste();
         }
     };
     SiteAction search = new AbstractEditorAction(AS_PUSH, "FindReplace", "Find/Replace ...", CTRL + "F", IconLoader.getKernelIcon("Search.gif")) {
         /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override public void actionPerformed(ActionEvent e) {
             if (searchDialog == null) {
                 searchDialog = new JSearchDialog(editor, data.getName());
             }
             searchDialog.search();
         }
     };
		SiteAction goTo = new AbstractEditorAction(AS_PUSH, "GoTo", "Go To Line", CTRL + "L", IconLoader.getKernelIcon("Goto.gif")) {
			private static final long serialVersionUID = 1L;
			@Override public void actionPerformed(ActionEvent e) {
             goToLine();
         }
     };
     
     SiteAction insearch = new AbstractEditorAction(AS_PUSH, "search", "Incremental search", CTRL + "I", IconLoader.getKernelIcon("IncrementalSearch.gif")) {
     	private static final long serialVersionUID = 1L;
     	@Override public void actionPerformed(ActionEvent e) {
             JTextComponent textComponent = editor;
             ActionEvent event = new ActionEvent(textComponent, Integer.MAX_VALUE, "");
             TextComponentFindAction.ACTION.actionPerformed(event);
         }
     };

		/*SiteAction undo = new AbstractEditorAction("UnDo") {
			private static final long serialVersionUID = 1L;
			@Override public void actionPerformed(ActionEvent e) {
             undo();
         }
     };
		SiteAction redo = new AbstractEditorAction("ReDo") {
			private static final long serialVersionUID = 1L;
			@Override public void actionPerformed(ActionEvent e) {
             redo();
         }
     };*/
		SiteAction select = new AbstractEditorAction("SelectAll") {
         
			private static final long serialVersionUID = 1L;
			@Override public void actionPerformed(ActionEvent e) {
             selectAll();
         }
     };
     

		menus.retarget(Retarget.SAVE_AS, saveAs);
		menus.retarget(Retarget.PRINT, print);
		menus.retarget(Retarget.SAVE, save);
     menus.retarget(Retarget.CUT, cut);
     menus.retarget(Retarget.COPY, copy);
     menus.retarget(Retarget.PASTE, paste);
     //menus.retarget(Retarget.UNDO, undo);
     //menus.retarget(Retarget.REDO, redo);  
     menus.insert(new Insert(MENU, MAIN, EDIT_ADDON), search);
     menus.insert(new Insert(MENU, MAIN, EDIT_ADDON), goTo);
     menus.retarget(Retarget.SELECT_ALL, select);
     menus.insert(new Insert(MENU, MAIN, EDIT_ADDON), insearch);


	}
	public void selectAll(){
		editor.selectAll();
	}
 /**
  * Opens a dialog for going to a given line.
  */
 public void goToLine() {
     int nLines = Math.max(1, getNumLines());
     String textLine = PopupDialog.askForInput(this, "Go to line (1.." + nLines + "):");
     if (textLine == null) {
         return;
     }
     try {
         int line = Integer.parseInt(textLine);
         gotoLine(line);
     } catch (NumberFormatException e) {
         PopupDialog.showError(this, textLine + " is not a valid number.");
         goToLine();
     } catch (IllegalArgumentException e) {
         PopupDialog.showError(this, e.getMessage());
         goToLine();
     }
 }

 /**
  * Moves the caret to the given line.
  * @param line The line to go to (indices start at 1)
  * @throws IllegalArgumentException if the line is out of the valid range [1..nLines]
  */
 public void gotoLine(int line) {
     int nLines = Math.max(1, getNumLines());
     if (line < 1 || nLines < line) {
         throw new IllegalArgumentException("Invalid line: " + line + "\nValid range is 1.." + nLines);
     }
     String text = editor.getText();
     int textSize = text.length();
     int position = 0;
     for (nLines = 1; nLines < line && position < textSize; position++) {
         if (text.charAt(position) == '\n') {
             nLines++;
         }
     }
     editor.setCaretPosition(position);
 }
 
 
 /*public void undo(){
 	editor.undo();
 }
 
 public void redo(){
 	editor.redo();
 }*/
 public int getNumLines() {
     return getLines().size();
 }
 public String getText(){
 	return editor.getText();
 }
 /**
  * Return a List conatining the lines of the text
  * @return List
  */
 public List<String> getLines() {
     String text = getText();
     return (text == null) ? new ArrayList<String>() : getLines(text);
 }

 /**
  * Return an array of lines from the given text
  */
 protected List<String> getLines(String text) {
     List<String> lines = Arrays.asList(text.split(NEWLINE, -1));
     int last = lines.size() - 1;
     if (last >= 0 && lines.get(last).isEmpty()) {
         lines = lines.subList(0, last);
     }
     return lines;
 }
	public void cut(){
		editor.cut();
	}
	public void copy(){
		editor.copy();
	}
	public void paste(){
		editor.paste();
	}
	public void search(){
		searchDialog.setVisible(true);
	}
	public void save(){
     try{

     	String text = editor.getText();
     		data.regenerate(text);
			//data.setXmlData(text);
			data.save();
	            

	         

     } catch (Exception e1) {
					JOptionPane.showMessageDialog(EvfFileEditor.this,
						    e1.getMessage(),
						    "Error",
						    JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
					editor.setText(data.toEvf());
				}		
	}
	
	public void saveAs(){
     try{
	          JFileChooser chooser = new JFileChooser();
	          try{
	        	  chooser.setSelectedFile(new File(data.getPath()+"/"+data.getName()));
	          }catch (Exception e3){
	        	chooser.setCurrentDirectory(new File(System.getProperty("user.home")));
	          }
	          chooser.setMultiSelectionEnabled(false);
	          
	          int option = chooser.showSaveDialog(EvfFileEditor.this);
	          //File sf;
	          if (option == JFileChooser.APPROVE_OPTION) {
	            File sf = chooser.getSelectedFile();
	            data.setName(sf.getName());
	            data.setPath(sf.getParent());

	            save();
	            classLabel.setText(data.getName());
	            
	          }

	         

       } catch (Exception e1) {
					JOptionPane.showMessageDialog(EvfFileEditor.this,
						    e1.getMessage(),
						    "Error",
						    JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
					editor.setText(data.toEvf());
				}	
	}
	

	public void setData(EventList data){
		this.data=data;
		init();
	}
 public boolean makeEditorContent() {
 	setData(getValue());
 	return true;
 }
 
	public Icon getComponentIcon() {
		// TODO Auto-generated method stub
     try {
         URL resource = EvfFileEditor.class.getResource(IconResources.ITL_ICON);
         BufferedImage imageIcon = ImageIO.read(resource);
         return new ImageIcon(imageIcon);
 } catch (IOException e) {
        
         e.printStackTrace();
         return null;
 }
	}
	@Override
	protected Class<? extends EventList> getVariableType() {
		return EventList.class;
	}
	
 public final void setGlobalScroll(boolean enabled) {
 
	
     setGlobalScroll(enabled, enabled); // horizontal scroll bar not disabled: HCSS-13801
 }
 

 
 private class PrintAction extends AbstractEditorAction {
     private static final long serialVersionUID = 1L;

     PrintAction() {
         super("Print", CTRL + "P");
         setText("Print...");
     }

     @Override
     public void actionPerformed(ActionEvent e) {
         try {
             new Thread("Print thread") {
                 @Override public void run() {
                     try {
                         editor.print();
                     } catch (PrinterException e) {
                         PopupDialog.showError("Error printing editor contents:\n" + e.getMessage());
                     }
                 }
             }.start();
         } finally {
         }
     }


 }


 

}
