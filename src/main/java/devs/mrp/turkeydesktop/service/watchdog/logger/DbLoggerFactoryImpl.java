package devs.mrp.turkeydesktop.service.watchdog.logger;

import devs.mrp.turkeydesktop.database.logandtype.LogAndTypeFacadeService;
import devs.mrp.turkeydesktop.view.container.FactoryInitializer;

public class DbLoggerFactoryImpl implements DbLoggerFactory {
    
    private FactoryInitializer factory;
    
    public DbLoggerFactoryImpl(FactoryInitializer factoryInitializer) {
        this.factory = factoryInitializer;
    }
    
    @Override
    public DbLogger getNew() {
        return new DbLoggerImpl(this);
    }

    @Override
    public LogAndTypeFacadeService getLogAndTypeFacadeService() {
        return factory.getLogAndTypeFacadeFactory().getService();
    }
    
}
