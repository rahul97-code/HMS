package hms.patient.slippdf;

import hms.Printer.PrintFile;
import hms.main.DateFormatChange;
import hms.main.MainLogin;
import hms.main.NumberToWordConverter;
import hms.opd.gui.OPDBrowser;
import hms.patient.database.PatientDBConnection;
import hms1.expenses.database.IPDExpensesDBConnection;
import hms1.ipd.database.IPDDBConnection;
import hms1.ipd.gui.IPDBrowser;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class ProvisionalIPDBillSlippdf {

	private static Font smallBold = new Font(Font.FontFamily.HELVETICA, 8);
	private static Font spaceFont = new Font(Font.FontFamily.HELVETICA, 2);
	private static Font font1 = new Font(Font.FontFamily.HELVETICA, 17,
			Font.BOLD, BaseColor.BLACK);
	private static Font font2 = new Font(Font.FontFamily.HELVETICA, 8, 
			Font.BOLD);
	private static Font HeaderFont = new Font(Font.FontFamily.TIMES_ROMAN, 14,
			Font.BOLDITALIC);
	private static Font font3 = new Font(Font.FontFamily.HELVETICA, 8.5f,
			Font.BOLD);
	private static Font font4 = new Font(Font.FontFamily.HELVETICA, 9,
			Font.BOLD, BaseColor.BLACK);
	private static Font tokenfont4 = new Font(Font.FontFamily.HELVETICA, 11,
			Font.BOLD, BaseColor.WHITE);
	public static String RESULT = "opdslip1.pdf";
	double totalCharges = 0, ipd_advance = 0, ipd_balance = 0,
			payableAmount = 0;
	String ipd_no_a, patient_id, patient_name, patient_address,patient_insurancetype,patient_claim_id,patient_insurance_no, patient_age, doctor_name,
			amt_received, date, bill_no;
	String ipd_doctor_id = "", ipd_doctorname = "", ipd_date = "",
			ipd_time = "", ipd_date_dis = "", ipd_time_dis = "", ipd_note = "",
			ipd_id = "", ward_name = "", building_name = "",
			bed_no = "Select Bed No", ward_incharge = "", ward_room = "",
			ward_code = "", descriptionSTR = "", charges = "", ipd_days,
			ipd_hours, ipd_minutes, ipd_expenses_id,emergency="No";

	String mainDir = "";
	Font font;
//	float[] TablCellWidth = {  0.7f,2.0f,1.0f, 1.1f,1.1f,1.1f };
	String[] open = new String[4];
private String DischargeType;
private String DischargeUser;

	public static void main(String[] argh) {
		try {
			new ProvisionalIPDBillSlippdf("Provisional Bill","55288", "sdfsdf",false);
		} catch (DocumentException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ProvisionalIPDBillSlippdf(String ipd_bill,String ipd_no, String doctorName,boolean AutoPrint)
			throws DocumentException, IOException {
		// TODO Auto-generated constructor stub
		bill_no=ipd_bill;
		getIPDData(ipd_no);
		getPatientDetail(patient_id);
		readFile();
		this.ipd_id=ipd_no;
		makeDirectory(ipd_no);
	
		Document document = new Document();
	
		PdfWriter wr = PdfWriter.getInstance(document, new FileOutputStream(
				RESULT));
		wr.setBoxSize("art", new Rectangle(36, 54, 559, 788));
		document.setPageSize(PageSize.LETTER);
		document.setMargins(0, 0, 10, 0);
		document.open();
		BaseFont base = BaseFont.createFont("indian.ttf", BaseFont.WINANSI,
				BaseFont.EMBEDDED);
		font = new Font(base, 8f);

		PdfPTable table = new PdfPTable(1);
		table.getDefaultCell().setBorder(0);
		table.setWidthPercentage(90);
		table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

		float[] tiltelTablCellWidth = { 0.1f, 1f, 0.1f };
		PdfPTable TitleTable = new PdfPTable(tiltelTablCellWidth);
		TitleTable.getDefaultCell().setBorder(0);

		java.net.URL imgURL = ProvisionalIPDBillSlippdf.class
				.getResource("/icons/rotaryLogo.png");
		Image image = Image.getInstance(imgURL);

		image.scalePercent(50);
		image.setAbsolutePosition(100, 260);

		java.net.URL imgURLRotaryClub = ProvisionalIPDBillSlippdf.class
				.getResource("/icons/Rotary-Club-logo.jpg");
		Image imageRotaryClub = Image.getInstance(imgURLRotaryClub);

		// imageRotaryClub.scalePercent(60);
		// imageRotaryClub.setAbsolutePosition(40, 750);

		PdfPCell logocell2 = new PdfPCell(imageRotaryClub);
		logocell2.setRowspan(3);
		logocell2.setBorder(Rectangle.NO_BORDER);
		logocell2.setHorizontalAlignment(Element.ALIGN_CENTER);
		logocell2.setPaddingRight(5);
		TitleTable.addCell(logocell2);

		PdfPCell namecell = new PdfPCell(new Phrase(
				"ROTARY AMBALA CANCER AND GENERAL HOSPITAL" + "\n", font1));
		namecell.setHorizontalAlignment(Element.ALIGN_CENTER);
		namecell.setPaddingBottom(5);
		namecell.setBackgroundColor(BaseColor.LIGHT_GRAY);
		TitleTable.addCell(namecell);

		PdfPCell logocell = new PdfPCell(image);
		logocell.setRowspan(3);
		logocell.setBorder(Rectangle.NO_BORDER);
		logocell.setHorizontalAlignment(Element.ALIGN_CENTER);
		logocell.setPaddingLeft(3);
		TitleTable.addCell(logocell);
		PdfPCell addressCell = new PdfPCell(new Phrase(
				"Opp. Dussehra Ground, Ram Bagh Road, Ambala Cantt (Haryana)",
				font2));
		addressCell.setPaddingBottom(2);
		addressCell.setBorder(Rectangle.NO_BORDER);
		addressCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		TitleTable.addCell(addressCell);

		PdfPCell addressCell2 = new PdfPCell(
				new Phrase(
						"Telephone No. : 0171-2690009, Mobile No. : 09034056793",
						font2));
		addressCell2.setPaddingBottom(5);
		addressCell2.setBorder(Rectangle.NO_BORDER);
		addressCell2.setHorizontalAlignment(Element.ALIGN_CENTER);
		TitleTable.addCell(addressCell2);

		table.addCell(TitleTable);

		
		String bill=" Bill cum Cash Receipt ";
		if(!patient_insurancetype.equals("Unknown"))
		{
			bill=patient_insurancetype +" Polyclinic Ambala Online";
		}
		
		PdfPCell spaceCell = new PdfPCell(new Paragraph(bill, font4));
		spaceCell.setBorder(Rectangle.NO_BORDER);
		spaceCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		spaceCell.setPaddingTop(1);
		spaceCell.setPaddingBottom(5);
		table.addCell(spaceCell);
		
		  PdfContentByte cb1 = wr.getDirectContent();
	        BaseFont bf = BaseFont.createFont();
	        
	        Font f1 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 59);
	        f1.setColor(BaseColor.GRAY);
	        Phrase phrase3 = new Phrase("PROVISIONAL", f1);
	        if(bill_no.equals("Provisional Bill"))
	        	ColumnText.showTextAligned(cb1, Element.ALIGN_CENTER, phrase3,300,400,30);
	        
	      
	        cb1.saveState();
	       // cb1.setColorStroke(BaseColor.BLACK);
	        //cb1.rectangle(501, 657,80, 70);
	        cb1.stroke();
	        cb1.restoreState();
		
		
		float[] opdTablCellWidth = { 0.8f, 1.3f, 1f, 1f };
		PdfPTable opdTable = new PdfPTable(opdTablCellWidth);
		opdTable.getDefaultCell().setBorder(0);

		PdfPCell tokencell = new PdfPCell(new Phrase("Bill No." + "\n", font3));
		tokencell.setBorder(Rectangle.NO_BORDER);

		PdfPCell tokenNocell = new PdfPCell(new Phrase(bill_no + "\n", font3));
		tokenNocell.setBorder(Rectangle.NO_BORDER);

		opdTable.addCell(tokencell);
		opdTable.addCell(tokenNocell);

		opdTable.addCell(new Phrase("Patient ID: ", font3));
		opdTable.addCell(new Phrase("" + patient_id, font3));

		opdTable.addCell(new Phrase("IPD No. : ", font3));
		if(emergency.equals("Yes")){
			opdTable.addCell(new Phrase("" + ipd_no_a+" "+"(Emergency)", font3));
		}else{
			opdTable.addCell(new Phrase("" + ipd_no_a, font3));

		}
		
		opdTable.addCell(new Phrase("Patient Name: ", font3));
		opdTable.addCell(new Phrase("" + patient_name, font3));
		
		
		opdTable.addCell(new Phrase("Doctor Incharge :", font3));
		PdfPCell consultantcell = new PdfPCell(new Phrase("" + doctorName,
				font3));
		consultantcell.setBorder(Rectangle.NO_BORDER);
		opdTable.addCell(consultantcell);
		
		PdfPCell agecell = new PdfPCell(new Phrase(""+patient_age, font3));
		agecell.setBorder(Rectangle.NO_BORDER);
		opdTable.addCell(new Phrase("Age : ", font3));
		opdTable.addCell(agecell);


		opdTable.addCell(new Phrase("Admissioin Date/Time : ", font3));
		opdTable.addCell(new Phrase(""+DateFormatChange.StringToDateFormat(ipd_date)+" / "+ipd_time, font3));

		opdTable.addCell(new Phrase("Address : ", font3));
		opdTable.addCell(new Phrase(""+patient_address, font3));


		opdTable.addCell(new Phrase("Discharge Date/Time : ", font3));
		opdTable.addCell(new Phrase(""+DateFormatChange.StringToDateFormat(ipd_date_dis)+" / "+ipd_time_dis, font3));
		
		opdTable.addCell(new Phrase("Insurance : ", font3));
		opdTable.addCell(new Phrase(""+patient_insurancetype, font3));

//		opdTable.addCell(new Phrase("Bed No. : ", font3));
//		opdTable.addCell(new Phrase(""+bed_no+" ("+ward_name+")", font3));
//		opdTable.addCell(new Phrase("No. of Days", font3));
//		opdTable.addCell(new Phrase(""+ipd_days+" Days, "+ipd_hours+" Hours", font3));
		
		opdTable.addCell(new Phrase("Insurance No. : ", font3));
		opdTable.addCell(new Phrase(""+patient_insurance_no, font3));
		
		opdTable.addCell(new Phrase("Generated By:", font3));
		opdTable.addCell(new Phrase(DischargeUser , font3));
		
		
		opdTable.addCell(new Phrase("Claim ID. : ", font3));
		opdTable.addCell(new Phrase(""+patient_claim_id, font3));
		
		opdTable.addCell(new Phrase("Discharge Type:", font3));
		opdTable.addCell(new Phrase(DischargeType , font3));
		
		opdTable.addCell(new Phrase("", font3));
		opdTable.addCell(new Phrase(" ", font3));
//		opdTable.addCell(new Phrase("Emergency:", font3));
//		opdTable.addCell(new Phrase(MainLogin.userName1, font3));

		PdfPCell opdCell = new PdfPCell(opdTable);
		opdCell.setBorderWidth(0.8f);
		opdCell.setPaddingBottom(5);
		table.addCell(opdCell);

		table.addCell(new Phrase("  ", font3));
		document.add(table);
		document.add(ipdAmount(ipd_no));
		
		PdfPTable table2 = new PdfPTable(1);
		table2.getDefaultCell().setBorder(0);
		table2.setWidthPercentage(90);
		
		table2.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		PdfPCell total = new PdfPCell(new Paragraph("Total :     "+totalCharges, smallBold));
		total.setBorder(Rectangle.NO_BORDER);
		total.setHorizontalAlignment(Element.ALIGN_RIGHT);
		total.setPaddingTop(10);
		total.setPaddingBottom(1);
		table2.addCell(total);
		
		PdfPCell advance = new PdfPCell(new Paragraph("(-)Less Advance Amt :        "+ipd_advance, smallBold));
		advance.setBorder(Rectangle.NO_BORDER);
		advance.setHorizontalAlignment(Element.ALIGN_RIGHT);
		advance.setPaddingTop(2);
		advance.setPaddingBottom(1);
		table2.addCell(advance);
		
		if(bill_no.equals("Provisional Bill"))
		{
			payableAmount=totalCharges-ipd_advance;
		}

		PdfPCell payable = new PdfPCell(new Paragraph(new Chunk(" Amount Payable :     "+payableAmount, font3).setUnderline(0.5f, -2f)));
		payable.setBorder(Rectangle.NO_BORDER);
		payable.setHorizontalAlignment(Element.ALIGN_RIGHT);
		payable.setPaddingTop(2);
		payable.setPaddingBottom(1);
		table2.addCell(payable);
		
		
		
		System.out.println("payableAmount  "+payableAmount);
		String neg1="";
	    double cashTotal2=0;
	    cashTotal2=payableAmount;
	    if(cashTotal2<0)
	    {
	    	cashTotal2=Math.abs(cashTotal2);
	        neg1="-";
	    }else {
	    	neg1="";
	    }
		
		PdfPCell payableInWords = new PdfPCell(new Paragraph("In Words : "+neg1+" "+NumberToWordConverter.convert((int)cashTotal2)+" Only", font3));
		//PdfPCell payableInWords = new PdfPCell(new Paragraph("In Words : "+NumberToWordConverter.convert((int)payableAmount)+" Only", font3));
		payableInWords.setBorder(Rectangle.NO_BORDER);
		payableInWords.setHorizontalAlignment(Element.ALIGN_RIGHT);
		payableInWords.setPaddingTop(5);
		payableInWords.setPaddingBottom(10);
		table2.addCell(payableInWords);
		
		PdfPCell footer2 = new PdfPCell(
				new Phrase(
						"Received Rs "+totalCharges+" by cash on date "+DateFormatChange.StringToDateFormat(ipd_date_dis)+" toward settlement of the above bill.",
						font3));
		footer2.setBorder(Rectangle.NO_BORDER);
		footer2.setPaddingBottom(5);
		footer2.setHorizontalAlignment(Element.ALIGN_CENTER);
		if(patient_insurancetype.equals("Unknown")&&!bill_no.equals("Provisional Bill"))
		{
			table2.addCell(footer2);
		}
		

		PdfPCell sign = new PdfPCell(new Paragraph("Authorised Signatory-Hospital", font4));
		sign.setBorder(Rectangle.NO_BORDER);
		sign.setHorizontalAlignment(Element.ALIGN_RIGHT);
		sign.setPaddingTop(10);
		sign.setPaddingBottom(1);

		if(!bill_no.equals("Provisional Bill")) 
		{
			table2.addCell(sign);
		}
		String value="";
//		if(bill_no.equals("Provisional Bill")){
//			value="Provisional Bill";
//		}
//		PdfPCell usename = new PdfPCell(new Paragraph(value+" By: "+MainLogin.userName1, font2));
//		usename.setBorder(Rectangle.NO_BORDER);
//		usename.setHorizontalAlignment(Element.ALIGN_RIGHT);
//		usename.setPaddingTop(5);
//		usename.setPaddingBottom(10);
//		table2.addCell(usename);
		document.add(table2);
		
		
		// onEndPage(wr,document);
		document.close();

		new OpenFile(RESULT);
		if(AutoPrint)
			new PrintFile(RESULT,8);
	}

	public PdfPTable ipdAmount(String ipd_no) {
		
		
		float[] TablCellWidth=new float[15];
		
		TablCellWidth = new float[]  {0.7f,3.4f,1.2f,0.6f,1.0f,0.7f,0.8f,0.9f,1.1f,1.2f,1.2f,0.9f};

		PdfPTable Table = new PdfPTable(TablCellWidth);
		Table.setWidthPercentage(90);	
			
		try {
			IPDExpensesDBConnection db1 = new IPDExpensesDBConnection();
			ResultSet rs = db1.retrieveAllCashIPD_DATA(ipd_id);
			AddAllHeaders(Table);
			int S_no = 1,B=0,C=0,M=0,E=0,O=0,P=0,R=0,HMS=0,MS=0;
			while (rs.next()) {
			
				Object Type=rs.getObject(4);
				if (Type.equals("B") && B == 0) {
					Table.addCell(Header("ACCOMODATION"));
					B++;
				} else if ((Type.equals("I") || Type.equals("M")) && M == 0) {
					Table.addCell(Header("PHARMACY"));
					M++;
				} else if (Type.equals("C") && C == 0) {
					Table.addCell(Header("CONSULTATION"));
					C++;
				} else if ((Type.equals("T") || Type.equals("S")) && E == 0) {
					Table.addCell(Header("EXAMS"));
					E++;
				} else if (Type.equals("O") && O == 0) {
					Table.addCell(Header("OTHERS"));
					O++;
				} else if (Type.equals("R") && R == 0) {
					Table.addCell(Header("RETURNS"));
					R++;
				}
				
				Object Desc=rs.getObject(2);
				Object Date=rs.getObject(3);
				Object Page=rs.getObject(6);
				Object ItemID=rs.getObject(5);
				Object Mrp=rs.getObject(7);
				Object Amount=rs.getObject(10);
				Double Amt=Double.parseDouble(Amount.equals(null)||Amount.equals("")?"0":Amount+"");
				Amt=Math.round(Amt*100.00)/100.00;
				String Batch=rs.getString(11);
				String Expiry=rs.getString(12);
				Object Qty=rs.getObject(9);
				Object Rate=rs.getObject(8);
				Double rate=Double.parseDouble(Rate.equals(null)||Rate.equals("")?"0":Rate+"");
				rate=Math.round(rate*100.00)/100.00;
				

				Table.addCell(new Phrase(	"" + S_no, font3)); 
				Table.addCell(new Phrase(Desc.equals(null)||Desc.equals("")?"":Desc.toString(), font3));
				Table.addCell(new Phrase(Date.equals(null)||Date.equals("")?"":Date.toString(), font3));
				Table.addCell(new Phrase(Type.equals(null)||Type.equals("")?"":Type.toString(), font3));
				Table.addCell(new Phrase(Page.equals(null)||Page.equals("")?"":Page.toString(), font3));
				Table.addCell(new Phrase(ItemID.equals(null)|| Desc.toString().contains(ItemID+"") ?"n/a":ItemID.toString(), font3));
				Table.addCell(new Phrase((Type.equals("R")?"": Mrp+""), font3));				
				Table.addCell(new Phrase((Type.equals("R")?"":rate+""),font3));	
				String temp=Type.equals("B")?" Hrs":"";
				Table.addCell(new Phrase(Qty+"", font3));
				Table.addCell(new Phrase(Batch , font3));
				Table.addCell(new Phrase(Expiry, font3));
			    
				PdfPCell amount1 = new PdfPCell(new Phrase("", font3)); //paste here (Amt)  change for all ins and cash bills
				amount1.setHorizontalAlignment(Element.ALIGN_RIGHT);
				Table.addCell(amount1);
				S_no++;
			}
		} catch (SQLException ex) {
			Logger.getLogger(IPDBrowser.class.getName()).log(Level.SEVERE,
					null, ex);
		}
		return Table;
	}
	
	private void AddAllHeaders(PdfPTable Table) {
		// TODO Auto-generated method stub
		Table.addCell(new Phrase("S.No.", font3));
		Table.addCell(new Phrase("Description", font3));
		Table.addCell(new Phrase("Date", font3));
		Table.addCell(new Phrase("Type", font3));
		Table.addCell(new Phrase("Remarks", font3));
		Table.addCell(new Phrase("Code", font3));
		Table.addCell(new Phrase("MRP", font3));
		Table.addCell(new Phrase("Rate", font3));
		Table.addCell(new Phrase("Quantity", font3));
		Table.addCell(new Phrase("Batch", font3));
		Table.addCell(new Phrase("Expiry", font3));
		PdfPCell amount = new PdfPCell(new Phrase("Amount", font3));
		amount.setHorizontalAlignment(Element.ALIGN_RIGHT);
		Table.addCell(amount);
	}

	public PdfPCell Header(String n) {
		PdfPCell cell = new PdfPCell(new Phrase(""+n, font2));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
		cell.setColspan(12);
		cell.setPaddingBottom(3);
		
		return cell;
	}
	public PdfPCell MainHeader(String n) {
		PdfPCell cell = new PdfPCell(new Phrase(""+n+" ...", HeaderFont));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
//		cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
		cell.setColspan(12);
		cell.setPaddingBottom(10);
		
		return cell;
	}

	public void getIPDData(String ipdID) {
		try {
			IPDDBConnection db = new IPDDBConnection();
			ResultSet rs = db.retrieveAllDataIPDID(ipdID);
			while (rs.next()) {
				ipd_no_a = rs.getObject(1).toString();
				patient_id = rs.getObject(2).toString();
				patient_name = rs.getObject(3).toString();
				ward_name = rs.getObject(4).toString();
				bed_no = rs.getObject(5).toString();
				ipd_date = rs.getObject(6).toString();
				ipd_time = rs.getObject(7).toString();
//				ipd_advance = Double.parseDouble(rs.getObject(8).toString());
//				totalCharges = Double.parseDouble(rs.getObject(9).toString());
//				ipd_balance = Double.parseDouble(rs.getObject(10).toString());
//				payableAmount = Double.parseDouble(rs.getObject(11).toString());
				emergency = rs.getObject(14).toString();
			    DischargeType=rs.getObject(16)!=null?rs.getObject(16)+"":"";
			    DischargeUser=rs.getObject(17)!=null || rs.getObject(17).equals("N/A")?rs.getObject(17)+"":MainLogin.userName1;
				if(!bill_no.equals("Provisional Bill"))
				{
					ipd_date_dis = rs.getObject(12).toString();
					ipd_time_dis = rs.getObject(13).toString();
				}
				else {
					ipd_date_dis=DateFormatChange.StringToMysqlDate(new Date());
					ipd_time_dis="";
				}
				
				int[] bedHours = DateFormatChange
						.calulateHoursAndDays_BetweenDates(
								DateFormatChange.javaDate(ipd_date + " "
										+ ipd_time),	
								DateFormatChange.javaDate(ipd_date_dis + " "
										+ ipd_time_dis));
				
				ipd_days=bedHours[0]+"";
				ipd_hours=bedHours[1]+"";
				ipd_minutes=bedHours[2]+"";
			}
		} catch (SQLException ex) {
			Logger.getLogger(OPDBrowser.class.getName()).log(Level.SEVERE,
					null, ex);
		}
		System.out.println(patient_id);
	}

	public void getPatientDetail(String indexId) {

		PatientDBConnection patientDBConnection = null;
		try {
			patientDBConnection = new PatientDBConnection();
			ResultSet resultSet = patientDBConnection
					.retrieveDataWithIndex(indexId);

			while (resultSet.next()) {

				patient_age = resultSet.getObject(2).toString();
				patient_address= resultSet.getObject(4).toString();
				patient_insurancetype=resultSet.getObject(7).toString();
				patient_insurance_no=resultSet.getObject(8).toString();
//				if(patient_insurancetype.equals("ECHS") || patient_insurancetype.equals("Railway")){
                // patient_claim_id=resultSet.getObject(10).toString();
				
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		IPDDBConnection ipddbConnection = new IPDDBConnection();
		ResultSet rs=ipddbConnection.retrieveClaim(ipd_no_a);
		try {
			if(rs.next()){

			patient_claim_id=rs.getObject(1).toString();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.print(patient_age+"  "+indexId);
		patientDBConnection.closeConnection();
		String data[] = new String[22];
		int i = 0;
		for (String retval : patient_age.split("-")) {
			data[i] = retval;
			i++;
		}
		if (!data[0].equals("0")) {
			patient_age = data[0] + " years";
		} else if (!data[1].equals("0")) {
			patient_age = data[1] + " months";
		} else if (!data[2].equals("0")) {
			patient_age = data[2] + " days";
		}
	}

	public void makeDirectory(String opd_no) {

	
		new File("IPDSlip").mkdir();
		RESULT = "IPDSlip/" + opd_no + ".pdf";
	}

	private void copyFileUsingJava7Files(String source, String dest)
			throws IOException {
		SmbFile remoteFile = new SmbFile(dest);
		OutputStream os = remoteFile.getOutputStream();
		InputStream is = new FileInputStream(new File(source));

		int bufferSize = 5096;

		byte[] b = new byte[bufferSize];
		int noOfBytes = 0;
		while ((noOfBytes = is.read(b)) != -1) {
			os.write(b, 0, noOfBytes);
		}
		os.close();
		is.close();

	}

	public void readFile() {
		// The name of the file to open.
		String fileName = "data.mdi";

		// This will reference one line at a time
		String line = null;

		try {
			// FileReader reads text files in the default encoding.
			FileReader fileReader = new FileReader(fileName);

			// Always wrap FileReader in BufferedReader.
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String str = null;
			boolean fetch=true;
			while ((line = bufferedReader.readLine()) != null&&fetch) {
				// System.out.println(line);
				str = line;
				fetch=false;
			}
			String data[] = new String[22];
			int i = 0;
			for (String retval : str.split("@")) {
				data[i] = retval;
				i++;
			}
			mainDir = data[1];
			open[0] = data[2];
			open[1] = data[3];
			open[2] = data[4];
			// Always close files.
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileName + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + fileName + "'");
		}
	}

	
	
	public void onEndPage(PdfWriter writer, Document document) {
		Rectangle rect = writer.getBoxSize("art");
		switch (writer.getPageNumber() % 2) {
		case 0:
			ColumnText
					.showTextAligned(
							writer.getDirectContent(),
							
							Element.ALIGN_RIGHT,
							new Phrase(
									"Saturday : General OPD Closed, Sunday : Working, EMERGENCY : 24x7",
									font4), rect.getRight(), rect.getTop(), 0);
			break;
		case 1:
			ColumnText
					.showTextAligned(
							writer.getDirectContent(),
							Element.ALIGN_LEFT,
							new Phrase(
									"Saturday : General OPD Closed, Sunday : Working, EMERGENCY : 24x7",
									font4), rect.getLeft(), rect.getTop(), 0);
			break;
		}
		ColumnText.showTextAligned(writer.getDirectContent(),
				Element.ALIGN_CENTER, new Phrase(String.format("page %d", 1)),
				(rect.getLeft() + rect.getRight()) / 2, rect.getBottom() - 18,
				0);
	}
}