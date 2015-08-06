package vega.hipe.git;

import herschel.ia.gui.kernel.AbstractSiteAction;
import herschel.ia.gui.kernel.SiteAction;
import herschel.ia.gui.kernel.menus.AbstractActionMaker;
import herschel.ia.gui.kernel.menus.ActionBars;
import herschel.ia.gui.kernel.menus.ActionBarsHolder;
import herschel.ia.gui.kernel.menus.ActionMaker;
import herschel.ia.gui.kernel.menus.Insert;

import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;


import vega.IconResources;
import vega.uplink.commanding.gui.SequenceEditor;

public class GitMenuFactory extends AbstractActionMaker {
    
    public static final String ID = "vega.git";
    private ActionBarsHolder holder;
    
    /**
     * Default constructor.
     */
    public GitMenuFactory() {
	super(ID, ActionMaker.NORMAL_PRIORITY);
    }

    /**
     * @see herschel.ia.gui.kernel.menus.ActionMaker#makeActions(herschel.ia.gui.kernel.menus.ActionBarsHolder)
     */
    @Override
    public void makeActions(ActionBarsHolder holder) {
    	this.holder=holder;
    	registerGitMenus();
	
    }
    
    private ActionBarsHolder getPart(){
    	return holder;
    }
	private void registerGitMenus(){
		AbstractSiteAction gitPull = new AbstractSiteAction(SiteAction.Style.AS_PUSH, "GIT_PULL", "Git Pull", getGitIcon()) {
	        /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
	    public void actionPerformed(ActionEvent e) {
			try {
				HipeGit.getInstance().gitPull();
			} catch (Exception e1) {
				IllegalArgumentException iae = new IllegalArgumentException(e1.getMessage());
				iae.initCause(e1);
				throw(iae);
			} 
	    }
	};

	AbstractSiteAction gitPush = new AbstractSiteAction(SiteAction.Style.AS_PUSH, "GIT_PUSH", "Git Push", getGitIcon()) {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
    public void actionPerformed(ActionEvent e) {
		try {
			HipeGit.getInstance().gitPush();
		} catch (Exception e1) {
			IllegalArgumentException iae = new IllegalArgumentException(e1.getMessage());
			iae.initCause(e1);
			throw(iae);
		} 
    }
};

AbstractSiteAction gitClone = new AbstractSiteAction(SiteAction.Style.AS_PUSH, "GIT_CLONE", "Git Clone", getGitIcon()) {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
public void actionPerformed(ActionEvent e) {
	try {
		HipeGit.getInstance().gitClone();
	} catch (Exception e1) {
		IllegalArgumentException iae = new IllegalArgumentException(e1.getMessage());
		iae.initCause(e1);
		throw(iae);
	} 
}
};

AbstractSiteAction gitTunnel = new AbstractSiteAction(SiteAction.Style.AS_PUSH, "GIT_TUNNEL", "Git Tunnel Server", getGitIcon()) {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
public void actionPerformed(ActionEvent e) {
	try {
		HipeGitSshTunnel.startTunnel();
	} catch (Exception e1) {
		IllegalArgumentException iae = new IllegalArgumentException(e1.getMessage());
		iae.initCause(e1);
		throw(iae);
	} 
}
};

	ActionBars mainMenu = getPart().getActionBars(herschel.ia.gui.kernel.menus.Insert.MAIN);
	mainMenu.insert(new Insert(Insert.Scheme.MENU, herschel.ia.gui.kernel.menus.Insert.MAIN, Insert.Extension.TOOLS_ADDON),gitPull);
	mainMenu.insert(new Insert(Insert.Scheme.MENU, herschel.ia.gui.kernel.menus.Insert.MAIN, Insert.Extension.TOOLS_ADDON),gitPush);
	mainMenu.insert(new Insert(Insert.Scheme.MENU, herschel.ia.gui.kernel.menus.Insert.MAIN, Insert.Extension.TOOLS_ADDON),gitClone);
	mainMenu.insert(new Insert(Insert.Scheme.MENU, herschel.ia.gui.kernel.menus.Insert.MAIN, Insert.Extension.TOOLS_ADDON),gitTunnel);

	}
	
	public static Icon getGitIcon() {
        try {
            URL resource = SequenceEditor.class.getResource(IconResources.GIT_ICON);
            BufferedImage imageIcon = ImageIO.read(resource);
            return new ImageIcon(imageIcon);
    } catch (IOException e) {
           
            e.printStackTrace();
            return null;
    }
	}
}
