package hms.test.gui;

import hms.exam.database.ReferenceTableDBConnection;
import hms.test.database.TestResultDBConnection;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.sun.media.ui.ComboBox;

public class TestReferencePanel extends JPanel {
	public JTextField lowerLimitTB;
	public JTextField upperLimitTB;
	public JTextField enterValueTB;
	public String lowerLimitSTR,upperLimitSTR,unitSTR,commentSTR;
	public JLabel lblUnits;
	public JTextArea commentsTA;
	public JLabel lblTestName;
	public JLabel lblResults;
	JComboBox comboBox ;
	JTextArea textArea ;
	public boolean valueSet=false;
	public String ref_id="";
	/**
	 * Create the panel.
	 */
	public TestReferencePanel(String index,String examCounter) {
		setLayout(null);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_2.setBounds(10, 11, 1174, 87);
		add(panel_2);
		 valueSet=false;
		panel_2.setLayout(null);
		lblTestName = new JLabel("Test Name");
		lblTestName.setForeground(new Color(0, 0, 255));
		lblTestName.setHorizontalAlignment(SwingConstants.CENTER);
		lblTestName.setBounds(10, 24, 183, 31);
		panel_2.add(lblTestName);
		lblTestName.setFont(new Font("Tahoma", Font.BOLD, 14));
		
		JLabel lblLowerLimit = new JLabel("Lower Limit :");
		lblLowerLimit.setBounds(217, 5, 75, 14);
		panel_2.add(lblLowerLimit);
		lblLowerLimit.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		lowerLimitTB = new JTextField();
		lowerLimitTB.setEditable(false);
		lowerLimitTB.setBounds(203, 30, 102, 22);
		panel_2.add(lowerLimitTB);
		lowerLimitTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lowerLimitTB.setColumns(10);
		
		JLabel lblUpperLimit = new JLabel("Upper Limit :");
		lblUpperLimit.setBounds(340, 5, 70, 14);
		panel_2.add(lblUpperLimit);
		lblUpperLimit.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		upperLimitTB = new JTextField();
		upperLimitTB.setEditable(false);
		upperLimitTB.setBounds(315, 30, 119, 22);
		panel_2.add(upperLimitTB);
		upperLimitTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		upperLimitTB.setColumns(10);
		
		JPanel panel = new JPanel();
		panel.setBounds(583, 60, 221, 1);
		panel_2.add(panel);
		panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Comments", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(6, 16, 205, 70);
		panel.add(scrollPane);
		
		commentsTA = new JTextArea();
		commentsTA.setFont(new Font("Tahoma", Font.PLAIN, 12));
		commentsTA.setLineWrap(true);
		commentsTA.setRows(6);
		scrollPane.setViewportView(commentsTA);
		
		
		JLabel lblEnterValue = new JLabel("Enter Value");
		lblEnterValue.setBounds(530, 5, 75, 14);
		panel_2.add(lblEnterValue);
		lblEnterValue.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		enterValueTB = new JTextField();
		enterValueTB.setBounds(531, 30, 97, 22);
		panel_2.add(enterValueTB);
		
		
//		String signs[]={">","<"};        
		enterValueTB.addKeyListener(new KeyAdapter() {
            @Override
			public void keyTyped(KeyEvent e) {
                char vChar = e.getKeyChar();
                if (!(Character.isDigit(vChar)
                        || (vChar == KeyEvent.VK_BACK_SPACE)
                        || (vChar == KeyEvent.VK_DELETE)||vChar== '.')) {
                    e.consume();
                    
                    //||vChar== '.'
                }
            }
        });
		enterValueTB.getDocument().addDocumentListener(
				new DocumentListener() {
					@Override
					public void insertUpdate(DocumentEvent e) {
						String str = enterValueTB.getText() + "";
						if (str.equals("")) {
							lblResults.setText("");
						} else {
							double value=Double.parseDouble("0"+str);
							double lowerValue=Double.parseDouble(lowerLimitSTR);
							double upperValue=Double.parseDouble(upperLimitSTR);
							
							if(value<=upperValue&&lowerValue<=value)
							{
								System.out.println("Value with in range");
								lblResults.setText("Within Range");
								lblResults.setForeground(Color.GREEN);
							}
							else {
								System.out.println("Value out of range");
								lblResults.setText("Out of Range");
								lblResults.setForeground(Color.RED);
							}
						}
					}
					@Override
					public void removeUpdate(DocumentEvent e) {
						String str = enterValueTB.getText() + "";
						if (str.equals("")) {
							lblResults.setText("");
						} else {
							double value=Double.parseDouble("0"+str);
							double lowerValue=Double.parseDouble(lowerLimitSTR);
							double upperValue=Double.parseDouble(upperLimitSTR);
							
							if(value<=upperValue&&lowerValue<=value)
							{
								System.out.println("Value with in range");
								lblResults.setText("Within Range");
								lblResults.setForeground(Color.GREEN);
							}
							else {
								System.out.println("Value out of range");
								lblResults.setText("Out of Range");
								lblResults.setForeground(Color.RED);
							}
						}
					}

					@Override
					public void changedUpdate(DocumentEvent e) {
						String str = enterValueTB.getText() + "";
						if (str.equals("")) {
							lblResults.setText("");
						} else {
							double value=Double.parseDouble("0"+str);
							double lowerValue=Double.parseDouble(lowerLimitSTR);
							double upperValue=Double.parseDouble(upperLimitSTR);
							
							if(value<=upperValue&&lowerValue<=value)
							{
								System.out.println("Value with in range");
								lblResults.setText("Within Range");
								lblResults.setForeground(Color.GREEN);
							}
							else {
								System.out.println("Value out of range");
								lblResults.setText("Out of Range");
								lblResults.setForeground(Color.RED);
							}
						}
					}
				});
		enterValueTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		enterValueTB.setColumns(10);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(876, 24, 137, 42);
		panel_2.add(panel_1);
		panel_1.setBackground(Color.LIGHT_GRAY);
		panel_1.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setLayout(null);
		
		lblResults = new JLabel("Results");
		lblResults.setBounds(0, 0, 137, 42);
		panel_1.add(lblResults);
		lblResults.setHorizontalAlignment(SwingConstants.CENTER);
		lblResults.setFont(new Font("Tahoma", Font.BOLD, 16));
		
		 comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {
				 "None","=",">", "<" }));
		comboBox.setBounds(436, 31, 70, 20);
//		comboBox.setVisible(false);
		panel_2.add(comboBox);
		
		 textArea = new JTextArea();
		textArea.setBounds(661, 29, 183, 35);
		panel_2.add(textArea);
		
		JLabel lblComment = new JLabel("Comment");
		lblComment.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblComment.setBounds(694, 6, 75, 14);
		panel_2.add(lblComment);
//		textArea.setVisible(false);
		
		lblUnits = new JLabel("units");
		lblUnits.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblUnits.setBounds(469, 40, 46, 14);
		add(lblUnits);
		getExamReferenceRange(index);
		getData(examCounter);
	}
	public void getExamReferenceRange(String index) {
		lowerLimitSTR="";
		upperLimitSTR="";
		unitSTR="";
		commentSTR="";
		System.out.println("hello h r u ?"+lowerLimitSTR);
		ReferenceTableDBConnection dbConnection = new ReferenceTableDBConnection();
		ResultSet resultSet = dbConnection.retrieveReferenceRangeWithId(index);
		try {
			while (resultSet.next()) {
				lowerLimitSTR=resultSet.getObject(1).toString();
				upperLimitSTR=(resultSet.getObject(2).toString());
				unitSTR=(resultSet.getObject(3).toString());
				commentSTR=(resultSet.getObject(4).toString());
				lblTestName.setText(""+resultSet.getObject(5).toString());
			//	System.out.println(""+lowerLimitSTR);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
		lowerLimitTB.setText(lowerLimitSTR+" "+unitSTR);
		upperLimitTB.setText(upperLimitSTR+" "+unitSTR);
		lblUnits.setText(unitSTR);
		commentsTA.setText(commentSTR);
		enterValueTB.setText("");
		lblResults.setText("");
	}
	
	public void getData(String examCounter)
	{
	//	System.out.println(lblTestName.getText().toString()+"  ;;;;;  "+examCounter);
		TestResultDBConnection dbConnection = new TestResultDBConnection();
		ResultSet resultSet = dbConnection.retrieveDATA(examCounter, lblTestName.getText().toString());
		try {
			while (resultSet.next()) {
				ref_id=resultSet.getObject(1).toString();
				enterValueTB.setText(resultSet.getObject(2).toString());
				comboBox.setSelectedItem(resultSet.getObject(3).toString()+"");
				textArea.setText(resultSet.getObject(4).toString()+"");
			//	enterValueTB.setEditable(false);
				valueSet=true;
			//	 System.out.println("value   :-  "+enterValueTB.getText());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
	}
}
