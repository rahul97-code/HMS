package hms.Printer;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JCheckBox;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import java.awt.Color;

public class SlipPanels extends JPanel {
	public JCheckBox SlipCheckBox;
	public JLabel SlipNameLBL;

	public SlipPanels(String SlipName ,boolean b) {
		setBorder(new LineBorder(Color.DARK_GRAY));
		setBounds(100, 100, 388, 30);
		setLayout(null);
		
		SlipNameLBL = new JLabel("...");
		SlipNameLBL.setFont(new Font("Dialog", Font.ITALIC, 13));
		SlipNameLBL.setBounds(22, 0, 288, 27);
		add(SlipNameLBL);
		SlipNameLBL.setText(SlipName);
		
		SlipCheckBox = new JCheckBox("Y/N");
		SlipCheckBox.setFont(new Font("Dialog", Font.ITALIC, 13));
		SlipCheckBox.setBounds(324, 1, 56, 27);
		add(SlipCheckBox);
		SlipCheckBox.setSelected(b);
	}
}
