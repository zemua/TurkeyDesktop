package devs.mrp.turkeydesktop.database.titledlog;

import devs.mrp.turkeydesktop.common.GenericCache;
import devs.mrp.turkeydesktop.common.TimeConverter;
import devs.mrp.turkeydesktop.database.Db;
import devs.mrp.turkeydesktop.database.logs.TimeLogService;
import devs.mrp.turkeydesktop.database.titles.TitleService;

public interface TitledLogFacadeFactory {
    
    Db getDb();
    TitledLogServiceFacade getService();
    TimeLogService getTimeLogService();
    TitledLogDaoFacade getTitledLog();
    <T1, T2> GenericCache<T1, T2> getGenericCache();
    TimeConverter getTimeConverter();
    TitleService getTitleService();
    
}
