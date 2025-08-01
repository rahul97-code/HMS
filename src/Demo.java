import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Demo extends JFrame {
    private DefaultTableModel tableModel;
    private JTable table;
    private JComboBox monthComboBox;
    private JComboBox yearComboBox;

    public Demo() {
        setTitle("Hospital Monthly Roster");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel topPanel = new JPanel(new FlowLayout());
        JLabel monthLabel = new JLabel("Month:");
        String[] months = new String[]{
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        };
        monthComboBox = new JComboBox(months);

        JLabel yearLabel = new JLabel("Year:");
        Integer[] years = new Integer[]{2021, 2022, 2023, 2024, 2025};
        yearComboBox = new JComboBox(years);

        JButton loadButton = new JButton("Load");
        loadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadRoster();
            }
        });

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
        int year = ((Integer) yearComboBox.getSelectedItem()).intValue();
        int monthIndex = getMonthIndex(month); // 0-based (January = 0)

        // Clear previous data
        tableModel.setColumnCount(0);
        tableModel.setRowCount(0);

        // Use Calendar to determine number of days
        Calendar calendar = new GregorianCalendar(year, monthIndex, 1);
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        // Set column headers
        for (int i = 1; i <= daysInMonth; i++) {
            tableModel.addColumn(String.valueOf(i));
        }

        // Dummy data for demonstration
        for (int i = 0; i < 10; i++) {
            Object[] rowData = new Object[daysInMonth];
            for (int j = 0; j < daysInMonth; j++) {
                if (j % 2 == 0) {
                    rowData[j] = "Store Dept - Emp " + (i + 1);
                } else {
                    rowData[j] = "Medical Dept - Emp " + (i + 1);
                }
            }
            tableModel.addRow(rowData);
        }
    }

    private int getMonthIndex(String month) {
        if (month.equals("January")) return 0;
        if (month.equals("February")) return 1;
        if (month.equals("March")) return 2;
        if (month.equals("April")) return 3;
        if (month.equals("May")) return 4;
        if (month.equals("June")) return 5;
        if (month.equals("July")) return 6;
        if (month.equals("August")) return 7;
        if (month.equals("September")) return 8;
        if (month.equals("October")) return 9;
        if (month.equals("November")) return 10;
        if (month.equals("December")) return 11;
        return -1;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Demo roster = new Demo();
                roster.setVisible(true);
            }
        });
    }
}
