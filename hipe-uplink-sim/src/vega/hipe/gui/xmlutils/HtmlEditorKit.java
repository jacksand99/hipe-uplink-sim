/*

   Licensed under GNU Lesser General Public License (http://www.gnu.org/licenses/lgpl.html)

 */
package vega.hipe.gui.xmlutils;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

import org.apache.commons.lang3.StringEscapeUtils;

//import vega.uplink.commanding.gui.SimulationView;
//import vega.uplink.pointing.gui.PtrXmlEditor;

/**
 * A complete Java class that demonstrates how to create an HTML viewer with styles,
 * using the JEditorPane, HTMLEditorKit, StyleSheet, and JFrame.
 * 
 * @author alvin alexander, devdaily.com.
 *
 */
public class HtmlEditorKit extends JFrame
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String htmlString;
	String title;
    static String[] rules={"body {color:#000; font-family:times; margin: 4px; }",
      		"h1 {color: blcak;}",
      		"h2 {color: black;}",
      		"pre {font : 10px monaco; color : black; background-color : #fafafa; }",
      		"table.gridtable {font-family: verdana,arial,sans-serif; font-size:11px; color:#333333; border-width: 1px; border-color: #666666; border-collapse: collapse;}",
      		"table.gridtable th {border-width: 1px;padding: 8px;border-style: solid;border-color: #666666;background-color: #dedede;}",
      		"table.gridtable td {border-width: 1px;padding: 8px;border-style: solid;border-color: #666666;background-color: #ffffff;}"
      };

	
  public static String escapeHTML(String original){
	  return StringEscapeUtils.escapeHtml4(original);
  }
  public static void main(String[] args)
  {
      String text = "<html>\n"
              + "<body>\n"
              + "<h1>Welcome!</h1>\n"
              + "<h2>This is an H2 header</h2>\n"
              + "<p>This is some sample text</p>\n"
              + "<table class=\"gridtable\">\n"
              + "<tr>\n"
              + "	<th>Info Header 1</th><th>Info Header 2</th><th>Info Header 3</th>\n"
              + "</tr>\n"
              + "<tr>\n"
              + "	<td>Text 1A</td><td>Text 1B</td><td>Text 1C</td>\n"
              + "</tr>\n"
              + "<tr>\n"
              + "	<td>Text 2A</td><td>Text 2B</td><td>Text 2C</td>\n"
              + "</tr>\n"
              + "</table>\n"
                                       
              + "<p><a href=\"http://devdaily.com/blog/\">devdaily blog</a></p>\n"
              +"<pre>"+HtmlEditorKit.escapeHTML("<xml>\t<hola>hola</hola>\n</xml>")+"</pre>"
              + "</body>\n";
    new HtmlEditorKit("Report",text);
  }
 public static String getRulesHTML(){
	  String result="";
	  result=result+"<head><title>Internal stylesheet</title>\n"+
			  "<style type=\"text/css\">\n";
      for (int i=0;i<rules.length;i++){
    	  result=result+rules[i]+"\n";
      }
      result=result+"</style> </head>\n";
	  return result;
  }
  public void saveReportToFile(String fileName) throws FileNotFoundException, UnsupportedEncodingException{
	  String text=new String(htmlString);
	  text=text.replace("<html>", "<html>\n"+getRulesHTML());
		//try{
			PrintWriter writer = new PrintWriter(fileName, "UTF-8");
			writer.print(text);
			writer.close();
		//}catch (Exception e){
			//e.printStackTrace();
		//}
  }
  public HtmlEditorKit(HtmlDocument doc){
	  this(doc.getTitle(),doc.getHtmlString());
  }
  public HtmlEditorKit(String titleText,String text)
  {
	  super();
	  this.setLayout(new BorderLayout());
	  htmlString=text;
	  title=titleText;
	  

	  
    SwingUtilities.invokeLater(new Runnable()
    {
      public void run()
      {
        // create jeditorpane
        JEditorPane jEditorPane = new JEditorPane();
        
        // make it read-only
        jEditorPane.setEditable(false);
        
        // create a scrollpane; modify its attributes as desired
        JScrollPane scrollPane = new JScrollPane(jEditorPane);
        
        // add an html editor kit
        HTMLEditorKit kit = new HTMLEditorKit();
        jEditorPane.setEditorKit(kit);
        
        // add some styles to the html
        StyleSheet styleSheet = kit.getStyleSheet();
        for (int i=0;i<rules.length;i++){
        	styleSheet.addRule(rules[i]);
        }
        /*styleSheet.addRule("body {color:#000; font-family:times; margin: 4px; }");
        styleSheet.addRule("h1 {color: blcak;}");
        styleSheet.addRule("h2 {color: black;}");
        styleSheet.addRule("pre {font : 10px monaco; color : black; background-color : #fafafa; }");
        styleSheet.addRule("table.gridtable {font-family: verdana,arial,sans-serif; font-size:11px; color:#333333; border-width: 1px; border-color: #666666; border-collapse: collapse;}");
        styleSheet.addRule("table.gridtable th {border-width: 1px;padding: 8px;border-style: solid;border-color: #666666;background-color: #dedede;}");
        styleSheet.addRule("table.gridtable td {border-width: 1px;padding: 8px;border-style: solid;border-color: #666666;background-color: #ffffff;}");
*/
        // create some simple html as a string
 
        
        // create a document, set it on the jeditorpane, then add the html
        Document doc = kit.createDefaultDocument();
        jEditorPane.setDocument(doc);
        jEditorPane.setText(htmlString);
        JPanel buttonsPanel=new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel,BoxLayout.LINE_AXIS));
        JButton saveButton=new JButton("Save Report");
	    ActionListener ptral=new ActionListener() {
	        public void actionPerformed(ActionEvent ae) {
	          JFileChooser chooser = new JFileChooser();
	          chooser.setMultiSelectionEnabled(false);
	          //chooser.setCurrentDirectory(new File(defaultDirectory));
	          int option = chooser.showSaveDialog(HtmlEditorKit.this);
	          if (option == JFileChooser.APPROVE_OPTION) {
	            File sf = chooser.getSelectedFile();
	            try{
	            	HtmlEditorKit.this.saveReportToFile(sf.getAbsolutePath());
	            }catch (Exception e){
					JOptionPane.showMessageDialog(HtmlEditorKit.this,
						    e.getMessage(),
						    "Error",
						    JOptionPane.ERROR_MESSAGE);
					// TODO Auto-generated catch block
					e.printStackTrace();

	            }
	            //ptrFile=sf;
	            //defaultDirectory=ptrFile.getParent();
	            //refreshLabels();
	            //String filelist="Nothing";
	            //filelist=sf.getName();
	            //itlbar.setText("ITL file: " + filelist);
	          }
	          else {
	            //itlbar.setText("You canceled.");
	          }
	        }

	      };
	      saveButton.addActionListener(ptral);
        buttonsPanel.add(saveButton);
        buttonsPanel.add(Box.createHorizontalGlue());
        HtmlEditorKit.this.setTitle(title);
        // now add it all to a frame
        //JFrame j = new JFrame(title);
        HtmlEditorKit.this.getContentPane().add(buttonsPanel, BorderLayout.NORTH);
        HtmlEditorKit.this.getContentPane().add(scrollPane, BorderLayout.CENTER);

        // make it easy to close the application
        //j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // display the frame
        HtmlEditorKit.this.setSize(new Dimension(600,600));
        
        // pack it, if you prefer
        //j.pack();
        
        // center the jframe, then make it visible
        HtmlEditorKit.this.setLocationRelativeTo(null);
        HtmlEditorKit.this.setVisible(true);
      }
    });
  }
}
