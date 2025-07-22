package hms.reports.gui;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class SplitCellRenderer extends DefaultTableCellRenderer {

    // This method checks if the cell value should be split into two parts
    private boolean isSplitted(Object value) {
        // Implement your logic to determine if the value should be split
        // For example, return true if the value is of a specific type or format
        return value instanceof String && ((String) value).contains(";");
    }

    // Extract the left part of the value
    private String getLeft(Object value) {
        String text = (String) value;
        return text.split(";")[0];
    }

    // Extract the right part of the value
    private String getRight(Object value) {
        String text = (String) value;
        return text.split(";")[1];
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        if (isSplitted(value)) {
            JPanel panel = new JPanel(new GridLayout(1, 2));
            JLabel left = new JLabel(getLeft(value));
            JLabel right = new JLabel(getRight(value));
            panel.add(left);
            panel.add(right);
            if (isSelected) {
                panel.setBackground(table.getSelectionBackground());
                left.setForeground(table.getSelectionForeground());
                right.setForeground(table.getSelectionForeground());
            } else {
                panel.setBackground(table.getBackground());
                left.setForeground(table.getForeground());
                right.setForeground(table.getForeground());
            }
            return panel;
        } else {
            // Use the default renderer for non-split cells
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            return c;
        }
    }

    public static void main(String[] args) {
        // Sample usage
        JFrame frame = new JFrame("Split Cell Renderer Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        // Create sample data
        String[][] data = {
            {"Left;Right", "Normal"},
            {"Another;Split", "Text"},
            {"NoSplit", "Example"}
        };
        String[] columns = {"Column 1", "Column 2"};

        JTable table = new JTable(data, columns);
        table.setDefaultRenderer(Object.class, new SplitCellRenderer());

        frame.add(new JScrollPane(table));
        frame.setVisible(true);
    }
}
