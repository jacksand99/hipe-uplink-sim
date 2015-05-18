package rosetta.uplink.pointing;

import java.util.HashMap;

import vega.uplink.pointing.net.AngularMomentum;
import vega.uplink.pointing.net.AttitudeConstrainEvent;
import vega.uplink.pointing.net.AttitudeGeneratorException;
import vega.uplink.pointing.net.AttitudeGeneratorFDImpl;
import vega.uplink.pointing.net.HgaOutages;
import vega.uplink.pointing.net.AngularMomentum.AMInterval;
import vega.hipe.gui.xmlutils.HtmlEditorKit;
//import rosetta.uplink.pointing.ExclusionPeriod.Period;
import vega.uplink.DateUtil;
import vega.uplink.EvtmEvent;
import vega.uplink.pointing.AttitudeGenerator;
import vega.uplink.pointing.Pdfm;
import vega.uplink.pointing.PointingBlock;
import vega.uplink.pointing.PointingBlocksSlice;
import vega.uplink.pointing.Ptr;
import vega.uplink.pointing.PtrChecker;
import vega.uplink.pointing.exclusion.AbstractExclusion;
//import vega.uplink.pointing.EvtmEvents.EvtmEventBdi;
import vega.uplink.track.Fecs;
import vega.uplink.track.GsPass;

public class RosettaPtrChecker {
	
	public static HashMap<String,String> getRosettaIlumEventMessages(){
		HashMap<String, String> result = new HashMap<String,String>();
		String ALIS="ALIS:the angle between the Sun direction and the S/C z-axis falls below, or rises above 11 deg.";
		String CSIS="CSIS:Sun enters or leaves a rectangular box around the S/C z-axis. The box is defined by all directions for which its projection into the S/C x/z plane is less than 15 deg away from the S/C z-axis, and its projection into the S/C y/z plane is less than 22 deg away from the S/C z-axis";
		String MRIS="MRIS:the angle between the Sun direction and the S/C z-axis falls below, or rises above 5 deg.";
		String SRHS="SRHS:the angle between the Sun direction and the S/C z-axis falls below, or rises above 11 deg.";
		String SRIS="SRIS:the angle between the Sun direction and the S/C z-axis falls below, or rises above 20 deg. [ID 252]";
		String VRIS="VRIS:the angle between the Sun direction and the S/C z-axis falls below, or rises above 10 deg.";
		String LDIS="LDIS:the angle from the S/C z-axis to the projection of the Sun direction into the S/C x/z plane counted positively towards the S/C x-axis falls below, or rises above 10 deg.";
		String Z02S="Z02S:when the angle from the S/C z-axis to the projection of the Sun direction into the S/C x/z plane counted positively towards the S/C x-axis falls below, or rises above 20 deg. [ID 252]";
		String Z18S="Z18S:when the angle from the S/C z-axis to the projection of the Sun direction into the S/C x/z plane counted positively towards the S/C x-axis rises above, or falls below 180 deg. [ID 253]";
		String Z14S="Z14S:the angle from the S/C z-axis to the projection of the Sun direction into the S/C x/z plane counted positively towards the S/C x-axis exceeds 140 deg. [ID 280] [Condition: @ 1.2 AU - no limit]";
		String Z17S="Z17S:the angle from the S/C z-axis to the projection of the Sun direction into the S/C x/z plane counted positively towards the S/C x-axis exceeds 175 deg. [ID 300] [Condition: @ 2.20 AU - no limit]";
		String Z19S="Z19S:the angle from the S/C z-axis to the projection of the Sun direction into the S/C x/z plane counted positively towards the S/C x-axis exceeds 192 deg. [ID 310] [Condition: @ 2.21 AU - no limit]";
		String YPIS="YPIS:the Sun incidence angle on S/C +y face rises above 5 deg. [ID 350]";
		String YP3S="YP3S:the Sun incidence angle on S/C +y face rises above 30 deg. [ID 370] [Condition: IF > 1.9 AU AND PL ops]";
		String YP4S="YP4S:the Sun incidence angle on S/C +y face rises above 40 deg. [ID 360] [Condition: IF > 1.25 AU AND PL ops]";
		String YMIS="YMIS:the Sun incidence angle on S/C -y face rises above 5 deg. [ID 350]";
		String YM3S="YM3S:the Sun incidence angle on S/C -y face rises above 30 deg. [ID 370] [Condition: IF > 1.9 AU AND PL ops]";
		String YM4S="YM4S:the Sun incidence angle on S/C -y face rises above 40 deg. [ID 360] [Condition: IF > 1.25 AU AND PL ops]";

		String ALIE="End of ALIS";
		String CSIE="End of CSIS";
		String MRIE="End of MRIS";			
		String SRHE="End of SRHS";
		String SRIE="End of SRIS";
		String VRIE="End of VRIS";
		String LDIE="End of LDIS";
		String Z02E="End of Z02S";
		String Z18E="End of Z18S";
		String Z14E="End of Z14S";
		String Z17E="End of Z17S";
		String Z19E="End of Z19S";
		String YPIE="End of YPIS";
		String YP3E="End of YP3S";
		String YP4E="End of YP4S";
		String YMIE="End of YMIS";
		String YM3E="End of YM3S";
		String YM4E="End of YM4S";
		result.put("ALIS", ALIS);
		result.put("CSIS", CSIS);
		result.put("MRIS", MRIS);
		result.put("SRHS", SRHS);
		result.put("SRIS", SRIS);
		result.put("VRIS", VRIS);
		result.put("LDIS", LDIS);
		result.put("Z02S", Z02S);
		result.put("Z18S", Z18S);
		result.put("Z14S", Z14S);
		result.put("Z17S", Z17S);
		result.put("Z19S", Z19S);
		result.put("YPIS", YPIS);
		result.put("YP3S", YP3S);
		result.put("YP4S", YP4S);
		result.put("YMIS", YMIS);
		result.put("YM3S", YM3S);
		result.put("YM4S", YM4S);
		
		result.put("ALIE", ALIE);
		result.put("CSIE", CSIE);
		result.put("MRIE", MRIE);
		result.put("SRHE", SRHE);
		result.put("SRIE", SRIE);
		result.put("VRIE", VRIE);
		result.put("LDIE", LDIE);
		result.put("Z02E", Z02E);
		result.put("Z18E", Z18E);
		result.put("Z14E", Z14E);
		result.put("Z17E", Z17E);
		result.put("Z19E", Z19E);
		result.put("YPIE", YPIE);
		result.put("YP3E", YP3E);
		result.put("YP4E", YP4E);
		result.put("YMIE", YMIE);
		result.put("YM3E", YM3E);
		result.put("YM4E", YM4E);

		return result;

	}
	/*public static String checkPtrHtmlMini(Ptr ptr,Pdfm pdfm,AttitudeGeneratorFDImplMini ag){
		//if (ep==null) ep=new ExclusionPeriod();
		HashMap<String, String> evtmDic = getRosettaIlumEventMessages();
		String result="";
		//if (ptsl!=null) result=result+PtrChecker.checkPtrHTML(ptr, ptsl);
		result=result+PtrChecker.checkPtrHTML(ptr);
		String PTR_TABLE_HEADER=""
				+ "<tr>\n"
				+ "	<th>Blocks</th><th>Error</th>\n"
				+ "</tr>\n";
		result=result+"<table class=\"gridtable\">"+PTR_TABLE_HEADER;

			AttitudeConstrainEvent[] attEvents = ag.getAttEvents();
			if (attEvents.length>0){
				//result=result+"Attitude:\n";
				for (int i=0;i<attEvents.length;i++){
					result=result+"<tr><td><pre>"+HtmlEditorKit.escapeHTML(ptr.getSegments()[0].getBlockAt(attEvents[i].start).toXml(0))+"</pre></td><td>"+attEvents[i]+"</td></tr>";

					//result=result+attEvents[i]+"\n";
				}
			}
			vega.uplink.pointing.EvtmEvent[] illuminationEv = ag.getEvtm().getEventsByType(EvtmEvent.EVENT_TYPE_BDI);
			if (illuminationEv.length>0){
				for (int i=0;i<illuminationEv.length;i++){
					result=result+"<tr><td><pre>"+HtmlEditorKit.escapeHTML(ptr.getSegments()[0].getBlockAt(illuminationEv[i].getTime()).toXml(0))+"</pre></td><td>"+evtmDic.get(illuminationEv[i].getId())+"</td></tr>";

				}
			}
		return result;
		
	}*/
	
	public static String checkPtrHTML(Ptr ptr,Ptr ptsl,Pdfm pdfm,AttitudeGeneratorFDImpl ag){
		return checkPtrHTML(ptr,ptsl,pdfm,ag,null);
	}
	public static String checkPtrHTML(Ptr ptr,Ptr ptsl,Pdfm pdfm,AttitudeGeneratorFDImpl ag,AbstractExclusion ep){
		if (ep==null) ep=new ExclusionPeriod();
		HashMap<String, String> evtmDic = getRosettaIlumEventMessages();
		String result="";
		if (ptsl!=null) result=result+PtrChecker.checkPtrHTML(ptr, ptsl);
		String PTR_TABLE_HEADER=""
				+ "<tr>\n"
				+ "	<th>Blocks</th><th>Error</th>\n"
				+ "</tr>\n";
		result=result+"<table class=\"gridtable\">"+PTR_TABLE_HEADER;

			AttitudeConstrainEvent[] attEvents = ag.getAttEvents();
			if (attEvents.length>0){
				//result=result+"Attitude:\n";
				for (int i=0;i<attEvents.length;i++){
					result=result+"<tr><td><pre>"+HtmlEditorKit.escapeHTML(ptr.getSegments()[0].getBlockAt(attEvents[i].start).toXml(0))+"</pre></td><td>"+attEvents[i]+"</td></tr>";

					//result=result+attEvents[i]+"\n";
				}
			}
			AngularMomentum agmom = ag.getAngularMomentum();
			result=result+agmom+"\n";
			HgaOutages[] hga = ag.getHgaOutages();
			if (hga.length>0){
				//result=result+"HGA coverage:\n";
				for (int i=0;i<hga.length;i++){
					result=result+"<tr><td><pre>"+HtmlEditorKit.escapeHTML(ptr.getSegments()[0].getBlocksAt(hga[i].getStartDate(),hga[i].getEndDate()).toXml(0))+"</pre></td><td>"+hga[i].toString();
					vega.uplink.pointing.exclusion.Period[] ex = ep.findOverlapingPeriods(hga[i].getStartDate(), hga[i].getEndDate());
					//if (ex.length>0){
					for (int p=0;p<ex.length;p++){
						result=result+"<font color=RED>In exclusion period:"+ex[p]+"</font><br>";
					}
					result=result+"</td></tr>";
					
					//result=result+hga[i].toString()+"\n";
				}
			}
			vega.uplink.pointing.EvtmEvent[] illuminationEv = ag.getEvtm().getEventsByType(EvtmEvent.EVENT_TYPE_BDI);
			if (illuminationEv.length>0){
				//result=result+"Illumination events:\n";
				for (int i=0;i<illuminationEv.length;i++){
					result=result+"<tr><td><pre>"+HtmlEditorKit.escapeHTML(ptr.getSegments()[0].getBlockAt(illuminationEv[i].getTime()).toXml(0))+"</pre></td><td>"+evtmDic.get(illuminationEv[i].getId())+"</td></tr>";

				}
			}
		return result;

	}

	public static String checkPtr(Ptr ptr,Ptr ptsl,Pdfm pdfm,AttitudeGeneratorFDImpl ag){
		HashMap<String, String> evtmDic = getRosettaIlumEventMessages();
		String result="";
		result=result+PtrChecker.checkPtr(ptr, ptsl);
			AttitudeConstrainEvent[] attEvents = ag.getAttEvents();
			if (attEvents.length>0){
				result=result+"Attitude:\n";
				for (int i=0;i<attEvents.length;i++){
					result=result+attEvents[i]+"\n";
				}
			}
			AngularMomentum agmom = ag.getAngularMomentum();
			result=result+agmom+"\n";
			HgaOutages[] hga = ag.getHgaOutages();
			if (hga.length>0){
				result=result+"HGA coverage:\n";
				for (int i=0;i<hga.length;i++){
					result=result+hga[i].toString()+"\n";
				}
			}
			vega.uplink.pointing.EvtmEvent[] illuminationEv = ag.getEvtm().getEventsByType(EvtmEvent.EVENT_TYPE_BDI);
			if (illuminationEv.length>0){
				result=result+"Illumination events:\n";
				for (int i=0;i<illuminationEv.length;i++){
					result=result+DateUtil.dateToZulu(illuminationEv[i].getTime())+" "+evtmDic.get(illuminationEv[i].getId())+"\n";
				}
			}
		return result;

	}
	public static String checkPtrHTML(Ptr ptr,Ptr ptsl,Pdfm pdfm){
		String result="";
		try {
			AttitudeGeneratorFDImpl ag = new RosettaAttitudeGenerator(ptr,pdfm);
			result=result+checkPtrHTML(ptr,ptsl,pdfm,ag);
		} catch (AttitudeGeneratorException e) {
			// TODO Auto-generated catch block
			result=result+e.getMessage()+"\n";
			e.printStackTrace();
		}
		return result;
	}

	public static String checkPtr(Ptr ptr,Ptr ptsl,Pdfm pdfm){
		String result="";
		try {
			AttitudeGeneratorFDImpl ag = new RosettaAttitudeGenerator(ptr,pdfm);
			result=result+checkPtr(ptr,ptsl,pdfm,ag);
		} catch (AttitudeGeneratorException e) {
			// TODO Auto-generated catch block
			result=result+e.getMessage()+"\n";
			e.printStackTrace();
		}
		return result;
	}
	
	public static String passToString(GsPass pass){
		String result="";
		result=result+"Pass "+pass.getGroundStation()+" from "+DateUtil.dateToZulu(pass.getStartPass())+" to "+DateUtil.dateToZulu(pass.getEndPass())+"\n";
		result=result+"\tDump Start at:"+DateUtil.dateToZulu(pass.getStartDump())+"\n";
		result=result+"\tDump End at:"+DateUtil.dateToZulu(pass.getEndDump())+"\n";
		
		return result;
	}
	public static String checkPtrHTML(Ptr ptr,Ptr ptsl,Pdfm pdfm,Fecs fecs,AttitudeGeneratorFDImpl ag){
		return checkPtrHTML(ptr,ptsl,pdfm,fecs,ag,null);
	}
	public static String checkPtrHTML(Ptr ptr,Ptr ptsl,Pdfm pdfm,Fecs fecs,AttitudeGeneratorFDImpl ag,AbstractExclusion ep){
		if (ep==null) ep=new ExclusionPeriod();
		HashMap<String, String> evtmDic = getRosettaIlumEventMessages();
		String result="";
		result=result+PtrChecker.checkPtrHTML(ptr, ptsl);
		String PTR_TABLE_HEADER=""
				+ "<tr>\n"
				+ "	<th>Blocks</th><th>Error</th>\n"
				+ "</tr>\n";
		result=result+"<table class=\"gridtable\">"+PTR_TABLE_HEADER;

			AttitudeConstrainEvent[] attEvents = ag.getAttEvents();
			if (attEvents.length>0){
				//result=result+"**************************\n";
				//result=result+"Attitude:\n";
				//result=result+"**************************\n";
				for (int i=0;i<attEvents.length;i++){
					PointingBlocksSlice blocks = ptr.getSegments()[0].getBlocksAt(attEvents[i].start,attEvents[i].end);
					if (blocks.getBlocks().length==0){
						PointingBlock actualblock = ptr.getSegments()[0].getBlockAt(attEvents[i].start);
						blocks.addBlock(ptr.getSegments()[0].blockBefore(actualblock));
						blocks.addBlock(actualblock);
						blocks.addBlock(ptr.getSegments()[0].blockAfter(actualblock));
					}
						result=result+"<tr><td><pre>"+HtmlEditorKit.escapeHTML(blocks.toXml(0))+"</pre></td><td>"+attEvents[i]+"</td></tr>";
						
				}
			}
			vega.uplink.pointing.EvtmEvent[] illuminationEv = ag.getEvtm().getEventsByType(EvtmEvent.EVENT_TYPE_BDI);
			if (illuminationEv.length>0){
				//result=result+"**************************\n";
				//result=result+"Illumination events:\n";
				//result=result+"**************************\n";

				for (int i=0;i<illuminationEv.length;i++){
					result=result+"<tr><td><pre>"+HtmlEditorKit.escapeHTML(ptr.getSegments()[0].getBlockAt(illuminationEv[i].getTime()).toXml(0))+"</pre></td><td>"+DateUtil.dateToZulu(illuminationEv[i].getTime())+" "+evtmDic.get(illuminationEv[i].getId())+"</td></tr>";

				}
			}
			//result=result+"</table>";
			AngularMomentum agmom = ag.getAngularMomentum();
			AMInterval[] agIntervals = agmom.getIntervals();
			for (int i=0; i<agIntervals.length;i++){
				result=result+"<tr><td><pre>"+HtmlEditorKit.escapeHTML(ptr.getSegments()[0].getBlocksAt(agIntervals[i].getStartTime(),agIntervals[i].getEndTime()).toXml(0))+"</pre></td><td>Angular Momentum:"+agIntervals[i].toString()+"</td></tr>";
				
			}
			result=result+"</table>";
			//result=result+agmom+"\n";
			HgaOutages[] hga = ag.getHgaOutages();
			if (hga.length>0){
				//result=result+"**************************\n";
				result=result+"<h2>HGA coverage</h2>";
				//result=result+"**************************\n";
				for (int i=0;i<hga.length;i++){
					vega.uplink.pointing.exclusion.Period[] ex = ep.findOverlapingPeriods(hga[i].getStartDate(), hga[i].getEndDate());
					//if (ex.length>0){
					for (int p=0;p<ex.length;p++){
						hga[i].addNote("In exclusion period:"+ex[p]);
						//result=result+"<font color=RED>In exclusion period:"+ex[p]+"</font><br>";
					}
					hga[i].setBlocks("<pre>"+HtmlEditorKit.escapeHTML(ptr.getSegments()[0].getBlocksAt(hga[i].getStartDate(),hga[i].getEndDate()).toXml(0))+"</pre>");

					result=result+hga[i].toHTML()+"<br>";

					GsPass[] affected = fecs.findOverlapingPasses(hga[i].getStartDate(), hga[i].getEndDate());
					result=result+"Affected GS passes:<br>";
					String PASS_TABLE_HEADER=""
							+ "<tr>\n"
							+ "	<th>Type</th><th>Station</th><th>Start Pass</th><th>End Pass</th><th>Start Dump</th><th>End Dump</th><th>Bitrate</th>\n"
							+ "</tr>\n";
					if (affected.length>0) result=result+"<table class=\"gridtable\">"+PASS_TABLE_HEADER;
					for (int j=0;j<affected.length;j++){
						result=result+Fecs.passToHTMLRow(affected[j]);
						//result=result+"--------------------------------------\n";

					}
					if (affected.length>0) result=result+"</table>";
					

				}
			}
			PointingBlock[] mocmBlocks = ptr.getSegments()[0].getAllBlocksOfType(PointingBlock.TYPE_MOCM).getBlocks();
			for (int m=0;m<mocmBlocks.length;m++){
				PointingBlock mocm = mocmBlocks[m];
				vega.uplink.pointing.exclusion.Period[] ex = ep.findOverlapingPeriods(mocm.getStartTime(), mocm.getEndTime());
				if (ex.length>0){
					HgaOutages hgaMocm = new HgaOutages(mocm.getStartTime(),mocm.getEndTime(),mocm.getStartTime(),"","MOCM",0);
					hgaMocm.setBlocks("<pre>"+HtmlEditorKit.escapeHTML(mocm.toXml(0))+"</pre>");
					for (int p=0;p<ex.length;p++){
						hgaMocm.addNote("In exclusion period:"+ex[p]);
						//result=result+"<font color=RED>In exclusion period:"+ex[p]+"</font><br>";
					}
					result=result+hgaMocm.toHTML()+"<br>";
					GsPass[] affected = fecs.findOverlapingPasses(hgaMocm.getStartDate(), hgaMocm.getEndDate());
					result=result+"Affected GS passes:<br>";
					String PASS_TABLE_HEADER=""
							+ "<tr>\n"
							+ "	<th>Type</th><th>Station</th><th>Start Pass</th><th>End Pass</th><th>Start Dump</th><th>End Dump</th><th>Bitrate</th>\n"
							+ "</tr>\n";
					if (affected.length>0) result=result+"<table class=\"gridtable\">"+PASS_TABLE_HEADER;
					for (int j=0;j<affected.length;j++){
						result=result+Fecs.passToHTMLRow(affected[j]);
						//result=result+"--------------------------------------\n";

					}
					if (affected.length>0) result=result+"</table>";
				}

			}
			PointingBlock[] mwolBlocks = ptr.getSegments()[0].getAllBlocksOfType(PointingBlock.TYPE_MWOL).getBlocks();
			for (int m=0;m<mwolBlocks.length;m++){
				PointingBlock mwol = mwolBlocks[m];
				vega.uplink.pointing.exclusion.Period[] ex = ep.findOverlapingPeriods(mwol.getStartTime(), mwol.getEndTime());
				if (ex.length>0){
					HgaOutages hgaMwol = new HgaOutages(mwol.getStartTime(),mwol.getEndTime(),mwol.getStartTime(),"","MWOL",0);
					hgaMwol.setBlocks("<pre>"+HtmlEditorKit.escapeHTML(mwol.toXml(0))+"</pre>");
					for (int p=0;p<ex.length;p++){
						hgaMwol.addNote("In exclusion period:"+ex[p]);
						//result=result+"<font color=RED>In exclusion period:"+ex[p]+"</font><br>";
					}
					result=result+hgaMwol.toHTML()+"<br>";
					GsPass[] affected = fecs.findOverlapingPasses(hgaMwol.getStartDate(), hgaMwol.getEndDate());
					result=result+"Affected GS passes:<br>";
					String PASS_TABLE_HEADER=""
							+ "<tr>\n"
							+ "	<th>Type</th><th>Station</th><th>Start Pass</th><th>End Pass</th><th>Start Dump</th><th>End Dump</th><th>Bitrate</th>\n"
							+ "</tr>\n";
					if (affected.length>0) result=result+"<table class=\"gridtable\">"+PASS_TABLE_HEADER;
					for (int j=0;j<affected.length;j++){
						result=result+Fecs.passToHTMLRow(affected[j]);
						//result=result+"--------------------------------------\n";

					}
					if (affected.length>0) result=result+"</table>";
				}

			}


		return result;
	
	}

	public static String checkPtr(Ptr ptr,Ptr ptsl,Pdfm pdfm,Fecs fecs,AttitudeGeneratorFDImpl ag){
		HashMap<String, String> evtmDic = getRosettaIlumEventMessages();
		String result="";
		result=result+PtrChecker.checkPtr(ptr, ptsl);
			AttitudeConstrainEvent[] attEvents = ag.getAttEvents();
			if (attEvents.length>0){
				result=result+"**************************\n";
				result=result+"Attitude:\n";
				result=result+"**************************\n";
				for (int i=0;i<attEvents.length;i++){
					result=result+attEvents[i]+"\n";
					result=result+ptr.getSegments()[0].getBlockAt(attEvents[i].start).toXml(0)+"\n";
				}
			}
			AngularMomentum agmom = ag.getAngularMomentum();
			result=result+agmom+"\n";
			HgaOutages[] hga = ag.getHgaOutages();
			if (hga.length>0){
				result=result+"**************************\n";
				result=result+"HGA coverage:\n";
				result=result+"**************************\n";
				for (int i=0;i<hga.length;i++){
					result=result+hga[i].toString()+"\n";
					GsPass[] affected = fecs.findOverlapingPasses(hga[i].getStartDate(), hga[i].getEndDate());
					for (int j=0;j<affected.length;j++){
						result=result+"HGA not available during pass:\n";
						result=result+passToString(affected[j])+"\n";
						result=result+"--------------------------------------\n";

					}
					

				}
			}
			vega.uplink.pointing.EvtmEvent[] illuminationEv = ag.getEvtm().getEventsByType(EvtmEvent.EVENT_TYPE_BDI);
			if (illuminationEv.length>0){
				result=result+"**************************\n";
				result=result+"Illumination events:\n";
				result=result+"**************************\n";

				for (int i=0;i<illuminationEv.length;i++){
					result=result+DateUtil.dateToZulu(illuminationEv[i].getTime())+" "+evtmDic.get(illuminationEv[i].getId())+"\n";
					result=result+ptr.getSegments()[0].getBlockAt(illuminationEv[i].getTime()).toXml(0)+"\n";
				}
			}
		return result;
	
	}
	public static String checkPtrHTML(Ptr ptr,Ptr ptsl,Pdfm pdfm,Fecs fecs){
		//HashMap<String, String> evtmDic = getRosettaIlumEventMessages();
		String result="";
		result=result+PtrChecker.checkPtrHTML(ptr, ptsl);
		try {
			AttitudeGeneratorFDImpl ag = new RosettaAttitudeGenerator(ptr,pdfm);
			result=result+checkPtrHTML(ptr,ptsl,pdfm,fecs,ag);
		} catch (AttitudeGeneratorException e) {
			// TODO Auto-generated catch block
			result=result+e.getMessage()+"\n";
			e.printStackTrace();
		}
		return result;
	}
	public static String checkPtrHTML(Ptr ptr,Ptr ptsl,Pdfm pdfm,Fecs fecs,AbstractExclusion exclusion){
		//HashMap<String, String> evtmDic = getRosettaIlumEventMessages();
		String result="";
		result=result+PtrChecker.checkPtrHTML(ptr, ptsl);
		try {
			AttitudeGeneratorFDImpl ag = new RosettaAttitudeGenerator(ptr,pdfm);
			result=result+checkPtrHTML(ptr,ptsl,pdfm,fecs,ag,exclusion);
		} catch (AttitudeGeneratorException e) {
			// TODO Auto-generated catch block
			result=result+e.getMessage()+"\n";
			e.printStackTrace();
		}
		return result;
	}

	public static String checkPtr(Ptr ptr,Ptr ptsl,Pdfm pdfm,Fecs fecs){
		//HashMap<String, String> evtmDic = getRosettaIlumEventMessages();
		String result="";
		result=result+PtrChecker.checkPtr(ptr, ptsl);
		try {
			AttitudeGeneratorFDImpl ag = new RosettaAttitudeGenerator(ptr,pdfm);
			result=result+checkPtr(ptr,ptsl,pdfm,fecs,ag);
		} catch (AttitudeGeneratorException e) {
			// TODO Auto-generated catch block
			result=result+e.getMessage()+"\n";
			e.printStackTrace();
		}
		return result;
	}


}
