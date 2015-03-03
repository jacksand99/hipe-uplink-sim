from vega.uplink.pointing import *
import java
from vega.uplink.commanding import *
dateFormat2 = java.text.SimpleDateFormat("dd-MMM-yyyy'_'HH:mm:ss")
dateFormat2.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));

def checkStatus(sTime,eTime,forbidden_status,hm):
	tm=hm.getTimes()
	for k in tm:
		if (k>=sTime and k<=eTime):
			if (hm.getStates(k).getStateForMode(forbidden_status)==forbidden_status):
				simulationContext.log("Forbidden "+forbidden_status+" during "+hm.getStates(k).getState("PTR_")+" at "+dateFormat2.format(java.util.Date(k)))
def checkCommand(sTime,eTime,forbidden_command,hm):
	tm=hm.getTimes()
	for k in tm:
		if (k>=sTime and k<=eTime):
			if (hm.getCommand(k)==forbidden_command):
				simulationContext.log("Forbidden "+forbidden_command+" at "+dateFormat2.format(java.util.Date(k)))
#PTR vs commanding constrains checks
forb_man=["RP_PW_ICA_HV","SR_NAC_Door_Open","SR_WAC_Door_Open"]
forb_comm=["ACSS120Z","ACSS180Z","ACSS123Z","ACSS124Z"]
for j in simulationContext.getHistoryModes().getTimes():
	states=simulationContext.getHistoryModes().getStatesAt(j+1)
	if (states==None):
		#print j
		ptr_state="None"
	else:
		ptr_state=states.getState("PTR_")
	if (ptr_state=="PTR_MWOL" or ptr_state=="PTR_MWNV"):
		for f in forb_man:
			if (states.getStateForMode(f)==f):
				simulationContext.log("Forbidden "+f+" during "+ptr_state+" at "+dateFormat2.format(java.util.Date(j)))
		for c in forb_comm:
			checkCommand(j-21600000,j,c,simulationContext.getHistoryModes())
			if (simulationContext.getHistoryModes().getCommand(j)==c):
				simulationContext.log("Forbidden command "+c+" during "+ptr_state+" at "+dateFormat2.format(java.util.Date(j)))
		checkStatus(j,j+1800000,"AL_PW_On",simulationContext.getHistoryModes())
		checkStatus(j,j+1800000,"AL_PW_Operational",simulationContext.getHistoryModes())
		checkStatus(j,j+1800000,"AL_Door_Open",simulationContext.getHistoryModes())
forb_man=["RN_PW_DPU_Only","RN_PW_COPS","RP_PW_ICA_HV","SR_NAC_Door_Open","SR_WAC_Door_Open", "AL_PW_On","AL_PW_Operational","VR_PW_ME_Idle","VR_PW_ME_Safe"]
forb_comm=["ACSS120Z","ACSS180Z","ACSS123Z","ACSS124Z"]
for j in simulationContext.getHistoryModes().getTimes():
	states=simulationContext.getHistoryModes().getStates(j+1)
	if (states==None):
		#print j
		ptr_state="None"
	else:
		ptr_state=states.getState("PTR_")
	#print ptr_state
	if (ptr_state=="PTR_MOCM"):
		#print "In maintenance",ptr_state
		for f in forb_man:
			if (states.getStateForMode(f)==f):
				simulationContext.log("Forbidden "+f+" during "+ptr_state+" at "+dateFormat2.format(java.util.Date(j)))
		for c in forb_comm:
			if (simulationContext.getHistoryModes().getCommand(j)==c):
				simulationContext.log("Forbidden command "+c+" during "+ptr_state+" at "+dateFormat2.format(java.util.Date(j))) 
		checkStatus(j,j+1800000,"ALICE_PW_On",simulationContext.getHistoryModes())
		checkStatus(j,j+1800000,"ALICE_PW_Operational",simulationContext.getHistoryModes())
del(dateFormat2)
del(c)
del(f)
del(forb_man)
del(forb_comm)
del(j)
del(ptr_state)
del(states)