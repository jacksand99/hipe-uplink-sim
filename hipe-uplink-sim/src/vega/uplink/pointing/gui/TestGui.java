package vega.uplink.pointing.gui;

import java.awt.event.WindowAdapter;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import vega.uplink.pointing.PtrParameters.Offset.OffsetScan;
import vega.uplink.pointing.attitudes.Limb;

import com.sun.glass.events.WindowEvent;

public class TestGui extends JFrame {
	  public TestGui() {
			herschel.share.util.Configuration.setProperty("var.hcss.dir", "C:\\Users\\jarenas\\Downloads\\hcss-12.0.2524");
			herschel.share.util.Configuration.setProperty("var.hcsstest.dir", "C:\\Users\\jarenas\\Downloads\\hcss-12.0.2524");

		  //Limb pm = new Limb();
		  OffsetScan pm = new OffsetScan(new java.util.Date(), 1, 2, 0.1f, 0.2f, 10.0f, 20.0f, null, 30.0f, 100.0f, 200.0f, 300.0f, "y", false, true);
		  PointingMetadataEditor editor = new PointingMetadataEditor();
		  editor.setPointingMetadata(pm);
		  this.add(editor);
		    /*super("Multi-Line Cell Example");
		    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		    DefaultTableModel dm = new DefaultTableModel() {
		      public Class<String> getColumnClass(int columnIndex) {
		        return String.class;
		      }
		      public boolean isCellEditable(int row, int column) {
		        return false;
		      }
		    };
		    dm.setDataVector(
		        new Object[][]{
		            {"A0, Line1\n\tA0, Line2\n\t\tA0, Line3",
		             "B0, Line1\nB0, Line2",
		             "C0, Line1"},
		            {"A1, Line1",
		             "B1, Line1\nB1, Line2",
		             "C1, Line1"},
		            {"A2, Line1",
		             "B2, Line1",
		             "C2, Line1"}
		            },
		            new Object[] {"A", "B", "C"});

		    JTable table = new JTable(dm);
		    table.setDefaultRenderer(String.class, new MultiLineCellRenderer());
		    TableRowSorter<? extends TableModel> sort = new TableRowSorter<DefaultTableModel>(dm);
		    table.setRowSorter(sort);
		    JScrollPane scroll = new JScrollPane(table);
		    getContentPane().add(scroll);
		    setLocationByPlatform(true);*/
		    setSize(400, 430);
		    setVisible(true);
		  }

		  public static void main(String[] args) {
			  TestGui frame = new TestGui();
		    frame.addWindowListener(new WindowAdapter() {
		      public void windowClosing(WindowEvent e) {
		        System.exit(0);
		      }
		    });
		  }
		}
