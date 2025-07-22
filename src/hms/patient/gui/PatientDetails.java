package hms.patient.gui;

import hms.amountreceipt.database.AmountReceiptDBConnection;
import hms.departments.gui.IndoorProcedureEntry;
import hms.exam.database.ExamDBConnection;
import hms.exams.gui.ExamEntery;
import hms.exams.gui.ExamsBrowser;
import hms.exams.gui.IPDExamEntery;
import hms.main.DateFormatChange;
import hms.patient.slippdf.ExamSlippdfRegenerate;
import hms.patient.slippdf.PO_PDF;
import hms.reception.gui.ReceptionMain;
import hms.reporttables.PatientOnBedReport;
import hms.store.gui.ItemBrowser.CustomRenderer;
import hms.store.gui.ItemBrowser.CustomRenderer;
import hms1.ipd.database.IPDDBConnection;
import hms1.ipd.gui.DischargeSummary;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import com.itextpdf.text.DocumentException;
import com.sun.prism.paint.Stop;
import com.toedter.calendar.JDateChooser;

import UsersActivity.database.UADBConnection;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.border.BevelBorder;
import javax.swing.JDesktopPane;
import javax.swing.JTabbedPane;
import java.awt.event.MouseMotionAdapter;

public class PatientDetails extends JDialog {

	UADBConnection ua=new UADBConnection();
	Vector<String> examlisCode=new Vector<String>();
	private JPanel contentPane;
	ButtonGroup agegroup = new ButtonGroup();
	DateFormatChange dateFormat = new DateFormatChange();
	private JDateChooser dateFromDC;
	String dateFrom, dateTo;
	private Timer timer;
	private String type="";
	private String search,printStr="";
	DefaultComboBoxModel modelCB=new DefaultComboBoxModel();

	private String w_ID,P_ID="",p_NAME="",r_ID="",DATE="";
	static String patientWorkOrderNo = "";
	private Vector originalTableModel;
	private JTextField textField;
	private JTable table;
	private JScrollPane scrollPane;
	private JPanel panel_2_1;
	private JPanel panel_2;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					PatientDetails frame = new PatientDetails("uname");
					frame.setVisible(true);
					//					frame.dispose();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public PatientDetails(final String username) {
		setResizable(false);
		setTitle("Patients Details");
		setModal(true);
		setIconImage(Toolkit.getDefaultToolkit().getImage(PatientDetails.class.getResource("/icons/rotaryLogo.png")));
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setBounds(100, 100,1250, 650);
		contentPane = new JPanel();
		contentPane.setBackground(UIManager.getColor("Button.focus"));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 235, 613);
		panel.setBackground(new Color(102, 102, 153));
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel lblOpdDetails = new JLabel("OPD DETAILS");
		lblOpdDetails.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		});
		lblOpdDetails.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 14));
		lblOpdDetails.setForeground(UIManager.getColor("Button.background"));
		lblOpdDetails.setIcon(new ImageIcon(PatientDetails.class.getResource("/icons/2rightarrow.png")));
		lblOpdDetails.setBounds(27, 96, 196, 15);
		panel.add(lblOpdDetails);
		
		JLabel lblOpdDetails_1 = new JLabel("IPD DETAILS");
		lblOpdDetails_1.setIcon(new ImageIcon(PatientDetails.class.getResource("/icons/2rightarrow.png")));
		lblOpdDetails_1.setForeground(UIManager.getColor("Button.background"));
		lblOpdDetails_1.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 14));
		lblOpdDetails_1.setBounds(27, 123, 196, 15);
		panel.add(lblOpdDetails_1);
		
		JLabel lblOpdDetails_1_1 = new JLabel("OPD DETAILS");
		lblOpdDetails_1_1.setIcon(new ImageIcon(PatientDetails.class.getResource("/icons/2rightarrow.png")));
		lblOpdDetails_1_1.setForeground(UIManager.getColor("Button.background"));
		lblOpdDetails_1_1.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 14));
		lblOpdDetails_1_1.setBounds(27, 150, 196, 15);
		panel.add(lblOpdDetails_1_1);
		
		JLabel lblOpdDetails_1_1_1 = new JLabel("OPD DETAILS");
		lblOpdDetails_1_1_1.setIcon(new ImageIcon(PatientDetails.class.getResource("/icons/2rightarrow.png")));
		lblOpdDetails_1_1_1.setForeground(UIManager.getColor("Button.background"));
		lblOpdDetails_1_1_1.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 14));
		lblOpdDetails_1_1_1.setBounds(27, 177, 196, 15);
		panel.add(lblOpdDetails_1_1_1);
		
		JLabel label_1 = new JLabel("");
		label_1.setIcon(new ImageIcon(PatientDetails.class.getResource("/icons/bp.gif")));
		label_1.setBounds(12, -11, 122, 63);
		panel.add(label_1);
		
		JLabel label_2 = new JLabel("-----------------------------");
		label_2.setForeground(new Color(192, 192, 192));
		label_2.setBounds(47, 69, 145, 15);
		panel.add(label_2);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(235, 0, 1025, 30);
		panel_1.setBackground(new Color(102, 51, 102));
		contentPane.add(panel_1);
		panel_1.setLayout(null);
		
		textField = new JTextField();
		textField.setBounds(40, 0, 243, 30);
		textField.setBorder(null);
		panel_1.add(textField);
		textField.setColumns(10);

		JLabel label = new JLabel("");
		label.setIcon(new ImageIcon(PatientDetails.class.getResource("/icons/search.png")));
		label.setBounds(12, 0, 49, 32);
		panel_1.add(label);
		
		JPanel panel_12 = new JPanel();
		panel_12.setBounds(235, 38, 1010, 559);;
		panel_12.setLayout(null);
		scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 0, 1010, 300);
		panel_12.add(scrollPane);
		contentPane.add(panel_12);
		
		table = new JTable();
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				scrollPane.setBounds(0, 0, 1010, 300);
				JViewport viewport = scrollPane.getViewport();
				scrollPane.setViewportView(table);
				scrollToPosition(table,table.getSelectedRow());
				panel_2.show();
				panel_2_1.show();

		        
			}
		});
		
		
		//table.setToolTipText("Bouble click to check status\n");
		table.setFont(new Font("Tahoma", Font.PLAIN, 12));
		table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		//((DefaultTableCellRenderer)table.getDefaultRenderer(Object.class)).setOpaque(false);
		table.setShowGrid(false);
		scrollPane.setViewportView(table);
		
		panel_2 = new JPanel();
		panel_2.setBackground(new Color(255, 228, 225));
		panel_2.setBounds(10, 301, 497, 246);
		panel_12.add(panel_2);
		panel_2.setLayout(null);
		panel_2.hide();
		
		panel_2_1 = new JPanel();
		panel_2_1.setBackground(new Color(224, 255, 255));
		panel_2_1.setBounds(519, 301, 478, 246);
		panel_12.add(panel_2_1);
		panel_2_1.hide();
		


		populateTable("2023-06-01","2023-07-01","");
	}
	
	public static void scrollToPosition(JTable table, int insertRow) {
        Rectangle rectangle = table.getCellRect(insertRow+3, 1, true);
        table.scrollRectToVisible(rectangle);
    }
	public void populateTable(String dateFrom, String dateTo,String type) {
		try {
			ExamDBConnection db = new ExamDBConnection();
			ResultSet rs = db.retrieveAllData1(dateFrom, dateTo,type);

			// System.out.println("Table: " + rs.getMetaData().getTableName(1));
			int NumberOfColumns = 0, NumberOfRows = 0;
			NumberOfColumns = rs.getMetaData().getColumnCount();

			while (rs.next()) {
				NumberOfRows++;
			}

			rs.beforeFirst();

			// to set rows in this array
			Object Rows_Object_Array[][];
			Rows_Object_Array = new Object[NumberOfRows][NumberOfColumns];

			int R = 0;
			while (rs.next()) {
				for (int C = 1; C <= NumberOfColumns; C++) {
					Rows_Object_Array[R][C - 1] = rs.getObject(C)==null?"...":rs.getObject(C);
				}
				R++;
			}
			db.closeConnection();
			DefaultTableModel model=null;
			// Finally load data to the table
			model = new DefaultTableModel(Rows_Object_Array,
					new String[]  {
							"Receipt ID","WorkOrder ID","Patient ID", "Patient Name","P Type", "Insurance Type", "Exam Name","Exam Price", "Exam Date","Status", "HMS Status"
			}) {
				//				DefaultTableModel model = new DefaultTableModel(Rows_Object_Array,
				//						new String[] { "IPD No.", "Patient ID", "Patient Name",
				//								"Insurance Type", "Ward Name", "Bed No.", "Date",
				//								"Button" }) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;// This causes all cells to be not editable
				}

			};
			table.setModel(model);
			
			originalTableModel = (Vector) ((DefaultTableModel) table.getModel())
					.getDataVector().clone();
			table.getColumnModel().getColumn(0).setPreferredWidth(100);
			table.getColumnModel().getColumn(0).setMinWidth(100);
			table.getColumnModel().getColumn(1).setPreferredWidth(120);
			table.getColumnModel().getColumn(1).setMinWidth(120);
			table.getColumnModel().getColumn(2).setPreferredWidth(130);
			table.getColumnModel().getColumn(2).setMinWidth(130);
			table.getColumnModel().getColumn(3).setPreferredWidth(100);
			table.getColumnModel().getColumn(3).setMinWidth(100);
			table.getColumnModel().getColumn(4).setPreferredWidth(120);
			table.getColumnModel().getColumn(4).setMinWidth(120);
			table.getColumnModel().getColumn(5).setPreferredWidth(160);
			table.getColumnModel().getColumn(5).setMinWidth(160);
			table.getColumnModel().getColumn(6).setPreferredWidth(180);
			table.getColumnModel().getColumn(6).setMinWidth(180);
			table.getColumnModel().getColumn(7).setPreferredWidth(100);
			table.getColumnModel().getColumn(7).setMinWidth(100);
			table.getColumnModel().getColumn(8).setPreferredWidth(100);
			table.getColumnModel().getColumn(8).setMinWidth(100);
			table.getColumnModel().getColumn(9).setPreferredWidth(120);
			table.getColumnModel().getColumn(9).setMinWidth(120);
			table.getTableHeader().setBackground(Color.lightGray);
			table.getTableHeader().setForeground(Color.black);
			//table.setBackground(new Color(0,0,0,0));

		   table.setDefaultRenderer(Object.class, new RedYellowRenderer1());

		} catch (SQLException ex) {
			Logger.getLogger(PatientDetails.class.getName()).log(Level.SEVERE,
					null, ex);
		}

	}
	class RedYellowRenderer1 extends DefaultTableCellRenderer {
		RedYellowRenderer1() {
			setHorizontalAlignment(CENTER);
		}
		@Override
		public Component getTableCellRendererComponent(
				JTable table, Object value,
				boolean isSelected, boolean hasFocus,
				int row, int column
				) {
			Component c = super.getTableCellRendererComponent(
					table, value, isSelected, hasFocus, row, column
					);

			if(isSelected) {
				c.setBackground(new Color(0,0,0));
				c.setForeground(Color.white);
			}else if(row%2==0){
				c.setBackground(new Color(204,204,204));	
				c.setForeground(Color.black);
			}else {
				c.setBackground(new Color(153,153,153));
				c.setForeground(Color.black);
			}

			return c;
		}
	}
	 public static void makeTransparent(JTable table, JScrollPane scrollPane) {
	        table.setOpaque(false);/*from  w w  w . ja  va 2s  .  c  o  m*/
	        ((DefaultTableCellRenderer) table.getDefaultRenderer(Object.class))
	                .setOpaque(false);
	        ((DefaultTableCellRenderer) table.getDefaultRenderer(String.class))
	                .setOpaque(false);
	        ((JComponent) table.getDefaultRenderer(Boolean.class))
	                .setOpaque(false);

	        scrollPane.setOpaque(false);
	        scrollPane.getViewport().setOpaque(false);
	    }
}