package hms.store.gui;

import hms.doctor.database.DoctorDBConnection;
import hms.main.DateFormatChange;
import hms.store.database.ItemsDBConnection;
import hms.store.database.ProceduresDBConnection;
import hms.store.database.SuppliersDBConnection;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;
import javax.swing.JComboBox;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import java.awt.Font;
import javax.swing.UIManager;
import javax.swing.SwingConstants;
import javax.swing.DefaultComboBoxModel;

import net.sf.jasperreports.engine.export.ooxml.DocxZip;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.toedter.calendar.JDateChooser;

public class NewItem extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField itemNameTF;
	private JTextField descTF;
	private JTextField brandNameTF;
	private JTextField hsnCodeTF;
	private JTextField totalStockTF;
	private JTextField sellingPriceTF;
	private JTextField purchasePriceTF;
	private JTextField mrpTF_1;
	private JTextField minUnitTF;
	private JTextField cgstTF;
	private JCheckBox chckbxInpatientAccomadation;
	private JComboBox subCategoryCB;
	private JRadioButton rdbtnInventory;
	private JCheckBox chckbxLaboratoryTest;
	private JCheckBox chckbxTreatment;
	private JCheckBox chckbxIsActive;
	private JCheckBox chckbxImagingTest;
	private JCheckBox chckbxSurgeryRelated;
	private JComboBox itemUnitCB;
	private JCheckBox chckbxDrug;
	private JRadioButton rdbtnServiceItem;
	private JCheckBox chckbxUseInMedic;
	String itemNameSTR,itemlocationSTR="",descSTR,brandSTR,hsnCodeSTR,totalStockSTR,sellingPriceSTR,purchasePriceSTR,mrpSTR,minUnitSTR,cgstSTR,sgstSTR,igstSTR,mainCategorySTR="",subCategorySTR="",measUnitSTR="Item Unit",itemTypeSTR,taxTypeSTR,expiryDateSTR,medicineLocationSTR;
	ButtonGroup itemType = new ButtonGroup();
	ButtonGroup tax = new ButtonGroup();
	private JDateChooser expiryDate;
	DateFormatChange dateFormat = new DateFormatChange();
	private JPanel panel_6;
	private JTextField sgstTF;
	private JLabel label_1;
	private JLabel lblItemTax;
	private JLabel lblItemSurcharge;
	JComboBox formulaActiveCB;
	NewInvoice invoice=null;
	QuotationPrice quotationPrice=null;
	private JLabel label_2;
	private JTextField igstTF;
	private JLabel label_3;
	private JTextField itemCodeTF;
	private JTextField maximumUnitsTF;
	private JTextField reoderingLevelTF;
	private JComboBox vendor3CB;
	private JComboBox vendor1CB;
	private JComboBox vendor2CB;
	JComboBox ItemTypeCB;
	final DefaultComboBoxModel supplierName1 = new DefaultComboBoxModel();
	final DefaultComboBoxModel supplierName2 = new DefaultComboBoxModel();
	final DefaultComboBoxModel supplierName3 = new DefaultComboBoxModel();
	final DefaultComboBoxModel supplierName4 = new DefaultComboBoxModel();
	final DefaultComboBoxModel drName = new DefaultComboBoxModel();
	
	final DefaultComboBoxModel mainCategory = new DefaultComboBoxModel();
	final DefaultComboBoxModel ItemLocationModel = new DefaultComboBoxModel();
	final DefaultComboBoxModel subCategory = new DefaultComboBoxModel();
	private JComboBox mainCategoryCB;
	private JTextField itemSaltNameTF;
	private JLabel lblItemType;
	private JLabel ItemCategory;
	private JComboBox ItemCategoryCB;
	JComboBox doctorcb ;
	JCheckBox checkBxOther;
	private JCheckBox chckbxIsActive_1;
	private JComboBox ItemLocationCB;
	private JCheckBox chckbxIsChngprice;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			NewItem dialog = new NewItem();
			dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public NewItem() {
		setResizable(false);
		setTitle("New Item");
		setBounds(100, 100, 1043, 580);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Item Information", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_2.setBounds(17, 11, 343, 257);
		contentPanel.add(panel_2);
		panel_2.setLayout(null);
		
		JLabel lblName = new JLabel("Name");
		lblName.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblName.setBounds(22, 49, 103, 20);
		panel_2.add(lblName);
		
		itemNameTF = new JTextField();
		itemNameTF.setFont(new Font("Tahoma", Font.PLAIN, 13));
		itemNameTF.setBounds(159, 44, 174, 25);
		panel_2.add(itemNameTF);
		itemNameTF.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Description :");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblNewLabel.setBounds(22, 116, 103, 20);
		panel_2.add(lblNewLabel);
		
		descTF = new JTextField();
		descTF.setFont(new Font("Tahoma", Font.PLAIN, 13));
		descTF.setBounds(159, 114, 174, 25);
		panel_2.add(descTF);
		descTF.setColumns(10);
		
		JLabel lblBrandName = new JLabel("Brand Name :");
		lblBrandName.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblBrandName.setBounds(22, 183, 103, 20);
		panel_2.add(lblBrandName);
		
		brandNameTF = new JTextField();
		brandNameTF.setFont(new Font("Tahoma", Font.PLAIN, 13));
		brandNameTF.setBounds(159, 181, 174, 25);
		panel_2.add(brandNameTF);
		brandNameTF.setColumns(10);
		
		JLabel lblHsnCode = new JLabel("HSN Code :");
		lblHsnCode.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblHsnCode.setBounds(22, 216, 103, 20);
		panel_2.add(lblHsnCode);
		
		hsnCodeTF = new JTextField();
		hsnCodeTF.setFont(new Font("Tahoma", Font.PLAIN, 13));
		hsnCodeTF.setBounds(159, 214, 174, 25);
		panel_2.add(hsnCodeTF);
		hsnCodeTF.setColumns(10);
		
		itemCodeTF = new JTextField();
		itemCodeTF.setFont(new Font("Tahoma", Font.PLAIN, 13));
		itemCodeTF.setColumns(10);
		itemCodeTF.setBounds(159, 80, 174, 25);
		panel_2.add(itemCodeTF);
		
		if(itemNameTF.getText().equals(""))
		{itemCodeTF.setText(GetUpdateItemID()+"");}
		
		JLabel lblItemCode = new JLabel("Item Code");
		lblItemCode.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblItemCode.setBounds(22, 85, 103, 20);
		panel_2.add(lblItemCode);
		
		chckbxIsActive = new JCheckBox("Is Active");
		chckbxIsActive.setBounds(159, 14, 22, 23);
		panel_2.add(chckbxIsActive);
		chckbxIsActive.setSelected(true);
		chckbxIsActive.setFont(new Font("Tahoma", Font.PLAIN, 13));
		
		JLabel lblSaltName = new JLabel("Salt Name :");
		lblSaltName.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblSaltName.setBounds(22, 150, 103, 20);
		panel_2.add(lblSaltName);
		
		itemSaltNameTF = new JTextField();
		itemSaltNameTF.setFont(new Font("Tahoma", Font.PLAIN, 13));
		itemSaltNameTF.setColumns(10);
		itemSaltNameTF.setBounds(159, 148, 174, 25);
		panel_2.add(itemSaltNameTF);
		
		JLabel lblItemStatus = new JLabel("Item Active");
		lblItemStatus.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblItemStatus.setBounds(22, 18, 103, 20);
		panel_2.add(lblItemStatus);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Medical Type", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(17, 279, 343, 129);
		contentPanel.add(panel);
		panel.setLayout(null);
		
		chckbxTreatment = new JCheckBox("Treatment");
		chckbxTreatment.setFont(new Font("Tahoma", Font.PLAIN, 13));
		chckbxTreatment.setBounds(9, 16, 140, 23);
		panel.add(chckbxTreatment);
		
		chckbxLaboratoryTest = new JCheckBox("Laboratory Test");
		chckbxLaboratoryTest.setFont(new Font("Tahoma", Font.PLAIN, 13));
		chckbxLaboratoryTest.setBounds(172, 16, 143, 23);
		panel.add(chckbxLaboratoryTest);
		
		chckbxSurgeryRelated = new JCheckBox("Surgery Related");
		chckbxSurgeryRelated.setFont(new Font("Tahoma", Font.PLAIN, 13));
		chckbxSurgeryRelated.setBounds(9, 42, 143, 23);
		panel.add(chckbxSurgeryRelated);
		
		chckbxImagingTest = new JCheckBox("Imaging Test");
		chckbxImagingTest.setFont(new Font("Tahoma", Font.PLAIN, 13));
		chckbxImagingTest.setBounds(172, 42, 143, 23);
		panel.add(chckbxImagingTest);
		
		chckbxDrug = new JCheckBox("Drug");
		chckbxDrug.setFont(new Font("Tahoma", Font.PLAIN, 13));
		chckbxDrug.setBounds(9, 68, 140, 23);
		panel.add(chckbxDrug);
		
		chckbxInpatientAccomadation = new JCheckBox("Inpatient Accomadation");
		chckbxInpatientAccomadation.setFont(new Font("Tahoma", Font.PLAIN, 13));
		chckbxInpatientAccomadation.setBounds(172, 68, 159, 23);
		panel.add(chckbxInpatientAccomadation);
		
		chckbxUseInMedic = new JCheckBox("Use in medic income");
		chckbxUseInMedic.setFont(new Font("Tahoma", Font.PLAIN, 13));
		chckbxUseInMedic.setBounds(9, 94, 159, 23);
		panel.add(chckbxUseInMedic);
		
		 checkBxOther = new JCheckBox("Other Item");
		checkBxOther.setFont(new Font("Tahoma", Font.PLAIN, 13));
		checkBxOther.setBounds(172, 94, 159, 23);
		panel.add(checkBxOther);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_3.setBounds(17, 419, 343, 122);
		contentPanel.add(panel_3);
		panel_3.setLayout(null);
		
		JLabel lblCategory = new JLabel("Sub Category :");
		lblCategory.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblCategory.setBounds(10, 54, 108, 20);
		panel_3.add(lblCategory);
		
		subCategoryCB = new JComboBox();
		subCategoryCB.setEditable(true);
		subCategoryCB.setFont(new Font("Tahoma", Font.PLAIN, 13));
		subCategoryCB.setBounds(153, 52, 174, 25);
		panel_3.add(subCategoryCB);
		subCategoryCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
					subCategorySTR = (String) subCategoryCB.getSelectedItem();
					System.out.print(""+subCategorySTR);
			}
		});
		final JTextField tfListText = (JTextField) subCategoryCB.getEditor()
				.getEditorComponent();
		tfListText.addCaretListener(new CaretListener() {

			@Override
			public void caretUpdate(CaretEvent e) {
				String text = tfListText.getText();
				if (!text.equals("")) {

					subCategorySTR = text;
					
				}
			}
		});

		JLabel lblMeasUnits = new JLabel("Pack Size :");
		lblMeasUnits.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblMeasUnits.setBounds(10, 88, 108, 20);
		panel_3.add(lblMeasUnits);
		
		itemUnitCB = new JComboBox();
		itemUnitCB.setEditable(true);
		itemUnitCB.setFont(new Font("Tahoma", Font.PLAIN, 13));
		itemUnitCB.setBounds(153, 86, 174, 25);
		panel_3.add(itemUnitCB);
		itemUnitCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
			
					measUnitSTR = (String) itemUnitCB.getSelectedItem();
			}
		});
		final JTextField tfListText1 = (JTextField) itemUnitCB.getEditor()
				.getEditorComponent();
		tfListText.addCaretListener(new CaretListener() {

			@Override
			public void caretUpdate(CaretEvent e) {
				String text = tfListText1.getText();
				measUnitSTR = text;
			}
		});

		
		JLabel lblMaincategory = new JLabel("Main Category :");
		lblMaincategory.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblMaincategory.setBounds(10, 13, 108, 20);
		panel_3.add(lblMaincategory);
		
		mainCategoryCB = new JComboBox();
		mainCategoryCB.setEditable(true);
		mainCategoryCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				mainCategorySTR = (String) mainCategoryCB.getSelectedItem();
					if(mainCategorySTR.toLowerCase().equals("discount")){
						
					}
			}
		});
		final JTextField mainCategoryListText = (JTextField) mainCategoryCB.getEditor()
				.getEditorComponent();
		mainCategoryListText.addCaretListener(new CaretListener() {

			@Override
			public void caretUpdate(CaretEvent e) {
				String text = mainCategoryListText.getText();
				if (!text.equals("")) {

					mainCategorySTR = text;
					if(mainCategorySTR.toLowerCase().equals("discount")){
						
					}
				}
			}
		});
		mainCategoryCB.setFont(new Font("Tahoma", Font.PLAIN, 13));
		mainCategoryCB.setBounds(153, 11, 174, 25);
		panel_3.add(mainCategoryCB);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Type", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(715, 11, 312, 51);
		contentPanel.add(panel_1);
		panel_1.setLayout(null);
		
		rdbtnInventory = new JRadioButton("Inventory Item");
		rdbtnInventory.setSelected(true);
		rdbtnInventory.setFont(new Font("Tahoma", Font.PLAIN, 13));
		rdbtnInventory.setBounds(6, 16, 134, 23);
		panel_1.add(rdbtnInventory);
		itemType.add(rdbtnInventory);
		
		rdbtnServiceItem = new JRadioButton("Service Item");
		
		rdbtnServiceItem.setFont(new Font("Tahoma", Font.PLAIN, 13));
		rdbtnServiceItem.setBounds(155, 16, 169, 23);
		panel_1.add(rdbtnServiceItem);
		itemType.add(rdbtnServiceItem);
		
		JPanel panel_4 = new JPanel();
		panel_4.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Item Tax", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_4.setBounds(370, 59, 343, 110);
		contentPanel.add(panel_4);
		panel_4.setLayout(null);
		
		cgstTF = new JTextField();
		cgstTF.setHorizontalAlignment(SwingConstants.RIGHT);
		cgstTF.setFont(new Font("Tahoma", Font.BOLD, 13));
		cgstTF.setBounds(148, 11, 86, 25);
		panel_4.add(cgstTF);
		cgstTF.setColumns(10);
		cgstTF.addKeyListener(new KeyAdapter() {
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
		
		JLabel label = new JLabel("%");
		label.setFont(new Font("Tahoma", Font.BOLD, 13));
		label.setBounds(240, 16, 46, 14);
		panel_4.add(label);
		
		sgstTF = new JTextField();
		sgstTF.setHorizontalAlignment(SwingConstants.RIGHT);
		sgstTF.setFont(new Font("Tahoma", Font.BOLD, 13));
		sgstTF.setColumns(10);
		sgstTF.setBounds(148, 41, 86, 25);
		panel_4.add(sgstTF);
		
		label_1 = new JLabel("%");
		label_1.setFont(new Font("Tahoma", Font.BOLD, 13));
		label_1.setBounds(240, 46, 46, 14);
		panel_4.add(label_1);
		
		lblItemTax = new JLabel("CGST:");
		lblItemTax.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblItemTax.setBounds(10, 17, 115, 20);
		panel_4.add(lblItemTax);
		
		lblItemSurcharge = new JLabel("SGST :");
		lblItemSurcharge.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblItemSurcharge.setBounds(10, 41, 115, 25);
		panel_4.add(lblItemSurcharge);
		
		label_2 = new JLabel("%");
		label_2.setFont(new Font("Tahoma", Font.BOLD, 13));
		label_2.setBounds(240, 78, 46, 14);
		panel_4.add(label_2);
		
		igstTF = new JTextField();
		igstTF.setHorizontalAlignment(SwingConstants.RIGHT);
		igstTF.setFont(new Font("Tahoma", Font.BOLD, 13));
		igstTF.setColumns(10);
		igstTF.setBounds(148, 73, 86, 25);
		panel_4.add(igstTF);
		
		label_3 = new JLabel("IGST :");
		label_3.setFont(new Font("Tahoma", Font.PLAIN, 13));
		label_3.setBounds(10, 75, 115, 20);
		panel_4.add(label_3);
		
		JPanel panel_5 = new JPanel();
		panel_5.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_5.setBounds(370, 180, 343, 291);
		contentPanel.add(panel_5);
		panel_5.setLayout(null);
		
		JLabel lblTotalStock = new JLabel("Total Stock :");
		lblTotalStock.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblTotalStock.setBounds(16, 13, 115, 20);
		panel_5.add(lblTotalStock);
		
		totalStockTF = new JTextField();
		totalStockTF.setFont(new Font("Tahoma", Font.PLAIN, 13));
		totalStockTF.setBounds(159, 11, 174, 25);
		panel_5.add(totalStockTF);
		totalStockTF.setColumns(10);
		totalStockTF.addKeyListener(new KeyAdapter() {
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
		
		JLabel lblSellingPrice = new JLabel("Selling Price :");
		lblSellingPrice.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblSellingPrice.setBounds(16, 49, 115, 20);
		panel_5.add(lblSellingPrice);
		
		sellingPriceTF = new JTextField();
		sellingPriceTF.setFont(new Font("Tahoma", Font.PLAIN, 13));
		sellingPriceTF.setBounds(159, 47, 174, 25);
		panel_5.add(sellingPriceTF);
		sellingPriceTF.setColumns(10);
		sellingPriceTF.addKeyListener(new KeyAdapter() {
            @Override
			public void keyTyped(KeyEvent e) {
                char vChar = e.getKeyChar();
                if (!(Character.isDigit(vChar)
                        || (vChar == KeyEvent.VK_BACK_SPACE) || (vChar == KeyEvent.VK_MINUS)
                        || (vChar == KeyEvent.VK_DELETE)||vChar== '.')) {
                    e.consume();
                    
                    //||vChar== '.'
                }
            }
        });
		
		purchasePriceTF = new JTextField();
		purchasePriceTF.setFont(new Font("Tahoma", Font.PLAIN, 13));
		purchasePriceTF.setBounds(159, 83, 174, 25);
		panel_5.add(purchasePriceTF);
		purchasePriceTF.setColumns(10);
		purchasePriceTF.addKeyListener(new KeyAdapter() {
            @Override
			public void keyTyped(KeyEvent e) {
                char vChar = e.getKeyChar();
                if (!(Character.isDigit(vChar)
                        || (vChar == KeyEvent.VK_BACK_SPACE)|| (vChar == KeyEvent.VK_MINUS)
                        || (vChar == KeyEvent.VK_DELETE)||vChar== '.')) {
                    e.consume();
                    
                    //||vChar== '.'
                }
            }
        });
		
		
		JLabel lblPurchasePrice = new JLabel("Purchase Price :");
		lblPurchasePrice.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblPurchasePrice.setBounds(16, 85, 115, 20);
		panel_5.add(lblPurchasePrice);
		
		JLabel mrpTF = new JLabel("MRP  :");
		mrpTF.setFont(new Font("Tahoma", Font.PLAIN, 13));
		mrpTF.setBounds(16, 121, 115, 20);
		panel_5.add(mrpTF);
		
		mrpTF_1 = new JTextField();
		mrpTF_1.setFont(new Font("Tahoma", Font.PLAIN, 13));
		mrpTF_1.setBounds(159, 119, 174, 25);
		panel_5.add(mrpTF_1);
		mrpTF_1.setColumns(10);
		mrpTF_1.addKeyListener(new KeyAdapter() {
            @Override
			public void keyTyped(KeyEvent e) {
                char vChar = e.getKeyChar();
                if (!(Character.isDigit(vChar)
                        || (vChar == KeyEvent.VK_BACK_SPACE)|| (vChar == KeyEvent.VK_MINUS)
                        || (vChar == KeyEvent.VK_DELETE)||vChar== '.')) {
                    e.consume();
                    
                    //||vChar== '.'
                }
            }
        });
		
		JLabel minimumUnitsTF = new JLabel("Minimum Units :");
		minimumUnitsTF.setFont(new Font("Tahoma", Font.PLAIN, 13));
		minimumUnitsTF.setBounds(16, 157, 115, 20);
		panel_5.add(minimumUnitsTF);
		
		minUnitTF = new JTextField();
		minUnitTF.setFont(new Font("Tahoma", Font.PLAIN, 13));
		minUnitTF.setBounds(159, 155, 174, 25);
		panel_5.add(minUnitTF);
		minUnitTF.setColumns(10);
		minUnitTF.addKeyListener(new KeyAdapter() {
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
		
		expiryDate = new JDateChooser();
		expiryDate.setBounds(158, 254, 175, 25);
		panel_5.add(expiryDate);
		
		expiryDate.getDateEditor().addPropertyChangeListener(
				new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent arg0) {
						// TODO Auto-generated method stub
						if ("date".equals(arg0.getPropertyName())) {
							expiryDateSTR = DateFormatChange
									.StringToMysqlDate((Date) arg0
											.getNewValue());
						}
					}
				});
		expiryDate.setMinSelectableDate(new Date());
		expiryDate.setDateFormatString("yyyy-MM-dd");
		
		JLabel lblExpiryDate = new JLabel("Expiry Date :");
		lblExpiryDate.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblExpiryDate.setBounds(16, 259, 115, 20);
		panel_5.add(lblExpiryDate);
		
		JLabel lblMaximumUnits = new JLabel("Maximum Units :");
		lblMaximumUnits.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblMaximumUnits.setBounds(16, 190, 115, 20);
		panel_5.add(lblMaximumUnits);
		
		maximumUnitsTF = new JTextField();
		maximumUnitsTF.setFont(new Font("Tahoma", Font.PLAIN, 13));
		maximumUnitsTF.setColumns(10);
		maximumUnitsTF.setBounds(159, 188, 174, 25);
		panel_5.add(maximumUnitsTF);
		
		JLabel lblReorderingUnits = new JLabel("Reordering Level :");
		lblReorderingUnits.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblReorderingUnits.setBounds(16, 223, 115, 20);
		panel_5.add(lblReorderingUnits);
		
		reoderingLevelTF = new JTextField();
		reoderingLevelTF.setFont(new Font("Tahoma", Font.PLAIN, 13));
		reoderingLevelTF.setColumns(10);
		reoderingLevelTF.setBounds(159, 221, 174, 25);
		panel_5.add(reoderingLevelTF);
		
		JButton btnNewButton = new JButton("Save Item");
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				itemNameSTR=itemNameTF.getText().toString().toUpperCase();
				descSTR=descTF.getText().toString();
				brandSTR=brandNameTF.getText().toString();
				hsnCodeSTR=hsnCodeTF.getText().toString();
				totalStockSTR=totalStockTF.getText().toString();
				sellingPriceSTR=sellingPriceTF.getText().toString();
				purchasePriceSTR=purchasePriceTF.getText().toString();
				mrpSTR=mrpTF_1.getText().toString();
				minUnitSTR=minUnitTF.getText().toString();
				cgstSTR=cgstTF.getText().toString();
				sgstSTR=sgstTF.getText().toString();
				igstSTR=igstTF.getText().toString();
				medicineLocationSTR=itemlocationSTR;
				if(rdbtnInventory.isSelected())
				{
					itemTypeSTR="Inventory";
				}
				else {
					itemTypeSTR="Service";
				}
				
				if(doctorcb.getSelectedItem().toString().equals("Select Doctor")){
					JOptionPane.showMessageDialog(null,
							"Please select Doctor Name", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
					taxTypeSTR="ItemTax";
					System.out.println(medicineLocationSTR+"medicineLocationSTR"+"itemNameSTR"+itemNameSTR+"descSTR"+descSTR+"brandNameTF"+brandNameTF.getText()+"hsnCodeSTR"+hsnCodeSTR+"sellingPriceSTR"+sellingPriceSTR);
					System.out.println(purchasePriceSTR+"purchasePriceSTR"+minUnitSTR+"minUnitSTR"+totalStockSTR+"totalStockSTR"+mainCategorySTR+"mainCategorySTR"+subCategorySTR+"subCategorySTR"+measUnitSTR+"measUnitSTR");
				if(medicineLocationSTR.equals("")||itemNameSTR.equals("")||descSTR.equals("")||brandNameTF.getText().equals("")||hsnCodeSTR.equals("")||sellingPriceSTR.equals("")||purchasePriceSTR.equals("")||minUnitSTR.equals("")||totalStockSTR.equals("")||measUnitSTR.equals(""))
				{
					JOptionPane.showMessageDialog(null,
							"Please enter value in fields", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}else if(!chckbxTreatment.isSelected()&&!chckbxDrug.isSelected()&&!chckbxImagingTest.isSelected()&&!chckbxInpatientAccomadation.isSelected()&&!chckbxLaboratoryTest.isSelected()&&!chckbxSurgeryRelated.isSelected()&&!chckbxUseInMedic.isSelected()&&!checkBxOther.isSelected()){
					
					JOptionPane.showMessageDialog(null,
							"Please select atleast one medical type", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				else {
					if(sgstSTR.equals("")||cgstSTR.equals("")||igstSTR.equals(""))
					{
						JOptionPane.showMessageDialog(null,
								"Please enter TAX values", "Input Error",
								JOptionPane.ERROR_MESSAGE);
						return;
					}
					if(expiryDateSTR.equals(""))
					{
						JOptionPane.showMessageDialog(null,
								"Please select expiry date", "Input Error",
								JOptionPane.ERROR_MESSAGE);
						return;
					}
					if(!chckbxIsActive_1.isSelected())
					{
						int result = JOptionPane.showConfirmDialog(null,"Do you want to provide batch to this Item or not", "Input Error",
					               JOptionPane.YES_NO_OPTION,
					               JOptionPane.QUESTION_MESSAGE);
					            if(result == JOptionPane.YES_OPTION){
					            	chckbxIsActive_1.setSelected(true);
					            }else if(result == JOptionPane.NO_OPTION){
					            	chckbxIsActive_1.setSelected(false);
					            }
					      
					}
					if(mainCategoryCB.getSelectedItem().equals("")) {
						JOptionPane.showMessageDialog(null,
								"Please select Main Category", "Input Error",
								JOptionPane.ERROR_MESSAGE);
						return;
					}
					if(subCategoryCB.getSelectedItem().equals("")) {
						JOptionPane.showMessageDialog(null,
								"Please select Sub Category", "Input Error",
								JOptionPane.ERROR_MESSAGE);
						return;
					}
					if(itemUnitCB.getSelectedItem().equals("")) {
						JOptionPane.showMessageDialog(null,
								"Please select Pack Size", "Input Error",
								JOptionPane.ERROR_MESSAGE);
						return;
					}
					if(ItemLocationCB.getSelectedItem().equals("")) {
						JOptionPane.showMessageDialog(null,
								"Please select Item Location", "Input Error",
								JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					String str=chckbxTreatment.isSelected() ? "Yes" : "No";
					long timeInMillis = System.currentTimeMillis();
					Calendar cal1 = Calendar.getInstance();
					cal1.setTimeInMillis(timeInMillis);
					SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
					String[] data=new String[45];
					
					data[0]=itemNameSTR;
					data[1]=itemCodeTF.getText().toString();
					data[2]=descSTR;
					data[3]=brandSTR;
					data[4]=hsnCodeSTR;
					data[5]=chckbxTreatment.isSelected() ? "Yes" : "No";
					data[6]=chckbxLaboratoryTest.isSelected() ? "Yes" : "No";
					data[7]=chckbxSurgeryRelated.isSelected() ? "Yes" : "No";
					data[8]=chckbxDrug.isSelected() ? "Yes" : "No";
					data[9]=chckbxImagingTest.isSelected() ? "Yes" : "No";
					data[10]=chckbxInpatientAccomadation.isSelected() ? "Yes" : "No";
					data[11]=chckbxUseInMedic.isSelected() ? "Yes" : "No";
					data[12]=mainCategorySTR;
					data[13]=subCategorySTR;
					data[14]=measUnitSTR;
					data[15]=medicineLocationSTR;
					data[16]=itemTypeSTR;
					data[17]=taxTypeSTR;
					data[18]=cgstSTR;
					data[19]=sgstSTR;
					data[20]=igstSTR;
					data[21]=purchasePriceSTR;
					data[22]=sellingPriceSTR;
					data[23]=totalStockSTR;
					data[24]=mrpSTR;
					data[25]=minUnitSTR;
					data[26]=chckbxIsActive.isSelected() ? "Yes" : "No";
					data[27]=expiryDateSTR;
					data[28]=DateFormatChange.StringToMysqlDate(new Date());
					data[29]=""+timeFormat.format(cal1.getTime());
					data[30]=""+StoreMain.userName;
					data[31]=""+maximumUnitsTF.getText().toString();
					data[32]=""+reoderingLevelTF.getText().toString();
					data[33]=""+itemSaltNameTF.getText().toString();
					data[34]=""+(vendor1CB.getSelectedIndex()==0 ? "" : vendor1CB.getSelectedItem() );
					data[35]=""+(vendor2CB.getSelectedIndex()==0 ? "" : vendor2CB.getSelectedItem() );
					data[36]=""+(vendor3CB.getSelectedIndex()==0 ? "" : vendor3CB.getSelectedItem() );
					data[37]="" +(formulaActiveCB.getSelectedIndex()==0 ? "0" :"1" );
					data[38]=""+ItemTypeCB.getSelectedItem();
					data[39]=""+ItemCategoryCB.getSelectedItem();
					data[40]=""+doctorcb.getSelectedItem();
					data[41]=checkBxOther.isSelected() ? "Yes" : "No";
					data[42]=chckbxIsActive_1.isSelected() ? "1" : "0";
					data[43]=purchasePriceTF.getText()+"";
					data[44]=chckbxIsChngprice.isSelected()?"1":"0";
					ItemsDBConnection itemsDBConnection=new ItemsDBConnection();
					
					try {
						itemsDBConnection.inserData(data);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					itemsDBConnection.closeConnection();
					JOptionPane.showMessageDialog(null,
							"Item Saved Successfully", "Save Item",
							JOptionPane.INFORMATION_MESSAGE);
					dispose();
					
					
				}
			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnNewButton.setBounds(370, 493, 168, 37);
		contentPanel.add(btnNewButton);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnCancel.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnCancel.setBounds(548, 493, 168, 37);
		contentPanel.add(btnCancel);
		
		panel_6 = new JPanel();
		panel_6.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_6.setBounds(370, 11, 343, 44);
		contentPanel.add(panel_6);
		panel_6.setLayout(null);
		
		JLabel lblMedicineLocation = new JLabel("Item Location:");
		lblMedicineLocation.setBounds(10, 13, 131, 20);
		panel_6.add(lblMedicineLocation);
		lblMedicineLocation.setFont(new Font("Tahoma", Font.PLAIN, 13));
		
		ItemLocationCB = new JComboBox();
		ItemLocationCB.setFont(new Font("Dialog", Font.PLAIN, 13));
		ItemLocationCB.setEditable(true);
		ItemLocationCB.setBounds(159, 11, 174, 25);
		final JTextField ItemLocationtext = (JTextField) ItemLocationCB.getEditor()
				.getEditorComponent();
		ItemLocationtext.addCaretListener(new CaretListener() {
			@Override
			public void caretUpdate(CaretEvent e) {
				String text = ItemLocationtext.getText();
				if (!text.equals("")) {
					itemlocationSTR = text;
				}
			}
		});
		panel_6.add(ItemLocationCB);
		
		JPanel panel_7 = new JPanel();
		panel_7.setBorder(new TitledBorder(null, "PREFERED VENDORS", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_7.setBounds(723, 73, 292, 457);
		contentPanel.add(panel_7);
		panel_7.setLayout(null);
		
		vendor1CB = new JComboBox();
		vendor1CB.setFont(new Font("Tahoma", Font.BOLD, 11));
		vendor1CB.setBounds(31, 49, 239, 20);
		panel_7.add(vendor1CB);
		
		JLabel lblVendor = new JLabel("Vendor 1");
		lblVendor.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblVendor.setBounds(31, 24, 89, 14);
		panel_7.add(lblVendor);
		
		vendor2CB = new JComboBox();
		vendor2CB.setFont(new Font("Tahoma", Font.BOLD, 11));
		vendor2CB.setBounds(31, 105, 239, 20);
		panel_7.add(vendor2CB);
		
		JLabel lblVendor_1 = new JLabel("Vendor 2");
		lblVendor_1.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblVendor_1.setBounds(31, 80, 89, 14);
		panel_7.add(lblVendor_1);
		
		vendor3CB = new JComboBox();
		vendor3CB.setFont(new Font("Tahoma", Font.BOLD, 11));
		vendor3CB.setBounds(31, 161, 239, 20);
		panel_7.add(vendor3CB);
		
		JLabel lblVendor_2 = new JLabel("Vendor 3");
		lblVendor_2.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblVendor_2.setBounds(31, 136, 89, 14);
		panel_7.add(lblVendor_2);
		
		JLabel lblVendor_3 = new JLabel("Formula Active");
		lblVendor_3.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblVendor_3.setBounds(31, 192, 89, 14);
		panel_7.add(lblVendor_3);
		lblItemType = new JLabel("Item Type");
		lblItemType.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblItemType.setBounds(31, 248, 89, 14);
		panel_7.add(lblItemType);
		
		 ItemTypeCB = new JComboBox();
//		ItemTypeCB.setSelectedIndex(0);
		ItemTypeCB.setFont(new Font("Tahoma", Font.BOLD, 11));
		ItemTypeCB.setBounds(31, 273, 239, 20);
		ItemTypeCB.setModel(new DefaultComboBoxModel(new String[] { "General",
				"High Risk", "SHC-H1"}));
		ItemTypeCB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		panel_7.add(ItemTypeCB);
		
		ItemCategory = new JLabel("Item Category");
		ItemCategory.setFont(new Font("Tahoma", Font.BOLD, 11));
		ItemCategory.setBounds(31, 304, 89, 14);
		panel_7.add(ItemCategory);
		
		ItemCategoryCB = new JComboBox();
		ItemCategoryCB.setFont(new Font("Tahoma", Font.BOLD, 11));
		ItemCategoryCB.setModel(new DefaultComboBoxModel(new String[] { "Consumable",
				"Non Consumable"}));
		ItemCategoryCB.setBounds(31, 329, 239, 20);
		panel_7.add(ItemCategoryCB);
		
		JLabel lblDoctorName = new JLabel("Doctor Name");
		lblDoctorName.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblDoctorName.setBounds(31, 360, 89, 14);
		panel_7.add(lblDoctorName);
		
		 doctorcb = new JComboBox();
		 doctorcb.setBounds(31, 383, 239, 20);
		 panel_7.add(doctorcb);
//		doctorcb.setSelectedIndex(0);
		doctorcb.setFont(new Font("Tahoma", Font.BOLD, 11));
		
		 formulaActiveCB = new JComboBox();
		formulaActiveCB.setModel(new DefaultComboBoxModel(new String[] {"Yes", "No"}));
		formulaActiveCB.setSelectedIndex(0);
		formulaActiveCB.setFont(new Font("Tahoma", Font.BOLD, 11));
		formulaActiveCB.setBounds(31, 217, 239, 20);
		panel_7.add(formulaActiveCB);
		
		chckbxIsActive_1 = new JCheckBox(" : Batch Reqd");
		chckbxIsActive_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				
			}
		});
		chckbxIsActive_1.setSelected(false);
		chckbxIsActive_1.setFont(new Font("Dialog", Font.PLAIN, 13));
		chckbxIsActive_1.setBounds(20, 413, 120, 25);
		panel_7.add(chckbxIsActive_1);
		
		chckbxIsChngprice = new JCheckBox(" : Price Change");
		chckbxIsChngprice.setSelected(false);
		chckbxIsChngprice.setFont(new Font("Dialog", Font.PLAIN, 13));
		chckbxIsChngprice.setBounds(150, 411, 130, 25);
		panel_7.add(chckbxIsChngprice);
		
		
		getSupplierName();
		getDoctorName();
		getAllMainCategories();
		getAllItemLocation();
		getAllSubCategories();
	}
	
	public void InvoiceInstance(NewInvoice invoice)
	{
		this.invoice=invoice;
	}
	public void QuotationInstance(QuotationPrice quotationPrice)
	{
		this.quotationPrice=quotationPrice;
	}
	public void getDoctorName() {

		DoctorDBConnection dbConnection = new DoctorDBConnection();
		ResultSet resultSet = dbConnection.retrieveData();
		drName.removeAllElements();
		drName.addElement("Select Doctor");
		
	
		int i = 0;
		try {
			while (resultSet.next()) {
				drName.addElement(resultSet.getObject(1).toString());
			
				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
		doctorcb.setModel(drName);
		
	
		doctorcb.setSelectedIndex(0);
	
		
	}
	public void getSupplierName() {

		SuppliersDBConnection suppliersDBConnection = new SuppliersDBConnection();
		ResultSet resultSet = suppliersDBConnection
				.retrieveAllData2();
		supplierName1.removeAllElements();
		supplierName1.addElement("Select Vendor 1");
		supplierName2.removeAllElements();
		supplierName2.addElement("Select Vendor 2");
		supplierName3.removeAllElements();
		supplierName3.addElement("Select Vendor 3");
		supplierName4.removeAllElements();
		supplierName4.addElement("Select Vendor 4");
		int i = 0;
		try {
			while (resultSet.next()) {
				supplierName1.addElement(resultSet.getObject(2).toString());
				supplierName2.addElement(resultSet.getObject(2).toString());
				supplierName3.addElement(resultSet.getObject(2).toString());
				supplierName4.addElement(resultSet.getObject(2).toString());
				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		suppliersDBConnection.closeConnection();
		vendor1CB.setModel(supplierName1);
		vendor2CB.setModel(supplierName2);
		vendor3CB.setModel(supplierName3);
	
		vendor1CB.setSelectedIndex(0);
		vendor2CB.setSelectedIndex(0);
		vendor3CB.setSelectedIndex(0);
		
	}
	public void getAllMainCategories() {
		ItemsDBConnection dbConnection = new ItemsDBConnection();
		ResultSet resultSet = dbConnection.retrieveMainCategories();

		mainCategory.removeAllElements();
		mainCategory.addElement("");
		int i = 0;
		try {
			while (resultSet.next()) {
				mainCategory.addElement(resultSet.getObject(1).toString());
			
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
		mainCategoryCB.setModel(mainCategory);
		if (i > 0) {
			mainCategoryCB.setSelectedIndex(0);
		}
	}
	public void getAllItemLocation() {
		ItemsDBConnection dbConnection = new ItemsDBConnection();
		ResultSet resultSet = dbConnection.retrieveItemLocations();

		ItemLocationModel.removeAllElements();
		ItemLocationModel.addElement("");
		int i = 0;
		try {
			while (resultSet.next()) {
				
				ItemLocationModel.addElement(resultSet.getObject(1).toString());
			
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
		ItemLocationCB.setModel(ItemLocationModel);
		if (i > 0) {
			ItemLocationCB.setSelectedIndex(0);
		}
	}
	private int GetUpdateItemID() {
		// TODO Auto-generated method stub
		int id=0;
		ItemsDBConnection dbConnection = new ItemsDBConnection();
		ResultSet resultSet = dbConnection.retrieveLastItemID();
		try {
			while(resultSet.next())
			{
				id=resultSet.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
		return id+1;
	}

	public void getAllSubCategories() {
		ItemsDBConnection dbConnection = new ItemsDBConnection();
		ResultSet resultSet = dbConnection.retrieveSubCategories();
		subCategory.removeAllElements();
		subCategory.addElement("");
		int i = 0;
		try {
			while (resultSet.next()) {
				
				subCategory.addElement(resultSet.getObject(1).toString());
			
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
		subCategoryCB.setModel(subCategory);
		if (i > 0) {
			subCategoryCB.setSelectedIndex(0);
		}
	}
}
