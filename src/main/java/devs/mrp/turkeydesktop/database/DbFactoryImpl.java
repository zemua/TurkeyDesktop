package devs.mrp.turkeydesktop.database;

import devs.mrp.turkeydesktop.common.TimeConverter;
import devs.mrp.turkeydesktop.service.watchdog.WatchDog;
import devs.mrp.turkeydesktop.view.container.FactoryInitializer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DbFactoryImpl implements DbFactory {
    
    private Db db;
    private final FactoryInitializer factory;
    
    private DbFactoryImpl(FactoryInitializer factory) {
        this.factory = factory;
    }
    
    private DbFactoryImpl(FactoryInitializer factory, Db db) {
        this.factory = factory;
        this.db = db;
    }
    
    public static DbFactoryImpl getNewFactory(FactoryInitializer factory) {
        DbFactoryImpl dbFactory = new DbFactoryImpl(factory);
        dbFactory.db = Db.getInstance(dbFactory);
        return dbFactory;
    }
    
    public static DbFactoryImpl getNewFactory(FactoryInitializer factory, Db db) {
        DbFactoryImpl dbFactory = new DbFactoryImpl(factory, db);
        return dbFactory;
    }
    
    @Override
    public Db getDb() {
        return db;
    }

    @Override
    public WatchDog getWatchDog() {
        return factory.getWatchDogFactory().getInstance();
    }

    @Override
    public TimeConverter getTimeConverter() {
        return factory.getTimeConverter();
    }
    
}
