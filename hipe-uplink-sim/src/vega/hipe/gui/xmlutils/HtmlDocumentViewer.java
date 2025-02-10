/*

   Licensed under GNU Lesser General Public License (http://www.gnu.org/licenses/lgpl.html)

 */
package vega.hipe.gui.xmlutils;

import java.awt.BorderLayout;
//import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
//import javax.swing.JScrollPane;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;




import vega.IconResources;
//import vega.uplink.pointing.Ptr;
import vega.uplink.pointing.gui.PtrXmlEditor;
import herschel.ia.gui.apps.components.util.BottomPanel;
import herschel.ia.gui.kernel.parts.AbstractVariableEditorComponent;

public class HtmlDocumentViewer extends AbstractVariableEditorComponent<HtmlDocument>{
	HtmlDocument document;
	public HtmlDocumentViewer(){
		super(new BorderLayout());
	}
	
    public boolean makeEditorContent() {
    	setDocument(getValue());
    	return true;
    }

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public void setDocument(HtmlDocument doc){
		this.document=doc;
		init();
	}
	public void init(){
        JEditorPane jEditorPane = new JEditorPane();
        
        // make it read-only
        jEditorPane.setEditable(false);
                
        // add an html editor kit
        HTMLEditorKit kit = new HTMLEditorKit();
        jEditorPane.setEditorKit(kit);
        
        // add some styles to the html
        StyleSheet styleSheet = kit.getStyleSheet();
        String[] rules = document.getRules();
        for (int i=0;i<rules.length;i++){
        	styleSheet.addRule(rules[i]);
        }
        Document doc = kit.createDefaultDocument();
        jEditorPane.setDocument(doc);
        jEditorPane.setText(document.getHtmlString());
        JPanel buttonsPanel=new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel,BoxLayout.LINE_AXIS));
        JButton saveButton=new JButton("Save Report");
	    ActionListener ptral=new ActionListener() {
	        public void actionPerformed(ActionEvent ae) {
	          JFileChooser chooser = new JFileChooser();
	          chooser.setMultiSelectionEnabled(false);
	          int option = chooser.showSaveDialog(HtmlDocumentViewer.this);
	          if (option == JFileChooser.APPROVE_OPTION) {
	            File sf = chooser.getSelectedFile();
	            try{
	            	document.saveReportToFile(sf.getAbsolutePath());
	            }catch (Exception e){
					JOptionPane.showMessageDialog(HtmlDocumentViewer.this,
						    e.getMessage(),
						    "Error",
						    JOptionPane.ERROR_MESSAGE);
					e.printStackTrace();

	            }
	          }
	          else {

	          }
	        }

	      };
	      saveButton.addActionListener(ptral);
        buttonsPanel.add(saveButton);
        buttonsPanel.add(Box.createHorizontalGlue());

        
        JPanel global=new JPanel(new BorderLayout());
		global.add(buttonsPanel,BorderLayout.NORTH);
		global.add(new JScrollPane(jEditorPane),BorderLayout.CENTER);
		this.add(new BottomPanel(this, global));


    }
	
	public Icon getComponentIcon() {
        try {
            URL resource = PtrXmlEditor.class.getResource(IconResources.HUS_ICON);
            BufferedImage imageIcon = ImageIO.read(resource);
            return new ImageIcon(imageIcon);
    } catch (IOException e) {
           
            e.printStackTrace();
            return null;
    }
	}
	@Override
	protected Class<? extends HtmlDocument> getVariableType() {
		return HtmlDocument.class;
	}
}
