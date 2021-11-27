/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.service.watchdog.logger;

import devs.mrp.turkeydesktop.database.logs.TimeLog;
import devs.mrp.turkeydesktop.database.logs.TimeLogService;

/**
 *
 * @author miguel
 */
public class DbLoggerImpl implements DbLogger {
    
    TimeLogService logService;
    
    public DbLoggerImpl() {
        logService = new TimeLogService();
    }
    
    @Override
    public void logEntry(long elapsed, String processPid, String processName, String windowTitle) {
        TimeLog entry = new TimeLog();
        entry.setElapsed(elapsed);
        entry.setEpoch(System.currentTimeMillis());
        entry.setPid(processPid);
        entry.setProcessName(processName);
        entry.setWindowTitle(windowTitle);
        logService.add(entry);
    }
    
}
