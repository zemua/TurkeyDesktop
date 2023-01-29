package devs.mrp.turkeydesktop.view.groups;

import devs.mrp.turkeydesktop.database.group.Group;
import devs.mrp.turkeydesktop.view.PanelHandler;
import devs.mrp.turkeydesktop.view.mainpanel.FeedbackerPanelWithFetcher;
import java.awt.AWTEvent;
import java.util.function.Supplier;
import javax.swing.JFrame;

public class FGroupsPanel {
    
    private static Supplier<FeedbackerPanelWithFetcher<GroupsEnum, AWTEvent>> panelSupplier;

    public static void setPanelSupplier(Supplier<FeedbackerPanelWithFetcher<GroupsEnum, AWTEvent>> panelSupplier) {
        FGroupsPanel.panelSupplier = panelSupplier;
    }
    
    public static FeedbackerPanelWithFetcher<GroupsEnum, AWTEvent> getPanel() {
        FeedbackerPanelWithFetcher<GroupsEnum, AWTEvent> result;
        if (panelSupplier == null) {
            result = new GroupsPanel();
        } else {
            result = panelSupplier.get();
        }
        return result;
    }
    
    public static PanelHandler<GroupsEnum, AWTEvent, FeedbackerPanelWithFetcher<GroupsEnum, AWTEvent>> getHandler(JFrame frame, PanelHandler<?, ?, ?> caller, Group.GroupType type) {
        return new GroupsHandler(frame, caller, type);
    }
    
}
