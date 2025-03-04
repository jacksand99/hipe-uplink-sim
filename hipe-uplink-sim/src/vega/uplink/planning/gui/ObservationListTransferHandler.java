package vega.uplink.planning.gui;

import javax.swing.*;

import org.jfree.util.Log;

import vega.hipe.logging.VegaLog;

import java.awt.datatransfer.*;
import java.util.logging.Logger;
 
public class ObservationListTransferHandler extends TransferHandler {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int[] indices = null;
    private int addIndex = -1; //Location where items were added
    private int addCount = 0;  //Number of items added.
    //private final Logger LOG = Logger.getLogger(ObservationListTransferHandler.class.getName());        
    public boolean canImport(TransferHandler.TransferSupport info) {
    	VegaLog.info("canImport");
        if (!info.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            return false;
        }
        return true;
   }
 
    protected Transferable createTransferable(JComponent c) {
    	Log.info("Transferable");
        return new StringSelection(exportString(c));
    }
     
    public int getSourceActions(JComponent c) {
    	Log.info("getSourceActions");
        return TransferHandler.COPY_OR_MOVE;
    }
     
    public boolean importData(TransferHandler.TransferSupport info) {
    	System.out.println("importData");
    	System.out.println(info.getTransferable().getClass());
    	Thread.dumpStack();
    	Log.info("importData");
        if (!info.isDrop()) {
            return false;
        }
 
        JList list = (JList)info.getComponent();
        DefaultListModel listModel = (DefaultListModel)list.getModel();
        JList.DropLocation dl = (JList.DropLocation)info.getDropLocation();
        int index = dl.getIndex();
        boolean insert = dl.isInsert();
 
        // Get the string that is being dropped.
        Transferable t = info.getTransferable();
        String data;
        try {
            data = (String)t.getTransferData(DataFlavor.stringFlavor);
        }
        catch (Exception e) { return false; }
                                 
        // Perform the actual import. 
        if (insert) {
            listModel.add(index, data);
        } else {
            listModel.set(index, data);
        }
        return true;
    }
 
    protected void exportDone(JComponent c, Transferable data, int action) {
    	Log.info("exportDone");
        cleanup(c, action == TransferHandler.MOVE);
    }
 
    //Bundle up the selected items in the list
    //as a single string, for export.
    protected String exportString(JComponent c) {
    	Log.info("exportString");
        JList list = (JList)c;
        indices = list.getSelectedIndices();
        Object[] values = list.getSelectedValues();
         
        StringBuffer buff = new StringBuffer();
 
        for (int i = 0; i < values.length; i++) {
            Object val = values[i];
            buff.append(val == null ? "" : val.toString());
            if (i != values.length - 1) {
                buff.append("\n");
            }
        }
         
        return buff.toString();
    }
 
    //Take the incoming string and wherever there is a
    //newline, break it into a separate item in the list.
    protected void importString(JComponent c, String str) {
    	Log.info("importString");
        JList target = (JList)c;
        DefaultListModel listModel = (DefaultListModel)target.getModel();
        int index = target.getSelectedIndex();
 
        //Prevent the user from dropping data back on itself.
        //For example, if the user is moving items #4,#5,#6 and #7 and
        //attempts to insert the items after item #5, this would
        //be problematic when removing the original items.
        //So this is not allowed.
        if (indices != null && index >= indices[0] - 1 &&
              index <= indices[indices.length - 1]) {
            indices = null;
            return;
        }
 
        int max = listModel.getSize();
        if (index < 0) {
            index = max;
        } else {
            index++;
            if (index > max) {
                index = max;
            }
        }
        addIndex = index;
        String[] values = str.split("\n");
        addCount = values.length;
        for (int i = 0; i < values.length; i++) {
            listModel.add(index++, values[i]);
        }
    }
     
    //If the remove argument is true, the drop has been
    //successful and it's time to remove the selected items
    //from the list. If the remove argument is false, it
    //was a Copy operation and the original list is left
    //intact.
    protected void cleanup(JComponent c, boolean remove) {
    	Log.info("cleanup");
        if (remove && indices != null) {
            JList source = (JList)c;
            DefaultListModel model  = (DefaultListModel)source.getModel();
            //If we are moving items around in the same list, we
            //need to adjust the indices accordingly, since those
            //after the insertion point have moved.
            if (addCount > 0) {
                for (int i = 0; i < indices.length; i++) {
                    if (indices[i] > addIndex) {
                        indices[i] += addCount;
                    }
                }
            }
            for (int i = indices.length - 1; i >= 0; i--) {
                model.remove(indices[i]);
            }
        }
        indices = null;
        addCount = 0;
        addIndex = -1;
    }
}
