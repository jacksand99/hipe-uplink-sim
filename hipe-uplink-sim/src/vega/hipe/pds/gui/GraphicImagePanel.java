package vega.hipe.pds.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;

import javax.swing.JLabel;

class GraphicImagePanel extends JLabel
{  
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Image image;  
    Line2D.Double line;  
    double distance=0;
    private static final DecimalFormat f = new DecimalFormat("0.###");
    double scale;
    
   
    public GraphicImagePanel(double scale,BufferedImage image)  
    {  
    	this.image=image;
    	this.scale=scale;
    	this.setSize(image.getWidth(),image.getHeight());
        line = new Line2D.Double(0,0,0,0);  
        GraphicMover mover = new GraphicMover(this);  
        addMouseListener(mover);  
        addMouseMotionListener(mover);  
    }  
    
    public void setImage(BufferedImage image){
    	this.image=image;
    	this.setSize(image.getWidth(),image.getHeight());
    	repaint();
    }
    
    public void setScale(double scale){
    	this.scale=scale;
    	repaint();
    }
    
    protected void paintComponent(Graphics g)  
    {  
        super.paintComponent(g);  
        Graphics2D g2 = (Graphics2D)g;  
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,  
                            RenderingHints.VALUE_ANTIALIAS_ON);  
        g2.drawImage(image,0,0,this);
        g2.setPaint(Color.red);  
        if (distance>0){
        	g2.draw(line);
        	g.drawString("" + f.format(distance)+" km", (new Double(line.x1+10)).intValue() , (new Double(line.y1+10)).intValue());
        }
    }  
    
}  
   
