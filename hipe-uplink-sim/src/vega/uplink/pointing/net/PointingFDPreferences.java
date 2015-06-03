package vega.uplink.pointing.net;

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

public class PointingFDPreferences extends PreferencesPanel{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField _fieldA;
    private JTextField _fieldB;
    private JTextField _fieldE;
    private JTextField _fieldC;
    private JTextField _fieldF;
    private JTextField _fieldG;
    private JTextField _fieldH;

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

        JLabel    labelF = new JLabel("Time out 1 (ms):");
        _fieldF = new JTextField();
        _fieldF.setMaximumSize(new Dimension(400,20));
        vPropsGroup = layout.createParallelGroup(BASELINE);
        vGroup.addGroup(vPropsGroup.addComponent(labelF).addComponent(_fieldF));
        hLabelGroup.addComponent(labelF);
        hComboGroup.addComponent(_fieldF);

        JLabel    labelG = new JLabel("Time out 2 (ms):");
        _fieldG = new JTextField();
        _fieldG.setMaximumSize(new Dimension(400,20));
        vPropsGroup = layout.createParallelGroup(BASELINE);
        vGroup.addGroup(vPropsGroup.addComponent(labelG).addComponent(_fieldG));
        hLabelGroup.addComponent(labelG);
        hComboGroup.addComponent(_fieldG);

        JLabel    labelH = new JLabel("Time out 2 (ms):");
        _fieldH = new JTextField();
        _fieldH.setMaximumSize(new Dimension(400,20));
        vPropsGroup = layout.createParallelGroup(BASELINE);
        vGroup.addGroup(vPropsGroup.addComponent(labelH).addComponent(_fieldH));
        hLabelGroup.addComponent(labelH);
        hComboGroup.addComponent(_fieldH);

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
    	registerHandler(FDClient.MISSION_ID_PROPERTY,new StringPreferenceHandler(Configuration.getProperty(FDClient.MISSION_ID_PROPERTY, ""),_fieldE,FDClient.MISSION_ID_PROPERTY));

		registerHandler(FDClient.FD_SERVER_URL_PROPERTY,new StringPreferenceHandler(Configuration.getProperty(FDClient.FD_SERVER_URL_PROPERTY, ""),_fieldA,FDClient.FD_SERVER_URL_PROPERTY));
     	registerHandler(FDClient.TRAJECTORY_PROPERTY,new StringPreferenceHandler(Configuration.getProperty(FDClient.TRAJECTORY_PROPERTY, ""),_fieldB,FDClient.TRAJECTORY_PROPERTY));
     	registerHandler(FDClient.TREJECTORIES_PROPERTY,new StringPreferenceHandler(Configuration.getProperty(FDClient.TREJECTORIES_PROPERTY, ""),_fieldC,FDClient.TREJECTORIES_PROPERTY));
     	registerHandler(FDClient.TIMEOUT_1_PROPERTY,new StringPreferenceHandler(Configuration.getProperty(FDClient.TIMEOUT_1_PROPERTY, ""),_fieldF,FDClient.TIMEOUT_1_PROPERTY));
     	registerHandler(FDClient.TIMEOUT_2_PROPERTY,new StringPreferenceHandler(Configuration.getProperty(FDClient.TIMEOUT_2_PROPERTY, ""),_fieldG,FDClient.TIMEOUT_2_PROPERTY));
     	registerHandler(FDClient.TIMEOUT_3_PROPERTY,new StringPreferenceHandler(Configuration.getProperty(FDClient.TIMEOUT_3_PROPERTY, ""),_fieldH,FDClient.TIMEOUT_3_PROPERTY));
        
    	//registerHandler("rosetta.uplink.pointing.AttitudeGeneratorFdImpl.tempDir",new FilePreferenceHandler(Configuration.getProperty("rosetta.uplink.pointing.AttitudeGeneratorFdImpl.tempDir", Configuration.getProperty("user.home")),_fieldD,"rosetta.uplink.pointing.AttitudeGeneratorFdImpl.tempDir"));

		// TODO Auto-generated method stub
		
	}

}
