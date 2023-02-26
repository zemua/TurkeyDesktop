package devs.mrp.turkeydesktop.view.groups;

import devs.mrp.turkeydesktop.database.group.Group;
import devs.mrp.turkeydesktop.database.group.GroupFactoryImpl;
import devs.mrp.turkeydesktop.database.group.GroupService;
import devs.mrp.turkeydesktop.view.PanelHandler;
import devs.mrp.turkeydesktop.view.PanelHandlerData;
import devs.mrp.turkeydesktop.view.groups.review.GroupReviewEnum;
import devs.mrp.turkeydesktop.view.groups.review.GroupReviewFactory;
import devs.mrp.turkeydesktop.view.groups.review.GroupReviewFactoryImpl;
import devs.mrp.turkeydesktop.view.mainpanel.FeedbackerPanelWithFetcher;
import java.awt.AWTEvent;

public class GroupsPanelFactoryImpl implements GroupsPanelFactory {
    
    private static GroupsPanelFactoryImpl instance;
    
    private GroupsPanelFactoryImpl() {}
    
    public static GroupsPanelFactoryImpl getInstance() {
        if (instance == null) {
            instance = new GroupsPanelFactoryImpl();
        }
        return instance;
    }
    
    @Override
    public FeedbackerPanelWithFetcher<GroupsEnum, AWTEvent> getPanel() {
        return new GroupsPanel();
    }
    
    @Override
    public PanelHandler<GroupsEnum, AWTEvent, FeedbackerPanelWithFetcher<GroupsEnum, AWTEvent>, GroupsPanelFactory> getHandler(PanelHandlerData<Group.GroupType> data) {
        return new GroupsHandler(data, this);
    }

    @Override
    public PanelHandler<GroupReviewEnum, AWTEvent, FeedbackerPanelWithFetcher<GroupReviewEnum, AWTEvent>, GroupReviewFactory> getReviewGroupHandler(PanelHandlerData<Group> data) {
        return GroupReviewFactoryImpl.getInstance().getHandler(data.getFrame(), data.getCaller(), data.getEntity());
    }

    @Override
    public GroupService getGroupSerice() {
        return GroupFactoryImpl.getInstance().getService();
    }
    
}
