/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.view.groups.review;

import devs.mrp.turkeydesktop.common.FeedbackListener;
import devs.mrp.turkeydesktop.database.group.Group;
import devs.mrp.turkeydesktop.database.group.assignations.FGroupAssignationService;
import devs.mrp.turkeydesktop.database.group.assignations.GroupAssignation;
import devs.mrp.turkeydesktop.database.group.assignations.IGroupAssignationService;
import devs.mrp.turkeydesktop.database.group.facade.AssignableElement;
import devs.mrp.turkeydesktop.database.group.facade.FAssignableElementService;
import devs.mrp.turkeydesktop.database.group.facade.IAssignableElementService;
import devs.mrp.turkeydesktop.view.PanelHandler;
import devs.mrp.turkeydesktop.view.groups.review.switchable.Switchable;
import devs.mrp.turkeydesktop.view.mainpanel.FeedbackerPanelWithFetcher;
import java.awt.AWTEvent;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author miguel
 */
public class GroupReviewHandler extends PanelHandler<GroupReviewEnum, AWTEvent, FeedbackerPanelWithFetcher<GroupReviewEnum, AWTEvent>> {

    private Group group;
    private final IGroupAssignationService groupAssignationService = FGroupAssignationService.getService();
    private final IAssignableElementService assignableProcessService = FAssignableElementService.getProcessesService();
    private final IAssignableElementService assignableTitlesService = FAssignableElementService.getTitlesService();
    
    public GroupReviewHandler(JFrame frame, PanelHandler<?, ?, ?> caller, Group group) {
        super(frame, caller);
        this.group = group;
    }
    
    @Override
    protected FeedbackerPanelWithFetcher<GroupReviewEnum, AWTEvent> initPanel() {
        return FGroupReviewPanel.getPanel();
    }

    @Override
    protected void initListeners(FeedbackerPanelWithFetcher<GroupReviewEnum, AWTEvent> pan) {
        pan.addFeedbackListener((tipo, feedback) -> {
            switch (tipo) {
                case BACK:
                    this.getCaller().show();
                    break;
                default:
                    break;
            }
        });
    }

    @Override
    protected void doExtraBeforeShow() {
        setGroupLabelName();
        setProcesses();
        setTitles();
        setConditions();
        setConfiguration();
    }
    
    private void setGroupLabelName() {
        Object object = this.getPanel().getProperty(GroupReviewEnum.GROUP_LABEL);
        if (object == null || !(object instanceof JLabel)) {
            return;
        }
        JLabel label = (JLabel) object;
        label.setText(group.getName());
    }
    
    private void setProcesses() {
        Object object = this.getPanel().getProperty(GroupReviewEnum.PROCESS_PANEL);
        if (object == null || !(object instanceof JPanel)) {
            return;
        }
        JPanel panel = (JPanel) object;
        panel.removeAll();
        
        List<AssignableElement> assignables = group.getType().equals(Group.GroupType.POSITIVE) ?
                assignableProcessService.positiveElementsWithAssignation() :
                assignableProcessService.negativeElementsWithAssignation();
        
        setSwitchablesFromAssignables(assignables, panel, GroupAssignation.ElementType.PROCESS);
    }
    
    private void setTitles() {
        Object object = this.getPanel().getProperty(GroupReviewEnum.TITLE_PANEL);
        if (object == null || !(object instanceof JPanel)) {
            return;
        }
        JPanel panel = (JPanel) object;
        panel.removeAll();
        
        List<AssignableElement> assignables = group.getType().equals(Group.GroupType.POSITIVE) ?
                assignableTitlesService.positiveElementsWithAssignation() :
                assignableTitlesService.negativeElementsWithAssignation();
        
        setSwitchablesFromAssignables(assignables, panel, GroupAssignation.ElementType.TITLE);
    }
    
    private void setSwitchablesFromAssignables(List<AssignableElement> assignables, JPanel panel, GroupAssignation.ElementType type) {
        assignables.forEach(a -> {
            Switchable switchable = new Switchable(a.getElementName(), 
                    a.getGroupId() != null && a.getGroupId().equals(group.getId()), // checked if it belongs to this group
                    a.getGroupId() == null || a.getGroupId().equals(group.getId())); // enabled if belongs to no group, or to this group
            setProcessSwitchableListener(switchable, a.getElementName(), type);
            panel.add(switchable);
        });
    }
    
    private void setProcessSwitchableListener(Switchable switchable, String name, GroupAssignation.ElementType processOrTitle) {
        switchable.addFeedbackListener((processOrTitleId, feedback) -> {
            if (!feedback) { // if the checkbox was unchecked with this event
                if (processOrTitle.equals(GroupAssignation.ElementType.PROCESS)) {
                    groupAssignationService.deleteByProcessId(processOrTitleId);
                } else {
                    groupAssignationService.deleteByTitleId(processOrTitleId);
                }
            } else { // if the checkbox was cheked with this event
                GroupAssignation ga = new GroupAssignation();
                ga.setElementId(name);
                ga.setGroupId(group.getId());
                ga.setType(processOrTitle);
                groupAssignationService.add(ga);
            }
        });
    }
    
    private void setConditions() {
        // TODO
    }
    
    private void setConfiguration() {
        // TODO
    }
    
}
