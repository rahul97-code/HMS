package hms.patient.data_bundle;

import hms.opd.database.OPDDBConnection;
import hms.opd.gui.OPDBrowser;

import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JList;

import jcifs.smb.SmbFile;

public class PatientOPD extends JPanel {
	private JTable opdHistoryTable;
	
	public JFileChooser jfc = new JFileChooser();
	public File[] file;
	public File[] filepath;
	private HashMap itemsHashMap = new HashMap();
	private HashMap examHashMap = new HashMap();
	Vector files = new Vector();
	String[] data = new String[20];
	private JList list;
	private JButton saveButton;
	private JButton browseButton;
	String dest = "";

	/**
	 * Create the panel.
	 */
	public PatientOPD() {
		setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(20, 41, 316, 367);
		add(scrollPane);
		
		opdHistoryTable = new JTable();
		opdHistoryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		opdHistoryTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		opdHistoryTable.setFont(new Font("Tahoma", Font.PLAIN, 12));
		opdHistoryTable.getTableHeader().setReorderingAllowed(false);
		scrollPane.setViewportView(opdHistoryTable);
		opdHistoryTable.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
				{null, null},
			},
			new String[] {
				"OPD ID", "OPD Date"
			}
		));
		opdHistoryTable.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					@Override
					public void valueChanged(ListSelectionEvent e) {
						// TODO Auto-generated method stub
						try {
							int selectedRowIndex = opdHistoryTable
									.getSelectedRow();
							Object selectedObject = opdHistoryTable.getModel()
									.getValueAt(selectedRowIndex, 0);
							System.out.println("" + selectedObject);
							browseButton.setEnabled(true);
							saveButton.setEnabled(true);
							
						} catch (Exception e2) {
							// TODO: handle exception
						}
					}
				});
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBorder(new TitledBorder(null, "Files List",
						TitledBorder.LEADING, TitledBorder.TOP, new Font("Tahoma",
								Font.PLAIN, 12), null));
		panel.setBounds(345, 244, 219, 164);
		add(panel);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(6, 16, 197, 137);
		panel.add(scrollPane_1);
		
		list = new JList();
		list.setFont(new Font("Tahoma", Font.PLAIN, 11));
		scrollPane_1.setViewportView(list);
		
		browseButton = new JButton("Browse File");
		browseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				file = getMultipleFile();
				filepath = file;
				
				final String[] values = new String[file.length];
				for (int i = 0; i < file.length; i++) {
					System.out.println("" + file[i].getPath());
					values[i] = file[i].getName();
					files.add(file[i].getName().toString());
				}
				list.setListData(files);
			}
		});
		browseButton.setFont(new Font("Tahoma", Font.PLAIN, 13));
		browseButton.setEnabled(false);
		browseButton.setBounds(366, 161, 120, 45);
		add(browseButton);
		
		saveButton = new JButton("Save");
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				
			}
		});
		saveButton.setToolTipText("Double Click to Save Report");
		saveButton.setEnabled(false);
		saveButton.setBounds(366, 105, 120, 45);
		
		saveButton.setToolTipText("Double Click to Save Report");
		saveButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {

//				if (arg0.getClickCount() == 2) {
//					if (files.size() > 0) {
//					//	dest = makeDirectory(p_id, exam_id);
//						try {
//							for (int i = 0; i < filepath.length; i++) {
//								try {
//									copyFileUsingJava7Files(filepath[i].getPath(),
//											dest + "/" + filepath[i].getName());
//								} catch (IOException e) {
//									// TODO Auto-generated catch block
//									e.printStackTrace();
//								}
//							}
//						} catch (Exception e) {
//							// TODO: handle exception
//						}
//						final File sourceLocation = new File(System.getProperty("user.dir")+"/localTemp");
//						
//						if(sourceLocation.isDirectory())
//						{
//							try {
//
//								String[] children = sourceLocation.list();
//								for (int i = 0; i < children.length; i++) {
//								
//									copyFileUsingJava7Files(new File(
//											sourceLocation, children[i])
//											.toString(), dest + "/"
//											+ children[i]);
//
//								}
//							} catch (IOException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}
//						}
//						JOptionPane.showMessageDialog(null,
//								"Files Attached Successfully ", "Data Save",
//								JOptionPane.INFORMATION_MESSAGE);
//						files.clear();
//						list.setListData(files);
//					}
//		
					browseButton.setEnabled(false);
					saveButton.setEnabled(false);
					
//				}
			}
		});
		
		add(saveButton);
		opdHistoryTable.getColumnModel().getColumn(0).setPreferredWidth(150);
		opdHistoryTable.getColumnModel().getColumn(0).setMinWidth(149);
		opdHistoryTable.getColumnModel().getColumn(1).setPreferredWidth(160);
		opdHistoryTable.getColumnModel().getColumn(1).setMinWidth(160);
		opdHistoryTable .getSelectionModel().addListSelectionListener(new ListSelectionListener() {
				@Override
				public void valueChanged(ListSelectionEvent e) {
					// TODO Auto-generated method stub
					try {
					int selectedRowIndex = opdHistoryTable.getSelectedRow();
					selectedRowIndex=opdHistoryTable.convertRowIndexToModel(selectedRowIndex);
					Object selectedObject = opdHistoryTable.getModel().getValueAt(selectedRowIndex, 0);
					
					} catch (Exception e2) {
						// TODO: handle exception
					}
				}
			});

	}
	public void populateOPDHistoryTable(String patientID,String dateFrom,String dateTo)
	{
		
		try {
            OPDDBConnection db=new OPDDBConnection();
            ResultSet rs = db.retrieveAllDataPatientID(patientID,dateFrom,dateTo);
            
           // System.out.println("Table: " + rs.getMetaData().getTableName(1));
            int NumberOfColumns = 0, NumberOfRows = 0;
            NumberOfColumns = rs.getMetaData().getColumnCount();
            
            while(rs.next()){
            NumberOfRows++;
            }
            rs = db.retrieveAllDataPatientID(patientID,dateFrom,dateTo);
            
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
    				"OPD No.","OPD Date"
    			}) {
    			@Override
    			public boolean isCellEditable(int row, int column) {
    				return false;// This causes all cells to be not editable
    			}
    		};
    		opdHistoryTable.setModel(model); 
    		opdHistoryTable.getColumnModel().getColumn(0).setPreferredWidth(150);
    		opdHistoryTable.getColumnModel().getColumn(0).setMinWidth(149);
    		opdHistoryTable.getColumnModel().getColumn(1).setPreferredWidth(165);
    		opdHistoryTable.getColumnModel().getColumn(1).setMinWidth(165);
    		db.closeConnection();
        } catch (SQLException ex) {
            Logger.getLogger(OPDBrowser.class.getName()).log(Level.SEVERE, null, ex);
        }
	}
	
	public File[] getMultipleFile() {
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"JPG & PNG Images", "jpg", "png");
		// jfc.setFileFilter(filter);
		jfc.setMultiSelectionEnabled(true);
		jfc.setDialogTitle("Open File");
		if (jfc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			return jfc.getSelectedFiles();
		} else {
			return null;
		}
	}
	private static void copyFileUsingJava7Files(String source, String dest)
			throws IOException {

		SmbFile remoteFile = new SmbFile(dest);
		OutputStream os = remoteFile.getOutputStream();
		InputStream is = new FileInputStream(new File(source));
		int bufferSize = 5096;
		byte[] b = new byte[bufferSize];
		int noOfBytes = 0;
		while ((noOfBytes = is.read(b)) != -1) {
			os.write(b, 0, noOfBytes);
		}
		os.close();
		is.close();
	}
	public JButton getSaveButton() {
		return saveButton;
	}
	public JButton getBrowseButton() {
		return browseButton;
	}
}
