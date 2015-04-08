# Introduction #

You can use Hipe with the UPPS plugin installed to schedule observations and produce a timeline in ITL format and the PTRM.



# What is a schedule #

An schedule is composed of:

  * Observations (explained later)
  * A PTSL segment
  * Optionally you can include also the PDFM, to allow checking directly with the FD server

# How to create an empty schedule #

The easier way is to create it from the command line or a jython script:
```

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
```

This script is also available from the menu tools-hus-createEmptySchedule.py

YOu have to change the PTSL path, the name of segment, the target file name and the PDFM path for your desired ones.

Once you execute the script you will have a variable called sch in the variables view and you can double click over it to start editing your schedule.

# What are observations #

An observation is a dataset that contains:
  * Metadata like the start time and end time of the observation
  * Pointing definition, with time relative to the start time or end time of the observation
  * Sequences, with execution time also relative to the start time and end time of the observation.

The Observations can be defined in XML. AN example observation will be like:
```
<observation>
	<name>1412768679616_OBS</name>
	<type>Unknown</type>
	<creator>Unknown</creator>
	<description>Unknown</description>
	<instrument>ALICE</instrument>
	<formatVersion>1.0</formatVersion>
	<startDate>2014-10-08T10:44:39</startDate>
	<endDate>2014-10-08T15:44:39</endDate>
	<sequences>
		<sequence name="AALS102A">
			<executionTime>
				<actionTime>START_OBS+00:31:00</actionTime>
			</executionTime>
			<parameterList count="2">
					<parameter name="VAL01340" position="1">
						<value representation="Engineering">EEPROM 0</value>
					</parameter>
					<parameter name="VAL01341" position="2">
						<value representation="Raw" radix="Hexadecimal">0</value>
					</parameter>
			</parameterList>
			<profileList count="2">
					<profile type="PW">
						<timeOffset>00:00:00</timeOffset>
						<value>6.3</value>
					</profile>
					<profile type="DR">
						<timeOffset>00:00:00</timeOffset>
						<value>1500.0</value>
					</profile>
			</profileList>
		</sequence>
		<sequence name="AALS103A">
			<executionTime>
				<actionTime>START_OBS+01:39:29</actionTime>
			</executionTime>
			<profileList count="2">
					<profile type="PW">
						<timeOffset>00:00:00</timeOffset>
						<value>0.0</value>
					</profile>
					<profile type="DR">
						<timeOffset>00:00:00</timeOffset>
						<value>0.0</value>
					</profile>
			</profileList>
		</sequence>
		<sequence name="AALS102A">
			<executionTime>
				<actionTime>START_OBS+02:15:00</actionTime>
			</executionTime>
			<parameterList count="2">
					<parameter name="VAL01340" position="1">
						<value representation="Engineering">EEPROM 0</value>
					</parameter>
					<parameter name="VAL01341" position="2">
						<value representation="Raw" radix="Hexadecimal">0</value>
					</parameter>
			</parameterList>
			<profileList count="2">
					<profile type="PW">
						<timeOffset>00:00:00</timeOffset>
						<value>6.3</value>
					</profile>
					<profile type="DR">
						<timeOffset>00:00:00</timeOffset>
						<value>1500.0</value>
					</profile>
			</profileList>
		</sequence>
		<sequence name="AALS103A">
			<executionTime>
				<actionTime>START_OBS+03:29:29</actionTime>
			</executionTime>
			<profileList count="2">
					<profile type="PW">
						<timeOffset>00:00:00</timeOffset>
						<value>0.0</value>
					</profile>
					<profile type="DR">
						<timeOffset>00:00:00</timeOffset>
						<value>0.0</value>
					</profile>
			</profileList>
		</sequence>
	</sequences>
	<pointingBlocks>
		<block ref='OBS'>
			<startTime>START_OBS+00:00:00</startTime>
			<endTime>END_OBS-00:00:30</endTime>
			<attitude ref='track_pwropt'>
				<boresight ref='MiroSMM'/>
				<target ref='CG'/>
				<offsetRefAxis ref='SC_Xaxis'/>
				<offsetAngles ref='fixed'>
					<xAngle units='deg'>0.000008005</xAngle>
					<yAngle units='deg'>0.</yAngle>
				</offsetAngles>
			</attitude>
			<metadata>
				<comment>ALICE GCS</comment>
			</metadata>
		</block>
	</pointingBlocks>
</observation>
```

There are a number of templates available in the Variables view, under Observations.
You can Doubel click to see then.
For an observation file to be read by Hipe, it need to be called `OBS_*.ROS` for Rosetta.
If you have observations defined in XML in files, just navigate in the Navigator View and double click over it.

# Manipulate an schedule #

If you double click over and schedule varibale (sch in the above example), the Schedule Editor will Open:

![https://hipe-uplink-sim.googlecode.com/git/hipe-uplink-sim/vega/doc/images/SchedulerEditor.png](https://hipe-uplink-sim.googlecode.com/git/hipe-uplink-sim/vega/doc/images/SchedulerEditor.png)

In this View you the a chart containing:
  * One row with the PTSL
  * One row with the observations
  * One row with the resulting PTR
  * One row per instrument showing the mode transitions

You can add observations by drag and drop observations from the variables view, by clicking in the + button below the observation list or by copying an existing observation from the schedule clicking in the C button.

Once you have your observation added to the schedule you can:
  * Edit the metadata like start time and end time in the Metadata tab
  * Edit the XML of the observation in the XML tab
  * Copy it by clicking in the C button, delete it by clicking in the - button, or split it in 2 by clicking in the / button.

To perform this operations it is recomended you unselect the recalculate box.

Te reason is that every time you change an observation, the PTR and the mode transitions are recalculated. This can take few seconds to perform and if you are going to make a lot of changes it can be very long.

You can always check again the box to get a preview of the PTR and the mode transitions.

![https://hipe-uplink-sim.googlecode.com/git/hipe-uplink-sim/vega/doc/images/RecalculateBox.png](https://hipe-uplink-sim.googlecode.com/git/hipe-uplink-sim/vega/doc/images/RecalculateBox.png)

Once you have finish to edit your schedule you can:
  * Save it to the file: by clicking in Save Schedule button
  * Check the PTR for sanity (block length, syntaxt, etc)
  * Check the PTR againts the FD server (Take some minutes)
  * Check the PTR againts the FD server (Take some minutes)
  * Check the POR againts the ORCD

If you want to export the schedule to MAPPS you can double clik over the task saceMappsProductsTask in the task view. You can introduce here a NAME to the files and a path.
If you clik in Execute button the following files will be saved in the selected path:
  * NAME.def (Mapps top event definition)
  * NAME.evf (Mapps event file)
  * NAME.itl (Mapps itl)
  * PTRM\_NAME.ROS (PTRM)
  * PDFM\_NAME.ROS (PDFM)

That should be all the files you need to load the schedule in MAPPS.