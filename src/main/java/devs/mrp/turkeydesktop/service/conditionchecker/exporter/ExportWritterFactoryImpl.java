package devs.mrp.turkeydesktop.service.conditionchecker.exporter;

import devs.mrp.turkeydesktop.common.FileHandler;
import devs.mrp.turkeydesktop.common.TimeConverter;
import devs.mrp.turkeydesktop.common.factory.CommonBeans;
import devs.mrp.turkeydesktop.database.group.expor.ExportedGroupFactoryImpl;
import devs.mrp.turkeydesktop.database.group.expor.ExportedGroupService;
import devs.mrp.turkeydesktop.database.logs.TimeLogFactoryImpl;
import devs.mrp.turkeydesktop.database.logs.TimeLogService;

public class ExportWritterFactoryImpl implements ExportWritterFactory {
    
    private static ExportWritterFactoryImpl instance;
    
    private ExportWritterFactoryImpl() {}
    
    public static ExportWritterFactoryImpl getInstance() {
        if (instance == null) {
            instance = new ExportWritterFactoryImpl();
        }
        return instance;
    }
    
    @Override
    public ExportWritter getWritter() {
        return new ExportWritterImpl(this);
    }

    @Override
    public ExportedGroupService getExportedGroupService() {
        return ExportedGroupFactoryImpl.getInstance().getService();
    }

    @Override
    public TimeLogService getTimeLogService() {
        return TimeLogFactoryImpl.getInstance().getService();
    }

    @Override
    public FileHandler getFileHandler() {
        return CommonBeans.getFileHandler();
    }

    @Override
    public TimeConverter getTimeConverter() {
        return CommonBeans.getTimeConverter();
    }
    
}
