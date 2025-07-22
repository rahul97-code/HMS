package hms.main;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

public class ChangeMessage extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField messageTB;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			ChangeMessage dialog = new ChangeMessage();
			dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public ChangeMessage() {
		setBounds(100, 100, 450, 161);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		setModal(true);
		{
			messageTB = new JTextField();
			messageTB.setFont(new Font("Tahoma", Font.PLAIN, 14));
			messageTB.setBounds(0, 37, 434, 42);
			contentPanel.add(messageTB);
			messageTB.setColumns(10);
		}
		{
			JLabel lblEnterNewMessage = new JLabel("Enter New Message Here");
			lblEnterNewMessage.setFont(new Font("Tahoma", Font.PLAIN, 14));
			lblEnterNewMessage.setBounds(133, 11, 198, 25);
			contentPanel.add(lblEnterNewMessage);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						
						 String[] data=new String[2];
						 
						 data[0]=messageTB.getText().toString();
						NewsDBConnection dbConnection=new NewsDBConnection();
						try {
							dbConnection.updateData(data);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						dbConnection.closeConnection();
						JOptionPane.showMessageDialog(null,
								"Message Saved Successfully ", "Data Save",
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
