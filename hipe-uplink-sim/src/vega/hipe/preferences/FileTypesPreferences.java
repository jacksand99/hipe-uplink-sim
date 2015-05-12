package vega.hipe.preferences;

import static javax.swing.GroupLayout.Alignment.BASELINE;

import java.awt.Dimension;

import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;

import vega.uplink.Properties;
import herschel.ia.gui.kernel.prefs.PreferencesPanel;
import herschel.ia.gui.kernel.prefs.handler.StringPreferenceHandler;
import herschel.share.util.Configuration;

public class FileTypesPreferences extends PreferencesPanel {
    private JTextField _fieldA;
    private JTextField _fieldB;
    private JTextField _fieldC;
    private JTextField _fieldD;
    private JTextField _fieldE;
    private JTextField _fieldF;
    private JTextField _fieldG;
    private JTextField _fieldH;
    private JTextField _fieldI;
    private JTextField _fieldJ;
    private JTextField _fieldK;
    private JTextField _fieldM;
    private JTextField _fieldN;
    private JTextField _fieldO;
    private JTextField _fieldP;


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

        JLabel    labelA = new JLabel("POR:");
        _fieldA = new JTextField();
        _fieldA.setMaximumSize(new Dimension(400,20));
        vPropsGroup = layout.createParallelGroup(BASELINE);
        vGroup.addGroup(vPropsGroup.addComponent(labelA).addComponent(_fieldA));
        hLabelGroup.addComponent(labelA);
        hComboGroup.addComponent(_fieldA);

        JLabel    labelB = new JLabel("PORG:");
        _fieldB = new JTextField();
        _fieldB.setMaximumSize(new Dimension(400,20));
        vPropsGroup = layout.createParallelGroup(BASELINE);
        vGroup.addGroup(vPropsGroup.addComponent(labelB).addComponent(_fieldB));
        hLabelGroup.addComponent(labelB);
        hComboGroup.addComponent(_fieldB);

        JLabel    labelC = new JLabel("PTR:");
        _fieldC = new JTextField();
        _fieldC.setMaximumSize(new Dimension(400,20));
        vPropsGroup = layout.createParallelGroup(BASELINE);
        vGroup.addGroup(vPropsGroup.addComponent(labelC).addComponent(_fieldC));
        hLabelGroup.addComponent(labelC);
        hComboGroup.addComponent(_fieldC);

        JLabel    labelD = new JLabel("PTSL:");
        _fieldD = new JTextField();
        _fieldD.setMaximumSize(new Dimension(400,20));
        vPropsGroup = layout.createParallelGroup(BASELINE);
        vGroup.addGroup(vPropsGroup.addComponent(labelD).addComponent(_fieldD));
        hLabelGroup.addComponent(labelD);
        hComboGroup.addComponent(_fieldD);

        JLabel    labelE = new JLabel("PDFM:");
        _fieldE = new JTextField();
        _fieldE.setMaximumSize(new Dimension(400,20));
        vPropsGroup = layout.createParallelGroup(BASELINE);
        vGroup.addGroup(vPropsGroup.addComponent(labelE).addComponent(_fieldE));
        hLabelGroup.addComponent(labelE);
        hComboGroup.addComponent(_fieldE);

        JLabel    labelF = new JLabel("EVTM:");
        _fieldF = new JTextField();
        _fieldF.setMaximumSize(new Dimension(400,20));
        vPropsGroup = layout.createParallelGroup(BASELINE);
        vGroup.addGroup(vPropsGroup.addComponent(labelF).addComponent(_fieldF));
        hLabelGroup.addComponent(labelF);
        hComboGroup.addComponent(_fieldF);

        JLabel    labelG = new JLabel("FECS:");
        _fieldG = new JTextField();
        _fieldG.setMaximumSize(new Dimension(400,20));
        vPropsGroup = layout.createParallelGroup(BASELINE);
        vGroup.addGroup(vPropsGroup.addComponent(labelG).addComponent(_fieldG));
        hLabelGroup.addComponent(labelG);
        hComboGroup.addComponent(_fieldG);

        JLabel    labelH = new JLabel("Observation:");
        _fieldH = new JTextField();
        _fieldH.setMaximumSize(new Dimension(400,20));
        vPropsGroup = layout.createParallelGroup(BASELINE);
        vGroup.addGroup(vPropsGroup.addComponent(labelH).addComponent(_fieldH));
        hLabelGroup.addComponent(labelH);
        hComboGroup.addComponent(_fieldH);

        JLabel    labelI = new JLabel("Schedule:");
        _fieldI = new JTextField();
        _fieldI.setMaximumSize(new Dimension(400,20));
        vPropsGroup = layout.createParallelGroup(BASELINE);
        vGroup.addGroup(vPropsGroup.addComponent(labelI).addComponent(_fieldI));
        hLabelGroup.addComponent(labelI);
        hComboGroup.addComponent(_fieldI);

        JLabel    labelJ = new JLabel("Periods:");
        _fieldJ = new JTextField();
        _fieldJ.setMaximumSize(new Dimension(400,20));
        vPropsGroup = layout.createParallelGroup(BASELINE);
        vGroup.addGroup(vPropsGroup.addComponent(labelJ).addComponent(_fieldJ));
        hLabelGroup.addComponent(labelJ);
        hComboGroup.addComponent(_fieldJ);

        JLabel    labelK = new JLabel("PDOR:");
        _fieldK = new JTextField();
        _fieldK.setMaximumSize(new Dimension(400,20));
        vPropsGroup = layout.createParallelGroup(BASELINE);
        vGroup.addGroup(vPropsGroup.addComponent(labelK).addComponent(_fieldK));
        hLabelGroup.addComponent(labelK);
        hComboGroup.addComponent(_fieldK);

        JLabel    labelM = new JLabel("EVF:");
        _fieldM = new JTextField();
        _fieldM.setMaximumSize(new Dimension(400,20));
        vPropsGroup = layout.createParallelGroup(BASELINE);
        vGroup.addGroup(vPropsGroup.addComponent(labelM).addComponent(_fieldM));
        hLabelGroup.addComponent(labelM);
        hComboGroup.addComponent(_fieldM);

        JLabel    labelN = new JLabel("ITL:");
        _fieldN = new JTextField();
        _fieldN.setMaximumSize(new Dimension(400,20));
        vPropsGroup = layout.createParallelGroup(BASELINE);
        vGroup.addGroup(vPropsGroup.addComponent(labelN).addComponent(_fieldN));
        hLabelGroup.addComponent(labelN);
        hComboGroup.addComponent(_fieldN);

        JLabel    labelO = new JLabel("PWPL:");
        _fieldO = new JTextField();
        _fieldO.setMaximumSize(new Dimension(400,20));
        vPropsGroup = layout.createParallelGroup(BASELINE);
        vGroup.addGroup(vPropsGroup.addComponent(labelO).addComponent(_fieldO));
        hLabelGroup.addComponent(labelO);
        hComboGroup.addComponent(_fieldO);

        JLabel    labelP = new JLabel("PWTL:");
        _fieldP = new JTextField();
        _fieldP.setMaximumSize(new Dimension(400,20));
        vPropsGroup = layout.createParallelGroup(BASELINE);
        vGroup.addGroup(vPropsGroup.addComponent(labelP).addComponent(_fieldP));
        hLabelGroup.addComponent(labelP);
        hComboGroup.addComponent(_fieldP);
        
        hGroup.addGroup(hLabelGroup);
        hGroup.addGroup(hComboGroup);
        layout.setHorizontalGroup(hGroup);
        layout.setVerticalGroup(vGroup);

	}
	
	@Override
	protected void registerHandlers() {
    	registerHandler(Properties.POR_FILE_PROPERTY,new StringPreferenceHandler(Configuration.getProperty(Properties.POR_FILE_PROPERTY, ""),_fieldA,Properties.POR_FILE_PROPERTY));
    	registerHandler(Properties.PORG_FILE_PROPERTY,new StringPreferenceHandler(Configuration.getProperty(Properties.PORG_FILE_PROPERTY, ""),_fieldB,Properties.PORG_FILE_PROPERTY));
    	registerHandler(Properties.PTR_FILE_PROPERTY,new StringPreferenceHandler(Configuration.getProperty(Properties.PTR_FILE_PROPERTY, ""),_fieldC,Properties.PTR_FILE_PROPERTY));
    	registerHandler(Properties.PTSL_FILE_PROPERTY,new StringPreferenceHandler(Configuration.getProperty(Properties.PTSL_FILE_PROPERTY, ""),_fieldD,Properties.PTSL_FILE_PROPERTY));
    	registerHandler(Properties.PDFM_FILE_PROPERTY,new StringPreferenceHandler(Configuration.getProperty(Properties.PDFM_FILE_PROPERTY, ""),_fieldE,Properties.PDFM_FILE_PROPERTY));
    	registerHandler(Properties.EVTM_FILE_PROPERTY,new StringPreferenceHandler(Configuration.getProperty(Properties.EVTM_FILE_PROPERTY, ""),_fieldF,Properties.EVTM_FILE_PROPERTY));
    	registerHandler(Properties.FECS_FILE_PROPERTY,new StringPreferenceHandler(Configuration.getProperty(Properties.FECS_FILE_PROPERTY, ""),_fieldG,Properties.FECS_FILE_PROPERTY));
    	registerHandler(Properties.OBS_FILE_PROPERTY,new StringPreferenceHandler(Configuration.getProperty(Properties.OBS_FILE_PROPERTY, ""),_fieldH,Properties.OBS_FILE_PROPERTY));
    	registerHandler(Properties.SCH_FILE_PROPERTY,new StringPreferenceHandler(Configuration.getProperty(Properties.SCH_FILE_PROPERTY, ""),_fieldI,Properties.SCH_FILE_PROPERTY));
    	registerHandler(Properties.PER_FILE_PROPERTY,new StringPreferenceHandler(Configuration.getProperty(Properties.PER_FILE_PROPERTY, ""),_fieldJ,Properties.PER_FILE_PROPERTY));
    	registerHandler(Properties.PDOR_FILE_PROPERTY,new StringPreferenceHandler(Configuration.getProperty(Properties.PDOR_FILE_PROPERTY, ""),_fieldK,Properties.PDOR_FILE_PROPERTY));
    	registerHandler(Properties.EVF_FILE_PROPERTY,new StringPreferenceHandler(Configuration.getProperty(Properties.EVF_FILE_PROPERTY, ""),_fieldM,Properties.EVF_FILE_PROPERTY));
    	registerHandler(Properties.ITL_FILE_PROPERTY,new StringPreferenceHandler(Configuration.getProperty(Properties.ITL_FILE_PROPERTY, ""),_fieldN,Properties.ITL_FILE_PROPERTY));
    	registerHandler(Properties.PWPL_FILE_PROPERTY,new StringPreferenceHandler(Configuration.getProperty(Properties.PWPL_FILE_PROPERTY, ""),_fieldO,Properties.PWPL_FILE_PROPERTY));
    	registerHandler(Properties.PWTL_FILE_PROPERTY,new StringPreferenceHandler(Configuration.getProperty(Properties.PWTL_FILE_PROPERTY, ""),_fieldP,Properties.PWTL_FILE_PROPERTY));

		
	}

}
