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
import devs.mrp.turkeydesktop.view.mainpanel.FeedbackerPanelWithFetcher;
import java.awt.AWTEvent;
import javax.swing.JFrame;

public interface GroupReviewFactory {
    
    GroupConditionFacadeService groupConditionFacadeService();
    PanelHandler<GroupReviewEnum, AWTEvent, FeedbackerPanelWithFetcher<GroupReviewEnum, AWTEvent>> getHandler(JFrame frame, PanelHandler<?, ?, ?> caller, Group group);
    FeedbackerPanelWithFetcher<GroupReviewEnum, AWTEvent> getPanel();
    GroupService getGroupService();
    GroupAssignationService getGroupAssignationService();
    AssignableElementService getAssignableProcessService();
    AssignableElementService getAssignableTitleService();
    ConditionService getConditionService();
    ExternalGroupService getExternalGroupService();
    ExportedGroupService getExportedGroupService();
    TimeConverter getTimeConverter();
    
}
