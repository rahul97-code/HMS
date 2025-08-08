package hms.reception.gui;

import java.awt.EventQueue;
import java.awt.event.*;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.table.*;

import com.itextpdf.text.DocumentException;
import com.toedter.calendar.JDateChooser;
import hms.patient.slippdf.IPDBillSlippdf;
import hms.reception.database.financialCounclingDBConnection;

public class FinancialCouncling extends JDialog {

    private JTable patientTable;
    private DefaultTableModel patientModel;
    private TableRowSorter<DefaultTableModel> rowSorter;

    private JTabbedPane tabbedPane;
    private JTextField searchBox;
    private JDateChooser fromDateChooser, toDateChooser;

    private HashMap<String, DefaultTableModel> tableModels = new HashMap<String, DefaultTableModel>();
    private HashMap<String, TableRowSorter<DefaultTableModel>> rowSorters = new HashMap<String, TableRowSorter<DefaultTableModel>>();

    private JTextField textField;
    private JLabel lblTotal;
    private JTextField txttabbed;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    FinancialCouncling window = new FinancialCouncling();
                    window.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public FinancialCouncling() {
        setTitle("Financial Councling");
        setBounds(100, 100, 946, 675);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(null);

        JPanel searchPanel = new JPanel();
        searchPanel.setBounds(20, 10, 896, 60);
        searchPanel.setBorder(new TitledBorder(new BevelBorder(BevelBorder.LOWERED), "Search Patients by Date"));
        searchPanel.setLayout(null);
        getContentPane().add(searchPanel);

        JLabel lblFrom = new JLabel("From:");
        lblFrom.setBounds(20, 25, 50, 15);
        searchPanel.add(lblFrom);

        fromDateChooser = new JDateChooser(new Date());
        fromDateChooser.setBounds(70, 22, 121, 22);
        searchPanel.add(fromDateChooser);

        JLabel lblTo = new JLabel("To:");
        lblTo.setBounds(219, 25, 30, 15);
        searchPanel.add(lblTo);

        toDateChooser = new JDateChooser(new Date());
        toDateChooser.setBounds(247, 22, 121, 22);
        searchPanel.add(toDateChooser);

        JButton btnFilter = new JButton("Search");
        btnFilter.setBounds(394, 20, 90, 25);
        searchPanel.add(btnFilter);

        JLabel lblSearch = new JLabel("Search IPD :");
        lblSearch.setBounds(593, 25, 100, 15);
        searchPanel.add(lblSearch);

        searchBox = new JTextField();
        searchBox.setBounds(694, 22, 181, 22);
        searchPanel.add(searchBox);

        patientModel = new DefaultTableModel(new Object[][] {}, new String[] {
                "Ipd Id", "Patient Id", "Patient Name", "Admission Date", "Discharge Date", "Discharge", "Total Amount", "Submitted Amount", "Ins Type", "Ipd Doctor"
        });

        patientTable = new JTable(patientModel);
        JScrollPane patientScrollPane = new JScrollPane(patientTable);
        patientScrollPane.setBounds(20, 80, 896, 150);
        getContentPane().add(patientScrollPane);

        rowSorter = new TableRowSorter<DefaultTableModel>(patientModel);
        patientTable.setRowSorter(rowSorter);

        searchBox.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                searchFilter();
            }
            public void removeUpdate(DocumentEvent e) {
                searchFilter();
            }
            public void changedUpdate(DocumentEvent e) {
                searchFilter();
            }
        });

        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setBounds(20, 265, 896, 330);
        getContentPane().add(tabbedPane);

        lblTotal = new JLabel("Total :");
        lblTotal.setBounds(648, 610, 136, 15);
        getContentPane().add(lblTotal);

        textField = new JTextField();
        textField.setBounds(802, 610, 114, 19);
        textField.setEditable(false);
        getContentPane().add(textField);
        textField.setColumns(10);

        createEmptyTab("Exams", new String[] { "Exam Name", "Exam Desc", "Per Item Price", "Quantity", "Amount", "Exam date", "Type" });
        createEmptyTab("Consumables", new String[] { "Med Name", "Med Desc", "Per Item Price", "Quantity", "Amount", "Med date", "Type" });
        createEmptyTab("Returns", new String[] { "Item Name", "Item Desc", "Per Item Price", "Quantity", "Amount", "Item date", "Type" });
        createEmptyTab("Beds", new String[] { "S.N", "Ward Name", "Ward Category","Date","Hrs", "Ward Charge", "Amount" });
        createEmptyTab("Extras", new String[] { "Item Name", "Item Desc", "Per Item Price", "Quantity", "Amount", "Item date", "Type" });

        tabbedPane.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                updateTotalForCurrentTab();
            }
        });

        btnFilter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	searchBox.setText("");
            	txttabbed.setText("");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String from = sdf.format(fromDateChooser.getDate());
                String to = sdf.format(toDateChooser.getDate());
                loadPatients(from, to);
            }
        });

        patientTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = patientTable.getSelectedRow();
                if (row >= 0) {
                    String pid = patientTable.getValueAt(row, 0).toString();
                    loadPatientDetails(pid);
                }
            }
        });

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String from = sdf.format(fromDateChooser.getDate());
        String to = sdf.format(toDateChooser.getDate());
        loadPatients(from, to);
        tabbedPane.setSelectedIndex(0);

        JButton btnBill = new JButton("Provisional Bill");
        btnBill.setBounds(20, 605, 142, 25);
        btnBill.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                        	
                int row = patientTable.getSelectedRow();
                if (row >= 0) {
                    String IpdId = patientModel.getValueAt(row, 0).toString();
                    String DoctorName = patientModel.getValueAt(row, 9).toString();
                    try {
                        new IPDBillSlippdf("Provisional Bill", IpdId, DoctorName, false);
                    } catch (DocumentException e1) {
                        e1.printStackTrace();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Please Select Patient", "Input Error", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        getContentPane().add(btnBill);
       

        JLabel lblSearchTab = new JLabel("Search :");
        lblSearchTab.setBounds(620, 253, 70, 15);
        getContentPane().add(lblSearchTab);

        txttabbed = new JTextField();
        txttabbed.setBounds(701, 251, 140, 19);
        getContentPane().add(txttabbed);
        txttabbed.setColumns(10);

        txttabbed.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                tabbedSearchFilter();
            }
            public void removeUpdate(DocumentEvent e) {
                tabbedSearchFilter();
            }
            public void changedUpdate(DocumentEvent e) {
                tabbedSearchFilter();
            }
        });
    }

    private void createEmptyTab(String title, String[] columns) {
        DefaultTableModel model = new DefaultTableModel(null, columns);
        JTable table = new JTable(model);
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<DefaultTableModel>(model);
        table.setRowSorter(sorter);

        JScrollPane scroll = new JScrollPane(table);
        tabbedPane.addTab(title, scroll);

        tableModels.put(title, model);
        rowSorters.put(title, sorter);
    }

    private void loadPatients(String from, String to) {
        patientModel.setRowCount(0);

        financialCounclingDBConnection db = new financialCounclingDBConnection();
        ResultSet rs = db.retrievePatientData(from, to);

        try {
            while (rs.next()) {
                patientModel.addRow(new Object[] {
                        rs.getObject(1).toString(),
                        rs.getObject(2).toString(),
                        rs.getObject(3).toString(),
                        rs.getObject(4).toString(),
                        rs.getObject(5).toString(),
                        rs.getObject(6).toString(),
                        rs.getObject(7).toString(),
                        rs.getObject(8).toString(),
                        rs.getObject(9).toString(),
                        rs.getObject(10).toString()
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.closeConnection();
        }
    }

    private void loadPatientDetails(String ipdId) {
        financialCounclingDBConnection db = new financialCounclingDBConnection();
        try {
            setTabData("Exams", resultSetToArray(db.getExamsByPatient(ipdId)));
            setTabData("Consumables", resultSetToArray(db.getMedicinesByPatient(ipdId)));
            setTabData("Returns", resultSetToArray(db.getReturnsByPatient(ipdId)));
            setTabData("Beds", resultSetToArray(db.retrievePatientBedDetailsNEW(ipdId)));
            setTabData("Extras", resultSetToArray(db.getExtrasByPatient(ipdId)));
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading patient details: " + e.getMessage());
        }
        finally {
        	db.closeConnection();
        }
    }

    private Object[][] resultSetToArray(ResultSet rs) throws SQLException {
        List<Object[]> rows = new ArrayList<Object[]>();
        int columnCount = rs.getMetaData().getColumnCount();
        while (rs.next()) {
            Object[] row = new Object[columnCount];
            for (int i = 0; i < columnCount; i++) {
                row[i] = rs.getObject(i + 1);
            }
            rows.add(row);
        }
        return rows.toArray(new Object[0][]);
    }

    private void setTabData(String tabName, Object[][] data) {
        DefaultTableModel model = tableModels.get(tabName);
        if (model != null) {
            model.setRowCount(0);
            for (int i = 0; i < data.length; i++) {
                model.addRow(data[i]);
            }
            updateTotalForCurrentTab();
        }
    }

    private void updateTotalForCurrentTab() {
        String currentTabName = tabbedPane.getTitleAt(tabbedPane.getSelectedIndex());
        DefaultTableModel model = tableModels.get(currentTabName);
        if (model != null) {
            updateTotal(currentTabName, model);
        }
    }

    private void updateTotal(String tabName, DefaultTableModel model) {
        double totalAmount = 0;
        int amountColumnIndex = -1;

        for (int i = 0; i < model.getColumnCount(); i++) {
            if (model.getColumnName(i).equalsIgnoreCase("Amount")) {
                amountColumnIndex = i;
                break;
            }
        }

        if (amountColumnIndex != -1) {
            for (int i = 0; i < model.getRowCount(); i++) {
                try {
                    Object amountObj = model.getValueAt(i, amountColumnIndex);
                    if (amountObj != null) {
                        totalAmount += Double.parseDouble(amountObj.toString());
                    }
                } catch (NumberFormatException e) {
                    continue;
                }
            }
        }

        lblTotal.setText("Total (" + tabName + "):");
        textField.setText(String.format("%.2f", totalAmount));
    }

    private void searchFilter() {
        String keyword = searchBox.getText().trim().toLowerCase();
        if (keyword.length() == 0) {
            rowSorter.setRowFilter(null);
        } else {
            rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + keyword, 0, 1, 2));
        }
    }

    private void tabbedSearchFilter() {
        String keyword = txttabbed.getText().trim().toLowerCase();
        String currentTab = tabbedPane.getTitleAt(tabbedPane.getSelectedIndex());

        TableRowSorter<DefaultTableModel> sorter = rowSorters.get(currentTab);
        if (sorter == null) return;

        if (keyword.length() == 0) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + keyword));
        }
    }
}
