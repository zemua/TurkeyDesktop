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
import devs.mrp.turkeydesktop.view.FeedbackerPanel;
import devs.mrp.turkeydesktop.view.PanelHandler;
import devs.mrp.turkeydesktop.view.mainpanel.FeedbackerPanelWithFetcher;
import java.awt.AWTEvent;
import javax.swing.JFrame;
import javax.swing.JSlider;

/**
 *
 * @author miguel
 */
public class ConfigurationHandler extends PanelHandler<ConfigurationPanelEnum, AWTEvent, FeedbackerPanelWithFetcher<ConfigurationPanelEnum, AWTEvent>> {
    
    private IConfigElementService configService = FConfigElementService.getService();
    private LocaleMessages localeMessages = LocaleMessages.getInstance();

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
    
}
