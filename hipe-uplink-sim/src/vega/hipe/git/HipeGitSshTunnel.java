package vega.hipe.git;

import com.jcraft.jsch.*;

import herschel.ia.gui.apps.components.util.PopupMessageHandler;
import herschel.share.interpreter.InterpreterUtil;

import java.awt.*;
import java.io.IOException;

import javax.swing.*;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.errors.UnsupportedCredentialItem;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.CredentialItem;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.CredentialsProviderUserInfo;
import org.eclipse.jgit.transport.JschConfigSessionFactory;
import org.eclipse.jgit.transport.OpenSshConfig;
import org.eclipse.jgit.transport.SshSessionFactory;
import org.eclipse.jgit.transport.URIish;
import org.eclipse.jgit.util.FS;

import vega.hipe.logging.VegaLog;
import vega.uplink.Properties;

public class HipeGitSshTunnel extends HipeGit{
    private static String localPath;
    private static String remotePath;
    private static String password;
    private static Repository localRepo;
    private static int LOCAL_PORT=1052;
    public  static String TUNNEL_SERVER_PROPERTY="vega.hipe.git.tunnel.server";
    public  static String TUNNEL_SERVER_PASSWORD="vega.hipe.git.tunnel.password";

    static Tunnel tunnel;

	public static void startTunnel(){
		Tunnel tun=new Tunnel();
		SwingUtilities.invokeLater(tun);
		HipeGit.TUNNEL=true;
		VegaLog.info("Tunnel started");
	}


 
    public void init() throws IOException {
	      try{
	    	  localPath=Properties.getProperty(LOCAL_PATH_PROPERTY);
  	      }catch (Exception e){
  	    	localPath=null;
  	      }
    	  if (localPath==null || localPath.equals("")){
    		  PopupMessageHandler popup = new PopupMessageHandler();
    		  localPath=popup.askForInput(null,"Local git path",System.getProperty("user.home")); 
    	  }

	      try{
	    	  remotePath=Properties.getProperty(REMOTE_PATH_PROPERTY);
  	      }catch (Exception e){
  	    	remotePath=null;
  	      }
    	  if (remotePath==null || remotePath.equals("")){
    		  PopupMessageHandler popup = new PopupMessageHandler();
    		  remotePath=popup.askForInput(null,"Remote git path",System.getProperty("user.name")+"@localhost:"+System.getProperty("user.home")+"/GIT.git"); 
    	  }

        localRepo = new FileRepository(localPath + "/.git");
        JschConfigSessionFactory sessionFactory = new JschConfigSessionFactory() {
        	@Override
        	protected com.jcraft.jsch.Session createSession(OpenSshConfig.Host hc, String user, String host, int port, FS fs) {

        	    try{

        	      OpenSshConfig.Host hs=new OpenSshConfig.Host(){
        	    	  public String getHostName(){
        	    		  return "localhost";
        	    	  }
        	    	  
        	    	  public int getPort(){
        	    		  return 22;
        	    	  }
        	    	  
        	      };
        	      return super.createSession(hs, user, "localhost", LOCAL_PORT, fs);
        	    }
        	    catch(Exception e){
        	    	VegaLog.warning(e.getMessage());
        	      e.printStackTrace();
        	      IllegalArgumentException iae = new IllegalArgumentException(e.getMessage());
        	      iae.initCause(e);
        	      throw iae;
        	    }
        	}
 
        	@Override
        	protected void configure(OpenSshConfig.Host hc, Session session) {
        	    CredentialsProvider provider = new CredentialsProvider() {
        	        @Override
        	        public boolean isInteractive() {
        	            return false;
        	        }

        	        @Override
        	        public boolean supports(CredentialItem... items) {
        	            return true;
        	        }

        	        @Override
        	        public boolean get(URIish uri, CredentialItem... items) throws UnsupportedCredentialItem {
          	  	      try{
          		    	  password=Properties.getProperty(PASSWORD_PROPERTY);
          	  	      }catch (Exception e){
          	  	    	password=null;
          	  	      }
          	    	  if (password==null || password.equals("")){
          	    		  JTextField passwordField=(JTextField)new JPasswordField(20);
          	    		  PopupMessageHandler popup = new PopupMessageHandler();
          	    	      int result=popup.showConfirm(null, passwordField,JOptionPane.OK_CANCEL_OPTION,"Password for "+uri.getHost());
          	    	      if(result==JOptionPane.OK_OPTION){
          	    	    	  password=passwordField.getText();
          	    	      }
          	    	      else{
          	    	    	  return false;
          	    	      }
          	        }

        	        	uri.setPort(LOCAL_PORT);
        	        	uri.setHost("localhost");
        	            for (CredentialItem item : items) {
        	            	if (InterpreterUtil.isInstance(CredentialItem.Password.class, item)){
        	            		((CredentialItem.Password) item).setValue(password.toCharArray());
        	            	}
        	            	if (InterpreterUtil.isInstance(CredentialItem.YesNoType.class, item)){
        	            		((CredentialItem.YesNoType) item).setValue(true);
        	            	}
        	            	if (InterpreterUtil.isInstance(CredentialItem.StringType.class, item)){
        	            		((CredentialItem.StringType) item).setValue(password);
        	            	}

        	            }
        	            return true;
        	        }
        	    };
        	    UserInfo userInfo = new CredentialsProviderUserInfo(session, provider);
        	    session.setUserInfo(userInfo);
        	}


        	};
        	SshSessionFactory.setInstance(sessionFactory);
        git = new Git(localRepo);
        


  }

  public static class MyUserInfo implements UserInfo, UIKeyboardInteractive{
    public String getPassword(){ return passwd; }
    public boolean promptYesNo(String str){
    	PopupMessageHandler popup = new PopupMessageHandler();
    	return popup.confirm(null,str);
    }
  
    String passwd;
    JTextField passwordField=(JTextField)new JPasswordField(20);

    public String getPassphrase(){ return null; }
    public boolean promptPassphrase(String message){ return true; }
    public boolean promptPassword(String message){
    	try{
    		passwd=Properties.getProperty(TUNNEL_SERVER_PASSWORD);
    	}catch (Exception e){
    		passwd=null;
    	}
    	if (password==null || passwd.equals("")){
	    	PopupMessageHandler popup = new PopupMessageHandler();
	      int result=popup.showConfirm(null, passwordField,JOptionPane.OK_CANCEL_OPTION,message);
	      if(result==JOptionPane.OK_OPTION){
	    	  passwd=passwordField.getText();
	    	  return true;
	      }
	      else{
	    	  return false;
	      }
    	}else{
    		return true;
    	}
    }
    public void showMessage(String message){
    	PopupMessageHandler popup = new PopupMessageHandler();
    	popup.showInfo(null,message);
    }
    final GridBagConstraints gbc = 
      new GridBagConstraints(0,0,1,1,1,1,
                             GridBagConstraints.NORTHWEST,
                             GridBagConstraints.NONE,
                             new Insets(0,0,0,0),0,0);
    private Container panel;
    public String[] promptKeyboardInteractive(String destination,
                                              String name,
                                              String instruction,
                                              String[] prompt,
                                              boolean[] echo){
      panel = new JPanel();
      panel.setLayout(new GridBagLayout());

      gbc.weightx = 1.0;
      gbc.gridwidth = GridBagConstraints.REMAINDER;
      gbc.gridx = 0;
      panel.add(new JLabel(instruction), gbc);
      gbc.gridy++;

      gbc.gridwidth = GridBagConstraints.RELATIVE;

      JTextField[] texts=new JTextField[prompt.length];
      for(int i=0; i<prompt.length; i++){
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 0;
        gbc.weightx = 1;
        panel.add(new JLabel(prompt[i]),gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 1;
        if(echo[i]){
          texts[i]=new JTextField(20);
        }
        else{
          texts[i]=new JPasswordField(20);
        }
        panel.add(texts[i], gbc);
        gbc.gridy++;
      }
      PopupMessageHandler popup = new PopupMessageHandler();
      if (popup.showConfirm(null, panel,JOptionPane.OK_CANCEL_OPTION,destination+": "+name)==JOptionPane.OK_OPTION){
          String[] response=new String[prompt.length];
          for(int i=0; i<prompt.length; i++){
            response[i]=texts[i].getText();
      }
	return response;
      }
      else{
        return null;  // cancel
      }
    }
  }
}