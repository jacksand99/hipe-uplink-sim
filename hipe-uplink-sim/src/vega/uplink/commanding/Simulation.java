package vega.uplink.commanding;
import java.awt.Color;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;

import herschel.ia.numeric.*;
import herschel.ia.gui.plot.*;

import org.jfree.ui.RefineryUtilities;
//import org.jfree.util.Log;

import vega.uplink.Properties;
import vega.uplink.commanding.gui.HistoryModesPlot;
import vega.uplink.commanding.itl.*;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;



//import javax.naming.ConfigurationException;
import javax.swing.JFrame;
//import java.lang.Runnable ;
public class Simulation {
	java.text.SimpleDateFormat dateFormat2;
	SimulationContext context;
	private static final Logger LOG = Logger.getLogger(Simulation.class.getName());
	private Simulation(){
		context=new SimulationContext();
		dateFormat2 = new java.text.SimpleDateFormat("dd-MMM-yyyy'_'HH:mm:ss");
		dateFormat2.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
		
	}
	private Simulation(SimulationContext context){
		this.context=context;
		//System.out.println("Init script:"+this.context.getInitScript());
		dateFormat2 = new java.text.SimpleDateFormat("dd-MMM-yyyy'_'HH:mm:ss");
		dateFormat2.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
		
	}

	public Simulation(Por[] pors){
		this();
		//context=new SimulationContext();
		addPors(pors);
		
		
	}
	public Simulation(Por[] pors,SimulationContext simContext){
		this(simContext);
		//context=new SimulationContext();
		addPors(pors);
		
		
	}
	
	private void addPors(Por[] pors){
		for (int i=0;i<pors.length;i++){
			context.getPor().addPor(pors[i]);
		}
		
	}
	public SimulationContext runSimulation(){
		return run(false);
	}
	public SimulationContext run(boolean printStrategy){
		if (!context.getInitScript().equals("")){
			try{
				LOG.info("Executing init script "+context.getInitScript());
				herschel.ia.jconsole.jython.Interpreter.getInterpreter().set("simulationContext", context);
				herschel.ia.jconsole.jython.Interpreter.getInterpreter().getPythonInterpreter().execfile(context.getInitScript());
				context=herschel.ia.jconsole.jython.Interpreter.getInterpreter().getPythonInterpreter().get("simulationContext", SimulationContext.class);

			}catch (Exception e){
				e.printStackTrace();
				LOG.throwing("Simulation", "run", e);
				LOG.severe("Error executing init script:"+e.getMessage());
				context.log(e.getMessage());
			}
		}
		Sequence[] seqs=context.getPor().getOrderedSequences();
		//SsmmSimulator memorySimulator=context.ssmm;
		SsmmSimulator memorySimulator=new RosettaSsmmSimulator(context);
		LOG.info("Inserting commands into the model");
		//String messages="";
		for (int i=0;i<seqs.length;i++){
			//if (seqs[i].getName())
			if (seqs[i].getInstrument().equals("ANTENNA")){
				//System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
				memorySimulator.addSequence(seqs[i]);
			}
			HashMap<Long,String> newmodes=context.getOrcd().getModesAsHistory(seqs[i].getName(),seqs[i].getExecutionDate().getTime());
			context.getHistoryModes().putAll(newmodes, seqs[i].getName(), seqs[i].getExecutionDate().getTime());
			SequenceProfile[] profiles=seqs[i].getProfiles();
			if (profiles!=null){
				for (int j=0;j<profiles.length;j++){
					if (profiles[j].getType().equals("PW")){
						context.getPowerInstrument().setPower(seqs[i].getInstrument(), new Float(profiles[j].getValue()));
						context.getZRecordDates().append(seqs[i].getExecutionDate().getTime()+(profiles[j].getOffSetSeconds()*1000));
						context.getHistoryPowerZ().append(context.getPowerInstrument().getTotalPower());
					}
					if (profiles[j].getType().equals("DR")){
						memorySimulator.addAction(seqs[i].getInstrument(), new Date(seqs[i].getExecutionDate().getTime()+(profiles[j].getOffSetSeconds()*1000)), new Float(profiles[j].getValue()));
						//System.out.println("Detected Z record. Inserted action in memorySimulator");
					}
				}
			}
		}
		java.util.Date start=context.getPor().getValidityDates()[0];
		java.util.Date end=context.getPor().getValidityDates()[1];
		LOG.info("Inserting the GS passes from the FECS into the model");
		TreeSet<GsPass> passes=context.getFecs().getPasses();
		Iterator<GsPass> it = passes.iterator();
		String strategy="";
		while (it.hasNext()){
			GsPass pass=it.next();
			if (pass.getStartPass().after(start) && pass.getStartPass().before(end)){
				
				strategy=strategy+memorySimulator.addGsPass(pass);
			}
			//messages=messages+memorySimulator.addGsPass(pass);
		}
		if (printStrategy) context.log(strategy);
		
		//System.out.println("Run full simulation:");
		//Synchronize()
		//boolean r=memorySimulator.runSimulation(context.sp.getValidityDates()[1]);
		long[] timess=context.getHistoryModes().getTimes();
		java.util.Vector<Long> temp=new java.util.Vector<Long>();
		for (int i=0;i<timess.length;i++){
			if (timess[i]>=start.getTime() && timess[i]<=end.getTime()){
				temp.add(timess[i]);
			}
		}
		Long[] times=new Long[temp.size()];
		temp.toArray(times);
		LOG.info("Calculating mode transitions");
		for (int i=0;i<times.length;i++){
			long j=times[i];
			String oldmode=context.getModelState().getStateForMode(context.getHistoryModes().get(j));
			String newmode=context.getHistoryModes().get(j);
			if (!context.getOrcd().checkTransion(oldmode, newmode)){
				String mess="Forbidden transition "+oldmode+"--->"+newmode+" at "+dateFormat2.format(new Date(j))+" via "+context.getHistoryModes().getCommand(j)+" executed at "+dateFormat2.format(new Date(context.getHistoryModes().getOriginalTime(j)));
				//messages=messages+mess;
				context.log(mess);
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, mess);
			}
			context.getModelState().setState(newmode);
			context.getHistoryModes().addStates(j,context.getModelState().clone());
			context.getExecutionDates().append(j);
			float modelPower = context.getOrcd().getTotalPowerForModes(context.getModelState().getAllStates());
			context.getHistoryPower().append(modelPower);
			float mPower = context.getMocPower().getPowerAt(new Date(j));
			context.getMocPowerHistory().append(mPower);
			if (modelPower>mPower){
				String mess="ALARM: Power over due via sequence "+context.getHistoryModes().getCommand(j)+" executed at "+dateFormat2.format(new Date(context.getHistoryModes().getOriginalTime(j)));
				//messages=messages+mess;
				context.log(mess);
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, mess);

			}
			
		}
		
		LOG.info("Generating power plots");
		PlotXY plot3=new PlotXY();
		LayerXY layer6 = new LayerXY(context.getExecutionDates(),context.getHistoryPower());
		layer6.setColor(java.awt.Color.BLUE);
		LayerXY layer7 = new LayerXY(context.getZRecordDates(),context.getHistoryPowerZ());
		layer7.setColor(java.awt.Color.GREEN);
		LayerXY layer8 = new LayerXY(context.getExecutionDates(),context.getMocPowerHistory());
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
		//LayerXY layerTotalMemory=new LayerXY(new Long1d(context.ssmmHistory.getAllTimesAsLongArray()),new Float1d(context.ssmmHistory.getAllTotalPower()));

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
		LOG.info("Simulating SSMM");
		String[] instruments=memorySimulator.getAllInstruments();
		for (int i=0;i<instruments.length;i++){
			long packetStoreSize;
			try{
				packetStoreSize=Long.parseLong(Properties.getProperty(Properties.SSMM_PACKETSTORE_PREFIX+instruments[i]));
			}catch (herschel.share.util.ConfigurationException e){
				//e.printStackTrace();
				packetStoreSize=104857600;
			}

			//LayerXY tempLayer=new LayerXY(new Long1d(context.ssmmHistory.getAllTimesAsLongArray()),new Float1d(context.ssmmHistory.getAllPower(instruments[i])));
			//LayerXY tempLayer=new LayerXY(memorySimulator.getAllTimesLong1d(instruments[i]),memorySimulator.geatAllMemoryFloat1d(instruments[i]));
			Float[] array = memorySimulator.getValuesAt(instruments[i], times);
			Float1d values=new Float1d();
			boolean inMemoryFull=false;
			String mess="";
			for (int j=0;j<array.length;j++){
				if (array[j]==packetStoreSize){
					if (!inMemoryFull){
						String mess2="Packet store for instrument "+instruments[i]+" full at "+dateFormat2.format(new java.util.Date(times[j]));
						if (!mess.equals(mess2)){
							mess=mess2;
							context.log(mess);
							//messages=messages+mess;
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
			Color color=Color.BLACK;
			try{
				color = Properties.getColor(Properties.SUBINSTRUMENT_COLOR_PROPERTY_PREFIX+instruments[i]);
			}catch (Exception ce){
				ce.printStackTrace();context.log(ce.getMessage());
			}
			LayerXY tempLayer=new LayerXY(memorySimulator.toLong1d(times),values);
			tempLayer.setColor(color);
			tempLayer.setName(instruments[i]);
			//if (!instruments[i].equals("ANTENNA") && !instruments[i].equals("PTR")) plot4.addLayer(tempLayer);
			plot4.addLayer(tempLayer);
		}

		//plot4.addLayer(layerTotalMemory);
		//plot4.addLayer(layerLimitMemory);
		LOG.info("Generating Datarate plots");
		plot4.getXaxis().setTitleText("Time");
		plot4.getYaxis().setTitleText("Data (bits)");
		plot4.getLayer(0).setXAxisType(herschel.ia.gui.plot.renderer.axtype.AxisType.DATE);
		plot4.getLegend().setVisible(true);
		
		HistoryModesPlot plot = new vega.uplink.commanding.gui.HistoryModesPlot("Time line",context.getHistoryModes());
		JFrame frame=new JFrame("Time line");
		frame.setContentPane(plot);
		plot.setPreferredSize(new java.awt.Dimension(500, 270));
		frame.pack();
		RefineryUtilities.centerFrameOnScreen(frame);

		frame.setVisible(true);
		LOG.info("Executing post script");
		if (!context.getPostScript().equals("")){
			try{
				herschel.ia.jconsole.jython.Interpreter.getInterpreter().set("simulationContext", context);
				herschel.ia.jconsole.jython.Interpreter.getInterpreter().getPythonInterpreter().execfile(context.getPostScript());
				context=herschel.ia.jconsole.jython.Interpreter.getInterpreter().getPythonInterpreter().get("simulationContext", SimulationContext.class);
				herschel.ia.jconsole.jython.Interpreter.getInterpreter().exec("del(simulationContext)");
			}catch (Exception e){
				e.printStackTrace();
				context.log(e.getMessage());
			}
		}
		return context;
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
	public Simulation(String por,SimulationContext simContext){
		this(simContext);
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
	public Simulation(Por por,SimulationContext simContext){
		this(simContext);
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
	/*@Override
	public void run() {
		// TODO Auto-generated method stub
		runSimulation();
	}*/
	
	//Public Simulation(String[] itlFiles)
}
