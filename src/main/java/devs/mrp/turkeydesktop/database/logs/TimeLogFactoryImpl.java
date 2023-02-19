package devs.mrp.turkeydesktop.database.logs;

import devs.mrp.turkeydesktop.common.GenericCache;
import devs.mrp.turkeydesktop.common.TimeConverter;
import devs.mrp.turkeydesktop.common.impl.GenericCacheImpl;
import devs.mrp.turkeydesktop.database.Db;
import devs.mrp.turkeydesktop.database.DbFactoryImpl;
import devs.mrp.turkeydesktop.database.config.ConfigElementFactoryImpl;
import devs.mrp.turkeydesktop.database.group.GroupService;
import io.reactivex.rxjava3.core.Single;

public class TimeLogFactoryImpl implements TimeLogFactory {
    
    private static TimeLogFactoryImpl instance;
    private static TimeLogService timeLogService;
    private static TimeLogDao repo;
    private GroupService groupService;
    private TimeConverter timeConverter;
    
    private TimeLogFactoryImpl() {}
    
    public static TimeLogFactoryImpl getInstance() {
        if (instance == null) {
            instance = new TimeLogFactoryImpl();
        }
        return instance;
    }
    
    @Override
    public TimeLogService getService() {
        if (timeLogService == null) {
            timeLogService = new TimeLogServiceImpl(this);
        }
        return timeLogService;
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
    public Db getDb() {
        return DbFactoryImpl.getInstance().getDb();
    }

    @Override
    public TimeLogDao getRepo() {
        if (repo == null) {
            repo = new TimeLogRepository(this);
        }
        return repo;
    }

    @Override
    public Single<TimeLog> asBlockable(TimeLog timeLog) {
        return Single.just(TimeLog.builder()
                .id(timeLog.getId())
                .epoch(timeLog.getEpoch())
                .elapsed(timeLog.getElapsed())
                .counted(timeLog.getCounted())
                .accumulated(timeLog.getAccumulated())
                .pid(timeLog.getPid())
                .processName(timeLog.getProcessName())
                .windowTitle(timeLog.getWindowTitle())
                .groupId(timeLog.getGroupId())
                .type(timeLog.getType())
                .blockable(true)
                .build());
    }

    @Override
    public Single<TimeLog> asNotBlockable(TimeLog timeLog) {
        return groupService.isPreventClose(timeLog.getGroupId())
                .map(isPreventCloseResult -> {
                    TimeLog result;
                    if (isPreventCloseResult) {
                        result = TimeLog.builder()
                        .id(timeLog.getId())
                        .epoch(timeLog.getEpoch())
                        .elapsed(timeLog.getElapsed())
                        .counted(timeLog.getCounted())
                        .accumulated(timeLog.getAccumulated())
                        .pid(timeLog.getPid())
                        .processName(timeLog.getProcessName())
                        .windowTitle(timeLog.getWindowTitle())
                        .groupId(timeLog.getGroupId())
                        .type(timeLog.getType())
                        .blockable(false)
                        .build();
                    } else {
                        result = timeLog;
                    }
                    return result;
                });
    }
    
}
