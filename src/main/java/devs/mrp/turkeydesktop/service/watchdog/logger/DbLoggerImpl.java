/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.service.watchdog.logger;

import devs.mrp.turkeydesktop.database.logandtype.FLogAndTypeService;
import devs.mrp.turkeydesktop.database.logandtype.ILogAndTypeService;
import devs.mrp.turkeydesktop.database.logs.TimeLog;

/**
 *
 * @author miguel
 */
public class DbLoggerImpl implements DbLogger {
    
    ILogAndTypeService logService;
    
    public DbLoggerImpl() {
        logService = FLogAndTypeService.getService();
    }
    
    @Override
    public TimeLog logEntry(long elapsed, String processPid, String processName, String windowTitle) {
        TimeLog entry = new TimeLog();
        entry.setElapsed(elapsed);
        entry.setEpoch(System.currentTimeMillis());
        entry.setPid(processPid);
        entry.setProcessName(processName);
        entry.setWindowTitle(windowTitle);
        return logService.addTimeLogAdjustingCounted(entry);
    }
    
}
