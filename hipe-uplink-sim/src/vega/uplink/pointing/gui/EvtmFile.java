package vega.uplink.pointing.gui;

import java.io.File;

/**
 * This is a class that makes sure we can handle Evtms in the Navigator.
 */
public class EvtmFile extends File {

    /**
     * Version ID for serialization.
     */
    private static final long serialVersionUID = 1L;

    //-------------
    // Constructors
    //-------------

    /**
     * Construction of a new EvtmFile with the given parent and child.
     * 
     * @param parent The parent abstract filename.
     * 
     * @param child The child abstract filename.
     * 
     * @see File#File(String, String)
     */
    public EvtmFile(String parent, String child) {
        super(parent, child);
    }

    /**
     * Construction of a new EvtmFile with the given filename.
     * 
     * @param pathname The filename.
     * 
     * @see File#File(String)
     */
    public EvtmFile(String pathname) {
        super(pathname);
    }

    /**
     * Construction of a new EvtmFile with the given parent file and child.
     * 
     * @param parent The parent file.
     * 
     * @param child The child abstract filename.
     * 
     * @see File#File(File, String)
     */
    public EvtmFile(File parent, String child) {
        super(parent, child);
    }
}
