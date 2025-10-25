package SmbUtils;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class SmbFileListerTask implements Callable<List<String>> {

    private String path;
    private String username;
    private String password;

    public SmbFileListerTask(String path, String username, String password) {
        this.path = path;
        this.username = username;
        this.password = password;
    }

    @Override
    public List<String> call() {
        List<String> fileNames = new ArrayList<String>();
        try {
            if (path.startsWith("smb://")) {
                // --- SMB Path ---
                NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication("", username, password);
                SmbFile smbDir = new SmbFile(path, auth);

                if (smbDir.exists() && smbDir.isDirectory()) {
                    SmbFile[] files = smbDir.listFiles();
                    for (int i = 0; i < files.length; i++) {
                        if (files[i].isFile()) {
                            fileNames.add(files[i].getName());
                        }
                    }
                } else {
                    System.err.println("SMB path not found or not a directory: " + path);
                }

            } else {
                // --- Local Path ---
                File localDir = new File(path);
                if (localDir.exists() && localDir.isDirectory()) {
                    File[] files = localDir.listFiles();
                    if (files != null) {
                        for (int i = 0; i < files.length; i++) {
                            if (files[i].isFile()) {
                                fileNames.add(files[i].getName());
                            }
                        }
                    }
                } else {
                    System.err.println("Local path not found or not a directory: " + path);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileNames;
    }

    // --- Example usage ---
    public static void main(String[] args) {
        try {
            // Local folder example
            SmbFileListerTask localTask = new SmbFileListerTask("/home/user/videos", "", "");
            List<String> localFiles = localTask.call();
            System.out.println("Local Files: " + localFiles);

            // SMB folder example
            SmbFileListerTask smbTask = new SmbFileListerTask(
                    "smb://192.168.1.10/shared/videos/", "username", "password"
            );
            List<String> smbFiles = smbTask.call();
            System.out.println("SMB Files: " + smbFiles);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
