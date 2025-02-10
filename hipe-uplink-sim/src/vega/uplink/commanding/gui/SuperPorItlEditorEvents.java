package vega.uplink.commanding.gui;

import vega.uplink.commanding.itl.ItlParser;

public class SuperPorItlEditorEvents extends SuperPorItlEditor{
	public void setText(){
		editor.setText(ItlParser.SuperPortoITLEvents(por));
	}
}
