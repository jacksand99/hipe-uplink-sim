package vega.hipe.pds.gui;

import herschel.ia.dataset.DoubleParameter;
import herschel.ia.dataset.Parameter;
import herschel.ia.gui.kernel.parts.AbstractVariableEditorComponent;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import vega.hipe.logging.VegaLog;
import vega.hipe.pds.InvalidValueException;
import vega.hipe.pds.PDSImage;
import vega.hipe.pds.gui.PDSNavigator;


public class PDSNavigator extends AbstractVariableEditorComponent<PDSImage>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	PDSImage pdsImage;
	double latitude;
	double longitude;
	double end_latitude;
	double end_longitude;
	double scale;
	JLabel coordinates;
	GraphicImagePanel imagePanel;
	RowRulerLabel rowheader;
	ColumnRulerLabel columnheader;
	PDSNavigator frame;
	private static final DecimalFormat f = new DecimalFormat("0.###");
	private double FACTOR_X=80;
	private double FACTOR_Y=0.75;
	public static void main(String[] args){
    	herschel.share.util.Configuration.setProperty("var.hcss.dir", "C:\\Users\\jarenas\\Downloads\\hcss-12.0.2524");
    	herschel.share.util.Configuration.setProperty("var.hcsstest.dir", "C:\\Users\\jarenas\\Downloads\\hcss-12.0.2524");

		try {
			PDSNavigator navi=new PDSNavigator();
			//navi.init(PDSImage.readPdsFile("Z:\\MEX\\mexhrsc_1001\\data\\0032\\h0032_0000_bl3.img"));
			//navi.init(PDSImage.readPdsFile("Z:\\MEX\\mexhrsc_1001\\data\\2176\\h2176_0000_p13.img"));
			//navi.init(PDSImage.readPdsFile("/Users/jarenas 1/Downloads/MEX/mexhrsc_1001/data/0010/h0010_0026_sr3.img"));
			navi.init(PDSImage.readPdsFile("/Users/jarenas 1/Rosetta/NAC_WAC/H0248_0000_S23.IMG"));
			JFrame frame=new JFrame();
			frame.add(navi);
	        frame.setSize(455,750);
	        frame.setVisible(true);
		} catch (InvalidValueException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void init(PDSImage pdsImg) throws InvalidValueException{
		if (pdsImg==null){
			InvalidValueException iae = new InvalidValueException("Null image not allowed");
			throw(iae);
		}
		pdsImage=pdsImg;
        latitude=pdsImage.getMinimumLatitude();
        longitude=pdsImage.getMinimumLongitude();
        try{
        	scale=pdsImage.getDoubleMetadata("IMAGE_MAP_PROJECTION."+PDSImage.MAP_SCALE);
        }catch (Exception e){
        	Object s = pdsImage.getMeta().get("IMAGE_MAP_PROJECTION."+PDSImage.MAP_SCALE).getValue();
        	String val=""+s;
        	scale=Double.parseDouble(val.split("<")[0]);
        	//VegaLog.severe(""+s);
        	//VegaLog.severe("Could not get metadata "+"IMAGE_MAP_PROJECTION."+PDSImage.MAP_SCALE);
        	//throw e;
        }
        end_latitude=latitude+(FACTOR_X*scale);
        end_longitude=longitude+(FACTOR_Y*1);
        frame=this;
        frame.setLayout(new BoxLayout(frame, BoxLayout.PAGE_AXIS));
        coordinates=new JLabel();
        imagePanel= new GraphicImagePanel(scale,navigate(latitude,longitude));
		JPanel locInputFieldLabel = new JPanel();		
		locInputFieldLabel.add(new JLabel("Lat, Lon "));
		
		locInputFieldLabel.setToolTipText("Specify new view center.");
		
		PasteField locInputField = new PasteField(getLocString(), 20);
		locInputField.addActionListener(new LocationListener(this));
		
		locInputFieldLabel.add(locInputField);
		frame.add(locInputFieldLabel);
		
		setCoordinatesText();
		frame.add(new JPanel().add(coordinates));
		JScrollPane scrollPane = new JScrollPane(imagePanel);
	    JLabel[] corners = new JLabel[4];
	    for (int i = 0; i < 4; i++) {
	      corners[i] = new JLabel();
	      corners[i].setBackground(Color.white);
	      corners[i].setOpaque(true);
	    }
	    rowheader = new RowRulerLabel();
	    rowheader.setBackground(Color.white);
	    rowheader.setOpaque(true);
	    columnheader = new ColumnRulerLabel();
	    columnheader.setBackground(Color.white);
	    columnheader.setOpaque(true);
	    scrollPane.setRowHeaderView(rowheader);
	    scrollPane.setColumnHeaderView(columnheader);
	    scrollPane.setCorner(JScrollPane.LOWER_LEFT_CORNER, corners[0]);
	    scrollPane.setCorner(JScrollPane.LOWER_RIGHT_CORNER, corners[1]);
	    scrollPane.setCorner(JScrollPane.UPPER_LEFT_CORNER, corners[2]);
	    scrollPane.setCorner(JScrollPane.UPPER_RIGHT_CORNER, corners[3]);
	    scrollPane.setPreferredSize(new Dimension(imagePanel.getWidth()+30,imagePanel.getHeight()+25));
	    frame.add(scrollPane);
	    
	    System.out.println(scrollPane.getSize());
	    System.out.println(scrollPane.getPreferredSize());
/*
 *         try{
        	maps = pdsImage.getDoubleMetadata("IMAGE_MAP_PROJECTION."+PDSImage.MAP_SCALE);
        	if (maps==null) throw new NullPointerException();
        }catch (Exception e){
        	Object s = pdsImage.getMeta().get("IMAGE_MAP_PROJECTION."+PDSImage.MAP_SCALE).getValue();
        	String val=""+s;
        	maps=Double.parseDouble(val.split("<")[0]);
        	System.out.println("Scale "+maps);
        	//VegaLog.severe(""+s);
        	//VegaLog.severe("Could not get metadata "+"IMAGE_MAP_PROJECTION."+PDSImage.MAP_SCALE);
        	//throw e;
        }

 */
	    String unit=null;
	    try{
        unit=((DoubleParameter) pdsImage.getMeta().get("IMAGE_MAP_PROJECTION."+PDSImage.MAP_SCALE)).getUnit().toString();
	    }catch (Exception e){
        	Object s = pdsImage.getMeta().get("IMAGE_MAP_PROJECTION."+PDSImage.MAP_SCALE).getValue();
        	String val=""+s;
        	unit=val.split("<")[1].replace(">", "");
	    	
	    }
        JPanel scaleInputFieldLabel = new JPanel();
        scaleInputFieldLabel.add(new JLabel("Scale "));
        PasteField scaleInputField = new PasteField(""+scale, 12);
        scaleInputField.addActionListener(new ScaleListener(this));
        scaleInputFieldLabel.add(scaleInputField);
        scaleInputFieldLabel.add(new JLabel(unit));
        frame.add(scaleInputFieldLabel);
        frame.setVisible(true);

	}
	
	private void setCoordinatesText() {
		coordinates.setText("Showing: "+f.format(latitude)+","+f.format(longitude)+" - "+f.format(end_latitude)+","+f.format(end_longitude));
		
	}
	public String getLocString()
	{
		return  f.format(latitude)+","+f.format(longitude);
	}

	
	public BufferedImage navigate(double latitude,double longitude) throws InvalidValueException{
		this.latitude=latitude;
		this.longitude=longitude;
		//System.out.println("Upper"+latitude+" "+longitude);
        end_latitude=latitude+(FACTOR_X*scale);
        end_longitude=longitude+(scale*FACTOR_Y);
        if (end_latitude>pdsImage.getMaximumLatitude()) end_latitude=pdsImage.getMaximumLatitude();
        if (end_longitude>pdsImage.getMaximumLongitude()) end_latitude=pdsImage.getMaximumLongitude();
        //System.out.println("Down"+end_latitude+" "+end_longitude);
        //System.out.println(FACTOR_X*scale);
        PDSImage subImage = pdsImage.getSubImage(latitude, longitude, end_latitude, end_longitude);
        BufferedImage bi = subImage.asBufferedImage();
        if (bi==null ) System.out.println("Buffered image is null");
        Double maps ;
        try{
        	maps = pdsImage.getDoubleMetadata("IMAGE_MAP_PROJECTION."+PDSImage.MAP_SCALE);
        	if (maps==null) throw new NullPointerException();
        }catch (Exception e){
        	Object s = pdsImage.getMeta().get("IMAGE_MAP_PROJECTION."+PDSImage.MAP_SCALE).getValue();
        	String val=""+s;
        	maps=Double.parseDouble(val.split("<")[0]);
        	System.out.println("Scale "+maps);
        	//VegaLog.severe(""+s);
        	//VegaLog.severe("Could not get metadata "+"IMAGE_MAP_PROJECTION."+PDSImage.MAP_SCALE);
        	//throw e;
        }
        System.out.println("Scale "+maps);
        System.out.println("Scale 2"+scale);
        
		BufferedImage result = this.scalateBufferedImage(bi,maps,scale);
		System.out.println(result.getWidth());
		System.out.println(result.getHeight());
		setCoordinatesText();
		return result;
		
	}
	
	public void changeScale(double newScale) throws InvalidValueException{
		Double maps ;
        try{
        	maps = pdsImage.getDoubleMetadata("IMAGE_MAP_PROJECTION."+PDSImage.MAP_SCALE);
        	if (maps==null) throw new NullPointerException();
        }catch (Exception e){
        	Object s = pdsImage.getMeta().get("IMAGE_MAP_PROJECTION."+PDSImage.MAP_SCALE).getValue();
        	String val=""+s;
        	maps=Double.parseDouble(val.split("<")[0]);
        	System.out.println("Scale "+maps);
        	//VegaLog.severe(""+s);
        	//VegaLog.severe("Could not get metadata "+"IMAGE_MAP_PROJECTION."+PDSImage.MAP_SCALE);
        	//throw e;
        }

		if (newScale<maps){
			throw new InvalidValueException("The minumum scale is "+maps);
		}
		scale=newScale;
		imagePanel.setImage(navigate(latitude,longitude));
		imagePanel.setScale(scale);
		rowheader.repaint();
		columnheader.repaint();
	}
	private  Point2D textToSpatial(String text) throws InvalidValueException
	{
		StringTokenizer t = new StringTokenizer(text, ",");
		int   nTokens = t.countTokens();
		


		if(nTokens == 2 )
		{
			try
			{
				double lat = Double.parseDouble(t.nextToken().trim());
				double lon = Double.parseDouble(t.nextToken().trim());
				return  new Point2D.Double(lat, lon);
			}catch (NumberFormatException e){
				throw new InvalidValueException("Illegal input. Numbers expected.");
			}
		}
		else
		{
			Toolkit.getDefaultToolkit().beep();
			throw new InvalidValueException("Illegal input format. Should be lat,lon");
		}
	}
	
	private BufferedImage scalateBufferedImage(BufferedImage before,double currentScale,double imageScale){
    	if (imageScale==currentScale) return before;
    	double sc = (1/(imageScale/currentScale));
    	System.out.println("****"+sc);
		int w = before.getWidth();
		int h = before.getHeight();
		BufferedImage after = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		AffineTransform at = new AffineTransform();
		at.scale(sc, sc);
		AffineTransformOp scaleOp = 
		   new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
		after = scaleOp.filter(before, after);
		return after;
	}
	
	class LocationListener implements ActionListener{
		PDSNavigator navigator;
		public LocationListener(PDSNavigator navigator){
			this.navigator=navigator;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				Point2D point = navigator.textToSpatial(((PasteField)e.getSource()).getText());
				System.out.println(point);
				navigator.imagePanel.setImage(navigate(point.getX(), point.getY()));
			} catch (InvalidValueException e1) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(frame,
					    e1.getMessage(),
					    "Navigation error",
					    JOptionPane.ERROR_MESSAGE);
				e1.printStackTrace();

				e1.printStackTrace();
			}
			
		}
		
	}

	class ScaleListener implements ActionListener{
		PDSNavigator navigator;
		public ScaleListener(PDSNavigator navigator){
			this.navigator=navigator;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				double newScale=Double.parseDouble(((PasteField)e.getSource()).getText());
				System.out.println(newScale);
				navigator.changeScale(newScale);
			} catch (InvalidValueException e1) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(frame,
					    e1.getMessage(),
					    "Navigation error",
					    JOptionPane.ERROR_MESSAGE);
				e1.printStackTrace();
			}
			
		}
		
	}
	
	class RowRulerLabel extends JLabel{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		DecimalFormat f2 = new DecimalFormat("0.#");
	      public void paintComponent(Graphics g) {
	          super.paintComponent(g);
	          Rectangle rect = g.getClipBounds();
	          for (int i = 50 - (rect.y % 50); i < rect.height; i += 50) {
	            g.drawLine(0, rect.y + i, 3, rect.y + i);
	            g.drawString("" + f2.format((rect.y + i)*scale), 6, rect.y + i + 3);
	          }
	        }

	        public Dimension getPreferredSize() {
	        	return new Dimension(30,700);
	        }
	}
	class ColumnRulerLabel extends JLabel{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		DecimalFormat f2 = new DecimalFormat("0.#");

	     public void paintComponent(Graphics g) {
	         super.paintComponent(g);
	         Rectangle r = g.getClipBounds();
	         for (int i = 50 - (r.x % 50); i < r.width; i += 50) {
	           g.drawLine(r.x + i, 0, r.x + i, 3);
	           g.drawString("" + f2.format((r.x + i)*scale), r.x + i - 10, 16);
	         }
	       }

	       public Dimension getPreferredSize() {
	    	   return new Dimension(445,25);
	       }
	}
	@Override
	public Icon getComponentIcon() {
		try {
			URL resource = PDSNavigator.class.getResource("/vega/hipe/pds/mars02.png");
			BufferedImage imageIcon = ImageIO.read(resource);
			return new ImageIcon(imageIcon);
		} catch (IOException e) {
			
			e.printStackTrace();
			return null;
		}
	}
	@Override
	public Class<? extends PDSImage> getVariableType() {
		return PDSImage.class;
	}
	
	public String getName(){
		return "PDS Image Navigator";
	}
	
    public boolean makeEditorContent() {
        PDSImage value = getValue();
        try {
			init(value);
			return true;
		} catch (InvalidValueException e) {
			e.printStackTrace();
			return false;
		}
    }


}
