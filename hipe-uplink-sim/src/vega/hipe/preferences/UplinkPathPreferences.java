package vega.hipe.preferences;

import java.awt.Dimension;

import javax.swing.*;

import herschel.ia.gui.kernel.util.field.FileSelectionMode;
import herschel.ia.gui.kernel.prefs.*;
import herschel.ia.gui.kernel.prefs.handler.FilePreferenceHandler;
import herschel.ia.gui.kernel.prefs.handler.StringPreferenceHandler;
import herschel.ia.gui.kernel.util.component.FilePathPanel;
import herschel.share.swing.*;
import herschel.share.util.Configuration;

import javax.swing.GroupLayout.*;

import vega.uplink.Properties;
import static javax.swing.GroupLayout.Alignment.BASELINE;


public class UplinkPathPreferences extends PreferencesPanel {

    private static final long serialVersionUID = 1L;
    private FilePathPanel _fieldA;
    private FilePathPanel _fieldB;
    private FilePathPanel _fieldC;
    private FilePathPanel _fieldD;
    private FilePathPanel _fieldE;
    private FilePathPanel _fieldF;
    private JTextField _fieldG;

    // private JTextField    _fieldA;  // text field associated to preference keyA
   /* private JTextField    _fieldB;  // text field associated to preference keyB
    private JTextField    _fieldC;  // text field associated to preference keyC
    private JTextField    _fieldD;  // text field associated to preference keyD
    private JTextField    _fieldE;  // text field associated to preference keyE
    private JTextField    _fieldF;  // text field associated to preference keyE*/

    @Override
    protected void registerHandlers() {
    	registerHandler(Properties.DEFAULT_FECS_FILE,new FilePreferenceHandler(Configuration.getProperty(Properties.DEFAULT_FECS_FILE, ""),_fieldA,Properties.DEFAULT_FECS_FILE));
    	registerHandler(Properties.PWPL_FILE,new FilePreferenceHandler(Configuration.getProperty(Properties.PWPL_FILE, ""),_fieldB,Properties.PWPL_FILE));
    	registerHandler(Properties.ORCD_FILE,new FilePreferenceHandler(Configuration.getProperty(Properties.ORCD_FILE, ""),_fieldC,Properties.ORCD_FILE));
    	registerHandler(Properties.DEFAULT_PLANNING_DIRECTORY,new FilePreferenceHandler(Configuration.getProperty(Properties.DEFAULT_PLANNING_DIRECTORY, Configuration.getProperty("user.home")),_fieldD,Properties.DEFAULT_PLANNING_DIRECTORY));
       	registerHandler(Properties.DEFAULT_EVT_DIRECTORY,new FilePreferenceHandler(Configuration.getProperty(Properties.DEFAULT_EVT_DIRECTORY, Configuration.getProperty("user.home")),_fieldE,Properties.DEFAULT_EVT_DIRECTORY));
       	registerHandler(Properties.MIB_LOCATION,new FilePreferenceHandler(Configuration.getProperty(Properties.MIB_LOCATION, Configuration.getProperty("user.home")),_fieldF,Properties.MIB_LOCATION));
    	registerHandler(Properties.ANTENNA_PRIORITY_COMMAND,new StringPreferenceHandler(Configuration.getProperty(Properties.ANTENNA_PRIORITY_COMMAND, ""),_fieldG,Properties.ANTENNA_PRIORITY_COMMAND));

    	// Preference IP with type String and default value "text"
        /*registerHandler(PropertyNames.DEFAULT_FECS_FILE, new AbstractPreferenceHandler<String>(Configuration.getProperty(PropertyNames.DEFAULT_FECS_FILE, "")) {
            public String getValue() { return _fieldA.getText(); }
            public void setValue(String value) { _fieldA.setText(value); }
        });

        // Preference port with type Integer and default value 3
        registerHandler(PropertyNames.PWPL_FILE, new AbstractPreferenceHandler<String>(Configuration.getProperty(PropertyNames.PWPL_FILE, "")) {
            public String getValue() { return _fieldB.getText(); }
            public void setValue(String value) { _fieldB.setText(value); }
        });

        // Preference repository with type Integer and default value 3
        registerHandler(PropertyNames.ORCD_FILE, new AbstractPreferenceHandler<String>(Configuration.getProperty(PropertyNames.ORCD_FILE, "")) {
            public String getValue() { return _fieldC.getText(); }
            public void setValue(String value) { _fieldC.setText(value); }
        });

        // Preference user with type Integer and default value 3
        registerHandler(PropertyNames.DEFAULT_PLANNING_DIRECTORY, new AbstractPreferenceHandler<String>(Configuration.getProperty(PropertyNames.DEFAULT_PLANNING_DIRECTORY, "")) {
            public String getValue() { return _fieldD.getText(); }
            public void setValue(String value) { _fieldD.setText(value); }
        });

        // Preference password with type Integer and default value 3
        registerHandler(PropertyNames.DEFAULT_EVT_DIRECTORY, new AbstractPreferenceHandler<String>(Configuration.getProperty(PropertyNames.DEFAULT_EVT_DIRECTORY, "")) {
            public String getValue() { return _fieldE.getText(); }
            public void setValue(String value) { _fieldE.setText(value); }
        });
        // Preference password with type Integer and default value 3
        registerHandler(PropertyNames.MIB_LOCATION, new AbstractPreferenceHandler<String>(Configuration.getProperty(PropertyNames.MIB_LOCATION, "")) {
            public String getValue() { return _fieldF.getText(); }
            public void setValue(String value) { _fieldF.setText(value); }
        });*/

    }

    @Override
    protected void makeContent() {
        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        SequentialGroup hGroup = layout.createSequentialGroup();
        SequentialGroup vGroup = layout.createSequentialGroup();
        ParallelGroup hLabelGroup = layout.createParallelGroup();
        ParallelGroup hComboGroup = layout.createParallelGroup();
        ParallelGroup vPropsGroup;

        JLabel    labelA = new JLabel("Default FECS File:");
        _fieldA = new FilePathPanel(FileSelectionMode.ALL);
        _fieldA.setMaximumSize(new Dimension(400,20));
        vPropsGroup = layout.createParallelGroup(BASELINE);
        vGroup.addGroup(vPropsGroup.addComponent(labelA).addComponent(_fieldA));
        hLabelGroup.addComponent(labelA);
        hComboGroup.addComponent(_fieldA);

        JLabel    labelB = new JLabel("PWPL File:");
        _fieldB = new FilePathPanel(FileSelectionMode.ALL);
        _fieldB.setMaximumSize(new Dimension(400,20));
        vPropsGroup = layout.createParallelGroup(BASELINE);
        vGroup.addGroup(vPropsGroup.addComponent(labelB).addComponent(_fieldB));
        hLabelGroup.addComponent(labelB);
        hComboGroup.addComponent(_fieldB);
        
        JLabel    labelC = new JLabel("ORCD File:");
        _fieldC = new FilePathPanel(FileSelectionMode.ALL);
        _fieldC.setMaximumSize(new Dimension(400,20));
        vPropsGroup = layout.createParallelGroup(BASELINE);
        vGroup.addGroup(vPropsGroup.addComponent(labelC).addComponent(_fieldC));
        hLabelGroup.addComponent(labelC);
        hComboGroup.addComponent(_fieldC);

        JLabel    labelD = new JLabel("Default Planning Directory:");
        _fieldD = new FilePathPanel(FileSelectionMode.DIRECTORY);
        _fieldD.setMaximumSize(new Dimension(400,20));
        vPropsGroup = layout.createParallelGroup(BASELINE);
        vGroup.addGroup(vPropsGroup.addComponent(labelD).addComponent(_fieldD));
        hLabelGroup.addComponent(labelD);
        hComboGroup.addComponent(_fieldD);

        JLabel    labelE = new JLabel("Default EVT Directory:");
        _fieldE =  new FilePathPanel(FileSelectionMode.DIRECTORY);
        _fieldE.setMaximumSize(new Dimension(400,20));
        vPropsGroup = layout.createParallelGroup(BASELINE);
        vGroup.addGroup(vPropsGroup.addComponent(labelE).addComponent(_fieldE));
        hLabelGroup.addComponent(labelE);
        hComboGroup.addComponent(_fieldE);

        JLabel    labelF = new JLabel("MIB location:");
        _fieldF =  new FilePathPanel(FileSelectionMode.DIRECTORY);
        _fieldF.setMaximumSize(new Dimension(400,20));
        vPropsGroup = layout.createParallelGroup(BASELINE);
        vGroup.addGroup(vPropsGroup.addComponent(labelF).addComponent(_fieldF));
        hLabelGroup.addComponent(labelF);
        hComboGroup.addComponent(_fieldF);

        JLabel    labelG = new JLabel("Dump Priority Command:");
        _fieldG =  new JTextField();
        _fieldG.setMaximumSize(new Dimension(400,20));
        vPropsGroup = layout.createParallelGroup(BASELINE);
        vGroup.addGroup(vPropsGroup.addComponent(labelG).addComponent(_fieldG));
        hLabelGroup.addComponent(labelG);
        hComboGroup.addComponent(_fieldG);


        hGroup.addGroup(hLabelGroup);
        hGroup.addGroup(hComboGroup);
        layout.setHorizontalGroup(hGroup);
        layout.setVerticalGroup(vGroup);

    }
}

