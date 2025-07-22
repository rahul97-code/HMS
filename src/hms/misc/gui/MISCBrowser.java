package hms.misc.gui;

import hms.main.DateFormatChange;
import hms.misc.database.MiscDBConnection;
import hms.opd.database.OPDDBConnection;
import hms.patient.slippdf.ExamSlippdfRegenerate;
import hms.patient.slippdf.OPDSlippdf2;
import hms.store.gui.POBrowser;

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
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
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
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import com.itextpdf.text.DocumentException;
import com.toedter.calendar.JDateChooser;

import UsersActivity.database.UADBConnection;

public class MISCBrowser extends JDialog {

	UADBConnection ua=new UADBConnection();
	public JPanel contentPane;
	private JTable jTable;
	ButtonGroup agegroup = new ButtonGroup();
	DateFormatChange dateFormat = new DateFormatChange();
	private JDateChooser dateToDC;
	private JDateChooser dateFromDC;
	String dateFrom, dateTo;
	private JTextField searchPatientNameTB;
	Vector originalTableModel;
	public static void main(String[] arg) {
		MISCBrowser browser = new MISCBrowser("uname");
		browser.setVisible(true);
	}

	/**
	 * Create the frame.
	 */
	public MISCBrowser(final String username) {
		setResizable(false);
		setTitle("MISC Entry List");
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				MISCBrowser.class.getResource("/icons/rotaryLogo.png")));
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setBounds(400, 200, 950, 524);
		contentPane = new JPanel();
		setModal(true);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(198, 11, 726, 418);
		contentPane.add(scrollPane);

		jTable = new JTable();
		jTable.setFont(new Font("Tahoma", Font.PLAIN, 12));
		jTable.getTableHeader().setReorderingAllowed(false);
		jTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		jTable.setModel(new DefaultTableModel(new Object[][] {},
				new String[] { "OPD No.", "Patient ID", "Patient Name",
						"Doctor Name", "OPD Date", "Token No.", "OPD Type" }));
		scrollPane.setViewportView(jTable);
		jTable.addMouseListener(new MouseListener() {

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

					Object receiptID = jTable.getModel()
							.getValueAt(row, 0);
					Object doctorName = jTable.getModel()
							.getValueAt(row, 3);
					Object charges= jTable.getModel()
							.getValueAt(row, 4);
					Object p_id= jTable.getModel()
							.getValueAt(row, 1);
					Object p_name= jTable.getModel()
							.getValueAt(row, 2);
					try {
						new MiscRegenerateSlipPdf(doctorName+"",charges+"",receiptID+"",p_id+"",p_name+"");
					} catch (DocumentException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					// try {
					// /new OPDSlippdf2(selectedObject+"");
					// } catch (DocumentException | IOException e) {
					// // TODO Auto-generated catch block
					// e.printStackTrace();
					// }
				}
			}
		});
		JButton btnNewButton = new JButton("New Entry");
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				MiscAmountEntery amountEntery = new MiscAmountEntery();
				amountEntery.setVisible(true);
                ua.check_activity(username, 127, 0);
			}
		});
		btnNewButton.setIcon(new ImageIcon(MISCBrowser.class
				.getResource("/icons/Business.png")));
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnNewButton.setBounds(294, 440, 160, 35);
		contentPane.add(btnNewButton);

		JButton btnNewButton_1 = new JButton("Close");
		btnNewButton_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnNewButton_1.setIcon(new ImageIcon(MISCBrowser.class
				.getResource("/icons/CANCEL.PNG")));
		btnNewButton_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnNewButton_1.setBounds(473, 440, 160, 35);
		contentPane.add(btnNewButton_1);

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panel.setBounds(4, 11, 192, 418);
		contentPane.add(panel);
		panel.setLayout(null);
		searchPatientNameTB = new JTextField();
		searchPatientNameTB.setBounds(6, 41, 178, 28);
		panel.add(searchPatientNameTB);
		searchPatientNameTB.getDocument().addDocumentListener(
				new DocumentListener() {
					@Override
					public void insertUpdate(DocumentEvent e) {
						String str = searchPatientNameTB.getText() + "";
						searchTableContents(str);
					}

					@Override
					public void removeUpdate(DocumentEvent e) {
						String str = searchPatientNameTB.getText() + "";
						searchTableContents(str);
					}

					@Override
					public void changedUpdate(DocumentEvent e) {
						String str = searchPatientNameTB.getText() + "";
						searchTableContents(str);
					}
				});
		searchPatientNameTB.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblSelectDisease = new JLabel("Search Patient");
		lblSelectDisease.setBounds(52, 16, 95, 14);
		panel.add(lblSelectDisease);
		lblSelectDisease.setFont(new Font("Tahoma", Font.PLAIN, 12));

		dateFromDC = new JDateChooser();
		dateFromDC.setBounds(4, 122, 178, 25);
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
		dateToDC.setBounds(4, 177, 178, 25);
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
		lblDateTo.setBounds(50, 158, 73, 14);
		panel.add(lblDateTo);
		lblDateTo.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblDateFrom = new JLabel("DATE : From");
		lblDateFrom.setBounds(50, 102, 82, 14);
		panel.add(lblDateFrom);
		lblDateFrom.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JRadioButton rdbtnAll = new JRadioButton("All");
		rdbtnAll.setBounds(4, 244, 49, 23);
		panel.add(rdbtnAll);
		rdbtnAll.setSelected(true);
		rdbtnAll.setFont(new Font("Tahoma", Font.PLAIN, 12));
		agegroup.add(rdbtnAll);

		JRadioButton rdbtnMale = new JRadioButton("Male");
		rdbtnMale.setBounds(50, 244, 59, 23);
		panel.add(rdbtnMale);
		rdbtnMale.setFont(new Font("Tahoma", Font.PLAIN, 12));
		agegroup.add(rdbtnMale);

		JRadioButton rdbtnFemale = new JRadioButton("Female");
		rdbtnFemale.setBounds(111, 244, 73, 23);
		panel.add(rdbtnFemale);
		rdbtnFemale.setFont(new Font("Tahoma", Font.PLAIN, 12));
		agegroup.add(rdbtnFemale);

		JLabel lblSelectSex = new JLabel("Select Sex");
		lblSelectSex.setBounds(50, 223, 73, 14);
		panel.add(lblSelectSex);
		lblSelectSex.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JButton btnNewButton_2 = new JButton("Search");
		btnNewButton_2.setBounds(36, 278, 111, 35);
		panel.add(btnNewButton_2);
		btnNewButton_2.setIcon(new ImageIcon(MISCBrowser.class
				.getResource("/icons/zoom_r_button.png")));
		btnNewButton_2.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setVerticalAlignment(SwingConstants.TOP);
		lblNewLabel.setIcon(new ImageIcon(MISCBrowser.class
				.getResource("/icons/opd.gif")));
		lblNewLabel.setBounds(35, 324, 111, 83);
		panel.add(lblNewLabel);
		populateTable(DateFormatChange.StringToMysqlDate(new Date()),
				DateFormatChange.StringToMysqlDate(new Date()));
	}
	public void searchTableContents(String searchString) {
		DefaultTableModel currtableModel = (DefaultTableModel) jTable.getModel();
		// To empty the table before search
		currtableModel.setRowCount(0);
		// To search for contents from original table content
		for (Object rows : originalTableModel) {
			Vector rowVector = (Vector) rows;
			for (Object column : rowVector) {
				if (column.toString().toLowerCase().contains(searchString.toLowerCase())) {
					// content found so adding to table
					currtableModel.addRow(rowVector);
					break;
				}
			}
		}
	}
	public void populateTable(String dateFrom, String dateTo) {

		try {
			MiscDBConnection db = new MiscDBConnection();
			ResultSet rs = db.retrieveAllDataCatWiseExcel(dateFrom, dateTo);

			// System.out.println("Table: " + rs.getMetaData().getTableName(1));
			int NumberOfColumns = 0, NumberOfRows = 0;
			NumberOfColumns = rs.getMetaData().getColumnCount();

			while (rs.next()) {
				NumberOfRows++;
			}
			rs.beforeFirst();

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
					new String[] { "Reciept Number",
							"Patient Id","Patient Name", "Doctor Name", "Charges", "Date"
							 }) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;// This causes all cells to be not editable
				}
			};
			jTable.setModel(model);
			TableColumn columnsize = null;
			for (int i = 0; i < 6; i++) {
				columnsize = jTable.getColumnModel().getColumn(i);
				columnsize.setPreferredWidth(110);
				if (i == 1 || i == 2 || i == 3) {
					columnsize.setPreferredWidth(150);
				}
				if (i == 5)
					columnsize.setPreferredWidth(60);
			}
			originalTableModel = (Vector) ((DefaultTableModel) jTable.getModel())
					.getDataVector().clone();
    	
		} catch (SQLException ex) {
			Logger.getLogger(MISCBrowser.class.getName()).log(Level.SEVERE,
					null, ex);
		}
	}
}
