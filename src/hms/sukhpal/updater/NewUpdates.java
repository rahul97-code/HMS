package hms.sukhpal.updater;


import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;

import javax.swing.AbstractListModel;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import hms.main.Developing_environment;

import javax.swing.UIManager;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileOutputStream;

import javax.swing.JProgressBar;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.ImageIcon;
import javax.swing.JTextArea;

public class NewUpdates extends JDialog {

	private static String UserName = "";
	private static String Password = "";
	private JPanel contentPane;
	private JTextField currentVersionTB;
	private JTextField newVersionTB;
	public JFileChooser jfc = new JFileChooser();
	public File[] file;
	public File[] filepath;
	Vector files = new Vector();
	private JList list;
	String mainDir = "";
	String dest = "";
	private JProgressBar progressBar;
	private JLabel RemainingFileSizeLable;
	private JLabel TotalFileSizeLable;
	private JTextArea textArea;
	private JLabel RemainingFilesName;

	public static void main(String[] args) {
		new NewUpdates().setVisible(true);
	}

	/**
	 * Create the frame.
	 */
	public NewUpdates() {
		setResizable(false);
		setTitle("New Update");
		setBounds(400, 150, 482, 461);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		readFile();
		JLabel lblHms = new JLabel("UPDATE HMS SOFTWARE");
		lblHms.setHorizontalAlignment(SwingConstants.CENTER);
		lblHms.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblHms.setBounds(116, 10, 248, 33);
		contentPane.add(lblHms);

		JLabel lblCurrentVersion = new JLabel("Current Version :");
		lblCurrentVersion.setFont(new Font("Dialog", Font.ITALIC, 13));
		lblCurrentVersion.setBounds(32, 55, 135, 24);
		contentPane.add(lblCurrentVersion);

		UpdateCheckerDBConnection updateCheckerDBConnection=new UpdateCheckerDBConnection();
		currentVersionTB = new JTextField(updateCheckerDBConnection.retrieveVersionNo());
		currentVersionTB.setEditable(false);
		currentVersionTB.setFont(new Font("Tahoma", Font.BOLD, 13));
		currentVersionTB.setBounds(189, 55, 248, 24);
		contentPane.add(currentVersionTB);
		currentVersionTB.setColumns(10);

		updateCheckerDBConnection.closeConnection();

		JLabel lblNewVersion = new JLabel("New Version :");
		lblNewVersion.setFont(new Font("Dialog", Font.ITALIC, 13));
		lblNewVersion.setBounds(32, 90, 135, 24);
		contentPane.add(lblNewVersion);

		newVersionTB = new JTextField();
		newVersionTB.setFont(new Font("Tahoma", Font.BOLD, 13)); 
		newVersionTB.setColumns(10);
		newVersionTB.setBounds(189, 90, 248, 24);
		contentPane.add(newVersionTB);

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "File List", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(36, 210, 220, 91);
		contentPane.add(panel);
		panel.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 15, 200, 66);
		panel.add(scrollPane);

		list = new JList();
		list.setFont(new Font("Tahoma", Font.PLAIN, 13));
		scrollPane.setViewportView(list);
		list.setModel(new AbstractListModel() {
			String[] values = new String[] {};

			@Override
			public int getSize() {
				return values.length;
			}

			@Override
			public Object getElementAt(int index) {
				return values[index];
			}
		});

		JButton btnNewButton = new JButton("Browse");
		btnNewButton.setIcon(new ImageIcon(NewUpdates.class.getResource("/icons/OPEN.GIF")));
		btnNewButton.setFont(new Font("Dialog", Font.ITALIC, 13));
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				file = getMultipleFile();
				filepath = file;
				files.clear();
				for (int i = 0; i < file.length; i++) {
					files.add(file[i].getName().toString()); 
				}
				list.setListData(files);
				RemainingFilesName.setText(files.get(0)+"");
			}
		});
		btnNewButton.setBounds(268, 221, 169, 33);
		contentPane.add(btnNewButton);

		JButton btnUpdate = new JButton("Upload Update");
		btnUpdate.setIcon(new ImageIcon(NewUpdates.class.getResource("/icons/LOGGOFF.PNG")));
		btnUpdate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(newVersionTB.getText().toString().equals(""))
				{
					JOptionPane.showMessageDialog(null,
							"Please input version no.", "Data Updated",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				if(newVersionTB.getText().toString().equals(currentVersionTB.getText().toString()))
				{
					JOptionPane.showMessageDialog(null,
							"Please Input New Version No.", "Data Updated",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (files.size() > 0) {
					
					dest = makeDirectory()+"/";
					new Task_StringUpdate(filepath,dest).execute();

				}
				else {
					JOptionPane.showMessageDialog(null,
							"Please select any file", "Data Updated",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnUpdate.setFont(new Font("Dialog", Font.ITALIC, 13));
		btnUpdate.setBounds(268, 266, 169, 33);
		contentPane.add(btnUpdate);

		RemainingFileSizeLable = new JLabel("");
		RemainingFileSizeLable.setFont(new Font("Dialog", Font.ITALIC, 12));
		RemainingFileSizeLable.setBounds(302, 325, 121, 15);
		contentPane.add(RemainingFileSizeLable);

		JLabel lblFileSize_1 = new JLabel("Uploaded : ");
		lblFileSize_1.setFont(new Font("Dialog", Font.ITALIC, 12));
		lblFileSize_1.setBounds(219, 325, 88, 15);
		contentPane.add(lblFileSize_1);

		JLabel lblFileSize = new JLabel("File Size : ");
		lblFileSize.setFont(new Font("Dialog", Font.ITALIC, 12));
		lblFileSize.setBounds(47, 325, 79, 15);
		contentPane.add(lblFileSize);

		TotalFileSizeLable = new JLabel("");
		TotalFileSizeLable.setFont(new Font("Dialog", Font.ITALIC, 12));
		TotalFileSizeLable.setBounds(116, 325, 104, 15);
		contentPane.add(TotalFileSizeLable);

		progressBar = new JProgressBar();
		progressBar.setToolTipText("HMS is updating so please wait");
		progressBar.setBounds(47, 362, 374, 29);
		contentPane.add(progressBar);

		JLabel label = new JLabel("");
		label.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Uploading", TitledBorder.RIGHT, TitledBorder.TOP, null, new Color(51, 51, 51)));
		label.setBounds(32, 341, 405, 61);
		contentPane.add(label);

		JLabel label_1 = new JLabel("");
		label_1.setIcon(new ImageIcon(NewUpdates.class.getResource("/icons/updates.png")));
		label_1.setBounds(32, 0, 79, 61);
		contentPane.add(label_1);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(38, 137, 265, 61);
		contentPane.add(scrollPane_1);

		textArea = new JTextArea();
		textArea.setRows(5);
		scrollPane_1.setViewportView(textArea);

		JLabel lblDescription = new JLabel("Description...");
		lblDescription.setFont(new Font("Dialog", Font.ITALIC, 11));
		lblDescription.setBounds(42, 122, 101, 15);
		contentPane.add(lblDescription);
		
		JLabel lblFileSize_2 = new JLabel("File : ");
		lblFileSize_2.setFont(new Font("Dialog", Font.ITALIC, 12));
		lblFileSize_2.setBounds(46, 298, 54, 15);
		contentPane.add(lblFileSize_2);
		
		RemainingFilesName = new JLabel("");
		RemainingFilesName.setFont(new Font("Dialog", Font.ITALIC, 12));
		RemainingFilesName.setBounds(91, 298, 176, 15);
		contentPane.add(RemainingFilesName);
		
		JLabel lblDevelopedByArun = new JLabel("Developed by Arun And Rahul ...");
		lblDevelopedByArun.setFont(new Font("Dialog", Font.ITALIC, 10));
		lblDevelopedByArun.setBounds(21, 407, 235, 15);
		contentPane.add(lblDevelopedByArun);
		
		JButton btnNewButton_1 = new JButton("<html>Prevous<br />Updates</html>");
		btnNewButton_1.setEnabled(false);
		btnNewButton_1.addActionListener(new ActionListener() {
			private SmbFile smbFileTarget;

			public void actionPerformed(ActionEvent e) {
				files.clear();
				dest = makeDirectory()+"/";
				try {
					smbFileTarget = new SmbFile(dest);
					SmbFile[] fileArr=smbFileTarget.listFiles();
					for (int i = 0; i < smbFileTarget.listFiles().length; i++) {
						files.add(fileArr[i].getName().toString());
					}
				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SmbException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} 
				list.setListData(files);
			}
		});
		btnNewButton_1.setIcon(new ImageIcon(NewUpdates.class.getResource("/icons/RELOAD.PNG")));
		btnNewButton_1.setFont(new Font("Dialog", Font.ITALIC, 13));
		btnNewButton_1.setBounds(315, 137, 122, 61);
		contentPane.add(btnNewButton_1);

	}

	public File[] getMultipleFile() {
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"JPG & PNG Images", "jpg", "png");
		// jfc.setFileFilter(filter);
		jfc.setMultiSelectionEnabled(true);
		jfc.setDialogTitle("Open File");
		if (jfc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			return jfc.getSelectedFiles();
		} else {
			return null;
		}
	}
	public String makeDirectory() {
		try {
			SmbFile dir = new SmbFile(mainDir + "/HMS/updates");
			if (!dir.exists())
				dir.mkdirs();
		} catch (SmbException | MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(mainDir);
		return mainDir + "/HMS/updates";
	}
	public void readFile() {
		// The name of the file to open.
		String fileName = "data.mdi";

		// This will reference one line at a time
		String line = null;

		try {
			// FileReader reads text files in the default encoding.
			FileReader fileReader = new FileReader(fileName);

			// Always wrap FileReader in BufferedReader.
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String str = null;
			boolean fetch=true;
			while ((line = bufferedReader.readLine()) != null&&fetch) {
				// System.out.println(line);
				str = line;
				fetch=false;
			}
			String data[] = new String[22];
			int i = 0;
			for (String retval : str.split("@")) {
				data[i] = retval;
				i++;
			}
			mainDir = data[1];
//			System.out.println("dsfdsfdsfsdf"+mainDir);
			// Always close files.
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileName + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + fileName + "'");
		}

	}

	public JTextField getNewVersionTB() {
		return newVersionTB;
	}
	class Task_StringUpdate extends SwingWorker<Void, String> {

		JLabel jlabel;
		public String source;
		public String target;	
		public File[] filepath;

		public Task_StringUpdate(File[] filepath, String dest) {
			// TODO Auto-generated constructor stub
			this.filepath=filepath;
			target=dest;
			
			progressBar.setStringPainted(true);
		}

		@Override
		public void process(List<String> chunks) {
			int i = Integer.parseInt(chunks.get(chunks.size()-1));
			progressBar.setValue(i);
		}

		@Override
		public Void doInBackground() throws Exception {	 
			for(int i=0;i<filepath.length;i++) {
				RemainingFilesName.setText(filepath[i].getName()+""); 
				InputStream fis = new FileInputStream(filepath[i].getPath());
				SmbFileOutputStream smbfos = new SmbFileOutputStream(new SmbFile(target+filepath[i].getName()));
				// writing data
				try {
					long completeFileSize=fis.available();
					TotalFileSizeLable.setText(GetFileSize(completeFileSize));
					final byte[] b  = new byte[16*1024];
					int read = 0;
					int count=0;
					long downloadedFileSize=0;
					while ((count=fis.read(b, 0, b.length)) > 0) {
						smbfos.write(b, 0, count);
						read += count;
						downloadedFileSize+= count;
						RemainingFileSizeLable.setText(GetFileSize(downloadedFileSize));
						final int currentProgress = (int) ((((double)downloadedFileSize) / ((double)completeFileSize)) * 100d);
						publish(currentProgress+"");
					}
				}
				finally {
					fis.close();
					smbfos.close();
				}
				
			}
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
				JOptionPane.showMessageDialog(null,
						"Files Upload successfully ", "Data Updated",
						JOptionPane.INFORMATION_MESSAGE);
				dispose();
				UpdateCheckerDBConnection DB=new UpdateCheckerDBConnection();
				try {
					DB.updateVersionNO(newVersionTB.getText().toString(),textArea.getText());
				} catch (Exception e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				DB.closeConnection();
			
		}
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
}
