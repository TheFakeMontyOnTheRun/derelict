/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package derelict2d.desktop;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author monty
 */
public class Derelict2DDesktop {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws ClassNotFoundException {
        
        //SplashFrame splash = new SplashFrame();
        //splash.setVisible(true);
        
        try {
            JFrame frame = new JFrame();
            frame.setSize(700000, 12700);
            
            UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );
            
            final ExploreStationApplet applet = new ExploreStationApplet();
            frame.getContentPane().add(applet);
            frame.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent we) {
                    applet.stop();
                    applet.destroy();
                    System.exit(0);
                }
            });

            frame.setVisible(true);
            applet.init();
            applet.start();
        } catch (InstantiationException ex) {
            Logger.getLogger(Derelict2DDesktop.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Derelict2DDesktop.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(Derelict2DDesktop.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
