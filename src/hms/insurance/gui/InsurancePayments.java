package hms.insurance.gui;

import hms.exam.database.ExamDBConnection;
import hms.main.DateFormatChange;
import hms.opd.database.OPDDBConnection;
import hms.patient.slippdf.InsuranceOpdPdfSlip;
import hms1.ipd.database.IPDDBConnection;

import java.awt.Color;
import java.awt.Component;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import com.itextpdf.text.DocumentException;
import com.toedter.calendar.JDateChooser;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTabbedPane;

public class InsurancePayments extends JDialog {

	public JPanel contentPane;
	private JTable ipdTable;
	private JTable opdTable;
	private JTable examsTable;
	ButtonGroup agegroup = new ButtonGroup();
	DateFormatChange dateFormat = new DateFormatChange();
	private JDateChooser dateToDC;
	private JDateChooser dateFromDC;
	String dateFrom,dateTo;
	private JTextField searchPatientNameTB;

	
	Vector originalTableModel;
	Vector originalTableModel1;
	Vector originalTableModel2;
	String userid="";
	private JTabbedPane tabbedPane;
	private JTable cashTable;
	private JTable cashlessTable;
	private JTable cashOPDTable;
	private JTable cashlessOPDTable;
	private JTable cashExamTable;
	private JTable cashlessExamTable;
	Vector<String> ITEM_NAME = new Vector<String>();
	Vector<String> ITEM_DESC = new Vector<String>();
	Vector<String> DATE = new Vector<String>();
	Vector<String> TYPE = new Vector<String>();
	Vector<String> ITEM_ID = new Vector<String>();
	Vector<String> PAGE_NO = new Vector<String>();
	Vector<String> MRP = new Vector<String>();
	Vector<Double> PER_ITEM_PRICE = new Vector<Double>();
	Vector<String> QTY = new Vector<String>();
	Vector<Double> AMOUNT = new Vector<Double>();
	Vector<String> BATCH = new Vector<String>();
	Vector<String> EXPENSE_ID = new Vector<String>();
	Vector<String> EXPIRY = new Vector<String>();
	private List<Vector> originalCashTableData = new ArrayList<>();
	private List<Vector> originalCashlessTableData = new ArrayList<>();
	private List<Vector> originalOPDCashTableData = new ArrayList<>();
	private List<Vector> originalOPDCashlessTableData = new ArrayList<>();
	private List<Vector> originalEXAMCashTableData = new ArrayList<>();
	private List<Vector> originalEXAMCashlessTableData = new ArrayList<>();

	public static void main(String[] arg)
	{
		InsurancePayments billBrowser=new InsurancePayments("","");
		billBrowser.setVisible(true);

	}
	/**
	 * Create the frame.
	 */
	public InsurancePayments(final String Userid,String type) {
		//		this.userid=Userid;
		setResizable(false);
		//		System.out.println(Userid+"userid");
		setTitle("Insurance Payments");
		setIconImage(Toolkit.getDefaultToolkit().getImage(InsurancePayments.class.getResource("/icons/rotaryLogo.png")));
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 950, 524);
		contentPane = new JPanel();
		setModal(true);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);



		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(31, 12, 907, 74);
		contentPane.add(panel);
		panel.setLayout(null);
		searchPatientNameTB = new JTextField();
		searchPatientNameTB.setBounds(12, 36, 178, 28);
		panel.add(searchPatientNameTB);
		searchPatientNameTB.getDocument().addDocumentListener(new DocumentListener() {
		    @Override
		    public void insertUpdate(DocumentEvent e) {
		        handleSearch();
		    }

		    @Override
		    public void removeUpdate(DocumentEvent e) {
		        handleSearch();
		    }

		    @Override
		    public void changedUpdate(DocumentEvent e) {
		        handleSearch();
		    }

		    private void handleSearch() {
		        String str = searchPatientNameTB.getText();
		        int i = tabbedPane.getSelectedIndex();

		        if (i == 0) {
		            searchTableContents(str, cashTable, originalCashTableData);
		            searchTableContents(str, cashlessTable, originalCashlessTableData);
		        } else if (i == 1) {
		            searchTableContents(str, cashOPDTable, originalOPDCashTableData);
		            searchTableContents(str, cashlessOPDTable, originalOPDCashlessTableData);
		        } else if (i == 2) {
		            searchTableContents(str, cashExamTable, originalEXAMCashTableData);
		            searchTableContents(str, cashlessExamTable, originalEXAMCashlessTableData);
		        }
		    }

		});

		searchPatientNameTB.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblSelectDisease = new JLabel("Search Patient");
		lblSelectDisease.setBounds(52, 12, 95, 14);
		panel.add(lblSelectDisease);
		lblSelectDisease.setFont(new Font("Tahoma", Font.PLAIN, 12));



		JLabel lblDateTo = new JLabel("DATE : TO");
		lblDateTo.setBounds(447, 12, 73, 14);
		panel.add(lblDateTo);
		lblDateTo.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblDateFrom = new JLabel("DATE : From");
		lblDateFrom.setBounds(248, 12, 82, 14);
		panel.add(lblDateFrom);
		lblDateFrom.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JButton btnNewButton_2 = new JButton("Search");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int i=tabbedPane.getSelectedIndex();
				if (i == 0) {
				    originalCashTableData.clear();
				    originalCashlessTableData.clear();
				    populateTable(cashTable, dateFrom, dateTo, 0, originalCashTableData);
				    populateTable(cashlessTable, dateFrom, dateTo, 1, originalCashlessTableData);
				}

				if (i == 1) {
				    originalOPDCashTableData.clear();
				    originalOPDCashlessTableData.clear();
				    populateTable1(cashOPDTable, dateFrom, dateTo, 0, originalOPDCashTableData);
				    populateTable1(cashlessOPDTable, dateFrom, dateTo, 1, originalOPDCashlessTableData);
				}

				if (i == 2) {
				    originalEXAMCashTableData.clear();
				    originalEXAMCashlessTableData.clear();
				    populateTable2(cashExamTable, dateFrom, dateTo, 0, originalEXAMCashTableData);
				    populateTable2(cashlessExamTable, dateFrom, dateTo, 1, originalEXAMCashlessTableData);
				}

			}
		});
		btnNewButton_2.setBounds(587, 39, 111, 25);
		panel.add(btnNewButton_2);
		btnNewButton_2.setIcon(new ImageIcon(InsurancePayments.class.getResource("/icons/zoom_r_button.png")));
		btnNewButton_2.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JButton btnExcel = new JButton("Excel");
		btnExcel.setEnabled(false);
		btnExcel.setBounds(713, 39, 120, 25);
		panel.add(btnExcel);
		btnExcel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

			}
		});
		btnExcel.setIcon(new ImageIcon(InsurancePayments.class.getResource("/icons/1BL.PNG")));
		btnExcel.setFont(new Font("Tahoma", Font.PLAIN, 12));

		// Create main tabbed pane for IPD Patients
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(31, 98, 907, 331);
		tabbedPane.setSelectedIndex(-1);
		tabbedPane.setFont(new Font("Tahoma", Font.PLAIN, 12));
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		contentPane.add(tabbedPane);
		tabbedPane.addChangeListener(new ChangeListener() {
		    @Override
		    public void stateChanged(ChangeEvent e) {
		    	int i=tabbedPane.getSelectedIndex();
		    	searchPatientNameTB.setText("");
				if (i == 0) {
				    originalCashTableData.clear();
				    originalCashlessTableData.clear();
				    populateTable(cashTable, dateFrom, dateTo, 0, originalCashTableData);
				    populateTable(cashlessTable, dateFrom, dateTo, 1, originalCashlessTableData);
				}

				if (i == 1) {
				    originalOPDCashTableData.clear();
				    originalOPDCashlessTableData.clear();
				    populateTable1(cashOPDTable, dateFrom, dateTo, 0, originalOPDCashTableData);
				    populateTable1(cashlessOPDTable, dateFrom, dateTo, 1, originalOPDCashlessTableData);
				}

				if (i == 2) {
				    originalEXAMCashTableData.clear();
				    originalEXAMCashlessTableData.clear();
				    populateTable2(cashExamTable, dateFrom, dateTo, 0, originalEXAMCashTableData);
				    populateTable2(cashlessExamTable, dateFrom, dateTo, 1, originalEXAMCashlessTableData);
				}
		    }
		});


		// Create sub-tabbed pane for Cash and Cashless
		JTabbedPane subTabbedPane = new JTabbedPane(JTabbedPane.TOP);
		subTabbedPane.setFont(new Font("Tahoma", Font.PLAIN, 12));

		// Create separate tables for Cash and Cashless tabs
		 cashTable = createIPDTable();
	     cashlessTable = createIPDTable();

		// Create separate scroll panes for each table
		JScrollPane cashScrollPane = new JScrollPane(cashTable);
		JScrollPane cashlessScrollPane = new JScrollPane(cashlessTable);

		// Disable the scroll panes (or keep enabled based on your logic)
		cashScrollPane.setEnabled(false);
		cashlessScrollPane.setEnabled(false);

		// Add Cash and Cashless sub-tabs to the subTabbedPane
		subTabbedPane.addTab("Cash", new ImageIcon(InsurancePayments.class.getResource("/icons/pick_patient_button.png")), cashScrollPane, "Cash");

		subTabbedPane.addTab("Cashless", new ImageIcon(InsurancePayments.class.getResource("/icons/pick_patient_button.png")), cashlessScrollPane, "Cashless");

		// Add the subTabbedPane as a tab under "IPD Patients"
		tabbedPane.addTab("IPD Patients", new ImageIcon(InsurancePayments.class.getResource("/icons/pick_patient_button.png")), subTabbedPane, "IPD Patients");

		// Mouse listener for Cash and Cashless tables
		cashTable.addMouseListener(new MouseListener() {
		    @Override
		    public void mouseClicked(MouseEvent arg0) {
		        if (arg0.getClickCount() == 2) {
		            int row = cashTable.getSelectedRow();
		            Object selectedObject = cashTable.getValueAt(row, 0);
		            Object selectedStatus = cashTable.getValueAt(row, 3);
		            if ("CLOSED".equals(selectedStatus) || "CLOSED(OBJ)".equals(selectedStatus)) {
		                int result = JOptionPane.showConfirmDialog(null, "Are you sure? This Bill Has Been Closed, You Can Edit Only Between The Closed Bill Date.",
		                        "Receiving Date", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		                if (result == JOptionPane.YES_OPTION) {
		                    insuranceIPDBill obj = new insuranceIPDBill(selectedObject.toString());
		                    obj.setVisible(true);
		                }
		            } else {
		                insuranceIPDBill obj = new insuranceIPDBill(selectedObject.toString());
		                obj.setVisible(true);
		            }
		        }
		    }

		    @Override
		    public void mousePressed(MouseEvent e) {}
		    @Override
		    public void mouseReleased(MouseEvent e) {}
		    @Override
		    public void mouseEntered(MouseEvent e) {}
		    @Override
		    public void mouseExited(MouseEvent e) {}
		});

		// Mouse listener for Cashless table
		cashlessTable.addMouseListener(new MouseListener() {
		    @Override
		    public void mouseClicked(MouseEvent arg0) {
		        if (arg0.getClickCount() == 2) {
		            int row = cashlessTable.getSelectedRow();
		            Object selectedObject = cashlessTable.getValueAt(row, 0);
		            Object selectedStatus = cashlessTable.getValueAt(row, 3);
		            if ("CLOSED".equals(selectedStatus) || "CLOSED(OBJ)".equals(selectedStatus)) {
		                int result = JOptionPane.showConfirmDialog(null, "Are you sure? This Bill Has Been Closed, You Can Edit Only Between The Closed Bill Date.",
		                        "Receiving Date", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		                if (result == JOptionPane.YES_OPTION) {
		                    insuranceIPDBill obj = new insuranceIPDBill(selectedObject.toString());
		                    obj.setVisible(true);
		                }
		            } else {
		                insuranceIPDBill obj = new insuranceIPDBill(selectedObject.toString());
		                obj.setVisible(true);
		            }
		        }
		    }

		    @Override
		    public void mousePressed(MouseEvent e) {}
		    @Override
		    public void mouseReleased(MouseEvent e) {}
		    @Override
		    public void mouseEntered(MouseEvent e) {}
		    @Override
		    public void mouseExited(MouseEvent e) {}
		});
		// Create sub-tabbed pane for Cash and Cashless
		JTabbedPane opdTabbedPane = new JTabbedPane(JTabbedPane.TOP);
		opdTabbedPane.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		cashOPDTable = createOPDTable();
	    cashlessOPDTable = createOPDTable();
		
		JScrollPane cashScrollPaneOPD = new JScrollPane(cashOPDTable);
		JScrollPane cashlessScrollPaneOPD = new JScrollPane(cashlessOPDTable);
		cashScrollPaneOPD.setEnabled(false);
		cashlessScrollPaneOPD.setEnabled(false);
		opdTabbedPane.addTab("Cash", new ImageIcon(InsurancePayments.class.getResource("/icons/pick_patient_button.png")), cashScrollPaneOPD, "Cash");

		opdTabbedPane.addTab("Cashless", new ImageIcon(InsurancePayments.class.getResource("/icons/pick_patient_button.png")), cashlessScrollPaneOPD, "Cashless");

		tabbedPane.addTab("OPD Patients", new ImageIcon(InsurancePayments.class.getResource("/icons/pick_patient_button.png")), opdTabbedPane, "OPD Patients");
		
		
		cashOPDTable.addMouseListener(new MouseListener() {

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
					int row = cashOPDTable.getSelectedRow();
					int column = cashOPDTable.getSelectedColumn();
					// do some action

					String opdID = cashOPDTable.getValueAt(row, 0)+"";
					String pID = cashOPDTable.getValueAt(row, 1)+"";
					String opdDate = cashOPDTable.getValueAt(row, 5)+"";
					String instype = cashOPDTable.getValueAt(row, 3)+"";
					String mode = "CASH".equals(cashOPDTable.getValueAt(row, 8) + "") ? "CASH" : "PANEL";
					insuranceOPDBill obj = new insuranceOPDBill(pID,opdID,opdDate,instype,mode);
					obj.setModal(true);
					obj.setVisible(true);


				}
			}
		});
		
		cashlessOPDTable.addMouseListener(new MouseListener() {

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
					int row = cashlessOPDTable.getSelectedRow();
					int column = cashlessOPDTable.getSelectedColumn();
					// do some action

					String opdID = cashlessOPDTable.getValueAt(row, 0)+"";
					String pID = cashlessOPDTable.getValueAt(row, 1)+"";
					String opdDate = cashlessOPDTable.getValueAt(row, 5)+"";
					String instype = cashlessOPDTable.getValueAt(row, 3)+"";
					String mode = "CASH".equals(cashlessOPDTable.getValueAt(row, 8) + "") ? "CASH" : "PANEL";
					insuranceOPDBill obj = new insuranceOPDBill(pID,opdID,opdDate,instype,mode);
					obj.setModal(true);
					obj.setVisible(true);


				}
			}
		});

		JTabbedPane ExamTabbedPane = new JTabbedPane(JTabbedPane.TOP);
		ExamTabbedPane.setFont(new Font("Tahoma", Font.PLAIN, 12));
		cashExamTable = createOPDTable();
	    cashlessExamTable = createOPDTable();
		
		JScrollPane cashScrollPaneExam = new JScrollPane(cashExamTable);
		JScrollPane cashlessScrollPaneExam = new JScrollPane(cashlessExamTable);
		cashScrollPaneExam.setEnabled(false);
		cashlessScrollPaneExam.setEnabled(false);
		ExamTabbedPane.addTab("Cash", new ImageIcon(InsurancePayments.class.getResource("/icons/pick_patient_button.png")), cashScrollPaneExam, "Cash");

		ExamTabbedPane.addTab("Cashless", new ImageIcon(InsurancePayments.class.getResource("/icons/pick_patient_button.png")), cashlessScrollPaneExam, "Cashless");

		tabbedPane.addTab("OPD Exams", new ImageIcon(InsurancePayments.class.getResource("/icons/pick_patient_button.png")), ExamTabbedPane, "OPD Exams");
		
		cashExamTable.addMouseListener(new MouseListener() {

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
					int row = cashExamTable.getSelectedRow();
					int column = cashExamTable.getSelectedColumn();
					String pID = cashExamTable.getValueAt(row, 3)+"";
					String ExamDate = cashExamTable.getValueAt(row, 6)+"";
					String instype = cashExamTable.getValueAt(row, 8)+"";
					String mode = "CASH".equals(cashExamTable.getValueAt(row, 9) + "") ? "CASH" : "PANEL";
					String receiptID = cashExamTable.getValueAt(row, 1)+"";
					GetBillDATA(pID,ExamDate,instype,mode);
					try {
						new InsuranceOpdPdfSlip(receiptID,pID,
								ITEM_NAME ,
								ITEM_DESC,
								DATE ,
								TYPE ,
								ITEM_ID,
								PER_ITEM_PRICE, 
								QTY ,
								AMOUNT									
								);
					} catch (DocumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
		});
		cashlessExamTable.addMouseListener(new MouseListener() {

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
					int row = cashlessExamTable.getSelectedRow();
					int column = cashlessExamTable.getSelectedColumn();
					String pID = cashlessExamTable.getValueAt(row, 3)+"";
					String ExamDate = cashlessExamTable.getValueAt(row, 6)+"";
					String instype = cashlessExamTable.getValueAt(row, 8)+"";
					String mode = "CASH".equals(cashlessExamTable.getValueAt(row, 9) + "") ? "CASH" : "PANEL";
					String receiptID = cashlessExamTable.getValueAt(row, 1)+"";
					GetBillDATA(pID,ExamDate,instype,mode);
					try {
						new InsuranceOpdPdfSlip(receiptID,pID,
								ITEM_NAME ,
								ITEM_DESC,
								DATE ,
								TYPE ,
								ITEM_ID,
								PER_ITEM_PRICE, 
								QTY ,
								AMOUNT									
								);
					} catch (DocumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
		});
		dateFromDC = new JDateChooser();
		dateFromDC.setBounds(202, 37, 178, 25);
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
						}
					}
				});
		dateFromDC.setDate(new Date());
		dateFromDC.setMaxSelectableDate(new Date());
		dateFromDC.setDateFormatString("yyyy-MM-dd");


		dateToDC = new JDateChooser();
		dateToDC.setBounds(392, 38, 178, 25);
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
						}
					}
				});
		dateToDC.setDate(new Date());
		dateToDC.setDateFormatString("yyyy-MM-dd");
	}

	public void searchTableContents(String searchString, JTable tableToPopulate, List<Vector> originalDataList) {
	    DefaultTableModel currtableModel = (DefaultTableModel) tableToPopulate.getModel();
	    currtableModel.setRowCount(0); // clear the current table

	    for (Vector rowVector : originalDataList) {
	        for (Object column : rowVector) {
	            if (column.toString().toLowerCase().contains(searchString.toLowerCase())) {
	                currtableModel.addRow(rowVector);
	                break;
	            }
	        }
	    }
	}

	public void searchTableContents1(String searchString, JTable tableToPopulate, List<Vector> originalDataList) {
	    DefaultTableModel currtableModel = (DefaultTableModel) tableToPopulate.getModel();
	    currtableModel.setRowCount(0); // clear the current table

	    for (Vector rowVector : originalDataList) {
	        for (Object column : rowVector) {
	            if (column.toString().toLowerCase().contains(searchString.toLowerCase())) {
	                currtableModel.addRow(rowVector);
	                break;
	            }
	        }
	    }
	}

	public void searchTableContents2(String searchString, JTable tableToPopulate, List<Vector> originalDataList) {
	    DefaultTableModel currtableModel = (DefaultTableModel) tableToPopulate.getModel();
	    currtableModel.setRowCount(0); // clear the current table

	    for (Vector rowVector : originalDataList) {
	        for (Object column : rowVector) {
	            if (column.toString().toLowerCase().contains(searchString.toLowerCase())) {
	                currtableModel.addRow(rowVector);
	                break;
	            }
	        }
	    }
	}

	
	// Method to create a JTable for displaying IPD data
	private JTable createIPDTable() {
	    ipdTable = new JTable();
	    ipdTable.setToolTipText("");
	    ipdTable.setFont(new Font("Tahoma", Font.PLAIN, 12));
	    ipdTable.getTableHeader().setReorderingAllowed(false);
	    ipdTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
	    ipdTable.setAutoCreateRowSorter(true);
	    ipdTable.setModel(new DefaultTableModel(
	        new Object[][] {
	            // Add actual data here (can be fetched dynamically)
	        },
	        new String[] {
	            "IPD ID.", "P NAME", "INS", "STATUS", "Total Amount", "SUB.AMT", "RCV.AMT", "OBJ ?", "DISCHRG DATE", "SUB DATE", "RCV DATE"
	        }
	    ));
	    ipdTable.getColumnModel().getColumn(0).setPreferredWidth(120);
	    ipdTable.getColumnModel().getColumn(1).setPreferredWidth(150);
		ipdTable.getColumnModel().getColumn(2).setPreferredWidth(130);
		ipdTable.getColumnModel().getColumn(3).setPreferredWidth(150);
		ipdTable.getColumnModel().getColumn(4).setPreferredWidth(100);
		ipdTable.getColumnModel().getColumn(5).setPreferredWidth(130);
		ipdTable.getColumnModel().getColumn(6).setPreferredWidth(130); 
		ipdTable.getColumnModel().getColumn(7).setPreferredWidth(130); 
		ipdTable.getColumnModel().getColumn(8).setPreferredWidth(130); 
		ipdTable.getColumnModel().getColumn(9).setPreferredWidth(130); 
		ipdTable.getColumnModel().getColumn(10).setPreferredWidth(130); 
	    return ipdTable;
	}
	private JTable createOPDTable() {
		opdTable = new JTable();
		opdTable.setToolTipText("Double Click to patient Details");
		opdTable.setFont(new Font("Tahoma", Font.PLAIN, 12));
		opdTable.getTableHeader().setReorderingAllowed(false);
		opdTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		opdTable.setAutoCreateRowSorter(true);
		opdTable.setModel(new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
						"OPD ID","P ID.", "P NAME","INSURANCE","OPD DOCTOR", "OPD DATE", "OPD TIME","ENTRY USER"}
				));
		opdTable.getColumnModel().getColumn(0).setPreferredWidth(120);
		opdTable.getColumnModel().getColumn(1).setPreferredWidth(150);
		opdTable.getColumnModel().getColumn(2).setPreferredWidth(130);
		opdTable.getColumnModel().getColumn(3).setPreferredWidth(150);
		opdTable.getColumnModel().getColumn(4).setPreferredWidth(100);
		opdTable.getColumnModel().getColumn(5).setPreferredWidth(130);
		opdTable.getColumnModel().getColumn(6).setPreferredWidth(130);
	    return opdTable;
	}
	private JTable createExamTable() {
		examsTable = new JTable();
		examsTable.setFont(new Font("Tahoma", Font.PLAIN, 12));
		examsTable.getTableHeader().setReorderingAllowed(false);
		examsTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		examsTable.setAutoCreateRowSorter(true);
		examsTable.setModel(new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
						"EXAM ID","RECEIPT ID", "EXAM NAME","P ID","P NAME", "EXAM DOCTOR", "EXAM DATE","EXAM RATE","INSURANCE","PAY MODE","IS CASHLESS"}
				));
		examsTable.getColumnModel().getColumn(0).setPreferredWidth(120);
		examsTable.getColumnModel().getColumn(1).setPreferredWidth(150);
		examsTable.getColumnModel().getColumn(2).setPreferredWidth(130);
		examsTable.getColumnModel().getColumn(3).setPreferredWidth(150);
		examsTable.getColumnModel().getColumn(4).setPreferredWidth(100);
		examsTable.getColumnModel().getColumn(5).setPreferredWidth(130);
		examsTable.getColumnModel().getColumn(6).setPreferredWidth(130);
	    return examsTable;
	}
	public void populateTable1(JTable tableToPopulate, String dateFrom, String dateTo, int iscashless, List<Vector> originalOPDCashTableData)
	{
		
		

		try {
			OPDDBConnection db=new OPDDBConnection();
			ResultSet  rs = db.getAllInsuranceOpdData(dateFrom, dateTo,iscashless); 

			// System.out.println("Table: " + rs.getMetaData().getTableName(1));
			int NumberOfColumns = 0, NumberOfRows = 0;
			NumberOfColumns = rs.getMetaData().getColumnCount();
			rs.last();
			NumberOfRows=rs.getRow();
			rs.beforeFirst();

			//to set rows in this array
			Object Rows_Object_Array[][];
			Rows_Object_Array = new Object[NumberOfRows][NumberOfColumns];

			int R = 0;
			while(rs.next()) {
				for(int C=1; C<=NumberOfColumns;C++) {
					Rows_Object_Array[R][C-1] = rs.getObject(C)==null?"..":rs.getObject(C);
				}
				R++;
			}
	
			//Finally load data to the ipdTable
			DefaultTableModel model = new DefaultTableModel(Rows_Object_Array, new String[] {
					"OPD ID","P ID", "P NAME","INSURANCE","OPD DOCTOR", "OPD DATE", "OPD TIME","ENTRY USER","PAY MODE","IS CASHLESS"}) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;// This causes all cells to be not editable
				}
			};
			 tableToPopulate.setModel(model);
			originalTableModel1 = (Vector) ((DefaultTableModel) tableToPopulate.getModel())
					.getDataVector().clone();

			tableToPopulate.getColumnModel().getColumn(0).setPreferredWidth(120);
			tableToPopulate.getColumnModel().getColumn(1).setPreferredWidth(150);
			tableToPopulate.getColumnModel().getColumn(2).setPreferredWidth(130);
			tableToPopulate.getColumnModel().getColumn(3).setPreferredWidth(150);
			tableToPopulate.getColumnModel().getColumn(4).setPreferredWidth(100);
			tableToPopulate.getColumnModel().getColumn(5).setPreferredWidth(130);
			tableToPopulate.getColumnModel().getColumn(6).setPreferredWidth(130); 
			tableToPopulate.getColumnModel().getColumn(7).setPreferredWidth(130); 
			tableToPopulate.getColumnModel().getColumn(8).setPreferredWidth(130); 
			tableToPopulate.setDefaultRenderer(Object.class, new RedYellowRenderer());
			saveOriginalData(tableToPopulate,originalOPDCashTableData);
		} catch (SQLException ex) {
			Logger.getLogger(InsurancePayments.class.getName()).log(Level.SEVERE, null, ex);
		}


	}
	private void saveOriginalData(JTable table, List<Vector> originalDataList) {
	    DefaultTableModel model = (DefaultTableModel) table.getModel();
	    originalDataList.clear();

	    for (int i = 0; i < model.getRowCount(); i++) {
	        Vector row = new Vector();
	        for (int j = 0; j < model.getColumnCount(); j++) {
	            row.add(model.getValueAt(i, j));
	        }
	        originalDataList.add(row);
	    }
	}

	public void populateTable2(JTable tableToPopulate, String dateFrom, String dateTo, int iscashless, List<Vector> originalEXAMCashTableData)
	{
		

		try {
			ExamDBConnection db = new ExamDBConnection();
			ResultSet rs = db.retrieveAllOpdExams(dateFrom, dateTo, iscashless);

			// System.out.println("Table: " + rs.getMetaData().getTableName(1));
			int NumberOfColumns = 0, NumberOfRows = 0;
			NumberOfColumns = rs.getMetaData().getColumnCount();
			rs.last();
			NumberOfRows=rs.getRow();
			rs.beforeFirst();

			//to set rows in this array
			Object Rows_Object_Array[][];
			Rows_Object_Array = new Object[NumberOfRows][NumberOfColumns];

			int R = 0;
			while(rs.next()) {
				for(int C=1; C<=NumberOfColumns;C++) {
					Rows_Object_Array[R][C-1] = rs.getObject(C)==null?"..":rs.getObject(C);
				}
				R++;
			}
			
			//Finally load data to the ipdTable
			DefaultTableModel model = new DefaultTableModel(Rows_Object_Array, new String[] {
					"EXAM ID","RECEIPT ID", "EXAM NAME","P ID","P NAME", "EXAM DOCTOR", "EXAM DATE","EXAM RATE","INSURANCE","PAY MODE","IS CASHLESS"}) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;// This causes all cells to be not editable
				}
			};
			tableToPopulate.setModel(model);
			originalTableModel2 = (Vector) ((DefaultTableModel) tableToPopulate.getModel())
					.getDataVector().clone();

			tableToPopulate.getColumnModel().getColumn(0).setPreferredWidth(120);
			tableToPopulate.getColumnModel().getColumn(1).setPreferredWidth(150);
			tableToPopulate.getColumnModel().getColumn(2).setPreferredWidth(130);
			tableToPopulate.getColumnModel().getColumn(3).setPreferredWidth(150);
			tableToPopulate.getColumnModel().getColumn(4).setPreferredWidth(100);
			tableToPopulate.getColumnModel().getColumn(5).setPreferredWidth(130);
			tableToPopulate.getColumnModel().getColumn(6).setPreferredWidth(130); 
			tableToPopulate.getColumnModel().getColumn(7).setPreferredWidth(130); 
			tableToPopulate.getColumnModel().getColumn(8).setPreferredWidth(130); 
			tableToPopulate.setDefaultRenderer(Object.class, new RedYellowRenderer());
			saveOriginalData(tableToPopulate,originalEXAMCashTableData);
		} catch (SQLException ex) {
			Logger.getLogger(InsurancePayments.class.getName()).log(Level.SEVERE, null, ex);
		}


	}
	private void GetBillDATA(String p_id, String exam_date, String instype, String mode) {
		// function to get all main data from ipd_expenses
		ITEM_NAME.removeAllElements();
		DATE.removeAllElements();
		TYPE.removeAllElements();
		ITEM_ID.removeAllElements();
		PAGE_NO.removeAllElements();
		PER_ITEM_PRICE.removeAllElements();
		QTY.removeAllElements();
		AMOUNT.removeAllElements();
		ITEM_DESC.removeAllElements();
		IPDDBConnection db = new IPDDBConnection();
		ResultSet rs = db.retrieveAllOPDEXAMData(p_id,exam_date,instype,mode);
		/** getting ipd data from DB**/
		try {
			while (rs.next()) {
				ITEM_NAME.add(rs.getString(3));
				DATE.add(rs.getString(4));  
				TYPE.add(rs.getString(1));
				ITEM_ID.add(rs.getString(2));
				PAGE_NO.add(rs.getString(4));
				PER_ITEM_PRICE.add(rs.getDouble(6)); 
				QTY.add(rs.getString(5));
				AMOUNT.add(rs.getDouble(6));
				ITEM_DESC.add(rs.getString(3));				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {	
			db.closeConnection();
		}
	}
	public void populateTable(JTable tableToPopulate, String dateFrom, String dateTo, int iscashless, List<Vector> originalCashTableData) {

	    try {
	        InsuranceDBConnection db = new InsuranceDBConnection();
	        ResultSet rs = db.retrieveAllData12(dateFrom, dateTo, iscashless);

	        int NumberOfColumns = rs.getMetaData().getColumnCount();
	        rs.last();
	        int NumberOfRows = rs.getRow();
	        rs.beforeFirst();

	        Object[][] Rows_Object_Array = new Object[NumberOfRows][NumberOfColumns + 1];

	        int R = 0;
	        while (rs.next()) {
	            for (int C = 1; C <= NumberOfColumns; C++) {
	                Rows_Object_Array[R][C - 1] = rs.getObject(C) == null ? ".." : rs.getObject(C);
	            }
	            R++;
	        }

	       

	        DefaultTableModel model = new DefaultTableModel(Rows_Object_Array, new String[]{
	            "IPD ID.", "P NAME", "INSURANCE", "STATUS", "BILL AMNT", "SUB.AMNT", "RCV.AMNT", "OBJ ?", "DISCHRG DATE", "SUB DATE", "RCV DATE"
	        }) {
	            @Override
	            public boolean isCellEditable(int row, int column) {
	                return false;
	            }
	        };

	        tableToPopulate.setModel(model);

	       

	        // Set column widths
	        TableColumn columnsize;
	        for (int i = 0; i < 9; i++) {
	            columnsize = tableToPopulate.getColumnModel().getColumn(i);
	            columnsize.setPreferredWidth(110);
	            if (i == 1 || i == 2 || i == 3)
	                columnsize.setPreferredWidth(150);
	            if (i == 5)
	                columnsize.setPreferredWidth(60);
	        }
	        saveOriginalData(tableToPopulate,originalCashTableData);
	    } catch (SQLException ex) {
	        Logger.getLogger(InsurancePayments.class.getName()).log(Level.SEVERE, null, ex);
	    }
	}


	class RedYellowRenderer extends DefaultTableCellRenderer {
		Color Purple = new Color(102,0,153);
		Color c2 = new Color(255,204,30);
		RedYellowRenderer() {
			setHorizontalAlignment(CENTER);
			
		}
		@Override
		public Component getTableCellRendererComponent(
				JTable table, Object value,
				boolean isSelected, boolean hasFocus,
				int row, int column
				) {
			Component c = super.getTableCellRendererComponent(
					table, value, isSelected, hasFocus, row, column
					);
	        String columnName = "IS CASHLESS";
	        int columnIndex = table.getColumnModel().getColumnIndex(columnName);
	        if (isSelected) {
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            }else if(Boolean.parseBoolean(table.getValueAt(row, columnIndex).toString()) && table.getRowCount() > 0) {
				c.setBackground(Color.white);
				c.setForeground(Color.black);
			}else{
				c.setBackground(Color.gray);
				c.setForeground(Color.white);
			}
			
			return c;
		}
	}
}
