/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.service.conditionchecker.exporter;

import devs.mrp.turkeydesktop.common.FileHandler;
import devs.mrp.turkeydesktop.common.TimeConverter;
import devs.mrp.turkeydesktop.database.group.expor.ExportedGroup;
import devs.mrp.turkeydesktop.database.group.expor.ExportedGroupService;
import devs.mrp.turkeydesktop.database.group.expor.ExportedGroupServiceFactory;
import devs.mrp.turkeydesktop.database.logs.TimeLogService;
import devs.mrp.turkeydesktop.database.logs.TimeLogServiceFactory;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author miguel
 */
public class ExportWritterImpl implements ExportWritter {

    private static final long TIME_BETWEEN_EXPORTS = 1 * 60 * 1000; // one minute in millis
    private static long lastExported = 0;

    private Logger logger = Logger.getLogger(ExportWritterImpl.class.getName());

    private ExportedGroupService exportedGroupService = ExportedGroupServiceFactory.getService();
    private TimeLogService timeLogService = TimeLogServiceFactory.getService();

    @Override
    public void exportChanged() {
        if (isExportDue()) {
            doExports();
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
        File file = new File(export.getFile());
        try {
            FileHandler.clearTxt(file);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "error clearing file " + export.getFile(), ex);
        }
        StringBuilder fileText = new StringBuilder();
        for (int i = 0; i < export.getDays(); i++) {
            long to = TimeConverter.endOfOffsetDaysConsideringDayChange(i);
            long from = TimeConverter.beginningOfOffsetDaysConsideringDayChange(i);
            long spent = timeLogService.timeSpentOnGroupForFrame(export.getGroup(), from, to);
            LocalDate date = LocalDate.now().minusDays(i);
            String result = String.format("%d-%d-%d-%d", date.getYear(), date.getMonthValue() - 1, date.getDayOfMonth(), spent); // TODO LocalDate month starts in 1 but in Android app is set to start on 0
            if (i != 0) {
                fileText.append(System.lineSeparator());
            }
            fileText.append(result);
        }
        try {
            FileHandler.writeToTxt(file, fileText.toString());
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "error exporting group time to file", ex);
        }
    }

}
