package hms1.ipd.gui;

import hms.departments.gui.IndoorProcedureEntry;
import hms.main.DateFormatChange;
import hms.main.ProgressBar;
import hms.main.loading;
import hms.patient.slippdf.PO_PDF;
import hms.patient.slippdf.PatientLabelPrint;
import hms.reception.gui.ReceptionMain;
import hms.reporttables.PatientDetails;
import hms.reporttables.PatientOnBedReport;
import hms.store.gui.ItemBrowser.CustomRenderer;
import hms1.ipd.database.IPDDBConnection;
import hms1.wards.database.WardsManagementDBConnection;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

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
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import com.itextpdf.text.DocumentException;
import com.sun.prism.paint.Stop;
import com.toedter.calendar.JDateChooser;

import UsersActivity.database.UADBConnection;
import javax.swing.JTextField;
import javax.swing.JWindow;

public class IPDBrowser extends JDialog {

	UADBConnection ua=new UADBConnection();
	private JPanel contentPane;
	private JTable ipdbrowserTable;
	private String patientDOA;
	ButtonGroup agegroup = new ButtonGroup();
	DateFormatChange dateFormat = new DateFormatChange();
	private JDateChooser dateToDC;
	private JDateChooser dateFromDC;
	private IPDBrowser progressWindow = this;
	String dateFrom, dateTo;
	Vector originalTableModel;
	static String ipdentry = "";
	static String patientid = "";
	static String patientname = "";
	private JTextField textField;
	private Timer timer;
	int t=0;
	private JTextField TodayIpdTF =new JTextField() ;
	private JTextField ToadayDischTF=new JTextField();
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					IPDBrowser frame = new IPDBrowser("uname");
					frame.setVisible(true);
					//					frame.dispose();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public IPDBrowser(final String username) {
		setResizable(false);
		setTitle("IPD Entry List");
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				IPDBrowser.class.getResource("/icons/rotaryLogo.png")));
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
		ipdbrowserTable.setModel(new DefaultTableModel(new Object[][] { { null,
			null, null, null, null }, }, new String[] { "IPD No.",
					"Patient ID", "Patient Name", "Insurance Type", "Ward Name",
			"Bed No." }));
		ipdbrowserTable.getColumnModel().getColumn(0).setMinWidth(100);
		ipdbrowserTable.getColumnModel().getColumn(1).setMinWidth(100);
		ipdbrowserTable.getColumnModel().getColumn(2).setMinWidth(100);
		ipdbrowserTable.getColumnModel().getColumn(3).setMinWidth(100);
		ipdbrowserTable.getColumnModel().getColumn(4).setMinWidth(100);
		scrollPane.setViewportView(ipdbrowserTable);
		populateTable(DateFormatChange.StringToMysqlDate(new Date()),
				DateFormatChange.StringToMysqlDate(new Date()));
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
				if (arg0.getClickCount() == 1) {
					JTable target = (JTable) arg0.getSource();
					int row = target.getSelectedRow();
					int column = target.getSelectedColumn();
					// do some action

					Object selectedObject = ipdbrowserTable.getModel()
							.getValueAt(row, 0);
					ipdentry = ipdbrowserTable.getModel().getValueAt(row, 0)
							.toString();
					// System.out.println(selectedObject);
					patientid = ipdbrowserTable.getModel().getValueAt(row, 1)
							.toString();
					patientname = ipdbrowserTable.getModel().getValueAt(row, 2)
							.toString();
					patientDOA = ipdbrowserTable.getModel().getValueAt(row, 6)
							.toString();
					System.out.println(ipdentry + " " + patientid + " "
							+ patientname);

				}

			}
		});
		JButton btnNewButton = new JButton("New IPD");
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				IPDEntery ipdEntery = new IPDEntery(IPDBrowser.this);
				// JInternalFrame internal =
				// new JInternalFrame("Frame", true, true, true, true);
				// internal.setContentPane(ipdEntery.getContentPane());
				// internal.setVisible( true );
				// ImageIcon icon = new
				// ImageIcon(Toolkit.getDefaultToolkit().getImage(ipdBrowser.class.getResource("/icons/rotaryLogo.png")));
				// internal.setFrameIcon(icon);
				// internal.putClientProperty("JInternalFrame.frameType",
				// "normal");
				// internal.setBounds(100, 100, 727, 595);
				// internalfram.desktop.add(internal );
				// internal.toFront();
				ipdEntery.setVisible(true);
				ua.check_activity(username, 119, 0);
			}
		});
		btnNewButton.setIcon(new ImageIcon(IPDBrowser.class
				.getResource("/icons/Business.png")));
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnNewButton.setBounds(14, 440, 110, 35);
		contentPane.add(btnNewButton);

		JButton btnNewButton_1 = new JButton("Patients Details");
		btnNewButton_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				PatientDetails PatientDetails=new PatientDetails();
				PatientDetails.setModal(true);
				PatientDetails.setVisible(true);
			}
		});
		btnNewButton_1.setIcon(null);
		btnNewButton_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnNewButton_1.setBounds(785, 440, 139, 35);
		contentPane.add(btnNewButton_1);

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panel.setBounds(4, 11, 192, 418);
		contentPane.add(panel);
		panel.setLayout(null);

		JDateChooser dateFromDC = new JDateChooser();
		dateFromDC.setBounds(6, 90, 178, 25);
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
							IpdDischargeCount(dateFrom, dateTo);
						}
					}

					
				});
		dateFromDC.setDate(new Date());
		dateFromDC.setMaxSelectableDate(new Date());
		dateFromDC.setDateFormatString("yyyy-MM-dd");

		dateToDC = new JDateChooser();
		dateToDC.setBounds(6, 153, 178, 25);
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
		lblDateTo.setBounds(52, 127, 73, 14);
		panel.add(lblDateTo);
		lblDateTo.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblDateFrom = new JLabel("DATE : From");
		lblDateFrom.setBounds(52, 64, 82, 14);
		panel.add(lblDateFrom);
		lblDateFrom.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setVerticalAlignment(SwingConstants.TOP);
		lblNewLabel.setIcon(new ImageIcon(IPDBrowser.class.getResource("/icons/ipdbed.gif")));
		lblNewLabel.setBounds(10, 326, 158, 92);
		panel.add(lblNewLabel);

		textField = new JTextField();
		textField.setBounds(6, 27, 178, 25);
		panel.add(textField);
		textField.getDocument().addDocumentListener(
				new DocumentListener() {
					@Override
					public void insertUpdate(DocumentEvent e) {
						String str = textField.getText() + "";
						searchTableContents(str);

					}

					@Override
					public void removeUpdate(DocumentEvent e) {
						String str = textField.getText() + "";
						searchTableContents(str);
					}

					@Override
					public void changedUpdate(DocumentEvent e) {
						String str = textField.getText() + "";
						searchTableContents(str);
					}
				});
		textField.setColumns(10);
		
		JLabel lblSearch = new JLabel("    Search");
		lblSearch.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblSearch.setBounds(52, 1, 82, 25);
		panel.add(lblSearch);
		
		JLabel lblTodayIpd = new JLabel("Active IPD's");
		lblTodayIpd.setHorizontalAlignment(SwingConstants.CENTER);
		lblTodayIpd.setBounds(52, 182, 92, 15);
		panel.add(lblTodayIpd);
		
		JLabel lblTodayDischarge = new JLabel("Discharge IPD's");
		lblTodayDischarge.setHorizontalAlignment(SwingConstants.CENTER);
		lblTodayDischarge.setBounds(27, 221, 141, 25);
		panel.add(lblTodayDischarge);
		
		TodayIpdTF = new JTextField();
		TodayIpdTF.setEditable(false);
		TodayIpdTF.setBounds(40, 199, 114, 19);
		panel.add(TodayIpdTF);
		TodayIpdTF.setColumns(10);
		
		
		ToadayDischTF = new JTextField();
		ToadayDischTF.setEditable(false);
		ToadayDischTF.setColumns(10);
		ToadayDischTF.setBounds(40, 245, 114, 19);
		panel.add(ToadayDischTF);
		
		JButton btnPrintLabel = new JButton("Print Label");
		btnPrintLabel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					new PatientLabelPrint(patientid,ipdentry,patientDOA,patientname,200,40,40);
				} catch (DocumentException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnPrintLabel.setBounds(41, 289, 117, 25);
		panel.add(btnPrintLabel);
		
		

		JButton btnIpdBill = new JButton("IPD Bill");
		btnIpdBill.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				IPDBill bill = new IPDBill();
				bill.setVisible(true);
				ua.check_activity(username, 120, 0);

			}
		});
		btnIpdBill.setIcon(new ImageIcon(IPDBrowser.class
				.getResource("/icons/list_button.png")));
		btnIpdBill.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnIpdBill.setBounds(131, 440, 110, 35);
		contentPane.add(btnIpdBill);
		JButton btnReturnDiposite = new JButton("Return / Deposit");
		btnReturnDiposite.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				IPDDeposit deposit = new IPDDeposit();
				deposit.setVisible(true);
				ua.check_activity(username, 121, 0);

			}
		});
		btnReturnDiposite.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnReturnDiposite.setBounds(253, 440, 123, 35);
		contentPane.add(btnReturnDiposite);
		if (ReceptionMain.receptionShift.equals("1")) {
			btnReturnDiposite.setEnabled(true);
		} else {
			btnReturnDiposite.setEnabled(false);
		}
		//

		JButton btnChangeDoctor = new JButton("Change Doctor");
		btnChangeDoctor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				IPDDoctors doctor = new IPDDoctors();
				doctor.setVisible(true);
				ua.check_activity(username, 122, 0);
			}
		});
		btnChangeDoctor.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnChangeDoctor.setBounds(386, 440, 123, 35);
		contentPane.add(btnChangeDoctor);

		JButton btnChangeBed = new JButton("Change Bed");
		btnChangeBed.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				IPDChangeBed chengBed = new IPDChangeBed();
				chengBed.setVisible(true);
				ua.check_activity(username, 124, 0);
			}
		});
		btnChangeBed.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnChangeBed.setBounds(652, 440, 123, 35);
		contentPane.add(btnChangeBed);

		JButton btnAddPackage = new JButton("Change Package");
		btnAddPackage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				IPDChangePackage IPDChangePackage = new IPDChangePackage();
				IPDChangePackage.setModal(true);
				IPDChangePackage.setVisible(true);
				ua.check_activity(username, 123, 0);
			}
		});
		btnAddPackage.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnAddPackage.setBounds(519, 440, 123, 35);
		contentPane.add(btnAddPackage);

		
		timer = new Timer(1, new ActionListener() {

			public void actionPerformed(ActionEvent e) {


				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		
				if (timer.isRunning()) {
					timer.stop();
				}
			}

		});
		
		// JButton btnOpen = new JButton("Open");
		// btnOpen.setFont(new Font("Tahoma", Font.PLAIN, 12));
		// btnOpen.setBounds(866, 440, 78, 35);
		// btnOpen.addActionListener(new ActionListener() {
		// public void actionPerformed(ActionEvent arg0) {
		// MainFrame indoorProcedureEntry = new MainFrame();
		// MainFrame up = new MainFrame();
		// up.start(null);
		// indoorProcedureEntry.setVisible(true);
		// String[] arg = new String[5];
		//
		// arg[0] = ipdentry;
		// arg[1] = patientid;
		// arg[2] = patientname;
		// Application.launch(MainFrame.class, arg);
		// dispose();

		// }
		// });
		// contentPane.add(btnOpen);
	}

	protected void searchTableContents(String str) {
		// TODO Auto-generated method stub
		DefaultTableModel currtableModel = (DefaultTableModel) ipdbrowserTable.getModel();
		// To empty the table before search
		currtableModel.setRowCount(0);
		// To search for contents from original table content
		for (Object rows : originalTableModel) {
			Vector rowVector = (Vector) rows;
			for (Object column : rowVector) {
				if (column.toString().toLowerCase().contains(str.toLowerCase())) {
					// content found so adding to table
					currtableModel.addRow(rowVector);
					break;
				}
			}
		}
	}

	
	public void populateTable(String dateFrom, String dateTo) {

		System.out.println("10");
		try {
			IPDDBConnection db = new IPDDBConnection();
			ResultSet rs = db.retrieveAllData(dateFrom, dateTo);

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
					new String[] { "IPD No.", "Patient ID", "Patient Name",
							"Insurance Type", "Ward Name", "Bed No.", "Admission Date", "Discharge Date"
			}) {
				//				DefaultTableModel model = new DefaultTableModel(Rows_Object_Array,
				//						new String[] { "IPD No.", "Patient ID", "Patient Name",
				//								"Insurance Type", "Ward Name", "Bed No.", "Date",
				//								"Button" }) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;// This causes all cells to be not editable
				}
			};

			ipdbrowserTable.setModel(model);

			ipdbrowserTable.getColumnModel().getColumn(0).setMinWidth(120);
			ipdbrowserTable.getColumnModel().getColumn(1).setMinWidth(150);
			ipdbrowserTable.getColumnModel().getColumn(2).setMinWidth(150);
			ipdbrowserTable.getColumnModel().getColumn(3).setMinWidth(100);
			ipdbrowserTable.getColumnModel().getColumn(4).setMinWidth(100);
			ipdbrowserTable.getColumnModel().getColumn(5).setMinWidth(110);
			ipdbrowserTable.getColumnModel().getColumn(6).setMinWidth(110);
			//			ipdbrowserTable.getColumnModel().getColumn(7).setMinWidth(170);
			//			ipdbrowserTable.getColumnModel().getColumn(7)
			//					.setCellRenderer(new JTableButtonRenderer());
			//			ipdbrowserTable.addMouseListener(new JTableButtonMouseListener(
			//					ipdbrowserTable));
			// ipdbrowserTable.add(new JTableButtonClickListner(table));
			// TableCellRenderer buttonRenderer = new JTableButtonRenderer();
			// ipdbrowserTable.getColumnModel().getColumn(6).setCellRenderer(buttonRenderer);
			// ipdbrowserTable.getColumnModel().getColumn(5).setCellRenderer(new
			// ButtonRenderer());
			// ipdbrowserTable.getColumnModel().getColumn(6).setCellEditor(new
			// ButtonEditor(new JCheckBox()));
			// table.getColumn(�Column 3�).setCellRenderer(new
			// ButtonRenderer());
			originalTableModel = (Vector) ((DefaultTableModel) ipdbrowserTable.getModel())
					.getDataVector().clone();
			if(NumberOfRows>0)
			{
				get();
			}
			t=1;
		} catch (SQLException ex) {
			Logger.getLogger(IPDBrowser.class.getName()).log(Level.SEVERE,
					null, ex);
		}
	}
	private void get() {
		// TODO Auto-generated method stub
		ipdbrowserTable.setAutoCreateRowSorter(true);
	}

	private static class JTableButtonRenderer implements TableCellRenderer {
		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			JButton button = new JButton("Doctor Summary");
			if (isSelected) {
				button.setForeground(table.getSelectionForeground());
				button.setBackground(table.getSelectionBackground());
			} else {
				button.setForeground(table.getForeground());
				button.setBackground(UIManager.getColor("Button.background"));
			}
			return button;
		}
	}

	private static class JTableButtonMouseListener extends MouseAdapter {
		private final JTable table;

		public JTableButtonMouseListener(JTable table) {
			this.table = table;
		}

		public void mouseClicked(MouseEvent e) {
			int column = table.getColumnModel().getColumnIndexAtX(e.getX());
			int row = e.getY() / table.getRowHeight();

			// String ipdentry1 = "", patientid = "", patientname = "";
			String[] arg = new String[5];

			arg[0] = table.getValueAt(row, 0).toString();
			arg[1] = table.getValueAt(row, 1).toString();
			arg[2] = table.getValueAt(row, 2).toString();
			Application.launch(DischargeSummary.class, arg);
			//			Platform.exit();
			//			 Platform.runLater(new Runnable() {
			//			       public void run() {             
			//			           new DischargeSummary().start(new Stage());
			//			       }
			//			    });
			//			IPDBrowser frame = new IPDBrowser();
			//			
			//			frame.setVisible(false);
			//			frame.dispose();
			//			dispose();
			//			Platform.exit();
			//			Platform.runLater(null);
			//			finish();
			System.exit(0);
			restartApplication();
			//			IPDBrowser frame = new IPDBrowser();
			//			frame.setVisible(true);


			// if (row < table.getRowCount() && row >= 0 && column <
			// table.getColumnCount() && column >= 0)
			// {
			// Object value = table.getValueAt(row, column);
			// System.out.println(value);
			// if (value instanceof JButton)
			// {
			// ((JButton)value).doClick();
			// }
			// }
		}
	}
	public void IpdDischargeCount(String dateFrom, String dateTo) {
		TodayIpdTF.setText("0");
		ToadayDischTF.setText("0");
		// TODO Auto-generated method stub
		WardsManagementDBConnection db =new WardsManagementDBConnection();
		ResultSet rs=db.retrievePatientIPDDataCount(dateFrom, dateTo);
		try {
			while(rs.next()) {
				TodayIpdTF.setText(rs.getString(1));
				ToadayDischTF.setText(rs.getString(2));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public static void restartApplication()
	{
		final String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
		File currentJar = null;
		try {
			currentJar = new File(IPDBrowser.class.getProtectionDomain().getCodeSource().getLocation().toURI());
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/* is it a jar file? */
		if(!currentJar.getName().endsWith(".jar"))
			return;

		/* Build command: java -jar application.jar */
		final ArrayList<String> command = new ArrayList<String>();
		command.add(javaBin);
		command.add("-jar");
		command.add(currentJar.getPath());

		final ProcessBuilder builder = new ProcessBuilder(command);
		try {
			builder.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.exit(0);
	}
}
