package hms.patient.gui;

import hms.amountreceipt.database.AmountReceiptDBConnection;
import hms.main.DateFormatChange;
import hms.patient.database.PatientDBConnection;
import hms.patient.slippdf.IDCardSlip;
import hms.reception.gui.ReceptionMain;

import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import jcifs.smb.SmbException;

import com.itextpdf.text.DocumentException;
import com.toedter.calendar.JDateChooser;

public class PatientBrowser extends JDialog {

	private JPanel contentPane;
	private JTable patientbrowserTable;
	ButtonGroup agegroup = new ButtonGroup();
	DateFormatChange dateFormat = new DateFormatChange();
	String dateFrom,dateTo;
	private TableRowSorter<TableModel> sorter;
	public String search="";
	int selectedRowIndex ;
	public JTextField searchTB;
	private JButton btnShowIdCard;
	static String OS;
	private JButton btnEditPatient;
	String[] open=new String[4];
	private JDateChooser dateFromDC;
	private JDateChooser dateToDC;
	private JButton btnNewButton_2;
	private JPanel panel;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					PatientBrowser frame = new PatientBrowser();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public PatientBrowser() {
		setTitle("Patient Entry List");
		setIconImage(Toolkit.getDefaultToolkit().getImage(PatientBrowser.class.getResource("/icons/rotaryLogo.png")));
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setBounds(150, 150, 950, 524);
		contentPane = new JPanel();
		readFile();
		setModal(true);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		OS = System.getProperty("os.name").toLowerCase();
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(198, 11, 726, 418);
		contentPane.add(scrollPane);
		
		patientbrowserTable = new JTable();
		patientbrowserTable.setFont(new Font("Tahoma", Font.PLAIN, 12));
		patientbrowserTable.getTableHeader().setReorderingAllowed(false);
		patientbrowserTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		patientbrowserTable.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Patient No.", "Patient ID", "Patient Name", "Guardian Name", "Insurance", "Age", "Sex"
			}
		));
		patientbrowserTable .getSelectionModel().addListSelectionListener(new ListSelectionListener() {
				@Override
				public void valueChanged(ListSelectionEvent e) {
					// TODO Auto-generated method stub
					selectedRowIndex = patientbrowserTable.getSelectedRow();
					selectedRowIndex=patientbrowserTable.convertRowIndexToModel(selectedRowIndex);
					int selectedColumnIndex = patientbrowserTable.getSelectedColumn();
					btnShowIdCard.setEnabled(true);
					btnEditPatient.setEnabled(true);
					System.out.print(""+selectedRowIndex);
				}
			});
		scrollPane.setViewportView(patientbrowserTable);
		
		JButton btnNewButton = new JButton("New Patient");
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				NewPatient newPatient=new NewPatient();
				newPatient.PatientBrowserObject(PatientBrowser.this);
				newPatient.setVisible(true);
			}
		});
		btnNewButton.setIcon(new ImageIcon(PatientBrowser.class.getResource("/icons/Business.png")));
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnNewButton.setBounds(229, 440, 160, 35);
		contentPane.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Cancel");
		btnNewButton_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnNewButton_1.setIcon(new ImageIcon(PatientBrowser.class.getResource("/icons/CANCEL.PNG")));
		btnNewButton_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnNewButton_1.setBounds(739, 440, 160, 35);
		contentPane.add(btnNewButton_1);
		
	    panel = new JPanel();
		panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(4, 11, 192, 418);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JRadioButton rdbtnAll = new JRadioButton("All");
		rdbtnAll.setBounds(6, 222, 49, 23);
		panel.add(rdbtnAll);
		rdbtnAll.setSelected(true);
		rdbtnAll.setFont(new Font("Tahoma", Font.PLAIN, 12));
		agegroup.add(rdbtnAll);
		
		JRadioButton rdbtnMale = new JRadioButton("Male");
		rdbtnMale.setBounds(52, 222, 59, 23);
		panel.add(rdbtnMale);
		rdbtnMale.setFont(new Font("Tahoma", Font.PLAIN, 12));
		agegroup.add(rdbtnMale);
		
		JRadioButton rdbtnFemale = new JRadioButton("Female");
		rdbtnFemale.setBounds(113, 222, 73, 23);
		panel.add(rdbtnFemale);
		rdbtnFemale.setFont(new Font("Tahoma", Font.PLAIN, 12));
		agegroup.add(rdbtnFemale);
		
		JLabel lblSelectSex = new JLabel("Select Sex");
		lblSelectSex.setBounds(52, 201, 73, 14);
		panel.add(lblSelectSex);
		lblSelectSex.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		btnNewButton_2 = new JButton("Search");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				populateTable1(dateFrom,dateTo);
			}
		});
		btnNewButton_2.setBounds(36, 278, 111, 35);
		panel.add(btnNewButton_2);
		btnNewButton_2.setIcon(new ImageIcon(PatientBrowser.class.getResource("/icons/zoom_r_button.png")));
		btnNewButton_2.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setVerticalAlignment(SwingConstants.TOP);
		lblNewLabel.setIcon(new ImageIcon(PatientBrowser.class.getResource("/icons/opd.gif")));
		lblNewLabel.setBounds(35, 324, 111, 83);
		panel.add(lblNewLabel);
		
		searchTB = new JTextField();
		searchTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		searchTB.setBounds(6, 160, 172, 29);
		panel.add(searchTB);
		searchTB.setColumns(10);
		searchTB.getDocument().addDocumentListener(
				new DocumentListener() {
					@Override
					public void insertUpdate(DocumentEvent e) {
						String str = searchTB.getText() + "";
						if (!str.equals("")) {
							search=str;
							populateTable();
						} else {
							populateTable1(dateFrom,dateTo);
						}
					}

					@Override
					public void removeUpdate(DocumentEvent e) {
						String str = searchTB.getText() + "";
						if (!str.equals("")) {
							search=str;
							populateTable();
						} else {
							populateTable1(dateFrom,dateTo);
						}
					}

					@Override
					public void changedUpdate(DocumentEvent e) {
						String str = searchTB.getText() + "";
						if (!str.equals("")) {
							search=str;
							populateTable();
						} else {
							populateTable1(dateFrom,dateTo);
						}
					}
				});

		
		JLabel lblSearchNameOr = new JLabel("Search Name or ID");
		lblSearchNameOr.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblSearchNameOr.setBounds(36, 134, 111, 14);
		panel.add(lblSearchNameOr);
		
		JLabel lblDateFrom = new JLabel("DATE : From");
		lblDateFrom.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblDateFrom.setBounds(52, 22, 82, 14);
		panel.add(lblDateFrom);
		
		dateFromDC = new JDateChooser();
		dateFromDC.setDateFormatString("yyyy-MM-dd");
		dateFromDC.setBounds(6, 42, 178, 25);
		panel.add(dateFromDC);
		dateFromDC.setDate(new Date());
		dateFromDC.getDateEditor().addPropertyChangeListener(
				new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent arg0) {
						// TODO Auto-generated method stub
						if ("date".equals(arg0.getPropertyName())) {
							dateFrom = DateFormatChange
									.StringToMysqlDate((Date) arg0
											.getNewValue());
						
						}
					}
				});
		
		JLabel lblDateTo = new JLabel("DATE : TO");
		lblDateTo.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblDateTo.setBounds(52, 78, 73, 14);
		panel.add(lblDateTo);
		
		dateToDC = new JDateChooser();
		dateToDC.setDateFormatString("yyyy-MM-dd");
		dateToDC.setBounds(6, 97, 178, 25);
		panel.add(dateToDC);
		dateToDC.setDate(new Date());
		dateToDC.getDateEditor().addPropertyChangeListener(
				new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent arg0) {
						// TODO Auto-generated method stub
						if ("date".equals(arg0.getPropertyName())) {
							dateTo = DateFormatChange
									.StringToMysqlDate((Date) arg0
											.getNewValue());
							
						}
					}
				});
		dateToDC.setMaxSelectableDate(new Date());
		
		btnShowIdCard = new JButton("Show ID Card");
		btnShowIdCard.setEnabled(false);
		btnShowIdCard.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
					Object selectedObject1 = patientbrowserTable.getModel().getValueAt(selectedRowIndex, 1);
					System.out.print(""+selectedObject1);
					long timeInMillis = System.currentTimeMillis();
					Calendar cal1 = Calendar.getInstance();
					cal1.setTimeInMillis(timeInMillis);
					SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
					String[] data1=new String[10];
					
					data1[0]="CARD";
					data1[1]="10";
					data1[2]=""+ReceptionMain.receptionNameSTR;
					data1[3]=""+selectedObject1.toString();
					data1[4]="N/A";
					data1[5]=""+ DateFormatChange.StringToMysqlDate(new Date());
					data1[6]=""+timeFormat.format(cal1.getTime());
					data1[7]="Unknown";
					data1[8]= 0+"";
					
					AmountReceiptDBConnection amountReceiptDBConnection=new AmountReceiptDBConnection();
					
					try {
						amountReceiptDBConnection.inserData(data1);
					} catch (Exception e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
					amountReceiptDBConnection.closeConnection();
					String filePath=selectedObject1.toString();
					try {
						new IDCardSlip(selectedObject1.toString());
					} catch (FileNotFoundException | SmbException
							| MalformedURLException | UnknownHostException
							| DocumentException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
//					if (isWindows()) {
//						OPenFileWindows("PatientSlip/" + filePath + ".pdf");
//						System.out.println("This is Windows");
//					} else if (isMac()) {
//						System.out.println("This is Mac");
//					} else if (isUnix()) {
//						if (System.getProperty("os.version").equals("3.11.0-12-generic")) {
//							Run(new String[] { "/bin/bash", "-c",
//									open[0]+" PatientSlip/" + filePath + ".pdf" });
//						} else {
//							Run(new String[] { "/bin/bash", "-c",
//									open[1]+" PatientSlip/" + filePath + ".pdf" });
//						}
//						System.out.println("This is Unix or Linux");
//					} else if (isSolaris()) {
//						System.out.println("This is Solaris");
//					} else {
//						System.out.println("Your OS is not support!!");
//					}
			}
		});
		btnShowIdCard.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnShowIdCard.setBounds(399, 440, 160, 35);
		contentPane.add(btnShowIdCard);
		
		btnEditPatient = new JButton("Edit Patient");
		btnEditPatient.setEnabled(false);
		btnEditPatient.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				Object selectedObject1 = patientbrowserTable.getModel().getValueAt(selectedRowIndex, 1);
				System.out.print(""+selectedObject1);
				String patient_id=selectedObject1.toString();
				 EditPatient editPatient=new EditPatient(patient_id);
				 editPatient.setVisible(true);
				 editPatient.PatientBrowserObject(PatientBrowser.this);
				 btnNewButton_2.doClick();
			}
		});
		btnEditPatient.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnEditPatient.setBounds(569, 440, 160, 35);
		contentPane.add(btnEditPatient);
		populateTable1(DateFormatChange.StringToMysqlDate(new Date()),
				DateFormatChange.StringToMysqlDate(new Date()));
	}
	
	public void populateTable1(String d1,String d2)
	{
		try {
            PatientDBConnection db=new PatientDBConnection();
            ResultSet rs = db.retrieveAllData(d1,d2);
            
           // System.out.println("Table: " + rs.getMetaData().getTableName(1));
            int NumberOfColumns = 0, NumberOfRows = 0;
            NumberOfColumns = rs.getMetaData().getColumnCount();
            
            while(rs.next()){
            NumberOfRows++;
            }
        	rs.beforeFirst();
            
            //to set rows in this array
            Object Rows_Object_Array[][];
            Rows_Object_Array = new Object[NumberOfRows][NumberOfColumns];
            
            int R = 0;
            while(rs.next()) {
                for(int C=1; C<=NumberOfColumns;C++) {
                    Rows_Object_Array[R][C-1] = rs.getObject(C);
                }
                R++;
            }
            //Finally load data to the table
            DefaultTableModel model = new DefaultTableModel(Rows_Object_Array,new String[] {
    				"Patient No.", "Patient ID", "Patient Name", "Guardian Name", "Insurance", "Age", "Sex"
    			}) {
    			@Override
    			public boolean isCellEditable(int row, int column) {
    				return false;// This causes all cells to be not editable
    			}
    		};
    		patientbrowserTable.setModel(model);
    		TableColumn columnsize = null;
    		for (int i = 0; i < 7; i++) {
    			columnsize = patientbrowserTable.getColumnModel().getColumn(i);
    			columnsize.setPreferredWidth(110); 
    			if(i==1||i==2||i==3)
    			{
    				columnsize.setPreferredWidth(150); 
    			}
    			if(i==5)
    				columnsize.setPreferredWidth(60); 
    		}   
        } catch (SQLException ex) {
            Logger.getLogger(PatientBrowser.class.getName()).log(Level.SEVERE, null, ex);
        }
	}
	public void populateTable()
	{
		try {
            PatientDBConnection db=new PatientDBConnection();
            ResultSet rs = db.retrieveAllData(search);
            
           // System.out.println("Table: " + rs.getMetaData().getTableName(1));
            int NumberOfColumns = 0, NumberOfRows = 0;
            NumberOfColumns = rs.getMetaData().getColumnCount();
            
            while(rs.next()){
            NumberOfRows++;
            }
        	rs.beforeFirst();
            
            //to set rows in this array
            Object Rows_Object_Array[][];
            Rows_Object_Array = new Object[NumberOfRows][NumberOfColumns];
            
            int R = 0;
            while(rs.next()) {
                for(int C=1; C<=NumberOfColumns;C++) {
                    Rows_Object_Array[R][C-1] = rs.getObject(C);
                }
                R++;
            }
            //Finally load data to the table
            DefaultTableModel model = new DefaultTableModel(Rows_Object_Array,new String[] {
    				"Patient No.", "Patient ID", "Patient Name", "Guardian Name", "Insurance", "Age", "Sex"
    			}) {
    			@Override
    			public boolean isCellEditable(int row, int column) {
    				return false;// This causes all cells to be not editable
    			}
    		};
    		patientbrowserTable.setModel(model);
    		btnShowIdCard.setEnabled(false);
    		btnEditPatient.setEnabled(false);
    		TableColumn columnsize = null;
    		for (int i = 0; i < 7; i++) {
    			columnsize = patientbrowserTable.getColumnModel().getColumn(i);
    			columnsize.setPreferredWidth(110); 
    			if(i==1||i==2||i==3)
    			{
    				columnsize.setPreferredWidth(150); 
    			}
    			if(i==5)
    				columnsize.setPreferredWidth(60); 
    		}   
        } catch (SQLException ex) {
            Logger.getLogger(PatientBrowser.class.getName()).log(Level.SEVERE, null, ex);
        }
	}
	public void OPenFileWindows(String path) {
		try {

			File f = new File(path);
			if (f.exists()) {
				if (Desktop.isDesktopSupported()) {
					Desktop.getDesktop().open(f);
				} else {
					System.out.println("File does not exists!");
				}
			}
		} catch (Exception ert) {
		}
	}
	public void Run(String[] cmd) {
		try {
			Process process = Runtime.getRuntime().exec(cmd);
			int processComplete = process.waitFor();
			if (processComplete == 0) {
				System.out.println("successfully");
			} else {
				System.out.println("Failed");
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static boolean isWindows() {

		return (OS.indexOf("win") >= 0);

	}

	public static boolean isMac() {

		return (OS.indexOf("mac") >= 0);

	}

	public static boolean isUnix() {

		return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS
				.indexOf("aix") > 0);

	}

	public static boolean isSolaris() {

		return (OS.indexOf("sunos") >= 0);

	}
	public JButton getBtnEditPatient() {
		return btnEditPatient;
	}
	
	public void readFile() {
		// The name of the file to open.
		String fileName = "data.mdi";

		// This will reference one line at a time
		String line = null;

		try {
			// FileReader reads text files in the default encoding.
			FileReader fileReader = new FileReader(fileName);

			// Always wrap FileReader in BufferedReader.
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String str = null;
			boolean fetch=true;
			while ((line = bufferedReader.readLine()) != null&&fetch) {
				// System.out.println(line);
				str = line;
				fetch=false;
			}
			String data[] = new String[22];
			int i = 0;
			for (String retval : str.split("@")) {
				data[i] = retval;
				i++;
			}
			open[0]=data[2];
			open[1]=data[3];
			open[2]=data[4];
			// Always close files.
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileName + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + fileName + "'");
		}
	}

}
