package hms.store.gui;

import hms.main.DateFormatChange;
import hms.patient.slippdf.ReturnInvoicePdf;
import hms.store.database.ItemsDBConnection;
import hms.store.database.ReturnInvoiceDBConnection;
import hms.store.database.SuppliersDBConnection;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.ListSelectionModel;
import javax.swing.ImageIcon;

import com.itextpdf.text.DocumentException;
import com.toedter.calendar.JDateChooser;

public class NewReturnInvoice extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField supplierTF;
	private JComboBox supplierCB;
	private JTextField mobileTF;
	private JTextField addressTF;
	private JTextField invoiceNoTF;
	private JTextField searchItemTF;
	private JTextField itemDescTF;
	private JTextField qtyTF;
	private JTextField unitPriceTF;
	private JTextField taxTF;
	private JTextField discountTF;
	private JTextField totalValueTF;
	private JTable table;
	private JTextField finalTaxTF;
	private JTextField finalDiscountTF;
	final DefaultComboBoxModel supplierName = new DefaultComboBoxModel();
	final DefaultComboBoxModel itemName = new DefaultComboBoxModel();
	final DefaultComboBoxModel measUnit = new DefaultComboBoxModel();
	Vector<String> supplierIDV = new Vector<String>();
	Vector<String> itemID = new Vector<String>();
	Vector<String> itemIDV = new Vector<String>();
	Vector<String> itemNameV = new Vector<String>();
	Vector<String> itemDescV = new Vector<String>();
	Vector<String> measUnitV = new Vector<String>();
	Vector<String> qtyV = new Vector<String>();
	Vector<String> unitPriceV = new Vector<String>();
	Vector<String> taxV = new Vector<String>();
	Vector<String> discountV = new Vector<String>();
	Vector<String> totalValueV = new Vector<String>();
	Vector<String> expiryDateV = new Vector<String>();
	String supplierDisplaySTR,mobileSTR,addressSTR,supplierID,supplierNameSTR;
	String itemIDSTR,itemNameSTR,itemDescSTR,taxTypeSTR,taxValueSTR,expiryDateSTR="",invoiceDateSTR="",dueDateSTR="";
	double unitPrice=0,taxValue=0,discountValue=0,itemValue,finalTaxValue=0,finalDiscountValue=0,finalTotalValue=0;
	int quantity=0,quantityInHand=0;
	Object[][] ObjectArray_ListOfexamsSpecs;
	private JComboBox itemNameCB;
	private JDateChooser expiryDateC;
	private JComboBox measUnitCD;
	private JTextField finalTotalTF;
	ButtonGroup paymentOption = new ButtonGroup();
	private JTextField qtyInHandTB;
	private JTextField cgsttax;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			NewReturnInvoice dialog = new NewReturnInvoice();
			dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Create the dialog.
	 */
	public NewReturnInvoice() {
		setResizable(false);
		setBounds(100, 70, 1031, 640);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblNewLabel_1 = new JLabel("Search Supplier :");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_1.setBounds(20, 11, 126, 25);
		contentPanel.add(lblNewLabel_1);
		
		supplierTF = new JTextField();
		supplierTF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		supplierTF.setBounds(156, 11, 218, 25);
		contentPanel.add(supplierTF);
		supplierTF.setColumns(10);
		supplierTF.getDocument().addDocumentListener(
				new DocumentListener() {
					@Override
					public void insertUpdate(DocumentEvent e) {
						String str = supplierTF.getText() + "";
						if (!str.equals("")) {
							getSupplierName(str);
						} else {
							
							addressTF.setText("");
							mobileTF.setText("");
							supplierName.removeAllElements();
							supplierCB.setModel(supplierName);
						
						}
					}

					@Override
					public void removeUpdate(DocumentEvent e) {
						String str = supplierTF.getText() + "";
						if (!str.equals("")) {
							getSupplierName(str);
						} else {
							addressTF.setText("");
							mobileTF.setText("");
							supplierName.removeAllElements();
							supplierCB.setModel(supplierName);
						}
					}

					@Override
					public void changedUpdate(DocumentEvent e) {
						String str = supplierTF.getText() + "";
						if (!str.equals("")) {
							getSupplierName(str);
						} else {
							addressTF.setText("");
							mobileTF.setText("");
							supplierName.removeAllElements();
							supplierCB.setModel(supplierName);
						}
					}
				});
		
		supplierCB = new JComboBox();
		supplierCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					supplierDisplaySTR = supplierCB.getSelectedItem().toString();
				} catch (Exception e) {
					// TODO: handle exception

				}
				addressTF.setText("");
				mobileTF.setText("");
			
				getSupplierDetail(supplierDisplaySTR);
				if (supplierName.getSize() > 0) {
					addressTF.setText(""+addressSTR);
					mobileTF.setText(""+mobileSTR);
				}
			}
		});
		supplierCB.setFont(new Font("Tahoma", Font.PLAIN, 14));
		supplierCB.setBounds(156, 44, 218, 25);
		contentPanel.add(supplierCB);
		
		JLabel lblCredit = new JLabel("Select Supplier");
		lblCredit.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblCredit.setBounds(20, 44, 126, 25);
		contentPanel.add(lblCredit);
		
		mobileTF = new JTextField();
		mobileTF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		mobileTF.setColumns(10);
		mobileTF.setBounds(156, 80, 218, 25);
		contentPanel.add(mobileTF);
		
		JLabel lblDebit = new JLabel("Mobile :");
		lblDebit.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblDebit.setBounds(20, 80, 126, 25);
		contentPanel.add(lblDebit);
		
		addressTF = new JTextField();
		addressTF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		addressTF.setColumns(10);
		addressTF.setBounds(156, 116, 218, 25);
		contentPanel.add(addressTF);
		
		JLabel lblBalance = new JLabel("Address :");
		lblBalance.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblBalance.setBounds(20, 116, 126, 25);
		contentPanel.add(lblBalance);
		
		invoiceNoTF = new JTextField();
		invoiceNoTF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		invoiceNoTF.setColumns(10);
		invoiceNoTF.setBounds(756, 63, 218, 25);
		contentPanel.add(invoiceNoTF);
		
		JLabel lblInvoiceNo = new JLabel("Invoice No. :");
		lblInvoiceNo.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblInvoiceNo.setBounds(620, 63, 126, 25);
		contentPanel.add(lblInvoiceNo);
		
		JLabel lblDate = new JLabel("Date :");
		lblDate.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblDate.setBounds(620, 96, 126, 25);
		contentPanel.add(lblDate);
		
		searchItemTF = new JTextField();
		searchItemTF.setFont(new Font("Tahoma", Font.BOLD, 11));
		searchItemTF.setBounds(10, 190, 119, 20);
		contentPanel.add(searchItemTF);
		searchItemTF.setColumns(10);
		searchItemTF.getDocument().addDocumentListener(
				new DocumentListener() {
					@Override
					public void insertUpdate(DocumentEvent e) {
						String str = searchItemTF.getText() + "";
						if (!str.equals("")) {
							getItemName(str);
						} else {
							
							itemDescTF.setText("");
							unitPriceTF.setText("");
							taxTF.setText("");
							discountTF.setText("");
							itemName.removeAllElements();
							itemNameCB.setModel(itemName);
							measUnit.removeAllElements();
							measUnitCD.setModel(measUnit);
						
						}
					}

					@Override
					public void removeUpdate(DocumentEvent e) {
						String str = searchItemTF.getText() + "";
						if (!str.equals("")) {
							getItemName(str);
						} else {
							itemDescTF.setText("");
							unitPriceTF.setText("");
							taxTF.setText("");
							discountTF.setText("");
							itemName.removeAllElements();
							itemNameCB.setModel(itemName);
							measUnit.removeAllElements();
							measUnitCD.setModel(measUnit);
						}
					}

					@Override
					public void changedUpdate(DocumentEvent e) {
						String str = searchItemTF.getText() + "";
						if (!str.equals("")) {
							getItemName(str);
						} else {
							itemDescTF.setText("");
							unitPriceTF.setText("");
							taxTF.setText("");
							discountTF.setText("");
							itemName.removeAllElements();
							itemNameCB.setModel(itemName);
							measUnit.removeAllElements();
							measUnitCD.setModel(measUnit);
						}
					}
				});
		
		itemDescTF = new JTextField();
		itemDescTF.setEditable(false);
		itemDescTF.setFont(new Font("Tahoma", Font.BOLD, 11));
		itemDescTF.setBounds(317, 190, 153, 20);
		contentPanel.add(itemDescTF);
		itemDescTF.setColumns(10);
		
		itemNameCB = new JComboBox();
		itemNameCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					itemNameSTR = itemNameCB.getSelectedItem().toString();
				} catch (Exception e) {
					// TODO: handle exception

				}
				
				if(itemNameCB.getSelectedIndex()>-1)
				{
					itemIDSTR=itemID.get(itemNameCB.getSelectedIndex());
				}
				
				itemDescTF.setText("");
				unitPriceTF.setText("");
				taxTF.setText("");
				discountTF.setText("");
				qtyInHandTB.setText("");
				expiryDateC.setCalendar(null);
				getItemDetail(itemIDSTR);
				if (itemName.getSize() > 0) {
					if(!taxTypeSTR.equals("CompanyTax")){
						taxTF.setText(taxValueSTR);
					}
					itemDescTF.setText(""+itemDescSTR);
					unitPriceTF.setText(""+unitPrice);
					qtyInHandTB.setText(""+quantityInHand);
					expiryDateC.setDate(DateFormatChange.StringToDate(expiryDateSTR));
				}
			}
		});
		itemNameCB.setFont(new Font("Tahoma", Font.BOLD, 11));
		itemNameCB.setBounds(139, 190, 168, 20);
		contentPanel.add(itemNameCB);
		
		measUnitCD = new JComboBox();
		measUnitCD.setFont(new Font("Tahoma", Font.BOLD, 11));
		measUnitCD.setBounds(480, 190, 80, 20);
		contentPanel.add(measUnitCD);
		
		qtyTF = new JTextField();
		qtyTF.setHorizontalAlignment(SwingConstants.RIGHT);
		qtyTF.setFont(new Font("Tahoma", Font.BOLD, 13));
		qtyTF.setBounds(567, 190, 71, 20);
		contentPanel.add(qtyTF);
		qtyTF.setColumns(10);
		qtyTF.addKeyListener(new KeyAdapter() {
            @Override
			public void keyTyped(KeyEvent e) {
                char vChar = e.getKeyChar();
                if (!(Character.isDigit(vChar)
                        || (vChar == KeyEvent.VK_BACK_SPACE)
                        || (vChar == KeyEvent.VK_DELETE))) {
                    e.consume();
                    
                    //||vChar== '.'
                }
            }
        });
		qtyTF.getDocument().addDocumentListener(
				new DocumentListener() {
					@Override
					public void insertUpdate(DocumentEvent e) {
						String str = qtyTF.getText() + "";
						if (!str.equals("")) {
							
							quantity=Integer.parseInt(str);
							itemValue();
							
						} else {
							
							quantity=0;
							itemValue();
						}
						
						qtyInHandTB.setText(""+(quantityInHand-quantity));
					}

					@Override
					public void removeUpdate(DocumentEvent e) {
						String str = qtyTF.getText() + "";
						if (!str.equals("")) {
							
							quantity=Integer.parseInt(str);
							itemValue();
							
						} else {
							
							quantity=0;
							itemValue();
						}
						qtyInHandTB.setText(""+(quantityInHand-quantity));
					}

					@Override
					public void changedUpdate(DocumentEvent e) {
						String str = qtyTF.getText() + "";
						if (!str.equals("")) {
							quantity=Integer.parseInt(str);
							itemValue();
							
						} else {
							
							quantity=0;
							itemValue();
						}
						qtyInHandTB.setText(""+(quantityInHand-quantity));
					}
				});
		
		
		unitPriceTF = new JTextField();
		unitPriceTF.setEditable(false);
		unitPriceTF.setHorizontalAlignment(SwingConstants.RIGHT);
		unitPriceTF.setFont(new Font("Tahoma", Font.BOLD, 11));
		unitPriceTF.setBounds(84, 221, 71, 20);
		contentPanel.add(unitPriceTF);
		unitPriceTF.setColumns(10);
		unitPriceTF.addKeyListener(new KeyAdapter() {
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
		unitPriceTF.getDocument().addDocumentListener(
				new DocumentListener() {
					@Override
					public void insertUpdate(DocumentEvent e) {
						String str = unitPriceTF.getText() + "";
						if (!str.equals("")) {
							
							unitPrice=Double.parseDouble(str);
							itemValue();
							
						} else {
							
							unitPrice=0;
							itemValue();
						}

					}

					@Override
					public void removeUpdate(DocumentEvent e) {
						String str = unitPriceTF.getText() + "";
						if (!str.equals("")) {
							
							unitPrice=Double.parseDouble(str);
							itemValue();
							
						} else {
							
							unitPrice=0;
							itemValue();
						}
					}

					@Override
					public void changedUpdate(DocumentEvent e) {
						String str = unitPriceTF.getText() + "";
						if (!str.equals("")) {
							
							unitPrice=Double.parseDouble(str);
							itemValue();
							
						} else {
							
							unitPrice=0;
							itemValue();
						}
					}
				});
		
		
		taxTF = new JTextField();
		taxTF.setHorizontalAlignment(SwingConstants.RIGHT);
		taxTF.setFont(new Font("Tahoma", Font.BOLD, 11));
		taxTF.setBounds(728, 190, 71, 20);
		contentPanel.add(taxTF);
		taxTF.setColumns(10);
		taxTF.addKeyListener(new KeyAdapter() {
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
		taxTF.getDocument().addDocumentListener(
				new DocumentListener() {
					@Override
					public void insertUpdate(DocumentEvent e) {
						String str = taxTF.getText() + "";
						if (!str.equals("")) {
							
							taxValue=Double.parseDouble("0"+str);
							itemValue();
							
						} else {
							
							taxValue=0;
							itemValue();
						}
					}

					@Override
					public void removeUpdate(DocumentEvent e) {
						String str = taxTF.getText() + "";
						if (!str.equals("")) {
							
							taxValue=Double.parseDouble("0"+str);
							itemValue();
							
						} else {
							
							taxValue=0;
							itemValue();
						}
					}

					@Override
					public void changedUpdate(DocumentEvent e) {
						String str = taxTF.getText() + "";
						if (!str.equals("")) {
							
							taxValue=Double.parseDouble("0"+str);
							itemValue();
							
						} else {
							
							taxValue=0;
							itemValue();
						}
					}
				});
		
		
		discountTF = new JTextField();
		discountTF.setHorizontalAlignment(SwingConstants.RIGHT);
		discountTF.setFont(new Font("Tahoma", Font.BOLD, 11));
		discountTF.setBounds(809, 190, 71, 20);
		contentPanel.add(discountTF);
		discountTF.setColumns(10);
		unitPriceTF.addKeyListener(new KeyAdapter() {
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

		discountTF.getDocument().addDocumentListener(
				new DocumentListener() {
					@Override
					public void insertUpdate(DocumentEvent e) {
						String str = discountTF.getText() + "";
						if (!str.equals("")) {
							
							discountValue=Double.parseDouble("0"+str);
							itemValue();
							
						} else {
							
							discountValue=0;
							itemValue();
						}
					}

					@Override
					public void removeUpdate(DocumentEvent e) {
						String str = discountTF.getText() + "";
						if (!str.equals("")) {
							
							discountValue=Double.parseDouble("0"+str);
							itemValue();
							
						} else {
							
							discountValue=0;
							itemValue();
						}
					}

					@Override
					public void changedUpdate(DocumentEvent e) {
						String str = discountTF.getText() + "";
						if (!str.equals("")) {
							
							discountValue=Double.parseDouble("0"+str);
							itemValue();
							
						} else {
							
							discountValue=0;
							itemValue();
						}
					}
				});
		
		
		totalValueTF = new JTextField();
		totalValueTF.setHorizontalAlignment(SwingConstants.RIGHT);
		totalValueTF.setFont(new Font("Tahoma", Font.BOLD, 11));
		totalValueTF.setBounds(480, 221, 158, 20);
		contentPanel.add(totalValueTF);
		totalValueTF.setColumns(10);
		
		JLabel lblNewLabel_2 = new JLabel("Total Value :");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewLabel_2.setBounds(389, 224, 86, 14);
		contentPanel.add(lblNewLabel_2);
		
		JLabel lblSearch = new JLabel("Search Item");
		lblSearch.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblSearch.setBounds(10, 165, 119, 14);
		contentPanel.add(lblSearch);
		
		JLabel lblDescription = new JLabel("Description :");
		lblDescription.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblDescription.setBounds(317, 165, 153, 14);
		contentPanel.add(lblDescription);
		
		JLabel lblMeasUnits = new JLabel("Meas Units");
		lblMeasUnits.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblMeasUnits.setBounds(480, 165, 80, 14);
		contentPanel.add(lblMeasUnits);
		
		JLabel lblQty = new JLabel("Qty.");
		lblQty.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblQty.setBounds(570, 165, 68, 14);
		contentPanel.add(lblQty);
		
		JLabel lblUnitPrice = new JLabel("Unit Price ");
		lblUnitPrice.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblUnitPrice.setBounds(10, 224, 67, 14);
		contentPanel.add(lblUnitPrice);
		
		JLabel lblTax = new JLabel("  SGST");
		lblTax.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblTax.setBounds(730, 165, 67, 14);
		contentPanel.add(lblTax);
		
		JLabel lblDiscount = new JLabel("Discount");
		lblDiscount.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblDiscount.setBounds(809, 165, 71, 14);
		contentPanel.add(lblDiscount);
		
		JButton btnNewButton = new JButton("Add Line");
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				

				if(itemDescTF.getText().toString().equals(""))
				{
					JOptionPane.showMessageDialog(null,
							"Please select item", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				if(qtyTF.getText().toString().equals(""))
				{
					JOptionPane.showMessageDialog(null,
							"Please enter quantity", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				if(unitPriceTF.getText().toString().equals(""))
				{
					JOptionPane.showMessageDialog(null,
							"Please select item or enter unit price", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				else if(totalValueTF.getText().toString().equals("")){
					JOptionPane.showMessageDialog(null,
							"Please enter final value", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				else if(expiryDateSTR.equals("")){
					JOptionPane.showMessageDialog(null,
							"Please enter expiry date", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				else if(itemIDV.indexOf(itemIDSTR)!=-1){
					JOptionPane.showMessageDialog(null,
							"this item already entered", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				
					itemIDV.add(itemIDSTR);
					itemNameV.add(itemNameSTR);
					itemDescV.add(itemDescSTR);
					measUnitV.add(measUnitCD.getSelectedItem().toString());
					qtyV.add(""+quantity);
					unitPriceV.add(unitPrice+"");
					taxV.add(taxValue+"");
					discountV.add(discountValue+"");
					totalValueV.add(totalValueTF.getText()+"");
					expiryDateV.add(expiryDateSTR);
					
					loadDataToTable();
				
			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnNewButton.setBounds(890, 165, 114, 31);
		contentPanel.add(btnNewButton);
		
		JLabel lblExpireDate = new JLabel("Expire Date :");
		lblExpireDate.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblExpireDate.setBounds(645, 224, 86, 14);
		contentPanel.add(lblExpireDate);
		
		final JButton btnRemove = new JButton("Remove");
		btnRemove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int cur_selectedRow;
				cur_selectedRow = table.getSelectedRow();
				cur_selectedRow = table
						.convertRowIndexToModel(cur_selectedRow);
				String toDelete = table.getModel()
						.getValueAt(cur_selectedRow, 0).toString();
			
				itemIDV.remove(cur_selectedRow);
				itemNameV.remove(cur_selectedRow);
				itemDescV.remove(cur_selectedRow);
				measUnitV.remove(cur_selectedRow);
				qtyV.remove(cur_selectedRow);
				unitPriceV.remove(cur_selectedRow);
				taxV.remove(cur_selectedRow);
				discountV.remove(cur_selectedRow);
				totalValueV.remove(cur_selectedRow);
				expiryDateV.remove(cur_selectedRow);
				
				loadDataToTable();
				btnRemove.setEnabled(false);
			}
		});
		btnRemove.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnRemove.setBounds(890, 210, 114, 31);
		contentPanel.add(btnRemove);
		btnRemove.setEnabled(false);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(14, 252, 992, 200);
		contentPanel.add(scrollPane);
		
		table = new JTable();
		table.setFont(new Font("Tahoma", Font.PLAIN, 12));
		table.getTableHeader().setReorderingAllowed(false);
		table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setFont(new Font("Tahoma", Font.BOLD, 11));
		table.setModel(new DefaultTableModel(
			new Object[][] {
				
			},
			new String[] {
				"Item ID", "Item Name", "Item Desc.", "MU", "Qty.", "Unit Price", "Discount", "Tax", "Value", "Expiry"
			}
		));
		table.getColumnModel().getColumn(0).setMinWidth(75);
		table.getColumnModel().getColumn(1).setPreferredWidth(180);
		table.getColumnModel().getColumn(1).setMinWidth(150);
		table.getColumnModel().getColumn(2).setPreferredWidth(180);
		table.getColumnModel().getColumn(2).setMinWidth(150);
		table.getColumnModel().getColumn(3).setMinWidth(75);
		table.getColumnModel().getColumn(4).setMinWidth(75);
		table.getColumnModel().getColumn(5).setMinWidth(75);
		table.getColumnModel().getColumn(6).setPreferredWidth(65);
		table.getColumnModel().getColumn(6).setMinWidth(65);
		table.getColumnModel().getColumn(7).setPreferredWidth(65);
		table.getColumnModel().getColumn(7).setMinWidth(65);
		table.getColumnModel().getColumn(8).setPreferredWidth(100);
		table.getColumnModel().getColumn(8).setMinWidth(75);
		table.getColumnModel().getColumn(9).setPreferredWidth(100);
		table.getColumnModel().getColumn(9).setMinWidth(90);
		table .getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				// TODO Auto-generated method stub
				int selectedRowIndex = table.getSelectedRow();
				selectedRowIndex=table.convertRowIndexToModel(selectedRowIndex);
				int selectedColumnIndex = table.getSelectedColumn();
				btnRemove.setEnabled(true);
			}
		});
		scrollPane.setViewportView(table);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(10, 149, 966, 2);
		contentPanel.add(separator);
		
		JButton btnNewButton_1 = new JButton("Return Items");
		btnNewButton_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(supplierCB.getSelectedIndex()==-1)
				{
					JOptionPane.showMessageDialog(null,
							"Please select supplier", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				

				if(invoiceNoTF.getText().toString().equals(""))
				{
					JOptionPane.showMessageDialog(null,
							"Please enter invoice no", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				if(invoiceDateSTR.equals(""))
				{
					JOptionPane.showMessageDialog(null,
							"Please enter date", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				if(itemIDV.size()==0)
				{
					JOptionPane.showMessageDialog(null,
							"Please add atlest one item", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				long timeInMillis = System.currentTimeMillis();
				Calendar cal1 = Calendar.getInstance();
				cal1.setTimeInMillis(timeInMillis);
				SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
				int index=0;
				String[] data=new String[30];
				data[0]=invoiceNoTF.getText().toString();
				data[1]=supplierID;
				data[2]=supplierDisplaySTR;
				data[3]=invoiceDateSTR;
				data[4]=""+timeFormat.format(cal1.getTime());
				data[5]=""+StoreMain.userName;  //user
				data[6]=finalTotalValue+"";
				data[7]=finalTaxValue+"";
				data[8]=finalDiscountValue+"";
				data[9]=finalTotalTF.getText().toString();
				
				ReturnInvoiceDBConnection invoiceDBConnection=new ReturnInvoiceDBConnection();
				try {
					index=invoiceDBConnection.inserreturn_invoice(data);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				ItemsDBConnection itemsDBConnection=new ItemsDBConnection();
				data[3]=index+"";
				data[11]=""+DateFormatChange.StringToMysqlDate(new Date());
				data[12]=""+timeFormat.format(cal1.getTime());
				data[13]="";  ///user
				for (int i = 0; i < itemIDV.size(); i++) {
					data[0]=itemIDV.get(i);
					data[1]=itemNameV.get(i);
					data[2]=itemDescV.get(i);
					data[4]=measUnitV.get(i);
					data[5]=qtyV.get(i);
					data[6]=unitPriceV.get(i);
					data[7]=discountV.get(i);
					data[8]=taxV.get(i);
					data[9]=totalValueV.get(i);
					data[10]=expiryDateV.get(i);
					
					try {
						invoiceDBConnection.inserInvoiceItem(data);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					try {
						itemsDBConnection.subtractStock(itemIDV.get(i),qtyV.get(i));
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				
				invoiceDBConnection.closeConnection();
				itemsDBConnection.closeConnection();
				
				JOptionPane.showMessageDialog(null,
						"Return Invoice saved successfully", "Return Invoice Save",
						JOptionPane.INFORMATION_MESSAGE);
				
				try {
					new ReturnInvoicePdf(index + "");
				} catch (DocumentException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				dispose();
			}
		});
		btnNewButton_1.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnNewButton_1.setBounds(33, 552, 153, 39);
		contentPanel.add(btnNewButton_1);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		btnCancel.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnCancel.setBounds(196, 552, 153, 39);
		contentPanel.add(btnCancel);
		
		JLabel label_1 = new JLabel("");
		label_1.setIcon(new ImageIcon(NewReturnInvoice.class.getResource("/icons/invoice_icon.jpg")));
		label_1.setBounds(444, 4, 100, 137);
		contentPanel.add(label_1);
		
		JLabel lblTax_1 = new JLabel("Tax % :");
		lblTax_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblTax_1.setBounds(758, 485, 80, 14);
		contentPanel.add(lblTax_1);
		
		JLabel lblDiscount_1 = new JLabel("Discount :");
		lblDiscount_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblDiscount_1.setBounds(758, 521, 80, 14);
		contentPanel.add(lblDiscount_1);
		
		finalTaxTF = new JTextField();
		finalTaxTF.setHorizontalAlignment(SwingConstants.RIGHT);
		finalTaxTF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		finalTaxTF.setBounds(838, 480, 162, 25);
		contentPanel.add(finalTaxTF);
		finalTaxTF.addKeyListener(new KeyAdapter() {
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
		finalTaxTF.getDocument().addDocumentListener(
				new DocumentListener() {
					@Override
					public void insertUpdate(DocumentEvent e) {
						String str = finalTaxTF.getText() + "";
						if (!str.equals("")) {
							
							finalTaxValue=Double.parseDouble("0"+str);
							finalTotal();
							
						} else {
							
							finalTaxValue=0;
							finalTotal();
						}
					}

					@Override
					public void removeUpdate(DocumentEvent e) {
						String str = finalTaxTF.getText() + "";
						if (!str.equals("")) {
							
							finalTaxValue=Double.parseDouble("0"+str);
							finalTotal();
							
						} else {
							
							finalTaxValue=0;
							finalTotal();
						}
					}

					@Override
					public void changedUpdate(DocumentEvent e) {
						String str = finalTaxTF.getText() + "";
						if (!str.equals("")) {
							
							finalTaxValue=Double.parseDouble("0"+str);
							finalTotal();
							
						} else {
							
							finalTaxValue=0;
							finalTotal();
						}
					}
				});
		finalTaxTF.setColumns(10);
		
		finalDiscountTF = new JTextField();
		finalDiscountTF.setHorizontalAlignment(SwingConstants.RIGHT);
		finalDiscountTF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		finalDiscountTF.setColumns(10);
		finalDiscountTF.setBounds(838, 516, 162, 25);
		finalDiscountTF.addKeyListener(new KeyAdapter() {
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
		finalDiscountTF.getDocument().addDocumentListener(
				new DocumentListener() {
					@Override
					public void insertUpdate(DocumentEvent e) {
						String str = finalDiscountTF.getText() + "";
						if (!str.equals("")) {
							
							finalDiscountValue=Double.parseDouble("0"+str);
							finalTotal();
							
						} else {
							
							finalDiscountValue=0;
							finalTotal();
						}
					}

					@Override
					public void removeUpdate(DocumentEvent e) {
						String str = finalDiscountTF.getText() + "";
						if (!str.equals("")) {
							
							finalDiscountValue=Double.parseDouble("0"+str);
							finalTotal();
							
						} else {
							
							finalDiscountValue=0;
							finalTotal();
						}
					}

					@Override
					public void changedUpdate(DocumentEvent e) {
						String str = finalDiscountTF.getText() + "";
						if (!str.equals("")) {
							
							finalDiscountValue=Double.parseDouble("0"+str);
							finalTotal();
							
						} else {
							
							finalDiscountValue=0;
							finalTotal();
						}
					}
				});
		contentPanel.add(finalDiscountTF);
		
		expiryDateC = new JDateChooser();
		expiryDateC.getCalendarButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		expiryDateC.getDateEditor().addPropertyChangeListener(
				new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent arg0) {
						// TODO Auto-generated method stub
						if ("date".equals(arg0.getPropertyName())&&expiryDateC.getDate() != null) {
							expiryDateSTR = DateFormatChange
									.StringToMysqlDate((Date) arg0
											.getNewValue());
							
						}
					}
				});
		expiryDateC.setMinSelectableDate(new Date());
		expiryDateC.setDateFormatString("yyyy-MM-dd");
		expiryDateC.setBounds(728, 221, 152, 20);
		contentPanel.add(expiryDateC);
		
		JLabel lblTotal = new JLabel("Total :");
		lblTotal.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblTotal.setBounds(758, 559, 80, 14);
		contentPanel.add(lblTotal);
		
		finalTotalTF = new JTextField();
		finalTotalTF.setHorizontalAlignment(SwingConstants.RIGHT);
		finalTotalTF.setEditable(false);
		finalTotalTF.setFont(new Font("Tahoma", Font.BOLD, 14));
		finalTotalTF.setColumns(10);
		finalTotalTF.setBounds(838, 554, 162, 25);
		contentPanel.add(finalTotalTF);
		
		JDateChooser invoiceDate = new JDateChooser();
		invoiceDate.setBounds(756, 96, 218, 25);
		contentPanel.add(invoiceDate);
		
		JLabel lblReturnToSupplier = new JLabel("Return To Supplier");
		lblReturnToSupplier.setHorizontalAlignment(SwingConstants.CENTER);
		lblReturnToSupplier.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblReturnToSupplier.setBounds(620, 18, 345, 25);
		contentPanel.add(lblReturnToSupplier);
		
		JLabel lblQtyInHand = new JLabel("Qty in hand :");
		lblQtyInHand.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblQtyInHand.setBounds(165, 224, 80, 14);
		contentPanel.add(lblQtyInHand);
		
		qtyInHandTB = new JTextField();
		qtyInHandTB.setEditable(false);
		qtyInHandTB.setHorizontalAlignment(SwingConstants.RIGHT);
		qtyInHandTB.setFont(new Font("Tahoma", Font.BOLD, 11));
		qtyInHandTB.setColumns(10);
		qtyInHandTB.setBounds(248, 221, 126, 20);
		contentPanel.add(qtyInHandTB);
		invoiceDate.getDateEditor().addPropertyChangeListener(
				new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent arg0) {
						// TODO Auto-generated method stub
						if ("date".equals(arg0.getPropertyName())) {
							invoiceDateSTR = DateFormatChange
									.StringToMysqlDate((Date) arg0
											.getNewValue());
							
						}
					}
				});
		invoiceDate.setDate(new Date());
		
		cgsttax = new JTextField();
		cgsttax.setHorizontalAlignment(SwingConstants.RIGHT);
		cgsttax.setFont(new Font("Tahoma", Font.BOLD, 11));
		cgsttax.setColumns(10);
		cgsttax.setBounds(647, 190, 71, 20);
		contentPanel.add(cgsttax);
		
		JLabel cgstlabel = new JLabel("   CGST");
		cgstlabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		cgstlabel.setBounds(648, 165, 68, 14);
		contentPanel.add(cgstlabel);
	}
	
	public void getSupplierName(String index) {
		
		SuppliersDBConnection suppliersDBConnection = new SuppliersDBConnection();
		ResultSet resultSet = suppliersDBConnection
				.searchSupplierWithIdOrNmae(index);
		supplierName.removeAllElements();
		int i=0;
		try {
			while (resultSet.next()) {
				supplierName.addElement(resultSet.getObject(3).toString());
				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		suppliersDBConnection.closeConnection();
		supplierCB.setModel(supplierName);
		if (i>0) {
			supplierCB.setSelectedIndex(0);
		}
		
	}
	public void getSupplierDetail(String index) {
		
		SuppliersDBConnection suppliersDBConnection = new SuppliersDBConnection();
		ResultSet resultSet = suppliersDBConnection
				.searchSupplierWithIdOrNmae(index);
		try {
			while (resultSet.next()) {
				
				supplierID=resultSet.getObject(1).toString();
				supplierNameSTR=resultSet.getObject(2).toString();
				mobileSTR=(resultSet.getObject(4).toString());
				addressSTR=(resultSet.getObject(6).toString()+", "+resultSet.getObject(7).toString()+", "+resultSet.getObject(8).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		suppliersDBConnection.closeConnection();
	}
	
	
	public void getItemName(String index) {
		
		ItemsDBConnection itemsDBConnection = new ItemsDBConnection();
		ResultSet resultSet = itemsDBConnection
				.searchItemWithIdOrNmae(index);
		itemName.removeAllElements();
		itemID.clear();
		int i=0;
		try {
			while (resultSet.next()) {
				itemID.add(resultSet.getObject(1).toString());
				itemName.addElement(resultSet.getObject(2).toString());
				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		itemsDBConnection.closeConnection();
		itemNameCB.setModel(itemName);
		if (i>0) {
			itemNameCB.setSelectedIndex(0);
		}
	}
	public void getItemDetail(String index) {
		ItemsDBConnection itemsDBConnection = new ItemsDBConnection();
		ResultSet resultSet = itemsDBConnection
				.itemDetail(index);
		measUnit.removeAllElements();
		int i=0;
		try {
			while (resultSet.next()) {
				itemDescSTR=resultSet.getObject(3).toString();
				measUnit.addElement(resultSet.getObject(4).toString());
				taxTypeSTR=resultSet.getObject(5).toString();
				taxValueSTR=resultSet.getObject(6).toString();
				unitPrice=Double.parseDouble(resultSet.getObject(7).toString());
				quantityInHand=Integer.parseInt(resultSet.getObject(8).toString());
				expiryDateSTR=resultSet.getObject(9).toString();
				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		itemsDBConnection.closeConnection();
		measUnitCD.setModel(measUnit);
		if (i>0) {
			measUnitCD.setSelectedIndex(0);
		}
		
		
	}
	public void itemValue()
	{
		itemValue=quantity*unitPrice;
		
		double k = itemValue*(taxValue/100.0f);
		
		itemValue=itemValue+k;
		
		itemValue=itemValue-discountValue;
		
		itemValue = Math.round( itemValue * 100.000 ) / 100.000;
		
		totalValueTF.setText(""+itemValue);
		
		
	}
	
	public void finalTotal()
	{
		 double total=0;
		double k = finalTotalValue*(finalTaxValue/100.0f);
		
		total=finalTotalValue+k;
		
		total=total-finalDiscountValue;
		
		total = Math.round( total * 100.000 ) / 100.000;
		
		finalTotalTF.setText(""+total);
		
		
	}
	
	private void loadDataToTable() {
		// get size of the hashmap
		int size = itemIDV.size();
		
		double total=0;
		
		ObjectArray_ListOfexamsSpecs = new Object[size][10];

		for (int i = 0; i < itemIDV.size(); i++) {
			
			
			ObjectArray_ListOfexamsSpecs[i][0] = itemIDV.get(i);
			ObjectArray_ListOfexamsSpecs[i][1] = itemNameV.get(i);
			ObjectArray_ListOfexamsSpecs[i][2] = itemDescV.get(i);
			ObjectArray_ListOfexamsSpecs[i][3]= measUnitV.get(i);
			ObjectArray_ListOfexamsSpecs[i][4] = qtyV.get(i);
			ObjectArray_ListOfexamsSpecs[i][5] = unitPriceV.get(i);
			ObjectArray_ListOfexamsSpecs[i][6] = taxV.get(i);
			ObjectArray_ListOfexamsSpecs[i][7] = discountV.get(i);
			ObjectArray_ListOfexamsSpecs[i][8] = totalValueV.get(i);
			ObjectArray_ListOfexamsSpecs[i][9] = expiryDateV.get(i);
			total=total+Double.parseDouble(totalValueV.get(i));
		}
		table.setModel(new DefaultTableModel(
				ObjectArray_ListOfexamsSpecs,
				new String[] {
						"Item ID", "Item Name", "Item Desc.", "MU", "Qty.", "Unit Price", "Discount", "Tax", "Value", "Expiry"
					}
				));
		
		
				table.getColumnModel().getColumn(0).setMinWidth(75);
				table.getColumnModel().getColumn(1).setPreferredWidth(180);
				table.getColumnModel().getColumn(1).setMinWidth(150);
				table.getColumnModel().getColumn(2).setPreferredWidth(180);
				table.getColumnModel().getColumn(2).setMinWidth(150);
				table.getColumnModel().getColumn(3).setMinWidth(75);
				table.getColumnModel().getColumn(4).setMinWidth(75);
				table.getColumnModel().getColumn(5).setMinWidth(75);
				table.getColumnModel().getColumn(6).setPreferredWidth(65);
				table.getColumnModel().getColumn(6).setMinWidth(65);
				table.getColumnModel().getColumn(7).setPreferredWidth(65);
				table.getColumnModel().getColumn(7).setMinWidth(65);
				table.getColumnModel().getColumn(8).setPreferredWidth(100);
				table.getColumnModel().getColumn(8).setMinWidth(75);
				table.getColumnModel().getColumn(9).setPreferredWidth(100);
				table.getColumnModel().getColumn(9).setMinWidth(90);
				
				DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
				centerRenderer.setHorizontalAlignment( SwingConstants.RIGHT );
				table.getColumnModel().getColumn(4).setCellRenderer( centerRenderer );
				table.getColumnModel().getColumn(5).setCellRenderer( centerRenderer );
				table.getColumnModel().getColumn(6).setCellRenderer( centerRenderer );
				table.getColumnModel().getColumn(7).setCellRenderer( centerRenderer );
				table.getColumnModel().getColumn(8).setCellRenderer( centerRenderer );
				table.getColumnModel().getColumn(9).setCellRenderer( centerRenderer );
				finalTotalValue=total;
				
				finalTotalTF.setText(""+finalTotalValue);
	}
}
