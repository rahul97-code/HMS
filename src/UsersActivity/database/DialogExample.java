package UsersActivity.database;

import javax.swing.*;
import java.awt.event.*;

public class DialogExample {

    public static void main(String[] args) {
        // Create the dialog
        JDialog dialog = new JDialog();
        dialog.setTitle("Dialog");
        dialog.setSize(200, 150);
        dialog.setLocationRelativeTo(null); // Center the dialog on the screen

        // Add a WindowListener to detect when the dialog is closed
        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                handleDialogClose();
            }

            @Override
            public void windowClosed(WindowEvent e) {
                System.out.println("Dialog has been closed!");
            }
        });

        // Add KeyListener to handle Escape key
        dialog.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    handleDialogClose();
                }
            }
        });

        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        // Add some content to the dialog
        dialog.add(new JLabel("Press Esc or close the window."), SwingConstants.CENTER);
        
        // Show the dialog
        dialog.setVisible(true);
    }

    private static void handleDialogClose() {
        // Handle the dialog closing logic here
        System.out.println("Dialog is closing!");
        // Any additional cleanup or processing can go here
    }
}
