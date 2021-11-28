/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.logs;

import java.util.List;

/**
 *
 * @author miguel
 */
public interface ITimeLogService {
    
    public long add(TimeLog element);
    public long update(TimeLog element);
    public List<TimeLog> findLast24H();
    public TimeLog findById(long id);
    public long deleteById(long id);
    
}
