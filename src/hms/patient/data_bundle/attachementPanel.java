package hms.patient.data_bundle;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JComboBox;

public class attachementPanel extends JPanel {
	private JTextField textField;
	private JTextField textField_1;

	/**
	 * Create the panel.
	 */
	public attachementPanel() {
		setLayout(null);
		
		textField = new JTextField();
		textField.setBounds(10, 11, 124, 25);
		add(textField);
		textField.setColumns(10);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setBounds(144, 12, 90, 23);
		add(comboBox);
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(244, 11, 90, 25);
		add(textField_1);

	}

}
