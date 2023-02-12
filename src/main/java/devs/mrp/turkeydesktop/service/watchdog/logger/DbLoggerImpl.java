package devs.mrp.turkeydesktop.service.watchdog.logger;

import devs.mrp.turkeydesktop.database.logandtype.LogAndTypeFacadeService;
import devs.mrp.turkeydesktop.database.logandtype.LogAndTypeFacadeFactoryImpl;
import devs.mrp.turkeydesktop.database.logs.TimeLog;
import io.reactivex.rxjava3.core.Single;
import java.util.logging.Logger;

public class DbLoggerImpl implements DbLogger {
    
    private static final Logger LOGGER = Logger.getLogger(DbLoggerImpl.class.getName());
    
    LogAndTypeFacadeService logService;
    
    public DbLoggerImpl() {
        logService = LogAndTypeFacadeFactoryImpl.getService();
    }
    
    @Override
    public Single<TimeLog> logEntry(long elapsed, String processPid, String processName, String windowTitle) {
        TimeLog entry = new TimeLog();
        entry.setElapsed(elapsed);
        entry.setEpoch(System.currentTimeMillis());
        entry.setPid(processPid);
        entry.setProcessName(processName);
        entry.setWindowTitle(windowTitle);
        return logService.addTimeLogAdjustingCounted(entry);
    }
    
}
