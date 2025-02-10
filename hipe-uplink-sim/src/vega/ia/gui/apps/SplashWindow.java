package vega.ia.gui.apps;

import java.io.IOException;

import javax.imageio.ImageIO;

//import herschel.ia.gui.kernel.util.SplashWindow;

public class SplashWindow extends herschel.ia.gui.kernel.util.SplashWindow {
	public SplashWindow() throws IOException{
		
		super();
		//java.awt.Image image = new java.awt.Image(); 
		java.awt.Image img = ImageIO.read(RspStarter.class.getResource("splash.jpg"));
		System.out.println(img);
		imageUpdate(img, 1, 1, 1, 625, 403);
	}
	public SplashWindow(boolean b) throws IOException{
		
		super(b);
		//java.awt.Image image = new java.awt.Image(); 
		java.awt.Image img = ImageIO.read(RspStarter.class.getResource("splash.jpg"));
		System.out.println(img);
		imageUpdate(img, 1, 1, 1, 625, 403);
	}

}
