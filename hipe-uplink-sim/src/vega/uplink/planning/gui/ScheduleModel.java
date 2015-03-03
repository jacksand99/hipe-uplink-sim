package vega.uplink.planning.gui;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jfree.data.category.IntervalCategoryDataset;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;

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
import vega.uplink.pointing.PtrSegment;
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
	//boolean STOP_LISTENING
	public ScheduleModel(Schedule schedule){
		super();
		viewer=null;
		//System.setProperty("user.timezone", "UTC");
		this.schedule=schedule;
		
		Por p = schedule.getPor();
		//System.out.println("Number of sequences:"+p.getSequences().length);
		//schedule.addObservationListener(this);
		JaretDate.setJaretDateFormatter(new DateFormatter());
		schedule.addObservationListener(this);
    	//DefaultTimeBarModel model = new DefaultTimeBarModel();
    	
    	DefaultRowHeader maintenance = new DefaultRowHeader("PTSL");
        //DefaultRowHeader obs = new DefaultRowHeader("PTSL "+PointingBlock.TYPE_OBS);
        DefaultRowHeader schObs = new DefaultRowHeader("SCH OBS");
        DefaultRowHeader ptr = new DefaultRowHeader("PTR");
       
     
        //LOG.info("adding PTSL intervals");
        DefaultTimeBarRowModel tbr1 = new DefaultTimeBarRowModel(maintenance);
        String[] MAINTENANCE={PointingBlock.TYPE_MNAV,PointingBlock.TYPE_MOCM,PointingBlock.TYPE_MSLW,PointingBlock.TYPE_MWAC,PointingBlock.TYPE_MWNV,PointingBlock.TYPE_MWOL};
        for (int i=0;i<MAINTENANCE.length;i++){
        	//String bType=MAINTENANCE[i];
          	PointingBlock[] blocks = schedule.getPtslSegment().getAllBlocksOfType(MAINTENANCE[i]).getBlocks();
        	for (int j=0;j<blocks.length;j++){
        		tbr1.addInterval(new BlockInterval(blocks[j]));
        	}

            
        }
        
    	
     	//DefaultTimeBarRowModel tbr2 = new DefaultTimeBarRowModel(obs);
     	
      	PointingBlock[] blocks = schedule.getPtslSegment().getAllBlocksOfType(PointingBlock.TYPE_OBS).getBlocks();
    	for (int j=0;j<blocks.length;j++){
    		tbr1.addInterval(new BlockInterval(blocks[j]));
    	}
    	////LOG.info("finsih adding PTSL intervals");
    	//LOG.info("adding observation intervals");
    	DefaultTimeBarRowModel tbr9 = new DefaultTimeBarRowModel(schObs);
    	Observation[] observations = schedule.getObservationsSchedule().getObservations();
    	for (int k=0;k<observations.length;k++){
    		ObservationInterval oi = new ObservationInterval(observations[k]);
    		oi.addObservationListener(this);
    		tbr9.addInterval(oi);
    	}
    	//LOG.info("adding observation intervals");
    	//LOG.info("adding ptr intervals");
    	DefaultTimeBarRowModel tbr2 = new DefaultTimeBarRowModel(ptr);
    	Ptr ptrSegment = schedule.getPtr();
    	PointingBlock[] ptrBlocks = ptrSegment.getAllBlocks();
    	for (int b=0;b<ptrBlocks.length;b++){
    		tbr2.addInterval(new BlockInterval(ptrBlocks[b]));
    	}
        this.addRow(tbr1);
        //this.addRow(tbr2);
        this.addRow(tbr9);
        this.addRow(tbr2);
        //LOG.info("finsih adding ptr intervals");
        //LOG.info("adding commanig transitions intervals");
		DefaultTimeBarRowModel[] newRows=this.createSampleDataset(this.getHistoryModes());
		for (int i=0;i<newRows.length;i++){
			//System.out.println("add commanding row");
			this.addRow(newRows[i]);
		}
		//LOG.info("fisnih adding commanig transitions intervals");
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
		// TODO Auto-generated method stub
		//LOG.info("Observation Changed");
		/*try{
			initialized=false;
			DefaultTimeBarRowModel ptrRow = ((DefaultTimeBarRowModel) this.getRow(2));
			List<Interval> iv = ptrRow.getIntervals();
			ptrRow.remIntervals(new java.util.Vector<Interval>(iv));
	        //DefaultRowHeader ptr = new DefaultRowHeader("PTR");
	       	//DefaultTimeBarRowModel tbr2 = new DefaultTimeBarRowModel(ptr);
	    	Ptr ptrSegment = schedule.getPtr();
	    	PointingBlock[] ptrBlocks = ptrSegment.getAllBlocks();
	    	for (int b=0;b<ptrBlocks.length;b++){
	    		ptrRow.addInterval(new BlockInterval(ptrBlocks[b]));
	    	}
	    	initialized=true;
		}catch (Exception e){
			e.printStackTrace();
			LOG.throwing(ScheduleModel.class.getName(), "observationChanged", e);

			throw e;
		}*/
    	
		
	}
	public void recualculatePtr(){
		if (!recalculate) return;
		LOG.info("Start recalculating PTR");
		//Thread.dumpStack();
		DefaultTimeBarRowModel ptrRow = ((DefaultTimeBarRowModel) this.getRow(2));
		List<Interval> iv = ptrRow.getIntervals();
		ptrRow.remIntervals(new java.util.Vector<Interval>(iv));
        //DefaultRowHeader ptr = new DefaultRowHeader("PTR");
       	//DefaultTimeBarRowModel tbr2 = new DefaultTimeBarRowModel(ptr);
    	Ptr ptrSegment = schedule.getPtr();
    	PointingBlock[] ptrBlocks = ptrSegment.getAllBlocks();
    	for (int b=0;b<ptrBlocks.length;b++){
    		ptrRow.addInterval(new BlockInterval(ptrBlocks[b]));
    	}
    	LOG.info("End recalculating PTR");
	}
	@Override
	public void scheduleChanged() {
		//LOG.info("Schedule Changed");

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
		
		//scheduleChanged();
		
	}

	@Override
	public void pointingChanged(ObservationChangeEvent event) {
		//if (!Observation.LISTEN) return;
		LOG.info("Observation pointing Changed");
		//Thread.dumpStack();
		if (initialized){
			try{
				initialized=false;
				recualculatePtr();
		    	initialized=true;
			}catch (Exception e){
				e.printStackTrace();
				//LOG.throwing(ScheduleModel.class.getName(), "observationChanged", e);
	
				throw e;
			}
		}
		
	}

	@Override
	public void commandingChanged(ObservationChangeEvent event) {
		LOG.info("Listenerd change in pointing");
		if (!recalculate) return;
		//LOG.info("Observation commanding Changed");
		// TODO Auto-generated method stub
		//LOG.info("Observation pointing Changed");
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
				//LOG.throwing(ScheduleModel.class.getName(), "observationChanged", e);
	
				throw e;
			}
		}
		
	}
    private HistoryModes getHistoryModes(){
    	HistoryModes result = new HistoryModes();
    	ModelState modelState= new ModelState();
    	
    	AbstractSequence[] seqs = schedule.getPor().getSequences();
    	//System.out.println(schedule.getPor().toXml());
		Orcd orcd;
		try{
			orcd=Orcd.readORCDfile(Properties.getProperty(Properties.ORCD_FILE));
		}catch(Exception e){
			orcd=Orcd.readORCDfromJar();

		}
		for (int i=0;i<seqs.length;i++){
			//System.out.println("sequence found");
			//if (seqs[i].getName())
			//LOG.info("Sequence "+seqs[i].getName());
			HashMap<Long,String> newmodes=orcd.getModesAsHistory(seqs[i].getName(),seqs[i].getExecutionDate().getTime());
			result.putAll(newmodes, seqs[i].getName(), seqs[i].getExecutionDate().getTime());

		}
		String[] states = modelState.getStateNames();
		long startTime = this.schedule.getPtslSegment().getSegmentStartDate().getTime();
		HashMap<Long, String> hm = new HashMap<Long,String>();
		for (int i=0;i<states.length;i++){
			hm.put(startTime, states[i]+"Off");
			//modelState.gets
		}
		result.putAll(hm, "init", startTime);
		//result.getSets().entrySet().
		long[] times=result.getTimes();
		//java.util.Vector<Long> temp=new java.util.Vector<Long>();
		/*for (int i=0;i<timess.length;i++){
			if (timess[i]>=start.getTime() && timess[i]<=end.getTime()){
				temp.add(timess[i]);
			}
		}*/
		//Long[] times=new Long[temp.size()];
		//temp.toArray(times);
		//LOG.info("Calculating mode transitions");
		for (int i=0;i<times.length;i++){
			long j=times[i];
			String oldmode=modelState.getStateForMode(result.get(j));
			String newmode=result.get(j);
			if (!orcd.checkTransion(oldmode, newmode)){
				String mess="Forbidden transition "+oldmode+"--->"+newmode+" at "+DateUtil.dateToZulu(new Date(j))+" via "+result.getCommand(j)+" executed at "+DateUtil.dateToZulu(new Date(result.getOriginalTime(j)));
				//messages=messages+mess;
				//context.log(mess);
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, mess);
			}
			//LOG.info("Transitting to "+newmode);
			modelState.setState(newmode);
			result.addStates(j,modelState.clone());
			//context.getExecutionDates().append(j);
			//float modelPower = context.getOrcd().getTotalPowerForModes(context.getModelState().getAllStates());
			//context.getHistoryPower().append(modelPower);
			//float mPower = context.getMocPower().getPowerAt(new Date(j));
			//context.getMocPowerHistory().append(mPower);
			/*if (modelPower>mPower){
				String mess="ALARM: Power over due via sequence "+context.getHistoryModes().getCommand(j)+" executed at "+dateFormat2.format(new Date(context.getHistoryModes().getOriginalTime(j)));
				//messages=messages+mess;
				context.log(mess);
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, mess);

			}*/
			
		}
		long endTime = this.schedule.getPtslSegment().getSegmentEndDate().getTime();
		String[] as = modelState.getAllStates();
		HashMap<Long, String> hm2 = new HashMap<Long,String>();
		for (int i=0;i<as.length;i++){
			hm2.put(endTime, as[i]);
			//modelState.gets
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

        
        //TaskSeries globalSerie=new TaskSeries("GLOBAL");
        String[] modes=hm.getAllStates();
        //System.out.println("Building the plot");
        for (int i=0;i<modes.length;i++){
    		
        	//System.out.println("To add "+modes[i]);
        }
        for (int i=0;i<modes.length;i++){
        	if (!modes[i].endsWith("Off") && !modes[i].endsWith("Closed")){	
	        	java.util.Date[] dates=hm.getBoundarieDatesForState(modes[i]);
	        	String instrument="";
	        	Iterator<String> it3 = instrumentsStartString.keySet().iterator();
	        	while(it3.hasNext()){
	        		String stString=it3.next();
	        		if (modes[i].startsWith(stString)){
	        		//ystem.out.print("start with "+stString+"?");
	        			instrument=instrumentsStartString.get(stString);
	        		}

	        	}
	        	//System.out.println(instrument);
	        	DefaultTimeBarRowModel task = taskVectorMap.get(instrument);
	        	//DefaultTimeBarRowModel task= new DefaultTimeBarRowModel( new DefaultRowHeader(instrument));
	        	for (int j=0;j<dates.length;j=j+2){
	        		CommandingInterval interval= new CommandingInterval(modes[i],instrument,dates[j],dates[j+1]);
	        		//LOG.info(interval.toString());
	        		try{
	        			task.addInterval(interval);
	        		}catch (NullPointerException e){
	        			IllegalArgumentException iae = new IllegalArgumentException("Unknown instrument "+instrument);
	        			iae.initCause(e);
	        			throw(iae);
	        		}
	        		//Task subtask=new Task(modes[i]+new Integer(j).toString(),dates[j],dates[j+1]);
	        		//task.addSubtask(subtask);
	        	}


        	
        	}
        }
        DefaultTimeBarRowModel[] result=new DefaultTimeBarRowModel[taskVectorMap.size()];
        result=taskVectorMap.values().toArray(result);
        return result;
	}


}
