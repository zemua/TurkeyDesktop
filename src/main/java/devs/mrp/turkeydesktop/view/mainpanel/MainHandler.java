package devs.mrp.turkeydesktop.view.mainpanel;

import devs.mrp.turkeydesktop.common.TimeConverter;
import devs.mrp.turkeydesktop.database.config.ConfigElementService;
import devs.mrp.turkeydesktop.database.group.Group;
import devs.mrp.turkeydesktop.i18n.LocaleMessages;
import devs.mrp.turkeydesktop.service.conditionchecker.ConditionChecker;
import devs.mrp.turkeydesktop.service.resourcehandler.ImagesEnum;
import devs.mrp.turkeydesktop.service.resourcehandler.ResourceHandler;
import devs.mrp.turkeydesktop.service.resourcehandler.ResourceHandlerFactory;
import devs.mrp.turkeydesktop.view.PanelHandler;
import devs.mrp.turkeydesktop.view.PanelHandlerData;
import devs.mrp.turkeydesktop.view.categorizeprocesspanel.CatProcessEnum;
import devs.mrp.turkeydesktop.view.categorizetitles.CategorizeTitlesEnum;
import devs.mrp.turkeydesktop.view.categorizetitles.CategorizeTitlesPanelFactoryImpl;
import devs.mrp.turkeydesktop.view.configuration.ConfigurationPanelEnum;
import devs.mrp.turkeydesktop.view.configuration.ConfigurationPanelFactoryImpl;
import devs.mrp.turkeydesktop.view.groups.GroupsEnum;
import devs.mrp.turkeydesktop.view.notcloseables.NotCloseablesEnum;
import devs.mrp.turkeydesktop.view.notcloseables.NotCloseablesPanelFactory;
import devs.mrp.turkeydesktop.view.times.FTimesPanel;
import devs.mrp.turkeydesktop.view.times.TimesEnum;
import java.awt.AWTEvent;
import java.awt.Image;
import javax.swing.JLabel;

public class MainHandler extends PanelHandler<MainEnum, AWTEvent, FeedbackerPanelWithFetcher<MainEnum, AWTEvent>> {
    
    public static final String MAIN_TITLE = LocaleMessages.getInstance().getString("timeturkey");
    
    private MainPanelFactory factory;
    
    PanelHandler<TimesEnum, AWTEvent, FeedbackerPanelWithFetcher<TimesEnum, AWTEvent>> timesHandler;
    PanelHandler<CatProcessEnum, AWTEvent, FeedbackerPanelWithFetcher<CatProcessEnum, AWTEvent>> categoryProcessHandler;
    PanelHandler<CategorizeTitlesEnum, AWTEvent, FeedbackerPanelWithFetcher<CategorizeTitlesEnum, AWTEvent>> categoryTitlesHandler;
    PanelHandler<GroupsEnum, AWTEvent, FeedbackerPanelWithFetcher<GroupsEnum, AWTEvent>> positiveGroupsHandler;
    PanelHandler<GroupsEnum, AWTEvent, FeedbackerPanelWithFetcher<GroupsEnum, AWTEvent>> negativeGroupsHandler;
    PanelHandler<ConfigurationPanelEnum, AWTEvent, FeedbackerPanelWithFetcher<ConfigurationPanelEnum, AWTEvent>> configHandler;
    PanelHandler<NotCloseablesEnum, Object, FeedbackerPanelWithFetcher<NotCloseablesEnum, Object>> notCloseableHandler;
    
    private ConfigElementService configService;
    private ConditionChecker conditionChecker;
    private ResourceHandler<Image,ImagesEnum> imageHandler;
    private TimeConverter timeConverter;

    public MainHandler(PanelHandlerData data, MainPanelFactory factory) {
        super(data.getFrame(), data.getCaller());
        this.factory = factory;
        this.configService = factory.getConfigElementService();
        this.conditionChecker = factory.getConditionChecker();
        this.timeConverter = factory.getTimeConverter();
    }
    
    @Override
    protected FeedbackerPanelWithFetcher<MainEnum, AWTEvent> initPanel() {
        this.getFrame().setTitle(MAIN_TITLE);
        imageHandler = ResourceHandlerFactory.getImagesHandler();
        this.getFrame().setIconImage(imageHandler.getResource(ImagesEnum.TURKEY));
        this.setPanel(factory.getMainPanel());
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
        // For an unknown reason there are some times when the watchdog is stopped during start.
        // This can re-start it when opening the main screen.
        factory.getWatchDog().begin();
    }
    
    private void initTimesHandler() {
        if (timesHandler == null) {
            timesHandler = FTimesPanel.getHandler(this.getFrame(), this);
        }
        timesHandler.show();
    }
    
    private void initCategorizeHandler() {
        if(categoryProcessHandler == null) {
            PanelHandlerData<?> data = new PanelHandlerData<>(this.getFrame(), this, null);
            categoryProcessHandler = factory.getCategoryProcessHandler(data);
        }
        categoryProcessHandler.show();
    }
    
    private void initCategorizeTitlesHandler() {
        if (categoryTitlesHandler == null) {
            categoryTitlesHandler = CategorizeTitlesPanelFactoryImpl.getHandler(this.getFrame(), this);
        }
        categoryTitlesHandler.show();
    }
    
    private void initPositiveGroupsHandler() {
        if (positiveGroupsHandler == null) {
            PanelHandlerData<Group.GroupType> data = new PanelHandlerData<>(this.getFrame(), this, Group.GroupType.POSITIVE);
            positiveGroupsHandler = factory.getGroupsHandler(data);
        }
        positiveGroupsHandler.show();
    }
    
    private void initNegativeGroupsHandler() {
        if (negativeGroupsHandler == null) {
            PanelHandlerData<Group.GroupType> data = new PanelHandlerData<>(this.getFrame(), this, Group.GroupType.NEGATIVE);
            negativeGroupsHandler = factory.getGroupsHandler(data);
        }
        negativeGroupsHandler.show();
    }
    
    private void initConfigHandler() {
        if (configHandler == null) {
            configHandler = ConfigurationPanelFactoryImpl.getHandler(this.getFrame(), this);
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
        conditionChecker.timeRemaining().subscribe(remaining -> {
            label.setText(timeConverter.millisToHMS(remaining));
        });
    }
    
    private void setupHeaderUpdater() {
        factory.getWatchDog().addFeedbacker((msg,data) -> {
            setTimeOnHeaderLabel();
        });
    }

    @Override
    protected void doBeforeExit() {
        // blank
    }
    
}
