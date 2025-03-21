package vega.hipe.preferences;

import java.util.Iterator;
import java.util.List;

import vega.uplink.Properties;
import herschel.ia.gui.kernel.ExtensionRegistry;
import herschel.ia.gui.kernel.prefs.UserPreferences;
import herschel.ia.gui.kernel.Extension;
import herschel.share.util.Configuration;

public class PreferencePanelRegistrator {
	
	public static void registerPanels(){
		ExtensionRegistry REGISTRY = ExtensionRegistry.getInstance();
		REGISTRY.register(UserPreferences.CATEGORY, new Extension("Mission Planning","vega.hipe.preferences.UplinkPathPreferences",null,null));
		REGISTRY.register(UserPreferences.CATEGORY, new Extension("Mission Planning/Instruments","vega.hipe.preferences.InstrumentsNamesPreferences",null,null));
		REGISTRY.register(UserPreferences.CATEGORY, new Extension("Mission Planning/Files","vega.hipe.preferences.FileTypesPreferences",null,null));
		REGISTRY.register(UserPreferences.CATEGORY, new Extension("Git","vega.hipe.git.GitPreferences",null,null));
		REGISTRY.register(UserPreferences.CATEGORY, new Extension("Mail","vega.hipe.mail.MailPreferencesPanel",null,null));
		REGISTRY.register(UserPreferences.CATEGORY, new Extension("Must","vega.hipe.must.MustPreferences",null,null));
		REGISTRY.register(UserPreferences.CATEGORY, new Extension("Mission Planning/Pointing","vega.uplink.pointing.net.PointingFDPreferences",null,null));

		List<String> instruments = Configuration.getList(Properties.INSTRUMENT_NAMES_PROPERTIES);
		Iterator<String> it = instruments.iterator();
		String ins;
		while(it.hasNext()){
			ins=it.next();
			System.out.println("Registering config panel for "+ins);
			REGISTRY.register(UserPreferences.CATEGORY, new Extension("Mission Planning/Instruments/"+ins,"vega.hipe.preferences.InstrumentPreferences",null,null));

		}
		
	}

}
