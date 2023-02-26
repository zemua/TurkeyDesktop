package devs.mrp.turkeydesktop.service.conditionchecker;

import devs.mrp.turkeydesktop.common.ChainHandler;
import devs.mrp.turkeydesktop.common.FileHandler;
import devs.mrp.turkeydesktop.common.TimeConverter;
import devs.mrp.turkeydesktop.common.factory.CommonBeans;
import devs.mrp.turkeydesktop.database.conditions.ConditionFactoryImpl;
import devs.mrp.turkeydesktop.database.conditions.ConditionService;
import devs.mrp.turkeydesktop.database.config.ConfigElementFactoryImpl;
import devs.mrp.turkeydesktop.database.config.ConfigElementService;
import devs.mrp.turkeydesktop.database.group.external.ExternalGroupFactoryImpl;
import devs.mrp.turkeydesktop.database.group.external.ExternalGroupService;
import devs.mrp.turkeydesktop.database.imports.ImportFactoryImpl;
import devs.mrp.turkeydesktop.database.imports.ImportService;
import devs.mrp.turkeydesktop.database.logs.TimeLogFactoryImpl;
import devs.mrp.turkeydesktop.database.logs.TimeLogService;
import devs.mrp.turkeydesktop.service.conditionchecker.idle.IdleChainCommander;
import devs.mrp.turkeydesktop.service.conditionchecker.idle.LongWrapper;
import devs.mrp.turkeydesktop.service.conditionchecker.imports.ImportReader;
import devs.mrp.turkeydesktop.service.conditionchecker.imports.ImportReaderFactoryImpl;
import devs.mrp.turkeydesktop.service.toaster.Toaster;

public class ConditionCheckerFactoryImpl implements ConditionCheckerFactory {
    
    private static ConditionCheckerFactoryImpl instance;
    
    private ConditionCheckerFactoryImpl(){}
    
    public static ConditionCheckerFactoryImpl getInstance() {
        if (instance == null) {
            instance = new ConditionCheckerFactoryImpl();
        }
        return instance;
    }
    
    @Override
    public ConfigElementService getConfigElementService() {
        return ConfigElementFactoryImpl.getInstance().getService();
    }
    
    @Override
    public ConditionChecker getConditionChecker() {
        return new ConditionCheckerImpl(this);
    }

    @Override
    public Toaster getToaster() {
        return CommonBeans.getToaster();
    }

    @Override
    public TimeConverter getTimeConverter() {
        return CommonBeans.getTimeConverter();
    }

    @Override
    public FileHandler fileHandler() {
        return CommonBeans.getFileHandler();
    }

    @Override
    public ConditionService getConditionService() {
        return ConditionFactoryImpl.getInstance().getService();
    }

    @Override
    public TimeLogService getTimeLogService() {
        return TimeLogFactoryImpl.getInstance().getService();
    }

    @Override
    public ImportService getImportService() {
        return ImportFactoryImpl.getInstance().getService();
    }

    @Override
    public ChainHandler<LongWrapper> gettIdleHandler() {
        return new IdleChainCommander().getHandlerChain();
    }

    @Override
    public ExternalGroupService getExternalGroupService() {
        return ExternalGroupFactoryImpl.getInstance().getService();
    }

    @Override
    public ImportReader getImportReader() {
        return ImportReaderFactoryImpl.getInstance().getReader();
    }
}
