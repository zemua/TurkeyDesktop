package devs.mrp.turkeydesktop.view.groups.review;

import devs.mrp.turkeydesktop.common.TimeConverter;
import devs.mrp.turkeydesktop.database.conditions.ConditionService;
import devs.mrp.turkeydesktop.database.group.Group;
import devs.mrp.turkeydesktop.database.group.GroupService;
import devs.mrp.turkeydesktop.database.group.assignations.GroupAssignationService;
import devs.mrp.turkeydesktop.database.group.expor.ExportedGroupService;
import devs.mrp.turkeydesktop.database.group.external.ExternalGroupService;
import devs.mrp.turkeydesktop.database.group.facade.AssignableElementService;
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
    public PanelHandler<GroupReviewEnum, AWTEvent, FeedbackerPanelWithFetcher<GroupReviewEnum, AWTEvent>, GroupReviewFactory> getHandler(JFrame frame, PanelHandler<?, ?, ?, ?> caller, Group group) {
        PanelHandlerData<Group> data = new PanelHandlerData<>(frame, caller, group);
        return new GroupReviewHandler(data, this);
    }

    @Override
    public GroupConditionFacadeService groupConditionFacadeService() {
        return factory.getGroupConditionFacadeFactory().getService();
    }

    @Override
    public GroupService getGroupService() {
        return factory.getGroupFactory().getService();
    }

    @Override
    public GroupAssignationService getGroupAssignationService() {
        return factory.getGroupAssignationFactory().getService();
    }

    @Override
    public AssignableElementService getAssignableProcessService() {
        return factory.getAssignableElementFactory().getProcessesService();
    }

    @Override
    public AssignableElementService getAssignableTitleService() {
        return factory.getAssignableElementFactory().getTitlesService();
    }

    @Override
    public ConditionService getConditionService() {
        return factory.getConditionFactory().getService();
    }

    @Override
    public ExternalGroupService getExternalGroupService() {
        return factory.getExternalGroupFactory().getService();
    }

    @Override
    public ExportedGroupService getExportedGroupService() {
        return factory.getExportedGroupFactory().getService();
    }

    @Override
    public TimeConverter getTimeConverter() {
        return factory.getTimeConverter();
    }
    
}
