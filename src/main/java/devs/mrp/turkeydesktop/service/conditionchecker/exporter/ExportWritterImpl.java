/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.service.conditionchecker.exporter;

import devs.mrp.turkeydesktop.common.TimeConverter;
import devs.mrp.turkeydesktop.database.group.expor.ExportedGroup;
import devs.mrp.turkeydesktop.database.group.expor.ExportedGroupService;
import devs.mrp.turkeydesktop.database.group.expor.ExportedGroupServiceFactory;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 *
 * @author miguel
 */
public class ExportWritterImpl implements ExportWritter {
    
    private static final long TIME_BETWEEN_EXPORTS = 1*60*1000; // one minute in millis
    private static long lastExported = 0;
    
    private ExportedGroupService exportedGroupService = ExportedGroupServiceFactory.getService();
    
    @Override
    public void exportChanged() {
        if (isExportDue()) {
            // TODO do stuff
        }
    }
    
    private boolean isExportDue() {
        long now = System.currentTimeMillis();
        if (now >= lastExported + TIME_BETWEEN_EXPORTS) {
            lastExported = now;
            return true;
        }
        return false;
    }
    
    private void doExports() {
        exportedGroupService.findAll().stream()
                .forEach(e -> processFile(e));
    }
    
    private void processFile(ExportedGroup export) {
        for (int i=0; i<export.getDays(); i++){
            long to = TimeConverter.endOfToday();
            long from = TimeConverter.beginningOfOffsetDays(i);
            // TODO
        }
    }
    
}
