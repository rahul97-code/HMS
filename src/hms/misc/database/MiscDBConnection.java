package hms.misc.database;

import hms.main.DBConnection;
import hms.main.DateFormatChange;
import hms.reception.gui.ReceptionMain;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import javax.swing.JOptionPane;

public class MiscDBConnection extends DBConnection {
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	public MiscDBConnection() {

		super();
		connection = getConnection();
		statement = getStatement();
	}

	public ResultSet retrieveData(String query) {
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllData(String amountReceiptID) {

		String query = "SELECT `misc_namecat`,`misc_amount`,`misc_date`,`misc_time`,`p_insurancetype` FROM `misc_entry` WHERE `misc_amount_reciept`="+amountReceiptID+"";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet getSummaryReportData(String dateFrom, String dateTo) {
		String query ="SELECT * FROM(\r\n"
				+ "	SELECT if(payment_mode='CASH','OPD CASH',if(payment_mode='UPI ONLINE','OPD UPI',if(payment_mode='PANEL','OPD PANEL', if(payment_mode='DEBIT CARD','OPD DEBIT CARD',payment_mode)))) as service,\r\n"
				+ " OPD.`p_insurance_type`,OPD.`opd_diseasestype`,COUNT(OPD.`opd_id`),SUM(COALESCE(OPD.`opd_charge`,0)),if(payment_mode='CASH',1,0) as c		\r\n"
				+ "FROM `opd_entery` OPD \r\n"
				+ "WHERE	OPD.`opd_date` BETWEEN '"+dateFrom+"' AND '"+dateTo+"' AND OPD.`opd_text3` IS NULL\r\n"
				+ "GROUP BY 1,2,3	\r\n"
				+ "UNION ALL \r\n"
				+ "SELECT	if(`receipt_text1`<>'Unknown' and is_cashless='0','CARD CASH','CARD') as service, `receipt_text1`, 'CARDS' ,count(`receipt_id`),sum(COALESCE(`receipt_amount`,0)),if(`receipt_text1`='Unknown' or is_cashless='0',1,0) as c\r\n"
				+ "FROM `amountreceipt`\r\n"
				+ "WHERE `receipt__date` BETWEEN '"+dateFrom+"' AND '"+dateTo+"'	AND `receipt_type` = 'CARD'		AND `receipt_text2` = 'N/A'\r\n"
				+ "GROUP BY 2	\r\n"
				+ "UNION ALL \r\n"
				+ "SELECT if(payment_mode='CASH','EXAM CASH', if(payment_mode='UPI ONLINE','EXAM UPI', if(payment_mode='PANEL','OPD PANEL', if(payment_mode='DEBIT CARD','EXAM DEBIT CARD',payment_mode)))) as service,\r\n"
				+ "	`p_insurancetype`,	E.`exam_cat`,	COUNT(E.`exam_id`),	COALESCE(SUM(E.`exam_charges`), 0),if(payment_mode='CASH',1,0) as c							\r\n"
				+ "FROM `exam_entery` E\r\n"
				+ "WHERE E.exam_date BETWEEN '"+dateFrom+"' AND '"+dateTo+"' AND `exam_chargespaid` = 'Yes'\r\n"
				+ "GROUP BY 1,2,3\r\n"
				+ "UNION ALL\r\n"
				+ "SELECT	 if(payment_mode='CASH','MISC CASH', if(payment_mode='UPI ONLINE','MISC UPI', if(payment_mode='PANEL','OPD PANEL', if(payment_mode='DEBIT CARD','MISC DEBIT CARD',payment_mode)))) as service,\r\n"
				+ "	`p_insurancetype`,	M.`misc_namecat`,	COUNT(M.`misc_amount_reciept`),	COALESCE(SUM(M.`misc_amount`), 0),if(payment_mode='CASH',1,0) as c\r\n"
				+ "FROM `misc_entry` M \r\n"
				+ "WHERE	M.misc_date BETWEEN '"+dateFrom+"' AND '"+dateTo+"' \r\n"
				+ "GROUP BY 1,2,3\r\n"
				+ "UNION ALL\r\n"
				+ "SELECT  'INDOOR' as service, P.`insurance_type`,if(I.`payment_type` = 'P','Advance Payment',if(I.`payment_type` = 'R','Return Amount',payment_type)),\r\n"
				+ "	COUNT(I.`payment_amount`),	COALESCE(SUM(I.`payment_amount`), 0),if(I.`payment_type` = 'P',1,if(I.`payment_type` = 'R',-1,0))as c\r\n"
				+ "FROM	`ipd_payments` I left join `ipd_entery` P on P.ipd_id = I.ipd_id	\r\n"
				+ "WHERE  I.`payment_date` BETWEEN '"+dateFrom+"' AND '"+dateTo+"'		AND I.`payment_text5` = 'N/A'\r\n"
				+ "GROUP BY 2,3\r\n"
				+ "UNION ALL \r\n"
				+ "SELECT if(t.pay_mode='CASH',CONCAT(`can_cat`,' ','CANCEL CASH'), if(t.pay_mode='UPI ONLINE',CONCAT(`can_cat`,' ','CANCEL UPI'), if(t.pay_mode='PANEL',CONCAT(`can_cat`,' ','CANCEL PANEL'), if(t.pay_mode='DEBIT CARD',CONCAT(`can_cat`,' ','CANCEL DEBIT CARD'),CONCAT(`can_cat`,' CANCEL ',t.pay_mode))))) as service,\r\n"
				+ "t.ins,\r\n"
				+ "CONCAT(`can_cat`,' ','CANCEL'),\r\n"
				+ "COUNT(*),\r\n"
				+ "SUM(`can_amount`+0) ,\r\n"
				+ "if(t.pay_mode = 'CASH',-1,0) as c \r\n"
				+ "from (SELECT\r\n"
				+ "	cd.*,\r\n"
				+ "	COALESCE (oe.p_insurance_type,ea.p_insurancetype,me.p_insurancetype) as ins,\r\n"
				+ "	COALESCE (oe.payment_mode ,ea.payment_mode,me.payment_mode)	 as pay_mode\r\n"
				+ "from\r\n"
				+ "	`cancel_detail` cd\r\n"
				+ "left join (SELECT DISTINCT p_insurancetype,payment_mode,receipt_id from exam_entery where `exam_date` between DATE_SUB('"+dateFrom+"', INTERVAL 4 MONTH) and '"+dateTo+"'  ) ea on\r\n"
				+ "	can_cat='Exam' and ea.receipt_id = cd.reciept_no \r\n"
				+ "left join opd_entery oe on\r\n"
				+ "	can_cat='OPD' and cd.reciept_no = oe.opd_id \r\n"
				+ "left join misc_entry me on\r\n"
				+ "	can_cat='MISC' and cd.reciept_no = me.misc_id \r\n"
				+ "WHERE date BETWEEN '"+dateFrom+"' AND '"+dateTo+"' )t\r\n"
				+ "GROUP by 1,2,3\r\n"
				+ "UNION ALL \r\n"
				+ "SELECT	'INDOOR' as service,insurance_type,'Indoor Discharge',	COUNT(I.`ipd_recieved_amount`),	COALESCE(SUM(I.`ipd_recieved_amount`), 0),if(I.is_cashless=0,1,0) as c\r\n"
				+ "FROM `ipd_entery` I\r\n"
				+ "WHERE I.`ipd_discharge_date`  BETWEEN '"+dateFrom+"' AND '"+dateTo+"' AND I.`ipd_discharged` = 'Yes'\r\n"
				+ "GROUP BY 2\r\n"
				+ ")t order by 2,1,3";
		try {
			System.out.println(query);
			rs = statement.executeQuery(query); 
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet myCashReportAllData(String fromDate,String toDate,String user) {	 
		String query = "\r\n"
				+ "select * from (SELECT if(payment_mode='CASH','OPD CASH',if(payment_mode='UPI ONLINE','OPD UPI',if(payment_mode='PANEL','OPD PANEL', if(payment_mode='DEBIT CARD','OPD DEBIT CARD',payment_mode)))) as service,\r\n"
				+ " OPD.`p_insurance_type`,OPD.`opd_diseasestype`,COUNT(OPD.`opd_id`),SUM(COALESCE(OPD.`opd_charge`,0)),if(payment_mode='CASH',1,0) as c		\r\n"
				+ "FROM `opd_entery` OPD \r\n"
				+ "WHERE	OPD.`opd_date` BETWEEN '"+fromDate+"' AND '"+toDate+"' AND OPD.`opd_entry_user` = '"+user+"'	AND OPD.`opd_text3` IS NULL\r\n"
				+ "GROUP BY 1,2,3	\r\n"
				+ "UNION ALL\r\n"
				+ "SELECT	if(`receipt_text1`<>'Unknown' and pd.is_cashless='0','CARD CASH','CARD') as service, `receipt_text1`, 'CARDS' ,count(`receipt_id`),sum(COALESCE(`receipt_amount`,0)),if(`receipt_text1`='Unknown' or pd.is_cashless='0',1,0) as c\r\n"
				+ "FROM `amountreceipt` a left join patient_detail pd on pd.pid1=a.receipt_ref_doctor\r\n"
				+ "WHERE `receipt__date` BETWEEN '"+fromDate+"' AND '"+toDate+"'	AND `receipt_type` = 'CARD'	AND `receipt_username` = '"+user+"'	AND `receipt_text2` = 'N/A'\r\n"
				+ "GROUP BY 2	\r\n"
				+ "UNION ALL\r\n"
				+ "SELECT if(payment_mode='CASH','EXAM CASH', if(payment_mode='UPI ONLINE','EXAM UPI', if(payment_mode='PANEL','EXAM PANEL', if(payment_mode='DEBIT CARD','EXAM DEBIT CARD',payment_mode)))) as service,\r\n"
				+ "	`p_insurancetype`,	E.`exam_cat`,	COUNT(E.`exam_id`),	COALESCE(SUM(E.`exam_charges`), 0),if(payment_mode='CASH',1,0) as c							\r\n"
				+ "FROM `exam_entery` E\r\n"
				+ "WHERE E.exam_date BETWEEN '"+fromDate+"' AND '"+toDate+"'	AND E.`exam_note2` = '"+user+"'	AND `exam_chargespaid` = 'Yes'\r\n"
				+ "GROUP BY 1,2,3\r\n"
				+ "UNION ALL\r\n"
				+ "SELECT	 if(payment_mode='CASH','MISC CASH', if(payment_mode='UPI ONLINE','MISC UPI', if(payment_mode='PANEL','OPD PANEL', if(payment_mode='DEBIT CARD','MISC DEBIT CARD',payment_mode)))) as service,\r\n"
				+ "	`p_insurancetype`,	M.`misc_namecat`,	COUNT(M.`misc_amount_reciept`),	COALESCE(SUM(M.`misc_amount`), 0),if(payment_mode='CASH',1,0) as c\r\n"
				+ "FROM `misc_entry` M \r\n"
				+ "WHERE	M.misc_date BETWEEN '"+fromDate+"' AND '"+toDate+"' AND M.`misc_username` = '"+user+"'\r\n"
				+ "GROUP BY 1,2,3\r\n"
				+ "UNION ALL\r\n"
				+ "SELECT  'INDOOR' as service, P.`insurance_type`,if(I.`payment_type` = 'P','Advance Payment',if(I.`payment_type` = 'R','Return Amount',payment_type)),\r\n"
				+ "	COUNT(I.`payment_amount`),	COALESCE(SUM(I.`payment_amount`), 0),if(I.`payment_type` = 'P',1,if(I.`payment_type` = 'R',-1,0))as c\r\n"
				+ "FROM	`ipd_payments` I left join `ipd_entery` P on P.ipd_id = I.ipd_id	\r\n"
				+ "WHERE  I.`payment_date` BETWEEN '"+fromDate+"' AND '"+toDate+"'	AND I.`payment_entry_user` = '"+user+"'	AND I.`payment_text5` = 'N/A'\r\n"
				+ "GROUP BY 2,3	\r\n"
				+ "UNION ALL \r\n"
				+ "SELECT	'INDOOR' as service,insurance_type,'Indoor Discharge',	COUNT(I.`ipd_recieved_amount`),	COALESCE(SUM(I.`ipd_recieved_amount`), 0),if(I.is_cashless=0,1,0) as c\r\n"
				+ "FROM `ipd_entery` I\r\n"
				+ "WHERE I.`ipd_discharge_date`  BETWEEN '"+fromDate+"' AND '"+toDate+"' AND I.`ipd_text2` = '"+user+"' AND I.`ipd_discharged` = 'Yes'\r\n"
				+ "GROUP BY 2	)t order by 2,1,3\r\n";

		System.out.println(query);
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public ResultSet retrieveAllDataUserWisekarunsparsh(String dateFrom, String dateTo,String username,String insurance) {
		String query ="select\r\n"
				+ "	COUNT(id), SUM(relief_amount) \r\n"
				+ "from\r\n"
				+ "	karun_sparsh_record ksr\r\n"
				+ "where\r\n"
				+ "	  created_by = '"+username+"' and cast(`created_at` as date) BETWEEN  '"+dateFrom+"' and '"+dateTo+"'";
		try {
			System.out.println(query);
			rs = statement.executeQuery(query); 
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllDataAllkarunsparsh(String dateFrom, String dateTo,String insurance) {
		String query ="select\r\n"
				+ "	COUNT(id), SUM(relief_amount) \r\n"
				+ "from\r\n"
				+ "	karun_sparsh_record ksr\r\n"
				+ "where\r\n"
				+ " cast(`created_at` as date) BETWEEN  '"+dateFrom+"' and '"+dateTo+"'";
		try {
			rs = statement.executeQuery(query); 
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public int retrieveCounterData() {
		String query = "SELECT * FROM `misc_entry` WHERE 1";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		int NumberOfRows = 0;
		try {
			while (rs.next()) {
				NumberOfRows++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		NumberOfRows++;
		return NumberOfRows;
	}

	public int retrieveCounterTodayToken() {
		new DateFormatChange();
		String query = "SELECT * FROM `misc_entry` WHERE `misc_date`= '"
				+ DateFormatChange.StringToMysqlDate(new Date()) + "'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		int NumberOfRows = 0;
		try {
			while (rs.next()) {
				NumberOfRows++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		NumberOfRows++;
		return NumberOfRows;
	}

	public ResultSet retrieveAllData() {
		String query = "SELECT  `exam_name`, `exam_nameid`, `misc_p_id`, `misc_p_name`, `misc_doctor_ref`, `misc_date`, `misc_amount`, `exam_note1` FROM `misc_entry` WHERE 1";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	//	public ResultSet retrieveAllData(String dateFrom, String dateTo) {
	//		String query = "SELECT  `exam_id`, `misc_p_id`, `misc_p_name`, `misc_doctor_ref`, `misc_date`, `exam_name`, `exam_nameid` FROM `misc_entry` WHERE misc_date BETWEEN '"
	//				+ dateFrom + "' AND '" + dateTo + "'  ORDER BY `exam_id` DESC";
	//		try {
	//			rs = statement.executeQuery(query);
	//		} catch (SQLException sqle) {
	//			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
	//					javax.swing.JOptionPane.ERROR_MESSAGE);
	//		}
	//		return rs;
	//	}
	//
	//	public ResultSet retrieveAllDataDoctorWise(String dateFrom, String dateTo,String doctorname) {
	//		String query = "SELECT  `exam_id`, `misc_p_id`, `misc_p_name`, `misc_doctor_ref`, `misc_date`, `exam_name`, `exam_nameid` FROM `misc_entry` WHERE misc_date BETWEEN '"
	//				+ dateFrom + "' AND '" + dateTo + "' AND `misc_doctor_ref`='"+doctorname+"'";
	//		try {
	//			rs = statement.executeQuery(query);
	//		} catch (SQLException sqle) {
	//			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
	//					javax.swing.JOptionPane.ERROR_MESSAGE);
	//		}
	//		return rs;
	//	}
	//
	public ResultSet retrieveAllDataCatWise(String dateFrom, String dateTo,String Cat) {
		String query = "SELECT  `misc_amount` FROM `misc_entry` WHERE misc_date BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "' AND `misc_namecat`= '"
				+ Cat
				+ "'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllDataCatWiseInsurance(String insuranceType,String dateFrom, String dateTo) {
		String query = "SELECT  M.`misc_namecat`,COUNT(M.`misc_amount_reciept`),COALESCE(SUM(M.`misc_amount`),0) FROM `misc_entry` M WHERE  M.misc_date BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "' AND M.`p_insurancetype`='"+insuranceType+"' GROUP BY M.`misc_namecat`";
		//		String query = "SELECT  M.`misc_namecat`,COUNT(M.`misc_amount_reciept`),COALESCE(SUM(M.`misc_amount`),0) FROM `misc_entry` M,`patient_detail` P WHERE M.`misc_p_id`=P.pid1 AND M.misc_date BETWEEN '"
		//				+ dateFrom + "' AND '" + dateTo + "' AND P.`p_insurancetype`='"+insuranceType+"' GROUP BY M.`misc_namecat`";

		System.out.println(query);
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllDetailCatWiseInsurance(String insuranceType,String dateFrom, String dateTo) {
		String query = "SELECT  M.`misc_amount_reciept` AS \"Reciept ID\",M.`misc_namecat` AS \"Misc Type\",P.`pid1` AS \"Patient ID\",P.`p_name` AS \"Patient Name\",M.`misc_amount` AS \"Misc Amount\",M.`misc_date` AS \"Misc Date\", M.`misc_time` AS \"Misc Time\" FROM `misc_entry` M,`patient_detail` P WHERE M.`misc_p_id`=P.pid1 AND M.misc_date BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "' AND P.`p_insurancetype`='"+insuranceType+"'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllDataCatWiseExcel(String dateFrom, String dateTo) {
		String query = "SELECT `misc_amount_reciept`, `misc_p_id`, `misc_p_name`,`misc_doctor_ref`,if(me.payment_mode<>'cash',pe.total_payment,sum(`misc_amount`) ),`misc_date`, `misc_time`,`misc_username`  FROM `misc_entry` me left join payment_entry pe on pe.ref_id =me.misc_amount_reciept  WHERE misc_date BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "' group by `misc_amount_reciept`";
		try {
			System.out.println(query);
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllData(String dateFrom, String dateTo) {
		String query = "SELECT `misc_amount_reciept`, `misc_namecat`, `misc_p_name`,`misc_doctor_ref`,`misc_amount`,`misc_date`, `misc_time`  FROM `misc_entry` WHERE misc_date BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllDataUserWise(String dateFrom, String dateTo,String username) {
		String query = "SELECT  `misc_namecat`, COUNT(`misc_amount_reciept`), COALESCE(SUM(`misc_amount`),0) FROM `misc_entry` WHERE misc_date BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "' AND `misc_username`= '"
				+ username
				+ "' GROUP BY `misc_namecat`";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public ResultSet retrieveAllDataUserWise(String dateFrom, String dateTo,String username,String insurance) {
		String query = "SELECT  M.`misc_namecat`,COUNT(M.`misc_amount_reciept`),COALESCE(SUM(M.`misc_amount`),0) FROM `misc_entry` M WHERE  M.misc_date BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "' AND  M.`misc_username`= '"
				+ username
				+ "' AND `p_insurancetype`='"+insurance+"' GROUP BY M.`misc_namecat`";
		//		String query = "SELECT  M.`misc_namecat`,COUNT(M.`misc_amount_reciept`),COALESCE(SUM(M.`misc_amount`),0) FROM `misc_entry` M,`patient_detail` P WHERE M.`misc_p_id`=P.pid1 AND M.misc_date BETWEEN '"
		//				+ dateFrom + "' AND '" + dateTo + "' AND  M.`misc_username`= '"
		//				+ username
		//				+ "' AND P.`p_insurancetype`='"+insurance+"' GROUP BY M.`misc_namecat`";
		try {
			System.out.println(query);
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}


	public ResultSet retrieveAllPatientWiseMiscData(String dateFrom, String dateTo,String patient_id) {
		String query = "SELECT  `misc_id`,`misc_namecat`,misc_date, misc_amount FROM `misc_entry` WHERE misc_date BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "' AND `misc_p_id`= '"
				+ patient_id
				+ "'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}


	public ResultSet retrieveAllPatientWise(String dateFrom, String dateTo,String patient_id) {
		String query = "SELECT  `misc_id`,`misc_namecat`,misc_date, misc_amount FROM `misc_entry` WHERE misc_date BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "' AND `misc_p_id`= '"
				+ patient_id
				+ "'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public ResultSet retrieveAllDataDoctorWise(String dateFrom, String dateTo,String doctorname) {
		String query = "SELECT `misc_namecat`, COUNT(`misc_amount_reciept`), COALESCE(SUM(`misc_amount`),0) FROM `misc_entry` WHERE misc_date BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "' AND `misc_doctor_ref`='"+doctorname+"' GROUP BY `misc_namecat`";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllDataDoctorWise(String dateFrom, String dateTo,String doctorname,String insurance) {
		String query = "SELECT  M.`misc_namecat`,COUNT(M.`misc_amount_reciept`),COALESCE(SUM(M.`misc_amount`),0) FROM `misc_entry` M,`patient_detail` P WHERE M.`misc_p_id`=P.pid1 AND M.misc_date BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "' AND  M.`misc_doctor_ref`='"+doctorname+"' AND P.`p_insurancetype`='"+insurance+"' GROUP BY M.`misc_namecat`";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public ResultSet retrieveAllDataAmount(String id) {
		String query = "SELECT  `misc_amount_reciept`, `misc_namecat`, `misc_p_id`, `misc_p_name`, `misc_amount`,`misc_date` FROM `misc_entry` WHERE  `misc_amount_reciept`= '"+ id	+ "'";
		try {
			System.out.println(query);
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllMiscCat() {
		String query = "SELECT DISTINCT `misc_namecat` FROM misc_entry";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public void deleteRow(String rowID) throws Exception {
		PreparedStatement preparedStatement = connection
				.prepareStatement("DELETE FROM misc_entry WHERE id=?");
		preparedStatement.setString(1, rowID);
		preparedStatement.executeUpdate();
	}

	// `opd_id`, `p_id`, `p_name`, `opd_doctor`, `opd_date`, `opd_token`,
	// `opd_type`
	public int inserData(String[] data) throws Exception {
		String insertSQL = "INSERT INTO `misc_entry`(`misc_amount_reciept`, `misc_namecat`, `misc_desc`, `misc_doctor_ref`, `misc_p_id`, `misc_p_name`, `misc_amount`, `misc_username`, `misc_date`, `misc_time`,`p_insurancetype`,`payment_mode`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement preparedStatement = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
		for (int i = 1; i < 13; i++) {

			preparedStatement.setString(i, data[i - 1]);
		}

		preparedStatement.executeUpdate();
		ResultSet rs = preparedStatement.getGeneratedKeys();
		rs.next();

		return  rs.getInt(1);
	}
}
