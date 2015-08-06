package rosetta.uplink.pointing;

import herschel.share.fltdyn.math.Quaternion;
import herschel.share.interpreter.InterpreterUtil;

import java.util.Date;
import java.util.HashMap;

import org.python.modules.math;

import vega.uplink.pointing.net.AngularMomentum;
import vega.uplink.pointing.net.AttitudeConstrainEvent;
import vega.uplink.pointing.net.AttitudeGeneratorException;
import vega.uplink.pointing.net.AttitudeGeneratorFDImpl;
import vega.uplink.pointing.net.HgaOutages;
import vega.uplink.pointing.net.AngularMomentum.AMInterval;
import vega.hipe.gui.xmlutils.HtmlDocument;
import vega.hipe.gui.xmlutils.HtmlEditorKit;
import vega.uplink.DateUtil;
import vega.uplink.EvtmEvent;
import vega.uplink.pointing.AttitudeMap;
import vega.uplink.pointing.AttitudeUtils;
import vega.uplink.pointing.Pdfm;
import vega.uplink.pointing.PointingAttitude;
import vega.uplink.pointing.PointingBlock;
import vega.uplink.pointing.PointingBlocksSlice;
import vega.uplink.pointing.PointingElement;
import vega.uplink.pointing.Ptr;
import vega.uplink.pointing.PtrChecker;
import vega.uplink.pointing.PtrSegment;
import vega.uplink.pointing.attitudes.IlluminatedPoint;
import vega.uplink.pointing.attitudes.Inertial;
import vega.uplink.pointing.attitudes.Limb;
import vega.uplink.pointing.attitudes.Specular;
import vega.uplink.pointing.attitudes.Track;
import vega.uplink.pointing.attitudes.Velocity;
import vega.uplink.pointing.exclusion.AbstractExclusion;
import vega.uplink.pointing.exclusion.Period;
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
				for (int i=0;i<attEvents.length;i++){
					result=result+"<tr><td><pre>"+HtmlEditorKit.escapeHTML(ptr.getSegments()[0].getBlockAt(attEvents[i].start).toXml(0))+"</pre></td><td>"+attEvents[i]+"</td></tr>";
				}
			}
			AngularMomentum agmom = ag.getAngularMomentum();
			result=result+agmom+"\n";
			HgaOutages[] hga = ag.getHgaOutages();
			if (hga.length>0){
				for (int i=0;i<hga.length;i++){
					result=result+"<tr><td><pre>"+HtmlEditorKit.escapeHTML(ptr.getSegments()[0].getBlocksAt(hga[i].getStartDate(),hga[i].getEndDate()).toXml(0))+"</pre></td><td>"+hga[i].toString();
					vega.uplink.pointing.exclusion.Period[] ex = ep.findOverlapingPeriods(hga[i].getStartDate(), hga[i].getEndDate());
					for (int p=0;p<ex.length;p++){
						result=result+"<font color=RED>In exclusion period:"+ex[p]+"</font><br>";
					}
					result=result+"</td></tr>";
				}
			}
			vega.uplink.pointing.EvtmEvent[] illuminationEv = ag.getEvtm().getEventsByType(EvtmEvent.EVENT_TYPE_BDI);
			if (illuminationEv.length>0){
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
		return checkPtrHTML(ptr,ptsl,pdfm,fecs,ag,ep,null);
	}
	private static boolean checkElement(PointingElement element){
		boolean result = false;
		if (checkAttributes(element)) result=true;
		if (element.getName().equals("frame") && element.getValue().equals("CG")) result=true;
		if (element.hasChildren()){
			PointingElement[] ch = element.getChildren();
			for (int i=0;i<ch.length;i++){
				if (checkElement(ch[i])) result=true;
			}
		}
		return result;
		
	}
	private static boolean checkAttributes(PointingElement element){
		boolean result = false;
		PointingElement[] att = element.getAttributes();
		for (int i=0;i<att.length;i++){
			PointingElement a = att[i];
			if (a.getName().equals("frame")){
				if (a.getValue().equals("CG")){
					result=true;
				}
			}
		}
		
		return result;
		
	}
	private static double[] modf(double num){
		double[] result=new double[2];
		long iPart;
		double fPart;

		iPart = (long) num;
		fPart = num - iPart;
		result[0]=fPart;
		result[1]=iPart;
		return result;
	}
	public static String additionalCheckPtrHTML(Ptr ptr,Pdfm pdfm,Ptr ptsl,AbstractExclusion ep,LanderVisibility landerVis,AttitudeMap ptslAtt,AttitudeMap ptrAtt){
		String PTR_TABLE_HEADER=""
				+ "<tr>\n"
				+ "	<th>Message<th>"+ptr.getName()+" Blocks</th>\n"
				+ "</tr>\n";
		String result="";
		result=result+"<table class=\"gridtable\">"+PTR_TABLE_HEADER;

		PtrSegment segment = ptr.getSegments()[0];
		PointingBlock[] blocks = segment.getBlocks();
		PointingBlock lastSlew;
		Period[] periods = ep.findOverlapingPeriods(segment.getSegmentStartDate(),segment.getSegmentEndDate());
		for (int i=0;i<periods.length;i++){
			Period p = periods[i];
			long dateStart = p.getStartDate().getTime()/1000;
			long dateEnd = p.getEndDate().getTime()/1000;
			PointingBlock lastPointingBlockError=null;
			double maxAngle=0;
			long maxDate=0;
			//for sample in range(dateStart,dateEnd):
			for (long sample=dateStart;sample<=dateEnd;sample++){
				Quaternion q1 = ptrAtt.get(sample*1000);
				Quaternion q2 = ptslAtt.get(sample*1000);
				double angle = (q1.multiply(q2.conjugate())).normalizeSign().angle();
				if (angle>2.25){
					PointingBlock b = segment.getBlockAt(new Date(sample*1000));
					if (lastPointingBlockError==null){
						lastPointingBlockError=b;
						maxAngle=angle;
						maxDate=sample;
					}else{
						if (lastPointingBlockError.equals(b)){
							if (angle>maxAngle){
								maxAngle=angle;
								maxDate=sample;

							}
						}else{
							result=result+"<tr><td>Error: difference between PTR and PTSL during exclusion period > 2.25 degrees ("+maxAngle+") at "+DateUtil.defaultDateToString(new Date(maxDate*1000))+"<br></td><td><pre>"+HtmlDocument.escapeHTML(lastPointingBlockError.toXml(0))+"</pre></td></tr>";
							lastPointingBlockError=b;
							maxAngle=angle;
							maxDate=sample;
						}
					}
					
					//result=result+"<tr><td>Error: difference between PTR and PTSL during exclusion period > 2.25 degrees ("+angle+") at "+DateUtil.defaultDateToString(new Date(sample*1000))+"<br></td><td><pre>"+HtmlDocument.escapeHTML(b.toXml(0))+"</pre></td></tr>";

				}
			}
			if (lastPointingBlockError!=null){
				result=result+"<tr><td>Error: difference between PTR and PTSL during exclusion period > 2.25 degrees ("+maxAngle+") at "+DateUtil.defaultDateToString(new Date(maxDate*1000))+"<br></td><td><pre>"+HtmlDocument.escapeHTML(lastPointingBlockError.toXml(0))+"</pre></td></tr>";
			}


		}
		periods=landerVis.findOverlapingPeriods(segment.getSegmentStartDate(),segment.getSegmentEndDate());
		//for p in periods:
		for (int i=0;i<periods.length;i++){

			Period p = periods[i];
			long dateStart = p.getStartDate().getTime()/1000;
			long dateEnd = p.getEndDate().getTime()/1000;
			for (long sample=dateStart;sample<=dateEnd;sample++){
				//for sample in range(dateStart,dateEnd):
				Quaternion q1 = ptrAtt.get(sample*1000);
				Quaternion q2 = ptslAtt.get(sample*1000);
				double angle = AttitudeUtils.getAngularDistanceZAxis(q1,q2);
				if (angle>0.6){
					PointingBlock b = segment.getBlockAt(new Date(sample*1000));
					result=result+"<tr><td>Error: difference between PTR and PTSL during lander communication period > 0.6 degrees in the Z axis ("+angle+") at "+DateUtil.defaultDateToString(new Date(sample*1000))+"<br></td><td><pre>"+HtmlDocument.escapeHTML(b.toXml(0))+"</pre></td></tr>";
				}
			}
		}
		//for b in blocks:

		for (int i=0;i<blocks.length;i++){
			PointingBlock b=blocks[i];

			if (checkElement(b)){
				result=result+"<tr><td>frame CG is not allowed<br></td><td><pre>"+HtmlDocument.escapeHTML(b.toXml(0))+"</pre></td></tr>";

			}
			PointingAttitude att = b.getAttitude();
			//type=att.__class__

			if (b.isSlew()){
				lastSlew=b;
			}
			if (InterpreterUtil.isInstance(Specular.class, att)){
				result=result+"<tr><td>Specular attitude is not allowed<br></td><td><pre>"+HtmlDocument.escapeHTML(b.toXml(0))+"</pre></td></tr>";
			}
			// Slews to/from Inertial = 1.5 hours
			if (InterpreterUtil.isInstance(Velocity.class, att)){
				result=result+"<tr><td>Velocity attitude should not be used<br></td><td><pre>"+HtmlDocument.escapeHTML(b.toXml(0))+"</pre></td></tr>";
			}
			//IlluminatedPoint followed by Track


			if (InterpreterUtil.isInstance(IlluminatedPoint.class, att)){
				if (segment.blockAfter(b).getType().equals("SLEW")){
					PointingBlock next=segment.blockAfter(segment.blockAfter(b));
					if (InterpreterUtil.isInstance(Track.class, next.getAttitude())){
						double distance = ptrAtt.getAngularDistance(b,next);
						double minimumMinutes=15;
						double delta = distance-5;
						double extra = 0;
						if (delta>0){
							extra=modf(delta)[1]*2;
						}
						minimumMinutes=minimumMinutes+extra;
						long d=segment.blockAfter(b).getDuration()/1000;
						if (d<minimumMinutes*60){
							PointingBlocksSlice bl=segment.getBlocksAt(b.getStartTime(),next.getEndTime());
							result=result+"<tr><td>Error slew between IlluminatedPoint and Track is less than "+minimumMinutes+"m distance is "+distance+" degrees<br></td><td><pre>"+HtmlDocument.escapeHTML(bl.toXml(0))+"</pre></td></tr>";
						}
					}
				}
			}
			//Inertial followed by Illuminated Point
			if (InterpreterUtil.isInstance(Inertial.class, att)){
				//PointingBlock PointingBlock next;
				if (segment.blockAfter(b).getType().equals("SLEW")){
					PointingBlock next=segment.blockAfter(segment.blockAfter(b));
					if (InterpreterUtil.isInstance(IlluminatedPoint.class, next.getAttitude())){
						int minimumMinutes = 90;
						long d = segment.blockAfter(b).getDuration()/1000;
						if (d<minimumMinutes*60){
							PointingBlocksSlice bl = segment.getBlocksAt(b.getStartTime(),next.getEndTime());
							result=result+"<tr><td>Error slew between Inertial and IlluminatedPoint is less than "+minimumMinutes+"m <br></td><td><pre>"+HtmlDocument.escapeHTML(bl.toXml(0))+"</pre></td></tr>";
						}
					}
				}
			}
			//Track followed by Track
			if (InterpreterUtil.isInstance(Track.class, att)){
				if (segment.blockAfter(b).getType().equals("SLEW")){
					PointingBlock next = segment.blockAfter(segment.blockAfter(b));
					if (InterpreterUtil.isInstance(Track.class, next.getAttitude())){
						//print next.toXml(0)
						double distancex = ptrAtt.getOffsetXAxis(b,next);
						//print "distance x "+str(distancex)
						double minimumMinutes = 5;
						if (distancex>20){
							minimumMinutes=10;
						}
						if (distancex>40){
							minimumMinutes=10+((distancex-40)/2);
						}
						long d = segment.blockAfter(b).getDuration()/1000;
						if (d<minimumMinutes*60){
							PointingBlocksSlice bl = segment.getBlocksAt(b.getStartTime(),next.getEndTime());
							result=result+"<tr><td>Error slew between Track and Track is less than "+minimumMinutes+"m offset x is "+distancex+" degrees<br></td><td><pre>"+HtmlDocument.escapeHTML(bl.toXml(0))+"</pre></td></tr>";
						}
						//print next.toXml(0)
						double distancey = ptrAtt.getOffsetYAxis(b,next);
						//print "distance y "+str(distancey)
						minimumMinutes=10;
						if (distancex>10){
							minimumMinutes=15;
						}
						if (distancex>20){
							minimumMinutes=30;
						}
						if (distancex>40){
							minimumMinutes=30+(distancey-40);
						}
						d=segment.blockAfter(b).getDuration()/1000;
						if (d<minimumMinutes*60){
							PointingBlocksSlice bl = segment.getBlocksAt(b.getStartTime(),next.getEndTime());
							result=result+"<tr><td>Error slew between Track and Track is less than "+minimumMinutes+"m offset y is "+distancey+" degrees<br></td><td><pre>"+HtmlDocument.escapeHTML(bl.toXml(0))+"</pre></td></tr>";
						}
					}
				}
			}
			//IlluminatedPoint followed by IlluminatedPoint
			if (InterpreterUtil.isInstance(IlluminatedPoint.class, att)){
				if (segment.blockAfter(b).getType().equals("SLEW")){
					PointingBlock next = segment.blockAfter(segment.blockAfter(b));
					if (InterpreterUtil.isInstance(IlluminatedPoint.class, next.getAttitude())){
						//print next.toXml(0)
						double distancex = ptrAtt.getOffsetXAxis(b,next);
						//print "distance x "+str(distancex)
						double minimumMinutes = 5;
						if (distancex>20){
							minimumMinutes=10;
						}
						if (distancex>40){
							minimumMinutes=10+((distancex-40)/2);
						}
						long d = segment.blockAfter(b).getDuration()/1000;
						if (d<minimumMinutes*60){
							PointingBlocksSlice bl = segment.getBlocksAt(b.getStartTime(),next.getEndTime());
							result=result+"<tr><td>Error slew between IlluminatedPoint and IlluminatedPoint is less than "+minimumMinutes+"m offset x is "+distancex+" degrees<br></td><td><pre>"+HtmlDocument.escapeHTML(bl.toXml(0))+"</pre></td></tr>";
						}
						//print next.toXml(0)
						double distancey = ptrAtt.getOffsetYAxis(b,next);
						//print "distance y "+str(distancey)
						minimumMinutes=10;
						if (distancex>10){
							minimumMinutes=15;
						}
						if (distancex>20){
							minimumMinutes=30;
						}
						if (distancex>40){
							minimumMinutes=30+(distancey-40);
						}
						d=segment.blockAfter(b).getDuration()/1000;
						if (d<minimumMinutes*60){
							PointingBlocksSlice bl = segment.getBlocksAt(b.getStartTime(),next.getEndTime());
							result=result+"<tr><td>Error slew between IlluminatedPoint and IlluminatedPoint is less than "+minimumMinutes+"m offset y is "+distancey+" degrees<br></td><td><pre>"+HtmlDocument.escapeHTML(bl.toXml(0))+"</pre></td></tr>";
						}
					}
				}
			}
			//limb followed by limb
			if (InterpreterUtil.isInstance(Limb.class, att)){
				if (segment.blockAfter(b).getType().equals("SLEW")){
					PointingBlock next = segment.blockAfter(segment.blockAfter(b));
					PointingBlocksSlice bl;
					if (InterpreterUtil.isInstance(Limb.class, next.getAttitude())){
						//print next.toXml(0)
						double distancex = ptrAtt.getOffsetXAxis(b,next);
						//print "distance x "+str(distancex)
						double minimumMinutes = 5;
						if (distancex>20){
							minimumMinutes=10;
						}
						if (distancex>40){
							minimumMinutes=10+((distancex-40)/2);
						}
						long d = segment.blockAfter(b).getDuration()/1000;
						if (d<minimumMinutes*60){
							bl = segment.getBlocksAt(b.getStartTime(),next.getEndTime());
							result=result+"<tr><td>Error slew between Limb and Limb is less than "+minimumMinutes+"m offset x is "+distancex+" degrees<br></td><td><pre>"+HtmlDocument.escapeHTML(bl.toXml(0))+"</pre></td></tr>";
						}
						//print next.toXml(0)
						double distancey = ptrAtt.getOffsetYAxis(b,next);
						//print "distance y "+str(distancey)
						minimumMinutes=10;
						if (distancex>10){
							minimumMinutes=15;
						}
						if (distancex>20){
							minimumMinutes=30;
						}
						if (distancex>40){
							minimumMinutes=30+(distancey-40);
						}
						d=segment.blockAfter(b).getDuration()/1000;
						if (d<minimumMinutes*60){
							bl=segment.getBlocksAt(b.getStartTime(),next.getEndTime());
							result=result+"<tr><td>Error slew between Limb and Limb is less than "+minimumMinutes+"m offset y is "+distancey+" degrees<br></td><td><pre>"+HtmlDocument.escapeHTML(bl.toXml(0))+"</pre></td></tr>";
						}
					}
				}
			}
			PointingBlock next;
			PointingBlocksSlice bl;
			//Limb followed by Illuminated Point
			if (InterpreterUtil.isInstance(Limb.class, att)){
				long d;
				if (segment.blockAfter(b).getType().equals("SLEW")){
					next=segment.blockAfter(segment.blockAfter(b));
					if (InterpreterUtil.isInstance(IlluminatedPoint.class, next.getAttitude())){
						//print next.toXml(0)
						double minimumMinutes=5;
						d=segment.blockAfter(b).getDuration()/1000;
						if (d<minimumMinutes*60){
							bl=segment.getBlocksAt(b.getStartTime(),next.getEndTime());
							result=result+"<tr><td>Error slew between Limb and IlluminatedPoint is less than "+minimumMinutes+"m <br></td><td><pre>"+HtmlDocument.escapeHTML(bl.toXml(0))+"</pre></td></tr>";
						}
					}
				}
			}
			//Limb followed by Track Point
			int minimumMinutes;
			long d;
			if (InterpreterUtil.isInstance(Limb.class, att)){
				if (segment.blockAfter(b).getType().equals("SLEW")){
					next=segment.blockAfter(segment.blockAfter(b));
					if (InterpreterUtil.isInstance(Track.class, next.getAttitude())){
						//print next.toXml(0)
						minimumMinutes=5;
						d=segment.blockAfter(b).getDuration()/1000;
						if (d<minimumMinutes*60){
							bl=segment.getBlocksAt(b.getStartTime(),next.getEndTime());
							result=result+"<tr><td>Error slew between Limb and Track is less than "+minimumMinutes+"m <br></td><td><pre>"+HtmlDocument.escapeHTML(bl.toXml(0))+"</pre></td></tr>";
						}
					}
				}
			}
			//Limb followed by Track Point
			if (InterpreterUtil.isInstance(IlluminatedPoint.class, att)){
				if (segment.blockAfter(b).getType().equals("SLEW")){
					next=segment.blockAfter(segment.blockAfter(b));
					if (InterpreterUtil.isInstance(Track.class, next.getAttitude())){
						//print next.toXml(0)
						minimumMinutes=5;
						d=segment.blockAfter(b).getDuration()/1000;
						if (d<minimumMinutes*60){
							bl=segment.getBlocksAt(b.getStartTime(),next.getEndTime());
							result=result+"<tr><td>Error slew between IlluminatedPoint and Track is less than "+minimumMinutes+"m <br></td><td><pre>"+HtmlDocument.escapeHTML(bl.toXml(0))+"</pre></td></tr>";
						}
					}
				}
			}
		}

		result=result+"</table>";
		return result;

	}
	public static String checkPtrHTML(Ptr ptr,Ptr ptsl,Pdfm pdfm,Fecs fecs,AttitudeGeneratorFDImpl ag,AbstractExclusion ep,LanderVisibility landerVis){
				if (ep==null) ep=new ExclusionPeriod();
				if (landerVis==null) landerVis=new LanderVisibility();
				String result="";
				if (ptsl!=null && pdfm!=null){
					AttitudeMap ptslAtt;
					try{
						ptslAtt = AttitudeGeneratorFDImpl.getAttitudeMap(ptsl,pdfm,AttitudeGeneratorFDImpl.getMtpNum(ptr),ag.getTrajectory());
					}catch (Exception e){
						IllegalArgumentException iae = new IllegalArgumentException("Could not calculate attitude for the PTSL:"+e.getMessage());
						iae.initCause(e);
						throw iae;
					}
					result=result+additionalCheckPtrHTML(ptr,pdfm,ptsl,ep,landerVis,ptslAtt,ag.getAllQuaternions());
				}
				HashMap<String, String> evtmDic = getRosettaIlumEventMessages();
				if (fecs==null){
					String PTR_TABLE_HEADER=""
							+ "<tr>\n"
							+ "	<th>Blocks</th><th>Error</th>\n"
							+ "</tr>\n";
					result=result+"<table class=\"gridtable\">"+PTR_TABLE_HEADER;

						AttitudeConstrainEvent[] attEvents = ag.getAttEvents();
						if (attEvents.length>0){
							for (int i=0;i<attEvents.length;i++){
								result=result+"<tr><td><pre>"+HtmlEditorKit.escapeHTML(ptr.getSegments()[0].getBlockAt(attEvents[i].start).toXml(0))+"</pre></td><td>"+attEvents[i]+"</td></tr>";
							}
						}
						AngularMomentum agmom = ag.getAngularMomentum();
						result=result+agmom+"\n";
						HgaOutages[] hga = ag.getHgaOutages();
						if (hga.length>0){
							for (int i=0;i<hga.length;i++){
								result=result+"<tr><td><pre>"+HtmlEditorKit.escapeHTML(ptr.getSegments()[0].getBlocksAt(hga[i].getStartDate(),hga[i].getEndDate()).toXml(0))+"</pre></td><td>"+hga[i].toString();
								vega.uplink.pointing.exclusion.Period[] ex = ep.findOverlapingPeriods(hga[i].getStartDate(), hga[i].getEndDate());
								for (int p=0;p<ex.length;p++){
									result=result+"<font color=RED>In exclusion period:"+ex[p]+"</font><br>";
								}
								result=result+"</td></tr>";
							}
						}
						vega.uplink.pointing.EvtmEvent[] illuminationEv = ag.getEvtm().getEventsByType(EvtmEvent.EVENT_TYPE_BDI);
						if (illuminationEv.length>0){
							for (int i=0;i<illuminationEv.length;i++){
								result=result+"<tr><td><pre>"+HtmlEditorKit.escapeHTML(ptr.getSegments()[0].getBlockAt(illuminationEv[i].getTime()).toXml(0))+"</pre></td><td>"+evtmDic.get(illuminationEv[i].getId())+"</td></tr>";

							}
						}
				}else{
		
		//String result="";
		result=result+PtrChecker.checkPtrHTML(ptr, ptsl);
		
		String PTR_TABLE_HEADER=""
				+ "<tr>\n"
				+ "	<th>Blocks</th><th>Error</th>\n"
				+ "</tr>\n";
		result=result+"<table class=\"gridtable\">"+PTR_TABLE_HEADER;

			AttitudeConstrainEvent[] attEvents = ag.getAttEvents();
			if (attEvents.length>0){
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

				for (int i=0;i<illuminationEv.length;i++){
					result=result+"<tr><td><pre>"+HtmlEditorKit.escapeHTML(ptr.getSegments()[0].getBlockAt(illuminationEv[i].getTime()).toXml(0))+"</pre></td><td>"+DateUtil.dateToZulu(illuminationEv[i].getTime())+" "+evtmDic.get(illuminationEv[i].getId())+"</td></tr>";

				}
			}
			AngularMomentum agmom = ag.getAngularMomentum();
			AMInterval[] agIntervals = agmom.getIntervals();
			for (int i=0; i<agIntervals.length;i++){
				result=result+"<tr><td><pre>"+HtmlEditorKit.escapeHTML(ptr.getSegments()[0].getBlocksAt(agIntervals[i].getStartTime(),agIntervals[i].getEndTime()).toXml(0))+"</pre></td><td>Angular Momentum:"+agIntervals[i].toString()+"</td></tr>";
				
			}
			result=result+"</table>";
			HgaOutages[] hga = ag.getHgaOutages();
			if (hga.length>0){
				result=result+"<h2>HGA coverage</h2>";
				for (int i=0;i<hga.length;i++){
					vega.uplink.pointing.exclusion.Period[] ex = ep.findOverlapingPeriods(hga[i].getStartDate(), hga[i].getEndDate());
					for (int p=0;p<ex.length;p++){
						hga[i].addNote("In exclusion period:"+ex[p]);
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

					}
					if (affected.length>0) result=result+"</table>";
				}

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
		String result="";
		result=result+PtrChecker.checkPtrHTML(ptr, ptsl);
		try {
			AttitudeGeneratorFDImpl ag = new RosettaAttitudeGenerator(ptr,pdfm);
			result=result+checkPtrHTML(ptr,ptsl,pdfm,fecs,ag);
		} catch (AttitudeGeneratorException e) {
			result=result+e.getMessage()+"\n";
			e.printStackTrace();
		}
		return result;
	}
	public static String checkPtrHTML(Ptr ptr,Ptr ptsl,Pdfm pdfm,Fecs fecs,AbstractExclusion exclusion){
		String result="";
		result=result+PtrChecker.checkPtrHTML(ptr, ptsl);
		try {
			AttitudeGeneratorFDImpl ag = new RosettaAttitudeGenerator(ptr,pdfm);
			result=result+checkPtrHTML(ptr,ptsl,pdfm,fecs,ag,exclusion);
		} catch (AttitudeGeneratorException e) {
			result=result+e.getMessage()+"\n";
			e.printStackTrace();
		}
		return result;
	}

	public static String checkPtr(Ptr ptr,Ptr ptsl,Pdfm pdfm,Fecs fecs){
		String result="";
		result=result+PtrChecker.checkPtr(ptr, ptsl);
		try {
			AttitudeGeneratorFDImpl ag = new RosettaAttitudeGenerator(ptr,pdfm);
			result=result+checkPtr(ptr,ptsl,pdfm,fecs,ag);
		} catch (AttitudeGeneratorException e) {
			result=result+e.getMessage()+"\n";
			e.printStackTrace();
		}
		return result;
	}


}
