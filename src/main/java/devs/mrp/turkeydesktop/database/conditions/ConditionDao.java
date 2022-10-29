/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.conditions;

import devs.mrp.turkeydesktop.database.GeneralDao;
import java.sql.ResultSet;
import rx.Observable;

/**
 *
 * @author miguel
 */
public interface ConditionDao extends GeneralDao<Condition, Long> {
    
    public Observable<ResultSet> findByGroupId(long groupId);
    public Observable<Long> deleteByGroupId(long groupId);
    public Observable<Long> deleteByTargetId(long targetId);
    
}
