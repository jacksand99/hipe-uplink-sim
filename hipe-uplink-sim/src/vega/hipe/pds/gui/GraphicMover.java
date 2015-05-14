package vega.hipe.pds.gui;

import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import javax.swing.event.MouseInputAdapter;

class GraphicMover extends MouseInputAdapter  
{  
    GraphicImagePanel imagePanel;  
    Point2D.Double offset;  
    boolean dragging;  
   
    public GraphicMover(GraphicImagePanel gip)  
    {  
        imagePanel = gip;  
        offset = new Point2D.Double();  
        dragging = false;  
    }  
   
    public void mousePressed(MouseEvent e)  
    {  
        imagePanel.line.x1 = e.getX();  
        imagePanel.line.y1 = e.getY();
        dragging=true;
    }  
   
    public void mouseReleased(MouseEvent e)  
    {  
    	imagePanel.line=new Line2D.Double(0,0,0,0);
    	imagePanel.distance=0;
        dragging = false; 
        imagePanel.repaint();
    }  
   
    public void mouseDragged(MouseEvent e)  
    {  
        if(dragging)  
        {  
            imagePanel.line.x2 = e.getX();  
            imagePanel.line.y2 = e.getY();
            imagePanel.distance = Math.sqrt((imagePanel.line.x1-imagePanel.line.x2)*(imagePanel.line.x1-imagePanel.line.x2) + (imagePanel.line.y1-imagePanel.line.y2)*(imagePanel.line.y1-imagePanel.line.y2))*imagePanel.scale;
            imagePanel.repaint();  
        }  
    }  
}  
