package hms.store.gui;

import hms.main.DateFormatChange;
import hms.store.database.SuppliersDBConnection;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.Font;
import javax.swing.UIManager;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class NewSupplier extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField companyNameTF;
	private JTextField displayNameTF;
	private JTextField mobileNoTF;
	private JTextField emailTF;
	private JTextField phoneTF;
	private JTextField faxTF;
	private JTextField fiscalCodeTF;
	private JTextField regNoTF;
	private JTextField taxIDTF;
	private JTextField managerNameTF;
	private JTextField bankNameTF;
	private JTextField bankAccountTF;
	private JTextField streetTF;
	private JTextField cityTF;
	private JTextField stateTF;
	private JTextField zipTF;
	private JTextField regionTF;

	String companyNameSTR,displayNameSTR,mobileNOSTR,emailSTR,phoneSTR,faxSTR,fiscalCodeSTR,regNoSTR,taxIDSTR,managerNameSTR,
	bankNameSTR,bankAcountSTR,streetSTR,citySTR,stateSTR,zipSTR,regionSTR;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			NewSupplier dialog = new NewSupplier();
			dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public NewSupplier() {
		setResizable(false);
		setTitle("New Vendor");
		setBounds(100, 100, 739, 465);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "General Information", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_2.setBounds(10, 11, 343, 153);
		contentPanel.add(panel_2);
		panel_2.setLayout(null);
		
		JLabel lblName = new JLabel("Campany Name :");
		lblName.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblName.setBounds(22, 23, 103, 20);
		panel_2.add(lblName);
		
		companyNameTF = new JTextField();
		companyNameTF.setFont(new Font("Tahoma", Font.PLAIN, 13));
		companyNameTF.setBounds(159, 18, 174, 25);
		panel_2.add(companyNameTF);
		companyNameTF.setColumns(10);
		companyNameTF.getDocument().addDocumentListener(
				new DocumentListener() {
					@Override
					public void insertUpdate(DocumentEvent e) {
						String str = companyNameTF.getText() + "";
						displayNameTF.setText(str);
					}

					@Override
					public void removeUpdate(DocumentEvent e) {
						String str = companyNameTF.getText() + "";
						displayNameTF.setText(str);
					}

					@Override
					public void changedUpdate(DocumentEvent e) {
						String str = companyNameTF.getText() + "";
						displayNameTF.setText(str);
					}
				});
		
		JLabel lblNewLabel = new JLabel("Display :");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblNewLabel.setBounds(22, 54, 103, 20);
		panel_2.add(lblNewLabel);
		
		displayNameTF = new JTextField();
		displayNameTF.setFont(new Font("Tahoma", Font.PLAIN, 13));
		displayNameTF.setBounds(159, 52, 174, 25);
		panel_2.add(displayNameTF);
		displayNameTF.setColumns(10);
		
		JLabel lblBrandName = new JLabel("Mobile  :");
		lblBrandName.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblBrandName.setBounds(22, 85, 103, 20);
		panel_2.add(lblBrandName);
		
		mobileNoTF = new JTextField();
		mobileNoTF.setFont(new Font("Tahoma", Font.PLAIN, 13));
		mobileNoTF.setBounds(159, 83, 174, 25);
		panel_2.add(mobileNoTF);
		mobileNoTF.setColumns(10);
		
		JLabel lblSaltName = new JLabel("Email* :");
		lblSaltName.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblSaltName.setBounds(22, 116, 103, 20);
		panel_2.add(lblSaltName);
		
		emailTF = new JTextField();
		emailTF.setFont(new Font("Tahoma", Font.PLAIN, 13));
		emailTF.setBounds(159, 114, 174, 25);
		panel_2.add(emailTF);
		emailTF.setColumns(10);
		
		JPanel panel_5 = new JPanel();
		panel_5.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Business", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_5.setBounds(370, 11, 343, 248);
		contentPanel.add(panel_5);
		panel_5.setLayout(null);
		
		JLabel lblTotalStock = new JLabel("Phone* :");
		lblTotalStock.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblTotalStock.setBounds(16, 25, 115, 20);
		panel_5.add(lblTotalStock);
		
		phoneTF = new JTextField();
		phoneTF.setFont(new Font("Tahoma", Font.PLAIN, 13));
		phoneTF.setBounds(159, 23, 174, 25);
		panel_5.add(phoneTF);
		phoneTF.setColumns(10);
		
		JLabel lblSellingPrice = new JLabel("Fax* :");
		lblSellingPrice.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblSellingPrice.setBounds(16, 61, 115, 20);
		panel_5.add(lblSellingPrice);
		
		faxTF = new JTextField();
		faxTF.setFont(new Font("Tahoma", Font.PLAIN, 13));
		faxTF.setBounds(159, 59, 174, 25);
		panel_5.add(faxTF);
		faxTF.setColumns(10);
		
		fiscalCodeTF = new JTextField();
		fiscalCodeTF.setFont(new Font("Tahoma", Font.PLAIN, 13));
		fiscalCodeTF.setBounds(159, 95, 174, 25);
		panel_5.add(fiscalCodeTF);
		fiscalCodeTF.setColumns(10);
		
		JLabel lblPurchasePrice = new JLabel("Fiscal Code* :");
		lblPurchasePrice.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblPurchasePrice.setBounds(16, 97, 115, 20);
		panel_5.add(lblPurchasePrice);
		
		JLabel lblGenericName = new JLabel("Reg. No.*  :");
		lblGenericName.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblGenericName.setBounds(16, 133, 115, 20);
		panel_5.add(lblGenericName);
		
		regNoTF = new JTextField();
		regNoTF.setFont(new Font("Tahoma", Font.PLAIN, 13));
		regNoTF.setBounds(159, 131, 174, 25);
		panel_5.add(regNoTF);
		regNoTF.setColumns(10);
		
		JLabel lblMinimumUnits = new JLabel("Tax ID* :");
		lblMinimumUnits.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblMinimumUnits.setBounds(16, 169, 115, 20);
		panel_5.add(lblMinimumUnits);
		
		taxIDTF = new JTextField();
		taxIDTF.setFont(new Font("Tahoma", Font.PLAIN, 13));
		taxIDTF.setBounds(159, 167, 174, 25);
		panel_5.add(taxIDTF);
		taxIDTF.setColumns(10);
		
		JLabel lblManagerName = new JLabel("Manager Name* :");
		lblManagerName.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblManagerName.setBounds(16, 202, 115, 20);
		panel_5.add(lblManagerName);
		
		managerNameTF = new JTextField();
		managerNameTF.setFont(new Font("Tahoma", Font.PLAIN, 13));
		managerNameTF.setColumns(10);
		managerNameTF.setBounds(159, 200, 174, 25);
		panel_5.add(managerNameTF);
		
		JButton saveBT = new JButton("Save");
		saveBT.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				companyNameSTR=companyNameTF.getText().toUpperCase().toString();
				displayNameSTR=displayNameTF.getText().toUpperCase().toString();
				mobileNOSTR=mobileNoTF.getText().toString();
				emailSTR=emailTF.getText().toString();
				streetSTR=streetTF.getText().toString();
				citySTR=cityTF.getText().toString();
				stateSTR=stateTF.getText().toString();
				zipSTR=zipTF.getText().toString();
				regionSTR=regionTF.getText().toString();
				phoneSTR=phoneTF.getText().toString();
				faxSTR=faxTF.getText().toString();
				fiscalCodeSTR=fiscalCodeTF.getText().toString();
				regNoSTR=regNoTF.getText().toString();
				bankNameSTR=bankNameTF.getText().toString();
				bankAcountSTR=bankAccountTF.getText().toString();
				taxIDSTR=taxIDTF.getText().toString();
				managerNameSTR=managerNameTF.getText().toString();
				
				if(companyNameSTR.equals("")||displayNameSTR.equals("")||mobileNOSTR.equals("")||streetSTR.equals("")||citySTR.equals("")||stateSTR.equals("")||regionSTR.equals(""))
				{
					JOptionPane.showMessageDialog(null,
							"Please enter value in fields", "Input Error",
							JOptionPane.ERROR_MESSAGE);
				}
				else {
					
					long timeInMillis = System.currentTimeMillis();
					Calendar cal1 = Calendar.getInstance();
					cal1.setTimeInMillis(timeInMillis);
					SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
					String[] data=new String[30];
					
					data[0]=companyNameSTR;
					data[1]=displayNameSTR;
					data[2]=mobileNOSTR;
					data[3]=emailSTR;
					data[4]=streetSTR;
					data[5]=citySTR;
					data[6]=stateSTR;
					data[7]=zipSTR;
					data[8]=regionSTR;
					data[9]=phoneSTR;
					data[10]=faxSTR;
					data[11]=fiscalCodeSTR;
					data[12]=regNoSTR;
					data[13]=taxIDSTR;
					data[14]=managerNameSTR;
					data[15]=bankNameSTR;
					data[16]=bankAcountSTR;
					data[17]="0";
					data[18]="0";
					data[19]="0";
					data[20]="Yes";
				
					data[21]=DateFormatChange.StringToMysqlDate(new Date());
					data[22]=""+timeFormat.format(cal1.getTime());
					data[23]=""+StoreMain.userName;
				
					SuppliersDBConnection itemsDBConnection=new SuppliersDBConnection();
					
					try {
						itemsDBConnection.inserData(data);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					itemsDBConnection.closeConnection();
					JOptionPane.showMessageDialog(null,
							"Supplier Saved Successfully", "Save Supplier",
							JOptionPane.INFORMATION_MESSAGE);
					dispose();
				}
			}
		});
		saveBT.setFont(new Font("Tahoma", Font.BOLD, 13));
		saveBT.setBounds(184, 377, 168, 37);
		contentPanel.add(saveBT);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnCancel.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnCancel.setBounds(370, 377, 168, 37);
		contentPanel.add(btnCancel);
		
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Bank Account", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(370, 270, 343, 95);
		contentPanel.add(panel);
		
		JLabel lblBankName = new JLabel("Bank Name* :");
		lblBankName.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblBankName.setBounds(22, 21, 103, 20);
		panel.add(lblBankName);
		
		bankNameTF = new JTextField();
		bankNameTF.setFont(new Font("Tahoma", Font.PLAIN, 13));
		bankNameTF.setColumns(10);
		bankNameTF.setBounds(159, 16, 174, 25);
		panel.add(bankNameTF);
		
		JLabel lblBankAccount = new JLabel("Bank Account* :");
		lblBankAccount.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblBankAccount.setBounds(22, 54, 103, 20);
		panel.add(lblBankAccount);
		
		bankAccountTF = new JTextField();
		bankAccountTF.setFont(new Font("Tahoma", Font.PLAIN, 13));
		bankAccountTF.setColumns(10);
		bankAccountTF.setBounds(159, 52, 174, 25);
		panel.add(bankAccountTF);
		
		JPanel panel_1 = new JPanel();
		panel_1.setLayout(null);
		panel_1.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Address", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(10, 165, 343, 204);
		contentPanel.add(panel_1);
		
		JLabel lblStreet = new JLabel("Street :");
		lblStreet.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblStreet.setBounds(16, 25, 115, 20);
		panel_1.add(lblStreet);
		
		streetTF = new JTextField();
		streetTF.setFont(new Font("Tahoma", Font.PLAIN, 13));
		streetTF.setColumns(10);
		streetTF.setBounds(159, 23, 174, 25);
		panel_1.add(streetTF);
		
		JLabel lblCity = new JLabel("City :");
		lblCity.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblCity.setBounds(16, 61, 115, 20);
		panel_1.add(lblCity);
		
		cityTF = new JTextField();
		cityTF.setFont(new Font("Tahoma", Font.PLAIN, 13));
		cityTF.setColumns(10);
		cityTF.setBounds(159, 59, 174, 25);
		panel_1.add(cityTF);
		
		stateTF = new JTextField();
		stateTF.setFont(new Font("Tahoma", Font.PLAIN, 13));
		stateTF.setColumns(10);
		stateTF.setBounds(159, 95, 174, 25);
		panel_1.add(stateTF);
		
		JLabel lblState = new JLabel("State :");
		lblState.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblState.setBounds(16, 97, 115, 20);
		panel_1.add(lblState);
		
		JLabel lblZipCode = new JLabel("Zip Code*  :");
		lblZipCode.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblZipCode.setBounds(16, 133, 115, 20);
		panel_1.add(lblZipCode);
		
		zipTF = new JTextField();
		zipTF.setFont(new Font("Tahoma", Font.PLAIN, 13));
		zipTF.setColumns(10);
		zipTF.setBounds(159, 131, 174, 25);
		panel_1.add(zipTF);
		
		JLabel lblRegion = new JLabel("Region :");
		lblRegion.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblRegion.setBounds(16, 169, 115, 20);
		panel_1.add(lblRegion);
		
		regionTF = new JTextField();
		regionTF.setFont(new Font("Tahoma", Font.PLAIN, 13));
		regionTF.setColumns(10);
		regionTF.setBounds(159, 167, 174, 25);
		panel_1.add(regionTF);
		
		JLabel lblIndicatesOptional = new JLabel("* indicates optional fields");
		lblIndicatesOptional.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblIndicatesOptional.setBounds(10, 389, 164, 22);
		contentPanel.add(lblIndicatesOptional);
	}
}
