package vega.uplink.commanding.gui;

import static javax.swing.GroupLayout.Alignment.BASELINE;
import herschel.ia.gui.kernel.parts.AbstractVariableEditorComponent;
//import herschel.ia.gui.kernel.util.component.ColorButton;
//import herschel.ia.gui.plot.renderer.Sequence;
//import herschel.share.swing.JLongField;
//import herschel.share.swing.





import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
//import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
//import java.util.StringTokenizer;


import javax.imageio.ImageIO;
import javax.swing.GroupLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
//import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;

import vega.IconResources;
import vega.uplink.commanding.Sequence;

public class SequenceEditor extends AbstractVariableEditorComponent<Sequence>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Sequence seq;
	JTextField _fieldA;
	JTextField _fieldB;
	JTextField _fieldC;
	JTextField _fieldD;
	JTextField _fieldE;
	JTextField _fieldF;
	JLabel    insLabel2;

	public boolean makeEditorContent() {
		setSequence(getValue());
		makeContent();
		return true;
	}
	
	public void setSequence(Sequence sequence){
		seq=sequence;
	}
	public void makeContent(){
		// TODO Auto-generated method stub
		//System.out.println("makeContent");
		//String categoryPath = this.getCategoryPath();
		//StringTokenizer tokenizer=new StringTokenizer(categoryPath,"/");
		//while (tokenizer.hasMoreTokens()){
			//instrument=tokenizer.nextToken();
		//}
		//seq=getValue();
        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        SequentialGroup hGroup = layout.createSequentialGroup();
        SequentialGroup vGroup = layout.createSequentialGroup();
        ParallelGroup hLabelGroup = layout.createParallelGroup();
        ParallelGroup hComboGroup = layout.createParallelGroup();
        ParallelGroup vPropsGroup;

        JLabel    insLabel = new JLabel("Instrument:");
        insLabel2 = new JLabel(seq.getInstrument());

        vPropsGroup = layout.createParallelGroup(BASELINE);
        vGroup.addGroup(vPropsGroup.addComponent(insLabel).addComponent(insLabel2));
        hLabelGroup.addComponent(insLabel);
        hComboGroup.addComponent(insLabel2);

        JLabel    labelA = new JLabel("Sequence Name:");
        _fieldA = new JTextField();
        _fieldA.setText(seq.getName());
        _fieldA.setMaximumSize(new Dimension(400,20));
        _fieldA.addActionListener(new ActionListener() {
		        public void actionPerformed(ActionEvent ae) {
		        	seq.setName(_fieldA.getText());
		        	insLabel2.setText(seq.getInstrument());
		        }

		      });
        vPropsGroup = layout.createParallelGroup(BASELINE);
        vGroup.addGroup(vPropsGroup.addComponent(labelA).addComponent(_fieldA));
        hLabelGroup.addComponent(labelA);
        hComboGroup.addComponent(_fieldA);

        JLabel    labelB = new JLabel("Unique ID:");
        _fieldB = new JTextField();
        _fieldB.setText(seq.getUniqueID());
        _fieldB.setMaximumSize(new Dimension(400,20));
        _fieldB.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent ae) {
	        	seq.setUniqueID(_fieldB.getText());
	        }

	      });

        vPropsGroup = layout.createParallelGroup(BASELINE);
        vGroup.addGroup(vPropsGroup.addComponent(labelB).addComponent(_fieldB));
        hLabelGroup.addComponent(labelB);
        hComboGroup.addComponent(_fieldB);

        JLabel    labelC = new JLabel("Execution Time:");
        _fieldC = new JTextField();
        _fieldC.setText(seq.getExecutionTime());
        _fieldC.setMaximumSize(new Dimension(400,20));
        _fieldC.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent ae) {
	        	try {
					seq.setExecutionTime(_fieldC.getText());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }

	      });

        vPropsGroup = layout.createParallelGroup(BASELINE);
        vGroup.addGroup(vPropsGroup.addComponent(labelC).addComponent(_fieldC));
        hLabelGroup.addComponent(labelC);
        hComboGroup.addComponent(_fieldC);

        JLabel    labelD = new JLabel("Source:");
        _fieldD = new JTextField(1);
        _fieldD.setText(""+seq.getSource());
        _fieldD.setMaximumSize(new Dimension(20,20));
        _fieldD.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent ae) {
	        	seq.setSource(_fieldD.getText().charAt(0));
	        }

	      });

        vPropsGroup = layout.createParallelGroup(BASELINE);
        vGroup.addGroup(vPropsGroup.addComponent(labelD).addComponent(_fieldD));
        hLabelGroup.addComponent(labelD);
        hComboGroup.addComponent(_fieldD);

        JLabel    labelE = new JLabel("Destination:");
        _fieldE = new JTextField(1);
        _fieldE.setText(""+seq.getDestination());
        _fieldE.setMaximumSize(new Dimension(20,20));
        _fieldE.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent ae) {
	        	seq.setDestination(_fieldE.getText().charAt(0));
	        }

	      });

        vPropsGroup = layout.createParallelGroup(BASELINE);
        vGroup.addGroup(vPropsGroup.addComponent(labelE).addComponent(_fieldE));
        hLabelGroup.addComponent(labelE);
        hComboGroup.addComponent(_fieldE);
        
        JLabel    labelF = new JLabel("Flag:");
        _fieldF = new JTextField();
        _fieldF.setText(""+seq.getFlag());
        _fieldF.setMaximumSize(new Dimension(400,20));
        _fieldF.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent ae) {
	        	seq.setFlag(_fieldF.getText());
	        }

	      });

        vPropsGroup = layout.createParallelGroup(BASELINE);
        vGroup.addGroup(vPropsGroup.addComponent(labelF).addComponent(_fieldF));
        hLabelGroup.addComponent(labelF);
        hComboGroup.addComponent(_fieldF);
        hGroup.addGroup(hLabelGroup);
        hGroup.addGroup(hComboGroup);
        layout.setHorizontalGroup(hGroup);
        layout.setVerticalGroup(vGroup);

		//System.out.println(instrument);
		
	}

	@Override
	public Icon getComponentIcon() {
        try {
            URL resource = SequenceEditor.class.getResource(IconResources.HUS_ICON);
            BufferedImage imageIcon = ImageIO.read(resource);
            return new ImageIcon(imageIcon);
    } catch (IOException e) {
           
            e.printStackTrace();
            return null;
    }
	}

	@Override
	protected Class<? extends Sequence> getVariableType() {
		return Sequence.class;
		// TODO Auto-generated method stub
		//return null;
	}
}
