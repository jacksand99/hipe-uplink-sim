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

        hGroup.addGroup(hLabelGroup);
        hGroup.addGroup(hComboGroup);
        layout.setHorizontalGroup(hGroup);
        layout.setVerticalGroup(vGroup);

	}

	@Override
	protected void registerHandlers() {
    	registerHandler("vega.file.type.POR",new StringPreferenceHandler(Configuration.getProperty("vega.file.type.POR", ""),_fieldA,"vega.file.type.POR"));
    	registerHandler("vega.file.type.PORG",new StringPreferenceHandler(Configuration.getProperty("vega.file.type.PORG", ""),_fieldB,"vega.file.type.PORG"));
    	registerHandler("vega.file.type.PTR",new StringPreferenceHandler(Configuration.getProperty("vega.file.type.PTR", ""),_fieldC,"vega.file.type.PTR"));
    	registerHandler("vega.file.type.PTSL",new StringPreferenceHandler(Configuration.getProperty("vega.file.type.PTSL", ""),_fieldD,"vega.file.type.PTSL"));
    	registerHandler("vega.file.type.PDFM",new StringPreferenceHandler(Configuration.getProperty("vega.file.type.PDFM", ""),_fieldE,"vega.file.type.PDFM"));
    	registerHandler("vega.file.type.EVTM",new StringPreferenceHandler(Configuration.getProperty("vega.file.type.EVTM", ""),_fieldF,"vega.file.type.EVTM"));
    	registerHandler("vega.file.type.FECS",new StringPreferenceHandler(Configuration.getProperty("vega.file.type.FECS", ""),_fieldG,"vega.file.type.FECS"));
    	registerHandler("vega.file.type.OBS",new StringPreferenceHandler(Configuration.getProperty("vega.file.type.OBS", ""),_fieldH,"vega.file.type.OBS"));
    	registerHandler("vega.file.type.SCH",new StringPreferenceHandler(Configuration.getProperty("vega.file.type.SCH", ""),_fieldI,"vega.file.type.SCH"));

		
	}

}
