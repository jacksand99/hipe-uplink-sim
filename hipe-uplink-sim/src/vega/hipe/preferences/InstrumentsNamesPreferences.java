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
//import herschel.ia.gui.kernel.util.component.TableScrollPane;
import herschel.ia.gui.kernel.prefs.handler.StringPreferenceHandler;
import herschel.share.util.Configuration;

public class InstrumentsNamesPreferences extends PreferencesPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField _fieldA;
	public InstrumentsNamesPreferences(){
		super();
		//Thread.dumpStack();
		//System.out.println("InstrumentsNamesPreferences constructor");
	}
	//TableScrollPane table;
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

        JLabel    labelA = new JLabel("Instrument Name List:");
        _fieldA = new JTextField();
        _fieldA.setMaximumSize(new Dimension(400,20));
        vPropsGroup = layout.createParallelGroup(BASELINE);
        vGroup.addGroup(vPropsGroup.addComponent(labelA).addComponent(_fieldA));
        hLabelGroup.addComponent(labelA);
        hComboGroup.addComponent(_fieldA);
        
        /*JLabel    labelb = new JLabel("Please, be aware that you will have to re-start hipe after you change this property.");
        vPropsGroup = layout.createParallelGroup(BASELINE);
        vGroup.addGroup(vPropsGroup.addComponent(labelb));
        //hLabelGroup.addComponent(labelb);
        //hComboGroup.addComponent(labelb);*/
        
        hGroup.addGroup(hLabelGroup);
        hGroup.addGroup(hComboGroup);
        //hGroup.addComponent(hPropsGroup.addComponent(labelb));
        layout.setHorizontalGroup(hGroup);
        layout.setVerticalGroup(vGroup);

		// TODO Auto-generated method stub
		
	}

	@Override
	protected void registerHandlers() {
    	registerHandler(Properties.INSTRUMENT_NAMES_PROPERTIES,new StringPreferenceHandler(Configuration.getProperty(Properties.INSTRUMENT_NAMES_PROPERTIES, ""),_fieldA,Properties.INSTRUMENT_NAMES_PROPERTIES));

		// TODO Auto-generated method stub
		
	}

}
