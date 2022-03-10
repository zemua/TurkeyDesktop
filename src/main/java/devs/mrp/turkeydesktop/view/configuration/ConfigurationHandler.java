/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.view.configuration;

import devs.mrp.turkeydesktop.common.FileHandler;
import devs.mrp.turkeydesktop.common.TimeConverter;
import devs.mrp.turkeydesktop.database.config.ConfigElement;
import devs.mrp.turkeydesktop.database.config.FConfigElementService;
import devs.mrp.turkeydesktop.database.config.IConfigElementService;
import devs.mrp.turkeydesktop.i18n.LocaleMessages;
import devs.mrp.turkeydesktop.view.PanelHandler;
import devs.mrp.turkeydesktop.view.mainpanel.FeedbackerPanelWithFetcher;
import java.awt.AWTEvent;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JToggleButton;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author miguel
 */
public class ConfigurationHandler extends PanelHandler<ConfigurationPanelEnum, AWTEvent, FeedbackerPanelWithFetcher<ConfigurationPanelEnum, AWTEvent>> {

    private IConfigElementService configService = FConfigElementService.getService();
    private LocaleMessages localeMessages = LocaleMessages.getInstance();
    private Logger logger = Logger.getLogger(ConfigurationHandler.class.getName());

    public ConfigurationHandler(JFrame frame, PanelHandler<?, ?, ?> caller) {
        super(frame, caller);
    }

    @Override
    protected FeedbackerPanelWithFetcher<ConfigurationPanelEnum, AWTEvent> initPanel() {
        this.setPanel(FConfigurationPanel.getPanel());
        return this.getPanel();
    }

    @Override
    protected void initListeners(FeedbackerPanelWithFetcher<ConfigurationPanelEnum, AWTEvent> pan) {
        pan.addFeedbackListener((tipo, feedback) -> {
            switch (tipo) {
                case BACK:
                    this.getCaller().show();
                    break;
                case PROPORTION:
                    handleNewProportion();
                    break;
                case LOCKDOWN:
                    try {
                        handleLockdownStatusChange();
                    } catch (Exception e) {
                        logger.log(Level.SEVERE, "error handling response", e);
                        getCaller().show();
                    }
                    break;
                case LOCKDOWN_FROM:
                    try{
                        handleLockdownFromChange();
                    } catch (Exception e) {
                        logger.log(Level.SEVERE, "error handling response", e);
                        getCaller().show();
                    }
                    break;
                case LOCKDOWN_TO:
                    try {
                        handleLockdownToChange();
                    } catch (Exception e) {
                        logger.log(Level.SEVERE, "error handling response", e);
                        getCaller().show();
                    }
                    break;
                case LOCKDOWN_NOTIFY:
                    try {
                        handleLockDownNotificationChange();
                    } catch (Exception e) {
                        logger.log(Level.SEVERE, "error handling response", e);
                        getCaller().show();
                    }
                    break;
                case LOCKDOWN_NOTIFY_MIN:
                    try {
                        handleLockDownMinutesNotificationChange();
                    } catch (Exception e) {
                        logger.log(Level.SEVERE, "error handling response", e);
                        getCaller().show();
                    }
                    break;
                case NOTIFY_MIN_LEFT:
                    try {
                        handleMinLeftNotificationChange();
                    } catch (Exception e) {
                        logger.log(Level.SEVERE, "error handling response", e);
                        getCaller().show();
                    }
                    break;
                case NOTIFY_MIN_LEFT_QTY:
                    try {
                        handleMinLeftQtyNotificationChange();
                    } catch (Exception e) {
                        logger.log(Level.SEVERE, "error handling response", e);
                        getCaller().show();
                    }
                    break;
                case EXPORT_BUTTON:
                    try {
                        handleExportButton();
                    } catch (Exception e) {
                        logger.log(Level.SEVERE, "error handling response", e);
                        getCaller().show();
                    }
                    break;
                case EXPORT_TOGGLE:
                    try {
                        handleExportToggle();
                    } catch (Exception e) {
                        logger.log(Level.SEVERE, "error handling response", e);
                        getCaller().show();
                    }
                    break;
                default:
                    break;
            }
        });
    }

    @Override
    protected void doExtraBeforeShow() {
        try {
            setupProportion();
            setupLockDown();
            setupLockDownNotification();
            setupMinLeftNotification();
            // TODO setupExportItems();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "error showing panel", e);
            getCaller().show();
        }
    }

    private void setupProportion() {
        int proportion = Integer.valueOf(configService.configElement(ConfigurationEnum.PROPORTION).getValue());
        JSlider slider = (JSlider) this.getPanel().getProperty(ConfigurationPanelEnum.PROPORTION);
        slider.setValue(proportion);
    }

    private void setupLockDown() throws Exception {
        boolean locked = Boolean.valueOf(configService.configElement(ConfigurationEnum.LOCKDOWN).getValue());
        JToggleButton lockedButton = (JToggleButton) getObjectFromPanel(ConfigurationPanelEnum.LOCKDOWN, JToggleButton.class).orElseThrow(() -> new Exception("wrong object"));
        lockedButton.setSelected(locked);

        long from = Long.valueOf(configService.configElement(ConfigurationEnum.LOCKDOWN_FROM).getValue());
        JSpinner fromHour = (JSpinner) getObjectFromPanel(ConfigurationPanelEnum.LOCKDOWN_FROM_HOUR, JSpinner.class).orElseThrow(() -> new Exception("wrong object"));
        JSpinner fromMin = (JSpinner) getObjectFromPanel(ConfigurationPanelEnum.LOCKDOWN_FROM_MIN, JSpinner.class).orElseThrow(() -> new Exception("wrong object"));
        fromHour.setValue(TimeConverter.getHours(from));
        fromMin.setValue(TimeConverter.getMinutes(from));

        long to = Long.valueOf(configService.configElement(ConfigurationEnum.LOCKDOWN_TO).getValue());
        JSpinner toHour = (JSpinner) getObjectFromPanel(ConfigurationPanelEnum.LOCKDOWN_TO_HOUR, JSpinner.class).orElseThrow(() -> new Exception("wrong object"));
        JSpinner toMin = (JSpinner) getObjectFromPanel(ConfigurationPanelEnum.LOCKDOWN_TO_MIN, JSpinner.class).orElseThrow(() -> new Exception("wrong object"));
        toHour.setValue(TimeConverter.getHours(to));
        toMin.setValue(TimeConverter.getMinutes(to));
    }

    private void setupLockDownNotification() throws Exception {
        boolean notify = Boolean.valueOf(configService.configElement(ConfigurationEnum.LOCK_NOTIFY).getValue());
        JToggleButton notifyButton = (JToggleButton) getObjectFromPanel(ConfigurationPanelEnum.LOCKDOWN_NOTIFY, JToggleButton.class).orElseThrow(() -> new Exception("wrong object"));
        notifyButton.setSelected(notify);

        long notifyMinutes = Long.valueOf(configService.configElement(ConfigurationEnum.LOCK_NOTIFY_MINUTES).getValue());
        if (notifyMinutes < 1 * 60 * 1000 || notifyMinutes > 60 * 60 * 1000) {
            notifyMinutes = 10 * 60 * 1000;
        }
        JSpinner minSpin = (JSpinner) getObjectFromPanel(ConfigurationPanelEnum.LOCKDOWN_NOTIFY_MIN, JSpinner.class).orElseThrow(() -> new Exception("wrong object"));
        minSpin.setValue(TimeConverter.getMinutes(notifyMinutes));
    }

    private void setupMinLeftNotification() throws Exception {
        boolean notify = Boolean.valueOf(configService.configElement(ConfigurationEnum.MIN_LEFT_BUTTON).getValue());
        JToggleButton notifyButton = (JToggleButton) getObjectFromPanel(ConfigurationPanelEnum.NOTIFY_MIN_LEFT, JToggleButton.class).orElseThrow(() -> new Exception("wrong object"));
        notifyButton.setSelected(notify);

        long notifyMinutes = Long.valueOf(configService.configElement(ConfigurationEnum.MIN_LEFT_QTY).getValue());
        if (notifyMinutes < 1 * 60 * 1000 || notifyMinutes > 60 * 60 * 1000) {
            notifyMinutes = 10 * 60 * 1000;
        }
        JSpinner minSpin = (JSpinner) getObjectFromPanel(ConfigurationPanelEnum.NOTIFY_MIN_LEFT_QTY, JSpinner.class).orElseThrow(() -> new Exception("wrong object"));
        minSpin.setValue(TimeConverter.getMinutes(notifyMinutes));
    }

    private void handleNewProportion() {
        JSlider slider = (JSlider) this.getPanel().getProperty(ConfigurationPanelEnum.PROPORTION);
        int proportion = slider.getValue();
        ConfigElement el = new ConfigElement();
        el.setKey(ConfigurationEnum.PROPORTION);
        el.setValue(String.valueOf(proportion));
        configService.add(el);
    }

    private void handleLockdownStatusChange() throws Exception {
        JToggleButton lockDownButton = (JToggleButton) getObjectFromPanel(ConfigurationPanelEnum.LOCKDOWN, JToggleButton.class).orElseThrow(() -> new Exception("wrong object"));
        boolean checked = lockDownButton.isSelected();
        ConfigElement el = new ConfigElement();
        el.setKey(ConfigurationEnum.LOCKDOWN);
        el.setValue(String.valueOf(checked));
        configService.add(el);
    }

    private void handleLockdownFromChange() throws Exception {
        JSpinner lockDownHourSpinner = (JSpinner) getObjectFromPanel(ConfigurationPanelEnum.LOCKDOWN_FROM_HOUR, JSpinner.class).orElseThrow(() -> new Exception("wrong object"));
        JSpinner lockDownMinSpinner = (JSpinner) getObjectFromPanel(ConfigurationPanelEnum.LOCKDOWN_FROM_MIN, JSpinner.class).orElseThrow(() -> new Exception("wrong object"));
        try {
            Long time = TimeConverter.minutesToMilis((Long) lockDownMinSpinner.getValue());
            time += TimeConverter.hoursToMilis((Long) lockDownHourSpinner.getValue());
            ConfigElement el = new ConfigElement();
            el.setKey(ConfigurationEnum.LOCKDOWN_FROM);
            el.setValue(String.valueOf(time));
            configService.add(el);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error getting values from spinners to db", e);
        }
    }

    private void handleLockdownToChange() throws Exception {
        JSpinner lockDownHourSpinner = (JSpinner) getObjectFromPanel(ConfigurationPanelEnum.LOCKDOWN_TO_HOUR, JSpinner.class).orElseThrow(() -> new Exception("wrong object"));
        JSpinner lockDownMinSpinner = (JSpinner) getObjectFromPanel(ConfigurationPanelEnum.LOCKDOWN_TO_MIN, JSpinner.class).orElseThrow(() -> new Exception("wrong object"));
        try {
            Long time = TimeConverter.minutesToMilis((Long) lockDownMinSpinner.getValue());
            time += TimeConverter.hoursToMilis((Long) lockDownHourSpinner.getValue());
            ConfigElement el = new ConfigElement();
            el.setKey(ConfigurationEnum.LOCKDOWN_TO);
            el.setValue(String.valueOf(time));
            configService.add(el);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error getting values from spinners to db", e);
        }
    }

    private void handleLockDownNotificationChange() throws Exception {
        JToggleButton lockDownNotification = (JToggleButton) getObjectFromPanel(ConfigurationPanelEnum.LOCKDOWN_NOTIFY, JToggleButton.class).orElseThrow(() -> new Exception("wrong object"));
        boolean checked = lockDownNotification.isSelected();
        ConfigElement el = new ConfigElement();
        el.setKey(ConfigurationEnum.LOCK_NOTIFY);
        el.setValue(String.valueOf(checked));
        configService.add(el);
    }

    private void handleLockDownMinutesNotificationChange() throws Exception {
        JSpinner lockDownMinSpinner = (JSpinner) getObjectFromPanel(ConfigurationPanelEnum.LOCKDOWN_NOTIFY_MIN, JSpinner.class).orElseThrow(() -> new Exception("wrong object"));
        try {
            Long time = TimeConverter.minutesToMilis((Long) lockDownMinSpinner.getValue());
            ConfigElement el = new ConfigElement();
            el.setKey(ConfigurationEnum.LOCK_NOTIFY_MINUTES);
            el.setValue(String.valueOf(time));
            configService.add(el);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error getting values from spinners to db", e);
        }
    }

    private void handleMinLeftNotificationChange() throws Exception {
        JToggleButton minLeftNotification = (JToggleButton) getObjectFromPanel(ConfigurationPanelEnum.NOTIFY_MIN_LEFT, JToggleButton.class).orElseThrow(() -> new Exception("wrong object"));
        boolean checked = minLeftNotification.isSelected();
        ConfigElement el = new ConfigElement();
        el.setKey(ConfigurationEnum.MIN_LEFT_BUTTON);
        el.setValue(String.valueOf(checked));
        configService.add(el);
    }

    private void handleMinLeftQtyNotificationChange() throws Exception {
        JSpinner minLeftQty = (JSpinner) getObjectFromPanel(ConfigurationPanelEnum.NOTIFY_MIN_LEFT_QTY, JSpinner.class).orElseThrow(() -> new Exception("wrong object"));
        try {
            Long time = TimeConverter.minutesToMilis((Long) minLeftQty.getValue());
            ConfigElement el = new ConfigElement();
            el.setKey(ConfigurationEnum.MIN_LEFT_QTY);
            el.setValue(String.valueOf(time));
            configService.add(el);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error getting values from spinners to db", e);
        }
    }

    private void handleExportToggle() throws Exception {
        JToggleButton exportToggle = (JToggleButton) getObjectFromPanel(ConfigurationPanelEnum.EXPORT_TOGGLE, JToggleButton.class).orElseThrow(() -> new Exception("wrong object"));
        boolean checked = exportToggle.isSelected();
        ConfigElement el = new ConfigElement();
        el.setKey(ConfigurationEnum.EXPORT_TOGGLE);
        el.setValue(String.valueOf(checked));
        configService.add(el);
    }

    private void handleExportButton() throws Exception {
        JButton exportButton = (JButton) getObjectFromPanel(ConfigurationPanelEnum.EXPORT_BUTTON, JButton.class).orElseThrow(() -> new Exception("wrong object"));
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Plain text files .txt only", "txt");
        chooser.setFileFilter(filter);
        chooser.setAcceptAllFileFilterUsed(false);
        int returnVal = chooser.showSaveDialog(chooser);
        if (returnVal != JFileChooser.APPROVE_OPTION) {
            return;
        }
        File file = FileHandler.createFile(chooser.getSelectedFile(), ".txt");
        // TODO save file path to db
        // TODO make a handler to write to the file
    }

}
