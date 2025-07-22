package hms.admin.gui;

import hms.departments.database.DepartmentDBConnection;
import hms.reception.database.ReceptionistDBConnection;
import hms.reception.gui.ReceptionistBrowser;
import hms1.wards.database.WardsManagementDBConnection;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.SwingConstants;
import javax.swing.JComboBox;

public class ReceptionCounters extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JComboBox deptNameCB;
	final DefaultComboBoxModel doctors = new DefaultComboBoxModel();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			ReceptionCounters dialog = new ReceptionCounters();
			dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public ReceptionCounters() {
		setBounds(100, 100, 452, 177);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JLabel lblHmsWards = new JLabel("Reception Counters");
		lblHmsWards.setFont(new Font("Tahoma", Font.BOLD, 25));
		lblHmsWards.setHorizontalAlignment(SwingConstants.CENTER);
		lblHmsWards.setBounds(50, 11, 299, 31);
		contentPanel.add(lblHmsWards);

		JLabel lblWardType = new JLabel("Select");
		lblWardType.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblWardType.setBounds(29, 70, 96, 14);
		contentPanel.add(lblWardType);

		deptNameCB = new JComboBox(new Object[] {});
		deptNameCB.setModel(new DefaultComboBoxModel(new String[] {"1", "2", "3"}));
		deptNameCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

			}
		});
		deptNameCB.setBounds(145, 67, 227, 24);
		contentPanel.add(deptNameCB);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {

						ReceptionistDBConnection connection=new ReceptionistDBConnection();
						try {
							connection.updateReceptionCounter(deptNameCB.getSelectedItem().toString());
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						connection.closeConnection();
						JOptionPane.showMessageDialog(null,
								"Updated Successfully ", "Data Update",
								JOptionPane.INFORMATION_MESSAGE);
						dispose();

					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}

	}

}
