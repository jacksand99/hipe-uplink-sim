package vega.hipe.mail;

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

/**
 * Preference panel for the email functions
 * @author jarenas
 *
 */
public class MailPreferencesPanel extends PreferencesPanel {

	private static final long serialVersionUID = 1L;
	private JTextField _fieldA;
    private JTextField _fieldB;


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

        JLabel    labelA = new JLabel("Frrom email address:");
        _fieldA = new JTextField();
        _fieldA.setMaximumSize(new Dimension(400,20));
        vPropsGroup = layout.createParallelGroup(BASELINE);
        vGroup.addGroup(vPropsGroup.addComponent(labelA).addComponent(_fieldA));
        hLabelGroup.addComponent(labelA);
        hComboGroup.addComponent(_fieldA);

        JLabel    labelB = new JLabel("Mail server:");
        _fieldB = new JTextField();
        _fieldB.setMaximumSize(new Dimension(400,20));
        vPropsGroup = layout.createParallelGroup(BASELINE);
        vGroup.addGroup(vPropsGroup.addComponent(labelB).addComponent(_fieldB));
        hLabelGroup.addComponent(labelB);
        hComboGroup.addComponent(_fieldB);

 

        hGroup.addGroup(hLabelGroup);
        hGroup.addGroup(hComboGroup);
        layout.setHorizontalGroup(hGroup);
        layout.setVerticalGroup(vGroup);

	}

	@Override
	protected void registerHandlers() {
    	registerHandler(SendEmail.EMAIL_FROM_PROPERTY,new StringPreferenceHandler(Configuration.getProperty(SendEmail.EMAIL_FROM_PROPERTY, ""),_fieldA,SendEmail.EMAIL_FROM_PROPERTY));
    	registerHandler(SendEmail.EMAIL_SMTP_PROPERTY,new StringPreferenceHandler(Configuration.getProperty(SendEmail.EMAIL_SMTP_PROPERTY, ""),_fieldB,SendEmail.EMAIL_SMTP_PROPERTY));

		
	}

}

