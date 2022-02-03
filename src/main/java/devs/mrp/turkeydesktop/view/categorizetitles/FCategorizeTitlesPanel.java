/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.view.categorizetitles;

import devs.mrp.turkeydesktop.view.PanelHandler;
import devs.mrp.turkeydesktop.view.mainpanel.FeedbackerPanelWithFetcher;
import java.awt.AWTEvent;
import javax.swing.JFrame;

/**
 *
 * @author miguel
 */
public class FCategorizeTitlesPanel {
    
    public static FeedbackerPanelWithFetcher<CategorizeTitlesEnum, AWTEvent> getPanel(){
        return new CategorizeTitlesPanel();
    }
    
    public static PanelHandler<CategorizeTitlesEnum, AWTEvent, FeedbackerPanelWithFetcher<CategorizeTitlesEnum, AWTEvent>> getHandler(JFrame frame, PanelHandler<?, ?, ?> caller) {
        return new CategorizeTitlesHandler(frame, caller);
    }
    
}
