/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.view.mainpanel;

import devs.mrp.turkeydesktop.view.PanelHandler;
import javax.swing.JFrame;

/**
 *
 * @author miguel
 */
public class FMainPanel {
    public static FeedbackerPanelWithLogger getMainPanel() {
        return new MainPanel();
    }
    
    public static PanelHandler getMainHandler(JFrame frame) {
        return new MainHandler(frame, null);
    }
}
