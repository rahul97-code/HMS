package hms.patient.slippdf;

import hms.exam.database.ExamDBConnection;
import hms.main.MainLogin;
import hms.opd.gui.OPDBrowser;
import hms.patient.database.PatientDBConnection;

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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;

import com.itextpdf.text.BaseColor;
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

public class ExamSlippdfRegenerate {

	private static Font smallBold = new Font(Font.FontFamily.HELVETICA, 5);
	private static Font speratorLine = new Font(Font.FontFamily.HELVETICA, 8,
			Font.BOLD, BaseColor.RED);
	private static Font spaceFont = new Font(Font.FontFamily.HELVETICA, 9,
			Font.BOLD, BaseColor.DARK_GRAY);
	private static Font font1 = new Font(Font.FontFamily.HELVETICA, 9,
			Font.BOLD, BaseColor.BLACK);
	private static Font font2 = new Font(Font.FontFamily.HELVETICA, 7,
			Font.BOLD);
	private static Font font3 = new Font(Font.FontFamily.HELVETICA, 7f,
			Font.BOLD);
	private static Font font4 = new Font(Font.FontFamily.HELVETICA, 9,
			Font.BOLD, BaseColor.WHITE);
	private static Font karunSprashFont = new Font(Font.FontFamily.TIMES_ROMAN, 13,
			Font.BOLDITALIC, BaseColor.WHITE);
	private static Font tokenfont4 = new Font(Font.FontFamily.HELVETICA, 7f,
			Font.BOLD, BaseColor.WHITE);
	public static String RESULT = "opdslip1.pdf";
	Vector<String> examname = new Vector<String>();
	Vector<Double> examCharges = new Vector<Double>();
	Vector<Double> RateCharges = new Vector<Double>();
	public static void main(String r[]) {
		Object[] o= {1,2,3};
		try {
			new ExamSlippdfRegenerate("882866","882868","882868","491291882866","");
		} catch (DocumentException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	Vector<String> doctorname = new Vector<String>();
	Vector<String> achievements = new Vector<String>();
	Vector<String> specialization = new Vector<String>();
	String exam_id, patient_id, patient_name, patient_age,patient_sex, doctor_name,second_doctor,
			 date,amount_receipt_id,generatedBy="";

	String mainDir = "",insuranceType ="",examdate="";
	boolean karuna=false;
	Font font;
	String[] open=new String[4];
	Object[] examnames;Object[] examRates;
	String desc2 ="";
	/** 
	 * @param exam_id
	 * @param examnames
	 * @param examRates
	 * @param totalAmountSTR
	 * @throws DocumentException
	 * @throws IOException
	 */
	public ExamSlippdfRegenerate(String exam_id1,String exam_id2,String amountReciptID,String totalAmountSTR,String time) throws DocumentException, IOException {
		// TODO Auto-generated constructor stub
		System.out.println(exam_id1+"   "+exam_id2+"   "+amountReciptID+"    "+totalAmountSTR);
		exam_id=exam_id1;
		amount_receipt_id=amountReciptID;
		getOPDData(exam_id);
		getPatientDetail(patient_id);
		readFile();
		makeDirectory(exam_id);
		Document document = new Document();
		
		PdfWriter wr = PdfWriter.getInstance(document, new FileOutputStream(
				RESULT));
		document.setPageSize(PageSize.LETTER);
		document.setMargins(0, 0, 50, 0);
		document.open();
		
	     BaseFont base = BaseFont.createFont("indian.ttf", BaseFont.WINANSI,
					BaseFont.EMBEDDED);
			font = new Font(base, 9f);
		PdfPTable table = new PdfPTable(1);
		table.getDefaultCell().setBorder(0);
		table.setWidthPercentage(40);
		table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

		float[] tiltelTablCellWidth = { 0.1f, 1f };
		PdfPTable TitleTable = new PdfPTable(tiltelTablCellWidth);
		TitleTable.getDefaultCell().setBorder(0);

		java.net.URL imgURL = ExamSlippdfRegenerate.class
				.getResource("/icons/rotaryLogo.png");
		Image image = Image.getInstance(imgURL);

		java.net.URL imgURL2 = ExamSlippdfRegenerate.class
				.getResource("/icons/footer.PNG");
		Image image2 = Image.getInstance(imgURL2);
	
		PdfContentByte cb1 = wr.getDirectContent();
		Water_Mark(cb1);
		cb1.saveState();
		cb1.stroke();
		cb1.restoreState();
		
		image.scalePercent(30);
		image.setAbsolutePosition(40, 750);

		PdfPCell logocell = new PdfPCell(image);
		logocell.setRowspan(3);
		logocell.setBorder(Rectangle.NO_BORDER);
		logocell.setHorizontalAlignment(Element.ALIGN_CENTER);
		logocell.setPaddingRight(5);
		TitleTable.addCell(logocell);

		PdfPCell namecell = new PdfPCell(new Phrase(
				"ROTARY AMBALA CANCER AND GENERAL HOSPITAL" + "\n", font1));
		namecell.setHorizontalAlignment(Element.ALIGN_CENTER);
		namecell.setPaddingBottom(3);
	//	namecell.setBackgroundColor(BaseColor.LIGHT_GRAY);
		TitleTable.addCell(namecell);

		PdfPCell addressCell = new PdfPCell(new Phrase(
				"Opp. Dussehra Ground, Ram Bagh Road, Ambala Cantt (Haryana)",
				font2));
		addressCell.setPaddingBottom(1);
		addressCell.setBorder(Rectangle.NO_BORDER);
		addressCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		TitleTable.addCell(addressCell);

		PdfPCell addressCell2 = new PdfPCell(
				new Phrase(
						"Telephone No. : 0171-2690009, Mobile No. : 09034056793",
						font2));
		addressCell2.setPaddingBottom(2);
		addressCell2.setBorder(Rectangle.NO_BORDER);
		addressCell2.setHorizontalAlignment(Element.ALIGN_CENTER);
		TitleTable.addCell(addressCell2);

		table.addCell(TitleTable);

		PdfPCell cashReceipt = new PdfPCell(new Paragraph("RECEIPT ",
				spaceFont));
		cashReceipt.setBorder(Rectangle.NO_BORDER);
		cashReceipt.setHorizontalAlignment(Element.ALIGN_CENTER);
		cashReceipt.setPaddingBottom(3);
		table.addCell(cashReceipt);
		
		float[] opdTablCellWidth = { 1f, 2f, 0.8f, 1.2f };
		PdfPTable opdTable = new PdfPTable(opdTablCellWidth);
		opdTable.getDefaultCell().setBorder(0);

		PdfPCell tokencell = new PdfPCell(new Phrase("Receipt No. :" + "\n",
				font3));
		tokencell.setBorder(Rectangle.NO_BORDER);
		tokencell.setPaddingBottom(4);

		PdfPCell tokenNocell = new PdfPCell(new Phrase( ""+amountReciptID, font3));
		tokenNocell.setBorder(Rectangle.NO_BORDER);
		tokenNocell.setPaddingBottom(4);

		opdTable.addCell(tokencell);
		opdTable.addCell(tokenNocell);

		opdTable.addCell(new Phrase("Patient ID : ", font3));
		opdTable.addCell(new Phrase("" + patient_id, font3));

		opdTable.addCell(new Phrase("Patient Name : ", font3));
		opdTable.addCell(new Phrase("" + patient_name, font3));

		opdTable.addCell(new Phrase("Sex : ", font3));
		opdTable.addCell(new Phrase(""+patient_sex, font3));

		opdTable.addCell(new Phrase("Ref By :", font3));
		PdfPCell consultantcell = new PdfPCell(new Phrase("" + doctor_name
				, font3));
		//consultantcell.setPaddingBottom(5);
		
		consultantcell.setBorder(Rectangle.NO_BORDER);
		opdTable.addCell(consultantcell);
//		opdTable.addCell(new Phrase("Ref By :", font3));
//		PdfPCell consultantcell1 = new PdfPCell(new Phrase("" + doctor_name
//				, font3));
//		consultantcell1.setPaddingBottom(5);
//		
//		consultantcell1.setBorder(Rectangle.NO_BORDER);
		opdTable.addCell(new Phrase("Age : ", font3));
		opdTable.addCell(new Phrase(""+patient_age, font3));

		long timeInMillis = System.currentTimeMillis();
		Calendar cal1 = Calendar.getInstance();
		cal1.setTimeInMillis(timeInMillis);
		SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss a");
		

		
//		PdfPCell space = new PdfPCell(new Paragraph("Date : " + date + " / "
//				+ time, font3));
//		space.setBorder(Rectangle.NO_BORDER);
//		space.setHorizontalAlignment(Element.ALIGN_RIGHT);
//		space.setPaddingBottom(6);
//		space.setPaddingTop(4);
//		table.addCell(space);

		float[] examTableWidth = { 0.3f, 2.5f, 0.7f };
		PdfPTable examtable = new PdfPTable(examTableWidth);

		examtable.addCell(new Phrase("S. No", font3));
		examtable.addCell(new Phrase("Exam Name", font3));
		examtable.addCell(new Phrase("Amount", font3));
		//int i=1;
		ResultSet rs;
		try {
			ExamDBConnection db = new ExamDBConnection();
			if(Integer.parseInt(amountReciptID)<263162){
				System.out.println("ifpqrt");
				 rs = db.retrieveAllDataIDBetween1(exam_id1,exam_id2);
			}else{
				System.out.println("elsepart");
				 rs = db.retrieveAllDataIDBetween(amountReciptID);
			}
		
			while (rs.next()) {
				//examtable.addCell(new Phrase(""+i++, font3));
				//examtable.addCell(new Phrase(""+rs.getObject(1).toString(), font3));
				examname.add(rs.getObject(1).toString());
				examCharges.add(rs.getDouble(2));
				//examtable.addCell(new Phrase(""+rs.getObject(2).toString(), font3));
				examdate=rs.getObject(3).toString()+"";
				karuna=rs.getBoolean(4);
				RateCharges.add(rs.getDouble(5));
				System.out.println(rs.getObject(3).toString()+"");
			}
			
			db.closeConnection();
		} catch (SQLException ex) {
			Logger.getLogger(OPDBrowser.class.getName()).log(Level.SEVERE,
					null, ex);
		}
		double total=0;
		for(int i=0;i<examname.size();i++) {
			examtable.addCell(new Phrase(""+(i+1), font3));
			examtable.addCell(new Phrase(""+examname.get(i), font3));
			examtable.addCell(new Phrase(examCharges.get(i)/RateCharges.get(i)+"", font3));
			total+=(examCharges.get(i)/RateCharges.get(i));
		}
		
	//	desc2=examdesc(examname);
		System.out.println(total+"hhhhh");
	opdTable.addCell(new Phrase("Performed By :", font3));
	opdTable.addCell(new Phrase(""+second_doctor, font3));
		
		opdTable.addCell(new Phrase("Insurance:", font3));
		opdTable.addCell(new Phrase(""+insuranceType, font3));
		opdTable.addCell(new Phrase("Date: ", font3));
		opdTable.addCell(new Phrase(""+examdate, font3));
		opdTable.addCell(new Phrase("", font3));
		opdTable.addCell(new Phrase("", font3));
		PdfPCell opdCell = new PdfPCell(opdTable);
		opdCell.setBorderWidth(0.8f);
		table.addCell(opdCell);
		float[] generated = { 0.3f,  0.7f };
		PdfPTable generatedtable = new PdfPTable(generated);

		PdfPCell generatedby = new PdfPCell(new Paragraph("Generated By : "+generatedBy, font3));
		generatedby.setBorder(Rectangle.NO_BORDER); 
		generatedby.setHorizontalAlignment(Element.ALIGN_LEFT);
		generatedby.setPaddingTop(15);
//		table.addCell(generatedby);
	
		PdfPCell space = new PdfPCell(new Paragraph("Date / Time : "+date+" / "+time, font3));
		space.setBorder(Rectangle.NO_BORDER);
		space.setHorizontalAlignment(Element.ALIGN_RIGHT);
		space.setPaddingTop(15);

           generatedtable.addCell(generatedby);
           generatedtable.addCell(space);

	
		table.addCell(generatedtable);
		table.addCell(examtable);

		PdfPCell totalAmount = new PdfPCell(new Paragraph("Total Amt : "	+total+"", font));
		totalAmount.setBorder(Rectangle.NO_BORDER);
		totalAmount.setHorizontalAlignment(Element.ALIGN_RIGHT);
		totalAmount.setPaddingTop(5);
		table.addCell(totalAmount);
		
		PdfPCell spaceamnt = new PdfPCell(new Paragraph("", font));
		spaceamnt.setBorder(Rectangle.NO_BORDER);
		//spaceamnt.setPaddingBottom(6);
		spaceamnt.setHorizontalAlignment(Element.ALIGN_LEFT);
		if (insuranceType.equals("Unknown")) {
			table.addCell(spaceamnt);
		}
		PdfPCell disc_Amount = new PdfPCell(new Paragraph(karuna?"Payable Discount Amt : "	+totalAmountSTR + "":"", font));
		disc_Amount.setBorder(Rectangle.NO_BORDER);
		//disc_Amount.setPaddingTop(-15);
		disc_Amount.setPaddingBottom(8);
		disc_Amount.setHorizontalAlignment(Element.ALIGN_RIGHT);
		if (insuranceType.equals("Unknown")) {
			table.addCell(disc_Amount);
		}
		
		PdfPCell KArusparsh = new PdfPCell(new Paragraph(karuna?"Karuna ":"", karunSprashFont));
		KArusparsh.setHorizontalAlignment(Element.ALIGN_CENTER);
		KArusparsh.setBorder(Rectangle.NO_BORDER);
		KArusparsh.setColspan(2);
		//KArusparsh.setPaddingTop(-2);
		KArusparsh.setBackgroundColor(karuna?BaseColor.GRAY:BaseColor.WHITE);
		//KArusparsh.setPaddingBottom(4);
		
//		PdfPCell desc = new PdfPCell(new Paragraph(karuna?"Desc :- "+desc2:"", font1));
//		desc.setHorizontalAlignment(Element.ALIGN_LEFT);
//		desc.setPaddingTop(8);
//		
//		desc.setBorder(Rectangle.NO_BORDER);
		PdfPCell signature = new PdfPCell(new Paragraph("Signature", font3));
		signature.setBorder(Rectangle.NO_BORDER);
		signature.setHorizontalAlignment(Element.ALIGN_RIGHT);
		signature.setPaddingTop(15);
		table.addCell(KArusparsh);
		//table.addCell(desc);
		table.addCell(signature);
		table.addCell(image2);
		

		float[] table2w = { 1f, 0.1f, 1f };
		PdfPTable table2 = new PdfPTable(table2w);
		table2.getDefaultCell().setBorder(0);
		table2.setWidthPercentage(94);

		PdfPCell call1 = new PdfPCell(table);
		call1.setBorder(Rectangle.NO_BORDER);
		call1.setHorizontalAlignment(Element.ALIGN_CENTER);

		table2.addCell(call1);

		PdfPCell call3 = new PdfPCell(
				new Paragraph(
						"   - - - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - - - - - - - - - -   ",
						speratorLine));
		call3.setBorder(Rectangle.NO_BORDER);
		call3.setRotation(270);
		call3.setPaddingRight(7);
		call3.setPaddingLeft(5);
		call3.setHorizontalAlignment(Element.ALIGN_CENTER);

		table2.addCell(call3);

		PdfPCell cell2 = new PdfPCell(table);
		cell2.setBorder(Rectangle.NO_BORDER);
		cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
		table2.addCell(cell2);

		document.add(table2);
		document.close();

		new OpenFile(RESULT);
		
	}

	public void getOPDData(String opdID) {
		String receiptID="";
		try {
			ExamDBConnection db = new ExamDBConnection();
			ResultSet rs = db.retrieveAllDataExamID(opdID);
			while (rs.next()) {
				patient_id = rs.getObject(3).toString();
				patient_name = rs.getObject(4).toString();
				receiptID = rs.getObject(12).toString();
				date = rs.getObject(6).toString();
				generatedBy=rs.getObject(13).toString();
			}

			db.closeConnection();
		} catch (SQLException ex) {
			Logger.getLogger(OPDBrowser.class.getName()).log(Level.SEVERE,
					null, ex);
		}
		getDoctor(receiptID);
	}
//	private String examdesc(Vector<String> examname2) {
//		// TODO Auto-generated method stub
//		String exam=examname2.get(0).substring(0,3);
//		String desc=null;
//		ExamDBConnection db=new ExamDBConnection();
//		ResultSet rs=db.retrieveExamkarunadesc(exam);
//		try {
//			while(rs.next()) {
//				 desc = rs.getObject(1).toString();
//			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		db.closeConnection();
//		return desc;
//	}
	public void getDoctor(String receiptID) {
		try {
			ExamDBConnection db = new ExamDBConnection();
			ResultSet rs = db.retrieveDoctor(receiptID);
			while (rs.next()) {
				doctor_name = rs.getObject(1).toString();
				second_doctor=rs.getObject(2).toString();
			}

			db.closeConnection();
		} catch (SQLException ex) {
			Logger.getLogger(OPDBrowser.class.getName()).log(Level.SEVERE,
					null, ex);
		}
	
	}

	public void getPatientDetail(String indexId) {

		PatientDBConnection patientDBConnection = null;
		try {
			patientDBConnection = new PatientDBConnection();
			ResultSet resultSet = patientDBConnection
					.retrieveDataWithIndex(indexId);

			while (resultSet.next()) {

				patient_name = resultSet.getObject(1).toString();
				patient_age = resultSet.getObject(2).toString();
				patient_sex= resultSet.getObject(3).toString();
				insuranceType = resultSet.getObject(7).toString();

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.print(patient_age);
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
	public void Water_Mark(PdfContentByte cb1) {
		Font f1 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 40);
		f1.setColor(new BaseColor(204,204,204));
		String str="DUPLICATE";
		Phrase phrase = new Phrase(str, f1);
		ColumnText.showTextAligned(cb1, Element.ALIGN_CENTER, phrase, 160,
				610, 30);
		ColumnText.showTextAligned(cb1, Element.ALIGN_CENTER, phrase, 460,
				610, 30);
		
		String randm_no=Random_String();
	        Font f3 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 11.5f);
	        f3.setColor(new BaseColor(204,204,204));
	       
	        Date date = new Date();  
	        SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");  
	        String strDate = formatter.format(date); 
	        Phrase phrase4 = new Phrase(strDate+" "+randm_no, f3);
	        ColumnText.showTextAligned(cb1, Element.ALIGN_CENTER, phrase4,170,600,30);
	        ColumnText.showTextAligned(cb1, Element.ALIGN_CENTER, phrase4,470,600,30);
	    
	        
	
		
	}
	public String Random_String() {
		String str="";
		StringBuilder sb = new StringBuilder();
		// TODO Auto-generated constructor stub
		sb.append(Random_char_3());
		long millis = System.currentTimeMillis();
		sb.append(millis+"");
		sb.append(Random_char_3());
		
		return sb.toString();
	}
	public String Random_char_3() {
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<3;i++) {
			Random r = new Random();
			char c = (char)(r.nextInt(26) + 'A');
			sb.append(c);
		}
		return sb.toString();
	}

	public void makeDirectory(String exam_id) {

	
		new File("ExamSlip").mkdir();
	
		RESULT = "ExamSlip/" + exam_id + ".pdf";
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
			open[0]=data[2];
			open[1]=data[3];
			open[2]=data[4];
			
			// Always close files.
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileName + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + fileName + "'");
			// Or we could just do this:
			// ex.printStackTrace();
		}
	}
	

}