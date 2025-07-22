package hms.patient.slippdf;
import hms.doctor.database.DoctorDBConnection;
import hms.opd.database.OPDDBConnection;
import hms.opd.gui.OPDBrowser;
import hms.patient.database.PatientDBConnection;
import hms.test.free_test.FreeTestDBConnection;

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
import com.itextpdf.text.pdf.Barcode128;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;


public class OPDSlippdf_new {
    
	private static Font smallBold = new Font(Font.FontFamily.HELVETICA, 8);
	private static Font smallBold3 =  new Font(Font.FontFamily.HELVETICA, 10,Font.BOLD,BaseColor.BLACK);
	private static Font smallBold2 = new Font(Font.FontFamily.HELVETICA,8.5f);
	private static Font spaceFont = new Font(Font.FontFamily.HELVETICA, 2);
	private static Font font1 = new Font(Font.FontFamily.HELVETICA, 15,Font.BOLD,BaseColor.BLACK);
	private static Font font5 = new Font(Font.FontFamily.HELVETICA, 20,Font.BOLD,BaseColor.BLACK);
	private static Font font14 = new Font(Font.FontFamily.HELVETICA, 14,Font.BOLD,BaseColor.BLACK);
	private static Font font2 = new Font(Font.FontFamily.HELVETICA, 8,Font.BOLD);
	private static Font font3 = new Font(Font.FontFamily.HELVETICA, 8.5f,Font.BOLD);
	private static Font font4 = new Font(Font.FontFamily.HELVETICA, 9,Font.BOLD,BaseColor.BLACK);
	private static Font tokenfont4 = new Font(Font.FontFamily.HELVETICA, 11,Font.BOLD,BaseColor.WHITE);
    public static String RESULT = "opdslip1.pdf";
    
    Vector<String> doctorname = new Vector<String>();
    Vector<String> achievements = new Vector<String>();
    Vector<String> specialization = new Vector<String>();
    String opd_no,patient_id,patient_name,patient_age,doctor_name,amt_received,date,token_no,time;
	String mainDir = "";
	Font font;
	String[] open=new String[4];
	public static void main(String[] argh)
	{
		try {
			new OPDSlippdf_new("118010",true,"Counter_Operator");
		} catch (DocumentException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    public OPDSlippdf_new(String opd_no,boolean original,String online) throws DocumentException, IOException  {
		// TODO Auto-generated constructor stub
    	getAllDoctors();
    	getOPDData(opd_no);
    	getPatientDetail(patient_id);
    	readFile();
		makeDirectory(opd_no);
        Document document = new Document();
       
        PdfWriter wr = PdfWriter.getInstance(document, new FileOutputStream(RESULT));
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

		java.net.URL imgURL = OPDSlippdf_new.class.getResource("/icons/rotaryLogo.png");
		Image image = Image.getInstance(imgURL);

		image.scalePercent(50);
		image.setAbsolutePosition(100, 260);

		java.net.URL imgURLRotaryClub = OPDSlippdf_new.class
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
        PdfPCell addressCell = new PdfPCell(new Phrase("Opp. Dussehra Ground, Ram Bagh Road, Ambala Cantt (Haryana)", font2));
        addressCell.setPaddingBottom(2);
        addressCell.setBorder(Rectangle.NO_BORDER);
        addressCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        TitleTable.addCell(addressCell);
        
        PdfPCell addressCell2 = new PdfPCell(new Phrase("Telephone No. : 0171-2690009, Mobile No. : 09034056793", font2));
        addressCell2.setPaddingBottom(5);
        addressCell2.setBorder(Rectangle.NO_BORDER);
        addressCell2.setHorizontalAlignment(Element.ALIGN_CENTER);
        TitleTable.addCell(addressCell2);
        
        table.addCell(TitleTable);
      
        float[] doctorTableCellWidth = { 1f, 1f,0.1f };
        PdfPTable doctorTable = new PdfPTable(doctorTableCellWidth);
        doctorTable.getDefaultCell().setBorder(0);
        doctorTable.addCell(new Phrase("Regular Consultants", font2));
        doctorTable.addCell(new Phrase("Visiting Consultants", font2));
        doctorTable.addCell(new Phrase(" ", smallBold));
        int k=doctorname.size()/2;
        for (int i = 0; i <k ; i++) {
        	
        	 doctorTable.addCell(new Phrase(doctorname.get(i)+" "+specialization.get(i)+"", smallBold));
        	 doctorTable.addCell(new Phrase(doctorname.get(k+i)+" "+specialization.get(k+i)+"", smallBold));
        	 doctorTable.addCell(new Phrase(" ", smallBold));
		}
        PdfPCell doctorCell = new PdfPCell(doctorTable);
        doctorCell.setPaddingTop(2);
        doctorCell.setPaddingBottom(5);
        doctorCell.setPaddingLeft(5);
        doctorCell.setBorderWidth(0.8f);
        table.addCell(doctorCell);
        
        
        PdfPCell spaceCell = new PdfPCell(new Paragraph("    ", spaceFont));
        spaceCell.setBorder(Rectangle.NO_BORDER);
        spaceCell.setPaddingTop(1);
        table.addCell(spaceCell);
//        
        float[] opdTablCellWidth = {0.8f,1.5f,1.1f,1f,1f,1f,1f};
        PdfPTable opdTable = new PdfPTable(opdTablCellWidth);
        opdTable.getDefaultCell().setBorder(0);
        
        PdfPCell tokencell = new PdfPCell(new Phrase("Token No."+"\n",tokenfont4));
        tokencell.setBorder(Rectangle.NO_BORDER);
        tokencell.setPaddingBottom(4);
        tokencell.setBackgroundColor(BaseColor.GRAY);
        
        PdfPCell tokenNocell = new PdfPCell(new Phrase(token_no+"\n",tokenfont4));
        tokenNocell.setBorder(Rectangle.NO_BORDER);
        tokenNocell.setHorizontalAlignment(Element.ALIGN_CENTER);
        tokenNocell.setPaddingBottom(4);
        tokenNocell.setBackgroundColor(BaseColor.GRAY);
        //********************************
        
        
        String str="";
		
		
		
		
	        
        PdfContentByte cb1 = wr.getDirectContent();
        BaseFont bf = BaseFont.createFont();
        Phrase phrase = new Phrase(""+token_no, new Font(bf, 45));
        Phrase phrase2 = new Phrase("Token No.", new Font(bf, 15));
        
        Font f1 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 59);
        f1.setColor(BaseColor.GRAY);
        Phrase phrase3 = new Phrase("DUPLICATE", f1);
        
 	   
        ColumnText.showTextAligned(cb1, Element.ALIGN_CENTER, phrase,540,668,0);
        ColumnText.showTextAligned(cb1, Element.ALIGN_CENTER, phrase2,540,710,0);
        

        if(!original)
        	ColumnText.showTextAligned(cb1, Element.ALIGN_CENTER, phrase3,300,400,30);
        
      
        cb1.saveState();
        cb1.setColorStroke(BaseColor.BLACK);
        cb1.rectangle(501, 657,80, 70);
        cb1.stroke();
        cb1.restoreState();
        
        
        //****************************
        opdTable.addCell(tokencell);
        opdTable.addCell(tokenNocell);
        
        opdTable.addCell(new Phrase("OPD Receipt No. : ", font3));
        opdTable.addCell(new Phrase(""+opd_no, font3));
        
        PdfContentByte cb = wr.getDirectContent();
        Barcode128 codeEAN = new Barcode128();
        codeEAN.setCode(patient_id);
        codeEAN.setX(1.5f);
        codeEAN.setN(1.5f);
        codeEAN.setSize(12f);
        codeEAN.setBaseline(-1f);
        codeEAN.setGuardBars(true);
        codeEAN.setBarHeight(20f);
        PdfPCell barcodecell = new PdfPCell(codeEAN.createImageWithBarcode(cb, null, null));
        barcodecell.setHorizontalAlignment(Element.ALIGN_CENTER);
        barcodecell.setPaddingRight(5);
        barcodecell.setBorder(Rectangle.NO_BORDER);
        barcodecell.setColspan(2);
        barcodecell.setRowspan(2);
        opdTable.addCell(barcodecell);
        opdTable.addCell(new Phrase("", font3));
       
        
        opdTable.addCell(new Phrase("Patient : ", font3));
        opdTable.addCell(new Phrase(""+patient_name, font3));
        
        PdfPCell agecell = new PdfPCell(new Phrase(""+"\n",font3));
        agecell.setPaddingBottom(5);
        agecell.setBorder(Rectangle.NO_BORDER);
        agecell.setColspan(4);
        opdTable.addCell(new Phrase("Age : "+patient_age, font3));
        opdTable.addCell(agecell);
       
       
        opdTable.addCell(new Phrase("Consultant :", font3));
        PdfPCell consultantcell = new PdfPCell(new Phrase(""+doctor_name+"\n",font3));
        consultantcell.setPaddingBottom(5);
        consultantcell.setBorder(Rectangle.NO_BORDER);
        consultantcell.setColspan(1);
        opdTable.addCell(consultantcell);
        PdfPCell onlineCell = new PdfPCell(new Phrase(online,font3));
        onlineCell.setPaddingBottom(5);
        onlineCell.setBorder(Rectangle.NO_BORDER);
        onlineCell.setColspan(2);
        opdTable.addCell(onlineCell);
     

        opdTable.addCell(new Phrase("Amt Received : ", font3));
        opdTable.addCell(new Phrase("`"+amt_received+"", font));
        opdTable.addCell(new Phrase("Signature", font3));
       
        opdTable.addCell(new Phrase("", font3));
        opdTable.addCell(new Phrase("", font3));
        opdTable.addCell(new Phrase("", font3));
        opdTable.addCell(new Phrase("", font3));
        PdfPCell opdCell = new PdfPCell(opdTable);
        opdCell.setBorderWidth(0.8f);
        table.addCell(opdCell);
        
      
        

		float[] facilityTablCellWidth = { 0.5f, 0.5f,0.5f,0.1f };
		PdfPTable mainvitalsTable = new PdfPTable(facilityTablCellWidth);

		PdfPTable cardiacTable = new PdfPTable(1);
		cardiacTable.getDefaultCell().setBorder(0);

		
		cardiacTable.addCell(new Phrase(" ", smallBold));
		PdfPCell cardiacTitleCell1 = new PdfPCell(new Phrase("Cardiac Care Services 24x7",
				font14));
		cardiacTitleCell1.setBorder(Rectangle.NO_BORDER);
		cardiacTitleCell1.setHorizontalAlignment(Element.ALIGN_CENTER);

		cardiacTable.addCell(cardiacTitleCell1);
		

		PdfPCell cardiacTitleCell2 = new PdfPCell(new Phrase("24x7",
				font14));
		cardiacTitleCell2.setBorder(Rectangle.NO_BORDER);
		cardiacTitleCell2.setHorizontalAlignment(Element.ALIGN_CENTER);

		cardiacTable.addCell(cardiacTitleCell2);
		
		
		
		PdfPTable vitalsTable = new PdfPTable(1);
		vitalsTable.getDefaultCell().setBorder(0);

	
		PdfPCell facilitiesTitleCell = new PdfPCell(new Phrase("Vital Physical Examination",
				font2));
		facilitiesTitleCell.setBorder(Rectangle.NO_BORDER);
		facilitiesTitleCell.setHorizontalAlignment(Element.ALIGN_CENTER);

		vitalsTable.addCell(facilitiesTitleCell);
		vitalsTable.addCell(new Phrase(12, "BP..............................Pulse..........................",
				smallBold2));
		vitalsTable.addCell(new Phrase("Wt. .............................Ht. .............................", smallBold2));
		vitalsTable.addCell(new Phrase("Temp. ........................Others........................", smallBold2));
		vitalsTable.addCell(new Phrase("Allergy(if any)", smallBold2));
		vitalsTable.addCell(new Phrase(" ", smallBold2));
		vitalsTable.addCell(new Phrase(" ", smallBold2));
		vitalsTable.addCell(new Phrase("Chief Complaints", smallBold2));
		vitalsTable.addCell(new Phrase(" ", smallBold2));
		vitalsTable.addCell(new Phrase(" ", smallBold2));
		vitalsTable.addCell(new Phrase(" ", smallBold2));
		vitalsTable.addCell(new Phrase(" ", smallBold2));
		vitalsTable.addCell(new Phrase("Past History", smallBold2));
		vitalsTable.addCell(new Phrase(" ", smallBold2));
		vitalsTable.addCell(new Phrase(" ", smallBold2));
		vitalsTable.addCell(new Phrase(" ", smallBold2));
		vitalsTable.addCell(new Phrase("Investigation",
				smallBold2));
		vitalsTable.addCell(new Phrase(" ", smallBold2));
		vitalsTable.addCell(new Phrase(" ", smallBold2));
		vitalsTable.addCell(new Phrase(" ", smallBold2));
		vitalsTable.addCell(new Phrase(" ", smallBold2));
		vitalsTable.addCell(new Phrase(" ", smallBold2));
		vitalsTable.addCell(new Phrase(" ", smallBold2));
		vitalsTable.addCell(new Phrase(" ", smallBold2));
		vitalsTable.addCell(new Phrase(" ", smallBold2));
		vitalsTable.addCell(new Phrase(" ", smallBold2));
		vitalsTable.addCell(new Phrase(" ", smallBold2));
		vitalsTable
				.addCell(new Phrase("Nutritional assessment Required: Yes/ No", smallBold2));
		vitalsTable.addCell(new Phrase(" ", smallBold2));
		vitalsTable.addCell(new Phrase("Next followup Date...................................", smallBold2));
		vitalsTable.addCell(new Phrase("",
				smallBold2));

		
		
		/////
		PdfPCell cardiacCell = new PdfPCell(new Phrase("", smallBold));


		cardiacCell.setBorderWidth(0);
		//mainvitalsTable.addCell(" ");
	//////
			PdfPCell facilityCell = new PdfPCell(vitalsTable);
			facilityCell.setPaddingBottom(2);
			facilityCell.setPaddingLeft(2);
			facilityCell.setBorderWidth(0.8f);
			mainvitalsTable.addCell(facilityCell);
		
		PdfPCell facilitiesCell2 = new PdfPCell(new Phrase("", font2));
		facilitiesCell2.setBorder(Rectangle.NO_BORDER);
		facilitiesCell2.setPaddingBottom(2);
		facilitiesCell2.setHorizontalAlignment(Element.ALIGN_CENTER);
		
		
		PdfPCell rxCell = new PdfPCell(new Phrase("Diagnosis\nRx", font1));
		rxCell.setBorder(Rectangle.NO_BORDER);
		rxCell.setHorizontalAlignment(Element.ALIGN_LEFT);
		
		mainvitalsTable.addCell(rxCell);
		
		rxCell = new PdfPCell(new Phrase("Time : "+time, smallBold3));
		rxCell.setBorder(Rectangle.NO_BORDER);
		rxCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		
		mainvitalsTable.addCell(rxCell);
		
		
		PdfPCell call3 = new PdfPCell(
				new Paragraph(
						" Cardiac Care Services 24x7 ",
						font5));
		//call3.setBorder(Rectangle.NO_BORDER);
		call3.setRowspan(5);
		call3.setRotation(90);
		call3.setPaddingRight(7);
		call3.setPaddingLeft(5);
		call3.setHorizontalAlignment(Element.ALIGN_CENTER);
		call3.setVerticalAlignment(Element.ALIGN_CENTER);

		mainvitalsTable.addCell(call3);

		mainvitalsTable.addCell(facilitiesCell2);
		
		//////
		PdfPCell emptyCell = new PdfPCell(new Phrase(" ", font2));
		emptyCell.setBorder(Rectangle.NO_BORDER);
		emptyCell.setPaddingBottom(2);
		emptyCell.setHorizontalAlignment(Element.ALIGN_CENTER);
	
		/////
		mainvitalsTable.addCell(emptyCell);
		mainvitalsTable.addCell(facilitiesCell2);
		/////
		
		mainvitalsTable.addCell(facilitiesCell2);
		
		
		/////////
		mainvitalsTable.addCell(emptyCell);


		PdfPCell emptyCell2 = new PdfPCell(new Phrase("", font2));
		emptyCell2.setBorder(Rectangle.NO_BORDER);
		emptyCell2.setPaddingBottom(5);
		emptyCell2.setHorizontalAlignment(Element.ALIGN_CENTER);
		mainvitalsTable.addCell(emptyCell2);

		//////////
		
		
		

		PdfPCell empanelledCell2 = new PdfPCell(new Phrase("", font2));
		empanelledCell2.setBorder(Rectangle.NO_BORDER);
		empanelledCell2.setPaddingBottom(5);
		empanelledCell2.setHorizontalAlignment(Element.ALIGN_CENTER);
		mainvitalsTable.addCell(empanelledCell2);
		
		PdfPCell addvitalsTable = new PdfPCell(mainvitalsTable);
		addvitalsTable.setBorder(Rectangle.NO_BORDER);
		addvitalsTable.setPaddingTop(10);
		int spaceINT=120-(5*doctorname.size());
		addvitalsTable.setPaddingBottom(spaceINT);
		table.addCell(addvitalsTable);

		PdfPCell space = new PdfPCell(new Paragraph("Date : "+date+"  Time : ...................  Signature: ..................", smallBold3));
        space.setBorder(Rectangle.NO_BORDER);
        space.setHorizontalAlignment(Element.ALIGN_RIGHT);
        space.setPaddingRight(50);
        space.setPaddingBottom(5);
        table.addCell(space);
		
        PdfPCell blankCell = new PdfPCell(new Phrase(" ", smallBold));
        blankCell.setRowspan(5);

		PdfPTable packagesTable = new PdfPTable(1);
		packagesTable.setWidthPercentage(30);
		packagesTable.getDefaultCell().setBorder(5);

		PdfPCell specialPackagesTitleCell = new PdfPCell(new Phrase(
				"FACILITIES", font2));
		specialPackagesTitleCell.setBorder(Rectangle.NO_BORDER);
		specialPackagesTitleCell.setHorizontalAlignment(Element.ALIGN_CENTER);

		packagesTable.addCell(specialPackagesTitleCell);
		
		packagesTable
				.addCell(new Phrase("FULLY EQUIPPED CCU & ICU, ANGIOGRAPHY & ANGIOPLASTY, ECHO, TMT, HEMODIALYSIS, MAMMOGRAPHY, COLOR DOPPLER ULTRASOUNDS, ENDOSCOPY, COMPUTERIZED LAB, ORTHO SURGERY (TRAUMA SPINE), ARTHROSCOPY, KNEE, HIP & JOINTS REPLACEMENT, AUDIOMETERY & SPEECH THERAPY, DIGITAL X-RAY, MRI, CT, Interventional Radiology", smallBold));


		PdfPTable empanelledTable = new PdfPTable(1);
		empanelledTable.setWidthPercentage(30);
		empanelledTable.getDefaultCell().setBorder(5);
		
		specialPackagesTitleCell = new PdfPCell(new Phrase(
				"EMPANELLED WITH", font2));
		specialPackagesTitleCell.setHorizontalAlignment(Element.ALIGN_CENTER);

		empanelledTable.addCell(specialPackagesTitleCell);
		
		empanelledTable
				.addCell(new Phrase("ECHS, ESIC, BSNL Employees, Railways, Major TPA", smallBold));



		PdfPCell packagesCell = new PdfPCell(packagesTable);
		packagesCell.setPaddingBottom(0);
		packagesCell.setPaddingLeft(0);
		packagesCell.setBorderWidth(0.8f);
		table.addCell(packagesCell);

		PdfPCell footer = new PdfPCell(
				new Phrase(
						"",
						font3));
		footer.setBorder(Rectangle.NO_BORDER);
		footer.setHorizontalAlignment(Element.ALIGN_CENTER);
	//	table.addCell(footer);

		PdfPCell footer2 = new PdfPCell(new Phrase(
				"EMERGENCY : 24x7 (Open all days) | General OPD : Sunday-Open (Saturday-Closed) ", font4));
		footer2.setBorder(Rectangle.NO_BORDER);
		footer2.setPaddingBottom(5);
		footer2.setHorizontalAlignment(Element.ALIGN_CENTER);
		footer2.setBackgroundColor(BaseColor.LIGHT_GRAY);
		
		table.addCell(footer2);
		
	packagesCell = new PdfPCell(empanelledTable);
		packagesCell.setPaddingBottom(0);
		packagesCell.setPaddingLeft(0);
		packagesCell.setBorderWidth(0.8f);
		table.addCell(packagesCell);
		
		// PdfPCell footerWrap = new PdfPCell(footer2);
		// footerWrap.setPaddingLeft(50);
		// footerWrap.setPaddingRight(50);
		// footerWrap.setHorizontalAlignment(Element.ALIGN_CENTER);
		
		

        FreeTestDBConnection connection = new FreeTestDBConnection();
		ResultSet resultSet = connection.retrieveDataWithOPD(opd_no);
		int i=0;
		try {
			while (resultSet.next()) {
				footer2=new PdfPCell(new Phrase(
						  resultSet.getObject(4).toString(), font4));
					footer2.setBorder(Rectangle.NO_BORDER);
					footer2.setPaddingBottom(2);
					footer2.setHorizontalAlignment(Element.ALIGN_CENTER);
					table.addCell(footer2);
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		connection.closeConnection();
		


		document.add(table);

		document.close();
        
		new OpenFile(RESULT);
		
  }

    
    public void getAllDoctors() {
		DoctorDBConnection dbConnection = new DoctorDBConnection();
		ResultSet resultSet = dbConnection.retrieveAllDataASC();
		try {
			while (resultSet.next()) {
				doctorname.addElement(resultSet.getObject(2).toString());
				specialization.addElement(" ("+resultSet.getObject(3).toString()+")");
				achievements.addElement(", "+resultSet.getObject(7).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
		if(doctorname.size()%2!=0)
		{
			doctorname.addElement("");
			specialization.addElement("");
			achievements.addElement("");
		}
	}
    public void getOPDData(String opdID)
	{
		try {
            OPDDBConnection db=new OPDDBConnection();
            ResultSet rs = db.retrieveAllDataWithOpdId(opdID);
            while(rs.next()){
            	opd_no=rs.getObject(1).toString();
            	patient_id= rs.getObject(2).toString();
            	patient_name= rs.getObject(3).toString();
            	doctor_name= rs.getObject(4).toString();
            	date= rs.getObject(5).toString();
            	token_no=rs.getObject(8).toString();
            	amt_received= rs.getObject(10).toString();
            	time= rs.getObject(11).toString();
            	System.out.println("Amount Recieved :"+amt_received);
            }
		} catch (SQLException ex) {
         Logger.getLogger(OPDBrowser.class.getName()).log(Level.SEVERE, null, ex);
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
			
				patient_name=resultSet.getObject(1).toString();
				patient_age = resultSet.getObject(2).toString();
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
    public void makeDirectory(String opd_no) {

    	new File("OPDSlip").mkdir();
		RESULT="OPDSlip/"+ opd_no + ".pdf";
	}

	private void copyFileUsingJava7Files(String source, String dest)
			throws IOException {
		SmbFile remoteFile = new SmbFile(dest);
		OutputStream os = remoteFile.getOutputStream();
		InputStream is = new FileInputStream(new File(source)) ;

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


	public void onEndPage(PdfWriter writer, Document document) {
        Rectangle rect = writer.getBoxSize("art");
        switch(writer.getPageNumber() % 2) {
        case 0:
            ColumnText.showTextAligned(writer.getDirectContent(),
                    Element.ALIGN_RIGHT, new Phrase(
            				"Saturday : General OPD Closed, Sunday : Working, EMERGENCY : 24x7", font4),
                    rect.getRight(), rect.getTop(), 0);
            break;
        case 1:
            ColumnText.showTextAligned(writer.getDirectContent(),
                    Element.ALIGN_LEFT, new Phrase(
            				"Saturday : General OPD Closed, Sunday : Working, EMERGENCY : 24x7", font4),
                    rect.getLeft(), rect.getTop(), 0);
            break;
        }
        ColumnText.showTextAligned(writer.getDirectContent(),
                Element.ALIGN_CENTER, new Phrase(String.format("page %d", 1)),
                (rect.getLeft() + rect.getRight()) / 2, rect.getBottom() - 18, 0);
    }
}