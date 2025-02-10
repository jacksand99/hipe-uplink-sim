package vega.hipe.logging;

import java.util.logging.Logger;


public class VegaLog {
	private static final Logger LOG = Logger.getLogger(VegaLog.class.getName());
	
	public static void info(String log){
		LOG.info(log);	
	}
	
	public static void severe(String log){
		LOG.severe(log);	
	}
	public static void warning(String log){
		LOG.warning(log);			
	}
	public static void finest(String log){
		LOG.finest(log);
	}
	public static void finer(String log){
		LOG.finer(log);
	}
	public static void throwing(Class sourceClass,String method,Exception thrown){
		LOG.info("Class "+sourceClass.getName()+" on method "+method+" throwing "+thrown.getClass().getName()+" with message "+thrown.getMessage());
		LOG.throwing(sourceClass.getName(), method, thrown);
	}

	

}
