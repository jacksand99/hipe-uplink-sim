from herschel.ia.gui.kernel import ExtensionRegistry
from herschel.ia.gui.kernel import Extension
from herschel.ia.gui.kernel.menus import ActionMaker
from sys import path
from java.io import File
from herschel.ia.gui.kernel.util import SiteFileUtil
from herschel.share.util import FileNameExtensionFilter
from herschel.ia.gui.kernel.prefs import UserPreferences
from herschel.share.util import Configuration

pluginsDir = SiteFileUtil.getSiteDirectory("plugins")
rspDir = File(pluginsDir, "hus/1.0")
for file in rspDir.listFiles(FileNameExtensionFilter("JAR files", ["jar"])):
    path.append(file.absolutePath)
    
from vega.help import HelpMenuFactory

REGISTRY = ExtensionRegistry.getInstance()
REGISTRY.register(ActionMaker.ID, Extension(
                 HelpMenuFactory.ID,
                 "vega.help.HelpMenuFactory",
                  None,    # not used
                  None))   # not used

#del(ExtensionRegistry, Extension, ActionMaker, REGISTRY) # cleanup

REGISTRY = ExtensionRegistry.getInstance();
REGISTRY.register(REGISTRY.VIEWABLE,Extension("site.view.variables","vega.uplink.commanding.gui.SimulationContextView","Workbench/Variables","vega/vega.gif"))
REGISTRY.register(REGISTRY.VIEWABLE,Extension("site.view.simulation","vega.uplink.commanding.gui.SimulationView","Workbench/Simulation","vega/vega.gif"))
REGISTRY.register(REGISTRY.COMPONENT,Extension("vega.uplink.commanding.gui.OtherOutline","vega.uplink.commanding.gui.OtherOutline","factory.outline.variable","vega.uplink.commanding.Por"))
REGISTRY.register(REGISTRY.COMPONENT,Extension("vega.uplink.pointing.gui.PtrOutline","vega.uplink.pointing.gui.PtrOutline","factory.outline.variable","vega.uplink.pointing.Ptr"))
REGISTRY.register(REGISTRY.COMPONENT,Extension("vega.uplink.pointing.gui.PointingMetadataOutline","vega.uplink.pointing.gui.PointingMetadataOutline","factory.outline.variable","vega.uplink.pointing.PointingMetadata"))
REGISTRY.register(REGISTRY.COMPONENT,Extension("vega.uplink.commanding.gui.ModelStateOutline","vega.uplink.commanding.gui.ModelStateOutline","factory.outline.variable","vega.uplink.commanding.ModelState"))
REGISTRY.register(REGISTRY.COMPONENT,Extension("HistoryModes Viewer","vega.uplink.commanding.gui.HistoryModesPlot","factory.editor.variable","vega.uplink.commanding.HistoryModes"))
REGISTRY.register(REGISTRY.COMPONENT,Extension("Power Plot Viewer","vega.uplink.commanding.gui.PowerPlotViewer","factory.editor.variable","vega.uplink.commanding.SimulationContext"))
REGISTRY.register(UserPreferences.CATEGORY, Extension("Mission Planning","vega.hipe.preferences.UplinkPathPreferences",None,None))  # unused
REGISTRY.register(UserPreferences.CATEGORY, Extension("Mission Planning/Instruments","vega.hipe.preferences.InstrumentsNamesPreferences",None,None))  # unused

instruments=Configuration.getList("vega.instrument.names")
for ins in instruments:
	REGISTRY.register(UserPreferences.CATEGORY, Extension("Mission Planning/Instruments/"+ins,"vega.hipe.preferences.InstrumentPreferences",None,None))  # unused

del(REGISTRY, pluginsDir, rspDir, file, ins, instruments)





