/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.view.mainpanel;

import devs.mrp.turkeydesktop.common.TimeConverter;
import devs.mrp.turkeydesktop.database.config.ConfigElement;
import devs.mrp.turkeydesktop.database.config.FConfigElementService;
import devs.mrp.turkeydesktop.database.config.IConfigElementService;
import devs.mrp.turkeydesktop.database.logs.FTimeLogService;
import devs.mrp.turkeydesktop.database.logs.ITimeLogService;
import devs.mrp.turkeydesktop.database.logs.TimeLog;
import devs.mrp.turkeydesktop.database.logs.TimeLogService;
import devs.mrp.turkeydesktop.i18n.LocaleMessages;
import devs.mrp.turkeydesktop.view.PanelHandler;
import devs.mrp.turkeydesktop.view.categorizeprocesspanel.CatProcessEnum;
import devs.mrp.turkeydesktop.view.categorizeprocesspanel.FCatProcessPanel;
import devs.mrp.turkeydesktop.view.configuration.ConfigurationEnum;
import devs.mrp.turkeydesktop.view.configuration.ConfigurationPanelEnum;
import devs.mrp.turkeydesktop.view.configuration.FConfigurationPanel;
import devs.mrp.turkeydesktop.view.times.FTimesPanel;
import devs.mrp.turkeydesktop.view.times.TimesEnum;
import java.awt.AWTEvent;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author miguel
 */
public class MainHandler extends PanelHandler<MainEnum, AWTEvent, FeedbackerPanelWithFetcher<MainEnum, AWTEvent>> {
    
    private static final String MAIN_TITLE = LocaleMessages.getInstance().getString("timeturkey");
    private static final String TURKEY_IMG = "turkey.png";
    
    PanelHandler<TimesEnum, AWTEvent, FeedbackerPanelWithFetcher<TimesEnum, AWTEvent>> timesHandler;
    PanelHandler<CatProcessEnum, AWTEvent, FeedbackerPanelWithFetcher<CatProcessEnum, AWTEvent>> categoryProcessHandler;
    PanelHandler<ConfigurationPanelEnum, AWTEvent, FeedbackerPanelWithFetcher<ConfigurationPanelEnum, AWTEvent>> configHandler;
    
    private ITimeLogService timeLogService = FTimeLogService.getService();
    private IConfigElementService configService = FConfigElementService.getService();

    public MainHandler(JFrame frame, PanelHandler<?,?, ?> caller) {
        super(frame, caller);
    }
    
    @Override
    protected FeedbackerPanelWithFetcher<MainEnum, AWTEvent> initPanel() {
        this.getFrame().setTitle(MAIN_TITLE);
        this.getFrame().setIconImage(Toolkit.getDefaultToolkit().getImage(TURKEY_IMG));
        this.setPanel(FMainPanel.getMainPanel());
        return this.getPanel();
    }

    @Override
    protected void initListeners(FeedbackerPanelWithFetcher<MainEnum, AWTEvent> pan) {
        pan.addFeedbackListener((tipo, feedback) -> {
            switch (tipo) {
                case CATEGORIZE:
                    initCategorizeHandler();
                    break;
                case TIMES:
                    initTimesHandler();
                    break;
                case CONFIG:
                    initConfigHandler();
                    break;
                default:
                    break;
            }
        });
    }
    
    @Override
    protected void doExtraBeforeShow() {
        setTimeOnHeaderLabel();
        this.getPanel().revalidate();
        this.getPanel().updateUI();
    }
    
    private void initTimesHandler() {
        if (timesHandler == null) {
            timesHandler = FTimesPanel.getHandler(this.getFrame(), this);
        }
        timesHandler.show();
    }
    
    private void initCategorizeHandler() {
        if(categoryProcessHandler == null) {
            categoryProcessHandler = FCatProcessPanel.getHandler(this.getFrame(), this);
        }
        categoryProcessHandler.show();
    }
    
    private void initConfigHandler() {
        if (configHandler == null) {
            configHandler = FConfigurationPanel.getHandler(this.getFrame(), this);
        }
        configHandler.show();
    }
    
    private void setTimeOnHeaderLabel() {
        TimeLog el = timeLogService.findMostRecent();
        ConfigElement cel = configService.configElement(ConfigurationEnum.PROPORTION);
        JLabel label = (JLabel)this.getPanel().getProperty(MainEnum.LABELIZER);
        long proportion = Integer.valueOf(cel.getValue());
        long accumulated = el.getAccumulated();
        label.setText(TimeConverter.millisToHMS(accumulated/proportion));
    }
    
}
