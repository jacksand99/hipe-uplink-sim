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
REGISTRY.register(REGISTRY.COMPONENT,Extension("vega.uplink.pointing.gui.PointingElementOutline","vega.uplink.pointing.gui.PointingElementOutline","factory.outline.variable","vega.uplink.pointing.PointingElement"))
REGISTRY.register(REGISTRY.COMPONENT,Extension("vega.uplink.commanding.gui.ModelStateOutline","vega.uplink.commanding.gui.ModelStateOutline","factory.outline.variable","vega.uplink.commanding.ModelState"))
REGISTRY.register(REGISTRY.COMPONENT,Extension("HistoryModes Viewer","vega.uplink.commanding.gui.HistoryModesPlot","factory.editor.variable","vega.uplink.commanding.HistoryModes"))
REGISTRY.register(REGISTRY.COMPONENT,Extension("SequenceEditor","vega.uplink.commanding.gui.SequenceEditor","factory.editor.variable","vega.uplink.commanding.Sequence"))
REGISTRY.register(REGISTRY.COMPONENT,Extension("Pointing Editor","vega.uplink.pointing.gui.PointingElementEditor","factory.editor.variable","vega.uplink.pointing.PointingElement"))
REGISTRY.register(REGISTRY.COMPONENT,Extension("Pointing XML Editor","vega.uplink.pointing.gui.PointingElementXMLEditor","factory.editor.variable","vega.uplink.pointing.PointingElement"))
REGISTRY.register(REGISTRY.COMPONENT,Extension("PTR XML Editor","vega.uplink.pointing.gui.PtrXmlEditor","factory.editor.variable","vega.uplink.pointing.Ptr"))
REGISTRY.register(REGISTRY.COMPONENT,Extension("Pointing Metadata Editor","vega.uplink.pointing.gui.PointingMetadataEditor","factory.editor.variable","vega.uplink.pointing.PointingMetadata"))
REGISTRY.register(REGISTRY.COMPONENT,Extension("Power Plot Viewer","vega.uplink.commanding.gui.PowerPlotViewer","factory.editor.variable","vega.uplink.commanding.SimulationContext"))
REGISTRY.register(UserPreferences.CATEGORY, Extension("Mission Planning","vega.hipe.preferences.UplinkPathPreferences",None,None))  # unused
REGISTRY.register(UserPreferences.CATEGORY, Extension("Mission Planning/Instruments","vega.hipe.preferences.InstrumentsNamesPreferences",None,None))  # unused
REGISTRY.register(REGISTRY.COMPONENT,Extension("PTR Reader","vega.uplink.pointing.gui.PtrFileComponent","factory.editor.file","vega.uplink.pointing.gui.PtrFile"))
REGISTRY.register(REGISTRY.COMPONENT,Extension("PDFM Reader","vega.uplink.pointing.gui.PdfmFileComponent","factory.editor.file","vega.uplink.pointing.gui.PdfmFile"))
REGISTRY.register(REGISTRY.COMPONENT,Extension("EVTM Reader","vega.uplink.pointing.gui.EvtmFileComponent","factory.editor.file","vega.uplink.pointing.gui.EvtmFile"))
REGISTRY.register(REGISTRY.COMPONENT,Extension("POR Reader","vega.uplink.commanding.gui.PorFileComponent","factory.editor.file","vega.uplink.commanding.gui.PorFile"))
REGISTRY.register(REGISTRY.COMPONENT,Extension("PORG Reader","vega.uplink.commanding.gui.PorgFileComponent","factory.editor.file","vega.uplink.commanding.gui.PorgFile"))
REGISTRY.register(REGISTRY.COMPONENT,Extension("MIB Reader","vega.uplink.commanding.gui.MibFileComponent","factory.editor.file","vega.uplink.commanding.gui.MibFile"))
REGISTRY.register(REGISTRY.COMPONENT,Extension("FECS Reader","vega.uplink.commanding.gui.FecsFileComponent","factory.editor.file","vega.uplink.commanding.gui.FecsFile"))
REGISTRY.register("site.fileType",Extension("site.file.ptr","vega.uplink.pointing.gui.PtrFile","regex:^PTR[a-zA-Z0-9_\-]*.ROS","vega/vega.gif"));
REGISTRY.register("site.fileType",Extension("site.file.ptsl","vega.uplink.pointing.gui.PtrFile","regex:^PTSL[a-zA-Z0-9_\-]*.ROS","vega/vega.gif"));
REGISTRY.register("site.fileType",Extension("site.file.pdfm","vega.uplink.pointing.gui.PdfmFile","regex:^PDFM[a-zA-Z0-9_\-]*.ROS","vega/vega.gif"));
REGISTRY.register("site.fileType",Extension("site.file.evtm","vega.uplink.pointing.gui.EvtmFile","regex:^EVTM[a-zA-Z0-9_\-]*.ROS","vega/vega.gif"));
REGISTRY.register("site.fileType",Extension("site.file.por","vega.uplink.commanding.gui.PorFile","regex:^POR_[a-zA-Z0-9_\-]*.ROS","vega/vega.gif"));
REGISTRY.register("site.fileType",Extension("site.file.fecs","vega.uplink.commanding.gui.FecsFile","regex:^FECS[a-zA-Z0-9_\-]*.ROS","vega/vega.gif"));
REGISTRY.register("site.fileType",Extension("site.file.porg","vega.uplink.commanding.gui.PorgFile","regex:^PORG[a-zA-Z0-9_\-]*.ROS","vega/vega.gif"));
REGISTRY.register("site.fileType",Extension("site.file.mib","vega.uplink.commanding.gui.MibFile","regex:^MIB[a-zA-Z0-9_\-]*.ROS","vega/vega.gif"));

toolRegistry = TaskToolRegistry.getInstance()
from vega.uplink.commanding.task import SavePorTask
from vega.uplink.commanding.task import SaveItlTask

from vega.uplink.commanding.task import SavePorgTask
from vega.uplink.pointing.task import SavePtrTask
from vega.uplink.pointing.task import SavePdfmTask
from vega.uplink.pointing.task import ComparePtrsTask
from vega.uplink.pointing.task import MergePtrsTask
from vega.uplink.pointing.task import PtrSanityCheckTask
from vega.uplink.pointing.task import RebasePtslTask

from vega.uplink.commanding.task import SimulateTask
toolRegistry.register(SavePorTask())
toolRegistry.register(SaveItlTask())
toolRegistry.register(SavePorgTask())
toolRegistry.register(SimulateTask())
toolRegistry.register(SavePtrTask())
toolRegistry.register(SavePdfmTask())
toolRegistry.register(ComparePtrsTask())
toolRegistry.register(MergePtrsTask())
toolRegistry.register(PtrSanityCheckTask())
toolRegistry.register(RebasePtslTask())

del(toolRegistry)

instruments=Configuration.getList("vega.instrument.names")
for ins in instruments:
	REGISTRY.register(UserPreferences.CATEGORY, Extension("Mission Planning/Instruments/"+ins,"vega.hipe.preferences.InstrumentPreferences",None,None))  # unused

del(REGISTRY, pluginsDir, rspDir, file, ins, instruments)





