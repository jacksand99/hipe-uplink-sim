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
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	HelpViewer(){
		super();
		InputStream stream = getHelpStream();
		StringWriter writer = new StringWriter();
		
		try {
			IOUtils.copy(stream, writer, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		String theString = writer.toString();
		JEditorPane editor = new JEditorPane();

		editor.setContentType("text/html");
		editor.setText(theString);
		HTMLDocument doc = (HTMLDocument) editor.getDocument();
		this.add(editor);
		this.setVisible(true);
	}
	
    public static InputStream getHelpStream() {
			return ObjectUtil.getClass("vega.help.HelpAction").getResourceAsStream("/vega/doc/index.html");
    }

}
