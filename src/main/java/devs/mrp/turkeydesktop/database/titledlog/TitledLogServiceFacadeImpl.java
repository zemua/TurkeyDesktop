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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import devs.mrp.turkeydesktop.database.logs.TimeLogService;
import devs.mrp.turkeydesktop.database.titles.Title;
import devs.mrp.turkeydesktop.database.titles.TitleService;
import java.util.ArrayList;
import java.util.List;
import rx.Observable;
import rx.Single;

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
    public Observable<TitledLog> getLogsWithTitleConditions(Date from, Date to) {
        return logService.logsGroupedByTitle(from, to).flatMap(e -> {
            TitledLog tl = new TitledLog();
            tl.setTitle(e.getValue1());
            tl.setElapsed(e.getValue2());
            return titleService.getQtyPerCategory(e.getValue1()).flatMapObservable(map -> {
                tl.setQtyNegatives(map.get(Title.Type.NEGATIVE));
                tl.setQtyNeutral(map.get(Title.Type.NEUTRAL));
                tl.setQtyPositives(map.get(Title.Type.POSITIVE));
                return titleService.findContainedByAndNegativeFirst(e.getValue1()).map(cond -> {
                    tl.addCondition(cond);
                    return tl;
                });
            });
        });
    }

    @Override
    public Observable<TitledLog> getLogsDependablesWithTitleConditions(Date from, Date to) {
        long fromMillis = TimeConverter.millisToBeginningOfDay(from.getTime());
        long toMillis = TimeConverter.millisToEndOfDay(to.getTime());
        
        return titleFacade.getTimeFrameOfDependablesGroupedByProcess(fromMillis, toMillis).flatMapObservable(set -> {
            List<TitledLog> logs = new ArrayList<>();
            try {
                while (set.next()) {
                    TitledLog log = TitledLog.builder()
                            .title(set.getString(TimeLog.WINDOW_TITLE))
                            .elapsed(set.getLong(2))
                            .build();
                    logs.add(log);
                }
            } catch (SQLException ex) {
                Logger.getLogger(TitledLogServiceFacadeImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
            return Observable.from(logs).flatMapSingle(this::completeTitledLog);
        });
    }
    
    private Single<TitledLog> titledLogFromResultSetEntry(ResultSet entry) {
        TitledLog log = new TitledLog();
        try {
            log.setTitle(entry.getString(TimeLog.WINDOW_TITLE));
            log.setElapsed(entry.getLong(2));
            return completeTitledLog(log);
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        return Single.just(log);
    }
    
    private Single<TitledLog> completeTitledLog(TitledLog log) {
        return titleService.getQtyPerCategory(log.getTitle()).flatMap(map -> {
                log.setQtyNegatives(map.get(Title.Type.NEGATIVE));
                log.setQtyNeutral(map.get(Title.Type.NEUTRAL));
                log.setQtyPositives(map.get(Title.Type.POSITIVE));
                return titleService.findContainedByAndNegativeFirst(log.getTitle())
                        .toList()
                        .map(contained -> {
                            log.setConditions(contained);
                            return log;
                        }).toSingle();
        });
    }
    
}
