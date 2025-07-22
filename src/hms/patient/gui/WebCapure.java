package hms.patient.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.media.Buffer;
import javax.media.CaptureDeviceInfo;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.Player;
import javax.media.control.FormatControl;
import javax.media.control.FrameGrabbingControl;
import javax.media.format.VideoFormat;
import javax.media.util.BufferToImage;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Window.Type;
import javax.swing.JSeparator;

import org.imgscalr.Scalr;

import java.awt.Color;
import java.awt.Dialog.ModalExclusionType;

public class WebCapure extends JDialog {

	private JPanel contentPane;
	private JPanel panel;
	CaptureDeviceInfo cam;
	MediaLocator mi;
	Player player;
	FormatControl formatControl;
	Component videoScreen;
	private JButton btnCancel;

	NewPatient patient=null;
	EditPatient editPatient=null;
	private JButton btnNewButton;

	/**
	 * Create the frame.
	 */
	@SuppressWarnings("restriction")
	public WebCapure() {
		setUndecorated(true);
		setBounds(320, 100, 389, 333);
		setModal(true);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		panel = new JPanel();
		panel.setBounds(35, 11, 320, 240);
		contentPane.add(panel);
		btnNewButton = new JButton("Take Picture");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				try {

					Thread.sleep(100);// wait 10 seconds before capturing
										// photo
				} catch (InterruptedException ex) {

				}

				FrameGrabbingControl fgc = (FrameGrabbingControl) player
						.getControl("javax.media.control.FrameGrabbingControl");

				Buffer buf = fgc.grabFrame();// grab the current frame on
												// video screen

				BufferToImage btoi = new BufferToImage((VideoFormat) buf
						.getFormat());

				Image img = btoi.createImage(buf);
				File path=new File("Patient_Images");
				if(!path.exists())
				{
					path.mkdir();
				}
				saveImagetoFile(img, "Patient_Images/patient.jpg");
				
				try {
					BufferedImage originalImgage = ImageIO.read(new File("Patient_Images/patient.jpg"));
					
					
					originalImgage=resizeWithScalr(originalImgage,640,480);
					
					System.out.println("Original Image Dimension: "+originalImgage.getWidth()+"x"+originalImgage.getHeight());

					
					BufferedImage SubImgage = originalImgage.getSubimage(70, 20, 500, 400);
					System.out.println("Cropped Image Dimension: "+SubImgage.getWidth()+"x"+SubImgage.getHeight());

					File outputfile = new File("Patient_Images/patient.jpg");
					ImageIO.write(SubImgage, "jpg", outputfile);

					System.out.println("Image cropped successfully: "+outputfile.getPath());

				} catch (IOException e) {
					e.printStackTrace();
				}
				
				
				if(patient!=null)
				{
					patient.setImageIcon("Patient_Images/patient.jpg");
				}
				else {
					editPatient.setImageIcon("Patient_Images/patient.jpg");
				}
				
				mi = null;
				player.stop();
				player.close();
				dispose();
			}
		});
		btnNewButton.setBounds(49, 262, 106, 23);
		contentPane.add(btnNewButton);
		btnNewButton.setEnabled(false);
		try {
			String mediaFile = "vfw:Micrsoft WDM Image Capture (Win32):0";

			mi = new MediaLocator(mediaFile);
			player = Manager.createRealizedPlayer(mi);

			player.start();
			// create video screen to display webcam preview
			videoScreen = player.getVisualComponent();
			videoScreen.setSize(panel.getSize());

			panel.removeAll();
			panel.add(videoScreen, BorderLayout.CENTER);
			panel.setLayout(null);
			btnNewButton.setEnabled(true);
		} catch (Exception e) {
			// TODO: handle exception
		}
		

		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (player!=null) {
					player.stop();
					mi = null;
					player.close();
					
				}
				dispose();
			}
		});
		btnCancel.setBounds(240, 262, 103, 23);
		contentPane.add(btnCancel);
		panel.repaint();
		panel.revalidate();

	}
	public static BufferedImage resizeWithScalr(BufferedImage originalImage, int width, int height) {
	    return Scalr.resize(
		originalImage, 
		Scalr.Method.QUALITY, 
		Scalr.Mode.FIT_EXACT,
		width, 
		height, 
		Scalr.OP_ANTIALIAS); 
	}
	private void saveImagetoFile(Image img, String string) {

		int w = img.getWidth(null);
		int h = img.getHeight(null);
		BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = bi.createGraphics();

		g2.drawImage(img, 0, 0, null);

		g2.dispose();

		String fileType = string.substring(string.indexOf('.') + 1);
		try {
			ImageIO.write(bi, fileType, new File(string));
		} catch (IOException ex) {

		}
	}

	public void newPatient(NewPatient newPatient) {
		patient = newPatient;
	}

	public void newPatient(EditPatient editP) {
		editPatient = editP;
	}

	
}
