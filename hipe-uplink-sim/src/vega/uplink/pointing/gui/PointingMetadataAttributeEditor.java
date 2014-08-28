package vega.uplink.pointing.gui;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
//import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import vega.uplink.pointing.PointingMetadata;
//import vega.uplink.pointing.gui.PointingMetadataEditor.ValueListener;

public class PointingMetadataAttributeEditor extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PointingMetadataAttributeEditor(PointingMetadata attribute,PointingMetadata parent){
		super();
		JPanel main=new JPanel();
		main.setLayout(new BoxLayout(this,BoxLayout.LINE_AXIS));
		JPanel namePanel=new JPanel();
		JPanel dataPanel=new JPanel();
		dataPanel.setLayout(new BoxLayout(dataPanel,BoxLayout.PAGE_AXIS));
		//dataPanel.setLayout(new FlowLayout());
		//namePanel.setLayout(new FlowLayout());
		JLabel nameLabel=new JLabel(attribute.getName()+":");
		namePanel.add(nameLabel);
		if (attribute.getValue()!=null && !attribute.getValue().equals("")){
			JTextField valueField=new JTextField();
			//valueField.setMaximumSize(new Dimension(400,20));
			valueField.setText(attribute.getValue());
			valueField.addActionListener(new ValueListener(attribute,parent,valueField));
			dataPanel.add(valueField);
		}
		//this.setLayout(new BoxLayout(this,BoxLayout.LINE_AXIS));
		//this.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		//this.add(Box.createHorizontalGlue());
		//this.add(Box.createVerticalGlue());
		//this.add(namePanel);
		//this.add(dataPanel);
		main.setLayout(new BoxLayout(main,BoxLayout.LINE_AXIS));
		main.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		main.add(Box.createHorizontalGlue());
		//this.add(Box.createVerticalGlue());
		main.add(namePanel);
		main.add(dataPanel);
		this.setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));
		this.add(main);
		this.add(Box.createVerticalGlue());

	}
	
	public class ValueListener implements ActionListener{
		JTextField field;
		PointingMetadata pm;
		PointingMetadata parentPm;
		public ValueListener(PointingMetadata metadataAttached,PointingMetadata parent,JTextField fieldAttached){
			field=fieldAttached;
			pm=metadataAttached;
			parentPm=parent;
		}

        public void actionPerformed(ActionEvent e){
        	//System.out.println("enter detected");
        	pm.setValue(field.getText());
        	parentPm.addAttribute(pm);
        }}

}
