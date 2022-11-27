/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.logs;

import devs.mrp.turkeydesktop.common.Dupla;
import devs.mrp.turkeydesktop.common.GenericCache;
import devs.mrp.turkeydesktop.common.TimeConverter;
import devs.mrp.turkeydesktop.common.impl.GenericCacheImpl;
import devs.mrp.turkeydesktop.database.group.Group;
import devs.mrp.turkeydesktop.database.type.Type;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Single;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

/**
 *
 * @author miguel
 */
public class TimeLogServiceImpl implements TimeLogService {

    private final TimeLogDao repo = TimeLogRepository.getInstance();
    private static final Logger logger = Logger.getLogger(TimeLogServiceImpl.class.getName());
    
    private static TimeLog lastTimeLog;
    
    private static GenericCache<Long,Single<TimeLog>> byIdCache = new GenericCacheImpl<>();
    private static GenericCache<Boolean,Observable<TimeLog>> last24cache = new GenericCacheImpl<>();
    private static GenericCache<FromTo,Observable<Dupla<String,Long>>> processTimeCache = new GenericCacheImpl<>();

    /**
     * @deprecated
     * Use ILogAndTypeService.addTimeLogAdjustingCounted(TimeLog element) instead
     */
    @Deprecated
    @Override
    public Single<Long> add(TimeLog element) {
        if (element == null) {
            return Single.just(-1L);
        } else {
            if (element.getWindowTitle().length() > 500) {
                logger.log(Level.SEVERE, String.format("Window title too long: %s", element.getWindowTitle()));
                element.setWindowTitle(element.getWindowTitle().substring(0, 499));
            }
            lastTimeLog = element;
            return repo.add(element);
        }
    }

    @Override
    public Single<Long> update(TimeLog element) {
        if (element == null || element.getId() <= 0) {
            return Single.just(-1L);
        } else {
            if (element.getWindowTitle().length() > 500) {
                logger.log(Level.SEVERE, String.format("Window title too long: %s", element.getWindowTitle()));
                element.setWindowTitle(element.getWindowTitle().substring(0, 499));
            }
            if (lastTimeLog != null && element.getId() == lastTimeLog.getId() && element.getEpoch() == lastTimeLog.getEpoch()) {
                lastTimeLog = element;
            }
            return repo.update(element);
        }
    }

    @Override
    public Observable<TimeLog> findLast24H() {
        return last24cache.get(true, () -> Observable.fromIterable(repo.findAll().flatMapObservable(TimeLogServiceImpl::listFromResultSet).toList().blockingGet()));
    }
    
    @EqualsAndHashCode
    @AllArgsConstructor
    private class FromTo {
        Date from;
        Date to;
    }
    
    @Override
    public Observable<Dupla<String,Long>> findProcessTimeFromTo(Date from, Date to) {
        // Set from to hour 0 of the day
        long fromMilis = TimeConverter.millisToBeginningOfDay(from.getTime());
        // Set "to" to the last second of the day
        long toMilis = TimeConverter.millisToEndOfDay(to.getTime());
        // use calendar objects to get milliseconds
        return processTimeCache.get(new FromTo(from, to), () -> {
            List<Dupla<String,Long>> list = repo.getTimeFrameGroupedByProcess(fromMilis, toMilis).flatMapObservable(set -> {
                return Observable.create((ObservableOnSubscribe<Dupla<String,Long>>)emitter -> {
                    try {
                        while (set.next()) {
                            Dupla<String,Long> dupla = new Dupla<>();
                            dupla.setValue1(set.getString(TimeLog.PROCESS_NAME));
                            dupla.setValue2(set.getLong(2));
                            emitter.onNext(dupla);
                        }
                    } catch (SQLException ex) {
                        emitter.onError(ex);
                    }
                    emitter.onComplete();
                });
            }).toList().blockingGet();
            // we create the oservable from a blocked list, so it can be checked on demand and
            // doesn't need to re-create the chain when called from cache
            return Observable.fromIterable(list);
        });
    }

    @Override
    public Single<TimeLog> findById(long id) {
        return byIdCache.get(id, () -> Single.just(repo.findById(id).map(set -> {
                TimeLog timeLog = null;
                try {
                    if (set.next()) {
                        timeLog = setTimeLogFromResultSetEntry(set);
                    }
                } catch (SQLException ex) {
                    logger.log(Level.SEVERE, null, ex);
                }
                return timeLog;
            }).blockingGet())); // we make the single from a blocked element to save it in cache and not re-use the chain
    }

    @Override
    public Single<Long> deleteById(long id) {
        byIdCache.remove(id);
        return repo.deleteById(id);
    }

    private static Observable<TimeLog> listFromResultSet(ResultSet set) {
        return Observable.create(emitter -> {
            try {
                while (set.next()) {
                    TimeLog timeLog = setTimeLogFromResultSetEntry(set);
                    emitter.onNext(timeLog);
                }
            } catch (SQLException ex) {
                emitter.onError(ex);
            }
            emitter.onComplete();
        });
    }

    @Override
    public Single<TimeLog> findMostRecent() {
        return lastTimeLog != null ? Single.just(lastTimeLog) : repo.getMostRecent().map(set -> {
            TimeLog entry = null;
            try {
                if (set.next()) {
                    entry = setTimeLogFromResultSetEntry(set);
                } else {
                    entry = TimeLog.builder()
                            .accumulated(0L)
                            .blockable(false)
                            .counted(0L)
                            .elapsed(0L)
                            .epoch(0L)
                            .groupId(-1)
                            .id(0)
                            .pid("")
                            .processName("")
                            .type(Type.Types.UNDEFINED)
                            .windowTitle("")
                            .build();
                }
            } catch (SQLException ex) {
                Logger.getLogger(TimeLogServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
            return entry;
        });
    }
    
    private static TimeLog setTimeLogFromResultSetEntry(ResultSet set) {
        TimeLog entry = new TimeLog();
        try {
            entry.setElapsed(set.getLong(TimeLog.ELAPSED));
            entry.setEpoch(set.getLong(TimeLog.EPOCH));
            entry.setCounted(set.getLong(TimeLog.COUNTED));
            entry.setAccumulated(set.getLong(TimeLog.ACCUMULATED));
            entry.setId(set.getLong(TimeLog.ID));
            entry.setPid(set.getString(TimeLog.PID));
            entry.setProcessName(set.getString(TimeLog.PROCESS_NAME));
            entry.setWindowTitle(set.getString(TimeLog.WINDOW_TITLE));
            entry.setGroupId(set.getLong(Group.GROUP));
            if (set.getString(Type.TYPE) != null) {
                entry.setType(Type.Types.valueOf(set.getString(Type.TYPE)));
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        return entry;
    }

    @Override
    public Observable<Dupla<String, Long>> logsGroupedByTitle(Date from, Date to) {
        // Set from to hour 0 of the day
        long fromMilis = TimeConverter.millisToBeginningOfDay(from.getTime());
        // Set 'to' to the last second of the day
        long toMilis = TimeConverter.millisToEndOfDay(to.getTime());
        // use calendar objects to get milliseconds
        return repo.getGroupedByTitle(fromMilis, toMilis).flatMapObservable(set -> {
            return Observable.create(emitter -> {
                try {
                    while (set.next()) {
                        Dupla<String, Long> dupla = new Dupla<>();
                        dupla.setValue1(set.getString(TimeLog.WINDOW_TITLE));
                        dupla.setValue2(set.getLong(2));
                        emitter.onNext(dupla);
                    }
                } catch (SQLException ex) {
                    emitter.onError(ex);
                }
                emitter.onComplete();
            });
        });
    }
    
    @Override
    public Single<Long> timeSpentOnGroupForFrame(long groupId, long from, long to) {
        return repo.getTimeFrameOfGroup(groupId, from, to).map(set -> {
            try {
                if (set.next()) {
                    return set.getLong(2);
                }
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
            return 0L;
        });
    }

}
