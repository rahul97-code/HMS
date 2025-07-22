package hms.Printer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PrinterSettingsDialog extends JDialog {
    private JTextField fontField;
    private JTextField yAxisField;
    private JTextField xAxisField;
    private JTextField printerNameField;
    private JButton submitButton;

    public PrinterSettingsDialog(Frame parent) {
        super(parent, "Printer Settings", true);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Font
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Font:"), gbc);

        gbc.gridx = 1;
        fontField = new JTextField(15);
        add(fontField, gbc);

        // Y Axis
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Y Axis:"), gbc);

        gbc.gridx = 1;
        yAxisField = new JTextField(15);
        add(yAxisField, gbc);

        // X Axis
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("X Axis:"), gbc);

        gbc.gridx = 1;
        xAxisField = new JTextField(15);
        add(xAxisField, gbc);

        // Printer Name
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("Printer Name:"), gbc);

        gbc.gridx = 1;
        printerNameField = new JTextField(15);
        add(printerNameField, gbc);

        // Submit Button
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        submitButton = new JButton("Submit");
        add(submitButton, gbc);

        // Button Action
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSubmit();
            }
        });

        pack();
        setLocationRelativeTo(parent);
    }

    private void handleSubmit() {
        String font = fontField.getText();
        String yAxis = yAxisField.getText();
        String xAxis = xAxisField.getText();
        String printerName = printerNameField.getText();

        System.out.println("Font: " + font);
        System.out.println("Y Axis: " + yAxis);
        System.out.println("X Axis: " + xAxis);
        System.out.println("Printer Name: " + printerName);

        // Close the dialog after submission
        dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                final JFrame frame = new JFrame("Main Frame");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(300, 200);
                frame.setLocationRelativeTo(null);

                JButton openDialogButton = new JButton("Open Printer Settings");
                openDialogButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        PrinterSettingsDialog dialog = new PrinterSettingsDialog(frame);
                        dialog.setVisible(true);
                    }
                });

                frame.add(openDialogButton);
                frame.setVisible(true);
            }
        });
    }
}
