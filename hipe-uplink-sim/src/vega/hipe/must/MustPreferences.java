package vega.hipe.must;


import javax.swing.*;

import herschel.ia.gui.kernel.prefs.*;
import herschel.ia.gui.kernel.prefs.handler.IntegerPreferenceHandler;
import herschel.ia.gui.kernel.prefs.handler.StringPreferenceHandler;
import herschel.ia.gui.kernel.util.field.IntegerField;
import herschel.share.util.Configuration;

import javax.swing.GroupLayout.*;

import vega.hipe.logging.VegaLog;
import static javax.swing.GroupLayout.Alignment.BASELINE;


public class MustPreferences extends PreferencesPanel {

    private static final long serialVersionUID = 1L;

    private JTextField    _fieldA;  // text field associated to preference keyA
    private IntegerField _fieldB;  // text field associated to preference keyB
    private JTextField    _fieldC;  // text field associated to preference keyC
    private JTextField    _fieldD;  // text field associated to preference keyD
    private JTextField    _fieldE;  // text field associated to preference keyE
    
    public MustPreferences(){
    	super();
    	VegaLog.info("MustPreferences: Constructor called");
    }
    @Override
    protected void registerHandlers() {
    	VegaLog.info("Must Plugin: Registering Handlers");
        // Preference IP with type String and default value "text"
        registerHandler("must.ip", new StringPreferenceHandler(Configuration.getProperty("vega.must.server.ip", "195.74.166.61"),_fieldA,"vega.must.server.ip"));
        registerHandler("must.port", new IntegerPreferenceHandler(new Integer(Integer.parseInt(Configuration.getProperty("vega.must.server.port", "3306"))),_fieldB,"vega.must.server.port"));

        // Preference port with type Integer and default value 3

        // Preference repository with type Integer and default value 3
        registerHandler("must.repository", new StringPreferenceHandler(Configuration.getProperty("vega.must.server.repository", "rep_flight_herschel_v3_0"),_fieldC,"vega.must.server.repository"));


        // Preference user with type Integer and default value 3
        registerHandler("must.user", new StringPreferenceHandler(Configuration.getProperty("vega.must.server.user", ""),_fieldD,"vega.must.server.user"));


        // Preference password with type Integer and default value 3
        registerHandler("must.password", new StringPreferenceHandler(Configuration.getProperty("vega.must.server.password", ""),_fieldE,"vega.must.server.password"));


    }

    @Override
    protected void makeContent() {
    	VegaLog.info("Must Plugin: make content");
        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        SequentialGroup hGroup = layout.createSequentialGroup();
        SequentialGroup vGroup = layout.createSequentialGroup();
        ParallelGroup hLabelGroup = layout.createParallelGroup();
        ParallelGroup hComboGroup = layout.createParallelGroup();
	ParallelGroup vPropsGroup;

        JLabel    labelA = new JLabel("Server IP:");
	_fieldA = new JTextField();
        vPropsGroup = layout.createParallelGroup(BASELINE);
        vGroup.addGroup(vPropsGroup.addComponent(labelA).addComponent(_fieldA));
        hLabelGroup.addComponent(labelA);
        hComboGroup.addComponent(_fieldA);

        JLabel    labelB = new JLabel("Server Port:");
	_fieldB = new IntegerField();
        vPropsGroup = layout.createParallelGroup(BASELINE);
        vGroup.addGroup(vPropsGroup.addComponent(labelB).addComponent(_fieldB));
        hLabelGroup.addComponent(labelB);
        hComboGroup.addComponent(_fieldB);
        
        JLabel    labelC = new JLabel("Must Repository:");
	_fieldC = new JTextField();
        vPropsGroup = layout.createParallelGroup(BASELINE);
        vGroup.addGroup(vPropsGroup.addComponent(labelC).addComponent(_fieldC));
        hLabelGroup.addComponent(labelC);
        hComboGroup.addComponent(_fieldC);

        JLabel    labelD = new JLabel("User:");
	_fieldD = new JTextField();
        vPropsGroup = layout.createParallelGroup(BASELINE);
        vGroup.addGroup(vPropsGroup.addComponent(labelD).addComponent(_fieldD));
        hLabelGroup.addComponent(labelD);
        hComboGroup.addComponent(_fieldD);

        JLabel    labelE = new JLabel("Password:");
	_fieldE = new JTextField();
        vPropsGroup = layout.createParallelGroup(BASELINE);
        vGroup.addGroup(vPropsGroup.addComponent(labelE).addComponent(_fieldE));
        hLabelGroup.addComponent(labelE);
        hComboGroup.addComponent(_fieldE);


        hGroup.addGroup(hLabelGroup);
        hGroup.addGroup(hComboGroup);
        layout.setHorizontalGroup(hGroup);
        layout.setVerticalGroup(vGroup);

    }
}
