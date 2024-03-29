/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.logs;

import devs.mrp.turkeydesktop.database.GeneralDao;
import java.sql.ResultSet;
import io.reactivex.rxjava3.core.Single;

/**
 *
 * @author miguel
 */
public interface TimeLogDao extends GeneralDao<TimeLog, Long> {
    
    public Single<ResultSet> getTimeFrameGroupedByProcess(long from, long to);
    public Single<ResultSet> getTimeFrameOfGroup(long groupId, long from, long to);
    
    public Single<ResultSet> getMostRecent();
    
    public Single<ResultSet> getGroupedByTitle(long from, long to);
    
}
