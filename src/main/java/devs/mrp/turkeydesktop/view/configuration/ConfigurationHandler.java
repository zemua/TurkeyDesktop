/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.view.configuration;

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
                default:
                    break;
            }
        });
    }

    @Override
    protected void doExtraBeforeShow() {
        setupProportion();
    }
    
    private void setupProportion() {
        int proportion = Integer.valueOf(configService.configElement(ConfigurationEnum.PROPORTION).getValue());
        JSlider slider = (JSlider) this.getPanel().getProperty(ConfigurationPanelEnum.PROPORTION);
        slider.setValue(proportion);
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
        el.setValue(String.valueOf(el));
        configService.add(el);
    }
    
    private void handleLockdownFromChange() {
        JSpinner lockDownHourSpinner = (JSpinner)getObjectFromPanel(ConfigurationPanelEnum.LOCKDOWN_FROM_HOUR, JSpinner.class).orElseThrow();
        JSpinner lockDownMinSpinner = (JSpinner)getObjectFromPanel(ConfigurationPanelEnum.LOCKDOWN_FROM_MIN, JSpinner.class).orElseThrow();
        try {
            Long time = 60*1000*(Long)lockDownMinSpinner.getValue();
            time += 60*60*1000*(Long)lockDownHourSpinner.getValue();
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
            Long time = 60*1000*(Long)lockDownMinSpinner.getValue();
            time += 60*60*1000*(Long)lockDownHourSpinner.getValue();
            ConfigElement el = new ConfigElement();
            el.setKey(ConfigurationEnum.LOCKDOWN_TO);
            el.setValue(String.valueOf(time));
            configService.add(el);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error getting values from spinners to db", e);
        }
    }
    
}
