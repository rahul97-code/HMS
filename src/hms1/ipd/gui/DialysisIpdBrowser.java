package hms1.ipd.gui;

import hms.departments.gui.IndoorProcedureEntry;
import hms.main.DateFormatChange;

import hms.reception.gui.ReceptionMain;
import hms.reporttables.PatientOnBedReport;
import hms1.ipd.database.IPDDBConnection;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import com.itextpdf.text.DocumentException;
import com.toedter.calendar.JDateChooser;

public class DialysisIpdBrowser extends JDialog {

	private JPanel contentPane;
	private JTable ipdbrowserTable;
	ButtonGroup agegroup = new ButtonGroup();
	DateFormatChange dateFormat = new DateFormatChange();
	private JDateChooser dateToDC;
	private JDateChooser dateFromDC;
	String dateFrom,dateTo,selectedIpdId="";
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					DialysisIpdBrowser frame = new DialysisIpdBrowser();
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
	public DialysisIpdBrowser() {
		setResizable(false);
		setTitle("Dialysis IPD Entry List");
		setIconImage(Toolkit.getDefaultToolkit().getImage(DialysisIpdBrowser.class.getResource("/icons/rotaryLogo.png")));
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 950, 524);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setModal(true);
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(198, 11, 726, 418);
		contentPane.add(scrollPane);
		
		ipdbrowserTable = new JTable();
		ipdbrowserTable.setFont(new Font("Tahoma", Font.PLAIN, 12));
		ipdbrowserTable.getTableHeader().setReorderingAllowed(false);
		ipdbrowserTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		ipdbrowserTable.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null, null, null},
			},
			new String[] {
				"IPD No.", "Patient ID", "Patient Name","Insurance Type", "Ward Name", "Bed No."
			}
		));
		ipdbrowserTable.getColumnModel().getColumn(0).setMinWidth(100);
		ipdbrowserTable.getColumnModel().getColumn(1).setMinWidth(100);
		ipdbrowserTable.getColumnModel().getColumn(2).setMinWidth(100);
		ipdbrowserTable.getColumnModel().getColumn(3).setMinWidth(100);
		ipdbrowserTable.getColumnModel().getColumn(4).setMinWidth(100);
		scrollPane.setViewportView(ipdbrowserTable);
		ipdbrowserTable.addMouseListener(new MouseListener() {

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
			
				if (arg0.getClickCount() == 2) {
					JTable target = (JTable) arg0.getSource();
					int row = target.getSelectedRow();
					int column = target.getSelectedColumn();
					// do some action

					selectedIpdId = ipdbrowserTable.getModel().getValueAt(row, 0)
							.toString();
					try {
						new IPDSlipPdfRegenrate(selectedIpdId,"3");
					} catch (DocumentException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
//					selectedPONum = pobrowserTable.getModel()
//							.getValueAt(row, 1).toString();
//
//					selectedVendName = pobrowserTable.getModel()
//							.getValueAt(row, 2).toString();
//					selectedStatus = pobrowserTable.getModel()
//							.getValueAt(row, 6).toString();

				}
			}
		});
		populateTable(DateFormatChange.StringToMysqlDate(new Date()),DateFormatChange.StringToMysqlDate(new Date()));
		JButton btnNewButton = new JButton("Dialysis IPD");
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				DialysisIPD ipdEntery=new DialysisIPD(DialysisIpdBrowser.this);
//		        JInternalFrame internal =
//		            new JInternalFrame("Frame", true, true, true, true);
//		        internal.setContentPane(ipdEntery.getContentPane());
//		        internal.setVisible( true );
//		        ImageIcon icon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(ipdBrowser.class.getResource("/icons/rotaryLogo.png")));
//		        internal.setFrameIcon(icon);
//		        internal.putClientProperty("JInternalFrame.frameType", "normal");
//		        internal.setBounds(100, 100, 727, 595);
//		        internalfram.desktop.add(internal );
//		        internal.toFront();
				ipdEntery.setVisible(true);
			}
		});
		btnNewButton.setIcon(new ImageIcon(DialysisIpdBrowser.class.getResource("/icons/Business.png")));
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnNewButton.setBounds(14, 440, 168, 35);
		contentPane.add(btnNewButton);
		JButton btnIpdBill = new JButton("IPD Bill");
		btnIpdBill.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				IPDBill bill=new IPDBill();
				bill.setVisible(true);
				
			}
		});
		btnIpdBill.setIcon(new ImageIcon(EmergencyIpdBrowser.class.getResource("/icons/list_button.png")));
		btnIpdBill.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnIpdBill.setBounds(208, 440, 110, 35);
		contentPane.add(btnIpdBill);
//		btnIpdBill.setVisible(false);
		JButton btnNewButton_1 = new JButton("Bed Details Report");
		btnNewButton_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				PatientOnBedReport bedReport=new PatientOnBedReport();
				bedReport.setModal(true);
				bedReport.setVisible(true);
			}
		});
		btnNewButton_1.setIcon(null);
		btnNewButton_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnNewButton_1.setBounds(785, 440, 139, 35);
		contentPane.add(btnNewButton_1);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(4, 11, 192, 418);
		contentPane.add(panel);
		panel.setLayout(null);
		JComboBox comboBox = new JComboBox();
		comboBox.setEnabled(false);
		comboBox.setBounds(6, 41, 178, 28);
		panel.add(comboBox);
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"ALL TYPE", "NOTIFIABLE DISEASES", "OTHER INFECTIOUS/COMMUNICABLE DISEASES", "MATERNAL AND PERNATAL DISEASES", "NON-COMMUNICABLE DISEASE", "ALL OTHER"}));
		comboBox.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		JLabel lblSelectDisease = new JLabel("Select ipd Type");
		lblSelectDisease.setEnabled(false);
		lblSelectDisease.setBounds(52, 16, 95, 14);
		panel.add(lblSelectDisease);
		lblSelectDisease.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		dateFromDC = new JDateChooser();
		dateFromDC.setBounds(6, 100, 178, 25);
		panel.add(dateFromDC);
		dateFromDC.getDateEditor().addPropertyChangeListener(
				new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent arg0) {
						// TODO Auto-generated method stub
						if ("date".equals(arg0.getPropertyName())) {
							dateFrom = DateFormatChange
									.StringToMysqlDate((Date) arg0
											.getNewValue());
							populateTable(dateFrom, dateTo);
						}
					}
				});
		dateFromDC.setDate(new Date());
		dateFromDC.setMaxSelectableDate(new Date());
		dateFromDC.setDateFormatString("yyyy-MM-dd");
		
		
		dateToDC = new JDateChooser();
		dateToDC.setBounds(6, 155, 178, 25);
		panel.add(dateToDC);
		dateToDC.getDateEditor().addPropertyChangeListener(
				new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent arg0) {
						// TODO Auto-generated method stub
						if ("date".equals(arg0.getPropertyName())) {
							dateTo = DateFormatChange
									.StringToMysqlDate((Date) arg0
											.getNewValue());
							populateTable(dateFrom, dateTo);
						}
					}
				});
		dateToDC.setDate(new Date());
		dateToDC.setMaxSelectableDate(new Date());
		dateToDC.setDateFormatString("yyyy-MM-dd");
		
		JLabel lblDateTo = new JLabel("DATE : TO");
		lblDateTo.setBounds(52, 136, 73, 14);
		panel.add(lblDateTo);
		lblDateTo.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		JLabel lblDateFrom = new JLabel("DATE : From");
		lblDateFrom.setBounds(52, 80, 82, 14);
		panel.add(lblDateFrom);
		lblDateFrom.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
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
		
		JButton btnNewButton_2 = new JButton("Search");
		btnNewButton_2.setBounds(35, 252, 111, 35);
		panel.add(btnNewButton_2);
		btnNewButton_2.setIcon(new ImageIcon(DialysisIpdBrowser.class.getResource("/icons/zoom_r_button.png")));
		btnNewButton_2.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setVerticalAlignment(SwingConstants.TOP);
		lblNewLabel.setIcon(new ImageIcon(DialysisIpdBrowser.class.getResource("/icons/ipdbed.gif")));
		lblNewLabel.setBounds(10, 311, 158, 96);
		panel.add(lblNewLabel);
		JButton btnReturnDiposite = new JButton("Return / Deposit");
		btnReturnDiposite.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				IPDDeposit deposit=new IPDDeposit();
				deposit.setVisible(true);
				
			}
		});
//		btnReturnDiposite.setVisible(false);
		btnReturnDiposite.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnReturnDiposite.setBounds(349, 440, 133, 35);
		contentPane.add(btnReturnDiposite);
//		if(ReceptionMain.receptionShift.equals("1")){
//			btnReturnDiposite.setEnabled(true);
//		}else{
//			btnReturnDiposite.setEnabled(false);
//		}
//		
		JButton btnChangeBed = new JButton("Change Bed");
		btnChangeBed.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				IPDChangeBed chengBed=new IPDChangeBed();
				chengBed.setVisible(true);
			}
		});
		btnChangeBed.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnChangeBed.setBounds(637, 440, 123, 35);
		contentPane.add(btnChangeBed);
		
		JButton btnAddPackage = new JButton("Change Package");
		btnAddPackage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				IPDChangePackage indoorProcedureEntry = new IPDChangePackage();
				indoorProcedureEntry.setModal(true);
				indoorProcedureEntry.setVisible(true);
			}
		});
		btnAddPackage.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnAddPackage.setBounds(504, 440, 123, 35);
		contentPane.add(btnAddPackage);
	}
	
	public void populateTable(String dateFrom,String dateTo)
	{
		try {
            IPDDBConnection db=new IPDDBConnection();
            ResultSet rs = db.retrieveAllDataDialysis(dateFrom, dateTo);
            
           // System.out.println("Table: " + rs.getMetaData().getTableName(1));
            int NumberOfColumns = 0, NumberOfRows = 0;
            NumberOfColumns = rs.getMetaData().getColumnCount();
            
            while(rs.next()){
            NumberOfRows++;
            }
          
            rs = db.retrieveAllDataDialysis(dateFrom, dateTo);
            
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
            DefaultTableModel model = new DefaultTableModel(Rows_Object_Array, new String[] {
            		"IPD No.","Type", "Patient ID", "Patient Name","Insurance Type",  "Ward Name", "Bed No.","Date"
    			}) {
    			@Override
    			public boolean isCellEditable(int row, int column) {
    				return false;// This causes all cells to be not editable
    			}
    		};
    		ipdbrowserTable.setModel(model);
    		ipdbrowserTable.getColumnModel().getColumn(0).setMinWidth(120);
    		ipdbrowserTable.getColumnModel().getColumn(1).setMinWidth(120);
    		ipdbrowserTable.getColumnModel().getColumn(2).setMinWidth(150);
    		ipdbrowserTable.getColumnModel().getColumn(3).setMinWidth(150);
    		ipdbrowserTable.getColumnModel().getColumn(4).setMinWidth(100);
    		ipdbrowserTable.getColumnModel().getColumn(5).setMinWidth(100);
    		ipdbrowserTable.getColumnModel().getColumn(6).setMinWidth(110);
   
        } catch (SQLException ex) {
            Logger.getLogger(DialysisIpdBrowser.class.getName()).log(Level.SEVERE, null, ex);
        }
	}
}
