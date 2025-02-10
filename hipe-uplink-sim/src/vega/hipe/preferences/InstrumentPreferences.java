package vega.hipe.preferences;

import static javax.swing.GroupLayout.Alignment.BASELINE;




import java.awt.Dimension;
import java.util.StringTokenizer;

import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.JTextField;

import vega.hipe.logging.VegaLog;
import vega.uplink.Properties;
import herschel.ia.gui.kernel.prefs.PreferencesPanel;
import herschel.ia.gui.kernel.prefs.handler.StringPreferenceHandler;
import herschel.ia.gui.kernel.prefs.handler.ColorPreferenceHandler;
import herschel.ia.gui.kernel.util.component.ColorButton;
import herschel.ia.gui.kernel.util.field.LongField;
//import herschel.share.swing.JLongField;
import herschel.share.util.Configuration;

public class InstrumentPreferences extends PreferencesPanel {
	private String instrument;
    private static final long serialVersionUID = 5L;
    private JTextField _fieldA;
    private JTextField _fieldB;
    private ColorButton _fieldC;
    private LongField _fieldD;
    private JTextField _fieldE;
    //private static final Logger LOG = Logger.getLogger(InstrumentPreferences.class.getName());
    public InstrumentPreferences(){
    	super();
    }
	@Override
	protected void makeContent() {
		VegaLog.info("Make content for config panel of instrument "+instrument);
		System.out.println("Make content for config panel of instrument "+instrument);
		String categoryPath = this.getCategoryPath();
		StringTokenizer tokenizer=new StringTokenizer(categoryPath,"/");
		while (tokenizer.hasMoreTokens()){
			instrument=tokenizer.nextToken();
		}
        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        SequentialGroup hGroup = layout.createSequentialGroup();
        SequentialGroup vGroup = layout.createSequentialGroup();
        ParallelGroup hLabelGroup = layout.createParallelGroup();
        ParallelGroup hComboGroup = layout.createParallelGroup();
        ParallelGroup vPropsGroup;

        JLabel    labelA = new JLabel("Instrument Acronym:");
        _fieldA = new JTextField();
        _fieldA.setMaximumSize(new Dimension(400,20));
        vPropsGroup = layout.createParallelGroup(BASELINE);
        vGroup.addGroup(vPropsGroup.addComponent(labelA).addComponent(_fieldA));
        hLabelGroup.addComponent(labelA);
        hComboGroup.addComponent(_fieldA);

        JLabel    labelB = new JLabel("Mode Name Identifiers:");
        _fieldB = new JTextField();
        _fieldB.setMaximumSize(new Dimension(400,20));
        vPropsGroup = layout.createParallelGroup(BASELINE);
        vGroup.addGroup(vPropsGroup.addComponent(labelB).addComponent(_fieldB));
        hLabelGroup.addComponent(labelB);
        hComboGroup.addComponent(_fieldB);

        JLabel    labelC = new JLabel("Instrument Color:");
        _fieldC = new ColorButton();
        _fieldC.setMaximumSize(new Dimension(400,20));
        vPropsGroup = layout.createParallelGroup(BASELINE);
        vGroup.addGroup(vPropsGroup.addComponent(labelC).addComponent(_fieldC));
        hLabelGroup.addComponent(labelC);
        hComboGroup.addComponent(_fieldC);

        JLabel    labelD = new JLabel("Packet Store Size:");
        _fieldD = new LongField();
        _fieldD.setMaximumSize(new Dimension(400,20));
        vPropsGroup = layout.createParallelGroup(BASELINE);
        vGroup.addGroup(vPropsGroup.addComponent(labelD).addComponent(_fieldD));
        hLabelGroup.addComponent(labelD);
        hComboGroup.addComponent(_fieldD);

        JLabel    labelE = new JLabel("Dump Priority Parameter:");
        _fieldE = new JTextField();
        _fieldE.setMaximumSize(new Dimension(400,20));
        vPropsGroup = layout.createParallelGroup(BASELINE);
        vGroup.addGroup(vPropsGroup.addComponent(labelE).addComponent(_fieldE));
        hLabelGroup.addComponent(labelE);
        hComboGroup.addComponent(_fieldE);
        hGroup.addGroup(hLabelGroup);
        hGroup.addGroup(hComboGroup);
        layout.setHorizontalGroup(hGroup);
        layout.setVerticalGroup(vGroup);
		
	}

	@Override
	protected void registerHandlers() {
		VegaLog.info("registering handlers for "+instrument);
    	registerHandler(Properties.SUBINSTRUMENT_ACRONYM_PROPERTY_PREFIX+instrument,new StringPreferenceHandler(Configuration.getProperty(Properties.SUBINSTRUMENT_ACRONYM_PROPERTY_PREFIX+instrument, ""),_fieldA,Properties.SUBINSTRUMENT_ACRONYM_PROPERTY_PREFIX+instrument));
       	registerHandler(Properties.SUBINSTRUMENT_MODE_START_PROPERTY_PREFIX+instrument,new StringPreferenceHandler(Configuration.getProperty(Properties.SUBINSTRUMENT_MODE_START_PROPERTY_PREFIX+instrument, ""),_fieldB,Properties.SUBINSTRUMENT_MODE_START_PROPERTY_PREFIX+instrument));
       	registerHandler(Properties.ANTENNA_PRIORITY_PARAMETER_PREFIX+instrument,new StringPreferenceHandler(Configuration.getProperty(Properties.ANTENNA_PRIORITY_PARAMETER_PREFIX+instrument, ""),_fieldE,Properties.ANTENNA_PRIORITY_PARAMETER_PREFIX+instrument));
      	registerHandler(Properties.SSMM_PACKETSTORE_PREFIX+instrument,new StringPreferenceHandler(Configuration.getProperty(Properties.SSMM_PACKETSTORE_PREFIX+instrument, ""),_fieldD,Properties.SSMM_PACKETSTORE_PREFIX+instrument));
    	registerHandler(Properties.SUBINSTRUMENT_COLOR_PROPERTY_PREFIX+instrument,new ColorPreferenceHandler(Properties.getColor(Properties.SUBINSTRUMENT_COLOR_PROPERTY_PREFIX+instrument),_fieldC,Properties.SUBINSTRUMENT_COLOR_PROPERTY_PREFIX+instrument));
		
	}
	
	public void setInstrument(String instrument){
		VegaLog.info("Setting instrument "+instrument);
		this.instrument=instrument;
	}

}
