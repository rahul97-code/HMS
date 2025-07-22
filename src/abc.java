import hms.main.MarqueeLabel;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;


public class abc {

    private void display() {
        JFrame f = new JFrame("MarqueeTest");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        String s = "Tomorrow, and tomorrow, and tomorrow, दीपावली का त्योहार पांच दिनों तक चलने वाला सबसे बड़ा पर्व होता है। दशहरे के बाद से ही घरों में दीपावली की तैयारियां शुरू हो जाती है, जो व्यापक स्तर पर की जाती है। इस दिन भगवान श्रीराम, माता सीता और भ्राता लक्ष्मण के साथ चौदह वर्ष का वनवास पूर्ण कर अयोध्या लौटे थे। इसके अलावा दीपावली को लेकर कुछ और भी पौराणिक कथाएं प्रचलित हैं। पढ़ि‍ए निबंध - "
        		 + "It is a tale told by an idiot, full of दीपावली का त्योहार पांच दिनों तक चलने वाला सबसे बड़ा पर्व होता है। दशहरे के बाद से ही घरों में दीपावली की तैयारियां शुरू हो जाती है, जो व्यापक स्तर पर की जाती है। इस दिन भगवान श्रीराम, माता सीता और भ्राता लक्ष्मण के साथ चौदह वर्ष का वनवास पूर्ण कर अयोध्या लौटे थे। इसके अलावा दीपावली को लेकर कुछ और भी पौराणिक कथाएं प्रचलित हैं। पढ़ि‍ए निबंध - "
        		
        		 
        		 
        + "It is a tale told by an idiot, full of दीपावली का त्योहार पांच दिनों तक चलने वाला सबसे बड़ा पर्व होता है। दशहरे के बाद से ही घरों में दीपावली की तैयारियां शुरू हो जाती है, जो व्यापक स्तर पर की जाती है। इस दिन भगवान श्रीराम, माता सीता और भ्राता लक्ष्मण के साथ चौदह वर्ष का वनवास पूर्ण कर अयोध्या लौटे थे। इसके अलावा दीपावली को लेकर कुछ और भी पौराणिक कथाएं प्रचलित हैं। पढ़ि‍ए निबंध - "
        + "sound and fury signifying nothing.";

        JLabel lbl_addBatch =  new MarqueeLabel(s,
				MarqueeLabel.RIGHT_TO_LEFT, 10);
     	
		lbl_addBatch.setFont(new Font("mangal", Font.BOLD, 50));
       
        JPanel pnl_addBatch = new JPanel();
        pnl_addBatch.add(lbl_addBatch);

//        Marquee marquee = new Marquee(lbl_addBatch, s, 32);
//        marquee.start();

        f.add(pnl_addBatch);
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
         
     
    }

    public static void main(String[] args) {
    	   File f1 = new File("Design.pdf");
    	   try {
			Runtime.getRuntime().exec("chmod 777 "+f1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	   System.out.println("Is Execute allow : " + f1.canExecute());
    	   System.out.println("Is Write allow : " + f1.canWrite());
    	   System.out.println("Is Read allow : " + f1.canRead());
           f1.setExecutable(false);
           f1.setWritable(false);
           f1.setReadable(false);
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                new abc().display();
            }
        });
    }
}

class Marquee implements ActionListener {

    private static final int RATE = 10;
    private final Timer timer = new Timer(1000 / RATE, this);
    private final JLabel label;
    private final String s;
    private final int n;
    private int index;

    public Marquee(JLabel label, String s, int n) {
        if (s == null || n < 1) {
            throw new IllegalArgumentException("Null string or n < 1");
        }
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            sb.append(' ');
        }
        this.label = label;
        this.s = sb + s + sb;
        this.n = n;
        label.setFont(new Font("Serif", Font.ITALIC, 36));
        label.setText(sb.toString());
    }

    public void start() {
        timer.start();
    }

    public void stop() {
        timer.stop();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        index++;
        if (index > s.length() - n) {
            index = 0;
        }
        label.setText(s.substring(index, index + n));
    }
    
    public class MarqueeLabel1 extends JLabel {  
    	  public int LEFT_TO_RIGHT = 1;  
    	  public int RIGHT_TO_LEFT = 2;  
    	  String text;  
    	  int Option;  
    	  int Speed;  
    	  public MarqueeLabel1(String text, int Option, int Speed) {  
    	    this.Option = Option;  
    	    this.Speed = Speed;  
    	    this.setText(text);  
    	  }  
    	  @Override  
    	  protected void paintComponent(Graphics g) {  
    	    if (Option == LEFT_TO_RIGHT) {  
    	      g.translate((int) ((System.currentTimeMillis() / Speed) % (getWidth() * 2) - getWidth()), 0);  
    	    } else if (Option == RIGHT_TO_LEFT) {  
    	      g.translate((int) (getWidth() - (System.currentTimeMillis() / Speed) % (getWidth() * 2)), 0);  
    	    }  
    	    super.paintComponent(g);  
    	    repaint(5);  
    	  }  
    	}  
}