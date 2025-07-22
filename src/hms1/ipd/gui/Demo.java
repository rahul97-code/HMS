package hms1.ipd.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.web.HTMLEditor;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class Demo extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Demo frame = new Demo();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Demo() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		
		 final VBox root = new VBox();        
	        root.setPadding(new Insets(8, 8, 8, 8));
	        root.setSpacing(5);
	        root.setAlignment(Pos.BOTTOM_LEFT);
	        
	        final HTMLEditor htmlEditor = new HTMLEditor();
	        htmlEditor.setPrefHeight(370);
	 
	        root.getChildren().add(htmlEditor);  
	       
		setContentPane(contentPane);
	}

}
