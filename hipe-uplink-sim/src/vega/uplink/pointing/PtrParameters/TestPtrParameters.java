package vega.uplink.pointing.PtrParameters;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import vega.uplink.DateUtil;
import vega.uplink.pointing.*;
import vega.uplink.pointing.PtrParameters.Offset.*;
//import vega.uplink.pointing.PtrParameters.dirvector.Reference;
import vega.uplink.pointing.attitudes.*;

public class TestPtrParameters {
	static PrintWriter writer;
	//public static String[] refs={};
	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		writer = new PrintWriter("Z:\\testptrall.xml", "UTF-8");
		// TODO Auto-generated method stub
		herschel.share.util.Configuration.setProperty("var.hcss.dir", "C:\\Users\\jarenas\\Downloads\\hcss-12.0.2524");
		herschel.share.util.Configuration.setProperty("var.hcsstest.dir", "C:\\Users\\jarenas\\Downloads\\hcss-12.0.2524");
		try {
		Ptr ptr=PtrUtils.readPTRfromFile("/Users/jarenas 1/OPS/ROS_SGS/PLANNING/LTP002/LTP002A/MTP007A/PTR/PTRM_DM_007_01____A__00015.ROS");
		PtrSegment seg = ptr.getSegment("MTP_007");

			PointingBlock block = seg.getBlockAt(DateUtil.zuluToDate("2014-09-03T18:30:00"));
			OffsetAngles os = block.getAttitude().getOffsetAngles();
			System.out.println(os.getDurationMilliSecs());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void createNewFakePTR(Vector<PointingAttitude> vector,Vector<OffsetAngles> vOffSet){
		PtrSegment segment = new PtrSegment("MTP_005");

		
		long startTime=1404282900000L;
		long duration=2100000L;
		long slewDuration=300000L;
		PointingBlock block;
		Iterator<PointingAttitude> it = vector.iterator();
		while (it.hasNext()){
			PointingAttitude at=it.next();
			block=new PointingBlock("OBS",new Date(startTime),new Date(startTime+duration));
			block.setAttitude(at);
			segment.addBlock(block);
			print(block.toXml(0));
			segment.addBlock(new PointingBlockSlew());
			print(new PointingBlockSlew().toXml(0));
			startTime=startTime+duration+slewDuration;

			Iterator<OffsetAngles> it2 = vOffSet.iterator();
			while (it2.hasNext()){
				PointingAttitude at2=at.copy();
				OffsetAngles offset=it2.next().copy();
				long offsetstart=startTime+(duration/4);
				System.out.println(DateUtil.dateToZulu(new Date(offsetstart)));
				if (!offset.getAttribute("ref").getValue().equals("fixed")) offset.setStartTime(DateUtil.dateToZulu(new Date(offsetstart)));
				
				at2.SetOffset(new OffsetRefAxis(), offset);
				block=new PointingBlock("OBS",new Date(startTime),new Date(startTime+duration));
				block.setAttitude(at2);
				segment.addBlock(block);

				print(block.toXml(0));
				print(new PointingBlockSlew().toXml(0));
				segment.addBlock(new PointingBlockSlew());
				startTime=startTime+duration+slewDuration;

			}
		}
		segment.repairMocm();
		//segment.getBlockAt(time)
		Ptr ptr=new Ptr();
		segment.addInclude("FDDF.xml");
		//System.out.println(segment.toXml(0));
		ptr.addSegment(segment);
		PtrUtils.writePTRtofile("Z:\\testptrall2.xml", ptr);
		writer.close();
	}
	

	public static void print(String string){
		writer.println(string);
		System.out.println(string);
	}
	/*public static PointingAttitude[] getAllBasicPointings(){
		Vector<PointingAttitude> vResult=new Vector<PointingAttitude>();

		PointingAttitude[] patt = getAllTrackAttitude();
		for (int i=0;i<patt.length;i++){
			vResult.add(patt[i]);
		}	
		PointingAttitude[] iatt = getAllInertialAttitude();
		for (int i=0;i<iatt.length;i++){
			vResult.add(iatt[i]);
		}	
		PointingAttitude[] latt = getAllLimbAttitude();
		for (int i=0;i<latt.length;i++){
			vResult.add(latt[i]);
		}	
		
		PointingAttitude[] result=new PointingAttitude[vResult.size()];
		vResult.toArray(result);
		result=addAllOffsetAngles(result);
		return result;


	}*/
	
	/*public static Boresight[] getAllCombinationsBoresight(){
		Vector<Boresight> vResult=new Vector<Boresight>();
		String[] frames={"SC"};
		String[] refs={"Nadir_Nav_Boresight","SC_Zaxis","SC_Xaxis","SC_Yaxis"};
		String[] values={"0.","1."};
		for (int i=0;i<frames.length;i++){
			String ref=frames[i];
			for (int x=0;x<values.length;x++){
				//System.out.println(x);
				String xV=values[x];
				for (int y=0;y<values.length;y++){
					String yV=values[y];
					for (int z=0;z<values.length;z++){
						String zV=values[z];
						vResult.add(new Boresight(ref,xV,yV,zV));
					}
				}
			}
		}
		for (int i=0;i<refs.length;i++){
			String ref=refs[i];
			vResult.add(new Boresight(refs[i]));
		}
		Boresight[] result=new Boresight[vResult.size()];
		vResult.toArray(result);
		return result;
	}*/
	
	public static PhaseAngle[] getAllCombinationsPhaseAngle(){
		Vector<PhaseAngle> vResult=new Vector<PhaseAngle>();
		String[] refs={"powerOptimised"};
		String[] dirs={"xDir","yDir","zDir"};
		//String[] values={"0.","1."};
		for (int i=0;i<refs.length;i++){
			String ref=refs[i];
			for (int x=0;x<dirs.length;x++){
				//System.out.println(x);
				String xV=dirs[x];
						vResult.add(new PhaseAngle(ref,xV));
			}
		}
		PhaseAngle[] result=new PhaseAngle[vResult.size()];
		vResult.toArray(result);
		return result;
		
	}
	
	/*public static TargetInert[] getAllCombinationsTarget(){
		Vector<DirVector> vResult=new Vector<TargetInert>();
		String[] frames={"EME2000"};
		float[] values={0.0f,1.0f};
		String[] refs={"CG"};
		for (int i=0;i<frames.length;i++){
			String ref=frames[i];
			for (int x=0;x<values.length;x++){
				//System.out.println(x);
				//String xV=values[x];
				for (int y=0;y<values.length;y++){
					//String yV=values[y];
					for (int z=0;z<values.length;z++){
						//String zV=values[z];
						vResult.add(new PointedAxis("target",ref,values[x],values[y],values[z]));
					}
				}
			}
		}
		
		for (int i=0;i<refs.length;i++){
			//System.out.println("here");
			Reference ta = new Reference("target",refs[i]);
			//System.out.println("here"+ta.toXml(0));
			//System.out.println(vResult.size());
			vResult.add(ta);
			//System.out.println(vResult.size());

		}
		StateVector[] result=new StateVector[vResult.size()];
		vResult.toArray(result);
		return result;
	}*/
	
	public static TargetDir[] getAllCombinationsTargetDir(){
		String[] refs={"CG2Sun"};
		Vector<TargetDir> vResult=new Vector<TargetDir>();

		for (int i=0;i<refs.length;i++){
			//System.out.println("here");
			TargetDir ta = new TargetDir(refs[i]);
			//System.out.println("here"+ta.toXml(0));
			//System.out.println(vResult.size());
			vResult.add(ta);
			//System.out.println(vResult.size());

		}
		TargetDir[] result=new TargetDir[vResult.size()];
		vResult.toArray(result);
		return result;

	}
	
	public static Height[] getAllCombinationsHeight(){
		String[] units={"km"};
		String[] values={"0.","1.","2."};
		Vector<Height> vResult=new Vector<Height>();
		for (int i=0;i<units.length;i++){
			String un=units[i];
			for (int x=0;x<values.length;x++){
				String va=values[x];
				vResult.add(new Height(un,va));
			}
		}
		Height[] result=new Height[vResult.size()];
		vResult.toArray(result);
		return result;


	}

	public static Surface[] getAllCombinationsSurface(){
		String[] refs={"CG"};
		Vector<Surface> vResult=new Vector<Surface>();

		for (int i=0;i<refs.length;i++){
			//System.out.println("here");
			Surface ta = new Surface(refs[i]);
			//System.out.println("here"+ta.toXml(0));
			//System.out.println(vResult.size());
			vResult.add(ta);
			//System.out.println(vResult.size());

		}
		Surface[] result=new Surface[vResult.size()];
		vResult.toArray(result);
		return result;
		
		
	}
	
	/*public static PointingAttitude[] getAllTrackAttitude(){
		Vector<PointingAttitude> vResult=new Vector<PointingAttitude>();
		Boresight[] brs = getAllCombinationsBoresight();
		PhaseAngle[] ph = getAllCombinationsPhaseAngle();
		StateVector[] ta = getAllCombinationsTarget();
		for (int i=0;i<=brs.length;i++){
			Boresight b=null;
			if (i<brs.length) b=brs[i];
			for (int j=0;j<=ph.length;j++){
				PhaseAngle p=null;
				if (j<ph.length) p=ph[j];
				for (int y=0;y<=ta.length;y++){
					StateVector t=null;
					if (y<ta.length) t=ta[y];
					PointingAttitude patt=new PointingAttitude("track");
					//System.out.println(b);
					if (b!=null) patt.addChild(b);
					if (p!=null) patt.addChild(p);
					if (t!=null) patt.addChild(t);
					/*patt.setBoresight(b);
					patt.setPhaseAngle(p);
					patt.setTarget(t);
					vResult.add(patt);
				}
			}
		}
		PointingAttitude[] result=new PointingAttitude[vResult.size()];
		vResult.toArray(result);
		return result;

	}*/

	/*public static PointingAttitude[] getAllInertialAttitude(){
		Vector<PointingAttitude> vResult=new Vector<PointingAttitude>();
		Boresight[] brs = getAllCombinationsBoresight();
		PhaseAngle[] ph = getAllCombinationsPhaseAngle();
		StateVector[] ta = getAllCombinationsTarget();
		for (int i=0;i<=brs.length;i++){
			Boresight b=null;
			if (i<brs.length) b=brs[i];
			for (int j=0;j<=ph.length;j++){
				PhaseAngle p=null;
				if (j<ph.length) p=ph[j];
				for (int y=0;y<=ta.length;y++){
					StateVector t=null;
					if (y<ta.length) t=ta[y];
					PointingAttitude patt=new PointingAttitude("inertial");
					//System.out.println(b);
					if (b!=null) patt.addChild(b);
					if (p!=null) patt.addChild(p);
					if (t!=null) patt.addChild(t);
					/*patt.setBoresight(b);
					patt.setPhaseAngle(p);
					patt.setTarget(t);
					vResult.add(patt);
				}
			}
		}
		PointingAttitude[] result=new PointingAttitude[vResult.size()];
		vResult.toArray(result);
		return result;

	}*/

	/*public static PointingAttitude[] getAllLimbAttitude(){
		Vector<PointingAttitude> vResult=new Vector<PointingAttitude>();
		Boresight[] brs = getAllCombinationsBoresight();
		PhaseAngle[] ph = getAllCombinationsPhaseAngle();
		TargetDir[] ta = getAllCombinationsTargetDir();
		Height[] he= getAllCombinationsHeight();
		Surface[] su= getAllCombinationsSurface();
		for (int i=0;i<=brs.length;i++){
			Boresight b=null;
			if (i<brs.length) b=brs[i];
			for (int j=0;j<=ph.length;j++){
				PhaseAngle p=null;
				if (j<ph.length) p=ph[j];
				for (int y=0;y<=ta.length;y++){
					TargetDir t=null;
					if (y<ta.length) t=ta[y];
					for (int z=0;z<=he.length;z++){
						Height h=null;
						if (z<he.length) h=he[z];
						for (int k=0;k<=su.length;k++){
							Surface s=null;
							if (k<su.length) s=su[k];
							PointingAttitude patt=new PointingAttitude("limb");
							//System.out.println(b);
							if (b!=null) patt.addChild(b);
							if (p!=null) patt.addChild(p);
							if (t!=null) patt.addChild(t);
							if (h!=null) patt.addChild(h);
							if (s!=null) patt.addChild(s);
							vResult.add(patt);
						}
					}

					/*patt.setBoresight(b);
					patt.setPhaseAngle(p);
					patt.setTarget(t);
					
				}
			}
		}
		PointingAttitude[] result=new PointingAttitude[vResult.size()];
		vResult.toArray(result);
		return result;

	}*/
	
	public static OffsetRefAxis[] getAllCombinationsOffsetAxis(){
		String[] refAxis={"SC_Xaxis","SC_Yaxis","SC_Yaxis"};
		Vector<OffsetRefAxis> vResult=new Vector<OffsetRefAxis>();

		for (int i=0;i<refAxis.length;i++){
			vResult.add(new OffsetRefAxis(refAxis[i]));
		}
		OffsetRefAxis[] result=new OffsetRefAxis[vResult.size()];
		vResult.toArray(result);
		return result;
		
	}
	
	public static OffsetAngles[] getAllCombinationsOffsetAngles(){
		String[] refAxis={"fixed"};
		String[] values={"0.","1.","-1."};

		Vector<OffsetAngles> vResult=new Vector<OffsetAngles>();

		for (int i=0;i<refAxis.length;i++){
			String ref=refAxis[i];
			for (int j=0;j<values.length;j++){
				String xa=values[j];
				for (int k=0;k<values.length;k++){
					String ya=values[k];
					//vResult.add(new OffsetAngles(refAxis[i],xa,ya));
				}
			}
			
		}
		OffsetAngles[] result=new OffsetAngles[vResult.size()];
		vResult.toArray(result);
		return result;
		
	}
	
	public static PointingAttitude[] addAllOffsetAngles(PointingAttitude[] pAtts){
		Vector<PointingAttitude> vResult=new Vector<PointingAttitude>();
		for (int i=0;i<pAtts.length;i++){
			vResult.add(pAtts[i].copy());
		}
		
		OffsetRefAxis[] oAxis = getAllCombinationsOffsetAxis();
		OffsetAngles[] oAng = getAllCombinationsOffsetAngles();
		for (int i=0;i<pAtts.length;i++){
			for (int j=0;j<oAxis.length;j++){
				for (int k=0;k<oAng.length;k++){
					PointingAttitude temp=pAtts[i].copy();
					temp.addChild(oAxis[j]);
					temp.addChild(oAng[k]);
					
					vResult.add(temp);
				}
			}
		}
		//String[] refAngles={"fixed"};
		//String[] values={"0.","1.","-1."};
		//String[] refAxis={"SC_Xaxis","SC_Yaxis","SC_Yaxis"};
		
		PointingAttitude[] result=new PointingAttitude[vResult.size()];
		vResult.toArray(result);
		return result;
		
	}
	
}
