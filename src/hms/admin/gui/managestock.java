package hms.admin.gui;

import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import hms.departments.database.DepartmentDBConnection;
import hms.main.DBConnection;

import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import java.awt.Color;

public class managestock extends DBConnection{
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;
	public managestock() {
		
		super();
		initialize();
		connection = getConnection();
		statement = getStatement();
	}
	
	public JFrame frame;
	Vector<String> itemid = new Vector<String>();
	Vector<String> expiry = new Vector<String>();
	Vector<String> batchid = new Vector<String>();
	Vector<String> itemidbatchtracking = new Vector<String>();
	Vector<String> batchname = new Vector<String>();
	Vector<String> itemidbatchexpiry = new Vector<String>();
	Vector<String> itemiddefaultbatch = new Vector<String>();
	Vector<String> expirydefaultexpiry = new Vector<String>();
	int size=0;
	private Boolean ab=false;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					managestock window = new managestock();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */

	

	private void fun2() {
		// TODO Auto-generated method stub
		itemiddefaultbatch.removeAllElements();
		expirydefaultexpiry.removeAllElements();
		for(int i=0;i<itemidbatchtracking.size();i++)
		{
			retrivebatch2(itemidbatchtracking.get(i),batchid.get(i),batchname.get(i),itemidbatchexpiry.get(i));
		}
		 ab=retrivebatch5();
		ResultSet rs=retrivebatch3();
		if(ab) {
		try {
			while (rs.next())
			{	
				itemiddefaultbatch.add(rs.getObject(1).toString());
				expirydefaultexpiry.add(rs.getObject(2).toString());
			}
			System.out.print(size+"hhhhhhhhhhhhhhhhhhhhhhhh");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		else {
			JOptionPane.showMessageDialog(null,
					"Already Updated", "Message",
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		
	}
	private void fun3() {
		if(ab) {
		for(int i=0;i<itemiddefaultbatch.size();i++)
		{
			retrivebatch4(itemiddefaultbatch.get(i), expirydefaultexpiry.get(i),i+1);
		}
		JOptionPane.showMessageDialog(null,
				"Sucessfully Updated", "Message",
				JOptionPane.INFORMATION_MESSAGE);
		}
		
	}
	private void fun1() {
		itemidbatchtracking.removeAllElements();
		batchid.removeAllElements();
		batchname.removeAllElements();
		itemidbatchexpiry.removeAllElements();
		// TODO Auto-generated method stub
		System.out.println(itemid.size());
		for(int i=0;i<itemid.size();i++)
		{
			ResultSet rs=retrivebatch1(itemid.get(i),expiry.get(i));
			try {
				while(rs.next()) {
					itemidbatchtracking.add(rs.getObject(1).toString());
					batchid.add(rs.getObject(2).toString());
					batchname.add(rs.getObject(3).toString());
					itemidbatchexpiry.add(rs.getObject(4).toString());
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.print(itemidbatchtracking.size());
//		for(int i=0;i<itemid.size();i++)
//		{
//			System.out.println(batchid.get(i)+" item id");
//			System.out.println(batchname.get(i)+" expiry");
//		}
		
	}
	public ResultSet retrivebatch() 
	{
		String sql1 = "SELECT `item_id`,`expiry_date` FROM `department_stock_register` WHERE `total_stock`>0 AND `batch_id` is null and expiry_date >=CURDATE()";
		try {
			PreparedStatement pstmt = null;
			pstmt = connection.prepareStatement(sql1);
			rs = pstmt.executeQuery();
			return rs;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}
	public ResultSet retrivebatch1(String itemid, String expiry) 
	{
		String sql1 = "SELECT `item_id`,`id`,`item_batch`,`item_expiry` FROM `batch_tracking` WHERE `item_id`='"+itemid+"' AND `item_expiry`='"+expiry+"' ";
		PreparedStatement preparedStatement = null;
		
		try {
			preparedStatement = connection.prepareStatement(sql1);
			rs=preparedStatement.executeQuery(sql1);
			System.out.println(sql1);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return rs;
	}
	public void retrivebatch2(String itemid, String batchid,String batchname,String expiry) 
	{
		try {
			statement.executeUpdate("update `department_stock_register` set `batch_id` = '"+batchid+"',`batch_name` = '"+batchname+"',`expiry_date`= '"+expiry+"'where `item_id` = "+itemid);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public ResultSet retrivebatch3() 
	{
		String sql1 = "SELECT item_id,\r\n"
				+ "	 expiry_date \r\n"
				+ "from\r\n"
				+ "	department_stock_register dsr\r\n"
				+ "where \r\n"
				+ "	expiry_date >CURDATE() and total_stock >0\r\n"
				+ "	and batch_id is null";
		PreparedStatement preparedStatement = null;
		
		try {
			preparedStatement = connection.prepareStatement(sql1);
			rs=preparedStatement.executeQuery(sql1);
			System.out.println(sql1);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return rs;
	}
	public void retrivebatch4(String itemid,String expiry,int i) 
	{
		
		try {
			statement.executeUpdate("update `department_stock_register` set `batch_id` = '"+i+"',`batch_name` = 'Rotary' where `item_id` = '"+itemid+"' and `expiry_date` =  '"+expiry+"' and `total_stock` >0 and `batch_id` is null and `expiry_date` > CURDATE()");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public Boolean retrivebatch5() 
	{
		Boolean ab = null;
		String sql1 = "SELECT case when EXISTS( select * from\r\n"
				+ "	department_stock_register dsr\r\n"
				+ "where\r\n"
				+ "	expiry_date >CURDATE()\r\n"
				+ "	and total_stock >0\r\n"
				+ "	and batch_id is null)  THEN '1' ELSE '0' END as RESULT;";
		PreparedStatement preparedStatement = null;
		
		try {
			preparedStatement = connection.prepareStatement(sql1);
			rs=preparedStatement.executeQuery(sql1);
			while(rs.next())
			{
				ab=rs.getBoolean(1);
			}
			System.out.println(sql1);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return ab;
	}
	
	private void fun() {
		// TODO Auto-generated method stub
		itemid.removeAllElements();
		expiry.removeAllElements();
	ResultSet rs=retrivebatch();
	try {
		while (rs.next())
		{
			itemid.add(rs.getObject(1).toString());
			expiry.add(rs.getObject(2).toString());
			
		}
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	for(int i=0;i<itemid.size();i++)
	{
		System.out.println(i);
		System.out.println(itemid.get(i)+" item id");
		System.out.println(expiry.get(i)+" expiry");
	}
	}
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 313, 165);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("      Update Stock Items");
		lblNewLabel.setForeground(Color.BLACK);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(45, 30, 194, 25);
		frame.getContentPane().add(lblNewLabel);
		
		JButton btnNewButton = new JButton("Update");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				  fun();
			      fun1();
				  fun2();
				  fun3();
				frame.dispose();
			}
		});
		btnNewButton.setBounds(35, 67, 89, 25);
		frame.getContentPane().add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Cancel");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});
		btnNewButton_1.setBounds(180, 67, 89, 25);
		frame.getContentPane().add(btnNewButton_1);
	}
	
}

