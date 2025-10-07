package hms.test.gui;

import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;

import java.io.*;
import java.util.List;

import javax.swing.JOptionPane;

public class DoSignatureOnDocx {

	public DoSignatureOnDocx(String InputPath,String OutputPath,String SignPath) {
        try {
            FileInputStream fis = new FileInputStream(InputPath);
            XWPFDocument doc = new XWPFDocument(fis);
            fis.close();

            String placeholder = "_signature_";
            String imagePath = SignPath;
            boolean replaced = false;

            // Replace in body paragraphs
            for (XWPFParagraph paragraph : doc.getParagraphs()) {
                if (replacePlaceholderInParagraph(paragraph, placeholder, imagePath)) {
                    replaced = true;
                }
            }

            // Replace in body tables
            for (XWPFTable table : doc.getTables()) {
                for (XWPFTableRow row : table.getRows()) {
                    for (XWPFTableCell cell : row.getTableCells()) {
                        for (XWPFParagraph paragraph : cell.getParagraphs()) {
                            if (replacePlaceholderInParagraph(paragraph, placeholder, imagePath)) {
                                replaced = true;
                            }
                        }
                    }
                }
            }

            // Replace in footers (paragraphs and tables)
            List<XWPFFooter> footers = doc.getFooterList();
            for (XWPFFooter footer : footers) {
                // Paragraphs in footer
                for (XWPFParagraph paragraph : footer.getParagraphs()) {
                    if (replacePlaceholderInParagraph(paragraph, placeholder, imagePath)) {
                        replaced = true;
                    }
                }

                // Tables in footer
                for (XWPFTable table : footer.getTables()) {
                    for (XWPFTableRow row : table.getRows()) {
                        for (XWPFTableCell cell : row.getTableCells()) {
                            for (XWPFParagraph paragraph : cell.getParagraphs()) {
                                if (replacePlaceholderInParagraph(paragraph, placeholder, imagePath)) {
                                    replaced = true;
                                }
                            }
                        }
                    }
                }
            }

            if (!replaced) {
            	JOptionPane.showMessageDialog(null,"Placeholder not found in document.");
            } else {
                FileOutputStream fos = new FileOutputStream(OutputPath);
                doc.write(fos);
                fos.close();
                System.out.println("Signature inserted successfully.");
                JOptionPane.showMessageDialog(null, "Signature uploaded successfully!", "Signature", JOptionPane.INFORMATION_MESSAGE);

            }

            doc.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	private static boolean replacePlaceholderInParagraph(XWPFParagraph paragraph, String placeholder, String imagePath) {
	    List<XWPFRun> runs = paragraph.getRuns();
	    if (runs == null || runs.isEmpty()) return false;

	    StringBuilder paragraphText = new StringBuilder();
	    for (XWPFRun run : runs) {
	        String text = run.getText(0);
	        if (text != null) {
	            paragraphText.append(text);
	        }
	    }

	    String fullText = paragraphText.toString();

	    if (fullText.contains(placeholder)) {
	        // Clear existing runs
	        for (int i = runs.size() - 1; i >= 0; i--) {
	            paragraph.removeRun(i);
	        }

	        // Load image from classpath
	        try (InputStream imageStream = DoSignatureOnDocx.class.getResourceAsStream(imagePath)) {
	            if (imageStream == null) {
	                JOptionPane.showMessageDialog(null, "Failed to load signature image: " + imagePath);
	                return false;
	            }

	            XWPFRun imageRun = paragraph.createRun();
	            imageRun.addPicture(
	                    imageStream,
	                    Document.PICTURE_TYPE_PNG,
	                    imagePath,
	                    Units.toEMU(100),
	                    Units.toEMU(50)
	            );
	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	        return true;
	    }

	    return false;
	}

    
}
