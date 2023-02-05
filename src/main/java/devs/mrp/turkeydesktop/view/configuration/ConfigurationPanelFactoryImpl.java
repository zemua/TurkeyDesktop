package devs.mrp.turkeydesktop.view.configuration;

import devs.mrp.turkeydesktop.common.FileHandler;
import devs.mrp.turkeydesktop.common.TimeConverter;
import devs.mrp.turkeydesktop.database.config.ConfigElementService;
import devs.mrp.turkeydesktop.view.PanelHandler;
import devs.mrp.turkeydesktop.view.PanelHandlerData;
import devs.mrp.turkeydesktop.view.container.FactoryInitializer;
import devs.mrp.turkeydesktop.view.mainpanel.FeedbackerPanelWithFetcher;
import java.awt.AWTEvent;

public class ConfigurationPanelFactoryImpl implements ConfigurationPanelFactory {
    
    private FactoryInitializer factory;
    
    public ConfigurationPanelFactoryImpl(FactoryInitializer factory) {
        this.factory = factory;
    }
    
    @Override
    public FeedbackerPanelWithFetcher<ConfigurationPanelEnum, AWTEvent> getPanel() {
        return new ConfigurationPanel();
    }
    
    @Override
    public PanelHandler<ConfigurationPanelEnum, AWTEvent, FeedbackerPanelWithFetcher<ConfigurationPanelEnum, AWTEvent>> getHandler(PanelHandlerData<?> data) {
        return new ConfigurationHandler(data, this);
    }

    @Override
    public ConfigElementService getConfigElementService() {
        return factory.getConfigElementFactory().getService();
    }

    @Override
    public TimeConverter getTimeConverter() {
        return factory.getTimeConverter();
    }

    @Override
    public FileHandler getFileHandler() {
        return factory.getFileHandler();
    }
    
}
