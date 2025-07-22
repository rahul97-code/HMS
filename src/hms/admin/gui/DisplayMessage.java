package hms.admin.gui;

import hms.amountreceipt.database.AmountReceiptDBConnection;
import hms.doctor.database.DoctorDBConnection;
import hms.doctor.gui.DoctorBrowser;
import hms.exam.database.ExamDBConnection;
import hms.exams.gui.ExamsBrowser;
import hms.patient.slippdf.ExamSlippdfRegenerate;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import java.awt.Font;
import javax.swing.JCheckBox;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JLabel;

import org.apache.commons.lang3.text.StrBuilder;

import com.itextpdf.text.DocumentException;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DisplayMessage extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTable table;
	int selectedRowIndex;
	private JTextArea messageTextArea;
	private JCheckBox messageStatusCB;
	private JSpinner scrollFrequencySp;
	private JSpinner scrollAmountSP;
	String messageID, messageSTR, messageStatus;
	JButton deleteMessageBT, updaeMessageBT;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			DisplayMessage dialog = new DisplayMessage();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public DisplayMessage() {
		setBounds(100, 100, 692, 457);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 656, 232);
		contentPanel.add(scrollPane);

		table = new JTable();
		table.setModel(new DefaultTableModel(new Object[][] { { null, null,
				null }, }, new String[] { "Message ID", "Message", "Status" }));
		table.getColumnModel().getColumn(0).setMinWidth(75);
		table.getColumnModel().getColumn(1).setPreferredWidth(200);
		table.getColumnModel().getColumn(1).setMinWidth(200);
		table.setFont(new Font("Mangal", Font.BOLD, 13));
		scrollPane.setViewportView(table);
		table.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				selectedRowIndex = table.getSelectedRow();
				selectedRowIndex = table
						.convertRowIndexToModel(selectedRowIndex);

				messageID = table.getModel()
						.getValueAt(selectedRowIndex, 0).toString();

				messageSTR = table.getModel()
						.getValueAt(selectedRowIndex, 1).toString();

				messageStatus = table.getModel()
						.getValueAt(selectedRowIndex, 2).toString();

				messageTextArea.setText(messageSTR);
				if (messageStatus.toString().equals("0")) {
					messageStatusCB.setSelected(false);
				} else {
					messageStatusCB.setSelected(true);
				}

				updaeMessageBT.setEnabled(true);
				deleteMessageBT.setEnabled(true);
				  
			}
		});


		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(10, 254, 423, 77);
		contentPanel.add(scrollPane_1);

		messageTextArea = new JTextArea();
		messageTextArea.setFont(new Font("Mangal", Font.BOLD, 13));
		messageTextArea.setLineWrap(true);
		scrollPane_1.setViewportView(messageTextArea);
		messageTextArea.setRows(8);

		JButton btnAddMessage = new JButton("Add Message");
		btnAddMessage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				messageSTR = messageTextArea.getText();

				if (messageStatusCB.isSelected()) {
					messageStatus = "1";
				} else {
					messageStatus = "0";
				}
				String[] data=new String[3];
				MessageDBConnection db = new MessageDBConnection();
				
				data[0]=messageSTR;
				data[1]=messageStatus;
				try {
					db.inserData(data);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				db.closeConnection();
				JOptionPane.showMessageDialog(null, "Added Successfully",
						"Saved", JOptionPane.INFORMATION_MESSAGE);
				populateTable();

			}
		});
		btnAddMessage.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnAddMessage.setBounds(10, 377, 132, 31);
		contentPanel.add(btnAddMessage);

		updaeMessageBT = new JButton("Update Message");
		updaeMessageBT.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				messageSTR = messageTextArea.getText();

				if (messageStatusCB.isSelected()) {
					messageStatus = "1";
				} else {
					messageStatus = "0";
				}

				MessageDBConnection db = new MessageDBConnection();
				try {
					db.updateMessage(messageID, messageSTR, messageStatus);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				db.closeConnection();
				JOptionPane.showMessageDialog(null, "Updated Successfully",
						"Saved", JOptionPane.INFORMATION_MESSAGE);
				populateTable();

			}
		});
		updaeMessageBT.setFont(new Font("Tahoma", Font.BOLD, 11));
		updaeMessageBT.setBounds(152, 377, 132, 31);
		updaeMessageBT.setEnabled(false);
		contentPanel.add(updaeMessageBT);

		deleteMessageBT = new JButton("Delete Message");
		deleteMessageBT.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				int dialogButton = JOptionPane.YES_NO_OPTION;
				int dialogResult = JOptionPane.showConfirmDialog(
						DisplayMessage.this, "Are you sure to delete message",
						"Alert", dialogButton);
				if (dialogResult == 0) {

					MessageDBConnection db = new MessageDBConnection();
					try {
						db.deleteRow(messageID);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					db.closeConnection();
					populateTable();

				}
			}
		});
		deleteMessageBT.setFont(new Font("Tahoma", Font.BOLD, 11));
		deleteMessageBT.setBounds(294, 377, 132, 31);
		deleteMessageBT.setEnabled(false);
		contentPanel.add(deleteMessageBT);

		messageStatusCB = new JCheckBox("Message Status (Active /Inactive)");
		messageStatusCB.setSelected(true);
		messageStatusCB.setFont(new Font("Tahoma", Font.BOLD, 11));
		messageStatusCB.setBounds(16, 347, 274, 23);
		contentPanel.add(messageStatusCB);

		JSeparator separator = new JSeparator();
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setBounds(444, 250, 13, 158);
		contentPanel.add(separator);
		
		JPanel panel = new JPanel();
		panel.setBounds(467, 263, 199, 145);
		contentPanel.add(panel);
		panel.setLayout(null);
		panel.setVisible(false);                        //////////////// show settings

		scrollFrequencySp = new JSpinner();
		scrollFrequencySp.setBounds(137, 0, 62, 23);
		panel.add(scrollFrequencySp);
		scrollFrequencySp.setFont(new Font("Tahoma", Font.BOLD, 13));
		scrollFrequencySp.setModel(new SpinnerNumberModel(1, 1, 30, 1));

		scrollAmountSP = new JSpinner();
		scrollAmountSP.setBounds(137, 34, 62, 23);
		panel.add(scrollAmountSP);
		scrollAmountSP.setModel(new SpinnerNumberModel(1, 1, 30, 1));
		scrollAmountSP.setFont(new Font("Tahoma", Font.BOLD, 13));

		JLabel lblNewLabel = new JLabel("Scroll Frequency");
		lblNewLabel.setBounds(0, 3, 127, 18);
		panel.add(lblNewLabel);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 13));

		JLabel lblScrollAmount = new JLabel("Scroll Amount");
		lblScrollAmount.setBounds(0, 39, 127, 18);
		panel.add(lblScrollAmount);
		lblScrollAmount.setFont(new Font("Tahoma", Font.BOLD, 13));

		JButton btnNewButton_2 = new JButton("Save");
		btnNewButton_2.setBounds(0, 118, 100, 27);
		panel.add(btnNewButton_2);
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnNewButton_2.setFont(new Font("Tahoma", Font.BOLD, 13));

		JButton btnCancel = new JButton("Cancel");
		btnCancel.setBounds(110, 116, 89, 27);
		panel.add(btnCancel);
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		btnCancel.setFont(new Font("Tahoma", Font.BOLD, 13));
		populateTable();
	}

	public void populateTable() {
		try {
			MessageDBConnection db = new MessageDBConnection();
			ResultSet rs = db.retrieveAllMessages();

			// System.out.println("Table: " + rs.getMetaData().getTableName(1));
			int NumberOfColumns = 0, NumberOfRows = 0;
			NumberOfColumns = rs.getMetaData().getColumnCount();

			while (rs.next()) {
				NumberOfRows++;
			}

			rs = db.retrieveAllMessages();

			// to set rows in this array
			Object Rows_Object_Array[][];
			Rows_Object_Array = new Object[NumberOfRows][NumberOfColumns];

			int R = 0;
			while (rs.next()) {
				for (int C = 1; C <= NumberOfColumns; C++) {
					Rows_Object_Array[R][C - 1] = rs.getObject(C);
				}
				R++;
			}
			// Finally load data to the table
			DefaultTableModel model = new DefaultTableModel(Rows_Object_Array,
					new String[] { "Message ID", "Message", "Status" }) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;// This causes all cells to be not editable
				}
			};
			table.getSelectionModel().clearSelection();
			table.setModel(model);
			table.getColumnModel().getColumn(0).setMinWidth(75);
			table.getColumnModel().getColumn(1).setPreferredWidth(200);
			table.getColumnModel().getColumn(1).setMinWidth(200);
			db.closeConnection();
		} catch (SQLException ex) {
			Logger.getLogger(ExamsBrowser.class.getName()).log(Level.SEVERE,
					null, ex);
		}

		updaeMessageBT.setEnabled(false);
		deleteMessageBT.setEnabled(false);
		messageTextArea.setText("");
		messageStatusCB.setSelected(true);
	}

}
