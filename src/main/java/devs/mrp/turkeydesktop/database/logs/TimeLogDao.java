/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.logs;

import devs.mrp.turkeydesktop.database.GeneralDao;
import java.sql.ResultSet;

/**
 *
 * @author miguel
 */
public interface TimeLogDao extends GeneralDao<TimeLog, Long> {
    
    public ResultSet getTimeFrameGroupedByProcess(long from, long to);
    
    public ResultSet getMostRecent();
    
    public ResultSet getGroupedByTitle(long from, long to);
    
}
