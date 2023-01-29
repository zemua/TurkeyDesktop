/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.view.groups.review;

import devs.mrp.turkeydesktop.database.group.Group;
import devs.mrp.turkeydesktop.view.PanelHandler;
import devs.mrp.turkeydesktop.view.mainpanel.FeedbackerPanelWithFetcher;
import java.awt.AWTEvent;
import javax.swing.JFrame;

/**
 *
 * @author miguel
 */
public class GroupReviewPanelFactory {
    
    public static FeedbackerPanelWithFetcher<GroupReviewEnum, AWTEvent> getPanel() {
        return new GroupReviewPanel();
    }
    
    public static PanelHandler<GroupReviewEnum, AWTEvent, FeedbackerPanelWithFetcher<GroupReviewEnum, AWTEvent>> getHandler(JFrame frame, PanelHandler<?, ?, ?> caller, Group group) {
        return new GroupReviewHandler(frame, caller, group);
    }
    
}
