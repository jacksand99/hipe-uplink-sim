from vega.uplink.planning import Observation
from vega.uplink.planning import ObservationEvent
from vega.uplink.planning import ObservationPointingBlock
from vega.uplink.planning import ObservationSequence
from vega.uplink.planning import ObservationUtil
from vega.uplink.planning import Schedule

PERIOD="MTP_011"
PTSL=PtrUtils.readPTRfromFile("/Users/jarenas 1/OPS/PTSL/PTSL_DL_004_01____H__00076.ROS")
TARGET_SCHEDULE="/Users/jarenas 1/Rosetta/hack 11/SCH_MTP_11_H_X06_v5.ROS"
PDFM=PtrUtils.readPdfmfromFile("/Users/jarenas 1/Rosetta/hack 11/PDFM_11.ROS")


ptslSegment=PTSL.getSegment(PERIOD)
sch=Schedule(ptslSegment)

sch.setPdfm(PDFM)
ObservationUtil.saveScheduleToFile(TARGET_SCHEDULE,sch)
del(PERIOD,PTSL,TARGET_SCHEDULE,PDFM,ptslSegment)
