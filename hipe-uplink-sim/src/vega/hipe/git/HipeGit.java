package vega.hipe.git;
import herschel.ia.gui.apps.components.util.PopupMessageHandler;
import herschel.share.interpreter.InterpreterUtil;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.eclipse.jgit.api.*;
import org.eclipse.jgit.api.errors.*;
import org.eclipse.jgit.api.CreateBranchCommand.SetupUpstreamMode;
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

import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;

import vega.hipe.logging.VegaLog;
import vega.uplink.Properties;

public class HipeGit {
    private String localPath;
    private String remotePath;
    private String password;
    private Repository localRepo;
    protected Git git;
    /**
     * vega.hipe.git.localPath
     */
    public static String LOCAL_PATH_PROPERTY="vega.hipe.git.localPath";
    /**
     * vega.hipe.git.remotePath
     */
    public static String REMOTE_PATH_PROPERTY="vega.hipe.git.remotePath";
    /**
     * vega.hipe.git.password
     */
    public static String PASSWORD_PROPERTY="vega.hipe.git.password";
    public static boolean TUNNEL=false;

    private static HipeGit instance;
    
    protected HipeGit(){
    	try {
			init();
		} catch (Exception e) {
			IllegalArgumentException iae = new IllegalArgumentException("Could not init git:"+e.getMessage());
			iae.initCause(e);
			throw iae;
		}
    }
    
    public static HipeGit getInstance(){
    	if (TUNNEL) return getTunnelInstance();
    	if (instance==null) instance=new HipeGit();
    	return instance;
    }
    
    public static HipeGit getTunnelInstance(){
    	if (instance==null) instance=new HipeGitSshTunnel();
    	return instance;
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
        	    	    	  //return true;
        	    	      }
        	    	      else{
        	    	    	  return false;
        	    	      }
        	        }


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

    
    public void gitCreate() throws IOException {
        Repository newRepo = new FileRepository(localPath + ".git");
        newRepo.create();
    }

    
    public void gitClone() throws IOException, GitAPIException {
    	(new Thread(new GitCloneRunnable())).start();
    }

   
    public void add(String myfileS) throws IOException, GitAPIException {
        File myfile = new File(localPath + "/"+myfileS);
        myfile.createNewFile();
        git.add().addFilepattern(myfileS).call();
    }

    
    public void gitCommit(String message) throws IOException, GitAPIException,
            JGitInternalException {
        git.commit().setMessage(message).call();
    }

    
    public void gitPush() throws IOException, JGitInternalException,
            GitAPIException {
        git.push().call();
    }

    
    public void gitTrackMaster() throws IOException, JGitInternalException,
            GitAPIException {
        git.branchCreate().setName("master")
                .setUpstreamMode(SetupUpstreamMode.SET_UPSTREAM)
                .setStartPoint("origin/master").setForce(true).call();
    }

    
    public void gitPull() throws IOException, GitAPIException {
    	(new Thread(new GitPullRunnable())).start();
    }
    
    private class GitPullRunnable implements Runnable {
 

        public void run() {
        	try{
        		VegaLog.info("Starting Git Pull");
        		PullResult pullResult = git.pull().call();
        		if (pullResult!=null){
        			VegaLog.info(pullResult.getFetchResult().getMessages());
	        		List<String> conflicst = pullResult.getMergeResult().getCheckoutConflicts();
	        		if (conflicst!=null){
		        		Iterator<String> it = conflicst.iterator();
		        		while (it.hasNext()){
		        			VegaLog.info("Checkout Conflicts:"+it.next());
		        		}
	        		}
	        		if (pullResult.isSuccessful()){
	        			VegaLog.info("Git pull successful");
	        		}else{
	        			VegaLog.info("Git pull failed");
	        		}
        		}
        		VegaLog.info("Finished Git Pull");
        	}catch (Exception e){
        		IllegalArgumentException iae=new IllegalArgumentException (e.getMessage());
        		VegaLog.severe("Git pull error:"+e.getMessage());
        		iae.initCause(e);
        		throw(iae);
        	}
        }



    }
    
    private class GitCloneRunnable implements Runnable {
    	 

        public void run() {
        	try{
        		VegaLog.info("Starting Git Clone");
        		Git.cloneRepository().setURI(remotePath)
                .setDirectory(new File(localPath)).call();
        		VegaLog.info("Finished Git Clone");
        	}catch (Exception e){
        		IllegalArgumentException iae=new IllegalArgumentException (e.getMessage());
        		VegaLog.severe("Git clone error:"+e.getMessage());
        		iae.initCause(e);
        		throw(iae);
        	}
        }



    }

}
