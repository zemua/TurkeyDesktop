/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.logs;

import devs.mrp.turkeydesktop.common.Dupla;
import devs.mrp.turkeydesktop.common.TimeConverter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author miguel
 */
public class TimeLogService implements ITimeLogService {

    TimeLogDao repo = TimeLogRepository.getInstance();

    public long add(TimeLog element) {
        if (element == null) {
            return -1;
        } else {
            return repo.add(element);
        }
    }

    public long update(TimeLog element) {
        if (element == null || element.getId() <= 0) {
            return -1;
        } else {
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
            Logger.getLogger(TimeLogService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return times;
    }

    public TimeLog findById(long id) {
        ResultSet set = repo.findById(id);
        TimeLog timeLog = new TimeLog();
        try {
            if (set.next()) {
                timeLog.setElapsed(set.getLong(TimeLog.ELAPSED));
                timeLog.setEpoch(set.getLong(TimeLog.EPOCH));
                timeLog.setId(set.getLong(TimeLog.ID));
                timeLog.setPid(set.getString(TimeLog.PID));
                timeLog.setProcessName(set.getString(TimeLog.PROCESS_NAME));
                timeLog.setWindowTitle(set.getString(TimeLog.WINDOW_TITLE));
            }
        } catch (SQLException ex) {
            Logger.getLogger(TimeLogService.class.getName()).log(Level.SEVERE, null, ex);
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
                TimeLog timeLog = new TimeLog();
                timeLog.setElapsed(set.getLong(TimeLog.ELAPSED));
                timeLog.setEpoch(set.getLong(TimeLog.EPOCH));
                timeLog.setId(set.getLong(TimeLog.ID));
                timeLog.setPid(set.getString(TimeLog.PID));
                timeLog.setProcessName(set.getString(TimeLog.PROCESS_NAME));
                timeLog.setWindowTitle(set.getString(TimeLog.WINDOW_TITLE));
                logList.add(timeLog);
            }
        } catch (SQLException ex) {
            Logger.getLogger(TimeLogService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return logList;
    }

}
