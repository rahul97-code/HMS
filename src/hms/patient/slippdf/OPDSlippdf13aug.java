package hms.patient.slippdf;
import hms.doctor.database.DoctorDBConnection;
import hms.opd.database.OPDDBConnection;
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


public class OPDSlippdf13aug {
    
	private static Font smallBold = new Font(Font.FontFamily.HELVETICA, 8);
	private static Font spaceFont = new Font(Font.FontFamily.HELVETICA, 2);
	private static Font font1 = new Font(Font.FontFamily.HELVETICA, 17,Font.BOLD,BaseColor.BLACK);
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
			new OPDSlippdf13aug("39");
		} catch (DocumentException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    public OPDSlippdf13aug(String opd_no) throws DocumentException, IOException  {
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

		java.net.URL imgURL = OPDSlippdf13aug.class.getResource("/icons/rotaryLogo.png");
		Image image = Image.getInstance(imgURL);

		image.scalePercent(50);
		image.setAbsolutePosition(100, 260);

		java.net.URL imgURLRotaryClub = OPDSlippdf13aug.class
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
      
        PdfPTable doctorTable = new PdfPTable(2);
        doctorTable.getDefaultCell().setBorder(0);
       
        for (int i = 0; i <doctorname.size() ; i++) {
        	
        	 doctorTable.addCell(new Phrase(doctorname.get(i)+""+achievements.get(i)+" "+specialization.get(i)+"", smallBold));
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
        
        PdfContentByte cb1 = wr.getDirectContent();
        BaseFont bf = BaseFont.createFont();
        Phrase phrase = new Phrase(""+token_no, new Font(bf, 45));
        Phrase phrase2 = new Phrase("Token No.", new Font(bf, 15));
        ColumnText.showTextAligned(cb1, Element.ALIGN_CENTER, phrase,540,668,0);
        ColumnText.showTextAligned(cb1, Element.ALIGN_CENTER, phrase2,540,710,0);
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
        consultantcell.setColspan(2);
        opdTable.addCell(consultantcell);
        opdTable.addCell(new Phrase("", font3));
       ;
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
        
       
        PdfPCell space = new PdfPCell(new Paragraph("Date / Time : "+date+" / "+time, smallBold));
        space.setBorder(Rectangle.NO_BORDER);
        space.setHorizontalAlignment(Element.ALIGN_RIGHT);

        space.setPaddingRight(20);
        table.addCell(space);
        

		float[] facilityTablCellWidth = { 0.3f, 1f };
		PdfPTable mainFacilityTable = new PdfPTable(facilityTablCellWidth);

		PdfPTable facilityTable = new PdfPTable(1);
		facilityTable.getDefaultCell().setBorder(0);

		
		facilityTable.addCell(new Phrase(" ", smallBold));
		PdfPCell facilitiesTitleCell = new PdfPCell(new Phrase("FACILITIES",
				font2));
		facilitiesTitleCell.setBorder(Rectangle.NO_BORDER);
		facilitiesTitleCell.setHorizontalAlignment(Element.ALIGN_CENTER);

		facilityTable.addCell(facilitiesTitleCell);
		facilityTable.addCell(new Phrase(12, "* Color Doppler Ultra Sound",
				smallBold));
		facilityTable.addCell(new Phrase("* Mammography", smallBold));
		facilityTable.addCell(new Phrase("* X-Ray Machine", smallBold));
		facilityTable.addCell(new Phrase("* ECHO", smallBold));
		facilityTable.addCell(new Phrase("* Angiography & Angioplasty",
				smallBold));
		facilityTable.addCell(new Phrase("* TMT", smallBold));
		facilityTable.addCell(new Phrase("* E.C.G.", smallBold));
		facilityTable.addCell(new Phrase("* Computerized Lab", smallBold));
		facilityTable.addCell(new Phrase("* Ortho Surgery (Trauma, Spine,",
				smallBold));
		facilityTable
				.addCell(new Phrase("  Arthroscopy, Knee Joint", smallBold));
		facilityTable.addCell(new Phrase("  Replacement)", smallBold));
		facilityTable.addCell(new Phrase("* Ventilator", smallBold));
		facilityTable.addCell(new Phrase("* Defebrilator", smallBold));
		facilityTable.addCell(new Phrase("* Endoscopy",
				smallBold));
		facilityTable.addCell(new Phrase("* Audiometery & Speech Therapy",
				smallBold));

		facilityTable.addCell(new Phrase(" ", smallBold));

		PdfPTable empanelledTable = new PdfPTable(1);
		empanelledTable.getDefaultCell().setBorder(0);
		empanelledTable.addCell(new Phrase(" ", smallBold));
		empanelledTable.addCell(new Phrase(" ", smallBold));
		PdfPCell empanelledTitleCell = new PdfPCell(new Phrase(
				"EMPANELLED WITH", font2));
		empanelledTitleCell.setBorder(Rectangle.NO_BORDER);
		empanelledTitleCell.setHorizontalAlignment(Element.ALIGN_CENTER);

		empanelledTable.addCell(empanelledTitleCell);
		empanelledTable.addCell(new Phrase("* BSNL Employees", smallBold));
		empanelledTable
				.addCell(new Phrase("* BPL Smart Card Holder", smallBold));
		empanelledTable.addCell(new Phrase("* For Cashless Treatment", font3));
		
		empanelledTable.addCell(new Phrase(" ", smallBold));
		empanelledTable.addCell(new Phrase(" ", smallBold));

		PdfPCell facilityCell = new PdfPCell(facilityTable);
		facilityCell.setPaddingBottom(5);
		facilityCell.setPaddingLeft(2);
		facilityCell.setBorderWidth(0.8f);
		mainFacilityTable.addCell(facilityCell);

		PdfPCell facilitiesCell2 = new PdfPCell(new Phrase("", font2));
		facilitiesCell2.setBorder(Rectangle.NO_BORDER);
		facilitiesCell2.setPaddingBottom(5);
		facilitiesCell2.setHorizontalAlignment(Element.ALIGN_CENTER);
		mainFacilityTable.addCell(facilitiesCell2);

		PdfPCell emptyCell = new PdfPCell(new Phrase(" ", font2));
		emptyCell.setBorder(Rectangle.NO_BORDER);
		emptyCell.setPaddingBottom(5);
		emptyCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		mainFacilityTable.addCell(emptyCell);

		PdfPCell emptyCell2 = new PdfPCell(new Phrase("", font2));
		emptyCell2.setBorder(Rectangle.NO_BORDER);
		emptyCell2.setPaddingBottom(5);
		emptyCell2.setHorizontalAlignment(Element.ALIGN_CENTER);
		mainFacilityTable.addCell(emptyCell2);

		PdfPCell empanelledCell = new PdfPCell(empanelledTable);
		empanelledCell.setPaddingBottom(5);
		empanelledCell.setPaddingLeft(2);
		empanelledCell.setBorderWidth(0.8f);
		mainFacilityTable.addCell(empanelledCell);

		PdfPCell empanelledCell2 = new PdfPCell(new Phrase("", font2));
		empanelledCell2.setBorder(Rectangle.NO_BORDER);
		empanelledCell2.setPaddingBottom(10);
		empanelledCell2.setHorizontalAlignment(Element.ALIGN_CENTER);
		mainFacilityTable.addCell(empanelledCell2);

		PdfPCell addFacilityTable = new PdfPCell(mainFacilityTable);
		addFacilityTable.setBorder(Rectangle.NO_BORDER);
		addFacilityTable.setPaddingTop(10);
		int spaceINT=190-(5*doctorname.size());
		addFacilityTable.setPaddingBottom(spaceINT);
		table.addCell(addFacilityTable);
        
        PdfPCell blankCell = new PdfPCell(new Phrase(" ", smallBold));
        blankCell.setRowspan(4);

		float[] packagesTableCellWidth = { 3f, 0.8f, 0.8f, 0.3f, 3f, 0.8f, 0.8f };
		PdfPTable packagesTable = new PdfPTable(packagesTableCellWidth);
		packagesTable.setWidthPercentage(30);
		packagesTable.getDefaultCell().setBorder(5);

		PdfPCell specialPackagesTitleCell = new PdfPCell(new Phrase(
				"SPECIAL PACKAGES", font2));
		specialPackagesTitleCell.setBorder(Rectangle.NO_BORDER);
		specialPackagesTitleCell.setColspan(7);
		specialPackagesTitleCell.setHorizontalAlignment(Element.ALIGN_CENTER);

		packagesTable.addCell(specialPackagesTitleCell);
		packagesTable
				.addCell(new Phrase("1. COMPLETE BODY PROFILE ", smallBold));
		packagesTable.addCell(new Phrase("Age >45 Yr ", smallBold));
		packagesTable.addCell(new Phrase("`600", font));
		packagesTable.addCell(blankCell);

		packagesTable.addCell(new Phrase("2. COMPLETE BODY + THYROID PANEL",
				smallBold));
		packagesTable.addCell(new Phrase("Age >45 Yr", smallBold));
		packagesTable.addCell(new Phrase("`850", font));

		packagesTable.addCell(new Phrase("3. LIPID PROFILE ", smallBold));
		packagesTable.addCell(new Phrase("Age 40 Yr ", smallBold));
		packagesTable.addCell(new Phrase("`270", font));
	

		packagesTable.addCell(new Phrase("4. RENAL + LIVER FUNCTION PANEL ",
				smallBold));
		packagesTable.addCell(new Phrase("Age >40 Yr", smallBold));
		packagesTable.addCell(new Phrase("`300", font));

		packagesTable.addCell(new Phrase("5. ARTHRITIC PANEL ", smallBold));
		packagesTable.addCell(new Phrase("Age >40 Yr", smallBold));
		packagesTable.addCell(new Phrase("`250", font));
	

		packagesTable.addCell(new Phrase("6. THYORID PANEL ", smallBold));
		packagesTable.addCell(new Phrase("Age >30 Yr", smallBold));
		packagesTable.addCell(new Phrase("`350", font));

		packagesTable.addCell(new Phrase("7. CARDIAC PANEL ", smallBold));
		packagesTable.addCell(new Phrase(" ", smallBold));
		packagesTable.addCell(new Phrase("`1200", font));

		packagesTable.addCell(new Phrase("8. DIABETIC PANEL ", smallBold));
		packagesTable.addCell(new Phrase(" ", smallBold));
		packagesTable.addCell(new Phrase("`1000", font));

		PdfPCell packagesCell = new PdfPCell(packagesTable);
		packagesCell.setPaddingBottom(0);
		packagesCell.setPaddingLeft(0);
		packagesCell.setBorderWidth(0.8f);
		table.addCell(packagesCell);

		PdfPCell footer = new PdfPCell(
				new Phrase(
						"   ",
						font3));
		footer.setBorder(Rectangle.NO_BORDER);
		footer.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(footer);

		PdfPCell footer2 = new PdfPCell(new Phrase(
				"Saturday : General OPD Closed, Sunday : Working, EMERGENCY : 24x7", font4));
		footer2.setBorder(Rectangle.NO_BORDER);
		footer2.setPaddingBottom(5);
		footer2.setHorizontalAlignment(Element.ALIGN_CENTER);
		footer2.setBackgroundColor(BaseColor.LIGHT_GRAY);
		
		// PdfPCell footerWrap = new PdfPCell(footer2);
		// footerWrap.setPaddingLeft(50);
		// footerWrap.setPaddingRight(50);
		// footerWrap.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(footer2);

		java.net.URL imgURLPin = OPDSlippdf13aug.class.getResource("/icons/pin.png");
		Image imagePin = Image.getInstance(imgURLPin);

		System.out.println("Height "+doctorTable.getRowHeight(1));
		int margin=(int)Math.round((doctorname.size()*6.6));
		imagePin.scalePercent(20);
		imagePin.setAbsolutePosition(55, 630-margin);
		
		document.add(imagePin);
		
		imagePin.setAbsolutePosition(55, 392-margin);
		
		document.add(imagePin);
		document.add(table);
	//	onEndPage(wr,document);
		document.close();
        
      
		new OpenFile(RESULT);
  }

    
    public void getAllDoctors() {
		DoctorDBConnection dbConnection = new DoctorDBConnection();
		ResultSet resultSet = dbConnection.retrieveAllData();
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