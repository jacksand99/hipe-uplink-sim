package rosetta.uplink.commanding;

import vega.uplink.commanding.AbstractAllowOverlapChecker;
import vega.uplink.commanding.AbstractSequence;
import vega.uplink.commanding.Mib;

/**
 * Sequence overlap checker for Rosetta
 * @author jarenas
 *
 */
public class RosettaOverlapChecker extends AbstractAllowOverlapChecker {
	public  boolean allowOverlap(AbstractSequence seq1,AbstractSequence seq2){

		if (!seq1.getInstrument().equals(seq2.getInstrument())) return true;
		if (seq1.getInstrument().equals("OSIRIS")){
				String camaraID1=getOsirisCamara(seq1);
				String camaraID2=getOsirisCamara(seq2);

				if (camaraID1.equals(camaraID2)){
					return false;
				}else return true;
		}
		if (seq1.getInstrument().equals("RPC")){

			String sub1 = getRPCSub(seq1);
			String sub2 = getRPCSub(seq2);
			if (sub1.equals(sub2)) return false;
			else return true;
			
		}
		return false;
	}
	
	private static String getRPCSub(AbstractSequence seq){
		Mib mib=null;
		try{
			mib=Mib.getMib();
		}catch(Exception e){
			IllegalArgumentException iae = new IllegalArgumentException("Could not get MIB:"+e.getMessage());
			iae.initCause(e);
			throw(iae);
		}
		String description = mib.getSequenceDescription(seq.getName());
		if (description.contains("ICA")) return "ICA";
		if (description.contains("MIP")) return "MIP";
		if (description.contains("MAG")) return "MAG";

		return "null";
		
	}
	
	private static String getOsirisCamara(AbstractSequence seq){
		String result="null";
		try{
			result = seq.getParameter("VSRGT006").getStringValue();
			if (result==null) result="null";
			//return result;
		}catch(Exception e){
			result="null";
		}
		if (result.equals("null")){
			if (seq.getName().equals("ASRS110B") || seq.getName().equals("ASRS988B") || seq.getName().equals("ASRS109B") ||seq.getName().equals("ASRS989B") ) result= "NAC";
			if (seq.getName().equals("ASRS110C") || seq.getName().equals("ASRS988C") || seq.getName().equals("ASRS109C") ||seq.getName().equals("ASRS989C") ) result= "WAC";

		}
		return result;
	}

}
