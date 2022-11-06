/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.logs;

import devs.mrp.turkeydesktop.common.Dupla;
import devs.mrp.turkeydesktop.common.TimeConverter;
import devs.mrp.turkeydesktop.database.group.Group;
import devs.mrp.turkeydesktop.database.type.Type;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import rx.Observable;
import rx.Single;

/**
 *
 * @author miguel
 */
public class TimeLogServiceImpl implements TimeLogService {

    private final TimeLogDao repo = TimeLogRepository.getInstance();
    private static final Logger logger = Logger.getLogger(TimeLogServiceImpl.class.getName());

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
            return repo.update(element);
        }
    }

    @Override
    public Observable<TimeLog> findLast24H() {
        return repo.findAll().flatMapObservable(this::listFromResultSet);
    }
    
    @Override
    public Observable<Dupla<String,Long>> findProcessTimeFromTo(Date from, Date to) {
        // Set from to hour 0 of the day
        long fromMilis = TimeConverter.millisToBeginningOfDay(from.getTime());
        // Set "to" to the last second of the day
        long toMilis = TimeConverter.millisToEndOfDay(to.getTime());
        // use calendar objects to get milliseconds
        return repo.getTimeFrameGroupedByProcess(fromMilis, toMilis).flatMapObservable(set -> {
            return Observable.create(emitter -> {
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
                emitter.onCompleted();
            });
        });
    }

    @Override
    public Single<TimeLog> findById(long id) {
        return repo.findById(id).map(set -> {
            TimeLog timeLog = null;
            try {
                if (set.next()) {
                    timeLog = setTimeLogFromResultSetEntry(set);
                }
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
            return timeLog;
        });
    }

    @Override
    public Single<Long> deleteById(long id) {
        return repo.deleteById(id);
    }

    private Observable<TimeLog> listFromResultSet(ResultSet set) {
        return Observable.create(emitter -> {
            try {
                while (set.next()) {
                    TimeLog timeLog = setTimeLogFromResultSetEntry(set);
                    emitter.onNext(timeLog);
                }
            } catch (SQLException ex) {
                emitter.onError(ex);
            }
            emitter.onCompleted();
        });
    }

    @Override
    public Single<TimeLog> findMostRecent() {
        return repo.getMostRecent().map(set -> {
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
    
    private TimeLog setTimeLogFromResultSetEntry(ResultSet set) {
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
                emitter.onCompleted();
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
