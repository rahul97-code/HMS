package hms.test.gui;

import hms.amountreceipt.database.AmountReceiptDBConnection;
import hms.departments.gui.IndoorProcedureEntry;
import hms.exam.database.ExamDBConnection;
import hms.exams.gui.ExamEntery;
import hms.exams.gui.ExamsBrowser;
import hms.exams.gui.IPDExamEntery;
import hms.main.DateFormatChange;
import hms.patient.slippdf.ExamSlippdfRegenerate;
import hms.patient.slippdf.FreeExamSlippdfRegenerate;
import hms.patient.slippdf.PO_PDF;
import hms.reception.gui.ReceptionMain;
import hms.reporttables.PatientOnBedReport;
import hms.store.gui.ItemBrowser.CustomRenderer;
import hms.store.gui.ItemBrowser.CustomRenderer;
import hms1.ipd.database.IPDDBConnection;
import hms1.ipd.gui.DischargeSummary;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
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
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;

import javax.swing.AbstractButton;
import javax.swing.AbstractListModel;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import com.itextpdf.text.DocumentException;
import com.sun.prism.paint.Stop;
import com.toedter.calendar.JDateChooser;

import LIS_System.LIS_Booking;
import LIS_System.LIS_StatusChecking;
import LIS_System.LIS_Status_result;
import UsersActivity.database.UADBConnection;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.border.BevelBorder;
import javax.swing.JProgressBar;

public class TestApproved extends JDialog {

	UADBConnection ua=new UADBConnection();
	Vector<String> examlisCode=new Vector<String>();
	private JPanel contentPane;
	private JTable table;
	ButtonGroup agegroup = new ButtonGroup();
	DateFormatChange dateFormat = new DateFormatChange();
	private JDateChooser dateToDC;
	private JDateChooser dateFromDC;
	String dateFrom, dateTo;
	Vector files = new Vector();
	Vector filesPath = new Vector();
	private Timer timer;
	private String type="";
	private String search,receipt_id="";
	DefaultComboBoxModel modelCB=new DefaultComboBoxModel();

	private String w_ID,P_ID="",p_NAME="",r_ID="",DATE="";
	static String patientWorkOrderNo = "";
	private Vector originalTableModel;
	public JButton btnNewButton_2;
	private String mainDir;
	public static String OS;
	private JList list;
	protected String fileSelected;
	protected String pid;
	private String[] open=new String[5];
	protected String examid;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					TestApproved frame = new TestApproved("16");
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
	public TestApproved(final String room) {
		setResizable(false);
		setTitle("REPORT SYSTEM");
		setModal(true);
		OS = System.getProperty("os.name").toLowerCase();
		readFile();
		setIconImage(Toolkit.getDefaultToolkit().getImage(TestApproved.class.getResource("/icons/rotaryLogo.png")));
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setBounds(100, 100,904, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		//
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 158, 868, 300);
		contentPane.add(scrollPane);

		table = new JTable();
		table.setToolTipText("Bouble click to check status\n");
		table.setFont(new Font("Tahoma", Font.PLAIN, 12));
		table.getTableHeader().setReorderingAllowed(false);
		table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		table.setModel(new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
						"Receipt ID","WorkOrder ID","Patient ID", "Patient Name","P Type", "Insurance Type", "Exam Name","Exam Price", "Exam Date", "Status"
				}
				));

		table.getColumnModel().getColumn(0).setPreferredWidth(100);
		table.getColumnModel().getColumn(0).setMinWidth(100);
		table.getColumnModel().getColumn(1).setPreferredWidth(120);
		table.getColumnModel().getColumn(1).setMinWidth(120);
		table.getColumnModel().getColumn(2).setPreferredWidth(180);
		table.getColumnModel().getColumn(2).setMinWidth(180);
		table.getColumnModel().getColumn(3).setPreferredWidth(400);
		table.getColumnModel().getColumn(3).setMinWidth(400);
		table.getColumnModel().getColumn(4).setPreferredWidth(70);
		table.getColumnModel().getColumn(4).setMinWidth(70);		
		
		scrollPane.setViewportView(table);

		table.addMouseListener(new MouseListener() {



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

				JTable target = (JTable) arg0.getSource();
				if (arg0.getClickCount() == 1) {
					try {
						
						int row=table.getSelectedRow();
						pid=table.getValueAt(row, 1).toString();
						examid=table.getValueAt(row, 0).toString();
						final File directory = new File(System
								.getProperty("user.dir") + "/localTemp");
						deleteLocalTemp(directory);
						files.clear();
						filesPath.clear();
						list.removeAll();
						list.setListData(files);
						new Thread() {
							@Override
							public void run() {
								try {
									try {
										System.out.println("Value"+getDirectory(pid, examid));
										LocalCopy(
												getDirectory(pid, examid),
												examid);
									} catch (MalformedURLException
											| SmbException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
								} catch (Exception ex) {
									ex.printStackTrace();
								}
							}
						}.start();
					} catch (Exception e2) {
						// TODO: handle exception
					}
				}



			}
		});

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panel.setBounds(13, 12, 867, 134);
		contentPane.add(panel);
		panel.setLayout(null);

		JDateChooser dateFromDC_1 = new JDateChooser();
		dateFromDC_1.setBounds(38, 85, 178, 27);
		panel.add(dateFromDC_1);
		dateFromDC_1.getDateEditor().addPropertyChangeListener(
				new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent arg0) {
						// TODO Auto-generated method stub
						if ("date".equals(arg0.getPropertyName())) {
							dateFrom = DateFormatChange
									.StringToMysqlDate((Date) arg0
											.getNewValue());
							//	populateTable(dateFrom, dateTo,type);
						}
					}
				});
		dateFromDC_1.setDate(new Date());
		dateFromDC_1.setMaxSelectableDate(new Date());
		dateFromDC_1.setDateFormatString("yyyy-MM-dd");
		dateToDC = new JDateChooser();
		dateToDC.setBounds(228, 85, 178, 27);
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
							//populateTable(dateFrom, dateTo,type);
						}
					}
				});
		dateToDC.setDate(new Date());
		dateToDC.setMaxSelectableDate(new Date());
		dateToDC.setDateFormatString("yyyy-MM-dd");

		JLabel lblDateTo = new JLabel("DATE : TO");
		lblDateTo.setBounds(276, 59, 73, 14);
		panel.add(lblDateTo);
		lblDateTo.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblDateFrom = new JLabel("DATE : From");
		lblDateFrom.setBounds(85, 59, 82, 14);
		panel.add(lblDateFrom);
		lblDateFrom.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setBounds(1039, 16, 158, 96);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setVerticalAlignment(SwingConstants.TOP);
		lblNewLabel.setIcon(new ImageIcon(TestApproved.class.getResource("/icons/graphics-hospitals-221777.gif")));
		panel.add(lblNewLabel);

		JLabel lblNewLabel_2 = new JLabel("");
		lblNewLabel_2.setBorder(new LineBorder(UIManager.getColor("Button.select")));
		lblNewLabel_2.setBounds(218, 29, 747, 2);
		panel.add(lblNewLabel_2);

		JLabel lblNewLabel_3 = new JLabel("RIS System");
		lblNewLabel_3.setForeground(UIManager.getColor("CheckBoxMenuItem.acceleratorForeground"));
		lblNewLabel_3.setFont(new Font("Dialog", Font.ITALIC, 16));
		lblNewLabel_3.setBounds(40, 16, 183, 31);
		panel.add(lblNewLabel_3);

		btnNewButton_2 = new JButton("Search");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				timer.start();

			}
		});
		btnNewButton_2.setBounds(418, 86, 88, 25);
		panel.add(btnNewButton_2);

		JPanel panel_1 = new JPanel();
		panel_1.setBounds(552, 43, 303, 82);
		panel.add(panel_1);
		panel_1.setLayout(null);
		panel_1.setBorder(new TitledBorder(null, "Files", TitledBorder.LEADING,
				TitledBorder.TOP, new Font("Tahoma", Font.PLAIN, 12), null));

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(12, 12, 281, 58);
		panel_1.add(scrollPane_1);
		list = new JList();
		list.setToolTipText("Double Click To Open File");
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setFont(new Font("Tahoma", Font.PLAIN, 11));
		scrollPane_1.setViewportView(list);
		list.setModel(new AbstractListModel() {
			String[] values = new String[] {};

			@Override
			public int getSize() {
				return values.length;
			}

			@Override
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent event) {
				if (!event.getValueIsAdjusting()) {
					try {
						JList source = (JList) event.getSource();
						fileSelected = source.getSelectedValue().toString();
					} catch (Exception e) {
						// TODO: handle exception
					}
					

				}
			}
		});

		list.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				try {
					if (!arg0.getValueIsAdjusting()) {
					
						if (isWindows()) {
							OPenFileWindows("localTemp/"
									+ list.getSelectedValue().toString() + "");
						}else if (isUnix()) {

							if (System.getProperty("os.version").equals("3.11.0-12-generic")) {
								Run(new String[] { "/bin/bash", "-c",
										open[0] + " localTemp/" + list.getSelectedValue().toString() });
							} else {
								Run(new String[] { "/bin/bash", "-c",
										open[1] + " localTemp/" + list.getSelectedValue().toString() });
							}
						} else {
							Run(new String[] { "/bin/bash", "-c",
									open[2] + " localTemp/" + list.getSelectedValue().toString() });
						}
						
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			}

		});

		timer = new Timer(1, new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				Thread thread = new Thread(new Runnable() {
					@Override
					public void run() {
						populateTable(dateFrom, dateTo,room);
					}
				});
				thread.start();
				if (timer.isRunning()) {
					timer.stop();
				}
			}

		});

	}


	public void searchTableContents(String searchString) {
		DefaultTableModel currtableModel = (DefaultTableModel) table.getModel();
		if(currtableModel.getRowCount()>0 )
			// To empty the table before search
		{
			currtableModel.setRowCount(0);
			// To search for contents from original table content
			for (Object rows : originalTableModel) {
				Vector rowVector = (Vector) rows;
				for (Object column : rowVector) {
					if (column.toString().toLowerCase()
							.contains(searchString.toLowerCase())) {
						// content found so adding to table
						currtableModel.addRow(rowVector);
						break;
					}
				}
			}
		}
	}

	public void populateTable(String dateFrom, String dateTo,String room) {
		try {
			ExamDBConnection db = new ExamDBConnection();
			ResultSet rs = db.retrievePerformedExamData(dateFrom, dateTo,room);
			int NumberOfColumns = 0, NumberOfRows = 0;
			NumberOfColumns = rs.getMetaData().getColumnCount();
			rs.last();
			NumberOfRows=rs.getRow();
			rs.beforeFirst();;

			// to set rows in this array
			Object Rows_Object_Array[][];
			Rows_Object_Array = new Object[NumberOfRows][NumberOfColumns];

			int R = 0;
			while (rs.next()) {
				for (int C = 1; C <= NumberOfColumns; C++) {
					Rows_Object_Array[R][C - 1] = rs.getObject(C)==null?"...":rs.getObject(C);
				}
				R++;
			}
			db.closeConnection();
			DefaultTableModel model=null;
			// Finally load data to the table
			model = new DefaultTableModel(Rows_Object_Array,
					new String[]  {
							"Exam ID","Patient ID", "Patient Name","Exam Name","Status"
			}) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;// This causes all cells to be not editable
				}

			};
			table.setModel(model);
			originalTableModel = (Vector) ((DefaultTableModel) table.getModel())
					.getDataVector().clone();
			table.getColumnModel().getColumn(0).setPreferredWidth(100);
			table.getColumnModel().getColumn(0).setMinWidth(100);
			table.getColumnModel().getColumn(1).setPreferredWidth(120);
			table.getColumnModel().getColumn(1).setMinWidth(120);
			table.getColumnModel().getColumn(2).setPreferredWidth(160);
			table.getColumnModel().getColumn(2).setMinWidth(160);
			table.getColumnModel().getColumn(3).setPreferredWidth(400);
			table.getColumnModel().getColumn(3).setMinWidth(400);
			table.getColumnModel().getColumn(4).setPreferredWidth(70);
			table.getColumnModel().getColumn(4).setMinWidth(70);			
			table.getColumnModel().getColumn(4).setCellRenderer(new CustomRenderer());


		} catch (SQLException ex) {
			Logger.getLogger(TestApproved.class.getName()).log(Level.SEVERE,
					null, ex);
		}

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
			boolean fetch = true;
			while ((line = bufferedReader.readLine()) != null && fetch) {
				// System.out.println(line);
				str = line;
				fetch = false;
			}
			String data[] = new String[22];
			int i = 0;
			for (String retval : str.split("@")) {
				data[i] = retval;
				i++;
			}
			mainDir = data[1];
			open[0] = data[2];
			open[1] = data[3];
			open[2] = data[4];
			// Always close files.
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileName + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + fileName + "'");
		}
	}

	public String getDirectory(String pid, String exam_id) {

		return mainDir + "/HMS/Patient/" + pid + "/Exam/" + exam_id + "/";
	}
	


	
	public void LocalCopy(String path, String index)
			throws MalformedURLException, SmbException {
		System.out.println(path);
		String files;
		SmbFile folder = new SmbFile(path);

		SmbFile[] listOfFiles;
		try {
			listOfFiles = folder.listFiles();
		} catch (Exception e) {
			// TODO: handle exception
			return;
		}
		// fileList.clear();
		System.out.println("Lengthgh"+listOfFiles.length);
		for (int i = 0; i < listOfFiles.length; i++) {

			if (listOfFiles[i].isFile()) {
				files = listOfFiles[i].getName();

				try {
					copyFileFilesLocal(getDirectory(pid, index) + "/" + files,
							"localTemp/" + files.replaceAll("\\s+", ""));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}

		try {
			getAllFiles("localTemp/");
		} catch (MalformedURLException | SmbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

	private void copyFileFilesLocal(String source, String dest)
			throws IOException {
		new File("localTemp").mkdir();

		SmbFile remoteFile = new SmbFile(source);
		OutputStream os = new FileOutputStream(dest);
		InputStream is = null;
		try {
			is = remoteFile.getInputStream();

		} catch (Exception e) {
			// TODO: handle exception
			return;
		}

		int bufferSize = 5096;

		byte[] b = new byte[bufferSize];
		int noOfBytes = 0;
		while ((noOfBytes = is.read(b)) != -1) {
			os.write(b, 0, noOfBytes);
		}
		os.close();
		is.close();

	}
	
	public static boolean deleteLocalTemp(File directory) {

		if (directory.exists()) {
			File[] files = directory.listFiles();
			if (null != files) {
				for (int i = 0; i < files.length; i++) {
					if (files[i].isDirectory()) {
						deleteLocalTemp(files[i]);
					} else {
						files[i].delete();
					}
				}
			}
		}
		return (directory.delete());
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

	public void getAllFiles(String path) throws MalformedURLException,
	SmbException {
		files.clear();
		filesPath.clear();
		File folder1 = new File(path);
		File[] listOfFiles1 = folder1.listFiles();
		// System.out.println(listOfFiles1.length+"  Total Files");
		for (int i = 0; i < listOfFiles1.length; i++) {
			if (listOfFiles1[i].isFile()) {
				files.add(listOfFiles1[i].getName().toString());
				filesPath.add(listOfFiles1[i].getPath().toString());
			}
		}
		list.removeAll();
		list.setListData(files);
	}
	public class CustomRenderer extends DefaultTableCellRenderer 
	  {
	      public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	      {
	          Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

	          if(table.getValueAt(row, column)!=null)
	          {
	        	  if(table.getValueAt(row, column).equals("Yes")){
		              cellComponent.setBackground(Color.GREEN);
		          } else{
		        	  cellComponent.setBackground(Color.WHITE);
		        	 
		          }
	          }
	         
	          return cellComponent;
	      }
	  }
	private void get() {
		// TODO Auto-generated method stub
		table.setAutoCreateRowSorter(true);
	}
}
