# Introduction #

Hipe with the UPPS plugin installed can open Mission Planning files. When possible, the files are in the ESA standard format (for example, commanding files are stored in POR or PORG files).
Once a file is opened is loaded into hipe as a toplevel Hipe Dataset. Most of the times, this dataset will contain low level datasets in a tree way.
For example, a POR contains sequences and the sequences, parameters and profiles.
Most of the dataset have default editors and viewers and can also be manipulated from jython scripts.
The files can be opened throught the GUI or by a jython command from the Hipe console.

# Opening from the GUI #

You can open a file from the GUI by double click over it in the Navigator View.
The files that can be opened by the plugin will have the icon ![https://hipe-uplink-sim.googlecode.com/git/hipe-uplink-sim/src/vega/vega.gif](https://hipe-uplink-sim.googlecode.com/git/hipe-uplink-sim/src/vega/vega.gif) next to it.

![https://hipe-uplink-sim.googlecode.com/git/hipe-uplink-sim/vega/doc/images/Navigator.png](https://hipe-uplink-sim.googlecode.com/git/hipe-uplink-sim/vega/doc/images/Navigator.png)

For the files to be identified as a type, it will have to follow a name convention.
For example, in Rosetta: the PORs will be named `POR*.ROS`, the PORG `PORG_*.ros`, the PTRs `PTRM_*.ROS`, the PTSL `PTSL_*.ROS`, the FECS `FECS_*.ROS`, etc...

Once you double click over the file you will see in the Hipe Console an echo of the command you can use to open the same file in ascript or from the Hipe console

![https://hipe-uplink-sim.googlecode.com/git/hipe-uplink-sim/vega/doc/images/ConsoleEcho.png](https://hipe-uplink-sim.googlecode.com/git/hipe-uplink-sim/vega/doc/images/ConsoleEcho.png)

It will also open the default editor for the file

![https://hipe-uplink-sim.googlecode.com/git/hipe-uplink-sim/vega/doc/images/EditorView.png](https://hipe-uplink-sim.googlecode.com/git/hipe-uplink-sim/vega/doc/images/EditorView.png)

You will also see the variable cretaed with the top level dataset in the Variables view

![https://hipe-uplink-sim.googlecode.com/git/hipe-uplink-sim/vega/doc/images/VariablesView.png](https://hipe-uplink-sim.googlecode.com/git/hipe-uplink-sim/vega/doc/images/VariablesView.png)

And you can see and navigate the contained datasets in the outline view

![https://hipe-uplink-sim.googlecode.com/git/hipe-uplink-sim/vega/doc/images/OutlineView.png](https://hipe-uplink-sim.googlecode.com/git/hipe-uplink-sim/vega/doc/images/OutlineView.png)

You can see the automatic task that are applicable to this dataset in the Task View

![https://hipe-uplink-sim.googlecode.com/git/hipe-uplink-sim/vega/doc/images/TaskView.png](https://hipe-uplink-sim.googlecode.com/git/hipe-uplink-sim/vega/doc/images/TaskView.png)

# Opening from the Hipe console #

You can always Open a file also from the command line or from a jython script.
For example, if you want to open a PTR from the command line:
```
PTRM_VR_M007_01_A_RSM6PIM1 = PtrUtils.readPTRfromFile("/Users/jarenas 1/OPS/ROS_SGS/PLANNING/LTP002/LTP002A/MTP007A/PTR/PTRM_VR_M007______01_A_RSM6PIM1.ROS")```

There are some datasets than can only be opened from command line because they do not map to only one file but several, for example, to open and itl:
```

from vega.uplink.commanding.itl import ItlParser
por=ItlParser("example.itl","example.evf","/home/user/evfs/",10)```

This is because you have to provide also the event file, the path where Hipe will try to look for the includes and the seed for the ids of the sequences.
The ITL will be loaded as a POR.

To browse the MIB:
```
mib=Mib.getMib()```