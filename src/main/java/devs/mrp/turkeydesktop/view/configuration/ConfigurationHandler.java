/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.view.configuration;

import devs.mrp.turkeydesktop.common.TimeConverter;
import devs.mrp.turkeydesktop.database.config.ConfigElement;
import devs.mrp.turkeydesktop.database.config.FConfigElementService;
import devs.mrp.turkeydesktop.database.config.IConfigElementService;
import devs.mrp.turkeydesktop.i18n.LocaleMessages;
import devs.mrp.turkeydesktop.view.PanelHandler;
import devs.mrp.turkeydesktop.view.mainpanel.FeedbackerPanelWithFetcher;
import java.awt.AWTEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JToggleButton;

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
                    handleLockdownStatusChange();
                    break;
                case LOCKDOWN_FROM:
                    handleLockdownFromChange();
                    break;
                case LOCKDOWN_TO:
                    handleLockdownToChange();
                    break;
                case LOCKDOWN_NOTIFY:
                    handleLockDownNotificationChange();
                    break;
                case LOCKDOWN_NOTIFY_MIN:
                    handleLockDownMinutesNotificationChange();
                    break;
                case NOTIFY_MIN_LEFT:
                    handleMinLeftNotificationChange();
                    break;
                case NOTIFY_MIN_LEFT_QTY:
                    handleMinLeftQtyNotificationChange();
                    break;
                default:
                    break;
            }
        });
    }

    @Override
    protected void doExtraBeforeShow() {
        setupProportion();
        setupLockDown();
        setupLockDownNotification();
        setupMinLeftNotification();
    }
    
    private void setupProportion() {
        int proportion = Integer.valueOf(configService.configElement(ConfigurationEnum.PROPORTION).getValue());
        JSlider slider = (JSlider) this.getPanel().getProperty(ConfigurationPanelEnum.PROPORTION);
        slider.setValue(proportion);
    }
    
    private void setupLockDown() {
        boolean locked = Boolean.valueOf(configService.configElement(ConfigurationEnum.LOCKDOWN).getValue());
        JToggleButton lockedButton = (JToggleButton)getObjectFromPanel(ConfigurationPanelEnum.LOCKDOWN, JToggleButton.class).orElseThrow();
        lockedButton.setSelected(locked);
        
        long from = Long.valueOf(configService.configElement(ConfigurationEnum.LOCKDOWN_FROM).getValue());
        JSpinner fromHour = (JSpinner)getObjectFromPanel(ConfigurationPanelEnum.LOCKDOWN_FROM_HOUR, JSpinner.class).orElseThrow();
        JSpinner fromMin = (JSpinner)getObjectFromPanel(ConfigurationPanelEnum.LOCKDOWN_FROM_MIN, JSpinner.class).orElseThrow();
        fromHour.setValue(TimeConverter.getHours(from));
        fromMin.setValue(TimeConverter.getMinutes(from));
        
        long to = Long.valueOf(configService.configElement(ConfigurationEnum.LOCKDOWN_TO).getValue());
        JSpinner toHour = (JSpinner)getObjectFromPanel(ConfigurationPanelEnum.LOCKDOWN_TO_HOUR, JSpinner.class).orElseThrow();
        JSpinner toMin = (JSpinner)getObjectFromPanel(ConfigurationPanelEnum.LOCKDOWN_TO_MIN, JSpinner.class).orElseThrow();
        toHour.setValue(TimeConverter.getHours(to));
        toMin.setValue(TimeConverter.getMinutes(to));
    }
    
    private void setupLockDownNotification() {
        boolean notify = Boolean.valueOf(configService.configElement(ConfigurationEnum.LOCK_NOTIFY).getValue());
        JToggleButton notifyButton = (JToggleButton)getObjectFromPanel(ConfigurationPanelEnum.LOCKDOWN_NOTIFY, JToggleButton.class).orElseThrow();
        notifyButton.setSelected(notify);
        
        long notifyMinutes = Long.valueOf(configService.configElement(ConfigurationEnum.LOCK_NOTIFY_MINUTES).getValue());
        if (notifyMinutes < 1*60*1000 || notifyMinutes > 60*60*1000) {
            notifyMinutes = 10*60*1000;
        }
        JSpinner minSpin = (JSpinner)getObjectFromPanel(ConfigurationPanelEnum.LOCKDOWN_NOTIFY_MIN, JSpinner.class).orElseThrow();
        minSpin.setValue(TimeConverter.getMinutes(notifyMinutes));
    }
    
    private void setupMinLeftNotification() {
        boolean notify = Boolean.valueOf(configService.configElement(ConfigurationEnum.MIN_LEFT_BUTTON).getValue());
        JToggleButton notifyButton = (JToggleButton)getObjectFromPanel(ConfigurationPanelEnum.NOTIFY_MIN_LEFT, JToggleButton.class).orElseThrow();
        notifyButton.setSelected(notify);
        
        long notifyMinutes = Long.valueOf(configService.configElement(ConfigurationEnum.MIN_LEFT_QTY).getValue());
        if (notifyMinutes < 1*60*1000 || notifyMinutes > 60*60*1000) {
            notifyMinutes = 10*60*1000;
        }
        JSpinner minSpin = (JSpinner)getObjectFromPanel(ConfigurationPanelEnum.NOTIFY_MIN_LEFT_QTY, JSpinner.class).orElseThrow();
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
    
    private void handleLockdownStatusChange() {
        JToggleButton lockDownButton = (JToggleButton)getObjectFromPanel(ConfigurationPanelEnum.LOCKDOWN, JToggleButton.class).orElseThrow();
        boolean checked = lockDownButton.isSelected();
        ConfigElement el = new ConfigElement();
        el.setKey(ConfigurationEnum.LOCKDOWN);
        el.setValue(String.valueOf(checked));
        configService.add(el);
    }
    
    private void handleLockdownFromChange() {
        JSpinner lockDownHourSpinner = (JSpinner)getObjectFromPanel(ConfigurationPanelEnum.LOCKDOWN_FROM_HOUR, JSpinner.class).orElseThrow();
        JSpinner lockDownMinSpinner = (JSpinner)getObjectFromPanel(ConfigurationPanelEnum.LOCKDOWN_FROM_MIN, JSpinner.class).orElseThrow();
        try {
            Long time = TimeConverter.minutesToMilis((Long)lockDownMinSpinner.getValue());
            time += TimeConverter.hoursToMilis((Long)lockDownHourSpinner.getValue());
            ConfigElement el = new ConfigElement();
            el.setKey(ConfigurationEnum.LOCKDOWN_FROM);
            el.setValue(String.valueOf(time));
            configService.add(el);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error getting values from spinners to db", e);
        }
    }
    
    private void handleLockdownToChange() {
        JSpinner lockDownHourSpinner = (JSpinner)getObjectFromPanel(ConfigurationPanelEnum.LOCKDOWN_TO_HOUR, JSpinner.class).orElseThrow();
        JSpinner lockDownMinSpinner = (JSpinner)getObjectFromPanel(ConfigurationPanelEnum.LOCKDOWN_TO_MIN, JSpinner.class).orElseThrow();
        try {
            Long time = TimeConverter.minutesToMilis((Long)lockDownMinSpinner.getValue());
            time += TimeConverter.hoursToMilis((Long)lockDownHourSpinner.getValue());
            ConfigElement el = new ConfigElement();
            el.setKey(ConfigurationEnum.LOCKDOWN_TO);
            el.setValue(String.valueOf(time));
            configService.add(el);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error getting values from spinners to db", e);
        }
    }
    
    private void handleLockDownNotificationChange() {
        JToggleButton lockDownNotification = (JToggleButton)getObjectFromPanel(ConfigurationPanelEnum.LOCKDOWN_NOTIFY, JToggleButton.class).orElseThrow();
        boolean checked = lockDownNotification.isSelected();
        ConfigElement el = new ConfigElement();
        el.setKey(ConfigurationEnum.LOCK_NOTIFY);
        el.setValue(String.valueOf(checked));
        configService.add(el);
    }
    
    private void handleLockDownMinutesNotificationChange() {
        JSpinner lockDownMinSpinner = (JSpinner)getObjectFromPanel(ConfigurationPanelEnum.LOCKDOWN_NOTIFY_MIN, JSpinner.class).orElseThrow();
        try {
            Long time = TimeConverter.minutesToMilis((Long)lockDownMinSpinner.getValue());
            ConfigElement el = new ConfigElement();
            el.setKey(ConfigurationEnum.LOCK_NOTIFY_MINUTES);
            el.setValue(String.valueOf(time));
            configService.add(el);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error getting values from spinners to db", e);
        }
    }
    
    private void handleMinLeftNotificationChange() {
        JToggleButton minLeftNotification = (JToggleButton)getObjectFromPanel(ConfigurationPanelEnum.NOTIFY_MIN_LEFT, JToggleButton.class).orElseThrow();
        boolean checked = minLeftNotification.isSelected();
        ConfigElement el = new ConfigElement();
        el.setKey(ConfigurationEnum.MIN_LEFT_BUTTON);
        el.setValue(String.valueOf(checked));
        configService.add(el);
    }
    
    private void handleMinLeftQtyNotificationChange() {
        JSpinner minLeftQty = (JSpinner)getObjectFromPanel(ConfigurationPanelEnum.NOTIFY_MIN_LEFT_QTY, JSpinner.class).orElseThrow();
        try {
            Long time = TimeConverter.minutesToMilis((Long)minLeftQty.getValue());
            ConfigElement el = new ConfigElement();
            el.setKey(ConfigurationEnum.MIN_LEFT_QTY);
            el.setValue(String.valueOf(time));
            configService.add(el);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error getting values from spinners to db", e);
        }
    }
    
}
