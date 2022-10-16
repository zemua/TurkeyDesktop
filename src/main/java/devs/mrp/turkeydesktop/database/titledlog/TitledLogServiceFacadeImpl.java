/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.titledlog;

import devs.mrp.turkeydesktop.common.TimeConverter;
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
import java.util.stream.Collectors;
import devs.mrp.turkeydesktop.database.logs.TimeLogService;
import devs.mrp.turkeydesktop.database.titles.TitleService;
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
    public void getLogsWithTitleConditions(Date from, Date to, Consumer<List<TitledLog>> consumer) {
        logService.logsGroupedByTitle(from, to, result -> {
            var computed = result.stream()
                .map(e -> {
                    TitledLog tl = new TitledLog();
                    tl.setTitle(e.getValue1());
                    tl.setElapsed(e.getValue2());
                    tl.setConditions(titleService.findContainedByAndNegativeFirst(e.getValue1()));
                    tl.setQtyPositives(titleService.countTypesOf(Title.Type.POSITIVE, e.getValue1()));
                    tl.setQtyNegatives(titleService.countTypesOf(Title.Type.NEGATIVE, e.getValue1()));
                    return tl;
                })
                .collect(Collectors.toList());
            consumer.accept(computed);
        });
    }

    @Override
    public List<TitledLog> getLogsDependablesWithTitleConditions(Date from, Date to) {
        List<TitledLog> logList = new ArrayList<>();
        long fromMillis = TimeConverter.millisToBeginningOfDay(from.getTime());
        long toMillis = TimeConverter.millisToEndOfDay(to.getTime());
        ResultSet set = titleFacade.getTimeFrameOfDependablesGroupedByProcess(fromMillis, toMillis);
        try {
            while (set.next()) {
                TitledLog titledLog = titledLogFromResultSetEntry(set);
                logList.add(titledLog);
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        return logList;
    }
    
    private TitledLog titledLogFromResultSetEntry(ResultSet entry) {
        TitledLog log = new TitledLog();
        try {
            String title = entry.getString(TimeLog.WINDOW_TITLE);
            log.setTitle(title);
            log.setElapsed(entry.getLong(2));
            log.setConditions(titleService.findContainedByAndNegativeFirst(title));
            log.setQtyPositives(titleService.countTypesOf(Title.Type.POSITIVE, title));
            log.setQtyNegatives(titleService.countTypesOf(Title.Type.NEGATIVE, title));
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        return log;
    }
    
}
