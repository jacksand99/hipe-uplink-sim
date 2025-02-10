package vega.hipe.pds;



import java.io.File;

/**
 * This is a class that makes sure we can handle PORs in the Navigator.
 */
public class ImgFile extends File {

    /**
     * Version ID for serialization.
     */
    private static final long serialVersionUID = 1L;

    //-------------
    // Constructors
    //-------------

    /**
     * Construction of a new ImgFile with the given parent and child.
     * 
     * @param parent The parent abstract filename.
     * 
     * @param child The child abstract filename.
     * 
     * @see File#File(String, String)
     */
    public ImgFile(String parent, String child) {
        super(parent, child);
    }

    /**
     * Construction of a new ImgFile with the given filename.
     * 
     * @param pathname The filename.
     * 
     * @see File#File(String)
     */
    public ImgFile(String pathname) {
        super(pathname);
    }

    /**
     * Construction of a new ImgFile with the given parent file and child.
     * 
     * @param parent The parent file.
     * 
     * @param child The child abstract filename.
     * 
     * @see File#File(File, String)
     */
    public ImgFile(File parent, String child) {
        super(parent, child);
    }
}
