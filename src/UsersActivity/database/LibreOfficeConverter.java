package UsersActivity.database;

import java.io.*;

public class LibreOfficeConverter {
    public static void main(String[] args) throws Exception {
        String inputFile = "/home/mdi-android-1/git/HMS/Digital Reports/AFB.doc";
        String outputDir = "/home/mdi-android-1/git/HMS/Digital Reports/";

        String[] command = {
            "libreoffice",
            "--headless",
            "--nofirststartwizard",
            "--convert-to",
            "pdf",
            inputFile,
            "--outdir",
            outputDir
        };

        ProcessBuilder pb = new ProcessBuilder(command);
        pb.directory(new File("/home/mdi-android-1/git/HMS"));
        pb.environment().putAll(System.getenv());
        Process process = pb.start();

        try (BufferedReader stdOut = new BufferedReader(new InputStreamReader(process.getInputStream()));
             BufferedReader stdErr = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {

            String line;
            while ((line = stdOut.readLine()) != null) {
                System.out.println("STDOUT: " + line);
            }
            while ((line = stdErr.readLine()) != null) {
                System.err.println("STDERR: " + line);
            }
        }

        int exitCode = process.waitFor();
        System.out.println("Exit code: " + exitCode);
    }
}
