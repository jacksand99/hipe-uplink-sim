package vega.uplink.planning.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ObservationActionListener implements ActionListener{
	ObservationInterval interval;
	public ObservationActionListener(ObservationInterval interval){
		this.interval=interval;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println(interval);
		
	}

}
