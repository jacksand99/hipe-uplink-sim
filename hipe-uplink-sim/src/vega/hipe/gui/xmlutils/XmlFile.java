/*

   Licensed under GNU Lesser General Public License (http://www.gnu.org/licenses/lgpl.html)

 */
package vega.hipe.gui.xmlutils;

import java.io.File;

/**
 * This is a class that makes sure we can handle xml files in the Navigator.
 */
public class XmlFile extends File {

    /**
     * Version ID for serialization.
     */
    private static final long serialVersionUID = 1L;

    //-------------
    // Constructors
    //-------------

    /**
     * Construction of a new  xml file with the given parent and child.
     * 
     * @param parent The parent abstract filename.
     * 
     * @param child The child abstract filename.
     * 
     * @see File#File(String, String)
     */
    public XmlFile(String parent, String child) {
        super(parent, child);
    }

    /**
     * Construction of a new  xml file with the given filename.
     * 
     * @param pathname The filename.
     * 
     * @see File#File(String)
     */
    public XmlFile(String pathname) {
        super(pathname);
    }

    /**
     * Construction of a new  xml file with the given parent file and child.
     * 
     * @param parent The parent file.
     * 
     * @param child The child abstract filename.
     * 
     * @see File#File(File, String)
     */
    public XmlFile(File parent, String child) {
        super(parent, child);
    }
}