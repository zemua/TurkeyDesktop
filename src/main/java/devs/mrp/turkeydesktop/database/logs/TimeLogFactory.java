package devs.mrp.turkeydesktop.database.logs;

import devs.mrp.turkeydesktop.common.GenericCache;
import devs.mrp.turkeydesktop.common.TimeConverter;
import devs.mrp.turkeydesktop.database.Db;
import io.reactivex.rxjava3.core.Single;

public interface TimeLogFactory {
    
    TimeLogService getService();
    <T1,T2> GenericCache<T1,T2> getGenericCache();
    TimeConverter getTimeConverter();
    Db getDb();
    TimeLogDao getRepo();
    Single<TimeLog> asBlockable(TimeLog timeLog);
    Single<TimeLog> asNotBlockable(TimeLog timeLog);
    
}
