package vega.hipe.preferences;

import java.util.Iterator;
import java.util.List;

import javax.swing.JFrame;

import vega.uplink.Properties;

public class TestPreferences {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JFrame frame = new JFrame();
		InstrumentsNamesPreferences uPreferences = new InstrumentsNamesPreferences();
		//uPreferences.set
		uPreferences.registerHandlers();
		uPreferences.makeContent();
		frame.add(uPreferences);
		frame.setVisible(true);
		List<String> instruments = Properties.getList(Properties.INSTRUMENT_NAMES_PROPERTIES);
		Iterator<String> it = instruments.iterator();
		while (it.hasNext()){
			String ins=it.next();
			//System.out.println(ins);
			//System.out.print(Properties.getColor(Properties.SUBINSTRUMENT_COLOR_PROPERTY_PREFIX+ins));
			System.out.println(Properties.SUBINSTRUMENT_COLOR_PROPERTY_PREFIX+ins+"="+Properties.getColor(Properties.SUBINSTRUMENT_COLOR_PROPERTY_PREFIX+ins).getRGB());
		}
		

	}

}
