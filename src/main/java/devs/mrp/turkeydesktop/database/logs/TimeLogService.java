/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.logs;

import devs.mrp.turkeydesktop.common.Dupla;
import java.util.Date;
import java.util.List;

/**
 *
 * @author miguel
 */
public interface TimeLogService {
    
    public long add(TimeLog element);
    public long update(TimeLog element);
    public List<TimeLog> findLast24H();
    public TimeLog findById(long id);
    public long deleteById(long id);
    public List<Dupla<String, Long>> findProcessTimeFromTo(Date from, Date to);
    public List<Dupla<String, Long>> logsGroupedByTitle(Date from, Date to);
    public long timeSpentOnGroupForFrame(long groupId, long from, long to);
    public TimeLog findMostRecent();
    
}
