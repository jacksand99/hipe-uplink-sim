#Script to show how to do different operations with PTRs

#Load PTR
ptr=PtrUtils.readPTRfromFile("/Users/jarenas 1/OPS/ROS_SGS/PLANNING/LTP003/LTP003A/MTP010A/PTR/PTRM_PL_M010______01_A_RSM0PIM0.ROS")
#It can also be done by doble click in the file navigator tree

#Load PDFM
pdfm=PtrUtils.readPdfmfromFile("/Users/jarenas 1/OPS/ROS_SGS/PLANNING/LTP002/LTP002A/MTP008A/PTR/PDFM_DM_008_01____A__00018.ROS")
#It can also be done by doble click in the file navigator tree

#Load PTSL
pdsl=PtrUtils.readPTRfromFile("/Users/jarenas 1/OPS/ROS_SGS/PLANNING/LTP002/LTP002A/MTP008A/PTR/PTSL_DL_002_02____A__00040.ROS")
#It can also be done by doble click in the file navigator tree

#All the following task can also be executed by selecting the PTR from the variables view
#Then, select Applicable from the Task view, and select the tasks
#The parameter can be added to the task gui by drag and drop from the variables view

#Save PTR with the same name and path as it was read:
savePtrTask(ptr=ptr)
#With different name and path
savePtrTask(ptr=ptr, name='PTRM_test.ROS', path='/Users/jarenas 1/Downloads')
#Linking it to a PDFM
savePtrTask(ptr=ptr, pdfm=pdfm, name='PTRM_test.ROS', path='/Users/jarenas 1/Downloads')
#The PDFM will  be saved ad PDFM_test.ROS and it will included in the PTR XML

#Check a Ptr for sintaxt sanity
ptrSanityCheckTask(ptr=ptr)
#With PTSL (Equivalent to PTR vs PTSL check in the FD tool)
ptrSanityCheckTask(ptr=ptr, ptsl=pdsl)

#The PTR can be edited by double click in the variable name

#Extract a Block Slice from the PTR
slice=ptr.getSlice(1)
miroSlice=ptr.getAllBlocksOfInstrument("MIRO") 

ptr2=PtrUtils.readPTRfromFile("/Users/jarenas 1/OPS/ROS_SGS/PLANNING/LTP003/LTP003A/MTP010A/PTR/PTRM_PL_M010______01_A_RSM0PIM0.ROS")
#Put back slices after mofification
#This can also be dono over a different PTR that the original one
ptr2.setSlice(slice)
ptr2.setSlice(miroSlice)

#Compare 2 PTRs
comparePtrsTask(ptr1=ptr, ptr2=ptr2)

#Load FECS
fecs=PorUtils.readFecsFromFile("/Users/jarenas 1/OPS/FECS/FECS_DL_002_01_______00035.ROS")

#Check PTR with FD server
#Go first to menu Edit/Preferences/Mission Planning/Pointing and check 
#the value of the property activity case to be set to the right trajectory
rosettaPtrCheckTask(ptr=ptr, ptsl=pdsl, pdfm=pdfm, fecs=fecs)

#Rebese the PTR in the PTSL (copy all maintenance from PTSL)
rebasePtslTask(ptr=ptr, ptsl=pdsl)

target=PtrUtils.readPTRfromFile("/Users/jarenas 1/OPS/ROS_SGS/PLANNING/LTP003/LTP003A/MTP010A/PTR/PTRM_PL_M010______01_A_RSM0PIM0.ROS")
#merge ptrs
#All differences between ptr and masterPtr will be copied to targetPtr
mergePtrsTask(masterPtr=ptr, ptr=ptr2, targetPtr=target)



