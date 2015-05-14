/*
 * This file is part of Herschel Common Science System (HCSS).
 * Copyright 2001-2011 Herschel Science Ground Segment Consortium
 *
 * HCSS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * HCSS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General
 * Public License along with HCSS.
 * If not, see <http://www.gnu.org/licenses/>.
 */
/* pbalm - Mar 25, 2011 */
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
    //private static final Logger LOG = Logger.getLogger(HelpAction.class.getName());
    
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
        	//HelpViewer hv = new HelpViewer();
        	//URI uri = HelpAction.getHelpUri();
        	/*if (uri!=null){
        		Desktop.getDesktop().browse(uri);
        	}*/
            try {
            	URL helpurl = ObjectUtil.getClass("vega.uplink.commanding.Por").getResource("/doc/index.html");
            	SimpleSwingBrowser browser=new SimpleSwingBrowser();
        		browser.run();
        		browser.loadURL(helpurl.toString());
                //Desktop.getDesktop().browse(URI.create(HELP_HOME));
                //Desktop.getDesktop().browse(uri);
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
    		//browser.run();
			System.out.println(ObjectUtil.getClass("vega.help.HelpAction").getResource("/vega/doc/index.html").toURI());
			//HelpViewer hv = new HelpViewer();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

}
