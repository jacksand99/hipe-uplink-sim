package vega.uplink.commanding.gui;

import java.io.File;

public class MibFile extends File {

    /**
     * Version ID for serialization.
     */
    private static final long serialVersionUID = 1L;

    //-------------
    // Constructors
    //-------------

    /**
     * Construction of a new MibFile with the given parent and child.
     * 
     * @param parent The parent abstract filename.
     * 
     * @param child The child abstract filename.
     * 
     * @see File#File(String, String)
     */
    public MibFile(String parent, String child) {
        super(parent, child);
    }

    /**
     * Construction of a new MibFile with the given filename.
     * 
     * @param pathname The filename.
     * 
     * @see File#File(String)
     */
    public MibFile(String pathname) {
        super(pathname);
    }

    /**
     * Construction of a new MibFile with the given parent file and child.
     * 
     * @param parent The parent file.
     * 
     * @param child The child abstract filename.
     * 
     * @see File#File(File, String)
     */
    public MibFile(File parent, String child) {
        super(parent, child);
    }

}
