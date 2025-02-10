import vega
from vega.uplink.commanding import SimulationContext
from vega.uplink.commanding.itl import ItlParser
from vega.uplink.pointing import PtrUtils
from vega.uplink.commanding import PorUtils
from vega.uplink.commanding import RosettaSsmmSimulator
simulationContext=SimulationContext()
#Read ITL end Event file. The 3rd parameter id the Unique ID sed (The number tat will start the inique ID count)
sp=ItlParser.parseItl("/some/path/OPS/ROS_SGS/PLANNING/LTP001/LTP001A/MTP005A/TLIS_PL_M005______01_A_OPS0001A.itl", "/Users/jarenas 1/OPS/ROS_SGS/PLANNING/LTP001/LTP001A/MTP005A/TLIS_PL_M005______01_A_OPS0001A.evf","/Users/jarenas 1/Downloads/MAPPS/RMOC/",10000)
#Read the PTR
ptr=PtrUtils.readPTRfromFile("/some/path/OPS/ROS_SGS/PLANNING/LTP001/LTP001A/MTP005A/PTR/PTRM_DM_005_01____A__00006.ROS")
#Read the FECS
fecs=PorUtils.readFecsFromFile("/some/path/Downloads/MAPPS/FECS_DL_001_02_______00022.ROS")
#Set the data into the the simulation context
simulationContext.getPor().addPor(sp)
simulationContext.setPtr(ptr)
simulationContext.setFecs(fecs)
#Load the Rosetta memory simulator
ssmmSimulator=RosettaSsmmSimulator()