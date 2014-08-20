import java
from vega.uplink.commanding import *
from vega.uplink.pointing import *

#Set default modes
simulationContext.getModelState().setState("GD_PW_Off") #5.33
simulationContext.getPowerInstrument().setPower("GIADA",5.33)
simulationContext.getModelState().setState("MR_PW_Off") #3.6
simulationContext.getPowerInstrument().setPower("MIRO",3.6)
simulationContext.getModelState().setState("SR_PW_Main_Off") #26
simulationContext.getPowerInstrument().setPower("OSIRIS",25.5)
simulationContext.getPowerInstrument().setPower("OSIRIS",26)
simulationContext.getModelState().setState("RN_PW_Off")
simulationContext.getPowerInstrument().setPower("ROSINA",6.2)
simulationContext.getModelState().setState("VR_PW_ME_Off")
simulationContext.getPowerInstrument().setPower("VIRTIS",1.0)
simulationContext.getModelState().setState("SE_PW_On")
simulationContext.getPowerInstrument().setPower("SREM",2.3)
simulationContext.getModelState().setState("SR_NAC_Door_Closed")
simulationContext.getModelState().setState("SR_WAC_Door_Closed")
simulationContext.getModelState().setState("AL_Door_Closed")
simulationContext.getModelState().setState("VR_M_Cover_Closed")
simulationContext.getModelState().setState("ALICE_Illum_False")
simulationContext.getModelState().setState("COSIMA_Illum_False")
simulationContext.getModelState().setState("MIRO_Illum_False")
simulationContext.getModelState().setState("OSIRIS_Illum_False")
simulationContext.getModelState().setState("OSIRIS_Hard_Illum_False")
simulationContext.getModelState().setState("VIRTIS_Illum_False")
simulationContext.getModelState().setState("LANDER_Illum_False")
simulationContext.getModelState().setState("ZPos20_Illum_False")
simulationContext.getModelState().setState("ZNeg180_Illum_False")
simulationContext.getModelState().setState("ZNeg140_Illum_False")
simulationContext.getModelState().setState("ZNeg175_Illum_False")
simulationContext.getModelState().setState("ZNeg192_Illum_False")
simulationContext.getModelState().setState("YPos5_Illum_False")
simulationContext.getModelState().setState("YNeg5_Illum_False")
simulationContext.getModelState().setState("YPos30_Illum_False")
simulationContext.getModelState().setState("YNeg30_Illum_False")
simulationContext.getModelState().setState("YNeg40_Illum_False")
simulationContext.getModelState().setState("YPos40_Illum_False")
