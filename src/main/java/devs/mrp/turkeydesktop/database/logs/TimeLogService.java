package devs.mrp.turkeydesktop.database.logs;

import devs.mrp.turkeydesktop.common.Dupla;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import java.util.Date;

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
