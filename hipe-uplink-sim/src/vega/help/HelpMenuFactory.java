package vega.help;
import static herschel.ia.gui.kernel.menus.Insert.MAIN;
import static herschel.ia.gui.kernel.menus.Insert.Scheme.MENU;
import herschel.ia.gui.kernel.menus.AbstractActionMaker;
import herschel.ia.gui.kernel.menus.ActionBars;
import herschel.ia.gui.kernel.menus.ActionBarsHolder;
import herschel.ia.gui.kernel.menus.ActionMaker;
import herschel.ia.gui.kernel.menus.Insert;

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

/**
 * This class creates Help menu items.
 *
 * @author pbalm
 */
public final class HelpMenuFactory extends AbstractActionMaker {
    
    public static final String ID = "vega.help";
    
    /**
     * Default constructor.
     */
    public HelpMenuFactory() {
	super(ID, ActionMaker.NORMAL_PRIORITY);
    }

    /**
     * @see herschel.ia.gui.kernel.menus.ActionMaker#makeActions(herschel.ia.gui.kernel.menus.ActionBarsHolder)
     */
    @Override
    public void makeActions(ActionBarsHolder holder) {

	// Define location to insert menu item:
	ActionBars menus = holder.getActionBars(MAIN);
	Insert location = new Insert(MENU, MAIN, Insert.Extension.HELP_END);
	
	menus.insert(location, new HelpAction());
	
    }

}
