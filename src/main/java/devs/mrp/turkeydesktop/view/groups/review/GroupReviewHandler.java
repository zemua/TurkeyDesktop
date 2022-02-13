/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.view.groups.review;

import devs.mrp.turkeydesktop.common.Dupla;
import devs.mrp.turkeydesktop.common.TimeConverter;
import devs.mrp.turkeydesktop.database.conditions.Condition;
import devs.mrp.turkeydesktop.database.conditions.FConditionService;
import devs.mrp.turkeydesktop.database.conditions.IConditionService;
import devs.mrp.turkeydesktop.database.group.FGroupService;
import devs.mrp.turkeydesktop.database.group.Group;
import devs.mrp.turkeydesktop.database.group.IGroupService;
import devs.mrp.turkeydesktop.database.group.assignations.FGroupAssignationService;
import devs.mrp.turkeydesktop.database.group.assignations.GroupAssignation;
import devs.mrp.turkeydesktop.database.group.assignations.IGroupAssignationService;
import devs.mrp.turkeydesktop.database.group.facade.AssignableElement;
import devs.mrp.turkeydesktop.database.group.facade.FAssignableElementService;
import devs.mrp.turkeydesktop.database.group.facade.IAssignableElementService;
import devs.mrp.turkeydesktop.i18n.LocaleMessages;
import devs.mrp.turkeydesktop.view.PanelHandler;
import devs.mrp.turkeydesktop.view.groups.review.switchable.Switchable;
import devs.mrp.turkeydesktop.view.mainpanel.FeedbackerPanelWithFetcher;
import java.awt.AWTEvent;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;

/**
 *
 * @author miguel
 */
public class GroupReviewHandler extends PanelHandler<GroupReviewEnum, AWTEvent, FeedbackerPanelWithFetcher<GroupReviewEnum, AWTEvent>> {

    private static final Logger logger = Logger.getLogger(GroupReviewHandler.class.getName());
    
    private final LocaleMessages locale = LocaleMessages.getInstance();
    
    private Group group;
    private final IGroupService groupService = FGroupService.getService();
    private final IGroupAssignationService groupAssignationService = FGroupAssignationService.getService();
    private final IAssignableElementService assignableProcessService = FAssignableElementService.getProcessesService();
    private final IAssignableElementService assignableTitlesService = FAssignableElementService.getTitlesService();
    private final IConditionService conditionService = FConditionService.getService();
    
    private JComboBox<Group> targetComboBox;
    private JSpinner hourSpinner;
    private JSpinner minuteSpinner;
    private JSpinner daySpinner;
    
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
                case ADD_CONDITION_BUTTON:
                    try {
                        this.addCondition();
                    } catch (Exception e) {
                        // print error and go back
                        logger.log(Level.SEVERE, "error adding condition");
                        this.getCaller().show();
                    }
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
        try {
            setConditions();
            setTargetNameComboBoxValues();
        } catch (Exception e) {
            // print error and go back
            logger.log(Level.SEVERE, "error setting conditions");
            this.getCaller().show();
        }
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
    
    @SuppressWarnings("unchecked")
    private void setConditions() throws Exception {
        // Setup the fields
        
        Object targetObject = this.getPanel().getProperty(GroupReviewEnum.TARGET_NAME_COMBO_BOX);
        if (targetObject == null || !(targetObject instanceof JComboBox)) {
            throw new Exception("wrong object type for Target Name Combo Box");
        }
        targetComboBox = (JComboBox)targetObject;
        
        Object hourObject = this.getPanel().getProperty(GroupReviewEnum.HOUR_SPINNER);
        if (hourObject == null || !(hourObject instanceof JSpinner)) {
            throw new Exception("wrong object type for Hour Spinner");
        }
        hourSpinner = (JSpinner)hourObject;
        
        Object minuteObject = this.getPanel().getProperty(GroupReviewEnum.MINUTE_SPINNER);
        if (minuteObject == null || !(minuteObject instanceof JSpinner)) {
            throw new Exception("wrong object type for Minute Spinner");
        }
        minuteSpinner = (JSpinner)minuteObject;
        
        Object dayObject = this.getPanel().getProperty(GroupReviewEnum.DAY_SPINNER);
        if (dayObject == null || !(dayObject instanceof JSpinner)) {
            throw new Exception("wrong object type for Day Spinner");
        }
        daySpinner = (JSpinner)dayObject;
        
        setNewConditionFields();
    }
    
    private void setTargetNameComboBoxValues() throws Exception {
        if (targetComboBox == null || hourSpinner == null || minuteSpinner == null || daySpinner == null) {
            throw new Exception("error getting some fields for condition");
        }
        comboItems().forEach(item -> targetComboBox.addItem(item));
    }
    
    private void setNewConditionFields() {
        // TODO
    }
    
    private void addCondition() throws Exception {
        if (targetComboBox == null || hourSpinner == null || minuteSpinner == null || daySpinner == null) {
            throw new Exception("error getting some fields for condition");
        }
        Condition condition = new Condition();
        condition.setGroupId(this.group.getId());
        condition.setTargetId(targetComboBox.getItemAt(targetComboBox.getSelectedIndex()).getId());
        condition.setUsageTimeCondition(TimeConverter.hoursToMilis((Long)hourSpinner.getValue()) + TimeConverter.minutesToMilis((Long)minuteSpinner.getValue()));
        condition.setLastDaysCondition((long)daySpinner.getValue());
        
        conditionService.add(condition);
    }
    
    private void setConfiguration() {
        // TODO
    }
    
    @SuppressWarnings("unchecked")
    private List<Group> comboItems() {
        return groupService.findAllPositive()
                .stream()
                .filter(g -> g.getId() != group.getId())
                .collect(Collectors.toList());
    }
    
}
