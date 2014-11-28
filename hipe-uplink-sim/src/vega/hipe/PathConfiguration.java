package vega.hipe;



import herschel.share.util.Configuration;

import java.io.File;
import java.util.logging.Logger;

/**
 * This class provides configuration info for paths in the system (the temp dir, the home dir, etc.).<p>
 *
 * <b>Warning:</b>
 * While these should be acting as read-only properties, they can be mistakenly overwritten.<p>
 * Prefer using the corresponding constants in {{FileUtil}} instead.
 * @author pbalm
 */
public class PathConfiguration {
    
    private static final Logger LOG = Logger.getLogger(PathConfiguration.class.getName());
    
    /**
     * Hide constructor.
     */
    private PathConfiguration() {
    }
    
    /**
     * The user home dir.
     * 
     * @return user.home property
     */
    public static File getHomeDir() {
        return new File(Configuration.getProperty("user.home"));
    }

    /**
     * The Java home dir -- the Java installation dir.
     * 
     * @return java.home property
     */
    public static File getJavaHomeDir() {
        return new File(Configuration.getProperty("java.home"));
    }

    /**
     * The user working directory.
     * 
     * @return user.dir property
     */
    public static File getWorkingDir() {
        return new File(Configuration.getProperty("user.dir"));
    }

    /**
     * The temporary directory.
     * 
     * @return var.hcss.workdir if defined, java.io.tmpdir otherwise
     */
    public static File getTempDir() {
        String prop = "var.hcss.workdir";
        if (Configuration.hasProperty(prop)) {
             File tmpDir = new File(Configuration.getProperty(prop));
             
             if (tmpDir.exists()) {
                 if (tmpDir.isDirectory() && tmpDir.canWrite()) {
                     return tmpDir;
                 } else {
                     LOG.info("Temporary directory pointed to by " + prop + " (" + tmpDir
                             + ") is not a writable directory.");
                 }
             } else if (tmpDir.mkdirs()) {
                 return tmpDir;
             } else {
                 LOG.info("Temporary direcotry pointed to by " + prop + " (" + tmpDir
                         + ") does not exist and could not be created.");
             }                 
             
        } else {
            LOG.info("No property: " + prop);
        }
        
        LOG.info("Using default temporary directory from JRE.");
        return new File(Configuration.getProperty("java.io.tmpdir"));
    }

}
