package hms.Printer;

import java.awt.Container;
import java.awt.EventQueue;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.swing.ButtonGroup;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JToggleButton;
import javax.swing.WindowConstants;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.print.PrinterJob;

import javax.swing.JRadioButton;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.beans.PropertyChangeEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.pdfbox.pdmodel.PDDocument;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;

import hms.patient.slippdf.HeaderAndFooter;

import javax.swing.event.ListSelectionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JCheckBox;

public class PrinterSetting extends JDialog{

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PrinterSetting window = new PrinterSetting();
					window.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	private String SelectedPrinterIndex="";
	private JList list;
	PrintService[] ps ;
	private JTextField PrinterTF;
	private JLabel label;
	String[] data = new String[15];
	private JButton btnNewButton;
	private JCheckBox IPDDischrChcBx;
	private JCheckBox IPDExmSlipChcBx;
	private JCheckBox OPDExmSlipChcBx;
	private JCheckBox OPDSlipChcBx;
	private JCheckBox StrToDeptSlipChcBx;
	private JCheckBox InvoiceBillChkBx;
	private JCheckBox InDoorSlipChcBx;
	private JCheckBox OutDoorSlipChcBx;


	public PrinterSetting() {
		setBounds(100, 100, 450, 304);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(null);
		setTitle("*** Printer Settings ***");

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(12, 0, 426, 255);
		getContentPane().add(tabbedPane);

		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Printers", null, panel_1, null);
		panel_1.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 12, 385, 138);
		panel_1.add(scrollPane);

		list = new JList();
		list.addListSelectionListener(new ListSelectionListener() {

			public void valueChanged(ListSelectionEvent e) {
				btnNewButton.setEnabled(true);
				SelectedPrinterIndex=list.getSelectedIndex()+"";
				//				System.out.println(SelectedPrinterIndex);
			}
		});

		//		list.setVisibleRowCount(10);
		list.setFont(new Font("Dialog", Font.ITALIC, 10));
		scrollPane.setViewportView(list);

		PrinterTF = new JTextField();
		PrinterTF.setFont(new Font("Dialog", Font.ITALIC, 12));
		PrinterTF.setEditable(false);
		PrinterTF.setBounds(12, 155, 385, 23);
		panel_1.add(PrinterTF);
		PrinterTF.setColumns(10);

		btnNewButton = new JButton("Set As Default");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					WriteFile(0,SelectedPrinterIndex);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				readData();
			}
		});
		btnNewButton.setEnabled(false);
		btnNewButton.setFont(new Font("Dialog", Font.ITALIC, 12));
		btnNewButton.setBounds(240, 190, 157, 25);
		panel_1.add(btnNewButton);

		label = new JLabel("default printer...");
		label.setFont(new Font("Dialog", Font.ITALIC, 10));
		label.setBounds(12, 172, 193, 23);
		panel_1.add(label);

		JPanel panel = new JPanel();
		panel.setBounds(12, 0, 426, 247);
		panel.setLayout(null);
		tabbedPane.addTab("AutoPrint Slips", panel);

		JLabel lblOutdoorSlipPrint = new JLabel("1. OutDoor Patient Slip ");
		lblOutdoorSlipPrint.setFont(new Font("Dialog", Font.ITALIC, 14));
		lblOutdoorSlipPrint.setBounds(28, 12, 187, 15);
		panel.add(lblOutdoorSlipPrint);

		JLabel lblIndoorSlip = new JLabel("2. InDoor Patient Slip ");
		lblIndoorSlip.setFont(new Font("Dialog", Font.ITALIC, 14));
		lblIndoorSlip.setBounds(28, 39, 199, 15);
		panel.add(lblIndoorSlip);

		JLabel lblInvoiceBills = new JLabel("3. Invoice Bills ");
		lblInvoiceBills.setFont(new Font("Dialog", Font.ITALIC, 14));
		lblInvoiceBills.setBounds(28, 66, 141, 15);
		panel.add(lblInvoiceBills);

		JLabel lblDepartmentsSlips = new JLabel("4. Store To Departments Slips ");
		lblDepartmentsSlips.setFont(new Font("Dialog", Font.ITALIC, 14));
		lblDepartmentsSlips.setBounds(28, 93, 217, 15);
		panel.add(lblDepartmentsSlips);

		JLabel lblOpdSlips = new JLabel("5. OPD Slips ");
		lblOpdSlips.setFont(new Font("Dialog", Font.ITALIC, 14));
		lblOpdSlips.setBounds(28, 120, 166, 15);
		panel.add(lblOpdSlips);

		JLabel lblOpdSlips_1 = new JLabel("6. OPD Exam Slips ");
		lblOpdSlips_1.setFont(new Font("Dialog", Font.ITALIC, 14));
		lblOpdSlips_1.setBounds(28, 147, 166, 15);
		panel.add(lblOpdSlips_1);

		JLabel lblOpdSlips_2 = new JLabel("7. IPD Exam Slips ");
		lblOpdSlips_2.setFont(new Font("Dialog", Font.ITALIC, 14));
		lblOpdSlips_2.setBounds(28, 174, 166, 15);
		panel.add(lblOpdSlips_2);

		JLabel lblOpdSlips_2_1 = new JLabel("8. IPD Discharge Bill ");
		lblOpdSlips_2_1.setFont(new Font("Dialog", Font.ITALIC, 14));
		lblOpdSlips_2_1.setBounds(28, 200, 166, 15);
		panel.add(lblOpdSlips_2_1);

		OutDoorSlipChcBx = new JCheckBox("  Y/N");
		OutDoorSlipChcBx.setFont(new Font("Dialog", Font.ITALIC, 12));
		OutDoorSlipChcBx.setBounds(265, 8, 129, 23);
		panel.add(OutDoorSlipChcBx);
		OutDoorSlipChcBx.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					WriteFile(1,OutDoorSlipChcBx.isSelected()+"");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				readData();
			}
		});

		InDoorSlipChcBx = new JCheckBox("  Y/N");
		InDoorSlipChcBx.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					WriteFile(2,InDoorSlipChcBx.isSelected()+"");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				readData();
			}
		});
		InDoorSlipChcBx.setFont(new Font("Dialog", Font.ITALIC, 12));
		InDoorSlipChcBx.setBounds(265, 35, 129, 23);
		panel.add(InDoorSlipChcBx);

		InvoiceBillChkBx = new JCheckBox("  Y/N");
		InvoiceBillChkBx.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					WriteFile(3,InvoiceBillChkBx.isSelected()+"");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				readData();
			}
		});
		InvoiceBillChkBx.setFont(new Font("Dialog", Font.ITALIC, 12));
		InvoiceBillChkBx.setBounds(265, 62, 129, 23);
		panel.add(InvoiceBillChkBx);

		StrToDeptSlipChcBx = new JCheckBox("  Y/N");
		StrToDeptSlipChcBx.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					WriteFile(4,StrToDeptSlipChcBx.isSelected()+"");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				readData();
			}
		});
		StrToDeptSlipChcBx.setFont(new Font("Dialog", Font.ITALIC, 12));
		StrToDeptSlipChcBx.setBounds(265, 89, 129, 23);
		panel.add(StrToDeptSlipChcBx);

		OPDSlipChcBx = new JCheckBox("  Y/N");
		OPDSlipChcBx.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					WriteFile(5,OPDSlipChcBx.isSelected()+"");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				readData();
			}
		});
		OPDSlipChcBx.setFont(new Font("Dialog", Font.ITALIC, 12));
		OPDSlipChcBx.setBounds(265, 116, 129, 23);
		panel.add(OPDSlipChcBx);

		OPDExmSlipChcBx = new JCheckBox("  Y/N");
		OPDExmSlipChcBx.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					WriteFile(6,OPDExmSlipChcBx.isSelected()+"");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				readData();
			}
		});
		OPDExmSlipChcBx.setFont(new Font("Dialog", Font.ITALIC, 12));
		OPDExmSlipChcBx.setBounds(265, 143, 129, 23);
		panel.add(OPDExmSlipChcBx);

		IPDExmSlipChcBx = new JCheckBox("  Y/N");
		IPDExmSlipChcBx.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					WriteFile(7,IPDExmSlipChcBx.isSelected()+"");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				readData();
			}
		});
		IPDExmSlipChcBx.setFont(new Font("Dialog", Font.ITALIC, 12));
		IPDExmSlipChcBx.setBounds(265, 170, 129, 23);
		panel.add(IPDExmSlipChcBx);

		IPDDischrChcBx = new JCheckBox("  Y/N");
		IPDDischrChcBx.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					WriteFile(8,IPDDischrChcBx.isSelected()+"");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				readData();
			}
		});
		IPDDischrChcBx.setFont(new Font("Dialog", Font.ITALIC, 12));
		IPDDischrChcBx.setBounds(265, 196, 129, 23);
		panel.add(IPDDischrChcBx);

		GetPrinters();
		readData();
	}
	public void WriteFile(int lineNumber,String content) throws IOException {
		File myFile = new File("printer.settings");
		ArrayList<String> fileContent = new ArrayList<String>();
		Scanner myReader = new Scanner(myFile);
		while (myReader.hasNextLine()) {
			fileContent.add(myReader.nextLine());
		}
		myReader.close();
		fileContent.remove(lineNumber);
		fileContent.add(lineNumber, content);
		// Writes the new content to file
		FileWriter myWriter = new FileWriter("printer.settings");
		for (String eachLine : fileContent) {
			myWriter.write(eachLine + "\n");
		}
		myWriter.close();	
	}
	public void readData() {
		FileInputStream fis = null;
		int counter = 0;
		try {
			File file = new File("printer.settings");
			if(!file.exists()) {
				file.createNewFile();
				WriteDefaultContent();
			}
			fis = new FileInputStream(file);
			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fis);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;

			// Read File Line By Line
			while ((strLine = br.readLine()) != null) {

				data[counter] = strLine;
				counter++;
				//				System.out.println(strLine);
			}
			in.close();
		} catch (Exception ex) {
			System.out.print(""+ex);
		} finally {
			try {
				if (null != fis)
					fis.close();
			} catch (IOException ex) {
			}
		}
		PrinterTF.setText(ps[Integer.parseInt(data[0])]+"");
		OutDoorSlipChcBx.setSelected(Boolean.valueOf(data[1]));
		InDoorSlipChcBx.setSelected(Boolean.valueOf(data[2]));
		InvoiceBillChkBx.setSelected(Boolean.valueOf(data[3]));
		StrToDeptSlipChcBx.setSelected(Boolean.valueOf(data[4]));
		OPDSlipChcBx.setSelected(Boolean.valueOf(data[5]));
		OPDExmSlipChcBx.setSelected(Boolean.valueOf(data[6]));
		IPDExmSlipChcBx.setSelected(Boolean.valueOf(data[7]));
		IPDDischrChcBx.setSelected(Boolean.valueOf(data[8]));
	}
	private void WriteDefaultContent() {
		// TODO Auto-generated method stub
		String fileName="printer.settings";
		String content="0\n"
				+ "true\n"
				+ "true\n"
				+ "true\n"
				+ "true\n"
				+ "true\n"
				+ "true\n"
				+ "true\n"
				+ "true";
		try {
			FileWriter writer = new FileWriter(fileName);
			writer.write(content);
			writer.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	
	}
	
	public void GetPrinters() {
		ps= PrinterJob.lookupPrintServices();
		list.setListData(ps);
	}
}