package hms.insurance.gui;
//import java.awt.*;
//import java.awt.event.*;
//import java.io.*;
//import java.sql.*;
//import java.text.*;
//import java.util.*;
//import javax.swing.*;
//import javax.swing.filechooser.FileNameExtensionFilter;
//
//import org.apache.poi.hssf.usermodel.HSSFWorkbook;
//import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//
//public class ExcelToDB extends JFrame {
//    private JButton btnImport;
//    private Connection conn;
//
//    public ExcelToDB() {
//        super("Excel Importer");
//
//        btnImport = new JButton("Import Excel to DB");
//        btnImport.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                importExcel();
//            }
//        });
//
//        add(btnImport, BorderLayout.CENTER);
//
//        setSize(300, 100);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setLocationRelativeTo(null);
//        setVisible(true);
//
//        connectDB();
//    }
//
//    // ✅ Connect to MySQL
//    private void connectDB() {
//        try {
//            Class.forName("com.mysql.jdbc.Driver");
//            conn = DriverManager.getConnection(
//                "jdbc:mysql://192.168.1.33:3306/hospital_db", "hospital", "ambalarotaryhospital"
//            );
//            System.out.println("✅ Database Connected");
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }
//    
//    
//    private Row findHeaderRow(Sheet sheet) {
//        for (int r = 0; r <= sheet.getLastRowNum(); r++) {
//            Row row = sheet.getRow(r);
//            if (row == null) continue;
//
//            Cell firstCell = row.getCell(0);
//            if (firstCell != null && firstCell.getCellType() == Cell.CELL_TYPE_STRING) {
//                String val = firstCell.getStringCellValue().trim().toLowerCase();
//                if (val.contains("settlement date")) {
//                    return row;
//                }
//            }
//        }
//        return null;
//    }
//
// // ✅ Import Excel (updated)
//    private void importExcel() {
//        JFileChooser fileChooser = new JFileChooser();
//        fileChooser.setFileFilter(new FileNameExtensionFilter("Excel Files", "xls", "xlsx"));
//
//        int result = fileChooser.showOpenDialog(this);
//        if (result != JFileChooser.APPROVE_OPTION) return;
//
//        File file = fileChooser.getSelectedFile();
//
//        try (FileInputStream fis = new FileInputStream(file)) {
//            Workbook workbook = null;
//            if (file.getName().toLowerCase().endsWith("xlsx")) {
//                workbook = new XSSFWorkbook(fis);
//            } else {
//                workbook = new HSSFWorkbook(fis);
//            }
//
//            Sheet sheet = workbook.getSheetAt(0);
//
//            // ✅ अब headerRow को खोजेंगे
//            Row headerRow = findHeaderRow(sheet);
//            if (headerRow == null) {
//                JOptionPane.showMessageDialog(this, "❌ Header row not found!");
//                return;
//            }
//            int headerRowIndex = headerRow.getRowNum();
//            int colCount = headerRow.getPhysicalNumberOfCells();
//
//            String tableName = "claim_settlement";
//
//            dropIfExists(tableName);
//            createTableFromHeader(sheet, headerRow, tableName);
//
//            // ✅ Data rows headerRow के बाद से शुरू होंगे
//            for (int r = headerRowIndex + 1; r <= sheet.getLastRowNum(); r++) {
//                Row row = sheet.getRow(r);
//                if (row == null) continue;
//
//                // Skip "Grand Total"
//                Cell firstCell = row.getCell(0);
//                if (firstCell != null && firstCell.getCellType() == Cell.CELL_TYPE_STRING) {
//                    if (firstCell.getStringCellValue().toLowerCase().contains("total")) {
//                        continue;
//                    }
//                }
//
//                insertRow(row, tableName, colCount);
//            }
//
//            workbook.close();
//            JOptionPane.showMessageDialog(this, "✅ Import Successful!");
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            JOptionPane.showMessageDialog(this, "❌ Error: " + ex.getMessage());
//        }
//    }
//    // ✅ Drop table if exists
//    private void dropIfExists(String tableName) throws SQLException {
//        Statement st = conn.createStatement();
//        st.executeUpdate("DROP TABLE IF EXISTS " + tableName);
//        st.close();
//    }
//
//    // ✅ Create table dynamically
// // ✅ Create table with fixed datatypes according to header name
//    private void createTableFromHeader(Sheet sheet, Row headerRow, String tableName) throws SQLException {
//        StringBuilder sb = new StringBuilder("CREATE TABLE " + tableName + " (");
//
//        int colCount = headerRow.getPhysicalNumberOfCells();
//
//        for (int i = 0; i < colCount; i++) {
//            String colName = headerRow.getCell(i).getStringCellValue().trim()
//                                .replace(" ", "_")
//                                .replace("/", "_")
//                                .replace("\\", "_")
//                                .replace("-", "_")
//                                .replace(".", "_");
//
//            String colType = "VARCHAR(255)"; // default
//
//            switch (colName.toLowerCase()) {
//                case "settlement_date":
//                case "admit_opd_date":
//                case "accept_date":
//                    colType = "DATE";
//                    break;
//
//                case "claim_id":
//                    colType = "BIGINT";
//                    break;
//
//                case "admit_opd_no":
//                case "bill_no":
//                    colType = "VARCHAR(50)";
//                    break;
//
//                case "claim_amt":
//                case "app_amt":
//                case "echs_disc":
//                case "tds_amt":
//                case "utitsl_fees":
//                case "penalty":
//                case "recovery_amt":
//                case "amt_credited":
//                    colType = "DECIMAL(12,2)";
//                    break;
//            }
//
//            sb.append("`").append(colName).append("` ").append(colType);
//            if (i != colCount - 1) sb.append(", ");
//        }
//
//        sb.append(")");
//        Statement st = conn.createStatement();
//        st.executeUpdate(sb.toString());
//        st.close();
//
//        System.out.println("✅ Table Created: " + tableName);
//    }
//
//
//    private void insertRow(Row row, String tableName, int colCount) throws SQLException {
//        StringBuilder sb = new StringBuilder("INSERT INTO " + tableName + " VALUES(");
//        for (int i = 0; i < colCount; i++) {
//            sb.append("?");
//            if (i != colCount - 1) sb.append(",");
//        }
//        sb.append(")");
//
//        PreparedStatement ps = conn.prepareStatement(sb.toString());
//
//        for (int i = 0; i < colCount; i++) {
//            Cell cell = row.getCell(i);
//            if (cell == null) {
//                ps.setNull(i + 1, Types.VARCHAR);
//                continue;
//            }
//
//            switch (cell.getCellType()) {
//                case Cell.CELL_TYPE_STRING:
//                    String strVal = cell.getStringCellValue().trim();
//                    java.sql.Date parsedDate = tryParseDate(strVal);
//                    if (parsedDate != null) {
//                        ps.setDate(i + 1, parsedDate);
//                    } else {
//                        ps.setString(i + 1, strVal);
//                    }
//                    break;
//
//                case Cell.CELL_TYPE_NUMERIC:
//                    if (DateUtil.isCellDateFormatted(cell)) {
//                        java.sql.Date sqlDate = new java.sql.Date(cell.getDateCellValue().getTime());
//                        ps.setDate(i + 1, sqlDate);
//                    } else {
//                        ps.setDouble(i + 1, cell.getNumericCellValue());
//                    }
//                    break;
//
//                case Cell.CELL_TYPE_BOOLEAN:
//                    ps.setBoolean(i + 1, cell.getBooleanCellValue());
//                    break;
//
//                default:
//                    ps.setString(i + 1, "");
//            }
//        }
//        ps.executeUpdate();
//        ps.close();
//    }
//
//    // ✅ Helper: Try parsing String to Date
//    private java.sql.Date tryParseDate(String value) {
//        String[] patterns = { "dd-MM-yyyy", "dd-MMM-yy", "dd-MMM-yyyy", "yyyy-MM-dd" };
//        for (String pattern : patterns) {
//            try {
//                java.util.Date utilDate = new SimpleDateFormat(pattern).parse(value);
//                return new java.sql.Date(utilDate.getTime());
//            } catch (Exception e) {
//                // ignore and try next
//            }
//        }
//        return null; // not a date
//    }
//
//
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(new Runnable() {
//            public void run() {
//                new ExcelToDB();
//            }
//        });
//    }
//}




import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import hms.main.DBConnection;

public class ExcelToDB extends JFrame {
    private JButton btnImport;
    private Connection conn;

    public ExcelToDB() {
        super("Excel Importer");
//        connectDB();
        DBConnection db=new DBConnection();
        conn = db.getConnection();
        importExcel();
        
    }

    private void connectDB() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(
                "jdbc:mysql://192.168.1.33:3306/hospital_db", "hospital", "ambalarotaryhospital"
            );
            System.out.println("✅ Database Connected");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private Row findHeaderRow(Sheet sheet) {
        for (int r = 0; r <= sheet.getLastRowNum(); r++) {
            Row row = sheet.getRow(r);
            if (row == null) continue;

            Cell firstCell = row.getCell(0);
            if (firstCell != null && firstCell.getCellType() == Cell.CELL_TYPE_STRING) {
                String val = firstCell.getStringCellValue().trim().toLowerCase();
                if (val.contains("settlement date")) {
                    return row;
                }
            }
        }
        return null;
    }

    private void importExcel() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Excel Files", "xls", "xlsx"));

        int result = fileChooser.showOpenDialog(this);
        if (result != JFileChooser.APPROVE_OPTION) return;

        File file = fileChooser.getSelectedFile();

        try (FileInputStream fis = new FileInputStream(file)) {
            Workbook workbook = null;
            if (file.getName().toLowerCase().endsWith("xlsx")) {
                workbook = new XSSFWorkbook(fis);
            } else {
                workbook = new HSSFWorkbook(fis);
            }

            Sheet sheet = workbook.getSheetAt(0);

            Row headerRow = findHeaderRow(sheet);
            if (headerRow == null) {
                JOptionPane.showMessageDialog(this, "❌ Header row not found!");
                return;
            }
            int headerRowIndex = headerRow.getRowNum();

            int updatedCount = 0;

            for (int r = headerRowIndex + 1; r <= sheet.getLastRowNum(); r++) {
                Row row = sheet.getRow(r);
                if (row == null) continue;

                Cell firstCell = row.getCell(0);
                if (firstCell != null && firstCell.getCellType() == Cell.CELL_TYPE_STRING) {
                    if (firstCell.getStringCellValue().toLowerCase().contains("total")) {
                        continue;
                    }
                }

                if (updateRow(row)) {
                    updatedCount++;
                }
            }

            workbook.close();
            JOptionPane.showMessageDialog(this, "✅ Import Successful! " + updatedCount + " rows updated.");

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "❌ Error: " + ex.getMessage());
        }
    }

    private boolean updateRow(Row row) throws SQLException {
        String billNo = getCellString(row.getCell(5));        // Bill No (column 5 → check Excel index)
        Double tdsAmt = getCellDouble(row.getCell(9));        // TDS Amt (column 9)
        Double amtCredited = getCellDouble(row.getCell(13));  // Amt Credited (column 13)

        if (billNo == null || billNo.isEmpty()) return false;

        String sql = "UPDATE insurance_reimbursement_tracking " +
                     "SET tds_amnt = ?, recieved_amount = ? " +
                     "WHERE ipd_id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setDouble(1, tdsAmt != null ? tdsAmt : 0.0);
        ps.setDouble(2, amtCredited != null ? amtCredited : 0.0);
        ps.setString(3, billNo);

        int updated = ps.executeUpdate();
        ps.close();

        if (updated > 0) {
            System.out.println("✅ Updated row for Bill No: " + billNo);
            return true;
        } else {
            System.out.println("⚠️ No match found for Bill No: " + billNo);
            return false;
        }
    }

    private String getCellString(Cell cell) {
        if (cell == null) return null;
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                return cell.getStringCellValue().trim();
            case Cell.CELL_TYPE_NUMERIC:
                return String.valueOf((long) cell.getNumericCellValue());
            default:
                return null;
        }
    }

    private Double getCellDouble(Cell cell) {
        if (cell == null) return null;
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_NUMERIC:
                return cell.getNumericCellValue();
            case Cell.CELL_TYPE_STRING:
                try {
                    return Double.parseDouble(cell.getStringCellValue().trim());
                } catch (Exception e) {
                    return null;
                }
            default:
                return null;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ExcelToDB();
            }
        });
    }
}

