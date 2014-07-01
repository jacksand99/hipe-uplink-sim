package vega.help;

import herschel.share.util.ObjectUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.text.html.HTMLDocument;

import org.apache.commons.io.IOUtils;

class HelpViewer extends JFrame{
	HelpViewer(){
		super();
		InputStream stream = getHelpStream();
		StringWriter writer = new StringWriter();
		
		try {
			IOUtils.copy(stream, writer, "UTF-8");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String theString = writer.toString();
		//HTMLDocument doc=new HTMLDocument();
		//doc.set
		JEditorPane editor = new JEditorPane();

		// Marcamos el editor para que use HTML
		editor.setContentType("text/html");
		editor.setText(theString);
		HTMLDocument doc = (HTMLDocument) editor.getDocument();
		this.add(editor);
		this.setVisible(true);
	}
	
    public static InputStream getHelpStream() {
    	//try {
			return ObjectUtil.getClass("vega.help.HelpAction").getResourceAsStream("/vega/doc/index.html");
		//} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			//return null;
		//}
    }

}
