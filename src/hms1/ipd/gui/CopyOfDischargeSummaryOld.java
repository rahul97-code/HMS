package hms1.ipd.gui;



import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.codec.Base64.InputStream;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import com.itextpdf.text.html.*;
import com.itextpdf.text.html.simpleparser.HTMLWorker;
import com.itextpdf.text.xml.*;

import hms.store.database.SuppliersDBConnection;
import hms1.ipd.database.IPDDBConnection;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Stage;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
//import javax.swing.JOptionPane;   
import com.itextpdf.text.PageSize;
 
public class CopyOfDischargeSummaryOld extends Application {
    
   String PatientID="",PatientName="",IPDNO="";
   String message_body="";
   static String OS;
   String[] open = new String[4];
   String[] data = new String[24];
    public void start(final Stage stage) {
//    	java.net.URL url = ClassLoader.getSystemResource("/icons/rotaryLogo.png");
    	stage.getIcons().add(new Image("/icons/rotaryLogo.png"));
    	OS = System.getProperty("os.name").toLowerCase();
    	 Parameters params = getParameters();
         List<String> list = params.getRaw();
//         System.out.println(list.size());
         for(String each : list){
             System.out.println(each);
         }
             IPDNO= list.get(0); 
             PatientID=list.get(1);
             PatientName=list.get(2);
         	getDischargeSummaryDetail(IPDNO);
//         }
        stage.setTitle("Discharge Summary Report");
        stage.setWidth(500);
        stage.setHeight(500);
        Scene scene = new Scene(new Group());
    
        final VBox root = new VBox();        
        root.setPadding(new Insets(8, 8, 8, 8));
        root.setSpacing(5);
        root.setAlignment(Pos.BOTTOM_LEFT);
        
        final GridPane grid = new GridPane();
        grid.setVgap(5);
        grid.setHgap(10);
              
     
        final Label sendTo = new Label("IPD No:");
        GridPane.setConstraints(sendTo, 0, 0);
        grid.getChildren().add(sendTo); 
        final TextField tbTo = new TextField();
        tbTo.setPrefWidth(400);
        GridPane.setConstraints(tbTo, 1, 0);
        grid.getChildren().add(tbTo);
        tbTo.setText(IPDNO);
        tbTo.setEditable(false);
//        tbTo.setDisable(true);
        
        final Label subjectLabel = new Label("Patient ID:");
        GridPane.setConstraints(subjectLabel, 0, 1);
        grid.getChildren().add(subjectLabel);        
        
        final TextField tbSubject = new TextField();
        tbTo.setPrefWidth(400);
        GridPane.setConstraints(tbSubject, 1, 1);
        grid.getChildren().add(tbSubject);
        tbSubject.setText(PatientID);
        tbSubject.setEditable(false);
        root.getChildren().add(grid);
        
        final HTMLEditor htmlEditor = new HTMLEditor();
        htmlEditor.setPrefHeight(370);
        htmlEditor.setHtmlText(message_body);
 
        root.getChildren().add(htmlEditor);  
        final TextArea textArea = new TextArea();
        // Set the Size of the TextArea
        textArea.setPrefHeight(60);
//        grid.getChildren().add(textArea);
        Button send = new Button("Send");
        send.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				 String k = "<html><body><h1><center><p style='font-family:HELVETICA'>ROTARY AMBALA CANCER & GENERAL HOSPITAL</center></h1><br><h2>" +
				    		"Managed by:Rotary Ambala Cancer Detection & Welfare Society(Regd)</h2></body></html>";
				IPDDBConnection ipddbConnection = new IPDDBConnection();
				data[0] =k+htmlEditor.getHtmlText();
				data[1]=IPDNO+"";
				data[2]=PatientID+"";
				data[3]=PatientName+"";
				try {
				 ipddbConnection.updateDischargeSummary(data);
//				 ipddbConnection.inserDataDischargeSummary(data);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
//				  stage.close();
				  try {
//					    String k = "<html><body><h1><center>ROTARY AMBALA CANCER & GENERAL HOSPITAL</center></h1><br><h2>" +
//					    		"Managed by:Rotary Ambala Cancer Detection & Welfare Society(Regd)</h2></body></html>";
					    OutputStream file = new FileOutputStream(new File("DischargeSummaryReport/DischargeSummary"+data[1]+".pdf"));
					    Document document = new Document();
					    PdfWriter.getInstance(document, file);
					    document.setPageSize(PageSize.A4.rotate());
						document.setMargins(10, 10, 10, 10);
					    document.open();
					    HTMLWorker htmlWorker = new HTMLWorker(document);
					    htmlWorker.parse(new StringReader(data[0]));
					    document.close();
					    
					    
						if (isWindows()) {
							OPenFileWindows("DischargeSummaryReport/" + data[1] + ".pdf");
//							OPenFileWindows("C:\\DischargeSummary/" + data[1] + ".pdf");
							System.out.println("This is Windows");
						}else if (isUnix()) {
							if (System.getProperty("os.version").equals("3.11.0-12-generic")) {
								Run(new String[] { "/bin/bash", "-c",
										open[0] + "DischargeSummaryReport/" + data[1] + ".pdf" });
							} else {
								Run(new String[] { "/bin/bash", "-c",
										open[1] + "DischargeSummaryReport/" + data[1] + ".pdf" });
							}
							
						}
						readFile();
					    file.close();
					    stage.close();
					} catch (Exception e) {
					    e.printStackTrace();
					}
//				  HtmlConverter.convertToPdf(HTML, new FileOutputStream("string-to-pdf.pdf"));
//				  dispose();
			}
		});
      
//        send.setOnAction(new EventHandler() {
//
//			public void handle(Event arg0) {
//				// TODO Auto-generated method stub
//				  textArea.setText(htmlEditor.getHtmlText());
////				JOptionPane.showMessageDialog(null, "Welcome");
//			}
//		});
        root.getChildren().add(send);  
   
    
        // Set the Style of the TextArea
     
                      
        scene.setRoot(root);
        stage.setScene(scene);
        stage.show();
        
       
    }
 
    public static void main(String[] args) {
        launch(args);
    }
	public void getDischargeSummaryDetail(String index) {

		IPDDBConnection ipddbConnection = new IPDDBConnection();
		ResultSet resultSet = ipddbConnection
				.searchIPDDischargeSummary(index);
		try {
			while (resultSet.next()) {

				message_body = resultSet.getObject(1).toString();
//				supplierNameSTR = resultSet.getObject(2).toString();
//				mobileSTR = (resultSet.getObject(4).toString());
//				addressSTR = (resultSet.getObject(6).toString() + ", "
//						+ resultSet.getObject(7).toString() + ", " + resultSet
//						.getObject(8).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ipddbConnection.closeConnection();
	}
	
	
	public static boolean isWindows() {

		return (OS.indexOf("win") >= 0);

	}
	public void OPenFileWindows(String path) {
		try {

			File f = new File(path);
			if (f.exists()) {
				if (Desktop.isDesktopSupported()) {
					Desktop.getDesktop().open(f);
				} else {
					System.out.println("File does not exists!");
				}
			}
		} catch (Exception ert) {
		}
	}
	public static boolean isUnix() {

		return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS
				.indexOf("aix") > 0);

	}
	public void Run(String[] cmd) {
		try {
			Process process = Runtime.getRuntime().exec(cmd);
			int processComplete = process.waitFor();
			if (processComplete == 0) {
				System.out.println("successfully");
			} else {
				System.out.println("Failed");
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void readFile() {
		// The name of the file to open.
//		File fileName = "C:\\DischargeSummary/" + data[1] + ".pdf";
		String mainDir = "";
		// This will reference one line at a time
		String line = null;
		
		   File myFile = new File("DischargeSummaryReport/DischargeSummary" + data[1] + ".pdf");
	        try {
				Desktop.getDesktop().open(myFile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//		try {
//			// FileReader reads text files in the default encoding.
//			FileReader fileReader = new FileReader(fileName);
//
//			// Always wrap FileReader in BufferedReader.
//			BufferedReader bufferedReader = new BufferedReader(fileReader);
//			String str = null;
//			boolean fetch = true;
//			while ((line = bufferedReader.readLine()) != null && fetch) {
//				// System.out.println(line);
//				str = line;
//				fetch = false;
//			}
//			String data[] = new String[22];
//			int i = 0;
//			for (String retval : str.split("@")) {
//				data[i] = retval;
//				i++;
//			}
//			mainDir = data[1];
//			open[0] = data[2];
//			open[1] = data[3];
//			open[2] = data[4];
//			// Always close files.
//			bufferedReader.close();
//		} catch (FileNotFoundException ex) {
//			System.out.println("Unable to open file '" + fileName + "'");
//		} catch (IOException ex) {
//			System.out.println("Error reading file '" + fileName + "'");
//			// Or we could just do this:
//			// ex.printStackTrace();
//		}
	}
}