package vega.uplink.commanding.gui;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import java.awt.Component;

import herschel.ia.gui.apps.components.outline.AbstractOutline;
import herschel.ia.gui.apps.components.variable.AbstractVariableTree;
import herschel.ia.gui.apps.views.outline.OutlineComponent;
import herschel.ia.gui.apps.views.outline.OutlineView;
import herschel.ia.gui.kernel.Selection;
import herschel.ia.gui.kernel.VariableSelection;
import herschel.ia.gui.kernel.util.TableUtil;
import herschel.ia.dataset.gui.views.ProductOutline;
import herschel.ia.dataset.gui.views.ProductTree;
import herschel.share.interpreter.InterpreterUtil;

public class PorOutline<S extends Selection> extends JPanel implements OutlineComponent { 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	herschel.ia.pal.gui.components.ContextOutline po;
	private JComponent extraComponent;
    /** Default table header for outlines showing a table. */
    protected static final Object[] TABLE_HEADER = { "Key", "Value" };

    /** Empty array used for defaults. */
    protected static final Object[][] EMPTY_TABLE_INFO = {};

    private VariableSelection selection;
    private OutlineView view;
    private JTable table;

	public PorOutline(){
		po=new herschel.ia.pal.gui.components.ContextOutline();
		
	}

   

    public final Class<VariableSelection> getSelectionType() {
    
	return VariableSelection.class;
    }

    /** Common information to be shown for all variables. */
   // @Override
    public final Object[][] getCommonTableInfo() {
    	
	String cName = "-";
	String pName = "(default)";
	Object value = getSelection().getValue();
	if (value != null) {
	    cName = InterpreterUtil.getClassName(value);
	    int index = cName.lastIndexOf('.');
	    if (index >= 0) {
		pName = cName.substring(0, index);
		cName = cName.substring(index + 1);
	    }
	}
	return new Object[][] { { "Name", getSelection().getKey() },
	                        { "Class", cName },
	                        { "Package", pName } };
    }
    
    public ProductTree getTree() {
    	
	return new ProductTree();
    }
    
    protected JComponent getExtraComponent() {
    	
        if (extraComponent == null) {
            AbstractVariableTree<?> tree = getTree();
            if (tree.init(getSelection(), OutlineView.ID)) {
                extraComponent = tree;
            } else {
                extraComponent = new JLabel(" Failed to initialize the variable tree");
            }
        }
	return extraComponent;
    }
    

    @Override
    public JComponent asComponent() {
    	return po.asComponent();
	//return this;
    }

    @Override
    public boolean init(Selection selection, OutlineView view) {
    	return po.init(selection, view);

    }

    /** Checks the selection. */
    public boolean canHandle(Selection selection) {
    	
        return getSelectionType().isInstance(selection);
    }


    /** Returns the associated selection. */
    public final VariableSelection getSelection() {
    	
    	return selection; }

    /** Returns the outline view. */
    public final OutlineView getView() { return view; }

    /** Top component. A header table by default. */
    public JComponent getTopComponent() {
    	
        if (table == null) {
            addTableInfo();
        }
        return table;
    }



    /** Extra information for table summary. May be overriden by children. */
    public Object[][] getExtraTableInfo()  {
    	
    	return EMPTY_TABLE_INFO; }

    /** Common information to be shown for all items. None by default. */

    /** Sets the table information, if existing, at the top. */
    private void addTableInfo() {
    

	// Obtain the common and extra information
	final Object[][] common = getCommonTableInfo();
	final Object[][] extra  = getExtraTableInfo();
	int length = common.length + extra.length;
	if (length == 0) { return; }
	Object[][] data = new Object[length][2];

	// Join common and extra information into one data array
	System.arraycopy(common, 0, data, 0, common.length);
	System.arraycopy(extra, 0, data, common.length, extra.length);

	// Create the table summary
	table = new JTable(new DefaultTableModel(data, TABLE_HEADER)) {
	    private static final long serialVersionUID = 1L;
	    @Override public boolean isCellEditable(int row, int column) {
	        return false;
	    }
	};
	table.setCellSelectionEnabled(true);
	TableUtil.resizeTable(table);
	add(table, BorderLayout.NORTH);
	revalidate();
	repaint();
    }

    /** Adds a component if not null. */
    private void addIfExisting(Component component, String constraint) {
	if (component != null) {
	    add(component, constraint);
	}
	
	
    }

}
