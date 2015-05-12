package vega.uplink.planning.gui;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


import vega.uplink.DateUtil;
import vega.uplink.Properties;
import vega.uplink.commanding.AbstractSequence;
import vega.uplink.commanding.HistoryModes;
import vega.uplink.commanding.ModelState;
import vega.uplink.commanding.Orcd;
import vega.uplink.commanding.Por;
import vega.uplink.planning.Observation;
import vega.uplink.planning.ObservationChangeEvent;
import vega.uplink.planning.ObservationListener;
import vega.uplink.planning.Schedule;
import vega.uplink.pointing.PointingBlock;
import vega.uplink.pointing.Ptr;
import de.jaret.util.date.Interval;
import de.jaret.util.date.JaretDate;
import de.jaret.util.ui.timebars.model.DefaultRowHeader;
import de.jaret.util.ui.timebars.model.DefaultTimeBarModel;
import de.jaret.util.ui.timebars.model.DefaultTimeBarRowModel;
import de.jaret.util.ui.timebars.model.TimeBarRow;

public class ScheduleModel extends DefaultTimeBarModel implements ObservationListener{
	Schedule schedule;
	protected ScheduleViewer viewer;
	private final Logger LOG = Logger.getLogger(ScheduleModel.class.getName());
	boolean initialized=false;
	boolean recalculate=true;
	public ScheduleModel(Schedule schedule){
		super();
		viewer=null;
		this.schedule=schedule;
		
		Por p = schedule.getPor();
		JaretDate.setJaretDateFormatter(new DateFormatter());
		schedule.addObservationListener(this);
    	
    	DefaultRowHeader maintenance = new DefaultRowHeader("PTSL");
        DefaultRowHeader schObs = new DefaultRowHeader("SCH OBS");
        DefaultRowHeader ptr = new DefaultRowHeader("PTR");
        DefaultTimeBarRowModel tbr1 = new DefaultTimeBarRowModel(maintenance);
        String[] MAINTENANCE={PointingBlock.TYPE_MNAV,PointingBlock.TYPE_MOCM,PointingBlock.TYPE_MSLW,PointingBlock.TYPE_MWAC,PointingBlock.TYPE_MWNV,PointingBlock.TYPE_MWOL};
        for (int i=0;i<MAINTENANCE.length;i++){
          	PointingBlock[] blocks = schedule.getPtslSegment().getAllBlocksOfType(MAINTENANCE[i]).getBlocks();
        	for (int j=0;j<blocks.length;j++){
        		tbr1.addInterval(new BlockInterval(blocks[j]));
        	}

            
        }
        
    	
     	
      	PointingBlock[] blocks = schedule.getPtslSegment().getAllBlocksOfType(PointingBlock.TYPE_OBS).getBlocks();
    	for (int j=0;j<blocks.length;j++){
    		tbr1.addInterval(new BlockInterval(blocks[j]));
    	}
    	DefaultTimeBarRowModel tbr9 = new DefaultTimeBarRowModel(schObs);
    	Observation[] observations = schedule.getObservationsSchedule().getObservations();
    	for (int k=0;k<observations.length;k++){
    		ObservationInterval oi = new ObservationInterval(observations[k]);
    		oi.addObservationListener(this);
    		tbr9.addInterval(oi);
    	}
    	DefaultTimeBarRowModel tbr2 = new DefaultTimeBarRowModel(ptr);
    	Ptr ptrSegment = schedule.getPtr();
    	PointingBlock[] ptrBlocks = ptrSegment.getAllBlocks();
    	for (int b=0;b<ptrBlocks.length;b++){
    		tbr2.addInterval(new BlockInterval(ptrBlocks[b]));
    	}
        this.addRow(tbr1);
        this.addRow(tbr9);
        this.addRow(tbr2);
		DefaultTimeBarRowModel[] newRows=this.createSampleDataset(this.getHistoryModes());
		for (int i=0;i<newRows.length;i++){
			this.addRow(newRows[i]);
		}
        initialized=true;

	}
	
	public void addObservation(Observation obs){
		schedule.addObservation(obs);
		if (viewer!=null) viewer.list.setSelectedValue(obs, true);
	}
	
	public de.jaret.util.date.JaretDate getMaxDate(){
		return new JaretDate(schedule.getPtslSegment().getEndDate().toDate());
	}
	
	public de.jaret.util.date.JaretDate getMinDate(){
		return new JaretDate(schedule.getPtslSegment().getStartDate().toDate());
	}

	@Override
	public void observationChanged(ObservationChangeEvent event) {
		if (!initialized) return;

    	
		
	}
	public void recualculatePtr(){
		if (!recalculate) return;
		LOG.info("Start recalculating PTR");
		DefaultTimeBarRowModel ptrRow = ((DefaultTimeBarRowModel) this.getRow(2));
		List<Interval> iv = ptrRow.getIntervals();
		ptrRow.remIntervals(new java.util.Vector<Interval>(iv));
    	Ptr ptrSegment = schedule.getPtr();
    	PointingBlock[] ptrBlocks = ptrSegment.getAllBlocks();
    	for (int b=0;b<ptrBlocks.length;b++){
    		ptrRow.addInterval(new BlockInterval(ptrBlocks[b]));
    	}
    	LOG.info("End recalculating PTR");
	}
	@Override
	public void scheduleChanged() {
		try{
			DefaultTimeBarRowModel schRow = ((DefaultTimeBarRowModel) this.getRow(1));
			List<Interval> iv = schRow.getIntervals();
			schRow.remIntervals(new java.util.Vector<Interval>(iv));
	    	Observation[] observations = schedule.getObservationsSchedule().getObservations();
	    	for (int k=0;k<observations.length;k++){
	    		ObservationInterval oi = new ObservationInterval(observations[k]);
	    		oi.addObservationListener(this);
	    		schRow.addInterval(oi);
	    	}
	    	recualculatePtr();
	    	commandingChanged(null);

		}catch (Exception e){
			e.printStackTrace();
			LOG.throwing(ScheduleModel.class.getName(), "scheduleChanged", e);
			throw e;
		}
		
	}

	@Override
	public void metadataChanged(ObservationChangeEvent event) {
		commandingChanged(event);
		pointingChanged(event);
		
	}

	@Override
	public void pointingChanged(ObservationChangeEvent event) {
		LOG.info("Observation pointing Changed");
		if (initialized){
			try{
				initialized=false;
				recualculatePtr();
		    	initialized=true;
			}catch (Exception e){
				e.printStackTrace();
	
				throw e;
			}
		}
		
	}

	@Override
	public void commandingChanged(ObservationChangeEvent event) {
		LOG.info("Listenerd change in pointing");
		if (!recalculate) return;
		if (initialized){
			try{
				initialized=false;
				int rowCount = this.getRowCount();
				for (int i=rowCount-1;i>2;i--){
					TimeBarRow row = this.getRow(i);
					this.remRow(row);
				}
				DefaultTimeBarRowModel[] newRows=this.createSampleDataset(this.getHistoryModes());
				for (int i=0;i<newRows.length;i++){
					this.addRow(newRows[i]);
				}

		    	initialized=true;
			}catch (Exception e){
				e.printStackTrace();
	
				throw e;
			}
		}
		
	}
    private HistoryModes getHistoryModes(){
    	HistoryModes result = new HistoryModes();
    	ModelState modelState= new ModelState();
    	
    	AbstractSequence[] seqs = schedule.getPor().getSequences();
		Orcd orcd=Orcd.getOrcd();
		for (int i=0;i<seqs.length;i++){
			HashMap<Long,String> newmodes=orcd.getModesAsHistory(seqs[i].getName(),seqs[i].getExecutionDate().getTime());
			result.putAll(newmodes, seqs[i].getName(), seqs[i].getExecutionDate().getTime());

		}
		String[] states = modelState.getSubsystemNames();
		long startTime = this.schedule.getPtslSegment().getSegmentStartDate().getTime();
		HashMap<Long, String> hm = new HashMap<Long,String>();
		for (int i=0;i<states.length;i++){
			hm.put(startTime, states[i]+"Off");
		}
		result.putAll(hm, "init", startTime);
		long[] times=result.getTimes();
		for (int i=0;i<times.length;i++){
			long j=times[i];
			String oldmode=modelState.getStateForMode(result.get(j));
			String newmode=result.get(j);
			if (!orcd.checkTransion(oldmode, newmode)){
				String mess="Forbidden transition "+oldmode+"--->"+newmode+" at "+DateUtil.dateToZulu(new Date(j))+" via "+result.getCommand(j)+" executed at "+DateUtil.dateToZulu(new Date(result.getOriginalTime(j)));
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, mess);
			}
			modelState.setState(newmode);
			result.addStates(j,modelState.clone());
			
		}
		long endTime = this.schedule.getPtslSegment().getSegmentEndDate().getTime();
		String[] as = modelState.getAllStates();
		HashMap<Long, String> hm2 = new HashMap<Long,String>();
		for (int i=0;i<as.length;i++){
			hm2.put(endTime, as[i]);
		}
		result.putAll(hm2, "end", endTime);
		result.addStates(endTime,modelState.clone());
    	return result;
    	
    }
	
	private DefaultTimeBarRowModel[] createSampleDataset(HistoryModes hm) {
		HashMap<String, String> instrumentsStartString = new HashMap<String,String>();
		List<String> insList = Properties.getList(Properties.INSTRUMENT_NAMES_PROPERTIES);
		Iterator<String> it = insList.iterator();
		while(it.hasNext()){
			String ins=it.next();
			List<String> beg=Properties.getList(Properties.SUBINSTRUMENT_MODE_START_PROPERTY_PREFIX+ins);
			Iterator<String> it2 = beg.iterator();
			while (it2.hasNext()){
				String begString=it2.next();
				instrumentsStartString.put(begString, ins);
			}
		}
		HashMap<String,DefaultTimeBarRowModel> taskVectorMap=new HashMap<String,DefaultTimeBarRowModel>();
		List<String> insList2 = Properties.getList(Properties.INSTRUMENT_NAMES_PROPERTIES);
		Iterator<String> it2 = insList2.iterator();
		while (it2.hasNext()){
			String ins=it2.next();
			if (!ins.equals("PTR"))taskVectorMap.put(ins, new DefaultTimeBarRowModel( new DefaultRowHeader(ins)));
		}

        
        String[] modes=hm.getAllStates();
        /*for (int i=0;i<modes.length;i++){
    		
        	//System.out.println("To add "+modes[i]);
        }*/
        for (int i=0;i<modes.length;i++){
        	if (!modes[i].endsWith("Off") && !modes[i].endsWith("Closed")){	
	        	java.util.Date[] dates=hm.getBoundarieDatesForState(modes[i]);
	        	String instrument="";
	        	Iterator<String> it3 = instrumentsStartString.keySet().iterator();
	        	while(it3.hasNext()){
	        		String stString=it3.next();
	        		if (modes[i].startsWith(stString)){
	        			instrument=instrumentsStartString.get(stString);
	        		}

	        	}
	        	DefaultTimeBarRowModel task = taskVectorMap.get(instrument);
	        	for (int j=0;j<dates.length;j=j+2){
	        		CommandingInterval interval= new CommandingInterval(modes[i],instrument,dates[j],dates[j+1]);
	        		try{
	        			task.addInterval(interval);
	        		}catch (NullPointerException e){
	        			IllegalArgumentException iae = new IllegalArgumentException("Unknown instrument "+instrument);
	        			iae.initCause(e);
	        			throw(iae);
	        		}
	        	}


        	
        	}
        }
        DefaultTimeBarRowModel[] result=new DefaultTimeBarRowModel[taskVectorMap.size()];
        result=taskVectorMap.values().toArray(result);
        return result;
	}


}
