package UsersActivity.database;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class SpannedTableExample {

    // Custom Table Model
    static class SpannedTableModel extends AbstractTableModel {
        private final Object[][] data;
        private final String[] columnNames;

        public SpannedTableModel(Object[][] data, String[] columnNames) {
            this.data = data;
            this.columnNames = columnNames;
        }

        @Override
        public int getRowCount() {
            return data.length;
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            return data[rowIndex][columnIndex];
        }

        @Override
        public String getColumnName(int column) {
            return columnNames[column];
        }
    }

    // Custom Cell Renderer
    static class SpannedCellRenderer extends DefaultTableCellRenderer {
        private final boolean[][] rowSpanMap;
        private final boolean[][] colSpanMap;

        public SpannedCellRenderer(boolean[][] rowSpanMap, boolean[][] colSpanMap) {
            this.rowSpanMap = rowSpanMap;
            this.colSpanMap = colSpanMap;
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (rowSpanMap[row][column] || colSpanMap[row][column]) {
                c.setBackground(Color.LIGHT_GRAY);
                // Adjust size or positioning as needed
            } else {
                c.setBackground(Color.WHITE);
            }

            return c;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Data for table
                Object[][] data = {
                    {"A", "B", "C", "D"},
                    {"E", "F", "G", "H"},
                    {"I", "J", "K", "L"}
                };
                String[] columnNames = {"Col1", "Col2", "Col3", "Col4"};

                // Define row and column spans (all cells are set to not span in this example)
                boolean[][] rowSpanMap = {
                    {false, false, false, false},
                    {false, false, false, false},
                    {false, false, false, false}
                };
                boolean[][] colSpanMap = {
                    {false, true, true, false},
                    {false, false, false, false},
                    {false, false, false, false}
                };

                // Create table model and table
                SpannedTableModel model = new SpannedTableModel(data, columnNames);
                JTable table = new JTable(model);

                // Set custom renderer
                table.setDefaultRenderer(Object.class, new SpannedCellRenderer(rowSpanMap, colSpanMap));

                // Create and set up the window
                JFrame frame = new JFrame("Spanned Table Example");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLayout(new BorderLayout());
                frame.add(new JScrollPane(table), BorderLayout.CENTER);
                frame.pack();
                frame.setVisible(true);
            }
        });
    }
}
