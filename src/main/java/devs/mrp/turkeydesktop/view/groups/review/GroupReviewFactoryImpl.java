package devs.mrp.turkeydesktop.view.groups.review;

import devs.mrp.turkeydesktop.common.TimeConverter;
import devs.mrp.turkeydesktop.common.factory.CommonBeans;
import devs.mrp.turkeydesktop.database.conditions.ConditionFactoryImpl;
import devs.mrp.turkeydesktop.database.conditions.ConditionService;
import devs.mrp.turkeydesktop.database.group.Group;
import devs.mrp.turkeydesktop.database.group.GroupFactoryImpl;
import devs.mrp.turkeydesktop.database.group.GroupService;
import devs.mrp.turkeydesktop.database.group.assignations.GroupAssignationFactoryImpl;
import devs.mrp.turkeydesktop.database.group.assignations.GroupAssignationService;
import devs.mrp.turkeydesktop.database.group.expor.ExportedGroupFactoryImpl;
import devs.mrp.turkeydesktop.database.group.expor.ExportedGroupService;
import devs.mrp.turkeydesktop.database.group.external.ExternalGroupFactoryImpl;
import devs.mrp.turkeydesktop.database.group.external.ExternalGroupService;
import devs.mrp.turkeydesktop.database.group.facade.AssignableElementFactoryImpl;
import devs.mrp.turkeydesktop.database.group.facade.AssignableElementService;
import devs.mrp.turkeydesktop.database.groupcondition.GroupConditionFacadeFactoryImpl;
import devs.mrp.turkeydesktop.database.groupcondition.GroupConditionFacadeService;
import devs.mrp.turkeydesktop.database.titles.TitleFactoryImpl;
import devs.mrp.turkeydesktop.database.titles.TitleService;
import devs.mrp.turkeydesktop.database.type.TypeFactoryImpl;
import devs.mrp.turkeydesktop.database.type.TypeService;
import devs.mrp.turkeydesktop.view.PanelHandler;
import devs.mrp.turkeydesktop.view.PanelHandlerData;
import devs.mrp.turkeydesktop.view.mainpanel.FeedbackerPanelWithFetcher;
import java.awt.AWTEvent;
import javax.swing.JFrame;

public class GroupReviewFactoryImpl implements GroupReviewFactory {
    
    private static GroupReviewFactoryImpl instance;
    
    private GroupReviewFactoryImpl() {}
    
    public static GroupReviewFactoryImpl getInstance() {
        if (instance == null) {
            instance = new GroupReviewFactoryImpl();
        }
        return instance;
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
        return GroupConditionFacadeFactoryImpl.getInstance().getService();
    }

    @Override
    public GroupService getGroupService() {
        return GroupFactoryImpl.getInstance().getService();
    }

    @Override
    public GroupAssignationService getGroupAssignationService() {
        return GroupAssignationFactoryImpl.getInstance().getService();
    }

    @Override
    public AssignableElementService getAssignableProcessService() {
        return AssignableElementFactoryImpl.getInstance().getProcessesService();
    }

    @Override
    public AssignableElementService getAssignableTitleService() {
        return AssignableElementFactoryImpl.getInstance().getTitlesService();
    }

    @Override
    public ConditionService getConditionService() {
        return ConditionFactoryImpl.getInstance().getService();
    }

    @Override
    public ExternalGroupService getExternalGroupService() {
        return ExternalGroupFactoryImpl.getInstance().getService();
    }

    @Override
    public ExportedGroupService getExportedGroupService() {
        return ExportedGroupFactoryImpl.getInstance().getService();
    }

    @Override
    public TimeConverter getTimeConverter() {
        return CommonBeans.getTimeConverter();
    }

    @Override
    public TitleService getTitleService() {
        return TitleFactoryImpl.getInstance().getService();
    }

    @Override
    public TypeService getProcessService() {
        return TypeFactoryImpl.getInstance().getService();
    }
    
}
