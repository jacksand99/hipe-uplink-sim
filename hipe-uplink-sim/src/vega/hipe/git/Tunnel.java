package vega.hipe.git;

import java.util.logging.Logger;

import javax.swing.SwingUtilities;

import herschel.ia.gui.apps.components.util.PopupMessageHandler;
import herschel.share.util.Configuration;
import vega.hipe.git.HipeGitSshTunnel.MyUserInfo;
import vega.hipe.logging.VegaLog;
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
	//private static final Logger LOG = Logger.getLogger(Tunnel.class.getName());
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
  	      tunnelHost=thost.substring(thost.indexOf('@')+1);
  	      JSch jsch=new JSch();
  	      Session session;
  	      session=jsch.getSession(tunnelUser, tunnelHost, 22);
  	      UserInfo ui=new MyUserInfo();
  	      session.setUserInfo(ui);
	      session.connect();
	      int assinged_port=session.setPortForwardingL(LOCAL_PORT, this.finalHost, 22);
	      VegaLog.info("localhost:"+assinged_port+" -> "+finalHost+":"+22);
	      init=true;
	    }
	    catch(Exception e){
	    	VegaLog.info(e.getMessage());
	      e.printStackTrace();
	      IllegalArgumentException iae = new IllegalArgumentException(e.getMessage());
	      iae.initCause(e);
	      throw iae;
	    }		
	}
	
}
