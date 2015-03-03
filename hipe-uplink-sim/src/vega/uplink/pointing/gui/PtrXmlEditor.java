package vega.uplink.pointing.gui;

import herschel.ia.gui.apps.components.util.BottomPanel;
import herschel.ia.gui.apps.components.util.TextComponentFindAction;
import herschel.ia.gui.kernel.SiteAction;
import herschel.ia.gui.kernel.menus.ActionBars;
import herschel.ia.gui.kernel.menus.Insert;
import herschel.ia.gui.kernel.menus.Retarget;
import herschel.ia.gui.kernel.parts.AbstractEditorAction;
import herschel.ia.gui.kernel.parts.AbstractVariableEditorComponent;
import herschel.ia.jconsole.util.TextLineNumber;
import herschel.ia.jconsole.util.UndoableTextEditor;
import herschel.share.swing.Components;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.print.PrinterException;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BoundedRangeModel;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.text.JTextComponent;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import vega.uplink.DateUtil;
import vega.uplink.Properties;
import vega.uplink.pointing.PointingBlock;
import vega.uplink.pointing.PointingElement;
import vega.uplink.pointing.Ptr;
import vega.uplink.pointing.PtrChecker;
import vega.uplink.pointing.PtrSegment;
import vega.uplink.pointing.PtrUtils;
import vega.uplink.pointing.gui.xmlutils.HtmlDocumentViewer;
//import vega.uplink.pointing.PointingElement;
import vega.uplink.pointing.gui.xmlutils.XMLEditorKit;
import vega.uplink.pointing.gui.xmlutils.XMLTextEditor;
import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED;
import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER;
import herschel.ia.jconsole.gui.JSearchDialog;
import static herschel.ia.gui.kernel.menus.Insert.MAIN;
import static herschel.ia.gui.kernel.menus.Insert.Extension.EDIT;
import static herschel.ia.gui.kernel.menus.Insert.Extension.EDIT_ADDON;
import static herschel.ia.gui.kernel.menus.Insert.Extension.EDIT_BEGIN;
import static herschel.ia.gui.kernel.menus.Insert.Extension.FILE_ADDON;
import static herschel.ia.gui.kernel.menus.Insert.Extension.UNDO;
import static herschel.ia.gui.kernel.menus.Insert.Extension.ADDON_1_BEGIN;
import static herschel.ia.gui.kernel.menus.Insert.Scheme.MENU;
import static herschel.ia.gui.kernel.menus.Insert.Scheme.POPUP;
import static herschel.ia.gui.kernel.menus.Insert.Scheme.TOOLBAR;
import static herschel.ia.gui.kernel.util.Constants.CTRL;
import herschel.ia.gui.kernel.util.IconLoader;
import herschel.ia.gui.kernel.util.PopupDialog;
import static herschel.ia.gui.kernel.SiteAction.Style.AS_PUSH;

public class PtrXmlEditor extends AbstractVariableEditorComponent<Ptr> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String NEWLINE = "\n";
	
	Ptr ptr;
	//JEditorPane editor;
	XMLTextEditor editor;
	JScrollPane scroll;
	JSearchDialog searchDialog;
	public PtrXmlEditor(){
		super(new BorderLayout());
	}
	public void init(){
		
		JPanel topPanel=new JPanel();
		topPanel.setLayout(new BorderLayout());
		JPanel buttonsPanel=new JPanel();
		buttonsPanel.setLayout(new BorderLayout());
		JLabel classLabel=new JLabel(ptr.getName());
		classLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		JButton button=new JButton("Finish Edditing");
		button.setAlignmentX(Component.LEFT_ALIGNMENT);
		
        button.addActionListener(new ActionListener() {
        	 
            public void actionPerformed(ActionEvent e)
            {
            	finishEdting();
            }
        });
		JButton buttonSave=new JButton("Save PTR file");
	       buttonSave.addActionListener(new ActionListener() {
	        	 
	            public void actionPerformed(ActionEvent e)
	            {
 
	            	saveAs();
	    			
	            }
	        });
	       
			JButton buttonSearch=new JButton("Search");
			buttonSearch.addActionListener(new ActionListener() {
		        	 
		            public void actionPerformed(ActionEvent e)
		            {
		            	//JSearchDialog searchDialog=new JSearchDialog(editor,ptr.getName());
		            	//searchDialog.setVisible(true);
		            	search();
		    			
		            }
		        });

		button.setAlignmentX(Component.LEFT_ALIGNMENT);
		buttonsPanel.add(buttonSave,BorderLayout.WEST);
		
		buttonsPanel.add(button,BorderLayout.CENTER);
		buttonsPanel.add(buttonSearch,BorderLayout.EAST);
		topPanel.add(buttonsPanel,BorderLayout.WEST);
		topPanel.add(classLabel,BorderLayout.EAST);
		JPanel global = new JPanel (new BorderLayout());
		global.add(topPanel,BorderLayout.NORTH);
		editor = new XMLTextEditor();
		editor.setText(ptr.toXml());
		scroll=new JScrollPane(editor);
		scroll.setRowHeaderView(new TextLineNumber(editor));
		searchDialog=new JSearchDialog(editor,ptr.getName());
		global.add(scroll,BorderLayout.CENTER);
		this.add(new BottomPanel(this, global));
		ActionBars menus = getArea().getActionBars(getId());
		SiteAction print = new PrintAction();
		SiteAction saveAs = new AbstractEditorAction("SaveAs") {
            @Override public void actionPerformed(ActionEvent e) {
                saveAs();
            }
        };
		SiteAction saveastxt = new AbstractEditorAction("SaveAsTextFile") {
            @Override public void actionPerformed(ActionEvent e) {
                saveAsTxt();
            }
        };
		SiteAction save = new AbstractEditorAction("Save") {
            @Override public void actionPerformed(ActionEvent e) {
                save();
            }
        };
		SiteAction cut = new AbstractEditorAction("Cut") {
            @Override public void actionPerformed(ActionEvent e) {
                cut();
            }
        };
		SiteAction copy = new AbstractEditorAction("Copy") {
            @Override public void actionPerformed(ActionEvent e) {
                copy();
            }
        };
		SiteAction paste = new AbstractEditorAction("Paste") {
            @Override public void actionPerformed(ActionEvent e) {
                paste();
            }
        };
		/*SiteAction search = new AbstractEditorAction("Search") {
            @Override public void actionPerformed(ActionEvent e) {
                search();
            }
        };*/
        SiteAction search = new AbstractEditorAction(AS_PUSH, "FindReplace", "Find/Replace ...", CTRL + "F", IconLoader.getKernelIcon("Search.gif")) {
            @Override public void actionPerformed(ActionEvent e) {
                if (searchDialog == null) {
                    searchDialog = new JSearchDialog(editor, ptr.getName());
                }
                searchDialog.search();
            }
        };
		SiteAction goTo = new AbstractEditorAction(AS_PUSH, "GoTo", "Go To Line", CTRL + "L", IconLoader.getKernelIcon("Goto.gif")) {
            @Override public void actionPerformed(ActionEvent e) {
                goToLine();
            }
        };
        
        SiteAction insearch = new AbstractEditorAction(AS_PUSH, "search", "Incremental search", CTRL + "I", IconLoader.getKernelIcon("IncrementalSearch.gif")) {
            @Override public void actionPerformed(ActionEvent e) {
                JTextComponent textComponent = editor;
                ActionEvent event = new ActionEvent(textComponent, Integer.MAX_VALUE, "");
                TextComponentFindAction.ACTION.actionPerformed(event);
            }
        };

		SiteAction undo = new AbstractEditorAction("UnDo") {
            @Override public void actionPerformed(ActionEvent e) {
                undo();
            }
        };
		SiteAction redo = new AbstractEditorAction("ReDo") {
            @Override public void actionPerformed(ActionEvent e) {
                redo();
            }
        };
		SiteAction select = new AbstractEditorAction("SelectAll") {
            @Override public void actionPerformed(ActionEvent e) {
                selectAll();
            }
        };
        
		SiteAction finishediting = new AbstractEditorAction("FinishEditing") {
            @Override public void actionPerformed(ActionEvent e) {
            	finishEdting();
            }
        };
		SiteAction gotoslice = new AbstractEditorAction("GoToSlice") {
            @Override public void actionPerformed(ActionEvent e) {
                goToSlice();
            }
        };
		SiteAction gotoblock = new AbstractEditorAction("GoToBlock") {
            @Override public void actionPerformed(ActionEvent e) {
                goToBlock();
            }
        };
		SiteAction gotoblockat = new AbstractEditorAction("GoToBlockAt") {
            @Override public void actionPerformed(ActionEvent e) {
                goToBlockAt();
            }
        };
		SiteAction gotovstp= new AbstractEditorAction("GoToBlockVstp") {
            @Override public void actionPerformed(ActionEvent e) {
                goToVstp();
            }
        };

		menus.retarget(Retarget.SAVE_AS, saveAs);
		menus.retarget(Retarget.PRINT, print);
		//menus.retarget(Retarget.REFRESH, finishediting);
		menus.retarget(Retarget.SAVE, save);
        menus.retarget(Retarget.CUT, cut);
        menus.retarget(Retarget.COPY, copy);
        menus.retarget(Retarget.PASTE, paste);
        menus.retarget(Retarget.UNDO, undo);
        menus.retarget(Retarget.REDO, redo);  
        menus.insert(new Insert(MENU, MAIN, FILE_ADDON), saveastxt);

        menus.insert(new Insert(MENU, MAIN, EDIT_ADDON), finishediting);
        menus.insert(new Insert(MENU, MAIN, EDIT_ADDON), search);
        menus.insert(new Insert(MENU, MAIN, EDIT_ADDON), goTo);
        //menus.insert(new Insert(MENU, MAIN, EDIT_ADDON), undo);
        //menus.insert(new Insert(MENU, MAIN, EDIT_ADDON), redo);
        menus.retarget(Retarget.SELECT_ALL, select);
        menus.insert(new Insert(MENU, MAIN, EDIT_ADDON), insearch);
        menus.insert(new Insert(MENU, MAIN, EDIT_ADDON), gotoslice);
        menus.insert(new Insert(MENU, MAIN, EDIT_ADDON), gotoblock);
        menus.insert(new Insert(MENU, MAIN, EDIT_ADDON), gotoblockat);
        menus.insert(new Insert(MENU, MAIN, EDIT_ADDON), gotovstp);
        //menus.insert(new Insert(MENU, MAIN, ADDON_1_BEGIN), gotovstp);
        // menus.insert(new Insert(TOOLBAR, MAIN, EDIT), goTo);


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
    public void goToSlice() {
    	int max=ptr.getSegments()[0].getAllSlices().length;
        String textLine = PopupDialog.askForInput(this, "Go to Slice (1-"+max+"):");
        if (textLine == null) {
            return;
        }
        try {
            int line = Integer.parseInt(textLine);
            if (line<1 || line>max) {
                PopupDialog.showError(this, textLine + " is not in the valid range (1-"+max+")");
                goToSlice();
            }else{
            	goToSlice(line);
            }
        } catch (NumberFormatException e) {
            PopupDialog.showError(this, textLine + " is not a valid number.");
            goToSlice();
        } catch (IllegalArgumentException e) {
            PopupDialog.showError(this, e.getMessage());
            goToSlice();
        }
    }
    public void goToBlock() {
    	int max=ptr.getSegments()[0].getBlocks().length;
        String textLine = PopupDialog.askForInput(this, "Go to Block (1-"+max+"):");
        if (textLine == null) {
            return;
        }
        try {
            int line = Integer.parseInt(textLine);
            if (line<1 || line>max) {
                PopupDialog.showError(this, textLine + " is not in the valid range (1-"+max+")");
                goToBlock();
            }else{
            	goToBlock(line);
            }
        } catch (NumberFormatException e) {
            PopupDialog.showError(this, textLine + " is not a valid number.");
            goToBlock();
        } catch (IllegalArgumentException e) {
            PopupDialog.showError(this, e.getMessage());
            goToBlock();
        }
    }
    public void goToVstp() {
    	PtrSegment[] seg = ptr.getSegments();
    	//String vstps="";
    	int[] fv = seg[0].getVstpNumbers();
    	int[] lv=seg[seg.length-1].getVstpNumbers();
    	int min=fv[0];
    	int max=lv[lv.length-1];
    	String vstps=min+" ... "+max;
        String textLine = PopupDialog.askForInput(this, "Go to Vstp ("+vstps+"):");
        if (textLine == null) {
            return;
        }
        try {
            int line = Integer.parseInt(textLine);
            if (line<min || line>max) {
                PopupDialog.showError(this, textLine + " is not in the valid range ("+vstps+")");
                goToVstp();
            }else{
            	goToVstp(line);
            }
        } catch (NumberFormatException e) {
            PopupDialog.showError(this, textLine + " is not a valid number.");
            goToVstp();
        } catch (IllegalArgumentException e) {
            PopupDialog.showError(this, e.getMessage());
            goToVstp();
        }
    }
    public void goToBlockAt() {
    	Date init = ptr.getStartDate().toDate();
    	Date fin = ptr.getEndDate().toDate();
    	String validDates=DateUtil.dateToZulu(init)+" - "+DateUtil.dateToZulu(fin)+NEWLINE;
        String textLine = PopupDialog.askForInput(this, validDates+"Go to Block At (yyyy-MM-ddTHH:mm:ss) :");
        if (textLine == null) {
            return;
        }
        //try {
            //Date date;
			try {
				Date date = DateUtil.parse(textLine);
				if (date.before(init) || date.after(fin)){
					PopupDialog.showError(this, textLine + " is not in the valid range:"+NEWLINE+validDates);
					goToBlockAt();
				}else{
				//System.out.println(date);
					goToBlockAt(date);
				}
			} catch (ParseException e) {
				PopupDialog.showError(this, textLine + " is not a valid date.");
				e.printStackTrace();
				goToBlockAt();
			}catch (IllegalArgumentException e) {
	            PopupDialog.showError(this, e.getMessage());
	            goToBlockAt();
	        }
            //goToBlockAt(date);
        //} /*catch (Exception e) {
 
        //} */
    }
    public void goToVstp(int number){
    	//PointingBlock block = ptr.getBlockAt(date);
    	int loc = editor.getText().indexOf("vstpNumber>"+String.format("%03d", number));
    	if (loc<0){
    		Date date = ptr.getSegments()[0].getAllBlocksOfVstp(number).getBlocks()[0].getStartTime();
    		goToBlockAt(date);
    	}
    	editor.setCaretPosition(loc);
    }
    public void goToBlockAt(Date date){
    	PointingBlock block = ptr.getBlockAt(date);
    	int loc = editor.getText().indexOf(DateUtil.dateToZulu(block.getStartTime()));
    	editor.setCaretPosition(loc);
    }
    public void goToSlice(int slice){
    	int loc = editor.getText().indexOf("<!--OBS SLICE #"+String.format("%04d", slice));
    	editor.setCaretPosition(loc);
    }
    public void goToBlock(int block){
    	int loc = editor.getText().indexOf("!-- BLOCK #"+block+"-->");
    	editor.setCaretPosition(loc);
    }
    public void undo(){
    	editor.undo();
    }
    
    public void redo(){
    	editor.redo();
    }
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

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			InputStream stream = new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));
			Document doc;

				doc = dBuilder.parse(stream);
				doc.getDocumentElement().normalize();
				Ptr tempPtr = PtrUtils.readPTRfromDoc(doc);
			
				ptr.regenerate(tempPtr);
				String warnings = PtrChecker.checkPtr(ptr);
				
				editor.setText(ptr.toXml());
				if (!warnings.equals("")) {
					JOptionPane.showMessageDialog(PtrXmlEditor.this,
							warnings,
						    "Warning",
						    JOptionPane.WARNING_MESSAGE);
				}

        	PtrUtils.savePTR(ptr);
	            

	         

        } catch (Exception e1) {
					JOptionPane.showMessageDialog(PtrXmlEditor.this,
						    e1.getMessage(),
						    "Error",
						    JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
					editor.setText(ptr.toXml());
				}		
	}
	public void saveAsTxt(){
        try{
	          JFileChooser chooser = new JFileChooser();
	          try{
	        	  chooser.setSelectedFile(new File(ptr.getPath()+"/"+ptr.getName()));
	          }catch (Exception e3){
	        	chooser.setCurrentDirectory(new File(Properties.getProperty(Properties.DEFAULT_PLANNING_DIRECTORY)));
	          }
	          chooser.setMultiSelectionEnabled(false);
	          
	          int option = chooser.showSaveDialog(PtrXmlEditor.this);
	          //File sf;
	          if (option == JFileChooser.APPROVE_OPTION) {
	            File sf = chooser.getSelectedFile();
	            ptr.setName(sf.getName());
	            ptr.setPath(sf.getParent());
 
	    		try{
	    			PrintWriter writer = new PrintWriter(sf.getAbsolutePath(), "UTF-8");
	    			writer.print(ptr.toXml());
	    			writer.close();
	    		}catch (Exception e){
	    			e.printStackTrace();
	    		}
	            
	          }
	          else {
	            //itlbar.setText("You canceled.");
	          }
	         

          } catch (Exception e1) {
					JOptionPane.showMessageDialog(PtrXmlEditor.this,
						    e1.getMessage(),
						    "Error",
						    JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
					editor.setText(ptr.toXml());
				}	
	}
	public void saveAs(){
        try{
	          JFileChooser chooser = new JFileChooser();
	          try{
	        	  chooser.setSelectedFile(new File(ptr.getPath()+"/"+ptr.getName()));
	          }catch (Exception e3){
	        	chooser.setCurrentDirectory(new File(Properties.getProperty(Properties.DEFAULT_PLANNING_DIRECTORY)));
	          }
	          chooser.setMultiSelectionEnabled(false);
	          
	          int option = chooser.showSaveDialog(PtrXmlEditor.this);
	          //File sf;
	          if (option == JFileChooser.APPROVE_OPTION) {
	            File sf = chooser.getSelectedFile();
	            ptr.setName(sf.getName());
	            ptr.setPath(sf.getParent());
 
	            save();
	            
	          }
	          else {
	            //itlbar.setText("You canceled.");
	          }
	         

          } catch (Exception e1) {
					JOptionPane.showMessageDialog(PtrXmlEditor.this,
						    e1.getMessage(),
						    "Error",
						    JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
					editor.setText(ptr.toXml());
				}	
	}
	
	public void finishEdting(){
    	String text = editor.getText();
        
    	int pos = editor.getCaretPosition();
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			InputStream stream = new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));
			Document doc;

			doc = dBuilder.parse(stream);
			doc.getDocumentElement().normalize();
			Ptr tempPtr = PtrUtils.readPTRfromDoc(doc);
			ptr.regenerate(tempPtr);
			String warnings = PtrChecker.checkPtr(ptr);
			
			editor.setText(ptr.toXml());
			editor.setCaretPosition(pos);
			if (!warnings.equals("")) {
				JOptionPane.showMessageDialog(PtrXmlEditor.this,
						warnings,
					    "Warning",
					    JOptionPane.WARNING_MESSAGE);
			}
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(PtrXmlEditor.this,
				    e1.getMessage(),
				    "Error",
				    JOptionPane.ERROR_MESSAGE);
			// TODO Auto-generated catch block
			e1.printStackTrace();
			editor.setText(ptr.toXml());
			editor.setCaretPosition(pos);
			
		} 	
	}
	public void setPtr(Ptr ptr){
		this.ptr=ptr;
		init();
	}
    public boolean makeEditorContent() {
    	setPtr(getValue());
    	return true;
    }
    
	public Icon getComponentIcon() {
		// TODO Auto-generated method stub
        try {
            URL resource = PtrXmlEditor.class.getResource("/vega/vega.gif");
            BufferedImage imageIcon = ImageIO.read(resource);
            return new ImageIcon(imageIcon);
    } catch (IOException e) {
           
            e.printStackTrace();
            return null;
    }
	}
	@Override
	protected Class<? extends Ptr> getVariableType() {
		return Ptr.class;
	}
	
    public final void setGlobalScroll(boolean enabled) {
    
	
        setGlobalScroll(enabled, enabled); // horizontal scroll bar not disabled: HCSS-13801
    }
    

    /*private void searchAction(){
    	JSearchDialog searchDialog=new JSearchDialog(editor);
    	searchDialog.setVisible(true);
    }*/
    
    private class PrintAction extends AbstractEditorAction {
        private static final long serialVersionUID = 1L;

        PrintAction() {
            super("Print", CTRL + "P");
            setText("Print...");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                //showDecorations(false);
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
                //showDecorations(true);
            }
        }


    }

   
    

}
