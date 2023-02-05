package devs.mrp.turkeydesktop.view.configuration;

import devs.mrp.turkeydesktop.common.FileHandler;
import devs.mrp.turkeydesktop.common.TimeConverter;
import devs.mrp.turkeydesktop.database.config.ConfigElementService;
import devs.mrp.turkeydesktop.view.PanelHandler;
import devs.mrp.turkeydesktop.view.PanelHandlerData;
import devs.mrp.turkeydesktop.view.mainpanel.FeedbackerPanelWithFetcher;
import java.awt.AWTEvent;

public interface ConfigurationPanelFactory {
    
    FeedbackerPanelWithFetcher<ConfigurationPanelEnum, AWTEvent> getPanel();
    PanelHandler<ConfigurationPanelEnum, AWTEvent, FeedbackerPanelWithFetcher<ConfigurationPanelEnum, AWTEvent>> getHandler(PanelHandlerData<?> data);
    ConfigElementService getConfigElementService();
    TimeConverter getTimeConverter();
    FileHandler getFileHandler();
    
}
