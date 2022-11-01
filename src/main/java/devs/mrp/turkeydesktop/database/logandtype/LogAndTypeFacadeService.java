/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.logandtype;

import devs.mrp.turkeydesktop.common.Tripla;
import devs.mrp.turkeydesktop.database.logs.TimeLog;
import devs.mrp.turkeydesktop.database.type.Type;
import java.util.Date;
import rx.Observable;
import rx.Single;

/**
 *
 * @author miguel
 */
public interface LogAndTypeFacadeService {
    
    public Observable<Tripla<String, Long, Type.Types>> getTypedLogGroupedByProcess(Date from, Date to);
    
    public Single<TimeLog> addTimeLogAdjustingCounted(TimeLog element);
}
