PTR=PtrUtils.readPTRfromFile("/Users/jarenas 1/Rosetta/hack 15/mapps/MTP_015_H_V01/PTR/PTRM_PL_M015______01_H_RSM0PIM0.ROS")
PERIOD="MTP_015"
PTSL=PtrUtils.readPTRfromFile("/Users/jarenas 1/Rosetta/hack 15/mapps/MTP_015_H_V01/PTR/PTSL_DL_005_02____H__00116.ROS")
PDFM=PtrUtils.readPdfmfromFile("/Users/jarenas 1/Rosetta/hack 15/mapps/MTP_015_H_V01/PTR/PDFM_DM_015_01____H__ZZZZZ.ROS")
PLANNING_DEF= Plan.readFromFile("/Users/jarenas 1/OPS/ROS_SGS/PLANNING/LTP005/LTP005H/ros_mtp_stp_vstp_LTP005H.def")
EXCLUSION = ExclusionPeriod.readFromFile("/Users/jarenas 1/OPS/ROS_SGS/PLANNING/LTP005/LTP005H/EXCL_DL_005_03____H__00137.evf")
FECS= PorUtils.readFecsFromFile("/Users/jarenas 1/OPS/ROS_SGS/PLANNING/RMOC/FCT/FECS_________________XXXXX.ROS")




ptslSegment=PTSL.getSegment(PERIOD)
sch=Schedule(ptslSegment)
sch.setPdfm(PDFM)
sch.setPlan(PLANNING_DEF)
sch.setExclusion(EXCLUSION)
sch.setFecs(FECS)

del(PTR,PERIOD,PTSL,PDFM,PLANNING_DEF,EXCLUSION,FECS,ptslSegment)
