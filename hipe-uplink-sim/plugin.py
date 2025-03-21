from herschel.ia.gui.kernel import ExtensionRegistry
from herschel.ia.gui.kernel import Extension
from herschel.ia.gui.kernel.menus import ActionMaker
#from sys import path
from java.io import File
from herschel.ia.gui.kernel.util import SiteFileUtil
from herschel.share.util import FileNameExtensionFilter
from herschel.ia.gui.kernel.prefs import UserPreferences
from herschel.share.util import Configuration

#pluginsDir = SiteFileUtil.getSiteDirectory("plugins")
#rspDir = File(pluginsDir, "hus/1.0")
#for file in rspDir.listFiles(FileNameExtensionFilter("JAR files", ["jar"])):
#    path.append(file.absolutePath)

# Plugin imports
from vega import IconResources
from vega.uplink.commanding import Pdor
from vega.uplink.track import Fecs
from vega.uplink.track import GsPass
from vega.uplink.commanding import Mib
from vega.uplink.commanding import Orcd
from vega.uplink.commanding import Parameter
from vega.uplink.commanding import Por
from vega.uplink.commanding import PorChecker
from vega.uplink.commanding import PorUtils
from vega.uplink.track import FecsUtils
from vega.uplink.commanding import Sequence
from vega.uplink.commanding import SequenceProfile
from vega.uplink.commanding import Simulation
from vega.uplink.commanding import SimulationContext
from vega.uplink.commanding import SuperPor
from vega.uplink.commanding import MocPower
from vega.uplink.commanding import SsmmSimulator
from vega.uplink.pointing import Ptr
from vega.uplink.pointing import PtrUtils
from vega.uplink.pointing import PtrSegment
from vega.uplink.pointing import PointingBlock
from vega.uplink.pointing import PointingBlockSlew
from vega.uplink.pointing import PtrChecker
from vega.uplink.pointing import PointingElement
from vega.uplink.pointing import PointingMetadata
from vega.uplink.pointing import Pdfm
from vega.uplink.pointing import AttitudeUtils
from vega.uplink.pointing import PointingBlockGSEP
from vega.uplink.pointing.attitudes import Inertial
from vega.uplink.pointing.attitudes import Track
from vega.uplink.pointing.attitudes import Limb
from vega.uplink.pointing.attitudes import Velocity
from vega.uplink.pointing.attitudes import Specular
from vega.uplink.pointing.attitudes import Terminator
from vega.uplink.pointing.attitudes import IlluminatedPoint
from vega.uplink.pointing.attitudes import DerivedPhaseAngle
from vega.uplink.pointing.attitudes import Capture
from vega.uplink.pointing.PtrParameters import Boresight
from vega.uplink.pointing.PtrParameters import PhaseAngle
from vega.uplink.pointing.PtrParameters import TargetInert
from vega.uplink.pointing.PtrParameters import TargetTrack
from vega.uplink.pointing.PtrParameters import TargetDir
from vega.uplink.pointing.PtrParameters import Height
from vega.uplink.pointing.PtrParameters import Surface
from vega.uplink.pointing.PtrParameters import OffsetRefAxis
from vega.uplink.pointing.PtrParameters import PointedAxis
from vega.uplink.pointing.PtrParameters.Offset import OffsetFixed
from vega.uplink.pointing.PtrParameters.Offset import OffsetRaster
from vega.uplink.pointing.PtrParameters.Offset import OffsetScan
from vega.uplink.pointing.PtrParameters.Offset import OffsetCustom
from vega.uplink.planning import Observation
from vega.uplink.planning import ObservationUtil
from vega.uplink.planning import Schedule
from vega.uplink.planning import ObservationsSchedule
from vega.uplink.planning import ObservationSequence
from vega.uplink.planning import ObservationPointingBlock
from vega.hipe.preferences import PreferencePanelRegistrator
from vega.uplink import Properties
from vega.hipe.git import HipeGit
from vega.hipe.git import Tunnel
from vega.hipe.git import HipeGitSshTunnel
from vega.uplink.planning.period import Plan
from vega.uplink.planning.period import Ltp
from vega.uplink.planning.period import Mtp
from vega.uplink.planning.period import Stp
from vega.uplink.planning.period import Vstp
from vega.uplink import DateUtil
from vega.hipe.gui.xmlutils import XmlData
from vega.hipe.gui.xmlutils import HtmlDocument
from vega.uplink.commanding.itl import ItlParser
from vega.uplink.commanding.itl import EventList
from vega.uplink.planning.aspen import AspenObservationSchedule
from vega.hipe.pds import PDSImage

PreferencePanelRegistrator.registerPanels();

from vega.help import HelpMenuFactory
from vega.hipe.git import GitMenuFactory

REGISTRY = ExtensionRegistry.getInstance()
REGISTRY.register(ActionMaker.ID, Extension(
                 HelpMenuFactory.ID,
                 "vega.help.HelpMenuFactory",
                  None,    # not used
                  None))   # not used
REGISTRY.register(ActionMaker.ID, Extension(
                 GitMenuFactory.ID,
                 "vega.hipe.git.GitMenuFactory",
                  None,    # not used
                  None))   # not used

#del(ExtensionRegistry, Extension, ActionMaker, REGISTRY) # cleanup
#PreferencePanelRegistrator.registerPanels();
REGISTRY = ExtensionRegistry.getInstance();
#REGISTRY.register(UserPreferences.CATEGORY, Extension("Mission Planning","vega.hipe.preferences.UplinkPathPreferences",None,None))  # unused
#REGISTRY.register(UserPreferences.CATEGORY, Extension("Mission Planning/Instruments","vega.hipe.preferences.InstrumentsNamesPreferences",None,None))  # unused
#instruments=Properties.getList("vega.instrument.names")
#for ins in instruments:
#	print "Registering config panel for "+ins
#	REGISTRY.register(UserPreferences.CATEGORY, Extension("Mission Planning/Instruments/"+ins,"vega.hipe.preferences.InstrumentPreferences",None,None))  # unused
REGISTRY.register(REGISTRY.VIEWABLE,Extension("site.view.variables","vega.uplink.commanding.gui.SimulationContextView","Workbench/Variables",IconResources.HUS_ICON_NOROOT))
REGISTRY.register(REGISTRY.VIEWABLE,Extension("site.view.simulation","vega.uplink.commanding.gui.SimulationView","Workbench/Simulation",IconResources.HUS_ICON_NOROOT))
REGISTRY.register(REGISTRY.COMPONENT,Extension("vega.uplink.commanding.gui.OtherOutline","vega.uplink.commanding.gui.OtherOutline","factory.outline.variable","vega.uplink.commanding.Por"))
REGISTRY.register(REGISTRY.COMPONENT,Extension("vega.uplink.pointing.gui.PtrOutline","vega.uplink.pointing.gui.PtrOutline","factory.outline.variable","vega.uplink.pointing.Ptr"))
REGISTRY.register(REGISTRY.COMPONENT,Extension("vega.uplink.pointing.gui.PointingElementOutline","vega.uplink.pointing.gui.PointingElementOutline","factory.outline.variable","vega.uplink.pointing.PointingElement"))
REGISTRY.register(REGISTRY.COMPONENT,Extension("vega.uplink.commanding.gui.ModelStateOutline","vega.uplink.commanding.gui.ModelStateOutline","factory.outline.variable","vega.uplink.commanding.ModelState"))
REGISTRY.register(REGISTRY.COMPONENT,Extension("HistoryModes Viewer","vega.uplink.commanding.gui.HistoryModesPlot","factory.editor.variable","vega.uplink.commanding.HistoryModes"))
REGISTRY.register(REGISTRY.COMPONENT,Extension("SequenceEditor","vega.uplink.commanding.gui.SequenceEditor","factory.editor.variable","vega.uplink.commanding.Sequence"))
REGISTRY.register(REGISTRY.COMPONENT,Extension("Pointing Editor","vega.uplink.pointing.gui.PointingElementEditor","factory.editor.variable","vega.uplink.pointing.PointingElement"))
REGISTRY.register(REGISTRY.COMPONENT,Extension("Pointing XML Editor","vega.uplink.pointing.gui.PointingElementXMLEditor","factory.editor.variable","vega.uplink.pointing.PointingElement"))
REGISTRY.register(REGISTRY.COMPONENT,Extension("PTR XML Editor","vega.uplink.pointing.gui.PtrXmlEditor","factory.editor.variable","vega.uplink.pointing.Ptr"))
REGISTRY.register(REGISTRY.COMPONENT,Extension("PointingBlocksSlice XML Editor","vega.uplink.pointing.gui.PointingBlocksSliceXmlEditor","factory.editor.variable","vega.uplink.pointing.PointingBlocksSlice"))
REGISTRY.register(REGISTRY.COMPONENT,Extension("Pointing Metadata Editor","vega.uplink.pointing.gui.PointingMetadataEditor","factory.editor.variable","vega.uplink.pointing.PointingMetadata"))
REGISTRY.register(REGISTRY.COMPONENT,Extension("Observation Editor","vega.uplink.planning.gui.ObservationEditor","factory.editor.variable","vega.uplink.planning.Observation"))
REGISTRY.register(REGISTRY.COMPONENT,Extension("Scheduler Viewer","vega.uplink.planning.gui.ScheduleViewer","factory.editor.variable","vega.uplink.planning.Schedule"))
REGISTRY.register(REGISTRY.COMPONENT,Extension("Power Plot Viewer","vega.uplink.commanding.gui.PowerPlotViewer","factory.editor.variable","vega.uplink.commanding.SimulationContext"))
REGISTRY.register(REGISTRY.COMPONENT,Extension("PTR Reader","vega.uplink.pointing.gui.PtrFileComponent","factory.editor.file","vega.uplink.pointing.gui.PtrFile"))
REGISTRY.register(REGISTRY.COMPONENT,Extension("PDFM Reader","vega.uplink.pointing.gui.PdfmFileComponent","factory.editor.file","vega.uplink.pointing.gui.PdfmFile"))
REGISTRY.register(REGISTRY.COMPONENT,Extension("EVTM Reader","vega.uplink.pointing.gui.EvtmFileComponent","factory.editor.file","vega.uplink.pointing.gui.EvtmFile"))
REGISTRY.register(REGISTRY.COMPONENT,Extension("POR Reader","vega.uplink.commanding.gui.PorFileComponent","factory.editor.file","vega.uplink.commanding.gui.PorFile"))
REGISTRY.register(REGISTRY.COMPONENT,Extension("PORG Reader","vega.uplink.commanding.gui.PorgFileComponent","factory.editor.file","vega.uplink.commanding.gui.PorgFile"))
REGISTRY.register(REGISTRY.COMPONENT,Extension("MIB Reader","vega.uplink.commanding.gui.MibFileComponent","factory.editor.file","vega.uplink.commanding.gui.MibFile"))
REGISTRY.register(REGISTRY.COMPONENT,Extension("FECS Reader","vega.uplink.track.gui.FecsFileComponent","factory.editor.file","vega.uplink.track.gui.FecsFile"))
REGISTRY.register(REGISTRY.COMPONENT,Extension("Planning Observation Reader","vega.uplink.planning.gui.ObservationFileComponent","factory.editor.file","vega.uplink.planning.gui.ObservationFile"))
REGISTRY.register(REGISTRY.COMPONENT,Extension("Planning Schedule Reader","vega.uplink.planning.gui.ScheduleFileComponent","factory.editor.file","vega.uplink.planning.gui.ScheduleFile"))
REGISTRY.register(REGISTRY.COMPONENT,Extension("Planning Period Reader","vega.uplink.planning.gui.PeriodsFileComponent","factory.editor.file","vega.uplink.planning.gui.PeriodsFile"))
REGISTRY.register(REGISTRY.COMPONENT,Extension("XML Reader","vega.hipe.gui.xmlutils.XmlFileComponent","factory.editor.file","vega.hipe.gui.xmlutils.XmlFile"))
REGISTRY.register(REGISTRY.COMPONENT,Extension("XML Editor","vega.hipe.gui.xmlutils.XmlFileEditor","factory.editor.variable","vega.hipe.gui.xmlutils.XmlDataInterface"))
REGISTRY.register(REGISTRY.COMPONENT,Extension("Fecs XML Editor","vega.hipe.gui.xmlutils.XmlFileEditor","factory.editor.variable","vega.uplink.track.Fecs"))
REGISTRY.register(REGISTRY.COMPONENT,Extension("HTML Reader","vega.hipe.gui.xmlutils.HtmlFileComponent","factory.editor.file","vega.hipe.gui.xmlutils.HtmlFile"))
REGISTRY.register(REGISTRY.COMPONENT,Extension("HTML Editor","vega.hipe.gui.xmlutils.HtmlDocumentViewer","factory.editor.variable","vega.hipe.gui.xmlutils.HtmlDocument"))
REGISTRY.register(REGISTRY.COMPONENT,Extension("XmlMapContextEditor","vega.hipe.products.gui.AbstractXmlMapContextXmlEditor","factory.editor.variable","vega.hipe.products.AbstractXmlMapContext"))
REGISTRY.register(REGISTRY.COMPONENT,Extension("PorItlEditor","vega.uplink.commanding.gui.PorItlEditor","factory.editor.variable","vega.uplink.commanding.Por"))
REGISTRY.register(REGISTRY.COMPONENT,Extension("ITL editor Explicit Time","vega.uplink.commanding.gui.SuperPorItlEditor","factory.editor.variable","vega.uplink.commanding.SuperPor"))
REGISTRY.register(REGISTRY.COMPONENT,Extension("ITL editor Events","vega.uplink.commanding.gui.SuperPorItlEditorEvents","factory.editor.variable","vega.uplink.commanding.SuperPor"))
REGISTRY.register(REGISTRY.COMPONENT,Extension("vega.uplink.commanding.itl.gui.EvfOutline","vega.uplink.commanding.itl.gui.EvfOutline","factory.outline.variable","vega.uplink.commanding.itl.EventList"))


REGISTRY.register("site.fileType",Extension("site.file.xml","vega.hipe.gui.xmlutils.HtmlFile","regex:^[a-zA-Z0-9_\-.]*.html",IconResources.HUS_ICON_NOROOT));
REGISTRY.register("site.fileType",Extension("site.file.xml","vega.hipe.gui.xmlutils.XmlFile","regex:^[a-zA-Z0-9_\-.]*.xml",IconResources.XML_ICON_NOROOT));
REGISTRY.register("site.fileType",Extension("site.file.ptr","vega.uplink.pointing.gui.PtrFile",Configuration.getProperty("vega.file.type.PTR"),IconResources.PTR_ICON_NOROOT));
REGISTRY.register("site.fileType",Extension("site.file.ptsl","vega.uplink.pointing.gui.PtrFile",Configuration.getProperty("vega.file.type.PTSL"),IconResources.PTSL_ICON_NOROOT));
REGISTRY.register("site.fileType",Extension("site.file.pdfm","vega.uplink.pointing.gui.PdfmFile",Configuration.getProperty("vega.file.type.PDFM"),IconResources.PDFM_ICON_NOROOT));
REGISTRY.register("site.fileType",Extension("site.file.evtm","vega.uplink.pointing.gui.EvtmFile",Configuration.getProperty("vega.file.type.EVTM"),IconResources.HUS_ICON_NOROOT));
REGISTRY.register("site.fileType",Extension("site.file.por","vega.uplink.commanding.gui.PorFile",Configuration.getProperty("vega.file.type.POR"),IconResources.POR_ICON_NOROOT));
REGISTRY.register("site.fileType",Extension("site.file.fecs","vega.uplink.track.gui.FecsFile",Configuration.getProperty("vega.file.type.FECS"),IconResources.FECS_ICON_NOROOT));
REGISTRY.register("site.fileType",Extension("site.file.porg","vega.uplink.commanding.gui.PorgFile",Configuration.getProperty("vega.file.type.PORG"),IconResources.PORG_ICON_NOROOT));
REGISTRY.register("site.fileType",Extension("site.file.obs","vega.uplink.planning.gui.ObservationFile",Configuration.getProperty("vega.file.type.OBS"),IconResources.HUS_ICON_NOROOT));
REGISTRY.register("site.fileType",Extension("site.file.schedule","vega.uplink.planning.gui.ScheduleFile",Configuration.getProperty("vega.file.type.SCH"),IconResources.HUS_ICON_NOROOT));
REGISTRY.register("site.fileType",Extension("site.file.period","vega.uplink.planning.gui.PeriodsFile",Configuration.getProperty("vega.file.type.PER"),IconResources.HUS_ICON_NOROOT));
REGISTRY.register("site.fileType",Extension("site.file.itl","vega.uplink.commanding.itl.gui.ItlFile",Configuration.getProperty("vega.file.type.ITL"),IconResources.ITL_ICON_NOROOT));
REGISTRY.register(REGISTRY.COMPONENT,Extension("ITL to POR","vega.uplink.commanding.itl.gui.ItlFileComponent","factory.editor.file","vega.uplink.commanding.itl.gui.ItlFile"))
REGISTRY.register("site.fileType",Extension("site.file.evf","vega.uplink.commanding.itl.gui.EvfFile",Configuration.getProperty("vega.file.type.EVF"),IconResources.ITL_ICON_NOROOT));
REGISTRY.register(REGISTRY.COMPONENT,Extension("Evf reader","vega.uplink.commanding.itl.gui.EvfFileComponent","factory.editor.file","vega.uplink.commanding.itl.gui.EvfFile"))
REGISTRY.register(REGISTRY.COMPONENT,Extension("EvfEditor","vega.uplink.commanding.itl.gui.EvfFileEditor","factory.editor.variable","vega.uplink.commanding.itl.EventList"))
REGISTRY.register("site.fileType",Extension("site.file.pdor","vega.uplink.commanding.gui.PdorFile",Configuration.getProperty("vega.file.type.PDOR"),IconResources.POR_ICON_NOROOT));
REGISTRY.register(REGISTRY.COMPONENT,Extension("PDOR reader","vega.uplink.commanding.gui.PdorFileComponent","factory.editor.file","vega.uplink.commanding.gui.PdorFile"))
REGISTRY.register("site.fileType",Extension("site.file.pwpl","vega.uplink.commanding.gui.PwplFile",Configuration.getProperty("vega.file.type.PWPL"),IconResources.POR_ICON_NOROOT));
REGISTRY.register("site.fileType",Extension("site.file.pwtl","vega.uplink.commanding.gui.PwplFile",Configuration.getProperty("vega.file.type.PWTL"),IconResources.POR_ICON_NOROOT));
REGISTRY.register(REGISTRY.COMPONENT,Extension("PWPL reader","vega.uplink.commanding.gui.PwplFileComponent","factory.editor.file","vega.uplink.commanding.gui.PwplFile"))
REGISTRY.register(REGISTRY.VIEWABLE, herschel.ia.gui.kernel.Extension("site.view.must","vega.hipe.must.MustView","Must","vega/vega.gif"))
#REGISTRY.register(REGISTRY.COMPONENT,Extension("PDS Navigator","vega.hipe.pds.gui.PDSNavigator","factory.editor.variable","vega.hipe.pds.PDSImage"))

REGISTRY.register("site.fileType",Extension("site.file.img","vega.hipe.pds.ImgFile",Configuration.getProperty("vega.file.type.IMG"),IconResources.HUS_ICON_NOROOT));
REGISTRY.register("site.fileType",Extension("site.file.lbl","vega.hipe.pds.LblFile",Configuration.getProperty("vega.file.type.LBL"),IconResources.HUS_ICON_NOROOT));
REGISTRY.register(REGISTRY.COMPONENT,Extension("IMG reader","vega.hipe.pds.ImgFileComponent","factory.editor.file","vega.hipe.pds.ImgFile"))
REGISTRY.register(REGISTRY.COMPONENT,Extension("LBL reader","vega.hipe.pds.LblFileComponent","factory.editor.file","vega.hipe.pds.LblFile"))

toolRegistry = TaskToolRegistry.getInstance()
from vega.uplink.commanding.task import SavePorTask
from vega.uplink.commanding.task import SaveItlTask
from vega.uplink.commanding.task import PorCheckTask
from vega.uplink.track.task import CompareFecsTask
from vega.uplink.track.task import FecsSummaryTask
from rosetta.uplink.track.task import RosettaFecsSummaryTask
from vega.uplink.commanding.task import CreateTimelineTask

from vega.uplink.commanding.task import SavePorgTask
from vega.uplink.pointing.task import SavePtrTask
from vega.uplink.pointing.task import SavePdfmTask
from vega.uplink.pointing.task import ComparePtrsTask
from vega.uplink.pointing.task import MergePtrsTask
from vega.uplink.pointing.task import PtrSanityCheckTask
from vega.uplink.pointing.task import RebasePtslTask

from vega.uplink.commanding.task import SimulateTask

from vega.uplink.planning.task import SaveMappsProductsTask

toolRegistry.register(FecsSummaryTask());
toolRegistry.register(RosettaFecsSummaryTask());
toolRegistry.register(CreateTimelineTask());
toolRegistry.register(CompareFecsTask());
toolRegistry.register(SavePorTask())
toolRegistry.register(SaveItlTask())
toolRegistry.register(PorCheckTask())
toolRegistry.register(SavePorgTask())
toolRegistry.register(SimulateTask())
toolRegistry.register(SavePtrTask())
toolRegistry.register(SavePdfmTask())
toolRegistry.register(ComparePtrsTask())
toolRegistry.register(MergePtrsTask())
toolRegistry.register(PtrSanityCheckTask())
toolRegistry.register(RebasePtslTask())
toolRegistry.register(SaveMappsProductsTask())
def tunnel():
	HipeGitSshTunnel.startTunnel()
from com.mysql.jdbc import Driver
from java.lang import *
from com.mysql.jdbc import *
from java.sql import *
from java.text import SimpleDateFormat
from herschel.share.fltdyn.time import FineTime
from com.mysql.jdbc import Driver

def mustReader(paramMnemonics,startDate,endDate) :
        #
        driverName="com.mysql.jdbc.Driver"
        #
        #Class.forName(driverName)
        #
        url = "jdbc:mysql://"+Configuration.getProperty("vega.must.server.ip")+"/"+Configuration.getProperty("vega.must.server.repository")+"?user="+Configuration.getProperty("vega.must.server.user")+"&password="+Configuration.getProperty("vega.must.server.password")
        #
        loader = Thread.currentThread().getContextClassLoader()
        c = loader.loadClass("com.mysql.jdbc.Driver")
        #print c
        driver = c.newInstance()
        #print driver
        con = driver.connect(url, java.util.Properties())
        #con = DriverManager.getConnection(url)
        
        stmt = con.createStatement()
        formatter = SimpleDateFormat ("yyyy-MM-dd HH:mm:ss")
        
        date1 =  formatter.parse(endDate)
        date2 =  formatter.parse(startDate)
        stringDate1 = formatter.format(date1)
        stringDate2 = formatter.format(date2)
        #
        p = Product()
        #ah=TableDataset()
        
        p.meta["type"]=StringParameter(value="MUST Parameter Product")
        p.meta["creator"]=StringParameter(value="MustClient")
        p.meta["description"]=StringParameter(value="MUST")
        p.meta["startDate"]=DateParameter(value=FineTime(date2))
        p.meta["endDate"]=DateParameter(value=FineTime(date1))
        #
        for pars in range(len(paramMnemonics)) :
                #
                ah=TableDataset()
                parameterName = paramMnemonics[pars]
                p.meta[parameterName]=BooleanParameter(value=Boolean(True))
                #
                sql = "select PID,DBTYPE from parameter where (PNAME='"+parameterName+"')"
                rs = stmt.executeQuery(sql)
                
                while (rs.next()):
                        pid=rs.getInt(1)
                        tableName = rs.getString(2)+"paramvalues"
                
                sql = "select datetime,value from %s where (pid=%s and datetime<'%s' and datetime>'%s')"%(tableName,pid,date1.time,date2.time)
                
                rs = stmt.executeQuery(sql)
                
                list = []
                dates=Long1d()
                values=Float1d()
                
                while (rs.next()):
                        dates.append(rs.getLong(1))
                        values.append(rs.getFloat(2))
                
                print "Retrieved parameter from MUST: ", parameterName
                
                if (pars == 0) :
                        #ah["Time"]=Column(data=dates,description="Time")
                        ah.meta["creator"]=StringParameter(value="MustClient")
                        ah.meta["description"]=StringParameter(value="MUST")
                        ah.meta["startDate"]=DateParameter(value=FineTime(date2))
                        ah.meta["endDate"]=DateParameter(value=FineTime(date1))
                #
                ah["Time"]=Column(data=dates,description="Time")
                ah[parameterName]=Column(data=values,description=parameterName)
                #
                p[parameterName]=ah
        rs.close()
        stmt.close()
        con.close()
        #
        return p
#End on function definition


#del(ins, instruments)

del(toolRegistry)

from herschel.ia.gui.kernel import ExtensionRegistry
from herschel.ia.gui.kernel import Extension
from herschel.ia.gui.kernel.menus import ActionMaker
#from sys import path
from java.io import File
from herschel.ia.gui.kernel.util import SiteFileUtil
from herschel.share.util import FileNameExtensionFilter
from herschel.ia.gui.kernel.prefs import UserPreferences
from herschel.share.util import Configuration
from rosetta.uplink.pointing import ExclusionPeriod
from rosetta.uplink.pointing import LanderVisibility
from vega import IconResources


#REGISTRY = ExtensionRegistry.getInstance();
REGISTRY.register("site.fileType",Extension("site.file.exclusion","rosetta.uplink.pointing.ExclFile","regex:^EXCL_[a-zA-Z0-9_\-]*.evf",IconResources.FECS_ICON_NOROOT));
REGISTRY.register(REGISTRY.COMPONENT,Extension("ExclusionPeriod Reader","rosetta.uplink.pointing.ExclFileComponent","factory.editor.file","rosetta.uplink.pointing.ExclFile"))
REGISTRY.register("site.fileType",Extension("site.file.exclusion","rosetta.uplink.pointing.LanderFile","regex:^OEF_[a-zA-Z0-9_\-]*.evf",IconResources.FECS_ICON_NOROOT));
REGISTRY.register(REGISTRY.COMPONENT,Extension("LanderPeriod Reader","rosetta.uplink.pointing.LanderFileComponent","factory.editor.file","rosetta.uplink.pointing.LanderFile"))


#import rosetta
from vega.uplink.pointing.net import AttitudeGeneratorFDImpl
toolRegistry = TaskToolRegistry.getInstance()
from rosetta.uplink.pointing import RosettaPtrCheckTask
from rosetta.uplink.pointing import RosettaBackupPtrTask

toolRegistry.register(RosettaPtrCheckTask())
toolRegistry.register(RosettaBackupPtrTask())

SsmmSimulator.registerSsmmSimulatorInstance("rosetta.uplink.commanding.RosettaSsmmSimulator")

del(toolRegistry)

del(REGISTRY)

from vega.uplink import Properties
from herschel.ia.gui.kernel.util import VariablesUtil
directory=Configuration.getProperty(Properties.DEFAULT_OBSERVATIONS_DIRECTORY)+"/"
#print directory
files=java.io.File(directory).listFiles()
if files!=None:
	for i in files:
		name=i.getName()
		if (name[:4]=="OBS_"):
			o=ObservationUtil.readObservationFromFile(directory+name)
			variableName = o.getName()
			variableName= String(variableName).replaceAll(" ", "_")
			VariablesUtil.addVariable(variableName, o)
del (directory,files,i,name,o,variableName)





