package hms.exams.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageZoom extends JPanel {
    private BufferedImage image;
    private double scale = 1.0;

    public ImageZoom(BufferedImage image) {
       this.image=image;

        addMouseWheelListener(new MouseAdapter() {
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
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        int imageWidth = (int) (image.getWidth() * scale);
        int imageHeight = (int) (image.getHeight() * scale);

        // Calculate the x and y coordinates to center the image
        int x = (getWidth() - imageWidth) / 2;
        int y = (getHeight() - imageHeight) / 2;

        // Translate the Graphics2D context to the center before scaling
        g2d.translate(x, y);
        g2d.scale(scale, scale);

        // Draw the image at the translated (0, 0) position
        g2d.drawImage(image, 0, 0, this);
    }



}


