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
import devs.mrp.turkeydesktop.database.group.Group;
import devs.mrp.turkeydesktop.database.logs.FTimeLogService;
import devs.mrp.turkeydesktop.database.logs.ITimeLogService;
import devs.mrp.turkeydesktop.database.logs.TimeLog;
import devs.mrp.turkeydesktop.i18n.LocaleMessages;
import devs.mrp.turkeydesktop.service.conditionchecker.FConditionChecker;
import devs.mrp.turkeydesktop.service.conditionchecker.IConditionChecker;
import devs.mrp.turkeydesktop.view.PanelHandler;
import devs.mrp.turkeydesktop.view.categorizeprocesspanel.CatProcessEnum;
import devs.mrp.turkeydesktop.view.categorizeprocesspanel.FCatProcessPanel;
import devs.mrp.turkeydesktop.view.categorizetitles.CategorizeTitlesEnum;
import devs.mrp.turkeydesktop.view.categorizetitles.FCategorizeTitlesPanel;
import devs.mrp.turkeydesktop.view.configuration.ConfigurationEnum;
import devs.mrp.turkeydesktop.view.configuration.ConfigurationPanelEnum;
import devs.mrp.turkeydesktop.view.configuration.FConfigurationPanel;
import devs.mrp.turkeydesktop.view.groups.FGroupsPanel;
import devs.mrp.turkeydesktop.view.groups.GroupsEnum;
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
    private static final String TURKEY_IMG = "/turkey.png";
    
    PanelHandler<TimesEnum, AWTEvent, FeedbackerPanelWithFetcher<TimesEnum, AWTEvent>> timesHandler;
    PanelHandler<CatProcessEnum, AWTEvent, FeedbackerPanelWithFetcher<CatProcessEnum, AWTEvent>> categoryProcessHandler;
    PanelHandler<CategorizeTitlesEnum, AWTEvent, FeedbackerPanelWithFetcher<CategorizeTitlesEnum, AWTEvent>> categoryTitlesHandler;
    PanelHandler<GroupsEnum, AWTEvent, FeedbackerPanelWithFetcher<GroupsEnum, AWTEvent>> positiveGroupsHandler;
    PanelHandler<GroupsEnum, AWTEvent, FeedbackerPanelWithFetcher<GroupsEnum, AWTEvent>> negativeGroupsHandler;
    PanelHandler<ConfigurationPanelEnum, AWTEvent, FeedbackerPanelWithFetcher<ConfigurationPanelEnum, AWTEvent>> configHandler;
    
    private ITimeLogService timeLogService = FTimeLogService.getService();
    private IConfigElementService configService = FConfigElementService.getService();
    private IConditionChecker conditionChecker = FConditionChecker.getConditionChecker();

    public MainHandler(JFrame frame, PanelHandler<?,?, ?> caller) {
        super(frame, caller);
    }
    
    @Override
    protected FeedbackerPanelWithFetcher<MainEnum, AWTEvent> initPanel() {
        this.getFrame().setTitle(MAIN_TITLE);
        this.getFrame().setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource(TURKEY_IMG)));
        this.setPanel(FMainPanel.getMainPanel());
        return this.getPanel();
    }

    @Override
    protected void initListeners(FeedbackerPanelWithFetcher<MainEnum, AWTEvent> pan) {
        pan.addFeedbackListener((tipo, feedback) -> {
            switch (tipo) {
                case CATEGORIZEPROCESS:
                    initCategorizeHandler();
                    break;
                case CATEGORIZETITLES:
                    initCategorizeTitlesHandler();
                    break;
                case POSITIVE_GROUPS:
                    initPositiveGroupsHandler();
                    break;
                case NEGATIVE_GROUPS:
                    initNegativeGroupsHandler();
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
    
    private void initCategorizeTitlesHandler() {
        if (categoryTitlesHandler == null) {
            categoryTitlesHandler = FCategorizeTitlesPanel.getHandler(this.getFrame(), this);
        }
        categoryTitlesHandler.show();
    }
    
    private void initPositiveGroupsHandler() {
        if (positiveGroupsHandler == null) {
            positiveGroupsHandler = FGroupsPanel.getHandler(this.getFrame(), this, Group.GroupType.POSITIVE);
        }
        positiveGroupsHandler.show();
    }
    
    private void initNegativeGroupsHandler() {
        if (negativeGroupsHandler == null) {
            negativeGroupsHandler = FGroupsPanel.getHandler(this.getFrame(), this, Group.GroupType.NEGATIVE);
        }
        negativeGroupsHandler.show();
    }
    
    private void initConfigHandler() {
        if (configHandler == null) {
            configHandler = FConfigurationPanel.getHandler(this.getFrame(), this);
        }
        configHandler.show();
    }
    
    private void setTimeOnHeaderLabel() {
        JLabel label = (JLabel)this.getPanel().getProperty(MainEnum.LABELIZER);
        label.setText(TimeConverter.millisToHMS(conditionChecker.timeRemaining()));
    }
    
}
