package hms.main;
import hms.opd.gui.OPDBrowser;

import java.awt.Color;
import java.awt.Toolkit;

import javax.swing.DefaultDesktopManager;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

public class internalfram extends JFrame
{
    public static JDesktopPane desktop;

    public internalfram()
    {
        desktop = new JDesktopPane();
        desktop.setDesktopManager( new NoDragDesktopManager() );
        getContentPane().add( desktop );
        desktop.removeAll();
        OPDBrowser newPatient=new OPDBrowser();
        desktop.add( createInternalFrame(1, Color.RED) );
       
    }

    private JInternalFrame createInternalFrame(int number, Color background)
    {
    	OPDBrowser newPatient=new OPDBrowser();
        JInternalFrame internal =
            new JInternalFrame("Frame", false, true, false, true);
      //  internal.setContentPane(newPatient.getContentPane());
        internal.setBackground( background );
        internal.setVisible( true );
        ImageIcon icon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(OPDBrowser.class.getResource("/icons/rotaryLogo.png")));
        internal.setFrameIcon(icon);
        internal.pack();
        internal.setBounds(100, 100, 950, 524);
        internal.toFront();
        return internal;
    }

    public static void main(String args[])
    {
    	try {
		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if ("Nimbus".equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
		    }
		} catch (Exception e) {
		    // If Nimbus is not available, you can set the GUI to another look and feel.
		}
    	
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
    	
    
    	
        internalfram frame = new internalfram();
        frame.setDefaultCloseOperation( EXIT_ON_CLOSE );
        frame.setSize(1000, 800);
        frame.setLocationRelativeTo( null );
        frame.setVisible(true);

    }

    class NoDragDesktopManager extends DefaultDesktopManager
    {
        @Override
		public void beginDraggingFrame(JComponent f)
        {
            if (!"fixed".equals(f.getClientProperty("dragMode")))
                super.beginDraggingFrame(f);
        }

        @Override
		public void dragFrame(JComponent f, int newX, int newY)
        {
            if (!"fixed".equals(f.getClientProperty("dragMode")))
                super.dragFrame(f, newX, newY);
        }

        @Override
		public void endDraggingFrame(JComponent f)
        {
            if (!"fixed".equals(f.getClientProperty("dragMode")))
                super.endDraggingFrame(f);
        }
    }
}