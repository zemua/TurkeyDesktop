/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.conditions;

import devs.mrp.turkeydesktop.database.GeneralDao;
import java.sql.ResultSet;
import io.reactivex.rxjava3.core.Single;

/**
 *
 * @author miguel
 */
public interface ConditionDao extends GeneralDao<Condition, Long> {
    
    public Single<ResultSet> findByGroupId(long groupId);
    public Single<Long> deleteByGroupId(long groupId);
    public Single<Long> deleteByTargetId(long targetId);
    
}
