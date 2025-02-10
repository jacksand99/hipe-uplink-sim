package rosetta.uplink.pointing;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import vega.hipe.gui.xmlutils.HtmlDocument;
import vega.hipe.gui.xmlutils.HtmlEditorKit;
import vega.hipe.mail.SendEmail;
import vega.uplink.Properties;
import vega.uplink.pointing.Pdfm;
import vega.uplink.pointing.Ptr;
import vega.uplink.pointing.PtrUtils;
import vega.uplink.pointing.net.AttitudeGeneratorException;
import vega.uplink.pointing.net.AttitudeGeneratorFDImpl;
import vega.uplink.pointing.net.ErrorBoxPoint;
import vega.uplink.pointing.net.FDClient;
import vega.uplink.track.Fecs;
import herschel.ia.gui.apps.modifier.Modifier;
import herschel.ia.gui.kernel.ParameterValidator;
import herschel.ia.gui.kernel.VariableSelection;
import herschel.ia.task.Task;
import herschel.ia.task.TaskParameter;



public class RosettaPtrCheckTask extends Task {
	private static final Logger LOGGER = Logger.getLogger(RosettaPtrCheckTask.class.getName());

	public RosettaPtrCheckTask(){
		
		super("rosettaPtrCheckTask");
		//List<String> trajectories = Properties.getList(FDClient.TREJECTORIES_PROPERTY);

		setDescription("Check Rosetta PTR againts PTSL, PDFM and FD server ");
		TaskParameter parameter = new TaskParameter("ptr", Ptr.class);
        parameter.setType(TaskParameter.IN);
        parameter.setMandatory(true);
        parameter.setDescription("The PTR "); //6
		TaskParameter ptsl = new TaskParameter("ptsl", Ptr.class);
        ptsl.setType(TaskParameter.IN);
        ptsl.setMandatory(false);
        ptsl.setDescription("The PTSL used for the simulation"); //6
		TaskParameter pdfm = new TaskParameter("pdfm", Pdfm.class);
        pdfm.setType(TaskParameter.IN);
        pdfm.setMandatory(false);
        pdfm.setDescription("The PDFM used for the simulation"); //6
		TaskParameter fecs = new TaskParameter("fecs", Fecs.class);
        fecs.setType(TaskParameter.IN);
        fecs.setMandatory(false);
        fecs.setDescription("The FECS used"); //6

		TaskParameter excl = new TaskParameter("excl", ExclusionPeriod.class);
		excl.setType(TaskParameter.IN);
		excl.setMandatory(false);
		excl.setDescription("The Exclusion Periods file used"); //6

		TaskParameter lander = new TaskParameter("landerVis", LanderVisibility.class);
		lander.setType(TaskParameter.IN);
		lander.setMandatory(false);
		lander.setDescription("The Lander Visibility Periods file used"); //6

		//TaskParameter trajectory = new TaskParameter("trajectory", String.class);
        TaskParameter trajectory = new TaskParameter("trajectory", String.class);
		trajectory.setType(TaskParameter.IN);
		trajectory.setMandatory(false);
		trajectory.setDescription("The trajectory to be used"); //6
		
		TaskParameter eta = new TaskParameter("eta", String.class);
		eta.setType(TaskParameter.IN);
		eta.setMandatory(false);
		eta.setDescription("The values for eta to calculate"); //6
		eta.setDefaultValue("0.0");
        //addTaskParameter(eta);

		TaskParameter zeta = new TaskParameter("zeta", String.class);
		zeta.setType(TaskParameter.IN);
		zeta.setMandatory(false);
		zeta.setDescription("The values for zeta to calculate"); //6
		zeta.setDefaultValue("0.0");
        //addTaskParameter(zeta);

		TaskParameter epsilon = new TaskParameter("epsilon", String.class);
		epsilon.setType(TaskParameter.IN);
		epsilon.setMandatory(false);
		epsilon.setDescription("The values for epsilon to calculate"); //6
		epsilon.setDefaultValue("0.0");
        //addTaskParameter(epsilon);
		TaskParameter defaultPoints = new TaskParameter("defaultPoints", Boolean.class);
		defaultPoints.setType(TaskParameter.IN);
		defaultPoints.setMandatory(true);
		defaultPoints.setDescription("Use the default FD error box points"); //6
		defaultPoints.setDefaultValue(false);
		
		TaskParameter email = new TaskParameter("email", String.class);
		email.setType(TaskParameter.IN);
		email.setMandatory(false);
		email.setDescription("Once executed send email to"); //6
		//email.setDefaultValue("false");
		
		TaskParameter report = new TaskParameter("ptrReport", HtmlDocument.class);
		report.setType(TaskParameter.OUT);
		 addTaskParameter(report);

        addTaskParameter(parameter);
        addTaskParameter(ptsl);
        addTaskParameter(pdfm);
        addTaskParameter(fecs);
        addTaskParameter(excl);
        addTaskParameter(lander);
        addTaskParameter(trajectory);
        addTaskParameter(eta);
        addTaskParameter(zeta);
        addTaskParameter(epsilon);
        addTaskParameter(defaultPoints);
        addTaskParameter(email);

	}
	
	public void execute() {
		String email=(String) getParameter("email").getValue();
		Float[] valuesEta=new Float[1];
		valuesEta[0]=new Float(0);
		Float[] valuesZeta=new Float[1];
		valuesZeta[0]=new Float(0);
		Float[] valuesEpsilon=new Float[1];
		valuesEpsilon[0]=new Float(0);
		
		String eta=(String) getParameter("eta").getValue();
		String zeta=(String) getParameter("zeta").getValue();
		String epsilon=(String) getParameter("epsilon").getValue();
		ExclusionPeriod excl = (ExclusionPeriod) getParameter("excl").getValue();
		if (excl==null) excl=new ExclusionPeriod();
		LanderVisibility lander=(LanderVisibility) getParameter("landerVis").getValue();
		if (lander==null) lander=new LanderVisibility();
		ErrorBoxPoint[] errorBoxPoints;
		boolean useDefaultPoints=(Boolean) getParameter("defaultPoints").getValue();
		if (!useDefaultPoints){
			if (eta!=null){
				String[] etaArray = eta.split(" ");
				valuesEta=new Float[etaArray.length];
				for (int i=0;i<etaArray.length;i++){
					valuesEta[i]=Float.parseFloat(etaArray[i]);
				}
			}
			
			if (zeta!=null){
				String[] zetaArray = zeta.split(" ");
				valuesZeta=new Float[zetaArray.length];
				for (int i=0;i<zetaArray.length;i++){
					valuesZeta[i]=Float.parseFloat(zetaArray[i]);
				}
			}
	
			if (epsilon!=null){
				String[] epsilonArray = epsilon.split(" ");
				valuesEpsilon=new Float[epsilonArray.length];
				for (int i=0;i<epsilonArray.length;i++){
					valuesEpsilon[i]=Float.parseFloat(epsilonArray[i]);
				}
			}
		
			errorBoxPoints=new ErrorBoxPoint[valuesEta.length*valuesZeta.length*valuesEpsilon.length];
			int index=0;
	        for (int e=0;e<valuesEta.length;e++){
	        	for (int z=0;z<valuesZeta.length;z++){
	        		for (int ep=0;ep<valuesEpsilon.length;ep++){
	        			errorBoxPoints[index]=new ErrorBoxPoint(valuesEta[z],valuesZeta[z],valuesEpsilon[ep]);
	        			index++;
	        		}
	        	}
	        }
		}else{
			errorBoxPoints=new ErrorBoxPoint[14];
			errorBoxPoints[0]=new ErrorBoxPoint(0.0f,0.0f,0.0f);
			errorBoxPoints[1]=new ErrorBoxPoint(1.0f,1.0f,1.0f);
			errorBoxPoints[2]=new ErrorBoxPoint(-1.0f,1.0f,1.0f);
			errorBoxPoints[3]=new ErrorBoxPoint(1.0f,-1.0f,1.0f);
			errorBoxPoints[4]=new ErrorBoxPoint(-1.0f,-1.0f,1.0f);
			errorBoxPoints[5]=new ErrorBoxPoint(1.0f,1.0f,-1.0f);
			errorBoxPoints[6]=new ErrorBoxPoint(-1.0f,1.0f,-1.0f);
			errorBoxPoints[7]=new ErrorBoxPoint(1.0f,-1.0f,-1.0f);			
			errorBoxPoints[8]=new ErrorBoxPoint(-1.0f,-1.0f,-1.0f);
			errorBoxPoints[9]=new ErrorBoxPoint(-1.0f,0.0f,1.0f);
			errorBoxPoints[10]=new ErrorBoxPoint(-1.0f,0.0f,0.0f);
			errorBoxPoints[11]=new ErrorBoxPoint(-1.0f,0.0f,-1.0f);
			errorBoxPoints[12]=new ErrorBoxPoint(-1.0f,1.0f,0.0f);
			errorBoxPoints[13]=new ErrorBoxPoint(-1.0f,-1.0f,0.0f);
			for (int eb=0;eb<errorBoxPoints.length;eb++){
				int pointName=eb+1;
				errorBoxPoints[eb].setName("Point "+pointName+": Epsilon,Zeta,Eta "+"("+errorBoxPoints[eb].getEpsilon()+","+errorBoxPoints[eb].getZeta()+","+errorBoxPoints[eb].getEta()+")");
			}
		}



		Ptr ptr = (Ptr) getParameter("ptr").getValue();
        if (ptr == null) {
            throw (new NullPointerException("Missing ptr value"));
        }
        Ptr ptsl=(Ptr ) getParameter("ptsl").getValue();
        Pdfm pdfm=(Pdfm ) getParameter("pdfm").getValue();
        if (pdfm == null) {
        	try{
        		String pdfmName=ptr.getSegments()[0].getPdfm();
        		if (pdfmName==null){
        			throw (new IllegalArgumentException("There is no pfdm defined in the PTR"));
        		}
        		File f=new File(ptr.getPath()+"/"+pdfmName);
        		if (!f.exists()){
        			throw (new IllegalArgumentException("The pfdm defined in the PTR coul not be found at "+f.getAbsolutePath()));
        		}
        		pdfm=PtrUtils.readPdfmfromFile(ptr.getPath()+"/"+pdfmName);
        	}catch (Exception e){
        		IllegalArgumentException iae = new IllegalArgumentException("Error loading PDFM "+e.getMessage());
        		iae.initCause(e);
        		throw(iae);
        	}
        }
        String trajectory= (String) getParameter("trajectory").getValue();
        String message="";
        int totalLoop=errorBoxPoints.length;
        int loop=0;
        //for (int e=0;e<valuesEta.length;e++){
        	//for (int z=0;z<valuesZeta.length;z++){
        		//for (int ep=0;ep<valuesEpsilon.length;ep++){
        		for (int eb=0;eb<errorBoxPoints.length;eb++){
        			this.setProgress(new Float(loop*100/totalLoop).intValue());
        			LOGGER.info(new Float(loop*100/totalLoop).intValue() + "% checks done");
        			loop++;
        			//Float currentEta=valuesEta[e];
        			//Float currentZeta=valuesZeta[z];
        			//Float currentEpsilon=valuesEpsilon[ep];
        	        AttitudeGeneratorFDImpl ag;
        	        String activityCase;
          	        String ptslName="";
        	        if (ptsl!=null) ptslName=ptsl.getName();
        	        try{
        	        if (trajectory==null){
        	        	activityCase=Properties.getProperty(FDClient.TRAJECTORY_PROPERTY);
        	    		try {
        	    			ag = new RosettaAttitudeGenerator(ptr,pdfm,errorBoxPoints[eb]);
        	    		} catch (AttitudeGeneratorException ex) {
        	    			LOGGER.severe(ex.getMessage());
        	    			IllegalArgumentException iae=new IllegalArgumentException(ex.getMessage());
        	    			iae.initCause(ex);
        	    			ex.printStackTrace();
        	    			throw(iae);
        	    			
        	    		}

        	        }else{
        	        	activityCase=trajectory;
        	    		try {
        	    			ag = new RosettaAttitudeGenerator(ptr,pdfm,AttitudeGeneratorFDImpl.getMtpNum(ptr),trajectory,errorBoxPoints[eb]);
        	    		} catch (AttitudeGeneratorException ex) {
        	    			LOGGER.severe(ex.getMessage());
        	    			IllegalArgumentException iae=new IllegalArgumentException(ex.getMessage());
        	    			iae.initCause(ex);
        	    			ex.printStackTrace();
        	    			throw(iae);
        	    			
        	    		}
        	        	
        	        }
        	        Fecs fecs=(Fecs) getParameter("fecs").getValue();
        	        message=message+"<table class=\"gridtable\">";
        	        message=message+"<tr><td>TRAJECTORY</td><td>"+activityCase+"</td></tr>"+
        	        		"<tr><td>PTR</td><td>"+ptr.getName()+"</td></tr>"+
        	        		"<tr><td>PTSL</td><td>"+ptslName+"</td></tr>"+
        	        		"<tr><td>PDFM</td><td>"+pdfm.getName()+"</td></tr>"+
        	        		"<tr><td>EXCL</td><td>"+excl.getName()+"</td></tr>"+
        	        		"<tr><td>Error Box Point</td><td>"+errorBoxPoints[eb].getName()+"</td></tr>";

	        	        if (fecs==null){
	        	        	message=message+"</table>";
	        	        	//message=message+RosettaPtrChecker.checkPtrHTML(ptr, ptsl, pdfm,ag,excl);
	        	        	message=message+RosettaPtrChecker.checkPtrHTML(ptr, ptsl, pdfm,null,ag,excl,lander);
	        	        }
	        	        else message=message+"<tr><td>FECS</td><td>"+fecs.getName()+"</td></tr></table>"+RosettaPtrChecker.checkPtrHTML(ptr, ptsl, pdfm,fecs,ag,excl,lander);
        	        }catch (Exception e2){
        	        	message=message+"<table class=\"gridtable\">";
            	        message=message+""+
            	        		"<tr><td>PTR</td><td>"+ptr.getName()+"</td></tr>"+
            	        		"<tr><td>PTSL</td><td>"+ptslName+"</td></tr>"+
            	        		"<tr><td>PDFM</td><td>"+pdfm.getName()+"</td></tr>"+
            	        		"<tr><td>EXCL</td><td>"+excl.getName()+"</td></tr>"+
            	        		"<tr><td>Error Box Point</td><td>"+errorBoxPoints[eb].getName()+"</td></tr>";
        	        	message=message+"</table>\n";
        	        	message=message+"<font color=red>ERROR:"+e2.getMessage()+"</font>";
        	        	e2.printStackTrace();
        	        }
        		}
        	//}
        //}
        message="<html><body>"+message+"</body><html>";
        LOGGER.info(message);
        HtmlDocument result=new HtmlDocument("Rosetta PTR check",message);
        if (email!=null){
        	try{
        		SendEmail.sendHtmlDocument(email, result);
        	}catch (Exception e){
        		LOGGER.warning("Could not send report by mail to "+email+":"+e.getMessage());
        		e.printStackTrace();
        	}
        }
        HtmlEditorKit kit=new HtmlEditorKit(result);
        getParameter("ptrReport").setValue(result);
 	}
	
	public Map<String,Modifier> getCustomModifiers(){
		JComboModifier modifier = new JComboModifier();
		HashMap<String,Modifier> result=new HashMap<String,Modifier>();
		result.put("trajectory", modifier);
		return result;
	}
	

    private class MessagesFrame extends JFrame{
    	/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		MessagesFrame(String messages){
    		super();
    		final JFrame frame=this;
            final JTextPane pane = new JTextPane();
            pane.setText(messages+"\n\n\n\n");
            JPanel pnl = new JPanel(new BorderLayout());
            JScrollPane scrollPane = new JScrollPane();
            scrollPane.getViewport().add(pane);
            pnl.add( scrollPane, BorderLayout.CENTER );
            pnl.add(new JButton(new AbstractAction("Close") {
                /**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
                public void actionPerformed( ActionEvent e ) {
                	frame.dispose();
                }
            }), BorderLayout.SOUTH);
            this.setContentPane(pnl);
            this.setTitle("PTR Errors");
    		this.pack();
    	}
    }
    
    private static class JComboModifier extends JComboBox implements Modifier {

        /*
         * Version ID for serialization.
         */
        private static final long serialVersionUID = 1L;

        /*
         * Holding the named variables.
         */
        private VariableSelection _selection;

        /*
         * Parameter validator.
         */
        private ParameterValidator parameterValidator;
        
        public JComboModifier() {


            // create the model
            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>();
            //model.addElement(PERCENT);
            //model.addElement("Median Filter");
    		List<String> trajectories = Properties.getList(FDClient.TREJECTORIES_PROPERTY);
    		Iterator<String> it = trajectories.iterator();
    		while (it.hasNext()){
    			model.addElement(it.next());
    		}

            setModel(model);

            setSelectedIndex(0); // prime the default
        }


        /*
         * The construction of a new Distribution.
         */
        public JComboModifier(ItemListener listener) {

            addItemListener(listener);

            // create the model
            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>();
            //model.addElement(PERCENT);
            //model.addElement("Median Filter");
    		List<String> trajectories = Properties.getList(FDClient.TREJECTORIES_PROPERTY);
    		Iterator<String> it = trajectories.iterator();
    		while (it.hasNext()){
    			model.addElement(it.next());
    		}

            setModel(model);

            setSelectedIndex(0); // prime the default
        }

        /*
         * Method that returns the object for this Method.
         * 
         * @return The object for this Method.
         */
        @Override
        public Object getObject() {
        	return getSelectedItem();
            //return getSelectedIndex();
        }

        /*
        * Method that is called by the JModifierSupport to actually show the value.
        * 
        * @param object The object to visualize.
        */
        private void visualize(Object object) {
            setSelectedIndex(1);
        }

        //----------------------------------------------
        // The delegates support to the JModifierSupport
        //----------------------------------------------

        // The internal object is not updated according to the visualized value

        /*
         * Method that sets the given object as object for this Method.
         * 
         * @param object The object to set as object for this Method.
         */
        @Override
        public void setObject(Object o) {
            visualize(o);
        }

        /*
         * Method that returns the variable selection for this Method.
         * 
         * @return The variable selection for this Method.
         */
        @Override
        public VariableSelection getVariableSelection() {
            return _selection;
        }

        /*
         * Method that sets the given selection as the variable selection for this Method.
         * 
         * @param The selection to set as the variable selection for this Method.
         */
        @Override
        public void setVariableSelection(VariableSelection selection) {
            //this.setObject(selection.getValue());
            this._selection = selection;
        }

        /*
         * Method that sets the parameter validaotr for this Method to the given parameter validator. Accepts null (reset).
         * 
         * @param parameterValidator The parameter validator to set as the parameter validator for this Method.
         */
        @Override
        public void setParameterValidator(ParameterValidator parameterValidator) {
            this.parameterValidator = parameterValidator;
        }

        /*
         * Method that returns the parameter validator for this Method. Can be null (not set).
         * 
         * @return The parameter validator for this Method.
         */
        @Override
        public ParameterValidator getParameterValidator() {
            return parameterValidator;
        }
    }


}

