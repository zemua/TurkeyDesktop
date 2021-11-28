/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.view.times;

import devs.mrp.turkeydesktop.view.PanelHandler;
import devs.mrp.turkeydesktop.view.mainpanel.FeedbackerPanelWithFetcher;
import java.awt.AWTEvent;
import javax.swing.JFrame;

/**
 *
 * @author miguel
 */
public class FTimesPanel {
    
    public static FeedbackerPanelWithFetcher<TimesEnum, AWTEvent> getPanel() {
        return new TimesPanel();
    }
    
    public static PanelHandler<TimesEnum, AWTEvent, FeedbackerPanelWithFetcher<TimesEnum, AWTEvent>> getHandler(JFrame frame, PanelHandler<?, ?, ?> caller) {
        return new TimesHandler(frame, caller);
    }
    
}
