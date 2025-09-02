package hms.test.gui;

import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.xwpf.usermodel.*;
import java.io.*;
import java.util.List;

public class WordReplacer {

    public static void process(
            String inputPath,
            String outputPath,
            String p_id,
            String p_name,
            String patient_age,
            String p_sex,
            String exam_doctorname,
            String test_date
    ) throws IOException {

        if (inputPath.toLowerCase().endsWith(".doc")) {
            processDoc(inputPath, outputPath, p_id, p_name, patient_age + "/" + p_sex, exam_doctorname, test_date);
        } else if (inputPath.toLowerCase().endsWith(".docx")) {
            processDocx(inputPath, outputPath, p_id, p_name, patient_age + "/" + p_sex, exam_doctorname, test_date);
        } else {
            throw new IllegalArgumentException("Unsupported file format: " + inputPath);
        }
    }

    private static void processDoc(
            String in, String out,
            String p_id, String p_name, String ageSex, String doctorRef, String testDate
    ) throws IOException {
        try (FileInputStream fis = new FileInputStream(in);
             POIFSFileSystem fs = new POIFSFileSystem(fis);
             HWPFDocument doc = new HWPFDocument(fs);
             FileOutputStream fos = new FileOutputStream(out)
        ) {
            Range range = doc.getRange();
            range.replaceText("p_id", p_id);
            range.replaceText("p_name", p_name);
            range.replaceText("age_sex", ageSex);
            range.replaceText("doctor_ref", doctorRef);
            range.replaceText("test_date", testDate);
            doc.write(fos);
        }
    }

    private static void processDocx(
            String in, String out,
            String p_id, String p_name, String ageSex, String doctorRef, String testDate
    ) throws IOException {
        try (FileInputStream fis = new FileInputStream(in);
             XWPFDocument docx = new XWPFDocument(fis);
             FileOutputStream fos = new FileOutputStream(out)
        ) {
            replaceInDocx(docx, "p_id", p_id);
            replaceInDocx(docx, "p_name", p_name);
            replaceInDocx(docx, "age_sex", ageSex);
            replaceInDocx(docx, "doctor_ref", doctorRef);
            replaceInDocx(docx, "test_date", testDate);
            docx.write(fos);
        }
    }

    private static void replaceInDocx(XWPFDocument doc, String placeholder, String value) {
        for (XWPFParagraph p : doc.getParagraphs()) {
            for (XWPFRun r : p.getRuns()) {
                String text = r.getText(0);
                if (text != null && text.contains(placeholder)) {
                    r.setText(text.replace(placeholder, value), 0);
                }
            }
        }
        for (XWPFTable tbl : doc.getTables()) {
            for (XWPFTableRow row : tbl.getRows()) {
                for (XWPFTableCell cell : row.getTableCells()) {
                    for (XWPFParagraph p : cell.getParagraphs()) {
                        for (XWPFRun r : p.getRuns()) {
                            String text = r.getText(0);
                            if (text != null && text.contains(placeholder)) {
                                r.setText(text.replace(placeholder, value), 0);
                            }
                        }
                    }
                }
            }
        }
    }
}
