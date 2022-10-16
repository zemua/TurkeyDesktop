/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.logs;

import devs.mrp.turkeydesktop.common.Dupla;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.LongConsumer;

/**
 *
 * @author miguel
 */
public interface TimeLogService {
    
    public void add(TimeLog element, LongConsumer consumer);
    public void update(TimeLog element, LongConsumer consumer);
    public void findLast24H(Consumer<List<TimeLog>> consumer);
    public void findById(long id, Consumer<TimeLog> consumer);
    public void deleteById(long id, LongConsumer consumer);
    public void findProcessTimeFromTo(Date from, Date to, Consumer<List<Dupla<String,Long>>> consumer);
    public void logsGroupedByTitle(Date from, Date to, Consumer<List<Dupla<String, Long>>> consumer);
    public void timeSpentOnGroupForFrame(long groupId, long from, long to, LongConsumer consumer);
    public void findMostRecent(Consumer<TimeLog> consumer);
    
}
