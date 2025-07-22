package hms.payments;

import java.awt.EventQueue;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JPanel;
import javax.swing.UIManager;

import hms.JDialogs.database.JDialogsDBConnection;
import hms.payments.database.PaymentsDBConnection;
import hms.reception.gui.ReceptionMain;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Vector;
import java.awt.event.ActionEvent;
import javax.swing.border.TitledBorder;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.Timer;
import javax.swing.JDesktopPane;
import java.awt.Button;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JTabbedPane;
import java.awt.SystemColor;

public class PaymentMain extends JDialog{
	private Thread thread;
	public static boolean THREAD_RUNNING=false;
	public static boolean FINAL_STATUS=false;
	private boolean stopTimer=false;
	public static final String ORGANIZATION_CODE = "Retail";
	private String MachineTID,MachineNAME;
	Vector<String> Tid=new Vector<>();
	Vector<Integer> Busy=new Vector<>();
	final DefaultComboBoxModel MachineName = new DefaultComboBoxModel();
	Vector<String> PayId=new Vector<>();
	Vector<String> PayType=new Vector<>();
	Vector<Double> PayModeCharges=new Vector<>();
	Vector<String> PayActionID=new Vector<>();
	final DefaultComboBoxModel PayMode = new DefaultComboBoxModel();
	private JComboBox machineCB;
	private JComboBox paymodeCB;
	private String PayModeName;
	private String PayModeActionID;
	private String PayModeType;
	private JTextField resReceiptTF;
	private JTextField resPNameTF;
	private JTextField resCardHolderTF;
	private JTextField resAmtTF;
	private JTextField resTF;
	private JPanel panel_3;
	private JTextField totalAmtTF;
	private int urn=0;
	private int req_urn=0;
	private JLabel countdown;
	private JLabel lblPlease;
	private JButton btnPayNow;
	private JLabel label;
	private double totalCharges;
	private JLabel lblLeftTime;
	private static boolean autoCancelPayment=false;
	private JTextField rrnTF;
	private JButton btnRequestApproved;
	private JLabel lblRequestUrnNo;

	/**
	 * Create the application.
	 */
	public PaymentMain(final String[] arr,final String type) {
		getContentPane().setLocation(-12, -170);
		setTitle("Money Transaction");
		setBounds(100, 100, 506, 386);

		setLocationRelativeTo(null);
		getContentPane().setForeground(Color.CYAN);
		getContentPane().setLayout(null);

		countdown = new JLabel("");
		countdown.setVisible(false);
		
		JPanel panel_4 = new JPanel();
		panel_4.setBackground(SystemColor.inactiveCaption);
		panel_4.setForeground(SystemColor.text);
		panel_4.setBounds(12, 12, 472, 39);
		getContentPane().add(panel_4);
		panel_4.setLayout(null);
		
		JLabel lblRequestUrn = new JLabel("Request URN :");
		lblRequestUrn.setForeground(SystemColor.text);
		lblRequestUrn.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 20));
		lblRequestUrn.setBounds(26, 6, 230, 27);
		panel_4.add(lblRequestUrn);
		
		lblRequestUrnNo = new JLabel("");
		lblRequestUrnNo.setForeground(SystemColor.text);
		lblRequestUrnNo.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 20));
		lblRequestUrnNo.setBounds(230, 5, 230, 29);
		panel_4.add(lblRequestUrnNo);
		countdown.setFont(new Font("Dialog", Font.BOLD, 45));
		countdown.setForeground(new Color(220, 20, 60));
		countdown.setBounds(230, 210, 41, 56);
		getContentPane().add(countdown);

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "Payment Modes",
				TitledBorder.RIGHT, TitledBorder.TOP, new Font("Tahoma",
						Font.PLAIN, 12), null));
		panel.setForeground(Color.LIGHT_GRAY);
		panel.setBounds(12, 61, 472, 69);
		getContentPane().add(panel);
		panel.setLayout(null);

		JLabel lblNewLabel = new JLabel("Machine : ");
		lblNewLabel.setFont(new Font("Dialog", Font.ITALIC, 12));
		lblNewLabel.setBounds(12, 30, 70, 15);
		panel.add(lblNewLabel);

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {        
				cancelPayment();
			}
			@Override
			public void windowClosed(WindowEvent e) {             
			}
		});

		machineCB= new JComboBox();
		machineCB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(machineCB.getSelectedIndex()!=0) {
					MachineTID=Tid.get(machineCB.getSelectedIndex());
					MachineNAME=machineCB.getSelectedItem()+"";
				}
			}
		});
		machineCB.setBounds(87, 26, 130, 24);
		panel.add(machineCB);

		JLabel lblNewLabel_1 = new JLabel("Pay Mode : ");
		lblNewLabel_1.setFont(new Font("Dialog", Font.ITALIC, 12));
		lblNewLabel_1.setBounds(235, 30, 84, 15);
		panel.add(lblNewLabel_1);

		label = new JLabel("");
		label.setIcon(new ImageIcon(PaymentMain.class.getResource("/icons/ewallet.gif")));
		label.setBounds(29, 0, 135, 117);



		paymodeCB = new JComboBox();
		paymodeCB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int index=paymodeCB.getSelectedIndex();
				if (index>0) {
					if (machineCB.getSelectedIndex() == 0) {
						JOptionPane.showMessageDialog(null, "Please select a machine before proceeding.", "Error",
								JOptionPane.ERROR_MESSAGE);
						paymodeCB.setSelectedIndex(0);
						return;
					}
					PayModeName = paymodeCB.getSelectedItem() + "";
					PayModeActionID = PayActionID.get(index);
					PayModeType = PayType.get(index);

					totalCharges = Double.parseDouble(arr[0])
							* Double.parseDouble(PayModeCharges.get(paymodeCB.getSelectedIndex()).toString());
					totalCharges = Math.round(totalCharges * 100.0) / 100.0;
					totalAmtTF.setText(totalCharges + "");
					if (index == 0) {
						label.setIcon(new ImageIcon(PaymentMain.class.getResource("/icons/ewallet.gif")));
					} else if (index == 1) {
						label.setIcon(new ImageIcon(PaymentMain.class.getResource("/icons/qrCode.gif")));
					} else if (index == 2) {
						label.setIcon(new ImageIcon(PaymentMain.class.getResource("/icons/pos.gif")));
					} 
				}
			}
		});
		paymodeCB.setBounds(326, 26, 123, 24);
		panel.add(paymodeCB);

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "Transaction Initiate",
				TitledBorder.RIGHT, TitledBorder.TOP, new Font("Tahoma",
						Font.PLAIN, 9), null));
		panel_1.setBounds(12, 135, 472, 165);
		getContentPane().add(panel_1);
		panel_1.setLayout(null);

		btnPayNow = new JButton("Pay Now");
		btnPayNow.setIcon(new ImageIcon(PaymentMain.class.getResource("/icons/money_button.png")));
		btnPayNow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!isInternetAvailable()) {
					JOptionPane.showMessageDialog(null,"No internet connection available.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				} 
				if(machineCB.getSelectedIndex()==0) {
					JOptionPane.showMessageDialog(null, "Please select a machine.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if(paymodeCB.getSelectedIndex()==0) {
					JOptionPane.showMessageDialog(null, "Please select payment mode.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				btnPayNow.setEnabled(false);
				try {
					System.out.println(Arrays.toString(arr));
					String[] data=new String[11];
					data[0]=MachineTID;
					data[1]=totalCharges+"";
					data[2]=ORGANIZATION_CODE;
					data[3]="";
					data[4]=arr[1]+"("+arr[2]+")";
					data[5]=arr[3];
					data[6]=arr[4];
					data[7]=type;
					data[8]=PayModeType;
					data[9]=PayModeActionID;
					req_urn=insertPaymentData(data);
					data[10]=req_urn+"";
					data[3]=req_urn+"";
					THREAD_RUNNING=true;
					System.out.println(Arrays.toString(data));
					urn=new TransactionExecutor().doTransaction(data);
					if(urn!=0) {
						lblRequestUrnNo.setText(req_urn+"");
						rrnTF.setEnabled(true);
						btnRequestApproved.setEnabled(true);
						stopTimer=false;
						waitTiming(lblLeftTime);
						label.setIcon(new ImageIcon(PaymentMain.class.getResource("/icons/paytime.gif")));
						thread=	new Thread(new Runnable() {
							@Override
							public void run() {
								while(THREAD_RUNNING) {
									try {
										String[] res=new TransactionStatus().getTransactionStatus(urn+"",MachineTID,""+req_urn);
										if(res!=null && !res[0].contains("INITIATE")) {
											if(res[0].contains("success")) {
												FINAL_STATUS=true;
												label.setIcon(new ImageIcon(PaymentMain.class.getResource("/icons/paid.gif")));
											}
											else {
												label.setIcon(new ImageIcon(PaymentMain.class.getResource("/icons/failed.gif")));
												FINAL_STATUS=false;
											}
											setBounds(100, 100, 506, 520);
											setLocationRelativeTo(null);
											panel_3.setVisible(true);
											resReceiptTF.setText(res[1]);
											resAmtTF.setText(res[4]);
											resPNameTF.setText(res[3]);
											resCardHolderTF.setText(res[2]);
											resTF.setText(res[0]);
											exitCountDown();
											break;
										}
										Thread.sleep(1000);
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							}
						});
						thread.start();
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});

		lblPlease = new JLabel("Please don’t close this window. Thank you!");
		lblPlease.setVisible(false);
		lblPlease.setForeground(new Color(220, 20, 60));
		lblPlease.setFont(new Font("Dialog", Font.BOLD, 16));
		lblPlease.setBounds(40, 136, 420, 44);
		panel_1.add(lblPlease);
		btnPayNow.setFont(new Font("Dialog", Font.PLAIN, 13));
		btnPayNow.setBounds(201, 56, 239, 44);
		panel_1.add(btnPayNow);

		JButton btnRequestCancel = new JButton("Cancel Request");
		btnRequestCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cancelPayment();
			}
		});
		btnRequestCancel.setIcon(new ImageIcon(PaymentMain.class.getResource("/icons/close_button.png")));
		btnRequestCancel.setFont(new Font("Dialog", Font.PLAIN, 13));
		btnRequestCancel.setBounds(201, 106, 239, 44);
		panel_1.add(btnRequestCancel);

		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "",
				TitledBorder.RIGHT, TitledBorder.TOP, new Font("Tahoma",
						Font.PLAIN, 9), null));
		panel_2.setBounds(13, 20, 176, 129);
		panel_1.add(panel_2);
		panel_2.setLayout(null);

		lblLeftTime = new JLabel("");
		lblLeftTime.setForeground(new Color(220, 20, 60));
		lblLeftTime.setFont(new Font("FreeSerif", Font.PLAIN, 13));
		lblLeftTime.setBounds(39, 113, 125, 16);
		panel_2.add(lblLeftTime);


		panel_2.add(label);

		JLabel lblTotalAmt = new JLabel("Total Amt : ");
		lblTotalAmt.setFont(new Font("Dialog", Font.PLAIN, 13));
		lblTotalAmt.setBounds(202, 29, 104, 15);
		panel_1.add(lblTotalAmt);

		totalAmtTF = new JTextField(arr[0]);
		totalAmtTF.setEditable(false);
		totalAmtTF.setBounds(293, 24, 147, 23);
		panel_1.add(totalAmtTF);
		totalAmtTF.setColumns(10);

		panel_3 = new JPanel();
		panel_3.setVisible(false);
		panel_3.setBounds(12, 351, 472, 130);
		panel_3.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "Tnx Details",
				TitledBorder.RIGHT, TitledBorder.TOP, new Font("Tahoma",
						Font.PLAIN, 9), null));
		getContentPane().add(panel_3);
		panel_3.setLayout(null);

		resReceiptTF = new JTextField();
		resReceiptTF.setEnabled(false);
		resReceiptTF.setBounds(153, 14, 307, 19);
		panel_3.add(resReceiptTF);
		resReceiptTF.setColumns(10);

		resPNameTF = new JTextField();
		resPNameTF.setEnabled(false);
		resPNameTF.setColumns(10);
		resPNameTF.setBounds(153, 37, 307, 19);
		panel_3.add(resPNameTF);

		resCardHolderTF = new JTextField();
		resCardHolderTF.setEnabled(false);
		resCardHolderTF.setColumns(10);
		resCardHolderTF.setBounds(153, 60, 307, 19);
		panel_3.add(resCardHolderTF);

		resAmtTF = new JTextField();
		resAmtTF.setEnabled(false);
		resAmtTF.setColumns(10);
		resAmtTF.setBounds(153, 82, 307, 19);
		panel_3.add(resAmtTF);

		resTF = new JTextField();
		resTF.setEnabled(false);
		resTF.setColumns(10);
		resTF.setBounds(93, 105, 367, 19);
		panel_3.add(resTF);

		JLabel lblReceiptId = new JLabel("Patient Name :");
		lblReceiptId.setFont(new Font("Dialog", Font.ITALIC, 11));
		lblReceiptId.setBounds(12, 40, 98, 15);
		panel_3.add(lblReceiptId);

		JLabel lblReceiptId_1 = new JLabel("Request ID :");
		lblReceiptId_1.setFont(new Font("Dialog", Font.ITALIC, 11));
		lblReceiptId_1.setBounds(12, 17, 98, 15);
		panel_3.add(lblReceiptId_1);

		JLabel lblCardHolderName = new JLabel("Card Holder Name :");
		lblCardHolderName.setFont(new Font("Dialog", Font.ITALIC, 11));
		lblCardHolderName.setBounds(12, 62, 123, 15);
		panel_3.add(lblCardHolderName);

		JLabel lblAmount = new JLabel("Amount :");
		lblAmount.setFont(new Font("Dialog", Font.ITALIC, 11));
		lblAmount.setBounds(12, 84, 98, 15);
		panel_3.add(lblAmount);

		JLabel lblResponse = new JLabel("Response :");
		lblResponse.setFont(new Font("Dialog", Font.ITALIC, 11));
		lblResponse.setBounds(12, 107, 98, 15);
		panel_3.add(lblResponse);

		btnRequestApproved = new JButton("Submit");
		btnRequestApproved.setEnabled(false);
		btnRequestApproved.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String rrn=rrnTF.getText();
				if(!rrn.equals("")) {
					setSubmitStatus(req_urn,rrn);
					resReceiptTF.setText(req_urn+"");
					FINAL_STATUS=true;
					THREAD_RUNNING=false;
					dispose();
				}else {
					JOptionPane.showMessageDialog(null, "Please enter rrn no.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
		});
		btnRequestApproved.setIcon(new ImageIcon(PaymentMain.class.getResource("/icons/ok_button.png")));
		btnRequestApproved.setFont(new Font("Dialog", Font.PLAIN, 13));
		btnRequestApproved.setBounds(283, 312, 170, 27);
		getContentPane().add(btnRequestApproved);

		rrnTF = new JTextField();
		rrnTF.setEnabled(false);
		rrnTF.setColumns(10);
		rrnTF.setBounds(63, 318, 198, 19);
		getContentPane().add(rrnTF);
		rrnTF.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char vChar = e.getKeyChar();
				if (!(Character.isDigit(vChar) || (vChar == KeyEvent.VK_BACK_SPACE) || (vChar == KeyEvent.VK_DELETE))) {
					e.consume();
				}
			}
		});

		JLabel lblReceiptId_1_1 = new JLabel("Rrn :");
		lblReceiptId_1_1.setFont(new Font("Dialog", Font.ITALIC, 11));
		lblReceiptId_1_1.setBounds(25, 322, 98, 15);
		getContentPane().add(lblReceiptId_1_1);

		getPayMode();
		getMachines("2");//arun after making mode dynamic
	}
	public String getRequestedUrn() {
		if(!resReceiptTF.getText().equals(""))
			return resReceiptTF.getText();
		return null;
	}
	public boolean getFinalStatus() {
		return FINAL_STATUS;
	}
	public double getTotalCharges() {
		return Double.parseDouble(totalAmtTF.getText().toString());
	}
	public String getPaymentMode() {
		if(paymodeCB.getSelectedIndex()>0)
			return paymodeCB.getSelectedItem()+"";
		return null;
	}
	private void getMachines(String PayModeID) {
		// TODO Auto-generated method stub
		MachineName.addElement("select");
		Tid.add(null);
		Busy.add(null);
		PaymentsDBConnection db = new PaymentsDBConnection();
		ResultSet rs=db.getPayMachines(PayModeID);
		try {
			while(rs.next()) {
				MachineName.addElement(rs.getString(2));
				Tid.add(rs.getString(3));
				Busy.add(rs.getInt(4));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			db.closeConnection();
		}
		if(MachineName.getSize()>0) {
			machineCB.setModel(MachineName);
		}
	}

	private void setCancelStatus(int urn) {
		PaymentsDBConnection PayDB = new PaymentsDBConnection();
		try {
			PayDB.updatePaymentCancelStatus(urn);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}finally {
			PayDB.closeConnection();
		}
	}
	private void setSubmitStatus(int req_urn,String rrn) {
		PaymentsDBConnection PayDB = new PaymentsDBConnection();
		try {
			PayDB.updatePaymentSubmitStatus(req_urn,rrn);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}finally {
			PayDB.closeConnection();
		}
	}
	public static int insertPaymentData(String[] data){
		PaymentsDBConnection PayDB = new PaymentsDBConnection();
		try {
			return PayDB.inserPaymentData(data);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}finally {
			PayDB.closeConnection();
		}
		return 0;
	}
	private void getPayMode() {
		// TODO Auto-generated method stub
		PayMode.addElement("select");
		PayType.add(null);
		PayActionID.add(null);
		PayModeCharges.add(null);	
		PaymentsDBConnection db = new PaymentsDBConnection();
		ResultSet rs=db.getPayModes();
		try {
			while(rs.next()) {
				PayType.add(rs.getString(3));
				PayActionID.add(rs.getString(4));
				PayMode.addElement(rs.getString(2));
				PayModeCharges.add(rs.getDouble(5));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			db.closeConnection();
		}
		if(PayMode.getSize()>0) {
			paymodeCB.setModel(PayMode);
		}

	}
	public void cancelPayment() {
		int response = 0;
		if (req_urn!=0) {
			if(!autoCancelPayment)
				response = JOptionPane.showConfirmDialog(null, "Are you sure you want to cancel this payment?",
						"Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if ((autoCancelPayment || response == JOptionPane.YES_OPTION) && thread!=null) {
				try {
					int r = new TransactionCancellation()
							.cancelTransaction(new String[] { MachineTID, getTotalCharges() + "", ORGANIZATION_CODE,
									PayModeType, PayModeActionID, urn + "", "" + req_urn });
					if (r == 1) {
						setCancelStatus(urn);
					} else if (r == 2) {
						lblRequestUrnNo.setText("");
						label.setIcon(new ImageIcon(PaymentMain.class.getResource("/icons/ewallet.gif")));
						btnPayNow.setText("Pay Again");
						paymodeCB.setSelectedIndex(0);
						machineCB.setSelectedIndex(0);
						THREAD_RUNNING = false;
						btnPayNow.setEnabled(true);
						rrnTF.setEnabled(false);
						btnRequestApproved.setEnabled(false);
						urn=0;req_urn=0;
						stopTimer=true;
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} else {
				return;
			}
		}else {
			dispose();
		}

	}
	private  void waitTiming(final JLabel label) {
		final int[] remainingTime = {5 * 60};         
		Timer timer = new Timer(1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				remainingTime[0]--;
				int minutes = remainingTime[0] / 60;
				int seconds = remainingTime[0] % 60; 
				label.setText(String.format("Time left: %02d:%02d", minutes, seconds));
				if(FINAL_STATUS) {
					((Timer) e.getSource()).stop(); 
					label.setText("  Done.");
				}else if (stopTimer) {
					((Timer) e.getSource()).stop(); 
					label.setText("");
				}else if (remainingTime[0] <= 0) {
					((Timer) e.getSource()).stop(); 
					label.setText(" Time's up!");
					autoCancelPayment=true;
					cancelPayment();
				}
			}
		});  
		timer.start(); 
	}
	private static boolean isInternetAvailable() {
		try {
			final URL url = new URL("http://www.google.com");
			final URLConnection conn = url.openConnection();
			conn.setConnectTimeout(3000);
			conn.setReadTimeout(3000);
			conn.connect();
			conn.getInputStream().close();
			return true;
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			return false;
		}
	}
	public void exitCountDown(){
		countdown.setVisible(true);
		lblPlease.setVisible(true);
		countdown.setText("");
		int c=3;
		while(c!=0) {
			try {
				countdown.setText(c+"");
				Thread.sleep(1300);
				c--;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		this.dispose();
	}
}
