package devs.mrp.turkeydesktop.view.mainpanel;

import devs.mrp.turkeydesktop.common.TimeConverter;
import devs.mrp.turkeydesktop.database.config.ConfigElementService;
import devs.mrp.turkeydesktop.database.group.Group;
import devs.mrp.turkeydesktop.service.conditionchecker.ConditionChecker;
import devs.mrp.turkeydesktop.service.watchdog.WatchDog;
import devs.mrp.turkeydesktop.view.PanelHandler;
import devs.mrp.turkeydesktop.view.PanelHandlerData;
import devs.mrp.turkeydesktop.view.categorizeprocesspanel.CatProcessEnum;
import devs.mrp.turkeydesktop.view.categorizetitles.CategorizeTitlesEnum;
import devs.mrp.turkeydesktop.view.configuration.ConfigurationPanelEnum;
import devs.mrp.turkeydesktop.view.container.FactoryInitializer;
import devs.mrp.turkeydesktop.view.groups.GroupsEnum;
import devs.mrp.turkeydesktop.view.notcloseables.NotCloseablesEnum;
import devs.mrp.turkeydesktop.view.times.TimesEnum;
import java.awt.AWTEvent;
import javax.swing.JFrame;

public class MainPanelFactoryImpl implements MainPanelFactory {
    
    private FactoryInitializer factory;
    
    public MainPanelFactoryImpl(FactoryInitializer factory) {
        this.factory = factory;
    }
    
    @Override
    public FeedbackerPanelWithFetcher<MainEnum, AWTEvent> getMainPanel() {
        return new MainPanel();
    }
    
    @Override
    public PanelHandler<MainEnum, AWTEvent, FeedbackerPanelWithFetcher<MainEnum, AWTEvent>> getMainHandler(JFrame frame) {
        PanelHandlerData data = new PanelHandlerData(frame, null, null);
        return new MainHandler(data, this);
    }

    @Override
    public ConfigElementService getConfigElementService() {
        return factory.getConfigElementFactory().getService();
    }

    @Override
    public ConditionChecker getConditionChecker() {
        return factory.getConditionCheckerFactory().getConditionChecker();
    }

    @Override
    public WatchDog getWatchDog() {
        return factory.getWatchDogFactory().getInstance();
    }

    @Override
    public PanelHandler<GroupsEnum, AWTEvent, FeedbackerPanelWithFetcher<GroupsEnum, AWTEvent>> getGroupsHandler(PanelHandlerData<Group.GroupType> data) {
        return factory.getGroupsPanelFactory().getHandler(data);
    }

    @Override
    public TimeConverter getTimeConverter() {
        return factory.getTimeConverter();
    }

    @Override
    public PanelHandler<CatProcessEnum, AWTEvent, FeedbackerPanelWithFetcher<CatProcessEnum, AWTEvent>> getCategoryProcessHandler(PanelHandlerData<?> data) {
        return factory.getCatProcessPanelFactory().getHandler(data);
    }

    @Override
    public PanelHandler<CategorizeTitlesEnum, AWTEvent, FeedbackerPanelWithFetcher<CategorizeTitlesEnum, AWTEvent>> getCategorizedTitlesHandler(JFrame frame, PanelHandler<?, ?, ?> caller) {
        return factory.getCategorizeTitlesPanelFactory().getHandler(frame, caller);
    }

    @Override
    public PanelHandler<ConfigurationPanelEnum, AWTEvent, FeedbackerPanelWithFetcher<ConfigurationPanelEnum, AWTEvent>> getConfigurationPanelHandler(PanelHandlerData<?> data) {
        return factory.getConfigurationPanelFactory().getHandler(data);
    }

    @Override
    public PanelHandler<NotCloseablesEnum, Object, FeedbackerPanelWithFetcher<NotCloseablesEnum, Object>> getNotCloseablesHandler(JFrame frame, PanelHandler<?, ?, ?> caller) {
        return factory.getNotCloseablesPanelFactory().getHandler(frame, caller);
    }

    @Override
    public PanelHandler<TimesEnum, AWTEvent, FeedbackerPanelWithFetcher<TimesEnum, AWTEvent>> getTimesHandler(JFrame frame, PanelHandler<?, ?, ?> caller) {
        return factory.getTimesPanelFactory().getHandler(frame, caller);
    }
}
