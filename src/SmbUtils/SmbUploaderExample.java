package SmbUtils;
import SmbUtils.SmbUploaderTask;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SmbUploaderExample {
    public static void main(String[] args) {
        // Example: Upload local file to SMB share
        String localFilePath = "/home/linux/Documents/ayushmanpdf.pdf";

        // NOTE: Format for smbPath (anonymous):
        // smb://<host>/<share>/<path>
        String smbFilePath = "smb://192.168.1.33/data/ayushmanpdf.pdf";

        // Create a task
        SmbUploaderTask uploadTask = new SmbUploaderTask(localFilePath, smbFilePath);

        // Run the task in a thread pool
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Boolean> result = executor.submit(uploadTask);

        try {
            // Wait for result
            if (result.get()) {
                System.out.println("Upload completed successfully.");
            } else {
                System.out.println("Upload failed.");
            }
        } catch (Exception e) {
            System.err.println("Error running upload task: " + e.getMessage());
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
    }
}
