package hms.sukhpal.updater;

import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import jcifs.smb.SmbFile;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.border.TitledBorder;
import javax.swing.border.LineBorder;
import java.awt.Color;

public class UpdaterMain extends JDialog {

	String[] data=new String[10];
	String version="";
	String newVersion="",mainDir="";
	static String OS;
	String path;
	public Timer timer;
	String mainPath;
	public Window lblNewLabel;
	private JLabel TotalFileSizeLable;
	private JLabel RemainingFileSizeLable;
	private JLabel cuurentVersionLbl;
	private JLabel NewVersionLbl;
	public static JProgressBar progressBar;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		new UpdaterMain().setVisible(true);
	}
	
	public UpdaterMain() {
		UpdateCheckerDBConnection updateCheckerDBConnection=new UpdateCheckerDBConnection();
		newVersion=updateCheckerDBConnection.retrieveVersionNo();
		updateCheckerDBConnection.closeConnection();
		readData();
		
		setTitle("New Update");
		setPreferredSize(new Dimension(400, 125));
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		//setBounds(new Rectangle(350, 401, 400, 125));
		setBounds(650, 400, 549, 231);
		setLocation((Toolkit.getDefaultToolkit().getScreenSize().width)/2 - getWidth()/2, (Toolkit.getDefaultToolkit().getScreenSize().height)/2 - getHeight()/2);

		getContentPane().setLayout(null);
		
		progressBar = new JProgressBar();
		progressBar.setBounds(27, 149, 483, 29);
		progressBar.setToolTipText("HMS is updating so please wait");
		getContentPane().add(progressBar);
		
		JLabel lblNewLabel_2 = new JLabel(" HMS UPDATING ...");
		lblNewLabel_2.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 16));
		lblNewLabel_2.setIcon(new ImageIcon(UpdaterMain.class.getResource("/icons/updates.png")));
		lblNewLabel_2.setBounds(12, 12, 262, 61);
		getContentPane().add(lblNewLabel_2);
		
		JLabel label = new JLabel("");
		label.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Downloading", TitledBorder.RIGHT, TitledBorder.TOP, null, new Color(51, 51, 51)));
		label.setBounds(12, 128, 515, 61);
		getContentPane().add(label);
		
		JLabel lblFileSize = new JLabel("File Size : ");
		lblFileSize.setBounds(27, 85, 79, 15);
		getContentPane().add(lblFileSize);
		
		TotalFileSizeLable = new JLabel("");
		TotalFileSizeLable.setBounds(96, 85, 104, 15);
		getContentPane().add(TotalFileSizeLable);
		
		JLabel lblFileSize_1 = new JLabel("Downloaded : ");
		lblFileSize_1.setBounds(27, 112, 119, 15);
		getContentPane().add(lblFileSize_1);
		
		RemainingFileSizeLable = new JLabel("");
		RemainingFileSizeLable.setBounds(134, 112, 111, 15);
		getContentPane().add(RemainingFileSizeLable);
		
		JLabel label_1 = new JLabel("Current version : ");
		label_1.setBounds(310, 12, 123, 15);
		getContentPane().add(label_1);
		
		JLabel label_1_1 = new JLabel("New version : ");
		label_1_1.setBounds(333, 35, 100, 15);
		getContentPane().add(label_1_1);
		
		cuurentVersionLbl= new JLabel(version);
		cuurentVersionLbl.setBounds(445, 12, 82, 15);
		getContentPane().add(cuurentVersionLbl);
		
	    NewVersionLbl = new JLabel(newVersion);
		NewVersionLbl.setBounds(445, 36, 82, 15);
		getContentPane().add(NewVersionLbl);

	}
	public boolean checkUpdate()
	{	
		path=System.getProperty("user.dir");
		System.out.print("New version: "+newVersion);
		if(version.equals(newVersion))
		{
			return false;
		}
		else {	
			return true;
		}
	}
	public void DoUpdate() {
		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		final String source=mainDir+"/HMS/updates/HMS.jar";
		System.out.println("SERVER HMS DIR:"+source);
		String dir = new String("" + System.getProperty("user.dir"));
		final File desti=new File(dir,"HMS.jar");

		System.out.println("LOCAL HMS DIR:"+desti);


		String fileName = "data.mdi";

		String content = null;
		File file = new File(fileName); // For example, foo.txt
		FileReader reader = null;
		try {
			reader = new FileReader(file);
			char[] chars = new char[(int) file.length()];
			reader.read(chars);
			content = new String(chars);
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(reader != null){
				try {
					reader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		System.out.print(version+"--------------"+newVersion);
		content=content.replace(version, newVersion);
		System.out.println("content after:"+content);
		try {
			FileWriter writer = new FileWriter(fileName);
			writer.write(content);
			writer.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	
		System.out.print(version+"---------g-----"+newVersion);
		new Task_StringUpdate(source,desti).execute();
	   
	}
	
	 class Task_StringUpdate extends SwingWorker<Void, String> {

        JLabel jlabel;
        public String url;
		public File target;		
        
        public Task_StringUpdate(String url,File target) {
           this.url=url;
           this.target=target;
           progressBar.setStringPainted(true);
           
        }

        @Override
        public void process(List<String> chunks) {
        	  int i = Integer.parseInt(chunks.get(chunks.size()-1));
        	  progressBar.setValue(i);
        }

        @Override
        public Void doInBackground() throws Exception {

        	SmbFile remoteFile = new SmbFile(url+"");
    		OutputStream os = new FileOutputStream(target);
    		InputStream is = remoteFile.getInputStream();
    		System.out.print("total lenght" + remoteFile.getContentLength());
    		long completeFileSize=remoteFile.getContentLength();
    		TotalFileSizeLable.setText(GetFileSize(completeFileSize));
    		long read = 0;
    		int count = 0;
    		long downloadedFileSize=0;
    		int bufferSize = 5096;    		
    		
    		byte[] b = new byte[bufferSize];
    		while ((count = is.read(b)) != -1) {
    			os.write(b, 0, count);

    			read += count;
    			downloadedFileSize+= count;
    			RemainingFileSizeLable.setText(GetFileSize(downloadedFileSize));
    			final int currentProgress = (int) ((((double)downloadedFileSize) / ((double)completeFileSize)) * 100d);
    			publish(currentProgress+"");
    			
    		}
    		os.close();
    		is.close();

            return null;
        }

        @Override
        public void done() {
          
                try {
					get();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                System.exit(0);
//                runprogram();
        }
    }
	
	public void readData() {
		FileInputStream fis = null;
		int counter = 0;
		try {
			File file = new File("data.mdi");
			fis = new FileInputStream(file);
			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fis);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			// Read File Line By Line
			while ((strLine = br.readLine()) != null) {

				data[counter] = strLine;
				counter++;
				System.out.println(strLine);
			}
			String mainDir1[] = new String[22];
			int j = 0;
			for (String retval : data[0].split("@")) {
				mainDir1[j] = retval;
				j++;
			}
			mainDir = mainDir1[1];
			System.out.println("Server dir:"+mainDir);
			if(counter==1)
			{
				return;
			}

			String verion[] = new String[22];
			int i = 0;
			for (String retval : data[1].split(":")) {
				verion[i] = retval;
				i++;
			}
			version=verion[1].trim();
			System.out.println("Current Version:"+version);
			in.close();
		} catch (Exception ex) {
			System.out.print(""+ex);
		} finally {
			try {
				if (null != fis)
					fis.close();
			} catch (IOException ex) {
			}
		}

	}
	public static void runprogram() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
		    public void run() {
		    	if (System.getProperty("java.version").toString().equals("null")) {
					ProcessBuilder pb = new ProcessBuilder(
							new File(System.getProperty("user.dir"), "jre/bin/java.exe")
									.toString(), "-jar", new File(System
									.getProperty("user.dir"), "HMS.jar")
									.toString());
					pb.directory(new File("" + System.getProperty("user.dir")));
					try {
						Process p = pb.start();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					
					ProcessBuilder pb = new ProcessBuilder("java", "-jar",
							new File("HMS.jar")
									.toString());
					System.out.println("java -jar "+new File(System.getProperty("user.dir")+"/HMS.jar")
					.toString());
					pb.directory(new File("" + System.getProperty("user.dir")));
					try {
						Process p = pb.start();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
		    }
		});
		System.exit(0);
	}

	public static String GetFileSize(long size) {

	    DecimalFormat df = new DecimalFormat("0.00");

	    float sizeKb = 1024.0f;
	    float sizeMb = sizeKb * sizeKb;
	    float sizeGb = sizeMb * sizeKb;
	    float sizeTerra = sizeGb * sizeKb;


	    if(size < sizeMb)
	        return df.format(size / sizeKb)+ " Kb";
	    else if(size < sizeGb)
	        return df.format(size / sizeMb) + " Mb";
	    else if(size < sizeTerra)
	        return df.format(size / sizeGb) + " Gb";

	    return "";
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
	public static boolean isWindows() {

		return (OS.indexOf("win") >= 0);

	}

	public static boolean isMac() {

		return (OS.indexOf("mac") >= 0);

	}

	public static boolean isUnix() {

		return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS
				.indexOf("aix") > 0);

	}

	public static boolean isSolaris() {

		return (OS.indexOf("sunos") >= 0);

	}
}
