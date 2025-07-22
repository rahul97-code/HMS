package hms.reception.gui;

import hms.reception.database.TokenDBConnection;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class CounterUpdateAlert extends JDialog {

	private final JPanel contentPanel = new JPanel();
	String counter="0";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			CounterUpdateAlert dialog = new CounterUpdateAlert();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public CounterUpdateAlert() {
		setResizable(false);
		setBounds(250, 200, 444, 171);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblDoYouWant = new JLabel("Do you want to Update Next Token");
		lblDoYouWant.setHorizontalAlignment(SwingConstants.CENTER);
		lblDoYouWant.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblDoYouWant.setBounds(10, 11, 419, 47);
		contentPanel.add(lblDoYouWant);
		
		JButton btnYes = new JButton("Yes");
		btnYes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if(!counter.equals("0"))
				{
					TokenDBConnection connection = new TokenDBConnection();

					try {
						connection.updateDataScreenDataIncrese(ReceptionMain._id,counter);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} finally {
						connection.closeConnection();
						
					}
				}
				dispose();
				
			}
		});
		btnYes.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnYes.setBounds(20, 57, 177, 62);
		contentPanel.add(btnYes);
		
		JButton btnNo = new JButton("No");
		btnNo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				dispose();
			}
		});
		btnNo.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnNo.setBounds(240, 57, 177, 62);
		contentPanel.add(btnNo);
	}
	
	public void updateCounter(String counter){
		this.counter=counter;
	}
}
