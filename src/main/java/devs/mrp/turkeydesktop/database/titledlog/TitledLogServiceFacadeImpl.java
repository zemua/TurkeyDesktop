/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.titledlog;

import devs.mrp.turkeydesktop.common.GenericCache;
import devs.mrp.turkeydesktop.common.TimeConverter;
import devs.mrp.turkeydesktop.common.impl.GenericCacheImpl;
import devs.mrp.turkeydesktop.database.logs.TimeLogServiceFactory;
import devs.mrp.turkeydesktop.database.logs.TimeLog;
import devs.mrp.turkeydesktop.database.titles.TitleFactory;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import devs.mrp.turkeydesktop.database.logs.TimeLogService;
import devs.mrp.turkeydesktop.database.titles.Title;
import devs.mrp.turkeydesktop.database.titles.TitleService;
import java.util.ArrayList;
import java.util.List;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author miguel
 */
@Slf4j
public class TitledLogServiceFacadeImpl implements TitledLogServiceFacade {
    
    private TitleService titleService = TitleFactory.getService();
    private TimeLogService logService = TimeLogServiceFactory.getService();
    private TitledLogDaoFacade titleFacadeRepo = TitledLogRepoFacade.getInstance();
    
    private static GenericCache<FromTo,Observable<TitledLog>> logsWithTitle = new GenericCacheImpl<>();
    private static GenericCache<FromTo,Observable<TitledLog>> groupedByTitle = new GenericCacheImpl<>();
    
    @EqualsAndHashCode
    @AllArgsConstructor
    private class FromTo {
        Date from;
        Date to;
    }

    @Override
    public Observable<TitledLog> getLogsWithTitleConditions(Date from, Date to) {
        return logsWithTitle.get(new FromTo(from,to), () -> {
            List<TitledLog> titledLogList = logService.logsGroupedByTitle(from, to).flatMap(e -> {
                TitledLog tl = new TitledLog();
                tl.setTitle(e.getValue1());
                tl.setElapsed(e.getValue2());
                return titleService.getQtyPerCategory(e.getValue1()).flatMapObservable(map -> {
                    tl.setQtyNegatives(map.getOrDefault(Title.Type.NEGATIVE,0));
                    tl.setQtyNeutral(map.getOrDefault(Title.Type.NEUTRAL,0));
                    tl.setQtyPositives(map.getOrDefault(Title.Type.POSITIVE,0));
                    return titleService.findContainedByAndNegativeFirst(e.getValue1()).map(cond -> {
                        tl.addCondition(cond);
                        return tl;
                    });
                });
            })
                .filter(t -> !t.getTitle().isBlank())
                .toList()
                .blockingGet();
            // return observable from blocked object for the cache
            return Observable.fromIterable(titledLogList);
        });
    }

    @Override
    public Observable<TitledLog> getLogsDependablesWithTitleConditions(Date from, Date to) {
        long fromMillis = TimeConverter.millisToBeginningOfDay(from.getTime());
        long toMillis = TimeConverter.millisToEndOfDay(to.getTime());
        
        return groupedByTitle.get(new FromTo(from,to), () -> {
            return titleFacadeRepo.getTimeFrameOfDependablesGroupedByTitle(fromMillis, toMillis).flatMapObservable(set -> {
                List<TitledLog> titledLogs = new ArrayList<>();
                try {
                    while (set.next()) {
                        TitledLog titledLog = TitledLog.builder()
                                .title(set.getString(TimeLog.WINDOW_TITLE))
                                .elapsed(set.getLong(2))
                                .build();
                        titledLogs.add(titledLog);
                    }
                } catch (SQLException ex) {
                    log.error("Exception getting next row from ResultSet", ex);
                }
                log.debug("Dependable titles grouped: {}", titledLogs.toString());
                List<TitledLog> completedLogs = Observable.fromIterable(titledLogs)
                        .filter(t -> !t.getTitle().isBlank())
                        .flatMapSingle(this::completeTitledLog)
                        .toList()
                        .blockingGet();
                // return observable from blocked object for the cache
                return Observable.fromIterable(completedLogs);
            });
        });
    }
    
    private Single<TitledLog> titledLogFromResultSetEntry(ResultSet entry) {
        TitledLog titledLog = new TitledLog();
        try {
            titledLog.setTitle(entry.getString(TimeLog.WINDOW_TITLE));
            titledLog.setElapsed(entry.getLong(2));
            return completeTitledLog(titledLog);
        } catch (SQLException ex) {
            log.error("Error extracting TitledLog from Result Set", ex);
        }
        return Single.just(titledLog);
    }
    
    private Single<TitledLog> completeTitledLog(TitledLog titledLog) {
        return titleService.getQtyPerCategory(titledLog.getTitle()).flatMap(map -> {
                log.debug("Setting quantities per type");
                titledLog.setQtyNegatives(map.getOrDefault(Title.Type.NEGATIVE, 0));
                titledLog.setQtyNeutral(map.getOrDefault(Title.Type.NEUTRAL, 0));
                titledLog.setQtyPositives(map.getOrDefault(Title.Type.POSITIVE, 0));
                return titleService.findContainedByAndNegativeFirst(titledLog.getTitle())
                        .toList()
                        .map(contained -> {
                            log.debug("Setting conditions for {}", titledLog.getTitle());
                            titledLog.setConditions(contained);
                            return titledLog;
                        });
        });
    }
    
}
