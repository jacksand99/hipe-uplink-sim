# UPLINK PLANNING & SIMULATION SYSTEM (UPSS) #



# Overview #

The Uplink Planning & Simulation System plugin is a software module that allows Hipe to load, write and manipulate operational files like PORs, PTRs, FECS, EVTM, ITLs, MIB, etc.
With the plugin installed Hipe is able to read, manipulate, write and simulate the execution of sequences in a POR, calculating power, datarate, spacecraft mode transitions, etc, check that no ORCD constrain in violated and the sequences are MIB compliant.
The plugin allows Hipe also to design and simulate the pointing of the spacecraft and check the validity against the Flight Dynamic ICD and that the pointing is possible connecting with FD servers at ESOC.
Finally, with the plugin Hipe can also be used to perform mission planning, defining observations composed of commanding sequences and pointing blocks and simulate and check the validity of the plan produced using the abilities described before.

# Hipe #
Hipe
Hipe is the data processing environment developed by Herschel Data Processing group. Originally was designed to load and manipulate Herschel Science Observations.
Hipe was never used to perform mission planning for Herschel.
Hipe is a Jython (Python for Java) IDE. It provides a Jython editor and jython interpreter and integrated in a nice GUI.
Hipe also provides an API to develop tasks (functions that can be executed from the GUI) and an API to develop graphical views to be added to the IDE to view data.
Hipe also provides Java mathematical, pointing, storage and dataset libraries.
Hipe can be installed with a commercial installer in Linux, Windows and OS X.
While Hipe has a lot of Herschel specific stuff, it also provides a plugin API, that allows any Java developer to  easily develop plugins that will be perfectly integrated in Hipe.

# Use of UPSS #
UPSS is used by Rosetta to:
  * Validate operational products before they are sent to MOC.
It acts as a “Shadow” planning system, performing most of the task that the primes system do, with a totally different implementation, providing independent validation.
  * Read, manipulate, compare and interpret the files received form MOC. For example, UPSS is used to compare the FECS (GS coverage) files received from MOC, highlighting the GS passes removed, added, extended or reduced from one version of the FECS to the next, allowing the planning group to react to new data volume constrains.
  * Produce test data to test the main Mission Planning System of Rosetta.
  * Perform ac-hoc operations over operational files, line bulk manipulation of PTRs.

# UPSS development #
HIpe was used as base for UPSS because:
  * It allows a very fast development and protyping. You can develop tor datasets and functions and use it directly from the Hipe Jython console or Jython scripts without the need to write specific GUIs.
  * Hipe is locally developed at ESAC and it is still developed and maintained by the Herschel DP team.
  * Java is the main development language at ESAC. Using Hipe allows fast integration and re-use of other mission software modules.
UPSS is a good example of this easy integration. It uses Hipe (Hershel as base), for the Mission Planning GUI it re-uses libraries from Lisa Pathfinder and it has re-used also libraries from former Rosetta development for connection to FD servers.
  * Java is used a lot in the open source community, allowing the use in Hipe of open source libraries.
  * Hipe is multi-platform.
  * ESA has published Hipe with LGPL license and the source code is available from public servers.
  * The Hipe APIs are well documented.
  * Hipe plugin framework allows that no fork of the Hipe code was needed. The final user can install the latest version of Hipe with the installer and the install the plugin with few clicks. The plugin updates itself if a new version is released.
  * Hipe can be downloaded from http://www.cosmos.esa.int/web/herschel/hipe-download
UPSS has been fully developed in Java and Jython and has also been release with LGPL license. The source code can be found at: https://code.google.com/p/hipe-uplink-sim/
UPSS is composed of the following main modules:
  * Commanding: Full implementation of PORs, FECS, MIB reader. It also includes a ORCD implementation and a simulator to simulate the sequences in a POR. It can simulate power and mode transitions of the spacecraft subsystems and also has an implementation of Rosetta SSMM.
  * Pointing: Full implementation of FD ICD. It can handle PTRs, PDFMs, PTSLs and EVTMs. It included a PTR checker against FD ICD.
  * Planning: Modules that allow to define Observations composed of commanding and pointing. It allows to manually place observations and simulate in real time the sequences and pointings. It does not have an automatic scheduler.
  * A Rosetta FD connector, connecting the pointing module to the FD web server allowing the simulation of pointing and constrains violations.

# Use of UPSS as base for other missions #
UPSS was designed with the idea of reuse. Most of the mission specific features like instruments, specific commands, packet store size, etc are configurable from the properties panel.
Any mission that wishes to reuse UPSS “as it is” should develop a SSMM model, a pointing connector to FD and very little more.
The Spacecraft power model and subsystem transitions are coded in an ASCII table that can be easily edited.

# Use of UPSS as base for a common mission planning system #
UPSS can not be used yet as a prime Mission Planning System, many features like downlink prioritization, orbit simulation, illumination model, etc are not implemented, but could be done in the future.
The main two modules that UPSS are missing are:
  * An automatic scheduler. UPSS allows manual mission planning, but not yet automatic.
  * An Attitude Generator module, allowing the system to simulate the attitude of spacecraft without connecting to FD servers.

![https://hipe-uplink-sim.googlecode.com/git/hipe-uplink-sim/vega/doc/images/Simulation.png](https://hipe-uplink-sim.googlecode.com/git/hipe-uplink-sim/vega/doc/images/Simulation.png)
Sequence Simulation

![https://hipe-uplink-sim.googlecode.com/git/hipe-uplink-sim/vega/doc/images/Timeline.png](https://hipe-uplink-sim.googlecode.com/git/hipe-uplink-sim/vega/doc/images/Timeline.png)
Spacecraft model transitions

![https://hipe-uplink-sim.googlecode.com/git/hipe-uplink-sim/vega/doc/images/SequenceEditor.png](https://hipe-uplink-sim.googlecode.com/git/hipe-uplink-sim/vega/doc/images/SequenceEditor.png)
Telecommand Sequence Editor

![https://hipe-uplink-sim.googlecode.com/git/hipe-uplink-sim/vega/doc/images/ORCDModel.png](https://hipe-uplink-sim.googlecode.com/git/hipe-uplink-sim/vega/doc/images/ORCDModel.png)
ORCD Spacecraft Model

![https://hipe-uplink-sim.googlecode.com/git/hipe-uplink-sim/vega/doc/images/MIB.png](https://hipe-uplink-sim.googlecode.com/git/hipe-uplink-sim/vega/doc/images/MIB.png)
Spacecraft MIB

![https://hipe-uplink-sim.googlecode.com/git/hipe-uplink-sim/vega/doc/images/PtrEdition.png](https://hipe-uplink-sim.googlecode.com/git/hipe-uplink-sim/vega/doc/images/PtrEdition.png)
PTR Edition

![https://hipe-uplink-sim.googlecode.com/git/hipe-uplink-sim/vega/doc/images/PtrValidation.png](https://hipe-uplink-sim.googlecode.com/git/hipe-uplink-sim/vega/doc/images/PtrValidation.png)
PTR Validation

![https://hipe-uplink-sim.googlecode.com/git/hipe-uplink-sim/vega/doc/images/scheduler.png](https://hipe-uplink-sim.googlecode.com/git/hipe-uplink-sim/vega/doc/images/scheduler.png)
Observations Scheduler

![https://hipe-uplink-sim.googlecode.com/git/hipe-uplink-sim/vega/doc/images/properties.png](https://hipe-uplink-sim.googlecode.com/git/hipe-uplink-sim/vega/doc/images/properties.png)
Configuration

![https://hipe-uplink-sim.googlecode.com/git/hipe-uplink-sim/vega/doc/images/ApiDocumentation.png](https://hipe-uplink-sim.googlecode.com/git/hipe-uplink-sim/vega/doc/images/ApiDocumentation.png)
API Documentation

![https://hipe-uplink-sim.googlecode.com/git/hipe-uplink-sim/vega/doc/images/Scriptin.png](https://hipe-uplink-sim.googlecode.com/git/hipe-uplink-sim/vega/doc/images/Scriptin.png)
Jython Script manipulating Mission Planning Data



