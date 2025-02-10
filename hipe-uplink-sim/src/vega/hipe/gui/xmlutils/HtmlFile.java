package vega.hipe.gui.xmlutils;

/*

Licensed under GNU Lesser General Public License (http://www.gnu.org/licenses/lgpl.html)

*/

import java.io.File;

/**
* This is a class that makes sure we can handle html files in the Navigator.
*/
public class HtmlFile extends File {

 /**
  * Version ID for serialization.
  */
 private static final long serialVersionUID = 1L;

 //-------------
 // Constructors
 //-------------

 /**
  * Construction of a new  html file with the given parent and child.
  * 
  * @param parent The parent abstract filename.
  * 
  * @param child The child abstract filename.
  * 
  * @see File#File(String, String)
  */
 public HtmlFile(String parent, String child) {
     super(parent, child);
 }

 /**
  * Construction of a new  html file with the given filename.
  * 
  * @param pathname The filename.
  * 
  * @see File#File(String)
  */
 public HtmlFile(String pathname) {
     super(pathname);
 }

 /**
  * Construction of a new  html file with the given parent file and child.
  * 
  * @param parent The parent file.
  * 
  * @param child The child abstract filename.
  * 
  * @see File#File(File, String)
  */
 public HtmlFile(File parent, String child) {
     super(parent, child);
 }
}
