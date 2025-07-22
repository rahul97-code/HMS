package UsersActivity.database;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.ss.usermodel.Cell;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelToJTableWithFileChooser {

    public static void main(String[] args) {
        // Ensuring Swing is run on the Event Dispatch Thread
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame("Excel Data in JTable");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(800, 600);
                frame.add(createMainPanel());
                frame.setVisible(true);
            }
        });
    }

    // Method to create the main panel with a button and table
    public static JPanel createMainPanel() {
        final JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Create the JTable and add it to a scroll pane
        final JTable table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Create a JList to display selected columns
        final DefaultListModel<String> listModel = new DefaultListModel<>();
        final JList<String> selectedColumnsList = new JList<>(listModel);
        selectedColumnsList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane listScrollPane = new JScrollPane(selectedColumnsList);
        listScrollPane.setPreferredSize(new Dimension(200, 100));
        panel.add(listScrollPane, BorderLayout.EAST);

        // Create a button that opens the file chooser
        JButton openButton = new JButton("Open Excel File");
        openButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                // Show the file chooser
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Select an Excel File");

                // Filter for .xls files (Excel 97-2003 format)
                fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Excel Files", "xls"));

                // Show the dialog and get the result
                int result = fileChooser.showOpenDialog(panel);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    String filePath = selectedFile.getAbsolutePath();
                    // Load data from the selected file into the JTable
                    loadDataIntoTable(filePath, table, listModel);
                }
            }
        });

        // Create a button to remove selected columns from the JList
        JButton removeButton = new JButton("Remove Selected Columns");
        removeButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                List<String> selectedColumns = selectedColumnsList.getSelectedValuesList();
                for (String col : selectedColumns) {
                    listModel.removeElement(col);
                }
            }
        });

        // Add buttons to the panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(openButton);
        buttonPanel.add(removeButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    // Method to load data from Excel file into the JTable and JList
    public static void loadDataIntoTable(String filePath, JTable table, DefaultListModel<String> listModel) {
        DefaultTableModel model = new DefaultTableModel();
        FileInputStream fileInputStream = null;
        HSSFWorkbook workbook = null;

        try {
            // Open the Excel file
            fileInputStream = new FileInputStream(new File(filePath));
            workbook = new HSSFWorkbook(fileInputStream);
            HSSFSheet sheet = workbook.getSheetAt(0); // Read the first sheet

            // Get the header row and set it as the table column names
            HSSFRow headerRow = sheet.getRow(0);
            Iterator<Cell> headerIterator = headerRow.iterator();
            while (headerIterator.hasNext()) {
                Cell cell = headerIterator.next();
                model.addColumn(cell.getStringCellValue());
                // Add column name to JList for selection
                listModel.addElement(cell.getStringCellValue());
            }

            // Add rows to the table model
            Iterator<org.apache.poi.ss.usermodel.Row> rowIterator = sheet.iterator();
            rowIterator.next(); // Skip the header row
            while (rowIterator.hasNext()) {
                org.apache.poi.ss.usermodel.Row row = rowIterator.next();
                Object[] rowData = new Object[row.getPhysicalNumberOfCells()];
                for (int i = 0; i < row.getPhysicalNumberOfCells(); i++) {
                    rowData[i] = row.getCell(i).toString(); // Convert all cells to string
                }
                model.addRow(rowData);
            }

            table.setModel(model); // Set the model to JTable
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error reading the file.", "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            // Close resources manually
            try {
                if (workbook != null) {
                    workbook.close();
                }
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Method to insert data from the selected columns into the MySQL database
    private static void insertDataIntoDatabase(DefaultListModel<String> listModel, JTable table) {
        List<String> selectedColumns = new ArrayList<>();
        for (int i = 0; i < listModel.getSize(); i++) {
            selectedColumns.add(listModel.getElementAt(i));
        }

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            // Establish a MySQL connection
            String url = "jdbc:mysql://localhost:3306/your_database";
            String user = "your_username";
            String password = "your_password";
            connection = DriverManager.getConnection(url, user, password);

            // Prepare the SQL statement to insert data
            StringBuilder sql = new StringBuilder("INSERT INTO your_table (");
            for (int i = 0; i < selectedColumns.size(); i++) {
                sql.append(selectedColumns.get(i));
                if (i < selectedColumns.size() - 1) {
                    sql.append(", ");
                }
            }
            sql.append(") VALUES (");
            for (int i = 0; i < selectedColumns.size(); i++) {
                sql.append("?");
                if (i < selectedColumns.size() - 1) {
                    sql.append(", ");
                }
            }
            sql.append(")");

            preparedStatement = connection.prepareStatement(sql.toString());

            // Loop through the rows of the table and insert data
            for (int row = 0; row < table.getRowCount(); row++) {
                for (int colIndex = 0; colIndex < selectedColumns.size(); colIndex++) {
                    String cellData = table.getValueAt(row, colIndex).toString();
                    preparedStatement.setString(colIndex + 1, cellData);
                }
                preparedStatement.addBatch();
            }

            // Execute the batch insert
            preparedStatement.executeBatch();
            JOptionPane.showMessageDialog(null, "Data inserted successfully into the database.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error inserting data into database.", "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            // Close resources
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
