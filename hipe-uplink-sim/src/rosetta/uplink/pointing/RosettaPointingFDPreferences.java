package rosetta.uplink.pointing;

import static javax.swing.GroupLayout.Alignment.BASELINE;

import java.awt.Dimension;

import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;

//import vega.uplink.Properties;
import herschel.ia.gui.kernel.prefs.PreferencesPanel;
import herschel.ia.gui.kernel.prefs.handler.FilePreferenceHandler;
import herschel.ia.gui.kernel.prefs.handler.StringPreferenceHandler;
//import herschel.ia.gui.kernel.util.component.ColorButton;
import herschel.ia.gui.kernel.util.component.FilePathPanel;
import herschel.ia.gui.kernel.util.field.FileSelectionMode;
import herschel.share.util.Configuration;

public class RosettaPointingFDPreferences extends PreferencesPanel{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField _fieldA;
    private JTextField _fieldB;
    private JTextField _fieldE;
    private JTextField _fieldC;

    //private FilePathPanel _fieldD;


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

        JLabel    labelE = new JLabel("Mission id:");
        _fieldE = new JTextField();
        _fieldE.setMaximumSize(new Dimension(400,20));
        vPropsGroup = layout.createParallelGroup(BASELINE);
        vGroup.addGroup(vPropsGroup.addComponent(labelE).addComponent(_fieldE));
        hLabelGroup.addComponent(labelE);
        hComboGroup.addComponent(_fieldE);

        JLabel    labelA = new JLabel("FD server URL:");
        _fieldA = new JTextField();
        _fieldA.setMaximumSize(new Dimension(400,20));
        vPropsGroup = layout.createParallelGroup(BASELINE);
        vGroup.addGroup(vPropsGroup.addComponent(labelA).addComponent(_fieldA));
        hLabelGroup.addComponent(labelA);
        hComboGroup.addComponent(_fieldA);

        JLabel    labelB = new JLabel("Activity Case:");
        _fieldB = new JTextField();
        _fieldB.setMaximumSize(new Dimension(400,20));
        vPropsGroup = layout.createParallelGroup(BASELINE);
        vGroup.addGroup(vPropsGroup.addComponent(labelB).addComponent(_fieldB));
        hLabelGroup.addComponent(labelB);
        hComboGroup.addComponent(_fieldB);

        JLabel    labelC = new JLabel("Trajectories:");
        _fieldC = new JTextField();
        _fieldC.setMaximumSize(new Dimension(400,20));
        vPropsGroup = layout.createParallelGroup(BASELINE);
        vGroup.addGroup(vPropsGroup.addComponent(labelC).addComponent(_fieldC));
        hLabelGroup.addComponent(labelC);
        hComboGroup.addComponent(_fieldC);

        /*JLabel    labelC = new JLabel("Temp Directory:");
        _fieldD = new FilePathPanel(FileSelectionMode.DIRECTORY);
        _fieldD.setMaximumSize(new Dimension(400,20));
        vPropsGroup = layout.createParallelGroup(BASELINE);
        vGroup.addGroup(vPropsGroup.addComponent(labelC).addComponent(_fieldD));
        hLabelGroup.addComponent(labelC);
        hComboGroup.addComponent(_fieldD);*/
        hGroup.addGroup(hLabelGroup);
        hGroup.addGroup(hComboGroup);
        layout.setHorizontalGroup(hGroup);
        layout.setVerticalGroup(vGroup);

		// TODO Auto-generated method stub
		
	}

	@Override
	protected void registerHandlers() {
    	registerHandler("rosetta.uplink.pointing.AttitudeGeneratorFdImpl.mission",new StringPreferenceHandler(Configuration.getProperty("rosetta.uplink.pointing.AttitudeGeneratorFdImpl.mission", ""),_fieldE,"rosetta.uplink.pointing.AttitudeGeneratorFdImpl.mission"));

		registerHandler("rosetta.uplink.pointing.AttitudeGeneratorFdImpl.serverUrl",new StringPreferenceHandler(Configuration.getProperty("rosetta.uplink.pointing.AttitudeGeneratorFdImpl.serverUrl", ""),_fieldA,"rosetta.uplink.pointing.AttitudeGeneratorFdImpl.serverUrl"));
     	registerHandler("rosetta.uplink.pointing.AttitudeGeneratorFdImpl.activityCase",new StringPreferenceHandler(Configuration.getProperty("rosetta.uplink.pointing.AttitudeGeneratorFdImpl.activityCase", ""),_fieldB,"rosetta.uplink.pointing.AttitudeGeneratorFdImpl.activityCase"));
     	registerHandler("rosetta.uplink.pointing.AttitudeGeneratorFdImpl.trajectories",new StringPreferenceHandler(Configuration.getProperty("rosetta.uplink.pointing.AttitudeGeneratorFdImpl.trajectories", ""),_fieldC,"rosetta.uplink.pointing.AttitudeGeneratorFdImpl.trajectories"));
        
    	//registerHandler("rosetta.uplink.pointing.AttitudeGeneratorFdImpl.tempDir",new FilePreferenceHandler(Configuration.getProperty("rosetta.uplink.pointing.AttitudeGeneratorFdImpl.tempDir", Configuration.getProperty("user.home")),_fieldD,"rosetta.uplink.pointing.AttitudeGeneratorFdImpl.tempDir"));

		// TODO Auto-generated method stub
		
	}

}
