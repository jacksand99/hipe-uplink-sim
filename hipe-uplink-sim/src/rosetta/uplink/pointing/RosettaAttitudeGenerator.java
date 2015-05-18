package rosetta.uplink.pointing;

import vega.uplink.pointing.Evtm;
import vega.uplink.pointing.Pdfm;
import vega.uplink.pointing.Ptr;
import vega.uplink.pointing.SolarAspectAngle;
import vega.uplink.pointing.EvtmEvents.EvtmEventBdi;
import vega.uplink.pointing.net.AttitudeGeneratorException;
import vega.uplink.pointing.net.AttitudeGeneratorFDImpl;
import vega.uplink.pointing.net.ErrorBoxPoint;
import vega.uplink.track.Fecs;

public class RosettaAttitudeGenerator extends AttitudeGeneratorFDImpl{
	public RosettaAttitudeGenerator(Ptr ptr,Pdfm pdfm,ErrorBoxPoint point) throws AttitudeGeneratorException{
		super( ptr, pdfm, point);
	}
	public RosettaAttitudeGenerator(Ptr ptr,Pdfm pdfm,Float eta,Float zeta,Float epsilon) throws AttitudeGeneratorException{
		super( ptr, pdfm, eta, zeta, epsilon);
	}
	public RosettaAttitudeGenerator(Ptr ptr,Pdfm pdfm) throws AttitudeGeneratorException{
			//this(ptr,pdfm,getMtpNum(ptr));
		super(ptr,pdfm);
	}
	public RosettaAttitudeGenerator(Ptr ptr,Pdfm pdfm,String mtpNum,ErrorBoxPoint point) throws AttitudeGeneratorException{
		super( ptr, pdfm, mtpNum, point);
	}
	public RosettaAttitudeGenerator(Ptr ptr,Pdfm pdfm,String mtpNum,Float eta, Float zeta,Float epsilon) throws AttitudeGeneratorException{
		super( ptr, pdfm, mtpNum, eta,  zeta, epsilon);
	}

	public RosettaAttitudeGenerator(Ptr ptr,Pdfm pdfm,String mtpNum) throws AttitudeGeneratorException{
		super( ptr, pdfm, mtpNum);
	}
	public RosettaAttitudeGenerator(Ptr ptr,Pdfm pdfm,String mtpNum,String activityCase,ErrorBoxPoint point) throws AttitudeGeneratorException{
		super( ptr, pdfm, mtpNum, activityCase, point);
	}
	public RosettaAttitudeGenerator(Ptr ptr,Pdfm pdfm,String mtpNum,String activityCase,Float eta,Float zeta,Float epsilon) throws AttitudeGeneratorException{
		super( ptr, pdfm, mtpNum, activityCase, eta, zeta, epsilon);
	}

	public RosettaAttitudeGenerator(Ptr ptr,Pdfm pdfm,String mtpNum,String activityCase) throws AttitudeGeneratorException{
		super( ptr, pdfm, mtpNum, activityCase);
	}
	
	public Evtm getEvtm(){
		RosettaDistance distance=RosettaDistance.getInstance();
		Evtm result=new Evtm();
		EvtmEventBdi ALIS=null;
		EvtmEventBdi CSIS=null;
		EvtmEventBdi MRIS=null;
		EvtmEventBdi SRHS=null;
		EvtmEventBdi SRIS=null;
		EvtmEventBdi VRIS=null;
		EvtmEventBdi LDIS=null;
		EvtmEventBdi Z02S=null;
		EvtmEventBdi Z18S=null;
		EvtmEventBdi Z14S=null;
		EvtmEventBdi Z17S=null;
		EvtmEventBdi Z19S=null;
		EvtmEventBdi YPIS=null;
		EvtmEventBdi YP3S=null;
		EvtmEventBdi YP4S=null;
		EvtmEventBdi YMIS=null;
		EvtmEventBdi YM3S=null;
		EvtmEventBdi YM4S=null;
		SolarAspectAngle[] saaArray = getAllSaa();
		if (saaArray.length==0) return result;
		result.setValidityStart(saaArray[0].getTime());
		result.setValidityEnd(saaArray[saaArray.length-1].getTime());
		for (int i=0;i<saaArray.length;i++){
			if (saaArray[i].getSCAnglePositiveZAxis()<11){
				if (ALIS==null) ALIS=new EvtmEventBdi( "ALIS", saaArray[i].getTime(), 0,0);
				if (SRHS==null) SRHS=new EvtmEventBdi( "SRHS", saaArray[i].getTime(), 0,0);				
			}
			if (saaArray[i].getSCAnglePositiveZAxis()>11){
				if (ALIS!=null){
					EvtmEventBdi ALIE=new EvtmEventBdi( "ALIE", saaArray[i].getTime(), 0,0);
					long duration=(ALIE.getTime().getTime()-ALIS.getTime().getTime())/1000;
					result.addEvent(new EvtmEventBdi("ALIS",ALIS.getTime(),duration,0));
					result.addEvent(ALIE);
					ALIS=null;
				}
				if (SRHS!=null){
					EvtmEventBdi SRHE=new EvtmEventBdi( "SRHE", saaArray[i].getTime(), 0,0);
					long duration=(SRHE.getTime().getTime()-SRHS.getTime().getTime())/1000;
					result.addEvent(new EvtmEventBdi("SRHS",SRHS.getTime(),duration,0));
					result.addEvent(SRHE);
					SRHS=null;
				}
			}
			
			if (saaArray[i].getSCAnglePositiveZAxis()<5){
				if (MRIS==null) MRIS=new EvtmEventBdi( "MRIS", saaArray[i].getTime(), 0,0);
			}
			if (saaArray[i].getSCAnglePositiveZAxis()>5){
				if (MRIS!=null){
					EvtmEventBdi MRIE=new EvtmEventBdi( "MRIE", saaArray[i].getTime(), 0,0);
					long duration=(MRIE.getTime().getTime()-MRIS.getTime().getTime())/1000;
					result.addEvent(new EvtmEventBdi("MRIS",MRIS.getTime(),duration,0));
					result.addEvent(MRIE);
					MRIS=null;

				}
			}
			if (saaArray[i].getSCAnglePositiveZAxis()<20){
				if (SRIS==null) SRIS=new EvtmEventBdi( "SRIS", saaArray[i].getTime(), 0,0);
				if (Z02S==null) Z02S=new EvtmEventBdi( "Z02S", saaArray[i].getTime(), 0,0);				
			}
			if (saaArray[i].getSCAnglePositiveZAxis()>20){
				if (SRIS!=null){
					EvtmEventBdi SRIE=new EvtmEventBdi( "SRIE", saaArray[i].getTime(), 0,0);
					long duration=(SRIE.getTime().getTime()-SRIS.getTime().getTime())/1000;
					result.addEvent(new EvtmEventBdi("SRIS",SRIS.getTime(),duration,0));
					result.addEvent(SRIE);
					SRIS=null;
				}
				if (Z02S!=null){
					EvtmEventBdi Z02E=new EvtmEventBdi( "Z02E", saaArray[i].getTime(), 0,0);
					long duration=(Z02E.getTime().getTime()-Z02S.getTime().getTime())/1000;
					result.addEvent(new EvtmEventBdi("Z02S",Z02S.getTime(),duration,0));
					result.addEvent(Z02E);
					Z02S=null;
				}
			}
			if (saaArray[i].getSCAnglePositiveZAxis()<10){
				if (VRIS==null) VRIS=new EvtmEventBdi( "VRIS", saaArray[i].getTime(), 0,0);
				if (LDIS==null) LDIS=new EvtmEventBdi( "LDIS", saaArray[i].getTime(), 0,0);				
			}
			if (saaArray[i].getSCAnglePositiveZAxis()>10){
				if (VRIS!=null){
					EvtmEventBdi VRIE=new EvtmEventBdi( "VRIE", saaArray[i].getTime(), 0,0);
					long duration=(VRIE.getTime().getTime()-VRIS.getTime().getTime())/1000;
					result.addEvent(new EvtmEventBdi("VRIS",VRIS.getTime(),duration,0));
					result.addEvent(VRIE);
					VRIS=null;
				}
				if (LDIS!=null){
					EvtmEventBdi LDIE=new EvtmEventBdi( "LDIE", saaArray[i].getTime(), 0,0);
					long duration=(LDIE.getTime().getTime()-LDIS.getTime().getTime())/1000;
					result.addEvent(new EvtmEventBdi("LDIS",LDIS.getTime(),duration,0));
					result.addEvent(LDIE);
					LDIS=null;
				}
			}
			if (saaArray[i].getSCAnglePositiveZAxis()>180){
				if (Z18S==null) Z18S=new EvtmEventBdi( "Z18S", saaArray[i].getTime(), 0,0);
			}
			if (saaArray[i].getSCAnglePositiveZAxis()<180){
				if (Z18S!=null){
					EvtmEventBdi Z18E=new EvtmEventBdi( "Z18E", saaArray[i].getTime(), 0,0);
					long duration=(Z18E.getTime().getTime()-Z18S.getTime().getTime())/1000;
					result.addEvent(new EvtmEventBdi("Z18S",Z18S.getTime(),duration,0));
					result.addEvent(Z18E);
					Z18S=null;
				}
			}

			if (saaArray[i].getSCAnglePositiveZAxis()>140 && distance.getDistanceSun(saaArray[i].getTime())<1.20){
				if (Z14S==null) Z14S=new EvtmEventBdi( "Z14S", saaArray[i].getTime(), 0,0);
			}
			if (saaArray[i].getSCAnglePositiveZAxis()<140 && distance.getDistanceSun(saaArray[i].getTime())<1.20){
				if (Z14S!=null){
					EvtmEventBdi Z14E=new EvtmEventBdi( "Z14E", saaArray[i].getTime(), 0,0);
					long duration=(Z14E.getTime().getTime()-Z14S.getTime().getTime())/1000;
					result.addEvent(new EvtmEventBdi("Z14S",Z14S.getTime(),duration,0));
					result.addEvent(Z14E);
					Z14S=null;
				}
			}

			if (saaArray[i].getSCAnglePositiveZAxis()>175 && distance.getDistanceSun(saaArray[i].getTime())<2.20 ){
				if (Z17S==null) Z17S=new EvtmEventBdi( "Z17S", saaArray[i].getTime(), 0,0);
			}
			if (saaArray[i].getSCAnglePositiveZAxis()<175 && distance.getDistanceSun(saaArray[i].getTime())<2.20){
				if (Z17S!=null){
					EvtmEventBdi Z17E=new EvtmEventBdi( "Z17E", saaArray[i].getTime(), 0,0);
					long duration=(Z17E.getTime().getTime()-Z17S.getTime().getTime())/1000;
					result.addEvent(new EvtmEventBdi("Z17S",Z17S.getTime(),duration,0));
					result.addEvent(Z17E);
					Z17S=null;
				}
			}

			if (saaArray[i].getSCAnglePositiveZAxis()>192 && distance.getDistanceSun(saaArray[i].getTime())<2.21){
				if (Z19S==null) Z19S=new EvtmEventBdi( "Z19S", saaArray[i].getTime(), 0,0);
			}
			if (saaArray[i].getSCAnglePositiveZAxis()<192 && distance.getDistanceSun(saaArray[i].getTime())<2.21){
				if (Z19S!=null){
					EvtmEventBdi Z19E=new EvtmEventBdi( "Z19E", saaArray[i].getTime(), 0,0);
					long duration=(Z19E.getTime().getTime()-Z19S.getTime().getTime())/1000;
					result.addEvent(new EvtmEventBdi("Z19S",Z19S.getTime(),duration,0));
					result.addEvent(Z19E);
					Z19S=null;
				}
			}

			if (saaArray[i].getSCAnglePositiveYAxis()>5){
				if (YPIS==null) YPIS=new EvtmEventBdi( "YPIS", saaArray[i].getTime(), 0,0);
			}
			if (saaArray[i].getSCAnglePositiveYAxis()<5){
				if (YPIS!=null){
					EvtmEventBdi YPIE=new EvtmEventBdi( "YPIE", saaArray[i].getTime(), 0,0);
					long duration=(YPIE.getTime().getTime()-YPIS.getTime().getTime())/1000;
					result.addEvent(new EvtmEventBdi("YPIS",YPIS.getTime(),duration,0));
					result.addEvent(YPIE);
					YPIS=null;
				}
			}

			if (saaArray[i].getSCAnglePositiveYAxis()>30){
				if (YP3S==null) YP3S=new EvtmEventBdi( "YP3S", saaArray[i].getTime(), 0,0);
			}
			if (saaArray[i].getSCAnglePositiveYAxis()<30){
				if (YP3S!=null){
					EvtmEventBdi YP3E=new EvtmEventBdi( "YP3E", saaArray[i].getTime(), 0,0);
					long duration=(YP3E.getTime().getTime()-YP3S.getTime().getTime())/1000;
					result.addEvent(new EvtmEventBdi("YP3S",YP3S.getTime(),duration,0));
					result.addEvent(YP3E);
					YP3S=null;
				}
			}

			if (saaArray[i].getSCAnglePositiveYAxis()>40){
				if (YP4S==null) YP4S=new EvtmEventBdi( "YP4S", saaArray[i].getTime(), 0,0);
			}
			if (saaArray[i].getSCAnglePositiveYAxis()<40){
				if (YP4S!=null){
					EvtmEventBdi YP4E=new EvtmEventBdi( "YP4E", saaArray[i].getTime(), 0,0);
					long duration=(YP4E.getTime().getTime()-YP4S.getTime().getTime())/1000;
					result.addEvent(new EvtmEventBdi("YP4S",YP4S.getTime(),duration,0));
					result.addEvent(YP4E);
					YP4S=null;
				}
			}
			
			if (saaArray[i].getSCAngleNegativeYAxis()>5){
				if (YMIS==null) YMIS=new EvtmEventBdi( "YMIS", saaArray[i].getTime(), 0,0);
			}
			if (saaArray[i].getSCAngleNegativeYAxis()<5){
				if (YMIS!=null){
					EvtmEventBdi YMIE=new EvtmEventBdi( "YMIE", saaArray[i].getTime(), 0,0);
					long duration=(YMIE.getTime().getTime()-YMIS.getTime().getTime())/1000;
					result.addEvent(new EvtmEventBdi("YMIS",YPIS.getTime(),duration,0));
					result.addEvent(YMIE);
					YMIS=null;
				}
			}

			if (saaArray[i].getSCAngleNegativeYAxis()>30){
				if (YM3S==null) YM3S=new EvtmEventBdi( "YM3S", saaArray[i].getTime(), 0,0);
			}
			if (saaArray[i].getSCAngleNegativeYAxis()<30){
				if (YP3S!=null){
					EvtmEventBdi YM3E=new EvtmEventBdi( "YM3E", saaArray[i].getTime(), 0,0);
					long duration=0;
					if (YM3E!=null) duration=(YM3E.getTime().getTime()-YM3S.getTime().getTime())/1000;					
					result.addEvent(new EvtmEventBdi("YM3S",YM3S.getTime(),duration,0));
					result.addEvent(YM3E);
					YM3S=null;
				}
			}

			if (saaArray[i].getSCAngleNegativeYAxis()>40){
				if (YM4S==null) YM4S=new EvtmEventBdi( "YM4S", saaArray[i].getTime(), 0,0);
			}
			if (saaArray[i].getSCAngleNegativeYAxis()<40){
				if (YM4S!=null){
					EvtmEventBdi YM4E=new EvtmEventBdi( "YM4E", saaArray[i].getTime(), 0,0);
					long duration=(YM4E.getTime().getTime()-YM4S.getTime().getTime())/1000;
					result.addEvent(new EvtmEventBdi("YM4S",YM4S.getTime(),duration,0));
					result.addEvent(YM4E);
					YM4S=null;
				}
			}

			
		}
		
		return result;
	}
	
	@Override
	public String checkPtr(Ptr ptr, Ptr ptsl, Pdfm pdfm) {
		// TODO Auto-generated method stub
		return RosettaPtrChecker.checkPtr(ptr, ptsl, pdfm, this);
	}
	@Override
	public String checkPtr(Ptr ptr, Ptr ptsl, Pdfm pdfm, Fecs fecs) {
		// TODO Auto-generated method stub
		return RosettaPtrChecker.checkPtr(ptr, ptsl, pdfm, fecs, this);
	}
	@Override
	public String checkPtrHTML(Ptr ptr, Ptr ptsl, Pdfm pdfm) {
		// TODO Auto-generated method stub
		return RosettaPtrChecker.checkPtrHTML(ptr, ptsl, pdfm,this);
	}
	@Override
	public String checkPtrHTML(Ptr ptr, Ptr ptsl, Pdfm pdfm, Fecs fecs) {
		// TODO Auto-generated method stub
		return RosettaPtrChecker.checkPtrHTML(ptr, ptsl, pdfm,fecs,this);
	}


}
