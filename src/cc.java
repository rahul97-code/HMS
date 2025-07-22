import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JTextField;

public class cc extends JTextField {
    private JComponent nextComponent;


    public cc() {
        addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyReleased(KeyEvent e) {
                // Check for the four characters. 
                if (getText().length() >= 4) {
                    nextComponent.requestFocus();
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {}
        });
    }


    public static void main(String[] args) {

        JFrame frame = new JFrame("Debug Frame");
        frame.setSize(400, 80);
        frame.setLayout(new GridLayout(1,5));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        cc box1 = new cc();
        cc box2 = new cc();
        cc box3 = new cc();
        cc box4 = new cc();
        JButton button1 = new JButton("Done");

        box1.setNextComponent(box2);
        box2.setNextComponent(box3);
        box3.setNextComponent(box4);
        box4.setNextComponent(button1);

        frame.add(box1);
        frame.add(box2);
        frame.add(box3);
        frame.add(box4);
        frame.add(button1);

        frame.setVisible(true); 
    }

    /**
     * @param nextComponent the nextComponent to set
     */
    public void setNextComponent(JComponent nextComponent) {
        this.nextComponent = nextComponent;
    }

    /**
     * @return the nextComponent
     */
    public JComponent getNextComponent() {
        return nextComponent;
    }
}