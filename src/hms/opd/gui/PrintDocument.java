package hms.opd.gui;

import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.print.DocPrintJob;

import org.apache.pdfbox.pdmodel.PDDocument;


public class PrintDocument {

	
	
	public PrintDocument(String document) throws FileNotFoundException, IOException, PrinterException
	{
		PDDocument doc = PDDocument.load(new FileInputStream(document)); // read pdf file.
		String printerNameDesired = "EPSON";
		//String printerNameDesired = "CutePDF";

		javax.print.PrintService[] service = PrinterJob.lookupPrintServices();
		DocPrintJob docPrintJob = null;
		int count = service.length;

		for (int i = 0; i < count; i++) {
			if (service[i].getName().contains(printerNameDesired)) {
				docPrintJob = service[i].createPrintJob();
				i = count;
			}
		}
		PrinterJob pjob = PrinterJob.getPrinterJob();
		pjob.setPrintService(docPrintJob.getPrintService());
		pjob.setJobName("doctor token");
		//doc.print(pjob);
		doc.silentPrint(pjob);
	}
}
