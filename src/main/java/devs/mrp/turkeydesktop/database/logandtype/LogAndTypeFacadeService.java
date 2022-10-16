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
import java.util.List;
import java.util.function.Consumer;

/**
 *
 * @author miguel
 */
public interface LogAndTypeFacadeService {
    
    public void getTypedLogGroupedByProcess(Date from, Date to, Consumer<List<Tripla<String, Long, Type.Types>>> consumer);
    
    public void addTimeLogAdjustingCounted(TimeLog element, Consumer<TimeLog> consumer);
}
