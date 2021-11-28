/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.logs;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author miguel
 */
public class TimeLogService {

    TimeLogDao repo = new TimeLogRepository();

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
