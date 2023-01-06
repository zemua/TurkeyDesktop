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
import devs.mrp.turkeydesktop.database.conditions.ConditionFactory;
import devs.mrp.turkeydesktop.database.group.GroupServiceFactory;
import devs.mrp.turkeydesktop.database.group.Group;
import devs.mrp.turkeydesktop.database.group.assignations.GroupAssignationFactory;
import devs.mrp.turkeydesktop.database.group.assignations.GroupAssignation;
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
import java.util.Comparator;
import org.apache.commons.lang3.StringUtils;
import devs.mrp.turkeydesktop.database.group.GroupService;
import devs.mrp.turkeydesktop.database.groupcondition.GroupConditionFacade;
import devs.mrp.turkeydesktop.database.type.Type;
import devs.mrp.turkeydesktop.database.type.Type.Types;
import java.util.Optional;
import javax.swing.JCheckBox;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import lombok.extern.slf4j.Slf4j;
import devs.mrp.turkeydesktop.database.group.assignations.GroupAssignationService;
import devs.mrp.turkeydesktop.database.conditions.ConditionService;

/**
 *
 * @author miguel
 */
@Slf4j
public class GroupReviewHandler extends PanelHandler<GroupReviewEnum, AWTEvent, FeedbackerPanelWithFetcher<GroupReviewEnum, AWTEvent>> {

    private ConfirmationWithDelay popupMaker = new ConfirmationWithDelayFactory();
    
    private static final Logger logger = Logger.getLogger(GroupReviewHandler.class.getName());
    private final LocaleMessages localeMessages = LocaleMessages.getInstance();

    private Group group;
    private final GroupService groupService = GroupServiceFactory.getService();
    private final GroupAssignationService groupAssignationService = GroupAssignationFactory.getService();
    private final AssignableElementService assignableProcessService = AssignableElementServiceFactory.getProcessesService();
    private final AssignableElementService assignableTitlesService = AssignableElementServiceFactory.getTitlesService();
    private final ConditionService conditionService = ConditionFactory.getService();
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
    
    private void processAssignableObservable(Observable<AssignableElement<Type.Types>> observable, JPanel panel, GroupAssignation.ElementType type) {
        log.debug("starting process");
        observable.filter(element -> {
                                try {
                                    return getFilterText().isEmpty() ||
                                            StringUtils.containsIgnoreCase(element.getElementName(), getFilterText());
                                } catch (Exception e) {
                                    logger.log(Level.SEVERE, e.toString());
                                }
                                return true;
                            })
                    .toSortedList(getAssignableComparatorFunction())
                    .subscribe(result -> {
                        log.debug("handling result for type: {} -> {}", type, result);
                        setSwitchablesFromAssignables(result, panel, type);
                        panel.revalidate();
                        panel.updateUI();
                        log.debug("updated ui");
                    });
    }

    private void setProcesses() {
        Object object = this.getPanel().getProperty(GroupReviewEnum.PROCESS_PANEL);
        if (object == null || !(object instanceof JPanel)) {
            return;
        }
        JPanel panel = (JPanel) object;
        panel.removeAll();
        
        if (group.getType().equals(Group.GroupType.POSITIVE)){
            processAssignableObservable(assignableProcessService.positiveElementsWithAssignation(), panel, GroupAssignation.ElementType.PROCESS);
        } else {
            processAssignableObservable(assignableProcessService.negativeElementsWithAssignation(), panel, GroupAssignation.ElementType.PROCESS);
        }
        
    }

    private void setTitles() {
        Object object = this.getPanel().getProperty(GroupReviewEnum.TITLE_PANEL);
        if (object == null || !(object instanceof JPanel)) {
            return;
        }
        JPanel panel = (JPanel) object;
        panel.removeAll();
        
        if (group.getType().equals(Group.GroupType.POSITIVE)) {
            processAssignableObservable(assignableTitlesService.positiveElementsWithAssignation(), panel, GroupAssignation.ElementType.TITLE);
        } else {
            processAssignableObservable(assignableTitlesService.negativeElementsWithAssignation(), panel, GroupAssignation.ElementType.TITLE);
        }
        
    }
    
    private String getFilterText() throws Exception {
        JTextField field = (JTextField) getObjectFromPanel(GroupReviewEnum.TEXT_FILTER, JTextField.class).orElseThrow(() -> new Exception("wrong object"));
        return field.getText();
    }
    
    private Comparator<AssignableElement<Types>> getAssignableComparatorFunction() {
        return (AssignableElement<Types> o1, AssignableElement<Types> o2) -> {
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

    private void setSwitchablesFromAssignables(List<AssignableElement<Type.Types>> assignables, JPanel panel, GroupAssignation.ElementType type) {
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
                popupMaker.show(this.getFrame(), () -> {
                    // positive
                    if (processOrTitle.equals(GroupAssignation.ElementType.PROCESS)) {
                        groupAssignationService.deleteByProcessId(processOrTitleId).subscribe();
                    } else {
                        groupAssignationService.deleteByTitleId(processOrTitleId).subscribe();
                    }
                }, () -> {
                    // negative
                    switchable.setSelected(true);
                });
            } else { // if the checkbox was cheked with this event
                GroupAssignation ga = GroupAssignation.builder()
                        .elementId(name)
                        .groupId(group.getId())
                        .type(processOrTitle)
                        .build();
                groupAssignationService.add(ga).subscribe();
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
        fillConditionsInPanel();
    }

    private void setTargetNameComboBoxValues() throws Exception {
        if (targetComboBox == null || hourSpinner == null || minuteSpinner == null || daySpinner == null) {
            throw new Exception("error getting some fields for condition");
        }
        targetComboBox.removeAllItems();
        log.debug("filling combo boxes");
        comboItems().subscribe(item -> targetComboBox.addItem(item));
    }

    @SuppressWarnings("unchecked")
    private Observable<Group> comboItems() {
        return groupService.findAllPositive().filter(g -> g.getId() != group.getId());
    }

    private void fillConditionsInPanel() {
        Object conditionsListObject = this.getPanel().getProperty(GroupReviewEnum.CONDITIONS_PANEL_LIST);
        if (conditionsListObject == null || !(conditionsListObject instanceof JPanel)) {
            return;
        }
        JPanel panel = (JPanel) conditionsListObject;
        panel.removeAll();
        Observer<GroupConditionFacade> subscriber = new Observer<GroupConditionFacade>() {
            @Override
            public void onComplete() {
                panel.revalidate();
                panel.repaint();
            }

            @Override
            public void onError(Throwable thrwbl) {
                // nothing to do here
            }

            @Override
            public void onNext(GroupConditionFacade t) {
                ConditionElement element = new ConditionElement(t);
                panel.add(element);
                element.addFeedbackListener((tipo, feedback) -> {
                    switch (feedback) {
                        case DELETE:
                            popupMaker.show(GroupReviewHandler.this.getFrame(), () -> {
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
            }

            @Override
            public void onSubscribe(Disposable d) {
                // nothing here
            }
        };
        log.debug("handling conditions by this group id");
        groupConditionFacadeService.findByGroupId(group.getId()).subscribe(subscriber);
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

        conditionService.add(condition).subscribe(r -> {
            try {
                fillConditionFields();
            } catch (Exception ex) {
                Logger.getLogger(GroupReviewHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    private void removeCondition(long id) {
        conditionService.deleteById(id).subscribe(r -> {
            try {
                fillConditionFields();
            } catch (Exception e) {
                logger.log(Level.SEVERE, "failed refreshing conditions fields after condition deletion", e);
            }
        });
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
        groupService.update(group).subscribe(r -> setGroupLabelName(group.getName()));
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
        conditionService.deleteByGroupId(group.getId()).subscribe();
        conditionService.deleteByTargetId(group.getId()).subscribe();
        externalGroupService.deleteByGroup(group.getId()).subscribe();
        groupAssignationService.deleteByGroupId(group.getId()).subscribe();
        groupService.deleteById(group.getId()).subscribe(r -> exit());
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
        externalGroupService.add(externalGroup).subscribe(r -> {
            try {
                refreshExternalTime();
            } catch (Exception ex) {
                Logger.getLogger(GroupReviewHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    private void refreshExternalTime() throws Exception {
        JPanel panel = (JPanel) getObjectFromPanel(GroupReviewEnum.EXTERNAL_TIME_PANEL, JPanel.class).orElseThrow(() -> new Exception("wrong object"));
        panel.removeAll();
        Observer<RemovableLabel<ExternalGroup>> subscriber = new Observer<RemovableLabel<ExternalGroup>>() {
            @Override
            public void onComplete() {
                panel.revalidate();
                panel.repaint();
            }

            @Override
            public void onError(Throwable thrwbl) {
                log.error("error on refreshExternalTime subscription", thrwbl.getStackTrace());
            }

            @Override
            public void onNext(RemovableLabel<ExternalGroup> t) {
                panel.add(t);
            }

            @Override
            public void onSubscribe(Disposable d) {
                // nothing here
            }
        };
        log.debug("going to refresh external time");
        externalGroupService.findByGroup(this.group.getId())
                .map(externalGroup -> {
                    log.debug("refreshing external time for {}", externalGroup.getFile());
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
                            externalGroupService.deleteById(tipo.getId()).subscribe();
                            try {
                                refreshExternalTime();
                            } catch (Exception e) {
                                logger.log(Level.SEVERE, "could not refresh external time panel after path deletion", e);
                            }
                        }
                    });
                    return label;
                })
                .subscribe(subscriber);
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
        log.debug("adding export info");
        exportedGroupService.add(exportedGroup).subscribe(r -> {
            log.debug("adding export info for {}", r);
            try {
                refreshGroupExporter();
            } catch (Exception ex) {
                Logger.getLogger(GroupReviewHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }, onerror -> {log.debug("error on subcription selectFileGroupExporter", onerror.getStackTrace());});
    }

    private void updateGroupExporterDays() throws Exception {
        JSpinner daysSpinner = (JSpinner) getObjectFromPanel(GroupReviewEnum.EXPORT_GROUP_DAYS, JSpinner.class).orElseThrow(() -> new Exception("wrong object"));
        exportedGroupService.findByGroup(this.group.getId()).subscribe(existing -> {
            if (Objects.isNull(existing)) {
                return;
            }
            existing.setDays((Long) daysSpinner.getValue());
            exportedGroupService.add(existing).subscribe(r -> {
                try {
                    refreshGroupExporter();
                } catch (Exception ex) {
                    Logger.getLogger(GroupReviewHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            
        }, onerror -> {log.debug("error on subscription to updateGroupExporterDays", onerror.getStackTrace());});
    }

    private void refreshGroupExporter() throws Exception {
        JSpinner daysSpinner = (JSpinner) getObjectFromPanel(GroupReviewEnum.EXPORT_GROUP_DAYS, JSpinner.class).orElseThrow(() -> new Exception("wrong object"));
        JButton button = (JButton) getObjectFromPanel(GroupReviewEnum.EXPORT_GROUP_TARGET, JButton.class).orElseThrow(() -> new Exception("wrong object"));
        log.debug("refresh group exporter");
        exportedGroupService.findByGroup(this.group.getId()).subscribe(existing -> {
            log.debug("refreshing {}", existing.getFile());
            if (Objects.isNull(existing)) {
                return;
            }
            daysSpinner.setValue(existing.getDays());
            String text = existing.getFile();
            if (text.length() > 25) {
                text = text.substring(text.length() - 25);
            }
            button.setText(text);
        }, onerror -> {log.debug("error on refreshGroupExporter", onerror.getStackTrace());});
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
                    groupService.update(group).subscribe();
                    preventClose.setEnabled(true);
                }, () -> {
                    // negative runnable
                    // recover unchecked state
                    preventClose.setEnabled(true);
                    preventClose.setSelected(false);
                });
            } else {
                group.setPreventClose(false);
                groupService.update(group).subscribe();
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error setting prevent close");
        }
    }

}
