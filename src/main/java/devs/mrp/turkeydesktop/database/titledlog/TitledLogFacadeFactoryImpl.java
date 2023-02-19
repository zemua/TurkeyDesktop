package devs.mrp.turkeydesktop.database.titledlog;

import devs.mrp.turkeydesktop.common.GenericCache;
import devs.mrp.turkeydesktop.common.TimeConverter;
import devs.mrp.turkeydesktop.common.impl.GenericCacheImpl;
import devs.mrp.turkeydesktop.database.Db;
import devs.mrp.turkeydesktop.database.DbFactoryImpl;
import devs.mrp.turkeydesktop.database.config.ConfigElementFactoryImpl;
import devs.mrp.turkeydesktop.database.logs.TimeLogFactoryImpl;
import devs.mrp.turkeydesktop.database.logs.TimeLogService;
import devs.mrp.turkeydesktop.database.titles.TitleFactoryImpl;
import devs.mrp.turkeydesktop.database.titles.TitleService;

public class TitledLogFacadeFactoryImpl implements TitledLogFacadeFactory {
    
    private static TitledLogFacadeFactoryImpl instance;
    private static TitledLogServiceFacade titledLogServiceFacade;
    private static TitledLogDaoFacade titledLogRepoFacade;
    private TimeConverter timeConverter;
    
    private TitledLogFacadeFactoryImpl() {}
    
    public static TitledLogFacadeFactoryImpl getInstance() {
        if (instance == null) {
            instance = new TitledLogFacadeFactoryImpl();
        }
        return instance;
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
        return DbFactoryImpl.getInstance().getDb();
    }

    @Override
    public TimeLogService getTimeLogService() {
        return TimeLogFactoryImpl.getInstance().getService();
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
        if (timeConverter == null) {
            timeConverter = new TimeConverter(ConfigElementFactoryImpl.getInstance().getService());
        }
        return timeConverter;
    }

    @Override
    public TitleService getTitleService() {
        return TitleFactoryImpl.getInstance().getService();
    }
    
}
