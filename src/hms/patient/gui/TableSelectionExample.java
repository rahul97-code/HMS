package hms.patient.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class TableSelectionExample {
    public static void main(String[] args) {
        // Use a traditional approach for invoking Swing components
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("JTable Auto Select Example");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(400, 300);

                // Sample column names and data
                String[] columns = {"ID", "Name", "Age"};
                Object[][] data = {
                    {1, "John", 25},
                    {2, "Alice", 30},
                    {3, "Bob", 22},
                    {4, "Eve", 28}
                };

                // Create a DefaultTableModel with sample data
                DefaultTableModel model = new DefaultTableModel(data, columns);
                JTable table = new JTable(model);

                // Create a JScrollPane and add the JTable to it
                JScrollPane scrollPane = new JScrollPane(table);
                frame.add(scrollPane, BorderLayout.CENTER);

                // Select a row based on the condition (e.g., "Alice" is in the second row)
                String targetName = "Alice"; // Name we want to search for
                selectRowByName(table, targetName);

                frame.setVisible(true);
            }
        });
    }

    public static void selectRowByName(JTable table, String name) {
        // Loop through the rows to find the matching name and select it
        for (int row = 0; row < table.getRowCount(); row++) {
            if (table.getValueAt(row, 1).equals(name)) { // Column 1 contains the "Name"
                table.setRowSelectionInterval(row, row); // Select the row

                // Make the selected row visible in the JScrollPane
                Rectangle rect = table.getCellRect(row, 0, true);
                table.scrollRectToVisible(rect);
                break;
            }
        }
    }
}
