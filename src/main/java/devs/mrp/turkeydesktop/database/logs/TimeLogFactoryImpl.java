package devs.mrp.turkeydesktop.database.logs;

import devs.mrp.turkeydesktop.common.GenericCache;
import devs.mrp.turkeydesktop.common.TimeConverter;
import devs.mrp.turkeydesktop.common.impl.GenericCacheImpl;
import devs.mrp.turkeydesktop.database.Db;
import devs.mrp.turkeydesktop.database.group.GroupService;
import devs.mrp.turkeydesktop.view.container.FactoryInitializer;
import io.reactivex.rxjava3.core.Single;

public class TimeLogFactoryImpl implements TimeLogFactory {
    
    private FactoryInitializer factory;
    private static TimeLogService timeLogService;
    private static TimeLogDao repo;
    private GroupService groupService;
    
    public TimeLogFactoryImpl(FactoryInitializer factory) {
        this.factory = factory;
        this.groupService = factory.getGroupFactory().getService();
    }
    
    @Override
    public TimeLogService getService() {
        if (timeLogService == null) {
            timeLogService = factory.getTimeLogServiceFactory().getService();
        }
        return timeLogService;
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
    public Db getDb() {
        return factory.getDbFactory().getDb();
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
