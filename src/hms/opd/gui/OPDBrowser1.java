package hms.opd.gui;

import hms.main.DateFormatChange;
import hms.opd.database.OPDDBConnection;
import hms.patient.slippdf.OPDSlippdf2;

import java.awt.Font;
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
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import com.itextpdf.text.DocumentException;
import com.toedter.calendar.JDateChooser;

public class OPDBrowser1 extends JPanel {


	private JTable opdbrowserTable;
	ButtonGroup agegroup = new ButtonGroup();
	DateFormatChange dateFormat = new DateFormatChange();
	private JDateChooser dateToDC;
	private JDateChooser dateFromDC;
	String dateFrom,dateTo;
	private JTextField searchPatientNameTB;
	/**
	 * Create the panel.
	 */
	public OPDBrowser1() {
		setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(10, 11, 622, 458);
		add(panel);
		panel.setLayout(null);
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(198, 11, 726, 418);
		panel.add(scrollPane);
		
		opdbrowserTable = new JTable();
		opdbrowserTable.setToolTipText("Double Click to reprint OPD Slip");
		opdbrowserTable.setFont(new Font("Tahoma", Font.PLAIN, 12));
		opdbrowserTable.getTableHeader().setReorderingAllowed(false);
		opdbrowserTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		opdbrowserTable.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"OPD No.", "Patient ID", "Patient Name", "Doctor Name", "OPD Date", "Token No.", "OPD Type"
			}
		));
		scrollPane.setViewportView(opdbrowserTable);
		opdbrowserTable.addMouseListener(new MouseListener() {
			
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
				         JTable target = (JTable)arg0.getSource();
				         int row = target.getSelectedRow();
				         int column = target.getSelectedColumn();
				         // do some action
				         
				         Object selectedObject = opdbrowserTable.getModel().getValueAt(row, 0);
				         
				     	try {
							new OPDSlippdf2(selectedObject+"");
						} catch (DocumentException | IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				  }
			}
		});
		JButton btnNewButton = new JButton("New OPD");
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
//				OPDEntery opdEntery=new OPDEntery(OPDBrowser.this);
//		        JInternalFrame internal =
//		            new JInternalFrame("Frame", true, true, true, true);
//		        internal.setContentPane(opdEntery.getContentPane());
//		        internal.setVisible( true );
//		        ImageIcon icon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(OPDBrowser.class.getResource("/icons/rotaryLogo.png")));
//		        internal.setFrameIcon(icon);
//		        internal.putClientProperty("JInternalFrame.frameType", "normal");
//		        internal.setBounds(100, 100, 727, 595);
//		        internalfram.desktop.add(internal );
//		        internal.toFront();
			//	opdEntery.setVisible(true);
			}
		});
		btnNewButton.setIcon(new ImageIcon(OPDBrowser.class.getResource("/icons/Business.png")));
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnNewButton.setBounds(294, 440, 160, 35);
		panel.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Close");
		btnNewButton_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			//	dispose();
			}
		});
		btnNewButton_1.setIcon(new ImageIcon(OPDBrowser.class.getResource("/icons/CANCEL.PNG")));
		btnNewButton_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnNewButton_1.setBounds(473, 440, 160, 35);
		panel.add(btnNewButton_1);
		
		JPanel panel1 = new JPanel();
		panel1.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel1.setBounds(4, 11, 192, 418);
		panel.add(panel1);
		panel1.setLayout(null);
		searchPatientNameTB = new JTextField();
		searchPatientNameTB.setBounds(6, 41, 178, 28);
		panel1.add(searchPatientNameTB);
		searchPatientNameTB.getDocument().addDocumentListener(
				new DocumentListener() {
					@Override
					public void insertUpdate(DocumentEvent e) {
						String str = searchPatientNameTB.getText() + "";
						if (!str.equals("")) {
							populateTable(dateFrom, dateTo);
						} else {
							populateTable1(dateFrom, dateTo);
						}
					}

					@Override
					public void removeUpdate(DocumentEvent e) {
						String str = searchPatientNameTB.getText() + "";
						if (!str.equals("")) {
							
							populateTable(dateFrom, dateTo);
						} else {
							populateTable1(dateFrom, dateTo);
						}
					}

					@Override
					public void changedUpdate(DocumentEvent e) {
						String str = searchPatientNameTB.getText() + "";
						if (!str.equals("")) {
							populateTable(dateFrom, dateTo);
						} else {
							populateTable1(dateFrom, dateTo);
						}
					}
				});
		searchPatientNameTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		JLabel lblSelectDisease = new JLabel("Search Patient");
		lblSelectDisease.setBounds(52, 16, 95, 14);
		panel1.add(lblSelectDisease);
		lblSelectDisease.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		dateFromDC = new JDateChooser();
		dateFromDC.setBounds(4, 122, 178, 25);
		panel1.add(dateFromDC);
		dateFromDC.getDateEditor().addPropertyChangeListener(
				new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent arg0) {
						// TODO Auto-generated method stub
						if ("date".equals(arg0.getPropertyName())) {
							dateFrom = DateFormatChange
									.StringToMysqlDate((Date) arg0
											.getNewValue());
							populateTable1(dateFrom, dateTo);
						}
					}
				});
		dateFromDC.setDate(new Date());
		dateFromDC.setMaxSelectableDate(new Date());
		dateFromDC.setDateFormatString("yyyy-MM-dd");
		
		
		dateToDC = new JDateChooser();
		dateToDC.setBounds(4, 177, 178, 25);
		panel1.add(dateToDC);
		dateToDC.getDateEditor().addPropertyChangeListener(
				new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent arg0) {
						// TODO Auto-generated method stub
						if ("date".equals(arg0.getPropertyName())) {
							dateTo = DateFormatChange
									.StringToMysqlDate((Date) arg0
											.getNewValue());
							populateTable1(dateFrom, dateTo);
						}
					}
				});
		dateToDC.setDate(new Date());
		dateToDC.setMaxSelectableDate(new Date());
		dateToDC.setDateFormatString("yyyy-MM-dd");
		
		JLabel lblDateTo = new JLabel("DATE : TO");
		lblDateTo.setBounds(50, 158, 73, 14);
		panel1.add(lblDateTo);
		lblDateTo.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		JLabel lblDateFrom = new JLabel("DATE : From");
		lblDateFrom.setBounds(50, 102, 82, 14);
		panel1.add(lblDateFrom);
		lblDateFrom.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		JRadioButton rdbtnAll = new JRadioButton("All");
		rdbtnAll.setBounds(4, 244, 49, 23);
		panel1.add(rdbtnAll);
		rdbtnAll.setSelected(true);
		rdbtnAll.setFont(new Font("Tahoma", Font.PLAIN, 12));
		agegroup.add(rdbtnAll);
		
		JRadioButton rdbtnMale = new JRadioButton("Male");
		rdbtnMale.setBounds(50, 244, 59, 23);
		panel1.add(rdbtnMale);
		rdbtnMale.setFont(new Font("Tahoma", Font.PLAIN, 12));
		agegroup.add(rdbtnMale);
		
		JRadioButton rdbtnFemale = new JRadioButton("Female");
		rdbtnFemale.setBounds(111, 244, 73, 23);
		panel1.add(rdbtnFemale);
		rdbtnFemale.setFont(new Font("Tahoma", Font.PLAIN, 12));
		agegroup.add(rdbtnFemale);
		
		JLabel lblSelectSex = new JLabel("Select Sex");
		lblSelectSex.setBounds(50, 223, 73, 14);
		panel1.add(lblSelectSex);
		lblSelectSex.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		JButton btnNewButton_2 = new JButton("Search");
		btnNewButton_2.setBounds(36, 278, 111, 35);
		panel1.add(btnNewButton_2);
		btnNewButton_2.setIcon(new ImageIcon(OPDBrowser.class.getResource("/icons/zoom_r_button.png")));
		btnNewButton_2.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setVerticalAlignment(SwingConstants.TOP);
		lblNewLabel.setIcon(new ImageIcon(OPDBrowser.class.getResource("/icons/opd.gif")));
		lblNewLabel.setBounds(35, 324, 111, 83);
		panel1.add(lblNewLabel);
		populateTable1(DateFormatChange.StringToMysqlDate(new Date()),DateFormatChange.StringToMysqlDate(new Date()));
	}
	
	public void populateTable(String dateFrom,String dateTo)
	{
		String index=searchPatientNameTB.getText() + "";
		try {
            OPDDBConnection db=new OPDDBConnection();
            ResultSet rs = db.retrieveAllData1(dateFrom, dateTo,index);
            
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
            DefaultTableModel model = new DefaultTableModel(Rows_Object_Array, new String[] {
    				"OPD No.", "Patient ID", "Patient Name", "Doctor Name", "OPD Date", "Token No.", "OPD Type"
    			}) {
    			@Override
    			public boolean isCellEditable(int row, int column) {
    				return false;// This causes all cells to be not editable
    			}
    		};
    		opdbrowserTable.setModel(model);
    		TableColumn columnsize = null;
    		for (int i = 0; i < 7; i++) {
    			columnsize = opdbrowserTable.getColumnModel().getColumn(i);
    			columnsize.setPreferredWidth(110); 
    			if(i==1||i==2||i==3)
    			{
    				columnsize.setPreferredWidth(150); 
    			}
    			if(i==5)
    				columnsize.setPreferredWidth(60); 
    		}   
        } catch (SQLException ex) {
            Logger.getLogger(OPDBrowser.class.getName()).log(Level.SEVERE, null, ex);
        }
	}
	public void populateTable1(String dateFrom,String dateTo)
	{
		
		searchPatientNameTB.setText("");
		try {
            OPDDBConnection db=new OPDDBConnection();
            ResultSet rs = db.retrieveAllData(dateFrom, dateTo);
            
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
            DefaultTableModel model = new DefaultTableModel(Rows_Object_Array, new String[] {
    				"OPD No.", "Patient ID", "Patient Name", "Doctor Name", "OPD Date", "Token No.", "OPD Type"
    			}) {
    			@Override
    			public boolean isCellEditable(int row, int column) {
    				return false;// This causes all cells to be not editable
    			}
    		};
    		opdbrowserTable.setModel(model);
    		TableColumn columnsize = null;
    		for (int i = 0; i < 7; i++) {
    			columnsize = opdbrowserTable.getColumnModel().getColumn(i);
    			columnsize.setPreferredWidth(110); 
    			if(i==1||i==2||i==3)
    			{
    				columnsize.setPreferredWidth(150); 
    			}
    			if(i==5)
    				columnsize.setPreferredWidth(60); 
    		}   
        } catch (SQLException ex) {
            Logger.getLogger(OPDBrowser.class.getName()).log(Level.SEVERE, null, ex);
        }
	}

}
