/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.view.groups.review;

import devs.mrp.turkeydesktop.common.ConfirmationWithDelay;
import devs.mrp.turkeydesktop.common.RemovableLabel;
import devs.mrp.turkeydesktop.common.TimeConverter;
import devs.mrp.turkeydesktop.common.impl.ConfirmationWithDelayFactory;
import devs.mrp.turkeydesktop.database.conditions.Condition;
import devs.mrp.turkeydesktop.database.conditions.FConditionService;
import devs.mrp.turkeydesktop.database.conditions.IConditionService;
import devs.mrp.turkeydesktop.database.group.GroupServiceFactory;
import devs.mrp.turkeydesktop.database.group.Group;
import devs.mrp.turkeydesktop.database.group.assignations.FGroupAssignationService;
import devs.mrp.turkeydesktop.database.group.assignations.GroupAssignation;
import devs.mrp.turkeydesktop.database.group.assignations.IGroupAssignationService;
import devs.mrp.turkeydesktop.database.group.expor.ExportedGroup;
import devs.mrp.turkeydesktop.database.group.expor.ExportedGroupService;
import devs.mrp.turkeydesktop.database.group.expor.ExportedGroupServiceFactory;
import devs.mrp.turkeydesktop.database.group.external.ExternalGroup;
import devs.mrp.turkeydesktop.database.group.external.ExternalGroupService;
import devs.mrp.turkeydesktop.database.group.external.ExternalGroupServiceFactory;
import devs.mrp.turkeydesktop.database.group.facade.AssignableElement;
import devs.mrp.turkeydesktop.database.group.facade.AssignableElementServiceFactory;
import devs.mrp.turkeydesktop.database.groupcondition.FGroupConditionFacadeService;
import devs.mrp.turkeydesktop.database.groupcondition.IGroupConditionFacadeService;
import devs.mrp.turkeydesktop.i18n.LocaleMessages;
import devs.mrp.turkeydesktop.view.PanelHandler;
import devs.mrp.turkeydesktop.view.groups.review.switchable.Switchable;
import devs.mrp.turkeydesktop.view.mainpanel.FeedbackerPanelWithFetcher;
import java.awt.AWTEvent;
import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import devs.mrp.turkeydesktop.database.group.facade.AssignableElementService;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import org.apache.commons.lang3.StringUtils;
import devs.mrp.turkeydesktop.database.group.GroupService;
import java.util.Optional;
import java.util.function.Consumer;
import javax.swing.JCheckBox;

/**
 *
 * @author miguel
 */
public class GroupReviewHandler extends PanelHandler<GroupReviewEnum, AWTEvent, FeedbackerPanelWithFetcher<GroupReviewEnum, AWTEvent>> {

    private ConfirmationWithDelay popupMaker = new ConfirmationWithDelayFactory();
    
    private static final Logger logger = Logger.getLogger(GroupReviewHandler.class.getName());
    private final LocaleMessages localeMessages = LocaleMessages.getInstance();

    private Group group;
    private final GroupService groupService = GroupServiceFactory.getService();
    private final IGroupAssignationService groupAssignationService = FGroupAssignationService.getService();
    private final AssignableElementService assignableProcessService = AssignableElementServiceFactory.getProcessesService();
    private final AssignableElementService assignableTitlesService = AssignableElementServiceFactory.getTitlesService();
    private final IConditionService conditionService = FConditionService.getService();
    private final ExternalGroupService externalGroupService = ExternalGroupServiceFactory.getService();
    private final ExportedGroupService exportedGroupService = ExportedGroupServiceFactory.getService();
    private final IGroupConditionFacadeService groupConditionFacadeService = FGroupConditionFacadeService.getService();

    private JComboBox<Group> targetComboBox;
    private JSpinner hourSpinner;
    private JSpinner minuteSpinner;
    private JSpinner daySpinner;
    private JPanel conditionsListPanel;

    public GroupReviewHandler(JFrame frame, PanelHandler<?, ?, ?> caller, Group group) {
        super(frame, caller);
        this.group = group;
    }

    @Override
    protected FeedbackerPanelWithFetcher<GroupReviewEnum, AWTEvent> initPanel() {
        return GroupReviewPanelFactory.getPanel();
    }

    @Override
    protected void initListeners(FeedbackerPanelWithFetcher<GroupReviewEnum, AWTEvent> pan) {
        pan.addFeedbackListener((tipo, feedback) -> {
            if (isListening()) {
                switch (tipo) {
                    case BACK:
                        exit();
                        break;
                    case ADD_CONDITION_BUTTON:
                    try {
                        this.addCondition();
                    } catch (Exception e) {
                        // print error and go back
                        logger.log(Level.SEVERE, "error adding condition", e);
                        exit();
                    }
                    break;
                    case SAVE_GROUP_NAME:
                    try {
                        saveGroupName();
                    } catch (Exception ex) {
                        logger.log(Level.SEVERE, "error saving group name", ex);
                    }
                    break;
                    case DELETE_GROUP:
                    try {
                        deleteGroup();
                    } catch (Exception ex) {
                        logger.log(Level.SEVERE, "error deleting group", ex);
                    }
                    break;
                    case EXTERNAL_TIME_BUTTON:
                    try {
                        addExternalTime();
                    } catch (Exception ex) {
                        logger.log(Level.SEVERE, "error adding external time", ex);
                    }
                    break;
                    case EXPORT_GROUP_TARGET:
                    try {
                        selectFileGroupExporter();
                    } catch (Exception ex) {
                        logger.log(Level.SEVERE, "error setting group time exporter", ex);
                    }
                    break;
                    case EXPORT_GROUP_DAYS:
                    try {
                        updateGroupExporterDays();
                    } catch (Exception ex) {
                        logger.log(Level.SEVERE, "error setting group time exporter", ex);
                    }
                    break;
                    case ORDER_DROPDOWN:
                    case TEXT_FILTER:
                        setTitles();
                        setProcesses();
                        break;
                    case PREVENT_CLOSE:
                        try {
                            handlePreventClose();
                        } catch (Exception ex) {
                            logger.log(Level.SEVERE, "error setting prevent close option", ex);
                        }
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    protected void doExtraBeforeShow() {
        try {
            setGroupLabelName();
            setProcesses();
            setTitles();
            setConditions();
            refreshExternalTime();
            setConfiguration();
            showHideSetPreventClosing();
        } catch (Exception e) {
            // print error and go back
            logger.log(Level.SEVERE, "error setting up UI", e);
            exit();
        }
    }

    @Override
    protected void doBeforeExit() {
    }

    private void setGroupLabelName() {
        setGroupLabelName(group.getName());
    }

    private void setGroupLabelName(String name) {
        Object object = this.getPanel().getProperty(GroupReviewEnum.GROUP_LABEL);
        if (object == null || !(object instanceof JLabel)) {
            return;
        }
        JLabel label = (JLabel) object;
        label.setText(name);
    }

    private void setProcesses() {
        Object object = this.getPanel().getProperty(GroupReviewEnum.PROCESS_PANEL);
        if (object == null || !(object instanceof JPanel)) {
            return;
        }
        JPanel panel = (JPanel) object;
        panel.removeAll();

        Consumer<List<AssignableElement<GroupAssignation.ElementType>>> consumer = result -> {
            Iterator<AssignableElement<GroupAssignation.ElementType>> iterator = result.iterator();
            while (iterator.hasNext()) {
                AssignableElement element = iterator.next();
                try {
                    if (!getFilterText().isEmpty() && !StringUtils.containsIgnoreCase(element.getElementName(), getFilterText())) {
                        iterator.remove();
                    }
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "error getting the text from filter, defaulting to no filter", e);
                }

            }

            Collections.sort(result, getAssignableComparator());

            setSwitchablesFromAssignables(result, panel, GroupAssignation.ElementType.PROCESS);
            panel.revalidate();
            panel.updateUI();
        };
        
        if (group.getType().equals(Group.GroupType.POSITIVE)){
            assignableProcessService.positiveElementsWithAssignation(consumer);
        } else {
            assignableProcessService.negativeElementsWithAssignation(consumer);
        }
        
    }

    private void setTitles() {
        Object object = this.getPanel().getProperty(GroupReviewEnum.TITLE_PANEL);
        if (object == null || !(object instanceof JPanel)) {
            return;
        }
        JPanel panel = (JPanel) object;
        panel.removeAll();

        Consumer<List<AssignableElement<GroupAssignation.ElementType>>> consumer = result -> {
            Iterator<AssignableElement<GroupAssignation.ElementType>> iterator = result.iterator();
            while (iterator.hasNext()) {
                AssignableElement element = iterator.next();
                try {
                    if (!getFilterText().isEmpty() && !StringUtils.containsIgnoreCase(element.getElementName(), getFilterText())) {
                        iterator.remove();
                    }
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "error getting the text from filter, defaulting to no filter", e);
                }

            }

            Collections.sort(result, getAssignableComparator());

            setSwitchablesFromAssignables(result, panel, GroupAssignation.ElementType.TITLE);
            panel.revalidate();
            panel.updateUI();
        };
        if (group.getType().equals(Group.GroupType.POSITIVE)) {
            assignableTitlesService.positiveElementsWithAssignation(consumer);
        } else {
            assignableTitlesService.negativeElementsWithAssignation(consumer);
        }
        
    }
    
    private String getFilterText() throws Exception {
        JTextField field = (JTextField) getObjectFromPanel(GroupReviewEnum.TEXT_FILTER, JTextField.class).orElseThrow(() -> new Exception("wrong object"));
        return field.getText();
    }
    
    private Comparator<AssignableElement> getAssignableComparator() {
        Comparator<AssignableElement> comparator = (AssignableElement o1, AssignableElement o2) -> {
            try {
                JComboBox orderDropdown = (JComboBox) getObjectFromPanel(GroupReviewEnum.ORDER_DROPDOWN, JComboBox.class).orElseThrow(() -> new Exception("wrong object"));
                if (localeMessages.getString(ComboOrderEnum.UNASSIGNED_FIRST.getKey()).equals(orderDropdown.getSelectedItem())){
                    boolean b1 = !assignableBelongsToGroup(o1) && assignableIsEnabled(o1);
                    boolean b2 = !assignableBelongsToGroup(o2) && assignableIsEnabled(o2);
                    return Boolean.compare(b2, b1);
                }
                if (localeMessages.getString(ComboOrderEnum.ASSIGNED_HERE_FIRST.getKey()).equals(orderDropdown.getSelectedItem())) {
                    boolean b1 = assignableBelongsToGroup(o1);
                    boolean b2 = assignableBelongsToGroup(o2);
                    return Boolean.compare(b2, b1);
                }
            } catch (Exception e) {
                logger.log(Level.SEVERE, "error getting the combo box", e);
            }
            return 0;
        };
        return comparator;
    }

    private void setSwitchablesFromAssignables(List<AssignableElement<GroupAssignation.ElementType>> assignables, JPanel panel, GroupAssignation.ElementType type) {
        assignables.forEach(a -> {
            Switchable switchable = new Switchable(a.getElementName(),
                    assignableBelongsToGroup(a), // checked if it belongs to this group
                    assignableIsEnabled(a)); // enabled if belongs to no group, or to this group
            setProcessSwitchableListener(switchable, a.getElementName(), type);
            panel.add(switchable);
        });
    }
    
    private boolean assignableBelongsToGroup(AssignableElement assignable) {
        return assignable.getGroupId() != null && assignable.getGroupId().equals(group.getId());
    }
    
    private boolean assignableIsEnabled(AssignableElement assignable) {
        return assignable.getGroupId() == null || assignable.getGroupId().equals(group.getId());
    }

    private void setProcessSwitchableListener(Switchable switchable, String name, GroupAssignation.ElementType processOrTitle) {
        switchable.addFeedbackListener((processOrTitleId, feedback) -> {
            if (!feedback) { // if the checkbox was unchecked with this event
                if (Group.GroupType.NEGATIVE.equals(this.group.getType())) {
                    popupMaker.show(this.getFrame(), () -> {
                        // positive
                        if (processOrTitle.equals(GroupAssignation.ElementType.PROCESS)) {
                            groupAssignationService.deleteByProcessId(processOrTitleId, r -> {});
                        } else {
                            groupAssignationService.deleteByTitleId(processOrTitleId, r -> {});
                        }
                    }, () -> {
                        // negative
                        switchable.setSelected(true);
                    });
                } else {
                    if (processOrTitle.equals(GroupAssignation.ElementType.PROCESS)) {
                        groupAssignationService.deleteByProcessId(processOrTitleId, r -> {});
                    } else {
                        groupAssignationService.deleteByTitleId(processOrTitleId, r -> {});
                    }
                }
            } else { // if the checkbox was cheked with this event
                GroupAssignation ga = new GroupAssignation();
                ga.setElementId(name);
                ga.setGroupId(group.getId());
                ga.setType(processOrTitle);
                groupAssignationService.add(ga, r -> {});
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
        targetComboBox = (JComboBox) targetObject;

        Object hourObject = this.getPanel().getProperty(GroupReviewEnum.HOUR_SPINNER);
        if (hourObject == null || !(hourObject instanceof JSpinner)) {
            throw new Exception("wrong object type for Hour Spinner");
        }
        hourSpinner = (JSpinner) hourObject;

        Object minuteObject = this.getPanel().getProperty(GroupReviewEnum.MINUTE_SPINNER);
        if (minuteObject == null || !(minuteObject instanceof JSpinner)) {
            throw new Exception("wrong object type for Minute Spinner");
        }
        minuteSpinner = (JSpinner) minuteObject;

        Object dayObject = this.getPanel().getProperty(GroupReviewEnum.DAY_SPINNER);
        if (dayObject == null || !(dayObject instanceof JSpinner)) {
            throw new Exception("wrong object type for Day Spinner");
        }
        daySpinner = (JSpinner) dayObject;

        Object conditionsListObject = this.getPanel().getProperty(GroupReviewEnum.CONDITIONS_PANEL_LIST);
        if (conditionsListObject == null || !(conditionsListObject instanceof JPanel)) {
            throw new Exception("wrong object type for conditions list panel");
        }
        conditionsListPanel = (JPanel) conditionsListObject;

        fillConditionFields();
    }

    private void fillConditionFields() throws Exception {
        setTargetNameComboBoxValues();
        hourSpinner.setValue(0L);
        minuteSpinner.setValue(15L);
        daySpinner.setValue(0L);
        fillConditionsInPanel(conditionsListPanel);
    }

    private void setTargetNameComboBoxValues() throws Exception {
        if (targetComboBox == null || hourSpinner == null || minuteSpinner == null || daySpinner == null) {
            throw new Exception("error getting some fields for condition");
        }
        targetComboBox.removeAllItems();
        comboItems().forEach(item -> targetComboBox.addItem(item));
    }

    @SuppressWarnings("unchecked")
    private List<Group> comboItems() {
        return groupService.findAllPositive()
                .stream()
                .filter(g -> g.getId() != group.getId())
                .collect(Collectors.toList());
    }

    private void fillConditionsInPanel(JPanel panel) {
        panel.removeAll();
        groupConditionFacadeService.findByGroupId(group.getId(), groupConditions -> {
            groupConditions.forEach(cond -> {
                    ConditionElement element = new ConditionElement(cond);
                    panel.add(element);
                    element.addFeedbackListener((tipo, feedback) -> {
                        switch (feedback) {
                            case DELETE:
                                popupMaker.show(this.getFrame(), () -> {
                                    // positive
                                    removeCondition(tipo.getConditionId());
                                }, () -> {
                                    // negative
                                    // do nothing, intentionally left blank
                                });
                                break;
                            default:
                                break;
                        }
                    });
                });
        });
        panel.revalidate();
        panel.updateUI();
    }

    private void addCondition() throws Exception {
        if (targetComboBox == null || hourSpinner == null || minuteSpinner == null || daySpinner == null) {
            throw new Exception("error getting some fields for condition");
        }
        Condition condition = new Condition();
        condition.setGroupId(this.group.getId());
        condition.setTargetId(targetComboBox.getItemAt(targetComboBox.getSelectedIndex()).getId());
        condition.setUsageTimeCondition(TimeConverter.hoursToMilis((Long) hourSpinner.getValue()) + TimeConverter.minutesToMilis((Long) minuteSpinner.getValue()));
        condition.setLastDaysCondition((long) daySpinner.getValue());

        conditionService.add(condition, r -> {});
        fillConditionFields();
    }

    private void removeCondition(long id) {
        conditionService.deleteById(id);
        try {
            fillConditionFields();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "failed refreshing conditions fields after condition deletion", e);
        }
    }

    private void setConfiguration() throws Exception {
        refreshGroupExporter();
    }
    
    private void showHideSetPreventClosing() {
        Optional opt = getObjectFromPanel(GroupReviewEnum.PREVENT_CLOSE, JCheckBox.class);
        if (opt.isEmpty()) {
            return;
        }
        JCheckBox checkbox = (JCheckBox) opt.get();
        if (Group.GroupType.POSITIVE.equals(group.getType())) {
            checkbox.setVisible(false); // hide for positive groups
        } else {
            checkbox.setVisible(true); // show for others (negative)
            if (group.isPreventClose()) {
                checkbox.setSelected(true);
            } else {
                checkbox.setSelected(false);
            }
        }
    }

    private void saveGroupName() throws Exception {
        Object nameObject = this.getPanel().getProperty(GroupReviewEnum.GROUP_NAME_TEXT);
        if (nameObject == null || !(nameObject instanceof JTextField)) {
            throw new Exception("wrong object type for Group Name Text");
        }
        JTextField field = (JTextField) nameObject;
        if (field.getText().isBlank()) {
            return;
        }
        group.setName(field.getText());
        groupService.update(group);
        setGroupLabelName(group.getName());
    }

    private void deleteGroup() throws Exception {
        Object deleteObject = this.getPanel().getProperty(GroupReviewEnum.DELETE_TEXT);
        if (deleteObject == null || !(deleteObject instanceof JTextField)) {
            throw new Exception("wrong object type for Delete Text");
        }
        JTextField field = (JTextField) deleteObject;
        if (!field.getText().equals("delete")) {
            return;
        }
        groupService.deleteById(group.getId());
        conditionService.deleteByGroupId(group.getId());
        conditionService.deleteByTargetId(group.getId());
        externalGroupService.deleteByGroup(group.getId());
        groupAssignationService.deleteByGroupId(group.getId(), r -> {});
        exit();
    }

    private void addExternalTime() throws Exception {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Plain text files .txt only", "txt");
        chooser.setFileFilter(filter);
        chooser.setAcceptAllFileFilterUsed(false);
        int returnVal = chooser.showOpenDialog(chooser);
        if (returnVal != JFileChooser.APPROVE_OPTION) {
            return;
        }
        File file = chooser.getSelectedFile();
        if (file.getPath().length() > 500) {
            JLabel message = new JLabel();
            message.setText(localeMessages.getString("errorPath500"));
            JPanel panel = (JPanel) getObjectFromPanel(GroupReviewEnum.EXTERNAL_TIME_PANEL, JPanel.class).orElseThrow(() -> new Exception("wrong object"));
            panel.add(message);
            panel.revalidate();
            panel.repaint();
        }
        ExternalGroup externalGroup = new ExternalGroup();
        externalGroup.setGroup(this.group.getId());
        externalGroup.setFile(file.getPath());
        externalGroupService.add(externalGroup);
        refreshExternalTime();
    }

    private void refreshExternalTime() throws Exception {
        JPanel panel = (JPanel) getObjectFromPanel(GroupReviewEnum.EXTERNAL_TIME_PANEL, JPanel.class).orElseThrow(() -> new Exception("wrong object"));
        panel.removeAll();
        externalGroupService.findByGroup(this.group.getId()).stream()
                .map(externalGroup -> {
                    RemovableLabel<ExternalGroup> label = new RemovableLabel<>(externalGroup) {
                        @Override
                        protected String getNameFromElement(ExternalGroup element) {
                            String filePath = element.getFile();
                            if (filePath.length() > 25) {
                                filePath = filePath.substring(filePath.length() - 25);
                            }
                            return filePath;
                        }

                        @Override
                        protected void initializeOtherElements() {
                            // ¯\_ (ツ)_/¯
                        }

                        @Override
                        protected void addOtherItems(JPanel panel) {
                            // ¯\_ (ツ)_/¯
                        }
                    };
                    label.addFeedbackListener((ExternalGroup tipo, RemovableLabel.Action feedback) -> {
                        if (feedback.equals(RemovableLabel.Action.DELETE)) {
                            externalGroupService.deleteById(tipo.getId());
                            try {
                                refreshExternalTime();
                            } catch (Exception e) {
                                logger.log(Level.SEVERE, "could not refresh external time panel after path deletion", e);
                            }
                        }
                    });
                    return label;
                }).forEach(label -> panel.add(label));
        panel.revalidate();
        panel.repaint();
    }

    private void selectFileGroupExporter() throws Exception {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Plain text files .txt only", "txt");
        chooser.setFileFilter(filter);
        chooser.setAcceptAllFileFilterUsed(false);
        int returnVal = chooser.showSaveDialog(chooser);
        if (returnVal != JFileChooser.APPROVE_OPTION) {
            return;
        }
        File file = chooser.getSelectedFile();
        if (file.getPath().length() > 500) {
            JButton button = (JButton) getObjectFromPanel(GroupReviewEnum.EXPORT_GROUP_TARGET, JButton.class).orElseThrow(() -> new Exception("wrong object"));
            button.setText(localeMessages.getString("errorPath500"));
        }
        ExportedGroup exportedGroup = new ExportedGroup();
        JSpinner daysSpinner = (JSpinner) getObjectFromPanel(GroupReviewEnum.EXPORT_GROUP_DAYS, JSpinner.class).orElseThrow(() -> new Exception("wrong object"));
        exportedGroup.setDays((Long) daysSpinner.getValue());
        exportedGroup.setFile(file.getPath());
        exportedGroup.setGroup(this.group.getId());
        exportedGroupService.add(exportedGroup);
        refreshGroupExporter();
    }

    private void updateGroupExporterDays() throws Exception {
        JSpinner daysSpinner = (JSpinner) getObjectFromPanel(GroupReviewEnum.EXPORT_GROUP_DAYS, JSpinner.class).orElseThrow(() -> new Exception("wrong object"));
        ExportedGroup existing = exportedGroupService.findById(this.group.getId());
        if (Objects.isNull(existing)) {
            return;
        }
        existing.setDays((Long) daysSpinner.getValue());
        exportedGroupService.add(existing);
        refreshGroupExporter();
    }

    private void refreshGroupExporter() throws Exception {
        JSpinner daysSpinner = (JSpinner) getObjectFromPanel(GroupReviewEnum.EXPORT_GROUP_DAYS, JSpinner.class).orElseThrow(() -> new Exception("wrong object"));
        JButton button = (JButton) getObjectFromPanel(GroupReviewEnum.EXPORT_GROUP_TARGET, JButton.class).orElseThrow(() -> new Exception("wrong object"));
        ExportedGroup existing = exportedGroupService.findById(this.group.getId());
        if (Objects.isNull(existing)) {
            return;
        }
        daysSpinner.setValue(existing.getDays());
        String text = existing.getFile();
        if (text.length() > 25) {
            text = text.substring(text.length() - 25);
        }
        button.setText(text);
    }
    
    private void handlePreventClose() {
        Optional opt = getObjectFromPanel(GroupReviewEnum.PREVENT_CLOSE, JCheckBox.class);
        if (opt.isEmpty()) {
            return;
        }
        JCheckBox preventClose = (JCheckBox) opt.get();
        boolean isPreventClose = preventClose.isSelected();
        try {
            if (isPreventClose) {
                preventClose.setEnabled(false);
                popupMaker.show(getFrame(), () ->{
                    // positive runnable
                    group.setPreventClose(true);
                    groupService.update(group);
                    preventClose.setEnabled(true);
                }, () -> {
                    // negative runnable
                    // recover unchecked state
                    preventClose.setEnabled(true);
                    preventClose.setSelected(false);
                });
            } else {
                group.setPreventClose(false);
                groupService.update(group);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error setting prevent close");
        }
    }

}
