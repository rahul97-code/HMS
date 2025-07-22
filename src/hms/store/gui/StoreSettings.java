package hms.store.gui;

import hms.doctor.database.DoctorDBConnection;
import hms.store.database.StoreAccountDBConnection;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class StoreSettings extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JPasswordField oldPasswordTB;
	private JPasswordField newPasswordTB;
	private JPasswordField confirmPasswordTB;
	String doctorUserName,previousPass,newPass,confPass;
	boolean oldBL=false,newBL=false,congBL=false;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			StoreSettings dialog = new StoreSettings("nidhi");
			dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public StoreSettings(final String storeID) {
		setTitle("Change Password");
		setIconImage(Toolkit.getDefaultToolkit().getImage(StoreSettings.class.getResource("/icons/rotaryLogo.png")));
		setBounds(100, 100, 450, 226);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		StoreAccountDBConnection db=new StoreAccountDBConnection();
		ResultSet rs=db.retrievePassword(storeID);
		try {
			while (rs.next()) {
				previousPass=rs.getObject(1).toString();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			db.closeConnection();
		}
		db.closeConnection();
		contentPanel.setLayout(null);
		{
			JLabel lblPrviousePassword = new JLabel("Previous Password");
			lblPrviousePassword.setFont(new Font("Tahoma", Font.PLAIN, 13));
			lblPrviousePassword.setBounds(10, 42, 137, 17);
			contentPanel.add(lblPrviousePassword);
		}
		{
			JLabel lblNewPassword = new JLabel("New Password :");
			lblNewPassword.setFont(new Font("Tahoma", Font.PLAIN, 13));
			lblNewPassword.setBounds(10, 80, 123, 17);
			contentPanel.add(lblNewPassword);
		}
		{
			JLabel lblConfirmPassword = new JLabel("Confirm Password :");
			lblConfirmPassword.setFont(new Font("Tahoma", Font.PLAIN, 13));
			lblConfirmPassword.setBounds(10, 113, 123, 17);
			contentPanel.add(lblConfirmPassword);
		}
		
		oldPasswordTB = new JPasswordField();
		oldPasswordTB.setFont(new Font("Tahoma", Font.BOLD, 13));
		oldPasswordTB.setBounds(157, 39, 243, 24);
		contentPanel.add(oldPasswordTB);
		oldPasswordTB.setColumns(10);
		oldPasswordTB.getDocument().addDocumentListener(
				new DocumentListener() {
					@Override
					public void insertUpdate(DocumentEvent e) {
						String str = oldPasswordTB.getText() + "";
						if (str.equals(""+previousPass)) {
							oldBL=true;
							oldPasswordTB.setForeground(Color.green);
						} else {
							oldPasswordTB.setForeground(Color.red);
							oldBL=false;
						}
					}

					@Override
					public void removeUpdate(DocumentEvent e) {
						String str = oldPasswordTB.getText() + "";
						if (str.equals(""+previousPass)) {
							oldBL=true;
							oldPasswordTB.setForeground(Color.green);
						} else {
							oldPasswordTB.setForeground(Color.red);
							oldBL=false;
						}
					}

					@Override
					public void changedUpdate(DocumentEvent e) {
						String str = oldPasswordTB.getText() + "";
						if (str.equals(""+previousPass)) {
							oldBL=true;
							oldPasswordTB.setForeground(Color.green);
						} else {
							oldPasswordTB.setForeground(Color.red);
							oldBL=false;
						}
					}
				});
		
		newPasswordTB = new JPasswordField();
		newPasswordTB.setFont(new Font("Tahoma", Font.BOLD, 13));
		newPasswordTB.setBounds(157, 74, 243, 24);
		contentPanel.add(newPasswordTB);
		newPasswordTB.setColumns(10);
		newPasswordTB.getDocument().addDocumentListener(
				new DocumentListener() {
					@Override
					public void insertUpdate(DocumentEvent e) {
						String str = newPasswordTB.getText() + "";
						newPass=str;
						if (str.length()>4) {
							newBL=true;
							newPasswordTB.setForeground(Color.green);
						} else {
							newBL=false;
							newPasswordTB.setForeground(Color.red);
						}
					}

					@Override
					public void removeUpdate(DocumentEvent e) {
						String str = newPasswordTB.getText() + "";
						newPass=str;
						if (str.length()>4) {
							newBL=true;
							newPasswordTB.setForeground(Color.green);
						} else {
							newBL=false;
							newPasswordTB.setForeground(Color.red);
						}
					}

					@Override
					public void changedUpdate(DocumentEvent e) {
						String str = newPasswordTB.getText() + "";
						newPass=str;
						if (str.length()>4) {
							newBL=true;
							newPasswordTB.setForeground(Color.green);
						} else {
							newBL=false;
							newPasswordTB.setForeground(Color.red);
						}
					}
				});
		
		confirmPasswordTB = new JPasswordField();
		confirmPasswordTB.setFont(new Font("Tahoma", Font.BOLD, 13));
		confirmPasswordTB.setBounds(157, 110, 243, 24);
		contentPanel.add(confirmPasswordTB);
		confirmPasswordTB.setColumns(10);
		confirmPasswordTB.getDocument().addDocumentListener(
				new DocumentListener() {
					@Override
					public void insertUpdate(DocumentEvent e) {
						String str = confirmPasswordTB.getText() + "";
						confPass=str;
						if (str.equals(""+newPass)) {
							congBL=true;
							confirmPasswordTB.setForeground(Color.green);
						} else {
							congBL=false;		
							confirmPasswordTB.setForeground(Color.red);
						}
					}

					@Override
					public void removeUpdate(DocumentEvent e) {
						String str = confirmPasswordTB.getText() + "";
						confPass=str;
						if (str.equals(""+newPass)) {
							congBL=true;
							confirmPasswordTB.setForeground(Color.green);
						} else {
							congBL=false;		
							confirmPasswordTB.setForeground(Color.red);
						}
					}

					@Override
					public void changedUpdate(DocumentEvent e) {
						String str = confirmPasswordTB.getText() + "";
						confPass=str;
						if (str.equals(""+newPass)) {
							congBL=true;
							confirmPasswordTB.setForeground(Color.green);
						} else {
							congBL=false;		
							confirmPasswordTB.setForeground(Color.red);
						}
					}
				});
		JLabel lblChangePassword = new JLabel("Change Password");
		lblChangePassword.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblChangePassword.setBounds(133, 0, 129, 25);
		contentPanel.add(lblChangePassword);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(-14, 141, 448, 6);
		contentPanel.add(separator);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						
						if(!oldBL)
						{
							JOptionPane.showMessageDialog(null,"Enter correct previous password",  "input error" , JOptionPane.ERROR_MESSAGE);
						}else if(!newBL)
						{
							JOptionPane.showMessageDialog(null,"Password must be at least 5 characters",  "input error" , JOptionPane.ERROR_MESSAGE);
						}
						else if(!congBL)
						{
							JOptionPane.showMessageDialog(null,"Your password and confirmation password do not match.",  "input error" , JOptionPane.ERROR_MESSAGE);
						}else
						{
							StoreAccountDBConnection db=new StoreAccountDBConnection();
							try {
								db.updateDataPassword(storeID, newPass);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								db.closeConnection();
							}
							db.closeConnection();
							JOptionPane.showMessageDialog(null,"Your Password Updated Successfully",  "updated successfully" , JOptionPane.INFORMATION_MESSAGE);
							dispose();
						}
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
