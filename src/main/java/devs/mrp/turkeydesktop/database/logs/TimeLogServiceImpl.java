/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.logs;

import devs.mrp.turkeydesktop.common.Dupla;
import devs.mrp.turkeydesktop.common.TimeConverter;
import devs.mrp.turkeydesktop.common.WorkerFactory;
import devs.mrp.turkeydesktop.database.group.Group;
import devs.mrp.turkeydesktop.database.type.Type;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.LongConsumer;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    public void add(TimeLog element, LongConsumer consumer) {
        if (element == null) {
            consumer.accept(-1);
        } else {
            if (element.getWindowTitle().length() > 500) {
                logger.log(Level.SEVERE, String.format("Window title too long: %s", element.getWindowTitle()));
                element.setWindowTitle(element.getWindowTitle().substring(0, 499));
            }
            WorkerFactory.runLongWorker(() -> repo.add(element), consumer);
        }
    }

    @Override
    public void update(TimeLog element, LongConsumer consumer) {
        if (element == null || element.getId() <= 0) {
            consumer.accept(-1);
        } else {
            if (element.getWindowTitle().length() > 500) {
                logger.log(Level.SEVERE, String.format("Window title too long: %s", element.getWindowTitle()));
                element.setWindowTitle(element.getWindowTitle().substring(0, 499));
            }
            WorkerFactory.runLongWorker(() -> repo.update(element), consumer);
        }
    }

    @Override
    public void findLast24H(Consumer<List<TimeLog>> consumer) {
        WorkerFactory.runResultSetWorker(() -> repo.findAll(), res -> {
            consumer.accept(listFromResultSet(res));
        });
    }
    
    @Override
    public void findProcessTimeFromTo(Date from, Date to, Consumer<List<Dupla<String,Long>>> consumer) {
        // Set from to hour 0 of the day
        long fromMilis = TimeConverter.millisToBeginningOfDay(from.getTime());
        // Set "to" to the last second of the day
        long toMilis = TimeConverter.millisToEndOfDay(to.getTime());
        // use calendar objects to get milliseconds
        List<Dupla<String,Long>> times = new ArrayList<>();
        WorkerFactory.runResultSetWorker(() -> repo.getTimeFrameGroupedByProcess(fromMilis, toMilis), set -> {
            try {
                while (set.next()) {
                    Dupla<String,Long> dupla = new Dupla<>();
                    dupla.setValue1(set.getString(TimeLog.PROCESS_NAME));
                    dupla.setValue2(set.getLong(2));
                    times.add(dupla);
                }
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
            consumer.accept(times);
        });
    }

    @Override
    public void findById(long id, Consumer<TimeLog> consumer) {
        WorkerFactory.runResultSetWorker(() -> repo.findById(id), set -> {
            TimeLog timeLog = null;
            try {
                if (set.next()) {
                    timeLog = setTimeLogFromResultSetEntry(set);
                }
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
            consumer.accept(timeLog);
        });
    }

    @Override
    public void deleteById(long id, LongConsumer consumer) {
        WorkerFactory.runLongWorker(() -> repo.deleteById(id), consumer);
    }

    private List<TimeLog> listFromResultSet(ResultSet set) {
        List<TimeLog> logList = new ArrayList<>();
        try {
            while (set.next()) {
                TimeLog timeLog = setTimeLogFromResultSetEntry(set);
                logList.add(timeLog);
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        return logList;
    }

    @Override
    public void findMostRecent(Consumer<TimeLog> consumer) {
        WorkerFactory.runResultSetWorker(() -> repo.getMostRecent(), set -> {
            TimeLog entry = null;
            try {
                if (set.next()) {
                    entry = setTimeLogFromResultSetEntry(set);
                }
            } catch (SQLException ex) {
                Logger.getLogger(TimeLogServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
            consumer.accept(entry);
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
    public void logsGroupedByTitle(Date from, Date to, Consumer<List<Dupla<String, Long>>> consumer) {
        // Set from to hour 0 of the day
        long fromMilis = TimeConverter.millisToBeginningOfDay(from.getTime());
        // Set 'to' to the last second of the day
        long toMilis = TimeConverter.millisToEndOfDay(to.getTime());
        // use calendar objects to get milliseconds
        WorkerFactory.runResultSetWorker(() -> repo.getGroupedByTitle(fromMilis, toMilis), set -> {
            List<Dupla<String, Long>> groupedTimes = new ArrayList<>();
            try {
                while (set.next()) {
                    Dupla<String, Long> dupla = new Dupla<>();
                    dupla.setValue1(set.getString(TimeLog.WINDOW_TITLE));
                    dupla.setValue2(set.getLong(2));
                    groupedTimes.add(dupla);
                }
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
            consumer.accept(groupedTimes);
        });
    }
    
    @Override
    public void timeSpentOnGroupForFrame(long groupId, long from, long to, LongConsumer consumer) {
        WorkerFactory.runResultSetWorker(() -> repo.getTimeFrameOfGroup(groupId, from, to), set -> {
            long spent = 0;
            try {
                if (set.next()) {
                    spent = set.getLong(2);
                }
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
            consumer.accept(spent);
        });
    }

}
