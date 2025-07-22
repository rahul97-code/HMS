package hms.main;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JOptionPane;

public class Pdf_troubleshooting {
	
	 static String OS = System.getProperty("os.name").toLowerCase();
	 
	public Pdf_troubleshooting(String password) throws IOException {
		if (isUnix()) {
			ProcessBuilder processBuilder = new ProcessBuilder();
			processBuilder.command("bash", "-c", "echo " + password + "| sudo -S apt-get install exo-utils");
			try {

				Process process = processBuilder.start();

				StringBuilder output = new StringBuilder();

				BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

				String line;
				while ((line = reader.readLine()) != null) {
					output.append(line + "\n");
					System.out.println(line);
				}

				int exitVal = process.waitFor();
				if (exitVal == 0) {
					if (output.toString().toLowerCase().contains("already")) {
						JOptionPane.showMessageDialog(null, "Package Already installed.", "Info.",
								JOptionPane.INFORMATION_MESSAGE);
						return;
					}

					JOptionPane.showMessageDialog(null, "Problem solved succsessfully.", "Success",
							JOptionPane.INFORMATION_MESSAGE);
					System.out.println("Success!");
					System.out.println(output);
					System.exit(0);
				} else {
					//abnormal...
				
						JOptionPane.showMessageDialog(null, "Error!", "Error",
								JOptionPane.ERROR_MESSAGE);
					

				}

			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} 
		}else
		{
			JOptionPane.showMessageDialog(null, "This is not Unix or Linux",
					"Info.", JOptionPane.ERROR_MESSAGE);
		}

	}
	public static boolean isUnix() {

		return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS
				.indexOf("aix") > 0);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			new Pdf_troubleshooting("mdi@123");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
