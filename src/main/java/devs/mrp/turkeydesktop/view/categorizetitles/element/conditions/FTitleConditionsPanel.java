/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.view.categorizetitles.element.conditions;

import devs.mrp.turkeydesktop.view.PanelHandler;
import devs.mrp.turkeydesktop.view.mainpanel.FeedbackerPanelWithFetcher;
import java.awt.AWTEvent;
import javax.swing.JFrame;

/**
 *
 * @author miguel
 */
public class FTitleConditionsPanel {
    
    public static FeedbackerPanelWithFetcher<TitleConditionsEnum, AWTEvent> getPanel() {
        return new TitleConditionsPanel();
    }
    
    public static PanelHandler<TitleConditionsEnum, AWTEvent, FeedbackerPanelWithFetcher<TitleConditionsEnum, AWTEvent>> getHandler(JFrame frame, PanelHandler<?, ?, ?> caller) {
        return new TitleConditionsHandler(frame, caller);
    }
    
}
