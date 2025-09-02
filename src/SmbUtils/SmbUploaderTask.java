package SmbUtils;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.JOptionPane;

public class SmbUploaderTask implements Runnable {

    public static void main(String[] args) {
        // Sample usage
        String localFilePath = "C:/localfolder/test.docx";
        String smbUrl = "smb://username:password@192.168.1.100/sharedfolder/test.docx";

        new Thread(new SmbUploaderTask(localFilePath, smbUrl)).start();
    }


        private String localPath;
        private String smbPath;

        public SmbUploaderTask(String localPath, String smbPath) {
            this.localPath = localPath;
            this.smbPath = smbPath;
        }

        @Override
        public void run() {
            try {
                File localFile = new File(localPath);
                InputStream in = new FileInputStream(localFile);

                SmbFile smbFile = new SmbFile(smbPath);
                OutputStream out = new SmbFileOutputStream(smbFile);

                byte[] buffer = new byte[4096];
                int len;

                while ((len = in.read(buffer)) > 0) {
                    out.write(buffer, 0, len);
                }

                in.close();
                out.close();

                System.out.println("File uploaded successfully to SMB share.");
                JOptionPane.showMessageDialog(null, "File uploaded successfully!", "Upload Complete", JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

