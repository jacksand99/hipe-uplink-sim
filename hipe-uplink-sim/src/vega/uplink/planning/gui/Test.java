package vega.uplink.planning.gui;

import herschel.share.fltdyn.time.FineTime;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.JFrame;

import vega.uplink.planning.Observation;
import vega.uplink.planning.ObservationUtil;
import vega.uplink.planning.Schedule;
import vega.uplink.pointing.Pdfm;
import vega.uplink.pointing.PointingBlock;
import vega.uplink.pointing.PointingBlocksSlice;
import vega.uplink.pointing.Ptr;
import vega.uplink.pointing.PtrSegment;
import vega.uplink.pointing.PtrUtils;
import de.jaret.util.date.Interval;
import de.jaret.util.date.IntervalImpl;
import de.jaret.util.date.JaretDate;
import de.jaret.util.ui.timebars.model.DefaultRowHeader;
import de.jaret.util.ui.timebars.model.DefaultTimeBarModel;
import de.jaret.util.ui.timebars.model.DefaultTimeBarRowModel;
import de.jaret.util.ui.timebars.model.TimeBarModel;
import de.jaret.util.ui.timebars.model.TimeBarRow;
import de.jaret.util.ui.timebars.swing.TimeBarViewer;



public class Test {
    public static final List _headerList = new ArrayList();
    private static final Logger LOG = Logger.getLogger(Test.class.getName());
    public static void main(String[] args) {
    	herschel.share.util.Configuration.setProperty("var.hcss.dir", "/Users/jarenas 1/Downloads/hcss-12.0.2524");
    	herschel.share.util.Configuration.setProperty("var.hcsstest.dir", "/Users/jarenas 1/Downloads/hcss-12.0.2524");
    	herschel.share.util.Configuration.setProperty("vega.mib.location", "/Users/jarenas 1/Downloads/MAPPS/MIB");
    	herschel.share.util.Configuration.setProperty("var.hcss.cfg.dir", "/Users/jarenas 1/.hcss");
    	herschel.share.util.Configuration.setProperty("vega.orcd.file","/Users/jarenas 1/Downloads/MAPPS/MIB/orcd.csv");
    	herschel.share.util.Configuration.setProperty("vega.pwpl.file","/Users/jarenas 1/OPS/ROS_SGS/PLANNING/RMOC/FCT/PWPL_14_001_14_365__OPT_01.ROS");



        JFrame f = new JFrame(Test.class.getName());
        f.setSize(300, 330);
        f.getContentPane().setLayout(new BorderLayout());
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //TimeBarModel model = createRandomModel(2, 120, 5);
		Ptr ptr;
		try {
			ptr = PtrUtils.readPTRfromFile("/Users/jarenas 1/OPS/ROS_SGS/PLANNING/LTP001/LTP001A/MTP005A/PTR/PTSL_DM_005_02____A__00002.ROS");
			PtrSegment segment = ptr.getSegment("MTP_005");
	        //TimeBarModel model = createModel(segment);
			Schedule sch=new Schedule(segment);
			
			Observation obs = ObservationUtil.readObservationFromFile("/users/jarenas 1/Downloads/obs1.xml");
			ObservationEditor editor=new ObservationEditor();
			editor.setObservation(obs);
			sch.addObservation(obs);
			long duration = obs.getDurationMilliSecs();
			obs.setStartDate(new FineTime(PointingBlock.zuluToDate("2014-07-04T19:00:00Z")));
			obs.setEndDate(new FineTime(new Date(obs.getStartDate().toDate().getTime()+duration)));
			
			ObservationUtil.saveScheduleToFile("/Users/jarenas 1/Rosetta/testing/SCH_example.ROS", sch);
			//Schedule sch2=ObservationUtil.readScheduleFromFile("/Users/jarenas 1/Rosetta/testing/SCH_example_2.ROS");
			LOG.info("Start reading schedule");
			Schedule sch2=ObservationUtil.readScheduleFromFile("/Users/jarenas 1/Rosetta/hack 11/SCH_MTP_11_H_4.ROS");
			LOG.info("Finish reading schedule");
			//System.out.println(ObservationUtil.scheduleToITL(sch2));
			//System.out.println(ObservationUtil.scheduleToEVF(sch2));
			//Pdfm pdfm = PtrUtils.readPdfmfromFile("/Users/jarenas 1/Rosetta/testing/PDFM_DM_010_01___A_ZZZZZ.ROS");
			//sch2.setPdfm(pdfm);
			ScheduleViewer scViewer = new ScheduleViewer();
			scViewer.setSchedule(sch2);
			f.getContentPane().add(scViewer, BorderLayout.CENTER);
			//f.getContentPane().add(editor, BorderLayout.CENTER);
			//System.out.println(editor.getParent().getLayout());
			f.setVisible(true);
			/*System.out.println(sch2.toXml());
			TimeBarModel model= new ScheduleModel(sch);
	        TimeBarViewer tbv = new TimeBarViewer(model);
	        //tbv.setTimeScaleRenderer(new SegmentTimeScaleRenderer());
	        tbv.setPixelPerSecond(0.005);
	        System.out.println(tbv.getTimeScaleRenderer().getClass());
	        
	        tbv.setStartDate(new JaretDate(segment.getStartDate().toDate()));
	        tbv.setTitle(segment.getName());
	        //tbv.setEndDate(new JaretDate(segment.getStartDate().toDate()));

	        f.getContentPane().add(tbv, BorderLayout.CENTER);

	        f.setVisible(true);
	        sch2.addObservation(obs);*/
			

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        // model will be changed by the main thread
        //startChanging(model);

    }

    /**
     * @param model
     */
    private static void startChanging(TimeBarModel model) {
        long delay = 800;
        for (int r = 0; r < model.getRowCount(); r++) {
            TimeBarRow row = model.getRow(r);
            double sum = getIntervalSum(row);
            DefaultRowHeader header = (DefaultRowHeader) _headerList.get(r);
            header.setLabel("R" + r + "(" + sum + ")");
            System.out.println("Changed header " + r);
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for (int r = 0; r < model.getRowCount(); r++) {
            TimeBarRow row = model.getRow(r);
            Iterator it = row.getIntervals().iterator();
            while (it.hasNext()) {
                Interval interval = (Interval) it.next();
                double minutes = interval.getEnd().diffMinutes(interval.getBegin());
                JaretDate date = interval.getEnd().copy();
                date.backMinutes(minutes / 4);
                interval.setEnd(date);
                double sum = getIntervalSum(row);
                DefaultRowHeader header = (DefaultRowHeader) _headerList.get(r);
                header.setLabel("R" + r + "(" + sum + ")");
                System.out.println("Changed interval " + interval);
                try {
                    Thread.sleep(delay / 2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private static double getIntervalSum(TimeBarRow row) {
        double result = 0;
        Iterator it = row.getIntervals().iterator();
        while (it.hasNext()) {
            Interval interval = (Interval) it.next();
            result += interval.getEnd().diffMinutes(interval.getBegin());
        }

        return result;
    }
    public static void createRowHeaders(){
        DefaultRowHeader gsep = new DefaultRowHeader(PointingBlock.TYPE_GSEP);
        DefaultRowHeader mnav = new DefaultRowHeader(PointingBlock.TYPE_MNAV);
        DefaultRowHeader mocm = new DefaultRowHeader(PointingBlock.TYPE_MOCM);
        DefaultRowHeader mslw = new DefaultRowHeader(PointingBlock.TYPE_MSLW);
        DefaultRowHeader mwac = new DefaultRowHeader(PointingBlock.TYPE_MWAC);
        DefaultRowHeader mwnv = new DefaultRowHeader(PointingBlock.TYPE_MWNV);
        DefaultRowHeader mwol = new DefaultRowHeader(PointingBlock.TYPE_MWOL);
        DefaultRowHeader obs = new DefaultRowHeader(PointingBlock.TYPE_OBS);
        DefaultRowHeader slew = new DefaultRowHeader(PointingBlock.TYPE_SLEW);
       
     

        _headerList.add(gsep);
        _headerList.add(mnav);
        _headerList.add(mocm);
        _headerList.add(mslw);
        _headerList.add(mwac);
        _headerList.add(mwnv);
        _headerList.add(mwol);
        _headerList.add(obs);
        _headerList.add(slew);

    
    
    
    }
    
    public static TimeBarModel createModel(PtrSegment segment){
    	DefaultTimeBarModel model = new DefaultTimeBarModel();
    	
    	DefaultRowHeader maintenance = new DefaultRowHeader("MAINTENANCE");
        /*DefaultRowHeader gsep = new DefaultRowHeader(PointingBlock.TYPE_GSEP);
        DefaultRowHeader mnav = new DefaultRowHeader(PointingBlock.TYPE_MNAV);
        DefaultRowHeader mocm = new DefaultRowHeader(PointingBlock.TYPE_MOCM);
        DefaultRowHeader mslw = new DefaultRowHeader(PointingBlock.TYPE_MSLW);
        DefaultRowHeader mwac = new DefaultRowHeader(PointingBlock.TYPE_MWAC);
        DefaultRowHeader mwnv = new DefaultRowHeader(PointingBlock.TYPE_MWNV);
        DefaultRowHeader mwol = new DefaultRowHeader(PointingBlock.TYPE_MWOL);*/
        DefaultRowHeader obs = new DefaultRowHeader(PointingBlock.TYPE_OBS);
        DefaultRowHeader slew = new DefaultRowHeader(PointingBlock.TYPE_SLEW);
       
     
        _headerList.add(maintenance);
        /*_headerList.add(gsep);
        _headerList.add(mnav);
        _headerList.add(mocm);
        _headerList.add(mslw);
        _headerList.add(mwac);
        _headerList.add(mwnv);
        _headerList.add(mwol);*/
        _headerList.add(obs);
        _headerList.add(slew);
        
        DefaultTimeBarRowModel tbr1 = new DefaultTimeBarRowModel(maintenance);
        String[] MAINTENANCE={PointingBlock.TYPE_MNAV,PointingBlock.TYPE_MOCM,PointingBlock.TYPE_MSLW,PointingBlock.TYPE_MWAC,PointingBlock.TYPE_MWNV,PointingBlock.TYPE_MWOL};
        for (int i=0;i<MAINTENANCE.length;i++){
        	String bType=MAINTENANCE[i];
          	PointingBlock[] blocks = segment.getAllBlocksOfType(MAINTENANCE[i]).getBlocks();
        	for (int j=0;j<blocks.length;j++){
        		tbr1.addInterval(new BlockInterval(blocks[j]));
        	}

            
        }
    	
     	DefaultTimeBarRowModel tbr2 = new DefaultTimeBarRowModel(obs);
     	
      	PointingBlock[] blocks = segment.getAllBlocksOfType(PointingBlock.TYPE_OBS).getBlocks();
    	for (int j=0;j<blocks.length;j++){
    		tbr2.addInterval(new BlockInterval(blocks[j]));
    	}

    	DefaultTimeBarRowModel tbr9 = new DefaultTimeBarRowModel(slew);
        model.addRow(tbr1);
        model.addRow(tbr2);
        model.addRow(tbr9);

        return model;
    	
    }
    

    public static TimeBarModel createRandomModel(int rows, int averageLengthInMinutes, int countPerRow) {
        DefaultTimeBarModel model = new DefaultTimeBarModel();

        for (int row = 0; row < rows; row++) {
            DefaultRowHeader header = new DefaultRowHeader("r" + row);
            _headerList.add(header);
            DefaultTimeBarRowModel tbr = new DefaultTimeBarRowModel(header);
            JaretDate date = new JaretDate();
            for (int i = 0; i < countPerRow; i++) {
                IntervalImpl interval = new IntervalImpl();
                int length = averageLengthInMinutes / 2 + (int) (Math.random() * (double) averageLengthInMinutes);
                interval.setBegin(date.copy());
                date.advanceMinutes(length);
                interval.setEnd(date.copy());

                tbr.addInterval(interval);

                int pause = (int) (Math.random() * (double) averageLengthInMinutes / 5);
                date.advanceMinutes(pause);
            }
            model.addRow(tbr);
        }

        System.out.println("Created " + (rows * countPerRow) + " Intervals");

        return model;
    }
    

}