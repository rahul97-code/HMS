package hms.mrd.gui;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import javax.swing.table.DefaultTableModel;

import hms.mrd.database.MRDUserDBConnection;

import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.border.BevelBorder;

public class MrdSystem extends JDialog {


	private int xPosition = 20;
	boolean bool =true;// Initial X position for the first button
	boolean bool1 =true;// Initial X position for the first button
	private int yPosition = 20; // Initial Y position
	private final int MAX_WIDTH = 800; // Frame width
	private static JPanel panel;
	String btnName="Add Rack";
	private JTable table;
	private DefaultTableModel tableModel;
	protected String clickbtn = ""; // Store reference to the last clicked button
	protected int rackID;
	private static JPanel panel_4;
	private JScrollPane scrollPane_1;
	int xrowPosition = 20;
	int yrowPosition = 20;
	int rowMAX_WIDTH = 400; // Maximum width of the panel
	private JTextField searchTf;
	private JTextField currentRackTf;
	private JTextField currentRowTf;
	private JTextField yearTf;
	private JButton editRowBtn;
	private JTextField startIPDTf;
	private JTextField endIPDTf;
	private JTextField rowTf;
	private String roomNo = "";
	private String roomName = "";
	public JButton clearBtn;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		// Using anonymous class since Java 7 doesn't support lambda expressions
		EventQueue.invokeLater(new Runnable() {


			public void run() {
				try {
					new MrdSystem().setVisible(true); 
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public MrdSystem() {
		
		setBounds(250, 100, 1208, 691);
		setLocationRelativeTo(null);    // Center the dialog
	    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    setTitle("Patient Record Details");
	    getContentPane().setLayout(null);
	    setModal(true);

		panel = new JPanel();
		panel.setBorder(new TitledBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null), "Rack Data", TitledBorder.LEFT, TitledBorder.TOP, null, SystemColor.activeCaptionText));
		panel.setLayout(null); // Set layout to null for absolute positioning

		JScrollPane scrollPane = new JScrollPane(panel);
		scrollPane.setBounds(22, 62, 727, 200); // Set the size of the scroll pane
		getContentPane().add(scrollPane);

		
		// Create the main panel
		panel_4 = new JPanel();
		panel_4.setBorder(new TitledBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null), "Row Data", TitledBorder.LEFT, TitledBorder.TOP, null, SystemColor.activeCaptionText));

		// Set the layout to null for absolute positioning
		panel_4.setLayout(null);

		// Create a JScrollPane and set the bounds for it
		JScrollPane scrollPane_1 = new JScrollPane(panel_4);
		scrollPane_1.setBounds(759, 62, 410, 200); // Set the size and position of the scroll pane

		// Add the scroll pane to the content pane
		getContentPane().add(scrollPane_1);

		// Example: Add dynamic buttons to the panel
// Global variables for button positioning
		
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		// Initialize panel_1 for JTable
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null), "Row Data", TitledBorder.LEFT, TitledBorder.TOP, null, SystemColor.activeCaptionText));
		panel_1.setBounds(22, 309, 1148, 265); // Set position of panel_1
		getContentPane().add(panel_1);


		// Create the table with dynamic data
		String[] columns = {"Patient_ID", "Ipd Id", "P_Name", "Insurance_Name"}; // Define the column names
		tableModel = new DefaultTableModel(columns, 0); // Default model with no rows initially
		table = new JTable(tableModel);


		// Wrap the JTable in a JScrollPane
		JScrollPane tableScrollPane = new JScrollPane(table);
		panel_1.setLayout(new BorderLayout());
		panel_1.add(tableScrollPane, BorderLayout.CENTER);

		JButton cancelBtn = new JButton("Cancel");
		cancelBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		cancelBtn.setBounds(1047, 593, 109, 50);
		getContentPane().add(cancelBtn);
		
		JButton addBtn = new JButton("Add ");
		addBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new_record newrecord = new new_record();
				newrecord.setModal(true);
				newrecord.setVisible(true);
			}
			
		});
		addBtn.setBounds(911, 593, 109, 50);
		getContentPane().add(addBtn);
		
		JLabel lblSearch = new JLabel("Ipd Search : ");
		lblSearch.setBounds(47, 25, 90, 26);
		getContentPane().add(lblSearch);
		
		searchTf = new JTextField();
		searchTf.setBounds(142, 25, 205, 26);
		getContentPane().add(searchTf);
		searchTf.setColumns(10);
		
		
		JButton searchBtn = new JButton("Search");
		searchBtn.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        MRDUserDBConnection db = new MRDUserDBConnection();
		        
		        // Get the input from the text field
		        String ipdid = searchTf.getText().trim(); // Added trim to avoid issues with extra spaces
		        
		        // Validate if the input is empty
		        if (ipdid.isEmpty()) {
		            JOptionPane.showMessageDialog(null, "IPD value can't be null or empty.", "Validation Error", JOptionPane.ERROR_MESSAGE);
		            currentRackTf.setText("");
		    	    currentRowTf.setText("");
		    	    yearTf.setText("");
                    startIPDTf.setText("");
                    endIPDTf.setText("");
                    rowTf.setText("");
                    editRowBtn.setEnabled(false);
		            return; // Exit the method if input is empty
		        }

		        // Perform the database search
		        ResultSet rs = null;
		        try {
		            rs = db.searchipd(ipdid); // Execute the search query

		            // Check if the ResultSet has any rows
		            if (!rs.next()) {
		                // If no rows are returned, show a message
		                JOptionPane.showMessageDialog(null, "No data found for the provided IPD.", "No Results", JOptionPane.INFORMATION_MESSAGE);
		                
		                // Reset any data or UI elements when no result is found
		                RetriveRackData();
		                RetriveRackRowData(null);
		                currentRackTf.setText("");
		        	    currentRowTf.setText("");
		        	    yearTf.setText("");
	                    startIPDTf.setText("");
	                    endIPDTf.setText("");
	                    rowTf.setText("");
	                    editRowBtn.setEnabled(false);
		        	    
		                // Refresh the panel to reflect any changes
		                panel_4.revalidate(); // Ensures that the layout manager is notified of any changes
		                panel_4.repaint(); // Redraws the panel to reflect changes
		                return; // Exit if no data found
		            }

		            // Process the data in the ResultSet
		            do {
		                String rack_no = rs.getObject(1).toString(); // Assuming first column is rack_no
		                String row = rs.getObject(2).toString(); // Assuming second column is row
		                System.out.println(rack_no + " row " + row);
		                clickbtn = rack_no;
		                selectRackAndRow(rack_no, row); // Call your method to handle the data
		                
		            } while (rs.next()); // Continue processing if there are more rows
		            
		        } catch (SQLException e1) {
		            // Print stack trace for detailed error information
		            e1.printStackTrace();
		            JOptionPane.showMessageDialog(null, "Error occurred while fetching data: " + e1.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
		        } finally {
		            // Ensure resources are closed in the finally block to prevent memory leaks
		            try {
		                if (rs != null) {
		                    rs.close(); // Close the ResultSet
		                }
		                db.closeConnection(); // Close the database connection
		            } catch (SQLException e2) {
		                e2.printStackTrace(); // Log any errors encountered while closing resources
		            }
		        }
		    }
		});


		searchBtn.setBounds(374, 25, 89, 26);
		getContentPane().add(searchBtn);
		
		 clearBtn = new JButton("Clear");
		clearBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 RetriveRackData();
			        RetriveRackRowData(null);

			        // If any UI elements need to be updated or reset, you might want to use both revalidate() and repaint()
			        panel_4.revalidate(); // Ensures that the layout manager is notified of any changes
			        panel_4.repaint(); // Redraws the panel to reflect changes
			        searchTf.setText(""); // Clear the text field
			        currentRackTf.setText("");
			        currentRowTf.setText("");
			        yearTf.setText("");
                    startIPDTf.setText("");
                    endIPDTf.setText("");
                    rowTf.setText("");
                    editRowBtn.setEnabled(false);
			}
		});
		clearBtn.setBounds(481, 28, 89, 23);
		getContentPane().add(clearBtn);
		
		JLabel clearlbl = new JLabel("Rack No :");
		clearlbl.setBounds(60, 278, 102, 26);
		getContentPane().add(clearlbl);
		
		currentRackTf = new JTextField();
		currentRackTf.setEditable(false);
		currentRackTf.setBounds(172, 278, 205, 26);
		getContentPane().add(currentRackTf);
		currentRackTf.setColumns(10);
		
		JLabel rowlbl = new JLabel("Row No :");
		rowlbl.setBounds(796, 278, 82, 26);
		getContentPane().add(rowlbl);
		
		currentRowTf = new JTextField();
		currentRowTf.setEditable(false);
		currentRowTf.setColumns(10);
		currentRowTf.setBounds(916, 278, 240, 26);
		getContentPane().add(currentRowTf);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null), "Edit Row", TitledBorder.CENTER, TitledBorder.TOP, null, null));
		panel_2.setBounds(22, 579, 856, 64);
		getContentPane().add(panel_2);
		panel_2.setLayout(null);
		
		yearTf = new JTextField();
		yearTf.setBounds(58, 18, 68, 26);
		panel_2.add(yearTf);
		yearTf.setColumns(10);
		
		JLabel txtrow = new JLabel("Row :");
		txtrow.setBounds(550, 18, 49, 26);
		panel_2.add(txtrow);
		
		JLabel startIPDlbl = new JLabel("Start IPD :");
		startIPDlbl.setBounds(150, 18, 62, 26);
		panel_2.add(startIPDlbl);
		
		startIPDTf = new JTextField();
		startIPDTf.setBounds(222, 18, 96, 26);
		panel_2.add(startIPDTf);
		startIPDTf.setColumns(10);
		
	    editRowBtn = new JButton("Edit Row");
	    editRowBtn.setBounds(730, 17, 95, 26);
	    panel_2.add(editRowBtn);
	   
	    
	    endIPDTf = new JTextField();
	    endIPDTf.setColumns(10);
	    endIPDTf.setBounds(420, 18, 96, 26);
	    panel_2.add(endIPDTf);
	    
	    JLabel endIPDlbl = new JLabel("End IPD :");
	    endIPDlbl.setBounds(347, 18, 61, 26);
	    panel_2.add(endIPDlbl);
	    
	    rowTf = new JTextField();
	    rowTf.setEditable(false);
	    rowTf.setColumns(10);
	    rowTf.setBounds(612, 18, 89, 26);
	    panel_2.add(rowTf);
	    
	    JLabel yearlbl = new JLabel("Year : ");
	    yearlbl.setBounds(10, 18, 49, 26);
	    panel_2.add(yearlbl);
	    editRowBtn.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	            // Collect input values
	            String ipdStart = startIPDTf.getText().trim();
	            String ipdEnd = endIPDTf.getText().trim();
	            String year = yearTf.getText().trim();
	            String rowNo = rowTf.getText().trim();
	            clickbtn=clickbtn.trim();

	            // Validate that none are empty
	            if (ipdStart.isEmpty() || ipdEnd.isEmpty() || year.isEmpty() || rowNo.isEmpty() || clickbtn.isEmpty()) {
	                JOptionPane.showMessageDialog(null, "All fields must be filled.", "Validation Error", JOptionPane.WARNING_MESSAGE);
	                return;
	            }

	            // Validate that IPD and Year values are numbers
	            try {
	                int ipdStartVal = Integer.parseInt(ipdStart);
	                int ipdEndVal = Integer.parseInt(ipdEnd);
	                int yearVal = Integer.parseInt(year);
	                int rowVal = Integer.parseInt(rowNo);

	                // Optional: Check value ranges (e.g., year must be >= 1900)
	                if (yearVal < 1900 || yearVal > 3000) {
	                    JOptionPane.showMessageDialog(null, "Enter a valid year (1900–3000).", "Validation Error", JOptionPane.WARNING_MESSAGE);
	                    return;
	                }

	                if (ipdStartVal > ipdEndVal) {
	                    JOptionPane.showMessageDialog(null, "Start IPD shou``ld not be greater than End IPD.", "Validation Error", JOptionPane.WARNING_MESSAGE);
	                    return;
	                }

	                // Call DB method if everything is valid
	                MRDUserDBConnection db = new MRDUserDBConnection();
	                boolean success = db.updateIPDData(ipdStart, ipdEnd, year, roomNo, roomName, clickbtn, rowNo);
	                db.closeConnection();

	                if (success) {
	                    JOptionPane.showMessageDialog(null, "Data updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
	                    RetriveRackData();
				        RetriveRackRowData(null);

				        // If any UI elements need to be updated or reset, you might want to use both revalidate() and repaint()
				        panel_4.revalidate(); // Ensures that the layout manager is notified of any changes
				        panel_4.repaint(); // Redraws the panel to reflect changes
				        searchTf.setText(""); // Clear the text field
				        currentRackTf.setText("");
				        currentRowTf.setText("");
				        yearTf.setText("");
	                    startIPDTf.setText("");
	                    endIPDTf.setText("");
	                    rowTf.setText("");
	                    editRowBtn.setEnabled(false);
	                    clickbtn = "";
	                } else {
	                    JOptionPane.showMessageDialog(null, "No matching record found or update failed.", "Update Failed", JOptionPane.WARNING_MESSAGE);
	                }

	            } catch (NumberFormatException ex) {
	                JOptionPane.showMessageDialog(null, "IPD Start, IPD End, Year, and Row must be valid integers.", "Validation Error", JOptionPane.WARNING_MESSAGE);
	            }
	        }
	    });


		String[] years = new String[41];
		years[0]="Select Year";// Array of size 41 (2010 to 2050 inclusive)
		for (int i = 1; i < 41; i++) {
			years[i] = String.valueOf(2009 + i); // Populate the array with years
		}
		
		
		
		RetriveRackData();

	}

	


	private void addDynamicrowButton(final String buttonName) {
				   
					 // Fixed width and height for the buttons
				    final int BUTTON_WIDTH = 350;
				    final int BUTTON_HEIGHT = 50;

				    
				 // Increment the xPosition for the next button
					xrowPosition += BUTTON_WIDTH + 20;  // Add space between buttons (button width + some padding)
					if(bool1) {
						xrowPosition= 20;
						bool1=false;
					}

				    // Check if the button exceeds the panel's width (wrap to the next row)
				    if (xrowPosition + BUTTON_WIDTH > rowMAX_WIDTH) {
				        // Reset xPosition and move to the next row
				    	xrowPosition = 20;
				    	yrowPosition += BUTTON_HEIGHT + 20; // Add space between rows (button height + padding)
				    }

				    // Create the button
				    final JButton button = new JButton(buttonName);
				    button.setBounds(xrowPosition, yrowPosition, BUTTON_WIDTH, BUTTON_HEIGHT); // Set position and size

				    // Set the button's background color to sky blue (default color)
				    button.setBackground(new Color(135, 206, 235)); // Sky blue color
				    button.setFont(new Font("Arial", Font.BOLD, 14)); // Set font style and size

				    // Add ActionListener to handle button clicks
				    button.addActionListener(new ActionListener() {
				        public void actionPerformed(ActionEvent e) {
				            MRDUserDBConnection db = new MRDUserDBConnection();
				            System.out.println(buttonName + " clicked");
				            button.setBackground(Color.GREEN);
				            currentRowTf.setText(buttonName);
				            
				            for (Component comp : panel_4.getComponents()) {
				                if (comp instanceof JButton) {
				                    JButton otherButton = (JButton) comp;
				                    if (otherButton != button) {
				                        otherButton.setBackground(new Color(135, 206, 235)); // Default color
				                    }
				                }
				            }


				            String[] result = extractRangeAsStrings(buttonName);
				            
				            if (result != null) {
				                String ipdStart = result[0];
				                String ipdEnd = result[1];
				                String rowN = result[2];
				                String year = result[3];
				                System.out.println("ipdStart = " + ipdStart);
				                System.out.println("ipdEnd = " + ipdEnd);
                                yearTf.setText(rowN);
                                startIPDTf.setText(ipdStart);
                                endIPDTf.setText(ipdEnd);
                                rowTf.setText(year);
                                editRowBtn.setEnabled(true);
				                RetriveRackRawData(ipdStart , ipdEnd);
				                
				            } else {
				                System.out.println("Could not extract range.");
				            }
//				            panel_4.removeAll();
				         // Reset the color of the previous clicked button to default (if any)
							
				        }     
				    });

				    // Add the button to the panel
				    panel_4.add(button);

				    // Update the panel's preferred size to accommodate the new button
				    panel_4.setPreferredSize(new Dimension(200, yrowPosition + BUTTON_HEIGHT + 20)); // Adjust size based on added buttons

				    // Refresh the frame to display the new button
				    panel_4.revalidate();
				    panel_4.repaint();
				

	}

	public void addDynamicButton(final String buttonName) {
		// St a fixed width for the buttons

		final int BUTTON_WIDTH = 200;
		final int BUTTON_HEIGHT = 50;

		// Increment the xPosition for the next button
		xPosition += BUTTON_WIDTH + 20;  // Add space between buttons (button width + some padding)
		if(bool) {
			xPosition= 20;
			bool=false;
		}

		// Check if the button exceeds the frame's width (wrap to the next row)
		if (xPosition + BUTTON_WIDTH > MAX_WIDTH) {
			// Reset xPosition and move to the next row
			xPosition = 20;
			yPosition += BUTTON_HEIGHT + 20; // Add space between rows (button height + padding)
		}

		// Create the button
		final JButton button = new JButton(buttonName);
		button.setBounds(xPosition, yPosition, BUTTON_WIDTH, BUTTON_HEIGHT); // Set position and size

		// Set the button's background color to sky blue (default color)
		button.setBackground(new Color(135, 206, 235));

		// Anonymous class for ActionListener (no lambdas in Java 7)
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				RetriveRackData();
				MRDUserDBConnection db = new MRDUserDBConnection();
				System.out.println(buttonName + " clicked");
				clickbtn = buttonName;
				RetriveAllData(buttonName);
				// Change the clicked button color to green
				button.setBackground(Color.GREEN);
				currentRackTf.setText(buttonName);
				 yearTf.setText("");
                 startIPDTf.setText("");
                 endIPDTf.setText("");
                 rowTf.setText("");
                 editRowBtn.setEnabled(false);
                 
                 currentRowTf.setText("");
                 searchTf.setText("");
				for (Component comp : panel.getComponents()) {
		            if (comp instanceof JButton) {
		                JButton otherButton = (JButton) comp;
		                if (otherButton != button) {
		                    otherButton.setBackground(new Color(135, 206, 235)); // Default color
		                }
		            }
		        }

				RetriveRackRowData(buttonName);
				// Reset the color of the previous clicked button to default (if any)
				
			
				db.closeConnection();
			}
		});

		// Add the button to the panel
		panel.add(button);

		// Update the panel's preferred size to accommodate the new button
		panel.setPreferredSize(new Dimension(MAX_WIDTH, yPosition + BUTTON_HEIGHT + 20)); // Adjust size based on added buttons

		// Refresh the frame to display the new button
		revalidate();
		repaint();
	}
	
	


	public  void RetriveRackData() {
		MRDUserDBConnection db = new MRDUserDBConnection();
		ResultSet rs = db.retrieveRackData();
		panel.removeAll();
		xPosition = 20;
		yPosition = 20;
		bool = true;
		try {
			while(rs.next()) {
				addDynamicButton(rs.getObject(1).toString());
				
			}
			db.closeConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			db.closeConnection();
			e.printStackTrace();
		}
	}
	public  void RetriveAllData(String btn) {
		MRDUserDBConnection db = new MRDUserDBConnection();
		ResultSet rs = db.retrieveAllData(btn);
		roomNo = "";
		roomName = "";
		try {
			while(rs.next()) {
				roomNo = rs.getObject(1).toString();
				roomName = rs.getObject(2).toString();
				
			}
			db.closeConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			db.closeConnection();
			e.printStackTrace();
		}
	}
	
	public  void RetriveRackRowData(String rackno) {
		MRDUserDBConnection db = new MRDUserDBConnection();
		ResultSet rs = db.retrieveRackRowData1(rackno);
		System.out.println("name of data ");
		panel_4.removeAll();
		xrowPosition = 20;
		yrowPosition = 20;
		tableModel.setRowCount(0);
		bool1 = true;
		try {
			while(rs.next()) {
				addDynamicrowButton(rs.getObject(1).toString());
			}
			db.closeConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			db.closeConnection();
			e.printStackTrace();
		}
	}
	
	
	public static String[] extractRangeAsStrings(String input) {
		 String ipdStart = null, ipdEnd = null, year = null, rowNo = null;

		    // Match IPD range like "0 To 0"
		    java.util.regex.Matcher rangeMatcher = java.util.regex.Pattern
		        .compile("(\\d+)\\s+To\\s+(\\d+)", java.util.regex.Pattern.CASE_INSENSITIVE)
		        .matcher(input);
		    if (rangeMatcher.find()) {
		        ipdStart = rangeMatcher.group(1);  // e.g., "0"
		        ipdEnd = rangeMatcher.group(2);    // e.g., "0"
		    }

		    // Match year like "year 0"
		    java.util.regex.Matcher yearMatcher = java.util.regex.Pattern
		        .compile("year\\s+(\\d+)", java.util.regex.Pattern.CASE_INSENSITIVE)
		        .matcher(input);
		    if (yearMatcher.find()) {
		        year = yearMatcher.group(1);  // e.g., "0"
		    }

		    // Match row like "Row 2"
		    java.util.regex.Matcher rowMatcher = java.util.regex.Pattern
		        .compile("Row\\s+(\\d+)", java.util.regex.Pattern.CASE_INSENSITIVE)
		        .matcher(input);
		    if (rowMatcher.find()) {
		        rowNo = rowMatcher.group(1);  // e.g., "2"
		    }

		    // Check all values are captured (null-safe, not value-safe)
		    if (ipdStart != null && ipdEnd != null && year != null && rowNo != null) {
		        return new String[]{ipdStart, ipdEnd, year, rowNo};
		    } else {
		        return null;
		    }
    }
	
	public void selectRackAndRow(String rackNo, String row) {
	    // Iterate through the dynamic buttons in the panel to find and select the correct rack button
	    for (Component comp : panel.getComponents()) {
	        if (comp instanceof JButton) {
	            JButton rackButton = (JButton) comp;
	            if (rackButton.getText().equals(rackNo)) {
	                // Change rack button color to indicate selection
	                rackButton.setBackground(Color.GREEN);
	                RetriveRackRowData(rackNo);
	            } else {
	                rackButton.setBackground(new Color(135, 206, 235)); // Reset to default
	            }
	        }
	    }
	    // Iterate through the dynamic row buttons in panel_4 to find and select the correct row button
	    for (Component comp : panel_4.getComponents()) {
	        if (comp instanceof JButton) {
	            JButton rowButton = (JButton) comp;
	            if (rowButton.getText().equals(row)) {
	                // Change row button color to indicate selection
	                rowButton.setBackground(Color.GREEN);
	                tableModel.setRowCount(0);
		            String[] result = extractRangeAsStrings(row);
		            
		            if (result != null) {
		                String ipdStart = result[0];
		                String ipdEnd = result[1];
		                String rowN = result[2];
		                String year = result[3];
		                System.out.println("ipdStart = " + ipdStart);
		                System.out.println("ipdEnd = " + ipdEnd);
		                
                        yearTf.setText(rowN);
                        startIPDTf.setText(ipdStart);
                        endIPDTf.setText(ipdEnd);
                        rowTf.setText(year);
                        editRowBtn.setEnabled(true);
		                RetriveRackRawData(ipdStart , ipdEnd);
		                highlightTableRow(searchTf.getText().toString());  // This will highlight the table row based on IPD ID

		                
		            } else {
		                System.out.println("Could not extract range.");
		            }
	            } else {
	                rowButton.setBackground(new Color(135, 206, 235)); // Reset to default
	            }
	        }
	    }
	   
	    RetriveAllData(rackNo);
	    currentRackTf.setText(rackNo);
	    currentRowTf.setText(row);
	    
	}	
	
	
	public void highlightTableRow(String ipd) {
	    // Clear any previous selection
	    table.clearSelection();

	    // Iterate through all rows in the table to find the row that matches the IPD ID
	    for (int i = 0; i < tableModel.getRowCount(); i++) {
	        // Get the IPD value from the first column (you can change this column index if needed)
	        String tableIPD = tableModel.getValueAt(i, 1).toString();
	        
	        // If we find a match with the IPD, highlight the row
	        if (tableIPD.equals(ipd)) {
	            // Select the matching row
	            table.setRowSelectionInterval(i, i);
	            
	            // Highlight the background color of the selected row
	            table.setSelectionBackground(Color.GREEN);
	            
	            // Scroll the table to make sure the row is visible
	            Rectangle rect = table.getCellRect(i, 2, true);  // Get the rectangle for the cell
	            table.scrollRectToVisible(rect);  // Scroll the table to the cell (this brings the row into view)
	            break;  // Exit loop once the row is found
	        }
	    }
	}

	
	public void RetriveRackRawData(String ipd_start, String ipd_end) {
		MRDUserDBConnection db = new MRDUserDBConnection();
		ResultSet rs = db.retrieveRackRowData(ipd_start,ipd_end);

		try {
			// Clear existing data in the table (if any)
			tableModel.setRowCount(0);

			while (rs.next()) {
				// Retrieve data from the result set and add to the table model
				String p_id = rs.getString("p_id"); // Replace with actual column name
				String ipd_id = rs.getString("ipd_id"); // Replace with actual column name
				String p_name = rs.getString("p_name"); // Replace with actual column name
				String insurance_name = rs.getString("insurance_type"); // Replace with actual column name

				tableModel.addRow(new Object[]{p_id,ipd_id, p_name, insurance_name});
			}
			db.closeConnection();
		} catch (SQLException e) {
			db.closeConnection();
			e.printStackTrace();
		}
	}
}

