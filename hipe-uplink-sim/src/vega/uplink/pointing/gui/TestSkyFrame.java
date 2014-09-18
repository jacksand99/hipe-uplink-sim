package vega.uplink.pointing.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.util.List;
import java.util.Vector;

import javax.swing.JFrame;

import herschel.share.fltdyn.gui.AdjustableSkyPane;
import herschel.share.fltdyn.gui.SkyPane;
import herschel.share.fltdyn.gui.SkyViewAdjuster;
import herschel.share.fltdyn.math.Quaternion;
import herschel.share.fltdyn.math.Vector3;

public class TestSkyFrame extends JFrame{
	//public static AttitudeGenerator instance;
	SkyViewAdjuster adjuster;
	SkyPane skypane;
	AdjustableSkyPane aSkyPane;
	public TestSkyFrame(){
		super();
		//TestSkyFrame frame = new TestSkyFrame();
		adjuster=new SkyViewAdjuster(10.0);
		adjuster.setViewPort(new Quaternion(0,0,0,0.0));
		//SkyPane skypane = new herschel.share.fltdyn.gui.SkyPane(adjuster);
		Vector<Vector3> list=new Vector<Vector3>();
		list.add(new Vector3(0.0010821020,-0.0018675001,0.99999767));
		list.add(new Vector3(0.5,-0.8,0.99999767));
		list.add(new Vector3(1,1,1));
		list.add(new Vector3(159,31,269));

		//list.add(new Vector3());
		//list.add(new Vector3());
		
		skypane = new DemoView(list);
		aSkyPane = new herschel.share.fltdyn.gui.AdjustableSkyPane(skypane,10.0);
		this.add(aSkyPane);
		this.setVisible(true);
		adjuster.setViewPort(new Quaternion(0.1,0.1,0.1,0.0));

	}
	public static void main(String[] args) {
		new TestSkyFrame();
		/*
		// TODO Auto-generated method stub
		TestSkyFrame frame = new TestSkyFrame();
		SkyViewAdjuster adjuster=new SkyViewAdjuster(1.0);
		adjuster.setViewPort(new Quaternion(0.48386531145804212,0.43239370294900265,0.71399302075734583,0.26291445819403297));
		//SkyPane skypane = new herschel.share.fltdyn.gui.SkyPane(adjuster);
		Vector<Vector3> list=new Vector<Vector3>();
		list.add(new Vector3(0.0010821020,-0.0018675001,0.99999767));
		//list.add(new Vector3());
		//list.add(new Vector3());
		
		SkyPane skypane = frame.new DemoView(list);
		AdjustableSkyPane aSkyPane = new herschel.share.fltdyn.gui.AdjustableSkyPane(skypane,1.0);
		frame.add(aSkyPane);
		frame.setVisible(true);
		//instance=frame;*/

	}
	
	class DemoView extends SkyPane {
	    /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private List<Vector3> _vectors;

	    public DemoView(List d) {
	        super();
	        _vectors = d;
	        setPreferredSize(new Dimension(700, 700));
	        MouseAdapter mouse = createZoomMouseListener();
	        addMouseMotionListener(mouse);
	        addMouseListener(mouse);
	    }

	    protected void drawContents(Graphics g) {
	        g.setColor(Color.red);
	        Vector3 d = _vectors.get(0);
	        moveTo(g,d);
	        for(Vector3 di : _vectors) {
	            drawLineTo(g, di);
	        }
	    }
	}


}
