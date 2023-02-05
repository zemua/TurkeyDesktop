package devs.mrp.turkeydesktop.database.logandtype;

import devs.mrp.turkeydesktop.common.Tripla;
import devs.mrp.turkeydesktop.database.logs.TimeLog;
import devs.mrp.turkeydesktop.database.type.Type;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import java.util.Date;

public interface LogAndTypeFacadeService {
    
    public Observable<Tripla<String, Long, Type.Types>> getTypedLogGroupedByProcess(Date from, Date to);
    
    public Single<TimeLog> addTimeLogAdjustingCounted(TimeLog element);
}
