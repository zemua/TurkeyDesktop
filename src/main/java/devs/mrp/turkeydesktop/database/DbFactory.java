package devs.mrp.turkeydesktop.database;

import devs.mrp.turkeydesktop.common.TimeConverter;
import devs.mrp.turkeydesktop.service.watchdog.WatchDog;

public interface DbFactory {
    
    Db getDb();
    WatchDog getWatchDog();
    TimeConverter getTimeConverter();
    
}
