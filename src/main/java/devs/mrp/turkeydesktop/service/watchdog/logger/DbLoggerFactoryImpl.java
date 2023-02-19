package devs.mrp.turkeydesktop.service.watchdog.logger;

import devs.mrp.turkeydesktop.database.logandtype.LogAndTypeFacadeFactoryImpl;
import devs.mrp.turkeydesktop.database.logandtype.LogAndTypeFacadeService;

public class DbLoggerFactoryImpl implements DbLoggerFactory {
    
    private static DbLoggerFactoryImpl instance;
    
    private DbLoggerFactoryImpl() {}
    
    public static DbLoggerFactoryImpl getInstance() {
        if (instance == null) {
            instance = new DbLoggerFactoryImpl();
        }
        return instance;
    }
    
    @Override
    public DbLogger getNew() {
        return new DbLoggerImpl(this);
    }

    @Override
    public LogAndTypeFacadeService getLogAndTypeFacadeService() {
        return LogAndTypeFacadeFactoryImpl.getInstance().getService();
    }
    
}
