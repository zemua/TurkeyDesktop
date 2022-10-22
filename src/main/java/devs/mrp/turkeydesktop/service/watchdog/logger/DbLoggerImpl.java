/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.service.watchdog.logger;

import devs.mrp.turkeydesktop.database.logandtype.LogAndTypeServiceFactory;
import devs.mrp.turkeydesktop.database.logs.TimeLog;
import devs.mrp.turkeydesktop.database.logandtype.LogAndTypeFacadeService;
import java.util.function.Consumer;
import java.util.logging.Logger;

/**
 *
 * @author miguel
 */
public class DbLoggerImpl implements DbLogger {
    
    private static final Logger LOGGER = Logger.getLogger(DbLoggerImpl.class.getName());
    
    LogAndTypeFacadeService logService;
    
    public DbLoggerImpl() {
        logService = LogAndTypeServiceFactory.getService();
    }
    
    @Override
    public void logEntry(long elapsed, String processPid, String processName, String windowTitle, Consumer<TimeLog> consumer) {
        TimeLog entry = new TimeLog();
        entry.setElapsed(elapsed);
        entry.setEpoch(System.currentTimeMillis());
        entry.setPid(processPid);
        entry.setProcessName(processName);
        entry.setWindowTitle(windowTitle);
        logService.addTimeLogAdjustingCounted(entry, consumer);
    }
    
}
