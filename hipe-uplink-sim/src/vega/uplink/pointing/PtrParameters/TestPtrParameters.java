package vega.uplink.pointing.PtrParameters;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

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
		Surface sur = new Surface("CG_Lamy","CG","m",2398.0f,"m",1887f,"m",1532f,"CG",0.9f,-0.3f,0.0f,"CG",0.9f,-0.3f,0.0f,"CG",0.9f,-0.3f,0.0f);
		System.out.println(sur.toXml(0));
		//Ptr orPtr=PtrUtils.readPTRfromFile("C:\\ROS_SGS\\PLANNING\\LTP001\\LTP001A\\MTP004A\\PTR\\PTRM_DM_004_01____A__00005.ROS");
		//PtrSegment orSeg = orPtr.getSegment("MTP_004");
		//PointingBlock[] orBlocks = orSeg.getAllBlocksOfType("OBS");
		
		Vector<PointingAttitude> vector=new Vector<PointingAttitude>();
		//block=new PointingBlock("OBS",new Date(startTime),new Date(startTime+duration));
		PhaseAngle pa1=new PhaseAngle("SC",1.0f,1.0f,1.0f,"EME2000",1.0f,1.0f,1.0f);
		Boresight br1 = new Boresight("SC",0.0f,0.0f,1.0f);
		Boresight br2 = new Boresight("SC",1.0f,0.0f,0.0f);
		//Boresight br3 = new Boresight("SC",0.0f,1.0f,0.0f);


		IlluminatedPoint illp=new IlluminatedPoint();
		vector.add(illp);
		vector.add(new IlluminatedPoint(new Boresight("SC",1.1f,2.2f,3.3f),pa1,new Surface("CG")));
		//block.setAttitude(illp);
		//print(block.toXml(0));
		//print(new PointingBlockSlew().toXml(0));
		
		Inertial inertial=new Inertial();
		vector.add(inertial);
		vector.add(new Inertial(new Boresight(),new PhaseAngle(),new TargetInert("CG",5.5f,5.5f,5.5f)));
		vector.add(new Inertial(new Boresight(),pa1,new TargetInert("CG",5.5f,5.5f,5.5f)));

		//print(inertial.toXml(0));
		Limb limb=new Limb();
		vector.add(limb);
		vector.add(new Limb(new Boresight(),new PhaseAngle(),new TargetDir("SC2Sun"),new Height("km","5."),new Surface("CG")));
		vector.add(new Limb(new Boresight(),new PhaseAngle(),new TargetDir("SC2Sun"),new Height("km","0"),new Surface("CG")));
		vector.add(new Limb(new Boresight(),pa1,new TargetDir("SC2Sun"),new Height("km","5."),new Surface("CG")));
		vector.add(new Limb(new Boresight("SC",1.1f,2.2f,3.3f),new PhaseAngle(),new TargetDir("SC2Earth"),new Height("km","8.5"),new Surface("CG")));
		vector.add(new Limb(new Boresight(),new PhaseAngle(),new TargetDir("SC2Sun"),new Height("m","1000."),new Surface("CG")));
		vector.add(new Limb(new Boresight(),pa1,new TargetDir("Sun2CG"),new Height("m","1000."),new Surface("CG")));
		vector.add(new Limb(new Boresight(),pa1,new TargetDir("Sun2CG"),new Height("km","0."),new Surface("CG")));
		vector.add(new Limb(new Boresight(),new PhaseAngle(),new TargetDir("targetDir","CG_ROT_Pol"),new Height("km","0."),new Surface("CG")));
		vector.add(new Limb(new Boresight(),new PhaseAngle(),new TargetDir("targetDir","CG_ROT_Pol"),new Height("km","10."),new Surface("CG")));
		vector.add(new Limb(new Boresight(),new PhaseAngle(),new TargetDir("targetDir","CG_ROT_Pol"),new Height("km","10000."),new Surface("CG")));


		//print(limb.toXml(0));
		Specular specular=new Specular();
		vector.add(specular);

		//print(specular.toXml(0));
		Terminator terminator=new Terminator();
		vector.add(terminator);

		//print(terminator.toXml(0));
		Track track=new Track();
		vector.add(track);
		vector.add(new Track(new Boresight(),new PhaseAngle("SC",1.0f,1.0f,1.0f,"EME2000",1.0f,1.0f,1.0f),new TargetTrack("CG")));
		vector.add(new Track(new Boresight("SC",1.1f,2.2f,3.3f),new PhaseAngle(),new TargetTrack("CG")));
		vector.add(new Track(new Boresight("SC",1.1f,2.2f,3.3f),new PhaseAngle("SC",1.0f,1.0f,1.0f,"EME2000",1.0f,1.0f,1.0f),new TargetTrack("CG")));
		


		//print(track.toXml(0));
		Velocity velocity=new Velocity();
		vector.add(velocity);
		
		Vector<OffsetAngles> vOffSet=new Vector<OffsetAngles>();
		try {
			//vOffSet.add(new OffsetCustom(PointingBlock.dateToZulu(new Date())));
			//public OffsetCustom (Date startTime,float[] deltaTimes,float[] xAngles,float[] xRates,float[] yAngles,float[] yRates)
			vOffSet.add(new OffsetCustom(PointingBlock.dateToZulu(new Date()),"0. 1. 2. 3. 4.","0. 1. 2. 3. 4.","0. 1. 2. 3. 4.","0. 1. 2. 3. 4.","0. 1. 2. 3. 4."));
			vOffSet.add(new OffsetCustom(PointingBlock.dateToZulu(new Date()),"4. 4. 4. 4. 4.","4. 3. 2. 1. 0.","4. 3. 2. 1. 0.","4. 3. 2. 1. 0.","4. 3. 2. 1. 0."));
			//vOffSet.add(new OffsetCustom(PointingBlock.dateToZulu(new Date()),"5. 5. 5. 5. 5.","-1. 1. -1. 1. -1.","0.5 0.5 0.5 0.5 0.5","1. -1. 1. -1. 1.","0.5 0.5 0.5 0.5 0.5"));
			vOffSet.add(new OffsetCustom(PointingBlock.dateToZulu(new Date()),"0. 2. 3.5 2. 7. 2. 3.5 2.","-2.25 -2.25 2.25 2.25 2.25 2.25 -2.25 -2.25","0. 0. 0. 0. 0. 0. 0. 0.","-2.25 -2.25 -2.25 -2.25 2.25 2.25 2.25 2.25","0. 0. 0. 0. 0. 0. 0. 0."));

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//vOffSet.add(new OffsetFixed());
		vOffSet.add(new OffsetFixed("4.0","4.0"));
		vOffSet.add(new OffsetFixed("8.0","4.0"));

		try {
			//vOffSet.add(new OffsetRaster(PointingBlock.dateToZulu(new Date())));
			vOffSet.add(new OffsetRaster(PointingBlock.dateToZulu(new Date()),"4","1","1.","1.","1.","1.","1.","1.","1.","x","true"));
			vOffSet.add(new OffsetRaster(PointingBlock.dateToZulu(new Date()),"1","4","2.","2.","2.","2.","2.","2.","2.","y","false"));

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//OffsetScan scan;
		try {
			//vOffSet.add(new OffsetScan(PointingBlock.dateToZulu(new Date())));
			vOffSet.add(new OffsetScan(PointingBlock.dateToZulu(new Date()),"4","2","1.","1.","1.","1.","1.",null,"1.","1.","1.","x","true","false"));
			vOffSet.add(new OffsetScan(PointingBlock.dateToZulu(new Date()),"4","2","1.","1.","1.","1.",null,"1.","1.","1.","1.","x","false","true"));

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//OffsetScan scan=new OffsetScan(PointingBlock.dateToZulu(new Date()),"1","1","0.","0.","1.36","0.",null,"0.01","1.","1.","1.","y","false","false");
		//scan.set
		//vOffSet.add(scan);
		//createNewFakePTR(vector,vOffSet);
		insertInPTSL(vector,vOffSet);
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
				System.out.println(PointingBlock.dateToZulu(new Date(offsetstart)));
				if (!offset.getAttribute("ref").getValue().equals("fixed")) offset.setStartTime(PointingBlock.dateToZulu(new Date(offsetstart)));
				
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
	
	public static void insertInPTSL(Vector<PointingAttitude> vector,Vector<OffsetAngles> vOffSet){
		Ptr orPtr=PtrUtils.readPTRfromFile("/Users/jarenas 1/OPS/ROS_SGS/PLANNING/LTP002/LTP002A/MTP007A/PTR/PTRM_DM_008_01____Ab_00014.ROS");
		PtrSegment orSeg = orPtr.getSegment("MTP_007");
		//System.out.println(orSeg.toXml(0));
		PointingBlock[] orBlocks = orSeg.getAllBlocksOfType("OBS");
		long max_duration=2100000L;
		for (int i=0;i<orBlocks.length;i++){
			if (orBlocks[i].getDuration()>max_duration){
				orSeg.removeBlock(orBlocks[i]);
				int nofNewBlocks=new Long(orBlocks[i].getDuration()/max_duration).intValue();
				long intialStartDate=(orBlocks[i].getStartTime().getTime());
				for (int j=0;j<nofNewBlocks;j++){
					PointingBlock newBlock = new PointingBlock("OBS",new Date(intialStartDate),new Date(intialStartDate+max_duration));
					PointingAttitude newAtt = new PointingAttitude("track");
					newBlock.addChild(newAtt);
					orSeg.addBlock(newBlock);
					PointingBlockSlew slew = new PointingBlockSlew();
					System.out.println("Inserting block");
					slew.setBlockBefore(newBlock);
					//System.out.println("Inserting slew");
					//orSeg.addBlock(slew);
					
					intialStartDate=intialStartDate+max_duration+300000L;
				}
			}
		}
		//System.out.println(orSeg.toXml(0));
		orBlocks = orSeg.getAllBlocksOfType("OBS");
		int locator=0;
		//long startTime=1404282900000L;
		//long duration=2100000L;
		//long slewDuration=300000L;
		PointingBlock block;
		Iterator<PointingAttitude> it = vector.iterator();
		while (it.hasNext()){
			PointingAttitude at=it.next().copy();
			long startTime=orBlocks[locator].getStartTime().getTime();
			long duration=orBlocks[locator].getEndTime().getTime()-orBlocks[locator].getStartTime().getTime();
			block=new PointingBlock("OBS",new Date(startTime),new Date(startTime+duration));
			block.setAttitude(at);
			orSeg.removeBlock(orBlocks[locator]);
			orSeg.addBlock(block);
			//System.out.println(block.toXml(0));
			PointingBlockSlew slew = new PointingBlockSlew();
			slew.setBlockBefore(block);
			orSeg.addBlock(slew);
			//segment.addBlock(block);
			//print(block.toXml(0));
			//segment.addBlock(new PointingBlockSlew());
			//print(new PointingBlockSlew().toXml(0));
			//startTime=startTime+duration+slewDuration;
			locator++;

			Iterator<OffsetAngles> it2 = vOffSet.iterator();
			while (it2.hasNext()){
				startTime=orBlocks[locator].getStartTime().getTime();
				duration=orBlocks[locator].getEndTime().getTime()-orBlocks[locator].getStartTime().getTime();
				PointingAttitude at2=at.copy();
				OffsetAngles offset=it2.next().copy();
				long offsetstart=startTime+(duration/4);
				//System.out.println(PointingBlock.dateToZulu(new Date(offsetstart)));
				if (!offset.getAttribute("ref").getValue().equals("fixed")) offset.setStartTime(PointingBlock.dateToZulu(new Date(offsetstart)));
				
				at2.SetOffset(new OffsetRefAxis(), offset);
				PointingBlock block2=new PointingBlock("OBS",new Date(startTime),new Date(startTime+duration));
				block2.setAttitude(at2);
				//System.out.println(block2.toXml(0));
				//segment.addBlock(block);
				orSeg.removeBlock(orBlocks[locator]);
				orSeg.addBlock(block2);
				//print(block.toXml(0));
				//print(new PointingBlockSlew().toXml(0));
				//segment.addBlock(new PointingBlockSlew());
				//startTime=startTime+duration+slewDuration;
				locator++;
			}
			//System.out.println(block.toXml(0));

			//System.out.println(orPtr.toXml());
			orSeg.repairMocm();
			orSeg.repairSlews();
			Ptr ptr = new Ptr();
			ptr.addSegment(orSeg);
			PtrUtils.writePTRtofile("Z:\\MAPPS\\testptrall-007.xml", ptr);
		}


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
