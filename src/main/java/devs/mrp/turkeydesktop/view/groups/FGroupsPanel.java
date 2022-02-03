/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.view.groups;

import devs.mrp.turkeydesktop.database.group.Group;
import devs.mrp.turkeydesktop.view.PanelHandler;
import devs.mrp.turkeydesktop.view.mainpanel.FeedbackerPanelWithFetcher;
import java.awt.AWTEvent;
import javax.swing.JFrame;

/**
 *
 * @author miguel
 */
public class FGroupsPanel {
    
    public static FeedbackerPanelWithFetcher<GroupsEnum, AWTEvent> getPanel() {
        return new GroupsPanel();
    }
    
    public static PanelHandler<GroupsEnum, AWTEvent, FeedbackerPanelWithFetcher<GroupsEnum, AWTEvent>> getHandler(JFrame frame, PanelHandler<?, ?, ?> caller, Group.GroupType type) {
        return new GroupsHandler(frame, caller, type);
    }
    
}
