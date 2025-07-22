package hms.JDialogs.gui;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import hms.JDialogs.database.JDialogsDBConnection;
import hms.reception.gui.ReceptionMain;

public class CashReportDialog {
	public boolean DONE=false;

	public CashReportDialog(String FromDate,String ToDate) {
		// TODO Auto-generated constructor stub
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(6, 16, 297, 30);
		scrollPane_1.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "Enter Reason:-",
				TitledBorder.LEADING, TitledBorder.TOP, null));

		JTextArea txtArea = new JTextArea();
		txtArea.setLineWrap(true);
		txtArea.setRows(5);
		scrollPane_1.setViewportView(txtArea);
		JTextField txtCash= new JTextField();
		txtCash.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char vChar = e.getKeyChar();
				if (!(Character.isDigit(vChar) || (vChar == KeyEvent.VK_BACK_SPACE) || (vChar == KeyEvent.VK_DELETE))) {
					e.consume();
				}
			}
		});
		JDialogsDBConnection db=new JDialogsDBConnection();
		ResultSet rs=db.GetTodayReportsCount(ReceptionMain.receptionNameSTR);
		try {
			while(rs.next()) {
				if(rs.getInt(1)==0) {
					scrollPane_1=null;
				}
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			db.closeConnection();
		}
		db.closeConnection();
		final JComponent[] inputs = new JComponent[] {
				scrollPane_1,
				new JLabel("Enter Cash Amount"),
				txtCash
		};

		int option = JOptionPane.showConfirmDialog(null, inputs, "Cash Info", JOptionPane.OK_CANCEL_OPTION);
		System.out.println(option+"---"+txtArea.getText()+"---"+txtCash.getText());
		if(option==0) {
			if (scrollPane_1!=null && txtArea.getText().equals("")){
				JOptionPane.showMessageDialog(null,
						"Please Enter reason", "Input Error",
						JOptionPane.INFORMATION_MESSAGE);
				new CashReportDialog(FromDate,ToDate);
				DONE=false;
				return ;
			}
			if (txtCash.getText().equals("")){
				JOptionPane.showMessageDialog(null,
						"Please Enter Cash Amount", "Input Error",
						JOptionPane.INFORMATION_MESSAGE);
				new CashReportDialog(FromDate,ToDate);
				DONE=false;
				return;
			}
			db=new JDialogsDBConnection();
			String[] data=new String[7];
			data[0]=ReceptionMain.receptionIdSTR;
			data[1]=ReceptionMain.receptionNameSTR;
			data[2]=txtCash.getText();
			String amt=GetBillAmount(FromDate,ToDate);
			data[3]=amt;
			data[4]="RECEPTION";
			data[5]=txtArea.getText();
			data[6]=txtCash.getText().equals(amt)?"1":"0";
			try {
				db.InsertData(data);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			db.closeConnection();
			DONE=true;
		}else {
			DONE=false;
		}

	}

	private String GetBillAmount(String fromDate, String toDate) {
		// TODO Auto-generated method stub
		JDialogsDBConnection db=new JDialogsDBConnection();
		ResultSet rs=db.GetUserCashTotalAmount(ReceptionMain.receptionNameSTR,fromDate,toDate);
		try {
			while(rs.next()) {
				return rs.getString(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			db.closeConnection();
		}
		db.closeConnection();
		return 0+"";
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new CashReportDialog("","");
	}

}
