package vega.uplink.pointing.gui;

import herschel.ia.gui.kernel.parts.AbstractVariableEditorComponent;
import herschel.ia.numeric.String1d;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import vega.IconResources;
import vega.uplink.pointing.PointingElement;
import vega.uplink.pointing.PointingMetadata;
import herschel.ia.dataset.gui.views.ArrayDataComponent;
//import vega.uplink.pointing.gui.PointingElementEditor.ValueListener;

public class PointingMetadataEditor extends AbstractVariableEditorComponent<PointingMetadata> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	PointingMetadata pointingMetadata;
	public PointingMetadataEditor(){
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
		String1d comments = pointingMetadata.getComments();
		if (comments.length()>0){
			dataPanel.add(new MetadataCommentsPanel(this.getPart(),comments));
			//ArrayDataComponent adComponent=new ArrayDataComponent();
			//JPanel commentsPanel=new JPanel();
			//commentsPanel.setLayout(new BoxLayout(dataPanel,BoxLayout.PAGE_AXIS));
		}
		main.setLayout(new BoxLayout(main,BoxLayout.LINE_AXIS));
		main.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		main.add(Box.createHorizontalGlue());
		main.add(namePanel);
		main.add(dataPanel);
		this.setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));

		this.add(main,BorderLayout.PAGE_START);


	}
	
	public void setPointingMetadata(PointingMetadata pMetadata){
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
            URL resource = PointingElementEditor.class.getResource(IconResources.PTR_ICON);
            BufferedImage imageIcon = ImageIO.read(resource);
            return new ImageIcon(imageIcon);
    } catch (IOException e) {
           
            e.printStackTrace();
            return null;
    }
	}
	@Override
	protected Class<? extends PointingMetadata> getVariableType() {
		return PointingMetadata.class;
		// TODO Auto-generated method stub
	}
	
}