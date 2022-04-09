/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.view.mainpanel;

import devs.mrp.turkeydesktop.view.PanelHandler;
import java.awt.AWTEvent;
import javax.swing.JFrame;

/**
 *
 * @author miguel
 */
public class MainPanelFactory {
    public static FeedbackerPanelWithFetcher<MainEnum, AWTEvent> getMainPanel() {
        return new MainPanel();
    }
    
    public static PanelHandler<MainEnum, AWTEvent, FeedbackerPanelWithFetcher<MainEnum, AWTEvent>> getMainHandler(JFrame frame) {
        return new MainHandler(frame, null);
    }
}
