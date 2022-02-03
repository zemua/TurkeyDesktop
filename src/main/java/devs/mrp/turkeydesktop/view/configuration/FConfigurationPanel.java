/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.view.configuration;

import devs.mrp.turkeydesktop.view.PanelHandler;
import devs.mrp.turkeydesktop.view.mainpanel.FeedbackerPanelWithFetcher;
import java.awt.AWTEvent;
import javax.swing.JFrame;

/**
 *
 * @author miguel
 */
public class FConfigurationPanel {
    
    public static FeedbackerPanelWithFetcher<ConfigurationPanelEnum, AWTEvent> getPanel() {
        return new ConfigurationPanel();
    }
    
    public static PanelHandler<ConfigurationPanelEnum, AWTEvent, FeedbackerPanelWithFetcher<ConfigurationPanelEnum, AWTEvent>> getHandler(JFrame frame, PanelHandler<?, ?, ?> caller) {
        return new ConfigurationHandler(frame, caller);
    }
    
}
