package vega.hipe.git;

import java.util.logging.Logger;

import javax.swing.SwingUtilities;

import herschel.ia.gui.apps.components.util.PopupMessageHandler;
import herschel.share.util.Configuration;
import vega.hipe.git.HipeGitSshTunnel.MyUserInfo;
import vega.uplink.Properties;
import vega.uplink.commanding.PorUtils;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;

class Tunnel extends Thread{
	String tunnelHost;
	String finalHost;
	String user;
	Channel channel;
	Session session;
	private int LOCAL_PORT=1052;
	private static final Logger LOG = Logger.getLogger(PorUtils.class.getName());
	boolean init=false;

	
    public void stopTunnel() {
    	session.disconnect();
    }
	
	public void run(){

	    try{
	    	String remotePath;
		      try{
		    	  remotePath=Properties.getProperty(HipeGit.REMOTE_PATH_PROPERTY);
	  	      }catch (Exception e){
	  	    	remotePath=null;
	  	      }
	    	  if (remotePath==null || remotePath.equals("")){
	    		  PopupMessageHandler popup = new PopupMessageHandler();
	    		  remotePath=popup.askForInput(null,"Remote git path",System.getProperty("user.name")+"@localhost:"+System.getProperty("user.home")+"/GIT.git"); 
	    	  }

	    	//String remotePath=Properties.getProperty("vega.hipe.git.remotePath");
	    	finalHost=remotePath.substring(remotePath.indexOf("@")+1,remotePath.indexOf(":"));
  	      String tunnelHost=null;
  	      PopupMessageHandler popup = new PopupMessageHandler();
  	    String thost;
  	      try{
  	    	  thost=Properties.getProperty(HipeGitSshTunnel.TUNNEL_SERVER_PROPERTY);
  	      }catch (Exception e){
  	    	  thost=null;
  	      }
    	  if (thost==null || thost.equals("")){
    		thost=popup.askForInput(null,"Enter username@hostname",System.getProperty("user.name")+"@localhost"); 
    	  }

  	      String tunnelUser=thost.substring(0, thost.indexOf('@'));
  	      //System.out.println("*"+thost);
  	      //System.out.println("**"+tunnelUser);
  	      tunnelHost=thost.substring(thost.indexOf('@')+1);
  	      //System.out.println("****"+tunnelHost);
  	      
  	      
  	      JSch jsch=new JSch();
  	      //System.out.println("Starting thread session:"+user+"@"+tunnelHost+" 22 ");
  	      Session session;
  	      session=jsch.getSession(tunnelUser, tunnelHost, 22);
  	      UserInfo ui=new MyUserInfo();
  	      session.setUserInfo(ui);

	      session.connect();

	      //channel=session.openChannel("shell");
	      //channel.connect();
	      
	      int assinged_port=session.setPortForwardingL(LOCAL_PORT, this.finalHost, 22);
	      //session.setPortForwardingR(22, lhost, lport);
	      LOG.info("localhost:"+assinged_port+" -> "+finalHost+":"+22);
	      init=true;
	    }
	    catch(Exception e){
	    	LOG.info(e.getMessage());
	      e.printStackTrace();
	      IllegalArgumentException iae = new IllegalArgumentException(e.getMessage());
	      iae.initCause(e);
	      throw iae;
	    }		
	}
	
}
