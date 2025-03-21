package vega.hipe.git;

import static javax.swing.GroupLayout.Alignment.BASELINE;





import java.awt.Dimension;

import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import herschel.ia.gui.kernel.prefs.PreferencesPanel;
import herschel.ia.gui.kernel.prefs.handler.StringPreferenceHandler;
import herschel.share.util.Configuration;

public class GitPreferences extends PreferencesPanel {
  private static final long serialVersionUID = 5L;
  private JTextField _fieldA;
  private JTextField _fieldB;
  private JTextField _fieldE;
  private JTextField _fieldF;
  private JTextField _fieldG;

  public GitPreferences(){
  	super();
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

      JLabel    labelA = new JLabel("Local Path:");
      _fieldA = new JTextField();
      _fieldA.setMaximumSize(new Dimension(400,20));
      vPropsGroup = layout.createParallelGroup(BASELINE);
      vGroup.addGroup(vPropsGroup.addComponent(labelA).addComponent(_fieldA));
      hLabelGroup.addComponent(labelA);
      hComboGroup.addComponent(_fieldA);

      JLabel    labelB = new JLabel("Remote Path:");
      _fieldB = new JTextField();
      _fieldB.setMaximumSize(new Dimension(400,20));
      vPropsGroup = layout.createParallelGroup(BASELINE);
      vGroup.addGroup(vPropsGroup.addComponent(labelB).addComponent(_fieldB));
      hLabelGroup.addComponent(labelB);
      hComboGroup.addComponent(_fieldB);


      JLabel    labelE = new JLabel("Git Password:");
      _fieldE = new JPasswordField();
      _fieldE.setMaximumSize(new Dimension(400,20));
      vPropsGroup = layout.createParallelGroup(BASELINE);
      vGroup.addGroup(vPropsGroup.addComponent(labelE).addComponent(_fieldE));
      hLabelGroup.addComponent(labelE);
      hComboGroup.addComponent(_fieldE);
      
      JLabel    labelF = new JLabel("Tunnel server (user@host):");
      _fieldF = new JTextField();
      _fieldF.setMaximumSize(new Dimension(400,20));
      vPropsGroup = layout.createParallelGroup(BASELINE);
      vGroup.addGroup(vPropsGroup.addComponent(labelF).addComponent(_fieldF));
      hLabelGroup.addComponent(labelF);
      hComboGroup.addComponent(_fieldF);

      JLabel    labelG = new JLabel("Tunnel Server Password:");
      _fieldG = new JPasswordField();
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

	@Override
	protected void registerHandlers() {
  	registerHandler(HipeGit.LOCAL_PATH_PROPERTY,new StringPreferenceHandler(Configuration.getProperty(HipeGit.LOCAL_PATH_PROPERTY, ""),_fieldA,HipeGit.LOCAL_PATH_PROPERTY));
     	registerHandler(HipeGit.REMOTE_PATH_PROPERTY,new StringPreferenceHandler(Configuration.getProperty(HipeGit.REMOTE_PATH_PROPERTY, ""),_fieldB,HipeGit.REMOTE_PATH_PROPERTY));
     	registerHandler(HipeGit.PASSWORD_PROPERTY,new StringPreferenceHandler(Configuration.getProperty(HipeGit.PASSWORD_PROPERTY, ""),_fieldE,HipeGit.PASSWORD_PROPERTY));
    	registerHandler(HipeGitSshTunnel.TUNNEL_SERVER_PROPERTY,new StringPreferenceHandler(Configuration.getProperty(HipeGitSshTunnel.TUNNEL_SERVER_PROPERTY, ""),_fieldF,HipeGitSshTunnel.TUNNEL_SERVER_PROPERTY));
    	registerHandler(HipeGitSshTunnel.TUNNEL_SERVER_PASSWORD,new StringPreferenceHandler(Configuration.getProperty(HipeGitSshTunnel.TUNNEL_SERVER_PASSWORD, ""),_fieldG,HipeGitSshTunnel.TUNNEL_SERVER_PASSWORD));
    	 		
	}
}