package vega.uplink.commanding.gui;

import herschel.ia.gui.kernel.Selection;
import static herschel.ia.gui.kernel.menus.Insert.MAIN;
import herschel.ia.gui.kernel.SiteAction;
import herschel.ia.gui.kernel.menus.ActionBars;
import herschel.ia.gui.kernel.parts.AbstractVariableEditorComponent;
import herschel.ia.gui.kernel.parts.EditorArea;
import herschel.ia.gui.kernel.parts.EditorPart;
import herschel.share.swing.Components;
//import herschel.share.util.Configuration;










import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.Paint;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JScrollPane;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.CategoryItemEntity;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.CategoryItemRendererState;
import org.jfree.chart.renderer.category.GanttRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.IntervalCategoryDataset;
import org.jfree.data.gantt.GanttCategoryDataset;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

//import com.oracle.jrockit.jfr.InvalidValueException;







import vega.IconResources;
import vega.uplink.Properties;
import vega.uplink.commanding.HistoryModes;

public class HistoryModesPlot extends AbstractVariableEditorComponent<HistoryModes> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4262249051727577295L;

	//private Selection selection;
	
	//private String viewerId;
    private List<SiteAction> actions;

	//EditorPart part;
	
	private ChartPanel chartPanel;
	private HashMap<String,String> instrumentsStartString;
	private List<String> insList;
	
	
	public HistoryModesPlot(){
		super();
		this.setLayout(new GridLayout(1,1));
		System.out.println("Constructor");
	}
	
	public HistoryModesPlot(String title,HistoryModes hm){
		super();
		this.setLayout(new GridLayout(1,2));
		makePlot(title,hm);
	}
	private HistoryModesPlot makePlot(String title,HistoryModes hm) {
		instrumentsStartString=new HashMap<String,String>();
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
		//super();
        //super(title);
		
        BarRenderer.setDefaultBarPainter(new MyBarPainter());
        final IntervalCategoryDataset dataset = createSampleDataset(hm);

        // create the chart...
        final JFreeChart chart = ChartFactory.createGanttChart(
            title,  // chart title
            "System",              // domain axis label
            "Time",              // range axis label
            dataset,             // data
            false,                // include legend
            true,                // tooltips
            false                // urls
        );
        final CategoryPlot plot = (CategoryPlot) chart.getPlot();
        MyRenderer renderer =new MyRenderer(dataset);
        renderer.title=title;
        plot.setRenderer(renderer);
        MyTipGenerator tips= new MyTipGenerator();
        renderer.setBaseToolTipGenerator(tips);
        tips.title=title;
        chartPanel = new ChartPanel(chart,true);
        JScrollPane jsp=new JScrollPane(chartPanel);
        //jsp.add(chartPanel);
        //part.getArea().
        //if (part==null) chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        //add(chartPanel);
        add(jsp);
        return this;
	}
	
	public void setPreferedSize(java.awt.Dimension dimension){
		chartPanel.setPreferredSize(dimension);
	}
	private IntervalCategoryDataset createSampleDataset(HistoryModes hm) {
		HashMap<String,java.util.Vector<Task>> taskVectorMap=new HashMap<String,java.util.Vector<Task>>();
		List<String> insList2 = Properties.getList(Properties.INSTRUMENT_NAMES_PROPERTIES);
		Iterator<String> it = insList2.iterator();
		while (it.hasNext()){
			String ins=it.next();
			taskVectorMap.put(ins, new java.util.Vector<Task>());
		}

        
        TaskSeries globalSerie=new TaskSeries("GLOBAL");
        String[] modes=hm.getAllStates();
        //System.out.println("Building the plot");
        for (int i=0;i<modes.length;i++){
    		
        	//System.out.println("To add "+modes[i]);
        }
        for (int i=0;i<modes.length;i++){
        	if (!modes[i].endsWith("Off")){	
	        	java.util.Date[] dates=hm.getBoundarieDatesForState(modes[i]);
	        	Task task= new Task(modes[i],dates[0],dates[dates.length-1]);
	        	for (int j=0;j<dates.length;j=j+2){
	        		Task subtask=new Task(modes[i]+new Integer(j).toString(),dates[j],dates[j+1]);
	        		task.addSubtask(subtask);
	        	}
	        	Iterator<String> it2 = this.instrumentsStartString.keySet().iterator();
	        	while(it2.hasNext()){
	        		String stString=it2.next();
	        		String ins=this.instrumentsStartString.get(stString);
	        		java.util.Vector<Task> taskVector=taskVectorMap.get(ins);
	        		if (modes[i].startsWith(stString)) taskVector.add(task);
	        	}

        	
        	}
        }
        

        final TaskSeriesCollection collection = new TaskSeriesCollection();
        List<String> insList = Properties.getList(Properties.INSTRUMENT_NAMES_PROPERTIES);
		Iterator<String> it3 = insList.iterator();
		while (it3.hasNext()){
			String ins=it3.next();
			Vector<Task> tempTask = taskVectorMap.get(ins);
			addToSerie(globalSerie,tempTask);
		}



        
        collection.add(globalSerie);
        return collection;
    }
	
	private void addToSerie(TaskSeries serie,java.util.Vector<Task> vector){
		int size=vector.size();
		for (int i=0;i<size;i++){
			serie.add(vector.get(i));
		}
	}

    /**
     * Utility method for creating <code>Date</code> objects.
     *
     * @param day  the date.
     * @param month  the month.
     * @param year  the year.
     *
     * @return a date.
     */
    private static Date date(final int day, final int month, final int year) {

        final Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        final Date result = calendar.getTime();
        return result;

    }
    private static class MyTipGenerator extends org.jfree.chart.labels.IntervalCategoryToolTipGenerator{
    	int subTaskIndex;
    	String title="";
    	public MyTipGenerator(){
    	
    		super(org.jfree.chart.labels.IntervalCategoryToolTipGenerator.DEFAULT_TOOL_TIP_FORMAT_STRING,new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS"));
    		subTaskIndex=0;
    		title="GLOBAL";
    	}
    	public void setSubTaskIndex(int subTaskIndex){
    		this.subTaskIndex = subTaskIndex;
    	}
    	protected Object[] oldCreateItemArray(CategoryDataset dataset,int row, int column) {

			Object[] result = new Object[5];
			result[0] = title;
			result[1] = dataset.getColumnKey(column).toString();
			Number value = dataset.getValue(row, column);
			
			if (getNumberFormat() != null) {
			result[2] = getNumberFormat().format(value);
			}
			else if (getDateFormat() != null) {
			result[2] = getDateFormat().format(value);
			}
			
			if (dataset instanceof IntervalCategoryDataset) {
			IntervalCategoryDataset icd = (IntervalCategoryDataset) dataset;
			Number start = icd.getStartValue(row, column);
			
			Number end = icd.getEndValue(row, column);
			if (getNumberFormat() != null) {
			result[3] = getNumberFormat().format(start);
			result[4] = getNumberFormat().format(end);
			}
			else if (getDateFormat() != null) {
			result[3] = getDateFormat().format(start);
			result[4] = getDateFormat().format(end);

			}
			}
			return result;
        }
    		
    	
    	
        protected Object[] createItemArray(CategoryDataset dataset,int row, int column) {
        	TaskSeriesCollection data = (TaskSeriesCollection) dataset;
        	if (subTaskIndex>=data.getSeries(row).get(column).getSubtaskCount()){
        		return oldCreateItemArray(dataset,row, column);
        	}

        	Task subTask =data.getSeries(row).get(column).getSubtask(subTaskIndex);
			Object[] result = new Object[5];
			result[0] = title;
			
			result[1] = dataset.getColumnKey(column).toString();
			HashMap<String,String> instrumentsStartStringTemp=new HashMap<String,String>();
			List<String> insList = Properties.getList(Properties.INSTRUMENT_NAMES_PROPERTIES);
			Iterator<String> it = insList.iterator();
			while(it.hasNext()){
				String ins=it.next();
				List<String> beg=Properties.getList(Properties.SUBINSTRUMENT_MODE_START_PROPERTY_PREFIX+ins);
				Iterator<String> it2 = beg.iterator();
				while (it2.hasNext()){
					String begString=it2.next();
					instrumentsStartStringTemp.put(begString, ins);
				}
			}

        	Iterator<String> it2 = instrumentsStartStringTemp.keySet().iterator();
        	while(it2.hasNext()){
        		String stString=it2.next();
        		String ins=instrumentsStartStringTemp.get(stString);
        		//java.util.Vector<Task> taskVector=taskVectorMap.get(ins);
        		if (((String)result[1]).startsWith(stString)) result[0]=ins;
        		//if (modes[i].startsWith(stString)) taskVector.add(task);
        	}

        	/*if (((String)result[1]).startsWith("AL")) result[0]="ALICE";
        	if (((String)result[1]).startsWith("CN")) result[0]="CONSERT";
        	if (((String)result[1]).startsWith("CS")) result[0]="COSIMA";
        	if (((String)result[1]).startsWith("GD")) result[0]="GIADA";
        	if (((String)result[1]).startsWith("GDS")) result[0]="GIADA";
        	if (((String)result[1]).startsWith("MBS")) result[0]="GIADA";
        	if (((String)result[1]).startsWith("IS")) result[0]="GIADA";
        	if (((String)result[1]).startsWith("LAS")) result[0]="GIADA";
        	if (((String)result[1]).startsWith("HEAT")) result[0]="GIADA";
        	if (((String)result[1]).startsWith("MD")) result[0]="MIDAS";
        	if (((String)result[1]).startsWith("MR")) result[0]="MIRO";
        	if (((String)result[1]).startsWith("SR")) result[0]="OSIRIS";
        	if (((String)result[1]).startsWith("RN")) result[0]="ROSINA";
        	if (((String)result[1]).startsWith("RP")) result[0]="RPC";
        	if (((String)result[1]).startsWith("LBS")) result[0]="RSI";
        	if (((String)result[1]).startsWith("BSR")) result[0]="RSI";
        	if (((String)result[1]).startsWith("SE")) result[0]="SREM";
        	if (((String)result[1]).startsWith("VR")) result[0]="VIRTIS";
        	if (((String)result[1]).startsWith("ESS")) result[0]="LANDER";
        	if (((String)result[1]).startsWith("PTR")) result[0]="PTR";*/
			Number value = dataset.getValue(row, column);
			
			if (getNumberFormat() != null) {
			result[2] = getNumberFormat().format(value);
			}
			else if (getDateFormat() != null) {
			result[2] = getDateFormat().format(value);
			}
			
			if (dataset instanceof IntervalCategoryDataset) {
			IntervalCategoryDataset icd = (IntervalCategoryDataset) dataset;
			Number start = icd.getStartValue(row, column);
			
			Number end = icd.getEndValue(row, column);
			if (getNumberFormat() != null) {
			result[3] = getNumberFormat().format(start);
			result[4] = getNumberFormat().format(end);
			}
			else if (getDateFormat() != null) {
			result[3] = getDateFormat().format(subTask.getDuration().getStart());
			result[4] = getDateFormat().format(subTask.getDuration().getEnd());

			}
			}
			return result;
        }

    }
    private static class MyBarPainter extends org.jfree.chart.renderer.category.StandardBarPainter{
    	public void paintBarShadow(java.awt.Graphics2D g2,
                BarRenderer renderer,
                int row,
                int column,
                java.awt.geom.RectangularShape bar,
                org.jfree.ui.RectangleEdge base,
                boolean pegShadow){
    		
    	}
    }
    private static class MyRenderer extends GanttRenderer {
    	IntervalCategoryDataset dst;
    	private CategoryItemRendererState finalState;
    	String title;
        public MyRenderer(IntervalCategoryDataset ds){
        	super();
        	dst=ds;
        	setDefaultShadowsVisible(false);
        	finalState=null;
        	title="GLOBAL";
        	//this.getBarPainter().paintBarShadow(arg0, arg1, arg2, arg3, arg4, arg5, arg6);
        }
        
        public CategoryItemRendererState getState(){
        	return finalState;
        }
        
        public Paint getItemPaint(int row, int col) {
        	String task=dst.getColumnKey(col).toString();
        	java.awt.Color result=new java.awt.Color(0,0,255);
			HashMap<String,String> instrumentsStartStringTemp=new HashMap<String,String>();
			List<String> insList = Properties.getList(Properties.INSTRUMENT_NAMES_PROPERTIES);
			Iterator<String> it = insList.iterator();
			while(it.hasNext()){
				String ins=it.next();
				List<String> beg=Properties.getList(Properties.SUBINSTRUMENT_MODE_START_PROPERTY_PREFIX+ins);
				Iterator<String> it2 = beg.iterator();
				while (it2.hasNext()){
					String begString=it2.next();
					instrumentsStartStringTemp.put(begString, ins);
				}
			}

        	Iterator<String> it2 = instrumentsStartStringTemp.keySet().iterator();
        	while(it2.hasNext()){
        		String stString=it2.next();
        		String ins=instrumentsStartStringTemp.get(stString);
        		java.awt.Color colIns=Properties.getColor(Properties.SUBINSTRUMENT_COLOR_PROPERTY_PREFIX+ins);
        		/*List<String> colTempList = Properties.getList(Properties.SUBINSTRUMENT_COLOR_PROPERTY_PREFIX+ins);
        		java.awt.Color colIns=new java.awt.Color(Integer.parseInt(colTempList.get(0)),Integer.parseInt(colTempList.get(1)),Integer.parseInt(colTempList.get(2)));*/
        		if (task.startsWith(stString)) result=colIns;
        		//java.util.Vector<Task> taskVector=taskVectorMap.get(ins);
        		//if (((String)result[1]).startsWith(stString)) result[0]=ins;
        		//if (modes[i].startsWith(stString)) taskVector.add(task);
        	}

        	/*if (task.startsWith("AL")) result=java.awt.Color.BLUE;
        	if (task.startsWith("CN")) result=java.awt.Color.LIGHT_GRAY;
        	if (task.startsWith("CS")) result=java.awt.Color.GREEN;
        	if (task.startsWith("GD")) result=java.awt.Color.RED;
        	if (task.startsWith("GDS")) result=java.awt.Color.RED;
        	if (task.startsWith("MBS")) result=java.awt.Color.RED;
        	if (task.startsWith("IS")) result=java.awt.Color.RED;
        	if (task.startsWith("LAS")) result=java.awt.Color.RED;
        	if (task.startsWith("HEAT")) result=java.awt.Color.RED;
        	if (task.startsWith("MD")) result=java.awt.Color.MAGENTA;
        	if (task.startsWith("MR")) result=java.awt.Color.PINK;
        	if (task.startsWith("SR")) result=java.awt.Color.CYAN;
        	if (task.startsWith("RN")) result=java.awt.Color.GRAY;
        	if (task.startsWith("RP")) result=java.awt.Color.ORANGE;
        	if (task.startsWith("LBS")) result=java.awt.Color.DARK_GRAY;
        	if (task.startsWith("BSR")) result=java.awt.Color.DARK_GRAY;
        	if (task.startsWith("SE")) result=java.awt.Color.YELLOW;
        	if (task.startsWith("VR")) result=java.awt.Color.WHITE;
        	if (task.startsWith("ESS")) result=java.awt.Color.ORANGE;
        	if (task.startsWith("PTR")) result=java.awt.Color.BLACK;*/
            //System.out.println(row + " " + col + " " + super.getItemPaint(row, col));
            
            return result;
        }
        
        protected void drawTasks(Graphics2D g2, CategoryItemRendererState state, Rectangle2D dataArea, CategoryPlot plot, CategoryAxis domainAxis, ValueAxis rangeAxis, GanttCategoryDataset dataset, int row, int column){
        	EntityCollection oldEntities = state.getEntityCollection();
        	Collection oldCollection =oldEntities.getEntities();
        	Object[] oldArray=oldCollection.toArray();
        	//System.out.println("Old Size:");
        	//System.out.println(oldCollection.size());
        	super.drawTasks(g2, state, dataArea, plot, domainAxis, rangeAxis, dataset, row, column);
        	EntityCollection newEntities = state.getEntityCollection();
        	Collection newCollection =newEntities.getEntities();
        	Object[] newArray=newCollection.toArray();
        	//System.out.println("Old Size:");
        	//System.out.println(oldArray.length);
        	//System.out.println("New Size:");
        	//System.out.println(newCollection.size());
        	org.jfree.chart.entity.ChartEntity[] entityOldArray=new org.jfree.chart.entity.ChartEntity[oldArray.length];
        	org.jfree.chart.entity.ChartEntity[] entityNewArray=new org.jfree.chart.entity.ChartEntity[newArray.length];
        	//System.out.println("Old Entities:");
        	for (int i=0;i<oldArray.length;i++){
        		entityOldArray[i]=(org.jfree.chart.entity.ChartEntity)oldArray[i];
        		//System.out.println(entityOldArray[i].getToolTipText());
        	}
        	//System.out.println("New Entities:");
        	for (int i=0;i<newArray.length;i++){
        		entityNewArray[i]=(org.jfree.chart.entity.ChartEntity)newArray[i];
        		//System.out.println(entityNewArray[i].getToolTipText());
        	}
        	
        	int index=0;
        	for (int i=entityOldArray.length;i<entityNewArray.length;i++){
        		MyTipGenerator tips= new MyTipGenerator();
        		tips.subTaskIndex=index;
        		tips.title=title;
        		entityNewArray[i].setToolTipText(tips.generateToolTip(dataset, row, column));
        		//System.out.println("Adding: ");
        		//System.out.println(entityNewArray[i].getToolTipText());
        		index++;
        	}
        	
        	//int count = dataset.getSubIntervalCount(row, column);
        	

        	finalState=state;
        	/*int count = dataset.getSubIntervalCount(row, column);
        	for (int subinterval = 0; subinterval < count; subinterval++){
        		super.drawTasks(g2, state, dataArea, plot, domainAxis, rangeAxis, dataset.gets, row, column);
        	}*/
        	//CategoryToolTipGenerator tooltip = getToolTipGenerator(row, column);
    		/*int count = dataset.getSubIntervalCount(row, column);
        	for (int subinterval = 0; subinterval < count; subinterval++){
        		EntityCollection entities = state.getEntityCollection();
        		Collection col=entities.getEntities();
        		Iterator it=col.iterator();
        		while (it.hasNext()){
        			System.out.println(((org.jfree.chart.entity.ChartEntity)it.next()).getToolTipText());
        		}
        	}*/
        }



        
    }
	/*@Override
	public void dispose() {
        if (actions != null && !actions.isEmpty()) {
            ActionBars menus = getArea().getActionBars(MAIN);
            for (SiteAction action : actions) {
                action.setVisible(false);
                menus.remove(action);
            }
            actions = Collections.emptyList();
        }
        selection = null;
        part = null;
        removeAll();
		
	}*/

	@Override
	public boolean aboutToClose() {
		// TODO Auto-generated method stub
		return true;
	}

	/*public boolean addSelection(Selection arg0) {
		selection=arg0;
		// TODO Auto-generated method stub
		return true;
	}*/

	/*public Component asComponent() {
		return this;
		// TODO Auto-generated method stub
		//return null;
	}*/

	public Icon getComponentIcon() {
        try {
            URL resource = HistoryModesPlot.class.getResource(IconResources.HUS_ICON);
            BufferedImage imageIcon = ImageIO.read(resource);
            return new ImageIcon(imageIcon);
    } catch (IOException e) {
           
            e.printStackTrace();
            return null;
    }
	}

	/*@Override
	public EditorPart getPart() {
		// TODO Auto-generated method stub
		return part;
	}*/

	/*@Override
	public Selection getSelection() {
		return selection;
		// TODO Auto-generated method stub
		//return null;
	}*/

	/*@Override
	public String getViewerId() {
		
		// TODO Auto-generated method stub
		return viewerId;
	}*/
    public boolean makeEditorContent() {
        HistoryModes value = getValue();
        try {
                        init(value);
                        return true;
                } catch (Exception e) {
                        e.printStackTrace();
                        return false;
                }
    }


	//@Override
	public void init(HistoryModes value) {
		HistoryModes hd=value;
		/*for (int i=0;i<hd.getAllStates().length;i++){
			System.out.println(hd.getAllStates()[i]);
		}*/
		;
		HistoryModesPlot plot=makePlot("Timeline",hd);
		//this.add(plot);
		//part=arg1;
		//part.setTitle("Time line");
		//arg1.getArea().
		//part.setEditorComponent(plot);
		//this.
		//arg1.getTitle()
		// TODO Auto-generated method stub
		//return true;
	}

	@Override
	public boolean isApplicable(Selection arg0) {
		System.out.println(arg0.getType());

		if (arg0.getType().equals(HistoryModes.class)){
			System.out.println("Is applicable");
			
			return true;
		}
		else return false;
	}

	@Override
	public boolean isSingleton() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setFocused(boolean arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	/*public void setViewerId(String arg0) {
		viewerId=arg0;
		System.out.println("Setviewerid");
		// TODO Auto-generated method stub
		
	}*/
	

    public String getName() {
        String name = super.getName();
        return name == null? getViewerId() : name;
    }
    
    /*protected final EditorArea getArea() {
        return (part == null)? null : part.getArea();
    }*/
    
    public HistoryModesPlot(LayoutManager layout) {
        super(layout);
    }

	@Override
	protected Class<? extends HistoryModes> getVariableType() {
		// TODO Auto-generated method stub
		return HistoryModes.class;
	}

    


}
