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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
    public long add(TimeLog element) {
        if (element == null) {
            return -1;
        } else {
            if (element.getWindowTitle().length() > 500) {
                logger.log(Level.SEVERE, String.format("Window title too long: %s", element.getWindowTitle()));
                element.setWindowTitle(element.getWindowTitle().substring(0, 499));
            }
            return repo.add(element);
        }
    }

    @Override
    public long update(TimeLog element) {
        if (element == null || element.getId() <= 0) {
            return -1;
        } else {
            if (element.getWindowTitle().length() > 500) {
                logger.log(Level.SEVERE, String.format("Window title too long: %s", element.getWindowTitle()));
                element.setWindowTitle(element.getWindowTitle().substring(0, 499));
            }
            return repo.update(element);
        }
    }

    public List<TimeLog> findLast24H() {
        return listFromResultSet(repo.findAll());
    }
    
    @Override
    public List<Dupla<String, Long>> findProcessTimeFromTo(Date from, Date to) {
        // Set from to hour 0 of the day
        long fromMilis = TimeConverter.millisToBeginningOfDay(from.getTime());
        // Set "to" to the last second of the day
        long toMilis = TimeConverter.millisToEndOfDay(to.getTime());
        // use calendar objects to get milliseconds
        List<Dupla<String,Long>> times = new ArrayList<>();
        ResultSet set = repo.getTimeFrameGroupedByProcess(fromMilis, toMilis);
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
        return times;
    }

    public TimeLog findById(long id) {
        ResultSet set = repo.findById(id);
        TimeLog timeLog = null;
        try {
            if (set.next()) {
                timeLog = setTimeLogFromResultSetEntry(set);
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        return timeLog;
    }

    public long deleteById(long id) {
        return repo.deleteById(id);
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
    public TimeLog findMostRecent() {
        TimeLog entry = null;
        try {
            ResultSet set = repo.getMostRecent();
            if (set.next()) {
                entry = setTimeLogFromResultSetEntry(set);
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        return entry;
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
    public List<Dupla<String, Long>> logsGroupedByTitle(Date from, Date to) {
        // Set from to hour 0 of the day
        long fromMilis = TimeConverter.millisToBeginningOfDay(from.getTime());
        // Set 'to' to the last second of the day
        long toMilis = TimeConverter.millisToEndOfDay(to.getTime());
        // use calendar objects to get milliseconds
        List<Dupla<String, Long>> groupedTimes = new ArrayList<>();
        ResultSet set = repo.getGroupedByTitle(fromMilis, toMilis);
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
        return groupedTimes;
    }
    
    @Override
    public long timeSpentOnGroupForFrame(long groupId, long from, long to) {
        ResultSet set = repo.getTimeFrameOfGroup(groupId, from, to);
        long spent = 0;
        try {
            if (set.next()) {
                spent = set.getLong(2);
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        return spent;
    }

}
