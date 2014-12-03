package vega.hipe.git;
import herschel.ia.gui.kernel.menus.ActionBars;
import herschel.share.interpreter.InterpreterUtil;
import herschel.share.util.Configuration;

import java.io.File;
import java.io.IOException;

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
import org.eclipse.jgit.transport.OpenSshConfig.Host;
//import org.eclipse.jgit.transport.
import org.eclipse.jgit.transport.SshSessionFactory;
import org.eclipse.jgit.transport.URIish;

import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;

import vega.uplink.Properties;

public class HipeGit {
    private String localPath, remotePath, password;
    private Repository localRepo;
    private Git git;
    public static String LOCAL_PATH_PROPERTY="vega.hipe.git.localPath";
    public static String REMOTE_PATH_PROPERTY="vega.hipe.git.remotePath";
    public static String PASSWORD_PROPERTY="vega.hipe.git.password";

    private static HipeGit instance;
    //public static String LOCAL_REPOSITORY_PROPERTY="vega.hipe.git.localPath";
    
    private HipeGit(){
    	try {
			init();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			IllegalArgumentException iae = new IllegalArgumentException("Could not init git:"+e.getMessage());
			iae.initCause(e);
			throw iae;
			//e.printStackTrace();
		}
    }
    
    public static HipeGit getInstance(){
    	if (instance==null) instance=new HipeGit();
    	return instance;
    }
    
    public void init() throws IOException {
    	localPath=Properties.getProperty(LOCAL_PATH_PROPERTY);
    	remotePath=Properties.getProperty(REMOTE_PATH_PROPERTY);
    	password=Properties.getProperty(PASSWORD_PROPERTY);
        /*localPath = "/home/me/repos/mytest";
        remotePath = "git@github.com:me/mytestrepo.git";*/
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
        Git.cloneRepository().setURI(remotePath)
                .setDirectory(new File(localPath)).call();
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
        git.pull().call();
    }
    

}
