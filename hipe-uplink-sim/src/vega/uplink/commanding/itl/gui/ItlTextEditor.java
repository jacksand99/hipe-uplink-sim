package vega.uplink.commanding.itl.gui;

import javax.swing.JEditorPane;

import herschel.ia.jconsole.util.UndoableTextEditor;

public class ItlTextEditor extends UndoableTextEditor{
	public ItlTextEditor(){
		super();
		this.getTextComponent().setEditorKitForContentType(ITLEditorKit.ITL_MIME_TYPE, new ITLEditorKit());
		this.getTextComponent().setContentType(ITLEditorKit.ITL_MIME_TYPE);
		//this.getTextComponent().
	}
	
	public JEditorPane getTextComponent(){
		return (JEditorPane) super.getTextComponent();
	}
}
