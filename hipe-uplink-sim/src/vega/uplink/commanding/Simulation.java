package vega.uplink.commanding;
import java.awt.Color;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;

import herschel.ia.numeric.*;
import herschel.ia.gui.plot.*;

import org.jfree.ui.RefineryUtilities;

import vega.uplink.Properties;
import vega.uplink.commanding.gui.HistoryModesPlot;
import vega.uplink.commanding.itl.*;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
public class Simulation {
	java.text.SimpleDateFormat dateFormat2;
	
	private Simulation(){
		dateFormat2 = new java.text.SimpleDateFormat("dd-MMM-yyyy'_'HH:mm:ss");
		dateFormat2.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
		
	}
	public Simulation(Por[] pors){
		this();
		addPors(pors);
		
		
	}
	
	private void addPors(Por[] pors){
		for (int i=0;i<pors.length;i++){
			SimulationContext.getInstance().sp.addPor(pors[i]);
		}
		
	}
	public String run(){
		return run(false);
	}
	public String run(boolean printStrategy){
		
		Sequence[] seqs=SimulationContext.getInstance().sp.getOrderedSequences();
		SsmmSimulator memorySimulator=SimulationContext.getInstance().ssmm;
		String messages="";
		for (int i=0;i<seqs.length;i++){
			//if (seqs[i].getName())
			if (seqs[i].getInstrument().equals("ANTENNA")){
				//System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
				memorySimulator.addSequence(seqs[i]);
			}
			HashMap<Long,String> newmodes=SimulationContext.getInstance().orcd.getModesAsHistory(seqs[i].getName(),seqs[i].getExecutionDate().getTime());
			SimulationContext.getInstance().historyModes.putAll(newmodes, seqs[i].getName(), seqs[i].getExecutionDate().getTime());
			SequenceProfile[] profiles=seqs[i].getProfiles();
			if (profiles!=null){
				for (int j=0;j<profiles.length;j++){
					if (profiles[j].getType().equals("PW")){
						SimulationContext.getInstance().powerInstrument.setPower(seqs[i].getInstrument(), new Float(profiles[j].getValue()));
						SimulationContext.getInstance().zRecordDates.append(seqs[i].getExecutionDate().getTime()+(profiles[j].getOffSetSeconds()*1000));
						SimulationContext.getInstance().historyPowerZ.append(SimulationContext.getInstance().powerInstrument.getTotalPower());
					}
					if (profiles[j].getType().equals("DR")){
						memorySimulator.addAction(seqs[i].getInstrument(), new Date(seqs[i].getExecutionDate().getTime()+(profiles[j].getOffSetSeconds()*1000)), new Float(profiles[j].getValue()));
						//System.out.println("Detected Z record. Inserted action in memorySimulator");
					}
				}
			}
		}
		java.util.Date start=SimulationContext.getInstance().sp.getValidityDates()[0];
		java.util.Date end=SimulationContext.getInstance().sp.getValidityDates()[1];
		
		TreeSet<GsPass> passes=SimulationContext.getInstance().fecs.getPasses();
		Iterator<GsPass> it = passes.iterator();
		String strategy="";
		while (it.hasNext()){
			GsPass pass=it.next();
			if (pass.getStartPass().after(start) && pass.getStartPass().before(end)){
				
				strategy=strategy+memorySimulator.addGsPass(pass);
			}
			//messages=messages+memorySimulator.addGsPass(pass);
		}
		if (printStrategy) messages=messages+strategy;
		
		//System.out.println("Run full simulation:");
		//Synchronize()
		//boolean r=memorySimulator.runSimulation(SimulationContext.getInstance().sp.getValidityDates()[1]);
		long[] timess=SimulationContext.getInstance().historyModes.getTimes();
		java.util.Vector<Long> temp=new java.util.Vector<Long>();
		for (int i=0;i<timess.length;i++){
			if (timess[i]>=start.getTime() && timess[i]<=end.getTime()){
				temp.add(timess[i]);
			}
		}
		Long[] times=new Long[temp.size()];
		temp.toArray(times);
		
		for (int i=0;i<times.length;i++){
			long j=times[i];
			String oldmode=SimulationContext.getInstance().ms.getStateForMode(SimulationContext.getInstance().historyModes.get(j));
			String newmode=SimulationContext.getInstance().historyModes.get(j);
			if (!SimulationContext.getInstance().orcd.checkTransion(oldmode, newmode)){
				String mess="Forbidden transition "+oldmode+"--->"+newmode+" at "+dateFormat2.format(new Date(j))+" via "+SimulationContext.getInstance().historyModes.getCommand(j)+" executed at "+dateFormat2.format(new Date(SimulationContext.getInstance().historyModes.getOriginalTime(j)))+"\n";
				messages=messages+mess;
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, mess);
			}
			SimulationContext.getInstance().ms.setState(newmode);
			SimulationContext.getInstance().historyModes.addStates(j,SimulationContext.getInstance().ms.clone());
			SimulationContext.getInstance().executionDates.append(j);
			float modelPower = SimulationContext.getInstance().orcd.getTotalPowerForModes(SimulationContext.getInstance().ms.getAllStates());
			SimulationContext.getInstance().historyPower.append(modelPower);
			float mPower = SimulationContext.getInstance().mocPower.getPowerAt(new Date(j));
			SimulationContext.getInstance().mocPowerHistory.append(mPower);
			if (modelPower>mPower){
				String mess="ALARM: Power over due via sequence "+SimulationContext.getInstance().historyModes.getCommand(j)+" executed at "+dateFormat2.format(new Date(SimulationContext.getInstance().historyModes.getOriginalTime(j)))+"\n";
				messages=messages+mess;
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, mess);

			}
			
		}
		
		
		PlotXY plot3=new PlotXY();
		LayerXY layer6 = new LayerXY(SimulationContext.getInstance().executionDates,SimulationContext.getInstance().historyPower);
		layer6.setColor(java.awt.Color.BLUE);
		LayerXY layer7 = new LayerXY(SimulationContext.getInstance().zRecordDates,SimulationContext.getInstance().historyPowerZ);
		layer7.setColor(java.awt.Color.GREEN);
		LayerXY layer8 = new LayerXY(SimulationContext.getInstance().executionDates,SimulationContext.getInstance().mocPowerHistory);
		layer8.setColor(java.awt.Color.RED);
		plot3.addLayer(layer6);
		plot3.addLayer(layer7);
		plot3.addLayer(layer8);
		plot3.setTitleText("ORCD/Z records (ITL)");
		layer7.setName("Power from Z records");
		layer6.setName("Power from ORCD");
		layer8.setName("Allowed power from moc");
		plot3.getXaxis().setTitleText("Time");
		plot3.getYaxis().setTitleText("Power (Watts)");
		plot3.getLayer(0).setXAxisType(herschel.ia.gui.plot.renderer.axtype.AxisType.DATE);
		plot3.getLegend().setVisible(true);
		
		PlotXY plot4=new PlotXY();
		//LayerXY layerTotalMemory=new LayerXY(new Long1d(SimulationContext.getInstance().ssmmHistory.getAllTimesAsLongArray()),new Float1d(SimulationContext.getInstance().ssmmHistory.getAllTotalPower()));

		/*LayerXY layerTotalMemory=new LayerXY(memorySimulator.toLong1d(times),memorySimulator.toFloat1d(memorySimulator.getAllMemoryAt(times)));

		layerTotalMemory.setColor(java.awt.Color.BLUE);
		layerTotalMemory.setName("Total SSMM");*/
		/*Long1d limitTimes=new Long1d();
		limitTimes.append(times[0]);
		limitTimes.append(times[times.length-1]);
		Float1d limitValues=new Float1d();
		limitValues.append(104857600);
		limitValues.append(104857600);
		
		LayerXY layerLimitMemory=new LayerXY(limitTimes,limitValues);

		layerLimitMemory.setColor(java.awt.Color.RED);
		layerLimitMemory.setName("Packet Store limit");*/

		String[] instruments=memorySimulator.getAllInstruments();
		for (int i=0;i<instruments.length;i++){
			long packetStoreSize;
			try{
				packetStoreSize=Long.parseLong(Properties.getProperty(Properties.SSMM_PACKETSTORE_PREFIX+instruments[i]));
			}catch (herschel.share.util.ConfigurationException e){
				//e.printStackTrace();
				packetStoreSize=104857600;
			}

			//LayerXY tempLayer=new LayerXY(new Long1d(SimulationContext.getInstance().ssmmHistory.getAllTimesAsLongArray()),new Float1d(SimulationContext.getInstance().ssmmHistory.getAllPower(instruments[i])));
			//LayerXY tempLayer=new LayerXY(memorySimulator.getAllTimesLong1d(instruments[i]),memorySimulator.geatAllMemoryFloat1d(instruments[i]));
			Float[] array = memorySimulator.getValuesAt(instruments[i], times);
			Float1d values=new Float1d();
			boolean inMemoryFull=false;
			String mess="";
			for (int j=0;j<array.length;j++){
				if (array[j]==packetStoreSize){
					if (!inMemoryFull){
						String mess2="Packet store for instrument "+instruments[i]+" full at "+dateFormat2.format(new java.util.Date(times[j]))+"\n";
						if (!mess.equals(mess2)){
							mess=mess2;
							messages=messages+mess;
							Logger.getLogger(getClass().getName()).log(Level.SEVERE, mess);

						}
						
					}
						
					inMemoryFull=true;
				}else{
					//System.out.println("Resetting memory warning because value is "+array[j]+ " for instrument "+instruments[i]);
					inMemoryFull=false;
					//mess="";
				}
				values.append(array[j].floatValue());
				
			}

			//Float1d values = memorySimulator.toFloat1d(memorySimulator.getValuesAt(instruments[i], times));
			//System.out.println(values);
			Color color = Properties.getColor(Properties.SUBINSTRUMENT_COLOR_PROPERTY_PREFIX+instruments[i]);
			LayerXY tempLayer=new LayerXY(memorySimulator.toLong1d(times),values);
			tempLayer.setColor(color);
			tempLayer.setName(instruments[i]);
			//if (!instruments[i].equals("ANTENNA") && !instruments[i].equals("PTR")) plot4.addLayer(tempLayer);
			plot4.addLayer(tempLayer);
		}

		//plot4.addLayer(layerTotalMemory);
		//plot4.addLayer(layerLimitMemory);

		plot4.getXaxis().setTitleText("Time");
		plot4.getYaxis().setTitleText("Date (bits)");
		plot4.getLayer(0).setXAxisType(herschel.ia.gui.plot.renderer.axtype.AxisType.DATE);
		plot4.getLegend().setVisible(true);
		
		HistoryModesPlot plot = new vega.uplink.commanding.gui.HistoryModesPlot("Time line",SimulationContext.getInstance().historyModes);
		JFrame frame=new JFrame("Time line");
		frame.setContentPane(plot);
		plot.setPreferredSize(new java.awt.Dimension(500, 270));
		frame.pack();
		RefineryUtilities.centerFrameOnScreen(frame);

		frame.setVisible(true);
		return messages;
	}
	public Simulation(String[] porFiles){
		this();
		Por[] pors=new Por[porFiles.length];
		for (int i=0;i<porFiles.length;i++){
			pors[i]=PorUtils.readPORfromFile(porFiles[i]);
		}
		addPors(pors);
	}
	
	public Simulation(String por){
		this();
		Por[] pors=new Por[1];
		pors[0]=PorUtils.readPORfromFile(por);
		addPors((pors));
	}
	
	public Simulation(Por por){
		this();
		Por[] pors=new Por[1];
		pors[0]=por;
		addPors((pors));
		
	}
	
	public Simulation(String itlFile,String evtFile) throws java.text.ParseException{
		this();
		String defaultDirectory=Properties.getProperty(Properties.DEFAULT_EVT_DIRECTORY);
		//Por por;
		Por[] pors=new Por[1];
		//try{
			pors[0] = ItlParser.parseItl(itlFile, evtFile, defaultDirectory, 1);
			
		//}catch (Exception e){
			//pors[0]=new Por();
			//e.printStackTrace();
		//}
		addPors(pors);
	}
	
	//Public Simulation(String[] itlFiles)
}
