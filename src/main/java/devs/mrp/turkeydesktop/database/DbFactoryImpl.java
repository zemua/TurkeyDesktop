package devs.mrp.turkeydesktop.database;

import devs.mrp.turkeydesktop.common.TimeConverter;
import devs.mrp.turkeydesktop.service.watchdog.WatchDog;
import devs.mrp.turkeydesktop.service.watchdog.WatchDogFactoryImpl;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DbFactoryImpl implements DbFactory {
    
    private Db db;
    private static DbFactoryImpl instance;
    
    private DbFactoryImpl() {
    }
    
    public static DbFactoryImpl getInstance() {
        if (instance == null) {
            instance = new DbFactoryImpl();
        }
        return instance;
    }
    
    @Override
    public Db getDb() {
        if (db == null) {
            db = Db.getInstance(this);
        }
        return db;
    }

    @Override
    public WatchDog getWatchDog() {
        return WatchDogFactoryImpl.getInstance().getWatchDog();
    }

    @Override
    public TimeConverter getTimeConverter() {
        return new TimeConverter();
    }
    
}
