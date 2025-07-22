import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;

public class demo extends JFrame {
    private DefaultTableModel tableModel;
    private JTable table;
    private JComboBox<String> monthComboBox;
    private JComboBox<Integer> yearComboBox;

    public demo() {
        setTitle("Hospital Monthly Roster");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel topPanel = new JPanel(new FlowLayout());
        JLabel monthLabel = new JLabel("Month:");
        monthComboBox = new JComboBox<>(new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"});
        JLabel yearLabel = new JLabel("Year:");
        yearComboBox = new JComboBox<>(new Integer[]{2021, 2022, 2023, 2024, 2025}); // Adjust years as needed
        JButton loadButton = new JButton("Load");
        loadButton.addActionListener(e -> loadRoster());

        topPanel.add(monthLabel);
        topPanel.add(monthComboBox);
        topPanel.add(yearLabel);
        topPanel.add(yearComboBox);
        topPanel.add(loadButton);

        tableModel = new DefaultTableModel();
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(topPanel, BorderLayout.NORTH);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
    }

    private void loadRoster() {
        String month = (String) monthComboBox.getSelectedItem();
        int year = (int) yearComboBox.getSelectedItem();

        // Clear previous data
        tableModel.setColumnCount(0);
        tableModel.setRowCount(0);

        // Set column headers
        LocalDate date = LocalDate.of(year, getMonthIndex(month) + 1, 1);
        int daysInMonth = date.lengthOfMonth();
        for (int i = 1; i <= daysInMonth; i++) {
            tableModel.addColumn(String.valueOf(i));
        }

        // Load roster data (You can replace this with your actual data loading logic)
        // For demonstration purpose, just filling the cells with dummy data
        for (int i = 0; i < 10; i++) { // Assuming 10 employees shared between departments
            Object[] rowData = new Object[daysInMonth];
            for (int j = 0; j < daysInMonth; j++) {
                // For simplicity, alternating between the two departments
                if (j % 2 == 0) {
                    rowData[j] = "Store Dept - Employee " + (i + 1);
                } else {
                    rowData[j] = "Medical Dept - Employee " + (i + 1);
                }
            }
            tableModel.addRow(rowData);
        }
    }

    private int getMonthIndex(String month) {
        switch (month) {
            case "January":
                return 0;
            case "February":
                return 1;
            case "March":
                return 2;
            case "April":
                return 3;
            case "May":
                return 4;
            case "June":
                return 5;
            case "July":
                return 6;
            case "August":
                return 7;
            case "September":
                return 8;
            case "October":
                return 9;
            case "November":
                return 10;
            case "December":
                return 11;
            default:
                return -1;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            demo roster = new demo();
            roster.setVisible(true);
        });
    }
}
