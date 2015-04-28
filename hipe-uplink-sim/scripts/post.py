from vega.uplink.pointing import *
import java
from java.util import Date
from vega.uplink.commanding import *

def checkStatus(sTime,eTime,forbidden_status,hm):
	modes=hm.getModesBetween(sTime,eTime)
	for m in modes:
		if (m==forbidden_status):
			return True
	return False
def checkCommand(sTime,eTime,forbidden_command,st):
	seqs=st.getSequencesBetween(sTime,eTime)
	for s in seqs:
		if (s.getName()==forbidden_command):
			return True
	return False
#WOL / OCM and Payload State Constraints
if (simulationContext.getPtr()!=None):
    ptrSegment=simulationContext.getPtr().getSegments()[0]
    #WOL Payload State Constraints
    #ALICE
    blocks=ptrSegment.getAllBlocksOfType("MWOL").getBlocks()
    forb_man=["AL_Door_Open","AL_HV_On"]
    for fb in forb_man:
    	for b in blocks:
    		if checkStatus(b.getStartTime(),Date(b.getEndTime().getTime()+1800000),fb,simulationContext.getHistoryModes()):
    			simulationContext.log("Forbidden "+fb+" during MWOL + 30m at "+DateUtil.defaultDateToString(b.getStartTime()))
    blocks=ptrSegment.getAllBlocksOfType("MWNV").getBlocks()
    for fb in forb_man:
    	for b in blocks:
    		if checkStatus(Date(b.getEndTime().getTime()-2100000),Date(b.getEndTime().getTime()+1800000),fb,simulationContext.getHistoryModes()):
    			simulationContext.log("Forbidden "+fb+" during last 25min of MWNV + 30m at "+DateUtil.defaultDateToString(b.getStartTime()))
    #COSIMA
    forb_comm=["ACSS120Z","ACSS123Z","ACSS124Z","ACSS125Z"]
    blocks=ptrSegment.getAllBlocksOfType("MWOL").getBlocks()
    for fc in forb_comm:
    	for b in blocks:
    		if checkCommand(b.getStartTime(),b.getEndTime(),fc,simulationContext.getSequenceTimeline()):
    			simulationContext.log("Forbidden "+fc+" during MWOL at "+DateUtil.defaultDateToString(b.getStartTime()))
    for fc in forb_comm:
    	for b in blocks:
    		if checkCommand(Date(b.getStartime().getTime()-19800000),b.getStartTime(),fc,simulationContext.getSequenceTimeline()):
    			simulationContext.log("Forbidden "+fc+" 5.5h before MWOL at "+DateUtil.defaultDateToString(b.getStartTime()))
    blocks=ptrSegment.getAllBlocksOfType("MWNV").getBlocks()
    for fc in forb_comm:
    	for b in blocks:
    		if checkCommand(Date(b.getEndTime().getTime()-2100000),b.getEndTime(),fc,simulationContext.getSequenceTimeline()):
    			simulationContext.log("Forbidden "+fc+" during last 25min of MWNV at "+DateUtil.defaultDateToString(b.getStartTime()))
    for fc in forb_comm:
    	for b in blocks:
    		if checkCommand(Date(b.getEndTime().getTime()-1500000-19800000),Date(b.getEndTime().getTime()-1500000),fc,simulationContext.getSequenceTimeline()):
    			simulationContext.log("Forbidden "+fc+" 5.5h before last 25min of MWNV at "+DateUtil.defaultDateToString(b.getStartTime()))
    #RPC_ICA, OSIRIS-NAC & OSIRIS-WAC
    blocks=ptrSegment.getAllBlocksOfType("MWOL").getBlocks()
    forb_man=["RP_PW_ICA_HV","SR_NAC_Door_Open","SR_WAC_Door_Open"]
    for fb in forb_man:
    	for b in blocks:
    		if checkStatus(b.getStartTime(),b.getEndTime(),fb,simulationContext.getHistoryModes()):
    			simulationContext.log("Forbidden "+fb+" during MWOL at "+DateUtil.defaultDateToString(b.getStartTime()))
    blocks=ptrSegment.getAllBlocksOfType("MWNV").getBlocks()
    for fb in forb_man:
    	for b in blocks:
    		if checkStatus(Date(b.getEndTime().getTime()-2100000),b.getEndTime(),fb,simulationContext.getHistoryModes()):
    			simulationContext.log("Forbidden "+fb+" during last 25min of MWNV at "+DateUtil.defaultDateToString(b.getStartTime()))
    #OCM and Payload State Constraints
    #ALICE
    blocks=ptrSegment.getAllBlocksOfType("MOCM").getBlocks()
    forb_man=["AL_Door_Open","AL_HV_On"]
    for fb in forb_man:
    	for b in blocks:
    		if checkStatus(b.getStartTime(),Date(b.getEndTime().getTime()+1800000),fb,simulationContext.getHistoryModes()):
    			simulationContext.log("Forbidden "+fb+" during MOCM + 30m at "+DateUtil.defaultDateToString(b.getStartTime()))
    #COSIMA
    #blocks=ptrSegment.getAllBlocksOfType("MOCM").getBlocks()
    for fc in forb_comm:
    	for b in blocks:
    		if checkCommand(b.getStartTime(),b.getEndTime(),fc,simulationContext.getSequenceTimeline()):
    			simulationContext.log("Forbidden "+fc+" during MOCM at "+DateUtil.defaultDateToString(b.getStartTime()))
    for fc in forb_comm:
    	for b in blocks:
    		if checkCommand(Date(b.getStartTime().getTime()-19800000),b.getStartTime(),fc,simulationContext.getSequenceTimeline()):
    			simulationContext.log("Forbidden "+fc+" 5.5h before MOCM at "+DateUtil.defaultDateToString(b.getStartTime()))
    #ROSINA, RPC_ICA, OSIRIS-NAC,OSIRIS-WAC & VIRTIS
    forb_man=["RP_PW_ICA_HV","SR_NAC_Door_Open","SR_WAC_Door_Open","VR_PW_ME_Idle","VR_PW_ME_Safe"]
    for fb in forb_man:
    	for b in blocks:
    		if checkStatus(b.getStartTime(),b.getEndTime(),fb,simulationContext.getHistoryModes()):
    			simulationContext.log("Forbidden "+fb+" during MOCM at "+DateUtil.defaultDateToString(b.getStartTime()))


del(blocks,fb,fc,forb_comm,forb_man,ptrSegment)