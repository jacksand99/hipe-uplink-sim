package vega.ia.gui.apps;



//import rosetta.uplink.commanding.Mib;
import herschel.ia.gui.apps.plugin.PluginRegistry;
import herschel.ia.gui.kernel.SiteApplication;
import herschel.ia.gui.kernel.ExtensionRegistry;
import herschel.ia.gui.kernel.Extension;

/**
 * Starts HIPE.
 */
public class RspStarter {

    // No need to instantiate this class
    private RspStarter() {}

    /**
     * Starts the HIPE application.<p>
     * If there are Jython script paths in the arguments, it will be run in batch mode.
     * @param args the command line arguments
     */
    public static void main(String[] args) {
		herschel.share.util.Configuration.setProperty("var.hcss.dir", "C:\\Users\\jarenas\\Downloads\\hcss-12.0.2524");
		herschel.share.util.Configuration.setProperty("var.hcsstest.dir", "C:\\Users\\jarenas\\Downloads\\hcss-12.0.2524");
		//herschel.share.util.Configuration.setProperty(Mib.ROSETTA_MIB_LOCATION, "Z:\\MAPPS\\MIB");
		String FACTORY = ExtensionRegistry.FACTORY;

		ExtensionRegistry REGISTRY = ExtensionRegistry.getInstance();

		REGISTRY.register(FACTORY, new Extension("site.resolver.factory","herschel.ia.gui.apps.HipeClassResolver","site.resolver.factory","herschel.ia.gui.apps.HipeClassResolver"));
		REGISTRY.register(FACTORY, new Extension("site.statusbar","herschel.ia.gui.kernel.SimpleComponentFactoryManager","site.statusbar","herschel.ia.gui.kernel.SimpleComponentFactoryManager"));

		PluginRegistry plugins = PluginRegistry.getInstance();
        SiteApplication.start(RosettaSite.class, args);
        //SiteApplication.start(herschel.ia.gui.apps.HipeSite.class, args);

    }
}

