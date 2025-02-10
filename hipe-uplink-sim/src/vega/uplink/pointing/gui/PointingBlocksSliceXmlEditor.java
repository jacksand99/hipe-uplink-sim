package vega.uplink.pointing.gui;

import herschel.ia.gui.apps.components.util.BottomPanel;
import herschel.ia.gui.kernel.parts.AbstractVariableEditorComponent;
import herschel.ia.jconsole.gui.JSearchDialog;
import herschel.ia.jconsole.util.TextLineNumber;
import herschel.share.interpreter.InterpreterUtil;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

import vega.IconResources;
import vega.hipe.gui.xmlutils.XMLTextEditor;
import vega.uplink.planning.Observation;
import vega.uplink.planning.ObservationPointingSlice;
import vega.uplink.planning.ObservationUtil;
import vega.uplink.pointing.PointingBlock;
import vega.uplink.pointing.PointingBlocksSlice;
//import vega.uplink.pointing.Ptr;
import vega.uplink.pointing.PtrChecker;
import vega.uplink.pointing.PtrUtils;

public class PointingBlocksSliceXmlEditor extends AbstractVariableEditorComponent<PointingBlocksSlice> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	PointingBlocksSlice slice;
	XMLTextEditor editor;
	boolean initialized;
	public PointingBlocksSliceXmlEditor(){
		super(new BorderLayout());
		initialized=false;
	}
	public void init(){
		/*this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		JPanel topPanel=new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.LINE_AXIS));
		JLabel classLabel=new JLabel(slice.getName());
		classLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		JButton button=new JButton("Finish Edditing");
		button.setAlignmentX(Component.LEFT_ALIGNMENT);*/
		JPanel topPanel=new JPanel();
		topPanel.setLayout(new BorderLayout());
		JPanel buttonsPanel=new JPanel();
		buttonsPanel.setLayout(new BorderLayout());
		JLabel classLabel=new JLabel(PointingBlocksSlice.class.getName());
		classLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		JButton button=new JButton("Finish Edditing");
		button.setAlignmentX(Component.LEFT_ALIGNMENT);
		
        button.addActionListener(new ActionListener() {
        	 
            public void actionPerformed(ActionEvent e)
            {
            	String text = editor.getText();
            	if (InterpreterUtil.isInstance(ObservationPointingSlice.class, slice)){
            		try{
		    			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		    			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		    			String tempText="<slice>\n"+text+"</slice>\n";
		    			InputStream stream = new ByteArrayInputStream(tempText.getBytes(StandardCharsets.UTF_8));
		    			Document doc;
	
						doc = dBuilder.parse(stream);
						doc.getDocumentElement().normalize();
						ObservationPointingSlice tempSlice = ObservationUtil.readSlicefromDoc(doc);
						tempSlice.setObservation(((ObservationPointingSlice)slice).getObservation());
						PointingBlock[] obs = tempSlice.getBlocks();
						for (int i=0;i<obs.length;i++){
							obs[i].validate();
						}
					
						slice.regenerate(tempSlice);
						String warnings = PtrChecker.checkSlice(tempSlice);
						//slice=tempSlice;
						
						editor.setText(((ObservationPointingSlice) slice).toObsXml(0));
						if (!warnings.equals("")) {
							JOptionPane.showMessageDialog(PointingBlocksSliceXmlEditor.this,
									warnings,
								    "Warning",
								    JOptionPane.WARNING_MESSAGE);
						}
            		}catch(Exception e2){
						JOptionPane.showMessageDialog(PointingBlocksSliceXmlEditor.this,
							    e2.getMessage(),
							    "Error",
							    JOptionPane.ERROR_MESSAGE);
						e2.printStackTrace();
						editor.setText(((ObservationPointingSlice) slice).toObsXml(0));
           			
            		}
            	}else {
					try {
		    			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		    			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		    			String tempText="<slice>\n"+text+"</slice>\n";
		    			InputStream stream = new ByteArrayInputStream(tempText.getBytes(StandardCharsets.UTF_8));
		    			Document doc;
	
						doc = dBuilder.parse(stream);
						doc.getDocumentElement().normalize();
						PointingBlocksSlice tempSlice = PtrUtils.readBlocksfromDoc(doc);
						PointingBlock[] obs = tempSlice.getBlocks();
						for (int i=0;i<obs.length;i++){
							obs[i].validate();
						}
					
						slice.regenerate(tempSlice);
						String warnings = PtrChecker.checkSlice(slice);
						
						editor.setText(slice.toXml(0));
						if (!warnings.equals("")) {
							JOptionPane.showMessageDialog(PointingBlocksSliceXmlEditor.this,
									warnings,
								    "Warning",
								    JOptionPane.WARNING_MESSAGE);
						}
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(PointingBlocksSliceXmlEditor.this,
							    e1.getMessage(),
							    "Error",
							    JOptionPane.ERROR_MESSAGE);
						e1.printStackTrace();
						editor.setText(slice.toXml(0));
					} 
            	}
    			
            }
        });
        /*topPanel.add(button);
        topPanel.add(Box.createHorizontalGlue());
		topPanel.add(classLabel);
		JPanel global = new JPanel(new BorderLayout());
		global.add(topPanel);*/
		button.setAlignmentX(Component.LEFT_ALIGNMENT);
		//buttonsPanel.add(buttonSave,BorderLayout.WEST);
		
		buttonsPanel.add(button,BorderLayout.CENTER);
		//buttonsPanel.add(buttonSearch,BorderLayout.EAST);
		topPanel.add(buttonsPanel,BorderLayout.WEST);
		topPanel.add(classLabel,BorderLayout.EAST);
		JPanel global = new JPanel (new BorderLayout());
		global.add(topPanel,BorderLayout.NORTH);
		editor = new XMLTextEditor();
		//editor.setText(ptr.toXml());
		JScrollPane scroll=new JScrollPane(editor);
		scroll.setRowHeaderView(new TextLineNumber(editor));
		//searchDialog=new JSearchDialog(editor,ptr.getName());
		global.add(scroll,BorderLayout.CENTER);
		this.add(new BottomPanel(this, global));
		//editor = new XMLTextEditor();
		if (InterpreterUtil.isInstance(ObservationPointingSlice.class, slice)){
			editor.setText(((ObservationPointingSlice) slice).toObsXml(0));
		}
		else editor.setText(slice.toXml(0));
		global.add(new JScrollPane(editor));
		this.add(new BottomPanel(this, global));
		initialized=true;
	}
	
	public void setSlice(PointingBlocksSlice slice){
		if (InterpreterUtil.isInstance(ObservationPointingSlice.class, slice)){
			boolean oldListen=Observation.LISTEN;
			Observation.LISTEN=false;
			this.slice=slice;
			
			if (!initialized) init();
			editor.setText(((ObservationPointingSlice) slice).toObsXml(0));
			Observation.LISTEN=oldListen;

			
			//editor.setText(((ObservationPointingSlice) slice).toObsXml(0));
		}else{

			this.slice=slice;
			init();
		}
		

	}
    public boolean makeEditorContent() {
    	setSlice(getValue());
    	return true;
    }


	
	public Icon getComponentIcon() {
        try {
            URL resource = PtrXmlEditor.class.getResource(IconResources.HUS_ICON);
            BufferedImage imageIcon = ImageIO.read(resource);
            return new ImageIcon(imageIcon);
    } catch (IOException e) {
           
            e.printStackTrace();
            return null;
    }
	}
	@Override
	protected Class<? extends PointingBlocksSlice> getVariableType() {
		return PointingBlocksSlice.class;
		// TODO Auto-generated method stub
	}


}
