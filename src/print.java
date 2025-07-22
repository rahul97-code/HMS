import hms.main.MainLogin;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import javax.print.PrintService;

public class print {

	public static void printCard(final String bill) {
		final PrinterJob job = PrinterJob.getPrinterJob();

		Printable contentToPrint = new Printable() {
			@Override
			public int print(Graphics graphics, PageFormat pageFormat,
					int pageIndex) throws PrinterException {

				Graphics2D g2d = (Graphics2D) graphics;

				g2d.translate(pageFormat.getImageableX(),
						pageFormat.getImageableY());
				g2d.setFont(new Font("Mangal", Font.BOLD, 10));

				if (pageIndex > 0) {
					return NO_SUCH_PAGE;
				} // Only one page

				String Bill[] = bill.split(";");

				int y = 20;
				

					g2d.drawString("Token No.", 110, 110);
					g2d.setFont(new Font("Mangal", Font.BOLD, 40));
					g2d.drawString("30", 120, 140);
					g2d.setFont(new Font("Mangal", Font.BOLD, 40));
					g2d.drawString("राम कुमार", 120, 180);
					y = y + 50;
		
				g2d.draw(new Rectangle2D.Double(100, 100,
                        200,
                        200));
				return PAGE_EXISTS;

			}

		};

		PageFormat pageFormat = new PageFormat();
		pageFormat.setOrientation(PageFormat.PORTRAIT);
		Paper pPaper = pageFormat.getPaper();

		pPaper.setImageableArea(0, 0, pPaper.getWidth(), pPaper.getHeight() - 2);
		pageFormat.setPaper(pPaper);

		job.setPrintable(contentToPrint, pageFormat);
		String printerNameDesired = "Canon LBP2900-2";

		PrintService[] service = PrinterJob.lookupPrintServices(); // list of printers

		    int count = service.length;

		    for (int i = 0; i < count; i++) {
		        if (service[i].getName().contains(printerNameDesired )) {
		        	try {
						job.setPrintService(service[i].createPrintJob().getPrintService());
						try {
							  job.setJobName("job");
							job.print();
							System.out.println(""+printerNameDesired);
						} catch (PrinterException e) {
							System.err.println(e.getMessage());
						}
					} catch (PrinterException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		            i = count;
		            break;
		        }
		        System.out.println(service[i].getName());
		    }
		  
		   
		
	}
	public static void main(String[] arg)
	{
		new print().printCard("राम कुमार;Token Number;34");
	}
}