package devs.mrp.turkeydesktop.view.groups;

import devs.mrp.turkeydesktop.database.group.Group;
import devs.mrp.turkeydesktop.database.group.GroupService;
import devs.mrp.turkeydesktop.view.PanelHandler;
import devs.mrp.turkeydesktop.view.PanelHandlerData;
import devs.mrp.turkeydesktop.view.groups.review.GroupReviewEnum;
import devs.mrp.turkeydesktop.view.groups.review.GroupReviewFactory;
import devs.mrp.turkeydesktop.view.mainpanel.FeedbackerPanelWithFetcher;
import java.awt.AWTEvent;

public interface GroupsPanelFactory {
    
    FeedbackerPanelWithFetcher<GroupsEnum, AWTEvent> getPanel();
    PanelHandler<GroupsEnum, AWTEvent, FeedbackerPanelWithFetcher<GroupsEnum, AWTEvent>, GroupsPanelFactory> getHandler(PanelHandlerData<Group.GroupType> data);
    PanelHandler<GroupReviewEnum, AWTEvent, FeedbackerPanelWithFetcher<GroupReviewEnum, AWTEvent>, GroupReviewFactory> getReviewGroupHandler(PanelHandlerData<Group> data);
    GroupService getGroupSerice();
    
}
