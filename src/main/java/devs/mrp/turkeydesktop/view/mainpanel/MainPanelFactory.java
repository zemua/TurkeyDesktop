package devs.mrp.turkeydesktop.view.mainpanel;

import devs.mrp.turkeydesktop.common.TimeConverter;
import devs.mrp.turkeydesktop.database.config.ConfigElementService;
import devs.mrp.turkeydesktop.database.group.Group;
import devs.mrp.turkeydesktop.service.conditionchecker.ConditionChecker;
import devs.mrp.turkeydesktop.service.watchdog.WatchDog;
import devs.mrp.turkeydesktop.view.PanelHandler;
import devs.mrp.turkeydesktop.view.PanelHandlerData;
import devs.mrp.turkeydesktop.view.categorizeprocesspanel.CatProcessEnum;
import devs.mrp.turkeydesktop.view.categorizeprocesspanel.CatProcessPanelFactory;
import devs.mrp.turkeydesktop.view.categorizetitles.CategorizeTitlesEnum;
import devs.mrp.turkeydesktop.view.categorizetitles.CategorizeTitlesPanelFactory;
import devs.mrp.turkeydesktop.view.configuration.ConfigurationPanelEnum;
import devs.mrp.turkeydesktop.view.configuration.ConfigurationPanelFactory;
import devs.mrp.turkeydesktop.view.groups.GroupsEnum;
import devs.mrp.turkeydesktop.view.groups.GroupsPanelFactory;
import devs.mrp.turkeydesktop.view.notcloseables.NotCloseablesEnum;
import devs.mrp.turkeydesktop.view.notcloseables.NotCloseablesPanelFactory;
import devs.mrp.turkeydesktop.view.times.TimesEnum;
import devs.mrp.turkeydesktop.view.times.TimesPanelFactory;
import java.awt.AWTEvent;
import javax.swing.JFrame;

public interface MainPanelFactory {
    
    FeedbackerPanelWithFetcher<MainEnum, AWTEvent> getMainPanel();
    PanelHandler<MainEnum, AWTEvent, FeedbackerPanelWithFetcher<MainEnum, AWTEvent>, MainPanelFactory> getMainHandler(JFrame frame);
    ConfigElementService getConfigElementService();
    ConditionChecker getConditionChecker();
    WatchDog getWatchDog();
    PanelHandler<GroupsEnum, AWTEvent, FeedbackerPanelWithFetcher<GroupsEnum, AWTEvent>, GroupsPanelFactory> getGroupsHandler(PanelHandlerData<Group.GroupType> data);
    TimeConverter getTimeConverter();
    PanelHandler<CatProcessEnum, AWTEvent, FeedbackerPanelWithFetcher<CatProcessEnum, AWTEvent>, CatProcessPanelFactory> getCategoryProcessHandler(PanelHandlerData<?> data);
    PanelHandler<CategorizeTitlesEnum, AWTEvent, FeedbackerPanelWithFetcher<CategorizeTitlesEnum, AWTEvent>, CategorizeTitlesPanelFactory> getCategorizedTitlesHandler(JFrame frame, PanelHandler<?, ?, ?, ?> caller);
    PanelHandler<ConfigurationPanelEnum, AWTEvent, FeedbackerPanelWithFetcher<ConfigurationPanelEnum, AWTEvent>, ConfigurationPanelFactory> getConfigurationPanelHandler(PanelHandlerData<?> data);
    PanelHandler<NotCloseablesEnum,Object,FeedbackerPanelWithFetcher<NotCloseablesEnum,Object>, NotCloseablesPanelFactory> getNotCloseablesHandler(JFrame frame, PanelHandler<?,?,?,?> caller);
    PanelHandler<TimesEnum, AWTEvent, FeedbackerPanelWithFetcher<TimesEnum, AWTEvent>, TimesPanelFactory> getTimesHandler(JFrame frame, PanelHandler<?, ?, ?, ?> caller);
    
}
