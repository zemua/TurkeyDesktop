package devs.mrp.turkeydesktop.view.groups.review;

import devs.mrp.turkeydesktop.database.group.Group;
import devs.mrp.turkeydesktop.database.groupcondition.GroupConditionFacadeService;
import devs.mrp.turkeydesktop.view.PanelHandler;
import devs.mrp.turkeydesktop.view.PanelHandlerData;
import devs.mrp.turkeydesktop.view.container.FactoryInitializer;
import devs.mrp.turkeydesktop.view.mainpanel.FeedbackerPanelWithFetcher;
import java.awt.AWTEvent;
import javax.swing.JFrame;

public class GroupReviewFactoryImpl implements GroupReviewFactory {
    
    private FactoryInitializer factory;
    
    public GroupReviewFactoryImpl(FactoryInitializer factory) {
        this.factory = factory;
    }
    
    @Override
    public FeedbackerPanelWithFetcher<GroupReviewEnum, AWTEvent> getPanel() {
        return new GroupReviewPanel();
    }
    
    @Override
    public PanelHandler<GroupReviewEnum, AWTEvent, FeedbackerPanelWithFetcher<GroupReviewEnum, AWTEvent>> getHandler(JFrame frame, PanelHandler<?, ?, ?> caller, Group group) {
        var data = PanelHandlerData.builder().frame(frame).caller(caller).group(group).build();
        return new GroupReviewHandler(data, this);
    }

    @Override
    public GroupConditionFacadeService groupConditionFacadeService() {
        return factory.getGroupConditionFacadeFactory().getService();
    }
    
}
