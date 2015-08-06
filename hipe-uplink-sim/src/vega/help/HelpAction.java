package vega.help;

import herschel.ia.gui.kernel.AbstractSiteAction;
import herschel.ia.gui.kernel.util.IconLibrary;
import herschel.ia.gui.kernel.util.PopupDialog;
import herschel.share.util.ObjectUtil;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Logger;

import javax.swing.JEditorPane;
import javax.swing.JFrame;

import org.apache.commons.io.IOUtils;

import vega.hipe.logging.VegaLog;

/**
 * This is an example menu item.
 *
 * @author pbalm
 */
public final class HelpAction extends AbstractSiteAction {
    
    private static final long serialVersionUID = 1L;
    
    private static final String HELP_HOME = "http://www.google.com";
    private static final String MENU_ITEM_LABEL = "Help for Hipe Uplink Sim";
    
    /**
     * Default constructor.
     */
    public HelpAction() {
	super(Style.AS_PUSH, HelpMenuFactory.ID + ".default", MENU_ITEM_LABEL, IconLibrary.HELP);
    }
    
    /**
     * @see herschel.ia.gui.kernel.AbstractSiteAction#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent event) {
    VegaLog.info("Opening " + HELP_HOME);
	
	String msg = null;
        if (Desktop.isDesktopSupported()) { 
            try {
            	URL helpurl = ObjectUtil.getClass("vega.uplink.commanding.Por").getResource("/doc/index.html");
            	SimpleSwingBrowser browser=new SimpleSwingBrowser();
        		browser.run();
        		browser.loadURL(helpurl.toString());
            } catch (Exception e) {
                msg = "Could not open doc/index.html:\n" + e.getMessage();
            }
        } else {
            msg = "Opening a browser is not supported in your JRE.\n" +
            	  "Check your OS and Java versions.";
        }
        if (msg != null) {
            PopupDialog.showError(msg);
        }
    }
    
    
    public static void main (String[] args){
    	try {
    		SimpleSwingBrowser browser=new SimpleSwingBrowser();
    		browser.run();
    		browser.loadURL(ObjectUtil.getClass("vega.help.HelpAction").getResource("/vega/doc/index.html").toString());
			System.out.println(ObjectUtil.getClass("vega.help.HelpAction").getResource("/vega/doc/index.html").toURI());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
    }

}
