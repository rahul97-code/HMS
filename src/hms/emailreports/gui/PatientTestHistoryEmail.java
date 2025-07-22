package hms.emailreports.gui;

import hms.exam.database.ExamDBConnection;
import hms.opd.gui.OPDBrowser;
import hms.patient.database.PatientDBConnection;
import hms.test.database.TestResultDBConnection;

import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;

public class PatientTestHistoryEmail extends JPanel {
	private JTable testHistoryTable;
	String mainDir = "";
	public String p_id = "";
	public String p_name = "";
	private JList list;
	Vector fileList = new Vector();
	String performed = "No";
	Object ReportName = "";
	SmbFile folder;
	SmbFile[] listOfFiles;
	static String OS;
	File folder1;
	File[] listOfFiles1;
	private JTextField searchExamTB;
	Component component;
	private JProgressBar progressBar;
	Object results[];
	private Pattern pattern;
	private Matcher matcher;

	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	private JTextField emailAddressTB;
	private JTextField subjectTB;
	private JButton sendBT;
	private JLabel lblEmailMessage;
	private JTextArea messageTA;
	String[] open = new String[4];

	/**
	 * Create the panel.
	 */
	public PatientTestHistoryEmail() {
		setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(20, 44, 316, 405);
		add(scrollPane);
		readFile();
		pattern = Pattern.compile(EMAIL_PATTERN);
		OS = System.getProperty("os.name").toLowerCase();
		testHistoryTable = new JTable();
		testHistoryTable.getTableHeader().setReorderingAllowed(false);
		testHistoryTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		testHistoryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		testHistoryTable.setFont(new Font("Tahoma", Font.PLAIN, 12));
		scrollPane.setViewportView(testHistoryTable);
		testHistoryTable.setModel(new DefaultTableModel(new Object[][] {
				{ null, null, null }, { null, null, null },
				{ null, null, null }, { null, null, null },
				{ null, null, null }, { null, null, null },
				{ null, null, null }, { null, null, null },
				{ null, null, null }, { null, null, null }, }, new String[] {
				"Counter", "Exam Date", "Exam Name" }));
		testHistoryTable.getColumnModel().getColumn(0).setPreferredWidth(70);
		testHistoryTable.getColumnModel().getColumn(0).setMinWidth(70);
		testHistoryTable.getColumnModel().getColumn(1).setPreferredWidth(80);
		testHistoryTable.getColumnModel().getColumn(1).setMinWidth(80);
		testHistoryTable.getColumnModel().getColumn(2).setPreferredWidth(170);
		testHistoryTable.getColumnModel().getColumn(2).setMinWidth(170);

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Files", TitledBorder.LEADING,
				TitledBorder.TOP, new Font("Tahoma", Font.PLAIN, 12), null));
		panel.setBounds(365, 241, 310, 208);
		add(panel);
		panel.setLayout(null);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(6, 42, 294, 155);
		panel.add(scrollPane_1);

		list = new JList();
		list.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				try {

					if (!arg0.getValueIsAdjusting()) {
						if (fileList.size() < 1) {
							return;
						}
						if (isWindows()) {
							OPenFileWindows("localTemp/"
									+ list.getSelectedValue().toString() + "");
						} else if (isUnix()) {
							if (list.getSelectedValue().toString()
									.endsWith(".pdf")
									|| list.getSelectedValue().toString()
											.endsWith(".png")
									|| list.getSelectedValue().toString()
											.endsWith(".jpg")
									|| list.getSelectedValue().toString()
											.endsWith(".PNG")
									|| list.getSelectedValue().toString()
											.endsWith(".JPG")
									|| list.getSelectedValue().toString()
											.endsWith(".bmp")
									|| list.getSelectedValue().toString()
											.endsWith(".BMP")) {
								if (System.getProperty("os.version").equals(
										"3.11.0-12-generic")) {
									Run(new String[] {
											"/bin/bash",
											"-c",
											open[0]
													+ " localTemp/"
													+ list.getSelectedValue()
															.toString() });
								} else {
									Run(new String[] {
											"/bin/bash",
											"-c",
											open[1]
													+ " localTemp/"
													+ list.getSelectedValue()
															.toString() });
								}
							} else {
								Run(new String[] {
										"/bin/bash",
										"-c",
										open[2]
												+ " localTemp/"
												+ list.getSelectedValue()
														.toString() });
							}
						}

					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});
		list.setFont(new Font("Tahoma", Font.PLAIN, 12));
		scrollPane_1.setViewportView(list);

		progressBar = new JProgressBar();
		progressBar.setMinimum(0);
		progressBar.setStringPainted(true);
		progressBar.setBounds(7, 14, 293, 24);
		panel.add(progressBar);

		JLabel lblSearch = new JLabel("Search :");
		lblSearch.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblSearch.setBounds(20, 16, 56, 14);
		add(lblSearch);

		searchExamTB = new JTextField();
		searchExamTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		searchExamTB.setBounds(89, 11, 244, 23);
		add(searchExamTB);
		searchExamTB.setColumns(10);

		JLabel lblEmailId = new JLabel("Email ID :");
		lblEmailId.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblEmailId.setBounds(365, 60, 61, 14);
		add(lblEmailId);

		emailAddressTB = new JTextField();
		emailAddressTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		emailAddressTB.setBounds(436, 55, 239, 24);
		add(emailAddressTB);
		emailAddressTB.setColumns(10);

		JLabel lblSubject = new JLabel("Subject :");
		lblSubject.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblSubject.setBounds(365, 94, 61, 14);
		add(lblSubject);

		subjectTB = new JTextField();
		subjectTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		subjectTB.setBounds(436, 90, 239, 24);
		add(subjectTB);
		subjectTB.setColumns(10);

		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(436, 125, 239, 53);
		add(scrollPane_2);

		messageTA = new JTextArea();
		scrollPane_2.setViewportView(messageTA);
		messageTA.setFont(new Font("Tahoma", Font.PLAIN, 12));
		messageTA.setRows(3);

		JLabel lblMessage = new JLabel("Message :");
		lblMessage.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblMessage.setBounds(365, 127, 61, 14);
		add(lblMessage);

		sendBT = new JButton("Send Email");
		sendBT.setEnabled(false);
		sendBT.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				if (!validate(emailAddressTB.getText().toString())) {
					JOptionPane.showMessageDialog(null,
							"incorrect email address", "input error ",
							JOptionPane.INFORMATION_MESSAGE);
				} else if (subjectTB.getText().toString().equals("")) {
					JOptionPane.showMessageDialog(null, "enter subject",
							"input error ", JOptionPane.INFORMATION_MESSAGE);

				} else {

					final String emailAddress = emailAddressTB.getText()
							.toString();
					final String subject = subjectTB.getText().toString();
					// final String message = messageTA.getText().toString();

					final String message = "<table width='47%' border='1' align='center' height='250' cellpadding='0' cellspacing='0'><tr> <td width='100%' > <table border='0' cellpadding='1' cellspacing='0' width='100%'><tr><td   style='font-size:24px;text-align:center;font-family:Times New Roman, Times, serif;font-weight:700; color:#FFF;background:#333'>ROTARY AMBALA CANCER AND GENERAL HOSPITAL</td></tr>"
							+ " <tr><td height='20' align='center' style='font-size:12px; font-weight:700; background:#CCC' ><p>Opp. Dusshera Ground, Ram Bagh Road, Ambala Cantt (haryana) <br />Telephone No. : 0171-2690009, Mobile No. : 09034056794</p></td></tr></table>    </td>"
							+ " </tr> <tr> <td height='182' align='center'><div>&nbsp; "
							+ messageTA.getText().toString()
							+ "</div> </td </tr></table>";
					final String[] files = new String[fileList.size()];
					for (int i = 0; i < files.length; i++) {
						files[i] = (String) fileList.get(i);
					}
					final JDialog dialog = new JDialog();
					final JLabel label = new JLabel(
							"   Please wait for the current email to be sent");
					label.setFont(new Font("Tahoma", Font.PLAIN, 19));
					dialog.setLocationRelativeTo(null);
					dialog.setTitle("Please Wait...");
					dialog.getContentPane().add(label);
					dialog.setSize(400, 200);
					dialog.setUndecorated(true);
					dialog.setResizable(true);

					setEnabled(false);
					dialog.setVisible(true);
					new Thread() {
						@Override
						public void run() {
							try {
								try {
									SendFunction.setMessage(emailAddress,
											"Dear " + p_name, "" + subject, ""
													+ message, files);
								} catch (IOException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
								try {
									if (SendFunction.sendMail()) {
										lblEmailMessage
												.setText("email sent successfully");
										lblEmailMessage
												.setForeground(Color.GREEN);
										PatientDBConnection patientDBConnection = new PatientDBConnection();
										patientDBConnection.updateDataEmail(
												p_id, emailAddress);
										patientDBConnection.closeConnection();

									} else {
										lblEmailMessage
												.setText("email not sent successfully");
										lblEmailMessage
												.setForeground(Color.RED);
									}
								} catch (IOException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}

								Thread.sleep(150);
								dialog.setVisible(false);
								setEnabled(true);
							} catch (Exception ex) {
								ex.printStackTrace();
							}
						}
					}.start();

				}
			}
		});
		sendBT.setFont(new Font("Tahoma", Font.PLAIN, 15));
		sendBT.setBounds(523, 192, 152, 38);
		add(sendBT);

		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.LIGHT_GRAY);
		panel_1.setBounds(416, 15, 244, 17);
		add(panel_1);
		panel_1.setLayout(null);

		lblEmailMessage = new JLabel("");
		lblEmailMessage.setBounds(0, 0, 244, 17);
		panel_1.add(lblEmailMessage);
		lblEmailMessage.setHorizontalAlignment(SwingConstants.CENTER);
		lblEmailMessage.setFont(new Font("Tahoma", Font.BOLD, 12));

		JButton btnNewButton = new JButton("Open Directory");
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (fileList.size() > 0) {
					final String dirs = System.getProperty("user.dir");
					if (isWindows()) {
						File dir = new File(dirs + "/localTemp/");
						if (Desktop.isDesktopSupported()) {
							try {
								Desktop.getDesktop().open(dir);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					} else if (isUnix()) {

						if (System.getProperty("os.version").equals(
								"3.11.0-12-generic")) {
							Run(new String[] { "/bin/bash", "-c",
									open[0] + " " + dirs + "/localTemp/" });
						} else {
							Run(new String[] { "/bin/bash", "-c",
									open[1] + " " + dirs + "/localTemp/" });
						}
					}

				} else {
					JOptionPane.showMessageDialog(null,
							"Test Results not available", "Patient",
							JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		btnNewButton.setBounds(365, 192, 152, 38);
		add(btnNewButton);
		searchExamTB.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				String str = searchExamTB.getText() + "";
				if (!str.equals("")) {
					populateTestHistoryTable(p_id, p_name, str);
				} else {
					populateTestHistoryTable(p_id, p_name);
				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				String str = searchExamTB.getText() + "";
				if (!str.equals("")) {
					populateTestHistoryTable(p_id, p_name, str);
				} else {
					populateTestHistoryTable(p_id, p_name);
				}
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				String str = searchExamTB.getText() + "";
				if (!str.equals("")) {
					populateTestHistoryTable(p_id, p_name, str);
				} else {
					populateTestHistoryTable(p_id, p_name);
				}
			}
		});

		testHistoryTable.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					@Override
					public void valueChanged(ListSelectionEvent e) {
						// TODO Auto-generated method stub
						try {
							int selectedRowIndex = testHistoryTable
									.getSelectedRow();
							selectedRowIndex = testHistoryTable
									.convertRowIndexToModel(selectedRowIndex);
							Object selectedObject = testHistoryTable.getModel()
									.getValueAt(selectedRowIndex, 0);
							// System.out.println("Exam id" + selectedObject);
							sendBT.setEnabled(false);
							lblEmailMessage.setText("");
							fileList.clear();
							list.setListData(fileList);
							progressBar.setValue(0);
							if (getExamData("" + selectedObject)) {
								sendBT.setEnabled(true);
							}
						} catch (Exception e2) {
							// TODO: handle exception
						}

					}
				});
	}

	public boolean validate(final String hex) {

		matcher = pattern.matcher(hex);
		return matcher.matches();
	}

	public void populateTestHistoryTable(String patientID, String p_name) {
		p_id = patientID;
		this.p_name = p_name;
		fileList.clear();
		list.setListData(fileList);
		sendBT.setEnabled(false);
		lblEmailMessage.setText("");
		progressBar.setValue(0);
		try {
			ExamDBConnection db = new ExamDBConnection();
			ResultSet rs = db.retrieveTestHistoryDataPatientID(patientID);

			// System.out.println("Table: " + rs.getMetaData().getTableName(1));
			int NumberOfColumns = 0, NumberOfRows = 0;
			NumberOfColumns = rs.getMetaData().getColumnCount();

			while (rs.next()) {
				NumberOfRows++;
			}
			// System.out.println("rows : " + NumberOfRows);
			// System.out.println("columns : " + NumberOfColumns);
			rs = db.retrieveTestHistoryDataPatientID(patientID);

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
					new String[] { "Counter", "Exam Date", "Exam Name" }) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;// This causes all cells to be not editable
				}
			};
			testHistoryTable.setModel(model);
			testHistoryTable.getColumnModel().getColumn(0)
					.setPreferredWidth(70);
			testHistoryTable.getColumnModel().getColumn(0).setMinWidth(70);
			testHistoryTable.getColumnModel().getColumn(1)
					.setPreferredWidth(80);
			testHistoryTable.getColumnModel().getColumn(1).setMinWidth(80);
			testHistoryTable.getColumnModel().getColumn(2)
					.setPreferredWidth(170);
			testHistoryTable.getColumnModel().getColumn(2).setMinWidth(170);
			emailAddressTB.setText("");
			subjectTB.setText("");
			messageTA.setText("");
			PatientDBConnection patientDBConnection = new PatientDBConnection();

			rs = patientDBConnection.retrieveEmailWithIndex(patientID);
			Object email = null;
			while (rs.next()) {
				email = rs.getObject(1);
			}
			patientDBConnection.closeConnection();

			if (email != null) {
				emailAddressTB.setText(email.toString());
			}
		} catch (SQLException ex) {
			Logger.getLogger(OPDBrowser.class.getName()).log(Level.SEVERE,
					null, ex);
		}
	}

	public void populateTestHistoryTable(String patientID, String p_name,
			String searchText) {
		fileList.clear();
		list.setListData(fileList);
		progressBar.setValue(0);
		sendBT.setEnabled(false);
		lblEmailMessage.setText("");
		try {
			ExamDBConnection db = new ExamDBConnection();
			ResultSet rs = db.retrieveTestHistoryDataPatientID(patientID,
					searchText);

			// System.out.println("Table: " + rs.getMetaData().getTableName(1));
			int NumberOfColumns = 0, NumberOfRows = 0;
			NumberOfColumns = rs.getMetaData().getColumnCount();

			while (rs.next()) {
				NumberOfRows++;
			}
			// System.out.println("rows : " + NumberOfRows);
			// System.out.println("columns : " + NumberOfColumns);
			rs = db.retrieveTestHistoryDataPatientID(patientID, searchText);

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
					new String[] { "Counter", "Exam Date", "Exam Name" }) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;// This causes all cells to be not editable
				}
			};
			testHistoryTable.setModel(model);
			testHistoryTable.getColumnModel().getColumn(0)
					.setPreferredWidth(70);
			testHistoryTable.getColumnModel().getColumn(0).setMinWidth(70);
			testHistoryTable.getColumnModel().getColumn(1)
					.setPreferredWidth(80);
			testHistoryTable.getColumnModel().getColumn(1).setMinWidth(80);
			testHistoryTable.getColumnModel().getColumn(2)
					.setPreferredWidth(170);
			testHistoryTable.getColumnModel().getColumn(2).setMinWidth(170);
			emailAddressTB.setText("");
			subjectTB.setText("");
			messageTA.setText("");
			PatientDBConnection patientDBConnection = new PatientDBConnection();

			rs = patientDBConnection.retrieveEmailWithIndex(patientID);
			Object email = null;
			while (rs.next()) {
				email = rs.getObject(1);
			}
			patientDBConnection.closeConnection();

			if (email != null) {
				emailAddressTB.setText(email.toString());
			}
		} catch (SQLException ex) {
			Logger.getLogger(OPDBrowser.class.getName()).log(Level.SEVERE,
					null, ex);
		}
	}

	public String getTestResultCat(String testCounter) {
		String str = "";
		try {
			TestResultDBConnection db = new TestResultDBConnection();
			ResultSet rs = db.retrieveCategory(testCounter);
			while (rs.next()) {
				str = "" + rs.getObject(1);
			}

			db.closeConnection();
		} catch (SQLException ex) {
			Logger.getLogger(OPDBrowser.class.getName()).log(Level.SEVERE,
					null, ex);
		}
		System.out.println("Category " + str);
		return str;
	}

	public String getDirectory(String pid, String exam_id) {

		return mainDir + "/HMS/Patient/" + pid + "/Exam/" + exam_id + "/";
	}

	public String getDirectoryReport(String pid) {

		return mainDir + "/HMS/Patient/" + pid + "/Reports/";
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
			// Or we could just do this:
			// ex.printStackTrace();
		}
	}

	public void getAllFiles(String path) throws MalformedURLException,
			SmbException {
		String files;
		folder1 = new File(path);
		listOfFiles1 = folder1.listFiles();
		fileList.clear();
		for (int i = 0; i < listOfFiles1.length; i++) {

			if (listOfFiles1[i].isFile()) {
				files = listOfFiles1[i].getName();
				fileList.add(files.toString());
			}
		}
		list.setListData(fileList);
	}

	public void LocalCopy(String path, String index)
			throws MalformedURLException, SmbException {
		String files;
		folder = new SmbFile(path);
		listOfFiles = folder.listFiles();
		progressBar.setMaximum(listOfFiles.length);
		progressBar.setValue(0);
		fileList.clear();

		for (int i = 0; i < listOfFiles.length; i++) {

			if (listOfFiles[i].isFile()) {
				files = listOfFiles[i].getName();
				try {
					copyFileUsingJava7Files(getDirectory(p_id, index) + "/"
							+ files, "localTemp/" + files);
					progressBar.setValue(i + 1);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}

	private void copyFileUsingJava7Files(String source, String dest)
			throws IOException {
		new File("localTemp").mkdir();
		SmbFile remoteFile = new SmbFile(source);
		OutputStream os = new FileOutputStream(dest);
		InputStream is = remoteFile.getInputStream();

		int bufferSize = 5096;

		byte[] b = new byte[bufferSize];
		int noOfBytes = 0;
		while ((noOfBytes = is.read(b)) != -1) {
			os.write(b, 0, noOfBytes);
		}
		os.close();
		is.close();
		try {
			getAllFiles("localTemp/");
		} catch (MalformedURLException | SmbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean deleteDirectory(File directory) {
		if (directory.exists()) {
			File[] files = directory.listFiles();
			if (null != files) {
				for (int i = 0; i < files.length; i++) {
					if (files[i].isDirectory()) {
						deleteDirectory(files[i]);
					} else {
						files[i].delete();
					}
				}
			}
		}
		return (directory.delete());
	}

	public void getComponent(Component component) {
		this.component = component;
	}

	class CustomRenderer extends DefaultTableCellRenderer {
		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			Component cellComponent = super.getTableCellRendererComponent(
					table, value, isSelected, hasFocus, row, column);
			try {
				if (results[row].equals("Out of Range")) {
					cellComponent.setBackground(Color.RED);
					cellComponent.setForeground(Color.BLACK);
				} else {
					cellComponent.setBackground(Color.GREEN);
					cellComponent.setForeground(Color.BLACK);
				}
			} catch (Exception e) {
				// TODO: handle exception
			}

			return cellComponent;
		}
	}

	public boolean getExamData(final String index) {

		try {
			ExamDBConnection db = new ExamDBConnection();
			ResultSet rs = db.retrieveAllDataExamID(index);
			while (rs.next()) {
				p_id = "" + rs.getObject(3);
				performed = rs.getObject(8).toString();
				ReportName = rs.getObject(10);
			}
		} catch (SQLException ex) {
			Logger.getLogger(OPDBrowser.class.getName()).log(Level.SEVERE,
					null, ex);
		}
		if (performed.equals("No")) {
			JOptionPane.showMessageDialog(null, "Test Results not available",
					"Patient", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		if (new File("localTemp").exists()) {
			deleteDirectory(new File("localTemp"));
		}
		if (ReportName == null) {
			System.out.println("nullllllll  " + ReportName);
		} else {
			System.out.println("name      " + ReportName);
			System.out.println(getDirectoryReport(p_id) + "" + ReportName);
			try {

				copyFileUsingJava7Files(getDirectoryReport(p_id) + ""
						+ ReportName+".pdf", "localTemp/" + ReportName+".pdf");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		new Thread() {
			@Override
			public void run() {
				try {

					try {
						LocalCopy(getDirectory(p_id, index), index);
					} catch (MalformedURLException | SmbException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}.start();
		return true;
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

	public JButton getSendBT() {
		return sendBT;
	}

	public JLabel getLblEmailMessage() {
		return lblEmailMessage;
	}

	public JTextArea getMessageTA() {
		return messageTA;
	}
}
