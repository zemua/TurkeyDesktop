package devs.mrp.turkeydesktop.view.groups;

import devs.mrp.turkeydesktop.database.group.Group;
import devs.mrp.turkeydesktop.view.PanelHandler;
import devs.mrp.turkeydesktop.view.PanelHandlerData;
import devs.mrp.turkeydesktop.view.container.FactoryInitializer;
import devs.mrp.turkeydesktop.view.groups.review.GroupReviewEnum;
import devs.mrp.turkeydesktop.view.mainpanel.FeedbackerPanelWithFetcher;
import java.awt.AWTEvent;

public class GroupsPanelFactoryImpl implements GroupsPanelFactory {
    
    private FactoryInitializer factory;
    private FeedbackerPanelWithFetcher<GroupsEnum, AWTEvent> panel;
    
    public GroupsPanelFactoryImpl(FactoryInitializer factoryInitializer) {
        this.factory = factoryInitializer;
    }
    
    @Override
    public FeedbackerPanelWithFetcher<GroupsEnum, AWTEvent> getPanel() {
        return panel;
    }
    
    @Override
    public PanelHandler<GroupsEnum, AWTEvent, FeedbackerPanelWithFetcher<GroupsEnum, AWTEvent>> getHandler(PanelHandlerData<Group.GroupType> data) {
        return new GroupsHandler(data, this);
    }

    @Override
    public PanelHandler<GroupReviewEnum, AWTEvent, FeedbackerPanelWithFetcher<GroupReviewEnum, AWTEvent>> getReviewGroupHandler(PanelHandlerData<Group> data) {
        return factory.getGroupReviewFactory().getHandler(data.getFrame(), data.getCaller(), data.getEntity());
    }
    
}
