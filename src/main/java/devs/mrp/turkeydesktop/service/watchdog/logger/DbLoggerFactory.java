package devs.mrp.turkeydesktop.service.watchdog.logger;

import devs.mrp.turkeydesktop.database.logandtype.LogAndTypeFacadeService;

public interface DbLoggerFactory {
    DbLogger getNew();
    LogAndTypeFacadeService getLogAndTypeFacadeService();
}
