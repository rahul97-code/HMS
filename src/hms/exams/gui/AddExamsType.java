package hms.exams.gui;

import hms.exam.database.ExamDBConnection;
import hms.exam.database.TestMasterDBConnection;
import hms.pricemaster.database.PriceMasterDBConnection;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

public class AddExamsType extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField subCatExamTB;
	private JTextField examRoomTB;
	private JTextField examLabTB;
	private JTextField examOperatorTB;
	private JTextField examCharge1TB;
	private JTextField examCharge2TB;
	private JTextField examCharge3TB;
	private JTextField examCharge4TB;
	private JComboBox examCategoryCB;
	String examCategorySTR="",subCatExamSTR,examRoomSTR,examLabSTR,examOperatorSTR,itemCharges1STR,itemCharges2STR,itemCharges3STR,itemCharges4STR;
	 Vector<String> examCatList = new Vector<String>();
	 private JButton saveBT;
	 private JCheckBox saveReferenceRangeCB;
	 private JCheckBox chckbxAddTableValues;
	 String tableName,lis_mapping;
	 private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			AddExamsType dialog = new AddExamsType("");
			dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public AddExamsType(String tableN) {
		tableName=tableN;
		setIconImage(Toolkit.getDefaultToolkit().getImage(AddExamsType.class.getResource("/icons/rotaryLogo.png")));
		setTitle("Add Exams Types");
		setBounds(100, 100, 674, 341);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		setModal(true);
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Exam Category ID", TitledBorder.CENTER, TitledBorder.TOP, null, null));
		panel_2.setBounds(0, 0, 636, 299);
		contentPanel.add(panel_2);
		panel_2.setLayout(null);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(6, 16, 318, 225);
		panel_2.add(panel_1);
		panel_1.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "General Detail", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Exam Category :");
		lblNewLabel.setBounds(7, 20, 104, 17);
		panel_1.add(lblNewLabel);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
		
		JLabel lblNewLabel_1 = new JLabel("Sub Category :");
		lblNewLabel_1.setBounds(7, 53, 106, 17);
		panel_1.add(lblNewLabel_1);
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 13));
		
		JLabel lblNewLabel_2 = new JLabel("Room No. :");
		lblNewLabel_2.setBounds(7, 89, 112, 17);
		panel_1.add(lblNewLabel_2);
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 13));
		
		subCatExamTB = new JTextField();
		subCatExamTB.setBounds(127, 50, 185, 25);
		panel_1.add(subCatExamTB);
		subCatExamTB.setFont(new Font("Tahoma", Font.PLAIN, 13));
		subCatExamTB.setColumns(10);
		
		examRoomTB = new JTextField();
		examRoomTB.setBounds(127, 85, 185, 25);
		panel_1.add(examRoomTB);
		examRoomTB.setFont(new Font("Tahoma", Font.PLAIN, 13));
		examRoomTB.setColumns(10);
		
		examLabTB = new JTextField();
		examLabTB.setBounds(127, 122, 185, 25);
		panel_1.add(examLabTB);
		examLabTB.setFont(new Font("Tahoma", Font.PLAIN, 13));
		examLabTB.setColumns(10);
		
		examOperatorTB = new JTextField();
		examOperatorTB.setBounds(127, 156, 185, 25);
		panel_1.add(examOperatorTB);
		examOperatorTB.setFont(new Font("Tahoma", Font.PLAIN, 13));
		examOperatorTB.setColumns(10);
		getAllExamList();
		examCategoryCB = new JComboBox(examCatList);
		examCategoryCB.setEditable(true);
		examCategoryCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(examCategoryCB.getSelectedIndex()>0)
				{
					examCategorySTR=(String) examCategoryCB.getSelectedItem();
					System.out.println(examCategorySTR);
				}
			}
		});
		final JTextField tfListText = (JTextField) examCategoryCB.getEditor().getEditorComponent();
		tfListText.addCaretListener(new CaretListener() {
		 
		    @Override
		    public void caretUpdate(CaretEvent e) {
		        String text = tfListText.getText();
		        if (!text.equals("")) {
		           
		            examCategorySTR= text;
		            System.out.println(examCategorySTR);
		            // HERE YOU CAN WRITE YOUR CODE
		        }
		    }
		});
		examCategoryCB.setBounds(127, 16, 185, 25);
		panel_1.add(examCategoryCB);
		examCategoryCB.setFont(new Font("Tahoma", Font.PLAIN, 13));
		
		JLabel lblNewLabel_3 = new JLabel("Lab Name :");
		lblNewLabel_3.setBounds(7, 126, 107, 17);
		panel_1.add(lblNewLabel_3);
		lblNewLabel_3.setFont(new Font("Tahoma", Font.PLAIN, 13));
		
		JLabel lblNewLabel_4 = new JLabel("Operator Name :");
		lblNewLabel_4.setBounds(7, 160, 102, 17);
		panel_1.add(lblNewLabel_4);
		lblNewLabel_4.setFont(new Font("Tahoma", Font.PLAIN, 13));
		
		textField = new JTextField();
		textField.setBounds(127, 188, 185, 25);
		panel_1.add(textField);
		textField.setFont(new Font("Dialog", Font.PLAIN, 13));
		textField.setColumns(10);
		
		JLabel lblNewLabel_4_1 = new JLabel("LIS Mapping Code  :");
		lblNewLabel_4_1.setBounds(7, 192, 126, 17);
		panel_1.add(lblNewLabel_4_1);
		lblNewLabel_4_1.setFont(new Font("Dialog", Font.PLAIN, 13));
		
		JPanel panel = new JPanel();
		panel.setBounds(337, 17, 293, 188);
		panel_2.add(panel);
		panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Charges", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setLayout(null);
		
		JLabel lblCharges = new JLabel("Charges 1 :");
		lblCharges.setBounds(6, 19, 86, 17);
		panel.add(lblCharges);
		lblCharges.setFont(new Font("Tahoma", Font.PLAIN, 13));
		
		examCharge1TB = new JTextField();
		examCharge1TB.setBounds(102, 16, 185, 25);
		panel.add(examCharge1TB);
		examCharge1TB.setFont(new Font("Tahoma", Font.PLAIN, 13));
		examCharge1TB.setColumns(10);
		
		examCharge2TB = new JTextField();
		examCharge2TB.setEditable(false);
		examCharge2TB.setBounds(102, 51, 185, 25);
		panel.add(examCharge2TB);
		examCharge2TB.setFont(new Font("Tahoma", Font.PLAIN, 13));
		examCharge2TB.setColumns(10);
		
		JLabel lblCharges_1 = new JLabel("Charges 2 :");
		lblCharges_1.setBounds(6, 55, 86, 17);
		panel.add(lblCharges_1);
		lblCharges_1.setFont(new Font("Tahoma", Font.PLAIN, 13));
		
		JLabel lblCharges_2 = new JLabel("Charges 3 :");
		lblCharges_2.setBounds(6, 92, 86, 17);
		panel.add(lblCharges_2);
		lblCharges_2.setFont(new Font("Tahoma", Font.PLAIN, 13));
		
		examCharge3TB = new JTextField();
		examCharge3TB.setEditable(false);
		examCharge3TB.setBounds(102, 88, 185, 25);
		panel.add(examCharge3TB);
		examCharge3TB.setFont(new Font("Tahoma", Font.PLAIN, 13));
		examCharge3TB.setColumns(10);
		
		examCharge4TB = new JTextField();
		examCharge4TB.setEditable(false);
		examCharge4TB.setBounds(102, 122, 185, 25);
		panel.add(examCharge4TB);
		examCharge4TB.setFont(new Font("Tahoma", Font.PLAIN, 13));
		examCharge4TB.setColumns(10);
		
		JLabel lblCharges_3 = new JLabel("Charges 4 :");
		lblCharges_3.setBounds(6, 126, 86, 17);
		panel.add(lblCharges_3);
		lblCharges_3.setFont(new Font("Tahoma", Font.PLAIN, 13));
		
		saveReferenceRangeCB = new JCheckBox("Reference Ranges");
		saveReferenceRangeCB.setBounds(6, 154, 151, 23);
		panel.add(saveReferenceRangeCB);
		
		chckbxAddTableValues = new JCheckBox("Add Table Values");
		chckbxAddTableValues.setBounds(159, 154, 128, 23);
		panel.add(chckbxAddTableValues);
		
//		ButtonGroup reference = new ButtonGroup();
//		
//		reference.add(chckbxAddTableValues);
//		reference.add(saveReferenceRangeCB);
		
		saveBT = new JButton("Save");
		saveBT.setIcon(new ImageIcon(AddExamsType.class.getResource("/icons/SAVE.PNG")));
		saveBT.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				lis_mapping="";
				 lis_mapping=textField.getText().toString();
				if (examCategorySTR.equals("")||examCategorySTR.equals("Select Exam")) {
					JOptionPane.showMessageDialog(null,
							"Please select or enter exam category", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					examCategoryCB.requestFocus();
				} else if (subCatExamTB.getText().equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please enter sub category ", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					subCatExamTB.requestFocus();
				} else if (examRoomTB.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "Please enter exam room no",
							"Input Error", JOptionPane.ERROR_MESSAGE);
					examRoomTB.requestFocus();
				}
				else if (examLabTB.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "Please enter exam lab name",
							"Input Error", JOptionPane.ERROR_MESSAGE);
					examLabTB.requestFocus();
				}
				else if (examOperatorTB.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "Please enter exam operator",
							"Input Error", JOptionPane.ERROR_MESSAGE);
					examOperatorTB.requestFocus();
				}
				else if (examCharge1TB.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "Please enter exam cahrges 1",
							"Input Error", JOptionPane.ERROR_MESSAGE);
					examCharge1TB.requestFocus();
				}
				
				else {
					int a=JOptionPane.showConfirmDialog(contentPanel,"Are you sure?");  
					if(a!=JOptionPane.YES_OPTION){  
					   return;
					}  
					subCatExamSTR=subCatExamTB.getText();
					examRoomSTR=examRoomTB.getText();
					examLabSTR=examLabTB.getText();
					examOperatorSTR=examOperatorTB.getText();
					itemCharges1STR=examCharge1TB.getText();
					itemCharges2STR=examCharge2TB.getText();
					itemCharges3STR=examCharge3TB.getText();
					itemCharges4STR=examCharge4TB.getText();
					
					String[] data = new String[20];
					
					
					TestMasterDBConnection testMasterDBConnection=new TestMasterDBConnection();
					int itemCode=0;
					data[0]=""+getExamCode();
					data[1]=examCategorySTR;
					data[2]=subCatExamSTR;
					data[3]=examLabSTR;
					data[4]=examRoomSTR;
					data[5]=examOperatorSTR;
					data[6]=""+itemCharges1STR;
					data[7]="Yes";  
					data[8]=lis_mapping;  
					try {
						
						itemCode=testMasterDBConnection.inserData(data,tableName);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					testMasterDBConnection.closeConnection();
					
					JOptionPane.showMessageDialog(null, "Exam Detail added successfully ",
							"Save", JOptionPane.INFORMATION_MESSAGE);
					if(saveReferenceRangeCB.isSelected())
					{
						new AddTestReferences(examCategorySTR+" "+subCatExamSTR,""+itemCode).setVisible(true);
						dispose();
					}else if(chckbxAddTableValues.isSelected()) {
						new AddTestReferencesOutputs(examCategorySTR+" "+subCatExamSTR,""+itemCode).setVisible(true);
						dispose();
					}
					else {
						dispose();
					}
				}
			}
		});
		saveBT.setFont(new Font("Tahoma", Font.PLAIN, 14));
		saveBT.setBounds(208, 253, 116, 34);
		panel_2.add(saveBT);
		
		JButton cancelBT = new JButton("Cancel");
		cancelBT.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		cancelBT.setIcon(new ImageIcon(AddExamsType.class.getResource("/icons/close_button.png")));
		cancelBT.setFont(new Font("Tahoma", Font.PLAIN, 14));
		cancelBT.setBounds(337, 250, 116, 36);
		panel_2.add(cancelBT);
	}
	public int getExamCode() {
		ExamDBConnection dbConnection = new ExamDBConnection();
		ResultSet resultSet = dbConnection.retrieveExamCode(tableName, examCategoryCB.getSelectedItem().equals("OT CHARGES"));
		try {
			while (resultSet.next()) {
				return resultSet.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
		return 0;
	}
	public void getAllExamList() {
		examCatList.addElement("Select Exam");
		ExamDBConnection dbConnection = new ExamDBConnection();
		ResultSet resultSet = dbConnection.retrieveAllExams(tableName);
		try {
			while (resultSet.next()) {
				
				examCatList.addElement(resultSet.getObject(1).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
	}
	public JCheckBox getSaveReferenceRangeCB() {
		return saveReferenceRangeCB;
	}
	public JCheckBox getChckbxAddTableValues() {
		return chckbxAddTableValues;
	}
}
