// For checking User Activity for Whole Day
package UsersActivity.database;

import hms.main.DBConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Calendar;

import javax.swing.JOptionPane;

public class UADBConnection extends DBConnection{
	DBConnection db=new DBConnection();
	Connection connect = null;
	Statement stmt = null;
	ResultSet rs = null;
//	String Date = LocalDate.now().toString();
	String[] act = new String[500];
	String[] dpt = new String[9];


	public UADBConnection() {

		super();
		dpt[0]="Reception";
		dpt[1]="Doctor";
		dpt[2]="Exam Lab";
		dpt[3]="Admin";
		dpt[4]="Accounts"; 
		dpt[5]="Store";
		dpt[6]="Departments";
		dpt[7]="MRD";
		dpt[8]="EMO";

		/******************		Start OF Admin Activities     ********************/
		act[0]="Manage Accounts/Doctor/New doctor";
		act[1]="Manage Accounts/Doctor/Manage Doctors";
		act[2]="Manage Accounts/Doctor/Doctor Availability";
		act[3]="Manage Accounts/Doctor/Cancel Doctor Availability";
		act[4]="Manage Accounts/Salaries/Add Salary";
		act[5]="Manage Accounts/Lab Operator/New Oparator";
		act[6]="Manage Accounts/Lab Operator/Manage Oparator";
		act[7]="Manage Accounts/Receptionist/New Receptionist";
		act[8]="Manage Accounts/Receptionist/Manage Receptionist";
		act[9]="Manage Accounts/Receptionist/Reception Counters";
		act[10]="Manage Accounts/MRD/New User";
		act[11]="Manage Accounts/MRD/Manage User";
		act[12]="Manage Accounts/Accounts/New User";
		act[13]="Manage Accounts/Accounts/Manage User";
		act[14]="Manage Accounts/Departments/Create New Sub Department";
		act[15]="Manage Accounts/Departments/New Departments Login";
		act[16]="Manage Accounts/Departments/Manage Departments Charges";
		act[17]="Manage Accounts/Departments/Manage Department User Login";
		act[18]="Manage Accounts/Store Accounts/New Store Account";
		act[19]="Manage Accounts/Store Accounts/Manage Store Account";
		act[20]="Masters/OPD/Manager";
		act[21]="Masters/Exam/New Exam Type";
		act[22]="Masters/Exam/Manage Exam Types";//  module not created yet
		act[23]="Masters/Insurance/New Insurance Type";
		act[24]="Masters/Insurance/Manage Insurance Types";//module not created yet
		act[25]="Masters/Indoor Package/New Package";
		act[26]="Store/Suppliers/New Suppliers";
		act[27]="Store/Suppliers/Edit Suppliers";
		act[28]="Store/Item Master/New Item";
		act[29]="Store/Item Master/Manage Items";
		act[30]="Store/Item Master/Department Item Minimum Set";
		act[31]="Store/Item Master/Stock Register";
		act[32]="Store/Item Master/Stock Adjustment";
		act[33]="Store/Quotation/Quotation Entry";
		act[34]="Store/Reports/Item Detail";
		act[35]="Store/Reports/Department Issued Register";
		act[36]="Store/Reports/Patient Issued Register";
		act[37]="Store/Reports/Exam Done Report";
		act[38]="Store/Reports/Item Consumed Report";
		act[39]="Store/Reports/Total Pills Issued";
		act[40]="Store/Reports/Department Wise issued";
		act[41]="Store/Reports/Puchase Order";
		act[42]="Store/Reports/Invoice Items Reports";
		act[43]="Store/Reports/Items Issued From Department";
		act[44]="Store/Reports/Exam BOM Detail";
		act[45]="Store/Invoice/PO/Purchase Invoice/New Invoice";
		act[46]="Store/Invoice/PO/Purchase Invoice/Manager";
		act[47]="Store/Invoice/PO/Purchase Order/New Form";
		act[48]="Store/Invoice/PO/Purchase Order/Manager";
		act[49]="Store/Invoice/PO/Purchase Order/Automatic Generate";
		act[50]="Store/Invoice/PO/Return Invoice/New Invoice";
		act[51]="Store/Invoice/PO/Return Invoice/Return Invoice Manager";
		act[52]="Store/Issue/Return Register/Issue Items/New Issue Form";
		act[53]="Store/Issue/Return Register/Opening Stock/New Form";
		act[54]="Store/Issue/Return Register/Return Items/New Return Form";
		act[55]="Store/Issue/Return Register/Issue To Patient/New Issue Form";
		act[56]="Store/Issue/Return Register/Return From Patient/New Form";
		act[57]="Store/Issue/Return Register/Request Register/Register";
		act[58]="Store/Issue/Return Register/Procedure Entry/New Entry";
		act[59]="Store/Issue/Return Register/Exam BOM/New Entry";
		act[60]="Store/Issue/Return Register/Exam BOM/Details";
		act[61]="Store/My Stock/Stock Register";
		act[62]="Reports/Summery Report";
		act[63]="Reports/Insurance Summery";
		act[64]="Reports/Exams Report";
		act[65]="Reports/Exam Done Reports";
		act[66]="Reports/Item Consumed report";
		act[67]="Reports/IPD Reports/Pending Patients";
		act[68]="Reports/IPD Reports/Advance Amount";
		act[69]="Reports/IPD Reports/Discharged Patients";
		act[70]="Reports/Doctors Reports";
		act[71]="Reports/Doctors Cost Center Reports";
		act[72]="Reports/Insurance Wise Reports";
		act[73]="Reports/Cancelled Reports";
		act[74]="Reports/Summery Excel";
		act[75]="Reports/Free Test Report";
		act[76]="Reports/Patients Ward Visit";
		act[77]="Reports/Reports/Wards Issued Consume";
		act[78]="Reports/Reports/IPD And DPD Medicines";
		act[79]="Reports/Reports/Ward Wise, Doctor Wise Days";
		act[80]="Reports/Reports/Patient Wise Days";
		act[81]="Records Manager/Central Store/Main Stock";
		act[82]="Records Manager/Central Store/Purchase Manager/Purchase Invoice/New Invoice";
		act[83]="Records Manager/Central Store/Purchase Manager/Purchase Order/New Form";
		act[84]="Records Manager/Central Store/Purchase Manager/Purchase Order/Manager";
		act[85]="Records Manager/Central Store/Purchase Manager/Purchase Order/Automatic Generate";
		act[86]="Records Manager/Central Store/Purchase Manager/Return Invoice/New Invoice";
		act[87]="Records Manager/Central Store/Item Wise Purchase Manager";
		act[88]="Records Manager/Central Store/Item Issued To Patient";
		act[89]="Records Manager/Central Store/Deprtment Wise Issue Register";
		act[90]="Records Manager/Central Store/All Issue To Department Register";
		act[91]="Records Manager/Central Store/Main Store Top 20 Items";
		act[92]="Records Manager/Central Store/Department Top 20 Iems";
		act[93]="Records Manager/Reception/Patient Manager";
		act[94]="Records Manager/Reception/OPD Manager";
		act[162]="Records Manager/Reception/Exam Manager";
		act[95]="Records Manager/Reception/Misc Manager";
		act[96]="Records Manager/Reception/IPD Manager";
		act[97]="Records Manager/Reception/Procedure IPD Manager";
		act[98]="Records Manager/Reception/Dailysis IPD Manager";
		act[99]="Records Manager/Reception/Emergency IPD Manager";
		act[100]="Records Manager/Departments/Department Stock Register";
		act[101]="Records Manager/Departments/Item Received From Store Register";
		act[102]="Records Manager/Departments/All Receive From Store Register";
		act[103]="Records Manager/Departments/Issued To Patient";
		act[104]="Record Cancellation/Cancel OPD";
		act[105]="Record Cancellation/Cancel Exams";
		act[106]="Record Cancellation/Cancel Misc";
		act[107]="Record Cancellation/Cancel Patient Card";
		act[108]="Record Cancellation/Cancel IPD Exams";
		act[109]="Email Reports/Email Settings";// not created yet
		act[110]="Email Reports/Email Reports";
		act[111]="Email Reports/ Email";// not created yet

		/******************		End OF Admin Activities     ********************/

		/******************		Start OF Reception Activities     ********************/

		act[112]="My Report/My Collection";
		act[113]="IPD Billing/ESI Bill";
		act[114]="IPD Billing/ECHS Bill";
		act[115]="IPD Billing/Railway Bill";
		act[116]="OPD/New OPD";
		act[117]="Exam/New Exam";
		act[118]="Exam/IPD Exam Entry";
		act[119]="IPD/New IPD";
		act[120]="IPD/IPD Bill";
		act[121]="IPD/Return-Deposit";
		act[122]="IPD/Change Doctor";
		act[123]="IPD/Change Package";
		act[124]="IPD/Change Bed";
		act[125]="IPD/Bed Details Reports";
		act[126]="Patients/New Patients";
		act[127]="Misc Charges/New Entry";
		act[128]="Daily Colllection";
		act[129]="Patient History";
		act[130]="EMG IPD Manager";
		act[131]="Procedure IPD Manager";
		act[132]="Dialysis IPD MAnager";

		/********************** End OF Reception Activities*************/

		/********************** Start OF Accounts Activities*************/

		act[133]="Misc Reciept/Medicenes Request Register";
		act[134]="Misc Reciept/My Reports";
		act[135]="IPD Patients/IPD Advance/Return Entry";
		act[136]="IPD Patients/Generate Bill";
		act[137]="Report Accounts/Summery Report";
		act[138]="Report Accounts/Users Report";
		act[139]="Report Accounts/Doctors Report";

		/********************** End OF Accounts Activities*************/

		/********************** Start OF doctors Activities*************/
		act[140]="Doctors Module";
		act[141]="My Patient/IPD Patient";
		act[142]="My Patient/OPD Patient";

		/********************** End OF doctors Activities*************/

		/********************** Start OF MRD Activities*************/

		act[143]="MRD Records";
		act[144]="MRD Records/IPD Files";
		act[145]="MRD Records/OPD Files";
		act[146]="MRD Records/Exam Files";

		/********************** End OF MRD Activities*************/

		/********************** Start OF Departments Activities*************/

		act[147]="Pills Request/Indoor Patients/New Entry";
		act[148]="Patient Exams/New Exam Entry";
		act[149]="Patient Exams/Test History";
		act[150]="Indoor Patient/Change Bed";
		act[151]="Indoor Patient/Operation Theatre Time";
		act[152]="Pills Entry/Outdoor Patients/New Entry";
		act[153]="Pills Entry/Outdoor Patients/Previous Entries";
		act[154]="Pills Entry/Outdoor Patients/Procedure Entry";
		act[155]="Pills Entry/Outdoor Patients/Medicine Bill Request";
		act[157]="Pills Entry/Indoor Patients/New Entry";
		act[158]="Pills Entry/Indoor Patients/Procedure Entry";
		act[159]="Pills Entry/Indoor Patients/Previous Entries";
		act[160]="My Stock/Stock Register";
		// act[161]="Pills Reminder";

		//act[162] for mistake

		/********************** End OF Departments Activities*************/

		/********************** Start OF Store Activities*************/

		act[163]="Suppliers/New Supplier";
		act[164]="Suppliers/Edit Supplier";
		act[165]="Item Master/New Item";
		act[166]="Item Master/Manage Items";
		act[167]="Item Master/Department Item Minimum Set";
		act[168]="Item Master/Stock Register";
		act[169]="Item Master/Stock Adjustment";
		act[170]="Order/Puchase Order";
		act[171]="Reports/Puchase Order";
		act[172]="Reports/Item Detail";
		act[173]="Reports/Department Issued Register";
		act[174]="Reports/Patient Issued Register";
		act[175]="Reports/Exam Done Report";
		act[176]="Reports/Item Consumed Report";
		act[177]="Reports/Total Pills Issued";
		act[178]="Reports/Department Wise issued";
		act[179]="Reports/Invoice Items Reports";
		act[180]="Reports/Items Issued From Department";
		act[181]="Quotation/Quotation Entry/New Entry";
		act[182]="Invoice/PO/Purchase Invoice/New Invoice";
		act[183]="Invoice/PO/Purchase Invoice/Manager";
		act[184]="Invoice/PO/Purchase Order/New Form";
		act[185]="Invoice/PO/Purchase Order/Manager";
		act[186]="Invoice/PO/Return Invoice/New Invoice";
		act[187]="Invoice/PO/Return Invoice/Return Invoice Manager";
		act[188]="Issue/Return Register/Issue Items/New Issue Form";
		act[189]="Issue/Return Register/Opening Stock/New Form";
		act[190]="Issue/Return Register/Transfer Stock";
		act[191]="Issue/Return Register/Return Items/New Return Form";
		act[192]="Issue/Return Register/Issue To Patient/New Issue Form";
		act[193]="Issue/Return Register/Return From Patient/Medical Store Return";
		act[194]="Issue/Return Register/Return From Patient/Hospital Return";
		act[195]="Issue/Return Register/Request Register/Register";
		act[196]="Issue/Return Register/Procedure Entry/New Entry";
		act[197]="Issue/Return Register/Exam BOM/New Entry";
		act[198]="Issue/Return Register/Exam BOM/Details";
		act[199]="Issue/Return Register/Pills Entry Outdoor Patient/New Entry";
		act[200]="Issue/Return Register/Pills Entry Outdoor Patient/Previouse Entries";
		act[201]="Issue/Return Register/Pills Entry Outdoor Patient/Procedure Entry";
		act[202]="Issue/Return Register/Pills Entry Outdoor Patient/Medicine Bill Request";
		act[203]="Issue/Return Register/Dept. Request Pending Register";
		act[204]="Challan/Challan List/New Challan";
		act[205]="Challan/Challan List/Challan Manager";
		act[206]="Return/Issue Challan";
		act[207]="My Stock/Stock Register";

		/********************** End OF Store Activities*************/


		/********************** Start OF Exam Lab Activities*************/

		act[208]="Patients/My Patients";
		act[209]="Patients/Patients Test History";
		act[210]="Test/Results/Add Free Test Results";
		act[211]="Store/New Entry";
		act[212]="Store/My Stock";

		/********************** End OF Exam Lab Activities*************/

		connect = getConnection();
		stmt= getStatement();

	}


	public void adduser_activity(String u_name,int a,int f,int d) {
//		try {
//
//			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO `Users_activity`( `user_name`, `user_activity`, `frequency`, `date`,`department`) VALUES (?,?,?,?,?)");
//			pstmt.setString(1,u_name );
//			pstmt.setString(2, act[a]);
//			pstmt.setFloat(3, f);
//			pstmt.setString(4, Date);
//			pstmt.setString(5, dpt[d]);
//
//			pstmt.executeUpdate();
//
//
//
//		}catch (SQLException sqlex) {
//			JOptionPane.showMessageDialog(null, sqlex.getMessage(), "ERROR",
//					javax.swing.JOptionPane.ERROR_MESSAGE);
//		}
//		db.closeConnection();
	}

	public void Updateuser_activity(String u_name,int a,int d) {
		//To increase the user activity frequency by 1
//		try {
//
//			PreparedStatement pstmt = connect
//					.prepareStatement("update Users_activity set frequency=frequency+1 where user_name=? and user_activity=? and date=? and department=?");
//			pstmt.setString(1,u_name );
//			pstmt.setString(2, act[a]);
//			pstmt.setString(3, Date);
//			pstmt.setString(4, dpt[d]);
//			pstmt.executeUpdate();
//
//		}catch (SQLException sqlex) {
//			JOptionPane.showMessageDialog(null, sqlex.getMessage(), "ERROR",
//					javax.swing.JOptionPane.ERROR_MESSAGE);
//		}
//
//		db.closeConnection();

	}

	public void check_activity(String u_name,int a,int d) {
//		// To check present date activity
//		try {
//
//			PreparedStatement pstmt = connect.prepareStatement("select user_name,user_activity,date,department from Users_activity where user_name=? and user_activity=? and date=? and department=?");
//			pstmt.setString(1,u_name);
//			pstmt.setString(2,act[a]);
//			pstmt.setString(3,Date);
//			pstmt.setString(4,dpt[d]);
//
//			//System.out.println(pstmt);
//			rs = pstmt.executeQuery();
//			//rs = statement.executeQuery(sql);
//			if (rs.next()) 
//			{
//				UADBConnection ua=new UADBConnection();
//				ua.Updateuser_activity(u_name, a,d);
//			}
//			else
//			{
//				UADBConnection ua=new UADBConnection();
//				ua.adduser_activity(u_name, a, 1,d);
//			}
//		} 
//		catch (SQLException sqlex) {
//			JOptionPane.showMessageDialog(null, sqlex.getMessage(), "ERROR",
//					javax.swing.JOptionPane.ERROR_MESSAGE);
//		}
//
	}

}

