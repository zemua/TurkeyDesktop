package devs.mrp.turkeydesktop.view.mainpanel;

import devs.mrp.turkeydesktop.common.TimeConverter;
import devs.mrp.turkeydesktop.common.factory.CommonBeans;
import devs.mrp.turkeydesktop.database.config.ConfigElementFactoryImpl;
import devs.mrp.turkeydesktop.database.config.ConfigElementService;
import devs.mrp.turkeydesktop.database.group.Group;
import devs.mrp.turkeydesktop.service.conditionchecker.ConditionChecker;
import devs.mrp.turkeydesktop.service.conditionchecker.ConditionCheckerFactoryImpl;
import devs.mrp.turkeydesktop.service.watchdog.WatchDog;
import devs.mrp.turkeydesktop.service.watchdog.WatchDogFactoryImpl;
import devs.mrp.turkeydesktop.view.PanelHandler;
import devs.mrp.turkeydesktop.view.PanelHandlerData;
import devs.mrp.turkeydesktop.view.categorizeprocesspanel.CatProcessEnum;
import devs.mrp.turkeydesktop.view.categorizeprocesspanel.CatProcessPanelFactory;
import devs.mrp.turkeydesktop.view.categorizeprocesspanel.CatProcessPanelFactoryImpl;
import devs.mrp.turkeydesktop.view.categorizetitles.CategorizeTitlesEnum;
import devs.mrp.turkeydesktop.view.categorizetitles.CategorizeTitlesPanelFactory;
import devs.mrp.turkeydesktop.view.categorizetitles.CategorizeTitlesPanelFactoryImpl;
import devs.mrp.turkeydesktop.view.configuration.ConfigurationPanelEnum;
import devs.mrp.turkeydesktop.view.configuration.ConfigurationPanelFactory;
import devs.mrp.turkeydesktop.view.configuration.ConfigurationPanelFactoryImpl;
import devs.mrp.turkeydesktop.view.groups.GroupsEnum;
import devs.mrp.turkeydesktop.view.groups.GroupsPanelFactory;
import devs.mrp.turkeydesktop.view.groups.GroupsPanelFactoryImpl;
import devs.mrp.turkeydesktop.view.notcloseables.NotCloseablesEnum;
import devs.mrp.turkeydesktop.view.notcloseables.NotCloseablesPanelFactory;
import devs.mrp.turkeydesktop.view.notcloseables.NotCloseablesPanelFactoryImpl;
import devs.mrp.turkeydesktop.view.times.TimesEnum;
import devs.mrp.turkeydesktop.view.times.TimesPanelFactory;
import devs.mrp.turkeydesktop.view.times.TimesPanelFactoryImpl;
import java.awt.AWTEvent;
import javax.swing.JFrame;

public class MainPanelFactoryImpl implements MainPanelFactory {
    
    private static MainPanelFactoryImpl instance;
    
    private MainPanelFactoryImpl() {
    }
    
    public static MainPanelFactoryImpl getInstance() {
        if (instance == null) {
            instance = new MainPanelFactoryImpl();
        }
        return instance;
    }
    
    @Override
    public FeedbackerPanelWithFetcher<MainEnum, AWTEvent> getMainPanel() {
        return new MainPanel();
    }
    
    @Override
    public PanelHandler<MainEnum, AWTEvent, FeedbackerPanelWithFetcher<MainEnum, AWTEvent>, MainPanelFactory> getMainHandler(JFrame frame) {
        PanelHandlerData data = new PanelHandlerData(frame, null, null);
        return new MainHandler(data, this);
    }

    @Override
    public ConfigElementService getConfigElementService() {
        return ConfigElementFactoryImpl.getInstance().getService();
    }

    @Override
    public ConditionChecker getConditionChecker() {
        return ConditionCheckerFactoryImpl.getInstance().getConditionChecker();
    }

    @Override
    public WatchDog getWatchDog() {
        return WatchDogFactoryImpl.getInstance().getWatchDog();
    }

    @Override
    public PanelHandler<GroupsEnum, AWTEvent, FeedbackerPanelWithFetcher<GroupsEnum, AWTEvent>, GroupsPanelFactory> getGroupsHandler(PanelHandlerData<Group.GroupType> data) {
        return GroupsPanelFactoryImpl.getInstance().getHandler(data);
    }

    @Override
    public TimeConverter getTimeConverter() {
        return CommonBeans.getTimeConverter();
    }

    @Override
    public PanelHandler<CatProcessEnum, AWTEvent, FeedbackerPanelWithFetcher<CatProcessEnum, AWTEvent>, CatProcessPanelFactory> getCategoryProcessHandler(PanelHandlerData<?> data) {
        return CatProcessPanelFactoryImpl.getInstance().getHandler(data);
    }

    @Override
    public PanelHandler<CategorizeTitlesEnum, AWTEvent, FeedbackerPanelWithFetcher<CategorizeTitlesEnum, AWTEvent>, CategorizeTitlesPanelFactory> getCategorizedTitlesHandler(JFrame frame, PanelHandler<?, ?, ?, ?> caller) {
        return CategorizeTitlesPanelFactoryImpl.getInstance().getHandler(frame, caller);
    }

    @Override
    public PanelHandler<ConfigurationPanelEnum, AWTEvent, FeedbackerPanelWithFetcher<ConfigurationPanelEnum, AWTEvent>, ConfigurationPanelFactory> getConfigurationPanelHandler(PanelHandlerData<?> data) {
        return ConfigurationPanelFactoryImpl.getInstance().getHandler(data);
    }

    @Override
    public PanelHandler<NotCloseablesEnum, Object, FeedbackerPanelWithFetcher<NotCloseablesEnum, Object>, NotCloseablesPanelFactory> getNotCloseablesHandler(JFrame frame, PanelHandler<?, ?, ?, ?> caller) {
        return NotCloseablesPanelFactoryImpl.getInstance().getHandler(frame, caller);
    }

    @Override
    public PanelHandler<TimesEnum, AWTEvent, FeedbackerPanelWithFetcher<TimesEnum, AWTEvent>, TimesPanelFactory> getTimesHandler(JFrame frame, PanelHandler<?, ?, ?, ?> caller) {
        return TimesPanelFactoryImpl.getInstance().getHandler(frame, caller);
    }
}
