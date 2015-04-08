package vega.uplink.commanding.gui;
import javax.swing.Icon;

import herschel.ia.gui.kernel.parts.AbstractVariableEditorComponent;
import vega.IconResources;
import vega.hipe.gui.xmlutils.HtmlEditorKit;
import vega.uplink.commanding.Fecs;
import vega.uplink.commanding.Por;
import vega.uplink.commanding.PorChecker;
import vega.uplink.commanding.PorUtils;
import vega.uplink.commanding.Simulation;
import vega.uplink.commanding.SimulationContext;
import vega.uplink.commanding.SuperPor;
import vega.uplink.commanding.itl.ItlParser;
import vega.uplink.commanding.itl.gui.ITLEditorKit;
import vega.uplink.planning.gui.ScheduleViewer;
import vega.uplink.pointing.PointingBlock;
import vega.uplink.pointing.Ptr;
import vega.uplink.pointing.PtrSegment;
import herschel.ia.gui.apps.components.util.BottomPanel;
import herschel.ia.gui.apps.components.util.TextComponentFindAction;
import herschel.ia.gui.kernel.SiteAction;
import herschel.ia.gui.kernel.menus.ActionBars;
import herschel.ia.gui.kernel.menus.Insert;
import herschel.ia.gui.kernel.menus.Retarget;
import herschel.ia.gui.kernel.parts.AbstractEditorAction;
import herschel.ia.jconsole.util.TextLineNumber;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.print.PrinterException;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.text.JTextComponent;

import vega.uplink.Properties;
//import vega.uplink.pointing.PointingElement;
import herschel.ia.jconsole.gui.JSearchDialog;
import static herschel.ia.gui.kernel.menus.Insert.MAIN;
import static herschel.ia.gui.kernel.menus.Insert.Extension.EDIT_ADDON;
import static herschel.ia.gui.kernel.menus.Insert.Extension.FILE_ADDON;
import static herschel.ia.gui.kernel.menus.Insert.Scheme.MENU;
import static herschel.ia.gui.kernel.util.Constants.CTRL;
import herschel.ia.gui.kernel.util.IconLoader;
import herschel.ia.gui.kernel.util.PopupDialog;
import static herschel.ia.gui.kernel.SiteAction.Style.AS_PUSH;

public class SuperPorItlEditor extends AbstractVariableEditorComponent<SuperPor> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String NEWLINE = "\n";
	
	SuperPor por;
	//JEditorPane editor;
	JEditorPane editor;
	JScrollPane scroll;
	JSearchDialog searchDialog;
	public SuperPorItlEditor(){
		super(new BorderLayout());
	}
	public void init(){
		
		JPanel topPanel=new JPanel();
		topPanel.setLayout(new BorderLayout());
		JPanel buttonsPanel=new JPanel();
		//buttonsPanel.setLayout(new BorderLayout());
		JLabel classLabel=new JLabel(por.getName());
		classLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		JButton button=new JButton("Finish Edditing");
		button.setAlignmentX(Component.LEFT_ALIGNMENT);
		
        button.addActionListener(new ActionListener() {
        	 
            public void actionPerformed(ActionEvent e)
            {
            	finishEdting();
            }
        });
		JButton buttonSave=new JButton("Save POR file");
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
			JButton orcdCheck=new JButton("ORCD Check");
			orcdCheck.addActionListener(new ActionListener() {
		        	 
		            public void actionPerformed(ActionEvent e)
		            {
		            	//JSearchDialog searchDialog=new JSearchDialog(editor,ptr.getName());
		            	//searchDialog.setVisible(true);
		            	ORCDCheck();
		    			
		            }
		        });
		button.setAlignmentX(Component.LEFT_ALIGNMENT);
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel,BoxLayout.LINE_AXIS));
		buttonsPanel.add(buttonSave);
		
		buttonsPanel.add(button);
		buttonsPanel.add(buttonSearch);
		buttonsPanel.add(orcdCheck);
		topPanel.add(buttonsPanel,BorderLayout.WEST);
		topPanel.add(classLabel,BorderLayout.EAST);
		JPanel global = new JPanel (new BorderLayout());
		global.add(topPanel,BorderLayout.NORTH);
		editor = new JEditorPane();
		editor.setEditorKitForContentType(ITLEditorKit.ITL_MIME_TYPE, new ITLEditorKit());
		editor.setContentType(ITLEditorKit.ITL_MIME_TYPE);
		JScrollPane scroll=new JScrollPane(editor);
		//scroll.setRowHeaderView(new TextLineNumber(editor));
		setText();
		searchDialog=new JSearchDialog(editor,por.getName());
		global.add(scroll,BorderLayout.CENTER);
		this.add(new BottomPanel(this, global));
		ActionBars menus = getArea().getActionBars(getId());
		SiteAction print = new PrintAction();
		SiteAction saveAs = new AbstractEditorAction("SaveAs") {
            @Override public void actionPerformed(ActionEvent e) {
                saveAs();
            }
        };
		SiteAction saveastxt = new AbstractEditorAction("SaveAsItlFile") {
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
                    searchDialog = new JSearchDialog(editor, por.getName());
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


		menus.retarget(Retarget.SAVE_AS, saveAs);
		menus.retarget(Retarget.PRINT, print);
		//menus.retarget(Retarget.REFRESH, finishediting);
		menus.retarget(Retarget.SAVE, save);
        menus.retarget(Retarget.CUT, cut);
        menus.retarget(Retarget.COPY, copy);
        menus.retarget(Retarget.PASTE, paste);

        menus.insert(new Insert(MENU, MAIN, FILE_ADDON), saveastxt);

        menus.insert(new Insert(MENU, MAIN, EDIT_ADDON), finishediting);
        menus.insert(new Insert(MENU, MAIN, EDIT_ADDON), search);
        menus.insert(new Insert(MENU, MAIN, EDIT_ADDON), goTo);
        //menus.insert(new Insert(MENU, MAIN, EDIT_ADDON), undo);
        //menus.insert(new Insert(MENU, MAIN, EDIT_ADDON), redo);
        menus.retarget(Retarget.SELECT_ALL, select);
        menus.insert(new Insert(MENU, MAIN, EDIT_ADDON), insearch);

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


				Por tempPor = ItlParser.parseItlExplicitTime(text, 1000);
			
				por.removeAllSequences();
				por.setSequences(tempPor.getSequences());
				por.setSequences(tempPor.getSequences());
				por.setInitDataStore(tempPor.getInitDataStore());
				por.setInitMemory(tempPor.getInitMemory());
				por.setInitModes(tempPor.getInitModes());
				por.setInitMS(tempPor.getInitMS());
				String warnings = PorChecker.checkPor(por);
				
				setText();
				if (!warnings.equals("")) {
					JOptionPane.showMessageDialog(SuperPorItlEditor.this,
							warnings,
						    "Warning",
						    JOptionPane.WARNING_MESSAGE);
				}

        	PorUtils.savePor(por);
	            

	         

        } catch (Exception e1) {
					JOptionPane.showMessageDialog(SuperPorItlEditor.this,
						    e1.getMessage(),
						    "Error",
						    JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
					setText();
				}		
	}
	public void saveAsTxt(){
        try{
	          JFileChooser chooser = new JFileChooser();
	          try{
	        	  chooser.setSelectedFile(new File(por.getPath()+"/"+por.getName()));
	          }catch (Exception e3){
	        	chooser.setCurrentDirectory(new File(Properties.getProperty(Properties.DEFAULT_PLANNING_DIRECTORY)));
	          }
	          chooser.setMultiSelectionEnabled(false);
	          
	          int option = chooser.showSaveDialog(SuperPorItlEditor.this);
	          //File sf;
	          if (option == JFileChooser.APPROVE_OPTION) {
	            File sf = chooser.getSelectedFile();
	            por.setName(sf.getName());
	            por.setPath(sf.getParent());
 
	    		try{
	    			PrintWriter writer = new PrintWriter(sf.getAbsolutePath(), "UTF-8");
	    			setText();
	    			writer.print(editor.getText());
	    			writer.close();
	    		}catch (Exception e){
	    			e.printStackTrace();
	    		}
	            
	          }
	          else {
	            //itlbar.setText("You canceled.");
	          }
	         

          } catch (Exception e1) {
					JOptionPane.showMessageDialog(SuperPorItlEditor.this,
						    e1.getMessage(),
						    "Error",
						    JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
					setText();
				}	
	}
	public void saveAs(){
        try{
	          JFileChooser chooser = new JFileChooser();
	          try{
	        	  chooser.setSelectedFile(new File(por.getPath()+"/"+por.getName()));
	          }catch (Exception e3){
	        	chooser.setCurrentDirectory(new File(Properties.getProperty(Properties.DEFAULT_PLANNING_DIRECTORY)));
	          }
	          chooser.setMultiSelectionEnabled(false);
	          
	          int option = chooser.showSaveDialog(SuperPorItlEditor.this);
	          //File sf;
	          if (option == JFileChooser.APPROVE_OPTION) {
	            File sf = chooser.getSelectedFile();
	            por.setName(sf.getName());
	            por.setPath(sf.getParent());
 
	            save();
	            
	          }
	          else {
	            //itlbar.setText("You canceled.");
	          }
	         

          } catch (Exception e1) {
					JOptionPane.showMessageDialog(SuperPorItlEditor.this,
						    e1.getMessage(),
						    "Error",
						    JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
					setText();
				}	
	}
	public void ORCDCheck(){
		try {
			SimulationContext context=new SimulationContext();
			Ptr ptr = new Ptr();
			PtrSegment segment=new PtrSegment("MTP_001");
			ptr.addSegment(segment);
			context.setPtr(ptr);
			//String segmentName=(String) schedule.getPtslSegment().getName();
			//context.setPlanningPeriod(segmentName);
			//PtrSegment segment=ptr.getSegments()[0];
			//PointingBlock[] blocks=segment.getBlocks();
			//if (blocks.length==0) LOG.info("PTR has no blocks");
			//for (int i=0;i<blocks.length;i++){
			//		context.getHistoryModes().add(blocks[i].getStartTime().getTime(), "PTR_"+blocks[i].getType(), "PTR", blocks[i].getStartTime().getTime());
			//}
			//Fecs fecs = schedule.getFecs();
			//if (fecs!=null) context.setFecs(fecs);
			Simulation simulation=new Simulation(por,context);
			SimulationContext resultContext = simulation.runOnlyText(false);
			String messages="";
			messages=messages+resultContext.getLog();
			messages=messages.replace("\n", "\n<br>");
        	HtmlEditorKit htmlEditor=new HtmlEditorKit("Commanding Check",messages);

		}
		catch (Exception e1) {
			JOptionPane.showMessageDialog(SuperPorItlEditor.this,
				    e1.getMessage(),
				    "Error",
				    JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		}
	}
	
	public void finishEdting(){
    	String text = editor.getText();
        
    	int pos = editor.getCaretPosition();
		try {
			StringReader sr= new StringReader(text); // wrap your String
			BufferedReader br= new BufferedReader(sr); // wrap your StringReader
			SuperPor tempPor = ItlParser.parseItl(br, ItlParser.getDefaultEvents(por.getPath()+"/"+por.getName()), por.getPath(), por.getStartDate().toDate(), por.getEndDate().toDate());
			//Por tempPor = ItlParser.parseItlExplicitTime(br, 1000);
			
			por.removeAllSequences();
			por.setSequences(tempPor.getSequences());
			por.setInitDataStore(tempPor.getInitDataStore());
			por.setInitMemory(tempPor.getInitMemory());
			por.setInitModes(tempPor.getInitModes());
			por.setInitMS(tempPor.getInitMS());
			por.setVersion(tempPor.getVersion());
			String warnings = PorChecker.checkPor(por);
			
			setText();
			editor.setCaretPosition(pos);
			if (!warnings.equals("")) {
				JOptionPane.showMessageDialog(SuperPorItlEditor.this,
						warnings,
					    "Warning",
					    JOptionPane.WARNING_MESSAGE);
			}
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(SuperPorItlEditor.this,
				    e1.getMessage(),
				    "Error",
				    JOptionPane.ERROR_MESSAGE);
			// TODO Auto-generated catch block
			e1.printStackTrace();
			setText();
			editor.setCaretPosition(pos);
			
		} 	
	}
	public void setPor(SuperPor por){
		this.por=por;
		init();
	}
    public boolean makeEditorContent() {
    	setPor(getValue());
    	return true;
    }
    
    public void setText(){
    	editor.setText(ItlParser.SuperPortoITL(por));
    }
    
	public Icon getComponentIcon() {
		// TODO Auto-generated method stub
        try {
            URL resource = PorItlEditor.class.getResource(IconResources.HUS_ICON);
            BufferedImage imageIcon = ImageIO.read(resource);
            return new ImageIcon(imageIcon);
    } catch (IOException e) {
           
            e.printStackTrace();
            return null;
    }
	}
	@Override
	protected Class<? extends SuperPor> getVariableType() {
		return SuperPor.class;
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


