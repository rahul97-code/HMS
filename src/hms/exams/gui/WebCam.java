package hms.exams.gui;

import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamException;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;

import hms.patient.slippdf.OpenFile;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.imageio.ImageIO;
import javax.swing.AbstractListModel;
import javax.swing.ImageIcon;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.awt.event.ActionEvent;
import javax.swing.border.MatteBorder;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.UIManager;
import java.awt.event.MouseEvent;

public class WebCam extends JDialog{
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new WebCam().setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	private double scale = 1.0;
	Vector files = new Vector();
	List<BufferedImage> images = new ArrayList<>();
	private static Webcam webcam;
	private WebcamPanel liveCamPanel;
	int imagesCount=0;
	private JList list;

	BufferedImage image=null;
	private JPanel imagePanel;

	/**
	 * Create the application.
	 * @throws Exception 
	 */
	public WebCam() throws Exception {

		deleteDirectory(new File("TempImages"));
		makeDirectory("TempImages");

		setBounds(100, 100, 1022, 515);
		getContentPane().setLayout(null);

		final JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(533, 20, 466, 365);
		getContentPane().add(scrollPane);

		imagePanel = new JPanel();
		scrollPane.setViewportView(imagePanel);
		initializeWebcam() ;
		liveCamPanel = new WebcamPanel(webcam);
		liveCamPanel.setBounds(28, 12, 428, 454);
		liveCamPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		getContentPane().add(liveCamPanel);

		JButton btnNewButton = new JButton("");
		btnNewButton.setBounds(466, 77, 55, 54);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				image = webcam.getImage();
				if (image == null) {
					JOptionPane.showMessageDialog(null, "Failed to capture image. The webcam might not be functioning properly.");
					return;
				}
				scrollPane.setViewportView(new ImageZoom(image));
			}
		});
		btnNewButton.setIcon(new ImageIcon(WebCam.class.getResource("/icons/shoot_button.png")));
		getContentPane().add(btnNewButton);

		JButton btnUpload = new JButton("Upload");
		btnUpload.setBounds(857, 397, 142, 27);
		btnUpload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (images.size()==0) {
					JOptionPane.showMessageDialog(null, "Please capture images!");
					return;
				}
				saveImages(images, "TempImages");
				createPDFFromImages("TempImages", "TempImages/PreApproval.pdf");
				releaseResources();
			}
		});
		btnUpload.setForeground(new Color(0, 0, 205));
		btnUpload.setBackground(new Color(176, 224, 230));
		btnUpload.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 13));
		getContentPane().add(btnUpload);

		JButton btnCancel = new JButton("Cancel");
		btnCancel.setBounds(857, 434, 142, 27);
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				releaseResources();
			}
		});
		btnCancel.setForeground(new Color(255, 0, 0));
		btnCancel.setBackground(new Color(192, 192, 192));
		btnCancel.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 13));
		getContentPane().add(btnCancel);

		JPanel panel = new JPanel();
		panel.setBounds(622, 389, 220, 79);
		panel.setLayout(null);
		panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "File List", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		getContentPane().add(panel);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(10, 15, 200, 52);
		panel.add(scrollPane_1);
		list = new JList();
		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(!list.isSelectionEmpty() && e.getClickCount()==2) {
					scrollPane.setViewportView(new ImageZoom(images.get(list.getSelectedIndex())));
				}
			}
		});
		list.setFont(new Font("Tahoma", Font.PLAIN, 13));
		scrollPane_1.setViewportView(list);
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


		imagePanel.addMouseWheelListener(new MouseAdapter() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				if (e.getWheelRotation() < 0) {
					scale *= 1.1;
				} else {
					scale /= 1.1;
				}
				repaint();
			}
		});
		JButton btnNewButton_1_1 = new JButton("Add");
		btnNewButton_1_1.setBounds(476, 401, 122, 27);
		btnNewButton_1_1.setFont(new Font("Dialog", Font.ITALIC, 12));
		btnNewButton_1_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				list.removeAll();
				if(image!=null)
				{images.add(image);
				files.add("image_"+imagesCount);
				list.setListData(files);
				imagesCount++;
				}
			}
		});
		btnNewButton_1_1.setIcon(new ImageIcon(WebCam.class.getResource("/icons/plus_button.png")));
		getContentPane().add(btnNewButton_1_1);

		JButton btnNewButton_1_1_1 = new JButton("Remove");
		btnNewButton_1_1_1.setBounds(476, 436, 122, 27);
		btnNewButton_1_1_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				list.removeAll();
				int i=list.getSelectedIndex();
				images.remove(i);
				files.remove(i);
				list.setListData(files);
			}
		});
		btnNewButton_1_1_1.setFont(new Font("Dialog", Font.ITALIC, 12));
		btnNewButton_1_1_1.setIcon(new ImageIcon(WebCam.class.getResource("/icons/CANCEL.PNG")));
		getContentPane().add(btnNewButton_1_1_1);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				releaseResources();
			}
		});

	}
	private static void saveImages(List<BufferedImage> images, String folderPath) {
		int i = 1;
		for (BufferedImage image : images) {
			File outputFile = new File(folderPath, "image" + i + ".png");
			try {
				ImageIO.write(image, "png", outputFile);
				System.out.println("Saved image: " + outputFile.getAbsolutePath());
			} catch (IOException e) {
				System.out.println("Error saving image: " + e.getMessage());
			}
			i++;
		}
	}

	private static void createPDFFromImages(String folderPath, String pdfPath) {
		Document document = new Document();
		try {
			PdfWriter.getInstance(document, new FileOutputStream(pdfPath));
			document.open();

			File folder = new File(folderPath);
			File[] files = folder.listFiles(new FilenameFilter() {
	            @Override
	            public boolean accept(File dir, String name) {
	                return name.endsWith(".png");
	            }
	        });

			if (files != null) {
				for (File file : files) {
					Image img = Image.getInstance(file.getAbsolutePath());
					float imgWidth = img.getWidth();
					float imgHeight = img.getHeight();
					float pageWidth = imgWidth + 36f + 36f;
					float pageHeight = imgHeight + 36f + 36f;
					document.setPageSize(new Rectangle(pageWidth, pageHeight));
					document.newPage();
					document.setMargins(36f, 36f, 36f, 36f);
					img.setAbsolutePosition(36f, 36f); 
					img.scaleToFit(imgWidth, imgHeight);
					document.add(img);
					System.out.println("Added image to PDF: " + file.getAbsolutePath());
				}
			}
			document.close();
			System.out.println("PDF created successfully.");
			new OpenFile(pdfPath);
		} catch (DocumentException | IOException e) {
			System.out.println("Error creating PDF: " + e.getMessage());
		}
	}

	private static void makeDirectory(String folderPath) {
		File folder = new File(folderPath);
		if (!folder.exists()) {
			folder.mkdirs();
		} 
	}
	public static boolean deleteDirectory(File dir) {
		if (dir.isDirectory()) {
			File[] files = dir.listFiles();
			if (files != null) {
				for (File file : files) {
					if (file.isDirectory()) {
						deleteDirectory(file);
					} else {
						if (!file.delete()) {
							System.out.println("Failed to delete file: " + file.getAbsolutePath());
							return false;
						}
					}
				}
			}
		}
		return dir.delete();
	}
	private static void initializeWebcam() throws Exception {
		try {
			webcam = Webcam.getDefault();
			if (webcam == null) {
				throw new WebcamException("No webcam detected.");
			}

			// Get all available resolutions
			Dimension[] resolutions = webcam.getViewSizes();
			if (resolutions.length == 0) {
				throw new WebcamException("No resolutions available.");
			}

			// Find the highest resolution
			Dimension highestResolution = resolutions[0];
			for (Dimension resolution : resolutions) {
				if (resolution.width > highestResolution.width || 
						(resolution.width == highestResolution.width && resolution.height > highestResolution.height)) {
					highestResolution = resolution;
				}
			}

			// Set to the highest resolution
			webcam.setViewSize(highestResolution);
//			webcam.setViewSize(WebcamResolution.FHD.getSize());
			webcam.open();
			System.out.println("Webcam initialized successfully with resolution: " + highestResolution);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			throw new Exception("Webcam initialization failed: " + e.getMessage(), e);
		}
	}
	private void releaseResources() {
		if (webcam!=null && webcam.isOpen()) {
			webcam.close();
			System.out.println("Webcam closed.");
		}
		if (this != null) {
			dispose();
		}
	}
}
