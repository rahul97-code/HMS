package hms.doctor.gui;

import hms.doctor.database.DoctorDBConnection;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileOutputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.gtranslate.Language;
import com.gtranslate.Translator;

public class EditDoctor extends JDialog {

	private JPanel contentPane;
	private JTextField doctorNameTB;
	private JTextField userNameTB;
	private JPasswordField passwordTB;
	private JPasswordField confPasswordTB;
	private JTextField opdRoomTB;
	private JTextField telephoneTB;
	String docName = "", docID = "", docUserName = "", docPassword = "",
			docSpecialization = "", docOPDRoom = "", docTelephone = "",
			docAddress = "", doctorAchievement = "",indexSTR="",examDiscountSTR="0",examDaySTR="0",opdDaySTR="0";
	private JTextField specializationTB;
	private HashMap allDoctors = new HashMap();
	String[] data = new String[20];
	String mainDir = "";
	private JTextField indexTF;
	private JTextField examDiscountTB;
	private JTextField examsDayET;
	private JTextField opdDayET;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					EditDoctor frame = new EditDoctor("1");
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
	public EditDoctor(String doctorId) {
		setTitle("Edit Doctor");
		setIconImage(Toolkit.getDefaultToolkit().getImage(EditDoctor.class.getResource("/icons/rotaryLogo.png")));
		setResizable(false);
		setBounds(100, 100, 667, 374);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		getAllDoctors();
		readFile();
		docID=doctorId;
		getDoctorDetail(doctorId);
		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "Doctor ID : "+doctorId,
				TitledBorder.CENTER, TitledBorder.TOP, new Font("Tahoma",
						Font.PLAIN, 14), null));
		panel_3.setBounds(0, 0, 649, 331);
		contentPane.add(panel_3);
		panel_3.setLayout(null);

		JPanel panel_1 = new JPanel();
		panel_1.setBounds(6, 25, 321, 292);
		panel_3.add(panel_1);
		panel_1.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panel_1.setLayout(null);

		JLabel lblDoctorName = new JLabel("Doctor Name :");
		lblDoctorName.setBounds(6, 19, 93, 14);
		panel_1.add(lblDoctorName);
		lblDoctorName.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblUserName = new JLabel("User Name :");
		lblUserName.setBounds(6, 56, 81, 14);
		panel_1.add(lblUserName);
		lblUserName.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblPassword = new JLabel("Password :");
		lblPassword.setBounds(6, 90, 81, 14);
		panel_1.add(lblPassword);
		lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblConfirmPassword = new JLabel("Confirm Password :");
		lblConfirmPassword.setBounds(6, 125, 119, 14);
		panel_1.add(lblConfirmPassword);
		lblConfirmPassword.setFont(new Font("Tahoma", Font.PLAIN, 12));

		doctorNameTB = new JTextField(docName);
		doctorNameTB.setBounds(135, 16, 180, 25);
		panel_1.add(doctorNameTB);
		doctorNameTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		doctorNameTB.setColumns(10);

		userNameTB = new JTextField(docUserName);
		userNameTB.setEditable(false);
		userNameTB.setBounds(135, 53, 180, 25);
		panel_1.add(userNameTB);
		userNameTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		userNameTB.setColumns(10);

		passwordTB = new JPasswordField(docPassword);
		passwordTB.setEditable(false);
		passwordTB.setBounds(135, 87, 180, 25);
		panel_1.add(passwordTB);
		passwordTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		passwordTB.setColumns(10);

		confPasswordTB = new JPasswordField(docPassword);
		confPasswordTB.setEditable(false);
		confPasswordTB.setBounds(135, 122, 180, 25);
		panel_1.add(confPasswordTB);
		confPasswordTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		confPasswordTB.setColumns(10);

		JPanel panel_4 = new JPanel();
		panel_4.setBorder(new TitledBorder(null, "Achievement :",
				TitledBorder.LEADING, TitledBorder.TOP, new Font("Tahoma",
						Font.PLAIN, 12), null));
		panel_4.setBounds(6, 224, 305, 68);
		panel_1.add(panel_4);
		panel_4.setLayout(null);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(10, 17, 289, 47);
		panel_4.add(scrollPane_1);

		final JTextArea achievementTA = new JTextArea(doctorAchievement);
		achievementTA.setFont(new Font("Tahoma", Font.PLAIN, 12));
		achievementTA.setLineWrap(true);
		achievementTA.setRows(3);
		scrollPane_1.setViewportView(achievementTA);
		
		examsDayET = new JTextField(examDaySTR);
		examsDayET.setFont(new Font("Tahoma", Font.PLAIN, 12));
		examsDayET.setColumns(10);
		examsDayET.setBounds(135, 161, 180, 25);
		panel_1.add(examsDayET);
		
		JLabel lblExams = new JLabel("Exams/Day :");
		lblExams.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblExams.setBounds(6, 164, 93, 14);
		panel_1.add(lblExams);
		
		opdDayET = new JTextField(opdDaySTR);
		opdDayET.setFont(new Font("Tahoma", Font.PLAIN, 12));
		opdDayET.setColumns(10);
		opdDayET.setBounds(135, 197, 180, 25);
		panel_1.add(opdDayET);
		
		JLabel lblOpdday = new JLabel("OPD/Day :");
		lblOpdday.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblOpdday.setBounds(6, 200, 93, 14);
		panel_1.add(lblOpdday);

		JPanel panel_2 = new JPanel();
		panel_2.setBounds(343, 25, 300, 236);
		panel_3.add(panel_2);
		panel_2.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "", TitledBorder.LEADING,
				TitledBorder.TOP, new Font("Tahoma", Font.PLAIN, 14), null));
		panel_2.setLayout(null);

		JLabel lblOpdRoom = new JLabel("OPD Room No.:");
		lblOpdRoom.setBounds(6, 49, 104, 14);
		panel_2.add(lblOpdRoom);
		lblOpdRoom.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblTelephone = new JLabel("Telephone No. :");
		lblTelephone.setBounds(6, 80, 104, 18);
		panel_2.add(lblTelephone);
		lblTelephone.setFont(new Font("Tahoma", Font.PLAIN, 12));

		opdRoomTB = new JTextField(docOPDRoom);
		opdRoomTB.setBounds(120, 44, 165, 25);
		panel_2.add(opdRoomTB);
		opdRoomTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		opdRoomTB.setColumns(10);

		telephoneTB = new JTextField(docTelephone);
		telephoneTB.setBounds(120, 77, 165, 25);
		panel_2.add(telephoneTB);
		telephoneTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		telephoneTB.setColumns(10);

		JPanel panel = new JPanel();
		panel.setBounds(16, 172, 269, 53);
		panel_2.add(panel);
		panel.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "Addresss :",
				TitledBorder.LEADING, TitledBorder.TOP, new Font("Tahoma",
						Font.PLAIN, 12), null));
		panel.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 17, 257, 36);
		panel.add(scrollPane);

		final JTextArea addressTA = new JTextArea(docAddress);
		addressTA.setFont(new Font("Tahoma", Font.PLAIN, 12));
		addressTA.setLineWrap(true);
		addressTA.setRows(4);
		scrollPane.setViewportView(addressTA);

		specializationTB = new JTextField(docSpecialization);
		specializationTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		specializationTB.setColumns(10);
		specializationTB.setBounds(120, 11, 165, 25);
		panel_2.add(specializationTB);

		JLabel label = new JLabel("Specialization :");
		label.setFont(new Font("Tahoma", Font.PLAIN, 12));
		label.setBounds(6, 14, 95, 18);
		panel_2.add(label);
		
		indexTF = new JTextField(indexSTR);
		indexTF.setFont(new Font("Tahoma", Font.PLAIN, 12));
		indexTF.setColumns(10);
		indexTF.setBounds(120, 113, 165, 25);
		panel_2.add(indexTF);
		
		JLabel lblIndex = new JLabel("Index :");
		lblIndex.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblIndex.setBounds(6, 116, 104, 18);
		panel_2.add(lblIndex);
		
		examDiscountTB = new JTextField(examDiscountSTR);
		examDiscountTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		examDiscountTB.setColumns(10);
		examDiscountTB.setBounds(120, 149, 165, 25);
		panel_2.add(examDiscountTB);
		
		JLabel lblDiscountExam = new JLabel("Discount Exam :");
		lblDiscountExam.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblDiscountExam.setBounds(6, 152, 104, 18);
		panel_2.add(lblDiscountExam);

		JButton saveDoctorBT = new JButton("Save");
		saveDoctorBT.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				docName = doctorNameTB.getText().toString();
				docPassword = passwordTB.getText().toString();
				docUserName = userNameTB.getText().toString();
				docSpecialization = specializationTB.getText().toString();
				docOPDRoom = opdRoomTB.getText().toString();
				docTelephone = telephoneTB.getText().toString();
				doctorAchievement = achievementTA.getText().toString();
				docAddress = addressTA.getText().toString();
				indexSTR = indexTF.getText().toString();
				examDaySTR = examsDayET.getText().toString();
				opdDaySTR = opdDayET.getText().toString();
				Object getAlready = null;
				try {
					getAlready = allDoctors.get(docUserName);
				} catch (Exception e) {
					// TODO: handle exception
				}
				if (examDiscountTB.getText().toString().equals("")) {
					
					examDiscountTB.setText("0");
				
				} 
				if (docName.equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please enter Doctor name", "Input Error",
							JOptionPane.INFORMATION_MESSAGE);
					doctorNameTB.requestFocus();
					return;
				}  else if (docUserName.equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please Enter User Name", "Input Error",
							JOptionPane.INFORMATION_MESSAGE);
					userNameTB.requestFocus();
					return;

				} else if (docPassword.equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please enter password", "Input Error",
							JOptionPane.INFORMATION_MESSAGE);
					passwordTB.requestFocus();
					return;

				} else if (!confPasswordTB.getText().toString()
						.equals(docPassword)) {
					JOptionPane.showMessageDialog(null,
							"Password does not match", "Input Error",
							JOptionPane.INFORMATION_MESSAGE);
					confPasswordTB.requestFocus();
					return;

				} else if (docSpecialization.equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please enter Specialization", "Input Error",
							JOptionPane.INFORMATION_MESSAGE);
					specializationTB.requestFocus();
					return;

				} else if (docOPDRoom.equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please enter OPD Room", "Input Error",
							JOptionPane.INFORMATION_MESSAGE);
					opdRoomTB.requestFocus();
					return;

				} else if (docTelephone.equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please enter telephone number", "Input Error",
							JOptionPane.INFORMATION_MESSAGE);
					telephoneTB.requestFocus();
					return;

				}  else if (indexSTR.equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please enter Index", "Input Error",
							JOptionPane.INFORMATION_MESSAGE);
					indexTF.requestFocus();
					return;

				}  else if (examDaySTR.equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please enter Exam/Days", "Input Error",
							JOptionPane.INFORMATION_MESSAGE);
					indexTF.requestFocus();
					return;

				}  else if (opdDayET.equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please enter OPD/Day", "Input Error",
							JOptionPane.INFORMATION_MESSAGE);
					indexTF.requestFocus();
					return;

				}else {
					data[0] = docName;
					data[1] = docUserName;
					data[2] = docPassword;
					data[3] = docSpecialization;
					data[4] = docOPDRoom;
					data[5] = docTelephone;
					data[6] = docAddress;
					data[7] = doctorAchievement;
					data[8] = indexSTR;
					data[9] = examDiscountTB.getText().toString();
					data[10] = examsDayET.getText().toString();
					data[11] = opdDayET.getText().toString();
					data[12] = docID;
					DoctorDBConnection dbConnection = new DoctorDBConnection();
					try {
						dbConnection.updateData(data);
						//writeDoctorName();
						JOptionPane.showMessageDialog(null,
								"Doctor add successfully", "Data save",
								JOptionPane.INFORMATION_MESSAGE);
						dispose();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						dbConnection.closeConnection();
					}
					dbConnection.closeConnection();
				}

			}
		});
		saveDoctorBT.setIcon(new ImageIcon(EditDoctor.class
				.getResource("/icons/operation_dialog.png")));
		saveDoctorBT.setFont(new Font("Tahoma", Font.PLAIN, 12));
		saveDoctorBT.setBounds(343, 272, 143, 45);
		panel_3.add(saveDoctorBT);

		JButton closeBT = new JButton("Cancel");
		closeBT.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		closeBT.setIcon(new ImageIcon(EditDoctor.class
				.getResource("/icons/close_button.png")));
		closeBT.setFont(new Font("Tahoma", Font.PLAIN, 12));
		closeBT.setBounds(491, 272, 143, 45);
		panel_3.add(closeBT);
	}


	public void getAllDoctors() {
		DoctorDBConnection dbConnection = new DoctorDBConnection();
		ResultSet resultSet = dbConnection.retrieveAllData();
		try {
			while (resultSet.next()) {
				allDoctors.put(resultSet.getObject(5).toString(), resultSet
						.getObject(6).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
	}
	
	public void getDoctorDetail(String DocId) {
	
		DoctorDBConnection dbConnection = new DoctorDBConnection();
		ResultSet resultSet = dbConnection.retrieveDataWithID(DocId);
		try {
			while (resultSet.next()) {
				docName = resultSet.getObject(1).toString();
				docUserName=resultSet.getObject(2)
						.toString();
				docPassword=resultSet.getObject(3)
						.toString();
				docSpecialization=resultSet.getObject(4)
						.toString();
				docOPDRoom=resultSet.getObject(5)
						.toString();
				docTelephone=resultSet.getObject(6)
						.toString();
				docAddress=resultSet.getObject(7)
						.toString();
				doctorAchievement=resultSet.getObject(8)
						.toString();
				indexSTR=resultSet.getObject(9)+"";
				examDiscountSTR=resultSet.getObject(10)+"";
				examDaySTR=resultSet.getObject(11)+"";
				opdDaySTR=resultSet.getObject(12)+"";
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
	}
	public static String translator(String str)
	{

		  Translator translate = Translator.getInstance();
	   String translatedText = null;
	  String textInserted = ""+str;
	  
	  try {
	      try {
	          translatedText = translate.translate(
	              textInserted,
	              (String)Language.class.getField("ENGLISH").get(null),
	              (String)Language.class.getField("HINDI").get(null));
	          
	      } catch (IllegalArgumentException ex) {
	       //   showError(ex.toString());
	         
	      } catch (IllegalAccessException ex) {
	    	  
	       //   showError(ex.toString());
	        
	      }
	  } catch (NoSuchFieldException ex) {
	    //  showError(ex.toString());
	     
	  } catch (SecurityException ex) {
	    //  showError(ex.toString());
	  }
	  
	  String string2 = null;
	  try {
		   byte[] utf8 = translatedText.getBytes("UTF-8"); 
		string2 = new String(utf8, "UTF-8");
		System.out.print("sdasddasds   "+string2);
	} catch (UnsupportedEncodingException e2) {
		// TODO Auto-generated catch block
		e2.printStackTrace();
	}
	  System.out.print("aa2"+translatedText+"2aa");
	  if(!string2.equals(""))
	  {
		  return string2;
	  }
	  else {
		return str;
	}

	 
	}
	public void writeDoctorName()
	{
		
			try {
				SmbFile dir = new SmbFile(mainDir + "/HMS/Doctor/DATA");
				if (!dir.exists())
					dir.mkdirs();
				
			} catch (SmbException | MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		 SmbFileOutputStream fileOut = null;        
	        try {
	            // TODO code application logic here
	            Workbook wb = new HSSFWorkbook();
	            CreationHelper createHelper = wb.getCreationHelper();
	            Sheet sheet2 = wb.createSheet("second sheet");
	            
	            Row row = sheet2.createRow((short) 0);
	            
	            // Create a cell and put a value in it.
	            Cell cell = row.createCell(0);
	            cell.setCellValue(1);
	            String txt = new String();
	        
	            // Or do it on one line.
	            row.createCell(1).setCellValue("Sukhpal Saini");
	          //  row.createCell(2).setCellValue(createHelper.createRichTextString(translator(docName+" ("+docSpecialization+")")));
	            row.createCell(2).setCellValue(createHelper.createRichTextString(translator(docName+"")));
	            row.createCell(3).setCellValue(true);
	            
	            fileOut = new SmbFileOutputStream(mainDir + "/HMS/Doctor/DATA/"+docID+".mdi");
	            wb.write(fileOut);
	            fileOut.close();
	            
	        } catch (IOException ex) {
	            Logger.getLogger(NewDoctor.class.getName()).log(Level.SEVERE, null, ex);
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
}
