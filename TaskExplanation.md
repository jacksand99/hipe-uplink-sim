# Introduction #

A task in Hipe are routines than can be executed from the gui to avoid having to owrtite and execute a script.
Normally you define task for routine work.
A task are taking as parameters datasets.
If you select a variable in the variables view you will see the task that take as first parameter the selected type in the task view:

![https://hipe-uplink-sim.googlecode.com/git/hipe-uplink-sim/vega/doc/images/VariablesView.png](https://hipe-uplink-sim.googlecode.com/git/hipe-uplink-sim/vega/doc/images/VariablesView.png)

![https://hipe-uplink-sim.googlecode.com/git/hipe-uplink-sim/vega/doc/images/TaskView.png](https://hipe-uplink-sim.googlecode.com/git/hipe-uplink-sim/vega/doc/images/TaskView.png)

If you double click in the task, the task gui will open in the editor panel:

![https://hipe-uplink-sim.googlecode.com/git/hipe-uplink-sim/vega/doc/images/TaskGui.png](https://hipe-uplink-sim.googlecode.com/git/hipe-uplink-sim/vega/doc/images/TaskGui.png)

To file the parameters you need to drag and drop variables from the variables view into the dot next to paramter name.

If you cliock accept the task will execute and you will se in the console also the jython command you can use to execute the task from the hipe console or a script.



# Current tasks defined in the plugin #

  * PTRS, PTSLs and PDFMs:
    * ComparePtrsTask : Compare 2 PTRs and highlight differences
    * MergePtrsTask : Merge 2 PTRs into a third one
    * PtrSanityCheckTask : Check PTR againts PTSL and for syntaxt
    * RebasePtslTask : Rebase a PTR into the PTSL
    * RosettaPtrCheckTask : Check a PTR againts FD server
    * SavePtrTask : Save a PTR into a File
    * SavePdfmTask : Save PDFM into a file
  * PORs
    * SavePorTask : Save a POR into a file
    * SaveItlTask : Save a POR as an ITL
    * PorCheckTask : Check a POR againts the MIB
    * CreateTimelineTask : Create a mode transition graph from POR
    * SavePorgTask: Save a POR into a PORG
  * FECS
    * CompareFecsTask : Compare 2 FECS
    * FecsSummaryTask : Make a summary of a FECS
    * RosettaFecsSummaryTask : The same as above, custom Rosetta one.