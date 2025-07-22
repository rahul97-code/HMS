package hms.store.gui;

import hms.main.DateFormatChange;
import hms.store.database.SuppliersDBConnection;

import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class SupplierBrowser extends JDialog {

	private JPanel contentPane;
	private JTable supplierbrowserTable;
	ButtonGroup agegroup = new ButtonGroup();
	DateFormatChange dateFormat = new DateFormatChange();
	String dateFrom, dateTo;
	private TableRowSorter<TableModel> sorter;
	public String search = "";
	int selectedRowIndex;
	static String OS;
	private JButton btnEditDoctor;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					SupplierBrowser frame = new SupplierBrowser();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public SupplierBrowser() {
		setResizable(false);
		System.out.print("hdhdhdhh"+StoreMain.userID);
		setTitle("Supplier List");
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				SupplierBrowser.class.getResource("/icons/rotaryLogo.png")));
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 814, 463);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setModal(true);
		OS = System.getProperty("os.name").toLowerCase();
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(206, 11, 592, 418);
		contentPane.add(scrollPane);

		supplierbrowserTable = new JTable();
		supplierbrowserTable.setFont(new Font("Tahoma", Font.PLAIN, 12));
		supplierbrowserTable.getTableHeader().setReorderingAllowed(false);
		supplierbrowserTable
				.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		supplierbrowserTable.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null, null, null, null},
			},
			new String[] {
				"Supplier ID.", "Supplier Name", "Mob No.", "Credits", "Debits", "Ratings"
			}
		));
		supplierbrowserTable.getColumnModel().getColumn(0).setMinWidth(75);
		supplierbrowserTable.getColumnModel().getColumn(1).setPreferredWidth(150);
		supplierbrowserTable.getColumnModel().getColumn(1).setMinWidth(120);
		supplierbrowserTable.getColumnModel().getColumn(2).setPreferredWidth(150);
		supplierbrowserTable.getColumnModel().getColumn(2).setMinWidth(130);
		supplierbrowserTable.getColumnModel().getColumn(5).setMinWidth(75);
		
		supplierbrowserTable.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					@Override
					public void valueChanged(ListSelectionEvent e) {
						// TODO Auto-generated method stub
						selectedRowIndex = supplierbrowserTable.getSelectedRow();
						selectedRowIndex = supplierbrowserTable
								.convertRowIndexToModel(selectedRowIndex);
						int selectedColumnIndex = supplierbrowserTable
								.getSelectedColumn();
						if
						(StoreMain.access.equals("1") || StoreMain.update_item_access.equals("1"))
						{
							btnEditDoctor.setEnabled(true);
						}
						
					}
				});
		scrollPane.setViewportView(supplierbrowserTable);

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panel.setBounds(4, 11, 192, 418);
		contentPane.add(panel);
		panel.setLayout(null);

		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setVerticalAlignment(SwingConstants.TOP);
		lblNewLabel.setIcon(new ImageIcon(SupplierBrowser.class
				.getResource("/icons/opd.gif")));
		lblNewLabel.setBounds(35, 324, 111, 83);
		panel.add(lblNewLabel);
		
		JButton btnNewButton = new JButton("New Supplier");
		btnNewButton.setBounds(22, 182, 160, 35);
		panel.add(btnNewButton);
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
			
				NewSupplier newSupplier=new NewSupplier();
				newSupplier.setModal(true);
				newSupplier.setVisible(true);
			}
		});
		btnNewButton.setIcon(new ImageIcon(SupplierBrowser.class
				.getResource("/icons/Business.png")));
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 12));

		btnEditDoctor = new JButton("Edit Supplier");
		btnEditDoctor.setBounds(22, 228, 160, 35);
		panel.add(btnEditDoctor);
		btnEditDoctor.setEnabled(false);
		btnEditDoctor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				Object selectedObject1 = supplierbrowserTable.getModel()
						.getValueAt(selectedRowIndex, 0);
				System.out.print("" + selectedObject1);
				String id = selectedObject1.toString();
				EditSupplier editSupplier=new EditSupplier(id);
				editSupplier.setModal(true);
				editSupplier.setVisible(true);
			}
		});
		btnEditDoctor.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JButton btnNewButton_1 = new JButton("Cancel");
		btnNewButton_1.setBounds(22, 274, 160, 35);
		panel.add(btnNewButton_1);
		btnNewButton_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnNewButton_1.setIcon(new ImageIcon(SupplierBrowser.class
				.getResource("/icons/CANCEL.PNG")));
		btnNewButton_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		populateTable1();
	}

	public void populateTable1() {
		try {
			SuppliersDBConnection db = new SuppliersDBConnection();
			ResultSet rs = db.retrieveAllData2();

			// System.out.println("Table: " + rs.getMetaData().getTableName(1));
			int NumberOfColumns = 0, NumberOfRows = 0;
			NumberOfColumns = rs.getMetaData().getColumnCount();

			while (rs.next()) {
				NumberOfRows++;
			}
			System.out.println("rows : " + NumberOfRows);
			System.out.println("columns : " + NumberOfColumns);
			rs.beforeFirst();

			// to set rows in this array
			Object Rows_Object_Array[][];
			Rows_Object_Array = new Object[NumberOfRows][NumberOfColumns];

			int R = 0;
			while (rs.next()) {
				for (int C = 1; C <= NumberOfColumns; C++) {
					Rows_Object_Array[R][C - 1] = rs.getObject(C);
				}
				R++;
			}
			// Finally load data to the table
			DefaultTableModel model = new DefaultTableModel(Rows_Object_Array,
					new String[] { "Supplier ID.", "Supplier Name", "Mob No.", "Credits", "Debits", "Ratings"}) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;// This causes all cells to be not editable
				}
			};
			supplierbrowserTable.setModel(model);
			supplierbrowserTable.getColumnModel().getColumn(0).setMinWidth(75);
			supplierbrowserTable.getColumnModel().getColumn(1).setPreferredWidth(150);
			supplierbrowserTable.getColumnModel().getColumn(1).setMinWidth(120);
			supplierbrowserTable.getColumnModel().getColumn(2).setPreferredWidth(150);
			supplierbrowserTable.getColumnModel().getColumn(2).setMinWidth(130);
			supplierbrowserTable.getColumnModel().getColumn(5).setMinWidth(75);
			} catch (SQLException ex) {
			Logger.getLogger(SupplierBrowser.class.getName()).log(Level.SEVERE,
					null, ex);
		}
	}

	

	public void OPenFileWindows(String path) {
		try {

			File f = new File(path);
			if (f.exists()) {
				if (Desktop.isDesktopSupported()) {
					Desktop.getDesktop().open(f);
				} else {
					System.out.println("File does not exists!");
				}
			}
		} catch (Exception ert) {
		}
	}

	public void Run(String[] cmd) {
		try {
			Process process = Runtime.getRuntime().exec(cmd);
			int processComplete = process.waitFor();
			if (processComplete == 0) {
				System.out.println("successfully");
			} else {
				System.out.println("Failed");
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static boolean isWindows() {

		return (OS.indexOf("win") >= 0);

	}

	public static boolean isMac() {

		return (OS.indexOf("mac") >= 0);

	}

	public static boolean isUnix() {

		return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS
				.indexOf("aix") > 0);

	}

	public static boolean isSolaris() {

		return (OS.indexOf("sunos") >= 0);

	}

	public JButton getBtnEditPatient() {
		return btnEditDoctor;
	}
}
