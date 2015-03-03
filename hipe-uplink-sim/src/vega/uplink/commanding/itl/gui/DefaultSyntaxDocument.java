package vega.uplink.commanding.itl.gui;

import java.awt.Color;

import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.Position;
import javax.swing.text.Segment;

import java.awt.Color;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.PlainDocument;
import javax.swing.text.Segment;
/**
* A simple implementation of <code>SyntaxDocument</code>. It takes care of inserting and
* deleting lines from the token marker's state.
*
* @author Slava Pestov
* @version $Id: DefaultSyntaxDocument.java,v 1.2 2006/01/13 20:27:25 notzippy Exp $
*
* @see org.gjt.sp.jedit.syntax.SyntaxDocument
*/
public class DefaultSyntaxDocument
extends PlainDocument
implements SyntaxDocument
{
//~ Instance variables ---------------------------------------------------------------
// protected members
/** The token marker */
protected TokenMarker tokenMarker;
/** The colors */
protected Color[] colors;
//~ Constructors ---------------------------------------------------------------------
/**
* Creates a new <code>DefaultSyntaxDocument</code> instance.
*/
public DefaultSyntaxDocument()
{
colors = SyntaxUtilities.getDefaultSyntaxColors();
addDocumentListener(new DocumentHandler());
}
//~ Methods --------------------------------------------------------------------------
/**
* Sets the color array that maps token identifiers to <code>java.awt.Color</code>
* ojects. May throw an exception if this is not supported for this type of
* document.
*
* @param colors The new color list
*/
public void setColors(Color[] colors)
{
this.colors = colors;
}
/**
* Returns the color array that maps token identifiers to <code>java.awt.Color</code>
* objects.
*
* @return The colors
*/
public Color[] getColors()
{
return colors;
}
/**
* Sets the token marker that is to be used to split lines of this document up into
* tokens. May throw an exception if this is not supported for this type of
* document.
*
* @param tm The new token marker
*/
public void setTokenMarker(TokenMarker tm)
{
tokenMarker = tm;
if (tm == null)
{
return;
}
tokenMarker.insertLines(0, getDefaultRootElement().getElementCount());
tokenizeLines();
}
/**
* Returns the token marker that is to be used to split lines of this document up
* into tokens. May return null if this document is not to be colorized.
*
* @return The token marker
*/
public TokenMarker getTokenMarker()
{
return tokenMarker;
}
/**
* Reparses the document, by passing all lines to the token marker. This should be
* called after the document is first loaded.
*/
public void tokenizeLines()
{
tokenizeLines(0, getDefaultRootElement().getElementCount());
}
/**
* Reparses the document, by passing the specified lines to the token marker. This
* should be called after a large quantity of text is first inserted.
*
* @param start The first line to parse
* @param len The number of lines, after the first one to parse
*/
public void tokenizeLines(
int start,
int len)
{
if (tokenMarker == null)
{
return;
}
Segment lineSegment = new Segment();
Element map = getDefaultRootElement();
len += start;
try
{
for (int i = start; i < len; i++)
{
Element lineElement = map.getElement(i);
int lineStart = lineElement.getStartOffset();
getText(
lineStart, lineElement.getEndOffset() - lineStart - 1, lineSegment);
tokenMarker.markTokens(lineSegment, i);
}
}
catch (BadLocationException bl)
{
;
}
}
//~ Inner Classes --------------------------------------------------------------------
/**
* An implementation of <code>DocumentListener</code> that inserts and deletes lines
* from the token marker's state.
*/
public final class DocumentHandler
implements DocumentListener
{
public void changedUpdate(DocumentEvent evt)
{
}
public void insertUpdate(DocumentEvent evt)
{
if (tokenMarker == null)
{
return;
}
DocumentEvent.ElementChange ch = evt.getChange(getDefaultRootElement());
if (ch == null)
{
return;
}
tokenMarker.insertLines(
ch.getIndex() + 1,
ch.getChildrenAdded().length - ch.getChildrenRemoved().length);
}
public void removeUpdate(DocumentEvent evt)
{
if (tokenMarker == null)
{
return;
}
DocumentEvent.ElementChange ch = evt.getChange(getDefaultRootElement());
if (ch == null)
{
return;
}
Element[] children = ch.getChildrenRemoved();
if (children == null)
{
return;
}
tokenMarker.deleteLines(
ch.getIndex() + 1,
ch.getChildrenRemoved().length - ch.getChildrenAdded().length);
}
}
}