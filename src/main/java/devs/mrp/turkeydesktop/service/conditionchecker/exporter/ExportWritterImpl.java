/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.service.conditionchecker.exporter;

import devs.mrp.turkeydesktop.common.FileHandler;
import devs.mrp.turkeydesktop.common.TimeConverter;
import devs.mrp.turkeydesktop.common.TurkeyAppFactory;
import devs.mrp.turkeydesktop.database.group.expor.ExportedGroup;
import devs.mrp.turkeydesktop.database.group.expor.ExportedGroupService;
import devs.mrp.turkeydesktop.database.group.expor.ExportedGroupServiceFactory;
import devs.mrp.turkeydesktop.database.logs.TimeLogService;
import devs.mrp.turkeydesktop.database.logs.TimeLogServiceFactory;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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
        exportedGroupService.findAll(all -> {
            TurkeyAppFactory.runInNewThread(() -> {
                all.stream()
                    .forEach(e -> processFile(e));
            });
        });
    }

    private void processFile(ExportedGroup export) {
        File file = new File(export.getFile());
        try {
            FileHandler.clearTxt(file);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "error clearing file " + export.getFile(), ex);
        }
        StringBuilder fileText = new StringBuilder();
        List<Map.Entry<LocalDate, String>> processed = Collections.synchronizedList(new ArrayList<>());
        for (int i = 0; i < export.getDays(); i++) {
            int j = i;
            TimeConverter.endOfOffsetDaysConsideringDayChange(j, to -> {
                TimeConverter.beginningOfOffsetDaysConsideringDayChange(j, from -> {
                    long spent = timeLogService.timeSpentOnGroupForFrame(export.getGroup(), from, to);
                    LocalDate date = LocalDate.now().minusDays(j);
                    String result = String.format("%d-%d-%d-%d", date.getYear(), date.getMonthValue() - 1, date.getDayOfMonth(), spent); // TODO LocalDate month starts in 1 but in Android app is set to start on 0
                    
                    Map.Entry<LocalDate, String> entry = new AbstractMap.SimpleEntry<>(date, result);
                    processed.add(entry);
                    
                    if (processed.size() == export.getDays()){
                        processed.sort((e1, e2) -> e1.getKey().compareTo(e2.getKey()));
                        for (int k = 0; k < processed.size(); k++) {
                            if (k != 0) {
                                fileText.append(System.lineSeparator());
                            }
                            fileText.append(processed.get(k).getValue());
                        }
                        try {
                            FileHandler.writeToTxt(file, fileText.toString());
                        } catch (IOException ex) {
                            logger.log(Level.SEVERE, "error exporting group time to file", ex);
                        }
                    }
                });
            });
        }
    }

}
