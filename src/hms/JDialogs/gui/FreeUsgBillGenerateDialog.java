package hms.JDialogs.gui;

import com.itextpdf.text.DocumentException;
import com.toedter.calendar.JDateChooser;

import hms.patient.slippdf.CivilFreeExamsPdf;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.text.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class FreeUsgBillGenerateDialog extends JDialog {

	private JDateChooser fromDateChooser, toDateChooser;
	private JComboBox itemComboBox;
	private JTextField qtyField;
	private JLabel rateLabel;
	private JButton submitButton;

	// Items with rates
	private Map<String, Double> itemRates = new HashMap<String, Double>();

	public FreeUsgBillGenerateDialog(Frame parent) {
		super(parent, "USG Bills", true);
		setSize(450, 350);
		setResizable(false);
		setLocationRelativeTo(parent); 

		itemRates.put("PHC BOH", 300.0);
		itemRates.put("PHC PALLEDHAR", 350.0);
		itemRates.put("PHC SAHA", 300.0);
		itemRates.put("PHC SAMLEHRI", 300.0);


		JPanel mainPanel = new JPanel(new GridBagLayout());
		mainPanel.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(), "Bill Form",
				TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial", Font.BOLD, 14), Color.DARK_GRAY
				));
		mainPanel.setBackground(Color.WHITE); 
		fromDateChooser = new JDateChooser();
		toDateChooser = new JDateChooser();
		itemComboBox = new JComboBox(itemRates.keySet().toArray());
		qtyField = new JTextField(10);
		rateLabel = new JLabel("Rate: 0.0");
		submitButton = new JButton("Generate Bill");

		Font labelFont = new Font("Segoe UI", Font.PLAIN, 13);
		Font fieldFont = new Font("Segoe UI", Font.PLAIN, 13);

		fromDateChooser.setFont(fieldFont);
		toDateChooser.setFont(fieldFont);
		itemComboBox.setFont(fieldFont);
		qtyField.setFont(fieldFont);
		rateLabel.setFont(labelFont);
		submitButton.setFont(new Font("Segoe UI", Font.BOLD, 13));

		// Numeric filter for qty
		((AbstractDocument) qtyField.getDocument()).setDocumentFilter(new NumericFilter());

		itemComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateRate();
			}
		});

		submitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showResult();
			}
		});

		// Add components to mainPanel
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 0;

		mainPanel.add(new JLabel("From Date:"), gbc);
		gbc.gridx = 1;
		mainPanel.add(fromDateChooser, gbc);

		gbc.gridx = 0;
		gbc.gridy++;
		mainPanel.add(new JLabel("To Date:"), gbc);
		gbc.gridx = 1;
		mainPanel.add(toDateChooser, gbc);

		gbc.gridx = 0;
		gbc.gridy++;
		mainPanel.add(new JLabel("Bill Description:"), gbc);
		gbc.gridx = 1;
		mainPanel.add(itemComboBox, gbc);

		gbc.gridx = 0;
		gbc.gridy++;
		mainPanel.add(new JLabel("Quantity:"), gbc);
		gbc.gridx = 1;
		mainPanel.add(qtyField, gbc);

		gbc.gridx = 0;
		gbc.gridy++;
		mainPanel.add(new JLabel("Rate:"), gbc);
		gbc.gridx = 1;
		mainPanel.add(rateLabel, gbc);

		gbc.gridx = 1;
		gbc.gridy++;
		gbc.anchor = GridBagConstraints.EAST;
		mainPanel.add(submitButton, gbc);

		// Add main panel to dialog
		getContentPane().add(mainPanel);
		updateRate(); // Set initial rate
	}

	private void updateRate() {
		String selectedItem = (String) itemComboBox.getSelectedItem();
		if (selectedItem != null) {
			double rate = itemRates.containsKey(selectedItem) ? itemRates.get(selectedItem) : 0.0;
			rateLabel.setText("Rate: " + rate);
		}
	}

	private void showResult() {
		String item = (String) itemComboBox.getSelectedItem();
		String qtyText = qtyField.getText().trim();

		if (fromDateChooser.getDate() == null) {
			JOptionPane.showMessageDialog(this, "Please select the FROM date.", "Validation Error", JOptionPane.WARNING_MESSAGE);
			return;
		}

		if (toDateChooser.getDate() == null) {
			JOptionPane.showMessageDialog(this, "Please select the TO date.", "Validation Error", JOptionPane.WARNING_MESSAGE);
			return;
		}

		if (fromDateChooser.getDate().after(toDateChooser.getDate())) {
			JOptionPane.showMessageDialog(this, "FROM date cannot be after TO date.", "Date Error", JOptionPane.WARNING_MESSAGE);
			return;
		}

		if (item == null || item.trim().isEmpty()) {
			JOptionPane.showMessageDialog(this, "Please select a bill description.", "Validation Error", JOptionPane.WARNING_MESSAGE);
			return;
		}

		if (qtyText.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Please enter quantity.", "Validation Error", JOptionPane.WARNING_MESSAGE);
			return;
		}

		try {
			double qty = Double.parseDouble(qtyText);
			if (qty <= 0) {
				JOptionPane.showMessageDialog(this, "Quantity must be greater than zero.", "Validation Error", JOptionPane.WARNING_MESSAGE);
				return;
			}

			double rate = itemRates.get(item);
			double amount = qty * rate;
			String fromDate=new SimpleDateFormat("yyyy-MM-dd").format(fromDateChooser.getDate());
			String toDate=new SimpleDateFormat("yyyy-MM-dd").format(toDateChooser.getDate());

			try {
				new CivilFreeExamsPdf(fromDate,toDate,item,qty,rate);
			} catch (DocumentException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(this, "Invalid number format in quantity.", "Input Error", JOptionPane.ERROR_MESSAGE);
		}
	}


	// DocumentFilter to allow only numeric input (with optional one dot)
	static class NumericFilter extends DocumentFilter {
		private static final Pattern DIGIT_PATTERN = Pattern.compile("\\d*\\.?\\d*");

		public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
			String current = fb.getDocument().getText(0, fb.getDocument().getLength());
			StringBuilder sb = new StringBuilder(current);
			sb.insert(offset, string);
			if (DIGIT_PATTERN.matcher(sb.toString()).matches()) {
				super.insertString(fb, offset, string, attr);
			}
		}

		public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
			String current = fb.getDocument().getText(0, fb.getDocument().getLength());
			StringBuilder sb = new StringBuilder(current);
			sb.replace(offset, offset + length, text);
			if (DIGIT_PATTERN.matcher(sb.toString()).matches()) {
				super.replace(fb, offset, length, text, attrs);
			}
		}
	}

	// Main method to test the dialog
	public static void main(String[] args) {
		JFrame dummyParent = new JFrame();
		dummyParent.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		dummyParent.setSize(300, 200);
		dummyParent.setLocationRelativeTo(null);

		FreeUsgBillGenerateDialog dialog = new FreeUsgBillGenerateDialog(dummyParent);
		dialog.setVisible(true);
	}
}
