package vega.uplink.pointing.gui;

import java.awt.event.WindowAdapter;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import vega.hipe.gui.xmlutils.XMLTextEditor;
import vega.uplink.Properties;
import vega.uplink.pointing.Ptr;
import vega.uplink.pointing.PtrUtils;
import vega.uplink.pointing.PtrParameters.Offset.OffsetScan;
import vega.uplink.pointing.attitudes.Limb;

import com.sun.glass.events.WindowEvent;

public class TestGui extends JFrame {
	  /*public TestGui() {
			herschel.share.util.Configuration.setProperty("var.hcss.dir", "C:\\Users\\jarenas\\Downloads\\hcss-12.0.2524");
			herschel.share.util.Configuration.setProperty("var.hcsstest.dir", "C:\\Users\\jarenas\\Downloads\\hcss-12.0.2524");

		  //Limb pm = new Limb();
		  OffsetScan pm = new OffsetScan(new java.util.Date(), 1, 2, 0.1f, 0.2f, 10.0f, 20.0f, null, 30.0f, 100.0f, 200.0f, 300.0f, "y", false, true);
		  PointingElementEditor editor = new PointingElementEditor();
		  editor.setPointingMetadata(pm);
		  this.add(editor);

		    setSize(400, 430);
		    setVisible(true);
		  }*/

		  public static void main(String[] args) {
				herschel.share.util.Configuration.setProperty("var.hcss.dir", "C:\\Users\\jarenas\\Downloads\\hcss-12.0.2524");
				herschel.share.util.Configuration.setProperty("var.hcsstest.dir", "C:\\Users\\jarenas\\Downloads\\hcss-12.0.2524");
				//herschel.share.util.Configuration.setProperty(Mib.ROSETTA_MIB_LOCATION, "Z:\\MAPPS\\MIB");
				herschel.share.util.Configuration.setProperty(Properties.DEFAULT_EVT_DIRECTORY, "Z:\\MAPPS\\RMOC\\");
				herschel.share.util.Configuration.setProperty(Properties.PWPL_FILE, "C:\\ROS_SGS\\PLANNING\\RMOC\\FCT\\PWPL_14_001_14_365__OPT_01.ROS");
				herschel.share.util.Configuration.setProperty(Properties.ORCD_FILE, "Z:\\MAPPS\\MIB\\orcd.csv");
				herschel.share.util.Configuration.setProperty(Properties.DEFAULT_PLANNING_DIRECTORY, "C:\\ROS_SGS\\PLANNING\\");

				try{
					Ptr ptr1=PtrUtils.readPTRfromFile("/Users/jarenas 1/Rosetta/testing/comparePtr/PTRM_001.ROS");
					TestGui frame = new TestGui();
					//PointingElementXMLEditor editor = new PointingElementXMLEditor();
					PtrXmlEditor editor = new PtrXmlEditor();
					//editor.setPointingMetadata(ptr1.getSegments()[0].getBlocks()[0]);
					editor.setPtr(ptr1);
					//XMLTextEditor xmlEditor = new XMLTextEditor();
					//xmlEditor.setText(ptr1.toXml());
					frame.add(editor);
					frame.pack();
					frame.setVisible(true);
				}
				catch(Exception e){
					e.printStackTrace();
				}
		  }
		}
