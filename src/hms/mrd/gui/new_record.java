package hms.mrd.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

import hms.mrd.database.MRDUserDBConnection;

public class new_record extends JDialog {

	private JTextField txtDepartmentId;
	private JTextField txtDepartmentName;
	private JTextField txtRoomNo;
	private JTextField txtRoomName;
	private JTextField txtRackNo;
	private JTextField txtRow;
	private JTextField txtCol;
	private JTextField txtEntryUser;

	private JPanel tablePanel;
	private JTable dynamicTable;
	private JScrollPane scrollPane;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new_record dialog = new new_record();
					dialog.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public new_record() {
		initialize();
	}

	private void initialize() {
		setSize(779, 700);              // Fixed window size
	    setLocationRelativeTo(null);    // Center the dialog
	    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    setTitle("Library Master - New Record");
	    getContentPane().setLayout(null);
	    setModal(true);

		JLabel lblTitle = new JLabel("Library Master - New Record");
		lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
		lblTitle.setForeground(new Color(34, 139, 34));
		lblTitle.setBounds(200, 10, 400, 40);
		getContentPane().add(lblTitle);

		JLabel lblDepartmentId = new JLabel("Department ID:");
		lblDepartmentId.setFont(new Font("Arial", Font.PLAIN, 14));
		lblDepartmentId.setBounds(50, 80, 150, 30);
		getContentPane().add(lblDepartmentId);

		txtDepartmentId = new JTextField(MrdMain.mrdID);
		txtDepartmentId.setEditable(false);
		txtDepartmentId.setBounds(200, 80, 200, 30);
		txtDepartmentId.setFont(new Font("Arial", Font.PLAIN, 14));
		getContentPane().add(txtDepartmentId);

		JLabel lblDepartmentName = new JLabel("Department Name:");
		lblDepartmentName.setFont(new Font("Arial", Font.PLAIN, 14));
		lblDepartmentName.setBounds(50, 120, 150, 30);
		getContentPane().add(lblDepartmentName);

		txtDepartmentName = new JTextField("MRD");
		txtDepartmentName.setEditable(false);
		txtDepartmentName.setBounds(200, 120, 200, 30);
		txtDepartmentName.setFont(new Font("Arial", Font.PLAIN, 14));
		getContentPane().add(txtDepartmentName);

		JLabel lblRoomNo = new JLabel("Room No:");
		lblRoomNo.setFont(new Font("Arial", Font.PLAIN, 14));
		lblRoomNo.setBounds(50, 160, 150, 30);
		getContentPane().add(lblRoomNo);

		txtRoomNo = new JTextField();
		txtRoomNo.setBounds(200, 160, 200, 30);
		txtRoomNo.setFont(new Font("Arial", Font.PLAIN, 14));
		getContentPane().add(txtRoomNo);

		JLabel lblRoomName = new JLabel("Room Name:");
		lblRoomName.setFont(new Font("Arial", Font.PLAIN, 14));
		lblRoomName.setBounds(50, 200, 150, 30);
		getContentPane().add(lblRoomName);

		txtRoomName = new JTextField();
		txtRoomName.setBounds(200, 200, 200, 30);
		txtRoomName.setFont(new Font("Arial", Font.PLAIN, 14));
		getContentPane().add(txtRoomName);

		JLabel lblRackNo = new JLabel("Rack Name:");
		lblRackNo.setFont(new Font("Arial", Font.PLAIN, 14));
		lblRackNo.setBounds(50, 240, 150, 30);
		getContentPane().add(lblRackNo);

		txtRackNo = new JTextField();
		txtRackNo.setBounds(200, 240, 200, 30);
		txtRackNo.setFont(new Font("Arial", Font.PLAIN, 14));
		getContentPane().add(txtRackNo);

		JLabel lblRow = new JLabel("Row:");
		lblRow.setFont(new Font("Arial", Font.PLAIN, 14));
		lblRow.setBounds(50, 280, 150, 30);
		getContentPane().add(lblRow);

		txtRow = new JTextField();
		txtRow.setBounds(200, 280, 200, 30);
		txtRow.setFont(new Font("Arial", Font.PLAIN, 14));
		getContentPane().add(txtRow);

		JLabel lblCol = new JLabel("Column:");
		lblCol.setFont(new Font("Arial", Font.PLAIN, 14));
		lblCol.setBounds(50, 320, 150, 30);
		getContentPane().add(lblCol);

		txtCol = new JTextField("0");
		txtCol.setBounds(200, 320, 200, 30);
		txtCol.setFont(new Font("Arial", Font.PLAIN, 14));
		getContentPane().add(txtCol);

		JLabel lblEntryUser = new JLabel("Entry User:");
		lblEntryUser.setFont(new Font("Arial", Font.PLAIN, 14));
		lblEntryUser.setBounds(40, 360, 150, 30);
		getContentPane().add(lblEntryUser);

		txtEntryUser = new JTextField(MrdMain.userName);
		txtEntryUser.setEditable(false);
		txtEntryUser.setBounds(200, 361, 200, 30);
		txtEntryUser.setFont(new Font("Arial", Font.PLAIN, 14));
		getContentPane().add(txtEntryUser);

		// Table panel
		tablePanel = new JPanel();
		tablePanel.setBounds(49, 425, 680, 220);
		tablePanel.setLayout(new BorderLayout());
		getContentPane().add(tablePanel);

		// Listener for txtRow to update table instantly
		txtRow.getDocument().addDocumentListener(new DocumentListener() {
			public void insertUpdate(DocumentEvent e) { generateDynamicTable(); }
			public void removeUpdate(DocumentEvent e) { generateDynamicTable(); }
			public void changedUpdate(DocumentEvent e) { generateDynamicTable(); }
		});

		// Save button
		JButton btnSave = new JButton("Save");
		btnSave.setFont(new Font("Arial", Font.BOLD, 14));
		btnSave.setBackground(new Color(34, 139, 34));
		btnSave.setBounds(500, 80, 150, 40);
		getContentPane().add(btnSave);

		// Clear button
		JButton btnClear = new JButton("Clear");
		btnClear.setFont(new Font("Arial", Font.BOLD, 14));
		btnClear.setBackground(new Color(255, 69, 0));
		btnClear.setBounds(500, 140, 150, 40);
		getContentPane().add(btnClear);

		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				// Validate required text fields only (leave out table cell check)
				if (txtRoomNo.getText().trim().isEmpty() ||
					txtRoomName.getText().trim().isEmpty() ||
					txtRackNo.getText().trim().isEmpty() ||
					txtRow.getText().trim().isEmpty() ||
					txtCol.getText().trim().isEmpty() ||
					txtDepartmentId.getText().trim().isEmpty() ||
					txtDepartmentName.getText().trim().isEmpty() ||
					txtEntryUser.getText().trim().isEmpty()) {

					JOptionPane.showMessageDialog(new_record.this, "All text fields are required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
					return;
				}

				// Ensure table is generated
				if (dynamicTable == null || dynamicTable.getRowCount() == 0) {
					JOptionPane.showMessageDialog(new_record.this, "Please enter Row number to generate the table.", "Table Missing", JOptionPane.ERROR_MESSAGE);
					return;
				}

				// Prepare and save data
				String[] data = new String[11];
				MRDUserDBConnection db = new MRDUserDBConnection();
				data[0] = txtDepartmentId.getText().trim();
				data[1] = txtDepartmentName.getText().trim();
				data[2] = txtRoomNo.getText().trim();
				data[3] = txtRoomName.getText().trim();
				data[4] = txtRackNo.getText().trim();
				data[6] = txtCol.getText().trim();
				data[10] = txtEntryUser.getText().trim();

				for (int i = 0; i < dynamicTable.getRowCount(); i++) {
					data[5] = dynamicTable.getValueAt(i, 0).toString();  // Row No
					data[7] = getSafeValue(dynamicTable.getValueAt(i, 1))==""?"0":getSafeValue(dynamicTable.getValueAt(i, 1)); // Start IPD
					data[8] = getSafeValue(dynamicTable.getValueAt(i, 2))==""?"0":getSafeValue(dynamicTable.getValueAt(i, 2)); // End IPD
					data[9] = getSafeValue(dynamicTable.getValueAt(i, 3))==""?"0":getSafeValue(dynamicTable.getValueAt(i, 3)); // Year

					boolean result = db.insertRackData(data);
					// Optional: handle result
				}

				JOptionPane.showMessageDialog(new_record.this, "Data has been saved (including empty fields).", "Success", JOptionPane.INFORMATION_MESSAGE);
				 // Clear the table after saving
		        tablePanel.removeAll();
		        tablePanel.revalidate();
		        tablePanel.repaint();
		        txtRow.setText("");
			
			}
		});


		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				txtDepartmentId.setText("");
				txtDepartmentName.setText("");
				txtRoomNo.setText("");
				txtRoomName.setText("");
				txtRackNo.setText("");
				txtRow.setText("");
				txtCol.setText("");
//				txtEntryUser.setText("");
				tablePanel.removeAll();
				tablePanel.revalidate();
				tablePanel.repaint();
			}
		});
	}

	private String getSafeValue(Object obj) {
		return obj == null ? "" : obj.toString().trim();
	}
	
	private void generateDynamicTable() {
	    String rowText = txtRow.getText().trim();
	    int rows;
	    try {
	        rows = Integer.parseInt(rowText);
	        if (rows <= 0) throw new NumberFormatException();
	    } catch (NumberFormatException e) {
	        tablePanel.removeAll();
	        tablePanel.revalidate();
	        tablePanel.repaint();
	        return;
	    }

	    String[] columnNames = { "Row No", "Start IPD", "End IPD", "Year" };
	    Object[][] data = new Object[rows][4];

	    for (int i = 0; i < rows; i++) {
	        data[i][0] = (i + 1); // Row No (non-editable)
	        data[i][1] = "";       // Start IPD default to 0
	        data[i][2] = "";       // End IPD default to 0
	        data[i][3] = "";       // Year default to 0
	    }

	    DefaultTableModel model = new DefaultTableModel(data, columnNames) {
	        @Override
	        public boolean isCellEditable(int row, int column) {
	            return column != 0; // Make "Row No" non-editable
	        }

	        @Override
	        public Class<?> getColumnClass(int columnIndex) {
	            if (columnIndex == 0) return Integer.class;
	            return Integer.class; // Ensure correct column type for editing/spinner
	        }
	    };

	    dynamicTable = new JTable(model);
	    scrollPane = new JScrollPane(dynamicTable);

	    tablePanel.removeAll();
	    tablePanel.add(scrollPane, BorderLayout.CENTER);
	    tablePanel.revalidate();
	    tablePanel.repaint();
	}

}