package devs.mrp.turkeydesktop.service.conditionchecker;

import devs.mrp.turkeydesktop.common.ChainHandler;
import devs.mrp.turkeydesktop.common.FileHandler;
import devs.mrp.turkeydesktop.common.TimeConverter;
import devs.mrp.turkeydesktop.database.conditions.ConditionService;
import devs.mrp.turkeydesktop.database.config.ConfigElementService;
import devs.mrp.turkeydesktop.database.group.external.ExternalGroupService;
import devs.mrp.turkeydesktop.database.imports.ImportService;
import devs.mrp.turkeydesktop.database.logs.TimeLogService;
import devs.mrp.turkeydesktop.service.conditionchecker.idle.IdleChainCommander;
import devs.mrp.turkeydesktop.service.conditionchecker.idle.LongWrapper;
import devs.mrp.turkeydesktop.service.conditionchecker.imports.ImportReader;
import devs.mrp.turkeydesktop.service.conditionchecker.imports.ImportReaderFactory;
import devs.mrp.turkeydesktop.service.toaster.Toaster;
import devs.mrp.turkeydesktop.view.container.FactoryInitializer;

public class ConditionCheckerFactoryImpl implements ConditionCheckerFactory {
    
    private FactoryInitializer factory;
    
    public ConditionCheckerFactoryImpl(FactoryInitializer factories) {
        this.factory = factories;
    }
    
    @Override
    public ConfigElementService getConfigElementService() {
        return factory.getConfigElementFactory().getService();
    }
    
    @Override
    public ConditionChecker getConditionChecker() {
        return new ConditionCheckerImpl(this);
    }

    @Override
    public Toaster getToaster() {
        return factory.getToaster();
    }

    @Override
    public TimeConverter getTimeConverter() {
        return factory.getTimeConverter();
    }

    @Override
    public FileHandler fileHandler() {
        return factory.getFileHandler();
    }

    @Override
    public ConditionService getConditionService() {
        return factory.getConditionFactory().getService();
    }

    @Override
    public TimeLogService getTimeLogService() {
        return factory.getTimeLogServiceFactory().getService();
    }

    @Override
    public ImportService getImportService() {
        return factory.getImportFactory().getService();
    }

    @Override
    public ChainHandler<LongWrapper> gettIdleHandler() {
        return new IdleChainCommander().getHandlerChain();
    }

    @Override
    public ExternalGroupService getExternalGroupService() {
        return factory.getExternalGroupFactory().getService();
    }

    @Override
    public ImportReader getImportReader() {
        return ImportReaderFactory.getReader();
    }
}
