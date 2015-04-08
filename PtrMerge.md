# Introduction #

This is a procedure to check and merge updates to PTR by PI teams from the Hipe command line. It can also be done from the tasks guis


# From script #
```

#Load the Master PTR
PTRM_MASTER = PtrUtils.readPTRfromFile("/Users/jarenas 1/Rosetta/hack 11/PTRM_PL_M011______01_H_RSM0PIM0.ROS")
#Load the update
PTRM_UPDATE = PtrUtils.readPTRfromFile("/Users/jarenas 1/Rosetta/hack 11/PTRM_VR_M011______01_H_RSM0PIM1.ROS")
#Load target PTR (originally just a copy of the master one)
PTRM_FINAL = PtrUtils.readPTRfromFile("/Users/jarenas 1/Rosetta/hack 11/PTRM_PL_M011______01_H_RSM1PIM0.ROS")
#Load the PDFM
PDFM = PtrUtils.readPdfmfromFile("/Users/jarenas 1/Rosetta/hack 11/PDFM_11.ROS")
#Load the PTSL
PTSL = PtrUtils.readPTRfromFile("/Users/jarenas 1/OPS/PTSL/PTSL_DL_004_01____H__00076.ROS")
#Load FECS
FECS = PorUtils.readFecsFromFile("/Users/jarenas 1/OPS/FECS/FECS_DL_003_01_______00043.ROS")
 

#See the differences between the master and the update.
 

#Check that they have only modified blocks belonging to theirselves
comparePtrsTask(ptr1=PTRM_MASTER, ptr2=PTRM_UPDATE)
 

#Check the update againsts FD server
rosettaPtrCheckTask(ptr=PTRM_UPDATE, ptsl=PTSL, pdfm=PDFM, fecs=FECS, trajectory='RORL_DL_004_01____H__00076')
 

#If everything is ok proceed to the merge
mergePtrsTask(masterPtr=PTRM_MASTER, ptr=PTRM_UPDATE, targetPtr=PTRM_FINAL)
 

#Save merged PTR
PtrUtils.savePTR(PTRM_FINAL)

```

# With the Schedule Editor #

```
from vega.uplink.planning import Observation
from vega.uplink.planning import ObservationEvent
from vega.uplink.planning import ObservationPointingBlock
from vega.uplink.planning import ObservationSequence
from vega.uplink.planning import ObservationUtil
from vega.uplink.planning import Schedule
#Load the Master PTR
PTRM_MASTER = PtrUtils.readPTRfromFile("/Users/jarenas 1/Rosetta/hack 11/PTRM_PL_M011______01_H_RSM0PIM0.ROS")
#Load the update
PTRM_UPDATE = PtrUtils.readPTRfromFile("/Users/jarenas 1/Rosetta/hack 11/PTRM_VR_M011______01_H_RSM0PIM1.ROS")
#Load the PDFM
PDFM = PtrUtils.readPdfmfromFile("/Users/jarenas 1/Rosetta/hack 11/PDFM_11.ROS")
TARGET_SCHEDULE="/Users/jarenas 1/Rosetta/hack 11/SCH_MTP_11_H_VIRTIS_02.ROS"
PERIOD="MTP_011"
INSTRUMENT="VIRTIS"
#Create an empty schedule with the master PTR as PTSL segment
sch=Schedule(PTRM_MASTER.getSegment(PERIOD))
sch.setPdfm(PDFM)

#Extract all observations of the instrument
#Repeat for all the updates you receive
obs=PTRM_UPDATE.getSegment(PERIOD).getAllBlocksOfInstrument(INSTRUMENT).getBlocks()
for o in obs:
        observation=Observation(o.getStartTime(),o.getEndTime())
        obsBlock1=ObservationPointingBlock(observation,ObservationEvent.START_OBS,0,ObservationEvent.END_OBS,0,o)
        observation.addObservationBlock(obsBlock1)
        ins=o.getInstrument()
        if (ins!=None):
                observation.setInstrument(ins)
                name=o.getMetadataElement().getComments().getArray()[0]
                observation.setName(name)
                print o.getStartTime()
                print o.getEndTime()
                print o.getInstrument()
                sch.addObservation(observation)
ObservationUtil.saveScheduleToFile(TARGET_SCHEDULE,sch)
#Double click over the variable sch and do all the changes you want

#Save PTR
PTR_FINAL=sch.getPtr()
savePtrTask(ptr=PTR_FINAL, pdfm=PDFM, name='PTRM_final_v2.ROS', path='/Users/jarenas 1/Rosetta/hack 11/')

```

Doucble click to variable sch.
Delete or modify the observations as desired