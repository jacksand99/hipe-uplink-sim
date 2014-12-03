package vega.hipe.git;

import static herschel.ia.gui.kernel.menus.Insert.MAIN;
import static herschel.ia.gui.kernel.menus.Insert.Scheme.MENU;
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

//import vega.help.HelpAction;
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
				// TODO Auto-generated catch block
				IllegalArgumentException iae = new IllegalArgumentException(e1.getMessage());
				iae.initCause(e1);
				throw(iae);
				//e1.printStackTrace();
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
			// TODO Auto-generated catch block
			IllegalArgumentException iae = new IllegalArgumentException(e1.getMessage());
			iae.initCause(e1);
			throw(iae);
			//e1.printStackTrace();
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
		// TODO Auto-generated catch block
		IllegalArgumentException iae = new IllegalArgumentException(e1.getMessage());
		iae.initCause(e1);
		throw(iae);
		//e1.printStackTrace();
	} 
}
};

	ActionBars mainMenu = getPart().getActionBars(herschel.ia.gui.kernel.menus.Insert.MAIN);
	mainMenu.insert(new Insert(Insert.Scheme.MENU, herschel.ia.gui.kernel.menus.Insert.MAIN, Insert.Extension.TOOLS_ADDON),gitPull);
	mainMenu.insert(new Insert(Insert.Scheme.MENU, herschel.ia.gui.kernel.menus.Insert.MAIN, Insert.Extension.TOOLS_ADDON),gitPush);
	mainMenu.insert(new Insert(Insert.Scheme.MENU, herschel.ia.gui.kernel.menus.Insert.MAIN, Insert.Extension.TOOLS_ADDON),gitClone);

	}
	
	public static Icon getGitIcon() {
        try {
            URL resource = SequenceEditor.class.getResource("/vega/Git-Icon.png");
            BufferedImage imageIcon = ImageIO.read(resource);
            return new ImageIcon(imageIcon);
    } catch (IOException e) {
           
            e.printStackTrace();
            return null;
    }
	}
}
