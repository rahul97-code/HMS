package hms.main;

import hms.AutoDeleteFiles.AutoDeleteFiles;
import hms.Printer.PrinterSetting;

import hms.accounts.database.AccountsUserDBConnection;
import hms.accounts.gui.AccountsMain;
import hms.admin.gui.AdminDBConnection;
import hms.admin.gui.AdminMain;
import hms.admin.gui.DeptUserDBConnection;
import hms.departments.database.DepartmentDBConnection;
import hms.departments.gui.DepartmentMain;
import hms.doctor.database.DoctorDBConnection;
import hms.doctor.gui.DoctorMain;
import hms.mrd.database.MRDUserDBConnection;
import hms.mrd.gui.MrdMain;
import hms.reception.database.ReceptionistDBConnection;
import hms.reception.gui.ReceptionMain;
import hms.store.database.StoreAccountDBConnection;
import hms.store.gui.StoreMain;
import hms.sukhpal.updater.UpdaterMain;
import hms.test.database.OperatorDBConnection;
import hms.test.gui.Test;
import hms.doctor.gui.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import de.javasoft.plaf.synthetica.SyntheticaBlueLightLookAndFeel;
import de.javasoft.plaf.synthetica.SyntheticaGreenDreamLookAndFeel;
import de.javasoft.plaf.synthetica.SyntheticaOrangeMetallicLookAndFeel;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import com.seaglasslookandfeel.SeaGlassLookAndFeel;

import UsersActivity.database.UADBConnection;

@SuppressWarnings("serial")
public class MainLogin extends JFrame {

	UADBConnection ua=new UADBConnection();
	private JPanel contentPane;
	private JPasswordField passwordField;
	ResultSet resultSet;
	JTextField userNameTB;
	Vector userNameItem = new Vector();
	Vector departmentItem = new Vector();
	private JButton loginBT;
	String  userName="Admin", validEDate = "", password = "";
	public static String userName1="";
	public static String DoctorName;
	String mainDir = "";

	public static void main(String args[]) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		MainLogin frame = new MainLogin();
		frame.setVisible(true);
	}

	/**
	 * Create the frame.
	 */
	@SuppressWarnings("unchecked")
	public MainLogin() {

		new AutoDeleteFiles().DeleteFolderData();// used to clean RDP data storage

		setIconImage(Toolkit.getDefaultToolkit().getImage(
				MainLogin.class.getResource("/icons/rotaryLogo.png")));
		setTitle("Hospital Management System");
		setBackground(Color.WHITE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setBounds(350, 200, 452, 379);// 350, 200, 549, 358
		contentPane = new JPanel();
		contentPane.setBackground(new Color(248, 248, 255));
		contentPane.setBorder(UIManager.getBorder("InternalFrame.border"));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblLaboratoryInformationManagement = new JLabel(	"Hospital Management System");
		lblLaboratoryInformationManagement.setForeground(new Color(105, 105,
				105));
		lblLaboratoryInformationManagement.setFont(new Font("Vani", Font.BOLD,
				17));
		lblLaboratoryInformationManagement.setBounds(10, 42, 288, 39);
		contentPane.add(lblLaboratoryInformationManagement);

		JSeparator separator = new JSeparator();
		separator.setBounds(10, 92, 424, 2);
		contentPane.add(separator);

		JLabel lblUserName = new JLabel("User Name ");
		lblUserName.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblUserName.setBounds(154, 131, 77, 14);
		contentPane.add(lblUserName);

		userNameTB = new JTextField();
		userNameTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		userNameTB.setBounds(241, 125, 179, 27);
		contentPane.add(userNameTB);

		JLabel lblPassword = new JLabel("Password");
		lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblPassword.setBounds(154, 168, 77, 14);
		contentPane.add(lblPassword);

		passwordField = new JPasswordField();
		passwordField.setFont(new Font("Tahoma", Font.BOLD, 12));
		passwordField.setBounds(241, 163, 179, 26);
		contentPane.add(passwordField);

		JLabel lblDepartment = new JLabel("Department");
		lblDepartment.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblDepartment.setBounds(154, 198, 77, 27);
		contentPane.add(lblDepartment);

		final JComboBox comboBox_1 = new JComboBox(departmentItem);
		comboBox_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		comboBox_1.setModel(new DefaultComboBoxModel(new String[] {
				"Reception", "Doctor", "Exam Lab", "Admin", "Accounts",
				"Store", "Departments","MRD","EMO" }));
		comboBox_1.setBounds(241, 200, 179, 27);
		contentPane.add(comboBox_1);

		JLabel lblLogin = new JLabel("Login");
		lblLogin.setForeground(Color.BLACK);
		lblLogin.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblLogin.setBounds(139, 93, 67, 27);
		contentPane.add(lblLogin);

		loginBT = new JButton("Login");
		loginBT.setIcon(new ImageIcon(MainLogin.class.getResource("/icons/Key.gif")));
		getRootPane().setDefaultButton(loginBT);
		loginBT.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				UpdaterMain up = new UpdaterMain();
				if (up.checkUpdate()) {	
					dispose();
					up.setVisible(true);
					up.DoUpdate();
					return;
				}		

				String pass = passwordField.getText().toString();
				String user = userNameTB.getText().toString();
				userName1=user;
				if (user.equals("")) {
					JOptionPane.showMessageDialog(null, "Enter Username",
							"Login Fail", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (pass.equals("")) {
					JOptionPane.showMessageDialog(null, "Enter password",
							"Login Fail", JOptionPane.ERROR_MESSAGE);
					return;
				}	
				AdminDBConnection ipaddress =new AdminDBConnection();
				try {
					ipaddress.inserIpAddressData(user,comboBox_1.getSelectedItem().toString());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ipaddress.closeConnection();
				switch (comboBox_1.getSelectedIndex()) {
				case 0:
					int n0 = 0;
					ReceptionistDBConnection receptionistDBConnection = new ReceptionistDBConnection();
					ResultSet rs0 = receptionistDBConnection.retrieveUserPassword(user, pass);
					try {
						while (rs0.next()) {
							n0++;
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						receptionistDBConnection.closeConnection();
					}
					receptionistDBConnection.closeConnection();
					if (n0 == 0) {
						JOptionPane.showMessageDialog(null,
								"Invalide username and password combination",
								"Login Fail", JOptionPane.ERROR_MESSAGE);
						return;
					}

					ReceptionMain frame = new ReceptionMain(user);
					frame.setVisible(true);

					dispose();
					break;
				case 1:
					int n = 0;
					DoctorDBConnection db = new DoctorDBConnection();
					ResultSet rs = db.retrieveUserPassword(user, pass);
					DoctorName="";
					try {
						while (rs.next()) {
							DoctorName=rs.getObject(2).toString();
							n++;
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						db.closeConnection();
					}
					db.closeConnection();
					if (n == 0) {
						JOptionPane.showMessageDialog(null,
								"Invalide username and password combination",
								"Login Fail", JOptionPane.ERROR_MESSAGE);
						return;
					}

					ua.check_activity(user, 140, 1);
					DoctorMain doctorMain = new DoctorMain(user);
					doctorMain.setVisible(true);
					db.closeConnection();
					dispose();
					break;
				case 2:
					int n1 = 0;
					OperatorDBConnection db1 = new OperatorDBConnection();
					ResultSet rs1 = db1.retrieveUserPassword(user, pass);

					try {
						while (rs1.next()) {							
							n1++;
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						db1.closeConnection();
					}
					db1.closeConnection();
					if (n1 == 0) {
						JOptionPane.showMessageDialog(null,
								"Invalide username and password combination",
								"Login Fail", JOptionPane.ERROR_MESSAGE);
						return;
					}

					Test test = new Test(user);
					test.setVisible(true);
					dispose();
					break;
				case 3:
					int n11 = 0;
					AdminDBConnection db11 = new AdminDBConnection();
					String acess = null;
					ResultSet rs11 = db11.retrieveUserPassword(user, pass);
					try {
						while (rs11.next()) {
							n11++;
							acess=rs11.getObject(6).toString();
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						db11.closeConnection();
					}
					db11.closeConnection();
					if (n11 == 0) {
						JOptionPane.showMessageDialog(null,
								"Invalide username and password combination",
								"Login Fail", JOptionPane.ERROR_MESSAGE);
						return;
					}
					AdminMain adminMain = new AdminMain(user,acess);
					adminMain.setVisible(true);
					dispose();
					break;
				case 4:
					n0 = 0;
					AccountsUserDBConnection accountsUserDBConnection = new 	AccountsUserDBConnection();
					rs0 = accountsUserDBConnection
							.retrieveUserPassword(user, pass);
					try {
						while (rs0.next()) {
							n0++;
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						accountsUserDBConnection.closeConnection();
					}
					accountsUserDBConnection.closeConnection();
					if (n0 == 0) {
						JOptionPane.showMessageDialog(null,
								"Invalide username and password combination",
								"Login Fail", JOptionPane.ERROR_MESSAGE);
						return;
					}

					AccountsMain accountsMain = new AccountsMain(user,user);
					accountsMain.setVisible(true);
					dispose();
					break;
				case 5:
					int n01 = 0;
					String id = "",
							name = "";
					String store_account_access="",departmental_stock_access="",stock_adjustment_access="",minimum_max_qty_access="",stockaccess="",update_item_access="";
					StoreAccountDBConnection storeAccountDBConnection = new StoreAccountDBConnection();
					ResultSet rs01 = storeAccountDBConnection.retrieveUserPassword(user, pass);
					try {
						while (rs01.next()) {
							id = rs01.getObject(1).toString();
							name = rs01.getObject(2).toString();
							departmental_stock_access=rs01.getObject(3).toString();
							stock_adjustment_access=rs01.getObject(4).toString();
							stockaccess=rs01.getObject(5).toString();
							minimum_max_qty_access=rs01.getObject(6).toString();
							update_item_access=rs01.getObject(7).toString();
							store_account_access=rs01.getObject(8).toString();
							n01++;
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						storeAccountDBConnection.closeConnection();
					}
					storeAccountDBConnection.closeConnection();
					if (n01 == 0) {
						JOptionPane.showMessageDialog(null,
								"Invalide username and password combination",
								"Login Fail", JOptionPane.ERROR_MESSAGE);
						return;
					}
					StoreMain storeMain = new StoreMain(user,name, id,departmental_stock_access,stock_adjustment_access,stockaccess,minimum_max_qty_access,update_item_access,store_account_access);
					storeMain.setVisible(true);
					dispose();
					break;
				case 6:
					int n111 = 0;
					String deptName = "";
					String userName = "";
					String userID = "";
					DeptUserDBConnection db111 = new DeptUserDBConnection();
					ResultSet rs111 = db111.retrieveUserPassword(user, pass);
					try {
						while (rs111.next()) {
							userID = rs111.getObject(1).toString();
							userName = rs111.getObject(2).toString();
							deptName = rs111.getObject(11).toString();
							n111++;
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						db111.closeConnection();
					}
					db111.closeConnection();
					if (n111 > 0) {
						DepartmentMain departmentMain = new DepartmentMain(
								deptName, "" + userName, userID);
						departmentMain.setVisible(true);
						dispose();
					}
					break;
				case 7:
					n0 = 0;
					String userName1 = "";
					String deptID = "";
					MRDUserDBConnection mrdUserDBConnection = new 	MRDUserDBConnection();
					rs0 = mrdUserDBConnection
							.retrieveUserPassword(user, pass);
					try {
						while (rs0.next()) {
							userName1 = rs0.getObject(3).toString();
							deptID = rs0.getObject(1).toString();

							n0++;
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						mrdUserDBConnection.closeConnection();
					}
					mrdUserDBConnection.closeConnection();
					if (n0 == 0) {
						JOptionPane.showMessageDialog(null,
								"Invalide username and password combination",
								"Login Fail", JOptionPane.ERROR_MESSAGE);
						return;
					}

					ua.check_activity(user, 143, 7);
					MrdMain mrdMain = new MrdMain(userName1,deptID);
					mrdMain.setVisible(true);

					dispose();
					break;
				case 8:
					int nE = 0;
					DoctorDBConnection dbE = new DoctorDBConnection();
					ResultSet rsE = dbE.retrieveUserPasswordEMO(user, pass);
					try {
						while (rsE.next()) {
							nE++;
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						dbE.closeConnection();
					}
					dbE.closeConnection();
					if (nE == 0) {
						JOptionPane.showMessageDialog(null,
								"Invalide username and password combination",
								"Login Fail", JOptionPane.ERROR_MESSAGE);
						return;
					}

					IPDPatients doctorMain1 = new IPDPatients(user);
					doctorMain1.setVisible(true);

					dispose();
					break;


				}
			}
		});
		loginBT.setFont(new Font("Tahoma", Font.PLAIN, 12));
		loginBT.setBounds(295, 253, 125, 40);
		contentPane.add(loginBT);
		InputMap im = loginBT.getInputMap();
		im.put(KeyStroke.getKeyStroke("ENTER"), "pressed");
		im.put(KeyStroke.getKeyStroke("released ENTER"), "released");

		JButton cancelBT = new JButton("Cancel");
		cancelBT.setIcon(new ImageIcon(MainLogin.class
				.getResource("/icons/Exit.gif")));
		cancelBT.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				dispose();
				updateLAF(1);
			}
		});
		cancelBT.setFont(new Font("Tahoma", Font.PLAIN, 12));
		cancelBT.setBounds(154, 253, 125, 40);
		contentPane.add(cancelBT);
		InputMap cancelIM = cancelBT.getInputMap();
		cancelIM.put(KeyStroke.getKeyStroke("ENTER"), "pressed");
		cancelIM.put(KeyStroke.getKeyStroke("released ENTER"), "released");

		JLabel iconImage = new JLabel();
		iconImage.setIcon(new ImageIcon(MainLogin.class
				.getResource("/icons/rotaryLogo.png")));
		iconImage.setBounds(10, 103, 130, 190);
		contentPane.add(iconImage);

		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.setBounds(10, 304, 424, 36);
		contentPane.add(panel);
		panel.setLayout(null);

		//		JLabel lblDevelopedBy = new JLabel(
		//				"Developed By : Sukhpal  Saini, Mob. 9728073421");
		JLabel lblDevelopedBy = new JLabel(
				"For Any Query/Support Contact : 9215575671");
		lblDevelopedBy.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDevelopedBy.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblDevelopedBy.setBounds(10, 5, 404, 25);
		panel.add(lblDevelopedBy);

		JButton btnNewButton = new JButton("");
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {


				UpdaterMain up = new UpdaterMain();
				if (up.checkUpdate()) {	
					dispose();
					up.setVisible(true);
					up.DoUpdate();
				}
				else {
					JOptionPane.showMessageDialog(null,
							"Your HMS is up to date", "Updates",
							JOptionPane.INFORMATION_MESSAGE);
				}


			}
		});
		btnNewButton.setToolTipText("Check Updates");
		btnNewButton.setIcon(new ImageIcon(MainLogin.class
				.getResource("/icons/updates.png")));
		btnNewButton.setBounds(331, 29, 105, 61);
		contentPane.add(btnNewButton);

		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 446, 27);
		contentPane.add(menuBar);

		JMenu mnThemes = new JMenu("Themes");
		mnThemes.setFont(new Font("Tahoma", Font.BOLD, 12));
		menuBar.add(mnThemes);

		JMenuItem mntmSystemTheme = new JMenuItem("System Theme");
		mntmSystemTheme.setFont(new Font("Tahoma", Font.PLAIN, 12));
		mntmSystemTheme.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				updateLAF(1);
			}
		});
		mnThemes.add(mntmSystemTheme);

		JMenuItem mntmNewMenuItem = new JMenuItem("Blue Light");
		mntmNewMenuItem.setFont(new Font("Tahoma", Font.PLAIN, 12));
		mntmNewMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				updateLAF(2);
			}
		});
		mnThemes.add(mntmNewMenuItem);

		JMenuItem mntmNimbusTheme = new JMenuItem("Nimbus Theme");
		mntmNimbusTheme.setFont(new Font("Tahoma", Font.PLAIN, 12));
		mntmNimbusTheme.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				updateLAF(3);
			}
		});
		mnThemes.add(mntmNimbusTheme);

		JMenuItem mntmOrangeMetallic = new JMenuItem("Orange Metallic");
		mntmOrangeMetallic.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				updateLAF(4);
			}
		});
		mntmOrangeMetallic.setFont(new Font("Tahoma", Font.PLAIN, 12));
		mnThemes.add(mntmOrangeMetallic);

		JMenuItem mntmNewMenuItem_1 = new JMenuItem("Green Dream");
		mntmNewMenuItem_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				updateLAF(5);
			}
		});
		mntmNewMenuItem_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		mnThemes.add(mntmNewMenuItem_1);

		JMenu mnAboutHms = new JMenu("About HMS");
		mnAboutHms.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

			}
		});
		mnAboutHms.setFont(new Font("Tahoma", Font.BOLD, 12));
		menuBar.add(mnAboutHms);

		JMenuItem mntmAboutHms = new JMenuItem("About HMS");
		mntmAboutHms.setFont(new Font("Tahoma", Font.PLAIN, 12));
		mntmAboutHms.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				AboutHMS aboutHMS = new AboutHMS();
				aboutHMS.setModal(true);
				aboutHMS.setVisible(true);
			}
		});
		mnAboutHms.add(mntmAboutHms);

		JMenu mnHelp = new JMenu("Printer");
		mnHelp.setFont(new Font("Dialog", Font.BOLD, 12));
		menuBar.add(mnHelp);

		JMenuItem mntmPdfTroubleshooting = new JMenuItem("Printer Settings");
		mntmPdfTroubleshooting.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				PrinterSetting PrinterSetting=new PrinterSetting(); 
				PrinterSetting.setVisible(true);
				PrinterSetting.setModal(true);
			}
		});
		mntmPdfTroubleshooting.setFont(new Font("Dialog", Font.PLAIN, 12));
		mnHelp.add(mntmPdfTroubleshooting);

		readCounterFile();

	}

	public void runprogram() {
		//		System.out.println("nn"+System.getProperty("user.dir"));
		if (System.getProperty("java.version").toString().equals("null")) {
			ProcessBuilder pb = new ProcessBuilder(
					new File(System.getProperty("user.dir"), "jre/bin/java.exe")
					.toString(), "-jar", new File(System
							.getProperty("user.dir"), "HMS.jar")
					.toString());
			pb.directory(new File("" + System.getProperty("user.dir")));
			try {
				Process p = pb.start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {

			ProcessBuilder pb = new ProcessBuilder("java", "-jar",
					new File("HMS.jar")
					.toString());
			System.out.println("java -jar "+new File(System.getProperty("user.dir")+"/HMS.jar")
					.toString());
			pb.directory(new File("" + System.getProperty("user.dir")));
			try {
				Process p = pb.start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		dispose();
	}

	public void makeDirectory() {

		try {
			SmbFile dir = new SmbFile(mainDir + "/HMS/Patient");
			if (!dir.exists()) {
				dir.mkdirs();
				new SmbFile(mainDir + "/HMS/Doctor").mkdirs();
				new SmbFile(mainDir + "/HMS/opdslip").mkdirs();
				new SmbFile(mainDir + "/HMS/patientslip").mkdirs();
			}
		} catch (SmbException | MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void readCounterFile() {
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
			// Always close files.
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out
			.println("Unable to open file '" + fileName + "' \n" + ex);
		} catch (IOException ex) {
			System.out.println("Error reading file '" + fileName + "'");
			// Or we could just do this:
			// ex.printStackTrace();
		}
	}

	public static void updateLAF(int key) {

		try {
			switch (key) {
			case 1:
				UIManager.setLookAndFeel(UIManager
						.getSystemLookAndFeelClassName());
				for (Frame frame : Frame.getFrames()) {
					updateLAFRecursively(frame);
				}
				break;

			case 2:
				UIManager.setLookAndFeel(new SyntheticaBlueLightLookAndFeel());
				for (Frame frame : Frame.getFrames()) {
					updateLAFRecursively(frame);
				}
				break;

			case 3:
				for (LookAndFeelInfo info : UIManager
						.getInstalledLookAndFeels()) {
					if ("Nimbus".equals(info.getName())) {
						UIManager.setLookAndFeel(info.getClassName());
						break;
					}
				}
				for (Frame frame : Frame.getFrames()) {
					updateLAFRecursively(frame);
				}
				break;
			case 4:
				try {
					UIManager
					.setLookAndFeel(new SyntheticaOrangeMetallicLookAndFeel());
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case 5:
				try {
					UIManager
					.setLookAndFeel(new SyntheticaGreenDreamLookAndFeel());
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;

			default:
				break;
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				UIManager.setLookAndFeel(UIManager
						.getSystemLookAndFeelClassName());
			} catch (ClassNotFoundException | InstantiationException
					| IllegalAccessException | UnsupportedLookAndFeelException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			for (Frame frame : Frame.getFrames()) {
				updateLAFRecursively(frame);
			}
		}
	}

	public static void updateLAFRecursively(Window window) {
		for (Window childWindow : window.getOwnedWindows()) {
			updateLAFRecursively(childWindow);
		}
		SwingUtilities.updateComponentTreeUI(window);
	}
}
