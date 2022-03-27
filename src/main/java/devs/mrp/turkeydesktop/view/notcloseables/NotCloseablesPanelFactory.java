/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.view.notcloseables;

import devs.mrp.turkeydesktop.view.PanelHandler;
import devs.mrp.turkeydesktop.view.mainpanel.FeedbackerPanelWithFetcher;
import javax.swing.JFrame;

/**
 *
 * @author miguel
 */
public class NotCloseablesPanelFactory {
    
    public static FeedbackerPanelWithFetcher<NotCloseablesEnum,Object> getPanel() {
        return new NotCloseablesPanel();
    }
    
    public static PanelHandler<NotCloseablesEnum,Object,FeedbackerPanelWithFetcher<NotCloseablesEnum,Object>> getHandler(JFrame frame, PanelHandler<?,?,?> caller) {
        return new NotCloseablesHandler(frame, caller);
    }
    
}
