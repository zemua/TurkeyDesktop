/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.view.mainpanel;

import devs.mrp.turkeydesktop.common.TimeConverter;
import devs.mrp.turkeydesktop.database.config.FConfigElementService;
import devs.mrp.turkeydesktop.database.config.IConfigElementService;
import devs.mrp.turkeydesktop.database.group.Group;
import devs.mrp.turkeydesktop.database.logs.TimeLogServiceFactory;
import devs.mrp.turkeydesktop.i18n.LocaleMessages;
import devs.mrp.turkeydesktop.service.conditionchecker.ConditionCheckerFactory;
import devs.mrp.turkeydesktop.view.PanelHandler;
import devs.mrp.turkeydesktop.view.categorizeprocesspanel.CatProcessEnum;
import devs.mrp.turkeydesktop.view.categorizeprocesspanel.CatProcessPanelFactory;
import devs.mrp.turkeydesktop.view.categorizetitles.CategorizeTitlesEnum;
import devs.mrp.turkeydesktop.view.categorizetitles.CategorizeTitlesPanelFactory;
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
import devs.mrp.turkeydesktop.service.conditionchecker.ConditionChecker;
import devs.mrp.turkeydesktop.database.logs.TimeLogService;
import devs.mrp.turkeydesktop.service.resourcehandler.ImagesEnum;
import devs.mrp.turkeydesktop.service.resourcehandler.ResourceHandler;
import devs.mrp.turkeydesktop.service.resourcehandler.ResourceHandlerFactory;
import devs.mrp.turkeydesktop.service.watchdog.WatchDogFactory;
import devs.mrp.turkeydesktop.view.notcloseables.NotCloseablesEnum;
import devs.mrp.turkeydesktop.view.notcloseables.NotCloseablesPanelFactory;
import java.awt.Image;

/**
 *
 * @author miguel
 */
public class MainHandler extends PanelHandler<MainEnum, AWTEvent, FeedbackerPanelWithFetcher<MainEnum, AWTEvent>> {
    
    public static final String MAIN_TITLE = LocaleMessages.getInstance().getString("timeturkey");
    
    PanelHandler<TimesEnum, AWTEvent, FeedbackerPanelWithFetcher<TimesEnum, AWTEvent>> timesHandler;
    PanelHandler<CatProcessEnum, AWTEvent, FeedbackerPanelWithFetcher<CatProcessEnum, AWTEvent>> categoryProcessHandler;
    PanelHandler<CategorizeTitlesEnum, AWTEvent, FeedbackerPanelWithFetcher<CategorizeTitlesEnum, AWTEvent>> categoryTitlesHandler;
    PanelHandler<GroupsEnum, AWTEvent, FeedbackerPanelWithFetcher<GroupsEnum, AWTEvent>> positiveGroupsHandler;
    PanelHandler<GroupsEnum, AWTEvent, FeedbackerPanelWithFetcher<GroupsEnum, AWTEvent>> negativeGroupsHandler;
    PanelHandler<ConfigurationPanelEnum, AWTEvent, FeedbackerPanelWithFetcher<ConfigurationPanelEnum, AWTEvent>> configHandler;
    PanelHandler<NotCloseablesEnum, Object, FeedbackerPanelWithFetcher<NotCloseablesEnum, Object>> notCloseableHandler;
    
    private TimeLogService timeLogService = TimeLogServiceFactory.getService();
    private IConfigElementService configService = FConfigElementService.getService();
    private ConditionChecker conditionChecker = ConditionCheckerFactory.getConditionChecker();
    private ResourceHandler<Image,ImagesEnum> imageHandler;

    public MainHandler(JFrame frame, PanelHandler<?,?, ?> caller) {
        super(frame, caller);
    }
    
    @Override
    protected FeedbackerPanelWithFetcher<MainEnum, AWTEvent> initPanel() {
        this.getFrame().setTitle(MAIN_TITLE);
        imageHandler = ResourceHandlerFactory.getImagesHandler();
        this.getFrame().setIconImage(imageHandler.getResource(ImagesEnum.TURKEY));
        this.setPanel(MainPanelFactory.getMainPanel());
        setupHeaderUpdater();
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
                case NOT_CLOSEABLES:
                    initNotCloseablesHandler();
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
            categoryProcessHandler = CatProcessPanelFactory.getHandler(this.getFrame(), this);
        }
        categoryProcessHandler.show();
    }
    
    private void initCategorizeTitlesHandler() {
        if (categoryTitlesHandler == null) {
            categoryTitlesHandler = CategorizeTitlesPanelFactory.getHandler(this.getFrame(), this);
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
    
    private void initNotCloseablesHandler() {
        if (notCloseableHandler == null) {
            notCloseableHandler = NotCloseablesPanelFactory.getHandler(this.getFrame(), this);
        }
        notCloseableHandler.show();
    }
    
    private void setTimeOnHeaderLabel() {
        JLabel label = (JLabel)this.getPanel().getProperty(MainEnum.LABELIZER);
        label.setText(TimeConverter.millisToHMS(conditionChecker.timeRemaining()));
    }
    
    private void setupHeaderUpdater() {
        WatchDogFactory.getInstance().addFeedbacker((msg,data) -> {
            setTimeOnHeaderLabel();
        });
    }

    @Override
    protected void doBeforeExit() {
        // blank
    }
    
}
