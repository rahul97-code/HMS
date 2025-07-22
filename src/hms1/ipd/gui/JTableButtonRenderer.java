package hms1.ipd.gui;

import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;

class JTableButtonRenderer implements TableCellRenderer {		
	
	@Override
	public Component getTableCellRendererComponent(JTable arg0, Object arg1,
			boolean arg2, boolean arg3, int arg4, int arg5) {
		// TODO Auto-generated method stub
		JButton button = (JButton)arg1;
		if (arg2) {
			button.setForeground(arg0.getSelectionForeground());
			button.setBackground(arg0.getSelectionBackground());
	    } else {
	    	button.setForeground(arg0.getForeground());
	    	button.setBackground(UIManager.getColor("Button.background"));
	    }
		return button;
	}
}