//package vega.uplink.pointing.gui;

/*public class PointingMetadataOutline {

}*/

package vega.uplink.pointing.gui;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

//import javafx.scene.control.TableColumn;


import javax.swing.JComponent;





import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.UIManager;










//import rosetta.uplink.commanding.Por;
//import rosetta.uplink.commanding.PorUtils;
import herschel.ia.dataset.gui.views.ProductOutline;
import herschel.ia.gui.apps.components.outline.ObjectOutline;
import herschel.ia.gui.apps.components.outline.StringOutline;
import herschel.ia.gui.apps.views.outline.OutlineComponent;
import herschel.ia.gui.apps.views.outline.OutlineView;
import herschel.ia.gui.kernel.Selection;
import herschel.ia.gui.kernel.SelectionVisitor;
import herschel.ia.gui.kernel.VariableSelection;
import herschel.ia.gui.kernel.util.TableUtil;
import herschel.ia.pal.gui.components.ContextOutline;
import herschel.share.interpreter.InterpreterUtil;

import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import vega.uplink.pointing.*;
//public class PointingMetadataOutline extends MultilineVariableOutline implements OutlineComponent{
public class PointingMetadataOutline  extends JPanel implements OutlineComponent{
	/**
	 * 
	 */
    private Selection selection;
    private OutlineView view;
	private static final long serialVersionUID = 1L;
	JTable table;
	protected static final Object[] TABLE_HEADER = { "Key", "Value" };
	//VariableSelection selection;

    /*@Override
    protected Object[][] getExtraTableInfo() { 
 
        Object[] row0 = { "Value", ((PointingMetadata)getSelection().getValue()).toString() };
        Object[] row1 = { "XML", ((PointingMetadata)getSelection().getValue()).toXml(0) };
        String key = getSelection().getKey();

        return new Object[][] { row0, row1 };
    }*/

	@Override
	public JComponent asComponent() {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public boolean init(Selection arg0, OutlineView arg1) {
		this.selection=arg0;
		view=arg1;
		setBackground(Color.WHITE);
		setLayout(new BorderLayout(0, 2));
		addIfExisting(getTopComponent(), BorderLayout.NORTH);
		//addIfExisting(getExtraComponent(), BorderLayout.CENTER);

		return true;
		// TODO Auto-generated method stub
		//return false;
	}
    /** Adds a component if not null. */
    private void addIfExisting(Component component, String constraint) {
	if (component != null) {
	    add(component, constraint);
	}
    }

    protected JComponent getTopComponent() {
        if (table == null) {
        	String cName = "-";
        	String pName = "(default)";
        	Object value = ((VariableSelection)selection).getValue();
        	if (value != null) {
        	    cName = InterpreterUtil.getClassName(value);
        	    int index = cName.lastIndexOf('.');
        	    if (index >= 0) {
        		pName = cName.substring(0, index);
        		cName = cName.substring(index + 1);
        	    }
        	}
        	Object[] row0={ "Name", ((VariableSelection)selection).getKey() };
        	Object[] row1={ "Class", cName };
        	Object[] row2={ "Package", pName };
            //Object[] row3 = { "Value", ((PointingMetadata)((VariableSelection)selection).getValue()).toString() };
            Object[] row4 = { "XML", ((PointingMetadata)((VariableSelection)selection).getValue()).toXml(0) };
            Object[][] data = new Object[][] { row0, row1,row2,row4 };
        	/*int length = common.length + extra.length;
        	if (length == 0) { return; }
        	Object[][] data = new Object[length][2];*/

        	// Join common and extra information into one data array
        	/*System.arraycopy(common, 0, data, 0, common.length);
        	System.arraycopy(extra, 0, data, common.length, extra.length);*/
            DefaultTableModel dm = new DefaultTableModel() {
        	      public Class<String> getColumnClass(int columnIndex) {
        	        return String.class;
        	      }
        	      public boolean isCellEditable(int row, int column) {
        	        return false;
        	      }
        	    };
        	dm.setDataVector(data,TABLE_HEADER);
            JTable table = new JTable(dm);
            table.setDefaultRenderer(String.class, new MultiLineCellRenderer());
            TableRowSorter<? extends TableModel> sort = new TableRowSorter<DefaultTableModel>(dm);
            table.setRowSorter(sort);
        	// Create the table summary
        	/*table = new JTable(new DefaultTableModel(data, TABLE_HEADER)) {
        	    private static final long serialVersionUID = 1L;
        	    @Override public boolean isCellEditable(int row, int column) {
        	        return false;
        	    }
        	};*/
        	table.setCellSelectionEnabled(true);
            /*for(int column = 0; column < table.getColumnCount(); column ++){
                int  width =0;
            for (int row = 0; row < table.getRowCount(); row++) {
                 TableCellRenderer renderer = table.getCellRenderer(row, column);
                 Component comp = table.prepareRenderer(renderer, row, column);
                 width = Math.max (comp.getPreferredSize().width, width);
                 //System.out.println(width);
             }
            TableColumn col = new TableColumn();
            col = table.getColumnModel().getColumn(column);
            System.out.println(width);*/
            //col.setWidth(width);
            //gui.testsuiteInfoTable.revalidate();

            //}
        	//table.setDefaultRenderer(String.class, new MultiLineCellRenderer());
        	TableUtil.resizeTable(table);
        	add(table, BorderLayout.NORTH);
            TableColumn col = new TableColumn();
            col = table.getColumnModel().getColumn(0);
            col.setWidth(50);
        	revalidate();
        	repaint();
        }
        return table;
    }
    
    
    
    


	


}
