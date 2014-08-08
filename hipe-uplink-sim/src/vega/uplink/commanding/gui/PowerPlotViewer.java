package vega.uplink.commanding.gui;

import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;

import vega.uplink.commanding.SimulationContext;
import herschel.ia.gui.kernel.parts.AbstractVariableEditorComponent;
import herschel.ia.gui.plot.LayerXY;
import herschel.ia.gui.plot.PlotXY;

public class PowerPlotViewer extends AbstractVariableEditorComponent<SimulationContext>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PowerPlotViewer(){
		super();
		this.setLayout(new GridLayout(1,1));
	}

	@Override
	public Icon getComponentIcon() {
	     try {
	            URL resource = HistoryModesPlot.class.getResource("/vega/vega.gif");
	            BufferedImage imageIcon = ImageIO.read(resource);
	            return new ImageIcon(imageIcon);
	    } catch (IOException e) {
	           
	            e.printStackTrace();
	            return null;
	    }	}

	@Override
	protected Class<? extends SimulationContext> getVariableType() {
		// TODO Auto-generated method stub
		return SimulationContext.class;
	}
	
    @Override
    protected boolean makeEditorContent() {
        String name = getKey();
        SimulationContext context = getValue();
        PlotXY plot3=new PlotXY(false);
		LayerXY layer6 = new LayerXY(context.getExecutionDates(),context.getHistoryPower());
		layer6.setColor(java.awt.Color.BLUE);
		LayerXY layer7 = new LayerXY(context.getZRecordDates(),context.getHistoryPowerZ());
		layer7.setColor(java.awt.Color.GREEN);
		LayerXY layer8 = new LayerXY(context.getExecutionDates(),context.getMocPowerHistory());
		layer8.setColor(java.awt.Color.RED);
		plot3.addLayer(layer6);
		plot3.addLayer(layer7);
		plot3.addLayer(layer8);
		plot3.setTitleText("ORCD/Z records (ITL)");
		layer7.setName("Power from Z records");
		layer6.setName("Power from ORCD");
		layer8.setName("Allowed power from moc");
		plot3.getXaxis().setTitleText("Time");
		plot3.getYaxis().setTitleText("Power (Watts)");
		plot3.getLayer(0).setXAxisType(herschel.ia.gui.plot.renderer.axtype.AxisType.DATE);
		plot3.getLegend().setVisible(true);
		JComponent content=plot3.getComponent();

        //JComponent content = ...;    // build the content with the provided value
        add(content);
        return true;
    }

}
