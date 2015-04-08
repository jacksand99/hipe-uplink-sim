package vega.uplink.commanding;

import java.util.HashMap;
import java.util.logging.Logger;

public class MappsModesOrcdMap {
	private static HashMap<String,String> map;
	private static final Logger LOG = Logger.getLogger(MappsModesOrcdMap.class.getName());
	private static HashMap<String,String> getMap(){
		if (map!=null) return map;
		else{
			map=new HashMap<String,String>();
			map.put("GIADA GDS ON", "GDS_PW_On");
			map.put("GIADA GDS OFF", "GDS_PW_Off");
			map.put("GIADA IS ON", "IS_PW_On");
			map.put("GIADA IS OFF", "IS_PW_Off");
			map.put("GIADA LAS ON", "LAS_PW_On");
			map.put("GIADA LAS OFF", "LAS_PW_Off");
			map.put("GIADA MBS ON", "MBS_PW_On");
			map.put("GIADA MBS OFF", "MBS_PW_Off");
			//map.put("OSIRIS DPU MCB_ON", "");
			//map.put("OSIRIS HEATERS OP_HTR", "");
			map.put("OSIRIS MMB ON", "SR_PW_MMB_On");
			map.put("OSIRIS MMB OFF", "SR_PW_MMB_Off");
			map.put("OSIRIS NAC SCIENCE", "SR_PW_NAC_On");
			map.put("OSIRIS WAC SCIENCE", "SR_PW_WAC_On");
			//map.put("RPC IES NORMAL", "");
			map.put("RPC IES_HV HV", "RP_PW_IES_HV");
			map.put("RPC LAP NORMAL", "RP_PW_LAP_On");
			map.put("RPC MAG NORMAL", "RP_PW_MAG_On");
			map.put("RPC MIP NORMAL", "RP_PW_MIP_On");
			map.put("RPC PIU NORMAL", "RP_PW_PIU_On");
			map.put("ALICE CHECKOUT", "AL_PW_Operational");
			map.put("COSIMA SCIENCE", "CS_PW_Science");
			map.put("MIDAS APPROACH", "MD_PW_Approach");
			map.put("MIRO CTS_DUAL_CONTINUUM", "MR_PW_CTSDual");
			map.put("OSIRIS IDLE", "SR_PW_Main_Idle");
			map.put("ROSINA DPU", "RN_PW_DPUOnly");
			//map.put("RPC ON", "");
			map.put("SREM ON", "SE_PW_On");
			
		}
		return map;
	}
	
	public static void initStates(SimulationContext sc){
		SuperPor por = sc.getPor();
		String[] initModes = por.getInitModes().toArray();
		for (int i=0;i<initModes.length;i++){
			String translation = getMap().get(initModes[i]);
			if (translation==null){
				LOG.info("Unknown mode "+initModes[i]);
			}else{
				sc.getModelState().setState(translation);
			}
		}
		
		String[] initMS = por.getInitMS().toArray();
		for (int i=0;i<initMS.length;i++){
			String translation = getMap().get(initMS[i]);
			if (translation==null){
				LOG.info("Unknown mode "+initMS[i]);
			}else{
				sc.getModelState().setState(translation);
			}
		}

	}
	
	public static void initMemory(SimulationContext sc,SsmmSimulator memorySimulator){
		SuperPor por = sc.getPor();
		String[] initMemory=por.getInitMemory().toArray();
		HashMap<String,Float> iMem=new HashMap<String,Float>();
		for (int i=0;i<initMemory.length;i++){
			String memoryLine = initMemory[i];
			memoryLine=memoryLine.trim();
			memoryLine=memoryLine.replace(" [Mbits]", "");
			memoryLine=memoryLine.replace("[Mbits]", "");
			String[] parts = memoryLine.split(" ");
			String ins = parts[0];
			float mem = Float.parseFloat(parts[parts.length-1]);
			iMem.put(ins, mem);
			
		}
		
		
	}
}
