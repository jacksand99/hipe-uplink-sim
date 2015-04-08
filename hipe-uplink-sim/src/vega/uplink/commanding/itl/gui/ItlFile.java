package vega.uplink.commanding.itl.gui;

import java.io.File;

/**
 * This is a class that makes sure we can handle ITLs in the Navigator.
 */
public class ItlFile extends File {

    /**
     * Version ID for serialization.
     */
    private static final long serialVersionUID = 1L;

    //-------------
    // Constructors
    //-------------

    /**
     * Construction of a new ItlFile with the given parent and child.
     * 
     * @param parent The parent abstract filename.
     * 
     * @param child The child abstract filename.
     * 
     * @see File#File(String, String)
     */
    public ItlFile(String parent, String child) {
        super(parent, child);
    }

    /**
     * Construction of a new ItlFile with the given filename.
     * 
     * @param pathname The filename.
     * 
     * @see File#File(String)
     */
    public ItlFile(String pathname) {
        super(pathname);
    }

    /**
     * Construction of a new ItlFile with the given parent file and child.
     * 
     * @param parent The parent file.
     * 
     * @param child The child abstract filename.
     * 
     * @see File#File(File, String)
     */
    public ItlFile(File parent, String child) {
        super(parent, child);
    }
}