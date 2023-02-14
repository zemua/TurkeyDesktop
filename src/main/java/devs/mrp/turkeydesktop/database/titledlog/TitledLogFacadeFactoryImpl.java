package devs.mrp.turkeydesktop.database.titledlog;

import devs.mrp.turkeydesktop.common.GenericCache;
import devs.mrp.turkeydesktop.common.TimeConverter;
import devs.mrp.turkeydesktop.common.impl.GenericCacheImpl;
import devs.mrp.turkeydesktop.database.Db;
import devs.mrp.turkeydesktop.database.logs.TimeLogService;
import devs.mrp.turkeydesktop.database.titles.TitleService;
import devs.mrp.turkeydesktop.view.container.FactoryInitializer;

public class TitledLogFacadeFactoryImpl implements TitledLogFacadeFactory {
    
    private FactoryInitializer factory;
    private static TitledLogServiceFacade titledLogServiceFacade;
    private static TitledLogDaoFacade titledLogRepoFacade;
    
    public TitledLogFacadeFactoryImpl(FactoryInitializer factory) {
        this.factory = factory;
    }
    
    @Override
    public TitledLogServiceFacade getService() {
        if (titledLogServiceFacade == null) {
            titledLogServiceFacade = new TitledLogServiceFacadeImpl(this);
        }
        return titledLogServiceFacade;
    }

    @Override
    public Db getDb() {
        return factory.getDbFactory().getDb();
    }

    @Override
    public TimeLogService getTimeLogService() {
        return factory.getTimeLogServiceFactory().getService();
    }

    @Override
    public TitledLogDaoFacade getTitledLog() {
        if (titledLogRepoFacade == null) {
            titledLogRepoFacade = new TitledLogRepoFacade(this);
        }
        return titledLogRepoFacade;
    }
    
    @Override
    public <T1, T2> GenericCache<T1, T2> getGenericCache() {
        return new GenericCacheImpl<>();
    }

    @Override
    public TimeConverter getTimeConverter() {
        return factory.getTimeConverter();
    }

    @Override
    public TitleService getTitleService() {
        return factory.getTitleFactory().getService();
    }
    
}
