package SmbUtils;

import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileOutputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.Callable;

public class SmbUploaderTask implements Callable<Boolean> {

    private String localPath;
    private String smbPath;

    public SmbUploaderTask(String localPath, String smbPath) {
        this.localPath = localPath;
        this.smbPath = smbPath;
    }

    @Override
    public Boolean call() {
        try {
            File localFile = new File(localPath);
            if (!localFile.exists()) {
                System.err.println("Local file does not exist: " + localPath);
                return false;
            }

            try (InputStream in = new FileInputStream(localFile);
                 OutputStream out = new SmbFileOutputStream(new SmbFile(smbPath))) {

                byte[] buffer = new byte[4096];
                int len;

                while ((len = in.read(buffer)) > 0) {
                    out.write(buffer, 0, len);
                }

                System.out.println("File uploaded successfully to SMB share.");
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
