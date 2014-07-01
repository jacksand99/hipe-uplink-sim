package vega.uplink.pointing.gui;

import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;



//import rosetta.uplink.commanding.HistoryModes;

import vega.uplink.pointing.PointingMetadata;
import vega.uplink.pointing.PtrParameters.Boresight;
import herschel.ia.dataset.gui.views.CompositeDatasetOutline;
import herschel.ia.gui.apps.views.outline.OutlineComponent;
import herschel.ia.gui.kernel.Selection;
import herschel.ia.gui.kernel.parts.EditorPart;

public class PointingMetadataEditor extends javax.swing.JPanel implements herschel.ia.gui.kernel.parts.EditorComponent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String viewwerID;
	PointingMetadata pointingMetadata;
	JLabel frameBox;
	JLabel xBox;
	JLabel yBox;
	JLabel zBox;
	JLabel refBox;
	public PointingMetadataEditor(){
		super();
		frameBox=new JLabel();
		xBox=new JLabel();
		yBox=new JLabel();
		zBox=new JLabel();
		refBox=new JLabel();
		JLabel frameTitle=new JLabel("Frame:");
		JLabel xTitle=new JLabel("X:");
		JLabel yTitle=new JLabel("Y:");
		JLabel zTitle=new JLabel("Z:");
		JLabel refTitle=new JLabel("Ref:");
		
		frameTitle.setMaximumSize(new Dimension(200, 40));
		xTitle.setMaximumSize(new Dimension(200, 40));
		yTitle.setMaximumSize(new Dimension(200, 40));
		zTitle.setMaximumSize(new Dimension(200, 40));
		refTitle.setMaximumSize(new Dimension(200, 40));
	      
	      frameBox.setMaximumSize(new Dimension(400,40));
	      xBox.setMaximumSize(new Dimension(400,40));
	      yBox.setMaximumSize(new Dimension(400,40));
	      zBox.setMaximumSize(new Dimension(400,40));
	      refBox.setMaximumSize(new Dimension(400,40));
	      
	        GridLayout gl=new GridLayout(0,2);
	        
	        JPanel panel1=new JPanel();
	        panel1.setLayout(gl);
	        
	        JPanel frameTitlePanel=new JPanel();
	        frameTitlePanel.setLayout(new BoxLayout(frameTitlePanel,BoxLayout.LINE_AXIS));
	        frameTitlePanel.add(frameTitle);
	        panel1.add(frameTitlePanel);
	        
	        JPanel frameBoxPanel=new JPanel();
	        frameBoxPanel.setLayout(new BoxLayout(frameBoxPanel,BoxLayout.LINE_AXIS));
	        frameBoxPanel.add(frameBox);
	        panel1.add(frameBoxPanel);
	        
	        JPanel xTitlePanel=new JPanel();
	        xTitlePanel.setLayout(new BoxLayout(xTitlePanel,BoxLayout.LINE_AXIS));
	        xTitlePanel.add(xTitle);
	        panel1.add(xTitlePanel);
	        
	        JPanel xBoxPanel=new JPanel();
	        xBoxPanel.setLayout(new BoxLayout(xBoxPanel,BoxLayout.LINE_AXIS));
	        xBoxPanel.add(xBox);
	        panel1.add(xBoxPanel);
	        
	        JPanel yTitlePanel=new JPanel();
	        yTitlePanel.setLayout(new BoxLayout(yTitlePanel,BoxLayout.LINE_AXIS));
	        yTitlePanel.add(yTitle);
	        panel1.add(yTitlePanel);
	        
	        JPanel yBoxPanel=new JPanel();
	        yBoxPanel.setLayout(new BoxLayout(yBoxPanel,BoxLayout.LINE_AXIS));
	        yBoxPanel.add(yBox);
	        panel1.add(yBoxPanel);

	        JPanel zTitlePanel=new JPanel();
	        zTitlePanel.setLayout(new BoxLayout(zTitlePanel,BoxLayout.LINE_AXIS));
	        zTitlePanel.add(zTitle);
	        panel1.add(zTitlePanel);
	        
	        JPanel zBoxPanel=new JPanel();
	        zBoxPanel.setLayout(new BoxLayout(zBoxPanel,BoxLayout.LINE_AXIS));
	        zBoxPanel.add(zBox);
	        panel1.add(zBoxPanel);
	        

	        
	        panel1.setMaximumSize(new Dimension(800,350));
	       // panel1.add(porbar);
	        
	    	BoxLayout bl=new BoxLayout(this,BoxLayout.PAGE_AXIS);
	    	this.setLayout(bl);


			add(panel1);
			JPanel panel2=new JPanel();
			panel2.setLayout(new BoxLayout(panel2,BoxLayout.LINE_AXIS));
			panel2.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
			panel2.add(Box.createHorizontalGlue());
			//panel2.add(Box.)
			//panel2.add(runButton);
			panel2.add(Box.createRigidArea(new Dimension(10, 0)));
			//panel2.add(resetButton);
			
			add(panel2);
			//add(runButton);
			//add(resetButton);
			this.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

	}
	
	public void setPointingMetadata(PointingMetadata pMetadata){
		pointingMetadata=pMetadata;
		/*boresight=bore;
		frameBox.setText(bore.getFrame());
		xBox.setText(""+bore.getX());*/
	}
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean aboutToClose() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addSelection(Selection arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Component asComponent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Icon getComponentIcon() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EditorPart getPart() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Selection getSelection() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getViewerId() {
		return viewwerID;
		// TODO Auto-generated method stub
		//return null;
	}

	@Override
	public boolean init(Selection arg0, EditorPart arg1) {
		setPointingMetadata((PointingMetadata) arg0.getValue());
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isApplicable(Selection arg0) {
		System.out.println(arg0.getType());

		if (arg0.getType().equals(Boresight.class)){
			//System.out.println("Is applicable");
			
			return true;
		}
		else return false;

		// TODO Auto-generated method stub
		//return false;
	}

	@Override
	public boolean isSingleton() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setFocused(boolean arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setViewerId(String arg0) {
		viewwerID=arg0;
		// TODO Auto-generated method stub
		
	}
	
}
