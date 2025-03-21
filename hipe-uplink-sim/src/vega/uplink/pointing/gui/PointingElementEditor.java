package vega.uplink.pointing.gui;

import static javax.swing.GroupLayout.Alignment.BASELINE;
import herschel.ia.gui.kernel.parts.AbstractVariableEditorComponent;





import herschel.ia.gui.kernel.util.component.ColorButton;
//import herschel.share.swing.JLongField;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
//import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;



//import rosetta.uplink.commanding.HistoryModes;

import javax.swing.JTextField;








import vega.IconResources;
/*import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;

import vega.uplink.commanding.HistoryModes;*/
//import vega.uplink.commanding.gui.HistoryModesPlot;
import vega.uplink.pointing.PointingElement;

public class PointingElementEditor extends AbstractVariableEditorComponent<PointingElement> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	PointingElement pointingMetadata;
	public PointingElementEditor(){
		super();
	}
	public void init(){

		JPanel main=new JPanel();
		main.setLayout(new BoxLayout(this,BoxLayout.LINE_AXIS));
		JPanel namePanel=new JPanel();
		JPanel dataPanel=new JPanel();
		dataPanel.setLayout(new BoxLayout(dataPanel,BoxLayout.PAGE_AXIS));
		JLabel nameLabel=new JLabel(pointingMetadata.getName()+":");
		namePanel.add(nameLabel);
		if (pointingMetadata.getValue()!=null && !pointingMetadata.getValue().equals("")){
			JTextField valueField=new JTextField();
			valueField.setMaximumSize(new Dimension(400,20));
			valueField.setText(pointingMetadata.getValue());
			valueField.addActionListener(new ValueListener(pointingMetadata,valueField));
			dataPanel.add(valueField);
		}
		if (pointingMetadata.hasAttributtes()){
			PointingElement[] attributes = pointingMetadata.getAttributes();
			for (int i=0;i<attributes.length;i++){
				PointingElementAttributeEditor cPanel = new PointingElementAttributeEditor(attributes[i],pointingMetadata);
				dataPanel.add(cPanel);
				
			}
		}
		if (pointingMetadata.hasChildren()){
			PointingElement[] children = pointingMetadata.getChildren();
			for (int i=0;i<children.length;i++){
				PointingElementEditor cPanel = new PointingElementEditor();
				cPanel.setPointingMetadata(children[i]);
				dataPanel.add(cPanel);
			}
		}
		main.setLayout(new BoxLayout(main,BoxLayout.LINE_AXIS));
		main.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		main.add(Box.createHorizontalGlue());
		main.add(namePanel);
		main.add(dataPanel);
		this.setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));

		this.add(main,BorderLayout.PAGE_START);


	}
	
	public void setPointingMetadata(PointingElement pMetadata){
		pointingMetadata=pMetadata;
		init();
	}
    public boolean makeEditorContent() {
    	setPointingMetadata(getValue());
		Dimension minSize = new Dimension(5, 100);
		Dimension prefSize = new Dimension(Short.MAX_VALUE, Short.MAX_VALUE);
		Dimension maxSize = new Dimension(Short.MAX_VALUE, Short.MAX_VALUE);
		this.add(Box.createVerticalGlue());
    	return true;
    }

	
	public class ValueListener implements ActionListener{
		JTextField field;
		PointingElement pm;
		public ValueListener(PointingElement metadataAttached,JTextField fieldAttached){
			field=fieldAttached;
			pm=metadataAttached;
		}

        public void actionPerformed(ActionEvent e){
        	pm.setValue(field.getText());

        }}

	@Override
	public Icon getComponentIcon() {
		// TODO Auto-generated method stub
        try {
            URL resource = PointingElementEditor.class.getResource(IconResources.HUS_ICON);
            BufferedImage imageIcon = ImageIO.read(resource);
            return new ImageIcon(imageIcon);
    } catch (IOException e) {
           
            e.printStackTrace();
            return null;
    }
	}
	@Override
	protected Class<? extends PointingElement> getVariableType() {
		return PointingElement.class;
		// TODO Auto-generated method stub
	}
	
}
