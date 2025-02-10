package vega.uplink.commanding.itl.gui;

import java.io.File;

/**
 * This is a class that makes sure we can handle ITLs in the Navigator.
 */
public class EvfFile extends File {

    /**
     * Version ID for serialization.
     */
    private static final long serialVersionUID = 1L;

    //-------------
    // Constructors
    //-------------

    /**
     * Construction of a new EvfFile with the given parent and child.
     * 
     * @param parent The parent abstract filename.
     * 
     * @param child The child abstract filename.
     * 
     * @see File#File(String, String)
     */
    public EvfFile(String parent, String child) {
        super(parent, child);
    }

    /**
     * Construction of a new EvfFile with the given filename.
     * 
     * @param pathname The filename.
     * 
     * @see File#File(String)
     */
    public EvfFile(String pathname) {
        super(pathname);
    }

    /**
     * Construction of a new EvfFile with the given parent file and child.
     * 
     * @param parent The parent file.
     * 
     * @param child The child abstract filename.
     * 
     * @see File#File(File, String)
     */
    public EvfFile(File parent, String child) {
        super(parent, child);
    }
}
