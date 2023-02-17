package devs.mrp.turkeydesktop.service.conditionchecker.exporter;

import devs.mrp.turkeydesktop.common.FileHandler;
import devs.mrp.turkeydesktop.common.TimeConverter;
import devs.mrp.turkeydesktop.database.group.expor.ExportedGroupService;
import devs.mrp.turkeydesktop.database.logs.TimeLogService;

public interface ExportWritterFactory {
    
    ExportedGroupService getExportedGroupService();
    TimeLogService getTimeLogService();
    FileHandler getFileHandler();
    TimeConverter getTimeConverter();
    ExportWritter getWritter();
    
}
