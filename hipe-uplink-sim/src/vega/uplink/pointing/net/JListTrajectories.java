package vega.uplink.pointing.net;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import javax.swing.JList;
import javax.swing.ListModel;

import herschel.ia.gui.apps.modifier.ListEditor;
import herschel.ia.gui.apps.modifier.Modifier;
import herschel.ia.gui.apps.modifier.ModifierEditorListener;
import herschel.ia.gui.kernel.ParameterValidator;
import herschel.ia.gui.kernel.SiteEvent;
import herschel.ia.gui.kernel.SiteEventListener;
import herschel.ia.gui.kernel.VariableSelection;
//import herschel.ia.gui.kernel.parts.Disposable;
import herschel.share.util.ObjectUtil;

//public class JListTrajectories implements Modifier, ModifierEditorListener, SiteEventListener, Disposable {
public class JListTrajectories implements Modifier, ModifierEditorListener, SiteEventListener{
    private static final int MAX_VALUES = 32;
    private ListEditor editor;
    private JList<?> list;
	public JListTrajectories(String[] strings){
		list=new JList<String>(strings);
		editor=createEditor((Object[])strings);
	}
	/*@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}*/

	@Override
	public void selectionChanged(SiteEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void editorChanged() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object getObject() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ParameterValidator getParameterValidator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VariableSelection getVariableSelection() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setEnabled(boolean arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setObject(Object arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setParameterValidator(ParameterValidator arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setVariableSelection(VariableSelection arg0) {
		// TODO Auto-generated method stub
		
	}
	
    public int getItemCount() {
        return list.getModel().getSize();
    }

    /** Item at the given position. */
    public Object getItemAt(int index) {
        return list.getModel().getElementAt(index);
    }

    /** Index of the item in the options list; -1 if not found. */
    public int getIndexOf(Object item) {
        return editor.indexOf(item);
    }

    /**
     * Selects the given items.
     * If the items are not in the list it resets the selection
     */
    public void setSelectedValues(Object... items) {
        editor.setValue(Arrays.asList(items));
    }

    /** Returns the current selected indices. */
    public int[] getSelectedIndices() {
        return list.getSelectedIndices();
    }

    /** Returns the current selected indices. */
    public void setSelectedIndices(int[] indices) {
        list.setSelectedIndices(indices);
    }

    public Class<? extends List<?>> getDataType() {
        return ObjectUtil.cast(List.class);
    }

    protected ListEditor createEditor(Object... args) {
        Object arg = args[0];
        editor = (arg instanceof Vector<?>)? new ListEditor((Vector<?>)arg) :
                 (arg instanceof ListModel)? new ListEditor((ListModel<?>)arg) :
                                             new ListEditor(new Vector<Object>(Arrays.asList(args)));
        list = editor.getList();
        if (list.getModel().getSize() > MAX_VALUES) {
            String msg = "Maximum number of values (" + MAX_VALUES + ") exceeded : " + list.getModel().getSize();
            throw new IllegalArgumentException(msg);
        }
        return editor;
    }

    protected List<?> dataToEditor(List<?> list) {
        StringBuilder sb = new StringBuilder();
        if (list == null) {
            throw new IllegalArgumentException("Cannot accept null as a List");
        }
        for (Object o : list) {
            if (!editor.contains(o)) {
                sb.append(o).append(", ");
            }
        }
        if (sb.length() > 0) {
            throw new IllegalArgumentException("The following values are not in the list: " +
                                               sb.substring(0, sb.length() - 2));
        }
        return list;
    }

    protected List<?> editorToData(List<?> list) {
        return list;
    }


}
