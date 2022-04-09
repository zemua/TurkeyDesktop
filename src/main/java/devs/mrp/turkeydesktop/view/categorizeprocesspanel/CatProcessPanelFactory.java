/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.view.categorizeprocesspanel;

import devs.mrp.turkeydesktop.view.PanelHandler;
import devs.mrp.turkeydesktop.view.mainpanel.FeedbackerPanelWithFetcher;
import java.awt.AWTEvent;
import javax.swing.JFrame;

/**
 *
 * @author miguel
 */
public class CatProcessPanelFactory {
    public static FeedbackerPanelWithFetcher<CatProcessEnum, AWTEvent> getPanel() {
        return new CatProcessPanel();
    }
    public static PanelHandler<CatProcessEnum, AWTEvent, FeedbackerPanelWithFetcher<CatProcessEnum, AWTEvent>> getHandler(JFrame frame, PanelHandler<?, ?, ?> caller) {
        return new CatProcessHandler(frame, caller);
    }
}
