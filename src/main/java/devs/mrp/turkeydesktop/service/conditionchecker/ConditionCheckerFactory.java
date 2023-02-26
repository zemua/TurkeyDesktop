package devs.mrp.turkeydesktop.service.conditionchecker;

import devs.mrp.turkeydesktop.common.ChainHandler;
import devs.mrp.turkeydesktop.common.FileHandler;
import devs.mrp.turkeydesktop.common.TimeConverter;
import devs.mrp.turkeydesktop.database.conditions.ConditionService;
import devs.mrp.turkeydesktop.database.config.ConfigElementService;
import devs.mrp.turkeydesktop.database.group.external.ExternalGroupService;
import devs.mrp.turkeydesktop.database.imports.ImportService;
import devs.mrp.turkeydesktop.database.logs.TimeLogService;
import devs.mrp.turkeydesktop.service.conditionchecker.idle.LongWrapper;
import devs.mrp.turkeydesktop.service.conditionchecker.imports.ImportReader;
import devs.mrp.turkeydesktop.service.toaster.Toaster;

public interface ConditionCheckerFactory {
    ConditionChecker getConditionChecker();
    ConfigElementService getConfigElementService();
    Toaster getToaster();
    TimeConverter getTimeConverter();
    FileHandler fileHandler();
    ConditionService getConditionService();
    TimeLogService getTimeLogService();
    ImportService getImportService();
    ChainHandler<LongWrapper> gettIdleHandler();
    ExternalGroupService getExternalGroupService();
    ImportReader getImportReader();
}
