package vega.uplink.commanding.itl.gui;

import java.awt.Font;
import javax.swing.JEditorPane;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;

/**
 * This is the set of things needed by a text component to be a reasonably
 * functioning editor for xml type document.
 *
 * @author <a href="mailto:tonny@kiyut.com">Tonny Kohar</a>
 * @version $Id$
 */
public class ITLEditorKit extends DefaultEditorKit implements ViewFactory{

    public static final String ITL_MIME_TYPE = "text/itl";
    
    //protected XMLContext context;
    //protected ViewFactory factory = null;
    
    /** Creates a new instance of XMLEditorKit */
    public ITLEditorKit() {
    	super();
    }
        //this(null);
    
    

    

    
    /** Overriden to set the JEditorPane font to match with the XMLContext
     * {@inheritDoc}
     */
    public void install(JEditorPane c) {
        super.install(c);
        
        /*Object obj = context.getSyntaxFont(XMLContext.DEFAULT_STYLE);
        if (obj != null) {
            c.setFont((Font)obj);
        }*/
    }
    
    
    /**
     * Get the MIME type of the data that this
     * kit represents support for.  This kit supports
     * the type <code>text/xml</code>.
     */
    public String getContentType() {
        return ITL_MIME_TYPE;
    }
    /**
    * Returns an instance of a view factory that can be used for creating views from
    * elements. This implementation returns the current instance, because this class
    * already implements <code>ViewFactory</code>.
    *
    * @return DOCUMENT ME!
    */
    public ViewFactory getViewFactory()
    {
    return this;
    }
    /**
    * Creates a view from an element that can be used for painting that element.
    *
    * @param elem The element
    *
    * @return a new SyntaxView for an element
    */
    public View create(Element elem)
    {
    return new SyntaxView(elem);
    }
    /**
    * Creates a new instance of the default document for this editor kit.
    *
    * @return DOCUMENT ME!
    */
    public Document createDefaultDocument()
    {
    SyntaxDocument document = new DefaultSyntaxDocument();
    document.setTokenMarker(new ItlTokenMarker());
    return document;
    }
    
}
