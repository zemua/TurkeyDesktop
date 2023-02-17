package devs.mrp.turkeydesktop.service.conditionchecker.exporter;

import devs.mrp.turkeydesktop.common.FileHandler;
import devs.mrp.turkeydesktop.common.TimeConverter;
import devs.mrp.turkeydesktop.database.group.expor.ExportedGroupService;
import devs.mrp.turkeydesktop.database.logs.TimeLogService;
import devs.mrp.turkeydesktop.view.container.FactoryInitializer;

public class ExportWritterFactoryImpl implements ExportWritterFactory {
    
    private FactoryInitializer factory;
    
    public ExportWritterFactoryImpl(FactoryInitializer factoryInitializer) {
        this.factory = factoryInitializer;
    }
    
    @Override
    public ExportWritter getWritter() {
        return new ExportWritterImpl(this);
    }

    @Override
    public ExportedGroupService getExportedGroupService() {
        return factory.getExportedGroupFactory().getService();
    }

    @Override
    public TimeLogService getTimeLogService() {
        return factory.getTimeLogServiceFactory().getService();
    }

    @Override
    public FileHandler getFileHandler() {
        return factory.getFileHandler();
    }

    @Override
    public TimeConverter getTimeConverter() {
        return factory.getTimeConverter();
    }
    
}
