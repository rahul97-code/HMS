package hms.patient.slippdf;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

public class OpenFile {

	static String OS;
	public OpenFile(String filePath)
	{
		OS = System.getProperty("os.name").toLowerCase();
		
		if (isWindows()) {
			OPenFileWindows(filePath);
			
		}else if (isUnix()) {
			if (System.getProperty("os.version").equals("3.11.0-12-generic")) {
				Run(new String[] { "/bin/bash", "-c",
						"exo-open "+filePath });
			} else {
				Run(new String[] { "/bin/bash", "-c",
						"exo-open "+filePath });
			}
			System.out.println("This is Unix or Linux");
		} 
	}
	public void OPenFileWindows(String path) {

		try {

			File f = new File(path);
			if (f.exists()) {
				if (Desktop.isDesktopSupported()) {
					Desktop.getDesktop().open(f);
				} else {
					System.out.println("File does not exists!");
				}
			}
		} catch (Exception ert) {
		}
	}
	public void Run(String[] cmd) {
		try {
			Process process = Runtime.getRuntime().exec(cmd);
			int processComplete = process.waitFor();
			if (processComplete == 0) {
				System.out.println("successfully");
			} else {
				System.out.println("Failed");
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static boolean isWindows() {

		return (OS.indexOf("win") >= 0);

	}

	
	public static boolean isUnix() {

		return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS
				.indexOf("aix") > 0);
	}

}
