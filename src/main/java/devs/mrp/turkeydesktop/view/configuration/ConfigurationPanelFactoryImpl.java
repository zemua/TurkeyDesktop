package devs.mrp.turkeydesktop.view.configuration;

import devs.mrp.turkeydesktop.common.FileHandler;
import devs.mrp.turkeydesktop.common.TimeConverter;
import devs.mrp.turkeydesktop.common.factory.CommonBeans;
import devs.mrp.turkeydesktop.database.config.ConfigElementFactoryImpl;
import devs.mrp.turkeydesktop.database.config.ConfigElementService;
import devs.mrp.turkeydesktop.database.imports.ImportFactoryImpl;
import devs.mrp.turkeydesktop.database.imports.ImportService;
import devs.mrp.turkeydesktop.view.PanelHandler;
import devs.mrp.turkeydesktop.view.PanelHandlerData;
import devs.mrp.turkeydesktop.view.mainpanel.FeedbackerPanelWithFetcher;
import java.awt.AWTEvent;

public class ConfigurationPanelFactoryImpl implements ConfigurationPanelFactory {
    
    private static ConfigurationPanelFactoryImpl instance;
    
    private ConfigurationPanelFactoryImpl() {}
    
    public static ConfigurationPanelFactoryImpl getInstance() {
        if (instance == null) {
            instance = new ConfigurationPanelFactoryImpl();
        }
        return instance;
    }
    
    @Override
    public FeedbackerPanelWithFetcher<ConfigurationPanelEnum, AWTEvent> getPanel() {
        return new ConfigurationPanel();
    }
    
    @Override
    public PanelHandler<ConfigurationPanelEnum, AWTEvent, FeedbackerPanelWithFetcher<ConfigurationPanelEnum, AWTEvent>, ConfigurationPanelFactory> getHandler(PanelHandlerData<?> data) {
        return new ConfigurationHandler(data, this);
    }

    @Override
    public ConfigElementService getConfigElementService() {
        return ConfigElementFactoryImpl.getInstance().getService();
    }

    @Override
    public TimeConverter getTimeConverter() {
        return CommonBeans.getTimeConverter();
    }

    @Override
    public FileHandler getFileHandler() {
        return CommonBeans.getFileHandler();
    }

    @Override
    public ImportService getImportService() {
        return ImportFactoryImpl.getInstance().getService();
    }
    
}
