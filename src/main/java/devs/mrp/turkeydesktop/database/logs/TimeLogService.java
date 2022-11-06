/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.logs;

import devs.mrp.turkeydesktop.common.Dupla;
import java.util.Date;
import rx.Observable;
import rx.Single;

/**
 *
 * @author miguel
 */
public interface TimeLogService {
    
    public Single<Long> add(TimeLog element);
    public Single<Long> update(TimeLog element);
    public Observable<TimeLog> findLast24H();
    public Single<TimeLog> findById(long id);
    public Single<Long> deleteById(long id);
    public Observable<Dupla<String,Long>> findProcessTimeFromTo(Date from, Date to);
    public Observable<Dupla<String,Long>> logsGroupedByTitle(Date from, Date to);
    public Single<Long> timeSpentOnGroupForFrame(long groupId, long from, long to);
    public Single<TimeLog> findMostRecent();
    
}
