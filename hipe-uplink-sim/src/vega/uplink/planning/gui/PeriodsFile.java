package vega.uplink.planning.gui;

import java.io.File;

public class PeriodsFile extends File {
	/**
	 * This is a class that makes sure we can handle Ptrs in the Navigator.
	 */
	

	    /**
	     * Version ID for serialization.
	     */
	    private static final long serialVersionUID = 1L;

	    //-------------
	    // Constructors
	    //-------------

	    /**
	     * Construction of a new PtrFile with the given parent and child.
	     * 
	     * @param parent The parent abstract filename.
	     * 
	     * @param child The child abstract filename.
	     * 
	     * @see File#File(String, String)
	     */
	    public PeriodsFile(String parent, String child) {
	        super(parent, child);
	    }

	    /**
	     * Construction of a new PtrFile with the given filename.
	     * 
	     * @param pathname The filename.
	     * 
	     * @see File#File(String)
	     */
	    public PeriodsFile(String pathname) {
	        super(pathname);
	    }

	    /**
	     * Construction of a new PtrFile with the given parent file and child.
	     * 
	     * @param parent The parent file.
	     * 
	     * @param child The child abstract filename.
	     * 
	     * @see File#File(File, String)
	     */
	    public PeriodsFile(File parent, String child) {
	        super(parent, child);
	    }
	

}