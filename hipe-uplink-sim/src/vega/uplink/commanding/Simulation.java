package vega.uplink.commanding;
import java.awt.Color;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;

import herschel.ia.numeric.*;
import herschel.ia.gui.plot.*;

import org.jfree.ui.RefineryUtilities;

import vega.hipe.logging.VegaLog;
import vega.uplink.DateUtil;
import vega.uplink.Properties;
import vega.uplink.commanding.gui.HistoryModesPlot;
import vega.uplink.commanding.itl.*;
import vega.uplink.track.GsPass;

import java.util.Date;

import javax.swing.JFrame;
/**
 * 
 * @author jarenas
 *
 */
public class Simulation {
	SimulationContext context;
	//private static final Logger LOG = Logger.getLogger(Simulation.class.getName());
	private Simulation(){
		context=new SimulationContext();
	}
	public Simulation(SimulationContext context){
		this.context=context;
	}

	public Simulation(Por[] pors){
		this();
		addPors(pors);
		
		
	}
	public Simulation(Por[] pors,SimulationContext simContext){
		this(simContext);
		addPors(pors);
		
		
	}
	
	private void addPors(Por[] pors){
		for (int i=0;i<pors.length;i++){
			context.getPor().addPor(pors[i]);
		}
		
	}
	public SimulationContext runSimulation(){
		return run(false,false);
	}
	public SimulationContext runOnlyText(boolean printStrategy){
		return run(false,true);
	}
	public SimulationContext run(boolean printStrategy,boolean onlyText){
		if (!context.getInitScript().equals("")){
			try{
				VegaLog.info("Executing init script "+context.getInitScript());
				herschel.ia.jconsole.jython.Interpreter.getInterpreter().set("simulationContext", context);
				herschel.ia.jconsole.jython.Interpreter.getInterpreter().getPythonInterpreter().execfile(context.getInitScript());
				context=herschel.ia.jconsole.jython.Interpreter.getInterpreter().getPythonInterpreter().get("simulationContext", SimulationContext.class);

			}catch (Exception e){
				e.printStackTrace();
				VegaLog.throwing(Simulation.class, "run", e);
				VegaLog.severe("Error executing init script:"+e.getMessage());
				context.log(e.getMessage());
			}
		}
		AbstractSequence[] seqs=context.getPor().getOrderedSequences();
		SequenceTimeline seqTimeline=context.getSequenceTimeline();
		VegaLog.info("Inserting commands into the model");
		boolean osirisScience=true;
		for (int i=0;i<seqs.length;i++){
			seqTimeline.execute(seqs[i]);
			if (seqs[i].getInstrument().equals("ANTENNA")){
				context.getMemorySimulator().addSequence(seqs[i]);
			}
			if (seqs[i].getName().equals("ASRF071B")){
				osirisScience=false;
			}
			if (seqs[i].getName().equals("ASRF071A")){
				osirisScience=true;
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
						if (seqs[i].getInstrument().equals("OSIRIS")){
							if (osirisScience){
								context.getMemorySimulator().addAction(seqs[i].getInstrument(), new Date(seqs[i].getExecutionDate().getTime()+(profiles[j].getOffSetSeconds()*1000)), new Float(profiles[j].getValue()));
							}
						}else{
							context.getMemorySimulator().addAction(seqs[i].getInstrument(), new Date(seqs[i].getExecutionDate().getTime()+(profiles[j].getOffSetSeconds()*1000)), new Float(profiles[j].getValue()));
						}
					}
				}
			}
		}
		context.log(seqTimeline.findAllOverlapping());
		java.util.Date start=context.getPor().getValidityDates()[0];
		java.util.Date end=context.getPor().getValidityDates()[1];
		VegaLog.info("Inserting the GS passes from the FECS into the model");
		TreeSet<GsPass> passes=context.getFecs().getPasses();
		Iterator<GsPass> it = passes.iterator();
		String strategy="";
		while (it.hasNext()){
			GsPass pass=it.next();
			if (pass.getStartPass().after(start) && pass.getStartPass().before(end)){
				
				strategy=strategy+context.getMemorySimulator().addGsPass(pass);
			}
		}
		if (printStrategy) context.log(strategy);
		
		long[] timess=context.getHistoryModes().getTimes();
		java.util.Vector<Long> temp=new java.util.Vector<Long>();
		for (int i=0;i<timess.length;i++){
			if (timess[i]>=start.getTime() && timess[i]<=end.getTime()){
				temp.add(timess[i]);
			}
		}
		Long[] times=new Long[temp.size()];
		temp.toArray(times);
		VegaLog.info("Calculating mode transitions");
		for (int i=0;i<times.length;i++){
			long j=times[i];
			String oldmode=context.getModelState().getStateForMode(context.getHistoryModes().get(j));
			String newmode=context.getHistoryModes().get(j);
			if (!context.getOrcd().checkTransion(oldmode, newmode)){
				String mess="Forbidden transition "+oldmode+"--->"+newmode+" at "+DateUtil.dateToZulu(new Date(j))+" via "+context.getHistoryModes().getCommand(j)+" executed at "+DateUtil.dateToZulu(new Date(context.getHistoryModes().getOriginalTime(j)));
				context.log(mess);
				VegaLog.severe( mess);
			}
			context.getModelState().setState(newmode);
			context.getHistoryModes().addStates(j,context.getModelState().clone());
			context.getExecutionDates().append(j);
			float modelPower = context.getOrcd().getTotalPowerForModes(context.getModelState().getAllStates());
			context.getHistoryPower().append(modelPower);
			float mPower = context.getMocPower().getPowerAt(new Date(j));
			context.getMocPowerHistory().append(mPower);
			if (modelPower>mPower){
				String mess="ALARM: Power over due via sequence "+context.getHistoryModes().getCommand(j)+" executed at "+DateUtil.dateToZulu(new Date(context.getHistoryModes().getOriginalTime(j)));
				context.log(mess);
				VegaLog.severe( mess);

			}
			
		}

		VegaLog.info("Simulating SSMM");
		String[] instruments=context.getMemorySimulator().getAllInstruments();
		PlotXY plot3=null;
		PlotXY plot4=null;
		if (!onlyText){
			plot3=new PlotXY();
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
			try {
			    plot3.getLayer(0).setXAxisType(herschel.ia.gui.plot.renderer.axtype.AxisType.DATE);
			}catch (Exception e) {
			    VegaLog.severe(e.getMessage());
			    e.printStackTrace();
			}
			plot3.getLegend().setVisible(true);
			plot4=new PlotXY();
		}
		for (int i=0;i<instruments.length;i++){
			long packetStoreSize;
			try{
				packetStoreSize=Long.parseLong(Properties.getProperty(Properties.SSMM_PACKETSTORE_PREFIX+instruments[i]));
			}catch (herschel.share.util.ConfigurationException e){
				packetStoreSize=104857600;
			}

			Float[] array = context.getMemorySimulator().getValuesAt(instruments[i], times);
			Float1d values=new Float1d();
			boolean inMemoryFull=false;
			String mess="";
			for (int j=0;j<array.length;j++){
				if (array[j]==packetStoreSize){
					if (!inMemoryFull){
						String mess2="Packet store for instrument "+instruments[i]+" full at "+DateUtil.dateToZulu(new java.util.Date(times[j]));
						if (!mess.equals(mess2)){
							mess=mess2;
							context.log(mess);
							VegaLog.severe( mess);

						}
						
					}
						
					inMemoryFull=true;
				}else{
					inMemoryFull=false;
				}
				values.append(array[j].floatValue());
				
			}
			if (!onlyText){
				VegaLog.info("Generating power plots");

			
				Color color=Color.BLACK;
				try{
					color = Properties.getColor(Properties.SUBINSTRUMENT_COLOR_PROPERTY_PREFIX+instruments[i]);
				}catch (Exception ce){
					ce.printStackTrace();context.log(ce.getMessage());
				}
				LayerXY tempLayer=new LayerXY(context.getMemorySimulator().toLong1d(times),values);
				tempLayer.setColor(color);
				tempLayer.setName(instruments[i]);
				plot4.addLayer(tempLayer);
				if (instruments.length>0){
					plot4.getXaxis().setTitleText("Time");
					plot4.getYaxis().setTitleText("Data (bits)");
					try {
					    plot4.getLayer(0).setXAxisType(herschel.ia.gui.plot.renderer.axtype.AxisType.DATE);
					}catch (Exception e) {
					    VegaLog.severe(e.getMessage());
					    e.printStackTrace();
					}
					plot4.getLegend().setVisible(true);
				}

			}
		}
		if (!onlyText){
			HistoryModesPlot plot = new vega.uplink.commanding.gui.HistoryModesPlot("Timeline",context.getHistoryModes());
			JFrame frame=new JFrame("Timeline");
			frame.setContentPane(plot);
			plot.setPreferredSize(new java.awt.Dimension(500, 270));
			frame.pack();
			RefineryUtilities.centerFrameOnScreen(frame);
	
			frame.setVisible(true);
		}
		VegaLog.info("Executing post script");
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
	
	public Simulation(String itlFile,String evtFile) throws java.text.ParseException, IOException{
		this();
		String defaultDirectory=Properties.getProperty(Properties.DEFAULT_EVT_DIRECTORY);
		Por[] pors=new Por[1];
		pors[0] = ItlParser.parseItl(itlFile, evtFile, defaultDirectory, 1);
		addPors(pors);
	}
}
