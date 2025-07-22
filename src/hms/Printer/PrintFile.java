package hms.Printer;


import java.awt.print.PrinterJob;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.swing.JOptionPane;

import hms.main.Developing_environment;

public class PrintFile {
	
	private String[] data=new String[12];
	public static void main(String[] args) {
		new PrintFile("token.pdf",1);
	}
	
	public PrintFile(String FileName,int Slipindex){

		readData();
		PrintService myService=null;
		ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<PrintService[]> future = executor.submit(new Callable<PrintService[]>() {
            @Override
            public PrintService[] call() throws Exception {
                return PrinterJob.lookupPrintServices();
            }
        });
        try {
            // Try to get the available print services within 1 second
            PrintService[] ps = future.get(1, TimeUnit.SECONDS);
            
            if (ps.length == 0) {
                JOptionPane.showMessageDialog(null, "Printer not found", "Input Error", JOptionPane.ERROR_MESSAGE);
                throw new IllegalStateException("No Printer found");
            }
            
            // Replace `data[0]` with the appropriate index as per your program
          myService = ps[Integer.parseInt(data[0])];
            if (myService == null) {
                JOptionPane.showMessageDialog(null, "Printer not found", "Input Error", JOptionPane.ERROR_MESSAGE);
                throw new IllegalStateException("Printer not found");
            }
            
            // Proceed with the printer setup here...
            
        } catch (TimeoutException | InterruptedException | ExecutionException e) {
            System.out.println("Printer search timed out, skipping...");
            // Proceed without the printer or handle accordingly
        }  finally {
            executor.shutdown();
        }
	
	    if (!new Developing_environment().ACTIVE && Boolean.parseBoolean(data[Slipindex])) {
			try {
				FileInputStream fis = new FileInputStream(FileName);
				Doc pdfDoc = new SimpleDoc(fis, DocFlavor.INPUT_STREAM.AUTOSENSE, null);
				DocPrintJob printJob = myService.createPrintJob();
				printJob.print(pdfDoc, new HashPrintRequestAttributeSet());
				fis.close();
			} catch (IOException | PrintException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			JOptionPane.showMessageDialog(null, "Printing Successfully", "Success", JOptionPane.ERROR_MESSAGE);
		}
	}
	public void readData() {
		FileInputStream fis = null;
		int counter = 0;
		try {
			File file = new File("printer.settings");
			if(!file.exists()) {
				file.createNewFile();
				WriteDefaultContent();
			}
			fis = new FileInputStream(file);
			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fis);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			
			// Read File Line By Line
			while ((strLine = br.readLine()) != null) {

				data[counter] = strLine;
				counter++;
			}
			in.close();
		} catch (Exception ex) {
			System.out.print(""+ex);
		} finally {
			try {
				if (null != fis)
					fis.close();
			} catch (IOException ex) {
			}
		}
		
	}
	private void WriteDefaultContent() {
		// TODO Auto-generated method stub
//	    DocFlavor flavor = DocFlavor.SERVICE_FORMATTED.PAGEABLE;
//	    PrintRequestAttributeSet patts = new HashPrintRequestAttributeSet();
//	    patts.add(Sides.ONE_SIDED);
//	    System.out.println("Available printers: " + Arrays.asList(ps));
		String fileName="printer.settings";
		String content=0+"\n"
				+ "true\n"
				+ "true\n"
				+ "true\n"
				+ "true\n"
				+ "true\n"
				+ "true\n"
				+ "true\n"
				+ "true";
		try {
			FileWriter writer = new FileWriter(fileName);
			writer.write(content);
			writer.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	
	}
}