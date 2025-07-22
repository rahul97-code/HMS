
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
   
public class MyPrinter{  
   
   public static void main(String args[]){  
        
        FileInputStream psStream = null;  
        try {  
            psStream = new FileInputStream("opdslip1.pdf");  
            } catch (FileNotFoundException ffne) {  
              ffne.printStackTrace();  
            }  
            if (psStream == null) {  
                return;  
            }  
        DocFlavor psInFormat = DocFlavor.INPUT_STREAM.AUTOSENSE;  
        Doc myDoc = new SimpleDoc(psStream, psInFormat, null);    
        PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();  
        PrintService[] services = PrintServiceLookup.lookupPrintServices(psInFormat, aset);  
          
        // this step is necessary because I have several printers configured  
        PrintService myPrinter = null;  
        for (int i = 0; i < services.length; i++){  
          //  System.out.println("service found: "+svcName);  
            String svcName = services[i].toString();             
            if (svcName.contains("Canon LBP2900-2")){  
                myPrinter = services[i];  
                System.out.println("my printer found: "+svcName);  
                break;  
            }  
        }  
          
        if (myPrinter != null) {              
            DocPrintJob job = myPrinter.createPrintJob();  
            try {  
            job.print(myDoc, aset);  
              
            } catch (Exception pe) {pe.printStackTrace();}  
        } else {  
            System.out.println("no printer services found");  
        }  
   }  
}  