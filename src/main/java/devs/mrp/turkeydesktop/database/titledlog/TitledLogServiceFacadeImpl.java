/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.titledlog;

import devs.mrp.turkeydesktop.common.TimeConverter;
import devs.mrp.turkeydesktop.common.WorkerFactory;
import devs.mrp.turkeydesktop.database.logs.TimeLogServiceFactory;
import devs.mrp.turkeydesktop.database.logs.TimeLog;
import devs.mrp.turkeydesktop.database.titles.TitleServiceFactory;
import devs.mrp.turkeydesktop.database.titles.Title;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import devs.mrp.turkeydesktop.database.logs.TimeLogService;
import devs.mrp.turkeydesktop.database.titles.TitleService;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 *
 * @author miguel
 */
public class TitledLogServiceFacadeImpl implements TitledLogServiceFacade {
    
    private TitleService titleService = TitleServiceFactory.getService();
    private TimeLogService logService = TimeLogServiceFactory.getService();
    private TitledLogDaoFacade titleFacade = TitledLogRepoFacade.getInstance();
    private static final Logger logger = Logger.getLogger(TitledLogServiceFacadeImpl.class.getName());

    @Override
    public void getLogsWithTitleConditions(Date from, Date to, Consumer<List<TitledLog>> c) {
        var consumer = TitledLogServiceFacadeFactory.getListConsumer(c);
        logService.logsGroupedByTitle(from, to, result -> {
            List<TitledLog> logs = new ArrayList<>();
            if (result.isEmpty()) {
                consumer.accept(Collections.EMPTY_LIST);
            }
            result.stream().forEach(e -> {
                TitledLog tl = new TitledLog();
                tl.setTitle(e.getValue1());
                tl.setElapsed(e.getValue2());
                titleService.countTypesOf(Title.Type.POSITIVE, e.getValue1(), qtyPos -> {
                    tl.setQtyPositives(qtyPos);
                    titleService.countTypesOf(Title.Type.NEGATIVE, e.getValue1(), qtyNeg -> {
                        tl.setQtyNegatives(qtyNeg);
                        titleService.findContainedByAndNegativeFirst(e.getValue1(), cond -> {
                            tl.setConditions(cond);
                            logs.add(tl);
                            if (logs.size() == result.size()) {
                                consumer.accept(logs);
                            }
                        });
                    });
                });
            });
        });
    }

    @Override
    public void getLogsDependablesWithTitleConditions(Date from, Date to, Consumer<List<TitledLog>> c) {
        var consumer = TitledLogServiceFacadeFactory.getListConsumer(c);
        List<TitledLog> logList = new ArrayList<>();
        long fromMillis = TimeConverter.millisToBeginningOfDay(from.getTime());
        long toMillis = TimeConverter.millisToEndOfDay(to.getTime());
        WorkerFactory.runResultSetWorker(() -> titleFacade.getTimeFrameOfDependablesGroupedByProcess(fromMillis, toMillis), set -> {
            try {
                AtomicInteger read = new AtomicInteger(0);
                while (set.next()) {
                    read.addAndGet(1);
                    titledLogFromResultSetEntry(set, titledLog -> {
                        logList.add(titledLog);
                        if (read.get() == logList.size()) {
                            consumer.accept(logList);
                        }
                    });
                }
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
        });
    }
    
    private void titledLogFromResultSetEntry(ResultSet entry, Consumer<TitledLog> c) {
        var consumer = TitledLogServiceFacadeFactory.getConsumer(c);
        TitledLog log = new TitledLog();
        try {
            String title = entry.getString(TimeLog.WINDOW_TITLE);
            log.setTitle(title);
            log.setElapsed(entry.getLong(2));
            titleService.countTypesOf(Title.Type.POSITIVE, title, qtyPos -> {
                log.setQtyPositives(qtyPos);
                titleService.countTypesOf(Title.Type.NEGATIVE, title, qtyNeg -> {
                    log.setQtyNegatives(qtyNeg);
                    titleService.findContainedByAndNegativeFirst(title, contained -> {
                        log.setConditions(contained);
                        consumer.accept(log);
                    });
                });
            });
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, null, ex);
            consumer.accept(log);
        }
    }
    
}
